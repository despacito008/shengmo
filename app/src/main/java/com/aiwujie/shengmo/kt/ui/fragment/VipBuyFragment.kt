package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.Frag_VipGmDataBean
import com.aiwujie.shengmo.kt.ui.adapter.VipBuyAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class VipBuyFragment: NormalListFragment() {
    override fun loadData() {
        gLoadHolder.showLoading()
        getVipBuyData()
    }
    lateinit var dataList:ArrayList<Frag_VipGmDataBean.DataBean>
    var vipAdapter: VipBuyAdapter? = null
    override fun initView(rootView: View) {
        super.initView(rootView)
        dataList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getVipBuyData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getVipBuyData()
            }
        })
    }
    var page = 0
    private fun getVipBuyData() {
        HttpHelper.getInstance().getVipBuyList(page,object :HttpCodeMsgListener  {
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
                            vipAdapter = VipBuyAdapter(activity,dataList)
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