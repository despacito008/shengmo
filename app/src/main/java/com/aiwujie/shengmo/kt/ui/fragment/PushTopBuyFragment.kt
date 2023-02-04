package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.StampStatisticalBean
import com.aiwujie.shengmo.kt.adapter.PushTopBuyAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 推顶卡明细 - 购买记录
 */
class PushTopBuyFragment: NormalListFragment() {

    override fun loadData() {
        gLoadHolder.showLoading()
        getPushTopData()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        pushTopList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getPushTopData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getPushTopData()
            }
        })
    }

    var page = 0
    var pushTopAdapter: PushTopBuyAdapter? = null
    lateinit var pushTopList:ArrayList<StampStatisticalBean.DataBean>
    fun getPushTopData() {
        HttpHelper.getInstance().getPushTopBuyData(page,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    val tempData = GsonUtil.GsonToBean(data, StampStatisticalBean::class.java)
                    tempData?.data?.let {
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMore()
                        gLoadHolder.showLoadSuccess()
                        when (page) {
                            0 -> {
                                pushTopList.clear()
                                pushTopList.addAll(it)
                                pushTopAdapter = PushTopBuyAdapter(activity,pushTopList)
                                rvSearchResult.adapter = pushTopAdapter
                                rvSearchResult.layoutManager = LinearLayoutManager(activity)
                            }
                            else -> {
                                val temp = pushTopList.size
                                pushTopList.addAll(it)
                                pushTopAdapter?.notifyItemRangeInserted(temp,it.size)
                            }
                        }
                    }
                }

            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 3000 && page == 0) {
                    gLoadHolder.showEmpty()
                }
            }
        })
    }
}