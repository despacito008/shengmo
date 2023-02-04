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

class StampStatisticalAdapter(var context:Context,var type:Int,var dataList:List<StampStatisticalBean.DataBean>): RecyclerView.Adapter<StampStatisticalAdapter.StampStatisticHolder>() {
    inner class StampStatisticHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var tvItemInfo = itemView.findViewById<TextView>(R.id.tv_item_stamp_statistical_info)
        var tvItemTime = itemView.findViewById<TextView>(R.id.tv_item_stamp_statistical_time)
        var tvItemResult = itemView.findViewById<TextView>(R.id.tv_item_stamp_statistical_result)
        fun display(index:Int) {
            dataList[index]?.let {
                tvItemTime.text = "${it.addtime_format} ${it.week}"
                when(type) {
                    1 -> {
                        if (it.beans == "0") {
                            tvItemInfo.text = "使用${it.amount}元购买"
                        } else {
                            tvItemInfo.text = "使用${it.beans}魔豆兑换"
                        }
                        tvItemResult.text = "+${it.num}张邮票"
                    }
                    2 -> {
                        tvItemInfo.text = "${if (TextUtils.isEmpty(it.type) || it.type == null) "[系统赠送]" else "[${it.type}]"}赠男/女/CDTS票各${it.num}张"
                        tvItemResult.text = "+${it.num.toInt()*3}张邮票"
                    }
                    3 -> {
                        tvItemInfo.text = "给${it.nickname}发消息"
                        when(it.type) {
                            "1" -> {
                                tvItemResult.text = "-1张男票"
                            }
                            "2" -> {
                                tvItemResult.text = "-1张女票"
                            }
                            "3" -> {
                                tvItemResult.text = "-1张CDTS票"
                            }
                            "4" -> {
                                tvItemResult.text = "-1张通用邮票"
                            }
                        }
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StampStatisticHolder {
        return StampStatisticHolder(LayoutInflater.from(context).inflate(R.layout.app_item_stamp_statistical,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: StampStatisticHolder?, position: Int) {
        holder?.display(position)
    }
}