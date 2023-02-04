package com.aiwujie.shengmo.kt.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter2
import com.aiwujie.shengmo.bean.CheckUserTopBean
import com.aiwujie.shengmo.bean.TopicPageBean
import com.aiwujie.shengmo.eventbus.TopicRefreshEvent
import com.aiwujie.shengmo.kt.ui.activity.GroupingDetailActivity
import com.aiwujie.shengmo.kt.ui.fragment.LazyFragment
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageMessageFragment
import com.aiwujie.shengmo.kt.util.addOnPageChangeListenerDsl
import com.aiwujie.shengmo.kt.util.addOnTabSelectedListenerDsl
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomePageConversationTabFragment : LazyFragment() {
    private lateinit var vpNormal: ViewPager
    private lateinit var tabNormal: TabLayout
    private lateinit var flTop: FrameLayout
    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList: ArrayList<String>
    private var homePageConversationFragment: HomePageConversationFragment? = null
    private var homePageHighConversationFragment: HomePageHighConversationFragment? = null
    override fun loadData() {
        getPage()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_layout_fragment_conversation_tab
    }

    override fun initView(rootView: View) {
        rootView?.run {
            vpNormal = findViewById(R.id.view_pager_normal_tab)
            tabNormal = findViewById(R.id.tab_normal_tab)
            flTop = findViewById(R.id.fl_normal_tab_top)
            tabNormal.visibility = View.GONE
        }
        fragmentList = ArrayList()
        titleList = ArrayList()

        titleList.clear()
        fragmentList.clear()
        titleList.add("聊天")
        fragmentList.add(getFragment())
        homePageHighConversationFragment = HomePageHighConversationFragment()
        //  setViewPagerMarginTop()
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

    private fun getPage() {
        HttpHelper.getInstance().checkSelefTop(object : HttpCodeMsgListener {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, CheckUserTopBean::class.java)
                model?.data?.run {
                    if (this.is_top_user == "1"){
                        titleList.add("高端")
                        fragmentList.add(homePageHighConversationFragment!! )
                        tabNormal.visibility = View.VISIBLE
                    }
                    if (mViewPagerAdapter == null) {
                        initViewPager()
                    } else {
                        mViewPagerAdapter?.clear(vpNormal)
                        initViewPager()
                    }

                }
            }

            override fun onFail(code: Int, msg: String?) {
                if (mViewPagerAdapter == null) {
                    initViewPager()
                } else {
                    mViewPagerAdapter?.clear(vpNormal)
                    initViewPager()
                }
                msg?.showToast()

            }

        })

    }

    var mViewPagerAdapter: RankMyPagerAdapter2? = null
    fun initViewPager() {
        Log.v("initView","initViewPager")
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

    fun getCurrentType(): Int {
        return getFragment().getCurrentType()
    }

    fun showConversation() {
        getFragment().showConversation()
    }

    fun hideConversation() {
        getFragment().hideConversation()
    }

    fun showMenu() {
        getFragment().showMenu()
    }


    fun refreshCallList() {
        vpNormal.postDelayed({
            getFragment().refreshCallList()
        }, 2000)

    }


    fun getFragment(): HomePageConversationFragment {
        if (homePageConversationFragment == null) {
            homePageConversationFragment = HomePageConversationFragment()
        }
        return homePageConversationFragment!!
    }

    fun showSettingMenu() {
        (parentFragment as HomePageMessageFragment).showSettingMenu()
    }

    fun hideSettingMenu() {
        (parentFragment as HomePageMessageFragment).hideSettingMenu()
    }

    fun refreshHighUnreadNum(unread:Int) {
       // "高端未读消息 $unread".showToast()
        val tabView2 = tabNormal.getTabAt(1)?.customView
        val tvHighCount: TextView? = tabView2?.findViewById(R.id.tv_tab_red_dot)
        tvHighCount?.run {
            if (unread > 0) {
                visibility = View.VISIBLE
                text = unread.toString()
            } else {
                visibility = View.GONE
            }
        }

    }

    fun refreshNormalUnreadNum(unread: Int) {
        val tabView = tabNormal.getTabAt(0)?.customView
        val tvNormalCount: TextView? = tabView?.findViewById(R.id.tv_tab_red_dot)
        tvNormalCount?.run {
            if (unread > 0) {
                visibility = View.VISIBLE
                text = unread.toString()
            } else {
                visibility = View.GONE
            }
        }
    }


}