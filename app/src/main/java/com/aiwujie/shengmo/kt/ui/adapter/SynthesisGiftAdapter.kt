package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.SynthesisGiftBean
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener

class SynthesisGiftAdapter(val context:Context,val giftList:ArrayList<SynthesisGiftBean.DataBean.ListBean>): RecyclerView.Adapter<SynthesisGiftAdapter.SynthesisHolder>() {

    inner class SynthesisHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ivItemIcon:ImageView = itemView.findViewById(R.id.iv_item_gift_synthesis_icon)
        private val tvItemName:TextView = itemView.findViewById(R.id.tv_item_gift_synthesis_name)
        private val tvItemPrice:TextView = itemView.findViewById(R.id.tv_item_gift_synthesis_bean)
        private val tvItemNum:TextView = itemView.findViewById(R.id.tv_item_gift_synthesis_num)
        fun display(index:Int) {
            giftList[index].run {
                ImageLoader.loadImageWithoutHolder(context,gift_image,ivItemIcon)
                tvItemName.text = gift_name
                tvItemPrice.text = "$gift_beans 魔豆"
                tvItemNum.text = "x $num"
            }
            itemView.setOnClickListener {
                itemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SynthesisHolder {
        return SynthesisHolder(LayoutInflater.from(context).inflate(R.layout.app_item_synthesis_gift,parent,false))
    }

    override fun onBindViewHolder(holder: SynthesisHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return giftList.size
    }

    var itemListener:OnSimpleItemListener? = null
}