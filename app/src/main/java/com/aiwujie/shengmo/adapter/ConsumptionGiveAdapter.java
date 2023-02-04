package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ConsumptionGiveData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class ConsumptionGiveAdapter extends BaseAdapter {
    private Context context;
    private List<ConsumptionGiveData.DataBean> list;
    private LayoutInflater inflater;
    private String[] titles = {"棒棒糖", "狗粮", "雪糕", "黄瓜", "心心相印", "香蕉", "口红", "亲一个", "玫瑰花", "眼罩",
            "心灵束缚", "黄金", "拍之印", "鞭之痕", "一飞冲天", "一生一世", "水晶高跟", "恒之光", "666", "红酒", "蛋糕", "钻戒", "皇冠",
            "跑车", "直升机", "游轮", "城堡", "幸运草", "糖果", "玩具狗", "内内", "TT","未知礼物","未知礼物","未知礼物","未知礼物","未知礼物"};
    private String[] vips={"VIP1个月","VIP3个月","VIP6个月","VIP12个月"};
    private String[] svips={"SVIP1个月","SVIP3个月","SVIP6个月","SVIP12个月"};
    public ConsumptionGiveAdapter(Context context, List<ConsumptionGiveData.DataBean> list) {
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
        ConsumptionGiveData.DataBean data=list.get(position);
        holder.itemRecorderDate.setText(data.getAddtime_format());
        holder.itemRecorderDay.setText(data.getWeek());
        if(data.getAmount().equals("0")&&data.getBeans().equals("0")){
            holder.itemRecorderModou.setText("系统赠送");
//            switch (data.getState()) {
//                case "1":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + "[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum());
//                    break;
//                case "2":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + vips[Integer.parseInt(data.getType()) - 1]);
//                    break;
//                case "3":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + svips[Integer.parseInt(data.getType()) - 1]);
//                    break;
//                case "4":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + "[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum());
//                    break;
//                case "5":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() +"[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum());
//                    break;
//                case "6":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() +"红包");
//                    break;
//            }
            holder.itemRecorderRmb.setText(data.getType_name());
        }else{
            holder.itemRecorderModou.setText("-"+data.getBeans()+"魔豆");
//            switch (data.getState()) {
//                case "1":
//                    if (data.getType().equals("0")) {
//                        holder.itemRecorderRmb.setText("赠" + data.getNickname() + "[" + "直播间弹幕" + "]x" + data.getNum());
//                    } else {
//                        holder.itemRecorderRmb.setText("赠" + data.getNickname() + "[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum());
//                    }
//                    break;
//                case "2":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + vips[Integer.parseInt(data.getType()) - 1] );
//                    break;
//                case "3":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + svips[Integer.parseInt(data.getType()) - 1] );
//                    break;
//                case "4":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() + "[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum() );
//                    break;
//                case "5":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() +"[" + titles[Integer.parseInt(data.getType()) - 10] + "]x" + data.getNum() );
//                    break;
//                case "6":
//                    holder.itemRecorderRmb.setText("赠" + data.getNickname() +"红包");
//                    break;
//            }
            holder.itemRecorderRmb.setText(data.getType_name());
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
