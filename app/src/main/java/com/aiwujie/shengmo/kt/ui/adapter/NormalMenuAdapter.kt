package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import com.aiwujie.shengmo.utils.OnSimpleItemListener


class NormalMenuAdapter(private val context: Context,private val menuList: List<NormalMenuItem>) : RecyclerView.Adapter<NormalMenuAdapter.NormalMenuHolder>() {

    inner class NormalMenuHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        private var tvItem = itemView.findViewById<TextView>(R.id.tv_item_pop_normal_menu)
        private var ivItem = itemView.findViewById<ImageView>(R.id.iv_item_pop_normal_menu)
        private var tvItemLine = itemView.findViewById<TextView>(R.id.tv_item_pop_normal_menu_line)

        fun setData(position: Int) {
            tvItem.text = menuList[position].content
            when(menuList[position].type) {
                0->{
                    ivItem.visibility = View.GONE
                }
                1->{
                    ivItem.visibility = View.VISIBLE
                    ivItem.setImageResource(R.drawable.user_vip)
                }
                2->{
                    ivItem.visibility = View.VISIBLE
                    ivItem.setImageResource(R.drawable.user_svip)
                }
            }
            when(position) {
                menuList.size - 1 ->{
                    tvItemLine.visibility = View.GONE
                }
                else -> {
                    tvItemLine.visibility = View.VISIBLE
                }
            }
            itemView.setOnClickListener {
                onSimpleItemListener?.apply {
                    this.onItemListener(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NormalMenuHolder {
        return NormalMenuHolder(LayoutInflater.from(context).inflate(R.layout.app_item_pop_normal_menu, parent, false))
    }

    override fun getItemCount(): Int {
       return menuList.size
    }

    override fun onBindViewHolder(holder: NormalMenuHolder?, position: Int) {
        holder!!.setData(position)
    }

    private var onSimpleItemListener : OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener : OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }
}