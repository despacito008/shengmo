package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.DynamicVideoAdapter
import com.aiwujie.shengmo.bean.DynamicListData
import com.aiwujie.shengmo.customview.CustomViewPage
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.decoration.SpaceItemDecoration
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent
import com.aiwujie.shengmo.eventbus.DynamicEvent
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicPlaza
import com.aiwujie.shengmo.kt.util.OnRefreshStateListener
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.videoplay.VideoActivity
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable

/**
 * 动态 - 关注
 */
class NewVideoDynamicFragment : NormalListFragment() {
    lateinit var dynamicList: ArrayList<DynamicListData.DataBean>
    var dynamicAdapter: DynamicVideoAdapter? = null
    private lateinit var frameLayout:FrameLayout


    companion object {
        fun newInstance(mode: String): NewVideoDynamicFragment {
            val args = Bundle()
            args.putString("mode",mode)
            val fragment = NewVideoDynamicFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        mode = arguments.getString("mode")!!
        getDynamicSx()
        EventBus.getDefault().register(this)
        frameLayout = rootView.findViewById(R.id.fl_normal_list)
        initArrowBtn()
        dynamicList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object: OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getVideoFragment()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getVideoFragment()
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
                if (dy > 0) {
                    (parentFragment as SquareDynamicFragment).hideTab()
                    showOrHideTopArrow(false)
                } else {
                    (parentFragment as SquareDynamicFragment).showTab()
                    showOrHideTopArrow(true)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == CustomViewPage.SCROLL_STATE_IDLE) {
                    // DES: 找出当前可视Item位置
                    val layoutManager: RecyclerView.LayoutManager = rvSearchResult.getLayoutManager()
                    if (layoutManager is GridLayoutManager) {
//                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                            showOrHideTopArrow(false)
//                        } else {
//                            showOrHideTopArrow(true)
//                        }
                    }
                }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun loadData() {
        gLoadHolder.showLoading()
        getVideoFragment()
    }
    var mode = ""
    var sex = ""
    var sexual = ""
    var lastid = ""
    var page = 0
    var itemDecoration:RecyclerView.ItemDecoration? = null
    private fun getVideoFragment() {
        HttpHelper.getInstance().getShortVideoList(mode,sex,sexual,lastid,page, object : HttpCodeMsgListener {
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
                            dynamicAdapter = DynamicVideoAdapter(activity, dynamicList,"7")
                            var headerAdapter = HeaderViewAdapter(dynamicAdapter)
                            headerAdapter.addHeaderView(getHeaderView())
                            headerAdapter.addHeaderView(getHeaderView())
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                //layoutManager = HeaderViewGridLayoutManager(activity,2,headerAdapter)
                                layoutManager = GridLayoutManager(activity,2)
                                if (itemDecoration == null) {
                                    itemDecoration = GridItemDecoration(20)
                                    addItemDecoration(itemDecoration)
                                }
                            }
                            dynamicAdapter?.setOnItemClickListener(DynamicVideoAdapter.OnItemClickListener { index ->
                                val position = index - 2
                                val intent = Intent(context, VideoActivity::class.java)
                                val bundle = Bundle()
                                bundle.putSerializable("videoList", dynamicList as Serializable?)
                                bundle.putString("type", "7")
                                bundle.putString("page", page.toString() + "")
                                bundle.putString("currentPosition", position.toString() + "")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            })
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

    private fun getDynamicSx() {
        sex = SharedPreferencesUtils.getParam(activity.applicationContext, "dynamicSex", "") as String
        sexual = SharedPreferencesUtils.getParam(activity.applicationContext, "dynamicSexual", "") as String
    }

    fun refreshMode(mode: String?) {
        this.mode = mode!!
        page = 0
        refreshLayout.autoRefresh(100)
    }

    fun getHeaderView(): View? {
        return LayoutInflater.from(activity).inflate(R.layout.layout_header_fragment_hot, null)
    }
}