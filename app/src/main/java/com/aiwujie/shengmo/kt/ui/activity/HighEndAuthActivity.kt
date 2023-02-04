package com.aiwujie.shengmo.kt.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.MainActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.eventbus.DynamicPageTurnEvent
import com.aiwujie.shengmo.eventbus.HighPageTurnEvent
import com.aiwujie.shengmo.eventbus.MainPageTurnEvent

import com.aiwujie.shengmo.kt.ui.activity.normallist.HighServiceOpenActivity
import com.aiwujie.shengmo.kt.ui.fragment.*
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.utils.StatusBarUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HighEndAuthActivity : BaseActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var vpAuth: ViewPager

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var tvOpen: TextView
    private var wealthStatus: String? = null
    private var healthStatus: String? = null
    private var educationStatus: String? = null
    private var skillStatus: String? = null
    private var otherStatus: String? = null

    companion object {
        const val maxListItem = 3
        var IsTopUser: String? = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.app_activity_high_end_authentication)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        tvOpen = findViewById(R.id.tv_open)

        IsTopUser = intent.getStringExtra(IntentKey.FLAG)
        wealthStatus = intent.getStringExtra("wealth")
        healthStatus = intent.getStringExtra("health")
        educationStatus = intent.getStringExtra("education")
        skillStatus = intent.getStringExtra("skill")
        otherStatus = intent.getStringExtra("other")

        if(IsTopUser =="1"){
            tvOpen.visibility=View.GONE
        }else {
            tvOpen.visibility=View.VISIBLE
        }



        ivTitleBack.setOnClickListener { finish() }




        tvTitleRight.visibility = View.VISIBLE
        ivTitleRight.visibility = View.GONE
        tvTitle.text = "高端认证"
        tvTitleRight.text = "高端会员"



        tvTitleRight.setOnClickListener {
            EventBus.getDefault().post(MainPageTurnEvent(0, 3))
            finish()
        }

        tvOpen.setOnClickListener {
            startActivity(Intent(this@HighEndAuthActivity, HighServiceOpenActivity::class.java))
        }


        val fragments: MutableList<Fragment> = ArrayList()
        fragments.apply {
            add(HighEndAuthWealthFragment())
            add(HighEndAuthHealthFragment())
            add(HighEndAuthEducationFragment())
            add(HighEndAuthSkillFragment())
            add(HighEndAuthOtherFragment())
        }
        vpAuth = findViewById(R.id.view_pager_activity_high_end_authentication)
        tabLayout = findViewById(R.id.tab_activity_high_end_authentication)
        val fragmentStatePagerAdapter: FragmentStatePagerAdapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return fragments.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }
        }
        vpAuth.adapter = fragmentStatePagerAdapter
        tabLayout.setupWithViewPager(vpAuth)
        for (i in 0..5) {
            tabLayout.getTabAt(i)?.customView = getTabView(i)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<ImageView>(R.id.iv_tab_high_end_auth)
                val tvTab = tab?.customView?.findViewById<TextView>(R.id.tv_tab_high_end_auth)
//                ivTab?.isSelected = false
                tvTab?.apply {
                    setTextColor(ContextCompat.getColor(this@HighEndAuthActivity, R.color.normalGray))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<ImageView>(R.id.iv_tab_high_end_auth)
                val tvTab = tab?.customView?.findViewById<TextView>(R.id.tv_tab_high_end_auth)
//                ivTab?.isSelected = true

                tvTab?.apply {
                    setTextColor(ContextCompat.getColor(this@HighEndAuthActivity, R.color.titleBlack))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
            }
        })
        tabLayout.getTabAt(0)?.select()
    }


    @SuppressLint("InflateParams")
    fun getTabView(index: Int): View {
        val tabView = layoutInflater.inflate(R.layout.app_tab_high_end_tab, null)
        val ivTab = tabView.findViewById<ImageView>(R.id.iv_tab_high_end_auth)
        val tvTab = tabView.findViewById<TextView>(R.id.tv_tab_high_end_auth)
        when (index) {
            0 -> {
                ivTab.setImageResource(if (wealthStatus == "1") {
                    R.drawable.ic_high_end_wealth_done
                } else {
                    R.drawable.ic_high_end_wealth_normal
                })
//                ivTab.setImageResource(R.drawable.select_high_end_auth_wealth)
                tvTab.text = "财产认证"
            }
            1 -> {
                ivTab.setImageResource(if (healthStatus == "1") {
                    R.drawable.ic_high_end_health_done
                } else {
                    R.drawable.ic_high_end_health_normal
                })
//                ivTab.setImageResource(R.drawable.select_high_end_auth_health)
                tvTab.text = "健康认证"
            }
            2 -> {
                ivTab.setImageResource(if (educationStatus == "1") {
                    R.drawable.ic_high_end_education_done
                } else {
                    R.drawable.ic_high_end_education_normal
                })
//                ivTab.setImageResource(R.drawable.select_high_end_auth_education)
                tvTab.text = "学历认证"
            }
            3 -> {
                ivTab.setImageResource(if (skillStatus == "1") {
                    R.drawable.ic_high_end_skill_done
                } else {
                    R.drawable.ic_high_end_skill_normal
                })
//                ivTab.setImageResource(R.drawable.select_high_end_auth_skill)
                tvTab.text = "技能认证"
            }
            4 -> {
                ivTab.setImageResource(if (otherStatus == "1") {
                    R.drawable.ic_high_end_other_done
                } else {
                    R.drawable.ic_high_end_other_normal
                })
//                ivTab.setImageResource(R.drawable.select_high_end_auth_other)
                tvTab.text = "其他认证"
            }
        }
        return tabView
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: MainPageTurnEvent) {

    }


}