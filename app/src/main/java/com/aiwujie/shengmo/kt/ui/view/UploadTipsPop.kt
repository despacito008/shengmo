package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import razerdp.basepopup.BasePopupWindow

class UploadTipsPop: BasePopupWindow {
    private var mTaskNum = 100
    constructor(context: Context) : super(context)
    constructor(context: Context,taskNum:Int) : this(context) {
        initView()
        mTaskNum = taskNum
    }
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTips:TextView
    fun initView() {
        setOutSideDismiss(false)
        progressBar = findViewById(R.id.pb_upload_tips)
        progressBar.max = mTaskNum
        tvTips = findViewById(R.id.tv_upload_tips)
    }

    fun updateProcess(progress:Int) {
        progressBar.progress = progress
        tvTips.text = "完成 $progress"
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_upload_tips)
    }
}