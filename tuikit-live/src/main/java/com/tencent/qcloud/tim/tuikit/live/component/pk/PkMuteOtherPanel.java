package com.tencent.qcloud.tim.tuikit.live.component.pk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;

public class PkMuteOtherPanel extends BottomSheetDialog {

    public PkMuteOtherPanel(@NonNull Context context,String name) {
        super(context, R.style.live_action_sheet_theme);
        setContentView(R.layout.live_view_pk_mute_other);
        TextView tvMuteInfo = findViewById(R.id.tv_live_pk_stop_title);
        TextView tvMuteCancel = findViewById(R.id.tv_live_pk_stop_cancel);
        TextView tvMuteConfirm = findViewById(R.id.tv_live_pk_mute_other_confirm);
        tvMuteInfo.setText("是否关闭“" + name + "”的声音?");
        tvMuteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvMuteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (pkMuteListener != null) {
                    pkMuteListener.onPkMute();
                }
            }
        });
    }

    public interface OnPkMuteListener {
        void onPkMute();
    }

    OnPkMuteListener pkMuteListener;

    public void setPkMuteListener(OnPkMuteListener pkMuteListener) {
        this.pkMuteListener = pkMuteListener;
    }
}
