package cn.tillusory.tiui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.TiPanelLayout;
import cn.tillusory.tiui.model.TiDistortion;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko on 2018/5/13.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiDistortionAdapter extends RecyclerView.Adapter<TiDesViewHolder> {

    private List<TiDistortion> list;

    private int selectedPosition = TiSelectedPosition.POSITION_DISTORTION;

    private TiSDKManager tiSDKManager;

    public TiDistortionAdapter(List<TiDistortion> list, TiSDKManager tiSDKManager) {
        this.list = list;
        this.tiSDKManager = tiSDKManager;
    }

    @NonNull
    @Override
    public TiDesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_face_shape, parent, false);
        return new TiDesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TiDesViewHolder holder, int position) {
        if (position == 0) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins((int) (holder.itemView.getContext().getResources().getDisplayMetrics().density * 21 + 0.5f), 0, 0, 0);
            holder.itemView.requestLayout();
        } else {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.itemView.requestLayout();
        }

        holder.tiTextTV.setText(list.get(position).getString(holder.itemView.getContext()));
        if (TiPanelLayout.isFullRatio) {
            holder.tiTextTV.setTextColor(holder.itemView.getContext().getResources().getColorStateList(R.color.color_ti_selector_full));
            holder.tiImageIV.setImageDrawable(list.get(position).getFullImageDrawable(holder.itemView.getContext()));
        } else {
            holder.tiTextTV.setTextColor(holder.itemView.getContext().getResources().getColorStateList(R.color.color_ti_selector_not_full));
            holder.tiImageIV.setImageDrawable(list.get(position).getImageDrawable(holder.itemView.getContext()));

        }

        if (selectedPosition == position) {
            holder.tiTextTV.setSelected(true);
            holder.tiImageIV.setSelected(true);
        } else {
            holder.tiTextTV.setSelected(false);
            holder.tiImageIV.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    selectedPosition = holder.getAdapterPosition();
                    TiSelectedPosition.POSITION_DISTORTION = selectedPosition;
                }

                tiSDKManager.setDistortionEnum(list.get(selectedPosition).getDistortionEnum());

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}