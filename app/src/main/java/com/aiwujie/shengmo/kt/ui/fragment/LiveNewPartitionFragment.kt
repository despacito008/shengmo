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
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils


class LiveNewPartitionFragment : NormalTabFragment() {
    private var tid:String ?= null
    lateinit var livePartitionPlayBackListFragment: LivePartitionPlayBackListFragment
    override fun getFragmentList(): List<Fragment> {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(LivePartitionOnlineFragment.newInstance(tid))
        livePartitionPlayBackListFragment=  LivePartitionPlayBackListFragment.newInstance(tid)
        fragmentList.add(livePartitionPlayBackListFragment)
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
        tid = arguments.getString("tid")
        initSortView()
        loadData()
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

    private fun changeOrder(){
        livePartitionPlayBackListFragment .changeOrder(sortType % 3)
    }

}