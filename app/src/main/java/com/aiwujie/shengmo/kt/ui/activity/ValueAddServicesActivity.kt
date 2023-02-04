package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.StampActivity
import com.aiwujie.shengmo.activity.newui.DynamicPushActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.utils.StatusBarUtil


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: ValueAddServicesActivity
 * @Author: xmf
 * @CreateDate: 2022/5/25 16:51
 * @Description: 增值服务
 */
class ValueAddServicesActivity: BaseActivity() {
    private val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val ivBack: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val llCall:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_call) }
    private val llPushTop:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_push_top) }
    private val llStamp:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_stamp) }
    private val llFansClub:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_fans_club) }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context,ValueAddServicesActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_value_add_services)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle.text = "增值服务"
        llCall.setOnClickListener {
            ConversationCallActivity.start(this)
        }
        llPushTop.setOnClickListener {
            DynamicPushActivity.start(this)
        }
        llStamp.setOnClickListener {
           StampActivity.start(this)
        }
        ivBack.setOnClickListener { finish() }
        llFansClub.setOnClickListener {
            FansClubListActivity.start(this)
        }
    }
}
