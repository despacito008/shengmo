package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
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
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.bean.MemberData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class MemberListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<MemberData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    private Handler handler = new Handler();
    private String leaderLet = "1";

    public MemberListviewAdapter(Context context, List<MemberData.DataBean> list, int retcode) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
        inflater = LayoutInflater.from(context);
    }

    public MemberListviewAdapter(Context context, List<MemberData.DataBean> list, int retcode, String leaderLet) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
        this.leaderLet = leaderLet;
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
            convertView = inflater.inflate(R.layout.item_group_member, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MemberData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.ivUserAvatarIcon, 0);
        }
//        holder.itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
        if (retcode == 2000) {
            holder.itemNearListviewDistance.setVisibility(View.VISIBLE);
            holder.itemNearListviewDistance.setText(data.getDistance() + "km");
            if ("0.000000".equals(data.getLat()) || "0.000000".equals(data.getLng())) {
                holder.itemNearListviewDistance.setVisibility(View.GONE);
                holder.ivItemNearDistance.setVisibility(View.VISIBLE);
            } else {
                holder.itemNearListviewDistance.setVisibility(View.VISIBLE);
                holder.ivItemNearDistance.setVisibility(View.GONE);
            }
        } else {
            holder.itemNearListviewDistance.setVisibility(View.GONE);
            holder.ivItemNearDistance.setVisibility(View.VISIBLE);
        }
        //距离显示逻辑
        if (!"1".equals(data.getLocation_switch())) {
            holder.itemNearListviewDistance.setVisibility(View.VISIBLE);
            holder.itemNearListviewDistance.setText(data.getDistance() + "km");
            holder.ivItemNearDistance.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewDistance.setVisibility(View.GONE);
            holder.ivItemNearDistance.setVisibility(View.VISIBLE);
        }

        holder.itemNearListviewSex.setText(data.getAge());
        if (data.getState().equals("1")) {
            holder.itemNearListviewFlag.setVisibility(View.GONE);
        } else if (data.getState().equals("2")) {
            holder.itemNearListviewFlag.setVisibility(View.VISIBLE);
            holder.itemNearListviewFlag.setText("管理员");
            holder.itemNearListviewFlag.setBackgroundResource(R.drawable.item_member_flag_lan);
        } else if (data.getState().equals("3")) {
            holder.itemNearListviewFlag.setVisibility(View.VISIBLE);
            holder.itemNearListviewFlag.setText("群主" + ("1".equals(leaderLet) ? "" : leaderLet));
            holder.itemNearListviewFlag.setBackgroundResource(R.drawable.item_member_flag_huang);
        }
        if (data.getOnlinestate() == 0) {
            holder.ivUserAvatarOnline.setVisibility(View.GONE);
        } else {
            holder.ivUserAvatarOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.ivUserAvatarLevel);

//        if (data.getIs_hand().equals("0")) {
//            holder.itemNearListviewHongniang.setVisibility(View.INVISIBLE);
//        } else {
//            holder.itemNearListviewHongniang.setVisibility(View.VISIBLE);
//        }
        if (data.getRealname().equals("0")) {
            holder.itemNearListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewShiming.setVisibility(View.VISIBLE);
        }
        if ("1".equals(data.getRealids())) {
            holder.itemNearListviewIdCard.setVisibility(View.VISIBLE);
        } else {
            holder.itemNearListviewIdCard.setVisibility(View.GONE);
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
            holder.llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            holder.ivItemSex.setImageResource(R.mipmap.nan);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("2".equals(data.getSex())) {
            holder.llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            holder.ivItemSex.setImageResource(R.mipmap.nv);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("3".equals(data.getSex())) {
            holder.llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            holder.ivItemSex.setImageResource(R.mipmap.san);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
        }

        if (data.getRole().equals("S")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            holder.itemNearListviewRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            holder.itemNearListviewRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            holder.itemNearListviewRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_orientation_null);
            holder.itemNearListviewRole.setText("~");
        } else {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.bg_user_info_orientation_null);
            holder.itemNearListviewRole.setText("-");
        }
        if (data.getGagstate().equals("1")) {
            holder.itemNearListviewBanSomeOne.setVisibility(View.VISIBLE);
        } else {
            holder.itemNearListviewBanSomeOne.setVisibility(View.GONE);
        }
        String cardname = data.getCardname();
        if (!"".equals(cardname) && null != cardname) {
            holder.itemNearListviewName.setText(cardname);
        } else {
            if (!"".equals(data.getMarkname()) && null != data.getMarkname()) {
                holder.itemNearListviewName.setText(data.getMarkname());
            } else {
                holder.itemNearListviewName.setText(data.getNickname());
            }

        }
        if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
            //holder.itemNearListviewSign.setText("(用户已隐藏位置)");
            holder.ivItemNearLocation.setVisibility(View.VISIBLE);
            holder.itemNearListviewSign.setVisibility(View.GONE);
        } else {
            holder.ivItemNearLocation.setVisibility(View.GONE);
            holder.itemNearListviewSign.setVisibility(View.VISIBLE);
            if (data.getProvince().equals(data.getCity())) {
                holder.itemNearListviewSign.setText(data.getCity());
            } else {
                holder.itemNearListviewSign.setText(data.getProvince() + " " + data.getCity());
            }
        }
        //所在城市显示逻辑
        if (!"1".equals(data.getLocation_city_switch())) {
            holder.itemNearListviewSign.setVisibility(View.VISIBLE);
            if (data.getProvince().equals(data.getCity())) {
                holder.itemNearListviewSign.setText(data.getCity());
            } else {
                holder.itemNearListviewSign.setText(data.getProvince() + " " + data.getCity());
            }
            holder.ivItemNearLocation.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewSign.setVisibility(View.GONE);
            holder.ivItemNearLocation.setVisibility(View.VISIBLE);
        }
        holder.itemNearListviewTime.setText(data.getLast_login_time());
        if (!data.getCharm_val().equals("0")) {
            holder.itemNearListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemNearListviewBeautyCount.setText(data.getCharm_val());
        } else {
            holder.itemNearListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!data.getWealth_val().equals("0")) {
            holder.itemNearListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemNearListviewRichCount.setText(data.getWealth_val());
        } else {
            holder.itemNearListviewLlRichCount.setVisibility(View.GONE);
        }

        if (!TextUtil.isEmpty(data.getAnchor_room_id()) && !"0".equals(data.getAnchor_room_id())) {
            if ("0".equals(data.getAnchor_is_live())) {
                holder.ivUserAvatarState.setVisibility(View.VISIBLE);
                holder.lottieUserAvatarState.setVisibility(View.INVISIBLE);
                Glide.with(context).load(R.drawable.ic_user_liver).into(holder.ivUserAvatarState);
            } else {
                holder.ivUserAvatarState.setVisibility(View.INVISIBLE);
                holder.lottieUserAvatarState.setVisibility(View.VISIBLE);
                holder.lottieUserAvatarState.setImageAssetsFolder("images");
                holder.lottieUserAvatarState.setAnimation("user_living.json");
                holder.lottieUserAvatarState.setRepeatMode(LottieDrawable.RESTART);
                holder.lottieUserAvatarState.setRepeatCount(LottieDrawable.INFINITE);
                holder.lottieUserAvatarState.playAnimation();
                Animation mAnimation = AnimationUtils.loadAnimation(context, R.anim.live_icon_scale);
                holder.ivUserAvatarIcon.setAnimation(mAnimation);
                mAnimation.start();
            }
//            ConstraintSet constraintSet = new ConstraintSet();
//            constraintSet.constrainPercentWidth(R.id.iv_user_avatar_icon,0.6f);
//            constraintSet.constrainPercentHeight(R.id.iv_user_avatar_icon,0.6f);
//            constraintSet.applyTo(holder.layoutUserAvatar);
        } else {
            holder.ivUserAvatarState.setVisibility(View.INVISIBLE);
            holder.lottieUserAvatarState.setVisibility(View.INVISIBLE);
        }

        holder.itemNearListviewShiming.setTag(position);
        holder.itemNearListviewShiming.setOnClickListener(this);
        return convertView;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.item_near_listview_shiming:
                intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
                break;
        }
    }


    static
    class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


