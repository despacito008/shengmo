package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.NoticePresentData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.bumptech.glide.Glide;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/9.
 */
public class PresentListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<NoticePresentData.DataBean> list;
    private LayoutInflater inflater;
    private int[] presents = {R.mipmap.presentnew01, R.mipmap.presentnew02, R.mipmap.presentnew03, R.mipmap.presentnew04, R.mipmap.presentnew05,
            R.mipmap.presentnew06, R.mipmap.presentnew07, R.mipmap.presentnew08, R.mipmap.presentnew09,
            R.mipmap.presentnew10, R.mipmap.presentnew11, R.mipmap.presentnew12, R.mipmap.presentnew13,
            R.mipmap.presentnew14, R.mipmap.presentnew15, R.mipmap.presentnew16, R.mipmap.presentnew17,
            R.mipmap.presentnew18, R.mipmap.presentnew19, R.mipmap.presentnew20, R.mipmap.presentnew21,
            R.mipmap.presentnew22, R.mipmap.presentnew23, R.mipmap.presentnew24, R.mipmap.presentnew25,
            R.mipmap.presentnew26, R.mipmap.presentnew27, R.mipmap.presentnew28, R.mipmap.presentnew29, R.mipmap.presentnew30,
            R.mipmap.presentnew31, R.mipmap.presentnew32, R.mipmap.weizhiliwu, R.mipmap.weizhiliwu, R.mipmap.weizhiliwu, R.mipmap.weizhiliwu, R.mipmap.weizhiliwu};
    private String[] titles = {"棒棒糖", "狗粮", "雪糕", "黄瓜", "心心相印", "香蕉", "口红", "亲一个", "玫瑰花", "眼罩",
            "心灵束缚", "黄金", "拍之印", "鞭之痕", "一飞冲天", "一生一世", "水晶高跟", "恒之光", "666", "红酒", "蛋糕", "钻戒", "皇冠",
            "跑车", "直升机", "游轮", "城堡", "幸运草", "糖果", "玩具狗", "内内", "TT", "未知礼物", "未知礼物", "未知礼物", "未知礼物", "未知礼物"};

    public PresentListAdapter(Context context, List<NoticePresentData.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_notice_present, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NoticePresentData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewNoticePresentSendicon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewNoticePresentSendicon, 0);
        }
        if (data.getFhead_pic().equals("") || data.getFhead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewNoticePresentOthericon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getFhead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewNoticePresentOthericon, 0);
        }
       // holder.itemListviewNoticePresentIcon.setImageResource(presents[Integer.parseInt(data.getType()) - 10]);
       // holder.itemListviewNoticePresentName.setText(titles[Integer.parseInt(data.getType()) - 10] + "x" + data.getNum());
        if ("1".equals(data.getNum())) {
            holder.itemListviewNoticePresentName.setText(data.getGift_name() + " (" + data.getBeans() + "魔豆)");
        } else {
            holder.itemListviewNoticePresentName.setText(data.getGift_name() + "x" + data.getNum() + " (" + data.getBeans() + "魔豆)");
        }
        Glide.with(context).load(data.getGift_image()).into(holder.itemListviewNoticePresentIcon);
        holder.itemListviewNoticePresentSendname.setText(data.getNickname());
        holder.itemListviewNoticePresentReceivename.setText(data.getFnickname());
        holder.itemListviewNoticePresentSendtime.setText(data.getAddtime());
        holder.itemListviewNoticePresentSendicon.setTag(position);
        holder.itemListviewNoticePresentSendicon.setOnClickListener(this);
        holder.itemListviewNoticePresentOthericon.setTag(position);
        holder.itemListviewNoticePresentOthericon.setOnClickListener(this);
        holder.itemListviewNoticePresentPlace.setText(data.getSource_type());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_notice_present_sendicon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_notice_present_othericon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getFuid());
                context.startActivity(intent);
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_notice_present_sendname)
        TextView itemListviewNoticePresentSendname;
        @BindView(R.id.item_listview_notice_present_receivename)
        TextView itemListviewNoticePresentReceivename;
        @BindView(R.id.item_listview_notice_present_name)
        TextView itemListviewNoticePresentName;
        @BindView(R.id.item_listview_notice_present_sendicon)
        ImageView itemListviewNoticePresentSendicon;
        @BindView(R.id.item_listview_notice_present_icon)
        ImageView itemListviewNoticePresentIcon;
        @BindView(R.id.item_listview_notice_present_othericon)
        ImageView itemListviewNoticePresentOthericon;
        @BindView(R.id.item_listview_notice_present_sendtime)
        TextView itemListviewNoticePresentSendtime;
        @BindView(R.id.item_listview_notice_present_place)
        TextView itemListviewNoticePresentPlace;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
