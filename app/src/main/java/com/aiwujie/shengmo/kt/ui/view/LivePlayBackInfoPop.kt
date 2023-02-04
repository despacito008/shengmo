package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.ToastUtil
import razerdp.basepopup.BasePopupWindow

class LivePlayBackInfoPop(context: Context):BasePopupWindow(context) {
    lateinit var tvPlayPrice:TextView
    lateinit var tvPlayConfirm:TextView
    lateinit var etPlayPrice:EditText
    lateinit var cbNeedPay:CheckBox
    lateinit var videoId:String
    var isNeedPay:Boolean = false
    var free:Int = 0
    constructor(context: Context,videoId:String,isNeedPay:Boolean,free:Int): this(context) {
        this.videoId = videoId
        this.isNeedPay = isNeedPay
        this.free = free
        initView()
    }

    fun initView() {
        tvPlayPrice = findViewById(R.id.tv_start_live_ticket_price)
        etPlayPrice = findViewById(R.id.et_pop_live_info_ticket)
        tvPlayConfirm = findViewById(R.id.tv_pop_live_info_confirm)
        cbNeedPay = findViewById(R.id.cb_start_live_ticket)
        this.setAdjustInputMethod(true)
        if (isNeedPay) {
            cbNeedPay.isChecked = true
            tvPlayPrice.text = "（${free}魔豆）"
            etPlayPrice.visibility = View.VISIBLE
            etPlayPrice.setText(free.toString())
        } else {
            cbNeedPay.isChecked = false
            tvPlayPrice.text = "（非必选）"
            etPlayPrice.visibility = View.GONE
        }

        etPlayPrice.addTextChangedListener(object :TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())) {
                    free = 0
                } else {
                    val ticketBeansNum = s.toString().toInt()
                    free = ticketBeansNum
                    if (ticketBeansNum > 1000) {
                        free = 1000
                        etPlayPrice.setText(1000.toString())
                        etPlayPrice.setSelection(etPlayPrice.text.toString().length)
                    }
                }
                tvPlayPrice.text = "（" + free + "魔豆）"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        cbNeedPay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    etPlayPrice.visibility = View.VISIBLE
                    isNeedPay = true
                } else {
                    etPlayPrice.visibility = View.GONE
                    tvPlayPrice.text = "(非必选)"
                    isNeedPay = false
                }
            }
        }

        tvPlayConfirm.setOnClickListener {
            setPlayPayTicket()
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_play_back_info)
    }


    private fun setPlayPayTicket() {
        if (isNeedPay && free == 0) {
            ToastUtil.show(context,"设置回放付费需要设置价格")
            return
        }
        HttpHelper.getInstance().setPlayBackTicket(videoId,if (isNeedPay) "1" else "0",free.toString(),object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(context,"设置成功")
                dismiss()
                playBackInfoListener?.setInfoSuc(if (isNeedPay) "1" else "0",free.toString())
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }
        })
    }

    interface PlayBackInfoListener {
        fun setInfoSuc(isNeedPay:String,free:String)
    }

    var playBackInfoListener:PlayBackInfoListener? = null

}