package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.kt.ui.fragment.ExchangeRecordDetailsFragment
import com.aiwujie.shengmo.kt.ui.fragment.ReceiveGiftDetailFragment
import com.aiwujie.shengmo.kt.ui.fragment.SystemGiftDetailsFragment

/**
 * 礼物明细
 */
class PresentDetailActivity: StatisticalDetailActivity() {
    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        fragmentList.add(ReceiveGiftDetailFragment())
        fragmentList.add(SystemGiftDetailsFragment())
        fragmentList.add(ExchangeRecordDetailsFragment.newInstance(1))
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("收到的礼物")
        titleList.add("系统赠送")
        titleList.add("兑换记录")
        return titleList
    }

    override fun getPageTitle(): String {
        return "礼物明细"
    }
}