package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.GzFsHyListviewData
import com.aiwujie.shengmo.kt.adapter.GroupInviteAttentationAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class GroupInviteAttentionFragment : NormalListFragment() {
    private lateinit var userList: ArrayList<GzFsHyListviewData.DataBean>
    private lateinit var inviteUidList:ArrayList<String>
    override fun loadData() {
        gLoadHolder.showLoading()
        getNearData()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        userList = ArrayList()
        inviteUidList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getNearData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page=0
                getNearData()
            }
        })
    }

    var page = 0
    var nearAdapter:GroupInviteAttentationAdapter? = null
    private fun getNearData() {
        HttpHelper.getInstance().getSocialUser(MyApp.uid,0,page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, GzFsHyListviewData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(this)
                            nearAdapter = GroupInviteAttentationAdapter(activity,userList)
                            with(rvSearchResult) {
                                adapter = nearAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }
                        else -> {
                            val tempIndex = userList.size
                            userList.addAll(this)
                            nearAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
            }
        })
    }

    fun getChooseId():List<String> {
        return nearAdapter?.getChooseId()?:ArrayList()
    }
}