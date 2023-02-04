package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ConversationCallItemBean
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.setTextSizeWithDp
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.roundview.RoundFrameLayout

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.adapter
 * @ClassName: ConversationCallItemAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/25 11:49
 * @Description:
 */
class ConversationCallItemAdapter(private val context: Context, private val itemList: List<ConversationCallItemBean.DataBean>) : RecyclerView.Adapter<ConversationCallItemAdapter.ConversationCallItemHolder>() {
    var chooseIndex = 1

    inner class ConversationCallItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flItemBg: RoundFrameLayout by lazy { itemView.findViewById<RoundFrameLayout>(R.id.rfl_choose_item_bg) }
        private val tvItemInfo: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_choose_info) }
        private val tvItemTimes: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_conversation_call_times) }
        private val tvItemPrice: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_conversation_call_price) }
        fun display(index: Int) {
            itemList[index].run {
                tvItemInfo.text = discount_desc
                tvItemTimes.text = "${bag_times}次"
                tvItemPrice.text = bag_jg_desc
            }

            if (chooseIndex == index) {
                flItemBg.visibility = View.VISIBLE
                tvItemTimes.setTextSizeWithDp(18f)
                tvItemPrice.setTextSizeWithDp(15f)
                when (chooseIndex) {
                    0 -> {
                        tvItemInfo.visibility = View.VISIBLE
                    }
                    1 -> {
                        tvItemInfo.visibility = View.VISIBLE
                    }
                    2 -> {
                        tvItemInfo.visibility = View.GONE
                    }
                }
            } else {
                flItemBg.visibility = View.GONE
                tvItemTimes.setTextSizeWithDp(16f)
                tvItemPrice.setTextSizeWithDp(13f)
            }
            itemView.setOnClickListener {
                if (chooseIndex != index) {
                    val temp = chooseIndex
                    chooseIndex = index
                    notifyItemChanged(temp)
                    notifyItemChanged(chooseIndex)
                    onItemClickListener?.onItemListener(chooseIndex)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ConversationCallItemHolder {
        return ConversationCallItemHolder(LayoutInflater.from(context).inflate(R.layout.app_item_conversation_call_goods, parent, false))
    }

    override fun onBindViewHolder(holder: ConversationCallItemHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

    var onItemClickListener:OnSimpleItemListener? = null
}
