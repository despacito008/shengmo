package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.AddressBean
import com.aiwujie.shengmo.kt.util.showToast

class SearchAdapter(var context: Context, var list: List<AddressBean> =ArrayList())  : RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder>() {



    inner class SearchAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.map_item_title)
        val tvText: TextView = itemView.findViewById(R.id.map_item_text)

        fun display(position: Int) {
            list[position].run {
                tvTitle.text = title
                tvText.text = province + city + text

              itemView.setOnClickListener {
                  mOnAddressClick!!.onClick(it,position)
              }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchAdapterViewHolder {
        return SearchAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_map_address, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder?, position: Int) {
        holder?.display(position)

    }


    var mOnAddressClick: OnAddressClick? = null

    interface OnAddressClick {
        fun onClick(view:View,position: Int)
    }

    fun setOnAddressClick(onAddressClick: OnAddressClick?) {
        mOnAddressClick = onAddressClick
    }


}