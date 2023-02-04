package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.KeyboardShortcutInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LotteryDrawRecordBean

class LotteryDrawRecordAdapter(val context:Context,private val recordList:ArrayList<LotteryDrawRecordBean.DataBean>):RecyclerView.Adapter<LotteryDrawRecordAdapter.RecordHolder>() {

    inner class RecordHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvItemInfo:TextView = itemView.findViewById(R.id.tv_item_lottery_draw_record_info)
        var tvItemTime:TextView = itemView.findViewById(R.id.tv_item_lottery_draw_record_time)
        fun display(index:Int) {
            recordList[index].run {
                tvItemInfo.text = text
                tvItemTime.text = addtime_format
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecordHolder {
        return RecordHolder(LayoutInflater.from(context).inflate(R.layout.app_item_lottery_draw_record,parent,false))
    }

    override fun onBindViewHolder(holder: RecordHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }
}