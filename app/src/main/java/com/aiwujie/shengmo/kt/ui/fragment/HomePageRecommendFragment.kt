package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.activity.DynamicDetailActivity
import com.aiwujie.shengmo.activity.GroupSquareActivity
import com.aiwujie.shengmo.activity.TopicDetailActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.HomePageUserGridAdapter
import com.aiwujie.shengmo.adapter.HomePageUserListAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.*
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent
import com.aiwujie.shengmo.eventbus.MainPageTurnEvent
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent
import com.aiwujie.shengmo.kt.adapter.RecommendLivingAdapter
import com.aiwujie.shengmo.kt.ui.activity.MapActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.WarningRankListActivity
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.youth.banner.Banner
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener as OnRefreshLoadMoreListener1

class HomePageRecommendFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.app_fragment_homepage_recommend,container,false)
        initView(rootView!!)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

    }

    fun loadData() {
        gLoadHolder.showLoading()
        getUserList()
        getBanner()
        getLiveData()
        //ToastUtil.show(activity,"getData")
    }

    fun getContentViewId(): Int {
        return R.layout.app_fragment_homepage_recommend
    }
    lateinit var refreshLayout:SmartRefreshLayout
    lateinit var rvRecommend: RecyclerView
    lateinit var gLoadHolder:Gloading.Holder
    fun initView(rootView: View) {
        EventBus.getDefault().register(this)
        userList = ArrayList()
        refreshLayout = rootView.findViewById(R.id.refresh_layout_recommend)
        rvRecommend = rootView.findViewById(R.id.rv_fragment_recommend_user)
        initHeaderView()
        initScreenData()
        gLoadHolder = Gloading.getDefault().wrap(rvRecommend)
        val modle = SpKey.MODLE.getSpValue("0")
        layout = if (modle == "1") HomePageNearFragment.LayOutType.Grid else HomePageNearFragment.LayOutType.List
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener1 {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getUserList()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getUserList()
                getLiveData()
            }
        })

    }
    lateinit var headerView:View
    private fun initHeaderView() {
        headerView = View.inflate(activity, R.layout.item_home_header, null)
        initHeadView(headerView)
        initBannerView(headerView)
    }


    lateinit var bannerView: AdvertisementView
    private fun initBannerView(rootView: View) {
        bannerView = rootView.findViewById(R.id.ad_view_recommend) as AdvertisementView
    }
    lateinit var viewHeadConnection:View
    lateinit var viewHeadPlayBack:View
    lateinit var viewHeadGroup:View
    lateinit var viewHeadMap:View
    lateinit var viewHeadVideo:View
    lateinit var viewHeadTopic:View
    lateinit var viewHeadWarning:View
    lateinit var tvTabRecommend:TextView
    lateinit var tvTabRich:TextView
    lateinit var tvTabCharm:TextView
    lateinit var tvTabFans:TextView

    lateinit var llHomeLive:LinearLayout
    lateinit var llHomeLiveInfo:LinearLayout
    lateinit var rvHomeLive:RecyclerView
    private fun initHeadView(rootView: View) {
        viewHeadConnection = rootView.findViewById(R.id.view_connection)
        viewHeadPlayBack = rootView.findViewById(R.id.view_playback)
        viewHeadGroup = rootView.findViewById(R.id.view_group)
        viewHeadMap = rootView.findViewById(R.id.view_map)
        viewHeadVideo = rootView.findViewById(R.id.view_video)
        viewHeadTopic = rootView.findViewById(R.id.view_topics)
        viewHeadWarning = rootView.findViewById(R.id.view_punishment_list)
        tvTabRecommend = rootView.findViewById(R.id.tv_home_header_recommend)
        tvTabRich = rootView.findViewById(R.id.tv_home_header_wealth)
        tvTabCharm = rootView.findViewById(R.id.tv_home_header_charm)
        tvTabFans = rootView.findViewById(R.id.tv_home_header_fans)
        llHomeLive = rootView.findViewById(R.id.ll_home_living)
        llHomeLiveInfo = rootView.findViewById(R.id.ll_home_living_info)
        rvHomeLive = rootView.findViewById(R.id.rv_home_living)
        tvTabRecommend.setOnClickListener {
            if (type != 0) {
                changeTabTextView(tvTabRecommend)
                type = 0
                page = 0
                getUserList()
            }
        }
        tvTabRich.setOnClickListener{
            if (type != 5) {
                changeTabTextView(tvTabRich)
                type = 5
                page = 0
                getUserList()
            }
        }
        tvTabCharm.setOnClickListener{
            if (type != 6) {
                changeTabTextView(tvTabCharm)
                type = 6
                page = 0
                getUserList()
            }
        }
        tvTabFans.setOnClickListener{
            if (type != 2) {
                changeTabTextView(tvTabFans)
                type = 2
                page = 0
                getUserList()
            }
        }
        viewHeadConnection.setOnClickListener {
            EventBus.getDefault().post(MainPageTurnEvent(2, 0))
        }
        viewHeadPlayBack.setOnClickListener {
            EventBus.getDefault().post(MainPageTurnEvent(3, 1))
        }
        viewHeadGroup.setOnClickListener {
//            val intent = Intent(activity,GroupSquareActivity::class.java)
//            startActivity(intent)
            com.aiwujie.shengmo.kt.ui.activity.tabtopbar.GroupSquareActivity.startActivity(activity)
        }
        viewHeadMap.setOnClickListener {
//            val intent = Intent(activity, MapActivity::class.java)
            val intent = Intent(activity, MapActivity::class.java)
            intent.putExtra("mapflag", "1")
            startActivity(intent)
        }
        viewHeadTopic.setOnClickListener {
            EventBus.getDefault().post(MainPageTurnEvent(1, 3))
        }
        viewHeadVideo.setOnClickListener {
            EventBus.getDefault().post(MainPageTurnEvent(1, 1))
        }
        viewHeadWarning.setOnClickListener {
//            val intent = Intent(activity, WarningRankListActivity::class.java)
            val intent = Intent(activity, WarningRankListActivity::class.java)
            startActivity(intent)
        }
        llHomeLiveInfo.setOnClickListener {
            EventBus.getDefault().post(MainPageTurnEvent(3, 1))
        }
    }

    private fun changeTabTextView(tabTextView: TextView) {
        tvTabRecommend.setTextColor(resources.getColor(R.color.titleBlack))
        tvTabRich.setTextColor(resources.getColor(R.color.titleBlack))
        tvTabCharm.setTextColor(resources.getColor(R.color.titleBlack))
        tvTabFans.setTextColor(resources.getColor(R.color.titleBlack))
        tvTabRecommend.setBackgroundResource(R.drawable.bg_round_gray_home)
        tvTabRich.setBackgroundResource(R.drawable.bg_round_gray_home)
        tvTabCharm.setBackgroundResource(R.drawable.bg_round_gray_home)
        tvTabFans.setBackgroundResource(R.drawable.bg_round_gray_home)
        tabTextView.setTextColor(resources.getColor(R.color.white))
        tabTextView.setBackgroundResource(R.drawable.bg_round_purple_home)
    }

    private lateinit var userScreen: HomePageUserScreen
    private fun initScreenData() {
        val onlinestate = SharedPreferencesUtils.getParam(activity.applicationContext, "filterLine", "") as String
        val realname = SharedPreferencesUtils.getParam(activity.applicationContext, "filterAuthen", "") as String
        val age = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpAge", "") as String
        val sex = SharedPreferencesUtils.getParam(activity.applicationContext, "filterSex", "") as String
        val sexual = SharedPreferencesUtils.getParam(activity.applicationContext, "filterQx", "") as String
        val role = SharedPreferencesUtils.getParam(activity.applicationContext, "filterRole", "") as String
        val culture = SharedPreferencesUtils.getParam(activity.applicationContext, "filterCulture", "") as String
        val monthly = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpMoney", "") as String
        val upxzya = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpxzya", "") as String
        userScreen = HomePageUserScreen()
        with(userScreen) {
            this.age = age
            this.sex = sex
            this.sexual = sexual
            this.role = role
            salary = monthly
            auth = realname
            online = onlinestate
            edu = culture
            want = upxzya
        }
    }
    var type = 0
    var page = 0
    lateinit var userList:ArrayList<HomeNewListData.DataBean>
    var userAdapter:HomePageUserListAdapter? = null
    var gridAdapter:HomePageUserGridAdapter? = null
    var layout = HomePageNearFragment.LayOutType.List
    fun getUserList() {
        HttpHelper.getInstance().getHomepageUser(type,page,userScreen,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                gLoadHolder.showLoadSuccess()
                val tempIndex = GsonUtil.GsonToBean(data, HomeNewListData::class.java)
                tempIndex?.data?.run {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(this)
                            when (layout) {
                                HomePageNearFragment.LayOutType.List -> {
                                    userAdapter = HomePageUserListAdapter(activity,userList,type == 7)
                                    val headerAdapter = HeaderViewAdapter(userAdapter)
                                    headerAdapter.addHeaderView(headerView)
                                    with(rvRecommend) {
                                        adapter = headerAdapter
                                        layoutManager = HeaderViewGridLayoutManager(activity,1,headerAdapter)
                                    }
                                    userAdapter?.setOnSimpleItemListener {
                                        index ->
                                        gotoUserInfoPage(userList[index].uid)
                                    }
                                }
                                HomePageNearFragment.LayOutType.Grid -> {
                                    gridAdapter = HomePageUserGridAdapter(activity,userList)
                                    val headerAdapter = HeaderViewAdapter(gridAdapter)
                                    headerAdapter.addHeaderView(headerView)
                                    with(rvRecommend) {
                                        adapter = headerAdapter
                                        layoutManager = HeaderViewGridLayoutManager(activity,3,headerAdapter)
                                    }
                                    gridAdapter?.setOnSimpleItemListener {
                                        index ->
                                        gotoUserInfoPage(userList[index].uid)
                                    }
                                }
                            }

                        }
                        else -> {
                            val tempSize = userList.size
                            userList.addAll(this)
                            when (layout) {
                                HomePageNearFragment.LayOutType.List -> {
                                    userAdapter?.notifyItemRangeInserted(tempSize,this.size)
                                }
                                HomePageNearFragment.LayOutType.Grid -> {
                                    gridAdapter?.notifyItemRangeInserted(tempSize,this.size)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001 && page == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoadSuccess()
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: SharedprefrenceEvent) {
        with(userScreen) {
            this.age = event.age
            this.sex = event.sex
            this.sexual = event.sexual
            this.role = event.role
            salary = event.monthly
            auth = event.realname
            online = event.onlinestate
            edu = event.culture
            want = event.upxzya
        }
        page = 0
        getUserList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: ChangeLayoutEvent) {
        layout = if (event.falg == 1) {
            HomePageNearFragment.LayOutType.Grid
        } else {
            HomePageNearFragment.LayOutType.List
        }
        page = 0
        getUserList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(data: FilterData) {
        initScreenData()
        getUserList()
    }



    fun gotoUserInfoPage(uid:String) {
        UserInfoActivity.start(activity,uid)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun getBanner() {
        HttpHelper.getInstance().getBanner(1,object :HttpCodeListener {
            override fun onSuccess(data: String?) {
                //bannerLayout.visibility = View.VISIBLE
                val tempData = GsonUtil.GsonToBean(data,BannerNewData::class.java)
                tempData?.data?.run {
                    bannerView.initData(tempData)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                LogUtil.d("banner path hide ")
            }
        })
    }

    private fun getLiveData() {
        HttpHelper.getInstance().getRecommendLiveList(object :HttpCodeListener {
            override fun onSuccess(data: String?) {
                val tempData = GsonUtil.GsonToBean(data, SearchUserData::class.java)
                tempData?.data?.run {
                    if (this.size == 0) {
                        llHomeLive.visibility = View.GONE
                        return@run
                    } else {
                        llHomeLive.visibility = View.VISIBLE
                    }
                    val liveAdapter = RecommendLivingAdapter(activity,this)
                    with(rvHomeLive) {
                        adapter = liveAdapter
                        layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    }
                    liveAdapter.setOnSimpleItemListener(OnSimpleItemListener {
                        index ->
                        RoomManager.enterLiveRoom(activity, this[index].uid,this[index].room_id)
                    })
                }
            }

            override fun onFail(code: Int, msg: String?) {
                llHomeLive.visibility = View.GONE
            }
        })
    }
}