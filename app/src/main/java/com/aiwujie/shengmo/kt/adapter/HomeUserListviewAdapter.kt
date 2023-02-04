package com.aiwujie.shengmo.kt.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.HomeNewListData
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide
import org.feezu.liuli.timeselector.Utils.TextUtil

class HomeUserListviewAdapter(var context: Context, var list: List<HomeNewListData.DataBean>, var retcode: Int, var isVisivleTime: Int) : RecyclerView.Adapter<HomeUserListviewAdapter.HomeUserListViewHolder>() {


    inner class HomeUserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivUserAvatarState: ImageView = itemView.findViewById(R.id.iv_user_avatar_state)
        var lottieAnimationView: LottieAnimationView = itemView.findViewById(R.id.lottie_user_avatar_state)
        var ivUserAvatarIcon: ImageView = itemView.findViewById(R.id.iv_user_avatar_icon)

        var ivUserAvatarLevel: ImageView = itemView.findViewById(R.id.iv_user_avatar_level)

        var ivUserAvatarOnline: ImageView = itemView.findViewById(R.id.iv_user_avatar_online)

        var layoutUserAvatar: ConstraintLayout = itemView.findViewById(R.id.layout_user_avatar)

        var tvItemUserNormalInfoName: TextView = itemView.findViewById(R.id.tv_item_user_normal_info_name)

        var ivItemUserNormalInfoAuthPhoto: ImageView = itemView.findViewById(R.id.iv_item_user_normal_info_auth_photo)

        var ivItemUserNormalInfoAuthIdCard: ImageView = itemView.findViewById(R.id.iv_item_user_normal_info_auth_idCard)

        var ivItemUserNormalInfoAuthVideo: ImageView = itemView.findViewById(R.id.iv_item_user_normal_info_auth_Video)

        var tvItemUserNormalInfoDistance: TextView = itemView.findViewById(R.id.tv_item_user_normal_info_distance)
        var ivItemUserNormalInfoNoDistance: ImageView = itemView.findViewById(R.id.iv_item_user_normal_info_noDistance)

        var tvItemUserNormalInfoTime: TextView = itemView.findViewById(R.id.tv_item_user_normal_info_time)


        var ivItemUserNormalInfoNoTime: ImageView = itemView.findViewById(R.id.iv_item_user_normal_info_noTime)

        var ivLayoutUserNormalInfoSex: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_sex)

        var tvLayoutUserNormalInfoAge: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_age)

        var llLayoutUserNormalInfoSexAge: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_sex_age)

        var tvLayoutUserNormalInfoRole: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_role)

        var ivLayoutUserNormalInfoWealth: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_wealth)

        var tvLayoutUserNormalInfoWealth: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_wealth)

        var llLayoutUserNormalInfoWealth: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_wealth)

        var ivLayoutUserNormalInfoCharm: ImageView = itemView.findViewById(R.id.iv_layout_user_normal_info_charm)


        var tvLayoutUserNormalInfoCharm: TextView = itemView.findViewById(R.id.tv_layout_user_normal_info_charm)

        var llLayoutUserNormalInfoCharm: LinearLayout = itemView.findViewById(R.id.ll_layout_user_normal_info_charm)

        var layoutUserNormalInfo: LinearLayout = itemView.findViewById(R.id.layout_user_normal_info)

        var tvItemUserNormalInfoAddress: TextView = itemView.findViewById(R.id.tv_item_user_normal_info_address)

        var ivItemUserNormalInfoNoAddress: ImageView = itemView.findViewById(R.id.iv_item_user_normal_info_noAddress)

        var clAnchorLevel: ConstraintLayout = itemView.findViewById(R.id.constraint_layout_anchor_level)

        var tvAnchorLevel: TextView = itemView.findViewById(R.id.tv_live_anchor_level)

        var clAudienceLevel: ConstraintLayout = itemView.findViewById(R.id.constraint_layout_audience_level)

        var tvAudienceLevel: TextView = itemView.findViewById(R.id.tv_live_audience_level)

        fun display(position: Int) {
            list[position].run {


                if (head_pic.isEmpty() || head_pic == HttpUrl.NetPic()) { //"http://59.110.28.150:888/"
                    ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang)
                } else {
                    GlideImgManager.glideLoader(context, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivUserAvatarIcon, 0)
                }

                if (isVisivleTime != 0) {
                    if ("隐身" == last_login_time) {
                        ivItemUserNormalInfoNoTime.visibility = View.VISIBLE
                        tvItemUserNormalInfoTime.visibility = View.GONE
                    } else {
                        ivItemUserNormalInfoNoTime.visibility = View.GONE
                        tvItemUserNormalInfoTime.visibility = View.VISIBLE
                        tvItemUserNormalInfoTime.text = last_login_time
                    }
                } else {
                    ivItemUserNormalInfoNoTime.visibility = View.GONE
                    tvItemUserNormalInfoTime.visibility = View.GONE
                }

                if ("1" == login_time_switch) {
                    tvItemUserNormalInfoTime.text = "隐身"
                }

                if (retcode == 2000 && lat != "0.000000" && lng != "0.000000" && "隐身" != distance) {
                    tvItemUserNormalInfoDistance.text = distance
                    tvItemUserNormalInfoDistance.visibility = View.VISIBLE
                    ivItemUserNormalInfoNoDistance.visibility = View.GONE
                } else {
                    ivItemUserNormalInfoNoDistance.visibility = View.VISIBLE
                    tvItemUserNormalInfoDistance.visibility = View.GONE
                }

                if (onlinestate == 0) {
                    ivUserAvatarOnline.visibility = View.GONE
                } else {
                    ivUserAvatarOnline.visibility = View.VISIBLE
                }
                //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
                UserIdentityUtils.showIdentity(context, head_pic, uid, is_volunteer, is_admin, svipannual, svip, vipannual, vip, bkvip, blvip, ivUserAvatarLevel)

                //是否实名认证
                if ("1" == realids) {
                    ivItemUserNormalInfoAuthIdCard.visibility = View.VISIBLE
                } else {
                    ivItemUserNormalInfoAuthIdCard.visibility = View.GONE
                }

                //是否自拍认证
                if ("1" == realname) {
                    ivItemUserNormalInfoAuthPhoto.visibility = View.VISIBLE
                } else {
                    ivItemUserNormalInfoAuthPhoto.visibility = View.GONE
                }
                //短视频认证
                //短视频认证
                if ("1" == video_auth_status) {
                    ivItemUserNormalInfoAuthVideo.visibility = View.VISIBLE
                } else {
                    ivItemUserNormalInfoAuthVideo.visibility = View.GONE
                }
                tvLayoutUserNormalInfoAge.text = age

                when (sex) {
                    "1" -> {
                        llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                        ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan)
                    }
                    "2" -> {
                        llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                        ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv)
                    }
                    "3" -> {
                        llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                        ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san)
                    }
                }
                if ("S" == role) {
                    tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                    tvLayoutUserNormalInfoRole.text = "斯"
                } else if ("M" == role) {
                    tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                    tvLayoutUserNormalInfoRole.text = "慕"
                } else if ("SM" == role) {
                    tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                    tvLayoutUserNormalInfoRole.text = "双"
                } else {
                    tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
                    tvLayoutUserNormalInfoRole.text = role
                }
                tvItemUserNormalInfoName.text = nickname

                if ("1" == location_city_switch) {
                    tvItemUserNormalInfoAddress.visibility = View.GONE
                    ivItemUserNormalInfoNoAddress.visibility = View.VISIBLE
                } else {
                    if (city.isEmpty() && province.isEmpty()) {
                        tvItemUserNormalInfoAddress.visibility = View.GONE
                        ivItemUserNormalInfoNoAddress.visibility = View.VISIBLE
                    } else {
                        tvItemUserNormalInfoAddress.visibility = View.VISIBLE
                        ivItemUserNormalInfoNoAddress.visibility = View.GONE
                        if (province == city) {
                            tvItemUserNormalInfoAddress.text = city
                        } else {
                            tvItemUserNormalInfoAddress.text = province + " " + city
                        }
                    }
                }


                if ("0" != charm_val) {
                    llLayoutUserNormalInfoCharm.visibility = View.VISIBLE
                    tvLayoutUserNormalInfoCharm.text = charm_val
                } else {
                    llLayoutUserNormalInfoCharm.visibility = View.GONE
                }

                if ("0" != wealth_val) {
                    llLayoutUserNormalInfoWealth.visibility = View.VISIBLE
                    tvLayoutUserNormalInfoWealth.text = wealth_val
                } else {
                    llLayoutUserNormalInfoWealth.visibility = View.GONE
                }

                if (anchor_room_id.isNotEmpty() && "0" != anchor_room_id) {
                    if ("0" == anchor_is_live) {
                        ivUserAvatarState.visibility = View.VISIBLE
                        lottieAnimationView.visibility = View.INVISIBLE
                        Glide.with(context).load(R.drawable.ic_user_liver).into(ivUserAvatarState)
                    } else {
                        ivUserAvatarState.visibility = View.INVISIBLE
                        lottieAnimationView.visibility = View.VISIBLE
                        lottieAnimationView.setImageAssetsFolder("images")
                        lottieAnimationView.setAnimation("user_living.json")
                        lottieAnimationView.repeatMode = LottieDrawable.RESTART
                        lottieAnimationView.repeatCount = LottieDrawable.INFINITE
                        lottieAnimationView.playAnimation()
                        val mAnimation = AnimationUtils.loadAnimation(context, R.anim.live_icon_scale)
                        ivUserAvatarIcon.animation = mAnimation
                        mAnimation.start()
                    }
                } else {
                    ivUserAvatarState.visibility = View.INVISIBLE
                    lottieAnimationView.visibility = View.INVISIBLE
                }



                if (user_level.isEmpty() || "0" == user_level) {
                    clAudienceLevel.visibility = View.GONE
                } else {
                    clAudienceLevel.visibility = View.VISIBLE
                    tvAudienceLevel.text = user_level
                }
                if (anchor_level.isEmpty() || "0" == anchor_level) {
                    clAnchorLevel.visibility = View.GONE
                } else {
                    clAnchorLevel.visibility = View.VISIBLE
                    tvAnchorLevel.text = anchor_level
                }


                itemView.setOnClickListener {
                    mapAdapterClickListener!!.onClick(position);
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeUserListViewHolder {
        return HomeUserListViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_user_normal_info, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: HomeUserListViewHolder?, position: Int) {
        holder?.display(position)
    }


    var mapAdapterClickListener: OnMapAdapterClickListener? = null

    interface OnMapAdapterClickListener {
        fun onClick(position: Int)
    }

    fun setOnMapAdapterClickListener(onMapAdapterClickListener: OnMapAdapterClickListener) {
        mapAdapterClickListener = onMapAdapterClickListener
    }
}