package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.PushTopBean;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.List;

public class PushTopRecyclerAdapter extends RecyclerView.Adapter<PushTopRecyclerAdapter.PushTopHolder> {
    Context context;
    List<PushTopBean> pushTopBeanList;
    int currentIndex = -1;

    public PushTopRecyclerAdapter(Context context, List<PushTopBean> pushTopBeanList) {
        this.context = context;
        this.pushTopBeanList = pushTopBeanList;
    }

    @Override
    public PushTopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pop_push_top,parent,false);
        return new PushTopHolder(view);
    }

    @Override
    public void onBindViewHolder(PushTopHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return pushTopBeanList.size();
    }

    class PushTopHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public PushTopHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item_pop_push_top);
        }
        public void setData(final int position) {
            if(position == currentIndex) {
                tvItem.setSelected(true);
            } else {
                tvItem.setSelected(false);
            }
            tvItem.setText(pushTopBeanList.get(position).getName());
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(position);
                    }
                }
            });
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }

    public void changeItem(int index) {
        int temp = currentIndex;
        currentIndex = index;
        if(currentIndex >= 0) {
            notifyItemChanged(currentIndex);
        }
        if(temp >= 0) {
            notifyItemChanged(temp);
        }
    }
}
