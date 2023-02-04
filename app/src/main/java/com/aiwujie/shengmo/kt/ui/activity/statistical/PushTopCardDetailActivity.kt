package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.fragment.purserecordfragment.*
import com.aiwujie.shengmo.kt.ui.fragment.PushTopBuyFragment
import com.aiwujie.shengmo.kt.ui.fragment.PushTopUsFragment
import com.aiwujie.shengmo.kt.ui.fragment.PushTopUseFragment

class PushTopCardDetailActivity: StatisticalDetailActivity() {

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        //fragmentList.add(FragmentEjectionBuy())
        //fragmentList.add(EjectionStampUse())
        //fragmentList.add(TaRenEjectionStampUse())
        fragmentList.add(PushTopBuyFragment())
        fragmentList.add(PushTopUseFragment())
        fragmentList.add(PushTopUsFragment())
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("购买记录")
        titleList.add("使用记录")
        titleList.add("被推记录")
        return titleList
    }

    override fun getPageTitle(): String {
        return "推顶卡明细"
    }
}