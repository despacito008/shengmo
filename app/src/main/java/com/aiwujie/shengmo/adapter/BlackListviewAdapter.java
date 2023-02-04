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
import com.aiwujie.shengmo.bean.BlackListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class BlackListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<BlackListData.DataBean> list;
    private LayoutInflater inflater;
    private Handler handler = new Handler();
    private int retcode;

    public BlackListviewAdapter(Context context, List<BlackListData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_blacklist_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BlackListData.DataBean data = list.get(position);
        if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemBlackListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemBlackListviewIcon, 0);
        }

//        holder.itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
        holder.itemBlackListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemBlackListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemBlackListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(),data.getUid(),data.getIs_volunteer(),data.getIs_admin(),data.getSvipannual(),data.getSvip(),data.getVipannual(),data.getVip(),data.getBkvip(),data.getBlvip(),holder.itemBlackListviewVip);
        if (data.getIs_hand().equals("0")) {
            holder.itemBlackListviewHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemBlackListviewHongniang.setVisibility(View.VISIBLE);
        }
        if (data.getRealname().equals("0")) {
            holder.itemBlackListviewShiming.setVisibility(View.INVISIBLE);
        } else {
            holder.itemBlackListviewShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemBlackListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemBlackListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemBlackListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemBlackListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemBlackListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemBlackListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemBlackListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemBlackListviewRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemBlackListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemBlackListviewRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemBlackListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemBlackListviewRole.setText("双");
        }else if(data.getRole().equals("~")){
            holder.itemBlackListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemBlackListviewRole.setText("~");
        }
        holder.itemBlackListviewName.setText(data.getNickname());
        holder.itemBlackListviewTime.setText("拉黑时间  " + data.getBlack_time());
        holder.itemBlackListviewCity.setText(data.getCity());
        holder.itemBlackListviewShiming.setTag(position);
        holder.itemBlackListviewShiming.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.item_black_listview_shiming:
                intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_black_listview_icon)
        ImageView itemBlackListviewIcon;
        @BindView(R.id.item_black_listview_vip)
        ImageView itemBlackListviewVip;
        @BindView(R.id.item_black_listview_hongniang)
        ImageView itemBlackListviewHongniang;
        @BindView(R.id.item_black_listview_name)
        TextView itemBlackListviewName;
        @BindView(R.id.item_black_listview_online)
        ImageView itemBlackListviewOnline;
        @BindView(R.id.item_black_listview_shiming)
        ImageView itemBlackListviewShiming;
        @BindView(R.id.item_black_listview_time)
        TextView itemBlackListviewTime;
        @BindView(R.id.item_black_listview_Sex)
        TextView itemBlackListviewSex;
        @BindView(R.id.item_black_listview_role)
        TextView itemBlackListviewRole;
        @BindView(R.id.item_black_listview_city)
        TextView itemBlackListviewCity;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


