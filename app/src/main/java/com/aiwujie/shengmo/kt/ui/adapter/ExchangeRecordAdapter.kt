package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.BillData
import com.aiwujie.shengmo.bean.ConsumptionExchangeData
import com.aiwujie.shengmo.bean.PresentReceiveData

class ExchangeRecordAdapter(var context:Context, var dataList:List<ConsumptionExchangeData.DataBean>): RecyclerView.Adapter<ExchangeRecordAdapter.ReceiveGiftHolder>() {

    inner class ReceiveGiftHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvItemInfo:TextView = itemView.findViewById(R.id.item_recorder_rmb)
        var tvItemDate:TextView = itemView.findViewById(R.id.item_recorder_date)
        var tvItemType:TextView = itemView.findViewById(R.id.item_recorder_day)
        var tvItemBean:TextView = itemView.findViewById(R.id.item_recorder_modou)

        fun display(index:Int) {
            dataList[index]?.let {
                tvItemInfo.text = it.text
                tvItemDate.text = it.addtime_format
                tvItemType.text = it.week
                tvItemBean.text = it.beans
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReceiveGiftHolder {
        return ReceiveGiftHolder(LayoutInflater.from(context).inflate(R.layout.item_listview_recorder,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ReceiveGiftHolder?, position: Int) {
        holder?.display(position)
    }
}