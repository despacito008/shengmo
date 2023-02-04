package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ChatAnchorBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener

class FeeLinkUserAdapter(val context: Context, val anchorList: List<ChatAnchorBean.DataBean>) : RecyclerView.Adapter<FeeLinkUserAdapter.FeeLinkUserHolder>() {
    inner class FeeLinkUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivItemIcon: ImageView = itemView.findViewById(R.id.iv_item_link_user_icon)
        private var ivItemState: ImageView = itemView.findViewById(R.id.iv_item_link_user_state)
        private var tvItemState: TextView = itemView.findViewById(R.id.tv_item_link_user_state)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_link_user_name)
        private var tvItemInfo: TextView = itemView.findViewById(R.id.tv_item_link_user_info)
        private var tvItemAudioPrice: TextView = itemView.findViewById(R.id.tv_item_link_user_audio)
        private var tvItemVideoPrice: TextView = itemView.findViewById(R.id.tv_item_link_user_video)
        fun display(index: Int) {
            anchorList[index].run {
                tvItemName.text = nickname
                ImageLoader.loadImage(context, head_pic, ivItemIcon)
                tvItemAudioPrice.text = "$chat_voice_beans 魔豆/分钟"
                tvItemVideoPrice.text = "$chat_video_beans 魔豆/分钟"
                tvItemInfo.text = "总时长:${if (all_time_length.isNotEmpty()) all_time_length else "--"}/总收入:${all_live_beans}魔豆"
                when (live_chat_status) {
                    "1" -> {
                        ivItemState.setImageResource(R.drawable.dot_green)
                        tvItemState.text = "可连线"
                    }
                    "2" -> {
                        ivItemState.setImageResource(R.drawable.dot_orange)
                        tvItemState.text = "直播中"
                    }
                    "3" -> {
                        ivItemState.setImageResource(R.drawable.dot_orange)
                        tvItemState.text = "连线中"
                    }
                }
                when (sex) {
                    "1" -> {
                        tvItemName.setTextColor(ContextCompat.getColor(context,R.color.liveBoyColor))
                    }
                    "2" -> {
                        tvItemName.setTextColor(ContextCompat.getColor(context,R.color.liveGirlColor))
                    }
                    else -> {
                        tvItemName.setTextColor(ContextCompat.getColor(context,R.color.liveCdtColor))
                    }
                }
                itemView.setOnClickListener {
                    itemListener?.onItemListener(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeeLinkUserHolder {
        return FeeLinkUserHolder(LayoutInflater.from(context).inflate(R.layout.app_item_link_user, parent, false))
    }

    override fun onBindViewHolder(holder: FeeLinkUserHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return anchorList.size
    }

    var itemListener:OnSimpleItemListener? = null
}