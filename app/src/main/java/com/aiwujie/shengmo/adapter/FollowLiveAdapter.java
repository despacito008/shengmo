package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.FollowLiveData;
import com.aiwujie.shengmo.utils.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/7/18.
 */

public class FollowLiveAdapter extends BaseAdapter {
    private Context context;
    private List<FollowLiveData.DataBean> list;
    private LayoutInflater inflater;

    public FollowLiveAdapter(Context context, List<FollowLiveData.DataBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_follow_live, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.default_error);
        requestOptions.error(R.mipmap.default_error);
        requestOptions.transform(new GlideRoundTransform(context,16));
        Glide.with(context).load(Uri.parse( list.get(position).getPullUrl())).apply(requestOptions).into(holder.liveImg);
        holder.nameTv.setText(list.get(position).getNickname());
        holder.timeTv.setText("已直播"+list.get(position).getLive_time()+"小时");
        holder.audienceNumTv.setText("历史最高"+list.get(position).getWatchsum()+"人观看");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.live_img)
        ImageView liveImg;
        @BindView(R.id.live_name)
        TextView nameTv;
        @BindView(R.id.audience_num)
        TextView audienceNumTv;
        @BindView(R.id.live_time)
        TextView timeTv;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
