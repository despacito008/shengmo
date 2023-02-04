package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.ArrayList;
import java.util.List;


public class ShareGroupRecyclerAdapter extends RecyclerView.Adapter<ShareGroupRecyclerAdapter.AtGroupHolder>  {
    Context context;
    List<GroupData.DataBean> groupList;
    List<Integer> chooseIndexList;

    public ShareGroupRecyclerAdapter(Context context, List<GroupData.DataBean> groupList) {
        this.context = context;
        this.groupList = groupList;
        chooseIndexList = new ArrayList<>();
    }

    @Override
    public AtGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_at_group,parent,false);
        return new AtGroupHolder(view);
    }

    @Override
    public void onBindViewHolder(AtGroupHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class AtGroupHolder extends RecyclerView.ViewHolder {
        ImageView ivItemCheck,ivItemIcon;
        TextView tvItemName;

        public AtGroupHolder(View itemView) {
            super(itemView);
            ivItemCheck = itemView.findViewById(R.id.iv_item_at_group_check);
            ivItemIcon = itemView.findViewById(R.id.iv_item_at_group_icon);
            tvItemName = itemView.findViewById(R.id.tv_item_at_group_name);
        }

        public void setData(final int index) {
            GlideImgManager.glideLoader(context, groupList.get(index).getGroup_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemIcon, 0);
            tvItemName.setText("[群]" + groupList.get(index).getGroupname());
            ivItemCheck.setVisibility(View.GONE);
            if (chooseIndexList.contains(index)) {
                ivItemCheck.setImageResource(R.mipmap.atxuanzhong);
            } else {
                ivItemCheck.setImageResource(R.mipmap.atweixuanzhong);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (onSimpleItemListener != null) {
                       onSimpleItemListener.onItemListener(index);
                   }
                }
            });
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
