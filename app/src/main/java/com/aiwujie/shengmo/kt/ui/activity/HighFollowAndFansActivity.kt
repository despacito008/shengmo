package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.aiwujie.shengmo.activity.LabelDefaultsActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.customview.BindSvipDialog
import com.aiwujie.shengmo.fragment.FragmentFans
import com.aiwujie.shengmo.fragment.FragmentFollow
import com.aiwujie.shengmo.fragment.FragmentFriend
import com.aiwujie.shengmo.fragment.FragmentlabelTopic
import com.aiwujie.shengmo.kt.ui.activity.GroupingDetailActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.RelationShipDetailActivity
import com.aiwujie.shengmo.kt.ui.fragment.HighFansFragment
import com.aiwujie.shengmo.kt.ui.fragment.HighFollowFragment
import com.aiwujie.shengmo.kt.ui.fragment.MyRelationShipGroupingFragment
import com.aiwujie.shengmo.kt.ui.fragment.RelationShipFansFragment
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.utils.SharedPreferencesUtils

class HighFollowAndFansActivity : RelationShipDetailActivity() {
    lateinit var uid: String
    var currentIndex = -1

    companion object {
        fun start(context: Context, uid: String, currentIndex: Int) {
            val intent = Intent(context, HighFollowAndFansActivity::class.java)
            intent.putExtra(IntentKey.UID, uid)
            intent.putExtra(IntentKey.INDEX, currentIndex)
            context.startActivity(intent)
        }
    }

    override fun initViewComplete() {
        super.initViewComplete()
        uid = intent.getStringExtra(IntentKey.UID)!!
        currentIndex = intent.getIntExtra(IntentKey.INDEX, 0)

    }

    override fun initViewPagerComplete() {
        super.initViewPagerComplete()
        vpStatistical.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

            }
        })
        vpStatistical.currentItem = currentIndex
    }

    override fun getFragmentList(): List<Fragment> {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        fragmentList.add(HighFollowFragment())
        fragmentList.add(HighFansFragment())
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList: ArrayList<String> = ArrayList()

        titleList.add("关注")
        titleList.add("粉丝")

        return titleList
    }
}