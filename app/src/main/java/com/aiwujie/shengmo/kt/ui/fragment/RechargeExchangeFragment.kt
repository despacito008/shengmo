package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.Frag_VipGmDataBean
import com.aiwujie.shengmo.kt.ui.adapter.VipReceiveAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class RechargeExchangeFragment: NormalListFragment() {
    override fun loadData() {
        gLoadHolder.showLoading()
        getExchangeData()
    }
    lateinit var dataList:ArrayList<Frag_VipGmDataBean.DataBean>
    var vipAdapter: VipReceiveAdapter? = null
    override fun initView(rootView: View) {
        super.initView(rootView)
        dataList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getExchangeData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getExchangeData()
            }
        })
    }
    var page = 0
    private fun getExchangeData() {
        HttpHelper.getInstance().getExchangeRecord(0,page,object :HttpCodeMsgListener  {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                val tempData = GsonUtil.GsonToBean(data, Frag_VipGmDataBean::class.java)
                tempData?.data?.run {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    when (page) {
                        0 -> {
                            dataList.clear()
                            dataList.addAll(this)
                            vipAdapter = VipReceiveAdapter(activity,dataList)
                            with(rvSearchResult) {
                                adapter = vipAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                        }
                        else -> {
                            val tempIndex = dataList.size
                            dataList.addAll(this)
                            vipAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001 && page == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoadSuccess()
                }
            }
        })
    }
}