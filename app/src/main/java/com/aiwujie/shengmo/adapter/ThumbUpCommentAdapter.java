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
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.LaudListData;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class ThumbUpCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<LaudListData.DataBean> list;
    private Fragment fragment;

    public ThumbUpCommentAdapter(Context context, List<LaudListData.DataBean> list,Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gz_fs_hy_listview,parent,false);
        return new ThumbUpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ThumbUpViewHolder)holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ThumbUpViewHolder extends RecyclerView.ViewHolder {
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

        public ThumbUpViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            LaudListData.DataBean data = list.get(position);
            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//http://59.110.28.150:888/
                itemGzfshyListviewIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemGzfshyListviewIcon, 0);
                ImageLoader.loadCircleImage(fragment,data.getHead_pic(),itemGzfshyListviewIcon,R.mipmap.morentouxiang);
            }
//        holder.itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
            itemGzfshyListviewSex.setText(data.getAge());
            if (data.getOnlinestate() == 0) {
                itemGzfshyListviewOnline.setVisibility(View.GONE);
            } else {
                itemGzfshyListviewOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), itemGzfshyListviewVip);

            if (data.getIs_hand().equals("0")) {
                itemGzfshyListviewHongiang.setVisibility(View.INVISIBLE);
            } else {
                itemGzfshyListviewHongiang.setVisibility(View.VISIBLE);
            }
            if (data.getRealname().equals("0")) {
                itemGzfshyListviewShiming.setVisibility(View.GONE);
            } else {
                itemGzfshyListviewShiming.setVisibility(View.VISIBLE);
            }

            if (data.getSex().equals("1")) {
                itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
            } else if (data.getSex().equals("2")) {
                itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
            } else if (data.getSex().equals("3")) {
                itemGzfshyListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemGzfshyListviewSex.setCompoundDrawables(drawable, null, null, null);
            }
            if (data.getRole().equals("S")) {
                itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                itemGzfshyListviewRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                itemGzfshyListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                itemGzfshyListviewRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
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
                if (!data.getCity().equals("")) {
                    itemGzfshyListviewCity.setText(data.getCity());
                } else {
                    itemGzfshyListviewCity.setVisibility(View.GONE);
                }
            }

            if(1 == list.get(position).getOnlinestate()) {
                itemGzfshyListviewOnline.setVisibility(View.VISIBLE);
            } else {
                itemGzfshyListviewOnline.setVisibility(View.INVISIBLE);
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
                    itemGzfshyListviewFlag.setVisibility(View.INVISIBLE);
                    break;
            }
            itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
            itemDynamicListviewLlRichCount.setVisibility(View.GONE);

     /*       if (!data.getCharm_val().equals("0")) {
                itemDynamicListviewLlBeautyCount.setVisibility(View.VISIBLE);
                itemDynamicListviewBeautyCount.setText(data.getCharm_val());
            } else {
                itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
            }

            if (!data.getWealth_val().equals("0")) {
                itemDynamicListviewLlRichCount.setVisibility(View.VISIBLE);
                itemDynamicListviewRichCount.setText(data.getWealth_val());
            } else {
                itemDynamicListviewLlRichCount.setVisibility(View.GONE);
            }
*/
            itemGzfshyListviewFlag.setTag(position);
            itemGzfshyListviewFlag.setOnClickListener(ThumbUpCommentAdapter.this);
            itemGzfshyListviewIcon.setTag(position);
            itemGzfshyListviewIcon.setOnClickListener(ThumbUpCommentAdapter.this);
            itemGzfshyListviewShiming.setTag(position);
            itemGzfshyListviewShiming.setOnClickListener(ThumbUpCommentAdapter.this);
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
