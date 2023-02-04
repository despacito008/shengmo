package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.UserInviteListBean
import com.aiwujie.shengmo.utils.OnSimpleItemListener


class UserInviteListAdapter(var context:Context,var inviteList:List<UserInviteListBean.DataBean>) : RecyclerView.Adapter<UserInviteListAdapter.UserInviteHolder>() {
    inner class UserInviteHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvItemName:TextView = itemView.findViewById(R.id.tv_item_invite_name)
        var tvItemTime:TextView = itemView.findViewById(R.id.tv_item_invite_time)
        fun display(index:Int) {
            inviteList[index]?.let {
                when(it.sex) {
                    "1" -> {
                        tvItemName.setTextColor(context.resources.getColor(R.color.boyColor))
                    }
                    "2" -> {
                        tvItemName.setTextColor(context.resources.getColor(R.color.girlColor))
                    }
                    "3" -> {
                        tvItemName.setTextColor(context.resources.getColor(R.color.cdtColor))
                    }
                }
                tvItemName.text = it.nickname
                tvItemTime.text = "邀请时间：" + it.register_time
            }
            itemView.setOnClickListener {
                simpleListener?.onItemListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserInviteHolder {
        return UserInviteHolder(LayoutInflater.from(context).inflate(R.layout.app_item_invite_user,parent,false))
    }

    override fun getItemCount(): Int {
        return inviteList.size
    }

    override fun onBindViewHolder(holder: UserInviteHolder?, position: Int) {
        holder?.display(position)
    }

    var simpleListener:OnSimpleItemListener? = null
}