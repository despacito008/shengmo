package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.*
import com.aiwujie.shengmo.eventbus.MainPageTurnEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.adapter.HighRedMenAdapter
import com.aiwujie.shengmo.kt.adapter.HomeHighUserAdapter
import com.aiwujie.shengmo.kt.adapter.OpenHighRuleAdapter
import com.aiwujie.shengmo.kt.bean.NormalResultBean
import com.aiwujie.shengmo.kt.bean.SelfieStateBean
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.MyAuthActivity
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.HighServiceDetailActivity
import com.aiwujie.shengmo.kt.ui.view.OpenHighTipPop
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toJsonBean
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.AliPayMentTaskManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.WxPayMentTaskManager
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.bigkoo.alertview.AlertView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject

class HighServiceOpenActivity : NormalListActivity() {


    //  红娘view
    private lateinit var menRecycleview: RecyclerView
    private lateinit var openRuleVieW: RecyclerView

    private lateinit var llOpenNan: LinearLayout
    private lateinit var llOpenNv: LinearLayout
    private lateinit var rlOpenContent: RelativeLayout

    private lateinit var llNanOpen: LinearLayout

    private lateinit var llNvOpen: LinearLayout
    private lateinit var tvNvFree: TextView
    private lateinit var tvNvOpenAuth: TextView

    private lateinit var tvRecharge: TextView
    private lateinit var ivUserImg: CircleImageView
    private lateinit var tvEndTime: TextView
    private lateinit var tvTopDesc: TextView
    private lateinit var llTopDesc: LinearLayout


    private lateinit var highRedMenAdapter: HighRedMenAdapter
    private lateinit var openHighRuleAdapter: OpenHighRuleAdapter

    private lateinit var ruleList: ArrayList<OpenHighRuleBean.DataBean>
    private lateinit var menList: ArrayList<RedwomenMarkerData.DataBean>

    private lateinit var headView: View
    private var page = 0
    private var homeHighUserAdapter: HomeHighUserAdapter? = null
    private lateinit var mList: ArrayList<HighUserBean>
    private var mSex = ""
    private var mBagId = ""
    private var userTopId: String? = ""
    private var authStatus: String = ""
    private var isTop: String = ""
    override fun initViewComplete() {
        super.initViewComplete()
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        flRootView.setPadding(0,0,0,40)


        loadUserInfo()
        mList = ArrayList()
        ruleList = ArrayList()
        menList = ArrayList()
        mSex = "mysex".getSpValue("")
        initHeadView()
        setListener()
    }


    private fun loadUserInfo() {
        HttpHelper.getInstance().checkSelefTop(object : HttpCodeMsgListener {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, CheckUserTopBean::class.java)
                model?.data?.run {
                    userTopId = top_id
                    isTop = is_top_user
                    if (is_top_user == "1") {
                        tvNormalTitleMore.text = "明细"
                    } else {
                        tvNormalTitleMore.text = "高端会员"
                    }

                    if (!TextUtil.isEmpty(top_overtime)) {
                        tvEndTime.visibility = View.VISIBLE
                        tvEndTime.text = "高端交友到期时间:${top_overtime}"
                    } else {
                        tvEndTime.visibility = View.GONE
                    }

                }
            }

            override fun onFail(code: Int, msg: String?) {
//                    msg?.showToast()
                ivNormalTitleMore.visibility = View.INVISIBLE
            }

        })
    }


    private fun initHeadView() {
        headView = View.inflate(this, R.layout.app_layout_high_service_header, null)
        menRecycleview = headView.findViewById(R.id.menRecycleview)
        openRuleVieW = headView.findViewById(R.id.openRuleVieW)
        llOpenNan = headView.findViewById(R.id.ll_open_nan)
        llOpenNv = headView.findViewById(R.id.ll_open_nv)
        rlOpenContent = headView.findViewById(R.id.rl_open_content)
        llNanOpen = headView.findViewById(R.id.ll_nanOpen)
        llNvOpen = headView.findViewById(R.id.ll_openNv)
        tvNvFree = headView.findViewById(R.id.tv_nvFree)
        tvNvOpenAuth = headView.findViewById(R.id.tv_open_auth)
        tvRecharge = headView.findViewById(R.id.tv_recharge)
        tvEndTime = headView.findViewById(R.id.tv_endTime)
        tvTopDesc = headView.findViewById(R.id.tv_top_desc)
        llTopDesc = headView.findViewById(R.id.ll_top_desc)
        menRecycleview.isFocusableInTouchMode = false
        openRuleVieW.isFocusableInTouchMode = false

        if (mSex == "1") {
            btnBgChanged("男")
            llNanOpen.visibility = View.VISIBLE
            llNvOpen.visibility = View.GONE
            getRule()
        } else {
            btnBgChanged("女")
            llNanOpen.visibility = View.GONE
            llNvOpen.visibility = View.VISIBLE
        }

        tvNvOpenAuth.setOnClickListener {
            authStatus.run {
                if (isTop != "1") {
                    when (authStatus) {
                        "1" -> {
                            showOpenPop()
                        }
                        else -> {
                            val intent = Intent(this@HighServiceOpenActivity, MyAuthActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

            }
        }

        getRedMen()
        getConfig()
        getSelfieState()
    }

    private fun showOpenPop() {
        var pop = OpenHighTipPop(this@HighServiceOpenActivity, "提示")
        pop.onSimpleItemListener = OnSimpleItemListener {
            isTop == "1"
            tvNvOpenAuth.text = "您已开通高端交友"
            tvNvOpenAuth.background = ContextCompat.getDrawable(this@HighServiceOpenActivity, R.drawable.bg_high_open_end)
        }
        pop.showPopupWindow()
    }


    private fun getSelfieState() {
        HttpHelper.getInstance().getSelfieState(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val result = data?.toJsonBean<NormalResultBean<SelfieStateBean>>()
                result?.data?.run {
                    authStatus = status
                    if (isTop == "1") {
                        tvNvOpenAuth.text = "您已开通高端交友"
                        tvNvOpenAuth.background = ContextCompat.getDrawable(this@HighServiceOpenActivity, R.drawable.bg_high_open_end)
                    } else {
                        if (status == "1") {
                            tvNvOpenAuth.text = "免费开通高端交友"
                            tvNvOpenAuth.background = ContextCompat.getDrawable(this@HighServiceOpenActivity, R.drawable.bg_high_recharge)
                        } else {
                            tvNvOpenAuth.text = "去自拍认证"
                            tvNvOpenAuth.background = ContextCompat.getDrawable(this@HighServiceOpenActivity, R.drawable.bg_high_recharge)
                        }
                    }

                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun getData() {
        HttpHelper.getInstance().getHighList(mSex, page.toString(), "3", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, HighBeanModel::class.java)
                if (model?.data == null) {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    loadingHolder.showEmpty()
                    return
                }
                model.data?.run {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    loadingHolder.showLoadSuccess()
                    when (page) {
                        0 -> {
                            mList.clear()
                            mList.addAll(this)
                            with(rvList) {
                                homeHighUserAdapter = HomeHighUserAdapter(context, mList)
                                val headerAdapter = HeaderViewAdapter(homeHighUserAdapter)
                                headerAdapter.addHeaderView(headView)
                                layoutManager = HeaderViewGridLayoutManager(context, 1, headerAdapter)
                                adapter = headerAdapter
                                homeHighUserAdapter?.setOnSimpleItemListener(OnSimpleItemListener {
                                    startActivity(Intent(context, HighEndUserActivity::class.java).putExtra(IntentKey.UID, mList[it].top_id))
                                })
                            }
                        }
                        else -> {
                            val tempIndex = mList.size
                            mList.addAll(this)
                            homeHighUserAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                loadingHolder.showLoadSuccess()
                msg?.showToast()
            }
        })

    }

    private fun notifyData() {
        HttpHelper.getInstance().getHighList(mSex, page.toString(), "3", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, HighBeanModel::class.java)
                if (model?.data == null) {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    loadingHolder.showEmpty()
                    return
                }
                model.data?.run {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    loadingHolder.showLoadSuccess()

                    mList.clear()
                    mList.addAll(this)
                    homeHighUserAdapter?.notifyDataSetChanged()

                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                loadingHolder.showLoadSuccess()
                msg?.showToast()
            }
        })
    }

    private fun getConfig() {
        HttpHelper.getInstance().getTopConfig(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, ConfigModel::class.java)
                model?.data?.run {
                    if (!TextUtil.isEmpty(top_text)) {
                        llTopDesc.visibility = View.VISIBLE
                        tvTopDesc.text = top_text
                    } else {
                        llTopDesc.visibility = View.GONE
                    }

                    tvNvFree.text = top_lady_tips
                }

            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }


    private fun getRule() {
        HttpHelper.getInstance().getHighRule(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, OpenHighRuleBean::class.java)
                model?.data?.run {
                    ruleList.clear()
                    ruleList.addAll(this)
                    openHighRuleAdapter = OpenHighRuleAdapter(this@HighServiceOpenActivity, ruleList)
                    with(openRuleVieW) {
                        layoutManager = GridLayoutManager(this@HighServiceOpenActivity, 2)
                        adapter = openHighRuleAdapter
                        mBagId = ruleList[0].bag_id
                        openHighRuleAdapter.setOnSimpleItemListener(OnSimpleItemListener {
                            mBagId = ruleList[it].bag_id
                        })

                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }

        })
    }

    private fun getRedMen() {
        HttpHelper.getInstance().getRedMenList(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val data: RedwomenMarkerData = GsonUtil.GsonToBean(data, RedwomenMarkerData::class.java)
                data.data?.run {
                    menList.clear()
                    menList.addAll(this)
                    highRedMenAdapter = HighRedMenAdapter(this@HighServiceOpenActivity, menList)

                    with(menRecycleview) {
                        var spountCounts = if (menList.size < 4) {
                            menList.size
                        } else {
                            4
                        }
                        layoutManager = GridLayoutManager(this@HighServiceOpenActivity, spountCounts)
                        adapter = highRedMenAdapter
                    }

                    highRedMenAdapter.onSimpleItemListener = OnSimpleItemListener {
                        startActivity(Intent(this@HighServiceOpenActivity, UserInfoActivity::class.java).putExtra(IntentKey.UID, menList[it].uid))

                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    @SuppressLint("ClickableViewAccessibility")
    fun setListener() {
        tvNormalTitleMore.setOnClickListener {

            HttpHelper.getInstance().checkSelefTop(object : HttpCodeMsgListener {
                override fun onSuccess(data: String?, msg: String?) {
                    val model = GsonUtil.GsonToBean(data, CheckUserTopBean::class.java)
                    model?.data?.run {
                        if (is_top_user == "1") {
                            startActivity(Intent(this@HighServiceOpenActivity, HighServiceDetailActivity::class.java).putExtra(IntentKey.UID, top_id))
                        } else {
                            EventBus.getDefault().post(MainPageTurnEvent(0, 3))
                            finish()
                        }


                    }
                }

                override fun onFail(code: Int, msg: String?) {
                    msg?.showToast()
                }

            })

        }
        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                loadData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                loadData()
            }
        })

        llOpenNan.setOnClickListener {
            btnBgChanged("男")
            llNanOpen.visibility = View.VISIBLE
            llNvOpen.visibility = View.GONE
            mSex = "1"
            notifyData()
            getRule()

        }


        llOpenNv.setOnClickListener {
            btnBgChanged("女")
            llNanOpen.visibility = View.GONE
            llNvOpen.visibility = View.VISIBLE
            mSex = "2"
            if ("mysex".getSpValue("") == "1") {
                tvNvOpenAuth.visibility = View.INVISIBLE
            }
            notifyData()
        }


        tvRecharge.setOnClickListener {
            showPayMenu()
        }


    }


    private fun btnBgChanged(str: String) {
        Log.v("btnBgChanged", str)
        when (str) {
            "男" -> {
                rlOpenContent.background = ContextCompat.getDrawable(this, R.drawable.bg_nan_open_high)
                llOpenNv.setBackgroundResource(R.drawable.bg_open_high_nv_btn)
                llOpenNan.setBackgroundResource(R.drawable.bg_high_nan_btn)

            }
            "女" -> {
                rlOpenContent.background = ContextCompat.getDrawable(this, R.drawable.bg_nv_open_high)
                llOpenNan.setBackgroundResource(R.drawable.bg_open_high_nan_btn)
                llOpenNv.setBackgroundResource(R.drawable.bg_high_nv_btn)

            }

        }

    }


    private fun showPayMenu() {
        if (mBagId.isEmpty()) {
            "请选择充值".showToast()
            return
        }

        val alertView = AlertView(null, null, "取消", null, arrayOf("微信", "支付宝"),
                this@HighServiceOpenActivity, AlertView.Style.ActionSheet, null)
        alertView.setOnItemClickListener { _: Any?, _: Int, data: String ->
            FloatWindowLayout.getInstance().clearAnchorFloat()
            if ("支付宝" == data) {
                payByAli()
            } else {
                payByWeChat()
            }
        }
        alertView.show()
    }

    private fun payByAli() {
        try {
            val `object` = JSONObject()
            `object`.put("token", MyApp.token)
            `object`.put("bag_id", mBagId)
            AliPayMentTaskManager(this@HighServiceOpenActivity, HttpUrl.openHighAliPay, `object`.toString(), "High")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun payByWeChat() {
        try {
            val `object` = JSONObject()

            `object`.put("bag_id", mBagId)
            `object`.put("token", MyApp.token)
            Log.v("WxPayMentManager Token", MyApp.token)
            WxPayMentTaskManager(this@HighServiceOpenActivity, HttpUrl.openHighWechat, `object`.toString(), "high")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun getPageTitle(): String {
        return "高端交友"
    }

    override fun loadData() {
        loadingHolder.showLoading()
        getData()

    }
}