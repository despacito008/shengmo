package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout

abstract class NormalListActivity: BaseActivity() {

    lateinit var ivNormalTitleBack: ImageView
    lateinit var tvNormalTitleTitle: TextView
    lateinit var ivNormalTitleMore: ImageView
    lateinit var tvNormalTitleMore: TextView
    lateinit var refreshLayout:SmartRefreshLayout
    lateinit var flRootView:FrameLayout
    lateinit var rvList:RecyclerView
    lateinit var loadingHolder:Gloading.Holder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_normal_list)
        StatusBarUtil.showLightStatusBar(this)
        ivNormalTitleBack = findViewById(R.id.iv_normal_title_back)
        tvNormalTitleTitle = findViewById(R.id.tv_normal_title_title)
        tvNormalTitleMore = findViewById(R.id.tv_normal_title_more)
        ivNormalTitleMore = findViewById(R.id.iv_normal_title_more)
        refreshLayout = findViewById(R.id.smart_refresh_activity_normal_list)
        flRootView = findViewById(R.id.fl_activity_normal_list)
        rvList = findViewById(R.id.rv_activity_normal_list)
        loadingHolder = Gloading.getDefault().wrap(rvList)
        initViewComplete()
        tvNormalTitleTitle.text = getPageTitle()
        ivNormalTitleBack.setOnClickListener {
            clickBack()
        }
        loadData()
    }

    private fun clickBack() {
        finish()
    }

    abstract fun getPageTitle(): String

    abstract fun loadData()

    open fun initViewComplete() {}
}