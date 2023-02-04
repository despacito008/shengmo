package com.aiwujie.shengmo.kt.ui.activity.statistical

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
import com.aiwujie.shengmo.kt.ui.fragment.MyRelationShipGroupingFragment
import com.aiwujie.shengmo.kt.ui.fragment.RelationShipFansFragment
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
class UserRelationShipActivity: RelationShipDetailActivity() {
    lateinit var uid:String
    var currentIndex = -1

    companion object {
        fun start(context: Context, uid: String, currentIndex: Int) {
            val intent = Intent(context, UserRelationShipActivity::class.java)
            intent.putExtra(IntentKey.UID, uid)
            intent.putExtra(IntentKey.INDEX, currentIndex)
            context.startActivity(intent)
        }
    }

    override fun initViewComplete() {
        super.initViewComplete()
        uid = intent.getStringExtra(IntentKey.UID)!!


        currentIndex = intent.getIntExtra(IntentKey.INDEX,0)
        tvNormalTitleMore.text = "设置"
        tvNormalTitleMore.setOnClickListener {
            val svip = SpKey.SVIP.getSpValue("0")
            if (svip == "1") {
                startActivity(Intent(this, GroupingDetailActivity::class.java))
            } else {
                BindSvipDialog.bindAlertDialog(this, "设置分组限svip可用")
            }
        }
    }

    override fun initViewPagerComplete() {
        super.initViewPagerComplete()
        vpStatistical.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    tvNormalTitleMore.visibility = View.VISIBLE
                } else {
                    tvNormalTitleMore.visibility = View.INVISIBLE
                }
            }
        })
        vpStatistical.currentItem = currentIndex
    }

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        if (uid == MyApp.uid) {
            fragmentList.add(FragmentFollow())
            fragmentList.add(RelationShipFansFragment.newInstance(1))
            fragmentList.add(RelationShipFansFragment.newInstance(2))
            fragmentList.add(MyRelationShipGroupingFragment())
        } else {
            fragmentList.add(FragmentFollow())
            fragmentList.add(FragmentFans())
        }
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        if (uid == MyApp.uid) {
            titleList.add("关注")
            titleList.add("粉丝")
            titleList.add("好友")
            titleList.add("分组")
        } else {
            titleList.add("关注")
            titleList.add("粉丝")
        }
        return titleList
    }
}