package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.TopicTopListBean;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;

import java.util.List;

public class TopicPushTopRecyclerAdapter extends RecyclerView.Adapter<TopicPushTopRecyclerAdapter.PushTopHolder> {
    Context context;
    List<TopicTopListBean.DataBean> topList;

    public TopicPushTopRecyclerAdapter(Context context, List<TopicTopListBean.DataBean> topList) {
        this.context = context;
        this.topList = topList;
    }

    @Override
    public PushTopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_topic_push_top,parent,false);
        return new PushTopHolder(view);
    }

    @Override
    public void onBindViewHolder(PushTopHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return topList.size();
    }

    class PushTopHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public PushTopHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item_topic_push_top);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSimpleItemViewListener != null) {
                        onSimpleItemViewListener.onItemListener(v);
                    }
                }
            });
        }
        public void setData(int index) {
            tvItem.setText(topList.get(index).getTitle());
        }
    }

    public OnSimpleItemViewListener onSimpleItemViewListener;

    public void setOnSimpleItemViewListener(OnSimpleItemViewListener onSimpleItemViewListener) {
        this.onSimpleItemViewListener = onSimpleItemViewListener;
    }
}
