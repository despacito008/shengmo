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
import com.aiwujie.shengmo.bean.ClubMemberListBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: FansClubMemberAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/31 17:53
 * @Description:
 */
class FansClubMemberAdapter(val context: Context,private val memberList:ArrayList<ClubMemberListBean.DataBean>):RecyclerView.Adapter<FansClubMemberAdapter.FansClubMemberHolder>() {
    inner class FansClubMemberHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val ivItemIcon:ImageView by lazy { itemView.findViewById<ImageView>(R.id.civ_item_icon) }
        private val tvItemName:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_name) }
        private val tvItemMark:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_fans_name) }
        private val tvItemLevel:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_fans_level) }
        private val tvItemRank:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_rank) }
        fun display(index:Int) {
            memberList[index].run {
                ImageLoader.loadImage(context,head_pic,ivItemIcon)
                tvItemName.text = nickname
                tvItemMark.text = fanclub_card
                tvItemLevel.text = fanclub_level.toString()
                tvItemRank.text = "${index + 1}"
                when (sex) {
                    "1" -> {tvItemName.setTextColor(ContextCompat.getColor(context,R.color.boyColor))}
                    "2" -> {tvItemName.setTextColor(ContextCompat.getColor(context,R.color.girlColor))}
                    else -> {tvItemName.setTextColor(ContextCompat.getColor(context,R.color.cdtColor))}
                }
            }
            itemView.setOnClickListener {
                onSimpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FansClubMemberHolder {
        return FansClubMemberHolder(LayoutInflater.from(context).inflate(R.layout.app_item_fans_club_member,parent,false))
    }

    override fun onBindViewHolder(holder: FansClubMemberHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    var onSimpleItemListener:OnSimpleItemListener? = null
}
