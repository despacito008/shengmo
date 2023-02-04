package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveLogBean
import com.aiwujie.shengmo.bean.PartitionTipBean
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.decoration.GridHeaderItemDecoration
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.LiveLogListAdapter
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter2
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 直播 - 最新 - 回放
 */
class LivePartitionPlayBackListFragment : LazyFragment() {

    private var tid: String? = null
    private var page = 0
    lateinit var liveLogList: ArrayList<LiveLogBean.DataBean>
    private var liveAdapter: LiveLogListAdapter? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private var tvTip: TextView? = null
    lateinit var partHeadView: View
    lateinit var gLoadHolder: Gloading.Holder
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var recycleview: RecyclerView
    var sortType = 1
    companion object {
        @JvmStatic
        var fragment: LivePartitionPlayBackListFragment? = null
        fun newInstance(tid: String? = null): LivePartitionPlayBackListFragment {
            val args = Bundle()
            args.putString("tid", tid)
//            if (fragment == null){
            fragment = LivePartitionPlayBackListFragment()
//            }
            fragment?.arguments = args
            return fragment as LivePartitionPlayBackListFragment
        }


    }

    fun changeOrder(type:Int) {
        sortType = if (type == 0) 3 else type
        page = 0
        refreshLayout.autoRefresh(100)
    }

    override fun loadData() {

        getLiveLogList()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_live_partition_online
    }

    override fun initView(rootView: View) {
        rootView.run {
            refreshLayout = findViewById(R.id.smartRefresh)
            recycleview = findViewById(R.id.recycleview)
            gLoadHolder = Gloading.getDefault().wrap(recycleview)

        }
        tid = arguments.getString("tid")
        liveLogList = ArrayList()
        initLivePartHeadView()
        getLivePartitionTip()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getLiveLogList()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getLiveLogList()
            }
        })
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

    private fun getLiveLogList() {
        HttpHelper.getInstance().getPlayBackListByTid(tid, page,sortType, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, LiveLogBean::class.java)
                if (tempData?.data == null && page == 0) {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadHolder.showEmpty()
                    return
                }
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (page == 0 && tempData?.data?.size == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoadSuccess()
                }
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            liveLogList.clear()
                            liveLogList.addAll(this)
                            liveAdapter = LiveLogListAdapter(activity, liveLogList)
                            val headerAdapter = HeaderViewAdapter(liveAdapter)
                            headerAdapter.addHeaderView(partHeadView)
                            with(recycleview) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(activity, 2, headerAdapter)
                                if (itemDecoration == null) {
                                    itemDecoration = GridHeaderItemDecoration(15, 1)
                                    addItemDecoration(itemDecoration)
                                }
                            }

                            liveAdapter?.setOnSimpleItemListener { index ->
                                liveLogList[index].run {
                                    if (id.isNotEmpty() && uid.isNotEmpty()) {
                                        PlayBackVideoActivity.start(activity, id, uid)
                                    }
                                }

                            }
                        }
                        else -> {
                            val tempIndex = liveLogList.size
                            liveLogList.addAll(this)
                            liveAdapter?.notifyItemRangeInserted(tempIndex, this.size)
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

    private fun initLivePartHeadView() {
        partHeadView = LayoutInflater.from(activity).inflate(R.layout.app_layout_live_part_header, null)
        var layoutParams :FrameLayout.LayoutParams= FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 120
        partHeadView.layoutParams = layoutParams
        tvTip = partHeadView.findViewById(R.id.tv_live_part_tips)
    }


}