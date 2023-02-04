package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.adapter.ThumbUpCommentAdapter
import com.aiwujie.shengmo.adapter.ThumbUpUsAdapter
import com.aiwujie.shengmo.bean.LaudListData
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 动态详情 - 点赞列表
 */
class DynamicThumbUpFragment : NormalListFragment() {

    companion object {
        fun newInstance(): DynamicThumbUpFragment {
            val args = Bundle()
            val fragment = DynamicThumbUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        thumbList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getThumbUpList()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getThumbUpList()
            }
        })
    }

    lateinit var did: String
    var page = 0
    lateinit var thumbList: ArrayList<LaudListData.DataBean>
    var thumbAdapter: ThumbUpCommentAdapter? = null
    override fun loadData() {
        gLoadHolder.showLoading()
        did = activity.intent.getStringExtra("did")!!
        getThumbUpList()

    }

    fun getThumbUpList() {
        HttpHelper.getInstance().getDynamicThumb(did, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                gLoadHolder.showLoadSuccess()
                val tempData = GsonUtil.GsonToBean(data, LaudListData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            thumbList.clear()
                            thumbList.addAll(this)
                            thumbAdapter = ThumbUpCommentAdapter(activity, thumbList,this@DynamicThumbUpFragment)
                            with(rvSearchResult) {
                                adapter = thumbAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }

                        }
                        else -> {
                            val tempSize = thumbList.size
                            thumbList.addAll(this)
                            thumbAdapter?.notifyItemRangeInserted(tempSize, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (page == 0) {
                    gLoadHolder.showEmpty()
                }
            }
        })
    }
}