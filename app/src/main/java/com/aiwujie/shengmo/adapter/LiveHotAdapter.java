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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/6/2.
 */

public class LiveHotAdapter extends BaseAdapter {
    private Context context;

    private List<LiveAnchorData.HotAnchor> data = new ArrayList<LiveAnchorData.HotAnchor>();

    public LiveHotAdapter(List<LiveAnchorData.HotAnchor> data, Context context) {
        super();
        this.context = context;
        this.data = data;

    }

    @Override
    public int getCount() {

        if (null != data) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
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
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_hot_live, null);

            // 初始化组件
//            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tab_text);
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
//        viewHolder.image.setImageResource(picture.getImageId());
        viewHolder.title.setText(data.get(position).getNickname());

        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
    }

}
