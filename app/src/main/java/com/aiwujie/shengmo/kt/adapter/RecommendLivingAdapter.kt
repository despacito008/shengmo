package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.HomeNewListData
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.utils.DateUtils
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide
import org.feezu.liuli.timeselector.Utils.TextUtil

class RecommendLivingAdapter(private val context: Context, private val data: List<ScenesRoomInfoBean>) : RecyclerView.Adapter<RecommendLivingAdapter.RecommendLivingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecommendLivingHolder {
        return RecommendLivingHolder(LayoutInflater.from(context).inflate(R.layout.app_item_home_recomend_living, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecommendLivingHolder?, position: Int) {
        holder!!.display(position)
    }

    inner class RecommendLivingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivItemIcon: ImageView = itemView.findViewById(R.id.iv_item_home_recommend_living)
        private var ivItemLiveIng: ImageView = itemView.findViewById(R.id.iv_item_home_recommend_living_ing)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_home_recommend_living)
        private var ivItemInteract:ImageView = itemView.findViewById(R.id.iv_item_live_interact)
        private var ivItemAudio:ImageView = itemView.findViewById(R.id.iv_item_live_audio)
        private var ivItemContract:ImageView = itemView.findViewById(R.id.iv_item_live_contract)
        private var tvItemTicket:TextView = itemView.findViewById(R.id.tv_item_live_ticket_count)
        private var groupTicket:Group = itemView.findViewById(R.id.group_item_live_ticket)
        private var ivItemPassword:ImageView = itemView.findViewById(R.id.iv_item_live_password)

        fun display(index:Int) {
            var userBean = data[index]
            if ("1" == userBean.is_live) {
                ivItemLiveIng.visibility = View.VISIBLE
                Glide.with(context).load(R.drawable.ic_status_living).into(ivItemLiveIng)
            } else {
                ivItemLiveIng.visibility = View.GONE
            }

            ivItemAudio.visibility = if ("1" == userBean.camera_switch) View.VISIBLE else View.GONE
            ivItemInteract.visibility = if ("1" == userBean.is_interaction) View.VISIBLE else View.GONE
            ivItemPassword.visibility = if ("1" == userBean.is_pwd) View.VISIBLE else View.GONE
            ivItemContract.visibility = if ("2" == userBean.anchor_status || "3" == userBean.anchor_status) View.VISIBLE else View.GONE
            groupTicket.visibility = if (userBean.is_ticket == "1") View.VISIBLE else View.GONE
            tvItemTicket.text = userBean.ticket_beans
            GlideImgManager.glideLoader(context, userBean.live_poster, R.drawable.rc_image_error, R.drawable.rc_image_error, ivItemIcon, 1)
            tvItemName.text  = userBean.live_title
            itemView.setOnClickListener {
                onSimpleItemListener?.onItemListener(index)
            }

        }
    }


    private var onSimpleItemListener: OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener: OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }

}