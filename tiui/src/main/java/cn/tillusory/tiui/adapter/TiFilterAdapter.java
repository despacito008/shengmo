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
import cn.tillusory.tiui.model.TiBeautyFilter;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko on 2018/5/12.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiFilterAdapter extends RecyclerView.Adapter<TiDesViewHolder> {
    public static int ITEM_CATEGORY_TITLE = 11;
    public static int ITEM_COMMON = 21;

    private List<TiBeautyFilter> list;

    private int selectedPosition = TiSelectedPosition.POSITION_FILTER;

    public TiFilterAdapter(List<TiBeautyFilter> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @NonNull
    @Override
    public TiDesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_CATEGORY_TITLE) {
            return new TiDesCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_filter_category, parent, false));
        } else {
            return new TiDesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_filter, parent, false));
        }
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

        if (holder instanceof TiDesCategoryViewHolder) {
            ((TiDesCategoryViewHolder) holder).tiTitleTV.setText(list.get(position).getTitle(holder.itemView.getContext()));
            if (TiPanelLayout.isFullRatio) {
                ((TiDesCategoryViewHolder) holder).tiTitleTV.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            } else {
                ((TiDesCategoryViewHolder) holder).tiTitleTV.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.ti_unselected));
            }
        }

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
                    TiSelectedPosition.POSITION_FILTER = selectedPosition;
                }
                RxBus.get().post(RxBusAction.ACTION_FILTER, list.get(selectedPosition).getFilterName());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}

