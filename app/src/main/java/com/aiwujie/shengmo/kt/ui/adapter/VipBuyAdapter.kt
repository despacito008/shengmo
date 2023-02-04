package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ConsumptionGiveData
import com.aiwujie.shengmo.bean.Frag_VipGmDataBean
import com.aiwujie.shengmo.bean.PresentReceiveData

class VipBuyAdapter(var context:Context, var dataList:List<Frag_VipGmDataBean.DataBean>): RecyclerView.Adapter<VipBuyAdapter.ReceiveGiftHolder>() {

    inner class ReceiveGiftHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvItemInfo:TextView = itemView.findViewById(R.id.item_recorder_rmb)
        var tvItemDate:TextView = itemView.findViewById(R.id.item_recorder_date)
        var tvItemType:TextView = itemView.findViewById(R.id.item_recorder_day)
        var tvItemBean:TextView = itemView.findViewById(R.id.item_recorder_modou)

        fun display(index:Int) {
            dataList[index].run {
                if (TextUtils.isEmpty(nickname)) {
                    tvItemInfo.text = "我购买 $pay_type $days 天"
                } else {
                    tvItemInfo.text = "我赠送 $nickname $pay_type $days 天"
                }
                tvItemDate.text = addtime_format
                tvItemType.text = week
                tvItemBean.text = "$beans 金魔豆"
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