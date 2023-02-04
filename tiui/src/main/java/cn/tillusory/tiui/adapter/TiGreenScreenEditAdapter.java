package cn.tillusory.tiui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;

import java.util.List;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiGreenScreenEdit;
import cn.tillusory.tiui.model.TiSelectedPosition;

public class TiGreenScreenEditAdapter extends RecyclerView.Adapter<TiGreenScreenEditViewHolder> {

    private int selectedPosition = TiSelectedPosition.POSITION_GREEN_SCREEN_EDIT;

    private List<TiGreenScreenEdit> list;

    public TiGreenScreenEditAdapter(List<TiGreenScreenEdit> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TiGreenScreenEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TiGreenScreenEditViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_green_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TiGreenScreenEditViewHolder holder, int position) {
        if (position == 0) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins((int) (holder.itemView.getContext().getResources().getDisplayMetrics().density * 13 + 0.5f), 0, 0, 0);
            holder.itemView.requestLayout();
        } else {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.itemView.requestLayout();
        }

        holder.thumbIV.setImageDrawable(list.get(position).getImageDrawable(holder.itemView.getContext()));
        holder.nameTV.setText(list.get(position).getString(holder.itemView.getContext()));

        if (selectedPosition == position) {
            holder.nameTV.setSelected(true);
            holder.thumbIV.setSelected(true);
        } else {
            holder.nameTV.setSelected(false);
            holder.thumbIV.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPosition = holder.getAdapterPosition();
                TiSelectedPosition.POSITION_GREEN_SCREEN_EDIT = selectedPosition;

                switch (list.get(selectedPosition)) {
                    case SIMILARITY:
                        RxBus.get().post(RxBusAction.ACTION_GREEN_SCREEN_SIMILARITY);
                        break;
                    case SMOOTHNESS:
                        RxBus.get().post(RxBusAction.ACTION_GREEN_SCREEN_SMOOTHNESS);
                        break;
                    case ALPHA:
                        RxBus.get().post(RxBusAction.ACTION_GREEN_SCREEN_ALPHA);
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
