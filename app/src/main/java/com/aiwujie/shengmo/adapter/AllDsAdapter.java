package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.AllDsData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.TextViewUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class AllDsAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<AllDsData.DataBean> list;
    private LayoutInflater inflater;

    public AllDsAdapter(Context context, List<AllDsData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_all_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AllDsData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewAllcommentIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewAllcommentIcon, 0);
        }
        holder.itemListviewAllcommentName.setText(data.getNickname());
        holder.itemListviewAllcommentTime.setText(data.getAddtime());
        holder.itemListviewAllcommentIcon.setTag(position);
        holder.itemListviewAllcommentIcon.setOnClickListener(this);
        switch (data.getPsid()) {
            case "0":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你" + data.getAmount() + "个魔豆");
                break;
            case "1":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个握手");
                break;
            case "2":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个黄瓜");
                break;
            case "3":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个玫瑰");
                break;
            case "4":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个送吻");
                break;
            case "5":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个红酒");
                break;
            case "6":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个对戒");
                break;
            case "7":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个蛋糕");
                break;
            case "8":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个跑车");
                break;
            case "9":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一个游轮");
                break;
            case "10":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你棒棒糖" + "x" + data.getNum());
                break;
            case "11":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你狗粮" + "x" + data.getNum());
                break;
            case "12":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你冰淇淋" + "x" + data.getNum());
                break;
            case "13":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你黄瓜" + "x" + data.getNum());
                break;
            case "14":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你心心相印" + "x" + data.getNum());
                break;
            case "15":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你香蕉" + "x" + data.getNum());
                break;
            case "16":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你口红" + "x" + data.getNum());
                break;
            case "17":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你亲一个" + "x" + data.getNum());
                break;
            case "18":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你玫瑰花" + "x" + data.getNum());
                break;
            case "19":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你眼罩" + "x" + data.getNum());
                break;
            case "20":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你心灵束缚" + "x" + data.getNum());
                break;
            case "21":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你黄金" + "x" + data.getNum());
                break;
            case "22":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你拍之印" + "x" + data.getNum());
                break;
            case "23":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你鞭之痕" + "x" + data.getNum());
                break;
            case "24":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你一飞冲天" + "x" + data.getNum());
                break;
            case "25":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了一生一世" + "x" + data.getNum());
                break;
            case "26":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你水晶高跟" + "x" + data.getNum());
                break;
            case "27":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你恒之光" + "x" + data.getNum());
                break;
            case "28":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你666" + "x" + data.getNum());
                break;
            case "29":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你红酒" + "x" + data.getNum());
                break;
            case "30":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你蛋糕" + "x" + data.getNum());
                break;
            case "31":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你钻戒" + "x" + data.getNum());
                break;
            case "32":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你皇冠" + "x" + data.getNum());
                break;
            case "33":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你跑车" + "x" + data.getNum());
                break;
            case "34":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你直升机" + "x" + data.getNum());
                break;
            case "35":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你游轮" + "x" + data.getNum());
                break;
            case "36":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你城堡" + "x" + data.getNum());
                break;
            case "37":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你幸运草" + "x" + data.getNum());
                break;
            case "38":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你糖果" + "x" + data.getNum());
                break;
            case "39":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你雪糕" + "x" + data.getNum());
                break;
            case "40":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你内内" + "x" + data.getNum());
                break;
            case "41":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你TT" + "x" + data.getNum());
                break;
            case "42":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你未知礼物" + "x" + data.getNum());
                break;
            case "43":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你未知礼物" + "x" + data.getNum());
                break;
            case "44":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你未知礼物" + "x" + data.getNum());
                break;
            case "45":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你未知礼物" + "x" + data.getNum());
                break;
            case "46":
                holder.itemListviewAllarguementContent.setText(data.getNickname() + "打赏了你未知礼物" + "x" + data.getNum());
                break;
        }
        String content = holder.itemListviewAllarguementContent.getText().toString();
        TextViewUtil.setSpannedColorText(holder.itemListviewAllarguementContent,content,0,data.getNickname().length(), Color.parseColor("#db57f3"));

        if (!TextUtil.isEmpty(data.getPic())) {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.GONE);
            GlideImgManager.glideLoader(context, data.getPic(), R.mipmap.default_error, R.mipmap.default_error, holder.itemListviewAllcommentDynamiciv);
        } else {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.GONE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setText(data.getContent());
        }
        holder.tvItemApply.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent = new Intent(context, PesonInfoActivity.class);
        intent.putExtra("uid", list.get(pos).getUid());
        context.startActivity(intent);
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_allcomment_icon)
        ImageView itemListviewAllcommentIcon;
        @BindView(R.id.item_listview_allcomment_vip)
        ImageView itemListviewAllcommentVip;
        @BindView(R.id.item_listview_allcomment_name)
        TextView itemListviewAllcommentName;
        @BindView(R.id.item_listview_allcomment_time)
        TextView itemListviewAllcommentTime;
        @BindView(R.id.item_listview_allarguement_content)
        TextView itemListviewAllarguementContent;
        @BindView(R.id.item_listview_allcomment_dynamiccontent)
        TextView itemListviewAllcommentDynamiccontent;
        @BindView(R.id.item_listview_allcomment_dynamiciv)
        ImageView itemListviewAllcommentDynamiciv;
        @BindView(R.id.tv_item_all_comment_apply)
        TextView tvItemApply;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
