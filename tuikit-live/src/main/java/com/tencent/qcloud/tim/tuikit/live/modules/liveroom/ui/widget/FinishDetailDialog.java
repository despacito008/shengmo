package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;



import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAudienceLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;

import org.greenrobot.eventbus.EventBus;


/**
 * Module:   FinishDetailDialog
 * <p>
 * Function: 推流结束的详情页
 * <p>
 * 统计了观看人数、点赞数量、开播时间
 */
@SuppressLint("ValidFragment")
public class FinishDetailDialog extends DialogFragment {

    public static final String LIVE_TOTAL_TIME      = "live_total_time";       // KEY，表示本场直播的时长
    public static final String ANCHOR_HEART_COUNT   = "anchor_heart_count";    // KEY，表示本场主播收到赞的数量
    public static final String ANCHOR_LOCAL_BEANS   = "anchor_local_beans";    // KEY，表示本场的收益
    public static final String TOTAL_AUDIENCE_COUNT = "total_audience_count";  // KEY，表示本场观众的总人数
    private final String uid;
    private LiveRoomAudienceFragment.OnLiveOperateClickListener onLiveOperateClickListener;

    @SuppressLint("ValidFragment")
    public FinishDetailDialog(LiveRoomAudienceFragment.OnLiveOperateClickListener onLiveOperateClickListener, String uid) {
        this.onLiveOperateClickListener = onLiveOperateClickListener;
        this.uid = uid;
    }

    @SuppressLint("ValidFragment")
    public FinishDetailDialog(LiveRoomAudienceFragment.OnLiveOperateClickListener onLiveOperateClickListener, String uid,boolean isRecord) {
        this.onLiveOperateClickListener = onLiveOperateClickListener;
        this.uid = uid;
        this.isRecord = isRecord;
    }

    public FinishDetailDialog(String uid) {
        this.uid = uid;
    }

    boolean isRecord = false;
    public FinishDetailDialog(String uid,boolean isRecord) {
        this.uid = uid;
        this.isRecord = isRecord;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mDetailDialog = new Dialog(getActivity(), R.style.TUILiveDialogFragment);
        mDetailDialog.setContentView(R.layout.live_dialog_publish_detail);
        mDetailDialog.setCancelable(false);

        TextView tvCancel = (TextView) mDetailDialog.findViewById(R.id.btn_anchor_cancel);
        if (isRecord) {
            tvCancel.setText("设置回放魔豆");
        } else {
            tvCancel.setText("返回");
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord) {
                    mDetailDialog.dismiss();
                    EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SET_PLAY_BACK_TICKET,"",""));
                } else {
                    mDetailDialog.dismiss();
                    getActivity().finish();
                    if (onLiveOperateClickListener != null) {
                        String localUid = !TextUtils.isEmpty(uid) ? uid : "";
                        onLiveOperateClickListener.closeLive(localUid);
                    }
                }
            }
        });

        TextView tvDetailTime       = (TextView) mDetailDialog.findViewById(R.id.tv_time);
        TextView tvDetailAdmires    = (TextView) mDetailDialog.findViewById(R.id.tv_admires);
        TextView tvDetailWatchCount = (TextView) mDetailDialog.findViewById(R.id.tv_members);

        //确认则显示观看detail
        tvDetailTime.setText(getArguments().getString(LIVE_TOTAL_TIME));
        tvDetailAdmires.setText(getArguments().getString(ANCHOR_LOCAL_BEANS));
        tvDetailWatchCount.setText(getArguments().getString(TOTAL_AUDIENCE_COUNT));

        return mDetailDialog;
    }

}
