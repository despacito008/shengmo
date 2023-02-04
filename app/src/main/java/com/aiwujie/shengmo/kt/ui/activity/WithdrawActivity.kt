package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.SetBankcardActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.BankListData
import com.aiwujie.shengmo.bean.BannerNewData
import com.aiwujie.shengmo.bean.WithdrawInfoBean
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import java.math.BigDecimal

/**
 * 提现页面
 */
class WithdrawActivity : BaseActivity() {
    private lateinit var tvBank: TextView
    private lateinit var tvAccount: TextView
    private lateinit var tvRate: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvTip: TextView
    private lateinit var tvContract: TextView
    private lateinit var tvCommit: TextView
    private lateinit var etWithdrawNum: EditText

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var adViewWithdraw:AdvertisementView

    private lateinit var llBankCard: LinearLayout

    private var rate: Double = 0.0
    private var min = 9999
    private var balance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_withdraw)
        StatusBarUtil.showLightStatusBar(this)
        tvBank = findViewById(R.id.tv_withdraw_bank)
        llBankCard = findViewById(R.id.ll_withdraw_bank)
        tvAccount = findViewById(R.id.tv_withdraw_account)
        tvRate = findViewById(R.id.tv_withdraw_rate)
        tvBalance = findViewById(R.id.tv_withdraw_balance)
        tvTip = findViewById(R.id.tv_withdraw_tips)
        tvContract = findViewById(R.id.tv_withdraw_contract_tips)
        tvCommit = findViewById(R.id.tv_withdraw_commit)
        etWithdrawNum = findViewById(R.id.et_withdraw_num)
        //tvRate.text = "提现比例：10 魔豆 = ${a.multiply(b).toDouble()} 元"

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        adViewWithdraw = findViewById(R.id.ad_view_withdraw)
        tvTitle.text = "提现"
        ivTitleRight.visibility = View.GONE
        tvTitleRight.visibility = View.VISIBLE
        tvAccount.visibility = View.GONE
        tvBank.text = "请选择银行卡"
        setListener()
        getData()
        getBanner()
        getDefaultBindCard()
    }

    fun setListener() {

        ivTitleBack.setOnClickListener {
            finish()
        }

        etWithdrawNum.addTextChangedListenerDsl {
            afterTextChanged { s ->
                if (s.toString().isNotEmpty()) {
                    val num = s.toString().toInt()
                    showRateString(num)
                } else if (rate != 0.0) {
                    showRateString(min)
                }
            }
        }
        llBankCard.setOnClickListener {
            startActivityForResult(Intent(this@WithdrawActivity, SetBankcardActivity::class.java), 1000)
        }

        tvCommit.setOnClickListener {
            if (TextUtils.isEmpty(bid)) {
                ToastUtil.show(this@WithdrawActivity, "请选择银行卡")
                return@setOnClickListener
            }
            if (etWithdrawNum.editableText.toString().isEmpty()) {
                ToastUtil.show(this@WithdrawActivity, "请输入提现魔豆")
                return@setOnClickListener
            }
            if (etWithdrawNum.editableText.toString().toInt() < min) {
                ToastUtil.show(this@WithdrawActivity, "可用魔豆需要大于等于${min}个才可以提现。")
                return@setOnClickListener
            }
            if (etWithdrawNum.editableText.toString().toInt() > balance) {
                ToastUtil.show(this@WithdrawActivity, "可提现魔豆不足")
                return@setOnClickListener
            }
            withdrawMoney()
        }

        tvTitleRight.setOnClickListener {
            val intent = Intent(this@WithdrawActivity, WithdrawDetailActivity::class.java)
            startActivity(intent)
        }

    }

    fun getData() {
        HttpHelper.getInstance().getWithdrawInfo(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val withdrawBean = GsonUtil.GsonToBean(data, WithdrawInfoBean::class.java)
                withdrawBean?.data?.let {
                    balance = it.profit
                    val balanceInfo = "可提现魔豆 ：${balance}"
                    tvBalance.text = balanceInfo
                    rate = it.proportion
                    min = it.min
                    tvTip.text = it.tips
                    tvContract.visibility = if (TextUtils.isEmpty(it.cash_out)) View.GONE else View.VISIBLE
                    tvContract.text = it.cash_out
                    // tvRate.text = "提现比例：${min}魔豆 = ${showRateString(min)}元"
                    if (tvContract.visibility == View.VISIBLE) {
                        etWithdrawNum.setText(it.profit.toString())
                        etWithdrawNum.isEnabled = false
                        showRateString(it.profit)
                    } else {
                        showRateString(min)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@WithdrawActivity, msg)
            }

        })
    }


    fun getBanner() {
        HttpHelper.getInstance().getBanner(14, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val bannerData = GsonUtil.GsonToBean(data, BannerNewData::class.java)
                 adViewWithdraw.initData(bannerData)
            }

            override fun onFail(code: Int, msg: String?) {

            }

        })
    }

    fun showRateString(num: Int) {
        val a = BigDecimal(num.toString())
        val b = BigDecimal(getRate(rate).toString())
        val rateInfo = "提现比例：$num 魔豆 = ${a.multiply(b)} 元"
        //tvRate.text = String.format(resources.getString(R.string.withdraw_rate_info),num,a.multiply(b))
        tvRate.text = rateInfo
    }

    private fun getRate(rate: Double): Double {
        //val a = BigDecimal(0.1.toString())
        val b = BigDecimal(rate.toString())
        val c = BigDecimal(10.toString())
        return b.divide(c).toDouble()
    }



    private var bid = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == 200) {
            data?.let {
                bid = it.getStringExtra("bid")
                tvBank.text = it.getStringExtra("bank")
                tvAccount.text = it.getStringExtra("account")
                tvAccount.visibility = View.VISIBLE
            }
        }
    }

    private fun withdrawMoney() {
        HttpHelper.getInstance().withdrawMoney(bid, etWithdrawNum.editableText.toString().toInt(), object : HttpCodeMsgListener {
            override fun onSuccess(data: String?,msg:String?) {
                msg?.showToast()
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }

        })
    }

    private fun getDefaultBindCard() {
        HttpHelper.getInstance().getMyBankCard(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val bankData = GsonUtil.GsonToBean(data, BankListData::class.java)
                bankData?.data?.let {
                    if (it.size == 0) {
                        return
                    }
                    for (dataBean in it) {
                        if (dataBean.bank_status == "1") {
                            tvBank.text = dataBean.bankname
                            tvAccount.text = dataBean.bankcard
                            tvAccount.visibility = View.VISIBLE
                            bid = dataBean.bid
                            return
                        }
                    }

                    for (dataBean in it) {
                        if (dataBean.pay_status == "1") {
                            tvBank.text = dataBean.bankname
                            tvAccount.text = dataBean.bankcard
                            tvAccount.visibility = View.VISIBLE
                            bid = dataBean.bid
                            return
                        }
                    }
                    tvBank.text = it[0].bankname
                    tvAccount.text = it[0].bankcard
                    tvAccount.visibility = View.VISIBLE
                    bid = it[0].bid
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }
}