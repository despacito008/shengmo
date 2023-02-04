package com.tencent.qcloud.tim.tuikit.live.component.gift;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveBlindBoxBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.NormalGiftInfo;

import java.util.List;

public class GiftPanelBlindBoxAdapter extends RecyclerView.Adapter<GiftPanelBlindBoxAdapter.BlindBoxHolder> {
    Context context;
    List<NormalGiftInfo.DataBean.BlindBoxBean> boxList;
    int chooseIndex = 0;

    public GiftPanelBlindBoxAdapter(Context context, List<NormalGiftInfo.DataBean.BlindBoxBean> boxList) {
        this.context = context;
        this.boxList = boxList;
    }

    @NonNull
    @Override
    public BlindBoxHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_item_gift_panel_blind_box,viewGroup,false);
        return new BlindBoxHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlindBoxHolder blindBoxHolder, int i) {
        blindBoxHolder.display(i);
    }

    @Override
    public int getItemCount() {
        return boxList.size();
    }

    class BlindBoxHolder extends RecyclerView.ViewHolder {
        ImageView ivBlindBox;
        TextView tvItemName,tvItemPrice;
        public BlindBoxHolder(@NonNull View itemView) {
            super(itemView);
            ivBlindBox = itemView.findViewById(R.id.iv_item_blind_box);
            tvItemName = itemView.findViewById(R.id.tv_item_blind_box_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_blind_box_price);
        }
        public void display(final int index) {
            Glide.with(context).load(boxList.get(index).getBlindbox_image()).error(R.drawable.ic_live_blind_box).into(ivBlindBox);
            tvItemName.setText(boxList.get(index).getBlindbox_name());
            tvItemPrice.setText(boxList.get(index).getBeans() + "魔豆");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chooseIndex != index) {
                        if (onBlindBoxListener != null) {
                            onBlindBoxListener.doBlindBoxClick(index);
                        }
                        chooseIndex = index;
                        notifyDataSetChanged();
                    }
                }
            });
            if (index == chooseIndex) {
                itemView.setBackgroundResource(R.drawable.live_gift_shape_normal);
            } else {
                itemView.setBackground(null);
            }
        }
    }

    public interface OnBlindBoxListener {
        void doBlindBoxClick(int index);
    }

    OnBlindBoxListener onBlindBoxListener;

    public void setOnBlindBoxListener(OnBlindBoxListener onBlindBoxListener) {
        this.onBlindBoxListener = onBlindBoxListener;
    }
}
