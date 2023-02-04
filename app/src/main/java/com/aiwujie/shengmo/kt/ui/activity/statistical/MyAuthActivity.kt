package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.support.v4.app.Fragment
import com.aiwujie.shengmo.fragment.Wdrz_sfzrzfragment
import com.aiwujie.shengmo.fragment.Wdrz_zprzfragment
import com.aiwujie.shengmo.kt.ui.fragment.MyPhotoAuthFragment

class MyAuthActivity: StatisticalDetailActivity() {
    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(MyPhotoAuthFragment(),Wdrz_sfzrzfragment())
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("自拍认证","身份认证")
    }

    override fun getPageTitle(): String {
        return "我的认证"
    }


}