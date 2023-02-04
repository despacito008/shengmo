package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.GroupInfoActivity
import com.aiwujie.shengmo.adapter.SearchGroupResultAdapter
import com.aiwujie.shengmo.bean.GroupData
import com.aiwujie.shengmo.eventbus.GroupSxEvent
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NormalGroupFragment: NormalListFragment() {
    var page = 0
    var sexual = ""
    var type = 1
    var groupAdapter:SearchGroupResultAdapter?= null
    lateinit var groupList:ArrayList<GroupData.DataBean>

    companion object {
        private const val TYPE = "type"
        fun newInstance(aType: Int) = NormalGroupFragment().apply {
            arguments = Bundle(2).apply {
                putInt(TYPE, aType)
            }
        }
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        type = arguments.getInt("type")
        groupList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getGroupData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getGroupData()
            }
        })
    }

    override fun loadData() {
        gLoadHolder.showLoading()
        getGroupData()
    }

    private fun getGroupData() {
        HttpHelper.getInstance().getNormalGroupData(type,sexual,page,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                activity?.apply {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    val tempData = GsonUtil.GsonToBean(data,GroupData::class.java)
                    tempData?.data?.run {
                        when (page) {
                            0   -> {
                                groupList.clear()
                                groupList.addAll(this)
                                groupAdapter = SearchGroupResultAdapter(activity,groupList,tempData.retcode)
                                with(rvSearchResult) {
                                    adapter = groupAdapter
                                    layoutManager = LinearLayoutManager(activity)
                                }
                                groupAdapter?.setOnSimpleItemListener {
                                    index ->
                                    val intent = Intent(activity,GroupInfoActivity::class.java)
                                    intent.putExtra("groupId",groupList[index].gid)
                                    startActivity(intent)
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

            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001) {
                    if (page == 0) {
                        gLoadHolder.showEmpty()
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(data : GroupSxEvent) {
        if (type == 2 || type == 3) {
            return
        }
        sexual = data.sexFlag
        refreshLayout.autoRefresh()
    }

}