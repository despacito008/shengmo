package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.LiveChatSettingBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.IRequestCallback
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.NormalEditPop

class LiveChatSettingActivity : BaseActivity() {
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var rbNormal: RadioButton
    private lateinit var rbMute: RadioButton
    private lateinit var rgModel: RadioGroup
    private lateinit var tvAudioPrice: TextView
    private lateinit var tvVideoPrice: TextView
    private lateinit var llAudio: LinearLayout
    private lateinit var llVideo: LinearLayout

    private lateinit var llAuth: LinearLayout
    private lateinit var llSetting: LinearLayout
    private lateinit var tvAuth: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_live_chat_setting)
        StatusBarUtil.showLightStatusBar(this)

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        tvTitle.text = "1v1连线设置"
        ivTitleBack.setOnClickListener {
            finish()
        }

        rgModel = findViewById(R.id.rg_chat_model)
        rbNormal = findViewById(R.id.rb_chat_mode_normal)
        rbMute = findViewById(R.id.rb_chat_mode_mute)
        tvAudioPrice = findViewById(R.id.tv_live_chat_audio_price)
        tvVideoPrice = findViewById(R.id.tv_live_chat_video_price)
        llAudio = findViewById(R.id.ll_live_chat_audio)
        llVideo = findViewById(R.id.ll_live_chat_video)

        llAudio.setOnClickListener {
            showSettingPricePop(1)
        }

        llVideo.setOnClickListener {
            showSettingPricePop(2)
        }

        llAuth = findViewById(R.id.ll_chat_auth)
        tvAuth = findViewById(R.id.tv_chat_auth)
        llSetting = findViewById(R.id.ll_live_chat_setting)

        tvAuth.setOnClickListener {
            val intent = Intent(this, AnchorAuthActivity::class.java)
            startActivity(intent)
        }

        rgModel.setOnCheckedChangeListener { _, rbId ->
                when (rbId) {
                    rbMute.id -> {
                        if (rbMute.isPressed) {
                            setChatModel("0")
                        }
                    }
                    rbNormal.id -> {
                        if (rbNormal.isPressed) {
                            setChatModel("1")
                        }
                    }
                }
        }
    }

    private fun showSettingPricePop(type: Int) {
        val pricePop = NormalEditPop.Builder(this).setTitle(if (type == 1) "设置语音私聊价格" else "设置视频私聊价格")
                .setInfo("")
                .setHint(if (type == 1) "${aMin}魔豆 -${aMax}魔豆" else "${vMin}魔豆 - ${vMax}魔豆")
                .build()
        pricePop.showPopupWindow()
        pricePop.setOnPopClickListener(object : NormalEditPop.OnPopClickListener {
            override fun confirmClick(edit: String?) {
                if (edit.isNullOrEmpty()) {
                    //ToastUtil.show(this@LiveChatSettingActivity, "请设置价格")
                    "请设置价格".showToast()
                    return
                }
                if (type == 1) {
                    if (edit.toInt() < aMin || edit.toInt() > aMax) {
                        //ToastUtil.show(this@LiveChatSettingActivity, "语音聊天价格区间为${aMin}魔豆-${aMax}魔豆")
                        "语音聊天价格区间为${aMin}魔豆-${aMax}魔豆".showToast()
                        return
                    }
                    //tvAudioPrice.text = "${edit}魔豆/分钟"
                    setAudioPrice(edit)
                } else {
                    if (edit.toInt() < vMin || edit.toInt() > vMax) {
                        //ToastUtil.show(this@LiveChatSettingActivity, "视频聊天价格区间为${vMin}魔豆-${vMax}魔豆")
                        "视频聊天价格区间为${vMin}魔豆-${vMax}魔豆".showToast()
                        return
                    }
                    // tvVideoPrice.text = "${edit}魔豆/分钟"
                    setVideoPrice(edit)
                }
                pricePop.dismiss()
            }

            override fun cancelClick() {
                pricePop.dismiss()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getLiveSetting()
    }

    private var vMin = 0
    private var vMax = 0
    private var aMin = 0
    private var aMax = 0
    private fun getLiveSetting() {
        HttpHelper.getInstance().getLiveChat(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, LiveChatSettingBean::class.java)
                tempData?.data?.run {
                    rbMute.isChecked = chat_status == "0"
                    rbNormal.isChecked = chat_status == "1"
                    tvAudioPrice.text = "$chat_voice_beans 魔豆/分钟"
                    tvVideoPrice.text = "$chat_video_beans 魔豆/分钟"
                    llAuth.visibility = if (is_anchor == 0) View.VISIBLE else View.GONE
                    llSetting.visibility = if (is_anchor == 1) View.VISIBLE else View.GONE
                    vMin = video_beans_min
                    vMax = video_beans_max
                    aMin = voice_beans_min
                    aMax = voice_beans_max
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@LiveChatSettingActivity, msg)
            }
        })
    }

    private fun setChatModel(model: String) {
        setLiveChat(model, "", "", null)
    }

    private fun setAudioPrice(aPrice: String) {
        setLiveChat("", aPrice, "", object : IRequestCallback {
            override fun onSuccess(response: String?) {
                tvAudioPrice.text = "$aPrice 魔豆/分钟"
            }

            override fun onFailure(throwable: Throwable?) {
            }
        })
    }

    private fun setVideoPrice(aPrice: String) {
        setLiveChat("", "", aPrice, object : IRequestCallback {
            override fun onSuccess(response: String?) {
                tvVideoPrice.text = "$aPrice 魔豆/分钟"
            }

            override fun onFailure(throwable: Throwable?) {
            }
        })
    }

    private fun setLiveChat(state: String, aPrice: String, vPrice: String, callback: IRequestCallback?) {
        HttpHelper.getInstance().setLiveChat(state, aPrice, vPrice, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                //ToastUtil.show(this@LiveChatSettingActivity, msg)
                msg?.showToast()
                callback?.onSuccess("")
            }

            override fun onFail(code: Int, msg: String?) {
                //ToastUtil.show(this@LiveChatSettingActivity, msg)
                msg?.showToast()
            }
        })
    }
}