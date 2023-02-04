package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.CircleImageView
import com.tencent.imsdk.v2.V2TIMConversation


class GroupInviteChatAdapter(val context: Context,var dataList:ArrayList<V2TIMConversation>):RecyclerView.Adapter<GroupInviteChatAdapter.GroupInviteChatHolder>() {
    var chooseIndexList:ArrayList<Int> = ArrayList()

    inner class GroupInviteChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivIcon: CircleImageView = itemView.findViewById(R.id.iv_item_group_invite_icon)
        private var ivState: ImageView = itemView.findViewById(R.id.iv_item_group_invite_state)
        private var ivLevel: ImageView = itemView.findViewById(R.id.iv_item_group_invite_level)
        private var tvName: TextView = itemView.findViewById(R.id.tv_item_group_invite_name)
        fun display(index:Int) {
            dataList[index].run {
                ImageLoader.loadImage(context,this.faceUrl,ivIcon)
                tvName.text = if (this.type == V2TIMConversation.V2TIM_C2C) this.showName else "[群] ${this.showName}"
                ivState.setImageResource(if (chooseIndexList.contains(index)) R.mipmap.atxuanzhong else R.mipmap.atweixuanzhong)
                ivLevel.visibility = View.INVISIBLE
                itemView.setOnClickListener {
                    simpleItemListener?.onItemListener(index)
                    if (chooseIndexList.contains(index)) {
                        chooseIndexList.remove(index)
                    } else {
                        chooseIndexList.add(index)
                    }
                    notifyItemChanged(index)
                }
            }
        }
    }

    fun refreshData(indexList:ArrayList<Int>) {
        chooseIndexList.clear()
        chooseIndexList.addAll(indexList)
        notifyDataSetChanged()
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

    fun getChooseUserId():List<String> {
        val chooseIdList = ArrayList<String>()
        for (a in chooseIndexList) {
            if (dataList[a].type == V2TIMConversation.V2TIM_C2C) {
                chooseIdList.add(dataList[a].userID)
            }
        }
        return chooseIdList
    }

    fun getChooseGroupId():List<String> {
        val chooseIdList = ArrayList<String>()
        for (a in chooseIndexList) {
            if (dataList[a].type == V2TIMConversation.V2TIM_GROUP) {
                chooseIdList.add(dataList[a].groupID)
            }
        }
        return chooseIdList
    }

}