package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.CallConfigBean
import com.aiwujie.shengmo.bean.ConversationCallStatusBean
import com.aiwujie.shengmo.bean.ExtensionDetailBean
import com.aiwujie.shengmo.eventbus.WxPaySucBean
import com.aiwujie.shengmo.kt.ui.activity.normallist.ExtensionHistoryActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.CallCardDetailActivity
import com.aiwujie.shengmo.kt.ui.view.ConversationCallBuyPop
import com.aiwujie.shengmo.kt.ui.view.ConversationCallUsePop
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.TextViewUtil
import com.aiwujie.shengmo.utils.ToastUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: ConversationCallActivity
 * @Author: xmf
 * @CreateDate: 2022/5/25 16:34
 * @Description:
 */
class ConversationCallActivity: BaseActivity() {
    private val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val ivBack: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val ivDetail: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_more) }
    private val tvDetail: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_more) }
    private val llQuestion: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_call_question) }
    private val llLimit: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_call_limit) }
    private val llStart: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_call_start) }
    private val tvNum:TextView by lazy { findViewById<TextView>(R.id.tv_call_num) }
    private val tvTips:TextView by lazy { findViewById<TextView>(R.id.tv_call_tips) }
    private val tvBuy:TextView by lazy { findViewById<TextView>(R.id.tv_call_buy) }
    private val llHistory:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_conversation_call_list) }
    private val tvCallNum:TextView by lazy { findViewById<TextView>(R.id.tv_conversation_call_num) }
    private val tvCallTime:TextView by lazy { findViewById<TextView>(R.id.tv_conversation_call_time) }
    private val tvCallBao:TextView by lazy { findViewById<TextView>(R.id.tv_conversation_call_bao) }

    companion object {
        fun start(context:Context) {
            val intent = Intent(context,ConversationCallActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_conversation_call)
        StatusBarUtil.showLightStatusBar(this)
        ivDetail.visibility = View.GONE
        tvDetail.visibility = View.VISIBLE
        tvDetail.text = "明细"
        tvTitle.text = "呼唤"
        initListener()
        getCallTimes()
        getCallConfig()
        getCallHistory()
    }

    private fun initListener() {
        ivBack.setOnClickListener { finish()}

        tvDetail.setOnClickListener {
            CallCardDetailActivity.start(this)
        }

        llQuestion.setOnClickListener {
            BannerWebActivity.start(this, SpKey.API_HOST.getSpValue("") + "/Home/Info/agreementinfo/id/11", "呼唤常见问题")
        }

        llLimit.setOnClickListener {
            BannerWebActivity.start(this, SpKey.API_HOST.getSpValue("") + "Home/Info/agreementinfo/id/10", "推广要求")
        }

        tvBuy.setOnClickListener {
            showBuyCallPop()
        }

        llStart.setOnClickListener {
            getCallTimes(true)
        }

        llHistory.setOnClickListener {
            ExtensionHistoryActivity.startActivity(this)
        }
    }

    private fun getCallConfig() {
        HttpHelper.getInstance().getCallConfig(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val config = GsonUtil.GsonToBean(data, CallConfigBean::class.java)
                config?.data?.run {
                    tvTips.text = call_text2
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun getCallTimes(useCall:Boolean = false) {
        HttpHelper.getInstance().getConversationCallStatus(object : HttpCodeMsgListener {
            override fun onSuccess(data: String, msg: String) {
                val conversationCallStatusBean = GsonUtil.GsonToBean(data, ConversationCallStatusBean::class.java)
                conversationCallStatusBean?.data?.run {
                    if (useCall) {
                        if (status != "0") {
                            ToastUtil.show(conversationCallStatusBean.data.toast_tip)
                        } else {
                            if (call_times != "0") {
                                showUseCallPop()
                            } else {
                                showBuyCallPop()
                            }
                        }
                    } else {
                        tvNum.text = call_times
                    }
                }
            }

            override fun onFail(code: Int, msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    private fun showBuyCallPop() {
        val callBuyPop = ConversationCallBuyPop(this)
        callBuyPop.showPopupWindow()
        callBuyPop.onBuyCallListener = object : ConversationCallBuyPop.OnBuyCallListener {
            override fun doBuyCallSuc() {
                getCallTimes()
                showUseCallPop()
            }
        }
    }

    private fun showUseCallPop() {
        val useCallPop = ConversationCallUsePop(this)
        useCallPop.showPopupWindow()
        useCallPop.onCallPopListener = object : ConversationCallUsePop.OnCallPopListener {
            override fun doUseCall() {
                getCallTimes()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: WxPaySucBean) {
        getCallTimes()
    }

    private fun getCallHistory() {
        HttpHelper.getInstance().getCallDetail("",object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,ExtensionDetailBean::class.java)
                tempData?.data?.run {
                    llHistory.visibility = View.VISIBLE
                    val numStr = "呼唤期间已被 $call_uv 人看到"
                    TextViewUtil.setSpannedColorAndSizeText(tvCallNum, numStr, 7, 7 + call_uv.toString().length, Color.parseColor("#66ccff"),22 )
                    tvCallTime.text = add_time_string
                    TextViewUtil.setSpannedColorAndSizeText(tvCallBao, tvCallBao.text.toString(), 0, 2, Color.parseColor("#66ccff"),22)
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

}
