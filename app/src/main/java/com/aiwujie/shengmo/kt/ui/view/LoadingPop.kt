package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import razerdp.basepopup.BasePopupWindow

class LoadingPop : BasePopupWindow {
    private var mContent = ""
    private lateinit var tvTips: TextView
    constructor(context: Context) : super(context)
    constructor(context: Context, str: String) : this(context) {
        mContent = str
        initView()

    }



    fun initView() {
        setOutSideDismiss(false)
        popupWindow.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));

        tvTips = findViewById(R.id.tv_tip)
        tvTips.text = mContent
    }


    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_loading_tips)
    }
}