package com.aliyun.svideo.recorder.view.control;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.aliyun.svideo.record.R;
import com.aliyun.svideo.recorder.view.BaseScrollPickerView;
import com.aliyun.svideo.recorder.view.StringScrollPicker;
import com.aliyun.svideo.base.UIConfigManager;
import com.aliyun.svideo.base.utils.FastClickUtil;
import com.aliyun.svideo.common.utils.image.ImageLoaderImpl;
import com.aliyun.svideo.common.utils.image.ImageLoaderOptions;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;

import java.util.ArrayList;
import java.util.List;

public class ControlView extends RelativeLayout implements View.OnTouchListener {
    private static final String TAG = ControlView.class.getSimpleName();
    private static final int MAX_ITEM_COUNT = 5;
    private LinearLayout llBeautyFace;

    private ImageView ivReadyRecord;
    private ImageView aliyunSwitchLight;
    private ImageView aliyunSwitchCamera;
    public TextView aliyunComplete;
    private ImageView aliyunBack;
    private LinearLayout aliyunRecordLayoutBottom;
    private LinearLayout aliyunRateBar;
    private TextView aliyunRateQuarter;
    private TextView aliyunRateHalf;
    private TextView aliyunRateOrigin;
    private TextView aliyunRateDouble;
    private TextView aliyunRateDoublePower2;
    private TextView aliyunRecordDuration;
    private FrameLayout aliyunRecordBtn;
    private TextView aliyunDelete;
    private TextView mRecordTipTV;
    private FrameLayout mTitleView;
    private StringScrollPicker mPickerView;
    private ControlViewListener mListener;
    private LinearLayout mAlivcAspectRatio;
    private TextView mTvAspectRatio;
    private ImageView mIVAspectRatio;
    private View mAlivcTakePhoto;
    //????????????
    private RecordMode recordMode = RecordMode.LONG_PRESS;
    //????????????????????????true????????????????????????????????????????????????view??????
    private boolean hasRecordPiece = false;
    //?????????????????????????????????????????????????????????????????????true
    private boolean canComplete = false;
    //????????????????????????
    private boolean isMusicSelViewShow = false;
    //??????????????????????????????
    private boolean isEffectSelViewShow = false;
    //???????????????
    private FlashType flashType = FlashType.OFF;
    //???????????????
    private CameraType cameraType = CameraType.FRONT;
    //????????????
    private RecordRate recordRate = RecordRate.STANDARD;
    //???????????????????????????????????????,????????????UI??????
    private RecordState recordState = RecordState.STOP;
    //??????????????????
    private int itemWidth;
    //????????????????????????
    private boolean isCountDownRecording = false;
    //????????????????????????????????????????????????UI???????????????????????????????????????????????????????????????????????????????????????????????????
    private boolean isRecording = false;
    //
    private int mAspectRatio = AliyunSnapVideoParam.RATIO_MODE_9_16;
    //???????????? ??????true ????????????false
    private Boolean mIsMixRecorderType = false;


    public ControlView(Context context) {
        this(context, null);
    }

    public ControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        calculateItemWidth();
        //Inflate??????
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_recorder_view_control, this, true);
        assignViews();
        //??????view???????????????
        setViewListener();
        //??????view?????????
        updateAllViews();
    }

    private void assignViews() {
        mAlivcAspectRatio = findViewById(R.id.alivc_record_change_aspect_ratio_layout);

        mAlivcTakePhoto = findViewById(R.id.alivc_record_take_photo);
        mTvAspectRatio = findViewById(R.id.alivc_record_aspect_ratio_tv_change);
        mIVAspectRatio = findViewById(R.id.alivc_aspect_iv_ratio);
        ivReadyRecord = (ImageView) findViewById(R.id.aliyun_ready_record);
        aliyunSwitchLight = (ImageView) findViewById(R.id.aliyun_switch_light);

        aliyunSwitchCamera = (ImageView) findViewById(R.id.aliyun_switch_camera);
        aliyunComplete = findViewById(R.id.aliyun_complete);
        aliyunBack = (ImageView) findViewById(R.id.aliyun_back);
        aliyunRecordLayoutBottom = (LinearLayout) findViewById(R.id.aliyun_record_layout_bottom);
        aliyunRateBar = (LinearLayout) findViewById(R.id.aliyun_rate_bar);
        aliyunRateQuarter = (TextView) findViewById(R.id.aliyun_rate_quarter);
        aliyunRateHalf = (TextView) findViewById(R.id.aliyun_rate_half);
        aliyunRateOrigin = (TextView) findViewById(R.id.aliyun_rate_origin);
        aliyunRateDouble = (TextView) findViewById(R.id.aliyun_rate_double);
        aliyunRateDoublePower2 = (TextView) findViewById(R.id.aliyun_rate_double_power2);
        aliyunRecordDuration = (TextView) findViewById(R.id.aliyun_record_duration);
        aliyunRecordBtn = (FrameLayout) findViewById(R.id.aliyun_record_bg);
        aliyunDelete = (TextView) findViewById(R.id.aliyun_delete);
        llBeautyFace = findViewById(R.id.ll_beauty_face);

        mPickerView = findViewById(R.id.alivc_video_picker_view);
        mTitleView = findViewById(R.id.alivc_record_title_view);
        mRecordTipTV = findViewById(R.id.alivc_record_tip_tv);

        //uiStyleConfig
        //????????????
        //?????????
        //????????????????????? - ?????????
        //???????????????????????????????????????????????????
        //????????????mv?????????????????????
        UIConfigManager.setImageResourceConfig(
            new ImageView[] {ivReadyRecord, findViewById(R.id.iv_beauty_face)}
            , new int[] {R.attr.countdownImage, R.attr.faceImage, R.attr.magicImage}
            , new int[] {R.mipmap.alivc_svideo_icon_magic, R.mipmap.alivc_svideo_icon_beauty_face, R.mipmap.alivc_svideo_icon_gif_effect}
        );

        //?????????????????????
        //??????????????????????????????
        UIConfigManager.setImageResourceConfig(
            new TextView[] {aliyunDelete, aliyunRecordDuration}
            , new int[] {0, 0}
            , new int[] {R.attr.deleteImage, R.attr.dotImage}
            , new int[] {R.mipmap.alivc_svideo_icon_delete, R.mipmap.alivc_svideo_record_time_tip});
        //????????????????????????
        aliyunSwitchCamera.setImageDrawable(getSwitchCameraDrawable());
        List<String> strings = new ArrayList<>(2);
        strings.add(getResources().getString(R.string.alivc_recorder_control_click));
        strings.add(getResources().getString(R.string.alivc_recorder_control_long_press));
        mPickerView.setData(strings);
        //?????????????????????????????????
        mPickerView.setCenterItemBackground(UIConfigManager.getDrawableResources(getContext(), R.attr.triangleImage, R.mipmap.alivc_svideo_icon_selected_indicator));
    }

    /**
     * ?????????????????????????????????selector
     *
     * @return Drawable
     */
    private Drawable getSwitchCameraDrawable() {

        Drawable drawable = UIConfigManager.getDrawableResources(getContext(), R.attr.switchCameraImage, R.mipmap.alivc_svideo_icon_magic_turn);
        Drawable pressDrawable = drawable.getConstantState().newDrawable().mutate();
        pressDrawable.setAlpha(66);//?????????60%
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed},
                                   pressDrawable);
        stateListDrawable.addState(new int[] {},
                                   drawable);
        return stateListDrawable;
    }

    /**
     * ?????????view????????????
     */
    private void setViewListener() {
        mPickerView.setOnSelectedListener(new BaseScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(BaseScrollPickerView baseScrollPickerView, int position) {
                Log.i(TAG, "onSelected:" + position);
                //if (FastClickUtil.isFastClick()) {
                //    return;
                //}
                if (position == 0) {
                    recordMode = RecordMode.SINGLE_CLICK;
                } else {
                    recordMode = RecordMode.LONG_PRESS;
                }
                updateRecordBtnView();
            }
        });

        // ????????????
        aliyunBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mListener != null) {
                    mListener.onBackClick();
                }
            }
        });

        // ????????????
        ivReadyRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (isRecording) {
                    return;
                }
                if (recordState == RecordState.STOP) {
                    recordState = RecordState.READY;
                    updateAllViews();
                    if (mListener != null) {
                        mListener.onReadyRecordClick(false);
                    }
                } else {
                    recordState = RecordState.STOP;
                    if (mListener != null) {
                        mListener.onReadyRecordClick(true);
                    }
                }

            }
        });

        // ?????????
        aliyunSwitchLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }

                if (flashType == FlashType.ON) {
                    flashType = FlashType.OFF;
                } else {
                    flashType = FlashType.ON;
                }

                updateLightSwitchView();
                if (mListener != null) {
                    mListener.onLightSwitch(flashType);
                }
            }
        });

        // ????????????
        aliyunSwitchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mListener != null) {
                    mListener.onCameraSwitch();
                }
            }
        });

        // ?????????(????????????)
        aliyunComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mListener != null) {
                    mListener.onNextClick();
                }
            }
        });
        aliyunRateQuarter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                recordRate = RecordRate.VERY_FLOW;
                if (mListener != null) {
                    mListener.onRateSelect(recordRate.getRate());
                }
                updateRateItemView();
            }
        });
        aliyunRateHalf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                recordRate = RecordRate.FLOW;
                if (mListener != null) {
                    mListener.onRateSelect(recordRate.getRate());
                }
                updateRateItemView();
            }
        });
        aliyunRateOrigin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                recordRate = RecordRate.STANDARD;
                if (mListener != null) {
                    mListener.onRateSelect(recordRate.getRate());
                }
                updateRateItemView();
            }
        });
        aliyunRateDouble.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                recordRate = RecordRate.FAST;
                if (mListener != null) {
                    mListener.onRateSelect(recordRate.getRate());
                }
                updateRateItemView();
            }
        });
        aliyunRateDoublePower2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                recordRate = RecordRate.VERY_FAST;
                if (mListener != null) {
                    mListener.onRateSelect(recordRate.getRate());
                }
                updateRateItemView();
            }
        });
        // ????????????
        llBeautyFace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (FastClickUtil.isFastClick()) {
                        return;
                    }
                    mListener.onBeautyFaceClick();
                }
            }
        });
        // ????????????
        aliyunDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteClick();
                }
            }
        });

        mAlivcAspectRatio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                changeAspectRatio();

                if (mListener != null) {
                    mListener.onChangeAspectRatioClick(mAspectRatio);
                }

            }
        });

        mAlivcTakePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }

                if (mListener != null) {
                    mListener.onTakePhotoClick();
                }
            }
        });
        //?????????????????????????????????????????????
        aliyunRecordBtn.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (FastClickUtil.isRecordWithOtherClick()) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (recordState != RecordState.COUNT_DOWN_RECORDING && recordMode == RecordMode.LONG_PRESS) {
                if (isRecording) {
                    return true;
                } else {
                    if (mListener != null) {
                        mListener.onStartRecordClick();
                    }
                }
            }

        } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                   || event.getAction() == MotionEvent.ACTION_UP) {
            if (recordState == RecordState.COUNT_DOWN_RECORDING) {
                if (mListener != null) {
                    mListener.onStopRecordClick();
                    setRecordState(RecordState.STOP);
                    //?????????????????????????????????
                    if (hasRecordPiece) {
                        setHasRecordPiece(true);
                    }

                }
            } else {
                if (recordMode == RecordMode.LONG_PRESS) {
                    if (mListener != null && recordState == RecordState.RECORDING) {
                        mListener.onStopRecordClick();
                        setRecordState(RecordState.STOP);
                        //?????????????????????????????????
                        if (hasRecordPiece) {
                            setHasRecordPiece(true);
                        }
                    }
                } else {
                    if (recordState == RecordState.RECORDING) {
                        if (mListener != null) {
                            mListener.onStopRecordClick();
                            setRecordState(RecordState.STOP);
                            //?????????????????????????????????
                            if (hasRecordPiece) {
                                setHasRecordPiece(true);
                            }
                        }
                    } else {
                        if (mListener != null && !isRecording) {
                            mListener.onStartRecordClick();
                        }
                    }
                }
            }

        }
        return true;
    }

    /**
     * ????????????????????????
     *
     * @param scaleRate
     */
    private void recordBtnScale(float scaleRate) {
        RelativeLayout.LayoutParams recordBgLp = (RelativeLayout.LayoutParams) aliyunRecordBtn.getLayoutParams();
        recordBgLp.width = (int) (itemWidth * scaleRate);
        recordBgLp.height = (int) (itemWidth * scaleRate);
        aliyunRecordBtn.setLayoutParams(recordBgLp);
    }

    /**
     * ????????????????????????
     */
    private void calculateItemWidth() {
        itemWidth = getResources().getDisplayMetrics().widthPixels / MAX_ITEM_COUNT;
    }

    /**
     * ??????????????????
     */
    private void updateAllViews() {
        //??????????????????????????????????????????view??????
        if (isMusicSelViewShow || recordState == RecordState.READY) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            updateBottomView();
            updateTittleView();
        }
    }

    /**
     * ??????????????????
     */
    private void updateTittleView() {
        if (recordState == RecordState.STOP) {
            mTitleView.setVisibility(VISIBLE);

//            mAlivcAspectRatio.setVisibility(VISIBLE);
            mAlivcAspectRatio.setVisibility(GONE);
            mAlivcTakePhoto.setVisibility(GONE);
//            mAlivcTakePhoto.setVisibility(VISIBLE);

            updateLightSwitchView();
            updateMusicSelView();
            updateCompleteView();
        } else {
            mTitleView.setVisibility(GONE);

            mAlivcAspectRatio.setVisibility(GONE);
            mAlivcTakePhoto.setVisibility(GONE);

        }
        if (mIsMixRecorderType) {
            mAlivcAspectRatio.setVisibility(GONE);
            mAlivcTakePhoto.setVisibility(GONE);
        }
    }

    /**
     * ????????????????????????
     */
    private void updateCompleteView() {
        if (canComplete) {
            aliyunComplete.setSelected(true);
            aliyunComplete.setEnabled(true);
            //????????????????????? - ??????
            //UIConfigManager.setImageResourceConfig(aliyunComplete, R.attr.finishImageAble, R.mipmap.alivc_svideo_icon_next_complete);
        } else {
            aliyunComplete.setSelected(false);
            aliyunComplete.setEnabled(false);
            //????????????????????? - ?????????
            //UIConfigManager.setImageResourceConfig(aliyunComplete, R.attr.finishImageUnable, R.mipmap.alivc_svideo_icon_next_not_ready);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param isClickable true: ?????????, ????????????, false: ????????????, ????????????
     */
    public void updateCutDownView(boolean isClickable) {
        if (isClickable) {
            ivReadyRecord.setColorFilter(null);
            ivReadyRecord.setClickable(true);
        } else {
            ivReadyRecord.setColorFilter(ContextCompat.getColor(getContext(), R.color.alivc_record_color_filter));
            ivReadyRecord.setClickable(false);
        }
        UIConfigManager.setImageResourceConfig(ivReadyRecord, R.attr.countdownImage, R.mipmap.alivc_svideo_icon_magic);
    }

    /**
     * ????????????????????????
     */
    private void updateMusicSelView() {
        Drawable ratioDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.alivc_svideo_icon_aspect_ratio);
        if (hasRecordPiece) {
            //???????????????????????????????????????
            mIVAspectRatio.setColorFilter(ContextCompat.getColor(getContext(), R.color.alivc_record_color_filter), PorterDuff.Mode.MULTIPLY);
            mAlivcAspectRatio.setClickable(false);
            mIVAspectRatio.setImageDrawable(ratioDrawable);
            mTvAspectRatio.setTextColor(ContextCompat.getColor(getContext(), R.color.alivc_record_color_filter));
        } else {
            mIVAspectRatio.clearColorFilter();
            mIVAspectRatio.setImageDrawable(ratioDrawable);
            mAlivcAspectRatio.setClickable(true);
            mTvAspectRatio.setTextColor(ContextCompat.getColor(getContext(), R.color.alivc_common_font_white));

        }
    }

    /**
     * ????????????????????????
     */
    private void updateBottomView() {
        if (isEffectSelViewShow) {
            aliyunRecordLayoutBottom.setVisibility(GONE);
        } else {
            aliyunRecordLayoutBottom.setVisibility(VISIBLE);
            updateModeSelView();
            updateRateItemView();
            updateRecordBtnView();
            updateDeleteView();
            if (recordState == RecordState.STOP) {
                //??????????????????
                llBeautyFace.setVisibility(VISIBLE);

            } else {

                llBeautyFace.setVisibility(INVISIBLE);
            }
        }

    }

    /**
     * ????????????????????????
     */
    private void updateRateItemView() {
        if (recordState == RecordState.RECORDING || recordState == RecordState.COUNT_DOWN_RECORDING) {
            aliyunRateBar.setVisibility(INVISIBLE);
        } else {
            aliyunRateBar.setVisibility(VISIBLE);
            aliyunRateQuarter.setSelected(false);
            aliyunRateHalf.setSelected(false);
            aliyunRateOrigin.setSelected(false);
            aliyunRateDouble.setSelected(false);
            aliyunRateDoublePower2.setSelected(false);
            switch (recordRate) {
            case VERY_FLOW:
                aliyunRateQuarter.setSelected(true);
                break;
            case FLOW:
                aliyunRateHalf.setSelected(true);
                break;
            case STANDARD:
                aliyunRateOrigin.setSelected(true);
                break;
            case FAST:
                aliyunRateDouble.setSelected(true);
                break;
            case VERY_FAST:
                aliyunRateDoublePower2.setSelected(true);
                break;
            default:
                aliyunRateOrigin.setSelected(true);
            }
        }

    }


    private void changeAspectRatio() {
        switch (mAspectRatio) {
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            mAspectRatio = AliyunSnapVideoParam.RATIO_MODE_3_4;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            mAspectRatio = AliyunSnapVideoParam.RATIO_MODE_1_1;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            mAspectRatio = AliyunSnapVideoParam.RATIO_MODE_9_16;
            break;
        default:
            mAspectRatio = AliyunSnapVideoParam.RATIO_MODE_9_16;
            break;
        }
    }

    /**
     * ????????????????????????view
     */
    private void updateModeSelView() {
        if (hasRecordPiece || recordState == RecordState.RECORDING || recordState == RecordState.COUNT_DOWN_RECORDING) {
            mPickerView.setVisibility(GONE);
        } else {
            mPickerView.setVisibility(VISIBLE);
            if (recordMode == RecordMode.SINGLE_CLICK) {
                mPickerView.setSelectedPosition(0);
            } else {
                mPickerView.setSelectedPosition(1);
            }
        }
    }

    /**
     * ??????????????????
     */
    private void updateDeleteView() {

        if (!hasRecordPiece || recordState == RecordState.RECORDING
                || recordState == RecordState.COUNT_DOWN_RECORDING) {
            aliyunDelete.setVisibility(GONE);
        } else {
            aliyunDelete.setVisibility(VISIBLE);
        }
    }

    /**
     * ????????????????????????
     */
    private void updateRecordBtnView() {


        if (recordState == RecordState.STOP) {
            recordBtnScale(1f);
            //?????????????????? - ???????????????
            UIConfigManager.setImageBackgroundConfig(aliyunRecordBtn, R.attr.videoShootImageNormal, R.mipmap.alivc_svideo_bg_record_storp);
            aliyunRecordDuration.setVisibility(GONE);
            mRecordTipTV.setVisibility(VISIBLE);
            if (recordMode == RecordMode.LONG_PRESS) {
                mRecordTipTV.setText(R.string.alivc_recorder_control_press);
            } else {
                mRecordTipTV.setText("");
            }
        } else if (recordState == RecordState.COUNT_DOWN_RECORDING) {
            mRecordTipTV.setVisibility(GONE);
            aliyunRecordDuration.setVisibility(VISIBLE);
            recordBtnScale(1.25f);
            aliyunRecordBtn.setBackgroundResource(R.mipmap.alivc_svideo_bg_record_pause);
            //?????????????????? - ?????????
            UIConfigManager.setImageBackgroundConfig(aliyunRecordBtn, R.attr.videoShootImageShooting, R.mipmap.alivc_svideo_bg_record_pause);

        } else {
            mRecordTipTV.setVisibility(GONE);
            aliyunRecordDuration.setVisibility(VISIBLE);
            recordBtnScale(1.25f);
            if (recordMode == RecordMode.LONG_PRESS) {
                aliyunRecordBtn.setBackgroundResource(R.mipmap.alivc_svideo_bg_record_start);
                //?????????????????? - ?????????
                UIConfigManager.setImageBackgroundConfig(aliyunRecordBtn, R.attr.videoShootImageLongPressing, R.mipmap.alivc_svideo_bg_record_start);
            } else {
                aliyunRecordBtn.setBackgroundResource(R.mipmap.alivc_svideo_bg_record_pause);
                //?????????????????? - ?????????
                UIConfigManager.setImageBackgroundConfig(aliyunRecordBtn, R.attr.videoShootImageShooting, R.mipmap.alivc_svideo_bg_record_pause);
            }
        }
    }

    /**
     * ?????????????????????
     */
    private void updateLightSwitchView() {
        if (cameraType == CameraType.FRONT) {
            aliyunSwitchLight.setClickable(false);
            // ?????????????????????, ?????????????????????
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.aliyun_svideo_icon_magic_light_off);
            aliyunSwitchLight.setImageDrawable(drawable);
            aliyunSwitchLight.setColorFilter(ContextCompat.getColor(getContext(), R.color.alivc_record_color_filter), PorterDuff.Mode.MULTIPLY);

        } else if (cameraType == CameraType.BACK) {
            aliyunSwitchLight.setClickable(true);
            // ?????????????????????, ???????????????
            aliyunSwitchLight.clearColorFilter();
            switch (flashType) {
            case ON:
                aliyunSwitchLight.setSelected(true);
                aliyunSwitchLight.setActivated(false);
                UIConfigManager.setImageResourceConfig(aliyunSwitchLight, R.attr.lightImageOpen, R.mipmap.aliyun_svideo_icon_magic_light);
                break;
            case OFF:
                aliyunSwitchLight.setSelected(true);
                aliyunSwitchLight.setActivated(true);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.aliyun_svideo_icon_magic_light_off);
                aliyunSwitchLight.setImageDrawable(drawable);
                break;
            default:
                break;
            }
        }

    }

    public FlashType getFlashType() {
        return flashType;
    }

    public void setFlashType(FlashType flashType) {
        this.flashType = flashType;
        updateLightSwitchView();
    }

    public CameraType getCameraType() {
        return cameraType;
    }

    public RecordState getRecordState() {
        if (recordState.equals(RecordState.COUNT_DOWN_RECORDING) || recordState.equals(RecordState.RECORDING)) {
            return RecordState.RECORDING;
        }
        return recordState;
    }

    public void setRecordState(RecordState recordState) {
        if (recordState == RecordState.RECORDING) {
            if (this.recordState == RecordState.READY) {
                this.recordState = RecordState.COUNT_DOWN_RECORDING;
            } else {
                this.recordState = recordState;
            }
        } else {
            this.recordState = recordState;
        }
        updateAllViews();
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public boolean isRecording() {
        return isRecording;
    }

    /**
     * ?????????????????????
     *
     * @param hasRecordPiece
     */
    public void setHasRecordPiece(boolean hasRecordPiece) {
        this.hasRecordPiece = hasRecordPiece;
        updateModeSelView();
        updateDeleteView();
        updateMusicSelView();
    }

    /**
     * ??????????????????????????????
     *
     * @param musicSelViewShow
     */
    public void setMusicSelViewShow(boolean musicSelViewShow) {
        isMusicSelViewShow = musicSelViewShow;
        updateAllViews();
    }

    /**
     * ??????????????????????????????
     *
     * @param effectSelViewShow
     */
    public void setEffectSelViewShow(boolean effectSelViewShow) {
        isEffectSelViewShow = effectSelViewShow;
        updateBottomView();
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param recordTime
     */
    public void setRecordTime(String recordTime) {
        aliyunRecordDuration.setText(recordTime);
    }

    /**
     * ??????????????????????????????
     *
     * @param mListener
     */
    public void setControlViewListener(ControlViewListener mListener) {
        this.mListener = mListener;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param cameraType
     */
    public void setCameraType(CameraType cameraType) {
        this.cameraType = cameraType;
        updateLightSwitchView();
    }

    /**
     * ??????complete????????????????????????
     *
     * @param enable
     */
    public void setCompleteEnable(boolean enable) {
        canComplete = enable;
        updateCompleteView();
    }



    /**
     * ??????????????????
     */
    public void setAspectRatio(int radio) {
        mAspectRatio = radio;
    }


    /**
     * ??????????????????????????????text
     * @param stringId ??????id
     */
    public void setAliyunCompleteText(int stringId) {
        aliyunComplete.setText(stringId);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    public void setRecordType(Boolean recordType) {
        mIsMixRecorderType = recordType;
        updateTittleView();
    }
}
