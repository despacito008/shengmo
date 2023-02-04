package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LiveRankBean
import com.aiwujie.shengmo.kt.listener.OnLiveRankItemListener
import com.aiwujie.shengmo.utils.GlideImageLoader
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide
import org.feezu.liuli.timeselector.Utils.TextUtil


class LivePlayBackRankAdapter(var context:Context, var rankList:List<LiveRankBean.DataBean>, var type:Int): RecyclerView.Adapter<LivePlayBackRankAdapter.AnchorRankHolder>()  {

    inner class AnchorRankHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivItemIndex:ImageView = itemView.findViewById(R.id.iv_item_live_ranking_index)
        var tvItemIndex:TextView = itemView.findViewById(R.id.tv_item_live_ranking_index)
        var tvItemName:TextView = itemView.findViewById(R.id.tv_item_live_ranking_name)
        var llSexAndAge:LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)
        var ivItemSex:ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)
        var tvItemAge:TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)
        var tvItemRole:TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)
        var tvItemCount:TextView = itemView.findViewById(R.id.tv_item_live_ranking_count)
        var tvItemGetOrSend:TextView = itemView.findViewById(R.id.tv_item_live_ranking_getOrSend)
        var ivItemIcon:ImageView = itemView.findViewById(R.id.iv_user_avatar_icon)
        var ivItemIdentity:ImageView = itemView.findViewById(R.id.iv_user_avatar_level)
        var llItemLevel:LinearLayout = itemView.findViewById(R.id.ll_item_live_ranking_level)
        var ivItemLive:ImageView = itemView.findViewById(R.id.iv_user_avatar_state)
        var lottieItemLive: LottieAnimationView = itemView.findViewById(R.id.lottie_user_avatar_state)
        var ivItemLucky:ImageView = itemView.findViewById(R.id.iv_item_red_bag_rank_lucky)
        var tvItemLucky:TextView = itemView.findViewById(R.id.tv_item_live_lucky_count)


        fun display(index:Int) {
            rankList[index].let {
                when(index) {
                    0 -> {
                        ivItemIndex.visibility = View.VISIBLE
                        tvItemIndex.visibility = View.GONE
                        ivItemIndex.setImageResource(R.drawable.ic_reward_ranking_top_one)
                    }
                    1 -> {
                        ivItemIndex.visibility = View.VISIBLE
                        tvItemIndex.visibility = View.GONE
                        ivItemIndex.setImageResource(R.drawable.ic_reward_ranking_top_two)
                    }
                    2 -> {
                        ivItemIndex.visibility = View.VISIBLE
                        tvItemIndex.visibility = View.GONE
                        ivItemIndex.setImageResource(R.drawable.ic_reward_ranking_top_three)
                    }
                    else -> {
                        ivItemIndex.visibility = View.INVISIBLE
                        tvItemIndex.visibility = View.VISIBLE
                        tvItemIndex.text = "${index + 1}"
                    }
                }
                tvItemName.text = it.nickname
                tvItemAge.text = it.age
                //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
                UserIdentityUtils.showIdentity(context,it.head_pic, it.uid, it.is_volunteer, it.is_admin, it.svipannual,it.svip, it.vipannual, it.vip, it.bkvip, it.blvip, ivItemIdentity)
                GlideImgManager.showNormalCircleIcon(context,it.head_pic,ivItemIcon)
                when (it.sex) {
                    "1" -> {
                       llSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                       ivItemSex.setImageResource(R.mipmap.nan)
                    }
                    "2" -> {
                        llSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                        ivItemSex.setImageResource(R.mipmap.nv)
                    }
                    "3" -> {
                        llSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                        ivItemSex.setImageResource(R.mipmap.san)
                    }
                }
                when (it.role) {
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
                        tvItemRole.text = it.role
                    }
                }
                tvItemGetOrSend.text = "送出"
                tvItemCount.text = it.allamount

//                when(it.rewardeduserinfo.size) {
//                    0 -> {
//                        llItemLevel.visibility = View.GONE
//                    }
//                    else ->{
//                        llItemLevel.visibility = View.VISIBLE
//                        tvItemLucky.text = it.rewardeduserinfo[0].allamount
//                        GlideImgManager.showNormalCircleIcon(context,it.rewardeduserinfo[0].head_pic,ivItemLucky)
//                    }
//                }


                ivItemIcon.setOnClickListener {
                    rankItemLister?.doUserItemClick(index)
                }

                llItemLevel.setOnClickListener {
                    rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[0].fuid)
                }


                if (!TextUtil.isEmpty(it.anchor_room_id) && "0" != it.anchor_room_id) {
                    ivItemLive.visibility = View.VISIBLE
                    lottieItemLive.visibility = View.INVISIBLE
                    if ("0" == it.anchor_is_live) {
                        Glide.with(context).load(R.drawable.ic_user_liver).into(ivItemLive)
                    } else {
                        lottieItemLive.visibility = View.VISIBLE
                        lottieItemLive.imageAssetsFolder = "images"
                        lottieItemLive.setAnimation("user_living.json")
                        lottieItemLive.loop(true)
                        lottieItemLive.playAnimation()
                    }
                } else {
                    ivItemLive.visibility = View.INVISIBLE
                    lottieItemLive.setVisibility(View.INVISIBLE)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AnchorRankHolder {
       return AnchorRankHolder(LayoutInflater.from(context).inflate(R.layout.app_item_play_back_ranking,parent,false))
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: AnchorRankHolder?, position: Int) {
       holder?.display(position)
    }

    var rankItemLister: OnLiveRankItemListener? = null
}