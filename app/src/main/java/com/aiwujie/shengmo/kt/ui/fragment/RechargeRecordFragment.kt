package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.BillData
import com.aiwujie.shengmo.kt.ui.adapter.RechargeRecordAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 充值明细 - 充值记录
 */
class RechargeRecordFragment: LazyFragment() {

    lateinit var smartRefresh: SmartRefreshLayout
    lateinit var rvRefresh: RecyclerView
    private lateinit var llEmpty: LinearLayout
    var page = 0
    override fun loadData() {
        recordList = ArrayList()
        getRechargeRecord()
        initListener()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_layout_simple_list
    }


    override fun initView(rootView:View) {
        smartRefresh = rootView.findViewById(R.id.smart_refresh_simple_list)
        rvRefresh = rootView.findViewById(R.id.rv_simple_list)
        llEmpty = rootView.findViewById(R.id.layout_normal_empty)
    }

    fun initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getRechargeRecord()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getRechargeRecord()
            }

        })
    }
    var recordAdapter: RechargeRecordAdapter? = null
    lateinit var recordList:ArrayList<BillData.DataBean>
    private fun getRechargeRecord() {
        HttpHelper.getInstance().getRechargeRecordDetail(page,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?,msg:String?) {
                val tempData = GsonUtil.GsonToBean(data, BillData::class.java)
                tempData?.data?.let {
                    smartRefresh.finishRefresh()
                    smartRefresh.finishLoadMore()
                    when(page) {
                        0 -> {
                            recordList.clear()
                            recordList.addAll(it)
                            recordAdapter = RechargeRecordAdapter(activity,recordList)
                            val layoutManager = LinearLayoutManager(activity)
                            rvRefresh.adapter = recordAdapter
                            rvRefresh.layoutManager = layoutManager
                        }
                        else -> {
                            val temp = recordList.size
                            recordList.addAll(it)
                            recordAdapter?.notifyItemRangeInserted(temp,it.size)
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