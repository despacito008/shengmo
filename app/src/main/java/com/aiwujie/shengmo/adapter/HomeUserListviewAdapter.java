package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class HomeUserListviewAdapter extends BaseAdapter {
    private Context context;
    private List<HomeNewListData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    //isVisivleTime为0的时候不显示右上角时间
    private int isVisivleTime;
    private Handler handler = new Handler();

    public HomeUserListviewAdapter(Context context, List<HomeNewListData.DataBean> list, int retcode, int isVisivleTime) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
        this.isVisivleTime = isVisivleTime;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item_user_normal_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeNewListData.DataBean data = list.get(position);
        if (TextUtil.isEmpty(data.getHead_pic()) || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.ivUserAvatarIcon, 0);
        }

        if (isVisivleTime != 0) {
            if ("隐身".equals(data.getLast_login_time())) {
                holder.ivItemUserNormalInfoNoTime.setVisibility(View.VISIBLE);
                holder.tvItemUserNormalInfoTime.setVisibility(View.GONE);
            } else {
                holder.ivItemUserNormalInfoNoTime.setVisibility(View.GONE);
                holder.tvItemUserNormalInfoTime.setVisibility(View.VISIBLE);
                holder.tvItemUserNormalInfoTime.setText(data.getLast_login_time());
            }

        } else {
            holder.ivItemUserNormalInfoNoTime.setVisibility(View.GONE);
            holder.tvItemUserNormalInfoTime.setVisibility(View.GONE);
        }

        if ("1".equals(data.getLogin_time_switch())) {
            holder.tvItemUserNormalInfoTime.setText("隐身");
        }

        if (retcode == 2000 && !data.getLat().equals("0.000000") && !data.getLng().equals("0.000000") && !"隐身".equals(data.getDistance())) {
            holder.tvItemUserNormalInfoDistance.setText(data.getDistance());
            holder.tvItemUserNormalInfoDistance.setVisibility(View.VISIBLE);
            holder.ivItemUserNormalInfoNoDistance.setVisibility(View.GONE);
        } else {
            holder.ivItemUserNormalInfoNoDistance.setVisibility(View.VISIBLE);
            holder.tvItemUserNormalInfoDistance.setVisibility(View.GONE);
        }

        if (data.getOnlinestate() == 0) {
            holder.ivUserAvatarOnline.setVisibility(View.GONE);
        } else {
            holder.ivUserAvatarOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.ivUserAvatarLevel);


        //是否实名认证
        if ("1".equals(data.getRealids())) {
            holder.ivItemUserNormalInfoAuthIdCard.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemUserNormalInfoAuthIdCard.setVisibility(View.GONE);
        }
        //是否自拍认证
        if ("1".equals(data.getRealname())) {
            holder.ivItemUserNormalInfoAuthPhoto.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemUserNormalInfoAuthPhoto.setVisibility(View.GONE);
        }
        //短视频认证
        if ("1".equals(data.getVideo_auth_status())) {
            holder.ivItemUserNormalInfoAuthVideo.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemUserNormalInfoAuthVideo.setVisibility(View.GONE);
        }
        holder.tvLayoutUserNormalInfoAge.setText(data.getAge());

        switch (data.getSex()) {
            case "1":
                holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
                break;
            case "2":
                holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
                break;
            case "3":
                holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
                break;
        }
        if ("S".equals(data.getRole())) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            holder.tvLayoutUserNormalInfoRole.setText("斯");
        } else if ("M".equals(data.getRole())) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            holder.tvLayoutUserNormalInfoRole.setText("慕");
        } else if ("SM".equals(data.getRole())) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            holder.tvLayoutUserNormalInfoRole.setText("双");
        } else {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
            holder.tvLayoutUserNormalInfoRole.setText(data.getRole());
        }
        holder.tvItemUserNormalInfoName.setText(data.getNickname());

        if ("1".equals(data.getLocation_city_switch())) {
            holder.tvItemUserNormalInfoAddress.setVisibility(View.GONE);
            holder.ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
        } else {
            if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                holder.tvItemUserNormalInfoAddress.setVisibility(View.GONE);
                holder.ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
            } else {
                holder.tvItemUserNormalInfoAddress.setVisibility(View.VISIBLE);
                holder.ivItemUserNormalInfoNoAddress.setVisibility(View.GONE);
                if (data.getProvince().equals(data.getCity())) {
                    holder.tvItemUserNormalInfoAddress.setText(data.getCity());
                } else {
                    holder.tvItemUserNormalInfoAddress.setText(data.getProvince() + " " + data.getCity());
                }
            }
        }


        if (!"0".equals(data.getCharm_val())) {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoCharm.setText(data.getCharm_val());
        } else {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
        }

        if (!"0".equals(data.getWealth_val())) {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoWealth.setText(data.getWealth_val());
        } else {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
        }

        if (!TextUtil.isEmpty(data.getAnchor_room_id()) && !"0".equals(data.getAnchor_room_id())) {
            if ("0".equals(data.getAnchor_is_live())) {
                holder.ivUserAvatarState.setVisibility(View.VISIBLE);
                holder.lottieAnimationView.setVisibility(View.INVISIBLE);
                Glide.with(context).load(R.drawable.ic_user_liver).into(holder.ivUserAvatarState);
            } else {
                holder.ivUserAvatarState.setVisibility(View.INVISIBLE);
                holder.lottieAnimationView.setVisibility(View.VISIBLE);
                holder.lottieAnimationView.setImageAssetsFolder("images");
                holder.lottieAnimationView.setAnimation("user_living.json");
                holder.lottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
                holder.lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                holder.lottieAnimationView.playAnimation();
                Animation mAnimation = AnimationUtils.loadAnimation(context, R.anim.live_icon_scale);
                holder.ivUserAvatarIcon.setAnimation(mAnimation);
                mAnimation.start();
            }
        } else {
            holder.ivUserAvatarState.setVisibility(View.INVISIBLE);
            holder.lottieAnimationView.setVisibility(View.INVISIBLE);
        }



        if (TextUtil.isEmpty(data.getUser_level()) || "0".equals(data.getUser_level())) {
            holder.clAudienceLevel.setVisibility(View.GONE);
        } else {
            holder.clAudienceLevel.setVisibility(View.VISIBLE);
            holder.tvAudienceLevel.setText(data.getUser_level());
        }
        if (TextUtil.isEmpty(data.getAnchor_level()) || "0".equals(data.getAnchor_level())) {
            holder.clAnchorLevel.setVisibility(View.GONE);
        } else {
            holder.clAnchorLevel.setVisibility(View.VISIBLE);
            holder.tvAnchorLevel.setText(data.getAnchor_level());
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_user_avatar_state)
        ImageView ivUserAvatarState;
        @BindView(R.id.lottie_user_avatar_state)
        LottieAnimationView lottieAnimationView;
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
        @BindView(R.id.tv_item_user_normal_info_distance)
        TextView tvItemUserNormalInfoDistance;
        @BindView(R.id.iv_item_user_normal_info_noDistance)
        ImageView ivItemUserNormalInfoNoDistance;
        @BindView(R.id.tv_item_user_normal_info_time)
        TextView tvItemUserNormalInfoTime;
        @BindView(R.id.iv_item_user_normal_info_noTime)
        ImageView ivItemUserNormalInfoNoTime;
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
        @BindView(R.id.constraint_layout_anchor_level)
        ConstraintLayout clAnchorLevel;
        @BindView(R.id.tv_live_anchor_level)
        TextView tvAnchorLevel;
        @BindView(R.id.constraint_layout_audience_level)
        ConstraintLayout clAudienceLevel;
        @BindView(R.id.tv_live_audience_level)
        TextView tvAudienceLevel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


