package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.aiwujie.shengmo.fragment.user.GiftFragment
import com.aiwujie.shengmo.fragment.user.RechargeFragment

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.statistical
 * @ClassName: RechageAndGiftActivity
 * @Author: xmf
 * @CreateDate: 2022/4/7 17:47
 * @Description: 充值 & 礼物 页面
 */
class RechargeAndGiftActivity:RelationShipDetailActivity() {

    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(RechargeFragment(), GiftFragment())
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("充值", "礼物")
    }

    override fun initViewComplete() {
        super.initViewComplete()
        tvNormalTitleMore.visibility = View.VISIBLE
        tvNormalTitleMore.text = "明细"
        tvNormalTitleMore.setOnClickListener {
            when (vpStatistical.currentItem) {
                0 -> {
                    startActivity(Intent(this, RechargeDetailActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this, PresentDetailActivity::class.java))
                }
            }
        }
    }
}
