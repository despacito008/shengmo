package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.UserGroupingFriendAdapter
import com.aiwujie.shengmo.bean.FenzuListData
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class GroupingFriendFragment:NormalListFragment() {
    companion object {
        fun newInstance(gid: String): GroupingFriendFragment {
            val args = Bundle()
            args.putString("gid", gid)
            val fragment = GroupingFriendFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var gid = ""
    var page = 0
    lateinit var userList:ArrayList<FenzuListData.DataBean>
    var userFansAdapter:UserGroupingFriendAdapter? = null
    override fun loadData() {
        gid = arguments.getString("gid","")
        gLoadHolder.showLoading()
        userList = ArrayList()
        getGroupingData()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        refreshLayout.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getGroupingData()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getGroupingData()
            }
        })
    }

    private fun getGroupingData() {
        HttpHelper.getInstance().getGroupingFriendList(gid,page,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                val tempData = GsonUtil.GsonToBean(data, FenzuListData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(this)
                            userFansAdapter = UserGroupingFriendAdapter(activity,userList,"",false)
                            with(rvSearchResult) {
                                adapter = userFansAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            userFansAdapter?.setOnSimpleItemListener {
                                index->
                                val intent = Intent(activity,UserInfoActivity::class.java)
                                intent.putExtra("uid",userList[index].uid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val tempIndex = userList.size
                            userList.addAll(this)
                            userFansAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4002) {
                    if (page == 0) {
                        gLoadHolder.showEmpty()
                    } else {
                        gLoadHolder.showLoadSuccess()
                    }
                } else {
                    msg?.showToast()
                    gLoadHolder.showLoadSuccess()
                }
            }
        })
    }
}