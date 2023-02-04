package com.aiwujie.shengmo.kt.ui.fragment.topic

import android.annotation.SuppressLint
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
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter2
import com.aiwujie.shengmo.bean.TopicPageBean
import com.aiwujie.shengmo.eventbus.TopicRefreshEvent
import com.aiwujie.shengmo.kt.ui.fragment.LazyFragment
import com.aiwujie.shengmo.kt.util.addOnPageChangeListenerDsl
import com.aiwujie.shengmo.kt.util.addOnTabSelectedListenerDsl
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DynamicTopicFragment : LazyFragment() {
    private lateinit var vpNormal: ViewPager
    private lateinit var tabNormal: TabLayout
    private lateinit var flTop: FrameLayout
    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList: ArrayList<String>

    override fun loadData() {
        getTopicPage()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_layout_fragment_normal_tab
    }

    override fun initView(rootView: View) {
        rootView?.run {
            vpNormal = findViewById(R.id.view_pager_normal_tab)
            tabNormal = findViewById(R.id.tab_normal_tab)
            flTop = findViewById(R.id.fl_normal_tab_top)
        }
        setViewPagerMarginTop()
        fragmentList = ArrayList()
        titleList = ArrayList()

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("InflateParams")
    fun getTabView(position: Int): View? {
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

        vpNormal.addOnPageChangeListenerDsl {
            onPageSelected {
                selectTab(it)
            }
        }

        tabNormal.addOnTabSelectedListenerDsl {
            onTabSelected {
                it?.run {
                    chooseTab(this)
                }
            }
            onTabUnselected {
                it?.run {
                    unChooseTab(this)
                }
            }
        }
    }

    private fun selectTab(position: Int) {
        for (i in titleList.indices) {
            unChooseTab(tabNormal.getTabAt(position)!!)
        }
        for (i in titleList.indices) {
            if (i == position) {
                chooseTab(tabNormal.getTabAt(position)!!)
            }
        }
    }

    private fun chooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        view?.let {
            val tvTab = it.findViewById(R.id.tv_normal_tab) as TextView
            with(tvTab) {
                setTextColor(ContextCompat.getColor(activity, R.color.white))
                setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
            }
        }

    }

    private fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView
        view?.let {
            val tvTab = it.findViewById(R.id.tv_normal_tab) as TextView
            with(tvTab) {
                setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
                setBackgroundResource(R.drawable.bg_round_gray_tab)
            }
        }
    }

    private fun getTopicPage() {
        titleList.clear()
        fragmentList.clear()
        HttpHelper.getInstance().getTopicPage(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, TopicPageBean::class.java)
                tempData?.data?.run {
                    titleList.addAll(this.map { it.pname })
                    fragmentList.addAll(this.map { TopicFragment.newInstance(it.pid) })
                    if (mViewPagerAdapter == null) {
                        initViewPager()
                    } else {
                        mViewPagerAdapter?.clear(vpNormal)
                        initViewPager()
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    var mViewPagerAdapter: RankMyPagerAdapter2? = null
    fun initViewPager() {
        mViewPagerAdapter = RankMyPagerAdapter2(childFragmentManager, titleList, fragmentList)
        vpNormal.adapter = mViewPagerAdapter
        tabNormal.setupWithViewPager(vpNormal)
        tabNormal.tabMode = TabLayout.MODE_SCROLLABLE
        vpNormal.offscreenPageLimit = fragmentList.size
        for (i in titleList.indices) {
            tabNormal.getTabAt(i)?.customView = getTabView(i)
        }
        selectTab(0)
        setListener()
    }

    private fun setViewPagerMarginTop() {
        vpNormal.layoutParams = with(vpNormal.layoutParams as FrameLayout.LayoutParams) {
            topMargin = 50.dp
            this
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(data: TopicRefreshEvent) {
        refreshTopicPage()
    }

    private fun refreshTopicPage() {
        if (!titleList.contains("关注")) {
            getTopicPage()
        }
    }

}