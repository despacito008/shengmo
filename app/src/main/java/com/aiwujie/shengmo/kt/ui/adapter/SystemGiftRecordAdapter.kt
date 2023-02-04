package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.SystemGiftRecordBean

class SystemGiftRecordAdapter(var context:Context,var recordList:List<SystemGiftRecordBean.DataBean.ListBean>) : RecyclerView.Adapter<SystemGiftRecordAdapter.RecordHolder>() {

    inner class RecordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvItemInfo = itemView.findViewById<TextView>(R.id.tv_item_gift_record_info)
        private var tvItemTime = itemView.findViewById<TextView>(R.id.tv_item_gift_record_time)
        fun display(index:Int) {
            tvItemInfo.text = recordList[index].text
            tvItemTime.text = "${recordList[index].addtime_format}  ${recordList[index].week}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecordHolder {
        return RecordHolder(LayoutInflater.from(context).inflate(R.layout.app_item_sysetm_gift_record,parent,false))
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onBindViewHolder(holder: RecordHolder?, position: Int) {
        holder?.display(position)
    }
}