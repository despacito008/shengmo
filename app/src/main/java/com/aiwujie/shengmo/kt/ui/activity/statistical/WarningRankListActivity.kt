package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment

import com.aiwujie.shengmo.fragment.warningfragment.BanUserFragment
import com.aiwujie.shengmo.kt.ui.fragment.IllegalUserFragment
import com.aiwujie.shengmo.kt.ui.fragment.PunishmentFragment


class WarningRankListActivity :StatisticalDetailActivity(){


    override fun getFragmentList(): List<Fragment> {
        return  arrayListOf(PunishmentFragment.newInstance(),IllegalUserFragment.newInstance("1"),IllegalUserFragment.newInstance("2"),IllegalUserFragment.newInstance("3"))

    }

    override fun getTitleList(): List<String> {
        return  arrayListOf("处罚公示","违规用户","可疑用户","封号用户")
    }

    override fun getPageTitle(): String {
       return  "警示榜"
    }



}