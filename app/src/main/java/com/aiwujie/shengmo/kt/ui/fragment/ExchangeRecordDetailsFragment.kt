package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.ConsumptionExchangeData
import com.aiwujie.shengmo.bean.SystemGiftRecordBean
import com.aiwujie.shengmo.kt.ui.adapter.ExchangeRecordAdapter
import com.aiwujie.shengmo.kt.ui.adapter.SystemGiftRecordAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 兑换记录
 */
class ExchangeRecordDetailsFragment : NormalListFragment() {
    var page = 0
    companion object {
        fun newInstance(type: Int): ExchangeRecordDetailsFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = ExchangeRecordDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var type = 0
    override fun loadData() {
        gLoadHolder.showLoading()
        type = arguments.getInt("type")
        getExchangeRecord()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        recordList = ArrayList()
        initListener()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getExchangeRecord()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getExchangeRecord()
            }
        })
    }

    var recordAdapter: ExchangeRecordAdapter? = null
    lateinit var recordList:ArrayList<ConsumptionExchangeData.DataBean>
    private fun getExchangeRecord() {
        HttpHelper.getInstance().getExchangeRecord(type, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadHolder.showLoadSuccess()
                    val tempData = GsonUtil.GsonToBean(data, ConsumptionExchangeData::class.java)
                    tempData?.data?.let {
                        when (page) {
                            0 -> {
                                recordList.clear()
                                recordList.addAll(it)
                                recordAdapter = ExchangeRecordAdapter(activity, recordList)
                                val layoutManager = LinearLayoutManager(activity)
                                rvSearchResult.adapter = recordAdapter
                                rvSearchResult.layoutManager = layoutManager
                            }
                            else -> {
                                val temp = recordList.size
                                recordList.addAll(it)
                                recordAdapter?.notifyItemRangeInserted(temp, it.size)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(activity, msg)
                if (code == 4001 && page == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoading()
                }
            }

        })
    }

}