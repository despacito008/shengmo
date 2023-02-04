package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveLogBean
import com.aiwujie.shengmo.decoration.GridHeaderItemDecoration
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.LiveLogListAdapter
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager

class PurchaseRecordActivity: NormalListActivity() {
    override fun getPageTitle(): String {
        return "购买记录"
    }

    override fun loadData() {
        loadingHolder.showLoading()
        getPurchaseData()
    }

    override fun initViewComplete() {
        super.initViewComplete()
        liveLogList = ArrayList()

    }

    var page = 0
    lateinit var liveLogList:ArrayList<LiveLogBean.DataBean>
    var liveAdapter:LiveLogListAdapter? = null
    var itemDecoration:RecyclerView.ItemDecoration? = null
    private fun getPurchaseData() {
        HttpHelper.getInstance().getBuyLiveLogList(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                loadingHolder.showLoadSuccess()
                val tempData = GsonUtil.GsonToBean(data, LiveLogBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            liveLogList.clear()
                            liveLogList.addAll(this)
                            liveAdapter = LiveLogListAdapter(this@PurchaseRecordActivity, liveLogList)
                            val headerAdapter = HeaderViewAdapter(liveAdapter)
                            headerAdapter.addHeaderView(getHeader())
                            with(rvList) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(this@PurchaseRecordActivity, 2, headerAdapter)
                                //layoutManager = GridLayoutManager(this@PurchaseRecordActivity, 2)
                                if (itemDecoration == null) {
                                    itemDecoration = GridHeaderItemDecoration(20,headerAdapter.headersCount)
                                    addItemDecoration(itemDecoration)
                                }
                            }

                            liveAdapter?.setOnSimpleItemListener { index ->
                                val intent = Intent(this@PurchaseRecordActivity, PlayBackVideoActivity::class.java)
                                liveLogList[index].run {
                                    if (id.isNullOrEmpty() && uid.isNullOrEmpty()) {
                                        intent.putExtra("id",id)
                                        intent.putExtra("uid",uid)
                                        startActivity(intent)
                                    }
                                }

                            }
                        }
                        else -> {
                            val tempIndex = liveLogList.size
                            liveLogList.addAll(this)
                            liveAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 4000) {
                    if (page == 0) loadingHolder.showEmpty() else loadingHolder.showLoadSuccess()
                } else {
                    msg?.showToast()
                    loadingHolder.showLoadSuccess()
                }
            }
        })
    }

    private fun getHeader(): View {
        return View.inflate(this, R.layout.layout_header_purchase_record, null)
    }
}