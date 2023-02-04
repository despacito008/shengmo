package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.*
import com.bumptech.glide.Glide
import com.tencent.qcloud.tim.tuikit.live.bean.LiveLevelProcessBean

/**
 * 直播等级页面
 */
class MyLiveLevelActivity: BaseActivity() {

    lateinit var ivIcon:ImageView
    private lateinit var ivIdentity:ImageView
    private lateinit var ivLevelSign:ImageView
    private lateinit var ivLevelCurrent:ImageView
    private lateinit var ivLevelNext:ImageView
    lateinit var ivLevelDesc:ImageView
    lateinit var tvLevel:TextView
    lateinit var tvLevelCurrent:TextView
    lateinit var tvLevelNext:TextView
    lateinit var tvLevelTip:TextView
    lateinit var progressBar:ProgressBar
    private lateinit var llLivelevel:LinearLayout

    private lateinit var ivTitleRight:ImageView
    private lateinit var ivTitleBack:ImageView
    private lateinit var tvTitle:TextView
    private lateinit var tvTitleRight:TextView

    var type = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_my_live_level)
        type = intent.getStringExtra("type")!!
        StatusBarUtil.showLightStatusBar(this)
        initView()
        getLevelData()
    }

    fun initView() {
        ivIcon = findViewById(R.id.iv_my_live_level_icon)
        ivIdentity = findViewById(R.id.iv_my_live_level_identity)
        ivLevelSign = findViewById(R.id.iv_my_live_level)
        ivLevelCurrent = findViewById(R.id.iv_my_live_level_current)
        ivLevelNext = findViewById(R.id.iv_my_live_level_next)
        ivLevelDesc = findViewById(R.id.iv_my_live_level_desc)

        tvLevel = findViewById(R.id.tv_my_live_level)
        tvLevelCurrent = findViewById(R.id.tv_my_live_level_current)
        tvLevelNext = findViewById(R.id.tv_my_live_level_next)
        tvLevelTip = findViewById(R.id.tv_my_live_level_next_level_tips)

        llLivelevel = findViewById(R.id.ll_my_live_level)
        progressBar = findViewById(R.id.pb_my_live_level_progress)

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        ivTitleRight.visibility = View.INVISIBLE

        if (type == "1") {
            tvTitle.text = "主播等级"
        } else {
            tvTitle.text = "用户等级"
        }
        initLevelIcon(type == "1")

        ivTitleBack.setOnClickListener {
            finish()
        }
    }

    private fun initLevelIcon(isAnchor: Boolean) {
        if (isAnchor) {
            ivLevelSign.setImageResource(R.drawable.ic_item_live_anchor_level)
            ivLevelCurrent.setImageResource(R.drawable.ic_item_live_anchor_level)
            ivLevelNext.setImageResource(R.drawable.ic_item_live_anchor_level)
            progressBar.progressDrawable = ContextCompat.getDrawable(this@MyLiveLevelActivity,R.drawable.pg_page_anchor_level)
            llLivelevel.setBackgroundResource(R.drawable.bg_round_my_live_anchor_level)
            tvLevel.setTextColor(ContextCompat.getColor(this@MyLiveLevelActivity,R.color.liveLevelAnchorColor))
        } else {
            ivLevelSign.setImageResource(R.drawable.ic_item_live_audience_level)
            ivLevelCurrent.setImageResource(R.drawable.ic_item_live_audience_level)
            ivLevelNext.setImageResource(R.drawable.ic_item_live_audience_level)
            progressBar.progressDrawable = ContextCompat.getDrawable(this@MyLiveLevelActivity,R.drawable.pg_page_audience_level)
            llLivelevel.setBackgroundResource(R.drawable.bg_round_my_live_user_level)
            tvLevel.setTextColor(ContextCompat.getColor(this@MyLiveLevelActivity,R.color.liveLevelUserColor))
        }
    }

    private fun getLevelData() {
        HttpHelper.getInstance().getMyLiveLevel(type,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val liveData = GsonUtil.GsonToBean(data,LiveLevelProcessBean::class.java)
                liveData?.data?.let {
                    ImageLoader.loadCircleImage(this@MyLiveLevelActivity,it.head_pic,ivIcon)
                    tvLevelCurrent.text = "${it.level}"
                    tvLevel.text = "${it.level}"
                    val nextLevel = it.level + 1
                    tvLevelNext.text = "$nextLevel"
                    progressBar.progress = it.progress
                    tvLevelTip.text = it.diff_tips
                    Glide.with(this@MyLiveLevelActivity).load(it.img_url).into(ivLevelDesc)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@MyLiveLevelActivity,msg)
            }
        })
    }
}