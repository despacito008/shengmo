package com.aiwujie.shengmo.kt.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.BannerNewData
import com.aiwujie.shengmo.bean.HomeLiveLabelBean
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.kt.adapter.HomeLiveLabelAdapter
import com.aiwujie.shengmo.kt.adapter.RoomListAdapter
import com.aiwujie.shengmo.kt.adapter.`interface`.OnRoomItemClickListener
import com.aiwujie.shengmo.kt.ui.activity.LivePartitionActivity
import com.aiwujie.shengmo.kt.ui.activity.NewLivePartitionActivity
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity.Companion.start
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.bean.BannerLiveData
import com.aiwujie.shengmo.timlive.bean.HotRedBean
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.SafeCheckUtil
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.feezu.liuli.timeselector.Utils.TextUtil

class LiveRedHotFragment : NormalListFragment() {

    lateinit var headerView: View
    lateinit var adverBannerView: AdvertisementView
    private lateinit var redRecycleView: RecyclerView
    private lateinit var positionRecyclerView: RecyclerView
    private lateinit var tvRedList: TextView
    private lateinit var tvHotList: TextView
    var rvLivePartition: RecyclerView? = null
    private var page = 0
    lateinit var dataList: ArrayList<ScenesRoomInfoBean>
    lateinit var dataRedList: MutableList<ScenesRoomInfoBean>
    private var mRoomListAdapter: RoomListAdapter? = null
    private var mRedRoomListAdapter: RoomListAdapter? = null
    override fun loadData() {
        gLoadHolder.showLoading()
        getLiveRoomAndRed()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        dataList = ArrayList()
        dataRedList = ArrayList()
        initHeaderView()
        initRefreshLoad()
        getBannerData()
        getPartitionData()
    }

    private fun initRefreshLoad() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getLiveLogRoomInfo()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getLiveRoomAndRed()
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun initHeaderView() {
        headerView = LayoutInflater.from(context).inflate(R.layout.app_layout_live_hot_header, null)
        adverBannerView = headerView.findViewById(R.id.mMy_banner)
        redRecycleView = headerView.findViewById(R.id.rv_live_red)
        positionRecyclerView = headerView.findViewById(R.id.rv_live_partition)
        tvRedList = headerView.findViewById(R.id.tv_red_list)
        tvHotList = headerView.findViewById(R.id.tv_hot_list)
        rvLivePartition = headerView.findViewById(R.id.rv_live_partition)
    }

    private fun getBannerData() {
        HttpHelper.getInstance().getLiveBanner("3", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                adverBannerView.visibility = View.VISIBLE
                val tempData: BannerLiveData = GsonUtil.GsonToBean(data, BannerLiveData::class.java)
                val bannerModel = BannerNewData()
                val bannerList: ArrayList<BannerNewData.DataBean> = ArrayList()
                tempData.data?.run {
                    for (index in tempData.data.live_banner.indices) {
                        val bannerData: BannerNewData.DataBean = BannerNewData.DataBean()
                        bannerData.url = this.live_banner[index].url
                        bannerData.title = this.live_banner[index].title
                        bannerData.path = this.live_banner[index].path
                        bannerData.link_type = this.live_banner[index].link_type
                        bannerData.link_id = this.live_banner[index].link_id
                        bannerList.add(bannerData)
                    }
                    bannerModel.data = bannerList
                    adverBannerView.initData(bannerModel)
                }

            }

            override fun onFail(code: Int, msg: String?) {
                adverBannerView.visibility = View.GONE
            }
        })
    }

    private fun getLiveRoomAndRed() {
        HttpHelper.getInstance().getOnAirListNew(object : HttpCodeMsgListener {
            override fun onSuccess(data: String, msg: String) {
                showLoadComplete()
                val hotRedBean = Gson().fromJson(data, HotRedBean::class.java)
                hotRedBean?.data?.hotAnchor?.run {
                    when (page) {
                        0 -> {
                            if (size == 0) {
                                gLoadHolder.showEmpty()
                                tvHotList.visibility = View.GONE
                                return
                            }
                            tvHotList.visibility = View.VISIBLE
                            dataList.clear()
                            dataList.addAll(this)
                            mRoomListAdapter = RoomListAdapter(context, dataList, "hot")
                            val headerAdapter = HeaderViewAdapter(mRoomListAdapter)
                            headerAdapter.addHeaderView(headerView)
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(activity, 2, headerAdapter)
                            }
                            mRoomListAdapter?.onRoomItemClickListener= object : OnRoomItemClickListener {
                                override fun onRoomClickItem(possition: Int, model: ScenesRoomInfoBean) {
                                    val index = possition - 1 //减去头布局
                                    if (!TextUtil.isEmpty(model.live_log_id) && "0" != model.live_log_id) {
                                        start(activity, model.live_log_id, model.uid, "1")
                                    } else {
                                        if ("1" == model.is_live) { //直播中
                                            preEnterRoom2(index)
                                        } else { //已结束
                                            UserInfoActivity.start(activity, model.uid)
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            val tempIndex = dataList.size
                            dataList.addAll(this)
                            mRoomListAdapter?.notifyItemRangeInserted(tempIndex, dataList.size)

                        }
                    }
                }

                hotRedBean?.data?.sensationAnchor?.run {
                    when (page) {
                        0 -> {
                            if (size > 0) {
                                tvRedList.visibility = View.VISIBLE
                            } else {
                                tvRedList.visibility = View.GONE
                            }
                            dataRedList.clear()
                            dataRedList.addAll(this)
                            mRedRoomListAdapter = RoomListAdapter(context, dataRedList, "red")
                            with(redRecycleView) {
                                adapter = mRedRoomListAdapter
                                layoutManager = GridLayoutManager(activity, 3)
                            }
                            mRedRoomListAdapter?.onRoomItemClickListener= object : OnRoomItemClickListener {
                                override fun onRoomClickItem(possition: Int, model: ScenesRoomInfoBean) {
                                    if (!TextUtil.isEmpty(model.live_log_id) && "0" != model.live_log_id) {
                                        start(activity, model.live_log_id, model.uid, "1")
                                    } else {
                                        if ("1" == model.is_live) { //直播中
                                            //preEnterRoom2(possition)
                                            RoomManager.enterLiveRoom(activity,
                                                    dataRedList[possition].uid,
                                                    dataRedList[possition].room_id)
                                        } else { //已结束
                                            UserInfoActivity.start(activity, model.uid)
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            val tempIndex = dataRedList.size
                            dataRedList.addAll(this)
                            mRedRoomListAdapter?.notifyItemRangeInserted(tempIndex, dataRedList.size)

                        }
                    }
                }

            }

            override fun onFail(code: Int, msg: String) {
                showLoadError(code != 4000, page == 0, msg)
            }
        })
    }

    private fun getLiveLogRoomInfo() {
        HttpHelper.getInstance().getHotAnchorLiveLog(object : HttpCodeMsgListener {
            override fun onSuccess(data: String, msg: String) {
                showLoadComplete()
                val liveLogRoomInfoBean = GsonUtil.GsonToBean(data, SearchUserData::class.java)
                liveLogRoomInfoBean?.data?.run {
                    when (page) {
                        0 -> {
                            if (this.size == 0) {
                                gLoadHolder.showEmpty()
                                return
                            }
                            dataList.clear()
                            dataList.addAll(this)
                            mRoomListAdapter = RoomListAdapter(context, dataList, "hot")
                            val headerAdapter = HeaderViewAdapter(mRoomListAdapter)
                            headerAdapter.addHeaderView(headerView)
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(activity, 2, headerAdapter)
                            }
                        }
                        else -> {
                            val tempIndex = dataList.size
                            dataList.addAll(this)
                            mRoomListAdapter?.notifyItemRangeInserted(tempIndex, dataList.size)

                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String) {
                showLoadError(code != 4000, page == 0, msg)
            }
        })
    }

    private fun getPartitionData() {
        HttpHelper.getInstance().getHomePageLiveLabel(object : HttpCodeListener {
            override fun onSuccess(data: String) {
                if (SafeCheckUtil.isActivityFinish(activity)) {
                    return
                }
                val homeLiveLabelBean = GsonUtil.GsonToBean(data, HomeLiveLabelBean::class.java)
                val labelList = homeLiveLabelBean.data
                val labelAdapter = HomeLiveLabelAdapter(activity, labelList)
                val layoutManager = GridLayoutManager(activity, 5)
                rvLivePartition?.layoutManager = layoutManager
                rvLivePartition?.adapter = labelAdapter
                labelAdapter.itemListener = OnSimpleItemListener { position ->
//                    val intent = Intent(activity, LivePartitionActivity::class.java)
                    val intent = Intent(activity, NewLivePartitionActivity::class.java)
                    intent.putExtra("tid", labelList[position].tid)
                    intent.putExtra("part", labelList[position].name)
                    startActivity(intent)
                    //RoomManager.gotoQiNiuRoom(getActivity(),"402583","402583");
                }
            }

            override fun onFail(code: Int, msg: String) {}
        })
    }

    //只传入正在直播的主播
    fun preEnterRoom2(index: Int) {

        RoomManager.enterLiveRoom(activity,
                dataList[index].uid,
                dataList[index].room_id)
    }


}