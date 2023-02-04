package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.tencent.qcloud.tim.tuikit.live.bean.LivePermissionBean
import razerdp.basepopup.BasePopupWindow

class LiveBuyTicketPop(context: Context): BasePopupWindow(context) {
    lateinit var ivIcon:ImageView
    lateinit var tvName:TextView
    lateinit var tvTitle:TextView
    lateinit var llBuy:LinearLayout
    lateinit var tvPrice:TextView
    lateinit var ivClose:ImageView
    lateinit var anchorId: String
    constructor(context: Context,anchorId:String) : this(context) {
        this.anchorId = anchorId
        initView()
        getTicketInfo()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_buy_ticket)
    }

    fun initView() {
        ivIcon = findViewById(R.id.circle_iv_pop_ticket_icon)
        ivClose = findViewById(R.id.iv_pop_live_ticket_close)
        tvName = findViewById(R.id.tv_pop_ticket_name)
        tvTitle = findViewById(R.id.tv_pop_ticket_title)
        llBuy = findViewById(R.id.ll_pop_live_ticket_buy)
        tvPrice = findViewById(R.id.tv_pop_live_ticket_price)
        llBuy.setOnClickListener {
            buyTicket()
        }
        ivClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        if (!isBuySuc) {
            onTicketPopListener?.doPopDismiss()
        }
    }


    private fun getTicketInfo() {
        HttpHelper.getInstance().getTicketInfo(anchorId,object :HttpCodeListener {
            override fun onSuccess(data: String?) {
                val activity = context as Activity
                activity?.run {
                    val permissionBean = GsonUtil.GsonToBean(data,LivePermissionBean::class.java)
                    permissionBean?.data?.let {
                        GlideImgManager.load(activity,it.head_pic,ivIcon)
                        tvName.text = it.nickname
                        tvTitle.text = it.live_title
                        tvTitle.visibility = if (it.nickname == it.live_title) View.GONE else  View.VISIBLE
                        tvPrice.text = "${it.ticket_beans}  购买"
                    }
                }

            }

            override fun onFail(code: Int, msg: String?) {
               ToastUtil.show(context,msg)
            }
        })
    }
    var isBuySuc:Boolean = false
    private fun buyTicket() {
        llBuy.isClickable = false
        HttpHelper.getInstance().buyTicket(anchorId,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                isBuySuc = true
                onTicketPopListener?.doPopBuySuc()
            }

            override fun onFail(code: Int, msg: String?) {
                onTicketPopListener?.doPopBuyFail(msg)
            }
        })
    }

    interface OnTicketPopListener {
        fun doPopBuySuc()
        fun doPopBuyFail(msg:String?)
        fun doPopDismiss()
    }

    var onTicketPopListener:OnTicketPopListener? = null
}