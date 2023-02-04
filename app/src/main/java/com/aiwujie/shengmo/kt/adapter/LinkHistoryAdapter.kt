package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LinkHistoryBean
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import kotlinx.android.synthetic.main.app_item_link_history.view.*
import java.util.jar.Attributes

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: LinkHistoryAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/11 15:18
 * @Description:
 */
class LinkHistoryAdapter(val context:Context,private val historyList:List<LinkHistoryBean.DataBean.ListBean>): RecyclerView.Adapter<LinkHistoryAdapter.LinkHistoryHolder>() {
    inner class LinkHistoryHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val tvItemName:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_link_name) }
        private val tvItemTime:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_link_time) }
        private val tvItemLength:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_link_length) }
        private val tvItemBeans:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_link_beans) }
        fun display(index:Int) {
            historyList[index].run {
                tvItemName.text = nickname
                tvItemTime.text = add_time
                tvItemLength.text = "${ if (this.type == "1") "语音 " else "视频 "}${live_beans}豆 - $time_length_str"
                tvItemBeans.text = "$beans_current_count 豆"
            }
            itemView.setOnClickListener {
                simpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LinkHistoryHolder {
        return LinkHistoryHolder(LayoutInflater.from(context).inflate(R.layout.app_item_link_history,parent,false))
    }

    override fun onBindViewHolder(holder: LinkHistoryHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    var simpleItemListener:OnSimpleItemListener? = null
}
