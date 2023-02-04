package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LotteryDrawResultBean
import com.bumptech.glide.Glide


class LotteryResultAdapter(val context:Context,private val resultList:ArrayList<LotteryDrawResultBean.DataBean.ListBean>):RecyclerView.Adapter<LotteryResultAdapter.LotteryResultHolder>() {
    inner class LotteryResultHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val ivItemIcon:ImageView = itemView.findViewById(R.id.iv_item_lottery_draw_icon)
        private val tvItemName:TextView = itemView.findViewById(R.id.tv_item_lottery_draw_name)
        private val tvItemBean:TextView = itemView.findViewById(R.id.tv_item_lottery_draw_bean)
        private val tvItemCount:TextView = itemView.findViewById(R.id.tv_item_lottery_draw_count)
        fun display(index:Int) {
            resultList[index].run {
                Glide.with(context).load(gift_image).into(ivItemIcon)
                tvItemName.text = gift_name
                tvItemBean.text = gift_beans + "魔豆"
                tvItemCount.text = if (num == 1) "" else num.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LotteryResultHolder {
        return LotteryResultHolder(LayoutInflater.from(context).inflate(R.layout.app_item_lottery_draw_result,parent,false))
    }

    override fun onBindViewHolder(holder: LotteryResultHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
         return resultList.size
    }
}