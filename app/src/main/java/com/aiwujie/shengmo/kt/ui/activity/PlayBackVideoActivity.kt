package com.aiwujie.shengmo.kt.ui.activity

import android.Manifest
import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.bean.LiveLogBean
import com.aiwujie.shengmo.bean.PlayBackInfoBean
import com.aiwujie.shengmo.eventbus.PlayBackInfoEvent
import com.aiwujie.shengmo.eventbus.UserLiveHistoryRefreshBean
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.adapter.PlayBackCommentAdapter
import com.aiwujie.shengmo.kt.adapter.VideoEpisodeAdapter
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener
import com.aiwujie.shengmo.kt.ui.view.*
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.kt.util.livedata.UnFlowLiveData
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.LiveLogListAdapter2
import com.aiwujie.shengmo.timlive.kt.ui.view.LiveLotteryDrawPop
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.utils.jzplayer.JZMediaIjk
import com.aiwujie.shengmo.videoplay.view.MyJzvdStd
import com.aiwujie.shengmo.view.CommentDialogFragment
import com.aiwujie.shengmo.zdyview.CountDownButton
import com.bumptech.glide.Glide
import com.donkingliang.imageselector.utils.ImageSelector
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.horizontal.SmartRefreshHorizontal
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference

/**
 * 回放页面
 */
class PlayBackVideoActivity : BaseActivity(), PermissionCallbacks {
    private lateinit var jzvd: MyJzvdStd
    lateinit var ivPoster: ImageView
    lateinit var bm: View

    lateinit var llTitle: LinearLayout
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var tvInfoTitle: TextView
    private lateinit var tvInfoIntroduce: TextView
    private lateinit var rvVideoEpisode: RecyclerView

    private lateinit var cdbutton: CountDownButton

    private lateinit var ivPlayBackBeans: ImageView
    private lateinit var tvPlayBackPrice: TextView
    private lateinit var tvPlayBackWatchSum: TextView

    private val tvPlayBackName: TextView by lazy { findViewById<TextView>(R.id.tv_user_name) }
    private val tvPlayBackTime: TextView by lazy { findViewById<TextView>(R.id.tv_user_time) }
    private val ivPlayBackIcon: ImageView by lazy { findViewById<ImageView>(R.id.iv_user_icon) }
    private val clearScreenLayout: ConstraintLayout by lazy { findViewById<ConstraintLayout>(R.id.cscl_play_back) }
    private val llHeaderLayout: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_anchor_info) }
    private val tvAudienceNum: TextView by lazy { findViewById<TextView>(R.id.tv_audience_number) }
    private val tvAnchorName: TextView by lazy { findViewById<TextView>(R.id.tv_anchor_name) }
    private val ivAnchorHead: ImageView by lazy { findViewById<ImageView>(R.id.iv_anchor_head) }
    private val llRewardBean: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_reward_bean) }
    private val tvRewardBean: TextView by lazy { findViewById<TextView>(R.id.tv_reward_bean) }
    private val ivGift: ImageView by lazy { findViewById<ImageView>(R.id.iv_play_back_gift) }
    private val ivLotteryDraw: ImageView by lazy { findViewById<ImageView>(R.id.iv_play_back_lottery_draw) }
    private val groupPlayBack: Group by lazy { findViewById<Group>(R.id.group_play_back) }
    private val tvAnchorSexAndAge: TextView by lazy { findViewById<TextView>(R.id.tv_anchor_sex_and_age) }
    private val tvAnchorRole: TextView by lazy { findViewById<TextView>(R.id.tv_anchor_role) }
    private val tvAnchorFollowState: TextView by lazy { findViewById<TextView>(R.id.tv_anchor_follow_state) }
    private val rvAnchorRecommend:RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_recommend_play_back) }
    private val refreshAnchorRecommend:SmartRefreshHorizontal by lazy { findViewById<SmartRefreshHorizontal>(R.id.smart_refresh_play_back) }
    private val tvPlayBackComment:TextView by lazy { findViewById<TextView>(R.id.tv_play_back_comment) }

    //var rewardBeans = MutableLiveData<String>()
    var rewardBeans = UnFlowLiveData<String>()
    var totalReward:Int = 0

    var isNeedShowPayPop = false
    var isHide = false


    companion object {
        fun start(context: Context, id: String, uid: String, type: String = "") {
            val intent = Intent(context, PlayBackVideoActivity::class.java)
            intent.putExtra(IntentKey.ID, id)
            intent.putExtra(IntentKey.UID, uid)
            intent.putExtra(IntentKey.TYPE, type)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("play back onCreate")
        setContentView(R.layout.app_activity_play_back_video)
        //StatusBarUtil.showLightStatusBar(this)
        ImmersionBar.with(this).init()


        llTitle = findViewById(R.id.layout_normal_titlebar)
        jzvd = findViewById(R.id.jz_video_view)
        ivPoster = findViewById(R.id.iv_live_poster)

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        tvInfoTitle = findViewById(R.id.tv_play_back_title)
        tvInfoIntroduce = findViewById(R.id.tv_play_back_introduce)
        rvVideoEpisode = findViewById(R.id.rv_play_back_episode)
        cdbutton = findViewById(R.id.count_down_bt_ticket)

        ivPlayBackBeans = findViewById(R.id.iv_play_back_bean)
        tvPlayBackPrice = findViewById(R.id.tv_play_back_price)
        tvPlayBackWatchSum = findViewById(R.id.tv_play_back_watch_sum)

        bm = findViewById(R.id.view_bottom_bar)

        jzvd.setOnFullscreenListener(object : MyJzvdStd.OnFullscreenListener {
            override fun doNormalScreen() {
                Jzvd.VIDEO_IMAGE_DISPLAY_TYPE = Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER
                LogUtil.d("JZResizeTextureView", "normal screen")
                llTitle.visibility = View.VISIBLE
                bm.visibility = View.VISIBLE
                groupPlayBack.visibility = View.GONE
            }

            override fun doFullScreen() {
                Jzvd.VIDEO_IMAGE_DISPLAY_TYPE = Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP
                LogUtil.d("JZResizeTextureView", "full screen")
                llTitle.visibility = View.GONE
                bm.visibility = View.GONE
                groupPlayBack.visibility = View.VISIBLE
            }
        })

        jzvd.setOnVideoListener {
            if (isNeedShowPayPop) {
                showCountDownBtn()
                hidePlayerBtn()
            }
        }

        jzvd.setOnMenuListener(object : MyJzvdStd.OnMenuListener {
            override fun doMenuShow() {
                groupPlayBack.visibility = View.VISIBLE
            }

            override fun doMenuDismiss() {
                runOnUiThread {
                    groupPlayBack.visibility = View.GONE
                }
            }
        })

        tvTitle.text = "回放详情"

        ivTitleBack.setOnClickListener {
            finish()
        }

        ivTitleRight.setOnClickListener {
            showManagerPop()
            //showCommentPop()
        }

        if ("1" != MyApp.isAdmin) { //禁止截图
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
        groupPlayBack.visibility = View.GONE
       // clearScreenLayout.addClearViews(llHeaderLayout, llRewardBean, ivGift, ivLotteryDraw)
       // clearScreenLayout.setSlideDirection(SlideDirection.LEFT)

      //  jzvd.gotoFullscreen()
        liveLogList = ArrayList<LiveLogBean.DataBean>()
        initData()
        getRecommendData()


        rewardBeans.observe(this, Observer<String?> {
            tvRewardBean.text = it
            tvPlayBackWatchSum.text = "浏览$watchNum   收入${it}豆"
        })

        lifecycle.addObserver(PlayerListener())

        tvPlayBackComment.setOnClickListener {
            showCommentPop()
        }
    }

    var watchNum = ""
    private fun initData() {
        playBackId = intent.getStringExtra(IntentKey.ID) ?: ""
        anchorId = intent.getStringExtra(IntentKey.UID) ?: ""
        if (anchorId == MyApp.uid || "1" == MyApp.isAdmin) {
            ivTitleRight.visibility = View.VISIBLE
        } else {
            ivTitleRight.visibility = View.INVISIBLE
        }
        getLiveData()
        liveAdapter?.updateChooseId(playBackId)
        liveAdapter?.notifyDataSetChanged()
    }


    private lateinit var playBackId: String
    private lateinit var anchorId: String
    lateinit var videoList: List<PlayBackInfoBean.DataBean.VideoListBean>
    var playBackBean: PlayBackInfoBean? = null
    var liveTitle = ""
    var livePoster: String? = ""
    var isLock = false
    private fun getLiveData() {
        HttpHelper.getInstance().getLivePlayBackDetail(playBackId, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                playBackBean = GsonUtil.GsonToBean(data, PlayBackInfoBean::class.java)
                playBackBean?.data?.run {
                    mAnchorId = uid ?: ""
                    tvInfoTitle.text = this.title
                    tvInfoIntroduce.text = this.introduce
                    videoList = this.video_list
                    isHide = this.is_del == "1"
                    isLock = this.live_log_info.is_admin_hidden == "1"
                    tvPlayBackComment.text = "评论 ($comment_num) "
                    val adapter = VideoEpisodeAdapter(this@PlayBackVideoActivity, videoList)
                    rvVideoEpisode.adapter = adapter
                    rvVideoEpisode.layoutManager = LinearLayoutManager(this@PlayBackVideoActivity)
                    liveTitle = this.title
                    livePoster = this.live_poster ?: ""
                    adapter.onItemClickListener = OnSimpleItemListener {
//                        if (!isNeedShowPayPop) {
//                            startPlay(it)
//                        }
                        startPlay(it)
                    }
                    if (this.is_free == "1") {
                        ivPlayBackBeans.visibility = View.VISIBLE
                        tvPlayBackPrice.text = this.video_price
                        isNeedShowPayPop = !(this.is_pay == "1" || anchorId == MyApp.uid)
                    } else {
                        ivPlayBackBeans.visibility = View.GONE
                        tvPlayBackPrice.text = "免费"
                        isNeedShowPayPop = false
                    }
                    if (videoList.isNotEmpty()) {
                        if ((intent.getStringExtra(IntentKey.TYPE) ?: "").isNotEmpty()) {
                            startPlay(0)
                            jzvd.gotoFullscreen()
                            if (isNeedShowPayPop) {
                                jzvd.resetProgressAndTime()
                                showCountDownBtn()
                                //未购买门票，禁止左右滑动快进
                                jzvd.setMoveAllowed(false)
                                hidePlayerBtn()
                            }
                        } else {
                            choosePlay(0)
                        }
                        ivPoster.visibility = View.GONE
                    } else {
                        ivPoster.visibility = View.VISIBLE
                        Glide.with(this@PlayBackVideoActivity).load(this.live_poster).centerCrop().into(ivPoster)
                    }
                    watchNum = watch_num
                    totalReward = all_live_beans.toInt()
                    tvPlayBackWatchSum.text = "浏览$watchNum   收入${totalReward}豆"
                    ImageLoader.loadCircleImage(this@PlayBackVideoActivity, head_pic, ivPlayBackIcon)
                    tvPlayBackTime.text = addtime
                    tvPlayBackName.text = nickname
                    ivPlayBackIcon.clickDelay {
                        UserInfoActivity.start(this@PlayBackVideoActivity, uid)
                    }
                    tvPlayBackName.clickDelay {
                        UserInfoActivity.start(this@PlayBackVideoActivity, uid)
                    }
                    ImageLoader.loadCircleImage(this@PlayBackVideoActivity, head_pic, ivAnchorHead)
                    tvAnchorName.text = nickname
                    tvAudienceNum.text = "${watch_num}人"
                    tvRewardBean.text = all_live_beans
                    tvAnchorSexAndAge.text = age
                    when (sex) {
                        "1" -> {
                            tvAnchorSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                            tvAnchorSexAndAge.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.nan), null, null, null)
                        }
                        "2" -> {
                            tvAnchorSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                            tvAnchorSexAndAge.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.nv), null, null, null)
                        }
                        else -> {
                            tvAnchorSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                            tvAnchorSexAndAge.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.san), null, null, null)
                        }
                    }
                    when (role) {
                        "S" -> {
                            tvAnchorRole.text = "斯"
                            tvAnchorRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_boy)
                        }
                        "M" -> {
                            tvAnchorRole.text = "慕"
                            tvAnchorRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_girl)
                        }
                        "SM" -> {
                            tvAnchorRole.text = "双"
                            tvAnchorRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_cdts)
                        }
                        else -> {
                            tvAnchorRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_other)
                            tvAnchorRole.text = role
                        }
                    }
                    when (follow_state) {
                        "1", "3" -> {
                            tvAnchorFollowState.visibility = View.GONE
                        }
                        "2", "4" -> {
                            tvAnchorFollowState.visibility = View.VISIBLE
                        }
                    }
                    if (MyApp.uid == mAnchorId) {
                        tvAnchorFollowState.visibility = View.GONE
                    }
                    initLiveBackScreen()

                }

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                finish()
            }
        })

    }


    override fun onPause() {
        super.onPause()
        //Jzvd.releaseAllVideos()
        LogUtil.d("play back onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.d("play back onStop")
       // Jzvd.releaseAllVideos()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d("play back onDestory")
       // Jzvd.releaseAllVideos()
        if (isNeedShowPayPop) {
            if (jzvd.mediaInterface != null) {
                jzvd.mediaInterface.seekTo(0)
            }
        }
        Jzvd.releaseAllVideos()
    }


    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        } else {
            if (isNeedShowPayPop) {
                if (jzvd.mediaInterface != null) {
                    jzvd.mediaInterface.seekTo(0)
                }
            }
        }
        super.onBackPressed()
    }

    fun startPlay(index: Int) {
        videoList.let {
            runOnUiThread {
                //Jzvd.releaseAllVideos()
                jzvd.setUp(it[index].episode_url, "", JzvdStd.SCREEN_NORMAL, JZMediaIjk::class.java)
                jzvd.startPreloading()
                jzvd.startVideoAfterPreloading()
            }
        }
    }

    fun choosePlay(index: Int) {
//        videoList.let {
//            runOnUiThread {
//                //Jzvd.releaseAllVideos()
//                jzvd.setUp(it[index].episode_url, "", JzvdStd.SCREEN_NORMAL, JZMediaIjk::class.java)
//            }
//        }
        startPlay(index)
    }

    private fun showManagerPop() {
        val menuItemList: ArrayList<NormalMenuItem> = ArrayList()
        menuItemList.add(NormalMenuItem(0, "设置门票"))
        menuItemList.add(NormalMenuItem(0, "编辑信息"))
        menuItemList.add(if (isHide) NormalMenuItem(0, "取消仅自己可见") else NormalMenuItem(0, "仅自己可见"))
        if (MyApp.isAdmin == "1") {
            menuItemList.add(if (isLock) NormalMenuItem(0, "取消隐藏") else NormalMenuItem(0, "隐藏回放"))
        }
        val menuPop = NormalMenuPopup(this@PlayBackVideoActivity, menuItemList)
        menuPop.setOnSimpleItemListener(object : OnSimpleItemListener {
            override fun onItemListener(position: Int) {
                when (menuItemList[position].content) {
                    "设置门票" -> {
                        showSetTicketPop()
                    }
                    "删除回放" -> {
                        showDelTipPop()
                    }
                    "取消仅自己可见" -> {
                        cancelHidePlayBack()
                        menuPop.dismiss()
                    }
                    "仅自己可见" -> {
                        showHideTipPop()
                        menuPop.dismiss()
                    }
                    "编辑信息" -> {
                        showPlayBackInfoPop()
                    }
                    "隐藏回放" -> {
                        lockOrCancelPlayBack(true)
                        menuPop.dismiss()
                    }
                    "取消隐藏" -> {
                        lockOrCancelPlayBack(false)
                        menuPop.dismiss()
                    }
                }
            }

        })
        menuPop.showPopupWindow()
    }

    private fun showSetTicketPop() {
        playBackBean?.let {
            val infoPop = LivePlayBackInfoPop(this@PlayBackVideoActivity, playBackId, it.data.is_free == "1", it.data.video_price.toInt())
            infoPop.showPopupWindow()
            infoPop.playBackInfoListener = object : LivePlayBackInfoPop.PlayBackInfoListener {
                override fun setInfoSuc(isNeedPay: String, free: String) {
                    if (isNeedPay == "1") {
                        ivPlayBackBeans.visibility = View.VISIBLE
                        tvPlayBackPrice.text = free
                    } else {
                        ivPlayBackBeans.visibility = View.GONE
                        tvPlayBackPrice.text = "免费"
                    }
                }
            }
        }
    }

    private fun showDelTipPop() {
        val builder = AlertDialog.Builder(this@PlayBackVideoActivity)
        builder.setMessage("删除该条回放后将不可恢复，是否删除？")
                .setPositiveButton("否") { dialog, which -> dialog.dismiss() }
                .setNegativeButton("是") { dialog, which ->
                    HttpHelper.getInstance().delPlayBack(playBackId, anchorId, object : HttpCodeListener {
                        override fun onSuccess(data: String?) {
                            ToastUtil.show(this@PlayBackVideoActivity, "删除成功")
                        }

                        override fun onFail(code: Int, msg: String?) {
                            ToastUtil.show(this@PlayBackVideoActivity, msg)
                        }

                    })
                }.create().show()
    }

    private fun cancelHidePlayBack() {
        HttpHelper.getInstance().hidePlayBack(playBackId, anchorId, "0", object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(this@PlayBackVideoActivity, "取消隐藏")
                isHide = false
                sendPlayBackChangeEvent()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@PlayBackVideoActivity, msg)
            }

        })
    }

    private fun showHideTipPop() {
        val builder = AlertDialog.Builder(this@PlayBackVideoActivity)
        builder.setMessage("是否设置该条回放仅自己可见？")
                .setPositiveButton("否") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("是") { _, _ ->
                    HttpHelper.getInstance().hidePlayBack(playBackId, anchorId, "1", object : HttpCodeListener {
                        override fun onSuccess(data: String?) {
                            ToastUtil.show(this@PlayBackVideoActivity, "隐藏成功")
                            isHide = true
                            notifyLiveInfoChange()
                        }

                        override fun onFail(code: Int, msg: String?) {
                            ToastUtil.show(this@PlayBackVideoActivity, msg)
                        }

                    })
                }.create().show()
    }

    private fun hidePlayerBtn() {
        jzvd.progressBar.visibility = View.INVISIBLE
        jzvd.fullscreenButton.visibility = View.INVISIBLE
    }

    private fun showPlayBtn() {
        jzvd.progressBar.visibility = View.VISIBLE
        jzvd.fullscreenButton.visibility = View.VISIBLE
    }

    private fun showCountDownBtn() {
        if (cdbutton.visibility == View.VISIBLE) {
            return
        }
        cdbutton.visibility = View.VISIBLE
        cdbutton.totalTime = 15 * 1000
        cdbutton.delayTime = 500
        cdbutton.startTimer()
        cdbutton.setOnCountClickListener(object : CountDownButton.onCountClickListener {
            override fun onCountingClick() {
                showBuyTicketPop()
            }

            override fun onCountOverClick() {
                showBuyTicketPop()
            }

            override fun onCounting(process: Double) {
            }
        })
        cdbutton.setOnClickListener {
            showBuyTicketPop()
        }
    }

    private fun showBuyTicketPop() {
        cdbutton.cancle()
        cdbutton.visibility = View.GONE
        jzvd.mediaInterface.pause()
        jzvd.mediaInterface.seekTo(0)
        jzvd.onStatePause()
        jzvd.resetProgressAndTime()
        val buyPop = LiveBuyVideoPop(this, playBackId, anchorId)
        buyPop.showPopupWindow()
        buyPop.onTicketPopListener = object : LiveBuyVideoPop.OnTicketPopListener {
            override fun doPopBuySuc() {
                ToastUtil.show(this@PlayBackVideoActivity, "购买成功")
                isNeedShowPayPop = false
                jzvd.setMoveAllowed(true)
                showPlayBtn()
                buyPop.dismiss()
            }

            override fun doPopBuyFail(msg: String?) {
                ToastUtil.show(this@PlayBackVideoActivity, msg)
                buyPop.dismiss()
                finish()
            }

            override fun doPopDismiss() {
                finish()
            }

        }
    }

    var infoPop: PlaybackPosterPop? = null
    private fun showPlayBackInfoPop() {
        infoPop = PlaybackPosterPop(this@PlayBackVideoActivity, playBackId, liveTitle, livePoster!!)
        infoPop?.showPopupWindow()
        infoPop?.onSimpleListener = object : OnSimplePopListener {
            override fun doSimplePop() {
                choosePhoto()
            }
        }
        infoPop?.infoListener = object : PlaybackPosterPop.OnPlaybackInfoPopListener {
            override fun doLiveInfoRefresh(title: String, cover: String) {
                liveTitle = title
                livePoster = cover
                tvInfoTitle.text = title
                refreshPoster(cover)
                notifyLiveInfoChange()
            }
        }
    }

    fun refreshPoster(poster: String) {
        if (ivPoster.visibility == View.VISIBLE) {
            Glide.with(this).load(poster).centerCrop().into(ivPoster)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        if (perms!!.contains(Manifest.permission.CAMERA)) {
            ToastUtil.show(applicationContext, "授权失败,请开启相机权限")
            return
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(applicationContext, "授权失败,请开启读写权限")
            return
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        if (requestCode == photoRequestCode) {
            if (!perms!!.contains(Manifest.permission.CAMERA)) {
                return
            }
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return
            }
            selectPhoto()
        }
    }

    private val photoRequestCode = 101
    fun choosePhoto() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            selectPhoto()
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", photoRequestCode, *perms)
        }
    }

    private val selectRequestCode = 1001
    private fun selectPhoto() {
        ImageSelector.builder()
                .useCamera(true)
                .setSingle(false)
                .setMaxSelectCount(1)
                .canPreview(true)
                .setCrop(true)
                .setCropRatio(1f)
                .start(this, selectRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectRequestCode) {
            if (data != null) {
                ToastUtil.show(this@PlayBackVideoActivity, "图片上传中")
                //获取选择器返回的数据
                val images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT)
                for (i in images.indices) {
                    uploadImage(images[i])
                }
            }
        }
    }

    private var imgPre = ""
    fun uploadImage(path: String?) {
        imgPre = SharedPreferencesUtils.geParam(this, "image_host", "")
        val loadbitmap = BitmapFactory.decodeFile(path, getBitmapOption(2))
        val rotaBitmap = PhotoRemoteUtil.rotaingImageView(PhotoRemoteUtil.getBitmapDegree(path), loadbitmap)
        val `is` = ByteArrayInputStream(bitmap2Bytes(rotaBitmap))
        val put = PhotoUploadTask(
                HttpUrl.NetPic() + "Api/Api/filePhoto" //  "http://59.110.28.150:888/Api/Api/filePhoto"
                , `is`,
                this, handler)
        put.start()
    }

    private fun getBitmapOption(inSampleSize: Int): BitmapFactory.Options? {
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inSampleSize = inSampleSize
        return options
    }

    private fun bitmap2Bytes(bm: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    var handler = UploadHandler(this)

    class UploadHandler(activity: PlayBackVideoActivity) : Handler() {
        private var activityWeakReference: WeakReference<PlayBackVideoActivity>? = null

        init {
            activityWeakReference = WeakReference(activity)
        }

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity = activityWeakReference!!.get()!!
            if (msg!!.what == 152) {
                val s = msg.obj as String
                val beanIcon = Gson().fromJson(s, BeanIcon::class.java)
                val imgUrl: String = activity.imgPre + beanIcon.data
                activity.infoPop?.refreshImageUrl(imgUrl)
            }
        }
    }

    fun lockOrCancelPlayBack(isLockPlayBack: Boolean) {
        HttpHelper.getInstance().lockLiveLog(anchorId, playBackId, if (isLockPlayBack) "1" else "2", object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                if (isLockPlayBack) {
                    ToastUtil.show(this@PlayBackVideoActivity, "锁定成功")
                } else {
                    ToastUtil.show(this@PlayBackVideoActivity, "取消锁定")
                }
                isLock = isLockPlayBack
                notifyLiveInfoChange()
                EventBus.getDefault().post(PlayBackInfoEvent(if (isLockPlayBack) "1" else "2"))
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@PlayBackVideoActivity, msg)
            }
        })
    }

    fun notifyLiveInfoChange() {
        EventBus.getDefault().post(UserLiveHistoryRefreshBean())
    }

    private var mAnchorId = ""
    fun initLiveBackScreen() {
        llHeaderLayout.clickDelay {
            UserInfoActivity.start(this, mAnchorId)
        }
        ivGift.clickDelay {
            showGiftPop()
        }
        ivLotteryDraw.clickDelay {
            showLotteryDrawPop()
        }
        tvAnchorFollowState.clickDelay {
            followAnchor()
        }
    }

    fun followAnchor() {
        HttpHelper.getInstance().followAnchor(mAnchorId, true, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                "关注成功".showToast()
                tvAnchorFollowState.visibility = View.GONE
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun showLotteryDrawPop() {
        val liveLotteryDrawPop = LiveLotteryDrawPop(this)
        liveLotteryDrawPop.showPopupWindow()
    }

    private fun showGiftPop() {
        val giftPanelPop = GiftPanelPop3(this, 3, playBackId)
        giftPanelPop.showPopupWindow()
        giftPanelPop.setOnGiftSendSucListener(object : GiftPanelPop3.OnGiftSendSucListener {
            override fun onGiftSendSuc(orderId: String, giftReward: Int) {
                //"赠送成功".showToast()
                //getUserPresent()
                addPlaybackBalance(giftReward)
            }
        })
    }
    lateinit var liveLogList:ArrayList<LiveLogBean.DataBean>
    var liveAdapter:LiveLogListAdapter2? = null
    var page = 0
    private fun getRecommendData() {
        HttpHelper.getInstance().getUserLiveLogList(anchorId, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshAnchorRecommend.finishRefresh()
                refreshAnchorRecommend.finishLoadMore()
                val tempData = GsonUtil.GsonToBean(data, LiveLogBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            liveLogList.clear()
                            liveLogList.addAll(this)
                            liveAdapter = LiveLogListAdapter2(this@PlayBackVideoActivity, liveLogList, playBackId)
                            with(rvAnchorRecommend) {
                                adapter = liveAdapter
                                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            }
//                            rvAnchorRecommend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                                var lastItemCount = 0
//                                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                                    super.onScrolled(recyclerView, dx, dy)
//                                    if (rvAnchorRecommend.layoutManager is LinearLayoutManager) {
//                                        val itemCount = rvAnchorRecommend.layoutManager.itemCount
//                                        val lastPosition = (rvAnchorRecommend.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                                        if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
//                                            lastItemCount = itemCount
//                                            loadMorePage()
//                                        }
//                                    }
//                                }
//                            })
                            refreshAnchorRecommend.setEnableLoadMore(false)
                            refreshAnchorRecommend.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                                override fun onRefresh(refreshlayout: RefreshLayout) {
                                    page = 0
                                    getRecommendData()
                                }

                                override fun onLoadMore(refreshlayout: RefreshLayout) {
                                    // loadMorePage()
                                    refreshlayout.finishLoadMore()
                                }

                            })

                            liveAdapter?.setOnSimpleItemListener {
                                if (liveLogList.size > it) {
                                    if (playBackId != liveLogList[it].id) {
                                        start(this@PlayBackVideoActivity, liveLogList[it].id, liveLogList[it].uid)
                                    }
                                }
                            }
                        }
                        else -> {
                            val tempIndex = liveLogList.size
                            // liveAdapter?.notifyItemRangeRemoved(0, tempIndex);
                            liveLogList.addAll(this)
                            // liveAdapter?.notifyItemRangeChanged(0, liveLogList.size);
                            liveAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }

                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshAnchorRecommend.finishRefresh()
                refreshAnchorRecommend.finishLoadMore()
            }
        })
    }


    private fun loadMorePage() {
        page++
        getRecommendData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        cdbutton.cancle()
        cdbutton.visibility = View.GONE
        setIntent(intent)
        initData()
    }

    fun sendPlayBackChangeEvent() {
        EventBus.getDefault().post(PlayBackInfoEvent(
                if (isLock) "1" else if (isHide) "2" else "3"
        ))
    }


    fun addPlaybackBalance(reward: Int) {
        totalReward += reward
        rewardBeans.postValue(totalReward.toString())
    }


    class PlayerListener: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun removeAllView() {
            Jzvd.releaseAllVideos()
        }
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {

        }
    }

    private val commentDialogFragment:CommentDialogFragment by lazy { CommentDialogFragment() }
    private fun showCommentPop() {
        val commentPop = PlayBackCommentPop(this,playBackId)
        commentPop.showPopupWindow()
        commentPop.checkAnchor(anchorId == MyApp.uid)
    }

    private fun showAddCommentDialog() {
        val fragmentManager = supportFragmentManager
         commentDialogFragment.show(fragmentManager, "comment")
    }

}