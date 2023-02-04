package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.PresentSystemData;
import com.aiwujie.shengmo.utils.TextViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class PresentReceiveSystemAdapter extends BaseAdapter {
    private Context context;
    private List<PresentSystemData.DataBean> list;
    private LayoutInflater inflater;
    private String[] titles = {"棒棒糖", "狗粮", "雪糕", "黄瓜", "心心相印", "香蕉", "口红", "亲一个", "玫瑰花", "眼罩",
            "心灵束缚", "黄金", "拍之印", "鞭之痕", "一飞冲天", "一生一世", "水晶高跟", "恒之光", "666", "红酒", "蛋糕", "钻戒", "皇冠",
            "跑车", "直升机", "游轮", "城堡", "幸运草", "糖果", "玩具狗", "内内", "TT","未知礼物","未知礼物","未知礼物","未知礼物","未知礼物"};
    private String[] moneys = {"2魅力","6魅力","10魅力","38魅力","99魅力","88魅力","123魅力","166魅力","199魅力","520魅力","666魅力","250魅力","777魅力","888魅力","999魅力","1314魅力","1666魅力","1999魅力","666魅力","999魅力","1888魅力","2899魅力","3899魅力","6888魅力","9888魅力","52000魅力","99999魅力"  , "1魅力", "3魅力", "5魅力", "10魅力", "8魅力","未知","未知","未知","未知","未知"};

    public PresentReceiveSystemAdapter(Context context, List<PresentSystemData.DataBean> list) {
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
        PresentSystemData.DataBean data=list.get(position);
        holder.itemRecorderDate.setText(data.getAddtime_format());
        holder.itemRecorderDay.setText(data.getWeek());
        //holder.itemRecorderModou.setText("+" +moneys[Integer.parseInt(data.getType())-10]);
        holder.itemRecorderModou.setText("系统礼物");
        holder.ivImageBean.setVisibility(View.GONE);
        holder.itemRecorderRmb.setText("系统赠[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum());
        TextViewUtil.setNormal(holder.itemRecorderModou);
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
        ImageView ivImageBean;



        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
