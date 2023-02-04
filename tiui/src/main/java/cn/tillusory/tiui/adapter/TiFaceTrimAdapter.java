package cn.tillusory.tiui.adapter;

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
import cn.tillusory.tiui.model.TiFaceTrim;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko on 2018/11/25.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiFaceTrimAdapter extends RecyclerView.Adapter<TiDesViewHolder> {
    public static int ITEM_CATEGORY_TITLE = 11;
    public static int ITEM_COMMON = 21;

    private List<TiFaceTrim> list;

    private int selectedPosition = TiSelectedPosition.POSITION_FACE_TRIM;

    public TiFaceTrimAdapter(List<TiFaceTrim> list) {
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
            return new TiDesCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_des_category, parent, false));
        } else {
            return new TiDesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_des, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final TiDesViewHolder holder, int position) {
        if (position == 0) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins((int) (holder.itemView.getContext().getResources().getDisplayMetrics().density * 13 + 0.5f), 0, 0, 0);
            holder.itemView.requestLayout();
        } else {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.itemView.requestLayout();
        }
        holder.tiTextTV.setText(list.get(position).getString(holder.itemView.getContext()));

        if (holder instanceof TiDesCategoryViewHolder) {
            ((TiDesCategoryViewHolder) holder).tiTitleTV.setText(list.get(position).getTitle(holder.itemView.getContext()));
            if (TiPanelLayout.isFullRatio) {
                ((TiDesCategoryViewHolder) holder).tiTitleTV.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            } else {
                ((TiDesCategoryViewHolder) holder).tiTitleTV.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.ti_unselected));
            }
        }

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
                    TiSelectedPosition.POSITION_FACE_TRIM = selectedPosition;
                }

                switch (list.get(selectedPosition)) {
                    case EYE_MAGNIFYING:
                        RxBus.get().post(RxBusAction.ACTION_EYE_MAGNIFYING);
                        break;
                    case CHIN_SLIMMING:
                        RxBus.get().post(RxBusAction.ACTION_CHIN_SLIMMING);
                        break;
                    case FACE_NARROWING:
                        RxBus.get().post(RxBusAction.ACTION_FACE_NARROWING);
                        break;

                    case CHEEKBONE_SLIMMING:
                        RxBus.get().post(RxBusAction.ACTION_CHEEKBONE_SLIMMING);
                        break;
                    case JAWBONE_SLIMMING:
                        RxBus.get().post(RxBusAction.ACTION_JAWBONE_SLIMMING);
                        break;
                    case JAW_TRANSFORMING:
                        RxBus.get().post(RxBusAction.ACTION_JAW_TRANSFORMING);
                        break;
                    case JAW_SLIMMING:
                        RxBus.get().post(RxBusAction.ACTION_JAW_SLIMMING);
                        break;
                    case FOREHEAD_TRANSFORMING:
                        RxBus.get().post(RxBusAction.ACTION_FOREHEAD_TRANSFORMING);
                        break;

                    case EYE_INNER_CORNERS:
                        RxBus.get().post(RxBusAction.ACTION_EYE_INNER_CORNERS);
                        break;
                    case EYE_OUTER_CORNERS:
                        RxBus.get().post(RxBusAction.ACTION_EYE_OUTER_CORNERS);
                        break;
                    case EYE_SPACING:
                        RxBus.get().post(RxBusAction.ACTION_EYE_SPACING);
                        break;
                    case EYE_CORNERS:
                        RxBus.get().post(RxBusAction.ACTION_EYE_CORNERS);
                        break;

                    case NOSE_MINIFYING:
                        RxBus.get().post(RxBusAction.ACTION_NOSE_MINIFYING);
                        break;
                    case NOSE_ELONGATING:
                        RxBus.get().post(RxBusAction.ACTION_NOSE_ELONGATING);
                        break;

                    case MOUTH_TRANSFORMING:
                        RxBus.get().post(RxBusAction.ACTION_MOUTH_TRANSFORMING);
                        break;
                    case MOUTH_HEIGHT:
                        RxBus.get().post(RxBusAction.ACTION_MOUTH_HEIGHT);
                        break;
                    case MOUTH_LIP_SIZE:
                        RxBus.get().post(RxBusAction.ACTION_MOUTH_LIP_SIZE);
                        break;
                    case MOUTH_SMILING:
                        RxBus.get().post(RxBusAction.ACTION_MOUTH_SMILING);
                        break;

                    case BROW_HEIGHT:
                        RxBus.get().post(RxBusAction.ACTION_BROW_HEIGHT);
                        break;
                    case BROW_LENGTH:
                        RxBus.get().post(RxBusAction.ACTION_BROW_LENGTH);
                        break;
                    case BROW_SPACE:
                        RxBus.get().post(RxBusAction.ACTION_BROW_SPACE);
                        break;
                    case BROW_SIZE:
                        RxBus.get().post(RxBusAction.ACTION_BROW_SIZE);
                        break;
                    case BROW_CORNER:
                        RxBus.get().post(RxBusAction.ACTION_BROW_CORNER);
                        break;

                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}