package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.kt.ui.fragment.ShareRelationGroupFragment
import com.aiwujie.shengmo.kt.ui.fragment.ShareUserRelationShipFragment

class ShareUserActivity : StatisticalDetailActivity() {
    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(
                ShareUserRelationShipFragment.newInstance(0),
                ShareUserRelationShipFragment.newInstance(1),
                ShareUserRelationShipFragment.newInstance(2),
                ShareRelationGroupFragment()
        )
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("关注(SVIP)", "粉丝(SVIP)", "好友", "群组")
    }

    override fun getPageTitle(): String {
        return "分享到"
    }
}