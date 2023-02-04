package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.fragment.groupfragment.FragmentGroupZh
import com.aiwujie.shengmo.fragment.groupfragment.FragmentGroupZj
import com.aiwujie.shengmo.kt.ui.fragment.SearchNearGroupFragment

/**
 * 群组 - 搜索结果页
 */
class GroupSearchResultActivity : StatisticalDetailActivity() {
    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        fragmentList.add(SearchNearGroupFragment.newInstance(0))
        fragmentList.add(SearchNearGroupFragment.newInstance(1))
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("最近的群")
        titleList.add("最火的群")
        return titleList
    }

    override fun getPageTitle(): String {
        return "搜索：" + intent.getStringExtra("search")
    }
}