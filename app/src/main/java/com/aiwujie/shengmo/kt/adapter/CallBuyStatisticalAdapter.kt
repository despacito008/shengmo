package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CallBuyHistoryBean
import com.aiwujie.shengmo.bean.CallUseHistoryBean
import com.aiwujie.shengmo.bean.CallUserBean
import com.aiwujie.shengmo.kt.ui.fragment.normallist.CallCardUseHistoryFragment

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: CallUseStatisticalAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/26 17:26
 * @Description: 呼唤购买明细适配器
 */
class CallBuyStatisticalAdapter(val context:Context, private val historyList:ArrayList<CallBuyHistoryBean.DataBean>): RecyclerView.Adapter<CallBuyStatisticalAdapter.CallUserStatisticalHolder>() {
    inner class CallUserStatisticalHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val tvItemInfo:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_statistical_info) }
        private val tvItemTime:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_statistical_time) }
        private val tvItemStatus:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_statistical_status) }
        fun display(index:Int) {
            historyList[index].run {
                tvItemInfo.text = history_desc
                tvItemTime.text = add_time
                tvItemStatus.text = bag_times_desc
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CallUserStatisticalHolder {
        return CallUserStatisticalHolder(LayoutInflater.from(context).inflate(R.layout.app_item_normal_statistical,parent,false))
    }

    override fun onBindViewHolder(holder: CallUserStatisticalHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

}
