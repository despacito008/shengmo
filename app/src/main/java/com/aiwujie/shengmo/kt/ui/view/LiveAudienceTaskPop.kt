package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveTaskBean
import com.aiwujie.shengmo.kt.ui.adapter.LiveTaskAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import razerdp.basepopup.BasePopupWindow

class LiveAudienceTaskPop(context: Context) : BasePopupWindow(context) {
    lateinit var refreshTask:SmartRefreshLayout
    lateinit var rvTask:RecyclerView
    lateinit var tvTip:TextView
    private lateinit var tvRank:TextView
    lateinit var uid: String
    constructor(context: Context,uid:String) : this(context) {
        this.uid = uid
        initView()
        getTask()
    }

    fun initView() {
        refreshTask = findViewById(R.id.smart_refresh_live_audience_task)
        rvTask = findViewById(R.id.rv_live_audience_task)
        tvTip = findViewById(R.id.tv_live_audience_task)
        refreshTask.setNoMoreData(true)
        refreshTask.setOnRefreshListener { getTask() }
        tvRank = findViewById(R.id.tv_pop_live_task_rank)
        tvRank.setOnClickListener {
            onLiveTaskPopListener?.doTaskRank()
        }
    }

    private fun getTask() {
        HttpHelper.getInstance().getLiveTaskList(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                refreshTask.finishRefresh()
                val taskBean = GsonUtil.GsonToBean(data,LiveTaskBean::class.java)
                taskBean?.data?.run {
                    tvTip.text = taskTips
                    val taskAdapter = LiveTaskAdapter(context,taskList)
                    val layoutManager = LinearLayoutManager(context)
                    rvTask.adapter = taskAdapter
                    rvTask.layoutManager = layoutManager
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }

        })
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_audience_task)
    }

    interface OnLiveTaskPopListener {
        fun doTaskRank()
    }

    var onLiveTaskPopListener:OnLiveTaskPopListener? = null
}