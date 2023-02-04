package com.aiwujie.shengmo.kt.ui.fragment.tabtopbar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.activity.HomeFilterActivity
import com.aiwujie.shengmo.eventbus.*
import com.aiwujie.shengmo.fragment.mainfragment.FragmentNear
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.ui.activity.SearchHighUserActivity
import com.aiwujie.shengmo.kt.ui.activity.SearchUserActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.PublicScreenActivity
import com.aiwujie.shengmo.kt.ui.fragment.HomePageHighFragment
import com.aiwujie.shengmo.kt.ui.fragment.HomePageNearFragment
import com.aiwujie.shengmo.kt.ui.fragment.HomePageRecommendFragment
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.utils.FilterGroupUtils
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment.tabtopbar
 * @ClassName: HomePageAroundFragment
 * @Author: xmf
 * @CreateDate: 2022/4/24 11:23
 * @Description: 首页 - 身边
 */
class HomePageAroundFragment : TabTopBarFragment() {
    lateinit var nearRecommendFragment: HomePageRecommendFragment
    lateinit var nearNearFragment: HomePageNearFragment
    lateinit var nearOnlineFragment: HomePageNearFragment
    lateinit var homePageHighFragment: HomePageHighFragment
    override fun getFragmentList(): List<Fragment> {
        nearRecommendFragment = HomePageRecommendFragment()
        nearNearFragment = HomePageNearFragment.newInstance(1)
        nearOnlineFragment = HomePageNearFragment.newInstance(7)
        homePageHighFragment = HomePageHighFragment.newInstance()
        return arrayListOf(nearRecommendFragment, nearNearFragment, nearOnlineFragment, homePageHighFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("推荐", "附近", "在线", "高端")
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        EventBus.getDefault().register(this)
        getSharedprefrence()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    lateinit var tvPublicScreenDot: TextView
    override fun initTabLeftView() {
        super.initTabLeftView()
        val screenView = View.inflate(activity, R.layout.app_layout_public_screen, null)
        tvPublicScreenDot = screenView.findViewById(R.id.tv_home_notice_horn)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        screenView.layoutParams = params
        flLeft.addView(screenView)
        screenView.setOnClickListener {
            gotoPublicScreenPage()
        }
    }

    lateinit var ivScreen: ImageView
    override fun initTabRightView() {
        super.initTabRightView()
        ivScreen = ImageView(activity)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivScreen.setImageResource(R.drawable.shuaixuan)
        ivScreen.layoutParams = params
        ivScreen.setPadding(15.dp, 15.dp, 15.dp, 15.dp)
        flRight.addView(ivScreen)
        ivScreen.clickDelay {
            if (vpTabTopBar.currentItem == 3){
                showHighScreenPop()
            }else {
                showScreenPop()
            }

        }
    }

    private fun gotoPublicScreenPage() {
        val intent = Intent(activity, PublicScreenActivity::class.java)
        startActivity(intent)
    }


    var isFistLoad = true
    private var onlinestate = ""
    private var realname = ""
    private var age = ""
    private var sex = ""
    private var sexual = ""
    private var role = ""
    private var culture = ""
    private var monthly = ""
    private var upxzya = ""
    var llMenuShowType: LinearLayout? = null
    var ivMenuShowType: ImageView? = null
    var tvMenuShowType: TextView? = null
    private var mPopWindow: PopupWindow? = null
    private var mHighPopWindow: PopupWindow? = null
    private fun showScreenPop() {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.item_home_pop, null)
        mPopWindow = PopupWindow(contentView)
        with(mPopWindow!!) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            setOnDismissListener { activity.alphaBackground(1f) }
            animationStyle = R.style.AnimationPreview
            showAsDropDown(flRight)
        }
        activity.alphaBackground(0.5f)
        val llSx = contentView.findViewById<View>(R.id.item_home_pop_llSx) as LinearLayout
        val llSexual = contentView.findViewById<View>(R.id.item_home_pop_llSexual) as LinearLayout
        val tvSexual = contentView.findViewById<View>(R.id.item_home_pop_tvSexual) as TextView
        val llNan = contentView.findViewById<View>(R.id.item_home_pop_llNan) as TextView
        val llNv = contentView.findViewById<View>(R.id.item_home_pop_llNv) as TextView
        val llNao = contentView.findViewById<View>(R.id.item_home_pop_llCdts) as TextView
        val llAll = contentView.findViewById<View>(R.id.item_home_pop_llAll) as TextView
        val tvSx = contentView.findViewById<View>(R.id.item_home_pop_isOpen) as TextView
        val item_home_pop_tvAdvancedScreening = contentView.findViewById<TextView>(R.id.item_home_pop_tvAdvancedScreening)
        llMenuShowType = contentView.findViewById<LinearLayout>(R.id.ll_menu_show_type)
        ivMenuShowType = contentView.findViewById<ImageView>(R.id.iv_menu_show_type)
        tvMenuShowType = contentView.findViewById<TextView>(R.id.tv_menu_show_type)

        //左上角图标状态
        if (SpKey.MODLE.getSpValue("0") == "1") {
            ivMenuShowType?.setImageResource(R.drawable.gongge)
            tvMenuShowType?.text = "宫格模式"
        } else {
            ivMenuShowType?.setImageResource(R.drawable.liebiao)
            tvMenuShowType?.text = "列表模式"
        }
        llMenuShowType?.setOnClickListener(View.OnClickListener {
            val fastClick = FragmentNear.isFastClick()
            if (!fastClick) {
                return@OnClickListener
            }
            if (SpKey.MODLE.getSpValue("0") == "1") {
                SpKey.MODLE.saveSpValue("0")
                ivMenuShowType?.setImageResource(R.drawable.liebiao)
                tvMenuShowType?.text = "列表模式"
                EventBus.getDefault().post(ChangeLayoutEvent(0))
                mPopWindow?.dismiss()
            } else {
                SpKey.MODLE.saveSpValue("1")
                ivMenuShowType?.setImageResource(R.drawable.gongge)
                tvMenuShowType?.text = "宫格模式"
                EventBus.getDefault().post(ChangeLayoutEvent(1))
                mPopWindow?.dismiss()
            }
        })
        if (SharedPreferencesUtils.getParam(activity.applicationContext, "filterZong", "") == "1") {
            tvSx.text = "已开启"
        } else {
            tvSx.text = "未开启"
        }
        onlinestate = SharedPreferencesUtils.getParam(activity.applicationContext, "filterLine", "") as String
        realname = SharedPreferencesUtils.getParam(activity.applicationContext, "ckAuthen", "") as String
        age = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpAge", "") as String
        sex = SharedPreferencesUtils.getParam(activity.applicationContext, "filterSex", "") as String
        sexual = SharedPreferencesUtils.getParam(activity.applicationContext, "filterQx", "") as String
        role = SharedPreferencesUtils.getParam(activity.applicationContext, "filterRole", "") as String
        culture = SharedPreferencesUtils.getParam(activity.applicationContext, "filterCulture", "") as String
        monthly = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpMoney", "") as String
        upxzya = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpxzya", "") as String
        if (activity.intent.getIntExtra("gotoByRegister", -1) != -1 && isFistLoad) {
            when (FilterGroupUtils.isWhatSexual2(sex, sexual)) {
                "1" -> {
                    llNan.setTextColor(Color.parseColor("#b73acb"))
                }
                "2" -> {
                    llNv.setTextColor(Color.parseColor("#b73acb"))
                }
                "3" -> {
                    llNao.setTextColor(Color.parseColor("#b73acb"))
                }
                else -> {
                    llAll.setTextColor(Color.parseColor("#b73acb"))
                }
            }
        } else {
            if (onlinestate == "" && realname == "" && age == "" && sex == "" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llAll.setTextColor(Color.parseColor("#b73acb"))
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "1" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llNan.setTextColor(Color.parseColor("#b73acb"))
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "2" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llNv.setTextColor(Color.parseColor("#b73acb"))
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "3" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llNao.setTextColor(Color.parseColor("#b73acb"))
            }
        }


        //筛选取向选中
        if (SharedPreferencesUtils.getParam(activity.applicationContext, "nearSexual", "") == "1") {
            tvSexual.setTextColor(Color.parseColor("#b73acb"))
        }

        llSx.clickDelay {
            val intent = Intent(activity, HomeFilterActivity::class.java)
            activity.startActivity(intent)
            mPopWindow?.dismiss()
        }

        llNan.clickDelay {
            sex = "1"
            view!!.isSelected = true
            simpleSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
            mPopWindow?.dismiss()
        }
        llNv.clickDelay {
            sex = "2"
            view!!.isSelected = true
            simpleSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
            mPopWindow?.dismiss()
        }
        llNao.clickDelay {
            sex = "3"
            view!!.isSelected = true
            simpleSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
            mPopWindow?.dismiss()
        }
        llAll.clickDelay {
            sex = ""
            view!!.isSelected = true
            simpleSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.shuaixuan)
            mPopWindow?.dismiss()
        }
        llSexual.clickDelay {
            //根据自己性取向筛选
            val ownerSex = SharedPreferencesUtils.getParam(activity.applicationContext, "mysex", "") as String
            val ownerSexual = SharedPreferencesUtils.getParam(activity.applicationContext, "mysexual", "") as String
            if (ownerSexual != "" && ownerSex != "") {
                simpleSp(ownerSex, ownerSexual, "1", true)
                view!!.isSelected = true
                SharedPreferencesUtils.setParam(activity.applicationContext, "nearSexual", "1")
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            } else {
                ToastUtil.show(activity.applicationContext, "请您重新登录,才可以使用此功能...")
            }
            mPopWindow?.dismiss()
        }

        isFistLoad = false
        val llSearch = contentView.findViewById<LinearLayout>(R.id.ll_normal_search)
        llSearch.setOnClickListener { //Intent intent = new Intent(getActivity(), SearchActivity.class);
            val intent = Intent(activity, SearchUserActivity::class.java)
            startActivity(intent)
        }
        val llRule = contentView.findViewById<LinearLayout>(R.id.ll_normal_rule)
        llRule.setOnClickListener {
            val intent = Intent(activity, BannerWebActivity::class.java)
            intent.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/2")
            intent.putExtra("title", "圣魔")
            startActivity(intent)
        }
    }

    private fun showHighScreenPop() {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.app_item_home_high, null)
        mHighPopWindow = PopupWindow(contentView)
        with(mHighPopWindow!!) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            setOnDismissListener { activity.alphaBackground(1f) }
            animationStyle = R.style.AnimationPreview
            showAsDropDown(flRight)
        }
        activity.alphaBackground(0.5f)
        val llNan = contentView.findViewById<View>(R.id.item_home_pop_llNan) as TextView
        val llNv = contentView.findViewById<View>(R.id.item_home_pop_llNv) as TextView
        val llAll = contentView.findViewById<View>(R.id.item_home_pop_llAll) as TextView
        llMenuShowType = contentView.findViewById<LinearLayout>(R.id.ll_menu_show_type)
        ivMenuShowType = contentView.findViewById<ImageView>(R.id.iv_menu_show_type)
        tvMenuShowType = contentView.findViewById<TextView>(R.id.tv_menu_show_type)

        //左上角图标状态
        if (SpKey.MODLE.getSpValue("0") == "1") {
            ivMenuShowType?.setImageResource(R.drawable.gongge)
            tvMenuShowType?.text = "宫格模式"
        } else {
            ivMenuShowType?.setImageResource(R.drawable.liebiao)
            tvMenuShowType?.text = "列表模式"
        }
        llMenuShowType?.setOnClickListener(View.OnClickListener {
            val fastClick = FragmentNear.isFastClick()
            if (!fastClick) {
                return@OnClickListener
            }
            if (SpKey.MODLE.getSpValue("0") == "1") {
                SpKey.MODLE.saveSpValue("0")
                ivMenuShowType?.setImageResource(R.drawable.liebiao)
                tvMenuShowType?.text = "列表模式"
                EventBus.getDefault().post(ChangeLayoutEvent(0))
                mPopWindow?.dismiss()
                mHighPopWindow?.dismiss()
            } else {
                SpKey.MODLE.saveSpValue("1")
                ivMenuShowType?.setImageResource(R.drawable.gongge)
                tvMenuShowType?.text = "宫格模式"
                EventBus.getDefault().post(ChangeLayoutEvent(1))
                mPopWindow?.dismiss()
                mHighPopWindow?.dismiss()
            }
        })
        if (activity.intent.getIntExtra("gotoByRegister", -1) != -1 && isFistLoad) {
            when (FilterGroupUtils.isWhatSexual2(sex, sexual)) {
                "1" -> {
                    llNan.setTextColor(Color.parseColor("#b73acb"))
                }
                "2" -> {
                    llNv.setTextColor(Color.parseColor("#b73acb"))
                }
                else -> {
                    llAll.setTextColor(Color.parseColor("#b73acb"))
                }
            }
        } else {
            if (onlinestate == "" && realname == "" && age == "" && sex == "" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llAll.setTextColor(Color.parseColor("#b73acb"))
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "1" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llNan.setTextColor(Color.parseColor("#b73acb"))
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "2" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                llNv.setTextColor(Color.parseColor("#b73acb"))
            }
        }

        llNan.clickDelay {
            sex = "1"
            view!!.isSelected = true
            simpleHighSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
            mHighPopWindow?.dismiss()
        }
        llNv.clickDelay {
            sex = "2"
            view!!.isSelected = true
            simpleHighSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
            mHighPopWindow?.dismiss()
        }
        llAll.clickDelay {
            sex = ""
            view!!.isSelected = true
            simpleHighSp(sex, "", "1", false)
            ivScreen.setImageResource(R.drawable.shuaixuan)
            mHighPopWindow?.dismiss()
        }
        val llSearch = contentView.findViewById<LinearLayout>(R.id.ll_normal_search)
        llSearch.setOnClickListener { //Intent intent = new Intent(getActivity(), SearchActivity.class);
            SearchHighUserActivity.start(context)
        }

        isFistLoad = false
    }

    private fun simpleSp(sex: String, sexual: String, switchZ: String, isSxOwner: Boolean) {
        onlinestate = ""
        realname = ""
        age = ""
        role = ""
        culture = ""
        monthly = ""
        upxzya = ""
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterZong", switchZ)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterLine", onlinestate)
        SharedPreferencesUtils.setParam(activity.applicationContext, "ckAuthen", realname)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterUpAge", age)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterRole", role)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterCulture", culture)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterUpxzya", upxzya)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterxzya", upxzya)
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterUpMoney", monthly)
        SharedPreferencesUtils.setParam(activity.applicationContext, "nearSexual", "0")
        if (isSxOwner) {
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterSex", sexual)
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterQx", sex)
            EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sexual, sex, role, culture, monthly, upxzya))
        } else {
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterSex", sex)
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterQx", sexual)
            EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly, upxzya))
        }
        mPopWindow?.dismiss()
    }


    private fun simpleHighSp(sex: String, sexual: String, switchZ: String, isSxOwner: Boolean) {
        SharedPreferencesUtils.setParam(activity.applicationContext, "filterZong", switchZ)
        if (isSxOwner) {
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterSex", sexual)
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterQx", sex)
            EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sexual, sex, role, culture, monthly, upxzya))
        } else {
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterSex", sex)
            SharedPreferencesUtils.setParam(activity.applicationContext, "filterQx", sexual)
            EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly, upxzya))
        }
        mHighPopWindow?.dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessages(event: BigHornEvent) {
        if (event.isRedPoint == 1) {
            val labaallnum = SharedPreferencesUtils.getParam(activity.applicationContext, "labaallnum", 0) as Int
            if (labaallnum > 0) {
                tvPublicScreenDot.setText(labaallnum.toString() + "")
                tvPublicScreenDot.setVisibility(View.VISIBLE)
            }
        } else {
            tvPublicScreenDot.setVisibility(View.GONE)
        }
    }

    private fun getSharedprefrence() {
        //左上角图标状态
        onlinestate = SharedPreferencesUtils.getParam(activity.applicationContext, "filterLine", "") as String
        realname = SharedPreferencesUtils.getParam(activity.applicationContext, "ckAuthen", "") as String
        age = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpAge", "") as String
        sex = SharedPreferencesUtils.getParam(activity.applicationContext, "filterSex", "") as String
        sexual = SharedPreferencesUtils.getParam(activity.applicationContext, "filterQx", "") as String
        role = SharedPreferencesUtils.getParam(activity.applicationContext, "filterRole", "") as String
        culture = SharedPreferencesUtils.getParam(activity.applicationContext, "filterCulture", "") as String
        monthly = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpMoney", "") as String
        upxzya = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpxzya", "") as String
        EventBus.getDefault().post(SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly, upxzya))
        showSxState()
        val labaallnum = SharedPreferencesUtils.getParam(activity.applicationContext, "labaallnum", 0) as Int
        if (labaallnum > 0) {
            tvPublicScreenDot.setText(labaallnum.toString() + "")
            tvPublicScreenDot.setVisibility(View.VISIBLE)
        }
    }

    fun showSxState() {
        onlinestate = SharedPreferencesUtils.getParam(activity.applicationContext, "filterLine", "") as String
        realname = SharedPreferencesUtils.getParam(activity.applicationContext, "ckAuthen", "") as String
        age = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpAge", "") as String
        sex = SharedPreferencesUtils.getParam(activity.applicationContext, "filterSex", "") as String
        sexual = SharedPreferencesUtils.getParam(activity.applicationContext, "filterQx", "") as String
        role = SharedPreferencesUtils.getParam(activity.applicationContext, "filterRole", "") as String
        culture = SharedPreferencesUtils.getParam(activity.applicationContext, "filterCulture", "") as String
        monthly = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpMoney", "") as String
        upxzya = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpxzya", "") as String
        if (activity.intent.getIntExtra("gotoByRegister", -1) != -1 && isFistLoad) {
            val filter = FilterGroupUtils.isWhatSexual2(sex, sexual)
            if ("1" == filter) {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            } else if ("2" == filter) {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            } else if ("3" == filter) {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            } else {
                ivScreen.setImageResource(R.drawable.shuaixuan)
            }
        } else {
            if (onlinestate == "" && realname == "" && age == "" && sex == "" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                ivScreen.setImageResource(R.drawable.shuaixuan)
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "1" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "2" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            }
            if (onlinestate == "" && realname == "" && age == "" && sex == "3" && sexual == "" && role == "" && culture == "" && monthly == "" && upxzya == "") {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            }
        }

        //筛选取向选中
        if (SharedPreferencesUtils.getParam(activity.applicationContext, "nearSexual", "") == "1") {
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: HighPageTurnEvent) {
        vpTabTopBar.currentItem = 3
    }
}
