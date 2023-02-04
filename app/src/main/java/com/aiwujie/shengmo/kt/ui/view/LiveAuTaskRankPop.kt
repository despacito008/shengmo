package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveTaskRankBean
import com.aiwujie.shengmo.kt.ui.adapter.LiveTaskRankAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import razerdp.basepopup.BasePopupWindow

class LiveAuTaskRankPop(context: Context) : BasePopupWindow(context) {
    lateinit var refreshTask:SmartRefreshLayout
    lateinit var rvTask:RecyclerView
    lateinit var uid: String
    constructor(context: Context,uid:String) : this(context) {
        this.uid = uid
        initView()
        getTaskRank()
    }

    fun initView() {
        refreshTask = findViewById(R.id.smart_refresh_live_task_rank)
        rvTask = findViewById(R.id.rv_live_task_rank)
        refreshTask.setNoMoreData(true)
        refreshTask.setOnRefreshListener { getTaskRank() }
    }

    private fun getTaskRank() {
        HttpHelper.getInstance().getChatRanking(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                refreshTask.finishRefresh()
                val taskBean = GsonUtil.GsonToBean(data,LiveTaskRankBean::class.java)
                taskBean?.data?.run {
                    val taskAdapter = LiveTaskRankAdapter(context,this)
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
        return createPopupById(R.layout.app_pop_live_task_rank)
    }
}