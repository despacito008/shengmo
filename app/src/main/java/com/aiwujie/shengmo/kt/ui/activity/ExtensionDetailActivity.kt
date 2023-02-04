package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.ConversationCallStatusBean
import com.aiwujie.shengmo.bean.ExtensionDetailBean
import com.aiwujie.shengmo.eventbus.WxPaySucBean
import com.aiwujie.shengmo.kt.ui.view.ConversationCallBuyPop
import com.aiwujie.shengmo.kt.ui.view.ConversationCallUsePop
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.TextViewUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.CircleImageView
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.item_video.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: ExtensionDetailActivity
 * @Author: xmf
 * @CreateDate: 2022/6/6 20:43
 * @Description: 推广详情页面
 */
class ExtensionDetailActivity: BaseActivity() {

    private val ivBack:ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val tvTitle:TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val tvTime:TextView by lazy { findViewById<TextView>(R.id.tv_call_time) }
    private val tvBao:TextView by lazy { findViewById<TextView>(R.id.tv_call_bao) }
    private val tvNum:TextView by lazy { findViewById<TextView>(R.id.tv_call_see) }
    private val tvClick:TextView by lazy { findViewById<TextView>(R.id.tv_call_click) }
    private val tvCommit:TextView by lazy { findViewById<TextView>(R.id.tv_call_commit) }
    private val flUser1:FrameLayout by lazy { findViewById<FrameLayout>(R.id.fl_user_1) }
    private val ivUser1: CircleImageView by lazy { findViewById<CircleImageView>(R.id.iv_user_1) }
    private val flUser2:FrameLayout by lazy { findViewById<FrameLayout>(R.id.fl_user_2) }
    private val ivUser2:CircleImageView by lazy { findViewById<CircleImageView>(R.id.iv_user_2) }
    private val flUser3:FrameLayout by lazy { findViewById<FrameLayout>(R.id.fl_user_3) }
    private val ivUser3:CircleImageView by lazy { findViewById<CircleImageView>(R.id.iv_user_3) }
    private val llQuestion:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_call_question) }
    companion object {
        fun start(context: Context,id:String) {
            val intent = Intent(context,ExtensionDetailActivity::class.java)
            intent.putExtra(IntentKey.ID,id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).init()
        setContentView(R.layout.app_activity_extension_detail)
        ivBack.setColorFilter(Color.parseColor("#ffffffff"))
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
        tvTitle.text = "推广详情"
        initListener()
        getDetail()
    }

    private fun initListener() {
        ivBack.setOnClickListener {
            finish()
        }
        llQuestion.setOnClickListener {
            BannerWebActivity.start(this, SpKey.API_HOST.getSpValue("") + "/Home/Info/agreementinfo/id/11", "呼唤常见问题")
        }
        tvCommit.setOnClickListener {
            getCallTimes(true)
        }
    }

    var hid = ""
    private fun getDetail() {
        hid = intent.getStringExtra(IntentKey.ID) ?: ""
        HttpHelper.getInstance().getCallDetail(hid,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, ExtensionDetailBean::class.java)
                tempData?.data?.run {
                    val numStr = "呼唤期间已被 $call_uv 人看到"
                    TextViewUtil.setSpannedColorAndSizeText(tvNum, numStr, 7, 7 + call_uv.toString().length, Color.parseColor("#66ccff"),22 )
                    tvTime.text = add_time_string
                    TextViewUtil.setSpannedColorAndSizeText(tvBao, tvBao.text.toString(), 0, 2, Color.parseColor("#66ccff"),22)
                    tvClick.text = call_click
                    if (click_user_data.size >= 1) {
                        flUser3.visibility = View.VISIBLE
                        ImageLoader.loadImage(this@ExtensionDetailActivity,click_user_data[0].head_pic,ivUser3)
                    }
                    if (click_user_data.size >= 2) {
                        flUser2.visibility = View.VISIBLE
                        ImageLoader.loadImage(this@ExtensionDetailActivity,click_user_data[1].head_pic,ivUser2)
                    }
                    if (click_user_data.size >= 3) {
                        flUser1.visibility = View.VISIBLE
                        ImageLoader.loadImage(this@ExtensionDetailActivity,click_user_data[2].head_pic,ivUser1)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
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
}
