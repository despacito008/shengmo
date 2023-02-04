package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.AtData;
import com.aiwujie.shengmo.utils.DateUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/5/4.
 */

public class AtMineAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<AtData> list;
    private LayoutInflater inflater;

    public AtMineAdapter(Context context, List<AtData> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_at_mine, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AtData data = list.get(position);
        if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewAtMineIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewAtMineIcon, 0);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(data.getNickname() + "在动态中提到了你");
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
        builder.setSpan(purSpan, 0, data.getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.itemListviewAtMineName.setText(builder);
        holder.itemListviewAtMineTime.setText(DateUtils.toDate(data.getAddtime()));
        holder.itemListviewAtMineIcon.setTag(position);
        holder.itemListviewAtMineIcon.setOnClickListener(this);
        if (data.getContent().equals("")) {
            holder.itemListviewAtMineDynamiciv.setVisibility(View.VISIBLE);
            holder.itemListviewAtMineDynamiccontent.setVisibility(View.GONE);
            GlideImgManager.glideLoader(context, NetPic() + data.getPic(), R.mipmap.default_error, R.mipmap.default_error, holder.itemListviewAtMineDynamiciv);
        } else {
            holder.itemListviewAtMineDynamiciv.setVisibility(View.GONE);
            holder.itemListviewAtMineDynamiccontent.setVisibility(View.VISIBLE);
            holder.itemListviewAtMineDynamiccontent.setText(data.getContent());
        }
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
        @BindView(R.id.item_listview_at_mine_icon)
        ImageView itemListviewAtMineIcon;
        @BindView(R.id.item_listview_at_mine_vip)
        ImageView itemListviewAtMineVip;
        @BindView(R.id.item_listview_at_mine_name)
        TextView itemListviewAtMineName;
        @BindView(R.id.item_listview_at_mine_time)
        TextView itemListviewAtMineTime;
        @BindView(R.id.item_listview_at_mine_dynamiccontent)
        TextView itemListviewAtMineDynamiccontent;
        @BindView(R.id.item_listview_at_mine_dynamiciv)
        ImageView itemListviewAtMineDynamiciv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
