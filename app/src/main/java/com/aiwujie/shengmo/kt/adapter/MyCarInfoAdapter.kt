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
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * @program: newshengmo
 * @description: 购买座驾适配器
 * @author: whl
 * @create: 2022-05-26 11:45
 **/
class MyCarInfoAdapter(val context: Context, val list: List<CarListModel.DataBean>) : RecyclerView.Adapter<MyCarInfoAdapter.MyCarInfoViewHolder>() {


    inner class MyCarInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivImg: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_img) }
        private val tvUserd: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_userd) }
        private val tvName: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name) }
        private val tvTimes: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_times) }

        fun display(position: Int) {
            list[position].run {
//                ImageLoader.loadImage(context, cover, ivImg)
                Glide.with(context).load(cover).skipMemoryCache(true). apply(RequestOptions().error(R.mipmap.default_error)).into(ivImg)
                tvName.text = name
                tvTimes.text = "到期时间：${overtime}"
                if (is_default == "1") {
                    tvUserd.visibility = View.VISIBLE
                } else {
                    tvUserd.visibility = View.INVISIBLE
                }


                itemView.setOnClickListener {
                    onSimpleItemListener?.onItemListener(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyCarInfoViewHolder {
        return MyCarInfoViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_my_car_info, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyCarInfoViewHolder?, position: Int) {
        holder?.display(position)
    }


    var onSimpleItemListener: OnSimpleItemListener? = null
}