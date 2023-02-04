package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.FollowMsgData
import com.aiwujie.shengmo.bean.StampStatisticalBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener

class FollowMessageAdapter(var context: Context, var dataList: List<FollowMsgData.DataBean>) : RecyclerView.Adapter<FollowMessageAdapter.FollowMessageHolder>() {
    inner class FollowMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemName: TextView = itemView.findViewById(R.id.tv_item_follow_msg_name)
        private val tvItemDate: TextView = itemView.findViewById(R.id.tv_item_follow_msg_date)
        private val ivItemIcon: ImageView = itemView.findViewById(R.id.iv_item_follow_msg_icon)
        fun display(index: Int) {
            dataList[index].run {
                tvItemName.text = nickname
                tvItemDate.text = addtime
                ImageLoader.loadImage(context, head_pic, ivItemIcon)
                itemView.setOnClickListener {
                    onSimpleItemListener?.onItemListener(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FollowMessageHolder {
        return FollowMessageHolder(LayoutInflater.from(context).inflate(R.layout.app_item_follow_msg, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FollowMessageHolder?, position: Int) {
        holder?.display(position)
    }

    var onSimpleItemListener: OnSimpleItemListener? = null
}