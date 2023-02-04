package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
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
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserGroupingShipAdapter extends RecyclerView.Adapter<UserGroupingShipAdapter.UserRelationShipHolder> {
    private Context context;
    private List<GzFsHyListviewData.DataBean> userList;
    private String keyWord;
    private boolean isSelf;

    private ArrayList<String> memberList;
    private ArrayList<String> selectList;

    public UserGroupingShipAdapter(Context context, List<GzFsHyListviewData.DataBean> userList, String keyWord,ArrayList<String> memberList,ArrayList<String> selectList) {
        this.context = context;
        this.userList = userList;
        this.keyWord = keyWord;
        this.memberList = memberList;
        this.selectList = selectList;
    }

    @Override
    public UserRelationShipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_user_grouping_ship, parent, false);
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
        @BindView(R.id.iv_item_group_ing_state)
        ImageView ivItemGroupIngState;

        public UserRelationShipHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void display(int index) {
            GzFsHyListviewData.DataBean user = userList.get(index);
            GlideImgManager.glideLoader(context, user.getUserInfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivUserAvatarIcon, 0);
            tvLayoutUserNormalInfoAge.setText(user.getUserInfo().getAge());
            if (user.getUserInfo().getOnlinestate() == 0) {
                ivUserAvatarOnline.setVisibility(View.GONE);
            } else {
                ivUserAvatarOnline.setVisibility(View.VISIBLE);
            }
            UserIdentityUtils.showIdentity(context, user.getUserInfo().getHead_pic(), user.getUid(), user.getUserInfo().getIs_volunteer(), user.getUserInfo().getIs_admin(), user.getUserInfo().getSvipannual(), user.getUserInfo().getSvip(), user.getUserInfo().getVipannual(), user.getUserInfo().getVip(), user.getUserInfo().getBkvip(), user.getUserInfo().getBlvip(), ivUserAvatarLevel);
            if (!TextUtil.isEmpty(user.getUserInfo().getAnchor_room_id()) && !"0".equals(user.getUserInfo().getAnchor_room_id())) {
                ivUserAvatarState.setVisibility(View.VISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
                if ("0".equals(user.getUserInfo().getAnchor_is_live())) {
                    Glide.with(context).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
                } else {
                    lottieUserAvatarState.setVisibility(View.VISIBLE);
                    lottieUserAvatarState.setImageAssetsFolder("images");
                    lottieUserAvatarState.setAnimation("user_living.json");
                    lottieUserAvatarState.loop(true);
                    lottieUserAvatarState.playAnimation();
                }
            } else {
                ivUserAvatarState.setVisibility(View.INVISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
            }
            if (TextUtil.isEmpty(keyWord)) {
                if (TextUtil.isEmpty(user.getUserInfo().getMarkname())) {
                    tvLayoutUserNormalInfoName.setText(user.getUserInfo().getNickname());
                } else {
                    tvLayoutUserNormalInfoName.setText(user.getUserInfo().getMarkname());
                }
            } else {
                String nameString = "";
                if (TextUtil.isEmpty(user.getUserInfo().getMarkname())) {
                    nameString = user.getUserInfo().getNickname();
                } else {
                    nameString = user.getUserInfo().getMarkname();
                }
                int i = nameString.indexOf(keyWord);
                SpannableStringBuilder builder = new SpannableStringBuilder(nameString);
                if (i != -1) {
                    ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#c450d6"));
                    builder.setSpan(purSpan, i, i + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvLayoutUserNormalInfoName.setText(builder);
            }
            if ("0".equals(user.getUserInfo().getRealname())) {
                ivLayoutUserNormalInfoAuthCard.setVisibility(View.GONE);
            } else {
                ivLayoutUserNormalInfoAuthCard.setVisibility(View.VISIBLE);
            }
            tvLayoutUserNormalInfoAge.setText(user.getUserInfo().getAge());
            if (user.getUserInfo().getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
            } else if (user.getUserInfo().getSex().equals("2")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
            } else if (user.getUserInfo().getSex().equals("3")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
            }
            if (user.getUserInfo().getRole().equals("S")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if (user.getUserInfo().getRole().equals("M")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if (user.getUserInfo().getRole().equals("SM")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                tvLayoutUserNormalInfoRole.setText("双");
            } else if (user.getUserInfo().getRole().equals("~")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                tvLayoutUserNormalInfoRole.setText("~");
            }
            if (!user.getUserInfo().getCharm_val().equals("0")) {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText(user.getUserInfo().getCharm_val());
            } else {
                llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            }

            if (!user.getUserInfo().getWealth_val().equals("0")) {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText(user.getUserInfo().getWealth_val());
            } else {
                llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
            }

            if ("1".equals(user.getUserInfo().getLocation_city_switch())) {
                tvLayoutUserNormalInfoCity.setText("隐身");
            } else {
                if (TextUtil.isEmpty(user.getUserInfo().getCity()) && TextUtil.isEmpty(user.getUserInfo().getProvince())) {
                    tvLayoutUserNormalInfoCity.setText("(用户已隐藏位置)");
                } else {
                    if (user.getUserInfo().getProvince().equals(user.getUserInfo().getCity())) {
                        tvLayoutUserNormalInfoCity.setText(user.getUserInfo().getCity());
                    } else {
                        tvLayoutUserNormalInfoCity.setText(user.getUserInfo().getProvince() + " " + user.getUserInfo().getCity());
                    }
                }
            }

            if (memberList.contains(user.getUid())) {
                itemView.setBackgroundResource(R.drawable.item_click_bg_selector2);
                itemView.setClickable(false);
            } else {
                itemView.setBackgroundResource(R.drawable.item_click_bg_selector);
                itemView.setClickable(true);
            }

            if (selectList.contains(user.getUid())) {
                ivItemGroupIngState.setImageResource(R.mipmap.atxuanzhong);
            } else {
                ivItemGroupIngState.setImageResource(R.mipmap.atweixuanzhong);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!memberList.contains(user.getUid())) {
                        if (onSimpleItemListener != null) {
                            onSimpleItemListener.onItemListener(index);
                        }
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
                ToastUtil.show(context,"操作成功");
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
                ToastUtil.show(context,msg);
            }
        });
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
