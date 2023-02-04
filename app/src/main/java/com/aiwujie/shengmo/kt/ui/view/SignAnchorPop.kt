package com.aiwujie.shengmo.kt.ui.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.SignAnchorInfoBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: SignAnchorPop
 * @Author: xmf
 * @CreateDate: 2022/5/12 20:47
 * @Description:
 */
class SignAnchorPop: BasePopupWindow {
    constructor(context: Context) : super(context)
    constructor(context: Context, uid: String) : this(context) {
        this.uid = uid
        popupGravity = Gravity.BOTTOM
        initListener()
        getSignedInfo()
    }
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_sign_anchor)
    }

    var uid = ""
    private val tvBean:TextView by lazy {  findViewById<TextView>(R.id.tv_pop_sign_anchor_bean) }
    private val tvRate:TextView by lazy {  findViewById<TextView>(R.id.tv_pop_sign_anchor_rate) }
    private val tvMoney:TextView by lazy {  findViewById<TextView>(R.id.tv_pop_sign_anchor_money) }
    private val tvInfo:TextView by lazy {  findViewById<TextView>(R.id.tv_pop_info) }
    private val tvAccount:TextView by lazy {  findViewById<TextView>(R.id.tv_pop_sign_anchor_account) }
    private val tvSign:TextView by lazy {  findViewById<TextView>(R.id.tv_pop_sign_anchor_commit) }
    private fun initListener() {
        tvBean.setOnClickListener {
            initCopyText(tvBean)
        }
        tvRate.setOnClickListener {
            initCopyText(tvRate)
        }
        tvMoney.setOnClickListener {
            initCopyText(tvMoney)
        }
        tvAccount.setOnClickListener {
            initCopyText(tvAccount)
        }
        tvSign.setOnClickListener {
            onSignAnchorListener?.doSignAnchor()
        }
    }

    private fun getSignedInfo() {
        HttpHelper.getInstance().getSignedAnchorInfo(uid,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,SignAnchorInfoBean::class.java)
                tempData?.data?.run {
                    tvBean.text = wallet_receive
                    tvRate.text = invite_ratio.toString()
                    tvMoney.text = beans_val
                    tvAccount.text = id_name
                    tvInfo.text = key_text
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun initCopyText(textView: TextView) {
        //获取剪贴板管理器：
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", textView.text)
        // 将ClipData内容放到系统剪贴板里。
        cm.primaryClip = mClipData
        "已复制 ${textView.text} 到粘贴板".showToast()
    }

    interface OnSignAnchorListener {
        fun doSignAnchor()
    }

    var onSignAnchorListener:OnSignAnchorListener? = null
}
