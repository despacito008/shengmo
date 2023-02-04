package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.AllCommentData
import com.aiwujie.shengmo.utils.ImageLoader

class UnReadCommentAdapter(val context: Context, val fragment: Fragment, private val commentList: List<AllCommentData.DataBean>, private val newMessageNum: Int) : RecyclerView.Adapter<UnReadCommentAdapter.UnReadCommentHolder>() {
    inner class UnReadCommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivItemIcon: ImageView = itemView.findViewById(R.id.iv_item_unread_comment_icon)
        private var ivItemDynamic: ImageView = itemView.findViewById(R.id.iv_item_unread_comment_dynamic)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_unread_comment_name)
        private var tvItemNew: TextView = itemView.findViewById(R.id.tv_item_unread_comment_new)
        private var tvItemDate: TextView = itemView.findViewById(R.id.tv_item_unread_comment_time)
        private var tvItemContent: TextView = itemView.findViewById(R.id.tv_item_unread_comment_content)
        private var tvItemDynamic: TextView = itemView.findViewById(R.id.tv_item_unread_comment_dynamic)
        private var tvItemApply: TextView = itemView.findViewById(R.id.tv_item_unread_comment_apply)
        fun display(index: Int) {
            commentList[index].run {
                //GlideImgManager.glideLoader(context, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemIcon, 0)
                ImageLoader.loadCircleImage(fragment, head_pic, ivItemIcon, R.mipmap.morentouxiang)
                tvItemName.text = nickname
                tvItemDate.text = addtime
                tvItemContent.text = if (otheruid == "0") ccontent else "回复 $othernickname : $ccontent"
                if (pic.isNullOrEmpty() && coverUrl.isNullOrEmpty()) {
                    ivItemDynamic.visibility = View.GONE
                    tvItemDynamic.visibility = View.VISIBLE
                    tvItemDynamic.text = content
                } else {
                    ivItemDynamic.visibility = View.VISIBLE
                    tvItemDynamic.visibility = View.GONE
                    if (pic.isNullOrEmpty()) {
                        //GlideImgManager.glideLoader(context, coverUrl, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemDynamic, 1)
                        ImageLoader.loadRoundImage(fragment, coverUrl, ivItemDynamic, R.mipmap.morentouxiang)
                    } else {
                        //GlideImgManager.glideLoader(context, pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemDynamic, 1)
                        ImageLoader.loadRoundImage(fragment, pic, ivItemDynamic, R.mipmap.morentouxiang)
                    }
                }
            }
            tvItemNew.visibility = if (index < newMessageNum) View.VISIBLE else View.GONE
            tvItemApply.setOnClickListener {
                onCommentListener?.doCommentApply(index)
            }
            ivItemIcon.setOnClickListener {
                val intent = Intent(context, UserInfoActivity::class.java)
                intent.putExtra("uid", commentList[index].uid)
                context.startActivity(intent)
            }
            itemView.setOnClickListener {
                onCommentListener?.doCommentClick(index)
            }
            itemView.setOnLongClickListener {
                onCommentListener?.doCommentLongClick(index)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UnReadCommentHolder {
        return UnReadCommentHolder(LayoutInflater.from(context).inflate(R.layout.app_item_unread_comment, parent, false))
    }

    override fun onBindViewHolder(holder: UnReadCommentHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    interface OnCommentListener {
        fun doCommentClick(index: Int)
        fun doCommentApply(index: Int)
        fun doCommentLongClick(index: Int)
    }

    var onCommentListener: OnCommentListener? = null
}