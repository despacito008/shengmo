package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import org.greenrobot.eventbus.EventBus


class SquareDynamicFragment : NormalTabFragment() {
    lateinit var newFragment:NewestDynamicFragment
    lateinit var videoFragment:NewVideoDynamicFragment
    var videoSort = ""
    override fun getFragmentList(): List<Fragment> {
        newFragment = NewestDynamicFragment()
        videoSort = SharedPreferencesUtils.getParam(activity, SpKey.VIDEO_SORT, "0") as String
        videoFragment = NewVideoDynamicFragment.newInstance(videoSort)
        return arrayListOf(newFragment, videoFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("最新", "视频")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVideoSortView()
        vpNormal.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                llSort?.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                if (position == 0) {
                    EventBus.getDefault().post("xianshishalou")
                }
                if (position == 1) {
                    EventBus.getDefault().post("yincangshalou")
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    var llSort:LinearLayout? = null
    private fun initVideoSortView() {
        val sortView = View.inflate(activity, R.layout.app_layout_video_sort, null)
        llSort = sortView.findViewById(R.id.ll_video_sort)
        val tvSort: TextView = sortView.findViewById(R.id.tv_layout_video_sort)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER or Gravity.END
        sortView.layoutParams = params
        flTop.addView(sortView)
        tvSort.text = if (videoSort == "0") "时间排序" else "随机排序"
        llSort?.setOnClickListener {
            videoSort = if (videoSort == "0") "1" else "0"
            tvSort.text = if (videoSort == "0") "时间排序" else "随机排序"
            SharedPreferencesUtils.setParam(activity, SpKey.VIDEO_SORT, videoSort)
            videoFragment.refreshMode(videoSort)
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

}