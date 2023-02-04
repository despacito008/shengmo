package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveTaskRankBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide


class LiveTaskRankAdapter(var context:Context,var rankList:List<LiveTaskRankBean.DataBean>) : RecyclerView.Adapter<LiveTaskRankAdapter.LiveTaskHolder>() {
    inner class LiveTaskHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var ivItemIcon:ImageView = itemView.findViewById(R.id.iv_item_live_ranking_icon)
        var ivItemLevel:ImageView = itemView.findViewById(R.id.iv_item_live_ranking_level)
        var tvItemName:TextView = itemView.findViewById(R.id.tv_item_live_ranking_name)
        var tvItemInfo:TextView = itemView.findViewById(R.id.tv_item_live_ranking_info)
        var tvItemIndex:TextView = itemView.findViewById(R.id.tv_item_live_ranking_index)
        fun display(index:Int) {
            rankList[index]?.let {
                //Glide.with(context).load(it.head_pic).into(ivItemIcon)
                ImageLoader.loadCircleImage(context,it.head_pic,ivItemIcon)
                tvItemName.text = it.nickname
                UserIdentityUtils.showUserIdentity(context,it.role,ivItemLevel)
                tvItemInfo.text = it.complete_schedule.toString()
                tvItemIndex.text = (index + 1).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveTaskHolder {
        return LiveTaskHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_task_rank,parent,false))
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: LiveTaskHolder?, position: Int) {
        holder?.display(position)
    }
}