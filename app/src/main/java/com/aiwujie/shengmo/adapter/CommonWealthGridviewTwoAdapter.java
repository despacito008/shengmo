package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.WealthQuestionData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class CommonWealthGridviewTwoAdapter extends BaseAdapter {
    private Context context;
    private List<WealthQuestionData.DataBean> list;
    private LayoutInflater inflater;

    public CommonWealthGridviewTwoAdapter(Context context, List<WealthQuestionData.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_gridview_two_common_wealth, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WealthQuestionData.DataBean data=list.get(position);
        holder.itemGridviewTwoCommonWealthTv.setText(data.getTitle());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_gridview_two_common_wealth_tv)
        TextView itemGridviewTwoCommonWealthTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
