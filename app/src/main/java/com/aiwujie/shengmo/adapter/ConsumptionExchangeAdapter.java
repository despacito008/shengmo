package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ConsumptionExchangeData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class ConsumptionExchangeAdapter extends BaseAdapter {
    private Context context;
    private List<ConsumptionExchangeData.DataBean> list;
    private LayoutInflater inflater;
    private String[] vips={"VIP1个月","VIP3个月","VIP6个月","VIP12个月"};
    private String[] svips={"SVIP1个月","SVIP3个月","SVIP6个月","SVIP12个月"};
    public ConsumptionExchangeAdapter(Context context, List<ConsumptionExchangeData.DataBean> list) {
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
        ConsumptionExchangeData.DataBean data=list.get(position);
        holder.itemRecorderDate.setText(data.getAddtime_format());
        holder.itemRecorderDay.setText(data.getWeek());
        holder.itemRecorderModou.setText("-"+data.getBeans());
        switch (data.getState()){
            case "1":
                holder.itemRecorderRmb.setText("兑换"+vips[Integer.parseInt(data.getType())-1]);
                break;
            case "2":
                holder.itemRecorderRmb.setText("兑换圣魔邮票"+data.getNum()+"张");
                break;
            case "3":
                holder.itemRecorderRmb.setText("兑换"+svips[Integer.parseInt(data.getType())-1]);
                break;
            case "4":
                holder.itemRecorderRmb.setText("兑换推顶卡"+data.getNum()+"张");
                break;
            case "5":
                holder.itemRecorderRmb.setText("兑换金魔豆"+data.getNum()+"个");
                break;

        }
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
