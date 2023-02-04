package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.kt.ui.fragment.LiveNewPartitionFragment
import com.aiwujie.shengmo.timlive.frag.LiveRankFragment
import com.aiwujie.shengmo.utils.StatusBarUtil

class NewLivePartitionActivity:BaseActivity() {
    private  val ivBack:ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private  val flContent:FrameLayout by lazy { findViewById<FrameLayout>(R.id.fl_content) }
        private  val tvTitle:TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
        private  val ivMore:ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_more) }

    private var tid:String ? = null
    private var partName:String ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_new_live_partition)
        StatusBarUtil.showLightStatusBar(this)
        tid = intent.getStringExtra("tid")?:""
        partName = intent.getStringExtra("part")?:"分区直播"
        ivBack.setOnClickListener {finish()}
        tvTitle.text = partName
        ivMore .visibility = View.INVISIBLE

        val liveNewPartitionFragment = LiveNewPartitionFragment()
        val bundle =Bundle()
        bundle.putString("tid",tid)
        liveNewPartitionFragment.arguments= bundle
        supportFragmentManager.beginTransaction().add(R.id.fl_content,liveNewPartitionFragment).commitAllowingStateLoss()
//        liveNewPartitionFragment.setOnLoadCompleteListener {
//            liveRankFragment.loadData()
//        }
    }



}