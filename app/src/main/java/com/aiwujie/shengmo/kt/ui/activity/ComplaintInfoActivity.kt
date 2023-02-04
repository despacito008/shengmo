package com.aiwujie.shengmo.kt.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.adapter.ComplaintInformationAdapter
import com.aiwujie.shengmo.bean.ComplaintInformatoinBean
import com.aiwujie.shengmo.kt.ui.activity.normallist.NormalListActivity
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class ComplaintInfoActivity: NormalListActivity(){
    lateinit var name:String
    lateinit var type:String
    lateinit var uid:String
    lateinit var reportList:ArrayList<ComplaintInformatoinBean.ComplaintInformatoin>
    var reportAdapter: ComplaintInformationAdapter? = null
    override fun initViewComplete() {
        super.initViewComplete()
        name = intent.getStringExtra("user_name")!!
        type = intent.getStringExtra("type")!!
        uid = intent.getStringExtra("uid")!!
        reportList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object: OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getData()
            }
        })
    }

    override fun getPageTitle(): String {
        return name
    }

    override fun loadData() {
        loadingHolder.showLoading()
        getData()
    }

    var page = 0
    fun getData() {
        HttpHelper.getInstance().getComplaintRecordList(type,uid,page,object: HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                loadingHolder.showLoadSuccess()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                val tempData = GsonUtil.GsonToBean(data, ComplaintInformatoinBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            reportList.clear()
                            reportList.addAll(this)
                            reportAdapter = ComplaintInformationAdapter(reportList,type,this@ComplaintInfoActivity)
                            with(rvList) {
                                adapter = reportAdapter
                                layoutManager = LinearLayoutManager(this@ComplaintInfoActivity)
                            }
                        }
                        else -> {
                            val tempIndex = reportList.size
                            reportList.addAll(this)
                            reportAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 3000) {
                    if (page == 0) {
                        loadingHolder.showEmpty()
                    } else {
                        loadingHolder.showLoadSuccess()
                    }
                } else {
                    loadingHolder.showLoadSuccess()
                    ToastUtil.show(this@ComplaintInfoActivity,msg)
                }
            }
        })
    }
}