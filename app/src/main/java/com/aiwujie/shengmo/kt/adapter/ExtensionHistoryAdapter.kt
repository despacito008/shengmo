package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.CallUseHistoryBean
import com.aiwujie.shengmo.bean.CallUserBean
import com.aiwujie.shengmo.kt.ui.fragment.normallist.CallCardUseHistoryFragment
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.TextViewUtil

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: CallUseStatisticalAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/26 17:26
 * @Description: 推广明细适配器
 */
class ExtensionHistoryAdapter(val context:Context, private val historyList:ArrayList<CallUseHistoryBean.DataBean>): RecyclerView.Adapter<ExtensionHistoryAdapter.CallUserStatisticalHolder>() {
    inner class CallUserStatisticalHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val tvItemInfo:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_num) }
        private val tvItemTime:TextView by lazy { itemView.findViewById<TextView>(R.id.tv_item_time) }
        private val ivItemIcon:ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_item_icon) }
        fun display(index:Int) {
            historyList[index].run {
                tvItemInfo.text = history_desc
                tvItemTime.text = add_time_string
                val numStr = "$call_uv 人看到"
                TextViewUtil.setSpannedColorAndSizeText(tvItemInfo, numStr, 0, call_uv.toString().length, Color.parseColor("#131415"),22 )
                val timeStr = "开启时间: $add_time_string"
                TextViewUtil.setSpannedColorText(tvItemTime, timeStr, 6, 6 + add_time_string.toString().length, Color.parseColor("#131415"))
                ImageLoader.loadRoundImage(context,img_url,ivItemIcon,R.drawable.default_head)
            }
            itemView.setOnClickListener {
                simpleItemListener?.onItemListener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CallUserStatisticalHolder {
        return CallUserStatisticalHolder(LayoutInflater.from(context).inflate(R.layout.app_item_extension_history,parent,false))
    }

    override fun onBindViewHolder(holder: CallUserStatisticalHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    var simpleItemListener:OnSimpleItemListener? = null

}
