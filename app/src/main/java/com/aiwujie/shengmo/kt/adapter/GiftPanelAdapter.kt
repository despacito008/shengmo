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
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.bumptech.glide.Glide

class GiftPanelAdapter(private val context: Context,private val giftList: List<NormalGiftBean.DataBean.GoodsBean>) : RecyclerView.Adapter<GiftPanelAdapter.GiftPanelHolder>() {

    inner class GiftPanelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.item_gridview_present_name)
        var tvMoney:  TextView = itemView.findViewById(R.id.item_gridview_present_dou)
        var ivGift: ImageView = itemView.findViewById(R.id.item_gridview_present_iv)


        fun setData(position: Int) {
            tvName.text = giftList[position].gift_name
            tvMoney.text = giftList[position].gift_beans + "魔豆"
            Glide.with(context).load(giftList[position].gift_image).into(ivGift)
            itemView.setOnClickListener {
                onSimpleItemListener?.onItemListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GiftPanelHolder {
        return GiftPanelHolder(LayoutInflater.from(context).inflate(R.layout.item_gridview_present_new2, parent, false))
    }

    override fun getItemCount(): Int {
        return giftList.size
    }

    override fun onBindViewHolder(holder: GiftPanelHolder?, position: Int) {
       holder!!.setData(position)
    }

    private var onSimpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener: OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }
}