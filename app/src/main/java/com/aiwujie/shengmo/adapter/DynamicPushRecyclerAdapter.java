package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.PushGoodsBean;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicPushRecyclerAdapter extends RecyclerView.Adapter<DynamicPushRecyclerAdapter.DynamicPushHolder> {

    Context context;
    List<PushGoodsBean.DataBean> pushGoodsList;
    private int currentIndex = 0;

    public DynamicPushRecyclerAdapter(Context context, List<PushGoodsBean.DataBean> pushGoodsList) {
        this.context = context;
        this.pushGoodsList = pushGoodsList;
    }

    @Override
    public DynamicPushHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic_push, parent, false);
        return new DynamicPushHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicPushHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return pushGoodsList.size();
    }

    class DynamicPushHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_item_dynamic_push_bg)
        View viewItemDynamicPushBg;
        @BindView(R.id.tv_item_dynamic_push_name)
        TextView tvItemDynamicPushName;
        @BindView(R.id.tv_item_dynamic_push_price)
        TextView tvItemDynamicPushPrice;
        @BindView(R.id.iv_item_dynamic_push_rocket)
        ImageView ivItemDynamicPushRocket;
        @BindView(R.id.tv_item_dynamic_push_average_price)
        TextView tvItemDynamicPushAveragePrice;
        @BindView(R.id.tv_item_dynamic_push_original_price)
        TextView tvItemDynamicPushOriginalPrice;
        @BindView(R.id.tv_item_dynamic_push_discount)
        TextView tvItemDynamicPushDiscount;
        @BindView(R.id.group_item_dynamic_push)
        Group groupItemDynamicPush;
        public DynamicPushHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(final int position) {
            PushGoodsBean.DataBean pushBean = pushGoodsList.get(position);
            tvItemDynamicPushName.setText(pushBean.getPush_name());
            tvItemDynamicPushPrice.setText(pushBean.getPush_price());
            if(pushBean.getPush_price().equals(pushBean.getPush_original_price())) {
                groupItemDynamicPush.setVisibility(View.GONE);
            } else {
                groupItemDynamicPush.setVisibility(View.VISIBLE);
                tvItemDynamicPushOriginalPrice.setText("原价：" + pushBean.getPush_original_price() + "元");
                tvItemDynamicPushAveragePrice.setText("(仅¥" + pushBean.getPush_average_price() + "/张)");
                tvItemDynamicPushDiscount.setText(pushBean.getPush_discount());
            }
            if(currentIndex == position) {
                viewItemDynamicPushBg.setBackgroundResource(R.drawable.bg_dynamic_push_item_choose);
                ivItemDynamicPushRocket.setVisibility(View.VISIBLE);
            } else {
                viewItemDynamicPushBg.setBackgroundResource(R.drawable.bg_dynamic_push_item_normal);
                ivItemDynamicPushRocket.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentIndex == position) {
                        return;
                    }
                    int temp = currentIndex;
                    currentIndex = position;
                    notifyItemChanged(currentIndex);
                    notifyItemChanged(temp);
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
}
