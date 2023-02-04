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
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import razerdp.basepopup.BasePopupWindow

class OpenHighTipPop : BasePopupWindow {
    private var mContent = ""
    private val tvTips: TextView by lazy { findViewById<TextView>(R.id.tv_tip) }
    private val tvCancle: TextView by lazy { findViewById<TextView>(R.id.tv_cancle) }
    private val tvConfrim: TextView by lazy { findViewById<TextView>(R.id.tv_confirm) }
    private var mTopId: String = ""

    constructor(context: Context) : super(context)
    constructor(context: Context, str: String) : this(context) {
        mContent = str
        initView()

    }

   private fun initView() {
        setOutSideDismiss(false)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        tvTips.text = mContent

        tvCancle.setOnClickListener {
            dismiss()
        }

        tvConfrim.setOnClickListener {
            doSubmit()
            dismiss()
        }
    }

    private fun doSubmit() {

        HttpHelper.getInstance().adminOpenHigh("", "", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                onSimpleItemListener?.onItemListener(0)
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
        return createPopupById(R.layout.app_open_high_tip_pop)
    }

    public var onSimpleItemListener  :OnSimpleItemListener?= null
}