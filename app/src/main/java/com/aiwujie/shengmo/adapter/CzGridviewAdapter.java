package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.RechargeData;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class CzGridviewAdapter extends BaseAdapter{
    private Context context;
    private List<RechargeData> list;
    private LayoutInflater inflater;
    public  int selectIndex=-1;

    public  void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public CzGridviewAdapter(Context context, List<RechargeData> list) {
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
            convertView=inflater.inflate(R.layout.item_listview_chongzhi, null);
            holder.tvMoney= (TextView) convertView.findViewById(R.id.item_listview_chongzhi_money);
            holder.tvModou= (TextView) convertView.findViewById(R.id.item_listview_chongzhi_modou);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.tvMoney.setText(list.get(position).getMoney()+"元");
        holder.tvModou.setText(list.get(position).getModou()+"金魔豆");
        if( selectIndex == position ){
            convertView.setBackgroundResource(R.drawable.item_chongzhi_bg_zi);
            holder.tvMoney.setTextColor(Color.parseColor("#b73acb"));
            holder.tvModou.setTextColor(Color.parseColor("#b73acb"));
        }else{
            convertView.setBackgroundResource(R.drawable.item_chongzhi_bg_hui);
            holder.tvMoney.setTextColor(Color.parseColor("#333333"));
            holder.tvModou.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvMoney;
        TextView tvModou;
    }
}
