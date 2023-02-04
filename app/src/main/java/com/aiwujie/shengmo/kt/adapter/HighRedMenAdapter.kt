package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.RedwomenMarkerData
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener

class HighRedMenAdapter(var context: Context, var list: ArrayList<RedwomenMarkerData.DataBean>) : RecyclerView.Adapter<HighRedMenAdapter.HighRedMenViewHolder>() {


    inner class HighRedMenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivImg: ImageView = itemView.findViewById(R.id.iv_img)
        var tvName: TextView = itemView.findViewById(R.id.tv_name)

        fun display(position: Int) {
            list[position].run {
                tvName.text = nickname

                GlideImgManager.glideLoader(context, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivImg, 0)


                itemView.setOnClickListener {
                    if (onSimpleItemListener != null) {
                        onSimpleItemListener?.onItemListener(position)
                    }
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HighRedMenViewHolder {
        return HighRedMenViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_high_red_men, parent, false))
    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: HighRedMenViewHolder?, position: Int) {
        holder?.display(position)
    }

    var onSimpleItemListener: OnSimpleItemListener? = null
}