package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity
import com.aiwujie.shengmo.adapter.PushTopUsAdapter
import com.aiwujie.shengmo.adapter.ThumbUpUsAdapter
import com.aiwujie.shengmo.bean.DynamicDetailBean
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 消息 - 评论 - 赞过我的
 */
class ThumbUpUsActivity : NormalListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pushTopList = ArrayList()
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
        return "赞过我的"
    }

    lateinit var gLoadingHolder: Gloading.Holder
    var page = 0
    var pushTopAdapter: ThumbUpUsAdapter? = null
    lateinit var pushTopList:ArrayList<DynamicDetailBean.DataBean>
    override fun loadData() {
        HttpHelper.getInstance().getThumbUpUsData(page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                var tempData = GsonUtil.GsonToBean(data, DynamicDetailBean::class.java)
                tempData?.data?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadingHolder.showLoadSuccess()
                    when (page) {
                        0 -> {
                            pushTopList.clear()
                            pushTopList.addAll(it)
                            pushTopAdapter = ThumbUpUsAdapter(this@ThumbUpUsActivity,pushTopList)
                            rvList.adapter = pushTopAdapter
                            rvList.layoutManager = LinearLayoutManager(this@ThumbUpUsActivity)
                            pushTopAdapter?.setOnSimpleItemListener {
                                var intent = Intent(this@ThumbUpUsActivity,NewDynamicDetailActivity::class.java)
                                intent.putExtra("did",pushTopList[it].did)
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

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4002 && page == 0) {
                    gLoadingHolder.showEmpty()
                }
            }
        })
    }
}