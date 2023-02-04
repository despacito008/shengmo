package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.AddAliPayAccountActivity
import com.aiwujie.shengmo.activity.SfzrzActivity
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.bean.LiveAuthInfo
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil

/**
 * 提现认证页面
 */
class WithdrawAuthActivity : BaseActivity() {
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var llBindPhone: ConstraintLayout
    private lateinit var llBindBankCard: ConstraintLayout
    private lateinit var llAuthIDCard: ConstraintLayout
    private lateinit var tvBindPhone: TextView
    private lateinit var tvBindBankCard: TextView
    private lateinit var tvAuthIDCard: TextView

    private lateinit var btStartWithdraw: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_withdraw_auth)
        StatusBarUtil.showLightStatusBar(this)
        initView()
    }

    fun initView() {
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        llBindPhone = findViewById(R.id.ll_bind_phone)
        llBindBankCard = findViewById(R.id.ll_bind_bank_card)
        llAuthIDCard = findViewById(R.id.ll_auth_id_card)
        tvBindPhone = findViewById(R.id.tv_bind_phone_start)
        tvBindBankCard = findViewById(R.id.tv_bind_bank_card)
        tvAuthIDCard = findViewById(R.id.tv_auth_id_card_start)

        btStartWithdraw = findViewById(R.id.btn_start_withdraw)

        tvTitle.text = "申请提现"
        ivTitleRight.visibility = View.INVISIBLE
        ivTitleBack.setOnClickListener { finish() }

        getAuthState()
        initListener()
    }

    fun initListener() {
        tvBindPhone.setOnClickListener {
            val intent = Intent(this, BindingMobileActivity::class.java)
            intent.putExtra("neworchange", "new")
            startActivity(intent)
        }
        tvAuthIDCard.setOnClickListener {
            intent = Intent(this, SfzrzActivity::class.java)
            startActivity(intent)
        }
        tvBindBankCard.setOnClickListener {
//            val intent = Intent(this, SetBankcardActivity::class.java)
//            intent.putExtra("operation", "add")
//            startActivity(intent)
            if (!isBindBankCard) {
                addAliPayAccount()
            }
        }

        btStartWithdraw.setOnClickListener {
            val intent = Intent(this,WithdrawActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addAliPayAccount() {
        val intent = Intent(this@WithdrawAuthActivity, AddAliPayAccountActivity::class.java)
        intent.putExtra("operation", "add")
        startActivity(intent)
    }

    var isBindPhone = false
    var isBindBankCard = false
    var isIDCardAuth = false
    private fun getAuthState() {
        HttpHelper.getInstance().getWithdrawAuth(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val liveAuthInfo: LiveAuthInfo? = GsonUtil.GsonToBean(data, LiveAuthInfo::class.java)
                val authData: LiveAuthInfo.DataBean? = liveAuthInfo?.data
                authData?.let {
                    when (it.bindingMobileStatus) {
                        "0" -> {
                            tvBindPhone.text = "立即绑定"
                            tvBindPhone.setTextColor(ContextCompat.getColor(this@WithdrawAuthActivity,R.color.bind_common_bg_purple))
                            llBindPhone.isClickable = true
                            isBindPhone = false
                        }
                        "1" -> {
                            tvBindPhone.text = "已绑定"
                            tvBindPhone.setTextColor(Color.GRAY)
                            llBindPhone.isClickable = false
                            isBindPhone = true
                        }
                    }
                    when (it.bankCardStatus) {
                        "0" -> {
                            tvBindBankCard.text = "立即绑定"
                            tvBindBankCard.setTextColor((ContextCompat.getColor(this@WithdrawAuthActivity,R.color.bind_common_bg_purple)))
                            llBindBankCard.isClickable = true
                            isBindBankCard = false
                        }
                        "1" -> {
                            tvBindBankCard.text = "已绑定"
                            tvBindBankCard.setTextColor(Color.GRAY)
                            llBindBankCard.isClickable = false
                            isBindBankCard = true
                        }
                    }
                    // 0 未申请  1 通过 2 审核中 3 未通过
                    when (it.realidcardStatus) {
                        "0" -> {
                            tvAuthIDCard.text = "立即认证"
                            tvAuthIDCard.setTextColor((ContextCompat.getColor(this@WithdrawAuthActivity,R.color.bind_common_bg_purple)))
                            llAuthIDCard.isClickable = true
                            isIDCardAuth = false
                        }
                        "1" -> {
                            tvAuthIDCard.text = "已认证"
                            tvAuthIDCard.setTextColor(Color.GRAY)
                            llAuthIDCard.isClickable = false
                            isIDCardAuth = true
                        }
                        "2" -> {
                            tvAuthIDCard.text = "审核中"
                            tvAuthIDCard.setTextColor(Color.GRAY)
                            llAuthIDCard.isClickable = false
                            isIDCardAuth = false
                        }
                        "3" -> {
                            tvAuthIDCard.text = "审核失败"
                            tvAuthIDCard.setTextColor(Color.GRAY)
                            llAuthIDCard.isClickable = false
                            isIDCardAuth = false
                        }
                    }

                    if (isBindBankCard && isBindPhone && isIDCardAuth) {
                        btStartWithdraw.visibility = View.VISIBLE
                    } else {
                        btStartWithdraw.visibility = View.GONE
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
               ToastUtil.show(this@WithdrawAuthActivity,msg)
            }

        })

    }

    override fun onRestart() {
        super.onRestart()
        getAuthState()
    }
}