package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.WithdrawLogBean
import kotlinx.android.synthetic.main.app_item_widthdraw_list.view.*

class WithdrawLogAdapter(private val context:Context,private var withdrawList:List<WithdrawLogBean.DataBean>) : RecyclerView.Adapter<WithdrawLogAdapter.WithdrawLogHolder>() {

    inner class WithdrawLogHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvItemPrice = itemView.findViewById<TextView>(R.id.tv_item_withdraw_price)
        private var tvItemBean = itemView.findViewById<TextView>(R.id.tv_item_withdraw_bean)
        private var tvItemBank = itemView.findViewById<TextView>(R.id.tv_item_withdraw_bank)
        private var tvItemTime = itemView.findViewById<TextView>(R.id.tv_item_withdraw_date)
        private var tvItemState = itemView.findViewById<TextView>(R.id.tv_item_withdraw_state)

        fun display(index:Int) {
            tvItemPrice.text = withdrawList[index].money
            tvItemBean.text = withdrawList[index].beans + "魔豆"
            if (withdrawList[index].bankcard == null) {
                tvItemBank.text = "未获取到信息"
            } else {
                if (withdrawList[index].bankcard.length > 4) {
                    tvItemBank.text = "${withdrawList[index].bankname}(尾号${withdrawList[index].bankcard.substring(withdrawList[index].bankcard.length - 4, withdrawList[index].bankcard.length)})"
                } else {
                    tvItemBank.text = withdrawList[index].bankname
                }
            }
            tvItemTime.text = withdrawList[index].add_time
            if (withdrawList[index].anchor_status == "1") {
                when(withdrawList[index].state) {
                    "0" -> {
                        tvItemState.text = "审核中"
                        tvItemState.setTextColor(context.resources.getColor(R.color.withdraw_ing))
                    }
                    "1" -> {
                        tvItemState.text = "提现成功"
                        tvItemState.setTextColor(context.resources.getColor(R.color.withdraw_suc))
                    }
                    "2" -> {
                        tvItemState.text = "提现失败"
                        tvItemState.setTextColor(context.resources.getColor(R.color.withdraw_fail))
                    }
                }
            } else {
                tvItemState.text = "官方转账"
                tvItemState.setTextColor(context.resources.getColor(R.color.withdraw_suc))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WithdrawLogHolder {
        return WithdrawLogHolder(LayoutInflater.from(context).inflate(R.layout.app_item_widthdraw_list,parent,false))
    }

    override fun getItemCount(): Int {
        return withdrawList.size
    }

    override fun onBindViewHolder(holder: WithdrawLogHolder?, position: Int) {
        holder?.display(position)
    }
}