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
import com.aiwujie.shengmo.bean.MemberData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;
import com.shizhefei.view.indicator.RecyclerIndicatorView;
import com.zhy.autolayout.AutoLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.GroupMemberHolder> {
    private Context context;
    private List<MemberData.DataBean> memberList;
    private int retcode;

    public GroupMemberListAdapter(Context context, List<MemberData.DataBean> list, int retcode) {
        this.context = context;
        this.memberList = list;
        this.retcode = retcode;
    }

    @Override
    public GroupMemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_member,parent,false);
        return new GroupMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupMemberHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }


    class GroupMemberHolder extends RecyclerIndicatorView.ViewHolder {
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
        @BindView(R.id.item_near_listview_flag)
        TextView itemNearListviewFlag;
        @BindView(R.id.item_near_listview_name)
        TextView itemNearListviewName;
        @BindView(R.id.item_near_listview_shiming)
        ImageView itemNearListviewShiming;
        @BindView(R.id.item_near_listview_id_card)
        ImageView itemNearListviewIdCard;
        @BindView(R.id.item_near_listview_distance)
        TextView itemNearListviewDistance;
        @BindView(R.id.item_near_listview_time)
        TextView itemNearListviewTime;
        @BindView(R.id.iv_item_near_distance)
        ImageView ivItemNearDistance;
        @BindView(R.id.iv_item_sex)
        ImageView ivItemSex;
        @BindView(R.id.item_near_listview_Sex)
        TextView itemNearListviewSex;
        @BindView(R.id.ll_item_sex_and_age)
        LinearLayout llItemSexAndAge;
        @BindView(R.id.item_near_listview_role)
        TextView itemNearListviewRole;
        @BindView(R.id.item_near_listview_richCount)
        TextView itemNearListviewRichCount;
        @BindView(R.id.item_near_listview_ll_richCount)
        AutoLinearLayout itemNearListviewLlRichCount;
        @BindView(R.id.item_near_listview_beautyCount)
        TextView itemNearListviewBeautyCount;
        @BindView(R.id.item_near_listview_ll_beautyCount)
        AutoLinearLayout itemNearListviewLlBeautyCount;
        @BindView(R.id.item_near_listview_banSomeOne)
        ImageView itemNearListviewBanSomeOne;
        @BindView(R.id.item_near_listview_sign)
        TextView itemNearListviewSign;
        @BindView(R.id.iv_item_near_location)
        ImageView ivItemNearLocation;
        public GroupMemberHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void display(int index) {
            MemberData.DataBean data = memberList.get(index);
            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivUserAvatarIcon, 0);
            }
//        itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
            if (retcode == 2000) {
                itemNearListviewDistance.setVisibility(View.VISIBLE);
                itemNearListviewDistance.setText(data.getDistance() + "km");
                if ("0.000000".equals(data.getLat()) || "0.000000".equals(data.getLng())) {
                    itemNearListviewDistance.setVisibility(View.GONE);
                    ivItemNearDistance.setVisibility(View.VISIBLE);
                } else {
                    itemNearListviewDistance.setVisibility(View.VISIBLE);
                    ivItemNearDistance.setVisibility(View.GONE);
                }
            } else {
                itemNearListviewDistance.setVisibility(View.GONE);
                ivItemNearDistance.setVisibility(View.VISIBLE);
            }
            //距离显示逻辑
            if (!"1".equals(data.getLocation_switch())) {
                itemNearListviewDistance.setVisibility(View.VISIBLE);
                itemNearListviewDistance.setText(data.getDistance() + "km");
                ivItemNearDistance.setVisibility(View.GONE);
            } else {
                itemNearListviewDistance.setVisibility(View.GONE);
                ivItemNearDistance.setVisibility(View.VISIBLE);
            }

            itemNearListviewSex.setText(data.getAge());
            if (data.getState().equals("1")) {
                itemNearListviewFlag.setVisibility(View.GONE);
            } else if (data.getState().equals("2")) {
                itemNearListviewFlag.setVisibility(View.VISIBLE);
                itemNearListviewFlag.setText("管理员");
                itemNearListviewFlag.setBackgroundResource(R.drawable.item_member_flag_lan);
            } else if (data.getState().equals("3")) {
                itemNearListviewFlag.setVisibility(View.VISIBLE);
                itemNearListviewFlag.setText("群主");
                itemNearListviewFlag.setBackgroundResource(R.drawable.item_member_flag_huang);
            }
            if (data.getOnlinestate() == 0) {
                ivUserAvatarOnline.setVisibility(View.GONE);
            } else {
                ivUserAvatarOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivUserAvatarLevel);

//        if (data.getIs_hand().equals("0")) {
//            itemNearListviewHongniang.setVisibility(View.INVISIBLE);
//        } else {
//            itemNearListviewHongniang.setVisibility(View.VISIBLE);
//        }
            if (data.getRealname().equals("0")) {
                itemNearListviewShiming.setVisibility(View.GONE);
            } else {
                itemNearListviewShiming.setVisibility(View.VISIBLE);
            }
            if ("1".equals(data.getRealids())) {
                itemNearListviewIdCard.setVisibility(View.VISIBLE);
            } else {
                itemNearListviewIdCard.setVisibility(View.GONE);
            }

//        if (data.getSex().equals("1")) {
//            holder.itemNearListviewSex.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
//        } else if (data.getSex().equals("2")) {
//            holder.itemNearListviewSex.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
//        } else if (data.getSex().equals("3")) {
//            holder.itemNearListviewSex.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
//        }

            if (data.getSex().equals("1")) {
                llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivItemSex.setImageResource(R.mipmap.nan);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("2".equals(data.getSex())) {
                llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivItemSex.setImageResource(R.mipmap.nv);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("3".equals(data.getSex())) {
                llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivItemSex.setImageResource(R.mipmap.san);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
            }

            if (data.getRole().equals("S")) {
                itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                itemNearListviewRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                itemNearListviewRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                itemNearListviewRole.setText("双");
            } else if (data.getRole().equals("~")) {
                itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_orientation_null);
                itemNearListviewRole.setText("~");
            } else {
                itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_orientation_null);
                itemNearListviewRole.setText("-");
            }
            if (data.getGagstate().equals("1")) {
                itemNearListviewBanSomeOne.setVisibility(View.VISIBLE);
            } else {
                itemNearListviewBanSomeOne.setVisibility(View.GONE);
            }
            String cardname = data.getCardname();
            if (!"".equals(cardname) && null != cardname) {
                itemNearListviewName.setText(cardname);
            } else {
                if (!"".equals(data.getMarkname()) && null != data.getMarkname()) {
                    itemNearListviewName.setText(data.getMarkname());
                } else {
                    itemNearListviewName.setText(data.getNickname());
                }

            }
            if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                //itemNearListviewSign.setText("(用户已隐藏位置)");
                ivItemNearLocation.setVisibility(View.VISIBLE);
                itemNearListviewSign.setVisibility(View.GONE);
            } else {
                ivItemNearLocation.setVisibility(View.GONE);
                itemNearListviewSign.setVisibility(View.VISIBLE);
                if (data.getProvince().equals(data.getCity())) {
                    itemNearListviewSign.setText(data.getCity());
                } else {
                    itemNearListviewSign.setText(data.getProvince() + " " + data.getCity());
                }
            }
            //所在城市显示逻辑
            if (!"1".equals(data.getLocation_city_switch())) {
                itemNearListviewSign.setVisibility(View.VISIBLE);
                if (data.getProvince().equals(data.getCity())) {
                    itemNearListviewSign.setText(data.getCity());
                } else {
                    itemNearListviewSign.setText(data.getProvince() + " " + data.getCity());
                }
                ivItemNearLocation.setVisibility(View.GONE);
            } else {
                itemNearListviewSign.setVisibility(View.GONE);
                ivItemNearLocation.setVisibility(View.VISIBLE);
            }
            itemNearListviewTime.setText(data.getLast_login_time());
            if (!data.getCharm_val().equals("0")) {
                itemNearListviewLlBeautyCount.setVisibility(View.VISIBLE);
                itemNearListviewBeautyCount.setText(data.getCharm_val());
            } else {
                itemNearListviewLlBeautyCount.setVisibility(View.GONE);
            }

            if (!data.getWealth_val().equals("0")) {
                itemNearListviewLlRichCount.setVisibility(View.VISIBLE);
                itemNearListviewRichCount.setText(data.getWealth_val());
            } else {
                itemNearListviewLlRichCount.setVisibility(View.GONE);
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
//            constraintSet.applyTo(holder.layoutUserAvatar);
            } else {
                ivUserAvatarState.setVisibility(View.INVISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onGroupMemberListener != null) {
                        onGroupMemberListener.doItemClick(index);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onGroupMemberListener != null) {
                        onGroupMemberListener.doItemLongClick(index);
                    }
                    return true;
                }
            });
        }
    }

    public interface OnGroupMemberListener {
        void doItemClick(int index);
        void doItemLongClick(int index);
    }

    OnGroupMemberListener onGroupMemberListener;

    public void setOnGroupMemberListener(OnGroupMemberListener onGroupMemberListener) {
        this.onGroupMemberListener = onGroupMemberListener;
    }
}
