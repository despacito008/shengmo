package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.bean.LaudListData;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicThumbUpAdapter extends RecyclerView.Adapter<DynamicThumbUpAdapter.UserRelationShipHolder> {
    private Context context;
    private List<LaudListData.DataBean> userList;


    public DynamicThumbUpAdapter(Context context, List<LaudListData.DataBean> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public UserRelationShipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_user_relationship, parent, false);
        return new UserRelationShipHolder(view);
    }

    @Override
    public void onBindViewHolder(UserRelationShipHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserRelationShipHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.iv_item_user_relationship)
        ImageView ivItemUserRelationship;

        public UserRelationShipHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void display(int index) {
            LaudListData.DataBean user = userList.get(index);
            GlideImgManager.glideLoader(context, user.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivUserAvatarIcon, 0);
            tvLayoutUserNormalInfoAge.setText(user.getAge());
            if (user.getOnlinestate() == 0) {
                ivUserAvatarOnline.setVisibility(View.GONE);
            } else {
                ivUserAvatarOnline.setVisibility(View.VISIBLE);
            }
            UserIdentityUtils.showIdentity(context, user.getHead_pic(), user.getUid(), user.getIs_volunteer(), user.getIs_admin(), user.getSvipannual(), user.getSvip(), user.getVipannual(), user.getVip(), user.getBkvip(), user.getBlvip(), ivUserAvatarLevel);
            tvLayoutUserNormalInfoName.setText(user.getNickname());


            if ("0".equals(user.getRealname())) {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.GONE);
            } else {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.VISIBLE);
            }
            tvLayoutUserNormalInfoAge.setText(user.getAge());
            if (user.getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
            } else if (user.getSex().equals("2")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
            } else if (user.getSex().equals("3")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
            }
            if (user.getRole().equals("S")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if (user.getRole().equals("M")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if (user.getRole().equals("SM")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                tvLayoutUserNormalInfoRole.setText("双");
            } else if (user.getRole().equals("~")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                tvLayoutUserNormalInfoRole.setText("~");
            }
            if (!user.getCharm_val().equals("0")) {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText(user.getCharm_val());
            } else {
                llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            }

            if (!user.getWealth_val().equals("0")) {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText(user.getWealth_val());
            } else {
                llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
            }

            if ("1".equals(user.getLocation_city_switch())) {
                tvLayoutUserNormalInfoCity.setText("隐身");
            } else {
                if (TextUtil.isEmpty(user.getCity())) {
                    tvLayoutUserNormalInfoCity.setText("(用户已隐藏位置)");
                } else {
                    tvLayoutUserNormalInfoCity.setText(user.getCity());
                }
            }

            ivItemUserRelationship.setVisibility(View.VISIBLE);
            switch (user.getState()) {
                case 0:
                    ivItemUserRelationship.setImageResource(R.drawable.jiaguanzhu);
                    break;
                case 1:
                    ivItemUserRelationship.setImageResource(R.drawable.duigouquxiao);
                    break;
                case 2:
                    ivItemUserRelationship.setImageResource(R.drawable.beiguanzhu);
                    break;
                case 3:
                    ivItemUserRelationship.setImageResource(R.drawable.huxiangguanzhu);
                    break;
            }


            ivItemUserRelationship.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (user.getState()) {
                        case 0:
                        case 2:
                            //未关注 关注
                            followUser(index);
                            break;
                        case 1:
                        case 3:
                            //已关注 取消关注
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setMessage("您已经关注此人,确认取消关注吗?")
                                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    followUser(index);
                                }
                            }).create().show();
                            break;
                    }
                }
            });

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

    private void followUser(int index) {
        boolean isFollow = userList.get(index).getState() == 1 || userList.get(index).getState() == 3;
        HttpHelper.getInstance().followUser(!isFollow, userList.get(index).getUid(), new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(context, "操作成功");
                switch (userList.get(index).getState()) {
                    case 0:
                        userList.get(index).setState(1);
                        break;
                    case 1:
                        userList.get(index).setState(0);
                        break;
                    case 2:
                        userList.get(index).setState(3);
                        break;
                    case 3:
                        userList.get(index).setState(2);
                        break;
                }
                notifyItemChanged(index);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
