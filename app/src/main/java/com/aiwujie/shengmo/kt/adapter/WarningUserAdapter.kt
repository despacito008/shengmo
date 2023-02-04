package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.WarningListData
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.UserIdentityUtils

class WarningUserAdapter(var context: Context, var mList: ArrayList<WarningListData.DataBean>, var isShowFlag:Boolean) :RecyclerView.Adapter<WarningUserAdapter.WarningUserViewHolder>(){
    private  var  mType :String?= ""
    constructor( context: Context,  mList: ArrayList<WarningListData.DataBean>,  isShowFlag:Boolean,type:String ):this(context, mList, isShowFlag){
        mType =type
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WarningUserViewHolder {

        return  WarningUserViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_waring_user, parent,false))

    }

    override fun getItemCount(): Int {
        return  mList.size

    }

    override fun onBindViewHolder(holder: WarningUserViewHolder?, position: Int) {
        val data: WarningListData.DataBean = mList[position]
//          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        //          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.head_pic, data.uid, data.is_volunteer, data.is_admin, data.svipannual, data.svip, data.vipannual, data.vip, data.bkvip, data.blvip, holder!!.itemListviewWarningVip)
        if (data.realname == "0") {
            holder!!.itemListviewWarningShiming.visibility = View.GONE
        } else {
            holder!!.itemListviewWarningShiming.visibility = View.VISIBLE
        }

        if (data.sex == "1") {
            holder!!.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nan_bg)
            holder!!.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan)
        } else if (data.sex == "2") {
            holder!!.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nv_bg)
            holder!!.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv)
        } else if (data.sex == "3") {
            holder!!.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_san_bg)
            holder!!.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san)
        }
        if (data.role == "S") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nan_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "斯"
        } else if (data.role == "M") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "慕"
        } else if (data.role == "SM") {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg)
            holder!!.tvLayoutUserNormalInfoRole.text = "双"
        }
        else  {
            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
            holder!!.tvLayoutUserNormalInfoRole.text=data.role
        }
//        else if (data.role == "~") {
//            holder!!.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg)
//            holder!!.tvLayoutUserNormalInfoRole.text = "~"
//        }
        holder!!.tvLayoutUserNormalInfoAge.text = data.age

        if (data.head_pic != HttpUrl.NetPic() || data.head_pic != "") { //"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder!!.itemListviewWarningIcon, 0)
        } else {
            holder!!.itemListviewWarningIcon.setImageResource(R.mipmap.morentouxiang)
        }
        holder!!.itemListviewWarningName.text = data.nickname
        if (data.charm_val != "0") {
            holder!!.llLayoutUserNormalInfoCharm.visibility = View.VISIBLE
            holder!!.tvLayoutUserNormalInfoCharm.text = data.charm_val
        } else {
            holder!!.llLayoutUserNormalInfoCharm.visibility = View.GONE
        }

        if (data.wealth_val != "0") {
            holder!!.llLayoutUserNormalInfoWealth.visibility = View.VISIBLE
            holder!!.tvLayoutUserNormalInfoWealth.text = data.wealth_val
        } else {
            holder!!.llLayoutUserNormalInfoWealth.visibility = View.GONE
        }

        if (data.onlinestate == 1) {
            holder!!.ivItemOnline.visibility = View.VISIBLE
        } else {
            holder!!.ivItemOnline.visibility = View.GONE
        }

        if (isShowFlag) {
            if (mType  != "3"){
                if (data.dynamicstatus == "0") {
                    holder!!.tvItemBanDynamic.visibility = View.VISIBLE
                    holder!!.tvItemBanDynamic.text = "已禁动态-" + getBanTime(data.dynamic_blockingalong)
                } else {
                    holder!!.tvItemBanDynamic.visibility = View.GONE
                }
                if (data.infostatus == "0") {
                    holder!!.tvItemBanInfo.visibility = View.VISIBLE
                    holder!!.tvItemBanInfo.text = "已禁资料-" + getBanTime(data.info_blockingalong)
                } else {
                    holder!!.tvItemBanInfo.visibility = View.GONE
                }
                if (data.chatstatus == "0") {
                    holder!!.tvItemBanChat.visibility = View.VISIBLE
                    holder!!.tvItemBanChat.text = "已禁聊天-" + getBanTime(data.chat_blockingalong)
                } else {
                    holder!!.tvItemBanChat.visibility = View.GONE
                }
                if ("1" == data.prohibition_status) {
                    holder!!.tvBanLive.visibility = View.VISIBLE
                    holder!!.tvBanLive.text = "已禁直播-" + getBanTime(data.live_chat_blockingalong)
                } else {
                    holder!!.tvBanLive.visibility = View.GONE
                }
                if ("0" == data.livestatus) {
                    holder!!.tvBanWatchLive.visibility = View.VISIBLE
                    holder!!.tvBanWatchLive.text = "禁看直播-" + getBanTime(data.watchlive_blockingalong)
                } else {
                    holder!!.tvBanWatchLive.visibility = View.GONE
                }
            }else  {
                if (data.status == "0") {
                    holder!!.tvItemBanAccount.visibility = View.VISIBLE
                    holder!!.tvItemBanAccount.text = "已禁账号-" + getBanTime(data.account_blockingalong)
                } else {
                    holder!!.tvItemBanAccount.visibility = View.GONE
                }
                if (data.devicestatus == "0") {
                    holder!!.tvItemBanDevice.visibility = View.VISIBLE
                    holder!!.tvItemBanDevice.text = "已禁设备-" + getBanTime(data.device_blockingalong)
                } else {
                    holder!!.tvItemBanDevice.visibility = View.GONE
                }
                holder!!.tvItemBanTime.text = "" + data.handletime
            }

        }
        if (mType  != "3"){
            holder!!.tvItemBanTime.text = "" + data.handletime
        }
        holder.itemView.setOnClickListener {
            onUserClick!!.onUserClick(position)
        }

    }



    interface  OnUserClick{
        fun  onUserClick(position: Int)
    }
    var onUserClick:OnUserClick ?= null

    private fun getBanTime(along: String): String? {
        return when (along) {
            "1" -> "1天"
            "3" -> "3天"
            "7" -> "1周"
            "14" -> "2周"
            "30", "31" -> "1月"
            else -> "永久"
        }
    }

    class WarningUserViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){

        var itemListviewWarningIcon: ImageView  =itemView.findViewById(R.id.iv_user_avatar_icon)

        var itemListviewWarningVip: ImageView =itemView.findViewById(R.id.iv_user_avatar_level)

        var itemListviewWarningName: TextView=itemView.findViewById(R.id.tv_layout_user_normal_info_name)

        var itemListviewWarningShiming: ImageView=itemView.findViewById(R.id.iv_layout_user_normal_info_auth_pic)

        var ivLayoutUserNormalInfoSex: ImageView=itemView.findViewById(R.id.iv_layout_user_normal_info_sex)

        var tvLayoutUserNormalInfoAge: TextView=itemView.findViewById(R.id.tv_layout_user_normal_info_age)

        var llLayoutUserNormalInfoSexAge: LinearLayout=itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)

        var tvLayoutUserNormalInfoRole: TextView=itemView.findViewById(R.id.tv_layout_user_normal_info_role)

        var ivLayoutUserNormalInfoWealth: ImageView=itemView.findViewById(R.id.iv_layout_user_normal_info_wealth)

        var tvLayoutUserNormalInfoWealth: TextView=itemView.findViewById(R.id.tv_layout_user_normal_info_wealth)

        var llLayoutUserNormalInfoWealth: LinearLayout=itemView.findViewById(R.id.ll_layout_user_normal_info_wealth)

        var ivLayoutUserNormalInfoCharm: ImageView=itemView.findViewById(R.id.iv_layout_user_normal_info_charm)

        var tvLayoutUserNormalInfoCharm: TextView=itemView.findViewById(R.id.tv_layout_user_normal_info_charm)

        var llLayoutUserNormalInfoCharm: LinearLayout=itemView.findViewById(R.id.ll_layout_user_normal_info_charm)

        var ivItemOnline: ImageView=itemView.findViewById(R.id.iv_user_avatar_online)

        var tvItemBanDynamic: TextView=itemView.findViewById(R.id.tv_item_ban_dynamic)

        var tvItemBanInfo: TextView=itemView.findViewById(R.id.tv_item_ban_info)

        var tvItemBanChat: TextView=itemView.findViewById(R.id.tv_item_ban_chat)

        var tvItemBanAccount: TextView=itemView.findViewById(R.id.tv_item_ban_account)

        var tvItemBanDevice: TextView=itemView.findViewById(R.id.tv_item_ban_device)

        var tvItemBanTime: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_city)

        var tvBanLive: TextView= itemView.findViewById(R.id.tv_item_ban_live)

        var tvBanWatchLive: TextView= itemView.findViewById(R.id.tv_item_ban_watch_live)
    }
}