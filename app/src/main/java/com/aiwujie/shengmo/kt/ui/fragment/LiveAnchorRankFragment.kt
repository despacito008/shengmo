package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.LiveRankBean
import com.aiwujie.shengmo.fragment.BaseFragment
import com.aiwujie.shengmo.kt.adapter.LiveAnchorRankAdapter
import com.aiwujie.shengmo.kt.adapter.LiveAudienceRankAdapter
import com.aiwujie.shengmo.kt.listener.OnLiveRankItemListener
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 直播排行榜 - 主播榜
 */
class LiveAnchorRankFragment : LazyFragment() {
    lateinit var smartRefreshRank: SmartRefreshLayout;
    lateinit var rvRank: RecyclerView;

    //lateinit var llEmpty: LinearLayout
    lateinit var gLoadingHolder: Gloading.Holder
    override fun getContentViewId(): Int {
        return R.layout.app_fragment_live_ranking
    }

    companion object {
        private const val TYPE = "type"
        fun newInstance(aType: Int) = LiveAnchorRankFragment().apply {
            arguments = Bundle(2).apply {
                putInt(TYPE, aType)
            }
        }
    }


    var type = 0
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments.getInt("type")
        //llEmpty.visibility = View.GONE
        rankList = ArrayList()
        // getRankingData()
        //EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //EventBus.getDefault().unregister(this)
    }

    override fun loadData() {
        gLoadingHolder.showLoading()
        getRankingData()
    }

    override fun initView(rootView: View) {
        smartRefreshRank = rootView.findViewById(R.id.smart_fragment_refresh_live_ranking)
        rvRank = rootView.findViewById(R.id.rv_fragment_live_ranking)
        gLoadingHolder = Gloading.getDefault().wrap(rvRank)
        // llEmpty = rootView.findViewById(R.id.layout_normal_empty)
        initListener()
    }

    fun initListener() {
        smartRefreshRank.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++;
                getRankingData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0;
                getRankingData()
            }
        })
    }

    var page = 0;
    var adapter: LiveAnchorRankAdapter? = null
    lateinit var rankList: ArrayList<LiveRankBean.DataBean>
    private fun getRankingData() {
        HttpHelper.getInstance().getLiveAnchorRankList(type, page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                smartRefreshRank.setNoMoreData(false)
                gLoadingHolder.showLoadSuccess()
                //llEmpty.visibility = View.GONE
                var liveRankBean = GsonUtil.GsonToBean(data, LiveRankBean::class.java)
                activity?.let {
                    liveRankBean?.data?.let {
                        when (page) {
                            0 -> {
                                smartRefreshRank.finishRefresh()
                                rankList.clear()
                                rankList.addAll(it)
                                adapter = LiveAnchorRankAdapter(activity, rankList, type,1)
                                var layoutManager = LinearLayoutManager(activity)
                                rvRank.adapter = adapter
                                rvRank.layoutManager = layoutManager
                                adapter?.rankItemLister = object : OnLiveRankItemListener {
                                    override fun doUserItemClick(index: Int) {
                                        gotoUserActivity(rankList[index].uid)
                                    }

                                    override fun donFavourItemClick(uid: String) {
                                        gotoUserActivity(uid)
                                    }

                                }
                            }
                            else -> {
                                smartRefreshRank.finishLoadMore()
                                var temp = rankList.size
                                rankList.addAll(it)
                                adapter?.notifyItemRangeInserted(temp, it.size)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(activity, msg)
                smartRefreshRank.finishRefresh()
                if (code == 4000) {
                    smartRefreshRank.finishLoadMoreWithNoMoreData()
                }
                if (page == 0) {
                    // llEmpty.visibility = View.VISIBLE
                    gLoadingHolder.showEmpty()
                }
            }

        })
    }

    fun gotoUserActivity(uid: String) {
        var intent = Intent(activity, UserInfoActivity::class.java)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event:String) {
//        LogUtil.d("接收到消息 " + event)
//    }
}