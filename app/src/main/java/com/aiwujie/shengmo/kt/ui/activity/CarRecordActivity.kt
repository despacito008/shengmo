package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity
import com.aiwujie.shengmo.adapter.AtUsAdapter
import com.aiwujie.shengmo.bean.AtUserData
import com.aiwujie.shengmo.bean.CarRecordModel
import com.aiwujie.shengmo.kt.adapter.CarRecordAdapter
import com.aiwujie.shengmo.kt.ui.activity.normallist.NormalListActivity
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @program: newshengmo
 * @description: 座驾购买明细
 * @author: whl
 * @create: 2022-05-27 10:30
 **/
class CarRecordActivity:NormalListActivity() {
    lateinit var gLoadingHolder: Gloading.Holder
    var page = 0
    var carRecorddAdapter: CarRecordAdapter? = null
    lateinit var list:ArrayList<CarRecordModel.DataBean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = ArrayList()
        gLoadingHolder = Gloading.getDefault().wrap(rvList)
        initListener()

    }

    fun initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                loadData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                loadData()
            }
        })
    }


    override fun getPageTitle(): String {
       return "明细"
    }

    override fun loadData() {
        HttpHelper.getInstance().carRecord(page,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                var tempData = GsonUtil.GsonToBean(data, CarRecordModel::class.java)
                tempData?.data?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadingHolder.showLoadSuccess()
                    when (page) {
                        0 -> {
                            list.clear()
                            list.addAll(it)
                            carRecorddAdapter = CarRecordAdapter(this@CarRecordActivity,list)
                            rvList.adapter = carRecorddAdapter
                            rvList.layoutManager = LinearLayoutManager(this@CarRecordActivity)

                        }
                        else -> {
                            val temp = list.size
                            list.addAll(it)
                            carRecorddAdapter?.notifyItemRangeInserted(temp,it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4000 && page == 0) {
                    gLoadingHolder.showEmpty()
                }
            }
        })
    }
}