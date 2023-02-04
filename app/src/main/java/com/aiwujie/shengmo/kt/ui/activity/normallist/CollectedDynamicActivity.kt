package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter
import com.aiwujie.shengmo.bean.DynamicListData
import com.aiwujie.shengmo.kt.util.OnDynamicItemScrollVisibleListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class CollectedDynamicActivity : NormalListActivity() {
    lateinit var gLoadingHolder: Gloading.Holder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gLoadingHolder = Gloading.getDefault().wrap(rvList)
        initListener()
    }

    override fun getPageTitle(): String {
        return "动态收藏"
    }

    override fun loadData() {
        HttpHelper.getInstance().getCollectDynamic(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                gLoadingHolder.showLoadSuccess()
                var tempData = GsonUtil.GsonToBean(data, DynamicListData::class.java)
                tempData?.data?.let {
                    when(page) {
                        0-> {
                            dynamicList.clear()
                            dynamicList.addAll(it)
                            adapter = DynamicRecyclerAdapter(this@CollectedDynamicActivity,dynamicList,2000,"3")
                            var layoutManager = LinearLayoutManager(this@CollectedDynamicActivity)
                            rvList.adapter = adapter
                            rvList.layoutManager = layoutManager
                        }
                        else-> {
                            var temp = dynamicList.size
                            dynamicList.addAll(it)
                            adapter?.notifyItemRangeInserted(temp,it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001 && page == 0) {
                    gLoadingHolder.showEmpty()
                }
            }
        })

    }

    override fun initViewComplete() {
        super.initViewComplete()
        dynamicList = ArrayList()
    }

    var page = 0
    var adapter:DynamicRecyclerAdapter?=null
    lateinit var dynamicList:ArrayList<DynamicListData.DataBean>
    fun initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                loadData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                loadData()
            }
        })

        rvList.addOnScrollListener(object :OnDynamicItemScrollVisibleListener(dynamicList,0) {
            override fun onItemScrollVisible(index: Int) {
                adapter?.tryToPlayVideo(rvList,index,dynamicList[index].playUrl)
            }
        })
    }

}