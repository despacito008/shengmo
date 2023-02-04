package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.HomeFilterActivity
import com.aiwujie.shengmo.activity.PesonInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.FilterData
import com.aiwujie.shengmo.bean.HomeNewListData
import com.aiwujie.shengmo.bean.VipAndVolunteerData
import com.aiwujie.shengmo.eventbus.HomeFilterStateEvent
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.adapter.HomeUserListviewAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.IRequestCallback
import com.aiwujie.shengmo.net.RequestFactory
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.view.gloading.Gloading
import com.google.gson.Gson
import com.handmark.pulltorefresh.library.PullToRefreshBase
import com.handmark.pulltorefresh.library.PullToRefreshGridView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MapFindActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var mMapFindAddrdetail: TextView
    lateinit var mNearSx: ImageView
    lateinit var ivNormalTitleBack: ImageView
    lateinit var tvNormalTitleTitle: TextView

    //////////////
    var page = 0
    var lat: String? = null
    var lng: String? = null
    val handler = Handler()
    var addrdetail: String? = null
    var autoLoadListener: AutoLoadListener? = null
    val mapDatas: MutableList<HomeNewListData.DataBean> = ArrayList()
    var grideAdapter: HomeUserListviewAdapter? = null

    /**
     * 判断是否是初始化Fragment
     */
    val hasStarted = false
    var mPopWindow: PopupWindow? = null
    var onlinestate = ""
    var realname = ""
    var age = ""
    var sex = ""
    var sexual = ""
    var role = ""
    var culture = ""
    var monthly = ""
    val fragments: List<Fragment>? = null
    val titles: List<TextView>? = null
    val tvs: List<ImageView>? = null
    var gridData: HomeNewListData? = null
    var isvip = ""
    var upxzya = ""
    lateinit var gLoadHolder: Gloading.Holder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_map_find_user)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)
        StatusBarUtil.showLightStatusBar(this)
        ivNormalTitleBack = findViewById(R.id.iv_normal_title_back)
        tvNormalTitleTitle = findViewById(R.id.tv_normal_title_title)
        mMapFindAddrdetail = findViewById(R.id.mMap_find_addrdetail)
        mNearSx = findViewById(R.id.iv_normal_title_more)
        refreshLayout = findViewById(R.id.refreshLayout)
        recyclerView = findViewById(R.id.recycleView)
        gLoadHolder = Gloading.getDefault().wrap(recyclerView)
        mNearSx.setImageResource(R.drawable.item_near_filter_selector)
        setClick()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getUserList()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getUserList()
            }
        })
        setData()
        mnearSxState()
        isSVIP()
        gLoadHolder.showLoading()
        getUserList()
    }

    fun setClick() {
        ivNormalTitleBack.setOnClickListener { finish() }
        tvNormalTitleTitle.text = "地图找人"
        mNearSx.setOnClickListener { showOption() }
    }

    fun setData() {
        lat = intent.getStringExtra("lat")
        lng = intent.getStringExtra("lng")
        addrdetail = intent.getStringExtra("addrdetail")
        onlinestate = SharedPreferencesUtils.getParam(applicationContext, "filterLine", "") as String
        realname = SharedPreferencesUtils.getParam(applicationContext, "ckAuthen", "") as String
        age = SharedPreferencesUtils.getParam(applicationContext, "filterUpAge", "") as String
        sex = SharedPreferencesUtils.getParam(applicationContext, "filterSex", "") as String
        sexual = SharedPreferencesUtils.getParam(applicationContext, "filterQx", "") as String
        role = SharedPreferencesUtils.getParam(applicationContext, "filterRole", "") as String
        culture = SharedPreferencesUtils.getParam(applicationContext, "filterCulture", "") as String
        monthly = SharedPreferencesUtils.getParam(applicationContext, "filterUpMoney", "") as String
        mMapFindAddrdetail!!.text = addrdetail

    }


    fun getUserList() {
        HttpHelper.getInstance().getMapUser(page, lat, lng, onlinestate, realname, age, upxzya, sex, sexual, role, culture, monthly, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                var model = GsonUtil.GsonToBean(data, HomeNewListData::class.java)
                model?.data?.run {
                    when (page) {
                        0 -> {
                            mapDatas.clear()
                            mapDatas.addAll(this)
                            with(recyclerView) {
                                layoutManager = LinearLayoutManager(this@MapFindActivity, LinearLayoutManager.VERTICAL, false)
                                grideAdapter = HomeUserListviewAdapter(this@MapFindActivity, mapDatas, model.retcode, 0)
                                grideAdapter!!.setOnMapAdapterClickListener(object : HomeUserListviewAdapter.OnMapAdapterClickListener {
                                    override fun onClick(position: Int) {
                                        if ("1" == mapDatas[position].location_switch || "1" == mapDatas[position].location_city_switch) {
                                            val intent = Intent(this@MapFindActivity, PesonInfoActivity::class.java)
                                            intent.putExtra("uid", mapDatas[position].uid)
                                            startActivity(intent)
                                            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
                                        } else {
                                            val intent = Intent(this@MapFindActivity, PesonInfoActivity::class.java)
                                            intent.putExtra("uid", mapDatas[position].uid)
                                            startActivity(intent)
                                            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
                                        }
                                    }
                                })
                                adapter = grideAdapter


                            }

                        }
                        else -> {
                            var tempIndex = mapDatas.size;
                            mapDatas.addAll(this)
                            grideAdapter?.notifyItemRangeInserted(tempIndex, mapDatas.size)

                        }

                    }

                }

            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4002 && page == 0) {
                    gLoadHolder.showEmpty()
                }
            }
        })

    }


    //点击漏斗
    fun showOption() {
        val contentView: View = LayoutInflater.from(this@MapFindActivity).inflate(R.layout.item_home_pop, null)
        mPopWindow = PopupWindow(contentView)
        mPopWindow!!.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopWindow!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mPopWindow!!.isFocusable = true
        backgroundAlpha(0.5f)
        mPopWindow!!.setBackgroundDrawable(BitmapDrawable())
        mPopWindow!!.setOnDismissListener(poponDismissListener())
        mPopWindow!!.showAsDropDown(mNearSx)
        mPopWindow!!.animationStyle = R.style.AnimationPreview
        val llSx = contentView.findViewById<View>(R.id.item_home_pop_llSx) as LinearLayout
        val llSexual = contentView.findViewById<View>(R.id.item_home_pop_llSexual) as LinearLayout
        val tvSexual = contentView.findViewById<View>(R.id.item_home_pop_tvSexual) as TextView
        val llNan = contentView.findViewById<View>(R.id.item_home_pop_llNan) as TextView
        val llNv = contentView.findViewById<View>(R.id.item_home_pop_llNv) as TextView
        val llNao = contentView.findViewById<View>(R.id.item_home_pop_llCdts) as TextView
        val llAll = contentView.findViewById<View>(R.id.item_home_pop_llAll) as TextView
        val tvSx = contentView.findViewById<View>(R.id.item_home_pop_isOpen) as TextView
        val item_home_pop_tvAdvancedScreening = contentView.findViewById<TextView>(R.id.item_home_pop_tvAdvancedScreening)
        if ("1" == SharedPreferencesUtils.getParam(applicationContext, "filterZong", "")) {
            tvSx.text = "已开启"
            //            item_home_pop_tvAdvancedScreening.setTextColor(Color.parseColor("#b73acb"));//设置自定义高级筛选为紫色色
        } else {
            tvSx.text = "未开启"
            //            item_home_pop_tvAdvancedScreening.setTextColor(Color.parseColor("#646464"));
        }
        onlinestate = SharedPreferencesUtils.getParam(applicationContext, "filterLine", "") as String
        realname = SharedPreferencesUtils.getParam(applicationContext, "ckAuthen", "") as String
        age = SharedPreferencesUtils.getParam(applicationContext, "filterUpAge", "") as String
        sex = SharedPreferencesUtils.getParam(applicationContext, "filterSex", "") as String
        sexual = SharedPreferencesUtils.getParam(applicationContext, "filterQx", "") as String
        role = SharedPreferencesUtils.getParam(applicationContext, "filterRole", "") as String
        culture = SharedPreferencesUtils.getParam(applicationContext, "filterCulture", "") as String
        monthly = SharedPreferencesUtils.getParam(applicationContext, "filterUpMoney", "") as String
        upxzya = SharedPreferencesUtils.getParam(applicationContext, "filterUpxzya", "") as String
        if (TextUtil.isEmpty(onlinestate) && TextUtil.isEmpty(realname) && TextUtil.isEmpty(age) && TextUtil.isEmpty(sex) && TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly) && TextUtil.isEmpty(upxzya)) {
            llAll.setTextColor(Color.parseColor("#b73acb"))
        }
        if (TextUtil.isEmpty(onlinestate) && TextUtil.isEmpty(realname) && TextUtil.isEmpty(age) && "1" == sex && TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly) && TextUtil.isEmpty(upxzya)) {
            llNan.setTextColor(Color.parseColor("#b73acb"))
        }
        if (TextUtil.isEmpty(onlinestate) && TextUtil.isEmpty(realname) && TextUtil.isEmpty(age) && "2" == sex && TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly) && TextUtil.isEmpty(upxzya)) {
            llNv.setTextColor(Color.parseColor("#b73acb"))
        }
        if (TextUtil.isEmpty(onlinestate) && TextUtil.isEmpty(realname) && TextUtil.isEmpty(age) && "3" == sex && TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly) && TextUtil.isEmpty(upxzya)) {
            llNao.setTextColor(Color.parseColor("#b73acb"))
        }
        //筛选取向选中
        if ("1" == SharedPreferencesUtils.getParam(applicationContext, "nearSexual", "")) {
            tvSexual.setTextColor(Color.parseColor("#b73acb"))
        }
        llSx.setOnClickListener(this)
        llNan.setOnClickListener(this)
        llNv.setOnClickListener(this)
        llAll.setOnClickListener(this)
        llNao.setOnClickListener(this)
        llSexual.setOnClickListener(this)
    }


    fun backgroundAlpha(bgAlpha: Float) {
        val lp: WindowManager.LayoutParams = window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        window.attributes = lp
    }


    inner class poponDismissListener : PopupWindow.OnDismissListener {
        override fun onDismiss() {
            backgroundAlpha(1f)
        }

    }

    @OnClick(R.id.mNear_Sx) //R.id.mNear_findPeople,  R.id.mNear_title_near,R.id.mNear_title_hot, R.id.mNear_title_recommend,
    override fun onClick(view: View) {
        val intent: Intent
        when (view.id) {
            R.id.mNear_Sx -> showOption() //展示筛选框
            R.id.item_home_pop_llNan -> {
                sex = "1"
                mNearSx!!.setImageResource(R.mipmap.shuaixuanlv)
                //                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.isSelected = true
                simpleSp(sex, "", "1", false)
            }
            R.id.item_home_pop_llNv -> {
                sex = "2"
                mNearSx!!.setImageResource(R.mipmap.shuaixuanlv)
                //                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.isSelected = true
                simpleSp(sex, "", "1", false)
            }
            R.id.item_home_pop_llCdts -> {
                sex = "3"
                mNearSx!!.setImageResource(R.mipmap.shuaixuanlv)
                //                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.isSelected = true
                simpleSp(sex, "", "1", false)
            }
            R.id.item_home_pop_llAll -> {
                sex = ""
                mNearSx!!.setImageResource(R.mipmap.popshaixuan)
                view.isSelected = false
                simpleSp(sex, "", "", false)
            }
            R.id.item_home_pop_llSexual -> {
                //根据自己性取向筛选
                val ownerSex = SharedPreferencesUtils.getParam(applicationContext, "mysex", "") as String
                val ownerSexual = SharedPreferencesUtils.getParam(applicationContext, "mysexual", "") as String
                if (ownerSexual != "" && ownerSex != "") {
                    simpleSp(ownerSex, ownerSexual, "1", true)
                    view.isSelected = true
                    mNearSx!!.setImageResource(R.mipmap.shuaixuanlv)
                    //                    mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                    SharedPreferencesUtils.setParam(applicationContext, "nearSexual", "1")
                } else {
                    ToastUtil.show(this.applicationContext, "请您重新登录,才可以使用此功能...")
                }
            }
            R.id.item_home_pop_llSx -> {
                intent = Intent(this@MapFindActivity, HomeFilterActivity::class.java)
                startActivity(intent)
                mPopWindow!!.dismiss()
            }
        }
    }

    open fun simpleSp(sex: String, sexual: String, switchZ: String, isSxOwner: Boolean) {
        onlinestate = ""
        realname = ""
        age = ""
        role = ""
        culture = ""
        monthly = ""
        upxzya = ""
        SharedPreferencesUtils.setParam(this.applicationContext, "filterZong", switchZ)
        SharedPreferencesUtils.setParam(this.applicationContext, "filterLine", onlinestate)
        SharedPreferencesUtils.setParam(this.applicationContext, "ckAuthen", realname)
        SharedPreferencesUtils.setParam(this.applicationContext, "filterUpAge", age)
        SharedPreferencesUtils.setParam(this.applicationContext, "filterRole", role)
        SharedPreferencesUtils.setParam(this.applicationContext, "filterCulture", culture)
        SharedPreferencesUtils.setParam(this.applicationContext, "filterUpMoney", monthly)
        SharedPreferencesUtils.setParam(this.applicationContext, "nearSexual", "0")
        SharedPreferencesUtils.setParam(this.applicationContext, "filterUpxzya", upxzya)
        SharedPreferencesUtils.setParam(this.applicationContext, "filterxzya", upxzya)
        if (isSxOwner) {
            SharedPreferencesUtils.setParam(this.applicationContext, "filterSex", sexual)
            SharedPreferencesUtils.setParam(this.applicationContext, "filterQx", sex)
            EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sexual, sex, role, culture, monthly, upxzya))
        } else {
            SharedPreferencesUtils.setParam(this.applicationContext, "filterSex", sex)
            SharedPreferencesUtils.setParam(this.applicationContext, "filterQx", sexual)
            EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly, upxzya))
        }
        if (mPopWindow != null) {
            mPopWindow!!.dismiss()
        }
        getUserList()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessages(event: HomeFilterStateEvent?) {
        //右上角筛选图标状态
        mnearSxState()
    }

    fun mnearSxState() {
        if (SharedPreferencesUtils.getParam(this.applicationContext, "filterZong", "") == "1") {
//            mNearSx.setImageResource(R.mipmap.shaixuan_zise);//有颜色
            mNearSx!!.setImageResource(R.mipmap.shuaixuanlv)
        } else {
//            mNearSx.setImageResource(R.mipmap.popshaixuan);//没颜色
            mNearSx!!.setImageResource(R.mipmap.popshaixuan)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(data: FilterData) {
        upxzya = data.upxzya
        onlinestate = data.onlinestate
        realname = data.realname
        age = data.age
        sex = data.sex
        sexual = data.sexual
        role = data.role
        culture = data.culture
        monthly = data.income
        mapDatas.clear()
        if (grideAdapter != null) {
            grideAdapter!!.notifyDataSetChanged()
        }
        page = 0
        autoLoadListener!!.page = 0
        mapDatas.clear()
        getUserList()

//        mNearPullRefreshScrollview.setRefreshing();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessagee(event: SharedprefrenceEvent) {
        onlinestate = event.onlinestate
        realname = event.realname
        age = event.age
        sex = event.sex
        sexual = event.sexual
        role = event.role
        culture = event.culture
        monthly = event.monthly
        mapDatas.clear()
        if (grideAdapter != null) {
            grideAdapter!!.notifyDataSetChanged()
        }
        page = 0
        autoLoadListener!!.page = 0
        mapDatas.clear()
        getUserList()
    }

    fun isSVIP() {
        val map: MutableMap<String, String> = HashMap()
        map["uid"] = MyApp.uid
        val requestManager = RequestFactory.getRequestManager()
        requestManager.post(HttpUrl.GetUserPowerInfo, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
                PrintLogUtils.log(response, "--")
                try {
                    val `object` = JSONObject(response)
                    when (`object`.getInt("retcode")) {
                        2000 -> {
                            val data = Gson().fromJson(response, VipAndVolunteerData::class.java)
                            isvip = data.data.vip
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(throwable: Throwable) {}
        })
    }

}