package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CarRecordModel
import org.feezu.liuli.timeselector.Utils.TextUtil

/**
 * @program: newshengmo
 * @description: 座驾购买明细
 * @author: whl
 * @create: 2022-05-27 10:36
 **/
class CarRecordAdapter(var context: Context,var list:List<CarRecordModel.DataBean>) : RecyclerView.Adapter<CarRecordAdapter.CarRecordViewHolder>() {


    inner class CarRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemInfo: TextView = itemView.findViewById(R.id.item_recorder_rmb)
        var tvItemDate: TextView = itemView.findViewById(R.id.item_recorder_date)
        var tvItemType: TextView = itemView.findViewById(R.id.item_recorder_day)
        var tvItemBean: TextView = itemView.findViewById(R.id.item_recorder_modou)

        fun display(position: Int){
          list[position].run {
              tvItemInfo.text = name
              tvItemDate.text = pay_time
              tvItemType.text = week
              if (!TextUtil.isEmpty(amount)){
                  tvItemBean.text = amount
              }
          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CarRecordViewHolder {
        return CarRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_high_record, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CarRecordViewHolder?, position: Int) {
            holder?.display(position)
    }
}