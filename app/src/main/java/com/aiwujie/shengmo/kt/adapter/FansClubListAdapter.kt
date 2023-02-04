package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.JoinFanClubListBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: FansClubListAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/30 19:51
 * @Description:
 */
class FansClubListAdapter(val context:Context,private val clubList:ArrayList<JoinFanClubListBean.DataBean>):RecyclerView.Adapter<FansClubListAdapter.FansClubHolder>() {

    inner class FansClubHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val tvItemName:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_fans_club_name) }
        private val tvItemMark:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_fans_club_mark) }
        private val tvItemNum:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_fans_club_num) }
        private val tvItemRank:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_fans_club_rank) }
        private val ivItemIcon:ImageView by lazy { itemView.findViewById<ImageView>(R.id.civ_fans_club_icon) }
        fun display(index:Int) {
            clubList[index].run {
                tvItemName.text = fanclub_name
                tvItemMark.text = fanclub_card
                ImageLoader.loadImage(context,head_pic,ivItemIcon)
            }
            itemView.setOnClickListener {
                onSimpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FansClubHolder {
       return FansClubHolder(LayoutInflater.from(context).inflate(R.layout.app_item_fans_club_list,parent,false))
    }

    override fun onBindViewHolder(holder: FansClubHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return clubList.size
    }

    var onSimpleItemListener:OnSimpleItemListener? = null

}
