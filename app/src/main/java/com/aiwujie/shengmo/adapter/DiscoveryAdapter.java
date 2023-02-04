package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.DiscoveryData;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/9/15.
 */

public class DiscoveryAdapter extends BaseAdapter {
    private Context context;
    private List<DiscoveryData> list;
    private LayoutInflater inflater;

    public DiscoveryAdapter(Context context, List<DiscoveryData> list) {
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
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.item_gridview_discovery, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.itemGridviewDiscoveryIv.setImageResource(list.get(position).getImgeId());
        holder.itemGridviewDiscoveryTv01.setText(list.get(position).getTitle());
        //holder.itemGridviewDiscoveryTv02.setText(list.get(position).getContent());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_gridview_discovery_iv)
        ImageView itemGridviewDiscoveryIv;
        @BindView(R.id.item_gridview_discovery_tv01)
        TextView itemGridviewDiscoveryTv01;
        @BindView(R.id.item_gridview_discovery_tv02)
        TextView itemGridviewDiscoveryTv02;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
