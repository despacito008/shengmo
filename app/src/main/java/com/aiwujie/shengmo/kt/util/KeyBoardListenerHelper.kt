package com.aiwujie.shengmo.kt.util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import java.lang.ref.WeakReference

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.util
 * @ClassName: KeyBoardListenerHelper
 * @Author: xmf
 * @CreateDate: 2022/5/21 11:28
 * @Description: 软键盘监听
 */
class KeyBoardListenerHelper {
    private var weakActivity:WeakReference<Activity>? = null
    private val onGlobalLayoutListener:ViewTreeObserver.OnGlobalLayoutListener = object :ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (!isActivityValid() || onKeyBoardChangeListener == null) {
                return
            }
            try {
                val rect = Rect()
                weakActivity?.get()?.window?.decorView?.getWindowVisibleDisplayFrame(rect)
                val screenHeight = weakActivity?.get()?.window?.decorView?.height ?: 0
                val keyBoardHeight = screenHeight - rect.bottom;
                onKeyBoardChangeListener?.onKeyBoardChange(keyBoardHeight > 0, keyBoardHeight);
            } catch (e:Exception) {

            }
        }

    }

    constructor(activity: Activity) {
        weakActivity = WeakReference(activity)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val content = weakActivity?.get()?.findViewById<View>(android.R.id.content)
        content?.viewTreeObserver?.addOnGlobalLayoutListener { onGlobalLayoutListener }
    }

    fun destroy() {
        if (!isActivityValid()) {
            return
        }
        val content = weakActivity?.get()?.findViewById<View>(android.R.id.content)
        content?.viewTreeObserver?.removeOnGlobalLayoutListener { onGlobalLayoutListener }
    }


    interface OnKeyBoardChangeListener {
        fun onKeyBoardChange(isShow: Boolean,keyBoardHeight:Int)
    }
    var onKeyBoardChangeListener:OnKeyBoardChangeListener? = null

    fun isActivityValid():Boolean {
        return weakActivity != null && weakActivity?.get() != null
    }
}
