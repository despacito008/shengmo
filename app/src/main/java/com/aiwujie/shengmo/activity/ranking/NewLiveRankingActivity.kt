package com.aiwujie.shengmo.activity.ranking

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.timlive.frag.LiveRankFragment
import com.aiwujie.shengmo.utils.StatusBarUtil

class NewLiveRankingActivity:BaseActivity() {
    lateinit var ivBack:ImageView
    lateinit var tvRight:TextView
    lateinit var flContent:FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_new_live_rank)
        StatusBarUtil.showLightStatusBar(this)
        ivBack = findViewById(R.id.iv_live_ranking_return)
        tvRight = findViewById(R.id.tv_live_ranking_right)
        ivBack.setOnClickListener {finish()}
        tvRight.setOnClickListener {
            val intent = Intent(this,RewardRankingActivity::class.java)
            startActivity(intent)
        }
        val liveRankFragment = LiveRankFragment()
        supportFragmentManager.beginTransaction().add(R.id.fl_content,liveRankFragment).commitAllowingStateLoss()
        liveRankFragment.setOnLoadCompleteListener {
            liveRankFragment.loadData()
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

    }
}