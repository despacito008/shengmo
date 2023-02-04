package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.RewardData;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class DynamicRewardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<RewardData.DataBean> list;
    Fragment fragment;

    public DynamicRewardAdapter(Context context, List<RewardData.DataBean> list, Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_dynamic_reward, parent, false);
        return new RewardHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RewardHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RewardHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.tv_layout_user_normal_info_city)
        TextView tvLayoutUserNormalInfoCity;
        @BindView(R.id.tv_item_dynamic_reward_time)
        TextView tvItemDynamicRewardTime;
        @BindView(R.id.iv_item_dynamic_reward_gift)
        ImageView ivItemDynamicRewardGift;
        @BindView(R.id.tv_item_dynamic_reward_num)
        TextView tvItemDynamicRewardNum;
        @BindView(R.id.tv_item_dynamic_reward_beans)
        TextView tvItemDynamicRewardBeans;

        public RewardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            RewardData.DataBean data = list.get(position);
            if (TextUtil.isEmpty(data.getHead_pic()) || NetPic().equals(data.getHead_pic())) {//"http://59.110.28.150:888/"
                ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                ImageLoader.loadCircleImage(context, data.getHead_pic(), ivUserAvatarIcon, R.mipmap.morentouxiang);
            }
            tvLayoutUserNormalInfoAge.setText(data.getAge());
            if (data.getOnlinestate() == 0) {
                ivUserAvatarOnline.setVisibility(View.GONE);
            } else {
                ivUserAvatarOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivUserAvatarLevel);

            if ("0".equals(data.getRealname())) {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.GONE);
            } else {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.VISIBLE);
            }

            if ("1".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nan_bg);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
            } else if ("2".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nv_bg);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
            } else if ("3".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_san_bg);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
            }
            if ("S".equals(data.getRole())) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if ("M".equals(data.getRole())) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if ("SM".equals(data.getRole())) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                tvLayoutUserNormalInfoRole.setText("双");
            } else {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                tvLayoutUserNormalInfoRole.setText(data.getRole());
            }
            tvLayoutUserNormalInfoName.setText(data.getNickname());
            if ("1".equals(data.getLocation_city_switch())) {
                tvLayoutUserNormalInfoCity.setText("隐身");
            } else {
                if (!TextUtil.isEmpty(data.getCity())) {
                    tvLayoutUserNormalInfoCity.setText(data.getCity());
                } else {
                    tvLayoutUserNormalInfoCity.setVisibility(View.GONE);
                }
            }
            tvItemDynamicRewardTime.setText(data.getSendtime());
            ImageLoader.loadImage(fragment,data.getGift_image(),ivItemDynamicRewardGift,R.mipmap.weizhiliwu);
            tvItemDynamicRewardNum.setText("X" + data.getNum());
            tvItemDynamicRewardBeans.setText(data.getSum() + "豆");

            llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            llLayoutUserNormalInfoWealth.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("uid",data.getUid());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final int pos = (int) v.getTag();
        switch (v.getId()) {
            case R.id.item_gzfshy_listview_flag:
                switch (list.get(pos).getState()) {
                    case 0:
                        follow(list.get(pos).getUid(), pos);
                        break;
                    case 1:
                        final AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                        builder3.setMessage("您已经关注此人,确认取消关注吗?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                overfollow(list.get(pos).getUid(), pos);
                            }
                        }).create().show();
                        break;
                    case 2:
                        follow(list.get(pos).getUid(), pos);
                        break;
                    case 3:
                        final AlertDialog.Builder builder4 = new AlertDialog.Builder(context);
                        builder4.setMessage("您已经关注此人,确认取消关注吗?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                overfollow(list.get(pos).getUid(), pos);
                            }
                        }).create().show();
                        break;
                }
                break;
            case R.id.item_gzfshy_listview_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_gzfshy_listview_shiming:
//                intent = new Intent(context, PhotoRzActivity.class);
//                context.startActivity(intent);
                break;
        }

    }

    private void follow(String uid, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            switch (list.get(position).getState()) {
                                case 0:
                                    list.get(position).setState(1);
                                    break;
                                case 2:
                                    list.get(position).setState(3);
                                    break;
                                case 3:
                                    list.get(position).setState(2);
                                    break;
                            }
                            notifyDataSetChanged();
                            ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                            break;
                        case 4001:
                        case 4002:
                        case 8881:
                        case 8882:
                        case 4787:
                            BindGuanzhuDialog.bindAlertDialog(context, obj.getString("msg"));
                            break;
                        case 5000:
                            ToastUtil.show(context, obj.getString("msg") + "");
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void overfollow(String uid, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.OverFollow, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                            switch (list.get(position).getState()) {
                                case 0:
                                    list.get(position).setState(1);
                                    break;
                                case 1:
                                    list.get(position).setState(0);
                                    break;
                                case 2:
                                    list.get(position).setState(3);
                                    break;
                                case 3:
                                    list.get(position).setState(2);
                                    break;
                            }
                            notifyDataSetChanged();
//                                    EventBus.getDefault().post("refresh");
                            break;
                        case 4000:
                        case 4001:
                            ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
