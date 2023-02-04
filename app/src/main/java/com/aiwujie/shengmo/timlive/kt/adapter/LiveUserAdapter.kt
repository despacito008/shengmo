package com.aiwujie.shengmo.timlive.kt.adapter

import android.content.Context
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.utils.DateUtils
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide

class LiveUserAdapter(private val context: Context, private val data: List<ScenesRoomInfoBean>) : RecyclerView.Adapter<LiveUserAdapter.SearchUserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchUserHolder {
        return SearchUserHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_user, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchUserHolder?, position: Int) {
        holder!!.setData(position)
    }

    inner class SearchUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivItemIcon: ImageView = itemView.findViewById(R.id.iv_item_live_user_icon)
        private var ivItemIdentity: ImageView = itemView.findViewById(R.id.iv_item_live_user_identity)
        private var ivItemLiveIng: ImageView = itemView.findViewById(R.id.iv_item_live_user_live_ing)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_live_user_name)
        private var tvItemBrowse: TextView = itemView.findViewById(R.id.tv_item_live_user_browse)
        private var tvItemTime: TextView = itemView.findViewById(R.id.tv_item_live_user_time)
        private var llItemLiveStatus: LinearLayout = itemView.findViewById(R.id.ll_item_live_user_status)
        private var ivItemMask: View = itemView.findViewById(R.id.view_item_live_user_mask)
        private var tvItemEnd:TextView = itemView.findViewById(R.id.tv_item_live_user_end)
        private var ivItemInteract:ImageView = itemView.findViewById(R.id.iv_item_live_interact)
        private var ivItemAudio:ImageView = itemView.findViewById(R.id.iv_item_live_audio)
        private var ivItemContract:ImageView = itemView.findViewById(R.id.iv_item_live_contract)
        private var tvItemTicket:TextView = itemView.findViewById(R.id.tv_item_live_ticket_count)
        private var groupTicket: Group = itemView.findViewById(R.id.group_item_live_ticket)

        fun setData(index: Int) {
            var userBean = data[index]
            UserIdentityUtils.showIdentity(context, userBean.head_pic, userBean.uid, userBean.is_volunteer, userBean.is_admin, userBean.svipannual, userBean.svip, userBean.vipannual, userBean.vip, userBean.bkvip, userBean.blvip, ivItemIdentity)
            tvItemName.text = userBean.nickname
            tvItemBrowse.text = "历史最高${userBean.watchsum}同时观看"
            when (userBean.is_live) {
                "1" -> {
                    llItemLiveStatus.visibility = View.VISIBLE
                    GlideImgManager.glideLoader(context, userBean.head_pic, R.drawable.rc_image_error, R.drawable.rc_image_error, ivItemIcon, 1)
                    Glide.with(context).load(R.drawable.ic_status_living).into(ivItemLiveIng)
                    tvItemTime.text = getLiveTime(userBean.live_time)
                    ivItemMask.visibility = View.GONE
                    tvItemEnd.visibility = View.GONE
                    ivItemInteract.visibility = if ("1" == userBean.is_interaction) View.VISIBLE else View.GONE
                    ivItemAudio.visibility = if ("1" == userBean.camera_switch) View.VISIBLE else View.GONE
                    ivItemContract.visibility = if ("2" == userBean.anchor_status || "3" == userBean.anchor_status) View.VISIBLE else View.GONE
                    groupTicket.visibility = if (userBean.is_ticket == "1") View.VISIBLE else View.GONE
                    tvItemTicket.text = userBean.ticket_beans
                }
                else -> {
                    llItemLiveStatus.visibility = View.GONE
                    GlideImgManager.glideLoader(context, userBean.head_pic, R.drawable.rc_image_error, R.drawable.rc_image_error, ivItemIcon, 1)
                    tvItemTime.text = getLastLiveTime(userBean.live_time)
                    ivItemMask.visibility = View.VISIBLE
                    tvItemEnd.visibility = View.VISIBLE
                    ivItemInteract.visibility = View.GONE
                    groupTicket.visibility = View.GONE

                }
            }
            itemView.setOnClickListener {
                onSimpleItemListener?.apply {
                    this.onItemListener(index)
                }
            }
        }
    }

    private var onSimpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener: OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }
    fun getLiveTime(time:String):String {
        val current = System.currentTimeMillis()/1000
        val dataTime = time.toLong()
        return when(val liveTime = current - dataTime) {
            in 0..300 ->{
                "刚刚开播"
            }
            in 300..3600 ->{
                "已直播${liveTime/60}分"
            }
            else ->{
                "已直播${liveTime/3600}时${liveTime%3600/60}分"
            }
        }
    }

    fun getLastLiveTime(time:String):String {
        val current = System.currentTimeMillis()
        val dataTime = time.toLong() * 1000
        val liveTime = (current - dataTime)/1000
        return if (DateUtils.isSameData(dataTime.toString(),current.toString())) {
            when(liveTime) {
                in 0..3600 ->{
                    "刚刚关播"
                }
                else ->{
                    "上次直播${liveTime/3600}小时前"
                }
            }
        } else{
            return if (liveTime/3600/24 <= 1) {
                 "上次直播昨天"
            } else {
                 "上次直播${liveTime / 3600 / 24}天前"
            }
        }

    }
}