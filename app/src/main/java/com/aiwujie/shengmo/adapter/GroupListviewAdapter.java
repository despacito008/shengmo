package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class GroupListviewAdapter extends BaseAdapter {
    private Context context;
    private List<GroupData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;

    public GroupListviewAdapter(Context context, List<GroupData.DataBean> list, int retcode) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
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
            convertView = inflater.inflate(R.layout.item_listview_group, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupData.DataBean data = list.get(position);
        if (retcode == 2000) {
            holder.itemGroupListviewDistance.setVisibility(View.VISIBLE);
            holder.itemGroupListviewDistance.setText(data.getDistance() + "km");
        } else {
            holder.itemGroupListviewDistance.setVisibility(View.INVISIBLE);
        }
        if (data.getGroup_pic().equals("http:\\/\\/aiwujie.com.cn\\/")) {//"http:\\/\\/59.110.28.150:888\\/"
            holder.itemGroupListviewIcon.setImageResource(R.mipmap.qunmorentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getGroup_pic(), R.mipmap.qunmorentouxiang, R.mipmap.qunmorentouxiang, holder.itemGroupListviewIcon, 0);
        }
        holder.itemGroupListviewName.setText(data.getGroupname());
        holder.itemGroupListviewPeoplecount.setText(data.getMember() + "人");
        if (!data.getIntroduce().equals("")) {
            holder.itemGroupListviewSign.setText(data.getIntroduce());
        } else {
            holder.itemGroupListviewSign.setText("(该群未填写群介绍)");
        }
        if ("1".equals(data.getIs_claim())) {
            holder.itemGroupListviewClaim.setVisibility(View.VISIBLE);
        } else {
            holder.itemGroupListviewClaim.setVisibility(View.GONE);
        }
        if(data.getGroup_num().length()<=5){
            holder.itemGroupListviewVip.setVisibility(View.VISIBLE);
        }else{
            holder.itemGroupListviewVip.setVisibility(View.GONE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_group_listview_icon)
        ImageView itemGroupListviewIcon;
        @BindView(R.id.item_group_listview_vip)
        ImageView itemGroupListviewVip;
        @BindView(R.id.item_group_listview_name)
        TextView itemGroupListviewName;
        @BindView(R.id.item_group_listview_peoplecount)
        TextView itemGroupListviewPeoplecount;
        @BindView(R.id.item_group_listview_distance)
        TextView itemGroupListviewDistance;
        @BindView(R.id.item_group_listview_sign)
        TextView itemGroupListviewSign;
        @BindView(R.id.item_group_listview_claim)
        TextView itemGroupListviewClaim;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


