package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class GroupKeywordGridviewAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private Handler handler = new Handler();

    public GroupKeywordGridviewAdapter(Context context, List<String> list) {
        super();
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
            convertView = inflater.inflate(R.layout.item_gridview_keyword, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemGvTv.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_gv_tv)
        TextView itemGvTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


