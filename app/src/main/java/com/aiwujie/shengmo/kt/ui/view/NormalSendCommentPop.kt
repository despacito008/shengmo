package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.aiwujie.shengmo.R
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: NormalSendCommentPop
 * @Author: xmf
 * @CreateDate: 2022/6/10 14:32
 * @Description:
 */
class NormalSendCommentPop(context: Context): BasePopupWindow(context) {

    private val etComment:EditText by lazy { findViewById<EditText>(R.id.et_discuss) }
    private val btCommit:Button by lazy { findViewById<Button>(R.id.btn_confirm) }

    init {
        popupGravity = Gravity.BOTTOM
        //setAdjustInputMethod(false)
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setAutoShowInputMethod(etComment,true)
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_normal_send_comment)
    }


}
