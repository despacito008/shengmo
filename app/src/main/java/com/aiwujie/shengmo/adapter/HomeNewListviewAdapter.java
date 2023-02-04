package com.aiwujie.shengmo.adapter;

import android.content.Context;
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
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.bean.HomeListviewData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class HomeNewListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<HomeListviewData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    private Handler handler = new Handler();
    private String type;
    public HomeNewListviewAdapter(Context context, List<HomeListviewData.DataBean> list, int retcode, String type) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
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
            convertView = inflater.inflate(R.layout.item_new_near_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeListviewData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemNearListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemNearListviewIcon, 0);
        }
        if (retcode == 2000) {
            holder.itemNearListviewDistance.setVisibility(View.VISIBLE);
            holder.itemNearListviewDistance.setText(data.getDistance() + "km");
            if (data.getLat().equals("0.000000") || data.getLng().equals("0.000000")) {
                holder.itemNearListviewDistance.setVisibility(View.INVISIBLE);
            } else {
                holder.itemNearListviewDistance.setVisibility(View.VISIBLE);
            }
        } else {
            holder.itemNearListviewDistance.setVisibility(View.INVISIBLE);
        }
        holder.itemNearListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemNearListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemNearListviewVip);

        if (data.getIs_hand().equals("0")) {
            holder.itemNearListviewHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemNearListviewHongniang.setVisibility(View.VISIBLE);
        }
        if (data.getRealname().equals("0")) {
            holder.itemNearListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemNearListviewRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemNearListviewRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemNearListviewRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemNearListviewRole.setText("~");
        }
        holder.itemNearListviewName.setText(data.getNickname());
        if (type.equals("2")) {
            if (data.getIntroduce().equals("")) {
                holder.itemNearListviewSign.setText("(用户未设置签名)");
            } else {
                holder.itemNearListviewSign.setText(data.getIntroduce());
            }
        } else if (type.equals("0")) {
            holder.itemNearListviewSign.setText(data.getVisit_time() + "来访");
        } else if (type.equals("1")) {
            holder.itemNearListviewSign.setText(data.getVisit_time() + "查看");
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


    static class ViewHolder {
        @BindView(R.id.item_near_listview_icon)
        ImageView itemNearListviewIcon;
        @BindView(R.id.item_near_listview_vip)
        ImageView itemNearListviewVip;
        @BindView(R.id.item_near_listview_hongniang)
        ImageView itemNearListviewHongniang;
        @BindView(R.id.item_near_listview_flag)
        TextView itemNearListviewFlag;
        @BindView(R.id.item_near_listview_name)
        TextView itemNearListviewName;
        @BindView(R.id.item_near_listview_shiming)
        ImageView itemNearListviewShiming;
        @BindView(R.id.item_near_listview_online)
        ImageView itemNearListviewOnline;
        @BindView(R.id.item_near_listview_distance)
        TextView itemNearListviewDistance;
        @BindView(R.id.item_near_listview_time)
        TextView itemNearListviewTime;
        @BindView(R.id.item_near_listview_Sex)
        TextView itemNearListviewSex;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


