package com.aiwujie.shengmo.timlive.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.kt.adapter.LiveUserAdapter
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.ToastUtil
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class LiveUserFollowFragment : NormalListFragment() {

    var page = 1
    override fun loadData() {
        gLoadHolder.showLoading()
        getFollowData()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        followList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getFollowData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 1
                getFollowData()
            }
        })
    }

    lateinit var followList: ArrayList<ScenesRoomInfoBean>
    var followAdapter: LiveUserAdapter? = null
    private fun getFollowData() {
        HttpHelper.getInstance().getFollowAnchor(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, SearchUserData::class.java)
                tempData?.data?.run {
                    if (size > 0) {
                        showLoadComplete()
                    } else {
                        showLoadError(false, page == 1, "没有更多")
                        return@run
                    }
                    when (page) {
                        1 -> {
                            followList.clear()
                            followList.addAll(this)
                            followAdapter = LiveUserAdapter(activity, followList)
                            with(rvSearchResult) {
                                adapter = followAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            followAdapter?.setOnSimpleItemListener(
                                    OnSimpleItemListener { index ->
                                        if (followList[index].is_live == "1") {
                                            val roomInfo: ArrayList<ScenesRoomInfoBean> = ArrayList()
                                            roomInfo.add(followList[index])
                                            //RoomManager.enterRoom(activity,roomInfo,0,"")
                                            //QNRoomManager.getInstance().gotoLiveRoom(activity,followData[it].uid,followData[it].room_id)
                                            RoomManager.enterLiveRoom(activity, followList[index].uid, followList[index].room_id)
                                        } else {
                                            val intent = Intent(activity, UserInfoActivity::class.java)
                                            intent.putExtra("uid", followList[index].uid)
                                            startActivity(intent)
                                        }
                                    })
                        }
                        else -> {
                            val tempIndex = followList.size
                            followList.addAll(this)
                            followAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                showLoadComplete()
            }
        })
    }
}