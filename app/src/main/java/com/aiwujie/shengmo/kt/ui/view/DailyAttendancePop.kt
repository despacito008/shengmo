package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.bean.AttendanceStateBean
import com.aiwujie.shengmo.kt.bean.DailySignResult
import com.aiwujie.shengmo.kt.bean.NormalResultBean
import com.aiwujie.shengmo.kt.util.alphaBackground
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toJsonBean
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.ImageLoader
import razerdp.basepopup.BasePopupWindow

class DailyAttendancePop : BasePopupWindow {
    lateinit var tvSignConfirm: TextView
    lateinit var tvSignDays: TextView
    lateinit var ivSignClose: ImageView
    lateinit var ivDayOne: ImageView
    lateinit var ivDayTwo: ImageView
    lateinit var ivDayThree: ImageView
    lateinit var ivDayFour: ImageView
    lateinit var ivDayFive: ImageView
    lateinit var ivDaySix: ImageView
    lateinit var ivDaySeven: ImageView

    lateinit var llSignResult: LinearLayout
    lateinit var ivSignResult: ImageView
    lateinit var tvSignResult: TextView
    var mContext: Context? = null

    constructor(context: Context?) : super(context) {
        popupGravity = Gravity.CENTER
        initView()
        mContext = context
        if (context is Activity) {
            context.alphaBackground(0.5f)
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_sign_in)
    }

    fun initView() {
        tvSignConfirm = findViewById(R.id.tv_sign_in_sign)
        tvSignDays = findViewById(R.id.tv_sign_in_day)
        ivSignClose = findViewById(R.id.iv_sign_in_close)
        ivDayOne = findViewById(R.id.iv_day_one)
        ivDayTwo = findViewById(R.id.iv_day_two)
        ivDayThree = findViewById(R.id.iv_day_three)
        ivDayFour = findViewById(R.id.iv_day_four)
        ivDayFive = findViewById(R.id.iv_day_five)
        ivDaySix = findViewById(R.id.iv_day_six)
        ivDaySeven = findViewById(R.id.iv_day_seven)
        llSignResult = findViewById(R.id.ll_daily_sign_result)
        ivSignResult = findViewById(R.id.iv_daily_sign_result)
        tvSignResult = findViewById(R.id.tv_daily_sign_result)
        tvSignConfirm.isClickable = false
        getSignState()
        tvSignConfirm.setOnClickListener {
            if (mSignStatus == "0") {
                sign()
            } else {
                dismiss()
            }
        }

        llSignResult.setOnClickListener {
            llSignResult.visibility = View.GONE
        }

        ivSignClose.setOnClickListener {
            dismiss()
        }
    }

    var signDay = 0
    var mSignStatus = "0"
    private fun getSignState() {
        HttpHelper.getInstance().getSignState(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val result = data?.toJsonBean<NormalResultBean<AttendanceStateBean>>()
                result?.data?.run {
                    mSignStatus = signStatus
                    signTimes.toInt().let {
                        signDay = it
                        refreshSignDayView()
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    fun refreshSignDayView() {
        showSignState(ivDayOne, signDay > 0)
        showSignState(ivDayTwo, signDay > 1)
        showSignState(ivDayThree, signDay > 2)
        showSignState(ivDayFour, signDay > 3)
        showSignState(ivDayFive, signDay > 4)
        showSignState(ivDaySix, signDay > 5)
        showSignState(ivDaySeven, signDay > 6)
        tvSignDays.text = "本周已签到${signDay}天"
        if (mSignStatus == "0") {
            tvSignConfirm.text = "签到"
            //tvSignConfirm.isClickable = true
        } else {
            tvSignConfirm.text = "已签到"
            //tvSignConfirm.isClickable = false
        }
    }

    private fun showSignState(v: View, isShow: Boolean) {
        v.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    private fun sign() {
        HttpHelper.getInstance().dailySign(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                //msg?.showToast()
                val result = data?.toJsonBean<NormalResultBean<DailySignResult>>()
                result?.data?.run {
                    llSignResult.visibility = View.VISIBLE
                    ImageLoader.loadImage(mContext, images, ivSignResult)
                    tvSignResult.text = text
                    signDay++
                    mSignStatus = "1"
                    refreshSignDayView()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    override fun onDismiss() {
        super.onDismiss()
        mContext?.run {
            if (mContext is Activity) {
                (mContext as Activity).alphaBackground(1f)
            }
        }
    }
}