package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.eventbus.BigHornEvent
import com.aiwujie.shengmo.kt.ui.fragment.PublicScreenGiftFragment
import com.aiwujie.shengmo.kt.ui.fragment.PublicScreenPushTopFragment
import com.aiwujie.shengmo.kt.ui.fragment.PublicScreenRedBagFragment
import com.aiwujie.shengmo.kt.ui.fragment.PublicScreenVipFragment
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.saveSpValue
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import org.greenrobot.eventbus.EventBus

class PublicScreenActivity : PublicScreenBaseActivity() {

    lateinit var giftNum: String
    lateinit var redBagNum: String
    lateinit var vipNum: String
    lateinit var pushTopNum: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //修改下划线的长度
        /* mDynamicNoticeTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mDynamicNoticeTabs, 60, 60);
            }
        });*/
        SharedPreferencesUtils.setParam(applicationContext, "labaState", "0")
        EventBus.getDefault().post(BigHornEvent(0))
    }

    override fun getFragmentList(): List<Fragment> {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        //添加页卡视图
        //fragmentList.add(NoticePresentFragment())
        fragmentList.add(PublicScreenGiftFragment())
        fragmentList.add(PublicScreenRedBagFragment())
        //fragmentList.add(NoticeVipFragment())
        fragmentList.add(PublicScreenVipFragment())
        //fragmentList.add(NoticeTopcardFragment())
        fragmentList.add(PublicScreenPushTopFragment())
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList: ArrayList<String> = ArrayList()
        titleList.add("礼物")
        titleList.add("红包")
        titleList.add("会员")
        titleList.add("推顶")
        return titleList
    }

    override fun getPageTitle(): String {
        return "大喇叭"
    }

    override fun initViewComplete() {
        super.initViewComplete()
        giftNum = "giftnum".getSpValue("")
        pushTopNum = "topcardnum".getSpValue("")
        vipNum = "vipnum".getSpValue("")
        redBagNum = "redbagnum".getSpValue("")
        showRedDot(0, giftNum)
        showRedDot(1, redBagNum)
        showRedDot(2, vipNum)
        showRedDot(3, pushTopNum)
        "giftnum".saveSpValue("0")
        "topcardnum".saveSpValue("0")
        "vipnum".saveSpValue("0")
        "redbagnum".saveSpValue("0")
    }

    private fun showRedDot(index: Int, dotNum: String) {
        val tvRedDot: TextView? = tabStatistical.getTabAt(index)?.customView?.findViewById(R.id.tv_public_screen_count)
        if (dotNum.isEmpty() || dotNum == "0") {
            tvRedDot?.visibility = View.GONE
        } else {
            tvRedDot?.run {
                visibility = View.VISIBLE
                text = dotNum
            }
        }
    }
}