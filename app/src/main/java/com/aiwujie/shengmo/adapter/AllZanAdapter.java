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
import com.aiwujie.shengmo.bean.AllZanData;
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
public class AllZanAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<AllZanData.DataBean> list;
    private LayoutInflater inflater;

    public AllZanAdapter(Context context, List<AllZanData.DataBean> list) {
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
        AllZanData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewAllcommentIcon.setImageResource(R.mipmap.morentouxiang);
        }else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewAllcommentIcon, 0);
        }
        holder.itemListviewAllcommentName.setText(data.getNickname());
        holder.itemListviewAllcommentTime.setText(data.getAddtime());
        holder.itemListviewAllcommentIcon.setTag(position);
        holder.itemListviewAllcommentIcon.setOnClickListener(this);
        //holder.itemListviewAllarguementContent.setText(data.getNickname() + "赞了你的动态");
        String content = data.getNickname() + "赞了你的动态";
        TextViewUtil.setSpannedColorText(holder.itemListviewAllarguementContent,content,0,data.getNickname().length(), Color.parseColor("#db57f3"));
        if (!TextUtil.isEmpty(data.getPic())) {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.GONE);
            GlideImgManager.glideLoader(context,data.getPic(),R.mipmap.default_error,R.mipmap.default_error,holder.itemListviewAllcommentDynamiciv);
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
