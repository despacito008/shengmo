package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Context
import android.content.Intent
import com.aiwujie.shengmo.activity.newui.TopicDynamicActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.ListTopicData
import com.aiwujie.shengmo.kt.adapter.DynamicTopicAdapter
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.normallist
 * @ClassName: UserJoinTopicActivity
 * @Author: xmf
 * @CreateDate: 2022/4/14 16:53
 * @Description:
 */
class UserJoinTopicActivity: NormalListActivity() {

    companion object {
        fun start(context:Context,uid:String) {
            val intent = Intent(context,UserJoinTopicActivity::class.java)
            intent.putExtra(IntentKey.UID,uid)
            context.startActivity(intent)
        }
    }

    override fun getPageTitle(): String {
        return if (MyApp.uid == intent.getStringExtra(IntentKey.UID)) "我的话题" else "ta的话题"
    }

    override fun loadData() {
        loadingHolder.showLoading()
        getNormalTopic()
    }

    override fun initViewComplete() {
        super.initViewComplete()
        topicList = ArrayList()
        uid = intent.getStringExtra(IntentKey.UID)?:MyApp.uid
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getNormalTopic()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getNormalTopic()
            }
        })
    }

    var page = 0
    var uid = ""
    lateinit var topicList:ArrayList<ListTopicData.DataBean>
    var topicAdapter:DynamicTopicAdapter? = null
    private fun getNormalTopic() {
        HttpHelper.getInstance().getJoinTopicList(uid,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                loadingHolder.showLoadSuccess()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, ListTopicData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            topicList.clear()
                            topicList.addAll(this)
                            topicAdapter = DynamicTopicAdapter(this@UserJoinTopicActivity,topicList)
                            with(rvList) {
                                adapter = topicAdapter
                                layoutManager = android.support.v7.widget.LinearLayoutManager(this@UserJoinTopicActivity)
                            }
                            topicAdapter?.onItemClickListener = OnSimpleItemListener {
                                index ->
                                val intent = Intent(this@UserJoinTopicActivity, TopicDynamicActivity::class.java)
                                intent.putExtra("tid", topicList[index].tid)
                                intent.putExtra("topictitle", topicList[index].title)
                                intent.putExtra("topicState", 9)
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
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 3000 && page == 0) {
                    loadingHolder.showEmpty()
                } else {
                    loadingHolder.showLoadSuccess()
                }
            }
        })
    }
}
