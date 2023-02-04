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
 * Created by 290243232 on 2016/12/17.
 */
public class CusGridviewAdapter extends BaseAdapter{
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    public  int selectIndex=-1;

    public  void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public CusGridviewAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.list = list;
        inflater= LayoutInflater.from(context);
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
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_gv_textview, null);
            holder.tv= (TextView) convertView.findViewById(R.id.item_gv_tv);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position));
        if( selectIndex == position ){
            convertView.setBackgroundResource(R.drawable.item_gv_bg_zi2);
            holder.tv.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            convertView.setBackgroundResource(R.drawable.item_gv_bg_hui2);
            holder.tv.setTextColor(Color.parseColor("#B7B7B7"));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv;
    }
}
