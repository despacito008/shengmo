package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity
import com.aiwujie.shengmo.adapter.PushTopUsAdapter
import com.aiwujie.shengmo.adapter.RewardUsAdapter
import com.aiwujie.shengmo.bean.AllDsData
import com.aiwujie.shengmo.bean.DynamicDetailBean
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 消息 - 评论 - 打赏过我的
 */
class RewardUsActivity : NormalListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rewardList = ArrayList()
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
        return "打赏过我的"
    }

    lateinit var gLoadingHolder: Gloading.Holder
    var page = 0
    var rewardAdapter:RewardUsAdapter? = null
    lateinit var rewardList:ArrayList<AllDsData.DataBean>
    override fun loadData() {
        HttpHelper.getInstance().getRewardUsData(page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, AllDsData::class.java)
                tempData?.data?.let {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadingHolder.showLoadSuccess()
                    when (page) {
                        0 -> {
                            rewardList.clear()
                            rewardList.addAll(it)
                            rewardAdapter = RewardUsAdapter(this@RewardUsActivity,rewardList)
                            rvList.adapter = rewardAdapter
                            rvList.layoutManager = LinearLayoutManager(this@RewardUsActivity)
                            rewardAdapter?.setOnSimpleItemListener {
                                index ->
                                val intent = Intent(this@RewardUsActivity,NewDynamicDetailActivity::class.java)
                                intent.putExtra("did",rewardList[index].did)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val temp = rewardList.size
                            rewardList.addAll(it)
                            rewardAdapter?.notifyItemRangeInserted(temp,it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 3000 && page == 0) {
                    gLoadingHolder.showEmpty()
                }
            }
        })
    }
}