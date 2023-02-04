package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.StampStatisticalBean
import com.aiwujie.shengmo.kt.adapter.StampStatisticalAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 消息邮票 - 明细
 */
class StampStatisticFragment: NormalListFragment() {
    companion object {
        fun newInstance(type: Int):StampStatisticFragment {
            val args = Bundle()
            args.putInt("type",type)
            val fragment = StampStatisticFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var page = 0
    var stampAdapter:StampStatisticalAdapter? = null
    lateinit var statisticalList:ArrayList<StampStatisticalBean.DataBean>
    override fun initView(rootView: View) {
        super.initView(rootView)
        statisticalList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getStatisticalData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getStatisticalData()
            }
        })
    }



    override fun loadData() {
        gLoadHolder.showLoading()
        getStatisticalData()
    }

    fun getStatisticalData() {
        HttpHelper.getInstance().getStampStatistical(arguments.getInt("type"),page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    gLoadHolder.showLoadSuccess()
                    refreshLayout.finishLoadMore()
                    refreshLayout.finishRefresh()
                    val temp = GsonUtil.GsonToBean(data,StampStatisticalBean::class.java)
                    temp?.data?.let {
                        when (page) {
                            0 -> {
                                statisticalList.clear()
                                statisticalList.addAll(it)
                                stampAdapter = StampStatisticalAdapter(activity,arguments.getInt("type"),statisticalList)
                                rvSearchResult.adapter = stampAdapter
                                rvSearchResult.layoutManager = LinearLayoutManager(activity)
                            }
                            else -> {
                                val tempIndex = statisticalList.size
                                statisticalList.addAll(it)
                                stampAdapter?.notifyItemRangeInserted(tempIndex,it.size)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 3000 && page == 0) {
                    gLoadHolder.showEmpty()
                }
            }
        })
    }
}