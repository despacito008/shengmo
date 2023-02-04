package com.aiwujie.shengmo.timlive.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class LiveAnchorSettingPop extends BasePopupWindow {

    @BindView(R.id.tv_pop_live_setting_camera_front)
    TextView tvPopLiveSettingCameraFront;
    @BindView(R.id.ll_pop_live_setting_camera_front)
    LinearLayout llPopLiveSettingCameraFront;
    @BindView(R.id.tv_pop_live_setting_camera_back)
    TextView tvPopLiveSettingCameraBack;
    @BindView(R.id.ll_pop_live_setting_camera_back)
    LinearLayout llPopLiveSettingCameraBack;
    @BindView(R.id.tv_pop_live_setting_camera_close)
    TextView tvPopLiveSettingCameraClose;
    @BindView(R.id.ll_pop_live_setting_camera_close)
    LinearLayout llPopLiveSettingCameraClose;
    @BindView(R.id.tv_pop_live_setting_audio_open)
    TextView tvPopLiveSettingAudioOpen;
    @BindView(R.id.ll_pop_live_setting_audio_open)
    LinearLayout llPopLiveSettingAudioOpen;
    @BindView(R.id.tv_pop_live_setting_audio_close)
    TextView tvPopLiveSettingAudioClose;
    @BindView(R.id.ll_pop_live_setting_audio_close)
    LinearLayout llPopLiveSettingAudioClose;
    @BindView(R.id.tv_pop_live_setting_audio_music)
    TextView tvPopLiveSettingAudioMusic;
    @BindView(R.id.ll_pop_live_setting_audio_music)
    LinearLayout llPopLiveSettingAudioMusic;
    @BindView(R.id.ll_pop_live_setting_audio_setting)
    LinearLayout llPopLiveSettingAudioSetting;
    Context context;
    LiveSettingStateBean liveSettingStateBean;
    @BindView(R.id.iv_pop_live_setting_camera_front)
    ImageView ivPopLiveSettingCameraFront;
    @BindView(R.id.iv_pop_live_setting_camera_back)
    ImageView ivPopLiveSettingCameraBack;
    @BindView(R.id.iv_pop_live_setting_camera_close)
    ImageView ivPopLiveSettingCameraClose;
    @BindView(R.id.iv_pop_live_setting_audio_open)
    ImageView ivPopLiveSettingAudioOpen;
    @BindView(R.id.iv_pop_live_setting_audio_close)
    ImageView ivPopLiveSettingAudioClose;
    @BindView(R.id.tv_pop_live_setting_beauty)
    TextView tvPopLiveSettingBeauty;
    @BindView(R.id.ll_pop_live_setting_beauty)
    LinearLayout llPopLiveSettingBeauty;
    @BindView(R.id.ll_live_ing_setting)
    LinearLayout llLiveIngSetting;
    @BindView(R.id.ll_pop_live_setting_manager_speaking)
    LinearLayout llPopLiveSettingManagerSpeaking;

    boolean onlyShowVideoSetting = false;

    boolean isAnchor = true;

    public LiveAnchorSettingPop(Context context, LiveSettingStateBean liveSettingStateBean) {
        super(context);
        this.context = context;
        this.liveSettingStateBean = liveSettingStateBean;
        initListener();
        initView();
    }

    public LiveAnchorSettingPop(Context context,boolean isAnchor, LiveSettingStateBean liveSettingStateBean) {
        super(context);
        this.context = context;
        this.liveSettingStateBean = liveSettingStateBean;
        this.isAnchor = isAnchor;
        initListener();
        initView();
    }

    public LiveAnchorSettingPop(Context context, LiveSettingStateBean liveSettingStateBean, boolean showVideoSetting) {
        super(context);
        this.context = context;
        this.liveSettingStateBean = liveSettingStateBean;
        this.onlyShowVideoSetting = showVideoSetting;
        initListener();
        initView();
    }

    @Override
    public View onCreateContentView() {
        View popView = createPopupById(R.layout.app_pop_live_anchor_setting);
        ButterKnife.bind(this, popView);
        return popView;
    }

    public void initView() {
        clearChooseCameraView();
        if (liveSettingStateBean.isVideoMute()) {
            chooseView(llPopLiveSettingCameraClose, tvPopLiveSettingCameraClose, ivPopLiveSettingCameraClose);
            llPopLiveSettingBeauty.setVisibility(View.INVISIBLE);
        } else {
            llPopLiveSettingBeauty.setVisibility(View.VISIBLE);
            if (liveSettingStateBean.isCameraFront()) {
                chooseView(llPopLiveSettingCameraFront, tvPopLiveSettingCameraFront, ivPopLiveSettingCameraFront);
            } else {
                chooseView(llPopLiveSettingCameraBack, tvPopLiveSettingCameraBack, ivPopLiveSettingCameraBack);
            }
        }

        clearChooseAudioView();
        if (liveSettingStateBean.isAudioMute()) {
            chooseView(llPopLiveSettingAudioClose, tvPopLiveSettingAudioClose, ivPopLiveSettingAudioClose);
            llPopLiveSettingAudioMusic.setVisibility(View.INVISIBLE);
        } else {
            chooseView(llPopLiveSettingAudioOpen, tvPopLiveSettingAudioOpen, ivPopLiveSettingAudioOpen);
            llPopLiveSettingAudioMusic.setVisibility(View.VISIBLE);
        }

        if (onlyShowVideoSetting) {
            llLiveIngSetting.setVisibility(View.GONE);
        }

        if (!isAnchor) {
            llPopLiveSettingManagerSpeaking.setVisibility(View.INVISIBLE);
        }

    }


    public void initListener() {
        llPopLiveSettingCameraFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChooseCameraView();
                chooseView(llPopLiveSettingCameraFront, tvPopLiveSettingCameraFront, ivPopLiveSettingCameraFront);
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseCameraFront();
                }
                llPopLiveSettingBeauty.setClickable(true);
            }
        });
        llPopLiveSettingCameraBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChooseCameraView();
                chooseView(llPopLiveSettingCameraBack, tvPopLiveSettingCameraBack, ivPopLiveSettingCameraBack);
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseCameraBack();
                }
                llPopLiveSettingBeauty.setClickable(true);
            }
        });
        llPopLiveSettingCameraClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChooseCameraView();
                chooseView(llPopLiveSettingCameraClose, tvPopLiveSettingCameraClose, ivPopLiveSettingCameraClose);
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseCameraClose();
                }
                llPopLiveSettingBeauty.setClickable(false);
            }
        });
        llPopLiveSettingAudioOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChooseAudioView();
                chooseView(llPopLiveSettingAudioOpen, tvPopLiveSettingAudioOpen, ivPopLiveSettingAudioOpen);
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseAudioOpen();
                }
                llPopLiveSettingAudioMusic.setClickable(true);
            }
        });
        llPopLiveSettingAudioClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChooseAudioView();
                chooseView(llPopLiveSettingAudioClose, tvPopLiveSettingAudioClose, ivPopLiveSettingAudioClose);
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseAudioClose();
                }
                llPopLiveSettingAudioMusic.setClickable(false);
            }
        });
        llPopLiveSettingAudioSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseAudioSetting();
                }
            }
        });
        llPopLiveSettingBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseBeauty();
                }
                dismiss();
            }
        });

        llPopLiveSettingManagerSpeaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveSettingListener != null) {
                    onLiveSettingListener.onChooseManagerSpeak();
                }
            }
        });
    }

    void clearChooseCameraView() {
        unChooseView(llPopLiveSettingCameraFront, tvPopLiveSettingCameraFront, ivPopLiveSettingCameraFront);
        unChooseView(llPopLiveSettingCameraBack, tvPopLiveSettingCameraBack, ivPopLiveSettingCameraBack);
        unChooseView(llPopLiveSettingCameraClose, tvPopLiveSettingCameraClose, ivPopLiveSettingCameraClose);
    }

    void clearChooseAudioView() {
        unChooseView(llPopLiveSettingAudioOpen, tvPopLiveSettingAudioOpen, ivPopLiveSettingAudioOpen);
        unChooseView(llPopLiveSettingAudioClose, tvPopLiveSettingAudioClose, ivPopLiveSettingAudioClose);
    }

    private void unChooseView(LinearLayout linearLayout, TextView textView, ImageView imageView) {
        linearLayout.setBackgroundResource(R.drawable.bg_round_gray_home);
        textView.setTextColor(context.getResources().getColor(R.color.title_color));
        if (imageView != null) {
            imageView.setSelected(false);
        }
        linearLayout.setClickable(true);
    }

    private void chooseView(LinearLayout linearLayout, TextView textView, ImageView imageView) {
        linearLayout.setBackgroundResource(R.drawable.bg_round_purple_home);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        if (imageView != null) {
            imageView.setSelected(true);
        }
        linearLayout.setClickable(false);
    }

    public interface OnLiveSettingListener {
        void onChooseCameraFront();

        void onChooseCameraBack();

        void onChooseCameraClose();

        void onChooseAudioOpen();

        void onChooseAudioClose();

        void onChooseAudioSetting();

        void onChooseBeauty();

        void onChooseManagerSpeak();
    }

    OnLiveSettingListener onLiveSettingListener;

    public void setOnLiveSettingListener(OnLiveSettingListener onLiveSettingListener) {
        this.onLiveSettingListener = onLiveSettingListener;
    }
}
