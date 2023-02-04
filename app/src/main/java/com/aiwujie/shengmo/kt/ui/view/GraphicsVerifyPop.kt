package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.Gravity
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.showToast
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: GraphicsVerifyPop
 * @Author: xmf
 * @CreateDate: 2022/5/21 15:39
 * @Description:
 */
class GraphicsVerifyPop(context: Context): BasePopupWindow(context) {
    private val graphicsView:GraphicsVerifyView by lazy { findViewById<GraphicsVerifyView>(R.id.graphics_view) }
    init {
        popupGravity = Gravity.CENTER
        graphicsView.verifyCallBack = object :GraphicsVerifyView.VerifyCallBack {
            override fun onSuccess() {
                "验证成功".showToast()
                graphicsView.reset()
            }

            override fun onFail() {
                "验证失败，再试一次".showToast()
                graphicsView.reset()
            }
        }
    }
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_graphics_verify)
    }
}
