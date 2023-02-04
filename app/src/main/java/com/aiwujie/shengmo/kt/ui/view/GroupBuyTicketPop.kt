package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.FansClubTicketInfoBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.ToastUtil
import com.tencent.qcloud.tim.tuikit.live.bean.LivePermissionBean
import razerdp.basepopup.BasePopupWindow

//粉丝群购买入群券
class GroupBuyTicketPop(context: Context): BasePopupWindow(context) {
    lateinit var ivIcon:ImageView
    lateinit var tvName:TextView
    lateinit var tvTitle:TextView
    private lateinit var llBuy:LinearLayout
    lateinit var tvPrice:TextView
    private lateinit var ivClose:ImageView
    lateinit var mGroupId: String
    lateinit var anchorId:String
    lateinit var tvTicketInfo:TextView
    constructor(context: Context,groupId:String,anchorId:String) : this(context) {
        this.mGroupId = groupId
        this.anchorId = anchorId
        initView()
        getTicketInfo()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_buy_ticket)
    }

    private val viewBg:View by lazy { findViewById<View>(R.id.view_pop_bg) }
    fun initView() {
        ivIcon = findViewById(R.id.circle_iv_pop_ticket_icon)
        ivClose = findViewById(R.id.iv_pop_live_ticket_close)
        tvName = findViewById(R.id.tv_pop_ticket_name)
        tvTitle = findViewById(R.id.tv_pop_ticket_title)
        llBuy = findViewById(R.id.ll_pop_live_ticket_buy)
        tvPrice = findViewById(R.id.tv_pop_live_ticket_price)
        tvTicketInfo = findViewById(R.id.tv_pop_ticket_info)
        viewBg.setBackgroundColor(Color.parseColor("#55000000"))
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

    private var giftId = ""
    private fun getTicketInfo() {
        HttpHelper.getInstance().getFansClubGiftInfo(mGroupId,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?,msg:String?) {
                val tempData = GsonUtil.GsonToBean(data,FansClubTicketInfoBean::class.java)
                tempData?.data?.run {
                    tvName.text = groupname
                    ImageLoader.loadImage(context,group_pic,ivIcon)
                    tvTicketInfo.text = tip
                    tvTitle.text = introduce
                    tvPrice.text = "$gift_beans  入群"
                    giftId = gift_id
                    anchorId = anchor_uid
                }
            }

            override fun onFail(code: Int, msg: String?) {
               msg?.showToast()
            }
        })
    }
    var isBuySuc:Boolean = false
    private fun buyTicket() {
        llBuy.isClickable = false
        HttpHelper.getInstance().buyFansClubTicket(mGroupId,anchorId,giftId,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?,msg:String) {
                isBuySuc = true
                dismiss()
                msg.showToast()
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