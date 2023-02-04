package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

/**
 * Created by 290243232 on 2016/12/17.
 */
public class RewardListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<RewardData.DataBean> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();

    public RewardListviewAdapter(Context context, List<RewardData.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_reward_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RewardData.DataBean data = list.get(position);
        holder.rlReward.setVisibility(View.VISIBLE);
        holder.rlTuiding.setVisibility(View.GONE);
        if (TextUtil.isEmpty(data.getHead_pic()) || NetPic().equals(data.getHead_pic())) {//"http://59.110.28.150:888/"
            holder.itemGzfshyListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemGzfshyListviewIcon, 0);
        }
//        holder.itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
        holder.itemGzfshyListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemGzfshyListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.itemGzfshyListviewVip);

        if ("0".equals(data.getIs_hand())) {
            holder.itemGzfshyListviewHongiang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemGzfshyListviewHongiang.setVisibility(View.VISIBLE);
        }
        if ("0".equals(data.getRealname())) {
            holder.itemGzfshyListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewShiming.setVisibility(View.VISIBLE);
        }

        if ("1".equals(data.getSex())) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("2".equals(data.getSex())) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("3".equals(data.getSex())) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if ("S".equals(data.getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemGzfshyListviewRole.setText("斯");
        } else if ("M".equals(data.getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemGzfshyListviewRole.setText("慕");
        } else if ("SM".equals(data.getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemGzfshyListviewRole.setText("双");
        } else if ("~".equals(data.getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemGzfshyListviewRole.setText("~");
        }
        holder.itemGzfshyListviewName.setText(data.getNickname());
        holder.itemGzfshyListviewName.setText(data.getNickname());

        if ("1".equals(data.getLocation_city_switch())) {
            holder.itemGzfshyListviewCity.setText("隐身");
        } else {
            if (!TextUtil.isEmpty(data.getCity())) {
                holder.itemGzfshyListviewCity.setText(data.getCity());
            } else {
                holder.itemGzfshyListviewCity.setVisibility(View.GONE);
            }
        }
        holder.itemGzfshyListviewTime.setText(data.getSendtime());
        int state = data.getState();
        if (state != 4) {
            holder.itemGzfshyListviewFlag.setVisibility(View.VISIBLE);
        }
        switch (state) {
            case 0:
                holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.jiaguanzhu);
                break;
            case 1:
                holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.duigouquxiao);
                break;
            case 2:
                holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.beiguanzhu);
                break;
            case 3:
                holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.huxiangguanzhu);
                break;
            case 4:
                holder.itemGzfshyListviewFlag.setVisibility(View.GONE);
                break;
        }
        if (!"0".equals(data.getNum())) {
            holder.itemGzfshyListviewPresentCount.setVisibility(View.VISIBLE);

            holder.itemGzfshyListviewPresentCount.setText("x" + data.getNum() + "个=" + data.getSum() + "豆");
           /* if (data.getSum().equals("0")){
                holder.itemGzfshyListviewPresentCount.setText("x" + data.getNum());
            }*/
        }
        if ("0".equals(data.getPsid())) {
            holder.itemGzfshyListviewModou.setText(data.getAmount() + "魔豆");
        } else {

            switch (data.getPsid()) {
                case "1":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present1);
                    break;
                case "2":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present3);
                    break;
                case "3":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present5);
                    break;
                case "4":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present7);
                    break;
                case "5":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present9);
                    break;
                case "6":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present11);
                    break;
                case "7":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present13);
                    break;
                case "8":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present15);
                    break;
                case "9":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.present17);
                    break;
                case "10":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew01);
                    break;
                case "11":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew02);
                    break;
                case "12":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew03);
                    break;
                case "13":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew04);
                    break;
                case "14":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew05);
                    break;
                case "15":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew06);
                    break;
                case "16":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew07);
                    break;
                case "17":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew08);
                    break;
                case "18":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew09);
                    break;
                case "19":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew10);
                    break;
                case "20":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew11);
                    break;
                case "21":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew12);
                    break;
                case "22":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew13);
                    break;
                case "23":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew14);
                    break;
                case "24":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew15);
                    break;
                case "25":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew16);
                    break;
                case "26":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew17);
                    break;
                case "27":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew18);
                    break;
                case "28":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew19);
                    break;
                case "29":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew20);
                    break;
                case "30":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew21);
                    break;
                case "31":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew22);
                    break;
                case "32":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew23);
                    break;
                case "33":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew24);
                    break;
                case "34":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew25);
                    break;
                case "35":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew26);
                    break;
                case "36":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew27);
                    break;
                case "37":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew28);
                    break;
                case "38":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew29);
                    break;
                case "39":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew30);
                    break;
                case "40":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew31);
                    break;
                case "41":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.presentnew32);
                    break;
                case "42":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                    break;
                case "43":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                    break;
                case "44":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                    break;
                case "45":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                    break;
                case "46":
                    holder.itemGzfshyListviewPresent.setImageResource(R.mipmap.weizhiliwu);
                    break;
            }
        }
        holder.itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
        holder.itemDynamicListviewLlRichCount.setVisibility(View.GONE);
        holder.itemGzfshyListviewFlag.setTag(position);
        holder.itemGzfshyListviewFlag.setOnClickListener(this);
        holder.itemGzfshyListviewIcon.setTag(position);
        holder.itemGzfshyListviewIcon.setOnClickListener(this);
        holder.itemGzfshyListviewShiming.setTag(position);
        holder.itemGzfshyListviewShiming.setOnClickListener(this);
        return convertView;
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
                intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
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
                });
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
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
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    static
    class ViewHolder {
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
        @BindView(R.id.ll_reward)
        RelativeLayout rlReward;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


