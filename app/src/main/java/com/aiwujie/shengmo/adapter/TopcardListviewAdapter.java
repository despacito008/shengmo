package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.TopcardYsedRsData;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class TopcardListviewAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<TopcardYsedRsData.DataBean> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();

    public TopcardListviewAdapter(Context context, List<TopcardYsedRsData.DataBean> list) {
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
        LaudListviewAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gz_fs_hy_listview, null);
            holder = new LaudListviewAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (LaudListviewAdapter.ViewHolder) convertView.getTag();
        }
        TopcardYsedRsData.DataBean data = list.get(position);


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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


