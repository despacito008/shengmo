package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.StampBuyData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class StampBuyAdapter extends BaseAdapter {
    private Context context;
    private List<StampBuyData.DataBean> list;
    private LayoutInflater inflater;
    public StampBuyAdapter(Context context, List<StampBuyData.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = inflater.inflate(R.layout.item_listview_recorder, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        StampBuyData.DataBean data=list.get(position);
        holder.itemRecorderDate.setText(data.getAddtime_format());
        holder.itemRecorderDay.setText(data.getWeek());
        holder.itemRecorderModou.setText("+"+data.getNum()+"张邮票");
        if(data.getBeans().equals("0")){
            holder.itemRecorderRmb.setText("充值"+data.getAmount()+"元");
        }else {
            holder.itemRecorderRmb.setText("使用"+data.getBeans()+"魔豆兑换");
        }
        holder.ivBeans.setVisibility(View.INVISIBLE);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_recorder_date)
        TextView itemRecorderDate;
        @BindView(R.id.item_recorder_modou)
        TextView itemRecorderModou;
        @BindView(R.id.item_recorder_day)
        TextView itemRecorderDay;
        @BindView(R.id.item_recorder_rmb)
        TextView itemRecorderRmb;
        @BindView(R.id.iv_item_recorder_beans)
        ImageView ivBeans;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
