package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.utils.ImageLoader
import kotlinx.android.synthetic.main.app_item_live_room_sign.view.*

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.adapter
 * @ClassName: LiveRoomSignAdapter
 * @Author: xmf
 * @CreateDate: 2022/4/27 16:52
 * @Description:
 */
class LiveRoomSignAdapter(val context: Context,private val signList:List<String>): RecyclerView.Adapter<LiveRoomSignAdapter.SignHolder>() {
    inner class SignHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.iv_item_live_room_sign)
        fun display(index:Int) {
            ImageLoader.loadImage(context,signList[index],ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SignHolder {
        return SignHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_room_sign,parent,false))
    }

    override fun onBindViewHolder(holder: SignHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return signList.size
    }
}
