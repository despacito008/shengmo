package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.eventbus.AddHighUserEven
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import razerdp.basepopup.BasePopupWindow

class HighAddUserPop : BasePopupWindow {
    private var mTipText = ""
    private var mHintText = ""
    private var mType = ""    //  1 匹配用户  2  取消审核  3 隐藏用户
    private val tvTips: TextView by lazy { findViewById<TextView>(R.id.tv_tip) }
    private val tvCancle: TextView by lazy { findViewById<TextView>(R.id.tv_cancle) }
    private val tvConfrim: TextView by lazy { findViewById<TextView>(R.id.tv_confirm) }
    private val etContent: EditText by lazy { findViewById<EditText>(R.id.tv_desc) }
    private var mTopId: String = ""

    constructor(context: Context) : super(context)
    constructor(context: Context, str: String, topId: String) : this(context) {
        mTipText = str
        mTopId = topId
        initView()

    }
    constructor(context: Context, str: String,hintText:String , topId: String,type:String) : this(context) {
        mTipText = str
        mHintText = hintText
        mType = type
        mTopId = topId
        initView()

    }

   private fun initView() {
        setOutSideDismiss(false)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        tvTips.text = mTipText
        etContent.hint = mHintText

        tvCancle.setOnClickListener {
            dismiss()
        }

        tvConfrim.setOnClickListener {
            hideKeyBoard(etContent)
            if (mType  == "1" ){
                doSubmit()
            }else if (mType  == "2" ){
               adminHideUser(mTopId)
            }else if (mType  == "3" ){
                adminCancleAudit(mTopId)
            }

            dismiss()
        }
    }

    private fun doSubmit() {
        if (TextUtil.isEmpty(etContent.text.toString().trim())) {
            "请填写匹配用户ID".showToast()
            return
        }
        val targetUid = etContent.text.toString().trim()
        HttpHelper.getInstance().adminMatchPair(mTopId, targetUid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                EventBus.getDefault().post(AddHighUserEven())

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }


    private fun adminHideUser(targetUid: String?) {
        if (TextUtil.isEmpty(etContent.text.toString().trim())) {
            "请填写理由".showToast()
            return
        }
        HttpHelper.getInstance().adminHideUser(targetUid, etContent.text.toString().trim(),object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    private fun adminCancleAudit(targetUid: String?) {
        if (TextUtil.isEmpty(etContent.text.toString().trim())) {
            "请填写理由".showToast()
            return
        }
        HttpHelper.getInstance().adminCancleAudit(targetUid,etContent.text.toString().trim(), object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    private fun hideKeyBoard(view: View) {

        val imm: InputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_add_high_user_pop)
    }
}