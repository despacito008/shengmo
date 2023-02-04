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
import com.aiwujie.shengmo.bean.Group_liwulistbean;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 290243232 on 2017/1/23.
 */
public class GroupliwulistAdapter extends BaseAdapter {
    private Context context;
    private List<Group_liwulistbean.DataBean.DatasBean> list;
    private LayoutInflater inflater;

    public GroupliwulistAdapter(Context context, List<Group_liwulistbean.DataBean.DatasBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.redbagshou_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Group_liwulistbean.DataBean.DatasBean data = list.get(position);
        //Glide.with(context).load(data.getFhead_pic()).centerCrop().into(holder.iv_head);
        ImageLoader.loadCircleImage(context,data.getFhead_pic(),holder.iv_head);
        holder.tv_name.setText(data.getFnickname());
         String   format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        holder.tv_time.setText(sdf.format(new Date(Long.valueOf(list.get(position).getGettime())*1000))+"");
        holder.modou.setText("");
        holder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(position).getFuid());
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    static class ViewHolder {

        @BindView(R.id.iv_head)
        ImageView iv_head;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.modou)
        TextView modou;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
