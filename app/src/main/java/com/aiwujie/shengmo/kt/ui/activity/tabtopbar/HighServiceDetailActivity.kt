package com.aiwujie.shengmo.kt.ui.activity.tabtopbar

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentConsumption
import com.aiwujie.shengmo.fragment.user.GiftFragment
import com.aiwujie.shengmo.fragment.user.RechargeFragment
import com.aiwujie.shengmo.kt.ui.activity.statistical.PresentDetailActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeAndGiftActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeDetailActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.StatisticalDetailActivity
import com.aiwujie.shengmo.kt.ui.fragment.HighRecordFragment
import com.aiwujie.shengmo.kt.ui.fragment.RechargeRecordFragment
import com.aiwujie.shengmo.kt.util.dp


class HighServiceDetailActivity: StatisticalDetailActivity() {

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        fragmentList.add(HighRecordFragment.newInstance("1"))
        fragmentList.add(HighRecordFragment.newInstance("2"))
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("购买记录")
        titleList.add("获赠记录")
        return titleList
    }

    override fun getPageTitle(): String {
        return "高端明细"
    }
}
