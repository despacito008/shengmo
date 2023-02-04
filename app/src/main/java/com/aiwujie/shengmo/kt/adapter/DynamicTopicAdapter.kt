package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ListTopicData
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener


class DynamicTopicAdapter(var context: Context,var topList:ArrayList<ListTopicData.DataBean>): RecyclerView.Adapter<DynamicTopicAdapter.TopicHolder>() {
    inner class TopicHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var ivItemIcon: ImageView = itemView.findViewById(R.id.iv_item_dynamic_topic)
        var tvItemName:TextView = itemView.findViewById(R.id.tv_item_dynamic_topic_name)
        var tvItemInfo:TextView = itemView.findViewById(R.id.tv_item_dynamic_topic_info)
        var tvItemJoin:TextView = itemView.findViewById(R.id.tv_item_dynamic_topic_join)
        var tvItemNum:TextView = itemView.findViewById(R.id.tv_item_dynamic_topic_num)
        fun display(index:Int) {
            topList[index].run {
                ImageLoader.loadRoundImage(context,pic,ivItemIcon,R.drawable.default_error)
                tvItemName.text = "#$title#"
                tvItemInfo.text = introduce
                tvItemJoin.text = partaketimes + "参与"
                tvItemNum.text = dynamicnum + "动态"
            }
            itemView.setOnClickListener {
                onItemClickListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicHolder {
        return TopicHolder(LayoutInflater.from(context).inflate(R.layout.app_item_dynamic_topic,parent,false))
    }

    override fun getItemCount(): Int {
        return topList.size
    }

    override fun onBindViewHolder(holder: TopicHolder?, position: Int) {
        holder?.display(position)
    }

    var onItemClickListener:OnSimpleItemListener? = null
}