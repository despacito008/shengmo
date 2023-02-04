package com.aiwujie.shengmo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
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

public class TuidingCommentAdapter extends RecyclerView.Adapter<TuidingCommentAdapter.TuidingHolder> implements View.OnClickListener {

    private Context context;
    private List<DynamicDetailBean.DataBean> list;

    public TuidingCommentAdapter(Context context, List<DynamicDetailBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TuidingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reward_listview, parent, false);
        return new TuidingHolder(view);
    }

    @Override
    public void onBindViewHolder(TuidingHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final int pos = (int) v.getTag();
        switch (v.getId()) {
            case R.id.item_attention_state:
                switch (list.get(pos).getState()) {
                    case 0:
                    case 2:
                        follow(list.get(pos).getUid(), pos);
                        break;
                    case 1:
                    case 3:
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

    class TuidingHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.ll_reward)
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

        public TuidingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            DynamicDetailBean.DataBean data = list.get(position);
            llReward.setVisibility(View.GONE);
            rlTuiding.setVisibility(View.VISIBLE);
            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//http://59.110.28.150:888/
                itemGzfshyListviewIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemGzfshyListviewIcon, 0);
            }
//        itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
            itemGzfshyListviewSex.setText(data.getAge());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long aLong = Long.valueOf(list.get(position).getAddtime());
            long lt = new Long(aLong * 1000);
            Date date = new Date(lt);
            itemGzfshyListviewTime.setText("" + simpleDateFormat.format(date));
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

            String sum = data.getSum();
            if (!TextUtil.isEmpty(sum) && !sum.equals("1")) {
                itemTvTime.setText(data.getUse_sum() + "次/" + data.getSum() + "卡/" + data.getInterval());
                itemTvTime.setVisibility(View.VISIBLE);
            } else {
                itemTvTime.setVisibility(View.INVISIBLE);
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

            int state = data.getState();
            if (state != 4) {
                itemAttentionState.setVisibility(View.VISIBLE);
            }
            switch (state) {
                case 0:
                    itemAttentionState.setImageResource(R.drawable.jiaguanzhu);
                    break;
                case 1:
                    itemAttentionState.setImageResource(R.drawable.duigouquxiao);
                    break;
                case 2:
                    itemAttentionState.setImageResource(R.drawable.beiguanzhu);
                    break;
                case 3:
                    itemAttentionState.setImageResource(R.drawable.huxiangguanzhu);
                    break;
                case 4:
                    itemAttentionState.setVisibility(View.INVISIBLE);
                    break;
            }
            if (list.get(position).getUid().equals(MyApp.uid)) {
                itemAttentionState.setVisibility(View.INVISIBLE);
            } else {
                itemAttentionState.setVisibility(View.VISIBLE);
            }

            itemAttentionState.setTag(position);
            itemAttentionState.setOnClickListener(TuidingCommentAdapter.this);
            itemGzfshyListviewIcon.setTag(position);
            itemGzfshyListviewIcon.setOnClickListener(TuidingCommentAdapter.this);
            itemGzfshyListviewShiming.setTag(position);
            itemGzfshyListviewShiming.setOnClickListener(TuidingCommentAdapter.this);
        }

    }
}
