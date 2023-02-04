package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CallAliPayOrderBean
import com.aiwujie.shengmo.bean.CallConfigBean
import com.aiwujie.shengmo.bean.CallWxOrderBean
import com.aiwujie.shengmo.bean.ConversationCallItemBean
import com.aiwujie.shengmo.kt.ui.adapter.ConversationCallItemAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.AliPayMentTaskManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.WxPayMentTaskManager
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: ConversationCallPop
 * @Author: xmf
 * @CreateDate: 2022/5/25 10:13
 * @Description: 购买呼唤弹窗
 */
class ConversationCallBuyPop(context: Context) : BasePopupWindow(context) {
    private val llChoosePayType: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_choose_pay_type) }
    private val tvPayType: TextView by lazy { findViewById<TextView>(R.id.tv_pay_by_type) }
    private val rvCall: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_conversation_call_item) }
    private val ivClose: ImageView by lazy { findViewById<ImageView>(R.id.iv_pop_close) }
    private val tvBuyCall: TextView by lazy { findViewById<TextView>(R.id.tv_buy_call) }
    private val tvTips: TextView by lazy { findViewById<TextView>(R.id.tv_pop_tips) }

    init {
        llChoosePayType.setOnClickListener {
            showChoosePayTypePop()
        }
        ivClose.setOnClickListener {
            dismiss()
        }
        getCallItem()
        getCallConfig()
        tvBuyCall.setOnClickListener {
            when (payType) {
                1 -> {
                    buyCallByBean()
                }
                2 -> {
                    buyCallByWeChat()
                }
                else -> {
                    buyCallByAliPay()
                }
            }
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_conversation_call_buy)
    }

    var payType = 0
    var callId = ""
    private fun showChoosePayTypePop() {
        val choosePayTypePop = ChoosePayTypePop(context)
        choosePayTypePop.showPopupWindow()
        choosePayTypePop.payTypeChooseListener = object : ChoosePayTypePop.OnPayTypeChooseListener {
            override fun doChoosePayType(type: Int) {
                payType = type
                tvPayType.text = when (type) {
                    1 -> {
                        "魔豆支付"
                    }
                    2 -> {
                        "微信支付"
                    }
                    else -> {
                        "支付宝支付"
                    }
                }
            }
        }
    }

    lateinit var itemList: ArrayList<ConversationCallItemBean.DataBean>
    private fun getCallItem() {
        itemList = ArrayList()
        HttpHelper.getInstance().getCallItem(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, ConversationCallItemBean::class.java)
                tempData?.data?.run {
                    itemList.addAll(this)
                    val itemAdapter = ConversationCallItemAdapter(context, itemList)
                    with(rvCall) {
                        adapter = itemAdapter
                        layoutManager = GridLayoutManager(context, 3)
                    }
                    if (itemList.size > 1) {
                        callId = itemList[1].bag_id
                    }
                    itemAdapter.onItemClickListener = OnSimpleItemListener { index ->
                        callId = itemList[index].bag_id
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun getCallConfig() {
        HttpHelper.getInstance().getCallConfig(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val config = GsonUtil.GsonToBean(data, CallConfigBean::class.java)
                config?.data?.run {
                    tvTips.text = call_text1
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun buyCallByBean() {
        HttpHelper.getInstance().buyCallByBeans(callId, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                onBuyCallListener?.doBuyCallSuc()
                dismiss()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun buyCallByWeChat() {
        HttpHelper.getInstance().getCallWeChatOrder(callId, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, CallWxOrderBean::class.java)
                tempData?.data?.run {
                    WxPayMentTaskManager(context).payByWeChat(this)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun buyCallByAliPay() {
        HttpHelper.getInstance().getCallAliPayOrder(callId, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,CallAliPayOrderBean::class.java)
                tempData?.data?.run {
                    AliPayMentTaskManager(context as Activity).payByAli(ali_data)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

//    private fun showUseCallPop() {
//        val useCallPop = ConversationCallUsePop(context)
//        useCallPop.showPopupWindow()
//    }

    interface OnBuyCallListener {
        fun doBuyCallSuc()
    }

    var onBuyCallListener: OnBuyCallListener? = null
}
