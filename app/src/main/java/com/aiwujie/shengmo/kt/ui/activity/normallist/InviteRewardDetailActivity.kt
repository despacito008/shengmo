package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.adapter.InviteRewardRecyclerAdapter
import com.aiwujie.shengmo.bean.InviteRewardBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.statistical
 * @ClassName: InviteRewardDetailActivity
 * @Author: xmf
 * @CreateDate: 2022/4/9 9:24
 * @Description: 邀请奖励明细
 */
class InviteRewardDetailActivity:  NormalListActivity(){
    override fun getPageTitle(): String {
        return "奖励明细"
    }

    var page = 0
    override fun loadData() {
        loadingHolder.showLoading()
        getRewardData()
    }

    lateinit var rewardList:ArrayList<InviteRewardBean.DataBean>
    var inviteRewardAdapter:InviteRewardRecyclerAdapter? = null
    override fun initViewComplete() {
        super.initViewComplete()
        rewardList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getRewardData()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getRewardData()
            }
        })
    }

    private fun getRewardData() {
        HttpHelper.getInstance().getUserInviteRewardState(page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                loadingHolder.showLoadSuccess()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val inviteRewardBean = GsonUtil.GsonToBean(data, InviteRewardBean::class.java)
                inviteRewardBean?.data?.run {
                    when (page) {
                        0 -> {
                            rewardList.clear()
                            rewardList.addAll(this)
                            inviteRewardAdapter = InviteRewardRecyclerAdapter(this@InviteRewardDetailActivity,rewardList)
                            with(rvList) {
                                adapter = inviteRewardAdapter
                                layoutManager = LinearLayoutManager(this@InviteRewardDetailActivity)
                            }
                        }
                        else -> {
                            val index = rewardList.size
                            rewardList.addAll(this)
                            inviteRewardAdapter?.notifyItemRangeInserted(index,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                when(code) {
                    4001 -> {
                        if (page == 0) {
                            loadingHolder.showEmpty()
                        } else {
                            loadingHolder.showLoadSuccess()
                        }
                    }
                    else -> {
                        msg?.showToast()
                        loadingHolder.showLoadSuccess()
                    }
                }
            }
        })
    }
}
