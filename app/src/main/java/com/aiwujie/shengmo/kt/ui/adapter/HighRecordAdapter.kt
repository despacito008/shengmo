package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.BillData
import com.aiwujie.shengmo.bean.HighDetailModle
import com.aiwujie.shengmo.bean.PresentReceiveData
import org.feezu.liuli.timeselector.Utils.TextUtil

class HighRecordAdapter(var context:Context, var dataList:List<HighDetailModle.DataBean>): RecyclerView.Adapter<HighRecordAdapter.ReceiveGiftHolder>() {

    inner class ReceiveGiftHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvItemInfo:TextView = itemView.findViewById(R.id.item_recorder_rmb)
        var tvItemDate:TextView = itemView.findViewById(R.id.item_recorder_date)
        var tvItemType:TextView = itemView.findViewById(R.id.item_recorder_day)
        var tvItemBean:TextView = itemView.findViewById(R.id.item_recorder_modou)

        fun display(index:Int) {
            dataList[index]?.run {
                tvItemInfo.text = history_desc
                tvItemDate.text = history_addtime
                tvItemType.text = history_week
                if (!TextUtil.isEmpty(history_amount)){
                    tvItemBean.text = history_amount
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReceiveGiftHolder {
        return ReceiveGiftHolder(LayoutInflater.from(context).inflate(R.layout.app_item_high_record,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ReceiveGiftHolder?, position: Int) {
        holder?.display(position)
    }
}