package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.PlayBackCommentBean
import com.aiwujie.shengmo.kt.util.showUserLevelRole
import com.aiwujie.shengmo.utils.ImageLoader

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: PlayBackCommentAdapter
 * @Author: xmf
 * @CreateDate: 2022/6/13 19:43
 * @Description:
 */
class PlayBackCommentAdapter(val context:Context,private val commentList:ArrayList<PlayBackCommentBean.DataBean.CommentListBean>): RecyclerView.Adapter<PlayBackCommentAdapter.PlayBackCommentHolder>() {
    inner class PlayBackCommentHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val ivIcon:ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_user_avatar_icon) }
        private val ivLevel:ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_user_avatar_level) }
        private val viewOnLine:View by lazy { itemView.findViewById<View>(R.id.iv_user_avatar_online) }
        private val tvName:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_name) }
        private val tvDate:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_date) }
        private val tvContent:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_content) }

        fun display(index:Int) {
            commentList[index].run {
                ImageLoader.loadCircleImage(context,user.head_pic,ivIcon)
                ivLevel.showUserLevelRole(user.vip_type.toInt())
                tvContent.text = comment.comment_content
                tvDate.text = comment.comment_time
                tvName.text = user.nickname
            }
            ivIcon.setOnClickListener {
                commentListener?.doCommentHeaderClick(index)
            }
            itemView.setOnLongClickListener {
                commentListener?.doCommentLongClick(index)
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlayBackCommentHolder {
        return PlayBackCommentHolder(LayoutInflater.from(context).inflate(R.layout.app_item_play_back_comment,parent,false))
    }

    override fun onBindViewHolder(holder: PlayBackCommentHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    interface OnPlayBackCommentListener {
        fun doCommentHeaderClick(index: Int)
        fun doCommentLongClick(index: Int)
    }

    var commentListener:OnPlayBackCommentListener? = null
}
