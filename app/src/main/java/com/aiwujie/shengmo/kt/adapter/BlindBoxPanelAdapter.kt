package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.NormalGiftBean
import com.aiwujie.shengmo.utils.ImageLoader


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: BindBoxPanelAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/5 9:35
 * @Description:
 */
class BlindBoxPanelAdapter(val context:Context, val blindList:List<NormalGiftBean.DataBean.BlindBoxBean>):RecyclerView.Adapter<BlindBoxPanelAdapter.BlindBoxHolder>() {
    inner class BlindBoxHolder(item: View):RecyclerView.ViewHolder(item) {
        private val ivBlindBox:ImageView by lazy { item.findViewById<ImageView>(R.id.iv_item_blind_box) }
        private val tvItemName:TextView by lazy { item.findViewById<TextView>(R.id.tv_item_blind_box_name) }
        private val tvItemPrice:TextView by lazy { item.findViewById<TextView>(R.id.tv_item_blind_box_price) }
        fun display(index:Int) {
            blindList[index].run {
                ImageLoader.loadImage(context,blindbox_image,ivBlindBox)
                tvItemName.text = blindbox_name
                tvItemPrice.text = "${beans}魔豆"
            }
            itemView.setOnClickListener {
                blindBoxListener?.doBlindBoxClick(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BlindBoxHolder {
        return BlindBoxHolder(LayoutInflater.from(context).inflate(R.layout.live_item_gift_panel_blind_box,parent,false))
    }

    override fun onBindViewHolder(holder: BlindBoxHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return blindList.size
    }

    interface OnBlindBoxListener {
        fun doBlindBoxClick(index:Int)
    }

    var blindBoxListener:OnBlindBoxListener? = null
}
