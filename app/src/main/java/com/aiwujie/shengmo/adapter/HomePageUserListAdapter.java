package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class HomePageUserListAdapter extends RecyclerView.Adapter<HomePageUserListAdapter.HomePageUserHolder> {
    Context context;
    ArrayList<HomeNewListData.DataBean> userList;
    boolean isShowTime;
    public HomePageUserListAdapter(Context context, ArrayList<HomeNewListData.DataBean> userList, boolean isShowTime) {
        this.context = context;
        this.userList = userList;
        this.isShowTime = isShowTime;
    }

    @Override
    public HomePageUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_homepage_user_list, parent, false);
        return new HomePageUserHolder(view);
    }

    @Override
    public void onBindViewHolder(HomePageUserHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HomePageUserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_layout_avatar_bg)
        View viewLayoutAvatarBg;
        @BindView(R.id.lottie_user_avatar_state)
        LottieAnimationView lottieUserAvatarState;
        @BindView(R.id.iv_user_avatar_state)
        ImageView ivUserAvatarState;
        @BindView(R.id.iv_user_avatar_icon)
        ImageView ivUserAvatarIcon;
        @BindView(R.id.iv_user_avatar_level)
        ImageView ivUserAvatarLevel;
        @BindView(R.id.iv_user_avatar_online)
        ImageView ivUserAvatarOnline;
        @BindView(R.id.layout_user_avatar)
        ConstraintLayout layoutUserAvatar;
        @BindView(R.id.tv_layout_user_normal_info_name)
        TextView tvLayoutUserNormalInfoName;
        @BindView(R.id.iv_layout_user_normal_info_auth_pic)
        ImageView ivLayoutUserNormalInfoAuthPic;
        @BindView(R.id.iv_layout_user_normal_info_auth_card)
        ImageView ivLayoutUserNormalInfoAuthCard;
        @BindView(R.id.iv_layout_user_normal_info_sex)
        ImageView ivLayoutUserNormalInfoSex;
        @BindView(R.id.tv_layout_user_normal_info_age)
        TextView tvLayoutUserNormalInfoAge;
        @BindView(R.id.ll_layout_user_normal_info_sex_age)
        LinearLayout llLayoutUserNormalInfoSexAge;
        @BindView(R.id.tv_layout_user_normal_info_role)
        TextView tvLayoutUserNormalInfoRole;
        @BindView(R.id.iv_layout_user_normal_info_wealth)
        ImageView ivLayoutUserNormalInfoWealth;
        @BindView(R.id.tv_layout_user_normal_info_wealth)
        TextView tvLayoutUserNormalInfoWealth;
        @BindView(R.id.ll_layout_user_normal_info_wealth)
        LinearLayout llLayoutUserNormalInfoWealth;
        @BindView(R.id.iv_layout_user_normal_info_charm)
        ImageView ivLayoutUserNormalInfoCharm;
        @BindView(R.id.tv_layout_user_normal_info_charm)
        TextView tvLayoutUserNormalInfoCharm;
        @BindView(R.id.ll_layout_user_normal_info_charm)
        LinearLayout llLayoutUserNormalInfoCharm;
        @BindView(R.id.layout_user_normal_info)
        LinearLayout layoutUserNormalInfo;
        @BindView(R.id.tv_layout_user_normal_info_city)
        TextView tvLayoutUserNormalInfoCity;
        @BindView(R.id.tv_item_homepage_user_distance)
        TextView tvItemHomepageUserDistance;
        @BindView(R.id.iv_item_homepage_user_distance)
        ImageView ivItemHomepageUserDistance;
        @BindView(R.id.tv_live_audience_level)
        TextView tvLiveAudienceLevel;
        @BindView(R.id.view_live_audience_level)
        View viewLiveAudienceLevel;
        @BindView(R.id.iv_live_audience_level)
        ImageView ivLiveAudienceLevel;
        @BindView(R.id.constraint_layout_audience_level)
        ConstraintLayout constraintLayoutAudienceLevel;
        @BindView(R.id.tv_live_anchor_level)
        TextView tvLiveAnchorLevel;
        @BindView(R.id.view_live_anchor_level)
        View viewLiveAnchorLevel;
        @BindView(R.id.iv_live_anchor_level)
        ImageView ivLiveAnchorLevel;
        @BindView(R.id.constraint_layout_anchor_level)
        ConstraintLayout constraintLayoutAnchorLevel;
        @BindView(R.id.tv_item_homepage_user_time)
        TextView tvItemHomepageUserTime;

        public HomePageUserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void display(int index) {
            HomeNewListData.DataBean data = userList.get(index);
            if (TextUtil.isEmpty(data.getHead_pic()) || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivUserAvatarIcon, 0);
            }

            if (isShowTime) {
                tvItemHomepageUserTime.setVisibility(View.VISIBLE);
                tvItemHomepageUserTime.setText(data.getLast_login_time());
            } else {
                tvItemHomepageUserTime.setVisibility(View.GONE);
            }

            if ("1".equals(data.getLogin_time_switch())) {
                tvItemHomepageUserTime.setText("隐身");
            }

            if (!data.getLat().equals("0.000000") && !data.getLng().equals("0.000000") && !"隐身".equals(data.getDistance())) {
                tvItemHomepageUserDistance.setText(data.getDistance());
                tvItemHomepageUserDistance.setVisibility(View.VISIBLE);
                ivItemHomepageUserDistance.setVisibility(View.GONE);
            } else {
                ivItemHomepageUserDistance.setVisibility(View.VISIBLE);
                tvItemHomepageUserDistance.setVisibility(View.GONE);
            }

            if (data.getOnlinestate() == 0) {
                ivUserAvatarOnline.setVisibility(View.GONE);
            } else {
                ivUserAvatarOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivUserAvatarLevel);


            //是否实名认证
            if ("1".equals(data.getRealids())) {
                ivLayoutUserNormalInfoAuthCard.setVisibility(View.VISIBLE);
            } else {
                ivLayoutUserNormalInfoAuthCard.setVisibility(View.GONE);
            }
            //是否自拍认证
            if ("1".equals(data.getRealname())) {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.VISIBLE);
            } else {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.GONE);
            }
            //短视频认证
//            if ("1".equals(data.getVideo_auth_status())) {
//                ivItemUserNormalInfoAuthVideo.setVisibility(View.VISIBLE);
//            } else {
//                ivItemUserNormalInfoAuthVideo.setVisibility(View.GONE);
//            }
            tvLayoutUserNormalInfoAge.setText(data.getAge());

            if (data.getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("2".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("3".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            }
            if ("S".equals(data.getRole())) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if ("M".equals(data.getRole())) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if ("SM".equals(data.getRole())) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                tvLayoutUserNormalInfoRole.setText("双");
            } else {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
                tvLayoutUserNormalInfoRole.setText(data.getRole());
            }
            tvLayoutUserNormalInfoName.setText(data.getNickname());

            if ("1".equals(data.getLocation_city_switch())) {
                //tvLayoutUserNormalInfoCity.setVisibility(View.INVISIBLE);
                //ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCity.setText("未知");
            } else {
                if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                    //tvLayoutUserNormalInfoCity.setVisibility(View.INVISIBLE);
                    //ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
                    tvLayoutUserNormalInfoCity.setText("未知");
                } else {
                    tvLayoutUserNormalInfoCity.setVisibility(View.VISIBLE);
                    //ivItemUserNormalInfoNoAddress.setVisibility(View.GONE);
                    if (data.getProvince().equals(data.getCity())) {
                        tvLayoutUserNormalInfoCity.setText(data.getCity());
                    } else {
                        tvLayoutUserNormalInfoCity.setText(data.getProvince() + " " + data.getCity());
                    }
                }
            }


            if (!"0".equals(data.getCharm_val())) {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText(data.getCharm_val());
            } else {
                llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            }

            if (!"0".equals(data.getWealth_val())) {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText(data.getWealth_val());
            } else {
                llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
            }

            if (!TextUtil.isEmpty(data.getAnchor_room_id()) && !"0".equals(data.getAnchor_room_id())) {
                if ("0".equals(data.getAnchor_is_live())) {
                    ivUserAvatarState.setVisibility(View.VISIBLE);
                    lottieUserAvatarState.setVisibility(View.INVISIBLE);
                    Glide.with(context).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
                } else {
                    ivUserAvatarState.setVisibility(View.INVISIBLE);
                    lottieUserAvatarState.setVisibility(View.VISIBLE);
                    lottieUserAvatarState.setImageAssetsFolder("images");
                    lottieUserAvatarState.setAnimation("user_living.json");
                    lottieUserAvatarState.setRepeatMode(LottieDrawable.RESTART);
                    lottieUserAvatarState.setRepeatCount(LottieDrawable.INFINITE);
                    lottieUserAvatarState.playAnimation();
                    Animation mAnimation = AnimationUtils.loadAnimation(context, R.anim.live_icon_scale);
                    ivUserAvatarIcon.setAnimation(mAnimation);
                    mAnimation.start();
                }
//            ConstraintSet constraintSet = new ConstraintSet();
//            constraintSet.constrainPercentWidth(R.id.iv_user_avatar_icon,0.6f);
//            constraintSet.constrainPercentHeight(R.id.iv_user_avatar_icon,0.6f);
//            constraintSet.applyTo(layoutUserAvatar);
            } else {
                ivUserAvatarState.setVisibility(View.INVISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
//            ConstraintSet constraintSet = new ConstraintSet();
//            constraintSet.clone(layoutUserAvatar);
//            constraintSet.constrainPercentWidth(R.id.iv_user_avatar_icon,0.75f);
//            constraintSet.constrainPercentHeight(R.id.iv_user_avatar_icon,0.75f);
//            constraintSet.applyTo(layoutUserAvatar);
            }


            if (TextUtil.isEmpty(data.getUser_level()) || "0".equals(data.getUser_level())) {
                constraintLayoutAudienceLevel.setVisibility(View.GONE);
            } else {
                constraintLayoutAudienceLevel.setVisibility(View.VISIBLE);
                tvLiveAudienceLevel.setText(data.getUser_level());
            }
            if (TextUtil.isEmpty(data.getAnchor_level()) || "0".equals(data.getAnchor_level())) {
                constraintLayoutAnchorLevel.setVisibility(View.GONE);
            } else {
                constraintLayoutAnchorLevel.setVisibility(View.VISIBLE);
                tvLiveAnchorLevel.setText(data.getAnchor_level());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(index);
                    }
                }
            });
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
