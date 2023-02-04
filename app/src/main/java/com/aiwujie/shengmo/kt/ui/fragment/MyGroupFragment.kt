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
import com.aiwujie.shengmo.activity.CreateGroupActivity
import com.aiwujie.shengmo.activity.MakeGroupFailActivity
import com.aiwujie.shengmo.fragment.groupfragment.MyGroupFragment
import com.aiwujie.shengmo.fragment.groupfragment.MyGroupListFragment
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.utils.SharedPreferencesUtils

/**
 * @program: newshengmo
 * @description: 群广场-我的
 * @author: whl
 * @create: 2022-06-01 09:37
 **/
class MyGroupFragment:NormalTabFragment() {

    fun newInstance(): MyGroupFragment? {
        val args = Bundle()
        val fragment = MyGroupFragment()
        fragment.arguments = args
        return fragment
    }
    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(MyGroupListFragment.newInstance("3"), MyGroupListFragment.newInstance("2"))
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("创建的", "加入的")
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
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
            text = activity.resources.getString(R.string.create_group_str)
            layoutParams = params
        }
        flTop.addView(ruleView)
        val admin = SpKey.ADMIN.getSpValue()
        if ("1" == admin) {
            ruleView.visibility = View.VISIBLE
        } else {
            ruleView.visibility = View.INVISIBLE
        }

        ruleView.setOnClickListener {
            val vip =SpKey.VIP.getSpValue()
            val admin = SpKey.ADMIN.getSpValue()
            val intent: Intent
            intent = if (vip == "1" || admin == "1") {
                if ("1" == admin) {
                    Intent(activity, CreateGroupActivity::class.java)
                } else {
                    Intent(activity, MakeGroupFailActivity::class.java)
                }
            } else {
                Intent(activity, MakeGroupFailActivity::class.java)
            }
            startActivity(intent)
        }
    }

    private fun setViewPagerMarginTop() {
        vpNormal.layoutParams = with(vpNormal.layoutParams as FrameLayout.LayoutParams) {
            topMargin = 50.dp
            this
        }
    }

}