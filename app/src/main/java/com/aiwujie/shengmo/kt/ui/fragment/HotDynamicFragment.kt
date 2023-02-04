package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.eventbus.RedPointEvent
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 动态 - 热推
 */
class HotDynamicFragment : NormalTabFragment() {
    private lateinit var recommendationDynamicFragment: RecommendationDynamicFragment
    private lateinit var ejectionDynamicFragment: EjectionDynamicFragment
    override fun getFragmentList(): List<Fragment> {
        recommendationDynamicFragment = RecommendationDynamicFragment()
        ejectionDynamicFragment = EjectionDynamicFragment()
        return arrayListOf(recommendationDynamicFragment, ejectionDynamicFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("推荐", "推顶")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initVideoSortView()
        vpNormal.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                llSort?.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    var llSort: LinearLayout? = null
    var sortType = 1
    private fun initVideoSortView() {
        val sortView = View.inflate(activity, R.layout.app_layout_video_sort, null)
        llSort = sortView.findViewById(R.id.ll_video_sort)
        val tvSort: TextView = sortView.findViewById(R.id.tv_layout_video_sort)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER or Gravity.END
        sortView.layoutParams = params
        flTop.addView(sortView)
        val order = SharedPreferencesUtils.geParam(activity, SpKey.HOT_SORT, "time")
        sortType = if (order == "time") 2 else 1
        tvSort.text = if (sortType == 1) "推量排序" else "时间排序"
        llSort?.clickDelay {
            sortType = if (sortType == 1) 2 else 1
            tvSort.text = if (sortType == 1) "推量排序" else "时间排序"
            ejectionDynamicFragment.changeOrder(sortType)
        }
        llSort?.visibility = View.INVISIBLE
    }

    fun hideTab() {
        if (flTop.visibility == View.VISIBLE) {
            flTop.animation = AnimationUtil.hideTabAnimation(flTop)
            flTop.visibility = View.GONE
        }
    }

    fun showTab() {
        if (flTop.visibility == View.GONE) {
            flTop.animation = AnimationUtil.showTabAnimation(flTop)
            flTop.visibility = View.VISIBLE
        }
    }

    fun setCheckedTab(position: Int) {
        vpNormal.currentItem = position
        showTab()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
     fun onMessages(event: RedPointEvent) {
        val tabView1 = tabNormal.getTabAt(0)?.customView
        val tabView2 = tabNormal.getTabAt(1)?.customView
        val tvRecommendMessageCount: TextView? = tabView1?.findViewById(R.id.tv_tab_red_dot)
        val tvPushTopMessageCount: TextView? = tabView2?.findViewById(R.id.tv_tab_red_dot)

        when(event.flag) {
            6 -> {
                val num = SpKey.RECOMMEND_NUM.getSpValue(0) as Int
                if (num != 0) {
                    tvRecommendMessageCount?.run {
                        text = num.toString()
                        visibility = View.VISIBLE
                    }
                }
            }
            7 -> {
                val num = SpKey.PUSH_TOP_NUM.getSpValue(0) as Int
                if (num != 0) {
                    tvPushTopMessageCount?.run {
                        text = num.toString()
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun clearRedDum(type:Int) {
        val tabView1 = tabNormal.getTabAt(0)?.customView
        val tabView2 = tabNormal.getTabAt(1)?.customView
        val tvRecommendMessageCount: TextView? = tabView1?.findViewById(R.id.tv_tab_red_dot)
        val tvPushTopMessageCount: TextView? = tabView2?.findViewById(R.id.tv_tab_red_dot)
        if (type == 1) {
            tvRecommendMessageCount?.visibility = View.GONE
        } else {
            tvPushTopMessageCount?.visibility = View.GONE
        }
    }

}