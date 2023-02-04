package com.aiwujie.shengmo.kt.ui.activity

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.aiwujie.shengmo.BuildConfig
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.LoginActivity
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity
import com.aiwujie.shengmo.adapter.NormalPagerAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.*
import com.aiwujie.shengmo.eventbus.*
import com.aiwujie.shengmo.fragment.HomeMyFragment
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.bean.AttendanceStateBean
import com.aiwujie.shengmo.kt.bean.NormalResultBean
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageAroundFragment
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageDynamicFragment
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageLiveFragment
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageMessageFragment
import com.aiwujie.shengmo.kt.ui.view.DailyAttendancePop
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.service.AliveService
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.timlive.ui.LiveRoomAnchorActivity
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.view.NormalTipsPop
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.gyf.immersionbar.ImmersionBar
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.opensource.svgaplayer.SVGAParser
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import com.tencent.imsdk.v2.V2TIMValueCallback
import com.tencent.liteav.custom.Constents
import com.tencent.liteav.login.ProfileManager
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest
import com.tencent.qcloud.tim.tuikit.live.bean.OtherLiveRoomEvent
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: HomePageActivity
 * @Author: xmf
 * @CreateDate: 2022/4/25 10:54
 * @Description: 首页
 */
class HomePageActivity : BaseActivity() {

    private val vpMain: ViewPager? by lazy { findViewById<ViewPager>(R.id.vp_main) }

    private val tabMain: TabLayout? by lazy { findViewById<TabLayout>(R.id.tab_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_home_page)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        EventBus.getDefault().register(this)
        initViewPager()
        getMyOwnInfo()
        initHeartBeatListener()
        startAliveService()
        FinishActivityManager.getManager().addActivity(this)
        getChatState()
        getCacheUrlList()
    }

    var mFirstLoad = true
    override fun onResume() {
        super.onResume()
        if (mFirstLoad) {
            mFirstLoad = false
            getNoticeUnreadMessage()
            requestLocationPermission()
            checkTokenBeforeLoginIM()
            getSignTimesInWeeks()
            lastTime = SystemClock.currentThreadTimeMillis()
        }

    }

    override fun onStart() {
        super.onStart()

    }

    private fun checkTokenBeforeLoginIM() {
        HttpHelper.getInstance().getTimUserSign(object :HttpListener {
            override fun onSuccess(data: String?) {
                loginTencentIM()
            }

            override fun onFail(msg: String?) {
                loginTencentIM()
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        checkLiveRoomIntent()
        checkSignState()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        TUIKit.logout(object : IUIKitCallBack {
            override fun onSuccess(data: Any?) {}

            override fun onError(module: String?, errCode: Int, errMsg: String?) {}
        })
        stopAliveService()
        FinishActivityManager.getManager().finishActivity()
    }

    var onBackPressedTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val timeSpan = System.currentTimeMillis() - onBackPressedTime;
            onBackPressedTime = System.currentTimeMillis()
            LogUtil.d("time = " + timeSpan)
            if (timeSpan > 2000) {
                "再按一次退出程序".showToast()
            } else {
                if (Constents.isShowAudienceFloatWindow) {
                    EventBus.getDefault().post(StopLiveEvent())
                    vpMain?.postDelayed({
                        exitApp()
                    }, 1000)
                } else {
                    exitApp()
                }
            }
        }
        return false
    }

    //退出APP
    private fun exitApp() {
        TUIKit.logout(object : IUIKitCallBack {
            override fun onSuccess(data: Any?) {}
            override fun onError(module: String?, errCode: Int, errMsg: String?) {}
        })
        stopAliveService() 
        System.exit(0)
        Process.killProcess(Process.myPid())
        finish()
    }


    private val homePageMessageFragment: HomePageMessageFragment by lazy { HomePageMessageFragment() }
    fun initViewPager() {
        val fragmentList: List<Fragment> = arrayListOf(HomePageAroundFragment(), HomePageDynamicFragment(),
                homePageMessageFragment, HomePageLiveFragment(), HomeMyFragment())
        val mAdapter = NormalPagerAdapter(supportFragmentManager, fragmentList)
        vpMain?.adapter = mAdapter
        vpMain?.offscreenPageLimit = 5
        tabMain?.setupWithViewPager(vpMain)
        for (i in 0..6) {
            tabMain?.getTabAt(i)?.customView = getTabView(i)
        }
        tabMain?.addOnTabSelectedListenerDsl {
            onTabUnselected {
                it?.let { unChooseTab(it) }
            }
            onTabSelected {
                it?.let { chooseTab(it) }
            }
        }
    }

    fun getTabView(position: Int): View? {
        val view = LayoutInflater.from(this).inflate(R.layout.app_tab_home_page, null)
        val tvTabName = view.findViewById(R.id.tv_tab_home_page) as TextView
        val ivTab = view.findViewById(R.id.iv_tab_home_page) as ImageView
        when (position) {
            0 -> {
                ivTab.setImageResource(R.drawable.home_page_selector_arround)
                tvTabName.text = "身边"
            }
            1 -> {
                ivTab.setImageResource(R.drawable.home_page_selector_dynamic)
                tvTabName.text = "动态"
            }
            2 -> {
                ivTab.setImageResource(R.drawable.home_page_selector_message)
                tvTabName.text = "消息"
            }
            3 -> {
                ivTab.setImageResource(R.drawable.home_page_selector_live)
                tvTabName.text = "直播"
            }
            4 -> {
                ivTab.setImageResource(R.drawable.home_page_selector_my)
                tvTabName.text = "我的"
            }
        }
        return view
    }

    fun chooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        view?.run {
            val tvTabName = findViewById<TextView>(R.id.tv_tab_home_page)
            val ivTab = findViewById<ImageView>(R.id.iv_tab_home_page)
            ivTab.isSelected = true
            tvTabName.isSelected = true
        }
    }

    fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        view?.run {
            val tvTabName = findViewById<TextView>(R.id.tv_tab_home_page)
            val ivTab = findViewById<ImageView>(R.id.iv_tab_home_page)
            ivTab.isSelected = false
            tvTabName.isSelected = false
        }
    }

    //获取用户vip状态
    private fun getMyOwnInfo() {
        HttpHelper.getInstance().getMyOwnInfo(object : HttpListener {
            override fun onSuccess(data: String?) {
                val vipData = GsonUtil.GsonToBean(data, VipAndVolunteerData::class.java)
                vipData?.data?.run {
                    MyApp.isAdmin = is_admin
                    SpKey.VIP.saveSpValue(vip)
                    SpKey.SVIP.saveSpValue(svip)
                    SpKey.ADMIN.saveSpValue(is_admin)
                    if (intent.getIntExtra("gotoByRegister", -1) == -1) {
                        if (vip != "1") {
                            MyApp.clearScreen()
                        }
                    } else {
                        getNewUserInviteState()
                    }
                }
            }

            override fun onFail(msg: String?) {

            }
        })
    }

    //获取用户邀请状态
    private fun getNewUserInviteState() {
        HttpHelper.getInstance().getNewUserInviteState(object : HttpCodeListener {
            override fun onSuccess(data: String?) {

            }

            override fun onFail(code: Int, msg: String?) {
                if (code == 4001) {
                    vpMain?.postDelayed({
                        showInviteTipsPop(msg ?: "绑定手机后可领取5天svip")
                    }, 10000)
                }
            }
        })
    }

    //邀请提示框
    private fun showInviteTipsPop(msg: String) {
        val pop = NormalTipsPop.Builder(this)
                .setTitle("领取SVIP")
                .setInfo(msg)
                .setCancelStr("取消")
                .setConfirmStr("去绑定")
                .build()
        pop.showPopupWindow()
        pop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
            override fun cancelClick() {
                pop.dismiss()
            }

            override fun confirmClick() {
                pop.dismiss()
                val intent = Intent(this@HomePageActivity, BindingMobileActivity::class.java)
                intent.putExtra("neworchange", "new")
                startActivity(intent)
            }
        })
        pop.setOutSideDismiss(false)
    }

    //初始化浮窗心跳监听
    private fun initHeartBeatListener() {
        FloatWindowLayout.getInstance().setOnHeartBeatReport {
            HttpHelper.getInstance().reportHeartBeat(it)
        }
    }

    //开启保活
    private fun startAliveService() {
        startService(Intent(this, AliveService::class.java))
    }

    //关闭保活
    private fun stopAliveService() {
        stopService(Intent(this, AliveService::class.java))
    }

    //获取禁言状态
    private fun getChatState() {
        HttpHelper.getInstance().getBanUserStatus(MyApp.uid, object : HttpListener {
            override fun onSuccess(data: String?) {
                val stateBean = GsonUtil.GsonToBean(data, AllUserStates::class.java)
                stateBean?.data?.run {
                    "nospeak".saveSpValue(chatstatus)
                }
            }

            override fun onFail(msg: String?) {}

        })
    }

    //获取礼物大动画
    private fun getCacheUrlList() {
        var svgaUrl = SpKey.SVGA_KEY.getSpValue("")
        val finalSvgaUrl = svgaUrl
        HttpHelper.getInstance().getLargeGift(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempList: MutableList<String> = LinkedList()
                val largeSvgaBean = GsonUtil.GsonToBean(data, LargeSvgaBean::class.java)
                largeSvgaBean?.data?.run {
                    if (size > 0) {
                        this.forEach {
                            if (!finalSvgaUrl.contains(it.gift_svgaurl)) {
                                tempList.add(it.gift_svgaurl)
                            }
                        }
                        cacheSVGAImage(tempList)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    //缓存动画
    private fun cacheSVGAImage(urlList: List<String>) {
        val sucList: MutableList<String> = LinkedList()
        for (i in urlList.indices) {
            try {
                SVGAParser.shareParser().fileDownloader.resume(URL(urlList[i]), { inputStream: InputStream? ->
                    sucList.add(urlList[i])
                    if (sucList.size == urlList.size) {
                        doCacheComplete(sucList)
                    }
                }) { e: Exception? ->
                    sucList.add("")
                    if (sucList.size == urlList.size) {
                        doCacheComplete(sucList)
                    }
                }
            } catch (e: Exception) {
                sucList.add("")
                if (sucList.size == urlList.size) {
                    doCacheComplete(sucList)
                }
            }
        }
    }

    //缓存动画成功
    private fun doCacheComplete(urlList: List<String>) {
        val stringBuilder = StringBuilder()
        for (url in urlList) {
            if (!TextUtil.isEmpty(url)) {
                stringBuilder.append(url).append(",")
            }
        }
        SpKey.SVGA_KEY.saveSpValue(stringBuilder.toString())
    }

    //获取未读消息小红点
    private fun getNoticeUnreadMessage() {
        HttpHelper.getInstance().getNoticeUnreadMessage(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val unreadMessageBean = GsonUtil.GsonToBean(data, NoticeUnreadMessageBean::class.java)
                unreadMessageBean?.data?.run {
                    if (other.newRegerNum != 0) {
                        EventBus.getDefault().post(RedPointEvent(other.newRegerNum, 1))
                    }
                    SpKey.RECOMMEND_NUM.saveSpValue(other.dyRecommendNum)
                    if (other.dyRecommendNum != 0) {
                        EventBus.getDefault().postSticky(RedPointEvent(other.dyRecommendNum, 6))
                    }
                    SpKey.PUSH_TOP_NUM.saveSpValue(other.dyTopNum)
                    if (other.dyTopNum != 0) {
                        EventBus.getDefault().postSticky(RedPointEvent(other.dyTopNum, 7))
                    }
                    if (other.alldynum_nolaud != 0) {
                        EventBus.getDefault().post(MessageEvent(other.alldynum_nolaud, 1))
                        homePageMessageFragment.showCommentRedDot(other.alldynum_nolaud)
                    }
                    bigHorn?.run {
                        "giftnum".saveSpValue(giftnum.toString())
                        "topcardnum".saveSpValue(topcardnum.toString())
                        "vipnum".saveSpValue(vipnum.toString())
                        "redbagnum".saveSpValue(redbagnum.toString())
                        "labaallnum".saveSpValue(allnum)
                        "labaState".saveSpValue("1")
                        EventBus.getDefault().post(BigHornEvent(1))
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }

        })
    }

    //请求定位权限
    private fun requestLocationPermission() {
        XXPermissions.with(this)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            updateLocation()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        if (!never) {
                            "如果您需要定位请开启此权限...".showToast()
                        }
                    }
                })
    }

    //定位
    private fun updateLocation() {
        val weakClient = WeakReference<AMapLocationClient>(AMapLocationClient(applicationContext))
        val weakListener = WeakReference<LocationListener>(LocationListener(applicationContext, weakClient))
        val locationOption = AMapLocationClientOption()
        weakClient.get()?.setLocationListener(weakListener.get())
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        locationOption.isOnceLocation = true
        locationOption.isOnceLocationLatest = true
        weakClient.get()?.setLocationOption(locationOption)
        weakClient.get()?.startLocation()
        vpMain?.postDelayed({ updateLoginTimeAndLocation() }, 5000)
    }

    //上报定位和登录时间
    private fun updateLoginTimeAndLocation() {
        HttpHelper.getInstance().updateLocationAndLastLoginTime()
    }

    //判断 签到没签到
    private fun getSignTimesInWeeks() {
        HttpHelper.getInstance().getSignState(object : HttpCodeMsgListener {
            override fun onSuccess(data: String, msg: String) {
                val result = data.toJsonBean<NormalResultBean<AttendanceStateBean>>()
                result?.data?.run {
                    if (signStatus == "0") {
                        val dailyAttendancePop = DailyAttendancePop(this@HomePageActivity)
                        dailyAttendancePop.showPopupWindow()
                    }
                }

            }

            override fun onFail(code: Int, msg: String) {}
        })
    }


    private fun loginTencentIM() {
        if (V2TIMManager.getInstance().loginStatus == V2TIMManager.V2TIM_STATUS_LOGOUT) {

            TUIKit.login(MyApp.uid, MyApp.token, object : IUIKitCallBack {
                override fun onSuccess(data: Any?) {
                    LogUtil.d("im登录成功")
                    initTIMUserInfo()
                    EventBus.getDefault().postSticky(TIMLoginEvent(true))
                }

                override fun onError(module: String, errCode: Int, errMsg: String) {
                    LogUtil.d("im登录失败 $errMsg")
                    getTimUserSign()
                }
            })
        }
        ConversationManagerKit.getInstance().addUnreadWatcher {
            //LogUtil.d("im未读消息" + it)
            EventBus.getDefault().post(MessageEvent(it))
        }
        //初始化一下 live包网络请求 需要的url和token
        HttpPostRequest.getInstance().initUrlAndToken(HttpUrl.NetPic(), SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""), BuildConfig.VERSION_NAME)
    }

    fun initTIMUserInfo() {
        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(MyApp.uid), object : V2TIMValueCallback<List<V2TIMUserFullInfo>> {
            override fun onError(code: Int, desc: String) {}
            override fun onSuccess(v2TIMUserFullInfos: List<V2TIMUserFullInfo>) {
                val userModel = ProfileManager.getInstance().userModel
                userModel.userAvatar = v2TIMUserFullInfos[0].faceUrl
                userModel.userName = v2TIMUserFullInfos[0].nickName
                userModel.userId = v2TIMUserFullInfos[0].userID
            }
        })
    }

    fun getTimUserSign() {
        HttpHelper.getInstance().getTimUserSign(object : HttpListener {
            override fun onSuccess(data: String) {
                LogUtil.d(data)
                val timUserSignBean = GsonUtil.GsonToBean(data, TimUserSignBean::class.java)
                if (timUserSignBean != null && timUserSignBean.data != null) {
                    val uid = timUserSignBean.data.uid
                    val sign = timUserSignBean.data.t_sign
                    if (!TextUtil.isEmpty(sign)) {
                        TUIKit.login(uid, sign, object : IUIKitCallBack {
                            override fun onSuccess(data: Any?) {
                                initTIMUserInfo()
                                EventBus.getDefault().post(TIMLoginEvent(true))
                            }

                            override fun onError(module: String, errCode: Int, errMsg: String) {
                                ToastUtil.show("连接聊天服务器失败,请重新登录")
                                SharedPreferencesUtils.setParam(applicationContext, "t_sign", "yes")
                                EventBus.getDefault().post(TokenFailureEvent())
                                finish()
                            }
                        })
                    } else {
                        ToastUtil.show("连接聊天服务器失败,请重新登录")
                        SharedPreferencesUtils.setParam(applicationContext, "t_sign", "yes")
                        EventBus.getDefault().post(TokenFailureEvent())
                        finish()
                    }
                }
            }

            override fun onFail(msg: String) {}
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: TokenFailureEvent) {
        MyApp.clearSharedPreference()
        ConversationManagerKit.getInstance().destroyConversation()
        TUIKit.logout(object : IUIKitCallBack {
            override fun onSuccess(data: Any?) {}
            override fun onError(module: String, errCode: Int, errMsg: String) {}
        })
        "您的账号登录已过期".showToast()
        val intent = Intent(MyApp.getInstance(), LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    var mMessageNum = 0
    var mNoticeNum = 0
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: MessageEvent) {
        if (event.type == 0) {
            mMessageNum = event.msgCount
        } else {
            mNoticeNum = event.msgCount
        }
        val tvRedDot by lazy { tabMain?.getTabAt(2)?.customView?.findViewById<TextView>(R.id.tv_tab_home_page_red_dot)}
        when(mNoticeNum + mMessageNum) {
            0 -> {
                tvRedDot?.visibility = View.GONE
            }
            else -> {
                tvRedDot?.run {
                    text = (mNoticeNum + mMessageNum).toString()
                    visibility = View.VISIBLE
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: LiveMethodEvent) {
        when (event.type) {
            LiveEventConstant.AUDIENCE_RESUME_LIVING -> {
                if (!FloatWindowLayout.getInstance().floatState) {
                    FloatWindowLayout.getInstance().closeFloatWindow()
                    RoomManager.enterLiveRoom(this,
                            FloatWindowLayout.getInstance().anchorId,
                            FloatWindowLayout.getInstance().roomId)
                } else {
                    val intent = Intent(this, LiveRoomSwitchActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

            LiveEventConstant.ANCHOR_RESUME_LIVING -> {
                val intent = Intent(this, LiveRoomAnchorActivity::class.java)
                intent.flags = FLAG_ACTIVITY_NEW_TASK;
                startActivity(intent);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: MainPageTurnEvent) {
        vpMain?.currentItem = event.position
        when(event.position){
            0->{
                EventBus.getDefault().post(HighPageTurnEvent(event.type))
            }
            1->{
                EventBus.getDefault().post(DynamicPageTurnEvent(event.type))
            }
            2->{
                EventBus.getDefault().post(FeedLinkPageTurnEvent(event.type))
            }
            3->{
                EventBus.getDefault().post(PlayBackPageTurnEvent(event.type))
            }
        }

//        if (event.position == 1) {
//            EventBus.getDefault().post(DynamicPageTurnEvent(event.type))
//        }else if (event.position == 0){
//            EventBus.getDefault().post(HighPageTurnEvent(event.type))
//        }
    }

    var otherUid = ""
    var otherRoomId = ""

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: OtherLiveRoomEvent) {
        //LogUtil.d("赋值 " + event.uid)
        otherUid = event.uid
        otherRoomId = event.roomId
    }

    private fun checkLiveRoomIntent() {
        //LogUtil.d("跳转直播间" + otherUid + "      " + otherRoomId)
        if (otherUid.isNotEmpty() && otherRoomId.isNotEmpty()) {
            RoomManager.enterLiveRoom(this, otherUid, otherRoomId.toInt())
            otherUid = ""
            otherRoomId = ""
        }
    }

    var currentTime = 0L
    var lastTime = 0L
    //判断是否过了12点
    private fun checkSignState() {
        currentTime = SystemClock.currentThreadTimeMillis()
        if (!DateUtils.isSameData(currentTime.toString(), lastTime.toString())) {
            lastTime = currentTime
            getSignTimesInWeeks()
        }
    }


}
