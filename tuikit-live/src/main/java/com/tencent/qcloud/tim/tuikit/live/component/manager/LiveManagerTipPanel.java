package com.tencent.qcloud.tim.tuikit.live.component.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.Arrays;
import java.util.List;

public class LiveManagerTipPanel extends BottomSheetDialog {

    public LiveManagerTipPanel(@NonNull final Context context, final String uid, final boolean isMute) {
        super(context, R.style.live_action_sheet_theme);
        setContentView(R.layout.live_view_manager_tip);
        final TextView tvMuteInfo = findViewById(R.id.tv_live_pk_stop_title);
        final ImageView ivIcon = findViewById(R.id.iv_item_live_manager_icon);
        final TextView tvMuteCancel = findViewById(R.id.tv_live_manager_mute);
        TextView tvMuteConfirm = findViewById(R.id.tv_live_manager_kick);
//        if (isMute) {
//            tvMuteCancel.setText("取消静音");
//        } else {
//            tvMuteCancel.setText("静音场控");
//        }
        tvMuteCancel.setText("取消");
        tvMuteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                if (tipListener != null) {
//                    tipListener.onManagerMute(uid,!isMute);
//                }
            }
        });
        tvMuteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (tipListener != null) {
                    tipListener.onManagerKick(uid);
                }
            }
        });

        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(uid), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> userFullInfoList) {
                if (userFullInfoList != null && userFullInfoList.size() > 0) {
                    tvMuteInfo.setText(userFullInfoList.get(0).getNickName());
                    Glide.with(context).load(userFullInfoList.get(0).getFaceUrl()).into(ivIcon);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public interface OnManagerTipListener {
        void onManagerMute(String uid,boolean isMute);
        void onManagerKick(String uid);
    }

    OnManagerTipListener tipListener;

    public void setTipListener(OnManagerTipListener tipListener) {
        this.tipListener = tipListener;
    }
}
