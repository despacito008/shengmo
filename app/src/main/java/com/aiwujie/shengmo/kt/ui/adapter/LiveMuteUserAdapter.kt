package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.timlive.bean.LiveOnlineUserListInfo
import com.aiwujie.shengmo.timlive.bean.ManagerList
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener

class LiveMuteUserAdapter(private val context: Context, private var onLineUserList:List<ManagerList.DataBean>): RecyclerView.Adapter<LiveMuteUserAdapter.LiveOnlineUserHolder>() {

    private lateinit var inviteList:ArrayList<Int>

    inner class LiveOnlineUserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvItemName:TextView = itemView.findViewById(R.id.tv_item_live_online_user)
        private var ivItemIcon:ImageView = itemView.findViewById(R.id.iv_user_avatar_icon)
        private var ivItemOnline:ImageView = itemView.findViewById(R.id.iv_user_avatar_online)
        private var ivItemIdentity:ImageView = itemView.findViewById(R.id.iv_user_avatar_level)
        private var ivItemSex: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)
        private var llItemSexAndAge: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)
        private var tvItemAge: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)
        private var tvItemRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)
        private var llItemWealth: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_wealth)
        private var llItemCharm: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_charm)
        private var tvItemWealth: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_wealth)
        private var tvItemCharm: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_charm)
        private var tvItemLink:TextView = itemView.findViewById(R.id.tv_item_live_online_user_remove)
        private var tvItemTime:TextView = itemView.findViewById(R.id.tv_item_live_online_user_time)

        fun display(index:Int) {

            tvItemLink.visibility = View.VISIBLE
            tvItemLink.text = "????????????"

            onLineUserList[index].let {
                ImageLoader.loadCircleImage(context, it.head_pic, ivItemIcon, R.mipmap.morentouxiang)
                ivItemIdentity.visibility = View.VISIBLE
                if ("1" == it.is_admin) {
                    ivItemIdentity.setImageResource(R.drawable.user_manager)
                } else if ("1" == it.svipannual) {
                    ivItemIdentity.setImageResource(R.drawable.user_svip_year)
                } else if ("1" == it.svip) {
                    ivItemIdentity.setImageResource(R.drawable.user_svip)
                } else if ("1" == it.vipannual) {
                    ivItemIdentity.setImageResource(R.drawable.user_vip_year)
                } else if ("1" == it.vip) {
                    ivItemIdentity.setImageResource(R.drawable.user_vip)
                } else {
                    ivItemIdentity.visibility = View.GONE
                }
                tvItemName.text = it.nickname
                when(it.sex) {
                    "1" -> {
                        llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                        ivItemSex.setImageResource(R.mipmap.nan)
                    }
                    "2" -> {
                        llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                        ivItemSex.setImageResource(R.mipmap.nv)
                    }
                    else -> {
                        llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                        ivItemSex.setImageResource(R.mipmap.san)
                    }
                }
                tvItemAge.text = it.age
                when(it.role) {
                    "S" -> {
                        tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                        tvItemRole.text = "???"
                    }
                    "M" -> {
                        tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                        tvItemRole.text = "???"
                    }
                    "SM" -> {
                        tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                        tvItemRole.text = "???"
                    }

                    else -> {
                        tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
                        tvItemRole.text = it.role
                    }
                }
                llItemCharm.visibility = View.GONE
                llItemWealth.visibility = View.GONE
                ivItemOnline.visibility = View.GONE

                tvItemTime.text = it.unblock_time
            }


            tvItemLink.setOnClickListener {
                tvItemLink.isClickable = false
                tvItemLink.postDelayed(Runnable { tvItemLink.isClickable = true },2000)
                onMuteUserListener?.doUserCancelMute(index)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveOnlineUserHolder {
        inviteList = ArrayList<Int>()
        return LiveOnlineUserHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_mute_user,parent,false))
    }

    override fun getItemCount(): Int {
        return onLineUserList.size
    }

    override fun onBindViewHolder(holder: LiveOnlineUserHolder?, position: Int) {
        holder?.display(position)
    }


     interface OnMuteUserListener {
        fun doUserCancelMute(index:Int)
    }

    var onMuteUserListener: OnMuteUserListener?= null


}