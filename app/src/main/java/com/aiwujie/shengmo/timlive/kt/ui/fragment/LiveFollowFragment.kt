package com.aiwujie.shengmo.timlive.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.net.LiveHttpHelper
import com.aiwujie.shengmo.qnlive.utils.QNRoomManager
import com.aiwujie.shengmo.timlive.kt.adapter.LiveUserAdapter
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class LiveFollowFragment : Fragment() {

    lateinit var smartRefreshFollow: SmartRefreshLayout
    lateinit var rvFollow: RecyclerView
    lateinit var noData: LinearLayout
    lateinit var tvNoData:TextView
    var page: Int = 1
    lateinit var followData: ArrayList<ScenesRoomInfoBean>
    lateinit var followAdapter: LiveUserAdapter
    var isLoading = false
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.app_frag_live_follow,container,false)
        smartRefreshFollow = view.findViewById(R.id.smart_refresh_live_follow)
        rvFollow = view.findViewById(R.id.rv_live_follow)
        noData = view.findViewById(R.id.no_data)
        tvNoData = view.findViewById(R.id.tv_anchor_no_data);
        tvNoData.text = getString(R.string.no_follow_list)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followData = ArrayList()
        followAdapter = LiveUserAdapter(activity,followData)
        val linearLayoutManager = LinearLayoutManager(activity)
        rvFollow.layoutManager = linearLayoutManager
        getFollowData()
        smartRefreshFollow.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
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

    private fun getFollowData() {
        if (isLoading) return
        isLoading = true
        LiveHttpHelper.getInstance().getFollowAnchor(page,object: HttpListener {
            override fun onSuccess(data: String?) {
                isLoading = false
                smartRefreshFollow.finishLoadMore()
                smartRefreshFollow.finishRefresh()
                val tempData = Gson().fromJson(data, SearchUserData::class.java)
                when(page) {
                    1    ->{
                        followData.clear()
                        followData.addAll(tempData.data)
                        if(followData != null && followData.size > 0){
                            noData.visibility = View.GONE
                            rvFollow.visibility = View.VISIBLE
                            rvFollow.adapter = followAdapter
                            followAdapter.setOnSimpleItemListener(OnSimpleItemListener {
                                if (followData[it].is_live == "1") {
                                    val roomInfo:ArrayList<ScenesRoomInfoBean> = ArrayList()
                                    roomInfo.add(followData[it])
                                    //RoomManager.enterRoom(activity,roomInfo,0,"")
                                    //QNRoomManager.getInstance().gotoLiveRoom(activity,followData[it].uid,followData[it].room_id)
                                    RoomManager.enterLiveRoom(activity,followData[it].uid,followData[it].room_id)
                                } else {
                                    var intent = Intent(activity, UserInfoActivity::class.java)
                                    intent.putExtra("uid",followData[it].uid)
                                    startActivity(intent)
                                }
                            })
                        }else{
                            noData.visibility = View.VISIBLE
                            rvFollow.visibility = View.GONE
                        }
                    }
                    else ->{
                        val temp = followData.size
                        followData.addAll(tempData.data)
                        followAdapter.notifyItemRangeInserted(temp,tempData.data.size)
                    }
                }
            }

            override fun onFail(msg: String?) {
                isLoading = false
                smartRefreshFollow.finishLoadMore()
                smartRefreshFollow.finishRefresh()
                noData.visibility = View.VISIBLE
                rvFollow.visibility = View.GONE
            }
        } )
    }
}