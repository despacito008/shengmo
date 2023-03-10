package com.aliyun.svideo.recorder.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aliyun.common.global.AliyunTag;
import com.aliyun.common.utils.BitmapUtil;
import com.aliyun.common.utils.CommonUtil;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.svideo.base.utils.VideoInfoUtils;
import com.aliyun.svideo.base.widget.beauty.BeautyRaceConstants;
import com.aliyun.svideo.base.widget.beauty.BeautyShapeConstants;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyShapeItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyShapeParamsChangeListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyTableItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.sharp.BeautyShapeParams;
import com.aliyun.svideo.common.utils.LanguageUtils;
import com.aliyun.svideo.common.utils.ScreenUtils;
import com.aliyun.svideo.common.utils.UriUtils;
import com.aliyun.svideo.record.R;
import com.aliyun.svideo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.recorder.bean.RememberBeautyBean;
import com.aliyun.svideo.recorder.bean.RememberBeautyShapeBean;
import com.aliyun.svideo.recorder.bean.RenderingMode;
import com.aliyun.svideo.recorder.faceunity.FaceUnityManager;
import com.aliyun.svideo.recorder.mixrecorder.AlivcIMixRecorderInterface;
import com.aliyun.svideo.recorder.race.RaceManager;
import com.aliyun.svideo.recorder.util.RecordCommon;
import com.aliyun.svideo.recorder.util.FixedToastUtils;
import com.aliyun.svideo.recorder.util.OrientationDetector;
import com.aliyun.svideo.recorder.util.SharedPreferenceUtils;
import com.aliyun.svideo.recorder.util.TimeFormatterUtils;
import com.aliyun.svideo.recorder.view.control.CameraType;
import com.aliyun.svideo.recorder.view.control.ControlView;
import com.aliyun.svideo.recorder.view.control.ControlViewListener;
import com.aliyun.svideo.recorder.view.control.FlashType;
import com.aliyun.svideo.recorder.view.control.RecordState;
import com.aliyun.svideo.recorder.view.countdown.AlivcCountDownView;
import com.aliyun.svideo.recorder.view.dialog.AnimFilterEffectChooser;
import com.aliyun.svideo.recorder.view.dialog.BeautyEffectChooser;
import com.aliyun.svideo.recorder.view.dialog.DialogVisibleListener;
import com.aliyun.svideo.recorder.view.dialog.FilterEffectChooser;
import com.aliyun.svideo.recorder.view.dialog.GIfEffectChooser;
import com.aliyun.svideo.recorder.view.effects.face.BeautyFaceDetailChooser;
import com.aliyun.svideo.recorder.view.effects.face.BeautyService;
import com.aliyun.svideo.recorder.view.effects.filter.EffectInfo;
import com.aliyun.svideo.recorder.view.effects.filter.OnFilterItemClickListener;
import com.aliyun.svideo.recorder.view.effects.filter.animfilter.OnAnimFilterItemClickListener;
import com.aliyun.svideo.recorder.view.effects.paster.PasterSelectListener;
import com.aliyun.svideo.recorder.view.effects.shape.BeautyShapeDetailChooser;
import com.aliyun.svideo.recorder.view.effects.skin.BeautySkinDetailChooser;
import com.aliyun.svideo.recorder.view.focus.FocusView;
import com.aliyun.svideo.recorder.view.music.MusicChooser;
import com.aliyun.svideo.recorder.view.music.MusicSelectListener;
import com.aliyun.svideo.downloader.zipprocessor.DownloadFileUtils;
import com.aliyun.mix.AliyunMixRecorderDisplayParam;
import com.aliyun.mix.AliyunMixTrackLayoutParam;
import com.aliyun.mix.AliyunMixMediaInfoParam;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.base.http.MusicFileBean;
import com.aliyun.svideo.base.widget.ProgressDialog;
import com.aliyun.svideo.base.widget.RecordTimelineView;
import com.aliyun.svideo.base.widget.beauty.BeautyConstants;
import com.aliyun.svideo.base.widget.beauty.BeautyDetailSettingView;
import com.aliyun.svideo.base.widget.beauty.BeautyParams;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyLevel;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyMode;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyDetailClickListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyFaceItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyModeChangeListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyParamsChangeListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautySkinItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnViewClickListener;
import com.aliyun.svideo.common.utils.DensityUtils;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBase;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBean;
import com.aliyun.svideo.sdk.external.struct.effect.EffectFilter;
import com.aliyun.svideo.sdk.external.struct.effect.EffectImage;
import com.aliyun.svideo.sdk.external.struct.effect.EffectPaster;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.form.PreviewPasterForm;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraParam;
import com.aliyun.svideo.sdk.external.struct.recorder.MediaInfo;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.google.gson.Gson;
import com.qu.preview.callback.OnFrameCallBack;
import com.qu.preview.callback.OnTextureIdCallBack;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ?????????(> 3.6.5??????)????????????????????????????????????
 */
public class AliyunSVideoRecordView extends FrameLayout
    implements DialogVisibleListener, ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = AliyunSVideoRecordView.class.getSimpleName();
    private static final String TAG_GIF_CHOOSER = "gif";
    private static final String TAG_BEAUTY_CHOOSER = "beauty";
    private static final String TAG_MUSIC_CHOOSER = "music";
    private static final String TAG_FILTER_CHOOSER = "filter";
    private static final String TAG_ANIM_FILTER_CHOOSER = "anim_filter";
    private static final String TAG_BEAUTY_DETAIL_FACE_CHOOSER = "beautyFace";
    private static final String TAG_BEAUTY_DETAIL_SKIN_CHOOSER = "beautySkin";
    private static final String TAG_BEAUTY_DETAIL_SHAPE_CHOOSER = "beautyShape";
    //??????????????????
    private static final int MIN_RECORD_TIME = 0;
    //??????????????????
    private static final int MAX_RECORD_TIME = Integer.MAX_VALUE;

    /**
     * v3.7.8??????, GlsurfaceView --> SurfaceView
     * AliyunIRecorder.setDisplayView(GLSurfaceView surfaceView) =====>> AliyunIRecorder.setDisplayView(SurfaceView surfaceView)
     */
    private SurfaceView mRecorderSurfaceView;
    private SurfaceView mPlayerSurfaceView;

    private ControlView mControlView;
    private RecordTimelineView mRecordTimeView;
    private AlivcCountDownView mCountDownView;

    private AlivcIMixRecorderInterface recorder;

    private AliyunIClipManager clipManager;
    private com.aliyun.svideo.sdk.external.struct.recorder.CameraType cameraType
        = com.aliyun.svideo.sdk.external.struct.recorder.CameraType.FRONT;
    private FragmentActivity mActivity;
    private boolean isOpenFailed = false;
    //????????????????????????,readyview???????????????true????????????false
    private boolean isLoadingReady = false;
    //?????????????????????????????????
    private boolean isMaxDuration = false;
    //????????????
    private int recordTime = 0;

    //??????????????????
    private int minRecordTime = 2000;
    //??????????????????
    private int maxRecordTime = 15 * 1000;
    //???????????????
    private int mGop = 5;
    //????????????
    private VideoQuality mVideoQuality = VideoQuality.HD;
    //????????????
    private int mRatioMode = AliyunSnapVideoParam.RATIO_MODE_3_4;
    //????????????
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;
    //????????????
    private RenderingMode mRenderingMode = RenderingMode.Race;
    //?????????race?????????
    private boolean isSvideoRace = false;

    //??????????????????
    private MediaInfo mOutputInfo;

    //????????????????????????
    private AliyunMixMediaInfoParam mMixInputInfo;

    //???????????????????????????
    private String mMixVideoPath;

    private GIfEffectChooser gifEffectChooser;
    /**
     * ??????????????????
     */
    private FilterEffectChooser filterEffectChooser;
    /**
     * ????????????????????????
     */
    private AnimFilterEffectChooser mAnimFilterEffectChooser;
    //???????????????
    private int mResolutionMode = AliyunSnapVideoParam.RESOLUTION_540P;

    private BeautyEffectChooser beautyEffectChooser;
    //?????????????????????
    private EffectPaster effectPaster;
    private OrientationDetector orientationDetector;
    private int rotation;
    private MusicChooser musicChooseView;
    /**
     * ??????????????????????????????faceUnity?????????
     */
    private FaceUnityManager faceUnityManager;
    private RaceManager raceManager;
    /**
     * ???????????????NV21??????
     */
    private byte[] frameBytes;
    private byte[] mFuImgNV21Bytes;
    /**
     * ???????????????
     */
    private int frameWidth;
    /**
     * ???????????????
     */
    private int frameHeight;
    /**
     * faceUnity??????
     */
    private int mFrameId = 0;
    private BeautyParams beautyParams;
    private BeautyShapeParams beautyShapeParams;
    private BeautyFaceDetailChooser beautyFaceDetailChooser;
    private BeautySkinDetailChooser beautySkinDetailChooser;
    private BeautyShapeDetailChooser beautyShapeDetailChooser;
    /**
     * ??????????????????dialog??????????????????
     */
    private boolean isbeautyDetailShowing;

    /**
     * ??????????????????
     */
    private BeautyLevel defaultBeautyLevel = BeautyLevel.BEAUTY_LEVEL_THREE;

    /**
     * ??????????????????
     */
    private BeautyMode currentBeautyFaceMode = BeautyMode.Advanced;
    public static final int TYPE_FILTER = 1;
    public static final int TYPE_MUSIC = 3;
    private LinkedHashMap<Integer, Object> mConflictEffects = new LinkedHashMap<>();
    private EffectBean effectMusic;
    private AsyncTask<Void, Integer, Integer> finishRecodingTask;
    private AsyncTask<Void, Void, Void> faceTrackPathTask;

    /**
     * ??????filter?????????item??????
     */
    private int currentFilterPosition;

    /**
     * ????????????filter?????????item??????
     */
    private int mCurrentAnimFilterPosition;
    /**
     * ???????????????????????????, ?????????3
     */
    private int currentBeautyFacePosition = 3;

    /**
     * ???????????????????????????, ?????????3
     */
    private int currentBeautyFaceNormalPosition = 3;

    /**
     * ?????????????????????item??????, ?????????3
     */
    private int currentBeautySkinPosition = 3;
    /**
     * ?????????????????????item??????, ?????????0
     */
    private int currentBeautyShapePosition = 0;
    /**
     * ??????mv?????????, ???????????????,???????????????mv
     */
    private boolean isAllowChangeMv = true;
    private AsyncTask<Void, Void, Void> mFaceUnityTask;
    private List<BeautyParams> rememberParamList;
    private RememberBeautyBean rememberBeautyBean;
    private List<BeautyParams> rememberRaceParamList;
    private RememberBeautyBean rememberRaceBeautyBean;
    private List<BeautyShapeParams> rememberShapeParamList;
    private RememberBeautyShapeBean rememberBeautyShapeBean;
    private ProgressDialog progressBar;

    /**
     * ??????????????????????????????????????????, ????????????, ????????????????????????, restoreConflictEffect()?????????mv?????????, ??????????????????????????????
     * <p>
     * true: ?????? false: ?????????
     */
    private boolean isMusicViewShowing;

    /**
     * faceUnity?????????????????? true: ??????????????? false: ???????????????
     */
    private static boolean faceInitResult;
    /**
     * ??????????????????
     */
    private boolean mIsBackground;
    private BeautyService beautyService;

    private boolean isHasMusic = false;
    private FocusView mFocusView;
    private EffectFilter mCurrentAnimFilterEffect;
    private boolean mIsUseFlip = false;

    public boolean isHasMusic() {
        return isHasMusic;
    }

    /**
     * ????????????????????????????????????
     */
    private MusicSelectListener mOutMusicSelectListener;

    private boolean isRaceDrawed = false;

    /**
     * race?????????????????????
     */
    private EffectImage effectImage;

    /**
     * ??????????????????????????????????????????????????????????????????????????????MV?????????MV??????????????????????????????MV???????????????????????? ??????????????????????????????????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????????????? ?????????
     */
    private void restoreConflictEffect() {
        if (!mConflictEffects.isEmpty() && recorder != null) {
            for (Map.Entry<Integer, Object> entry : mConflictEffects.entrySet()) {
                switch (entry.getKey()) {
                case TYPE_FILTER:
                    recorder.applyFilter((EffectFilter) entry.getValue());
                    break;
                case TYPE_MUSIC:
                    EffectBean music = (EffectBean) entry.getValue();

                    if (music != null) {
                        recorder.setMusic(music.getPath(), music.getStartTime(), music.getDuration());
                        Log.i(TAG, "path :" + music.getPath());
                        // ???????????????????????????????????????????????????,
                        // ???????????????, ?????????????????????????????????, ???????????????????????????
                        isHasMusic = !TextUtils.isEmpty(music.getPath());
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    /**
     * ?????????onPause??????????????????, ????????????????????????????????????, ??????????????????
     */
    private String filterSourcePath;

    /**
     * ?????????????????????back??????
     */
    private boolean isbeautyDetailBack;

    /**
     * ????????????sdk???oncomplete????????????, ????????????startRecord
     */
    private boolean tempIsComplete = true;

    public AliyunSVideoRecordView(Context context) {
        super(context);
    }

    public AliyunSVideoRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AliyunSVideoRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initVideoView() {

        initControlView();
        initCountDownView();
        initBeautyParam();
        initRecordTimeView();
        initFocusView();
        if (mRenderingMode == RenderingMode.FaceUnity) {
            initFaceUnity(getContext());
            SharedPreferenceUtils.setIsRaceMode(mActivity, false);
        } else {
            RaceManager.getInstance().setUp(mActivity);
            SharedPreferenceUtils.setIsRaceMode(mActivity, true);
            raceManager = RaceManager.getInstance();
            raceDefaultParam();
        }
        setFaceTrackModePath();

    }

    /**
     * ??????focus
     */
    private void initFocusView() {
        mFocusView = new FocusView(getContext());
        mFocusView.setPadding(10, 10, 10, 10);
        addSubView(mFocusView);
    }

    private void initBeautyParam() {
        beautyParamCopy();
        //???????????????????????????????????????????????????
        currentBeautyFaceMode = SharedPreferenceUtils.getBeautyMode(getContext());

        currentBeautySkinPosition = SharedPreferenceUtils.getBeautySkinLevel(getContext());
        currentBeautyFacePosition = SharedPreferenceUtils.getBeautyFaceLevel(getContext());
        currentBeautyFaceNormalPosition = SharedPreferenceUtils.getBeautyNormalFaceLevel(getContext());
        currentBeautyShapePosition = SharedPreferenceUtils.getBeautyShapeLevel(getContext());
    }

    private void beautyParamCopy() {

//        faceunity??????
        rememberBeautyBean = new RememberBeautyBean();
        rememberParamList = new ArrayList<>();
        int size = BeautyConstants.BEAUTY_MAP.size();
        // ??????????????????????????????????????????, ?????????????????????json????????????,??????json?????????, ??????????????????
        String jsonBeautyParam = SharedPreferenceUtils.getBeautyParams(getContext());
        if (TextUtils.isEmpty(jsonBeautyParam)) {
            for (int i = 0; i < size; i++) {
                BeautyParams beautyParams = BeautyConstants.BEAUTY_MAP.get(i);
                BeautyParams rememberParam = beautyParams.clone();
                rememberParamList.add(rememberParam);
            }
        } else {
            for (int i = 0; i < size; i++) {
                BeautyParams beautyParams = getBeautyParams(i);
                if (beautyParams == null) {
                    BeautyParams defaultBeautyParams = BeautyConstants.BEAUTY_MAP.get(i);
                    beautyParams = defaultBeautyParams.clone();
                }
                rememberParamList.add(beautyParams);
            }
        }
        rememberBeautyBean.setBeautyList(rememberParamList);


//        race??????
        rememberRaceBeautyBean = new RememberBeautyBean();
        rememberRaceParamList = new ArrayList<>();
        int raceSize = BeautyRaceConstants.BEAUTY_MAP.size();
        // ??????????????????????????????????????????, ?????????????????????json????????????,??????json?????????, ??????????????????
        String jsonRaceBeautyParam = SharedPreferenceUtils.getRaceBeautyParams(getContext());
        if (TextUtils.isEmpty(jsonRaceBeautyParam)) {
            for (int i = 0; i < raceSize; i++) {
                BeautyParams beautyParams = BeautyRaceConstants.BEAUTY_MAP.get(i);
                BeautyParams rememberParam = beautyParams.clone();
                rememberRaceParamList.add(rememberParam);
            }
        } else {
            for (int i = 0; i < raceSize; i++) {
                BeautyParams beautyParams = getBeautyParams(i);
                rememberRaceParamList.add(beautyParams);
            }
        }
        rememberRaceBeautyBean.setBeautyList(rememberRaceParamList);

//      ??????
        rememberBeautyShapeBean = new RememberBeautyShapeBean();

        rememberShapeParamList = new ArrayList<>();

        int shapeSize = BeautyShapeConstants.BEAUTY_MAP.size();
        // ??????????????????????????????????????????, ?????????????????????json????????????,??????json?????????, ??????????????????
        String jsonBeautyShapeParam = SharedPreferenceUtils.getBeautyShapeParams(getContext());
        if (TextUtils.isEmpty(jsonBeautyShapeParam)) {
            for (int i = 0; i < shapeSize; i++) {
                BeautyShapeParams beautyParams = BeautyShapeConstants.BEAUTY_MAP.get(i);
                BeautyShapeParams rememberParam = beautyParams.clone();
                rememberShapeParamList.add(rememberParam);
            }
        } else {
            for (int i = 0; i < shapeSize; i++) {
                BeautyShapeParams beautyParams = getBeautyShapeParams(i);
                if (beautyParams == null) {
                    beautyParams = BeautyShapeConstants.BEAUTY_MAP.get(i);
                }
                rememberShapeParamList.add(beautyParams);
            }
        }
        rememberBeautyShapeBean.setBeautyList(rememberShapeParamList);
    }

    public void onPause() {
        mIsBackground = true;
    }

    public void onResume() {
        mIsBackground = false;
    }

    public void onStop() {
        if (mFocusView != null) {
            mFocusView.activityStop();
        }
    }

    private void initFaceUnity(Context context) {
        if (!faceInitResult) {
            mFaceUnityTask = new FaceUnityTask(this).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void isUseFlip(boolean isUseFlip) {
        this.mIsUseFlip = isUseFlip;
    }

    private static class FaceUnityTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<AliyunSVideoRecordView> weakReference;
        private Context mContext;
        FaceUnityTask(AliyunSVideoRecordView recordView) {
            weakReference = new WeakReference<>(recordView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AliyunSVideoRecordView recordView = weakReference.get();
            if (recordView != null) {
                mContext = recordView.getContext();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AliyunSVideoRecordView recordView = weakReference.get();
            if (recordView != null && mContext != null && FaceUnityManager.getInstance().isInit()) {
                recordView.faceUnityManager = FaceUnityManager.getInstance();
                faceInitResult = recordView.faceUnityManager.createBeautyItem(mContext);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AliyunSVideoRecordView recordView = weakReference.get();
            if (recordView != null && mContext != null && FaceUnityManager.getInstance().isInit()) {
                recordView.faceunityDefaultParam();
            }
        }
    }

    /**
     * ??????????????????view
     */
    private void initCountDownView() {
        if (mCountDownView == null) {
            mCountDownView = new AlivcCountDownView(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            addView(mCountDownView, params);
        }
    }

    private void initRecordTimeView() {
        mRecordTimeView = new RecordTimelineView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dip2px(getContext(), 6));

        params.setMargins(DensityUtils.dip2px(getContext(), 12),
                          DensityUtils.dip2px(getContext(), 6),
                          DensityUtils.dip2px(getContext(), 12),
                          0);
        mRecordTimeView.setColor(R.color.alivc_record_bg_timeview_duraton, R.color.alivc_record_bg_timeview_selected, R.color.alivc_common_white,
                                 R.color.alivc_record_bg_timeview);
        mRecordTimeView.setMaxDuration(clipManager.getMaxDuration());
        mRecordTimeView.setMinDuration(clipManager.getMinDuration());
        addView(mRecordTimeView, params);
        mRecordTimeView.setMaxDuration(getMaxRecordTime());
        mRecordTimeView.setMinDuration(minRecordTime);
    }

    /**
     * ?????????RecordersurfaceView
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initRecorderSurfaceView() {
        mRecorderSurfaceView = new SurfaceView(getContext());
        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        final GestureDetector gestureDetector = new GestureDetector(getContext(),
        new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (recorder == null) {
                    return true;
                }
                float x = e.getX() / mRecorderSurfaceView.getWidth();
                float y = e.getY() / mRecorderSurfaceView.getHeight();
                recorder.setFocus(x, y);

                mFocusView.showView();
                mFocusView.setLocation(e.getRawX(), e.getRawY());
                return true;
            }
        });
        mRecorderSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() >= 2) {
                    scaleGestureDetector.onTouchEvent(event);
                } else if (event.getPointerCount() == 1) {
                    gestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });
        //????????????surFaceView
        recorder.setDisplayView(mRecorderSurfaceView, mPlayerSurfaceView);

        addSubView(mRecorderSurfaceView);
        mRecorderSurfaceView.setLayoutParams(recorder.getLayoutParams());

    }

    /**
     * ?????????PlayerSurfaceView
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initPlayerSurfaceView() {
        mPlayerSurfaceView = new SurfaceView(getContext());
        addSubView(mPlayerSurfaceView);
    }


    /**
     * ??????????????????view
     */
    private void initControlView() {
        mControlView = new ControlView(getContext());
        if (isSvideoRace) {
            mControlView.setAliyunCompleteText(R.string.alivc_base_svideo_save);
        } else {
            mControlView.setAliyunCompleteText(R.string.alivc_base_svideo_next);
        }
        mControlView.setControlViewListener(new ControlViewListener() {
            @Override
            public void onBackClick() {
                if (mBackClickListener != null) {
                    mBackClickListener.onClick();
                }
            }

            @Override
            public void onNextClick() {
                // ????????????
                if (!isStopToCompleteDuration) {
                    finishRecording();
                }
            }

            @Override
            public void onBeautyFaceClick() {
                showBeautyFaceView();
            }

            @Override
            public void onMusicClick() {
                showMusicSelView();
            }

            @Override
            public void onCameraSwitch() {
                if (recorder != null) {
                    int cameraId = recorder.switchCamera();
                    for (com.aliyun.svideo.sdk.external.struct.recorder.CameraType type : com.aliyun.svideo.sdk
                            .external.struct.recorder.CameraType
                            .values()) {
                        if (type.getType() == cameraId) {
                            cameraType = type;
                        }
                    }
                    if (mControlView != null) {
                        for (CameraType type : CameraType.values()) {
                            if (type.getType() == cameraId) {
                                mControlView.setCameraType(type);
                            }
                        }

                        if (mControlView.getFlashType() == FlashType.ON
                                && mControlView.getCameraType() == CameraType.BACK) {
                            recorder.setLight(com.aliyun.svideo.sdk.external.struct.recorder.FlashType.TORCH);
                        }
                    }
                }
            }

            @Override
            public void onLightSwitch(FlashType flashType) {
                if (recorder != null) {
                    for (com.aliyun.svideo.sdk.external.struct.recorder.FlashType type : com.aliyun.svideo.sdk
                            .external.struct.recorder.FlashType
                            .values()) {
                        if (flashType.toString().equals(type.toString())) {
                            recorder.setLight(type);
                        }
                    }

                }
                if (mControlView.getFlashType() == FlashType.ON
                        && mControlView.getCameraType() == CameraType.BACK) {
                    recorder.setLight(com.aliyun.svideo.sdk.external.struct.recorder.FlashType.TORCH);
                }
            }

            @Override
            public void onRateSelect(float rate) {
                if (recorder != null) {
                    recorder.setRate(rate);
                }
            }

            @Override
            public void onGifEffectClick() {
                showGifEffectView();
            }

            @Override
            public void onReadyRecordClick(boolean isCancel) {
                if (isStopToCompleteDuration) {
                    /*TODO  ????????????????????? SDK ???????????????onComplete,?????????????????????crash */
                    return;
                }
                if (isCancel) {
                    cancelReadyRecord();
                } else {
                    showReadyRecordView();
                }

            }

            @Override
            public void onStartRecordClick() {
                if (!tempIsComplete) {
                    // ??????????????????????????????????????????????????????onComplete???????????????????????????????????????,
                    // ??????SDK?????????ANR??????, v3.7.8?????????????????????, ?????????????????????
                    return;
                }
                startRecord();
            }

            @Override
            public void onStopRecordClick() {
                if (!tempIsComplete) {
                    // ??????????????????????????????????????????????????????onComplete???????????????????????????????????????,
                    // ??????SDK?????????ANR??????, v3.7.8?????????????????????, ?????????????????????
                    return;
                }
                stopRecord();
            }

            @Override
            public void onDeleteClick() {
                if (isStopToCompleteDuration) {
                    // ????????????????????? SDK ???????????????onComplete,?????????????????????????????????????????????????????????
                    return;
                }
                mRecordTimeView.deleteLast();
                // clipManager.deletePart();
                recorder.deleteLastPart();
                isMaxDuration = false;
                if (mControlView != null) {
                    if (clipManager.getDuration() < clipManager.getMinDuration()) {
                        mControlView.setCompleteEnable(false);
                    }

                    mControlView.updateCutDownView(true);
                }

                if (clipManager.getDuration() == 0) {
                    //??????????????????
                    recorder.restartMv();
                    mControlView.setHasRecordPiece(false);
                    isAllowChangeMv = true;
                }
                mControlView.setRecordTime(TimeFormatterUtils.formatTime(clipManager.getDuration()));
            }

            @Override
            public void onFilterEffectClick() {
                // ????????????????????????
                showFilterEffectView();
            }

            @Override
            public void onChangeAspectRatioClick(int ratio) {
                //??????????????????
                setReSizeRatioMode(ratio);
            }

            @Override
            public void onAnimFilterClick() {

                showAnimFilterEffectView();
            }

            @Override
            public void onTakePhotoClick() {
                //??????
                recorder.takePhoto(true);
            }

            @Override
            public void onRaceDebug(boolean debug) {
                if (raceManager != null && mRenderingMode == RenderingMode.Race) {
                    raceManager.setmRaceDebug(debug);
                }
            }
        });
        mControlView.setRecordType(recorder.isMixRecorder());
        addSubView(mControlView);
        mControlView.setAspectRatio(mRatioMode);
    }

    /**
     * ????????????
     */
    String[] permission = {
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * ????????????
     */
    private void startRecord() {
        boolean checkResult = PermissionUtils.checkPermissionsGroup(getContext(), permission);
        if (!checkResult && mActivity != null) {
            PermissionUtils.requestPermissions(mActivity, permission,
                                               AlivcSvideoRecordActivity.PERMISSION_REQUEST_CODE);
            return;
        }

        if (CommonUtil.SDFreeSize() < 50 * 1000 * 1000) {
            FixedToastUtils.show(getContext(), getResources().getString(R.string.alivc_music_no_free_memory));
            return;
        }
        if (isMaxDuration) {
            mControlView.setRecordState(RecordState.STOP);
            return;
        }

        if (recorder != null && !mIsBackground) {
            // ????????????????????????, ?????????????????????,???????????????????????????, ??????????????????, ????????????false
            mControlView.setHasRecordPiece(true);
            mControlView.setRecordState(RecordState.RECORDING);
            mControlView.setRecording(true);
            String videoPath = Constants.SDCardConstants.getDir(getContext().getApplicationContext()) + File.separator + System.currentTimeMillis() + "-record.mp4";
            recorder.setOutputPath(videoPath);
            recorder.startRecording();

            Log.d(TAG, "startRecording    isStopToCompleteDuration:" + isStopToCompleteDuration);
        }

    }

    /**
     * ????????????????????????????????????stopRecord???onComplete????????????????????????????????????????????????????????????stopRecord
     * true: ????????????stop~onComplete, false??????
     */
    private boolean isStopToCompleteDuration;

    /**
     * ????????????
     */
    private void stopRecord() {
        Log.d(TAG, "stopRecord    isStopToCompleteDuration:" + isStopToCompleteDuration);
        if (recorder != null && !isStopToCompleteDuration && mControlView.isRecording()) {//
            isStopToCompleteDuration = true;

            //?????????????????????progressBar?????????????????????????????????????????????????????????stopRecording,
            //?????????finishRecording???????????????stopRecording????????????finishRecording??????
            //?????????????????????sdk?????????????????????????????????
            if ((progressBar == null || !progressBar.isShowing())) {
                recorder.stopRecording();

            }

        }

    }

    /**
     * ?????????????????????
     */
    private void cancelReadyRecord() {
        if (mCountDownView != null) {
            mCountDownView.cancle();
        }
    }

    /**
     * ???????????????????????????view
     */
    private void showReadyRecordView() {
        if (mCountDownView != null) {
            mCountDownView.start();
        }
    }

    /**
     * ???????????????????????????
     */
    private void showMusicSelView() {
        if (musicChooseView == null) {
            musicChooseView = new MusicChooser();

            musicChooseView.setRecordTime(getMaxRecordTime());

            musicChooseView.setMusicSelectListener(new MusicSelectListener() {

                @Override
                public void onMusicSelect(MusicFileBean musicFileBean, long startTime) {
                    // ?????????????????????
                }
            });

            musicChooseView.setDismissListener(new DialogVisibleListener() {
                @Override
                public void onDialogDismiss() {
                    mControlView.setMusicSelViewShow(false);
                    isMusicViewShowing = false;
                    restoreConflictEffect();
                }

                @Override
                public void onDialogShow() {
                    mControlView.setMusicSelViewShow(true);
                    recorder.applyMv(new EffectBean());
                    isMusicViewShowing = true;
                }
            });
        }
        musicChooseView.show(getFragmentManager(), TAG_MUSIC_CHOOSER);
    }

    /**
     * ??????????????????????????????
     */
    private String currPasterPath;

    /**
     * ??????????????????????????????
     */
    private void showGifEffectView() {
        if (gifEffectChooser == null) {
            gifEffectChooser = new GIfEffectChooser();
            gifEffectChooser.setDismissListener(this);
            gifEffectChooser.setPasterSelectListener(new PasterSelectListener() {
                @Override
                public void onPasterSelected(PreviewPasterForm imvForm) {
                    String path;
                    if (imvForm.getId() == 150) {
                        //id=150????????????????????????
                        path = imvForm.getPath();
                    } else {
                        path = DownloadFileUtils.getAssetPackageDir(getContext(),
                                imvForm.getName(), imvForm.getId()).getAbsolutePath();
                    }
                    currPasterPath = path;
                    addEffectToRecord(path);
                }

                @Override
                public void onSelectPasterDownloadFinish(String path) {
                    // ?????????paster???????????????, ?????????paster ???path
                    currPasterPath = path;
                }
            });

            gifEffectChooser.setDismissListener(new DialogVisibleListener() {
                @Override
                public void onDialogDismiss() {
                    mControlView.setEffectSelViewShow(false);
                }

                @Override
                public void onDialogShow() {
                    mControlView.setEffectSelViewShow(true);
                    if (!TextUtils.isEmpty(currPasterPath)) {
                        // dialog?????????,???????????????paster?????????, ?????????paster
                        addEffectToRecord(currPasterPath);
                    }
                }
            });
        }
        gifEffectChooser.show(getFragmentManager(), TAG_GIF_CHOOSER);
    }

    private void addEffectToRecord(String path) {

        if (effectPaster != null) {
            recorder.removePaster(effectPaster);
        }

        effectPaster = new EffectPaster(path);
        recorder.addPaster(effectPaster);

    }

    /**
     * ??????race??????????????????
     */
    private void setRaceEffectView() {
        String logo = getContext().getExternalFilesDir("") + "/AliyunDemo/tail/logo.png";
        if (effectImage == null) {
            effectImage = new EffectImage(logo);
            effectImage.x = 0.13f;
            effectImage.y = 0.1f;
        }
        switch (mRatioMode) {
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            effectImage.width = (float) 100.0 / ScreenUtils.getRealWidth(mActivity);
            effectImage.height = (float) 80.0 / ScreenUtils.getRealHeight(mActivity) / 3 * 4;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            effectImage.width = (float) 100.0 / ScreenUtils.getRealWidth(mActivity);
            effectImage.height = (float) 80.0 / ScreenUtils.getRealHeight(mActivity) / 9 * 16;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            effectImage.width = (float) 100.0 / ScreenUtils.getRealWidth(mActivity);
            effectImage.height = (float) 80.0 / ScreenUtils.getRealHeight(mActivity);
            break;
        default:
            effectImage.width = (float) 100.0 / ScreenUtils.getRealWidth(mActivity);
            effectImage.height = (float) 80.0 / ScreenUtils.getRealHeight(mActivity);
            break;
        }
        recorder.removeImage(effectImage);
        recorder.addImage(effectImage);
    }



    private FragmentManager getFragmentManager() {
        FragmentManager fm = null;
        if (mActivity != null) {
            fm = mActivity.getSupportFragmentManager();
        } else {
            Context mContext = getContext();
            if (mContext instanceof FragmentActivity) {
                fm = ((FragmentActivity) mContext).getSupportFragmentManager();
            }
        }
        return fm;
    }

    /**
     * ???????????????????????????
     */
    private void showFilterEffectView() {
        if (filterEffectChooser == null) {
            filterEffectChooser = new FilterEffectChooser();
        }
        if (filterEffectChooser.isAdded()) {
            return;
        }
        // ????????????listener
        filterEffectChooser.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onItemClick(EffectInfo effectInfo, int index) {
                if (effectInfo != null) {
                    filterSourcePath = effectInfo.getPath();
                    if (index == 0) {
                        filterSourcePath = null;
                    }
                    EffectFilter filterEffect = new EffectFilter(filterSourcePath);
                    recorder.applyFilter(filterEffect);
                    mConflictEffects.put(TYPE_FILTER, filterEffect);
                }
                currentFilterPosition = index;
            }
        });
        filterEffectChooser.setFilterPosition(currentFilterPosition);
        filterEffectChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                mControlView.setEffectSelViewShow(false);
            }

            @Override
            public void onDialogShow() {
                mControlView.setEffectSelViewShow(true);
            }
        });
        filterEffectChooser.show(getFragmentManager(), TAG_FILTER_CHOOSER);
    }

    /**
     * ?????????????????????????????????
     */
    private void showAnimFilterEffectView() {
        if (mAnimFilterEffectChooser == null) {
            mAnimFilterEffectChooser = new AnimFilterEffectChooser();
        }
        if (mAnimFilterEffectChooser.isAdded()) {
            return;
        }
        // ????????????listener
        mAnimFilterEffectChooser.setOnAnimFilterItemClickListener(new OnAnimFilterItemClickListener() {
            @Override
            public void onItemClick(EffectFilter effectInfo, int index) {

                String path = effectInfo.getPath();

                if (path == null || index == 0) {
                    recorder.removeAnimationFilter(mCurrentAnimFilterEffect);
                } else {
                    mCurrentAnimFilterEffect = effectInfo;
                    recorder.applyAnimationFilter(mCurrentAnimFilterEffect);
                }

                mCurrentAnimFilterPosition = index;
            }

        });
        mAnimFilterEffectChooser.setFilterPosition(mCurrentAnimFilterPosition);
        mAnimFilterEffectChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                mControlView.setEffectSelViewShow(false);
            }

            @Override
            public void onDialogShow() {
                mControlView.setEffectSelViewShow(true);
            }
        });
        mAnimFilterEffectChooser.show(getFragmentManager(), TAG_ANIM_FILTER_CHOOSER);
    }

    /**
     * ???????????????????????????
     */
    private void showBeautyFaceView() {
        if (beautyEffectChooser == null) {
            beautyEffectChooser = new BeautyEffectChooser();
        }
        currentBeautySkinPosition = SharedPreferenceUtils.getBeautySkinLevel(getContext());



        // ??????item??????listener
        beautyEffectChooser.setOnBeautyFaceItemSeletedListener(new OnBeautyFaceItemSeletedListener() {
            @Override
            public void onNormalSelected(int postion, BeautyLevel beautyLevel) {
                defaultBeautyLevel = beautyLevel;
                currentBeautyFaceNormalPosition = postion;
                // ????????????
                recorder.setBeautyLevel(beautyLevel.getValue());

                if (beautyService != null) {
                    beautyService.saveSelectParam(getContext(), currentBeautyFaceNormalPosition, currentBeautyFacePosition, currentBeautySkinPosition, currentBeautyShapePosition);
                }
            }

            @Override
            public void onAdvancedSelected(int position, BeautyLevel beautyLevel) {
                currentBeautyFacePosition = position;
                // ????????????
                BeautyParams beautyParams;
                if (mRenderingMode == RenderingMode.FaceUnity) {
                    beautyParams = rememberParamList.get(position);
                } else {
                    beautyParams = rememberRaceParamList.get(position);
                }
                //// ???????????????faceUnity???????????? 0~1.0f
                if (beautyService != null) {
                    beautyService.setBeautyParam(beautyParams, BeautyService.BEAUTY_FACE);
                    beautyService.saveSelectParam(getContext(), currentBeautyFaceNormalPosition, currentBeautyFacePosition, currentBeautySkinPosition, currentBeautyShapePosition);
                }
            }
        });

        // ??????item??????
        beautyEffectChooser.setOnBeautySkinSelectedListener(new OnBeautySkinItemSeletedListener() {
            @Override
            public void onItemSelected(int postion) {
                currentBeautySkinPosition = postion;
                BeautyParams beautyParams;
                if (mRenderingMode == RenderingMode.FaceUnity) {
                    beautyParams = rememberParamList.get(postion);
                } else {
                    beautyParams = rememberRaceParamList.get(postion);
                }
                if (beautyService != null) {
                    beautyService.setBeautyParam(beautyParams, BeautyService.BEAUTY_SKIN);
                    beautyService.saveSelectParam(getContext(), currentBeautyFaceNormalPosition, currentBeautyFacePosition, currentBeautySkinPosition, currentBeautyShapePosition);
                }
            }
        });

        // ??????item??????
        beautyEffectChooser.setOnBeautyShapeItemSeletedListener(new OnBeautyShapeItemSeletedListener() {
            @Override
            public void onItemSelected(int postion) {
                currentBeautyShapePosition = postion;
                if (beautyService != null) {
                    beautyService.saveSelectParam(getContext(), currentBeautyFaceNormalPosition, currentBeautyFacePosition, currentBeautySkinPosition, currentBeautyShapePosition);
                }
                if (raceManager != null && mRenderingMode == RenderingMode.Race) {
                    beautyShapeParams = getBeautyShapeParams(currentBeautyShapePosition);
                    if (beautyShapeParams == null) {
                        beautyShapeParams = rememberShapeParamList.get(currentBeautyShapePosition);
                    }
                    raceManager.setShapeParam(beautyShapeParams);
//                    raceManager.setCurrentShapeType(currentBeautyShapePosition);

                }
            }
        });
        // tab????????????  ????????????????????????????????????
        beautyEffectChooser.setOnTableSeletedListener(new OnBeautyTableItemSeletedListener() {
            @Override
            public void onNormalSelected(BeautyMode beautyMode) {
                if (raceManager != null && mRenderingMode == RenderingMode.Race) {
                    raceManager.setCurrentBeautyMode(beautyMode);
                }
            }
        });

        // ????????????dialog
        beautyEffectChooser.setOnBeautyFaceDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                beautyEffectChooser.dismiss();
                mControlView.setEffectSelViewShow(true);
                showBeautyFaceDetailDialog();

            }
        });

        // ????????????dialog
        beautyEffectChooser.setOnBeautySkinDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                beautyEffectChooser.dismiss();
                mControlView.setEffectSelViewShow(true);
                showBeautySkinDetailDialog();
            }
        });
        // ????????????dialog
        beautyEffectChooser.setOnBeautyShapeDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                beautyEffectChooser.dismiss();
                mControlView.setEffectSelViewShow(true);
                showBeautyShapeDetailDialog();
            }
        });

        // ?????????????????????????????????
        beautyEffectChooser.setOnBeautyModeChangeListener(new OnBeautyModeChangeListener() {
            @Override
            public void onModeChange(RadioGroup group, int checkedId) {

                BeautyParams beautyParams = null;
                if (checkedId == R.id.rb_level_advanced) {
                    currentBeautyFaceMode = BeautyMode.Advanced;
                    recorder.setBeautyStatus(false);
                    if (mRenderingMode == RenderingMode.FaceUnity) {
                        beautyParams = rememberParamList.get(currentBeautyFacePosition);
                    } else {
                        beautyParams = rememberRaceParamList.get(currentBeautyFacePosition);
                    }
                    if (beautyService != null) {
                        beautyService.setBeautyParam(beautyParams, BeautyService.BEAUTY_FACE);
                    }
                } else if (checkedId == R.id.rb_level_normal) {
                    currentBeautyFaceMode = BeautyMode.Normal;
                    recorder.setBeautyStatus(true);
                    recorder.setBeautyLevel(defaultBeautyLevel.getValue());
                }

                if (beautyService != null) {
                    beautyService.saveBeautyMode(getContext(), currentBeautyFaceMode);
                }
            }
        });

        beautyEffectChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                // ???????????????????????????????????????,
                if (!isbeautyDetailShowing) {
                    mControlView.setEffectSelViewShow(false);
                } else {
                    mControlView.setEffectSelViewShow(true);
                }
                isbeautyDetailBack = false;

                if (beautyService != null) {
                    beautyService.saveBeautyMode(getContext(), currentBeautyFaceMode);
                    beautyService.saveSelectParam(getContext(), currentBeautyFaceNormalPosition, currentBeautyFacePosition, currentBeautySkinPosition, currentBeautyShapePosition);
                }
            }

            @Override
            public void onDialogShow() {
                mControlView.setEffectSelViewShow(true);
                beautyEffectChooser.setBeautyParams(AliyunSVideoRecordView.this.beautyParams);

            }
        });


        beautyEffectChooser.show(getFragmentManager(), TAG_BEAUTY_CHOOSER);
    }

    /**
     * ??????????????????dialog
     */
    private void showBeautyFaceDetailDialog() {
        beautyParams = getBeautyParams(currentBeautyFacePosition);
        if (beautyParams == null) {
            if (mRenderingMode == RenderingMode.FaceUnity) {
                beautyParams = rememberParamList.get(currentBeautyFacePosition);
            } else {
                beautyParams = rememberRaceParamList.get(currentBeautyFacePosition);
            }
        }
        beautyFaceDetailChooser = new BeautyFaceDetailChooser();
        beautyFaceDetailChooser.setBeautyLevel(currentBeautyFacePosition);
        beautyFaceDetailChooser.setOnBeautyParamsChangeListener(
        new OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyChange(BeautyParams param) {
                if (beautyParams != null && param != null) {
                    beautyParams.beautyWhite = param.beautyWhite;
                    beautyParams.beautyBuffing = param.beautyBuffing;
                    beautyParams.beautyRuddy = param.beautyRuddy;
                    if (faceUnityManager != null && mRenderingMode == RenderingMode.FaceUnity) {
                        // ??????
                        faceUnityManager.setFaceBeautyWhite(param.beautyWhite / 100);
                        // ??????
                        faceUnityManager.setFaceBeautyRuddy(param.beautyRuddy / 100);
                        // ??????
                        //??????0-100????????????0-6???????????????0???06,
                        faceUnityManager.setFaceBeautyBuffing((float) (param.beautyBuffing * 0.06));
                    } else if (raceManager != null && mRenderingMode == RenderingMode.Race) {
                        // ??????
                        raceManager.setFaceBeautyWhite(param.beautyWhite / 100);
                        // ??????
                        raceManager.setFaceBeautySharpLevel(param.beautyRuddy / 100);
                        // ??????
                        raceManager.setFaceBeautyBuffing(param.beautyBuffing / 100);
                    }
                }
            }
        });
        // ??????back??????
        beautyFaceDetailChooser.setOnBackClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                isbeautyDetailShowing = false;
                isbeautyDetailBack = true;
                beautyFaceDetailChooser.dismiss();
                beautyEffectChooser.show(getFragmentManager(), TAG_BEAUTY_CHOOSER);
            }
        });
        // ??????????????????
        beautyFaceDetailChooser.setOnBlankClickListener(new BeautyDetailSettingView.OnBlanckViewClickListener() {
            @Override
            public void onBlankClick() {
                isbeautyDetailShowing = false;
            }
        });
        beautyFaceDetailChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                // ?????????????????????????????????back??????, controlview?????????view????????????????????????
                if (isbeautyDetailBack) {
                    mControlView.setEffectSelViewShow(true);
                } else {
                    mControlView.setEffectSelViewShow(false);
                }
                saveBeautyParams(currentBeautyFacePosition, beautyParams);
                isbeautyDetailBack = false;
            }

            @Override
            public void onDialogShow() {
            }
        });
        beautyFaceDetailChooser.setBeautyParams(beautyParams);
        beautyEffectChooser.dismiss();
        isbeautyDetailShowing = true;
        beautyFaceDetailChooser.show(getFragmentManager(), TAG_BEAUTY_DETAIL_FACE_CHOOSER);
    }


    /**
     * ??????????????????dialog
     */
    private void showBeautySkinDetailDialog() {
        beautyParams = getBeautyParams(currentBeautySkinPosition);
        if (beautyParams == null) {
            if (mRenderingMode == RenderingMode.FaceUnity) {
                beautyParams = rememberParamList.get(currentBeautyFacePosition);
            } else {
                beautyParams = rememberRaceParamList.get(currentBeautyFacePosition);
            }
        }

        beautySkinDetailChooser = new BeautySkinDetailChooser();
        beautySkinDetailChooser.setBeautyLevel(currentBeautySkinPosition);
        beautySkinDetailChooser.setOnBeautyParamsChangeListener(
        new OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyChange(BeautyParams param) {
                if (beautyParams != null && param != null) {

                    beautyParams.beautyBigEye = param.beautyBigEye;
                    beautyParams.beautySlimFace = param.beautySlimFace;
                    if (faceUnityManager != null && mRenderingMode == RenderingMode.FaceUnity) {
                        //??????
                        faceUnityManager.setFaceBeautyBigEye(param.beautyBigEye / 100);
                        //??????
                        faceUnityManager.setFaceBeautySlimFace(param.beautySlimFace / 100 * 1.5f);
                    } else if (raceManager != null && mRenderingMode == RenderingMode.Race) {
                        //??????
                        raceManager.setFaceBeautyBigEye(param.beautyBigEye / 50.F);
                        //??????
                        raceManager.setFaceBeautySlimFace(param.beautySlimFace / 50.F);
                    }
                    saveBeautyParams(currentBeautySkinPosition, beautyParams);

                }
            }
        });

        // ??????back??????
        beautySkinDetailChooser.setOnBackClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                isbeautyDetailShowing = false;
                beautySkinDetailChooser.dismiss();
                isbeautyDetailBack = true;
                beautyEffectChooser.show(getFragmentManager(), TAG_BEAUTY_CHOOSER);
            }
        });

        // ??????????????????
        beautySkinDetailChooser.setOnBlankClickListener(new BeautyDetailSettingView.OnBlanckViewClickListener() {
            @Override
            public void onBlankClick() {
                isbeautyDetailShowing = false;
            }
        });

        beautySkinDetailChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                // ?????????????????????????????????back??????, controlview?????????view????????????????????????
                if (isbeautyDetailBack) {
                    mControlView.setEffectSelViewShow(true);
                } else {
                    mControlView.setEffectSelViewShow(false);
                }
                saveBeautyParams(currentBeautySkinPosition, AliyunSVideoRecordView.this.beautyParams);
                isbeautyDetailBack = false;
            }

            @Override
            public void onDialogShow() {

            }
        });
        beautySkinDetailChooser.setBeautyParams(beautyParams);
        beautyEffectChooser.dismiss();
        isbeautyDetailShowing = true;
        beautySkinDetailChooser.show(getFragmentManager(), TAG_BEAUTY_DETAIL_SKIN_CHOOSER);
    }


    /**
     * ??????????????????dialog
     */
    private void showBeautyShapeDetailDialog() {
        beautyShapeParams = getBeautyShapeParams(currentBeautyShapePosition);
        if (beautyShapeParams == null) {
            beautyShapeParams = rememberShapeParamList.get(currentBeautyShapePosition);
        }

        beautyShapeDetailChooser = new BeautyShapeDetailChooser();
        beautyShapeDetailChooser.setBeautyLevel(currentBeautyShapePosition);
        beautyShapeDetailChooser.setOnBeautyParamsChangeListener(new OnBeautyShapeParamsChangeListener() {
            @Override
            public void onBeautyChange(BeautyShapeParams param) {
                if (beautyShapeParams != null && param != null) {

                    beautyShapeParams.beautyCutFace = param.beautyCutFace;
                    beautyShapeParams.beautyThinFace = param.beautyThinFace;
                    beautyShapeParams.beautyLongFace = param.beautyLongFace;
                    beautyShapeParams.beautyLowerJaw = param.beautyLowerJaw;
                    beautyShapeParams.beautyBigEye = param.beautyBigEye;
                    beautyShapeParams.beautyThinNose = param.beautyThinNose;
                    beautyShapeParams.beautyMouthWidth = param.beautyMouthWidth;
                    beautyShapeParams.beautyThinMandible = param.beautyThinMandible;
                    beautyShapeParams.beautyCutCheek = param.beautyCutCheek;
                    if (raceManager != null && mRenderingMode == RenderingMode.Race) {
                        raceManager.setShapeParam(beautyShapeParams);
                    }
                    saveBeautyShapeParams(currentBeautyShapePosition, beautyShapeParams);

                }
            }
        });

        // ??????back??????
        beautyShapeDetailChooser.setOnBackClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                isbeautyDetailShowing = false;
                beautyShapeDetailChooser.dismiss();
                isbeautyDetailBack = true;
                beautyEffectChooser.show(getFragmentManager(), TAG_BEAUTY_CHOOSER);
            }
        });

        // ??????????????????
        beautyShapeDetailChooser.setOnBlankClickListener(new BeautyDetailSettingView.OnBlanckViewClickListener() {
            @Override
            public void onBlankClick() {
                isbeautyDetailShowing = false;
            }
        });

        beautyShapeDetailChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                // ?????????????????????????????????back??????, controlview?????????view????????????????????????
                if (isbeautyDetailBack) {
                    mControlView.setEffectSelViewShow(true);
                } else {
                    mControlView.setEffectSelViewShow(false);
                }
                saveBeautyShapeParams(currentBeautyShapePosition, AliyunSVideoRecordView.this.beautyShapeParams);
                isbeautyDetailBack = false;
            }

            @Override
            public void onDialogShow() {

            }
        });
        beautyShapeDetailChooser.setBeautyShapeParams(beautyShapeParams);
        beautyEffectChooser.dismiss();
        isbeautyDetailShowing = true;
        beautyShapeDetailChooser.show(getFragmentManager(), TAG_BEAUTY_DETAIL_SHAPE_CHOOSER);
    }

    /**
     * ??????????????????
     */
    public void setRecorder(AlivcIMixRecorderInterface recorder) {
        this.recorder = recorder;

        initRecorder();
        initVideoView();

    }

    private void initRecorder() {
        recorder.setGop(mGop);
        recorder.setVideoQuality(mVideoQuality);
        recorder.setRatioMode(mRatioMode);
        recorder.useFlip(mIsUseFlip);
        recorder.setResolutionMode(mResolutionMode);
        clipManager = recorder.getClipManager();
        clipManager.setMaxDuration(getMaxRecordTime());
        clipManager.setMinDuration(minRecordTime);
        recorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);
        //mediaInfo.setHWAutoSize(true);//???????????????????????????16?????????
        cameraType = recorder.getCameraCount() == 1 ? com.aliyun.svideo.sdk.external.struct.recorder.CameraType.BACK : cameraType;
        recorder.setCamera(cameraType);
        recorder.setBeautyStatus(false);

        initOrientationDetector();


        //???????????????surFaceView??????
        if (recorder.isMixRecorder()) {
            initPlayerSurfaceView();
            initRecorderSurfaceView();
            //????????????????????????
            recorder.setMixRecorderRatio(mRecorderSurfaceView);
            //???????????????????????????
            recorder.setMixPlayerRatio(mPlayerSurfaceView);
        } else {
            initRecorderSurfaceView();
        }

        //??????????????????????????????
        setMediaInfo();

        recorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info) {
                //?????????????????? NV21,???????????????????????????????????????faceUnity??????????????????
                frameBytes = bytes;
                frameWidth = width;
                frameHeight = height;
            }

            @Override
            public Camera.Size onChoosePreviewSize(List<Camera.Size> supportedPreviewSizes,
                                                   Camera.Size preferredPreviewSizeForVideo) {
                return null;
            }

            @Override
            public void openFailed() {
                Log.e(AliyunTag.TAG, "openFailed----------");
                isOpenFailed = true;
            }
        });

        recorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(final boolean validClip, final long clipDuration) {
                Log.i(TAG, "onComplete   duration : " + clipDuration +
                      ", clipManager.getDuration() = " + clipManager.getDuration());
                tempIsComplete = true;
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onComplete    isStopToCompleteDuration:" + isStopToCompleteDuration);

                        if (recorder.isMixRecorder() && !isMaxDuration) {
                            ToastUtils.show(mActivity, getResources().getString(R.string.alivc_mix_record_continue), Gravity.CENTER, Toast.LENGTH_SHORT);
                        }

                        isStopToCompleteDuration = false;
                        handleStopCallback(validClip, clipDuration);
                        if (isMaxDuration && validClip) {
                            finishRecording();
                        }

                    }
                });

            }

            /**
             * ?????????????????????
             * @param outputPath
             */
            @Override
            public void onFinish(final String outputPath) {
                Log.i(TAG, "onFinish:" + outputPath);

                if (progressBar != null && progressBar.isShowing()) {
                    progressBar.dismiss();
                }

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCompleteListener != null) {
                            final int duration = clipManager.getDuration();
                            //deleteAllPart();
                            // ???????????????, ????????????????????????????????????
                            // ????????????????????????, sdk???????????????, ???????????????????????????, ?????????????????????????????????
                            if (activityStoped) {
                                pendingCompseFinishRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isStopToCompleteDuration) {
                                            mCompleteListener.onComplete(outputPath, duration, mRatioMode);
                                        }
                                    }
                                };
                            } else {
                                if (!isStopToCompleteDuration) {
                                    mCompleteListener.onComplete(outputPath, duration, mRatioMode);
                                }
                            }
                        }
                        VideoInfoUtils.printVideoInfo(outputPath);
                    }
                });

            }

            @Override
            public void onProgress(final long duration) {
                final int currentDuration = clipManager.getDuration();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isAllowChangeMv = false;
                        recordTime = 0;
                        //??????????????????
                        if (mRecordTimeView != null) {
                            mRecordTimeView.setDuration((int) duration);
                        }

                        recordTime = (int) (currentDuration + duration);

                        //Log.d(TAG, "onProgress: " + recordTime + "??????????????????" + currentDuration + "????????????" + duration);

                        if (recordTime <= clipManager.getMaxDuration() && recordTime >= clipManager.getMinDuration()) {
                            // 2018/7/11 ???????????????????????????
                            mControlView.setCompleteEnable(true);
                        } else {
                            mControlView.setCompleteEnable(false);
                        }
                        if (mControlView != null && mControlView.getRecordState().equals(RecordState.STOP)) {
                            return;
                        }
                        if (mControlView != null) {
                            mControlView.setRecordTime(TimeFormatterUtils.formatTime(recordTime));
                        }

                    }
                });

            }

            @Override
            public void onMaxDuration() {
                Log.i(TAG, "onMaxDuration:");
                isMaxDuration = true;
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mControlView != null) {
                            mControlView.setCompleteEnable(false);
                            mControlView.setRecordState(RecordState.STOP);
                            mControlView.updateCutDownView(false);
                        }
                    }
                });

            }

            @Override
            public void onError(int errorCode) {
                Log.e(TAG, "onError:" + errorCode);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempIsComplete = true;
                        if (progressBar != null && progressBar.isShowing()) {
                            progressBar.dismiss();
                            mControlView.setCompleteEnable(true);
                        } else {
                            handleStopCallback(false, 0);
                        }
                    }
                });
            }

            @Override
            public void onInitReady() {
                Log.i(TAG, "onInitReady");
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        restoreConflictEffect();
                        if (effectPaster != null) {
                            addEffectToRecord(effectPaster.getPath());
                        }
                    }
                });
            }

            @Override
            public void onDrawReady() {

            }

            @Override
            public void onPictureBack(final Bitmap bitmap) {

                ThreadUtils.runOnSubThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        final String imgPath = Constants.SDCardConstants.getDir(getContext().getApplicationContext()) + File.separator + System.currentTimeMillis() + "-photo.jpg";
                        try {
                            BitmapUtil.generateFileFromBitmap(bitmap, imgPath, "jpg");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                //??????android Q
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UriUtils.saveImgToMediaStore(getContext().getApplicationContext(), imgPath);
                                    }
                                });
                            } else {
                                MediaScannerConnection.scanFile(getContext().getApplicationContext(),
                                                                new String[] {imgPath}, new String[] {"image/jpeg"}, null);
                            }

                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show(getContext(), "????????????????????????");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onPictureDataBack(final byte[] data) {

            }

        });
        recorder.setOnTextureIdCallback(new OnTextureIdCallBack() {
            @Override
            public int onTextureIdBack(int textureId, int textureWidth, int textureHeight, float[] matrix) {
                //******************************** start ******************************************
                //?????????faceunity??????????????????????????????????????????faceunity???????????????????????????????????????????????????????????????license??????????????????????????????????????????
                //????????????????????????????????????faceUnity?????? ????????????????????????app gradle ????????????
                if (faceUnityManager != null && faceUnityManager.isInit() && faceInitResult && currentBeautyFaceMode == BeautyMode.Advanced && mRenderingMode == RenderingMode.FaceUnity) {
                    /**
                     * faceInitResult fix bug:???????????????????????????????????????,???????????????release?????????????????????????????????,???????????????release????????????????????????????????????
                     */
                    return faceUnityManager.draw(frameBytes, mFuImgNV21Bytes, textureId, frameWidth, frameHeight, mFrameId++, mControlView.getCameraType().getType());
                } else if (currentBeautyFaceMode == BeautyMode.Advanced && mRenderingMode == RenderingMode.Race && raceManager != null) {
                    isRaceDrawed = true;
                    return raceManager.draw(frameBytes, mFuImgNV21Bytes, textureId, frameWidth, frameHeight, mFrameId++, mControlView.getCameraType().getType(), rotation);
                }
                //******************************** end ********************************************
                return textureId;
            }

            @Override
            public int onScaledIdBack(int scaledId, int textureWidth, int textureHeight, float[] matrix) {

                return scaledId;
            }

            @Override
            public void onTextureDestroyed() {
                // sdk3.7.8??????, ??????????????????????????????????????????gl???????????????GLSurfaceView???????????????GLSurfaceView.queueEvent?????????
                // ?????????????????????gl??????????????????????????????????????????????????????
                if (mRenderingMode == RenderingMode.FaceUnity) {
                    if (faceUnityManager != null && faceInitResult) {
                        faceUnityManager.release();
                        faceInitResult = false;
                    }
                } else if (mRenderingMode == RenderingMode.Race) {
                    if (raceManager != null && isRaceDrawed) {
                        raceManager.release();
                    }
                }
            }
        });

        recorder.setFaceTrackInternalMaxFaceCount(2);
    }

    /**
     * ????????????????????????, 3??? ??????: 0.6 ??????: 0.6 ??????: 6 ??????: 0.6 ??????: 0.6 * 1.5 (?????????0~1.5)
     * ??????????????????????????????????????????
     */
    private void faceunityDefaultParam() {
        beautyService = new BeautyService();
        if (BeautyMode.Advanced == currentBeautyFaceMode) {
            recorder.setBeautyStatus(false);
            beautyService.bindFaceUnity(getContext(), faceUnityManager);
            initRememberParams();
        } else if (BeautyMode.Normal == currentBeautyFaceMode) {
            //??????????????????
            int beautyNormalFaceLevel = SharedPreferenceUtils.getBeautyNormalFaceLevel(getContext());
            recorder.setBeautyStatus(true);
            beautyService.bindNormalFaceUnity(faceUnityManager);
            recorder.setBeautyLevel(getNormalBeautyLevel(beautyNormalFaceLevel));
        } else {
            beautyService.bindFaceUnity(getContext(), faceUnityManager);
            initRememberParams();
        }

    }
    /**
     * race
     * ????????????????????????, 3??? ??????: 0.6 ??????: 0.6 ??????: 6 ??????: 0.6 ??????: 0.6 * 1.5 (?????????0~1.5)
     * ??????????????????????????????????????????
     */
    private void raceDefaultParam() {
        beautyService = new BeautyService();
        if (BeautyMode.Advanced == currentBeautyFaceMode) {
            recorder.setBeautyStatus(false);
            beautyService.bindRace(getContext(), raceManager);
            initRememberParams();
        } else if (BeautyMode.Normal == currentBeautyFaceMode) {
            //??????????????????
            int beautyNormalFaceLevel = SharedPreferenceUtils.getBeautyNormalFaceLevel(getContext());
            recorder.setBeautyStatus(true);
            beautyService.bindNormalRace(raceManager);
            recorder.setBeautyLevel(getNormalBeautyLevel(beautyNormalFaceLevel));
        } else {
            beautyService.bindRace(getContext(), raceManager);
            initRememberParams();
        }
    }

    private void initRememberParams() {
        //??????????????????
        int beautyFaceLevel = SharedPreferenceUtils.getBeautyFaceLevel(mActivity);
        //????????????
        int beautySkinLevel = SharedPreferenceUtils.getBeautySkinLevel(mActivity);
        BeautyParams beautyFaceParams;
        if (mRenderingMode == RenderingMode.FaceUnity) {
            beautyFaceParams = rememberParamList.get(beautyFaceLevel);
        } else {
            beautyFaceParams = rememberRaceParamList.get(beautyFaceLevel);
        }
        beautyService.setBeautyParam(beautyFaceParams, BeautyService.BEAUTY_FACE);
        BeautyParams beautyShinParams = rememberParamList.get(beautySkinLevel);
        beautyService.setBeautyParam(beautyShinParams, BeautyService.BEAUTY_SKIN);
        if (raceManager != null && !rememberShapeParamList.isEmpty()) {
            raceManager.setShapeParam(rememberShapeParamList.get(currentBeautyShapePosition));
        }
    }

    /**
     * ???????????????????????????????????????????????????0-5???-???0-100???
     */
    private int getNormalBeautyLevel(int beautyNormalFaceLevel) {

        switch (beautyNormalFaceLevel) {
        case 0:
            return BeautyLevel.BEAUTY_LEVEL_ZERO.getValue();
        case 1:
            return BeautyLevel.BEAUTY_LEVEL_ONE.getValue();
        case 2:
            return BeautyLevel.BEAUTY_LEVEL_TWO.getValue();
        case 3:
            return BeautyLevel.BEAUTY_LEVEL_THREE.getValue();
        case 4:
            return BeautyLevel.BEAUTY_LEVEL_FOUR.getValue();
        case 5:
            return BeautyLevel.BEAUTY_LEVEL_FIVE.getValue();
        default:
            return BeautyLevel.BEAUTY_LEVEL_THREE.getValue();

        }

    }

    public void setFaceTrackModePath() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                String path = getContext().getExternalFilesDir("") + File.separator + RecordCommon.QU_NAME + File.separator;
                if (recorder != null) {
                    recorder.needFaceTrackInternal(true);
                    recorder.setFaceTrackInternalModelPath(path + "/model");
//                    ???????????????????????????????????????????????????????????????????????????
                    if (isSvideoRace) {
                        setRaceEffectView();
                    }
                }
            }
        });

    }

    public void setRecordMute(boolean recordMute) {
        if (recorder != null) {
            recorder.setMute(recordMute);
        }
    }

    private void initOrientationDetector() {
        orientationDetector = new OrientationDetector(getContext().getApplicationContext());
        orientationDetector.setOrientationChangedListener(new OrientationDetector.OrientationChangedListener() {
            @Override
            public void onOrientationChanged() {
                rotation = getCameraRotation();
                recorder.setRotation(rotation);
            }
        });
    }

    private int getCameraRotation() {
        int orientation = orientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraType.getType(), cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (rotation != 0) {
                rotation = 360 - rotation;
            }
        }
        return rotation;
    }


    private boolean activityStoped;
    private Runnable pendingCompseFinishRunnable;

    /**
     * ????????????
     */
    public void startPreview() {

        if (recorder == null) {
            return;
        }

        activityStoped = false;
        if (pendingCompseFinishRunnable != null) {
            pendingCompseFinishRunnable.run();
        }
        pendingCompseFinishRunnable = null;

        recorder.startPreview();
        if (isAllowChangeMv) {
            restoreConflictEffect();
        }
        //            recorder.setZoom(scaleFactor);
        if (clipManager.getDuration() >= clipManager.getMinDuration() || isMaxDuration) {
            // 2018/7/11 ???????????????????????????
            mControlView.setCompleteEnable(true);
        } else {
            mControlView.setCompleteEnable(false);
        }
        if (orientationDetector != null && orientationDetector.canDetectOrientation()) {
            orientationDetector.enable();
        }

        mCountDownView.setOnCountDownFinishListener(new AlivcCountDownView.OnCountDownFinishListener() {
            @Override
            public void onFinish() {
                FixedToastUtils.show(getContext(), getResources().getString(R.string.alivc_recorder_record_start_recorder));
                startRecord();
            }
        });

    }

    /**
     * ????????????
     */
    public void stopPreview() {
        if (recorder == null) {
            return;
        }
        activityStoped = true;
        if (mControlView != null && mCountDownView != null && mControlView.getRecordState().equals(RecordState.READY)) {
            mCountDownView.cancle();
            mControlView.setRecordState(RecordState.STOP);
            mControlView.setRecording(false);
        }

        if (mControlView != null && mControlView.getRecordState().equals(RecordState.RECORDING)) {
            recorder.stopRecording();
        }
        recorder.stopPreview();

        if (beautyEffectChooser != null) {
            beautyEffectChooser.dismiss();
        }

        if (mFaceUnityTask != null) {
            mFaceUnityTask.cancel(true);
            mFaceUnityTask = null;
        }
        if (orientationDetector != null) {
            orientationDetector.disable();
        }

        if (mControlView != null && mControlView.getFlashType() == FlashType.ON
                && mControlView.getCameraType() == CameraType.BACK) {
            mControlView.setFlashType(FlashType.OFF);
        }
    }

    /**
     * ??????????????????activity??????fragment???????????????????????????
     */
    public void destroyRecorder() {

        //destroy????????????????????????????????????
        deleteSliceFile();
        if (finishRecodingTask != null) {
            finishRecodingTask.cancel(true);
            finishRecodingTask = null;
        }

        if (faceTrackPathTask != null) {
            faceTrackPathTask.cancel(true);
            faceTrackPathTask = null;
        }

        if (recorder != null) {
            recorder.release();
            recorder = null;
            Log.i(TAG, "recorder destroy");
        }

        if (orientationDetector != null) {
            orientationDetector.setOrientationChangedListener(null);
        }

    }

    /**
     * ??????????????????????????????
     */
    private void deleteSliceFile() {
        if (recorder != null) {
            recorder.getClipManager().deleteAllPart();
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????? ??????editorActivity???????????????????????????????????????{@link AlivcSvideoRecordActivity#()}
     */
    private void finishRecording() {
        //????????????
        if (progressBar == null) {
            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage(getResources().getString(R.string.alivc_recorder_record_create_video));
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setCancelable(false);
            progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        }
        progressBar.show();
        mControlView.setCompleteEnable(false);
        finishRecodingTask = new FinishRecodingTask(this).executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setMediaInfo() {
        // mMixInputInfo??????????????????????????????????????????????????????????????????
        AliyunMixRecorderDisplayParam recorderDisplayParam = new AliyunMixRecorderDisplayParam.Builder()
        .displayMode(VideoDisplayMode.SCALE)
        .layoutParam(
            new AliyunMixTrackLayoutParam.Builder()
            .centerX(0.25f)
            .centerY(0.5f)
            .widthRatio(0.5f)
            .heightRatio(1.0f)
            .build()
        )
        .build();
        AliyunMixRecorderDisplayParam sampleDisplayParam = new AliyunMixRecorderDisplayParam
        .Builder()
        .displayMode(VideoDisplayMode.FILL)
        .layoutParam(new AliyunMixTrackLayoutParam.Builder()
                     .centerX(0.75f)
                     .centerY(0.5f)
                     .widthRatio(0.5f)
                     .heightRatio(1.0f)
                     .build())
        .build();
        mMixInputInfo = new AliyunMixMediaInfoParam
        .Builder()
        .streamStartTimeMills(0L)
        .streamEndTimeMills(0L)
        .mixVideoFilePath(mMixVideoPath)
        .mixDisplayParam(sampleDisplayParam)
        .recordDisplayParam(recorderDisplayParam)
        .build();


        mOutputInfo = new MediaInfo();
        mOutputInfo.setFps(35);
        mOutputInfo.setVideoWidth(recorder.getVideoWidth());
        mOutputInfo.setVideoHeight(recorder.getVideoHeight());
        mOutputInfo.setVideoCodec(mVideoCodec);
        recorder.setMediaInfo(mMixInputInfo, mOutputInfo);
    }

    /**
     * ???????????????AsyncTask
     */
    public static class FinishRecodingTask extends AsyncTask<Void, Integer, Integer> {
        WeakReference<AliyunSVideoRecordView> weakReference;

        FinishRecodingTask(AliyunSVideoRecordView recordView) {
            weakReference = new WeakReference<>(recordView);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (weakReference == null) {
                return -1;
            }

            AliyunSVideoRecordView recordView = weakReference.get();
            if (recordView != null) {
                Log.i(TAG, "finishRecording");
                return recordView.recorder.finishRecording();

            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer code) {
            if (weakReference == null) {
                return;
            }
            if (code != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
                Log.e(TAG, "???????????? ????????? : " + code);
                AliyunSVideoRecordView recordView = weakReference.get();
                if (recordView != null) {
                    ToastUtils.show(recordView.getContext(), R.string.alivc_record_mix_compose_failure);
                    if (recordView.progressBar != null && recordView.progressBar.isShowing()) {
                        recordView.progressBar.dismiss();
                        recordView.mControlView.setCompleteEnable(true);
                    }
                }
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param isValid
     * @param duration
     */
    private void handleStopCallback(final boolean isValid, final long duration) {


        mControlView.setRecordState(RecordState.STOP);
        if (mControlView != null) {
            mControlView.setRecording(false);
        }

        if (!isValid) {
            if (mRecordTimeView != null) {
                mRecordTimeView.setDuration(0);

                if (mRecordTimeView.getTimelineDuration() == 0) {
                    mControlView.setHasRecordPiece(false);
                }
            }
            return;
        }

        if (mRecordTimeView != null) {
            mRecordTimeView.setDuration((int) duration);
            mRecordTimeView.clipComplete();
            mControlView.setHasRecordPiece(true);
            isAllowChangeMv = false;
        }
    }

    /**
     * addSubView ?????????view????????????
     *
     * @param view ???view
     */
    private void addSubView(View view) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, params);//??????????????????
    }

    /**
     * ????????????????????????click listener
     */
    private OnBackClickListener mBackClickListener;

    public void setBackClickListener(OnBackClickListener listener) {
        this.mBackClickListener = listener;
    }

    private OnFinishListener mCompleteListener;


    @Override
    public void onDialogDismiss() {
        if (mControlView != null) {
            mControlView.setEffectSelViewShow(false);
        }
    }

    @Override
    public void onDialogShow() {
        if (mControlView != null) {
            mControlView.setEffectSelViewShow(true);
        }
    }

    /**
     * ????????????????????????
     */
    public void deleteAllPart() {
        if (clipManager != null) {
            clipManager.deleteAllPart();
            if (clipManager.getDuration() < clipManager.getMinDuration() && mControlView != null) {
                mControlView.setCompleteEnable(false);
            }
            if (clipManager.getDuration() == 0) {
                // ??????????????????
                //                    musicBtn.setVisibility(View.VISIBLE);
                //                    magicMusic.setVisibility(View.VISIBLE);
                //recorder.restartMv();
                mRecordTimeView.clear();
                mControlView.setHasRecordPiece(false);
                isAllowChangeMv = true;
            }
        }
    }

    private float lastScaleFactor;
    private float scaleFactor;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float factorOffset = detector.getScaleFactor() - lastScaleFactor;
        scaleFactor += factorOffset;
        lastScaleFactor = detector.getScaleFactor();
        if (scaleFactor < 0) {
            scaleFactor = 0;
        }
        if (scaleFactor > 1) {
            scaleFactor = 1;
        }
        recorder.setZoom(scaleFactor);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScaleFactor = detector.getScaleFactor();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /**
     * ????????????????????????
     */
    public interface OnBackClickListener {
        void onClick();
    }

    /**
     * ????????????????????????
     */
    public interface OnFinishListener {
        void onComplete(String path, int duration, int ratioMode);
    }

    public void setCompleteListener(OnFinishListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public int getMaxRecordTime() {
        if (maxRecordTime < MIN_RECORD_TIME) {
            return MIN_RECORD_TIME;
        } else if (maxRecordTime > MAX_RECORD_TIME) {
            return MAX_RECORD_TIME;
        } else {

            return maxRecordTime;
        }

    }

    /**
     * ??????????????????
     *
     * @param maxRecordTime
     */
    public void setMaxRecordTime(int maxRecordTime) {
        this.maxRecordTime = maxRecordTime;
    }

    /**
     * ????????????????????????
     *
     * @param minRecordTime
     */
    public void setMinRecordTime(int minRecordTime) {
        this.minRecordTime = minRecordTime;
    }

    /**
     * ??????Gop
     *
     * @param mGop
     */
    public void setGop(int mGop) {
        this.mGop = mGop;

    }

    /**
     * ??????????????????
     *
     * @param mVideoQuality
     */
    public void setVideoQuality(VideoQuality mVideoQuality) {
        this.mVideoQuality = mVideoQuality;
    }

    /**
     * ????????????SurFaceView?????????
     */
    public void setReSizeRatioMode(int ratioMode) {
        this.mRatioMode = ratioMode;
        recorder.setRatioMode(ratioMode);
        mRecorderSurfaceView.setLayoutParams(recorder.getLayoutParams());
//        ???????????????????????????????????????
        if (isSvideoRace) {
            setRaceEffectView();
        }
    }

    /**
     * ??????????????????
     *
     * @param ratioMode
     */
    public void setRatioMode(int ratioMode) {
        this.mRatioMode = ratioMode;
    }

    /**
     * ???????????????????????????
     */
    public void setVideoPath(String path) {
        this.mMixVideoPath = path;
    }

    /**
     * ????????????????????????
     *
     * @param mVideoCodec
     */
    public void setVideoCodec(VideoCodecs mVideoCodec) {
        this.mVideoCodec = mVideoCodec;
    }

    /**
     * ??????????????????
     * @param mRenderingMode
     */
    public void setRenderingMode(RenderingMode mRenderingMode) {
        this.mRenderingMode = mRenderingMode;
    }

    /**
     * ???????????????race?????????
     * @param isSvideoRace ?????????race?????????
     */
    public void setSvideoRace(boolean isSvideoRace) {
        this.isSvideoRace = isSvideoRace;
    }

    /**
     * ??????????????????
     *
     * @param mResolutionMode
     */
    public void setResolutionMode(int mResolutionMode) {
        this.mResolutionMode = mResolutionMode;
    }


    private void saveBeautyParams(int position, BeautyParams beautyParams) {
        if (beautyParams != null) {
            if (mRenderingMode == RenderingMode.FaceUnity) {
                Gson gson = new Gson();
                rememberParamList.set(position, beautyParams);
                rememberBeautyBean.setBeautyList(rememberParamList);
                String jsonString = gson.toJson(rememberBeautyBean);
                if (!TextUtils.isEmpty(jsonString)) {
                    SharedPreferenceUtils.setBeautyParams(getContext(), jsonString);
                }
            } else {
                Gson gson = new Gson();
                rememberRaceParamList.set(position, beautyParams);
                rememberRaceBeautyBean.setBeautyList(rememberRaceParamList);
                String jsonString = gson.toJson(rememberRaceBeautyBean);
                if (!TextUtils.isEmpty(jsonString)) {
                    SharedPreferenceUtils.setRaceBeautyParams(getContext(), jsonString);
                }
            }

        }
    }
    private void saveBeautyShapeParams(int position, BeautyShapeParams beautyParams) {
        if (beautyParams != null) {
            Gson gson = new Gson();

            rememberShapeParamList.set(position, beautyParams);
            rememberBeautyShapeBean.setBeautyList(rememberShapeParamList);
            String jsonString = gson.toJson(rememberBeautyShapeBean);

            if (!TextUtils.isEmpty(jsonString)) {
                SharedPreferenceUtils.setBeautyShapeParams(getContext(), jsonString);
            }
        }
    }

    /**
     * ????????????????????????
     */
    private BeautyParams getBeautyParams(int position) {
        String jsonString;
        if (mRenderingMode == RenderingMode.FaceUnity) {
            jsonString = SharedPreferenceUtils.getBeautyParams(getContext());
        } else {
            jsonString = SharedPreferenceUtils.getRaceBeautyParams(getContext());
        }
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        Gson gson = new Gson();
        RememberBeautyBean rememberBeautyBean = gson.fromJson(jsonString, RememberBeautyBean.class);
        List<BeautyParams> beautyList = rememberBeautyBean.getBeautyList();
        if (beautyList == null) {
            return null;
        }
        return beautyList.get(position);

    }
    /**
     * ??????????????????
     */
    private BeautyShapeParams getBeautyShapeParams(int position) {
        String jsonString = SharedPreferenceUtils.getBeautyShapeParams(getContext());
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        Gson gson = new Gson();
        RememberBeautyShapeBean rememberBeautyBean = gson.fromJson(jsonString, RememberBeautyShapeBean.class);
        List<BeautyShapeParams> beautyList = rememberBeautyBean.getBeautyList();
        if (beautyList == null) {
            return null;
        }
        if (position >= beautyList.size()) {
            return null;
        }
        return beautyList.get(position);
    }


    public void setOnMusicSelectListener(MusicSelectListener musicSelectListener) {
        this.mOutMusicSelectListener = musicSelectListener;
    }

    /**
     * ?????????????????? ??????????????????/???????????????
     * @param path ??????????????????
     * @return name
     */
    private String getFilterName(String path) {
        if (path == null) {
            return null;
        }
        if (LanguageUtils.isCHEN(getContext())) {
            path = path + "/config.json";
        } else {
            path = path + "/configEn.json";
        }
        String name = "";
        StringBuffer var2 = new StringBuffer();
        File var3 = new File(path);

        try {
            FileReader var4 = new FileReader(var3);

            int var7;
            while ((var7 = var4.read()) != -1) {
                var2.append((char)var7);
            }

            var4.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        try {
            JSONObject var4 = new JSONObject(var2.toString());
            name = var4.optString("name");
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

        return name;

    }
}