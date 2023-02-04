package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/8.
 */
public class GridViewTopAdapter extends BaseAdapter {
    private List<String> tvs;
    private Context context;
    private LayoutInflater inflater;
    private String[] colors={"#225566","#29f566","#225f86","#2255ff","#228516","#ff7766"};
    public GridViewTopAdapter(List<String> tvs, Context context) {
        this.context=context;
        this.tvs = tvs;
        inflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return  tvs.size();
    }
    @Override
    public String getItem(int position) {
        return tvs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_recyclerview_topic_tv, parent,false);
            holder.tvTopic = (TextView) convertView.findViewById(R.id.item_mygridview_topic_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final String topic=tvs.get(position);
        holder.tvTopic.setText(topic);
        holder.tvTopic.setTextColor(Color.parseColor(colors[position]));
        return convertView;
    }
    class ViewHolder {
        TextView tvTopic;
    }

}
