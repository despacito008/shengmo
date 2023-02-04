package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ConversationCallStatusBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.TextViewUtil
import com.aiwujie.shengmo.utils.ToastUtil
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: ConversationCallUsePop
 * @Author: xmf
 * @CreateDate: 2022/5/25 14:54
 * @Description:
 */
class ConversationCallUsePop(context: Context):BasePopupWindow(context) {
    private val tvBalance:TextView by lazy { findViewById<TextView>(R.id.tv_pop_balance) }
    private val etNum:EditText by lazy { findViewById<EditText>(R.id.et_pop_num) }
    private val ivClose:ImageView by lazy { findViewById<ImageView>(R.id.iv_pop_close) }
    private val tvCommit:TextView by lazy { findViewById<TextView>(R.id.tv_pop_commit) }
    var balance = 10
    init {
        this.setAdjustInputMethod(false)
        getCallTimes()
        ivClose.setOnClickListener { dismiss() }
        tvCommit.setOnClickListener { userCallCard() }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_conversation_call_use)
    }

    private fun getCallTimes() {
        HttpHelper.getInstance().getConversationCallStatus(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val conversationCallStatusBean = GsonUtil.GsonToBean(data, ConversationCallStatusBean::class.java)
                conversationCallStatusBean?.data?.run {
                    balance = call_times.toInt()
                    val balanceStr = "剩余 $balance 张呼唤卡"

                    TextViewUtil.setSpannedColorAndSizeText(tvBalance, balanceStr, 3, 3 + balance.toString().length, Color.parseColor("#ffffff"),20 )
                    //TextViewUtil.setSpannedColorText(tvBalance, balanceStr, 3, 3 + balance.toString().length, Color.parseColor("#ffffff"))
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun userCallCard() {
        val numStr = etNum.text.toString()
        if (numStr.isNullOrEmpty()) {
            "请选择呼唤次数".showToast()
            return
        }
        val num = numStr.toInt()
        if (num > balance) {
            "呼唤次数不足,最多可以使用${balance}次".showToast()
            return
        }
        HttpHelper.getInstance().openCall(num,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                dismiss()
                onCallPopListener?.doUseCall()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    interface OnCallPopListener {
        fun doUseCall()
    }

    var onCallPopListener:OnCallPopListener? = null

}
