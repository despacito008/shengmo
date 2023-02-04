package com.aiwujie.shengmo.kt.ui.fragment

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.FragmentAdapter2
import com.aiwujie.shengmo.bean.FriendGroupListBean
import com.aiwujie.shengmo.fragment.FragmentFriendTopic
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MyRelationShipGroupingFragment:LazyFragment() {
    override fun getContentViewId(): Int {
        return R.layout.app_layout_fragment_normal_tab
    }
    private lateinit var vpNormal: ViewPager
    private lateinit var tabNormal: TabLayout
    private lateinit var flTop: FrameLayout
    override fun initView(rootView: View) {
        EventBus.getDefault().register(this)
        vpNormal = rootView.findViewById(R.id.view_pager_normal_tab)
        tabNormal = rootView.findViewById(R.id.tab_normal_tab)
        flTop = rootView.findViewById(R.id.fl_normal_tab_top)
        setViewPagerMarginTop()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    lateinit var titleList:List<String>
    override fun loadData() {
        HttpHelper.getInstance().getFriendGrouping(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, FriendGroupListBean::class.java)
                titleList = tempData?.data?.map { i: FriendGroupListBean.DataBean ->
                    i.fgname
                }!!
                val fragmentList = tempData.data?.map { i: FriendGroupListBean.DataBean ->
                    GroupingFriendFragment.newInstance(i.id)
                }
                titleList.run {
                    fragmentList?.run {
                        initViewPager(titleList,fragmentList)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    fun initViewPager(titleList: List<String>, fragmentList: List<Fragment>) {
        val fragmentAdapter = FragmentAdapter2(activity, childFragmentManager, fragmentList, titleList)
        with(vpNormal) {
            fragmentAdapter?.clear(vpNormal)
            adapter = fragmentAdapter
            offscreenPageLimit = titleList.size
            addOnPageChangeListener(object:ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    selectTab(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
            tabNormal.setupWithViewPager(this)
        }
        tabNormal.tabMode = TabLayout.MODE_SCROLLABLE
        for (index in titleList.indices) {
            tabNormal.getTabAt(index)?.customView = getTabView(titleList[index])
        }
        tabNormal.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                chooseTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                unChooseTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        selectTab(0)
    }

    fun getTabView(title: String?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_custom_tab_layout_normal, null)
        val tvItemTitle = view.findViewById(R.id.tv_normal_tab) as TextView
        tvItemTitle.text = title
        with(tvItemTitle) {
            setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
            setBackgroundResource(R.drawable.bg_round_gray_tab)
        }
        return view
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
        val view = tab.customView!!
        val tvTab = view.findViewById(R.id.tv_normal_tab) as TextView
        with(tvTab) {
            setTextColor(ContextCompat.getColor(activity,R.color.white))
            setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
        }
    }

    private fun unChooseTab(tab: TabLayout.Tab) {
        val view = tab.customView!!
        val tvTab = view.findViewById(R.id.tv_normal_tab) as TextView
        with(tvTab) {
            setTextColor(ContextCompat.getColor(activity,R.color.normalGray))
            setBackgroundResource(R.drawable.bg_round_gray_tab)
        }
    }

    private fun setViewPagerMarginTop() {
        vpNormal.layoutParams = with(vpNormal.layoutParams as FrameLayout.LayoutParams) {
            topMargin = 50.dp
            this
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(eventData:String) {
        if (eventData == "groupingRefresh") {
            loadData()
        }
    }

}