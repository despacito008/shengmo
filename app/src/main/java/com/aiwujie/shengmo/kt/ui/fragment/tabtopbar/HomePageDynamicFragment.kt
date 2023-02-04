package com.aiwujie.shengmo.kt.ui.fragment.tabtopbar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.SendDynamicActivity
import com.aiwujie.shengmo.activity.ranking.ActiveAndPopularityRankingActivity
import com.aiwujie.shengmo.customview.VipDialog
import com.aiwujie.shengmo.eventbus.DynamicPageTurnEvent
import com.aiwujie.shengmo.eventbus.DynamicSxEvent
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent
import com.aiwujie.shengmo.kt.ui.fragment.FollowDynamicFragment
import com.aiwujie.shengmo.kt.ui.fragment.HotDynamicFragment
import com.aiwujie.shengmo.kt.ui.fragment.SquareDynamicFragment
import com.aiwujie.shengmo.kt.ui.fragment.topic.DynamicTopicFragment
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.utils.AnimationUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.ToastUtil
import com.zhy.android.percent.support.PercentLinearLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment.tabtopbar
 * @ClassName: HomePageDynamicFragment
 * @Author: xmf
 * @CreateDate: 2022/4/22 14:37
 * @Description: 首页 - 动态
 */
class HomePageDynamicFragment : TabTopBarFragment() {
    private lateinit var squareDynamicFragment: SquareDynamicFragment
    private lateinit var followDynamicFragment: FollowDynamicFragment
    private lateinit var dynamicTopicFragment: DynamicTopicFragment
    private lateinit var hotDynamicFragment: HotDynamicFragment
    override fun getFragmentList(): List<Fragment> {
        hotDynamicFragment = HotDynamicFragment()
        squareDynamicFragment = SquareDynamicFragment()
        followDynamicFragment = FollowDynamicFragment()
        dynamicTopicFragment = DynamicTopicFragment()
        return arrayListOf(hotDynamicFragment, squareDynamicFragment, followDynamicFragment, dynamicTopicFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("热推", "广场", "关注", "话题")
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        EventBus.getDefault().register(this)
        initSendDynamicBtn()
        initScreenState()
        vpTabTopBar.addOnPageChangeListenerDsl {
            onPageSelected {
                when (it) {
                    0, 1 -> {
                        ivScreen.visibility = View.VISIBLE
                        EventBus.getDefault().post(ViewIsVisibleEvent(1))
                    }
                    2, 3 -> {
                        ivScreen.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    lateinit var ivSendDynamic: ImageView
    private fun initSendDynamicBtn() {
        val params = FrameLayout.LayoutParams(100.dp, 30.dp)
        params.gravity = Gravity.CENTER or Gravity.BOTTOM
        params.bottomMargin = 15.dp
        ivSendDynamic = ImageView(activity)
        ivSendDynamic.setImageResource(R.mipmap.senddynamic)
        ivSendDynamic.layoutParams = params
        flRoot.addView(ivSendDynamic)
        ivSendDynamic.clickDelay {
            gotoSendDynamicPage()
        }
    }

    override fun initTabLeftView() {
        super.initTabLeftView()
        val ivRank = ImageView(activity)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivRank.setImageResource(R.drawable.ic_home_notice_active)
        ivRank.layoutParams = params
        ivRank.setPadding(15.dp, 15.dp, 15.dp, 15.dp)
        flLeft.addView(ivRank)
        ivRank.clickDelay {
            gotoRankPage()
        }
    }

    lateinit var ivScreen: ImageView
    override fun initTabRightView() {
        super.initTabRightView()
        ivScreen = ImageView(activity)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivScreen.setImageResource(R.drawable.shuaixuan)
        ivScreen.layoutParams = params
        ivScreen.setPadding(15.dp, 15.dp, 15.dp, 15.dp)
        flRight.addView(ivScreen)
        ivScreen.clickDelay {
            showScreenPop()
        }
    }

    private fun gotoRankPage() {
        val intent = Intent(activity, ActiveAndPopularityRankingActivity::class.java)
        startActivity(intent)
    }

    private fun gotoSendDynamicPage() {
        val intent = Intent(activity,SendDynamicActivity::class.java)
        startActivity(intent)
    }

    private fun initScreenState() {
        val sex = "dynamicSex".getSpValue("")
        val sexual = "dynamicSexual".getSpValue("")
        val dynamicSexual = "dynamicSxSexual".getSpValue("")
        if (dynamicSexual == "1") {
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
        } else {
            if (sex.isEmpty() && sexual.isEmpty()) {
                ivScreen.setImageResource(R.drawable.shuaixuan)
            } else {
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            }
        }
    }

    private fun showScreenPop() {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.item_dynamic_pop, null)
        val mPopWindow = PopupWindow(contentView)
        mPopWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mPopWindow.isFocusable = true
        activity.alphaBackground(0.5f)
        mPopWindow.setBackgroundDrawable(BitmapDrawable())
        mPopWindow.showAsDropDown(flLeft)
        mPopWindow.animationStyle = R.style.AnimationPreview
        val llNan = contentView.findViewById<View>(R.id.item_dynamic_pop_llNan) as TextView
        val llNv = contentView.findViewById<View>(R.id.item_dynamic_pop_llNv) as TextView
        val llNao = contentView.findViewById<View>(R.id.item_dynamic_pop_llCdts) as TextView
        val llAll = contentView.findViewById<View>(R.id.item_dynamic_pop_llAll) as TextView
        val tvSexual = contentView.findViewById<View>(R.id.item_dynamic_pop_tvSexual) as TextView
        val llSexual = contentView.findViewById<View>(R.id.item_dynamic_pop_llSexual) as PercentLinearLayout
        val sex = "dynamicSex".getSpValue("")
        val sexual = "dynamicSexual".getSpValue("")
        val dynamicSexual = "dynamicSxSexual".getSpValue("")
        val vipState = SpKey.VIP.getSpValue("0")
        if (sex == "" && sexual == "") {
            llAll.setTextColor(Color.parseColor("#b73acb"))
        }
        if (sex == "1" && sexual == "") {
            llNan.setTextColor(Color.parseColor("#b73acb"))
        }
        if (sex == "2" && sexual == "") {
            llNv.setTextColor(Color.parseColor("#b73acb"))
        }
        if (sex == "3" && sexual == "") {
            llNao.setTextColor(Color.parseColor("#b73acb"))
        }
        if (dynamicSexual == "1") {
            tvSexual.setTextColor(Color.parseColor("#b73acb"))
        }
        mPopWindow.setOnDismissListener { activity.alphaBackground(1f) }
        llNan.clickDelay {
            if (vipState == "1") {
                screenDynamic("1", "", true)
                "dynamicSxSexual".saveSpValue("0")
                mPopWindow.dismiss()
            } else {
                VipDialog(activity, "会员才能使用筛选功能哦~")
            }
        }
        llNv.clickDelay {
            if (vipState == "1") {
                screenDynamic("2", "", true)
                "dynamicSxSexual".saveSpValue("0")
                mPopWindow.dismiss()
            } else {
                VipDialog(activity, "会员才能使用筛选功能哦~")
            }
        }
        llNao.clickDelay {
            if (vipState == "1") {
                screenDynamic("3", "", true)
                "dynamicSxSexual".saveSpValue("0")
                mPopWindow.dismiss()
            } else {
                VipDialog(activity, "会员才能使用筛选功能哦~")
            }
        }
        llAll.clickDelay {
            screenDynamic("", "", false)
            "dynamicSxSexual".saveSpValue("0")
            mPopWindow.dismiss()
        }
        llSexual.clickDelay {
            if (vipState == "1") {
                val dynamicSex = SharedPreferencesUtils.getParam(activity.applicationContext, "mydynamicSex", "") as String
                val dynamicSexual = SharedPreferencesUtils.getParam(activity.applicationContext, "mydynamicSexual", "") as String
                if (dynamicSex != "" && dynamicSexual != "") {
                    SharedPreferencesUtils.setParam(activity.applicationContext, "dynamicSxSexual", "1")
                    screenDynamic(dynamicSex, dynamicSexual, true)
                    //设置沙漏为紫色
                    ivScreen.setImageResource(R.drawable.iv_home_select_done)
                } else {
                    ToastUtil.show(activity.applicationContext, "请您重新登录,才可以使用此功能...")
                }
            } else {
                VipDialog(activity, "会员才能使用筛选功能哦~")
            }
        }
    }

    private fun screenDynamic(dynamicSex: String, dynamicSexual: String, isOpen: Boolean) {
        "dynamicSwitch".saveSpValue(if (isOpen) "1" else "0")
        "dynamicSex".saveSpValue(dynamicSex)
        "dynamicSexual".saveSpValue(dynamicSexual)
        EventBus.getDefault().post(DynamicSxEvent(dynamicSex, dynamicSexual))
        if (dynamicSex.isEmpty() && dynamicSexual.isEmpty()) {
            ivScreen.setImageResource(R.drawable.shuaixuan)
        } else {
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: DynamicPageTurnEvent) {
        //selectTab(event.position)
        vpTabTopBar.currentItem = event.position
        if (event.position == 1) {
            squareDynamicFragment.setCheckedTab(1)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessages(event: ViewIsVisibleEvent) {
        when (event.isVisible) {
            0 -> if (ivSendDynamic.visibility == View.VISIBLE) {
                ivSendDynamic.visibility = View.GONE
                ivSendDynamic.animation = AnimationUtil.moveToViewBottom()
            }
            1 -> if (ivSendDynamic.visibility == View.GONE) {
                ivSendDynamic.visibility = View.VISIBLE
                ivSendDynamic.animation = AnimationUtil.moveToViewLocation()
            }
        }
    }
}
