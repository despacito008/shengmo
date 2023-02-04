package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.newui.TopicDynamicActivity
import com.aiwujie.shengmo.bean.ListTopicData
import com.aiwujie.shengmo.kt.adapter.DynamicTopicAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class HomePageTopicFragment:NormalListFragment() {

    companion object {
        fun newInstance(type: Int): HomePageTopicFragment{
            val args = Bundle()
            args.putInt("type",type)
            val fragment = HomePageTopicFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var type = 0
    var page = 0
    lateinit var topicList:ArrayList<ListTopicData.DataBean>
    override fun initView(rootView: View) {
        super.initView(rootView)
        type = arguments.getInt("type")
        topicList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getTopicList()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getTopicList()
            }
        })
    }
    override fun loadData() {
        gLoadHolder.showLoading()
        getTopicList()
    }
    var topicAdapter:DynamicTopicAdapter? = null
    private fun getTopicList() {
        getNormalTopic()
    }

    private fun getNormalTopic() {
        HttpHelper.getInstance().getHomepageTopicList(type,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showLoadComplete()
                val tempData = GsonUtil.GsonToBean(data,ListTopicData::class.java)
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
                                intent.putExtra("topicState", type)
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