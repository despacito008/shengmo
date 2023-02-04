package com.aiwujie.shengmo.kt.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.AtGroupMemberData
import com.aiwujie.shengmo.http.HttpUrl.NetPic
import com.aiwujie.shengmo.utils.GlideImgManager

class AtMemberListViewAdapter(var context: Context, var list: List<AtGroupMemberData.DataBean>) : RecyclerView.Adapter<AtMemberListViewAdapter.AtMemberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AtMemberListViewAdapter.AtMemberViewHolder {
        return AtMemberListViewAdapter.AtMemberViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item_at_group_member, parent, false));
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AtMemberListViewAdapter.AtMemberViewHolder, position: Int) {
       var   model :AtGroupMemberData.DataBean= list[position]
        if (""  == model.head_pic    ||  NetPic().equals(model.head_pic) ){
            holder.itemAtGroupMemberIcon.setImageResource(R.mipmap.morentouxiang)
        }else {
            GlideImgManager.glideLoader(context,model.head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemAtGroupMemberIcon, 0)
        }

        if ("" !=model.cardname&& model.cardname != null) {
            holder.itemAtGroupMemberName.text = model.cardname
        } else {
            holder.itemAtGroupMemberName.text = model.nickname
        }

        holder.itemView.setOnClickListener {
            AtGroupClick!!.OnClick(position,model.uid,model.nickname,model.cardname,model.head_pic)
        }

    }

    class AtMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemAtGroupMemberIcon: ImageView = itemView.findViewById(R.id.item_at_group_member_icon)
        var itemAtGroupMemberName: TextView = itemView.findViewById(R.id.item_at_group_member_name)

    }

   var AtGroupClick :OnAtGroupClick? = null

    interface  OnAtGroupClick {
        fun  OnClick (position:Int,uid:String,nickName:String,cardName:String ,headPic:String)
    }

    fun  setAtGroupClickListener(onAtGroupClick :OnAtGroupClick?) {
        AtGroupClick =onAtGroupClick
    }


}