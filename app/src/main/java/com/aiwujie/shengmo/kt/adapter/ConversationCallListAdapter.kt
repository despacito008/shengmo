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
import com.aiwujie.shengmo.bean.CallUserBean
import com.aiwujie.shengmo.kt.util.livedata.UnFlowLiveData
import com.aiwujie.shengmo.kt.util.showUserLevelRole
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: conversationCallAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/26 10:49
 * @Description: 呼唤列表适配器
 */
class ConversationCallListAdapter(val context: Context,private val callUserList:ArrayList<CallUserBean.DataBean>):RecyclerView.Adapter<ConversationCallListAdapter.ConversationCallHolder>() {
    inner class ConversationCallHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val ivItemIcon by lazy { itemView.findViewById<ImageView>(R.id.iv_item_user_icon) }
        private val ivItemLevel by lazy { itemView.findViewById<ImageView>(R.id.iv_item_user_level) }
        private val ivItemOnline by lazy { itemView.findViewById<View>(R.id.view_item_user_online) }
        private val tvItemName by lazy { itemView.findViewById<TextView>(R.id.tv_item_user_name) }
        private val tvItemDistance by lazy { itemView.findViewById<TextView>(R.id.tv_item_user_distance) }

        fun display(index:Int) {
            callUserList[index].run {
                ImageLoader.loadImage(context,head_pic,ivItemIcon)
                ivItemLevel.showUserLevelRole(vip_type.toInt())
                ivItemOnline.visibility = if (onlinestate == 1) View.VISIBLE else View.GONE
                tvItemName.text = nickname
                tvItemDistance.text = distance
                when (sex) {
                    "1" -> tvItemName.setTextColor(ContextCompat.getColor(context,R.color.liveBoyColor))
                    "2" -> tvItemName.setTextColor(ContextCompat.getColor(context,R.color.liveGirlColor))
                    else -> tvItemName.setTextColor(ContextCompat.getColor(context,R.color.liveCdtColor))
                }
            }

            itemView.setOnClickListener {
                itemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ConversationCallHolder {
        return ConversationCallHolder(LayoutInflater.from(context).inflate(R.layout.app_item_conversation_call_user,parent,false))
    }

    override fun onBindViewHolder(holder: ConversationCallHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return callUserList.size
    }

    var itemListener:OnSimpleItemListener? = null
}
