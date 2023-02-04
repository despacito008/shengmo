package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.HomeGridviewData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class HomeGridviewAdapter extends BaseAdapter {
    private Context context;
    private List<HomeGridviewData.DataBean> list;
    private LayoutInflater inflater;

    public HomeGridviewAdapter(Context context, List<HomeGridviewData.DataBean> list, int retcode) {
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
            convertView = inflater.inflate(R.layout.item_near_gridview, null);//换一个布局   gridview变成listview
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.size() != 0) {
            HomeGridviewData.DataBean data = list.get(position);
            if (data.getHead_pic().equals("")) {
                holder.ivHead.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.ivHead, 1);
            }
            if ("1".equals(data.getLocation_city_switch())) {
                holder.tvAddress.setVisibility(View.GONE);
            } else {
                if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                    holder.tvAddress.setVisibility(View.GONE);

                } else {
                    holder.tvAddress.setVisibility(View.VISIBLE);
                    if (data.getProvince().equals(data.getCity())||!TextUtil.isEmpty(data.getCity())) {
                        holder.tvAddress.setText(data.getCity());
                    } else  {
                        holder.tvAddress.setText(data.getProvince());
                    }
                }
            }
            holder.tvItemSex.setText(data.getAge());
            if (data.getOnlinestate() == 0) {
                holder.viewOnlineState.setVisibility(View.INVISIBLE);
            } else {
                holder.viewOnlineState.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.itemIdentityIcon);
            if (data.getSex().equals("1")) {
                holder.tvItemSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if (data.getSex().equals("2")) {
                holder.tvItemSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if (data.getSex().equals("3")) {
                holder.tvItemSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
            }


        }
        return convertView;
    }


    static class ViewHolder {
        ImageView ivHead;
        ImageView itemIdentityIcon;
        View viewOnlineState;
        TextView tvItemSex;
        TextView tvAddress;

        public ViewHolder(View convertView) {
            ivHead = convertView.findViewById(R.id.iv_head);
            itemIdentityIcon = convertView.findViewById(R.id.item_identity_icon);
            viewOnlineState = convertView.findViewById(R.id.view_online_state);
            tvAddress = convertView.findViewById(R.id.tv_address);
            tvItemSex = convertView.findViewById(R.id.tv_item_sex);


        }
    }
}


