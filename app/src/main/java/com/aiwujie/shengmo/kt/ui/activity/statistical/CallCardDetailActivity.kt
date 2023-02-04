package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.aiwujie.shengmo.kt.ui.fragment.PushTopBuyFragment
import com.aiwujie.shengmo.kt.ui.fragment.normallist.CallCardBuyHistoryFragment
import com.aiwujie.shengmo.kt.ui.fragment.normallist.CallCardUseHistoryFragment

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.statistical
 * @ClassName: CallCardDetailActivity
 * @Author: xmf
 * @CreateDate: 2022/5/26 15:41
 * @Description: 呼唤明细
 */
class CallCardDetailActivity: StatisticalDetailActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context,CallCardDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(CallCardBuyHistoryFragment(), CallCardUseHistoryFragment())
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("购买明细","使用明细")
    }

    override fun getPageTitle(): String {
        return "呼唤明细"
    }
}
