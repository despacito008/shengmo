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
import com.aiwujie.shengmo.bean.GroupOperationData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/18.
 */
public class GroupOperationAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<GroupOperationData.DataBean> list;
    private LayoutInflater inflater;
    private View.OnClickListener onConfirmClick;
    private View.OnClickListener onRefuseClick;

    public void setOnConfirmClick(View.OnClickListener onConfirmClick) {
        this.onConfirmClick = onConfirmClick;
    }

    public void setOnRefuseClick(View.OnClickListener onRefuseClick) {
        this.onRefuseClick = onRefuseClick;
    }

    public GroupOperationAdapter(Context context, List<GroupOperationData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_group_operation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupOperationData.DataBean data = list.get(position);
        if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemGroupOperationIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemGroupOperationIcon, 0);
        }
        holder.itemGroupOperationContent.setText(data.getContent());
        switch (data.getState()) {
            case "0":
                holder.itemGroupOperationLltime.setVisibility(View.GONE);
                holder.itemGroupOperationLlMsg.setVisibility(View.GONE);
                holder.itemGroupOperationConfirm.setVisibility(View.GONE);
                holder.itemGroupOperationRefuse.setVisibility(View.GONE);
                holder.itemGroupOperationYichuli.setVisibility(View.GONE);
                holder.itemGroupOperationYichuli2.setVisibility(View.GONE);
                break;
            case "1":
                holder.itemGroupOperationLltime.setVisibility(View.VISIBLE);
                holder.itemGroupOperationLlMsg.setVisibility(View.VISIBLE);
                holder.itemGroupOperationtime.setText(""+data.getOperatortime());
                holder.itemGroupOperationMsg.setText("附加信息:" + data.getMsg());
                holder.itemGroupOperationConfirm.setVisibility(View.VISIBLE);
                holder.itemGroupOperationRefuse.setVisibility(View.VISIBLE);
                holder.itemGroupOperationYichuli.setVisibility(View.GONE);
                holder.itemGroupOperationYichuli2.setVisibility(View.GONE);
                holder.itemGroupOperationConfirm.setOnClickListener(onConfirmClick);
                holder.itemGroupOperationRefuse.setOnClickListener(onRefuseClick);
                holder.itemGroupOperationConfirm.setTag(position);
                holder.itemGroupOperationRefuse.setTag(position);
                break;
            case "2":
                holder.itemGroupOperationLltime.setVisibility(View.VISIBLE);
                holder.itemGroupOperationLlMsg.setVisibility(View.VISIBLE);
                holder.itemGroupOperationtime.setText(""+data.getOperatortime());
                holder.itemGroupOperationMsg.setText("附加信息:" + data.getMsg());
                holder.itemGroupOperationConfirm.setVisibility(View.GONE);
                holder.itemGroupOperationRefuse.setVisibility(View.GONE);
                holder.itemGroupOperationYichuli.setText("已同意");
                holder.itemGroupOperationYichuli.setVisibility(View.VISIBLE);
                holder.itemGroupOperationYichuli.setBackground(context.getResources().getDrawable(R.drawable.bg_group_request_done));
                holder.itemGroupOperationYichuli.setTextColor(context.getResources().getColor(R.color.normalGray));
                holder.itemGroupOperationYichuli2.setText(""+data.getNickname());
                holder.itemGroupOperationYichuli2.setVisibility(View.VISIBLE);
                break;
            case "3":
                holder.itemGroupOperationLltime.setVisibility(View.VISIBLE);
                holder.itemGroupOperationLlMsg.setVisibility(View.VISIBLE);
                holder.itemGroupOperationtime.setText(""+data.getOperatortime());
                holder.itemGroupOperationMsg.setText("附加信息:" + data.getMsg());
                holder.itemGroupOperationConfirm.setVisibility(View.GONE);
                holder.itemGroupOperationRefuse.setVisibility(View.GONE);
                holder.itemGroupOperationYichuli.setText("已拒绝");
                holder.itemGroupOperationYichuli.setVisibility(View.VISIBLE);
                holder.itemGroupOperationYichuli.setBackground(context.getResources().getDrawable(R.drawable.bg_group_request_refuse));
                holder.itemGroupOperationYichuli.setTextColor(context.getResources().getColor(R.color.redOrange));
                holder.itemGroupOperationYichuli2.setText(""+data.getNickname());
                holder.itemGroupOperationYichuli2.setVisibility(View.VISIBLE);
                break;
        }

        holder.itemGroupOperationIcon.setTag(position);
        holder.itemGroupOperationIcon.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos= (int) v.getTag();
        switch (v.getId()){
            case R.id.item_group_operation_icon:
                if (!list.get(pos).getOther_uid().equals("0")) {
                    Intent intent = new Intent(context, PesonInfoActivity.class);
                    intent.putExtra("uid", list.get(pos).getOther_uid());
                    context.startActivity(intent);
                }
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_group_operation_icon)
        ImageView itemGroupOperationIcon;
        @BindView(R.id.item_group_operation_content)
        TextView itemGroupOperationContent;
        @BindView(R.id.item_group_operation_msg)
        TextView itemGroupOperationMsg;
        @BindView(R.id.item_group_operation_time)
        TextView itemGroupOperationtime;
        @BindView(R.id.item_group_operation_ll_msg)
        PercentLinearLayout itemGroupOperationLlMsg;
        @BindView(R.id.item_group_operation_ll_time)
        PercentLinearLayout itemGroupOperationLltime;
        @BindView(R.id.item_group_operation_confirm)
        TextView itemGroupOperationConfirm;
        @BindView(R.id.item_group_operation_refuse)
        TextView itemGroupOperationRefuse;
        @BindView(R.id.item_group_operation_yichuli)
        TextView itemGroupOperationYichuli;
        @BindView(R.id.item_group_operation_yichul2)
        TextView itemGroupOperationYichuli2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
