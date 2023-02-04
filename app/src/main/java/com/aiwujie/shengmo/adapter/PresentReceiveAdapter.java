package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.PresentReceiveData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class PresentReceiveAdapter extends BaseAdapter {
    private Context context;
    private List<PresentReceiveData.DataBean> list;
    private LayoutInflater inflater;
    private String[] titles = {"棒棒糖", "狗粮", "雪糕", "黄瓜", "心心相印", "香蕉", "口红", "亲一个", "玫瑰花", "眼罩",
            "心灵束缚", "黄金", "拍之印", "鞭之痕", "一飞冲天", "一生一世", "水晶高跟", "恒之光", "666", "红酒", "蛋糕", "钻戒", "皇冠",
            "跑车", "直升机", "游轮", "城堡", "幸运草", "糖果", "玩具狗", "内内", "TT","未知礼物","未知礼物","未知礼物","未知礼物","未知礼物"};
    public PresentReceiveAdapter(Context context, List<PresentReceiveData.DataBean> list) {
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
        PresentReceiveData.DataBean data=list.get(position);
        holder.itemRecorderDate.setText(data.getAddtime_format());
        holder.itemRecorderDay.setText(data.getWeek());
        if(data.getBeans().equals("0")&&data.getAmount().equals("0")){
            holder.itemRecorderModou.setText("系统礼物");
        }else {
            holder.itemRecorderModou.setText("+" + data.getBeans());
        }
       // holder.itemRecorderRmb.setText(data.getNickname() + "赠[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum());
        holder.itemRecorderRmb.setText(data.getType_name());
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
