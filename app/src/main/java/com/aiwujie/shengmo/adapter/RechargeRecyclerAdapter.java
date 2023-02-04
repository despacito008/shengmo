package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.RechargeData;
import com.aiwujie.shengmo.bean.StampGoodsBean;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.List;

public class RechargeRecyclerAdapter extends RecyclerView.Adapter<RechargeRecyclerAdapter.RechargeHolder> {
    Context context;
    private List<StampGoodsBean.DataBean> rechargeDataList;
    int currentIndex = 0;

    public RechargeRecyclerAdapter(Context context, List<StampGoodsBean.DataBean> rechargeDataList) {
        this.context = context;
        this.rechargeDataList = rechargeDataList;
    }

    @Override
    public RechargeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_recharge,parent,false);
        return new RechargeHolder(view);
    }

    @Override
    public void onBindViewHolder(RechargeHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return rechargeDataList.size();
    }

    class RechargeHolder extends RecyclerView.ViewHolder {

        View viewBg;
        TextView tvNum,tvPrice;
        ImageView ivItem;

        public RechargeHolder(View itemView) {
            super(itemView);
            viewBg = itemView.findViewById(R.id.view_item_recharge_bg);
            tvNum = itemView.findViewById(R.id.tv_item_recharge_beans);
            tvPrice = itemView.findViewById(R.id.tv_item_recharge_price);
            ivItem = itemView.findViewById(R.id.iv_item_recharge);

        }

        public void setData(final int position) {
            String beanStr = rechargeDataList.get(position).getModou() + "金魔豆";
            String moneyStr = rechargeDataList.get(position).getMoney() + " 元";
            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(25, true);//设置前面的字体大小
            SpannableStringBuilder builder = new SpannableStringBuilder(moneyStr);
            builder.setSpan(sizeSpan,0,rechargeDataList.get(position).getMoney().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvNum.setText(beanStr);
            tvPrice.setText(builder);
            if(currentIndex == position) {
                viewBg.setSelected(true);
                tvNum.setSelected(true);
                tvPrice.setSelected(true);
            } else {
                viewBg.setSelected(false);
                tvNum.setSelected(false);
                tvPrice.setSelected(false);
            }
            if(position / 3 == 0) {
                ivItem.setImageResource(R.drawable.ic_recharge_beans_small);
            } else if (position / 3 == 1) {
                ivItem.setImageResource(R.drawable.ic_recharge_beans_middle);
            } else {
                ivItem.setImageResource(R.drawable.ic_recharge_beans_big);
            }
            viewBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentIndex == position) {
                        return;
                    }
                    changeChooseItem(position);
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

    public void changeChooseItem(int position) {
        int temp = currentIndex;
        currentIndex = position;
        if (currentIndex >= 0) {
            notifyItemChanged(currentIndex);
        }
        if (temp >= 0) {
            notifyItemChanged(temp);
        }
    }

}
