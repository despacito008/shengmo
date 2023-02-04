package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveLinkChatStateBean
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeAndGiftActivity
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.RechargeGiftActivity
import com.aiwujie.shengmo.kt.ui.fragment.RechargeRecordFragment
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import razerdp.basepopup.BasePopupWindow

class LinkUserTypePop(context: Context): BasePopupWindow(context) {

    lateinit var tvClose:ImageView
    lateinit var llLinkAudio:LinearLayout
    lateinit var llLinkVideo:LinearLayout
    lateinit var tvLinkAudioPrice:TextView
    lateinit var tvLinkVideoPrice:TextView
    var uid = ""

    private val tvLinkUserBalance:TextView by lazy { findViewById<TextView>(R.id.tv_pop_link_user_balance) }
    private val tvLinkUserRecharge:TextView by lazy { findViewById<TextView>(R.id.tv_pop_link_user_recharge) }

    constructor(context: Context, uid: String):this(context) {
        this.uid = uid
        initView()
        getLiveChatState()
    }

    fun initView() {
        tvClose = findViewById(R.id.iv_pop_link_user_close)
        llLinkAudio = findViewById(R.id.ll_pop_link_user_audio)
        llLinkVideo = findViewById(R.id.ll_pop_link_user_video)
        tvLinkAudioPrice = findViewById(R.id.tv_pop_link_type_audio_price)
        tvLinkVideoPrice = findViewById(R.id.tv_pop_link_type_video_price)

        tvClose.setOnClickListener {
            dismiss()
        }

        tvLinkUserRecharge.setOnClickListener {
            RechargeGiftActivity.start(context)
        }

    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_link_type)
    }

    interface OnLinkUserTypeListener {
        fun doLinkAudio()
        fun doLinkVideo()
    }

    var onLinkUserTypeListener:OnLinkUserTypeListener? = null

    private fun getLiveChatState() {
        HttpHelper.getInstance().getAnchorChatAuth(uid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String, msg: String) {
                val liveLinkChatStateBean = GsonUtil.GsonToBean(data, LiveLinkChatStateBean::class.java)
                liveLinkChatStateBean?.data?.run {
                    if (is_anchor == 1) {
                        tvLinkAudioPrice.text = "$chat_voice_beans 魔豆/分钟"
                        tvLinkVideoPrice.text = "$chat_video_beans 魔豆/分钟"
                        tvLinkUserBalance.text = wallet
                        llLinkAudio.setOnClickListener {
                            onLinkUserTypeListener?.doLinkAudio()
                        }

                        llLinkVideo.setOnClickListener {
                            onLinkUserTypeListener?.doLinkVideo()
                        }
                    }
                }

            }

            override fun onFail(code: Int, msg: String) {
                msg.showToast()
            }
        })
    }
}