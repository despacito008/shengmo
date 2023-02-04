package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveSealBean

class LiveSealAdapter(var context: Context,var sealList:List<LiveSealBean.DataBean.ListBean>): RecyclerView.Adapter<LiveSealAdapter.LiveSealHolder>() {
    inner class LiveSealHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvItemInfo:TextView = itemView.findViewById(R.id.tv_item_live_seal_info)
        var tvItemReason:TextView = itemView.findViewById(R.id.tv_item_live_seal_reason)
        var tvItemPeople:TextView = itemView.findViewById(R.id.tv_item_live_seal_people)
        var tvItemTime:TextView = itemView.findViewById(R.id.tv_item_live_seal_time)

        fun display(index:Int) {
            sealList[index].let {
                when (it.type) {
                    "montior_warning" -> {
                        tvItemInfo.text = "警告主播"
                    }
                    "montior_kick" -> {
                        tvItemInfo.text = "强制下播"
                    }
                    "montior_seal" -> {
                        tvItemInfo.text = "封禁主播 " + if (it.blockingalong == "0") "永久" else "${it.blockingalong}天"
                    }
                }
                tvItemReason.text = it.reasontext
                tvItemPeople.text = it.username
                tvItemTime.text = it.addtime
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveSealHolder {
        return LiveSealHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_seal,parent,false))
    }

    override fun getItemCount(): Int {
        return sealList.size
    }

    override fun onBindViewHolder(holder: LiveSealHolder?, position: Int) {
        holder?.display(position)
    }
}