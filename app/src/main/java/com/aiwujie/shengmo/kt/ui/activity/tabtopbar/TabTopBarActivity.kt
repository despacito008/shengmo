package com.aiwujie.shengmo.kt.ui.activity.tabtopbar

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.transition.Fade
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.kt.util.addOnPageChangeListenerDsl
import com.aiwujie.shengmo.kt.util.addOnTabSelectedListenerDsl
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.setTextSizeWithDp
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.TextViewUtil
import com.facebook.drawee.drawable.FadeDrawable

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.tabtopbar
 * @ClassName: TabTopBarActivity
 * @Author: xmf
 * @CreateDate: 2022/4/21 18:52
 * @Description:
 */
abstract class TabTopBarActivity : BaseActivity() {

    lateinit var flLeft: FrameLayout
    lateinit var flRight: FrameLayout
    lateinit var tabLayout: TabLayout
    lateinit var vpTabTopBar: ViewPager
    val llRoot:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_tab_top_bar_root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_tab_top_bar)
        StatusBarUtil.showLightStatusBar(this)
        flLeft = findViewById(R.id.fl_tab_top_bar_left)
        flRight = findViewById(R.id.fl_tab_top_bar_right)
        tabLayout = findViewById(R.id.tab_top_bar)
        vpTabTopBar = findViewById(R.id.vp_tab_top_bar)

        tabLayout.tabMode = TabLayout.MODE_FIXED
        titleList = getTitleList() as ArrayList<String>
        fragmentList = getFragmentList() as ArrayList<Fragment>
        val mAdapter = RankMyPagerAdapter(supportFragmentManager, titleList, fragmentList)
        vpTabTopBar.adapter = mAdapter
        tabLayout.setupWithViewPager(vpTabTopBar)
        vpTabTopBar.offscreenPageLimit = fragmentList.size
        for (i in titleList.indices) {
            tabLayout.getTabAt(i)!!.customView = getTabView(i)
        }
        selectTab(0)
        setListener()
        initTabLeftView()
        initTabRightView()
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
        val view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout_top_bar, null)
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
                setTextColor(ContextCompat.getColor(this@TabTopBarActivity, R.color.titleBlack))
                setTextSizeWithDp(18f)
            } else {
                TextViewUtil.setNormal(this)
                setTextColor(ContextCompat.getColor(this@TabTopBarActivity, R.color.normalGray))
                setTextSizeWithDp(16f)
            }
        }
    }

    abstract fun getFragmentList(): List<Fragment>

    abstract fun getTitleList(): List<String>
}
