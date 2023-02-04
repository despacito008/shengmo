package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveLogBean
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.LiveLogListAdapter
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 直播 - 最新 - 回放
 */
class LivePlayBackListFragment:NormalListFragment() {
    override fun loadData() {
        gLoadHolder.showLoading()
        sortType = when (SpKey.PLAYBACK_SORT.getSpValue(SpKey.VALUE_SORT_TIME)) {
            SpKey.VALUE_SORT_TIME -> 1
            SpKey.VALUE_SORT_WATCH -> 2
            SpKey.VALUE_SORT_BEANS -> 3
            else -> 1
        }
        getLiveLogList()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        frameLayout = rootView.findViewById(R.id.fl_normal_list)
        initArrowBtn()
        liveLogList = ArrayList()
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

    var page = 0
    lateinit var liveLogList:ArrayList<LiveLogBean.DataBean>
    var liveAdapter:LiveLogListAdapter? = null
    var itemDecoration:RecyclerView.ItemDecoration? = null
    var sortType = 1
    private fun getLiveLogList() {
        HttpHelper.getInstance().getPlayBackList(sortType,page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showLoadComplete()
                val tempData = GsonUtil.GsonToBean(data, LiveLogBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            liveLogList.clear()
                            liveLogList.addAll(this)
                            liveAdapter = LiveLogListAdapter(activity, liveLogList)
                            val headerAdapter = HeaderViewAdapter(liveAdapter)
                            headerAdapter.addHeaderView(getHeader())
                            headerAdapter.addHeaderView(getHeader())
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                //layoutManager = HeaderViewGridLayoutManager(activity, 2, headerAdapter)
                                layoutManager = GridLayoutManager(activity, 2)
                                if (itemDecoration == null) {
                                    itemDecoration = GridItemDecoration(20)
                                    addItemDecoration(itemDecoration)
                                }
                            }

                            liveAdapter?.setOnSimpleItemListener { index ->
                               // val intent = Intent(activity, PlayBackVideoActivity::class.java)
                                liveLogList[index].run {
                                    if (id.isNotEmpty() && uid.isNotEmpty()) {
//                                        intent.putExtra("id",id)
//                                        intent.putExtra("uid",uid)
//                                        intent.putExtra(IntentKey.TYPE,"1")
//                                        startActivity(intent)
                                        PlayBackVideoActivity.start(activity,id,uid)
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
               showLoadError(code != 4000,page == 0,msg)
            }
        })
    }

    private fun getHeader():View {
         return View.inflate(activity, R.layout.layout_header_fragment_playback, null)
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
            val mLayoutManager = rvSearchResult.layoutManager as LinearLayoutManager
            mLayoutManager.scrollToPositionWithOffset(0, 0)
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

    fun changeOrder(type:Int) {
        sortType = if (type == 0) 3 else type
        page = 0
        refreshLayout.autoRefresh(100)
    }

}