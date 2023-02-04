package com.aiwujie.shengmo.kt.ui.fragment.normallist

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.CallUseHistoryBean
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
 * @CreateDate: 2022/5/26 15:43
 * @Description: 呼唤明细 - 使用记录
 */
class CallCardUseHistoryFragment : NormalListFragment() {
    override fun loadData() {
        getCallUseHistory()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getCallUseHistory()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getCallUseHistory()
            }
        })
    }

    var page = 0
    private val historyList: ArrayList<CallUseHistoryBean.DataBean> by lazy { ArrayList<CallUseHistoryBean.DataBean>() }
    var historyAdapter: CallUseStatisticalAdapter? = null
    private fun getCallUseHistory() {
        HttpHelper.getInstance().getCallUseHistory(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, CallUseHistoryBean::class.java)
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
                            historyAdapter = CallUseStatisticalAdapter(activity, historyList)
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
