package com.aiwujie.shengmo.view;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import razerdp.basepopup.BasePopupWindow;

public class MediaMessageCheckPopup extends BasePopupWindow {
    private Uri uri;
    private int type;

    public MediaMessageCheckPopup(Context context,Uri uri,int type) {
        super(context);
        this.uri = uri;
        this.type = type;
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_media_message_check);
    }

    void initView() {
        TextView tvTips = findViewById(R.id.tv_pop_message_check_tips);
        TextView tvConfirm = findViewById(R.id.tv_pop_message_check_confirm);
        TextView tvCancel = findViewById(R.id.tv_pop_message_check_cancel);
        ImageView ivPlay = findViewById(R.id.iv_pop_message_check_play);
        ImageView ivPop = findViewById(R.id.iv_pop_message_check);

        if (type == 1) {
            tvTips.setText("你选择了图片");
            ivPlay.setVisibility(View.INVISIBLE);
        } else if (type == 2) {
            tvTips.setText("你选择了视频");
            ivPlay.setVisibility(View.VISIBLE);
        }
        ivPop.setImageURI(uri);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaMessageCheckPopup.this.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaMessageCheckPopup.this.dismiss();
                if (onSimpleItemListener != null) {
                    onSimpleItemListener.onItemListener(0);
                }
            }
        });
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
