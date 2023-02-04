package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.RedbagshouGroupBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class RedbagGroupAdapter extends BaseAdapter {
    private Context context;
    private List<RedbagshouGroupBean.DataBean.DatasBean> list;
    private LayoutInflater inflater;

    public RedbagGroupAdapter(Context context, List<RedbagshouGroupBean.DataBean.DatasBean> list) {
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
            convertView = inflater.inflate(R.layout.redbagshou_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RedbagshouGroupBean.DataBean.DatasBean data = list.get(position);
        Glide.with(context).load(data.getFhead_pic()).transform(new CenterCrop()).into(holder.iv_head);
        holder.tv_name.setText(data.getFnickname());
        String   format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        holder.tv_time.setText(sdf.format(new Date(Long.valueOf(list.get(position).getGettime())*1000))+"");
        holder.modou.setText(list.get(position).getNum()+"魔豆");

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
