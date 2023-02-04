package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.RechargeData;
import com.aiwujie.shengmo.bean.StampGoodsBean;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.List;

public class MessageStampRecyclerAdapter extends RecyclerView.Adapter<MessageStampRecyclerAdapter.MessageStampHolder> {
    Context context;
    private List<StampGoodsBean.DataBean> rechargeDataList;
    int currentIndex = 0;

    public MessageStampRecyclerAdapter(Context context,List<StampGoodsBean.DataBean> rechargeDataList) {
        this.context = context;
        this.rechargeDataList = rechargeDataList;
    }

    @Override
    public MessageStampHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_stamp,parent,false);
        return new MessageStampHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageStampHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return rechargeDataList.size();
    }

    class MessageStampHolder extends RecyclerView.ViewHolder {

        View viewBg;
        TextView tvNum,tvPrice,tvTips;

        public MessageStampHolder(View itemView) {
            super(itemView);
            viewBg = itemView.findViewById(R.id.view_item_message_stamp_bg);
            tvNum = itemView.findViewById(R.id.tv_item_message_stamp_num);
            tvPrice = itemView.findViewById(R.id.tv_item_message_stamp_price);
            tvTips = itemView.findViewById(R.id.tv_item_message_stamp_rmb);

        }

        public void setData(final int position) {
            tvNum.setText(rechargeDataList.get(position).getModou());
            tvPrice.setText(rechargeDataList.get(position).getMoney());
            if(currentIndex == position) {
                viewBg.setSelected(true);
                tvNum.setSelected(true);
                tvPrice.setSelected(true);
                tvTips.setSelected(true);
            } else {
                viewBg.setSelected(false);
                tvNum.setSelected(false);
                tvPrice.setSelected(false);
                tvTips.setSelected(false);
            }
            viewBg.setOnClickListener(new View.OnClickListener() {
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
