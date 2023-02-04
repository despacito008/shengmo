package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CreateGroupBean
import com.aiwujie.shengmo.bean.DefaultGroupBean
import com.aiwujie.shengmo.bean.GroupData
import com.aiwujie.shengmo.bean.NormalGroupBean
import com.aiwujie.shengmo.kt.adapter.UserGroupAdapter
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.normallist
 * @ClassName: UserGroupListActivity
 * @Author: xmf
 * @CreateDate: 2022/6/7 11:56
 * @Description: 用户相关群组列表页面
 */
class UserGroupListActivity:NormalHeaderListActivity() {

    companion object {
        fun start(context:Context,uid:String) {
            val intent = Intent(context,UserGroupListActivity::class.java)
            intent.putExtra(IntentKey.UID,uid)
            context.startActivity(intent)
        }
    }

    override fun initViewComplete() {
        super.initViewComplete()
        initHeaderView()
        refreshLayout.setOnRefreshListener {
            page = 0
            getJoinGroup()
        }
        refreshLayout.setOnLoadMoreListener {
            page++
            getJoinGroup()
        }
    }

    override fun getPageTitle(): String {
        return "Ta的群组"
    }

    override fun loadData() {
        page = 0
        getJoinGroup()
    }

    lateinit var headerView: View
    lateinit var flHeader1:FrameLayout
    lateinit var flHeader2:FrameLayout
    lateinit var flHeader3:FrameLayout
    lateinit var rvFans:RecyclerView
    lateinit var rvCreate:RecyclerView
    private fun initHeaderView() {
        headerView = View.inflate(this, R.layout.app_layout_user_group_header, null)
        flHeader1 = headerView.findViewById(R.id.fl_header_1)
        flHeader2 = headerView.findViewById(R.id.fl_header_2)
        flHeader3 = headerView.findViewById(R.id.fl_header_3)
        rvFans = headerView.findViewById(R.id.rv_header_fans)
        rvCreate = headerView.findViewById(R.id.rv_header_create)
        getCreateGroup()
    }

    var uid = ""
    private fun getCreateGroup() {
        uid = intent.getStringExtra(IntentKey.UID)?:""
        HttpHelper.getInstance().getCreateGroupList(uid,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,CreateGroupBean::class.java)
                tempData?.data?.run {
                    if (fangroup.isNotEmpty()) {
                        flHeader1.visibility = View.VISIBLE
                        val groupAdapter = UserGroupAdapter(this@UserGroupListActivity,fangroup)
                        with(rvFans) {
                            adapter = groupAdapter
                            layoutManager = LinearLayoutManager(this@UserGroupListActivity)
                        }
                        groupAdapter.simpleItemListener = OnSimpleItemListener {
                            GroupDetailActivity.start(this@UserGroupListActivity,fangroup[it].gid, 0,false)
                        }
                    }
                    if (group.isNotEmpty()) {
                        flHeader2.visibility = View.VISIBLE
                        val groupAdapter = UserGroupAdapter(this@UserGroupListActivity,group)
                        with(rvCreate) {
                            adapter = groupAdapter
                            layoutManager = LinearLayoutManager(this@UserGroupListActivity)
                        }
                        groupAdapter.simpleItemListener = OnSimpleItemListener {
                            GroupDetailActivity.start(this@UserGroupListActivity,group[it].gid, 0,false)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    var page = 0
    var groupAdapter:UserGroupAdapter? = null
    private val groupList:ArrayList<NormalGroupBean> by lazy { ArrayList<NormalGroupBean>() }
    private fun getJoinGroup() {
        HttpHelper.getInstance().getUserJoinGroupList(uid,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                val tempData = GsonUtil.GsonToBean(data,DefaultGroupBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            groupList.clear()
                            groupList.addAll(this)
                            groupAdapter = UserGroupAdapter(this@UserGroupListActivity,groupList)
                            val headerAdapter = HeaderViewAdapter(groupAdapter)
                            headerAdapter.addHeaderView(headerView)
                            with(rvList) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(this@UserGroupListActivity,1,headerAdapter)
                            }
                            groupAdapter?.simpleItemListener = OnSimpleItemListener {
                                GroupDetailActivity.start(this@UserGroupListActivity,groupList[it].gid, 0,false)
                            }
                        }
                        else -> {
                            val tempIndex = groupList.size
                            groupList.addAll(this)
                            groupAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (page == 0 && code == 4001) {
                    groupAdapter = UserGroupAdapter(this@UserGroupListActivity,groupList)
                    val headerAdapter = HeaderViewAdapter(groupAdapter)
                    headerAdapter.addHeaderView(headerView)
                    headerAdapter.addHeaderView(viewEmptyHeader)
                    with(rvList) {
                        adapter = headerAdapter
                        layoutManager = HeaderViewGridLayoutManager(this@UserGroupListActivity,1,headerAdapter)
                    }
                } else {
                    msg?.showToast()
                }
            }
        })
    }
}
