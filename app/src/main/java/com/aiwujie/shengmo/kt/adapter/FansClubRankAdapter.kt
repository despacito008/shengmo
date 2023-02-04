package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.FansClubRankBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import kotlinx.android.synthetic.main.app_item_fans_club_rank.view.*

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: FansClubRankAdapter
 * @Author: xmf
 * @CreateDate: 2022/6/1 14:25
 * @Description:
 */
class FansClubRankAdapter(private val context: Context,private val clubList:ArrayList<FansClubRankBean.DataBean>):RecyclerView.Adapter<FansClubRankAdapter.FansClubRankHolder>() {

    inner class FansClubRankHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val tvItemName:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_name) }
        private val tvItemMark:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_mark) }
        private val tvItemNum:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_num) }
        private val tvItemRank:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_rank) }
        private val ivItemIcon:ImageView by lazy { itemView.findViewById<ImageView>(R.id.civ_item_icon) }
        fun display(index:Int) {
            clubList[index].run {
                tvItemName.text = fanclub_name
                tvItemMark.text = fanclub_card
                tvItemNum.text = "粉丝团人数：${member}"
                tvItemRank.text = "${index + 1}"
                ImageLoader.loadImage(context,head_pic,ivItemIcon)
            }
            itemView.setOnClickListener {
                onSimpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FansClubRankHolder {
        return FansClubRankHolder(LayoutInflater.from(context).inflate(R.layout.app_item_fans_club_rank,parent,false))
    }

    override fun onBindViewHolder(holder: FansClubRankHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return clubList.size
    }

    var onSimpleItemListener:OnSimpleItemListener? = null
}
