package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.eventbus.PlayBackPageTurnEvent
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LiveNewFragment : NormalTabFragment() {
    lateinit var playBackListFragment: LivePlayBackListFragment
    override fun getFragmentList(): List<Fragment> {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(LiveOnlineNewListFragment())
        playBackListFragment = LivePlayBackListFragment()
        fragmentList.add(playBackListFragment)
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList = ArrayList<String>()
        titleList.add("直播")
        titleList.add("回放")
        return titleList
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initSortView()
        vpNormal.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                showTab()
                llSort?.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    var llSort: LinearLayout? = null
    var sortType = 1
    private fun initSortView() {
        val sortView = View.inflate(activity, R.layout.app_layout_video_sort, null)
        llSort = sortView.findViewById(R.id.ll_video_sort)
        val tvSort: TextView = sortView.findViewById(R.id.tv_layout_video_sort)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER or Gravity.END
        sortView.layoutParams = params
        flTop.addView(sortView)
        val order = SpKey.PLAYBACK_SORT.getSpValue(SpKey.VALUE_SORT_TIME)
        sortType = when (order) {
            SpKey.VALUE_SORT_TIME -> 1
            SpKey.VALUE_SORT_WATCH -> 2
            SpKey.VALUE_SORT_BEANS -> 3
            else -> 1
        }
        showSortContent(tvSort)
        llSort?.clickDelay {
            sortType++
            showSortContent(tvSort)
            changeOrder()
        }
        llSort?.visibility = View.INVISIBLE
    }

    private fun showSortContent(tvSort: TextView) {
        when (sortType % 3) {
            1 -> {
                tvSort.text = "最新"
                SpKey.PLAYBACK_SORT.saveSpValue(SpKey.VALUE_SORT_TIME)
            }
            2 -> {
                tvSort.text = "浏览量"
                SpKey.PLAYBACK_SORT.saveSpValue(SpKey.VALUE_SORT_WATCH)
            }
            0 -> {
                tvSort.text = "收入"
                SpKey.PLAYBACK_SORT.saveSpValue(SpKey.VALUE_SORT_BEANS)
            }
        }
    }

    private fun changeOrder() {
        playBackListFragment.changeOrder(sortType % 3)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: PlayBackPageTurnEvent) {
        Log.v("PlayBackPageTurnEvent",event.position.toString())
        if (event.position == 1) {
          setCheckedTab(1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}