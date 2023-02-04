package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.PlayBackInfoBean
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import kotlinx.android.synthetic.main.layout_item_play_back_eposide.view.*

class VideoEpisodeAdapter(var context:Context,var videoList:List<PlayBackInfoBean.DataBean.VideoListBean>) : RecyclerView.Adapter<VideoEpisodeAdapter.EpisodeHolder>() {

    var currentIndex = 0

    inner class EpisodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemTitle:TextView = itemView.findViewById(R.id.tv_item_play_back_episode)
        var ivItemTitle:ImageView = itemView.findViewById(R.id.iv_item_play_back_episode)
        var llItemTitle:LinearLayout = itemView.findViewById(R.id.ll_item_play_back_episode)
        fun display(index:Int) {
            tvItemTitle.text = videoList[index].episode_title
            tvItemTitle.setOnClickListener {
                changeIndex(index)
            }
            if (currentIndex == index) {
                tvItemTitle.setTextColor(context.resources.getColor(R.color.purpleColor))
                llItemTitle.setBackgroundResource(R.drawable.bg_round_episode_playing)
                ivItemTitle.setImageResource(R.drawable.ic_episode_play_choose)
            } else {
                tvItemTitle.setTextColor(context.resources.getColor(R.color.normalGray))
                llItemTitle.setBackgroundResource(R.color.transparent)
                ivItemTitle.setImageResource(R.drawable.ic_episode_play_normal)

            }
        }
    }

    fun changeIndex(index:Int) {
        var temp = currentIndex
        currentIndex = index
        if (temp != currentIndex) {
            onItemClickListener?.onItemListener(index)
            notifyItemChanged(temp)
            notifyItemChanged(currentIndex)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EpisodeHolder {
        return EpisodeHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_play_back_eposide,parent,false))
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: EpisodeHolder?, position: Int) {
        holder?.display(position)
    }

    var onItemClickListener:OnSimpleItemListener? = null
}