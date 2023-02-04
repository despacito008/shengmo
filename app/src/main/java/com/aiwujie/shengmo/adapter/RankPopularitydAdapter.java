package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.RankPopularityData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class RankPopularitydAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<RankPopularityData.DataBean> list;

    public RankPopularitydAdapter(Context context, List<RankPopularityData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_popularity_ranklist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RankPopularityData.DataBean data = list.get(position);
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemListviewPopularityRanklistVip);
        if (data.getRealname().equals("0")) {
            holder.itemListviewPopularityRanklistShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewPopularityRanklistShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemListviewPopularityRanklistSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewPopularityRanklistSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemListviewPopularityRanklistSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewPopularityRanklistSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemListviewPopularityRanklistSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewPopularityRanklistSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemListviewPopularityRanklistRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewPopularityRanklistRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemListviewPopularityRanklistRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewPopularityRanklistRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemListviewPopularityRanklistRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewPopularityRanklistRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemListviewPopularityRanklistRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewPopularityRanklistRole.setText("~");
        }
        holder.itemListviewPopularityRanklistSex.setText(data.getAge());
        holder.itemListviewPopularityRanklistNumber.setText(position + 1 + "");
        if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewPopularityRanklistIcon, 0);
        } else {
            holder.itemListviewPopularityRanklistIcon.setImageResource(R.mipmap.morentouxiang);
        }
        holder.itemListviewPopularityRanklistName.setText(data.getNickname());
        holder.itemListviewPopularityRanklistAllcount.setText(data.getAllnum());
        if (!data.getCharm_val().equals("0")) {
            holder.itemListviewPopularityRanklistLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemListviewPopularityRanklistBeautyCount.setText(data.getCharm_val());
        } else {
            holder.itemListviewPopularityRanklistLlBeautyCount.setVisibility(View.GONE);
        }

        if (!data.getWealth_val().equals("0")) {
            holder.itemListviewPopularityRanklistLlRichCount.setVisibility(View.VISIBLE);
            holder.itemListviewPopularityRanklistRichCount.setText(data.getWealth_val());
        } else {
            holder.itemListviewPopularityRanklistLlRichCount.setVisibility(View.GONE);
        }
        holder.itemListviewPopularityRanklistIcon.setTag(position);
        holder.itemListviewPopularityRanklistIcon.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_popularity_ranklist_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;

        }
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_popularity_ranklist_number)
        TextView itemListviewPopularityRanklistNumber;
        @BindView(R.id.item_listview_popularity_ranklist_icon)
        ImageView itemListviewPopularityRanklistIcon;
        @BindView(R.id.item_listview_popularity_ranklist_vip)
        ImageView itemListviewPopularityRanklistVip;
        @BindView(R.id.item_listview_popularity_ranklist_name)
        TextView itemListviewPopularityRanklistName;
        @BindView(R.id.item_listview_popularity_ranklist_shiming)
        ImageView itemListviewPopularityRanklistShiming;
        @BindView(R.id.item_listview_popularity_ranklist_Sex)
        TextView itemListviewPopularityRanklistSex;
        @BindView(R.id.item_listview_popularity_ranklist_role)
        TextView itemListviewPopularityRanklistRole;
        @BindView(R.id.item_listview_popularity_ranklist_richCount)
        TextView itemListviewPopularityRanklistRichCount;
        @BindView(R.id.item_listview_popularity_ranklist_ll_richCount)
        AutoLinearLayout itemListviewPopularityRanklistLlRichCount;
        @BindView(R.id.item_listview_popularity_ranklist_beautyCount)
        TextView itemListviewPopularityRanklistBeautyCount;
        @BindView(R.id.item_listview_popularity_ranklist_ll_beautyCount)
        AutoLinearLayout itemListviewPopularityRanklistLlBeautyCount;
        @BindView(R.id.item_listview_popularity_ranklist_allcount)
        TextView itemListviewPopularityRanklistAllcount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
