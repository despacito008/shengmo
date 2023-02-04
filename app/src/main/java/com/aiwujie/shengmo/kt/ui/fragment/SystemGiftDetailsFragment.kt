package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.SystemGiftRecordBean
import com.aiwujie.shengmo.kt.ui.adapter.SystemGiftRecordAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 礼物明细 - 系统赠送
 */
class SystemGiftDetailsFragment : NormalListFragment() {
    var page = 0
    override fun loadData() {
        gLoadHolder.showLoading()
        getGiftDetail()
    }

    override fun initView(rootView:View) {
        super.initView(rootView)
        recordList = ArrayList()
        initListener()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getGiftDetail()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getGiftDetail()
            }
        })
    }

    var recordAdapter:SystemGiftRecordAdapter? = null
    lateinit var recordList:ArrayList<SystemGiftRecordBean.DataBean.ListBean>
    private fun getGiftDetail() {
        HttpHelper.getInstance().getSystemGiftDetail(page,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                activity?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadHolder.showLoadSuccess()
                    val tempData = GsonUtil.GsonToBean(data, SystemGiftRecordBean::class.java)
                    tempData?.data?.let {
                        refreshLayout.setNoMoreData(it.has_more == "0")
                        when (page) {
                            0 -> {
                                recordList.clear()
                                recordList.addAll(it.list)
                                recordAdapter = SystemGiftRecordAdapter(activity, recordList)
                                val layoutManager = LinearLayoutManager(activity)
                                rvSearchResult.adapter = recordAdapter
                                rvSearchResult.layoutManager = layoutManager
                            }
                            else -> {
                                val temp = recordList.size
                                recordList.addAll(it.list)
                                recordAdapter?.notifyItemRangeInserted(temp, it.list.size)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(activity,msg)
            }

        })
    }

}