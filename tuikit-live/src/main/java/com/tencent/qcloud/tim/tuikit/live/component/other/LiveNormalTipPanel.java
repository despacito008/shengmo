package com.tencent.qcloud.tim.tuikit.live.component.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;

public class LiveNormalTipPanel extends BottomSheetDialog {

    public LiveNormalTipPanel(@NonNull Context context,String title) {
        super(context);
        setContentView(R.layout.live_view_normal_tip);
        TextView tvTitle    = findViewById(R.id.tv_live_live_tip_title);
        TextView tvLeft     = findViewById(R.id.tv_live_live_tip_left);
        TextView tvRight    = findViewById(R.id.tv_live_live_tip_right);
        tvTitle.setText(title);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onNormalTipListener != null) {
                    onNormalTipListener.doNormalLeftClick();
                }
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onNormalTipListener != null) {
                    onNormalTipListener.doNormalRightClick();
                }
            }
        });
    }

    public interface OnNormalTipListener {
        void doNormalLeftClick();
        void doNormalRightClick();
    }

    OnNormalTipListener onNormalTipListener;

    public void setOnNormalTipListener(OnNormalTipListener onNormalTipListener) {
        this.onNormalTipListener = onNormalTipListener;
    }
}
