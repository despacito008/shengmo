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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class NormalUserAdapter extends RecyclerView.Adapter<NormalUserAdapter.NormalUserHolder> {
    Context context;
    List<HomeNewListData.DataBean> userList;
    private int isVisivleTime;
    public NormalUserAdapter(Context context, List<HomeNewListData.DataBean> list) {
        this.context = context;
        this.userList = list;
    }

    @Override
    public NormalUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_user_normal_info, parent, false);
        return new NormalUserHolder(view);
    }

    @Override
    public void onBindViewHolder(NormalUserHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class NormalUserHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.tv_item_user_normal_info_name)
        TextView tvItemUserNormalInfoName;
        @BindView(R.id.iv_item_user_normal_info_auth_photo)
        ImageView ivItemUserNormalInfoAuthPhoto;
        @BindView(R.id.iv_item_user_normal_info_auth_idCard)
        ImageView ivItemUserNormalInfoAuthIdCard;
        @BindView(R.id.iv_item_user_normal_info_auth_Video)
        ImageView ivItemUserNormalInfoAuthVideo;
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
        @BindView(R.id.tv_item_user_normal_info_address)
        TextView tvItemUserNormalInfoAddress;
        @BindView(R.id.iv_item_user_normal_info_noAddress)
        ImageView ivItemUserNormalInfoNoAddress;
        @BindView(R.id.tv_item_user_normal_info_distance)
        TextView tvItemUserNormalInfoDistance;
        @BindView(R.id.iv_item_user_normal_info_noDistance)
        ImageView ivItemUserNormalInfoNoDistance;
        @BindView(R.id.tv_item_user_normal_info_time)
        TextView tvItemUserNormalInfoTime;
        @BindView(R.id.iv_item_user_normal_info_noTime)
        ImageView ivItemUserNormalInfoNoTime;
        public NormalUserHolder(View itemView) {
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

            if (isVisivleTime != 0) {
                if ("隐身".equals(data.getLast_login_time())) {
                    ivItemUserNormalInfoNoTime.setVisibility(View.VISIBLE);
                    tvItemUserNormalInfoTime.setVisibility(View.GONE);
                } else {
                    ivItemUserNormalInfoNoTime.setVisibility(View.GONE);
                    tvItemUserNormalInfoTime.setVisibility(View.VISIBLE);
                    tvItemUserNormalInfoTime.setText(data.getLast_login_time());
                }

            } else {
                ivItemUserNormalInfoNoTime.setVisibility(View.GONE);
                tvItemUserNormalInfoTime.setVisibility(View.GONE);
            }

            if ("1".equals(data.getLogin_time_switch())) {
                tvItemUserNormalInfoTime.setText("隐身");
            }

            if (!data.getLat().equals("0.000000") && !data.getLng().equals("0.000000") && !"隐身".equals(data.getDistance())) {
                tvItemUserNormalInfoDistance.setText(data.getDistance());
                tvItemUserNormalInfoDistance.setVisibility(View.VISIBLE);
                ivItemUserNormalInfoNoDistance.setVisibility(View.GONE);
            } else {
                ivItemUserNormalInfoNoDistance.setVisibility(View.VISIBLE);
                tvItemUserNormalInfoDistance.setVisibility(View.GONE);
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
                ivItemUserNormalInfoAuthIdCard.setVisibility(View.VISIBLE);
            } else {
                ivItemUserNormalInfoAuthIdCard.setVisibility(View.GONE);
            }
            //是否自拍认证
            if ("1".equals(data.getRealname())) {
                ivItemUserNormalInfoAuthPhoto.setVisibility(View.VISIBLE);
            } else {
                ivItemUserNormalInfoAuthPhoto.setVisibility(View.GONE);
            }
            //短视频认证
            if ("1".equals(data.getVideo_auth_status())) {
                ivItemUserNormalInfoAuthVideo.setVisibility(View.VISIBLE);
            } else {
                ivItemUserNormalInfoAuthVideo.setVisibility(View.GONE);
            }
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
            tvItemUserNormalInfoName.setText(data.getNickname());

            if ("1".equals(data.getLocation_city_switch())) {
                tvItemUserNormalInfoAddress.setVisibility(View.GONE);
                ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
            } else {
                if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                    tvItemUserNormalInfoAddress.setVisibility(View.GONE);
                    ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
                } else {
                    tvItemUserNormalInfoAddress.setVisibility(View.VISIBLE);
                    ivItemUserNormalInfoNoAddress.setVisibility(View.GONE);
                    if (data.getProvince().equals(data.getCity())) {
                        tvItemUserNormalInfoAddress.setText(data.getCity());
                    } else {
                        tvItemUserNormalInfoAddress.setText(data.getProvince() + " " + data.getCity());
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
                constraintLayoutAudienceLevel.setVisibility(View.GONE);
            } else {
                constraintLayoutAudienceLevel.setVisibility(View.VISIBLE);
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
