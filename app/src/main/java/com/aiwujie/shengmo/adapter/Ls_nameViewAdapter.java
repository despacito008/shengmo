package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.Ls_nameBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 290243232 on 2016/11/23.
 */
public class Ls_nameViewAdapter extends BaseAdapter {

    List<Ls_nameBean.DataBean> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public Ls_nameViewAdapter(List<Ls_nameBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return  list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ls_name_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getNickname()+"");
        String s = stampToDate(list.get(position).getAddtime());
        holder.shijian.setText(s+"");
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView shijian;

        ViewHolder(View view) {
            name = view.findViewById(R.id.name);
            shijian = view.findViewById(R.id.shijian);
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long l = Long.parseLong(String.valueOf(s)) * 1000;
        String sd = sdf.format(new Date(l));      // 时间戳转换成时间
        return sd;
    }

}
