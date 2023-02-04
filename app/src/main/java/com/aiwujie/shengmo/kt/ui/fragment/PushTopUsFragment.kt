package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity
import com.aiwujie.shengmo.adapter.PushTopUsAdapter
import com.aiwujie.shengmo.bean.DynamicDetailBean
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 推顶卡明细 - 被推记录
 */
class PushTopUsFragment: NormalListFragment() {

    override fun loadData() {
        gLoadHolder.showLoading()
        getPushTopData()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        pushTopList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getPushTopData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getPushTopData()
            }
        })
    }

    var page = 0
    var pushTopAdapter:PushTopUsAdapter? = null
    lateinit var pushTopList:ArrayList<DynamicDetailBean.DataBean>
    fun getPushTopData() {
        HttpHelper.getInstance().getPushTopUsData(page,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    val tempData = GsonUtil.GsonToBean(data, DynamicDetailBean::class.java)
                    tempData?.data?.let {
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMore()
                        gLoadHolder.showLoadSuccess()
                        when (page) {
                            0 -> {
                                pushTopList.clear()
                                pushTopList.addAll(it)
                                pushTopAdapter = PushTopUsAdapter(activity,pushTopList)
                                rvSearchResult.adapter = pushTopAdapter
                                rvSearchResult.layoutManager = LinearLayoutManager(activity)
                                pushTopAdapter?.setOnSimpleItemListener { index ->
                                    val intent = Intent(activity, NewDynamicDetailActivity::class.java)
                                    intent.putExtra("did",pushTopList[index].did)
                                    startActivity(intent)
                                }
                            }
                            else -> {
                                val temp = pushTopList.size
                                pushTopList.addAll(it)
                                pushTopAdapter?.notifyItemRangeInserted(temp,it.size)
                            }
                        }
                    }
                }

            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 3000 && page == 0) {
                    gLoadHolder.showEmpty()
                }
            }
        })
    }
}