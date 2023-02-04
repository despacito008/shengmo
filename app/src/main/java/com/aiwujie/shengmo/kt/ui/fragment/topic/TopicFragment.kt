package com.aiwujie.shengmo.kt.ui.fragment.topic

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.aiwujie.shengmo.activity.newui.TopicDynamicActivity
import com.aiwujie.shengmo.bean.ListTopicData
import com.aiwujie.shengmo.kt.adapter.DynamicTopicAdapter
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class TopicFragment:NormalListFragment() {
    companion object {
        fun newInstance(pid: String): TopicFragment {
            val args = Bundle()
            args.putString("pid", pid)
            val fragment = TopicFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var page = 0
    var pid = ""
    override fun initView(rootView: View) {
        super.initView(rootView)
        topicList = ArrayList()
        pid = arguments.getString("pid","")
        refreshLayout.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getTopicList()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getTopicList()
            }
        })
    }
    override fun loadData() {
        gLoadHolder.showLoading()
        getTopicList()
    }
    lateinit var topicList:ArrayList<ListTopicData.DataBean>
    var topicAdapter:DynamicTopicAdapter? = null
    fun getTopicList() {
        HttpHelper.getInstance().getPidTopicList(pid,page,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showLoadComplete()
                val tempData = GsonUtil.GsonToBean(data, ListTopicData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            topicList.clear()
                            topicList.addAll(this)
                            topicAdapter = DynamicTopicAdapter(activity,topicList)
                            with(rvSearchResult) {
                                adapter = topicAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            topicAdapter?.onItemClickListener = OnSimpleItemListener {
                                index ->
                                val intent = Intent(activity, TopicDynamicActivity::class.java)
                                intent.putExtra("tid", topicList[index].tid)
                                intent.putExtra("topictitle", topicList[index].title)
                                intent.putExtra("topicState", pid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val tempSize = topicList.size
                            topicList.addAll(this)
                            topicAdapter?.notifyItemRangeInserted(tempSize,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                showLoadError(code != 3000,page == 0,msg)
            }
        })
    }


}