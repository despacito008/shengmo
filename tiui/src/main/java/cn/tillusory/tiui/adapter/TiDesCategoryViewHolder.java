package cn.tillusory.tiui.adapter;

import android.view.View;
import android.widget.TextView;

import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2018/11/22.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiDesCategoryViewHolder extends TiDesViewHolder {

    public TextView tiTitleTV;

    public TiDesCategoryViewHolder(View itemView) {
        super(itemView);
        tiTitleTV = itemView.findViewById(R.id.tiTitleTV);
    }

}