package com.aiwujie.shengmo.timlive.kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.UserIdentityUtils
import org.feezu.liuli.timeselector.Utils.TextUtil

class SearchUserAdapter(private val context: Context, private val data: List<ScenesRoomInfoBean>) : RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchUserHolder {
        return SearchUserHolder(LayoutInflater.from(context).inflate(R.layout.app_item_search_user, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchUserHolder?, position: Int) {
        holder!!.setData(position)
    }

    inner class SearchUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivIcon: ImageView = itemView.findViewById(R.id.iv_item_search_user_icon)
        private var tvItemName: TextView = itemView.findViewById(R.id.tv_item_search_live_user_name)
        private var ivItemOnline: ImageView = itemView.findViewById(R.id.iv_item_search_user_online)
        private var ivItemIdentity: ImageView = itemView.findViewById(R.id.iv_item_search_user_identity)
        private var llItemSexAndAge: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)
        private var ivItemSex: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)
        private var tvItemAge: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)
        private var tvItemRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)
        private var tvItemAddress: TextView = itemView.findViewById(R.id.tv_item_search_live_user_address)
        private var tvItemLocation: TextView = itemView.findViewById(R.id.tv_item_search_live_user_location)
        private var ivItemNoAddress: ImageView = itemView.findViewById(R.id.iv_item_search_live_user_no_address)
        private var ivItemNoLocation: ImageView = itemView.findViewById(R.id.iv_item_search_live_user_no_location)
        private var llItemLive: LinearLayout = itemView.findViewById(R.id.ll_item_search_user_live_status)
        private var llItemWealth: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_wealth)
        private var llItemCharm: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_charm)
        private var tvItemWealth: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_wealth)
        private var tvItemCharm: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_charm)
        private var ivItemPhotoAuth: ImageView = itemView.findViewById(R.id.iv_item_search_user_photo_auth)
        private var ivItemIdAuth: ImageView = itemView.findViewById(R.id.iv_item_search_user_id_card_auth)
        private var ivItemVideoAuth: ImageView = itemView.findViewById(R.id.iv_item_search_user_video_auth)

        fun setData(index: Int) {
            var userBean = data[index]
            GlideImgManager.glideLoader(context, userBean.head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivIcon, 0)
            tvItemName.text = userBean.nickname
            ivItemOnline.visibility = if(userBean.onlinestate == 0) View.GONE else View.VISIBLE
            llItemLive.visibility =  if (userBean.is_live == "1") View.VISIBLE else View.GONE
            ivItemPhotoAuth.visibility =  if (userBean.realname == "1") View.VISIBLE else View.GONE
            ivItemIdAuth.visibility =  if (userBean.realids == "1") View.VISIBLE else View.GONE
            ivItemVideoAuth.visibility =  if (userBean.video_auth_status == "1") View.VISIBLE else View.GONE
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, userBean.head_pic, userBean.uid, userBean.is_volunteer, userBean.is_admin, userBean.svipannual, userBean.svip, userBean.vipannual, userBean.vip, userBean.bkvip, userBean.blvip,ivItemIdentity)
            when(userBean.sex) {
                "1" -> {
                    llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                    ivItemSex.setImageResource(R.mipmap.nan)
                }
                "2" -> {
                    llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                    ivItemSex.setImageResource(R.mipmap.nv)
                }
                "3" -> {
                    llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                    ivItemSex.setImageResource(R.mipmap.san)
                }
            }
            tvItemAge.text = userBean.age
            when(userBean.role) {
                "S" -> {
                    tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                    tvItemRole.text = "斯"
                }
                "M" -> {
                    tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                    tvItemRole.text = "慕"
                }
                "SM" -> {
                    tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                    tvItemRole.text = "双"
                }
                else -> {
                    tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
                    tvItemRole.text = userBean.role
                }
            }
            if ("1" == userBean.location_city_switch) {
                tvItemAddress.visibility = View.GONE
                ivItemNoAddress.visibility = View.VISIBLE
            } else {
                if (TextUtil.isEmpty(userBean.city) && TextUtil.isEmpty(userBean.province)) {
                    tvItemAddress.visibility = View.GONE
                    ivItemNoAddress.visibility = View.VISIBLE
                } else {
                    tvItemAddress.visibility = View.VISIBLE
                    ivItemNoAddress.visibility = View.GONE
                    if (userBean.city == userBean.province) {
                        tvItemAddress.text = userBean.city
                    } else {
                        tvItemAddress.text = userBean.province + userBean.city
                    }
                }
            }
            if (userBean.lat != "0.000000" && userBean.lat != "0.000000" && "隐身" != userBean.distance) {
                tvItemLocation.text = userBean.distance
                tvItemLocation.visibility = View.VISIBLE
                ivItemNoLocation.visibility = View.GONE
            } else {
                tvItemLocation.visibility = View.GONE
                ivItemNoLocation.visibility = View.VISIBLE
            }
            if (userBean.wealth_val == "0") {
                llItemWealth.visibility = View.GONE
            } else{
                llItemWealth.visibility = View.VISIBLE
                tvItemWealth.text = userBean.wealth_val
            }
            if (userBean.charm_val == "0") {
                llItemCharm.visibility = View.GONE
            } else{
                llItemCharm.visibility = View.VISIBLE
                tvItemCharm.text = userBean.charm_val
            }

            itemView.setOnClickListener {
                onSimpleItemListener?.apply {
                   this.onItemListener(index)
                }
            }
        }
    }

    private var onSimpleItemListener : OnSimpleItemListener? = null;

    fun setOnSimpleItemListener(onSimpleItemListener : OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }
}