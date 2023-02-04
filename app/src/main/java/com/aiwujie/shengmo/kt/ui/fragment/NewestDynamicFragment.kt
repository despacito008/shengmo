package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter
import com.aiwujie.shengmo.bean.DynamicListData
import com.aiwujie.shengmo.customview.CustomViewPage
import com.aiwujie.shengmo.eventbus.*
import com.aiwujie.shengmo.kt.util.OnDynamicItemScrollVisibleListener
import com.aiwujie.shengmo.kt.util.OnRefreshStateListener
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 动态 - 最新
 */
class NewestDynamicFragment : NormalListFragment() {
    lateinit var dynamicList: ArrayList<DynamicListData.DataBean>
    var dynamicAdapter: DynamicRecyclerAdapter? = null
    private lateinit var frameLayout:FrameLayout
    override fun initView(rootView: View) {
        super.initView(rootView)
        EventBus.getDefault().register(this)
        frameLayout = rootView.findViewById(R.id.fl_normal_list)
        initArrowBtn()
        dynamicList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object: OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getNewestDynamic()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getNewestDynamic()
            }
        })
        refreshLayout.setOnMultiPurposeListener(object : OnRefreshStateListener() {
            override fun doPullStart() {
                (parentFragment as SquareDynamicFragment).hideTab()
            }

            override fun doPullEnd() {
                (parentFragment as SquareDynamicFragment).showTab()
            }
        })
        rvSearchResult.addOnScrollListener(object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < -20) { // 当前处于上滑状态
                    EventBus.getDefault().post(ViewIsVisibleEvent(1))
                } else if (dy > 20) { // 当前处于下滑状态
                    EventBus.getDefault().post(ViewIsVisibleEvent(0))
                }
                if (dy > 20) {
                    (parentFragment as SquareDynamicFragment).hideTab()
                    showOrHideTopArrow(false)
                } else if (dy < -20) {
                    (parentFragment as SquareDynamicFragment).showTab()
                    if (rvSearchResult.computeVerticalScrollOffset() > 500) {
                        showOrHideTopArrow(true)
                    }
                }
            }
        })

        rvSearchResult.addOnScrollListener(object :OnDynamicItemScrollVisibleListener(dynamicList,1) {
            override fun onItemScrollVisible(index: Int) {
                val trueIndex = index - 1
                dynamicAdapter?.tryToPlayVideo(rvSearchResult,index,dynamicList[trueIndex].playUrl)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun loadData() {
        gLoadHolder.showLoading()
        getNewestDynamic()
    }

    var page = 0
    private fun getNewestDynamic() {
        HttpHelper.getInstance().getDynamicList("1", sex, sexual, "", lastId, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                gLoadHolder.showLoadSuccess()
                val tempData = GsonUtil.GsonToBean(data, DynamicListData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            dynamicList.clear()
                            dynamicList.addAll(this)
                            dynamicAdapter = DynamicRecyclerAdapter(activity, dynamicList, tempData.retcode, "1")
                            val headerAdapter = HeaderViewAdapter(dynamicAdapter)
                            headerAdapter.addHeaderView(getHeaderView())
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                        }
                        else -> {
                            val tempSize = dynamicList.size
                            dynamicList.addAll(this)
                            dynamicAdapter?.notifyItemRangeInserted(tempSize, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001 || code == 4343 || code == 4344) {
                    if (page == 0) {
                        gLoadHolder.showEmpty()
                    } else {
                        gLoadHolder.showLoadSuccess()
                    }
                } else {
                    gLoadHolder.showLoadSuccess()
                    ToastUtil.show(activity, msg)
                }
            }
        })
    }

    lateinit var topBtn:ImageButton
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: DynamicEvent) {
        if (dynamicList.isNotEmpty()) {
            dynamicList[event.position].laudstate = event.laudstate.toString() + ""
            dynamicList[event.position].laudnum = event.zancount.toString() + ""
            dynamicAdapter?.notifyItemChanged(event.position)
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: DynamicCommentEvent) {
        if (dynamicList.isNotEmpty()) {
            dynamicList[event.position].comnum = event.commentcount.toString() + ""
            dynamicAdapter?.notifyItemChanged(event.position)
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: DynamicRewardEvent) {
        if (dynamicList.isNotEmpty()) {
            dynamicList[event.position].rewardnum = event.rewardcount.toString() + ""
            dynamicAdapter?.notifyItemChanged(event.position)
        }
    }

    var sex = ""
    var sexual = ""
    var lastId = ""
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(data: DynamicSxEvent) {
        page = 0
        sex = data.sex
        sexual = data.sexual
        lastId = ""
        getNewestDynamic()
    }

    fun getHeaderView(): View? {
        return LayoutInflater.from(activity).inflate(R.layout.layout_header_fragment_hot, null)
    }
}