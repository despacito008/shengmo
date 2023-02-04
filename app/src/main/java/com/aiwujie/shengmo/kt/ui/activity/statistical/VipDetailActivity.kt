package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.fragment.purserecordfragment.*
import com.aiwujie.shengmo.kt.ui.fragment.VipBuyFragment
import com.aiwujie.shengmo.kt.ui.fragment.VipReceiveFragment
/**
 * 会员明细
 */
class VipDetailActivity: StatisticalDetailActivity() {

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        //fragmentList.add(FragmentVipGmGive())
        fragmentList.add(VipBuyFragment())
        fragmentList.add(VipReceiveFragment())
        //fragmentList.add(FragmentVipHzGive())
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("购买记录")
        titleList.add("获赠记录")
        return titleList
    }

    override fun getPageTitle(): String {
        return "会员明细"
    }
}