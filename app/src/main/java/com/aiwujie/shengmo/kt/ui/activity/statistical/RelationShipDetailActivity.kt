package com.aiwujie.shengmo.kt.ui.activity.statistical

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
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.TextViewUtil

abstract class RelationShipDetailActivity : BaseActivity() {
    lateinit var ivNormalTitleBack: ImageView
    lateinit var ivNormalTitleMore: ImageView
    lateinit var tvNormalTitleMore: TextView
    lateinit var tabStatistical: TabLayout
    lateinit var vpStatistical: ViewPager
    lateinit var rootView:LinearLayout


    lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_tab_title)
        StatusBarUtil.showLightStatusBar(this)
        initView()
    }

    fun initView() {
        ivNormalTitleBack = findViewById(R.id.iv_normal_title_back)
        tvNormalTitleMore = findViewById(R.id.tv_normal_title_more)
        ivNormalTitleMore = findViewById(R.id.iv_normal_title_more)
        tabStatistical = findViewById(R.id.tab_statistical)
        vpStatistical = findViewById(R.id.view_pager_statistical)
        rootView = findViewById(R.id.ll_root_view)
        ivNormalTitleMore.visibility = View.GONE
        initViewComplete()
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
        initViewPagerComplete()
    }

    open fun initViewComplete() {

    }

    open fun initViewPagerComplete() {

    }

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
        val view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout_punishment, null)
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
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.VISIBLE
        TextViewUtil.setBold(tvTitle)
        tvTitle.setTextColor(
                ContextCompat.getColor(this,R.color.purple_main)
        )
    }

    open fun chooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        val txt_title = view!!.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.VISIBLE
        TextViewUtil.setBold(txt_title)
        txt_title.setTextColor(ContextCompat.getColor(this,R.color.purple_main))
    }

    open fun unChooseTab(position: Int) {
        val view: View = tabStatistical.getTabAt(position)!!.customView!!
        val txt_title = view.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        TextViewUtil.setNormal(txt_title)
        txt_title.setTextColor(ContextCompat.getColor(this,R.color.normalGray))
    }

    open fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        val txt_title = view!!.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        TextViewUtil.setNormal(txt_title)
        txt_title.setTextColor(ContextCompat.getColor(this,R.color.normalGray))
    }

    fun getRootView():View {
        return rootView
    }

    abstract fun getFragmentList(): List<Fragment>

    abstract fun getTitleList(): List<String>

}