package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.InviteRewardBean;

import java.util.List;

public class InviteRewardRecyclerAdapter extends RecyclerView.Adapter<InviteRewardRecyclerAdapter.InviteRewardHolder> {
    Context context;
    List<InviteRewardBean.DataBean> rewardList;

    public InviteRewardRecyclerAdapter(Context context, List<InviteRewardBean.DataBean> rewardList) {
        this.context = context;
        this.rewardList = rewardList;

    }

    @Override
    public InviteRewardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_invite_reward,parent,false);
        return new InviteRewardHolder(view);
    }

    @Override
    public void onBindViewHolder(InviteRewardHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    class InviteRewardHolder extends RecyclerView.ViewHolder {
        TextView tvItemContent,tvItemTime;
        public InviteRewardHolder(View itemView) {
            super(itemView);
            tvItemContent = itemView.findViewById(R.id.tv_item_invite_reward_content);
            tvItemTime = itemView.findViewById(R.id.tv_item_invite_reward_time);
        }
        public void setData(int position) {
            tvItemContent.setText(rewardList.get(position).getInfo());
            tvItemTime.setText(rewardList.get(position).getGet_svip_time());
        }
    }
}
