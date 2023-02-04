package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.GroupOperationData
import com.aiwujie.shengmo.kt.adapter.GroupMsgOperateAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class GroupMsgOperateActivity : BaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvClear: TextView
    private lateinit var ivCancel: ImageView
    private lateinit var ivTitleRight: ImageView
    private lateinit var smartRefresh: SmartRefreshLayout
    private lateinit var rvGroupMsg: RecyclerView
    private lateinit var llEmpty: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_group_msg_operation)
        groupMsgList = ArrayList()
        initView()
        getMsgList()
        setListener()
    }

    private fun initView() {
        StatusBarUtil.showLightStatusBar(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        tvClear = findViewById(R.id.tv_normal_title_more)
        ivCancel = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        smartRefresh = findViewById(R.id.smart_refresh_group_msg_operation)
        rvGroupMsg = findViewById(R.id.rv_group_msg_operation)
        llEmpty = findViewById(R.id.layout_normal_empty)
        ivTitleRight.visibility = View.GONE
        tvClear.visibility = View.VISIBLE
        tvTitle.text = "群组通知"
        tvClear.text = "清空"
    }

    var page = 0
    lateinit var groupMsgList: ArrayList<GroupOperationData.DataBean>
    var operateAdapter: GroupMsgOperateAdapter? = null
    var isLoading = false
    var hasMore = false
    private fun getMsgList() {
        if (isLoading) {
            return
        }
        isLoading = true
        HttpHelper.getInstance().getGroupMsgOperateList(page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                isLoading = false
                hasMore = true
                llEmpty.visibility = View.GONE
                val tempData = GsonUtil.GsonToBean(data, GroupOperationData::class.java)
                val tempList = tempData.data
                smartRefresh.setNoMoreData(false)
                if (page == 0) {
                    smartRefresh.finishRefresh()
                    groupMsgList.clear()
                    groupMsgList.addAll(tempList)
                    operateAdapter = GroupMsgOperateAdapter(this@GroupMsgOperateActivity, groupMsgList)
                    val linearLayoutManager = LinearLayoutManager(this@GroupMsgOperateActivity)
                    rvGroupMsg.adapter = operateAdapter
                    rvGroupMsg.layoutManager = linearLayoutManager
                } else {
                    smartRefresh.finishLoadMore()
                    val temp = groupMsgList.size
                    groupMsgList.addAll(tempList)
                    operateAdapter?.notifyItemRangeInserted(temp, tempList.size)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMsgOperateActivity, msg)
                isLoading = false
                smartRefresh.finishRefresh()
                if (code == 4001) {
                    hasMore = false
                    smartRefresh.finishLoadMoreWithNoMoreData()
                    if (page == 0) llEmpty.visibility = View.VISIBLE
                } else {
                    smartRefresh.finishLoadMore()
                }
            }
        })
    }

    fun setListener() {
        ivCancel.setOnClickListener {
            finish()
        }
        tvClear.setOnClickListener {
            if (groupMsgList.size != 0) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("确认清空吗?")
                        .setPositiveButton("否") { dialog, _ -> dialog.dismiss() }.setNegativeButton("是") { _, _ -> clearData() }.create().show()
            }
        }
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                if (hasMore) {
                    page++
                    getMsgList()
                }
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getMsgList()
            }
        })
    }

    private fun clearData() {
        HttpHelper.getInstance().clearGroupOperateMsg(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(this@GroupMsgOperateActivity, "清空成功")
                llEmpty.visibility = View.VISIBLE
                groupMsgList.clear()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMsgOperateActivity, msg)
            }

        })
    }
}