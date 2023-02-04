package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.utils.OnSimpleItemListener

/**
 * @author: xmf
 * @date: 2022/6/14 16:01
 * @desc:
 */
class ReportConfigAdapter(val context: Context,val contentList:List<String>):RecyclerView.Adapter<ReportConfigAdapter.ReportConfigHolder>() {
    inner class ReportConfigHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val tvItemContent:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_content) }
        fun display(index:Int) {
            tvItemContent.text = contentList[index]
            itemView.setOnClickListener {
                simpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReportConfigHolder {
        return ReportConfigHolder(LayoutInflater.from(context).inflate(R.layout.app_item_report_config,parent,false))
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: ReportConfigHolder?, position: Int) {
        holder?.display(position)
    }

    var simpleItemListener:OnSimpleItemListener? = null
}