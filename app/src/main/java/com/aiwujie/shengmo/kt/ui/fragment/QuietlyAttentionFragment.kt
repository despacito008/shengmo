package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.view.View
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.UserRelationShipAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.GzFsHyListviewData
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class QuietlyAttentionFragment : NormalListFragment() {
    lateinit var userList: ArrayList<GzFsHyListviewData.DataBean>
    var userFansAdapter: UserRelationShipAdapter? = null
    override fun initView(rootView: View) {
        super.initView(rootView)
        userList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getAttentionData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getAttentionData()
            }
        })
    }

    var page = 0
    override fun loadData() {
        gLoadHolder.showLoading()
        getAttentionData()
    }

    private fun getAttentionData() {
        HttpHelper.getInstance().getQuietFollowUser(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, GzFsHyListviewData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(this)
                            userFansAdapter = UserRelationShipAdapter(activity, userList, "", false)
                            with(rvSearchResult) {
                                adapter = userFansAdapter
                                layoutManager = android.support.v7.widget.LinearLayoutManager(activity)
                            }
                            userFansAdapter?.setOnSimpleItemListener { index ->
                                val intent = Intent(activity, UserInfoActivity::class.java)
                                intent.putExtra("uid", userList[index].uid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val tempIndex = userList.size
                            userList.addAll(this)
                            userFansAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001) {
                    if (page == 0) {
                        gLoadHolder.showEmpty()
                    } else {
                        gLoadHolder.showLoadSuccess()
                    }
                } else {
                    gLoadHolder.showLoadSuccess()
                    ToastUtil.show(activity,msg)
                }
            }
        })
    }
}