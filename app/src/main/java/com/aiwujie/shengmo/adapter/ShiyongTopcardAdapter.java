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
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class ShiyongTopcardAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<DynamicDetailBean.DataBean> list;
    private LayoutInflater inflater;

    public ShiyongTopcardAdapter(Context context, List<DynamicDetailBean.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_shiyong_tuiding, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DynamicDetailBean.DataBean data = list.get(position);

        String sum = data.getSum();
        if (null!=sum&&!"".equals(sum)&&!sum.equals("1")){
            holder.tv_tuijige.setText(data.getUse_sum()+"次/"+data.getSum()+"卡/"+data.getInterval());
        }else {
            holder.tv_tuijige.setVisibility(View.GONE);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(Long.valueOf(data.getAddtime())*1000);
        Date date = new Date(lt);
        holder.itemListviewAllcommentTime.setText(simpleDateFormat.format(date));

        if (data.getContent().equals("")) {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.GONE);
            GlideImgManager.glideLoader(context,data.getPic(),R.mipmap.default_error,R.mipmap.default_error,holder.itemListviewAllcommentDynamiciv);
        } else {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.GONE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setText(data.getContent());
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent = new Intent(context, PesonInfoActivity.class);
        intent.putExtra("uid", MyApp.uid);
        context.startActivity(intent);
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_allcomment_time)
        TextView itemListviewAllcommentTime;
        @BindView(R.id.item_listview_allcomment_dynamiccontent)
        TextView itemListviewAllcommentDynamiccontent;
        @BindView(R.id.tv_tuijige)
        TextView tv_tuijige;
        @BindView(R.id.item_listview_allcomment_dynamiciv)
        ImageView itemListviewAllcommentDynamiciv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
