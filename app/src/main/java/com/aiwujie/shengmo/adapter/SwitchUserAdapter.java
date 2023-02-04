package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.SwitchMarkBean;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class SwitchUserAdapter extends BaseAdapter {
    private Context context;
    private List<SwitchMarkBean> list;
    private LayoutInflater inflater;

    public SwitchUserAdapter(Context context, List<SwitchMarkBean> list) {
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
            convertView = inflater.inflate(R.layout.item_switch_user_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SwitchMarkBean switchMarkBean = list.get(position);

        GlideImgManager.glideLoader(context, switchMarkBean.getHeadimage(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.item_listview_allcomment_icon, 0);
        holder.item_listview_allcomment_name.setText(""+switchMarkBean.getUser_name());
        holder.item_listview_allcomment_time.setText(""+switchMarkBean.getUser_id());
        if(String.valueOf(switchMarkBean.getUid()).equals(MyApp.uid)){
            holder.ivdui.setVisibility(View.VISIBLE);
        }else {
            holder.ivdui.setVisibility(View.GONE);
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_allcomment_icon)
        ImageView item_listview_allcomment_icon;
        @BindView(R.id.item_listview_allcomment_name)
        TextView item_listview_allcomment_name;
        @BindView(R.id.item_listview_allcomment_time)
        TextView item_listview_allcomment_time;
        @BindView(R.id.ivdui)
        ImageView ivdui;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
