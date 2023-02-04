package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveTaskBean
import com.bumptech.glide.Glide


class LiveTaskAdapter(var context:Context,var taskList:List<LiveTaskBean.DataBean.TaskListBean>) : RecyclerView.Adapter<LiveTaskAdapter.LiveTaskHolder>() {
    inner class LiveTaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemDesc:TextView = itemView.findViewById(R.id.tv_item_live_audience_task_desc)
        var tvItemReword:TextView = itemView.findViewById(R.id.tv_item_live_audience_task_reward)
        var tvItemState:TextView = itemView.findViewById(R.id.tv_item_live_audience_task_state)
        var ivItemIcon:ImageView = itemView.findViewById(R.id.iv_item_live_audience_task_icon)

        fun display(index:Int) {
            taskList[index]?.let {
                tvItemDesc.text = it.msg + "  " + it.complete_rate
                tvItemReword.text = it.reward
                tvItemState.text = it.status_text
                if (it.status == "1") {
                    tvItemState.setBackgroundColor(context.resources.getColor(R.color.transparent))
                    tvItemState.setTextColor(context.resources.getColor(R.color.purpleColor))
                } else {
                    tvItemState.background = context.resources.getDrawable(R.drawable.bg_round_gray_home)
                    tvItemState.setTextColor(context.resources.getColor(R.color.normalGray))
                }
                Glide.with(context).load(it.task_icon).error(R.drawable.ic_task_icon_gift_1).into(ivItemIcon)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveTaskHolder {
        return LiveTaskHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_audience_task,parent,false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: LiveTaskHolder?, position: Int) {
        holder?.display(position)
    }
}