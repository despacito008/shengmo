package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.OpenHighRuleBean
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import kotlinx.android.synthetic.main.item_rv_dynamic.view.*
import org.feezu.liuli.timeselector.Utils.TextUtil

class OpenHighRuleAdapter(var context: Context, var list: ArrayList<OpenHighRuleBean.DataBean>) : RecyclerView.Adapter<OpenHighRuleAdapter.OpenHighRuleViewHolder>() {
    var currentIndex: Int = 0

    inner class OpenHighRuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvPrice: TextView = itemView.findViewById(R.id.tv_item_vip_center_price)
        var tvTimes: TextView = itemView.findViewById(R.id.tv_times)
        var llRootView: View = itemView.findViewById(R.id.ll_rootView)
        var tvOldPrice: TextView = itemView.findViewById(R.id.tv_item_vip_center_original_price)
        var tvDiscount: TextView = itemView.findViewById(R.id.tv_item_vip_center_discount)

        fun display(position: Int) {
            list[position].run {
                tvPrice.text = bag_price.substring(0, bag_price.indexOf("."))
                tvTimes.text = "元/${bag_year}年"

                if (position == 0) {
                    if (TextUtil.isEmpty(discount_desc)) {
                        tvDiscount.visibility = View.GONE
                    } else {
                        tvDiscount.visibility = View.VISIBLE
                        tvDiscount.text = discount_desc
                    }
                } else {
                    if (TextUtil.isEmpty(discount_desc)) {
                        tvDiscount.visibility = View.GONE
                    } else {
                        tvDiscount.visibility = View.VISIBLE
                        tvDiscount.text = discount_desc
                    }
                }


                if (!TextUtil.isEmpty(original_price)) {
                    tvOldPrice.text = "原价:${original_price}元"
                    tvOldPrice.visibility = View.VISIBLE
                    tvOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvOldPrice.visibility = View.GONE
                }

                tvOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

                llRootView.isSelected = currentIndex == position


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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OpenHighRuleViewHolder {
        return OpenHighRuleViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_open_high_rule, null))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OpenHighRuleViewHolder?, position: Int) {
        holder?.display(position)
    }

    var simpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(simpleItemListener: OnSimpleItemListener?) {
        this.simpleItemListener = simpleItemListener
    }
}