package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.PartitionTipBean
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.decoration.GridHeaderItemDecoration
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter2
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.gloading.GlobalLoadingStatusView
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.liteav.login.ProfileManager

/**
 * @program: newshengmo
 * @description: 分类 - 直播
 * @author: whl
 * @create: 2022-05-24 14:19
 **/
class LivePartitionOnlineFragment : LazyFragment() {
    private var tid: String? = null
    private var page = 0
    lateinit var anchorList: ArrayList<ScenesRoomInfoBean>
    var anchorAdapter: RoomListAdapter2? = null
    var headAdapter: HeaderViewAdapter? = null
    var itemDecoration: RecyclerView.ItemDecoration? = null
    var tvTip: TextView? = null
    lateinit var partHeadView: View
    lateinit var gLoadHolder: Gloading.Holder
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var recycleview: RecyclerView

    companion object {
        var fragment: LivePartitionOnlineFragment? = null
        fun newInstance(tid: String?): LivePartitionOnlineFragment {
            val args = Bundle()
            args.putString("tid", tid)
//            if (fragment == null) {
                fragment = LivePartitionOnlineFragment()
//            }
            fragment?.arguments = args
            return fragment as LivePartitionOnlineFragment
        }
    }


    private fun initLivePartHeadView() {
        partHeadView = LayoutInflater.from(activity).inflate(R.layout.app_layout_live_part_header, null)
        var layoutParams : FrameLayout.LayoutParams= FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 120
        partHeadView.layoutParams = layoutParams
        tvTip = partHeadView.findViewById(R.id.tv_live_part_tips)
    }


    override fun initView(rootView: View) {
        rootView.run {
            refreshLayout = findViewById(R.id.smartRefresh)
            recycleview = findViewById(R.id.recycleview)
            gLoadHolder = Gloading.getDefault().wrap(recycleview)

        }
        tid = arguments.getString("tid")
        anchorList = ArrayList()
        initLivePartHeadView()
        getLivePartitionTip()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getLiveData()

            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getLiveData()
            }
        })
    }

    override fun loadData() {
        getLiveData()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_live_partition_online
    }

    private fun getLivePartitionTip() {
        HttpHelper.getInstance().getLabelIntroduce(tid, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val tipBean = GsonUtil.GsonToBean(data, PartitionTipBean::class.java)
                tipBean?.data?.let {
                    // tvPartition.text = it.introduce
                    //tvPartition.visibility = if (TextUtils.isEmpty(it.introduce)) View.GONE else View.VISIBLE
                    tvTip?.text = it.introduce
                    tvTip?.visibility = if (TextUtils.isEmpty(it.introduce)) View.INVISIBLE else View.VISIBLE
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(activity, msg)
            }
        })
    }

    var onItemClickListener = RoomListAdapter2.OnItemClickListener { position, roomInfo ->
        var index = position
        if (position > 0) {
            index--
        }
        val selfUserId = ProfileManager.getInstance().userModel.userId
        if (roomInfo.uid != selfUserId && "1" == roomInfo.is_live) {
            RoomManager.enterLiveRoom(activity, roomInfo.uid, roomInfo.room_id)
        } else {
            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra("uid", roomInfo.uid)
            startActivity(intent)
        }
    }

    private fun getLiveData() {
        HttpHelper.getInstance().getLabelAnchorList(tid, page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val scenesRoomInfo = Gson().fromJson(data, SearchUserData::class.java)
                if (scenesRoomInfo?.data == null  && page == 0) {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadHolder.showEmpty()
                    return
                }
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (page == 0 && scenesRoomInfo?.data?.size == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoadSuccess()
                }
                scenesRoomInfo?.data?.let {
                    when (page) {
                        0 -> {
                            anchorList.clear()
                            anchorList.addAll(it)
                            anchorAdapter = RoomListAdapter2(activity, anchorList, onItemClickListener)
                            headAdapter = HeaderViewAdapter(anchorAdapter)
                            headAdapter?.addHeaderView(partHeadView)
                            with(recycleview) {
                                adapter = headAdapter
                                layoutManager = HeaderViewGridLayoutManager(activity, 2, headAdapter)
//                                layoutManager = GridLayoutManager(activity, 2)
                                if (itemDecoration == null) {
                                    itemDecoration = GridHeaderItemDecoration(15, 1)
                                    addItemDecoration(itemDecoration)
                                }
                            }
                        }

                        else -> {
                            val temp = anchorList.size
                            anchorList.addAll(it)
                            anchorAdapter?.notifyItemRangeInserted(temp, it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                if (code == 4000 && page == 0) {
                    gLoadHolder.showEmpty()
                    refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    msg?.showToast()
                }

            }
        })
    }


}