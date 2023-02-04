package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CarListModel
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * @program: newshengmo
 * @description: 全部座驾-适配器
 * @author: whl
 * @create: 2022-05-24 16:44
 **/
class CarInfoAdapter(val context: Context,val   list:List<CarListModel.DataBean>) :RecyclerView.Adapter<CarInfoAdapter.CarInfoViewHolder>() {

   inner class CarInfoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        private val ivImg :ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_img) }
        private val tvName :TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name) }
        private val tvPrice :TextView by lazy { itemView.findViewById<TextView>(R.id.tv_price) }

        fun display(position: Int){
            list[position].run {
                Glide.with(context).load(cover).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).  apply(RequestOptions().error(R.mipmap.default_error)).into(ivImg)

                tvName.text = name
                tvPrice.text = jg


                itemView.setOnClickListener {
                    onSimpleItemListener?.onItemListener(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CarInfoViewHolder {
        return  CarInfoViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_car_info,parent,false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: CarInfoViewHolder?, position: Int) {
        holder?.display(position)
    }

    public var onSimpleItemListener:OnSimpleItemListener?=  null
}