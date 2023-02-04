package com.aiwujie.shengmo.kt.ui.fragment

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
import com.aiwujie.shengmo.adapter.FragmentAdapter2
import com.aiwujie.shengmo.bean.FriendGroupListBean
import com.aiwujie.shengmo.customview.MyViewpager
import com.aiwujie.shengmo.kt.ui.activity.GroupingDetailActivity
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageMessageFragment
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.tim.ConversationFragment
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment
 * @ClassName: HomePageConversationFragment
 * @Author: xmf
 * @CreateDate: 2022/5/18 19:53
 * @Description:
 */
class HomePageConversationFragment : LazyFragment() {
    override fun getContentViewId(): Int {
        return R.layout.app_fragment_homepage_conversation
    }

    private lateinit var vpNormal: MyViewpager
    private lateinit var tabNormal: TabLayout
    private lateinit var flTop: FrameLayout
    private lateinit var flContent: FrameLayout

    override fun initView(rootView: View) {

        EventBus.getDefault().register(this)
        vpNormal = rootView.findViewById(R.id.view_pager_normal_tab)
        tabNormal = rootView.findViewById(R.id.tab_normal_tab)
        flTop = rootView.findViewById(R.id.fl_normal_tab_top)
        flContent = rootView.findViewById(R.id.fl_content)
        //setViewPagerMarginTop()
        initConversationFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    lateinit var titleList: List<String>

    override fun loadData() {
        HttpHelper.getInstance().getChatFriendGrouping(object : HttpCodeMsgListener {
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
                        initViewPager(titleList, fragmentList)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                if (code == 4000) {
                    changeGroupingTab(false)
                    showConversation()
                }
            }
        })
    }

    fun initViewPager(titleList: List<String>, fragmentList: List<Fragment>) {
        changeGroupingTab(true)
        val fragmentAdapter = FragmentAdapter2(activity, childFragmentManager, fragmentList, titleList)
        with(vpNormal) {
            fragmentAdapter?.clear(vpNormal)
            adapter = fragmentAdapter
            offscreenPageLimit = titleList.size
            isScrollble = true
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
        tabNormal.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                hideConversation()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                unChooseTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                changeConversation()
            }
        })
        selectTab(-1)
    }

    fun getTabView(title: String?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_custom_tab_layout_coversation, null)
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
            unChooseTab(tabNormal.getTabAt(i)!!)
        }
        for (i in titleList.indices) {
            if (i == position) {
                chooseTab(tabNormal.getTabAt(position)!!)
            }
        }
    }

    private fun chooseTab(tab: TabLayout.Tab?) {
//        val view = tab.customView!!
//        val tvTab = view.findViewById(R.id.tv_normal_tab) as TextView
//        with(tvTab) {
//            setTextColor(ContextCompat.getColor(activity, R.color.white))
//            setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
//        }

        tab?.customView?.run {
            val tvTab = this.findViewById(R.id.tv_normal_tab) as TextView
            with(tvTab) {
                setTextColor(ContextCompat.getColor(activity, R.color.white))
                setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
            }
        }
    }

    private fun unChooseTab(tab: TabLayout.Tab?) {
//        val view = tab.customView
//        val tvTab = view.findViewById(R.id.tv_normal_tab) as TextView
//        with(tvTab) {
//            setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
//            setBackgroundResource(R.drawable.bg_round_gray_tab)
//        }
        tab?.customView?.run {
            val tvTab = this.findViewById(R.id.tv_normal_tab) as TextView
            with(tvTab) {
                setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
                setBackgroundResource(R.drawable.bg_round_gray_tab)
            }
        }

    }

    private fun setViewPagerMarginTop() {
        vpNormal.layoutParams = with(vpNormal.layoutParams as FrameLayout.LayoutParams) {
            topMargin = 50.dp
            this
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(eventData: String) {
        if (eventData == "groupingRefresh") {
            loadData()
        }
    }

    lateinit var conversationFragment: ConversationFragment
    private fun initConversationFragment() {
        conversationFragment = ConversationFragment()
        childFragmentManager.beginTransaction().replace(R.id.fl_content, conversationFragment).commit()
    }

    fun changeConversation() {
        if (flContent.visibility == View.GONE) {
            flContent.visibility = View.VISIBLE
            (parentFragment as HomePageConversationTabFragment).hideSettingMenu()
            selectTab(-1)
        } else if (flContent.visibility == View.VISIBLE) {
            flContent.visibility = View.GONE
            (parentFragment as HomePageConversationTabFragment).showSettingMenu()
            selectTab(vpNormal.currentItem)
        }
    }

    fun showConversation() {
//        if (flContent != null) {
        if (flContent.visibility == View.GONE) {
            flContent.visibility = View.VISIBLE
            (parentFragment as HomePageConversationTabFragment).hideSettingMenu()
            vpNormal.isScrollble = false
            vpNormal.visibility = View.GONE
            //vpNormal.currentItem = vpNormal.childCount
            selectTab(-1)
        }
//        }

    }

    fun hideConversation() {
//        if (flContent != null) {
        if (flContent.visibility == View.VISIBLE) {
            flContent.visibility = View.GONE
            (parentFragment as HomePageConversationTabFragment).showSettingMenu()
            vpNormal.isScrollble = true
            vpNormal.visibility = View.VISIBLE
            selectTab(vpNormal.currentItem)
        }


//        }
    }

    fun changeGroupingTab(isShow: Boolean) {
        when (isShow) {
            true -> {
                if (tabNormal.visibility == View.GONE) {
                    tabNormal.visibility = View.VISIBLE
                }
                conversationFragment.setPaddingTop(0.dp)

            }
            false -> {
                if (tabNormal.visibility == View.VISIBLE) {
                    tabNormal.visibility = View.GONE
                }
                conversationFragment.setPaddingTop(0)
            }
        }
    }

    fun showMenu() {
        if (flContent.visibility == View.VISIBLE) {
            conversationFragment.showMenu()
        } else {
            activity.startActivity(Intent(activity, GroupingDetailActivity::class.java))
        }
    }

    fun getCurrentType(): Int {
        return if (flContent.visibility == View.VISIBLE) 0 else 1
    }

    fun refreshCallList() {
//        vpNormal.postDelayed({
            conversationFragment.refreshCallList()
//        }, 2000)


    }

}
