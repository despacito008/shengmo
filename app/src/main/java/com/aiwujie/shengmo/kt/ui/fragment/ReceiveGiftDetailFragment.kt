package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.PresentReceiveData
import com.aiwujie.shengmo.kt.ui.adapter.ReceiveGiftAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 礼物明细 - 收到的礼物
 */
class ReceiveGiftDetailFragment : NormalListFragment() {

    var page = 0
    override fun loadData() {
        gLoadHolder.showLoading()
        getGiftDetail()
    }


    override fun initView(rootView: View) {
        super.initView(rootView)
        recordList = ArrayList()
        initListener()
    }

    fun initListener() {
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

    var recordAdapter: ReceiveGiftAdapter? = null
    lateinit var recordList: ArrayList<PresentReceiveData.DataBean>
    private fun getGiftDetail() {
        HttpHelper.getInstance().getReceiveGiftDetail(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadHolder.showLoadSuccess()
                    val tempData = GsonUtil.GsonToBean(data, PresentReceiveData::class.java)
                    tempData?.data?.let {
                        when (page) {
                            0 -> {
                                recordList.clear()
                                recordList.addAll(it)
                                recordAdapter = ReceiveGiftAdapter(activity, recordList)
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
                //ToastUtil.show(activity, msg)
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 3000 && page == 0) {
                    gLoadHolder.showEmpty()
                }
            }

        })
    }

}