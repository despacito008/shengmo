package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CarInfoModel
import com.aiwujie.shengmo.utils.OnSimpleItemListener

class CarRuleAdapter(var context: Context, var list: List<CarInfoModel.DataBean.DataInfoBean>) : RecyclerView.Adapter<CarRuleAdapter.CarRuleViewHolder>() {
    var currentIndex: Int = 0

    inner class CarRuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvPrice: TextView = itemView.findViewById(R.id.tv_item_vip_center_original_price)
        var tvTimes: TextView = itemView.findViewById(R.id.tv_times)
        var llRootView: View = itemView.findViewById(R.id.ll_rootView)
        var tvDiscount: TextView = itemView.findViewById(R.id.tv_item_vip_center_discount)

        fun display(position: Int) {
            list[position].run {
                tvPrice.text = jg
                tvTimes.text = month

                llRootView.isSelected = currentIndex == position
                tvPrice.isSelected =currentIndex == position
                tvTimes.isSelected =currentIndex == position

                if (!TextUtils.isEmpty(discount_desc)) {
                    tvDiscount.text =discount_desc
                    tvDiscount.visibility = View.VISIBLE
                } else {
                    tvDiscount.visibility = View.GONE
                }
                itemView.setOnClickListener {
                    val temp = currentIndex
                    currentIndex = position
                    notifyItemChanged(currentIndex)
                    notifyItemChanged(temp)

                    simpleItemListener?.onItemListener(position)

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CarRuleViewHolder {
        return CarRuleViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_car_rule, null))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CarRuleViewHolder?, position: Int) {
        holder?.display(position)
    }

    var simpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(simpleItemListener: OnSimpleItemListener?) {
        this.simpleItemListener = simpleItemListener
    }
}