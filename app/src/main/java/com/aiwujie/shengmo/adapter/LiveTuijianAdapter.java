package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.LiveAnchorData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/6/2.
 */

public class LiveTuijianAdapter extends BaseAdapter {
    private Context context;
    private List<LiveAnchorData.RecommendAnchor> recommendAnchor;


    public LiveTuijianAdapter(Context context, List<LiveAnchorData.RecommendAnchor> recommendAnchor) {
        super();
        this.context = context;
        this.recommendAnchor = recommendAnchor;
    }

    @Override
    public int getCount() {

        if (null != recommendAnchor) {
            return recommendAnchor.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        return recommendAnchor.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_tuijian_live, null);

            // 初始化组件
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
        Glide.with(context).load(recommendAnchor.get(position).getPullUrl()).into(viewHolder.image);
        viewHolder.title.setText(recommendAnchor.get(position).getNickname());

        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
    }

    class Picture {

        private String title;
        private int imageId;

        public Picture(String title, Integer imageId) {
            this.imageId = imageId;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public int getImageId() {
            return imageId;
        }

    }
}
