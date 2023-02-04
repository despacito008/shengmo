package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout

abstract class NormalHeaderListActivity: NormalListActivity() {

    override fun initViewComplete() {
        super.initViewComplete()
        initEmptyHeader()
    }
    lateinit var viewEmptyHeader: View
    private fun initEmptyHeader() {
        viewEmptyHeader = View.inflate(this,R.layout.app_layout_empty_header,null)
    }
}