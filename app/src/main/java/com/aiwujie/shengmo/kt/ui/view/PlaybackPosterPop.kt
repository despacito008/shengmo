package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.constraint.Group
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.bumptech.glide.Glide
import com.donkingliang.labels.LabelsView
import com.donkingliang.labels.LabelsView.*
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean.DataBean.LabelBean
import razerdp.basepopup.BasePopupWindow
import java.util.*

class PlaybackPosterPop(context: Context) : BasePopupWindow(context) {
    lateinit var ivPoster: ImageView
    lateinit var etTitle: EditText
    lateinit var tvConfirm: TextView

    lateinit var logId: String
    var liveTitle = ""
    var livePoster = ""
    constructor(context: Context, logId: String) : this(context) {
        this.logId = logId
        initView()
        popupGravity = Gravity.BOTTOM
    }

    constructor(context: Context, logId: String,title:String,poster:String) : this(context) {
        this.logId = logId
        this.liveTitle = title
        this.livePoster = poster
        initView()
        popupGravity = Gravity.BOTTOM
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_play_back_poster)
    }

    fun initView() {
        etTitle = findViewById(R.id.et_pop_live_info_title)
        tvConfirm = findViewById(R.id.tv_pop_live_info_confirm)
        ivPoster = findViewById(R.id.iv_pop_live_info_icon)
        this.setAdjustInputMethod(true)
        getLiveCardInfo()
        initListener()
    }

    fun initListener() {
        tvConfirm.setOnClickListener {
            changePlayBackInfo()
        }
        etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                liveTitle = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        ivPoster.setOnClickListener {
            onSimpleListener?.doSimplePop()
        }
    }


    private fun getLiveCardInfo() {
        if (!TextUtils.isEmpty(liveTitle)) {
            etTitle.setText(liveTitle)
        }
        if (!TextUtils.isEmpty(livePoster)) {
            Glide.with(context).load(livePoster).into(ivPoster)
        }
    }

    var onSimpleListener: OnSimplePopListener? = null

    fun refreshImageUrl(url: String) {
        livePoster = url;
        Glide.with(context).load(livePoster).into(ivPoster)
        changePlayBackInfo(false)
    }

    interface OnPlaybackInfoPopListener {
        fun doLiveInfoRefresh(title: String, cover: String)
    }

    var infoListener:OnPlaybackInfoPopListener? = null


    private fun changePlayBackInfo(isNeedDismiss: Boolean = true) {
        HttpHelper.getInstance().editPlayBackInfo(logId,liveTitle,livePoster,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(context,"更新成功")
                if (isNeedDismiss) {
                    dismiss()
                }
                infoListener?.doLiveInfoRefresh(liveTitle,livePoster)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }
        })
    }
}