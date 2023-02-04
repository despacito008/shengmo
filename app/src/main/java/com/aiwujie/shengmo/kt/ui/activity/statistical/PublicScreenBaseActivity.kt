package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.utils.TextViewUtil
import com.gyf.immersionbar.ImmersionBar


abstract class PublicScreenBaseActivity : BaseActivity() {
    lateinit var ivNormalTitleBack: ImageView
    lateinit var tvNormalTitleTitle: TextView
    lateinit var ivNormalTitleMore: ImageView
    lateinit var tvNormalTitleMore: TextView
    lateinit var tabStatistical: TabLayout
    lateinit var vpStatistical: ViewPager
    lateinit var rootView: LinearLayout
    lateinit var topLine: TextView
    lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_public_screen)
        ImmersionBar.with(this).init()
        initView()
    }



    fun initView() {
        ivNormalTitleBack = findViewById(R.id.iv_normal_title_back)
        tvNormalTitleTitle = findViewById(R.id.tv_normal_title_title)
        tvNormalTitleMore = findViewById(R.id.tv_normal_title_more)
        ivNormalTitleMore = findViewById(R.id.iv_normal_title_more)
        tabStatistical = findViewById(R.id.tab_statistical)
        vpStatistical = findViewById(R.id.view_pager_statistical)
        tvNormalTitleTitle.text = getPageTitle()
        ivNormalTitleMore.visibility = View.INVISIBLE
        rootView = findViewById(R.id.ll_root_view)
        tabStatistical.tabMode = TabLayout.MODE_FIXED
        titleList = getTitleList() as ArrayList<String>
        fragmentList = getFragmentList() as ArrayList<Fragment>
        val mAdapter = RankMyPagerAdapter(supportFragmentManager, titleList, fragmentList)
        vpStatistical.adapter = mAdapter
        tabStatistical.setupWithViewPager(vpStatistical)
        vpStatistical.offscreenPageLimit = fragmentList.size
        for (i in titleList.indices) {
            tabStatistical.getTabAt(i)!!.customView = getTabView(i)
        }
        selectTab(0)
        setListener()
        rootView.setBackgroundColor(Color.parseColor("#160122"))
        tvNormalTitleTitle.setTextColor(resources.getColor(R.color.white))
        tabStatistical.setBackgroundColor(resources.getColor(R.color.transparent))
        topLine = findViewById(R.id.tv_top_line)
        topLine.setBackgroundColor(resources.getColor(R.color.transparent))
        tabStatistical.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#db57f3"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivNormalTitleBack.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
        initViewComplete()
    }

    open fun initViewComplete() {}


    fun setListener() {
        ivNormalTitleBack.setOnClickListener {
            finish()
        }

        vpStatistical.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                selectTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        tabStatistical.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                chooseTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                unChooseTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    //    @OnClick(R.id.mStampBill_return)
    //    public void onViewClicked() {
    //        finish();
    //    }
    open fun getTabView(position: Int): View? {
        val view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout_public_screen, null)
        val tvItemTitle = view.findViewById(R.id.item_custom_tab_tv) as TextView
        tvItemTitle.text = titleList[position]
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        return view
    }

    open fun selectTab(position: Int) {
        for (i in titleList.indices) {
            unChooseTab(position)
        }
        for (i in titleList.indices) {
            if (i == position) {
                chooseTab(position)
            }
        }
    }

    open fun chooseTab(position: Int) {
        val view: View = tabStatistical.getTabAt(position)!!.customView!!
        val tvTitle = view.findViewById(R.id.item_custom_tab_tv) as TextView
        val tvCount = view.findViewById(R.id.tv_public_screen_count) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.VISIBLE
        TextViewUtil.setBold(tvTitle)
        tvTitle.setTextColor(resources.getColor(R.color.purpleColor))
        if (tvCount.visibility == View.VISIBLE) {
            tvCount.postDelayed({
                tvCount.visibility = View.GONE
            }, 1000)
        }
    }

    open fun chooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        val txt_title = view!!.findViewById(R.id.item_custom_tab_tv) as TextView
        val tvCount = view.findViewById(R.id.tv_public_screen_count) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.VISIBLE
        TextViewUtil.setBold(txt_title)
        txt_title.setTextColor(resources.getColor(R.color.purple_main))
        if (tvCount.visibility == View.VISIBLE) {
            tvCount.postDelayed({
                tvCount.visibility = View.GONE
            }, 1000)
        }
    }

    open fun unChooseTab(position: Int) {
        val view: View = tabStatistical.getTabAt(position)!!.customView!!
        val txt_title = view.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        TextViewUtil.setNormal(txt_title)
        txt_title.setTextColor(resources.getColor(R.color.white))
    }

    open fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        val txt_title = view!!.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        TextViewUtil.setNormal(txt_title)
        txt_title.setTextColor(resources.getColor(R.color.white))
    }

    fun getRootView(): View {
        return rootView
    }

    abstract fun getFragmentList(): List<Fragment>

    abstract fun getTitleList(): List<String>

    abstract fun getPageTitle(): String

    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}