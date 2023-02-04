package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentStampBuy
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentStampReceive
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentStampUse
import com.aiwujie.shengmo.kt.ui.fragment.StampStatisticFragment

/**
 * 推顶卡明细
 */
class StampDetailActivity: StatisticalDetailActivity() {

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
//        fragmentList.add(FragmentStampBuy())
//        fragmentList.add(FragmentStampReceive())
//        fragmentList.add(FragmentStampUse())
        fragmentList.add(StampStatisticFragment.newInstance(1))
        fragmentList.add(StampStatisticFragment.newInstance(2))
        fragmentList.add(StampStatisticFragment.newInstance(3))
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("购买记录")
        titleList.add("系统赠送")
        titleList.add("使用记录")
        return titleList
    }

    override fun getPageTitle(): String {
        return "邮票明细"
    }
}