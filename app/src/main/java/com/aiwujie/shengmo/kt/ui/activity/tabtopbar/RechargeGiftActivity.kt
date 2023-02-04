package com.aiwujie.shengmo.kt.ui.activity.tabtopbar

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.fragment.user.GiftFragment
import com.aiwujie.shengmo.fragment.user.RechargeFragment
import com.aiwujie.shengmo.kt.ui.activity.statistical.PresentDetailActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeAndGiftActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeDetailActivity
import com.aiwujie.shengmo.kt.util.dp

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.tabtopbar
 * @ClassName: RechargeGiftActivity
 * @Author: xmf
 * @CreateDate: 2022/4/21 19:48
 * @Description:
 */
class RechargeGiftActivity: TabTopBarActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RechargeGiftActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(RechargeFragment(),GiftFragment())
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("充值","礼物")
    }

    override fun initTabLeftView() {
        super.initTabLeftView()
        var ivBack = ImageView(this)
        val params = FrameLayout.LayoutParams(50.dp,50.dp)
       // val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        ivBack.setImageResource(R.drawable.normal_back)
        ivBack.layoutParams = params
        ivBack.setPadding(17.dp,17.dp,17.dp,17.dp)
        flLeft.addView(ivBack)
        ivBack.setOnClickListener {
            finish()
        }
    }

    override fun initTabRightView() {
        super.initTabRightView()
        var tvDetail = TextView(this)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        tvDetail.text = "明细"
        tvDetail.layoutParams = params
        tvDetail.gravity = Gravity.CENTER
        flRight.addView(tvDetail)
        tvDetail.setOnClickListener {
            when (vpTabTopBar.currentItem) {
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
