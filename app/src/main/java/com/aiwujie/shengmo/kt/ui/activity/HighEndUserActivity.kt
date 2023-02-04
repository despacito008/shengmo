package com.aiwujie.shengmo.kt.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.RedWomenActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.RedwomenNewListAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.GroupChatInfoModel
import com.aiwujie.shengmo.bean.HighUserModel
import com.aiwujie.shengmo.eventbus.RedWomenIntroData
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.ManagerBanActivity
import com.aiwujie.shengmo.kt.ui.activity.normallist.HighServiceOpenActivity
import com.aiwujie.shengmo.kt.ui.view.HighAddReasonPop
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup
import com.aiwujie.shengmo.kt.util.BlurFliter
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.tim.chat.ChatActivity
import com.aiwujie.shengmo.tim.utils.Constants
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.utils.AnimationUtil.*
import com.aiwujie.shengmo.view.NormalTipsPop
import com.aiwujie.shengmo.view.ZoomInScrollView
import com.aiwujie.shengmo.view.indicator.CircleIndicator
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import org.feezu.liuli.timeselector.Utils.TextUtil

class HighEndUserActivity : BaseActivity() {
    private lateinit var vpHighEndUser: ViewPager
    private lateinit var indicatorHighEndUser: CircleIndicator

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivMore: ImageView

    private lateinit var tvUserName: TextView
    private lateinit var tvUserSexAndAge: TextView
    private lateinit var tvUserRole: TextView
    private lateinit var tvUserCity: TextView
    private lateinit var ivUsercity: ImageView


    private lateinit var tvHight: TextView
    private lateinit var tvWeight: TextView
    private lateinit var tvConstellation: TextView
    private lateinit var tvDegree: TextView
    private lateinit var tvMonthly: TextView
    private lateinit var tvToSex: TextView
    private lateinit var tvWant: TextView

    private lateinit var tvMonogue: TextView
    private lateinit var tvHighDescBlur: TextView
    private lateinit var tvRecommd: TextView

    private lateinit var llChat: LinearLayout
    private lateinit var llOpenRed: LinearLayout
    private lateinit var llOpenHigh: LinearLayout
    private lateinit var llFollow: LinearLayout
    private lateinit var tvFollow: TextView
    private lateinit var llbottom: LinearLayout
    private lateinit var zoomScrollView: ZoomInScrollView
    private lateinit var ivCaichan: ImageView
    private lateinit var ivJiankang: ImageView
    private lateinit var ivXueli: ImageView
    private lateinit var ivJineng: ImageView
    private lateinit var ivQita: ImageView

    private lateinit var ivRealId: ImageView
    private lateinit var ivRealName: ImageView
    private lateinit var ivVidoeAuth: ImageView
    private lateinit var llRedRecommd: LinearLayout
    private lateinit var llDuBai: RelativeLayout


    private lateinit var recycleView: RecyclerView
    private lateinit var llRedNone: LinearLayout
    private lateinit var llRedMatch: LinearLayout
    private lateinit var tvRedStatus: TextView
    private val ivRecommdLock: ImageView by lazy { findViewById<ImageView>(R.id.iv_recomd_lock) }
    private val ivDescLock: ImageView by lazy { findViewById<ImageView>(R.id.iv_desc_lock) }

    private var redwomenNewAdapter: RedwomenNewListAdapter? = null
    private var redwomendata: RedWomenIntroData? = null
    private var userModle: HighUserModel.DataBean? = null
    private var adminItemListener: OnItemClickListener? = null

    companion object {
        fun start(context: Context, uid: String) {
            val intent = Intent(context, HighEndUserActivity::class.java)
            intent.putExtra(IntentKey.UID, uid)
            context.startActivity(intent)
        }
    }


    private var topUid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_high_end_user)
        StatusBarUtil.showLightStatusBar(this)
        this.vpHighEndUser = findViewById(R.id.view_pager_high_end_user)
        this.indicatorHighEndUser = findViewById(R.id.indicator_high_end_user)
        topUid = intent.getStringExtra(IntentKey.UID)   //  高端id
        tvTitle = findViewById(R.id.tv_tool_bar_title)
        ivTitleBack = findViewById(R.id.iv_user_info_return)
        ivTitleRight = findViewById(R.id.iv_user_info_edit)
        ivMore = findViewById(R.id.iv_user_info_more)

        tvUserName = findViewById(R.id.tv_high_end_user_name)
        tvUserCity = findViewById(R.id.tv_high_end_user_city)
        ivUsercity = findViewById(R.id.iv_city)
        tvUserSexAndAge = findViewById(R.id.tv_user_info_sex_and_age)
        tvUserRole = findViewById(R.id.tv_layout_user_normal_info_role)
        tvHight = findViewById(R.id.tv_hight)
        tvWeight = findViewById(R.id.tv_weight)
        tvMonthly = findViewById(R.id.tv_monthly)
        tvConstellation = findViewById(R.id.tv_constellation)
        tvDegree = findViewById(R.id.tv_degree)
        tvWant = findViewById(R.id.tv_want)
        tvMonogue = findViewById(R.id.tv_monogogue)
        tvHighDescBlur = findViewById(R.id.tv_high_desc_blur)
        tvRecommd = findViewById(R.id.tv_recommd)

        llChat = findViewById(R.id.ll_user_info_chat)
        llOpenRed = findViewById(R.id.ll_user_open_red)
        llOpenHigh = findViewById(R.id.ll_user_open_high)
        llbottom = findViewById(R.id.ll_user_info_bottom)
        llFollow = findViewById(R.id.ll_user_info_attention)
        tvFollow = findViewById(R.id.tv_user_info_attention)
        zoomScrollView = findViewById(R.id.zoomScrollView)

        ivCaichan = findViewById(R.id.iv_caichan)
        ivJiankang = findViewById(R.id.iv_jiankang)
        ivXueli = findViewById(R.id.iv_xueli)
        ivJineng = findViewById(R.id.iv_jineng)
        ivQita = findViewById(R.id.iv_qita)

        ivRealId = findViewById(R.id.iv_realId)
        ivRealName = findViewById(R.id.iv_realName)
        ivVidoeAuth = findViewById(R.id.iv_vidoeAuth)
        llRedRecommd = findViewById(R.id.ll_redRecommd)
        llDuBai = findViewById(R.id.ll_dubai)

        llRedNone = findViewById(R.id.ll_redNone)
        recycleView = findViewById(R.id.recycleView)
        llRedMatch = findViewById(R.id.ll_red_match)

        tvRedStatus = findViewById(R.id.tv_red_status)

        setListener()

        getData()
    }

    private fun setListener() {


        ivTitleBack.setOnClickListener { finish() }

        llChat.setOnClickListener {
            isChatWithHigh()
        }
        llOpenRed.setOnClickListener {
            startActivity(Intent(this@HighEndUserActivity, RedWomenActivity::class.java))
        }
        llOpenHigh.setOnClickListener {
            startActivity(Intent(this@HighEndUserActivity, HighServiceOpenActivity::class.java))
        }


        ivTitleRight.setOnClickListener {
            if (userModle?.chat_id == MyApp.uid) {
                if (userModle?.is_over_mine == "1") {
                    "高端交友服务已到期".showToast()
                } else {
                    startActivity(Intent(this, PushHighActivity::class.java).putExtra(IntentKey.UID, topUid))
                }
            } else {
                startActivity(Intent(this, PushHighActivity::class.java).putExtra(IntentKey.UID, topUid))
            }

        }

        llFollow.setOnClickListener {
            setFollow()
        }



        ivMore.setOnClickListener {
            showAdminNormalMenuPop()
        }

        zoomScrollView.setOnScrollListener(object : ZoomInScrollView.OnScrollListener {

            override fun onScroll(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                if (userModle == null) {
                    return
                }
                if (scrollY - oldScrollY < 0) {
                    showOrHideTopBottomBar(true)
                } else {
                    showOrHideTopBottomBar(false)
                }

            }

            override fun onZoom(zoomValue: Float) {

            }
        })

    }

    private fun setFollow() {
        HttpHelper.getInstance().setHighUserFollow(topUid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                if (userModle?.is_follow == 1) {
                    tvFollow.text = "关注"
                    userModle?.is_follow = 2
                } else {
                    userModle?.is_follow = 1
                    tvFollow.text = "取消关注"
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }


    private fun isChatWithHigh() {


        HttpHelper.getInstance().startHighGroup(topUid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                var model: GroupChatInfoModel = GsonUtil.GsonToBean(data, GroupChatInfoModel::class.java)
                model?.data?.run {
                    val chatInfo = ChatInfo()
                    chatInfo.type = V2TIMConversation.V2TIM_GROUP
                    chatInfo.id = img_gid
                    chatInfo.chatName = groupname
                    intent = Intent(MyApp.instance(), ChatActivity::class.java)
                    intent.putExtra(Constants.CHAT_INFO, chatInfo)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    MyApp.instance().startActivity(intent)
                }

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                //   群数量超过1000
                if (code == 4001) {
                    val normalTipsPop = NormalTipsPop.Builder(this@HighEndUserActivity)
                            .setTitle("提示")
                            .setInfo(msg)
                            .setCancelStr("取消")
                            .setConfirmStr("确定")
                            .build()
                    normalTipsPop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
                        override fun confirmClick() {
                            normalTipsPop.dismiss()
                            showClearMsgTimePop(this@HighEndUserActivity)
                        }

                        override fun cancelClick() {
                            normalTipsPop.dismiss()
                        }
                    })
                    normalTipsPop.showPopupWindow()

                }
            }
        })
    }

    private fun showClearMsgTimePop(context: Activity?) {
        val normalMenuItemList: MutableList<NormalMenuItem> = java.util.ArrayList()
        normalMenuItemList.add(NormalMenuItem(0, "7天"))
        normalMenuItemList.add(NormalMenuItem(0, "15天"))
        normalMenuItemList.add(NormalMenuItem(0, "30天"))
        val kickOutPop = NormalMenuPopup(context, normalMenuItemList)
        kickOutPop.setOnSimpleItemListener(OnSimpleItemListener { position ->
            when (position) {
                0 -> {
                    deleteMsg("1")
                    kickOutPop.dismiss()
                }

                1 -> {
                    deleteMsg("2")
                    kickOutPop.dismiss()
                }
                2 -> {
                    deleteMsg("3")
                    kickOutPop.dismiss()
                }
                else -> kickOutPop.dismiss()
            }
        })
        kickOutPop.showPopupWindow()
    }

    private fun deleteMsg(deleteType: String) {
        HttpHelper.getInstance().deleteMsgRecord(deleteType, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    var isAnimShowing = false

    fun showOrHideTopBottomBar(isShow: Boolean) {
        if (isAnimShowing) {
            return
        }
        if (isShow) {
            if (llbottom.visibility == View.GONE) {
                if (userModle?.chat_id != MyApp.uid) {
                    llbottom.visibility = View.VISIBLE
                }
                //llUserInfoBottom.setAnimation(AnimationUtil.moveToViewLocation());
                val translateAnimation = moveToViewLocation()
                translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        isAnimShowing = true
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        isAnimShowing = false
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                llbottom.animation = translateAnimation
                horizontalRightShow()

            }
        } else {
            if (llbottom.visibility == View.VISIBLE) {
                llbottom.visibility = View.GONE
                //llUserInfoBottom.setAnimation(AnimationUtil.moveToViewBottom());
                val translateAnimation = moveToViewBottom()
                translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        isAnimShowing = true
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        isAnimShowing = false
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                llbottom.animation = translateAnimation
                horizontalRightHide()

            }
        }
    }

    private fun showAdminNormalMenuPop() {
        if (userModle == null) {
            return
        }
        val menuItemList: MutableList<NormalMenuItem> = ArrayList()

        if (userModle?.top_user_status == "1") {
            menuItemList.add(NormalMenuItem(0, "关闭高端"))
        } else {
            menuItemList.add(NormalMenuItem(0, "开启高端"))
        }
        if (userModle?.top_match_status == "1") {
            menuItemList.add(NormalMenuItem(0, "关闭红娘牵线"))
        } else {
            menuItemList.add(NormalMenuItem(0, "开启红娘牵线"))
        }
        if (userModle?.top_chat_status == "1") {
            menuItemList.add(NormalMenuItem(0, "禁止聊天"))
        } else {
            menuItemList.add(NormalMenuItem(0, "恢复聊天"))
        }
        if (userModle?.top_info_status == "1") {
            menuItemList.add(NormalMenuItem(0, "禁止资料"))
        } else {
            menuItemList.add(NormalMenuItem(0, "恢复资料"))
        }
        if (userModle?.top_desc_hidden == "1") {
            menuItemList.add(NormalMenuItem(0, "恢复独白"))
        } else {
            menuItemList.add(NormalMenuItem(0, "隐藏独白"))
        }
        if (userModle?.top_red_desc_hidden == "1") {
            menuItemList.add(NormalMenuItem(0, "恢复荐语"))
        } else {
            menuItemList.add(NormalMenuItem(0, "隐藏荐语"))
        }

        menuItemList.add(NormalMenuItem(0, "取消审核"))
        if (userModle?.show_status == "1") {
            menuItemList.add(NormalMenuItem(0, "设为隐藏"))
        } else {
            menuItemList.add(NormalMenuItem(0, "取消隐藏"))
        }

        val normalMenuPopup = NormalMenuPopup(this@HighEndUserActivity, menuItemList)
        normalMenuPopup.showPopupWindow()
        normalMenuPopup.setOnSimpleItemListener(OnSimpleItemListener { position ->
            normalMenuPopup.dismiss()
            val data = menuItemList[position].content
            if (data == "隐藏独白" || data == "恢复独白") {
                mangerTopDesc("top_desc_hidden")
            } else if (data == "恢复荐语" || data == "隐藏荐语") {
                mangerTopDesc("top_red_desc_hidden")
            } else if (data == "禁止聊天" || data == "禁止资料" || data == "恢复聊天" || data == "恢复资料") {
                if (data.contains("聊天")) {
                    //  0 封禁 1正常
                    if (userModle?.top_chat_status == "0") {
                        adminCancleChatAndInfo("1")
                    } else {
                        showDaysMenu("1", data)
                    }
                } else if (data.contains("资料")) {
                    //  0 封禁 1正常
                    if (userModle?.top_info_status == "0") {
                        adminCancleChatAndInfo("2")
                    } else {
                        showDaysMenu("2", data)
                    }
                }

            } else if (data == "开启高端" || data == "开启红娘牵线" || data == "关闭高端" || data == "关闭红娘牵线") {
                if (data.contains("高端")) {
                    //  0未开启  1已开启
                    if (userModle?.top_user_status == "0") {
                        showServiceDaysMenu("1")
                    } else {
                        closeHigh(topUid)
                    }
                } else if (data.contains("牵线")) {
                    // 0未开启  1已开启
                    if (userModle?.top_match_status == "0") {
                        showServiceDaysMenu("2")
                    } else {
                        closeRedSerivce(topUid)
                    }
                }

            } else if (data == "取消审核") {
                val pop = HighAddReasonPop(this@HighEndUserActivity, "取消审核理由", topUid, "2")
                pop.showPopupWindow()
            } else if (data == "设为隐藏" || data == "取消隐藏") {
                if (data.contains("设为")) {
                    val pop = HighAddReasonPop(this@HighEndUserActivity, "隐藏理由", topUid, "3")
                    pop.showPopupWindow()
                } else {
                    adminHideUser(topUid)
                }

            }
        })
    }

    private fun adminHideUser(targetUid: String?) {

        HttpHelper.getInstance().adminHideUser(targetUid, "", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    private fun showDaysMenu(type: String, title: String) {
        val alertView = AlertView(null, null, "取消", null, arrayOf("1天", "3天", "1周", "2周", "1月", "永久"),
                this, AlertView.Style.ActionSheet, null)
        alertView.setOnItemClickListener { o, position, data ->
            punishment(type, (position + 1).toString(), ("${title}_${data}"))
        }
        alertView.show()
    }

    private fun showServiceDaysMenu(type: String) {
        val alertView = AlertView(null, null, "取消", null, arrayOf("1年", "2年", "3年", "永久"),
                this, AlertView.Style.ActionSheet, null)
        alertView.setOnItemClickListener { o, position, data ->
            if (type == "1") {
                openHigh(userModle?.chat_id, (position + 1).toString())
            } else if (type == "2") {
                openRedService(userModle?.chat_id, (position + 1).toString())
            }
        }
        alertView.show()
    }

    private fun punishment(type: String, days: String, title: String) {
        val intent = Intent(this, ManagerBanActivity::class.java)
        intent.putExtra("uid", topUid)
        intent.putExtra("method", type)
        if (!TextUtil.isEmpty(days)) {
            intent.putExtra("days", days)
        }
        intent.putExtra("title", title)
        intent.putExtra("activityType", "High")
        startActivity(intent)
    }


    private fun adminCancleChatAndInfo(type: String) {
        HttpHelper.getInstance().adminCancleChatAndInfo(topUid, type, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                getData()
                msg?.showToast()
            }


            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun mangerTopDesc(type: String) {
        HttpHelper.getInstance().adminMangerTopDesc(topUid, type, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                getData()
                msg?.showToast()
            }


            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun openHigh(targetUid: String?, type: String) {
        HttpHelper.getInstance().adminOpenHigh(targetUid, type, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun openRedService(targetUid: String?, type: String) {
        HttpHelper.getInstance().adminOpenRedService(targetUid, type, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun closeHigh(topId: String) {
        HttpHelper.getInstance().adminCloseHigh(topId, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun closeRedSerivce(topId: String) {
        HttpHelper.getInstance().adminCloseRedService(topId, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    private fun getData() {
        HttpHelper.getInstance().getHighUserDetail(topUid, "0", object : HttpCodeMsgListener {

            override fun onSuccess(data: String?, msg: String?) {
//                var userInfoBean = GsonUtil.GsonToBean(data, UserInfoBean::class.java)
                val userInfoBean = GsonUtil.GsonToBean(data, HighUserModel::class.java)
                userInfoBean?.data?.run {
                    val photoList = top_photo_arr
                    initViewPager(photoList, is_gaussian)
                    tvTitle.text = serial_id
                    showUserInfo(this)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun getTopMatchList() {
        HttpHelper.getInstance().getMatchList(topUid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                redwomendata = Gson().fromJson(data, RedWomenIntroData::class.java)
                redwomendata?.data?.run {
                    if (this != null && this.size > 0) {
                        llRedNone.visibility = View.GONE
                        recycleView.visibility = View.VISIBLE
                        redwomenNewAdapter = RedwomenNewListAdapter(this@HighEndUserActivity, this, "1")
                        recycleView.layoutManager = LinearLayoutManager(this@HighEndUserActivity)
                        recycleView.adapter = redwomenNewAdapter
                        redwomenNewAdapter?.onSimpleItemListener = OnSimpleItemListener {
                            startActivity(Intent(this@HighEndUserActivity, UserInfoActivity::class.java).putExtra(IntentKey.UID, this[it].fuid))
                        }
                    } else {
                        llRedNone.visibility = View.VISIBLE
                        recycleView.visibility = View.GONE
                    }
                }

            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    fun initViewPager(photoList: List<String>, isGaussian: String) {
        val photoViewList = ArrayList<ImageView>()
        for (url in photoList) {
            val imageView = ImageView(this@HighEndUserActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            if (isGaussian == "1") {
                GlideImgManager.glideBlurLoader(this, url, R.drawable.default_error, R.drawable.default_error, imageView)
            } else {
                Glide.with(this@HighEndUserActivity).load(url).into(imageView)
            }

            photoViewList.add(imageView)
        }
        val photoAdapter = ImagePagerAdapter(this@HighEndUserActivity, photoViewList)
        vpHighEndUser.adapter = photoAdapter
        indicatorHighEndUser.setViewPager(vpHighEndUser)
    }

    @SuppressLint("SetTextI18n")
    fun showUserInfo(user: HighUserModel.DataBean) {
        userModle = HighUserModel().DataBean()
        userModle = user
        user.run {

            if (chat_id == MyApp.uid) {
                ivMore.visibility = View.GONE
                ivTitleRight.visibility = View.VISIBLE
                if (is_match == "1") {
                    llRedMatch.visibility = View.VISIBLE
                    getTopMatchList()
                } else {
                    llRedMatch.visibility = View.GONE
                }
            } else {
                if (MyApp.isAdmin == "1") {
                    ivMore.visibility = View.VISIBLE
                    ivTitleRight.visibility = View.VISIBLE
                    if (is_match == "1") {
                        llRedMatch.visibility = View.VISIBLE
                        getTopMatchList()
                    } else {
                        llRedMatch.visibility = View.GONE
                    }
                } else {
                    ivMore.visibility = View.GONE
                    ivTitleRight.visibility = View.GONE
                }
            }

            if (chat_id == MyApp.uid) {
                llbottom.visibility = View.GONE
            } else {
                llbottom.visibility = View.VISIBLE
            }

            if (is_top_mine == "1") {
                llChat.visibility = View.VISIBLE
                llOpenHigh.visibility = View.GONE
                llOpenRed.visibility = View.VISIBLE
            } else {
                if (MyApp.isAdmin != "1") {
                    llOpenHigh.visibility = View.VISIBLE
                    llChat.visibility = View.VISIBLE
                    llOpenRed.visibility = View.GONE
                } else {
                    llOpenHigh.visibility = View.GONE
                    llChat.visibility = View.VISIBLE
                    llOpenRed.visibility = View.VISIBLE
                }
            }

            if (is_follow == 1) {
                tvFollow.text = "取消关注"
            } else {
                tvFollow.text = "关注"
            }



            tvUserName.text = serial_id
            tvUserSexAndAge.text = user_info.age
            tvUserCity.text = if (user_info.province == user_info.city) user_info.city else "${user_info.province} ${user_info.city}"
            if (user_info.location_city_switch == "1") {
                tvUserCity.visibility = View.GONE
                ivUsercity.visibility = View.VISIBLE
            } else {
                if (TextUtil.isEmpty(user_info.province) && TextUtil.isEmpty(user_info.city)) {
                    tvUserCity.visibility = View.GONE
                    ivUsercity.visibility = View.VISIBLE
                } else {
                    tvUserCity.visibility = View.VISIBLE
                    ivUsercity.visibility = View.GONE
                    tvUserCity.text = if (user_info.province == "" || user_info.province == user_info.city) {
                        user_info.city
                    } else {
                        "${user_info.province}  ${user_info.city}"
                    }
                }

            }
            var isTop = if (chat_id == MyApp.uid || MyApp.isAdmin == "1" || is_top_mine == "1") {
                "1"
            } else {
                "0"
            }

            suitText(tvHight, user_info.tall + "cm", user.user_info.sex, "1")
            suitText(tvWeight, user_info.weight + "kg", user.user_info.sex, "1")
            suitText(tvConstellation, user_info.starchar, user.user_info.sex, isTop)
            suitText(tvDegree, user_info.culture, user.user_info.sex, isTop)
            suitText(tvMonthly, user_info.monthly, user.user_info.sex, isTop)
            suitText(tvWant, user_info.want, user.user_info.sex, isTop)

            if (top_desc_hidden == "1") {
                if (chat_id == MyApp.uid || MyApp.isAdmin == "1") {
                    llDuBai.visibility = View.VISIBLE
                    tvMonogue.text = top_desc
                } else {
                    llDuBai.visibility = View.GONE
                }
            } else {
                if (TextUtil.isEmpty(top_red_desc)) {
                    llDuBai.visibility = View.VISIBLE
                    tvMonogue.text = top_desc
                } else {
                    if (is_top_mine == "1" || chat_id == MyApp.uid || MyApp.isAdmin == "1") {
                        llDuBai.visibility = View.VISIBLE
                        tvMonogue.text = top_desc
                    } else {
                        llDuBai.visibility = View.VISIBLE
                        tvMonogue.text = top_desc
                        // 遮罩
                        tvMonogue.BlurFliter()
                        tvHighDescBlur.visibility = View.VISIBLE
                    }
                }
            }
            if (top_red_desc_hidden == "1") {
                if (chat_id == MyApp.uid || MyApp.isAdmin == "1") {
                    tvRecommd.text = top_red_desc
                } else {
                    tvRecommd.text = ""
                    tvRecommd.hint = ""
                }
            } else {
                if (!TextUtil.isEmpty(top_red_desc)) {
                    llRedRecommd.visibility = View.VISIBLE
                    tvRecommd.visibility = View.VISIBLE
                    tvRecommd.text = top_red_desc
                } else {
                    llRedRecommd.visibility = View.GONE
                }
            }


            if (TextUtil.isEmpty(match_service_string)) {
                tvRedStatus.visibility = View.GONE
            } else {
                tvRedStatus.visibility = View.VISIBLE
                tvRedStatus.text = "服务状态：${match_service_string}"
            }

            if (top_desc_hidden == "1") {
                ivDescLock.visibility = View.VISIBLE
            } else {
                ivDescLock.visibility = View.GONE
            }
            if (top_red_desc_hidden == "1") {
                ivRecommdLock.visibility = View.VISIBLE
            } else {
                ivRecommdLock.visibility = View.GONE
            }




            if ("S" == user_info.role_string) {
                tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                tvUserRole.text = "斯"
            } else if ("M" == user_info.role_string) {
                tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                tvUserRole.text = "慕"
            } else if ("SM" == user_info.role_string) {
                tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                tvUserRole.text = "双"
            } else {
                tvUserRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
                tvUserRole.text = user_info.role_string
            }

            when (user.user_info.sex) {
                "1" -> {
                    val drawable = ContextCompat.getDrawable(this@HighEndUserActivity, R.mipmap.nan)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    tvUserSexAndAge.setCompoundDrawables(drawable, null, null, null)
                    tvUserSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                }
                "2" -> {
                    val drawable = ContextCompat.getDrawable(this@HighEndUserActivity, R.mipmap.nv)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    tvUserSexAndAge.setCompoundDrawables(drawable, null, null, null)
                    tvUserSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                }
                else -> {
                    val drawable = ContextCompat.getDrawable(this@HighEndUserActivity, R.mipmap.san)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    tvUserSexAndAge.setCompoundDrawables(drawable, null, null, null)
                    tvUserSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                }
            }

            if (user_info.realname == "1") {
                ivRealId.visibility = View.VISIBLE
            } else {
                ivRealId.visibility = View.GONE
            }
            if (user_info.video_auth_status == "1") {
                ivVidoeAuth.visibility = View.VISIBLE
            } else {
                ivVidoeAuth.visibility = View.GONE
            }
            if (user_info.realids == "1") {
                ivRealName.visibility = View.VISIBLE
            } else {
                ivRealName.visibility = View.GONE
            }
            if (top_cc_status == "1") {
                ivCaichan.visibility = View.VISIBLE
            } else {
                ivCaichan.visibility = View.GONE
            }
            if (top_jk_status == "1") {
                ivJiankang.visibility = View.VISIBLE
            } else {
                ivJiankang.visibility = View.GONE
            }
            if (top_xl_status == "1") {
                ivXueli.visibility = View.VISIBLE
            } else {
                ivXueli.visibility = View.GONE
            }
            if (top_jn_status == "1") {
                ivJineng.visibility = View.VISIBLE
            } else {
                ivJineng.visibility = View.GONE
            }
            if (top_qt_status == "1") {
                ivQita.visibility = View.VISIBLE
            } else {
                ivQita.visibility = View.GONE
            }
        }


    }

    private fun suitText(tv: TextView?, str: String?, sex: String?, isTop: String) {
        if (tv == null) {
            return
        }

        if (isTop == "1") {
            if (TextUtil.isEmpty(str)) {
                tv.text = "-"
            } else {
                tv.text = str
            }
            if ("1" == sex) { //男
                tv.setTextColor(resources.getColor(R.color.boyColor))
            } else if ("2" == sex) { //女
                tv.setTextColor(resources.getColor(R.color.girlColor))
            } else {
                tv.setTextColor(resources.getColor(R.color.boyColor))
            }
        } else {
            tv.text = "高端可见"
            tv.setTextColor(resources.getColor(R.color.color_high_title))
        }

    }

    private var TAG = "HighEndUser"
    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume")
        getData()
    }
}