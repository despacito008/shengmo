package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.Atbean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.CircleImageView
import com.tencent.imsdk.v2.V2TIMConversation


class AtConversationAdapter(val context: Context, var dataList:ArrayList<V2TIMConversation>,  var selectList:List<Atbean.DataBean>):RecyclerView.Adapter<AtConversationAdapter.GroupInviteChatHolder>() {
    inner class GroupInviteChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivIcon: CircleImageView = itemView.findViewById(R.id.iv_item_group_invite_icon)
        private var ivState: ImageView = itemView.findViewById(R.id.iv_item_group_invite_state)
        private var ivLevel: ImageView = itemView.findViewById(R.id.iv_item_group_invite_level)
        private var tvName: TextView = itemView.findViewById(R.id.tv_item_group_invite_name)
        fun display(index:Int) {
            dataList[index].run {
                ImageLoader.loadImage(context,this.faceUrl,ivIcon)
                tvName.text = if (type == V2TIMConversation.V2TIM_C2C) this.showName else "[群]" + this.showName
                ivState.setImageResource(if (selectList.map { it.uid }.contains(if (type == V2TIMConversation.V2TIM_C2C) userID else groupID)) R.mipmap.atxuanzhong else R.mipmap.atweixuanzhong)
                ivLevel.visibility = View.INVISIBLE
                itemView.setOnClickListener {
                    simpleItemListener?.onItemListener(index)
                    notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GroupInviteChatHolder {
        return GroupInviteChatHolder(LayoutInflater.from(context).inflate(R.layout.app_item_group_invite,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: GroupInviteChatHolder?, position: Int) {
        holder?.display(position)
    }

    var simpleItemListener:OnSimpleItemListener? = null

}