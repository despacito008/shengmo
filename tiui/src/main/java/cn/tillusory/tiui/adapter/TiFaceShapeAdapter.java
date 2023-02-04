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
import cn.tillusory.tiui.model.TiFaceShape;
import cn.tillusory.tiui.model.TiFaceTrim;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko on 2018/11/25.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiFaceShapeAdapter extends RecyclerView.Adapter<TiDesViewHolder> {

    private List<TiFaceShape> list;

    private int selectedPosition = TiSelectedPosition.POSITION_FACE_SHAPE;

    public TiFaceShapeAdapter(List<TiFaceShape> list) {
        this.list = list;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
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
                    TiSelectedPosition.POSITION_FACE_SHAPE = selectedPosition;
                }

//                switch (list.get(selectedPosition)) {
//                    case CLASSIC:
//                        RxBus.get().post(RxBusAction.ACTION_CLASSIC);
//                        break;
//                    case SQUARE_FACE:
//                        RxBus.get().post(RxBusAction.ACTION_SQUARE_FACE);
//                        break;
//                    case LONG_FACE:
//                        RxBus.get().post(RxBusAction.ACTION_LONG_FACE);
//                        break;
//                    case ROUND_FACE:
//                        RxBus.get().post(RxBusAction.ACTION_ROUND_FACE);
//                        break;
//                    case THIN_FACE:
//                        RxBus.get().post(RxBusAction.ACTION_THIN_FACE);
//                        break;
//                }
                RxBus.get().post(RxBusAction.ACTION_FACE_SHAPE, list.get(selectedPosition).getFaceShapeVal());

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}


