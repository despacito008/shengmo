package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.fragment.purserecordfragment.*
import com.aiwujie.shengmo.kt.ui.fragment.RechargeRecordFragment

/**
 * 充值明细
 */
class RechargeDetailActivity: StatisticalDetailActivity() {

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        fragmentList.add(RechargeRecordFragment())
        fragmentList.add(FragmentConsumption())
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("充值记录")
        titleList.add("消费记录")
        return titleList
    }

    override fun getPageTitle(): String {
        return "充值明细"
    }
}