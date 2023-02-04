package com.aiwujie.shengmo.kt.ui.fragment.tabtopbar

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter
import com.aiwujie.shengmo.kt.ui.fragment.LazyFragment
import com.aiwujie.shengmo.kt.util.addOnPageChangeListenerDsl
import com.aiwujie.shengmo.kt.util.addOnTabSelectedListenerDsl
import com.aiwujie.shengmo.kt.util.setTextSizeWithDp
import com.aiwujie.shengmo.utils.TextViewUtil


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment.tabtopbar
 * @ClassName: TabTopBarFragment
 * @Author: xmf
 * @CreateDate: 2022/4/22 12:00
 * @Description:
 */
abstract class TabTopBarFragment: LazyFragment() {
    override fun loadData() {
        initViewPager()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_tab_top_bar
    }

    lateinit var flLeft: FrameLayout
    lateinit var flRight: FrameLayout
    lateinit var tabLayout: TabLayout
    lateinit var vpTabTopBar: ViewPager
    lateinit var flRoot:FrameLayout

    override fun initView(rootView: View) {
        flRoot = rootView.findViewById(R.id.fl_tab_top_bar_root)
        flLeft = rootView.findViewById(R.id.fl_tab_top_bar_left)
        flRight = rootView.findViewById(R.id.fl_tab_top_bar_right)
        tabLayout = rootView.findViewById(R.id.tab_top_bar)
        vpTabTopBar = rootView.findViewById(R.id.vp_tab_top_bar)
        initTabLeftView()
        initTabRightView()
    }

    fun initViewPager() {
        tabLayout.tabMode = TabLayout.MODE_FIXED
        titleList = getTitleList() as ArrayList<String>
        fragmentList = getFragmentList() as ArrayList<Fragment>
        val mAdapter = RankMyPagerAdapter(childFragmentManager, titleList, fragmentList)
        vpTabTopBar.adapter = mAdapter
        tabLayout.setupWithViewPager(vpTabTopBar)
        vpTabTopBar.offscreenPageLimit = fragmentList.size
        for (i in titleList.indices) {
            tabLayout.getTabAt(i)!!.customView = getTabView(i)
        }
        selectTab(0)
        setListener()
    }



    open fun initTabLeftView() {}

    open fun initTabRightView() {}

    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList: ArrayList<String>

    fun setListener() {
        vpTabTopBar.addOnPageChangeListenerDsl {
            onPageSelected {
                selectTab(it)
            }
        }

        tabLayout.addOnTabSelectedListenerDsl {
            onTabSelected {
                it?.run { chooseTab(it) }
            }
            onTabUnselected {
                it?.run { unChooseTab(it) }
            }
        }
    }

    open fun getTabView(position: Int): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_custom_tab_layout_top_bar, null)
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
        val view: View = tabLayout.getTabAt(position)!!.customView!!
        val tvTitle = view.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.VISIBLE
        changeTextViewState(true,tvTitle)
    }

    open fun chooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        val tvTitle = view!!.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.VISIBLE
        changeTextViewState(true,tvTitle)
    }

    open fun unChooseTab(position: Int) {
        val view: View = tabLayout.getTabAt(position)!!.customView!!
        val tvTitle = view.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        changeTextViewState(false,tvTitle)
    }

    open fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        val tvTitle = view!!.findViewById(R.id.item_custom_tab_tv) as TextView
        val viewTabBottom: View = view.findViewById(R.id.view_item_tab_bottom)
        viewTabBottom.visibility = View.GONE
        changeTextViewState(false,tvTitle)
    }

    private fun changeTextViewState(mChoose: Boolean, tv: TextView) {
        with(tv) {
            if (mChoose) {
                TextViewUtil.setBold(this)
                setTextColor(ContextCompat.getColor(activity, R.color.titleBlack))
                setTextSizeWithDp(18f)
            } else {
                TextViewUtil.setNormal(this)
                setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
                setTextSizeWithDp(16f)
            }
        }
    }

    abstract fun getFragmentList(): List<Fragment>

    abstract fun getTitleList(): List<String>

}
