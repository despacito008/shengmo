package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.kt.util.OnRefreshStateListener
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 直播 - 最新 - 直播
 */
class LiveOnlineNewListFragment:NormalListFragment() {
    override fun loadData() {
        gLoadHolder.showLoading()
        getLiveOnlineList()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        frameLayout = rootView.findViewById(R.id.fl_normal_list)
        initArrowBtn()
        initHeaderView()
        liveRoomList = ArrayList()
        refreshLayout.setNoMoreData(true)
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 1
                getLiveOnlineList()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getLiveOnlineList()
            }
        })
        refreshLayout.setOnMultiPurposeListener(object : OnRefreshStateListener() {
            override fun doPullStart() {
                if (parentFragment is LiveNewFragment) {
                    (parentFragment as LiveNewFragment).hideTab()
                }
            }
            override fun doPullEnd() {
                if (parentFragment is LiveNewFragment) {
                    (parentFragment as LiveNewFragment).showTab()
                }
            }
        })
        rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 20) {
                    (parentFragment as LiveNewFragment).hideTab()
                    showOrHideTopArrow(false)
                } else if (dy < -20) {
                    (parentFragment as LiveNewFragment).showTab()
                    if (rvSearchResult.computeVerticalScrollOffset() > 500) {
                        showOrHideTopArrow(true)
                    }
                }
            }
        })
    }

    var page = 1
    lateinit var liveRoomList:ArrayList<ScenesRoomInfoBean>
    var liveAdapter: RoomListAdapter? = null
    private fun getLiveOnlineList() {
        HttpHelper.getInstance().newAnchorList(page, object : HttpCodeMsgListener {
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
                            liveRoomList.clear()
                            liveRoomList.addAll(this)
                            liveAdapter = RoomListAdapter(activity,liveRoomList) { _, roomInfo ->
                                if (roomInfo.uid == MyApp.uid) {
                                    val intent = Intent(activity, UserInfoActivity::class.java)
                                    intent.putExtra("uid", roomInfo.uid)
                                    startActivity(intent)
                                } else {
                                    RoomManager.enterLiveRoom(activity, roomInfo.uid, roomInfo.room_id)
                                }
                            }
                            val headerAdapter = HeaderViewAdapter(liveAdapter)
                            headerAdapter.addHeaderView(headerView)
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(activity, 2, headerAdapter)
                            }
                        }
                        else -> {
                            val tempIndex = liveRoomList.size
                            liveRoomList.addAll(this)
                            liveAdapter?.notifyItemRangeInserted(tempIndex, this.size)
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

    private lateinit var headerView:View
    private fun initHeaderView() {
        headerView = View.inflate(activity, R.layout.layout_header_fragment_hot, null)
    }

    private lateinit var topBtn: ImageButton
    private lateinit var frameLayout:FrameLayout
    private fun initArrowBtn() {
        topBtn = ImageButton(activity)
        val params = FrameLayout.LayoutParams(50.dp,50.dp)
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.bottomMargin = 10.dp
        params.rightMargin = 10.dp
        topBtn.setBackgroundResource(R.mipmap.dingbu)
        topBtn.layoutParams = params
        frameLayout.addView(topBtn)
        topBtn.setOnClickListener {
            topBtn.visibility = View.GONE
            rvSearchResult.scrollToPosition(0)
            if (rvSearchResult.layoutManager != null) {
                val mLayoutManager = rvSearchResult.layoutManager as LinearLayoutManager
                mLayoutManager.scrollToPositionWithOffset(0, 0)
            }
        }
    }

    private fun showOrHideTopArrow(isShow: Boolean) {
        if (isShow) {
            if (topBtn.visibility == View.GONE) {
                topBtn.visibility = View.VISIBLE
                topBtn.animation = AnimationUtil.moveToViewLocation()
            }
        } else {
            if (topBtn.visibility == View.VISIBLE) {
                topBtn.visibility = View.GONE
                topBtn.animation = AnimationUtil.moveToViewBottom()
            }
        }
    }

}