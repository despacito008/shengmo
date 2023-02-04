package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.net.LiveHttpHelper
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener


class NewLiveFragment: NormalListFragment() {
    var page = 1
    override fun loadData() {
        gLoadHolder.showLoading()
        getNewLiveData()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        liveRoomList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getNewLiveData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 1
                getNewLiveData()
            }

        })
    }

    lateinit var liveRoomList:ArrayList<ScenesRoomInfoBean>
    var liveAdapter:RoomListAdapter? = null
    private fun getNewLiveData() {
         LiveHttpHelper.getInstance().newAnchorList(page,object :HttpListener {
             override fun onSuccess(data: String?) {
                 activity?.let {
                     val tempData = GsonUtil.GsonToBean(data,SearchUserData::class.java)
                     tempData?.data?.run {
                         refreshLayout.finishLoadMore()
                         refreshLayout.finishRefresh()
                         if (this.size == 0) {
                             if (page == 1) {
                                 gLoadHolder.showEmpty()
                                 return
                             }
                         }
                         gLoadHolder.showLoadSuccess()
                         when (page) {
                             1 -> {
                                 liveRoomList.clear()
                                 liveRoomList.addAll(this)
                                 liveAdapter = RoomListAdapter(activity,liveRoomList, RoomListAdapter.OnItemClickListener {
                                     _, roomInfo ->
                                     if (roomInfo.uid == MyApp.uid) {
                                         val intent = Intent(activity, UserInfoActivity::class.java)
                                         intent.putExtra("uid", roomInfo.uid)
                                         startActivity(intent)
                                     } else {
                                         // enterRoom(getActivity(),scenesRoomInfos,position,TAG);
                                         // RoomManager.enterRoom(getActivity(),roomInfo.getUid(),roomInfo.getRoom_id());
                                         // QNRoomManager.getInstance().gotoLiveRoom(getActivity(),roomInfo.getUid(),roomInfo.getRoom_id());
                                         RoomManager.enterLiveRoom(activity, roomInfo.uid, roomInfo.room_id)
                                     }
                                 })
                                 with(rvSearchResult) {
                                     adapter = liveAdapter
                                     layoutManager = GridLayoutManager(activity,2)
                                 }
                             }
                             else -> {
                                 val tempIndex = liveRoomList.size
                                 liveRoomList.addAll(this)
                                 liveAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                             }
                         }
                     }
                 }
             }

             override fun onFail(msg: String?) {
                 activity?.let {
                     refreshLayout.finishLoadMore()
                     refreshLayout.finishRefresh()
                     ToastUtil.show(activity,msg)
                 }
             }
         })
    }
}