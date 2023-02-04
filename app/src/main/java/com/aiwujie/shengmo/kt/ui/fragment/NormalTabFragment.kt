package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter
import com.aiwujie.shengmo.kt.util.addOnTabSelectedListenerDsl



 abstract class NormalTabFragment: LazyFragment() {
    lateinit var vpNormal:ViewPager
    lateinit var tabNormal:TabLayout
    lateinit var flTop:FrameLayout
    lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList:ArrayList<String>
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater?.inflate(R.layout.app_layout_fragment_normal_tab, container, false)
//    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

     override fun getContentViewId(): Int {
         return R.layout.app_layout_fragment_normal_tab
     }

     override fun initView(rootView: View) {
         vpNormal = rootView!!.findViewById(R.id.view_pager_normal_tab)
         tabNormal = rootView.findViewById(R.id.tab_normal_tab)
         flTop = rootView.findViewById(R.id.fl_normal_tab_top)
     }

     override fun loadData() {
         initViewPager()
     }

     fun initViewPager() {

         titleList = getTitleList() as ArrayList<String>
         fragmentList = getFragmentList() as ArrayList<Fragment>
         val mAdapter = RankMyPagerAdapter(childFragmentManager, titleList, fragmentList)
         vpNormal.adapter = mAdapter
         tabNormal.setupWithViewPager(vpNormal)
         tabNormal.tabMode = TabLayout.MODE_SCROLLABLE
         vpNormal.offscreenPageLimit = fragmentList.size
         for (i in titleList.indices) {
             tabNormal.getTabAt(i)!!.customView = getTabView(i)
         }
         setListener()
         selectTab(0)
     }

     open fun getTabView(position: Int): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_custom_tab_layout_normal, null)
        val tvItemTitle = view.findViewById(R.id.tv_normal_tab) as TextView
        tvItemTitle.text = titleList[position]
        with(tvItemTitle) {
            setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
            setBackgroundResource(R.drawable.bg_round_gray_tab)
        }
        return view
    }

    fun setListener() {

        vpNormal.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                selectTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

//        tabNormal.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                chooseTab(tab)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                unChooseTab(tab)
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })

        tabNormal.addOnTabSelectedListenerDsl {
            onTabSelected { it?.run {  chooseTab(it)} }
            onTabUnselected { it?.run { unChooseTab(it) } }
        }

    }

    open fun selectTab(position: Int) {
        for (i in titleList.indices) {
            unChooseTab(tabNormal.getTabAt(position)!!)
        }
        for (i in titleList.indices) {
            if (i == position) {
                chooseTab(tabNormal.getTabAt(position)!!)
            }
        }
    }

    open fun chooseTab(tab: TabLayout.Tab) {
        val view = tab.customView!!
        val tvTab = view.findViewById(R.id.tv_normal_tab) as TextView
        with(tvTab) {
            setTextColor(ContextCompat.getColor(activity,R.color.white))
            setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
        }
    }

    open fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView!!
        val tvTab = view.findViewById(R.id.tv_normal_tab) as TextView
        with(tvTab) {
            setTextColor(ContextCompat.getColor(activity,R.color.normalGray))
            setBackgroundResource(R.drawable.bg_round_gray_tab)
        }
    }


    abstract fun getFragmentList(): List<Fragment>

    abstract fun getTitleList(): List<String>
}