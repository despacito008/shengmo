package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
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
public class SouHyListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<GzFsHyListviewData.DataBean> list;
    private LayoutInflater inflater;
    //who 0:自己 1:别人
    private int who;
    //type 0:关注 1:粉丝
    private String type;
    Handler handler = new Handler();
    String name;

    public SouHyListviewAdapter(Context context, List<GzFsHyListviewData.DataBean> list, int who, String type, String name) {
        super();
        this.context = context;
        this.list = list;
        this.who = who;
        this.type = type;
        this.name=name;
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
            convertView = inflater.inflate(R.layout.item_gz_fs_hy_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GzFsHyListviewData.DataBean data = list.get(position);
        if (data.getUserInfo().getHead_pic().equals("") || data.getUserInfo().getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemGzfshyListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getUserInfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemGzfshyListviewIcon, 0);
        }
        holder.itemGzfshyListviewSex.setText(data.getUserInfo().getAge());
        if (data.getUserInfo().getOnlinestate() == 0) {
            holder.itemGzfshyListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getUserInfo().getHead_pic(),data.getUid(), data.getUserInfo().getIs_volunteer(), data.getUserInfo().getIs_admin(), data.getUserInfo().getSvipannual(), data.getUserInfo().getSvip(), data.getUserInfo().getVipannual(), data.getUserInfo().getVip(), data.getUserInfo().getBkvip(), data.getUserInfo().getBlvip(), holder.itemGzfshyListviewVip);
       /* if (data.getUserInfo().getIs_hand().equals("0")) {
            holder.itemGzfshyListviewHongiang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemGzfshyListviewHongiang.setVisibility(View.VISIBLE);
        }*/
        if (data.getUserInfo().getRealname().equals("0")) {
            holder.itemGzfshyListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewShiming.setVisibility(View.VISIBLE);
        }

        if (data.getUserInfo().getSex().equals("1")) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getUserInfo().getSex().equals("2")) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getUserInfo().getSex().equals("3")) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getUserInfo().getRole().equals("S")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemGzfshyListviewRole.setText("斯");
        } else if (data.getUserInfo().getRole().equals("M")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemGzfshyListviewRole.setText("慕");
        } else if (data.getUserInfo().getRole().equals("SM")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemGzfshyListviewRole.setText("双");
        } else if (data.getUserInfo().getRole().equals("~")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemGzfshyListviewRole.setText("~");
        }

        if (data.getUserInfo().getMarkname().equals("")){
            int i = data.getUserInfo().getNickname().indexOf(name);
            SpannableStringBuilder builder = new SpannableStringBuilder(list.get(position).getUserInfo().getNickname());
            if (i!=-1){
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#c450d6"));
                builder.setSpan(purSpan, i, i+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.itemGzfshyListviewName.setText(builder);
        }else {
            int i = data.getUserInfo().getMarkname().indexOf(name);
            SpannableStringBuilder builder = new SpannableStringBuilder(list.get(position).getUserInfo().getMarkname());
            if (i!=-1){
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#c450d6"));
                builder.setSpan(purSpan, i, i+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.itemGzfshyListviewName.setText(builder);
        }


        if (who==0){
            holder.tv_cityya2.setVisibility(View.VISIBLE);
            if (data.getUserInfo().getLocation_city_switch().equals("1")){
                holder.tv_cityya2.setText("隐身");
            }else {
                if(data.getUserInfo().getCity().equals("")&&data.getUserInfo().getProvince().equals("")){
                    holder.tv_cityya2.setText("隐身");
                }else {
                    if(data.getUserInfo().getProvince().equals(data.getUserInfo().getCity())){
                        holder.tv_cityya2.setText(data.getUserInfo().getCity());
                    }else{
                        holder.tv_cityya2.setText(""+data.getUserInfo().getCity());
                    }
                }
            }
            if (!"".equals(data.getUserInfo().getLmarkname())&& null!=data.getUserInfo().getLmarkname()){
                int i = data.getUserInfo().getLmarkname().indexOf(name);
                SpannableStringBuilder builder = new SpannableStringBuilder(list.get(position).getUserInfo().getLmarkname());
                if (i!=-1){
                    ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#c450d6"));
                    builder.setSpan(purSpan, i, i+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.itemGzfshyListviewCity.setText(builder);
            }else {
                holder.itemGzfshyListviewCity.setText("");
            }
        }else {
            holder.tv_cityya2.setVisibility(View.GONE);
            if (data.getUserInfo().getLocation_city_switch().equals("1")){
                holder.itemGzfshyListviewCity.setText("隐身");
            }else {
                if(data.getUserInfo().getCity().equals("")&&data.getUserInfo().getProvince().equals("")){
                    holder.itemGzfshyListviewCity.setText("(用户已隐藏位置)");
                }else {
                    if(data.getUserInfo().getProvince().equals(data.getUserInfo().getCity())){
                        holder.itemGzfshyListviewCity.setText(data.getUserInfo().getCity());
                    }else{
                        holder.itemGzfshyListviewCity.setText(data.getUserInfo().getProvince()+" "+data.getUserInfo().getCity());
                    }
                }
            }
        }

        int state = data.getState();
        if (who == 0) {
            holder.itemGzfshyListviewFlag.setVisibility(View.VISIBLE);
            if (type.equals("0")) {
                switch (state) {
                    case 0:
                        holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.duigouquxiao);
                        break;
                    case 1:
                        holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.huxiangguanzhu);
                        break;
                }
            } else {
                switch (state) {
                    case 0:
                        holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.beiguanzhu);
                        break;
                    case 1:
                        holder.itemGzfshyListviewFlag.setImageResource(R.mipmap.huxiangguanzhu);
                        break;
                }
            }
        } else {
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
                    holder.itemGzfshyListviewFlag.setVisibility(View.INVISIBLE);
                    break;
            }

        }
        if (!data.getUserInfo().getCharm_val().equals("0")) {
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewBeautyCount.setText(data.getUserInfo().getCharm_val());
        } else {
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!data.getUserInfo().getWealth_val().equals("0")) {
            holder.itemDynamicListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewRichCount.setText(data.getUserInfo().getWealth_val());
        } else {
            holder.itemDynamicListviewLlRichCount.setVisibility(View.GONE);
        }
        holder.itemGzfshyListviewFlag.setTag(position);
        holder.itemGzfshyListviewFlag.setOnClickListener(this);
        holder.itemGzfshyListviewShiming.setTag(position);
        holder.itemGzfshyListviewShiming.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final int pos = (int) v.getTag();
        if (who == 0) {
            if (type.equals("0")) {
                switch (list.get(pos).getState()) {
                    case 0:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("您已经关注此人,确认取消关注吗?")
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
                    case 1:
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
                                overfollow(list.get(pos).getUid(), pos);
                            }
                        }).create().show();
                        break;
                }
            } else {
                switch (list.get(pos).getState()) {
                    case 0:
                        follow(list.get(pos).getUid(), pos);
                        break;
                    case 1:
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
                                overfollow(list.get(pos).getUid(), pos);
                            }
                        }).create().show();
                        break;
                }
            }
        } else {
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
                case R.id.item_near_listview_shiming:
                    intent = new Intent(context, PhotoRzActivity.class);
                    context.startActivity(intent);
                    break;
            }
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
                                    if (who == 0) {
                                        if (type.equals("1")) {
                                            switch (list.get(position).getState()) {
                                                case 0:
                                                    list.get(position).setState(1);
                                                    break;
                                            }
                                        }
                                    } else {
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
                                    }
                                    notifyDataSetChanged();
                                    ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4001:
                                case 4002:
                                case 8881:
                                case 8882:
                                case 4787:
                                    BindGuanzhuDialog.bindAlertDialog(context,obj.getString("msg"));
                                    break;
                                case 5000:
                                    ToastUtil.show(context, obj.getString("msg")+"");
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
                                    if (who == 0) {
                                        if (type.equals("0")) {
                                            list.remove(position);
                                        } else {
                                            list.get(position).setState(0);
                                        }
                                    } else {
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


    static class ViewHolder {
        @BindView(R.id.item_gzfshy_listview_icon)
        ImageView itemGzfshyListviewIcon;
        @BindView(R.id.item_gzfshy_listview_hongiang)
        ImageView itemGzfshyListviewHongiang;
        @BindView(R.id.item_gzfshy_listview_vip)
        ImageView itemGzfshyListviewVip;
        @BindView(R.id.item_gzfshy_listview_name)
        TextView itemGzfshyListviewName;
        @BindView(R.id.item_gzfshy_listview_shiming)
        ImageView itemGzfshyListviewShiming;
        @BindView(R.id.item_gzfshy_listview_online)
        ImageView itemGzfshyListviewOnline;
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
        @BindView(R.id.tv_cityya2)
        TextView tv_cityya2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


