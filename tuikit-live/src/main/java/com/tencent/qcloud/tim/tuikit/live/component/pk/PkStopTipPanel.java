package com.tencent.qcloud.tim.tuikit.live.component.pk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;

public class PkStopTipPanel extends BottomSheetDialog {

    public PkStopTipPanel(@NonNull Context context, String name) {
        super(context, R.style.live_action_sheet_theme);
        setContentView(R.layout.live_view_pk_stop);
        TextView tvMuteInfo = findViewById(R.id.tv_live_pk_stop_title);
        TextView tvMuteCancel = findViewById(R.id.tv_live_pk_stop_again);
        TextView tvMuteConfirm = findViewById(R.id.tv_live_pk_stop_confirm);
        tvMuteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (pkStopListener != null) {
                    pkStopListener.onPkAgain();
                }
            }
        });
        tvMuteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (pkStopListener != null) {
                    pkStopListener.onPkQuit();
                }
            }
        });
    }

    public interface OnPkStopListener {
        void onPkQuit();
        void onPkAgain();
    }

    OnPkStopListener pkStopListener;

    public void setPkStopListener(OnPkStopListener pkMuteListener) {
        this.pkStopListener = pkMuteListener;
    }
}
