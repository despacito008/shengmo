package com.aiwujie.shengmo.kt.ui.fragment.normallist

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.CallBuyHistoryBean
import com.aiwujie.shengmo.bean.CallUseHistoryBean
import com.aiwujie.shengmo.kt.adapter.CallBuyStatisticalAdapter
import com.aiwujie.shengmo.kt.adapter.CallUseStatisticalAdapter
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment.normallist
 * @ClassName: CallCardUseFragment
 * @Author: xmf
 * @CreateDate: 2022/5/27 15:43
 * @Description: 呼唤明细 - 购买记录
 */
class CallCardBuyHistoryFragment: NormalListFragment() {
    override fun loadData() {
        getCallBuyHistory()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getCallBuyHistory()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getCallBuyHistory()
            }
        })
    }

    var page = 0
    private val historyList: ArrayList<CallBuyHistoryBean.DataBean> by lazy { ArrayList<CallBuyHistoryBean.DataBean>() }
    var historyAdapter: CallBuyStatisticalAdapter? = null
    private fun getCallBuyHistory() {
        HttpHelper.getInstance().getCallBuyHistory(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, CallBuyHistoryBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            if (this.size == 0) {
                                gLoadHolder.showEmpty()
                            } else {
                                gLoadHolder.showLoadSuccess()
                            }
                            historyList.clear()
                            historyList.addAll(this)
                            historyAdapter = CallBuyStatisticalAdapter(activity, historyList)
                            with(rvSearchResult) {
                                adapter = historyAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                        }
                        else -> {
                            val tempIndex = historyList.size
                            historyList.addAll(this)
                            historyAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

}
