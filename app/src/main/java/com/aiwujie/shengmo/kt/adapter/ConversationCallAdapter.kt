package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.graphics.Color
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
import com.tencent.liteav.login.ProfileManager

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: conversationCallAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/24 14:49
 * @Description:
 */
class ConversationCallAdapter(val context: Context,private val callUserList:ArrayList<CallUserBean.DataBean>) : RecyclerView.Adapter<ConversationCallAdapter.ConversationCallHolder>() {
    var mCallStatus = 0
    var mCallTime = "计算中"
    inner class ConversationCallHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemName by lazy { itemView.findViewById<TextView>(R.id.tv_item_user_name) }
        private val ivItemIcon by lazy { itemView.findViewById<ImageView>(R.id.civ_user_icon) }
        private val ivItemLevel by lazy { itemView.findViewById<ImageView>(R.id.iv_item_user_level) }
        fun display(index: Int) {
            when (index) {
                0 -> {
                    ivItemLevel.visibility = View.INVISIBLE
                    tvItemName.text = when (mCallStatus) {
                        0 -> "加载中"
                        1 -> "开启呼唤"
                        2 ->  mCallTime
                        else -> "审核中"
                    }
                    tvItemName.setTextColor(ContextCompat.getColor(context,R.color.normalPurple))
                    ivItemIcon.setImageResource(R.drawable.ic_conversation_call)
                }
                else -> {
                    ivItemLevel.showUserLevelRole(callUserList[index].vip_type.toInt())
                    ImageLoader.loadImage(context,callUserList[index].head_pic,ivItemIcon)
                    tvItemName.text = callUserList[index].nickname
                    tvItemName.setTextColor(ContextCompat.getColor(context,R.color.titleBlack))
                }
            }
            itemView.setOnClickListener {
                itemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ConversationCallHolder {
        return ConversationCallHolder(LayoutInflater.from(context).inflate(R.layout.app_item_conversation_call, parent, false))
    }

    override fun onBindViewHolder(holder: ConversationCallHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return callUserList.size
    }

    var itemListener: OnSimpleItemListener? = null

    fun refreshCallStatus(callStatus:Int,remainTime:String) {
        mCallStatus = callStatus + 1
        mCallTime = remainTime
        notifyItemChanged(0)
    }

    fun justRefreshTime(remainTime: String) {
        mCallTime = remainTime
        notifyItemChanged(0)
    }
}
