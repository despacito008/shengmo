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
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.QqGzlistBean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PrintLogUtils;
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
public class QqGzListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<QqGzlistBean.DataBean> list;
    private LayoutInflater inflater;
    //who 0:自己 1:别人
    private int who;
    //type 0:关注 1:粉丝
    private String type;
    Handler handler = new Handler();

    public QqGzListviewAdapter(Context context, List<QqGzlistBean.DataBean> list, int who, String type) {
        super();
        this.context = context;
        this.list = list;
        this.who = who;
        this.type = type;
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
        QqGzlistBean.DataBean data = list.get(position);
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
        UserIdentityUtils.showIdentity(context, data.getUserInfo().getHead_pic(),data.getUid(), data.getUserInfo().getIs_volunteer(), data.getUserInfo().getIs_admin(), data.getUserInfo().getSvipannual(), data.getUserInfo().getSvip(), data.getUserInfo().getVipannual(), data.getUserInfo().getVip(),data.getUserInfo().getBkvip(),data.getUserInfo().getBlvip(), holder.itemGzfshyListviewVip);
       /* if (data.getUserInfo().getIs_hand().equals("0")) {
            holder.itemGzfshyListviewHongiang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemGzfshyListviewHongiang.setVisibility(View.VISIBLE);
        }*/
        if ("0".equals(data.getUserInfo().getRealname())) {
            holder.itemGzfshyListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewShiming.setVisibility(View.VISIBLE);
        }

        if ("1".equals(data.getUserInfo().getSex())) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("2".equals(data.getUserInfo().getSex())) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("3".equals(data.getUserInfo().getSex())) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if ("S".equals(data.getUserInfo().getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemGzfshyListviewRole.setText("斯");
        } else if ("M".equals(data.getUserInfo().getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemGzfshyListviewRole.setText("慕");
        } else if ("SM".equals(data.getUserInfo().getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemGzfshyListviewRole.setText("双");
        } else if ("~".equals(data.getUserInfo().getRole())) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemGzfshyListviewRole.setText("~");
        }

        if ("".equals(data.getUserInfo().getMarkname())){
            holder.itemGzfshyListviewName.setText(data.getUserInfo().getNickname()+"");
        }else {
            holder.itemGzfshyListviewName.setText(data.getUserInfo().getMarkname()+"");
        }

/*        if (data.getUserInfo().getLocation_city_switch().equals("1")){
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
        }*/
        holder.tv_cityya2.setVisibility(View.VISIBLE);
        if ("1".equals(data.getUserInfo().getLocation_city_switch())){
            holder.tv_cityya2.setText("隐身");
        }else {
            if(data.getUserInfo().getCity().equals("")&&data.getUserInfo().getProvince().equals("")){
                holder.tv_cityya2.setText("隐身");
            }else {
                if(data.getUserInfo().getProvince().equals(data.getUserInfo().getCity())){
                    holder.tv_cityya2.setText(data.getUserInfo().getCity());
                }else{
                    holder.tv_cityya2.setText(data.getUserInfo().getCity()+"");
                }
            }
        }


        if (!"".equals(data.getUserInfo().getLmarkname())&& null!=data.getUserInfo().getLmarkname()){
            holder.itemGzfshyListviewCity.setText(data.getUserInfo().getLmarkname()+"");
        }else {
            holder.itemGzfshyListviewCity.setText("");
        }


                int state = data.getState();
            holder.itemGzfshyListviewFlag.setVisibility(View.VISIBLE);
            holder.itemGzfshyListviewFlag.setImageResource(R.drawable.duigouquxiao);

        if (!"0".equals(data.getUserInfo().getCharm_val())) {
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewBeautyCount.setText(data.getUserInfo().getCharm_val());
        } else {
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!"0".equals(data.getUserInfo().getWealth_val())) {
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
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("您已经悄悄关注此人,确认取消关注吗?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quxiaoqiaoqiaodguanzhu(list.get(pos).getUid(),pos);
                            }
                        }).create().show();


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

    public void quxiaoqiaoqiaodguanzhu(String fuid, final int post) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.overfollowquiet, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            list.remove(post);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });

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

    public void qiaoqiaodguanzhu(String fuid, final int post) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.followOneBoxQuiet, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            list.get(post).setState(1);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });
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


