package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.support.v7.widget.ViewUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.bumptech.glide.Glide
import com.tencent.qcloud.tim.tuikit.live.bean.LivePermissionBean
import razerdp.basepopup.BasePopupWindow

class LiveBuyVideoPop(context: Context): BasePopupWindow(context) {
    lateinit var ivIcon:ImageView
    lateinit var tvName:TextView
    lateinit var tvTitle:TextView
    private lateinit var llBuy:LinearLayout
    lateinit var tvPrice:TextView
    private lateinit var ivClose:ImageView
    lateinit var videoId: String
    lateinit var anchorId:String
    lateinit var tvTicketInfo:TextView
    constructor(context: Context,videoId:String,anchorId:String) : this(context) {
        this.videoId = videoId
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
        tvTicketInfo = findViewById(R.id.tv_pop_live_ticket_price)
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
        HttpHelper.getInstance().getPlayBackTicketInfo(videoId,object :HttpCodeListener {
            override fun onSuccess(data: String?) {
                val permissionBean = GsonUtil.GsonToBean(data,LivePermissionBean::class.java)
                permissionBean?.data?.run {
                    //Glide.with(context).load(it.head_pic).into(ivIcon)
                    GlideImgManager.load(context as Activity?,head_pic,ivIcon)
                    tvName.text = nickname
                    tvTitle.text = live_title
                    tvTitle.visibility = if (nickname == live_title) View.GONE else View.VISIBLE
                    tvPrice.text = "${ticket_beans}  购买"
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
        HttpHelper.getInstance().buyPlayBack(videoId,anchorId,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                isBuySuc = true;
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