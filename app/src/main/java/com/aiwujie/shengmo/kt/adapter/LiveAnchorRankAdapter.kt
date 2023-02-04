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


class LiveAnchorRankAdapter(var context: Context, var rankList: List<LiveRankBean.DataBean>, var type: Int) : RecyclerView.Adapter<LiveAnchorRankAdapter.AnchorRankHolder>() {

   //1  主播榜  2  回放榜
    private var mChildType: Int = 0

    constructor(context: Context, rankList: List<LiveRankBean.DataBean>, type: Int, childType: Int) : this(context, rankList, type) {
        mChildType = childType
    }


    inner class AnchorRankHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivItemIndex: ImageView = itemView.findViewById(R.id.iv_item_live_ranking_index)
        var tvItemIndex: TextView = itemView.findViewById(R.id.tv_item_live_ranking_index)
        var tvItemName: TextView = itemView.findViewById(R.id.tv_item_live_ranking_name)
        var llSexAndAge: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)
        var ivItemSex: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)
        var tvItemAge: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)
        var tvItemRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)
        var tvItemCount: TextView = itemView.findViewById(R.id.tv_item_live_ranking_count)
        var tvItemGetOrSend: TextView = itemView.findViewById(R.id.tv_item_live_ranking_getOrSend)
        var ivFavourOne: ImageView = itemView.findViewById(R.id.iv_item_live_ranking_top_one)
        var ivFavourTwo: ImageView = itemView.findViewById(R.id.iv_item_live_ranking_top_two)
        var ivFavourThree: ImageView = itemView.findViewById(R.id.iv_item_live_ranking_top_three)
        var tvItemFavour: TextView = itemView.findViewById(R.id.tv_item_live_ranking_favour)
        var ivItemIcon: ImageView = itemView.findViewById(R.id.iv_user_avatar_icon)
        var ivItemIdentity: ImageView = itemView.findViewById(R.id.iv_user_avatar_level)
        var flFavourOne: FrameLayout = itemView.findViewById(R.id.fl_item_live_ranking_top_one)
        var flFavourTwo: FrameLayout = itemView.findViewById(R.id.fl_item_live_ranking_top_two)
        var flFavourThree: FrameLayout = itemView.findViewById(R.id.fl_item_live_ranking_top_three)
        var llItemLevel: LinearLayout = itemView.findViewById(R.id.ll_item_live_ranking_level)
        var llItemFavour: LinearLayout = itemView.findViewById(R.id.ll_item_live_ranking_top)
        var clAnchorLevel: ConstraintLayout = itemView.findViewById(R.id.constraint_layout_anchor_level)
        var tvAnchorLevel: TextView = itemView.findViewById(R.id.tv_live_anchor_level)
        var clAudienceLevel: ConstraintLayout = itemView.findViewById(R.id.constraint_layout_audience_level)
        var tvAudienceLevel: TextView = itemView.findViewById(R.id.tv_live_audience_level)

        var ivItemLive: ImageView = itemView.findViewById(R.id.iv_user_avatar_state)
        var lottieItemLive: LottieAnimationView = itemView.findViewById(R.id.lottie_user_avatar_state)


        fun display(index: Int) {
            rankList[index].let {
                when (index) {
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
                UserIdentityUtils.showIdentity(context, it.head_pic, it.uid, it.is_volunteer, it.is_admin, it.svipannual, it.svip, it.vipannual, it.vip, it.bkvip, it.blvip, ivItemIdentity)
                GlideImgManager.showNormalCircleIcon(context, it.head_pic, ivItemIcon)
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
                tvItemGetOrSend.text = "获得"
                tvItemCount.text = it.allamount

                when (it.rewardeduserinfo.size) {
                    0 -> {
                        flFavourOne.visibility = View.INVISIBLE
                        flFavourTwo.visibility = View.GONE
                        flFavourThree.visibility = View.GONE
                    }
                    1 -> {
                        flFavourOne.visibility = View.VISIBLE
                        flFavourTwo.visibility = View.GONE
                        flFavourThree.visibility = View.GONE
                        GlideImgManager.showNormalCircleIcon(context, it.rewardeduserinfo[0].head_pic, ivFavourOne)
                        ivFavourOne.setOnClickListener {
                            rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[0].fuid)
                        }
                        ivFavourTwo.setOnClickListener { }
                        ivFavourThree.setOnClickListener { }
                    }
                    2 -> {
                        flFavourOne.visibility = View.VISIBLE
                        flFavourTwo.visibility = View.VISIBLE
                        flFavourThree.visibility = View.GONE
                        GlideImgManager.showNormalCircleIcon(context, it.rewardeduserinfo[0].head_pic, ivFavourOne)
                        GlideImgManager.showNormalCircleIcon(context, it.rewardeduserinfo[1].head_pic, ivFavourTwo)
                        ivFavourOne.setOnClickListener {
                            rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[0].fuid)
                        }
                        ivFavourTwo.setOnClickListener {
                            rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[1].fuid)
                        }
                        ivFavourThree.setOnClickListener { }
                    }
                    3 -> {
                        flFavourOne.visibility = View.VISIBLE
                        flFavourTwo.visibility = View.VISIBLE
                        flFavourThree.visibility = View.VISIBLE
                        GlideImgManager.showNormalCircleIcon(context, it.rewardeduserinfo[0].head_pic, ivFavourOne)
                        GlideImgManager.showNormalCircleIcon(context, it.rewardeduserinfo[1].head_pic, ivFavourTwo)
                        GlideImgManager.showNormalCircleIcon(context, it.rewardeduserinfo[2].head_pic, ivFavourThree)
                        ivFavourOne.setOnClickListener {
                            rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[0].fuid)
                        }
                        ivFavourTwo.setOnClickListener {
                            rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[1].fuid)
                        }
                        ivFavourThree.setOnClickListener {
                            rankItemLister?.donFavourItemClick(rankList[index].rewardeduserinfo[2].fuid)
                        }
                    }
                    else -> {
                    }
                }
                tvItemFavour.text = "豪粉"
                tvItemFavour.setBackgroundResource(R.drawable.bg_round_purple)

                ivItemIcon.setOnClickListener {
                    rankItemLister?.doUserItemClick(index)
                }

                if (type == 4) {
                    if (mChildType == 1 ){
                        llItemLevel.visibility = View.VISIBLE
                        llItemFavour.visibility = View.GONE
                        clAnchorLevel.visibility = if (TextUtils.isEmpty(it.anchor_level) || "0" == it.anchor_level) View.GONE else View.VISIBLE;
                        clAudienceLevel.visibility = View.GONE
                        tvAnchorLevel.text = it.anchor_level
                    }else  if (mChildType == 2 ) {
                        llItemLevel.visibility = View.GONE
                        llItemFavour.visibility = View.VISIBLE
                        clAnchorLevel.visibility = View.GONE
                        clAudienceLevel.visibility = View.GONE
                        tvAnchorLevel.text = it.anchor_level
                    }


                } else {
                    llItemLevel.visibility = View.GONE
                    llItemFavour.visibility = View.VISIBLE
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
        return AnchorRankHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_ranking, parent, false))
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: AnchorRankHolder?, position: Int) {
        holder?.display(position)
    }

    var rankItemLister: OnLiveRankItemListener? = null
}