package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.util.dp

/**
 * 消息 - 群广场 - 认领
 */
class GroupClaimFragment: NormalTabFragment() {

    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(NormalGroupFragment.newInstance(6), NormalGroupFragment.newInstance(7))
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("待认领", "已认领")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRuleView()
        setViewPagerMarginTop()
    }

    private lateinit var ruleView:TextView
    private fun initRuleView() {
        ruleView = TextView(activity)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        with(params) {
            gravity = Gravity.END or Gravity.CENTER
            rightMargin = 10.dp
        }
        with(ruleView) {
            setBackgroundResource(R.drawable.border_group_claim)
            setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            setLineSpacing(1f, 0.9f)
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8f)
            setPadding(5.dp, 3.dp, 5.dp, 3.dp)
            text = activity.resources.getString(R.string.claim_rule)
            layoutParams = params
        }
        flTop.addView(ruleView)
        ruleView.setOnClickListener {
            val intent = Intent(activity, BannerWebActivity::class.java)
            with(intent) {
                putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/20")
                putExtra("title", "圣魔")
                startActivity(this)
            }
        }
    }

    private fun setViewPagerMarginTop() {
        vpNormal.layoutParams = with(vpNormal.layoutParams as FrameLayout.LayoutParams) {
            topMargin = 50.dp
            this
        }
    }

}