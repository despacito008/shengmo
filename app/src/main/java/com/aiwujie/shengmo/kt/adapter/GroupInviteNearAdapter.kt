package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.HomeNewListData
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.aiwujie.shengmo.view.CircleImageView


class GroupInviteNearAdapter(val context: Context, var dataList: ArrayList<HomeNewListData.DataBean>) : RecyclerView.Adapter<GroupInviteNearAdapter.GroupInviteChatHolder>() {
    var chooseIndexList: ArrayList<Int> = ArrayList()

    inner class GroupInviteChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivIcon: CircleImageView = itemView.findViewById(R.id.iv_item_group_invite_icon)
        private var ivState: ImageView = itemView.findViewById(R.id.iv_item_group_invite_state)
        private var ivLevel: ImageView = itemView.findViewById(R.id.iv_item_group_invite_level)
        private var tvName: TextView = itemView.findViewById(R.id.tv_item_group_invite_name)
        fun display(index: Int) {
            dataList[index].run {
                ImageLoader.loadImage(context, this.head_pic, ivIcon,R.mipmap.morentouxiang)
                with(tvName) {
                    text = nickname
                    setTextColor(when (sex) {
                        "1" -> ContextCompat.getColor(context,R.color.boyColor)
                        "2" -> ContextCompat.getColor(context,R.color.girlColor)
                        else -> ContextCompat.getColor(context,R.color.cdtColor)
                    })
                }
                //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
                UserIdentityUtils.showIdentity(context, head_pic, uid, is_volunteer, is_admin, svipannual,vipannual, vipannual,bkvip, blvip,ivLevel)
                ivState.setImageResource(if (chooseIndexList.contains(index)) R.mipmap.atxuanzhong else R.mipmap.atweixuanzhong)
                itemView.setOnClickListener {
                    simpleItemListener?.onItemListener(index)
                    if (chooseIndexList.contains(index)) {
                        chooseIndexList.remove(index)
                    } else {
                        chooseIndexList.add(index)
                    }
                    notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GroupInviteChatHolder {
        return GroupInviteChatHolder(LayoutInflater.from(context).inflate(R.layout.app_item_group_invite, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: GroupInviteChatHolder?, position: Int) {
        holder?.display(position)
    }

    fun getChooseId():List<String> {
        val chooseIdList = ArrayList<String>()
        for (a in chooseIndexList) {
            chooseIdList.add(dataList[a].uid)
        }
        return chooseIdList
    }

    var simpleItemListener: OnSimpleItemListener? = null
}