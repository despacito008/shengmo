package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.HomeLiveLabelBean
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.bumptech.glide.Glide

class HomeLiveLabelAdapter(var context:Context,var labelList:List<HomeLiveLabelBean.DataBean>):RecyclerView.Adapter<HomeLiveLabelAdapter.HomeLiveLabelHolder>() {

    inner class HomeLiveLabelHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvItemLabel:TextView = itemView.findViewById(R.id.tv_item_live_label)
        var ivItemActive:ImageView = itemView.findViewById(R.id.iv_item_part_active)
        fun disPlay(index:Int) {
            labelList[index].let {
                tvItemLabel.text = it.name
                Glide.with(context).load(R.drawable.ic_part_active).into(ivItemActive)
                when(it.is_active) {
                    "0" -> ivItemActive.visibility = View.GONE
                    "1" -> ivItemActive.visibility = View.VISIBLE
                }
            }
            itemView.setOnClickListener {
                itemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeLiveLabelHolder {
        return HomeLiveLabelHolder(LayoutInflater.from(context).inflate(R.layout.app_item_home_live_label,parent,false))
    }

    override fun getItemCount(): Int {
        return labelList.size
    }

    override fun onBindViewHolder(holder: HomeLiveLabelHolder?, position: Int) {
        holder?.disPlay(position)
    }

    var itemListener:OnSimpleItemListener? = null


}