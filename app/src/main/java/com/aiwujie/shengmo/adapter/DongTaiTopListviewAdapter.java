package com.aiwujie.shengmo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class DongTaiTopListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<DynamicDetailBean.DataBean> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();

    public DongTaiTopListviewAdapter(Context context, List<DynamicDetailBean.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_gz_top_dt_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DynamicDetailBean.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//http://59.110.28.150:888/
            holder.itemGzfshyListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemGzfshyListviewIcon, 0);
        }
//        holder.itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
        holder.itemGzfshyListviewSex.setText(data.getAge());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long aLong = Long.valueOf(list.get(position).getAddtime());
        long lt = new Long(aLong*1000);
        Date date = new Date(lt);
        holder.itemGzfshyListviewTime.setText(""+simpleDateFormat.format(date));
        if (data.getOnlinestate() == 0) {
            holder.itemGzfshyListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemGzfshyListviewVip);

        if (data.getIs_hand().equals("0")) {
            holder.itemGzfshyListviewHongiang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemGzfshyListviewHongiang.setVisibility(View.VISIBLE);
        }
        if (data.getRealname().equals("0")) {
            holder.itemGzfshyListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemGzfshyListviewShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
        }

        String sum = data.getSum();
        if (null!=sum&&!"".equals(sum)&&!sum.equals("1")){
            holder.tvtuijige.setText(data.getUse_sum()+"次/"+data.getSum()+"卡/"+data.getInterval());
            holder.tvtuijige.setVisibility(View.VISIBLE);
        }else {
            holder.tvtuijige.setVisibility(View.GONE);
        }

        if (data.getRole().equals("S")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemGzfshyListviewRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemGzfshyListviewRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemGzfshyListviewRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemGzfshyListviewRole.setText("~");
        }
        holder.itemGzfshyListviewName.setText(data.getNickname());
      /*  if (!data.getCity().equals("")) {
            holder.itemGzfshyListviewCity.setText(data.getCity());
        } else {
            holder.itemGzfshyListviewCity.setVisibility(View.GONE);
        }
        holder.itemGzfshyListviewTime.setText(data.getSendtime());*/
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
                holder.itemGzfshyListviewFlag.setVisibility(View.INVISIBLE);
                break;
        }
        if (list.get(position).getUid().equals(MyApp.uid)) {
            holder.itemGzfshyListviewFlag.setVisibility(View.INVISIBLE);
        }else {
            holder.itemGzfshyListviewFlag.setVisibility(View.VISIBLE);
        }
     /*   if(!data.getCharm_val().equals("0")){
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewBeautyCount.setText(data.getCharm_val());
        }else{
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if(!data.getWealth_val().equals("0")){
            holder.itemDynamicListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewRichCount.setText(data.getWealth_val());
        }else{
            holder.itemDynamicListviewLlRichCount.setVisibility(View.GONE);
        }*/

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
                                    BindGuanzhuDialog.bindAlertDialog(context,obj.getString("msg"));
                                    break;
                                case 5000:
                                    ToastUtil.show(context, obj.getString("msg")+"");
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
        @BindView(R.id.tv_tuijige)
        TextView tvtuijige;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


