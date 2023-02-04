package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.StampStatisticalBean

class PushTopBuyAdapter(var context: Context, var dataList: List<StampStatisticalBean.DataBean>) : RecyclerView.Adapter<PushTopBuyAdapter.StampStatisticHolder>() {
    inner class StampStatisticHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemInfo = itemView.findViewById<TextView>(R.id.tv_item_stamp_statistical_info)
        var tvItemTime = itemView.findViewById<TextView>(R.id.tv_item_stamp_statistical_time)
        var tvItemResult = itemView.findViewById<TextView>(R.id.tv_item_stamp_statistical_result)
        fun display(index: Int) {
            dataList[index]?.let {
                tvItemTime.text = "${it.addtime_format}"
                if (it.beans == "0") {
                    tvItemInfo.text = "使用${it.amount}元购买"
                } else {
                    tvItemInfo.text = "使用${it.beans}魔豆兑换"
                }
                tvItemResult.text = "+${it.num}张推顶卡"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StampStatisticHolder {
        return StampStatisticHolder(LayoutInflater.from(context).inflate(R.layout.app_item_stamp_statistical, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: StampStatisticHolder?, position: Int) {
        holder?.display(position)
    }
}