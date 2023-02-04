package cn.tillusory.tiui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tillusory.tiui.R;

public class TiGreenScreenEditViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbIV;
    public TextView nameTV;

    public TiGreenScreenEditViewHolder(@NonNull View itemView) {
        super(itemView);
        thumbIV = itemView.findViewById(R.id.thumbIV);
        nameTV = itemView.findViewById(R.id.nameTV);
    }
}
