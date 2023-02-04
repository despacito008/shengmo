package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ConsumptionGiveData
import com.aiwujie.shengmo.kt.ui.adapter.RechargeGiveAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 充值明细 - 消费记录 - 赠送
 */
class RechargeGiveFragment: NormalListFragment() {
    var page = 0
    override fun initView(rootView: View) {
        super.initView(rootView)
        gLoadHolder.showLoading()
        recordList = ArrayList()
        initListener()
    }

    override fun loadData() {
        getRechargeGiveData()
    }

    fun initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getRechargeGiveData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getRechargeGiveData()
            }

        })
    }
    var recordAdapter: RechargeGiveAdapter? = null
    lateinit var recordList:ArrayList<ConsumptionGiveData.DataBean>
    private fun getRechargeGiveData() {
        HttpHelper.getInstance().getGivePsRecord(0,page,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?,msg:String?) {
                gLoadHolder.showLoadSuccess()
                val tempData = GsonUtil.GsonToBean(data, ConsumptionGiveData::class.java)
                tempData?.data?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    when(page) {
                        0 -> {
                            recordList.clear()
                            recordList.addAll(it)
                            recordAdapter = RechargeGiveAdapter(activity,recordList)
                            val layoutManager = LinearLayoutManager(activity)
                            rvSearchResult.adapter = recordAdapter
                            rvSearchResult.layoutManager = layoutManager
                        }
                        else -> {
                            val tempIndex = recordList.size
                            recordList.addAll(it)
                            recordAdapter?.notifyItemRangeInserted(tempIndex,it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(activity,msg)
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