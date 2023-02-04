package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.LiveHistoryBean
import com.aiwujie.shengmo.bean.UserLivePlaybackListBean
import com.bumptech.glide.Glide

class LivePlaybackAdapter(var context: Context, var historyList:List<UserLivePlaybackListBean.DataBean>): RecyclerView.Adapter<LivePlaybackAdapter.LiveHistoryHolder>() {

    inner class LiveHistoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var ivItemPoster:ImageView = itemView.findViewById(R.id.iv_item_live_history_poster)
        var tvItemTitle:TextView = itemView.findViewById(R.id.tv_item_live_history_title)
        var tvItemPerson:TextView = itemView.findViewById(R.id.tv_item_live_history_person)
        var tvItemBean:TextView = itemView.findViewById(R.id.tv_item_live_history_bean)
        var ivItemStartTime:TextView = itemView.findViewById(R.id.tv_item_live_history_start_time)
        var ivItemPlay:ImageView = itemView.findViewById(R.id.iv_item_live_history_play)
        var tvItemPlayBack:TextView = itemView.findViewById(R.id.tv_item_live_history_play_back)
        var ivItemPlayBack:ImageView = itemView.findViewById(R.id.iv_item_live_history_play_back)
        var ivItemLock:ImageView = itemView.findViewById(R.id.iv_item_live_history_lock)
        fun display(index:Int) {
            historyList[index]?.let {
                if (TextUtils.isEmpty(it.live_poster)) {
                    Glide.with(context).load(R.drawable.ic_avatar).into(ivItemPoster)
                } else {
                    Glide.with(context).load(it.live_poster).into(ivItemPoster)
                }
                tvItemTitle.text = it.live_title
                tvItemPerson.text = "累计观众 ${it.watchsum}"
                tvItemBean.text = "本场收益 ${it.beans_current_count}"
                ivItemStartTime.text = "开播时间 ${it.start_time}/时长 ${it.time_lenght}"

                tvItemPlayBack.text = it.free_content
                if (it.is_admin_hidden == "0" && it.is_del == "0") {
                    itemView.setBackgroundColor(context.resources.getColor(R.color.transparent))
                    ivItemLock.visibility = View.GONE
                } else {
                    itemView.setBackgroundColor(Color.parseColor("#f5f5f5"))
                    ivItemLock.visibility = View.VISIBLE
                    if (it.is_admin_hidden == "1") {
                        ivItemLock.setImageResource(R.drawable.ic_play_back_lock)
                    } else {
                        ivItemLock.setImageResource(R.drawable.ic_play_back_hide)
                    }
                }


//                if (it.is_admin_hidden == "0") {
//                    ivItemLock.visibility = View.GONE
//                } else {
//                    ivItemLock.visibility = View.VISIBLE
//                }
//                if (it.is_del == "0") {
//                    tvItemPlayBack.text = it.free_content
//                    itemView.setBackgroundColor(context.resources.getColor(R.color.transparent))
//                } else {
//                    tvItemPlayBack.text = it.free_content + "( 仅自己可见 )"
//                    itemView.setBackgroundColor(Color.parseColor("#11000000"))
//                }
                if (it.is_record == "1") {
                    ivItemPlay.visibility = View.VISIBLE
                    ivItemPlayBack.visibility = View.VISIBLE
                    tvItemPlayBack.visibility = View.VISIBLE
                } else {
                    ivItemPlay.visibility = View.GONE
                    ivItemPlayBack.visibility = View.GONE
                    tvItemPlayBack.visibility = View.GONE
                }
            }

            itemView.setOnClickListener {
                if (historyList[index]?.is_record == "1" || MyApp.uid == historyList[index].uid || "1" == MyApp.isAdmin) {
                //if (historyList[index]?.is_record == "1") {
                    onItemHistoryListener?.doItemClick(historyList[index]?.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveHistoryHolder {
        return LiveHistoryHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_history,parent,false))
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: LiveHistoryHolder?, position: Int) {
        holder?.display(position)
    }

    interface OnItemHistoryListener {
        fun doItemClick(id:String)
    }

    var onItemHistoryListener:OnItemHistoryListener? = null
}