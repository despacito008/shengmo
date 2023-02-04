package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.NormalConstant
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.StatusBarUtil

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: ExchangeBeansActivity
 * @Author: xmf
 * @CreateDate: 2022/4/7 20:50
 * @Description: 银魔豆兑换金魔豆
 */
class ExchangeBeansActivity : BaseActivity() {
    // 标题栏
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView

    private lateinit var etExchangeBeans: EditText
    private lateinit var tvExchangeTip: TextView
    private lateinit var tvExchangeBalance: TextView
    private lateinit var llExchangeAddWealth: LinearLayout
    private lateinit var ivExchangeAddWealth: ImageView
    private lateinit var tvExchangeCommit: TextView

    private var mBalance = 0
    private var mExchangeBean = 0
    private var mAddWealth = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_exchange_beans)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        tvTitle.text = "银魔豆兑换金魔豆"
        ivTitleBack.setOnClickListener { finish() }
        etExchangeBeans = findViewById(R.id.et_exchange_num)
        tvExchangeTip = findViewById(R.id.tv_exchange_rate)
        tvExchangeBalance = findViewById(R.id.tv_exchange_balance)
        llExchangeAddWealth = findViewById(R.id.ll_exchange_add_wealth)
        ivExchangeAddWealth = findViewById(R.id.iv_exchange_add_wealth)
        tvExchangeCommit = findViewById(R.id.tv_exchange_commit)
        intent.getStringExtra(IntentKey.BALANCE)?.run {
            mBalance = this.toInt()
            tvExchangeBalance.text = "可用银魔豆 $mBalance  最多兑换${mBalance / 2}金魔豆 "
        }
        etExchangeBeans.addTextChangedListenerDsl {
            afterTextChanged { afterText ->
                if (afterText?.isNotEmpty() == true) {
                    if (afterText.toString().toInt() * 2 <= mBalance) {
                        mExchangeBean = afterText.toString().toInt()
                    } else {
                        mExchangeBean = 0
                        "可用银魔豆不足".showToast()
                        etExchangeBeans.setText(mExchangeBean.toString())
                    }
                } else {
                    mExchangeBean = 0
                }
                tvExchangeTip.text = "兑换比例：${mExchangeBean * 2}银魔豆 = ${mExchangeBean}金魔豆"
            }
        }
        tvExchangeCommit.setOnClickListener {
            exchangeBean()
        }
        llExchangeAddWealth.setOnClickListener {
            mAddWealth = !mAddWealth
            when (mAddWealth) {
                true -> {
                    ivExchangeAddWealth.setImageResource(R.mipmap.yuandiantaozi)
                }
                false -> {
                    ivExchangeAddWealth.setImageResource(R.mipmap.yuandiantaohui)
                }
            }
        }
    }

    private fun exchangeBean() {
        if (mExchangeBean <= 0) {
            "请选择兑换魔豆".showToast()
        }
        HttpHelper.getInstance().exchangeBeans(mExchangeBean,mAddWealth,object: HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                val intent = Intent()
                intent.putExtra(IntentKey.BALANCE,(mBalance - (mExchangeBean * 2)).toString())
                setResult(NormalConstant.RESULT_CODE_OK,intent)
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
}
