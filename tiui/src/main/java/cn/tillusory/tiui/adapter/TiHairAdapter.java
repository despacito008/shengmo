package cn.tillusory.tiui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;

import java.util.List;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.TiPanelLayout;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiHair;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko on 2018/5/12.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiHairAdapter extends RecyclerView.Adapter<TiDesViewHolder> {

    private List<TiHair> list;

    private int selectedPosition = TiSelectedPosition.POSITION_HAIR;

    public TiHairAdapter(List<TiHair> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TiDesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_filter, parent, false);
        return new TiDesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TiDesViewHolder holder, int position) {
        if (position == 0) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins((int) (holder.itemView.getContext().getResources().getDisplayMetrics().density * 16 + 0.5f), 0, 0, 0);
            holder.itemView.requestLayout();
        } else {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.itemView.requestLayout();
        }
        holder.tiTextTV.setText(list.get(position).getString(holder.itemView.getContext()));
        holder.tiImageIV.setImageDrawable(list.get(position).getImageDrawable(holder.itemView.getContext()));
        if (TiPanelLayout.isFullRatio) {
            holder.tiShadow.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.tiShadow.setBackgroundColor(Color.WHITE);
        }

        if (selectedPosition == position) {
            holder.tiTextTV.setSelected(true);
            holder.tiCover.setSelected(true);
        } else {
            holder.tiTextTV.setSelected(false);
            holder.tiCover.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    selectedPosition = holder.getAdapterPosition();
                    TiSelectedPosition.POSITION_HAIR = selectedPosition;
                }
                RxBus.get().post(RxBusAction.ACTION_HAIR, list.get(selectedPosition).getHairEnum());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}

