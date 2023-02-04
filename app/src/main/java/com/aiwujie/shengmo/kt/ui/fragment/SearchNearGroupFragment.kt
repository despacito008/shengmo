package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.GroupInfoActivity
import com.aiwujie.shengmo.adapter.SearchGroupResultAdapter
import com.aiwujie.shengmo.bean.GroupData
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 群广场 - 搜索
 */
class SearchNearGroupFragment: LazyFragment() {
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var rvSearchResult:RecyclerView
    var page = 0
    lateinit var groupList:ArrayList<GroupData.DataBean>
    var groupAdapter:SearchGroupResultAdapter? = null
    override fun loadData() {
        val keyword = activity.intent.getStringExtra("search")
        val type = arguments.getInt("type",0)
        HttpHelper.getInstance().searchGroup(type,keyword,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    gLoadHolder.showLoadSuccess()
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    refreshLayout.setNoMoreData(false)
                    var tempData = GsonUtil.GsonToBean(data,GroupData::class.java)
                    tempData?.data?.let {
                        when(page) {
                            0 -> {
                                groupList.clear()
                                groupList.addAll(it)
                                groupAdapter = SearchGroupResultAdapter(activity,groupList,tempData.retcode)
                                rvSearchResult.adapter = groupAdapter
                                rvSearchResult.layoutManager = LinearLayoutManager(activity)
                                groupAdapter?.setOnSimpleItemListener {
//                                    var intent = Intent(activity,GroupInfoActivity::class.java)
//                                    with(intent) {
//                                        putExtra("groupId",groupList[it].gid)
//                                        startActivity(this)
//                                    }
                                    index ->
                                    GroupDetailActivity.start(activity,groupList[index].gid,0,false)
                                }
                            }
                            else -> {
                                var temp = groupList.size
                                groupList.addAll(it)
                                groupAdapter?.notifyItemRangeInserted(temp,it.size)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                activity?.let {
                    ToastUtil.show(activity,msg)
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    if (code == 4001) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                        if (page == 0) {
                            gLoadHolder.showEmpty()
                        }
                    }
                }
            }
        })
    }
    companion object {
        fun newInstance(type:Int):SearchNearGroupFragment {
            val args = Bundle()
            args.putInt("type",type);
            val fragment = SearchNearGroupFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getContentViewId(): Int {
        return R.layout.app_layout_normal_list
    }
    lateinit var gLoadHolder:Gloading.Holder
    override fun initView(rootView: View) {
        refreshLayout = rootView.findViewById(R.id.smart_refresh_normal_list)
        rvSearchResult = rootView.findViewById(R.id.rv_normal_list)
        gLoadHolder = Gloading.getDefault().wrap(rvSearchResult)
        groupList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                loadData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                loadData()
            }

        })
    }

}