package com.aiwujie.shengmo.timlive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.LiveRedEnvelopesReceiveBean;


import java.util.List;

public class RedEnvelopesReceiveAdapter extends RecyclerView.Adapter<RedEnvelopesReceiveAdapter.RedEnvelopesHolder> {
    Context mContext;
    List<LiveRedEnvelopesReceiveBean.DataBean.ListBean> receiveList;

    public RedEnvelopesReceiveAdapter(Context mContext, List<LiveRedEnvelopesReceiveBean.DataBean.ListBean> receiveList) {
        this.mContext = mContext;
        this.receiveList = receiveList;
    }

    @Override
    public RedEnvelopesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_receive_red_envelopes,parent,false);
        return new RedEnvelopesHolder(view);
    }

    @Override
    public void onBindViewHolder(RedEnvelopesHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return receiveList.size();
    }

    class RedEnvelopesHolder extends RecyclerView.ViewHolder {
        TextView tvItemName,tvItemBean;
        public RedEnvelopesHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_receive_red_envelopes_name);
            tvItemBean = itemView.findViewById(R.id.tv_item_receive_red_envelopes_bean);
        }
        public void display(int index) {
            tvItemName.setText(receiveList.get(index).getNickname());
            tvItemBean.setText(receiveList.get(index).getNum());
        }
    }
}
