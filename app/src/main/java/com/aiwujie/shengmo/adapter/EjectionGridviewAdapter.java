package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.EjectionaData;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class EjectionGridviewAdapter extends BaseAdapter{
    private Context context;
    private List<EjectionaData> list;
    private LayoutInflater inflater;
    public  int selectIndex=-1;

    public  void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public EjectionGridviewAdapter(Context context, List<EjectionaData> list) {
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
            convertView=inflater.inflate(R.layout.item_listview_tuidingka, null);
            holder.tvMoney= (TextView) convertView.findViewById(R.id.item_listview_chongzhi_money);
            holder.tvModou= (TextView) convertView.findViewById(R.id.item_listview_chongzhi_modou);
            holder.yuanmodou= (TextView) convertView.findViewById(R.id.item_listview_chongzhi_yuanmodou);
            holder.jin = convertView.findViewById(R.id.item_listview_chongzhi_jin);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.tvMoney.setText(list.get(position).getMoney());
        holder.tvModou.setText(list.get(position).getModou());
        holder.yuanmodou.setText(list.get(position).getYuanmodou());
        holder.jin.setText(list.get(position).getJinyuan());
        holder.yuanmodou.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        if( selectIndex == position ){
            convertView.setBackgroundResource(R.drawable.item_chongzhi_bg_zi);
            holder.tvMoney.setTextColor(Color.parseColor("#b73acb"));
            holder.tvModou.setTextColor(Color.parseColor("#b73acb"));
            holder.yuanmodou.setTextColor(Color.parseColor("#b73acb"));
            holder.jin.setTextColor(Color.parseColor("#b73acb"));
        }else{
            convertView.setBackgroundResource(R.drawable.item_chongzhi_bg_hui);
            holder.jin.setTextColor(Color.parseColor("#333333"));
            holder.tvMoney.setTextColor(Color.parseColor("#333333"));
            if(holder.jin.getText().toString().length()==10){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.jin.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.jin.setText(builder01);
            }else if(holder.jin.getText().toString().length()==8){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.jin.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.jin.setText(builder01);
            }
            if(list.get(position).getNum().equals("3")){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.tvMoney.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvMoney.setText(builder01);
            }else if(list.get(position).getNum().equals("10")){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.tvMoney.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvMoney.setText(builder01);
            }else if(list.get(position).getNum().equals("30")){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.tvMoney.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvMoney.setText(builder01);
            }else if(list.get(position).getNum().equals("90")){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.tvMoney.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvMoney.setText(builder01);
            }else if(list.get(position).getNum().equals("270")){
                SpannableStringBuilder builder01 = new SpannableStringBuilder(holder.tvMoney.getText().toString());
                builder01.setSpan(new ForegroundColorSpan(Color.RED), 5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvMoney.setText(builder01);
            }

            holder.tvModou.setTextColor(Color.parseColor("#333333"));
            holder.yuanmodou.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvMoney;
        TextView tvModou;
        TextView yuanmodou;
        TextView jin;
    }
}
