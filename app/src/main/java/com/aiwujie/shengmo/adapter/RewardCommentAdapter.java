package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.RewardData;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

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

public class RewardCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<RewardData.DataBean> list;
    Fragment fragment;

    public RewardCommentAdapter(Context context, List<RewardData.DataBean> list,Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reward_listview, parent, false);
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
        @BindView(R.id.item_gzfshy_listview_icon)
        ImageView itemGzfshyListviewIcon;
        @BindView(R.id.item_gzfshy_listview_hongiang)
        ImageView itemGzfshyListviewHongiang;
        @BindView(R.id.item_gzfshy_listview_vip)
        ImageView itemGzfshyListviewVip;
        @BindView(R.id.item_gzfshy_listview_online)
        ImageView itemGzfshyListviewOnline;
        @BindView(R.id.item_gzfshy_listview_name)
        TextView itemGzfshyListviewName;
        @BindView(R.id.item_gzfshy_listview_shiming)
        ImageView itemGzfshyListviewShiming;
        @BindView(R.id.item_gzfshy_listview_Sex)
        TextView itemGzfshyListviewSex;
        @BindView(R.id.item_gzfshy_listview_role)
        TextView itemGzfshyListviewRole;
        @BindView(R.id.item_dynamic_listview_richCount)
        TextView itemDynamicListviewRichCount;
        @BindView(R.id.item_dynamic_listview_ll_richCount)
        PercentLinearLayout itemDynamicListviewLlRichCount;
        @BindView(R.id.item_dynamic_listview_beautyCount)
        TextView itemDynamicListviewBeautyCount;
        @BindView(R.id.item_dynamic_listview_ll_beautyCount)
        PercentLinearLayout itemDynamicListviewLlBeautyCount;
        @BindView(R.id.item_gzfshy_listview_city)
        TextView itemGzfshyListviewCity;
        @BindView(R.id.item_gzfshy_listview_time)
        TextView itemGzfshyListviewTime;
        @BindView(R.id.item_gzfshy_listview_present)
        ImageView itemGzfshyListviewPresent;
        @BindView(R.id.item_gzfshy_listview_presentCount)
        TextView itemGzfshyListviewPresentCount;
        @BindView(R.id.item_gzfshy_listview_modou)
        TextView itemGzfshyListviewModou;
        @BindView(R.id.item_gzfshy_listview_flag)
        ImageView itemGzfshyListviewFlag;
        @BindView(R.id.ll_present)
        LinearLayout llReward;
        @BindView(R.id.iv_tuiding)
        ImageView ivTuiding;
        @BindView(R.id.item_tv_time)
        TextView itemTvTime;
        @BindView(R.id.item_tv_add_charm)
        TextView itemTvAddCharm;
        @BindView(R.id.item_attention_state)
        ImageView itemAttentionState;
        @BindView(R.id.rl_tuiding)
        RelativeLayout rlTuiding;

        public RewardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            llReward.setVisibility(View.VISIBLE);
            rlTuiding.setVisibility(View.GONE);
            RewardData.DataBean data = list.get(position);
            if (TextUtil.isEmpty(data.getHead_pic()) || NetPic().equals(data.getHead_pic())) {//"http://59.110.28.150:888/"
                itemGzfshyListviewIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemGzfshyListviewIcon, 0);
                ImageLoader.loadCircleImage(context,data.getHead_pic(),itemGzfshyListviewIcon,R.mipmap.morentouxiang);
            }
//       itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
            itemGzfshyListviewSex.setText(data.getAge());
            if (data.getOnlinestate() == 0) {
                itemGzfshyListviewOnline.setVisibility(View.GONE);
            } else {
                itemGzfshyListviewOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), itemGzfshyListviewVip);

            if ("0".equals(data.getIs_hand())) {
                itemGzfshyListviewHongiang.setVisibility(View.INVISIBLE);
            } else {
                itemGzfshyListviewHongiang.setVisibility(View.VISIBLE);
            }
            if ("0".equals(data.getRealname())) {
                itemGzfshyListviewShiming.setVisibility(View.GONE);
            } else {
                itemGzfshyListviewShiming.setVisibility(View.VISIBLE);
            }

            if ("1".equals(data.getSex())) {
                itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("2".equals(data.getSex())) {
                itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("3".equals(data.getSex())) {
                itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
            }
            if ("S".equals(data.getRole())) {
                itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                itemGzfshyListviewRole.setText("斯");
            } else if ("M".equals(data.getRole())) {
                itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                itemGzfshyListviewRole.setText("慕");
            } else if ("SM".equals(data.getRole())) {
                itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                itemGzfshyListviewRole.setText("双");
            } else {
                itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                itemGzfshyListviewRole.setText(data.getRole());
            }
            itemGzfshyListviewName.setText(data.getNickname());
            if ("1".equals(data.getLocation_city_switch())) {
                itemGzfshyListviewCity.setText("隐身");
            } else {
                if (!TextUtil.isEmpty(data.getCity())) {
                    itemGzfshyListviewCity.setText(data.getCity());
                } else {
                    itemGzfshyListviewCity.setVisibility(View.GONE);
                }
            }
            itemGzfshyListviewTime.setText(data.getSendtime());
            int state = data.getState();
            if (state != 4) {
                itemGzfshyListviewFlag.setVisibility(View.VISIBLE);
            }
            switch (state) {
                case 0:
                    itemGzfshyListviewFlag.setImageResource(R.drawable.jiaguanzhu);
                    break;
                case 1:
                    itemGzfshyListviewFlag.setImageResource(R.drawable.duigouquxiao);
                    break;
                case 2:
                    itemGzfshyListviewFlag.setImageResource(R.drawable.beiguanzhu);
                    break;
                case 3:
                    itemGzfshyListviewFlag.setImageResource(R.drawable.huxiangguanzhu);
                    break;
                case 4:
                    itemGzfshyListviewFlag.setVisibility(View.GONE);
                    break;
            }
            if (!"0".equals(data.getNum())) {
                itemGzfshyListviewPresentCount.setVisibility(View.VISIBLE);

                itemGzfshyListviewPresentCount.setText("x" + data.getNum() + "个=" + data.getSum() + "豆");
           /* if (data.getSum().equals("0")){
               itemGzfshyListviewPresentCount.setText("x" + data.getNum());
            }*/
            }
            if ("0".equals(data.getPsid())) {
                itemGzfshyListviewPresent.setVisibility(View.GONE);
                itemGzfshyListviewModou.setVisibility(View.VISIBLE);
                itemGzfshyListviewModou.setText(data.getAmount() + "魔豆");
            } else {
                itemGzfshyListviewModou.setVisibility(View.GONE);
                itemGzfshyListviewPresent.setVisibility(View.VISIBLE);
                switch (data.getPsid()) {
                    case "1":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present1);
                        break;
                    case "2":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present3);
                        break;
                    case "3":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present5);
                        break;
                    case "4":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present7);
                        break;
                    case "5":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present9);
                        break;
                    case "6":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present11);
                        break;
                    case "7":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present13);
                        break;
                    case "8":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present15);
                        break;
                    case "9":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.present17);
                        break;
                    case "10":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew01);
                        break;
                    case "11":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew02);
                        break;
                    case "12":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew03);
                        break;
                    case "13":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew04);
                        break;
                    case "14":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew05);
                        break;
                    case "15":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew06);
                        break;
                    case "16":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew07);
                        break;
                    case "17":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew08);
                        break;
                    case "18":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew09);
                        break;
                    case "19":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew10);
                        break;
                    case "20":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew11);
                        break;
                    case "21":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew12);
                        break;
                    case "22":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew13);
                        break;
                    case "23":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew14);
                        break;
                    case "24":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew15);
                        break;
                    case "25":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew16);
                        break;
                    case "26":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew17);
                        break;
                    case "27":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew18);
                        break;
                    case "28":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew19);
                        break;
                    case "29":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew20);
                        break;
                    case "30":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew21);
                        break;
                    case "31":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew22);
                        break;
                    case "32":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew23);
                        break;
                    case "33":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew24);
                        break;
                    case "34":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew25);
                        break;
                    case "35":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew26);
                        break;
                    case "36":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew27);
                        break;
                    case "37":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew28);
                        break;
                    case "38":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew29);
                        break;
                    case "39":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew30);
                        break;
                    case "40":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew31);
                        break;
                    case "41":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew32);
                        break;
                    case "42":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                        break;
                    case "43":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                        break;
                    case "44":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                        break;
                    case "45":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                        break;
                    case "46":
                        itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                        break;
                }
            }
            itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
            itemDynamicListviewLlRichCount.setVisibility(View.GONE);
            itemGzfshyListviewFlag.setTag(position);
            itemGzfshyListviewFlag.setOnClickListener(RewardCommentAdapter.this);
            itemGzfshyListviewIcon.setTag(position);
            itemGzfshyListviewIcon.setOnClickListener(RewardCommentAdapter.this);
            itemGzfshyListviewShiming.setTag(position);
            itemGzfshyListviewShiming.setOnClickListener(RewardCommentAdapter.this);

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
