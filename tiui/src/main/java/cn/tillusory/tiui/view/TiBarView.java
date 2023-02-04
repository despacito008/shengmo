package cn.tillusory.tiui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiHairEnum;
import cn.tillusory.sdk.bean.TiMakeup;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.custom.TiSharePreferences;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiFaceShapeVal;
import cn.tillusory.tiui.model.TiQuickBeautyVal;

/**
 * Created by Anko on 2019-09-06.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiBarView extends LinearLayout {

    private TiSDKManager tiSDKManager;

    private TextView tiBubbleTV;
    private TextView tiNumberTV;
    private SeekBar tiSeekBar;
    private View tiProgressV;
    private View tiMiddleV;
    private ImageView tiRenderEnableIV;

    private int tiSeekBarWidth = 0;

    private String currentAction = RxBusAction.ACTION_SKIN_WHITENING;
    private static String currentBeautyAction = RxBusAction.ACTION_SKIN_WHITENING;
    private static String currentFaceTrimAction = RxBusAction.ACTION_EYE_MAGNIFYING;
    private static String currentGreenScreenEditAction = RxBusAction.ACTION_GREEN_SCREEN_SIMILARITY;

    private static TiQuickBeautyVal tiQuickBeautyVal = TiQuickBeautyVal.STANDARD_QUICK_BEAUTY;
    private static TiFaceShapeVal tiFaceShapeVal = TiFaceShapeVal.CLASSIC_FACE_SHAPE;
    public static String tiFilterName = "";
    private static TiHairEnum tiHairEnum = TiHairEnum.NO_HAIR;

    private static String tiBlusherName = "";
    private static String tiEyelashName = "";
    private static String tiEyebrowName = "";
    private static String tiEyeshadowName = "";
    private static String tiEyelineName = "";

    private static String tiGreenScreenName = "";
    private static int tiGreenScreenSimilarityValue = 0;
    private static int tiGreenScreenSmoothnessValue = 0;
    private static int tiGreenScreenAlphaValue = 0;


    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }

            switch (currentAction) {
                //美颜
                case RxBusAction.ACTION_SKIN_WHITENING:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putWhiteningVal(progress);
                    tiSDKManager.setSkinWhitening(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_SKIN_BLEMISH_REMOVAL:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putBlemishRemovalVal(progress);
                    tiSDKManager.setSkinBlemishRemoval(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_SKIN_PRECISE_BEAUTY:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putPreciseBeautyVal(progress);
                    tiSDKManager.setSkinPreciseBeauty(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_SKIN_TENDERNESS:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putTendernessVal(progress);
                    tiSDKManager.setSkinTenderness(progress);
                    enabledBtnReset();
                    break;
//                case RxBusAction.ACTION_SKIN_SATURATION:
//                    styleTransform(progress);
//                    tiSDKManager.setSkinSaturation(progress - 50);
//                    break;
                case RxBusAction.ACTION_SKIN_BRIGHTNESS:
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putBrightnessVal(progress - 50);
                    tiSDKManager.setSkinBrightness(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_SKIN_SHARPNESS:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putSharpnessVal(progress);
                    tiSDKManager.setSkinSharpness(progress);
                    enabledBtnReset();
                    break;

                //美型
                case RxBusAction.ACTION_EYE_MAGNIFYING:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putEyeMagnifyingVal(progress);
                    tiSDKManager.setEyeMagnifying(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_CHIN_SLIMMING:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putChinSlimmingVal(progress);
                    tiSDKManager.setChinSlimming(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_FACE_NARROWING:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putFaceNarrowingVal(progress);
                    tiSDKManager.setFaceNarrowing(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_CHEEKBONE_SLIMMING: //瘦颧骨
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putCheekboneSlimmingVal(progress);
                    tiSDKManager.setCheekboneSlimming(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_JAWBONE_SLIMMING: //瘦下颌
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putJawboneSlimmingVal(progress);
                    tiSDKManager.setJawboneSlimming(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_JAW_TRANSFORMING:   //下巴
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putJawTransformingVal(progress - 50);
                    tiSDKManager.setJawTransforming(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_JAW_SLIMMING:   //削下巴
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putJawSlimmingVal(progress);
                    tiSDKManager.setJawSlimming(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_FOREHEAD_TRANSFORMING:  //额头
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putForeheadTransformingVal(progress - 50);
                    tiSDKManager.setForeheadTransforming(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYE_INNER_CORNERS:    //内眼角
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putEyeInnerCornersVal(progress - 50);
                    tiSDKManager.setEyeInnerCorners(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYE_OUTER_CORNERS:    //外眼尾
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putEyeOuterCornersVal(progress - 50);
                    tiSDKManager.setEyeOuterCorners(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYE_SPACING:    //眼间距
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putEyeSpacingVal(progress - 50);
                    tiSDKManager.setEyeSpacing(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYE_CORNERS:    //眼角
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putEyeCornersVal(progress - 50);
                    tiSDKManager.setEyeCorners(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_NOSE_MINIFYING: //瘦鼻
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putNoseMinifyingVal(progress - 50);
                    tiSDKManager.setNoseMinifying(-(progress - 50));
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_NOSE_ELONGATING:   //长鼻
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putNoseElongatingVal(progress - 50);
                    tiSDKManager.setNoseElongating(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_MOUTH_TRANSFORMING: //嘴型
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putMouthTransformingVal(progress - 50);
                    tiSDKManager.setMouthTransforming(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_MOUTH_HEIGHT: //嘴高低
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putMouthHeightVal(progress - 50);
                    tiSDKManager.setMouthHeight(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_MOUTH_LIP_SIZE: //唇厚薄
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putMouthLipSizeVal(progress - 50);
                    tiSDKManager.setMouthLipSize(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_MOUTH_SMILING:    //扬嘴角
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putMouthSmilingVal(progress);
                    tiSDKManager.setMouthSmiling(progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_BROW_HEIGHT: //眉高低
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putBrowHeightVal(progress - 50);
                    tiSDKManager.setBrowHeight(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_BROW_LENGTH: //眉长短
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putBrowLengthVal(progress - 50);
                    tiSDKManager.setBrowLength(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_BROW_SPACE: //眉间距
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putBrowSpaceVal(progress - 50);
                    tiSDKManager.setBrowSpace(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_BROW_SIZE: //眉粗细
                    styleTransform(progress);
                    TiSharePreferences.getInstance().putBrowSizeVal(progress - 50);
                    tiSDKManager.setBrowSize(progress - 50);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_BROW_CORNER:    //提眉尾
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putBrowCornerVal(progress);
                    tiSDKManager.setBrowCorner(progress);
                    enabledBtnReset();
                    break;
//                case RxBusAction.ACTION_TEETH_WHITENING:
//                    styleNormal(progress);
//                    tiSDKManager.setTeethWhitening(progress);
//                    enabledBtnReset();
//                    break;

                //滤镜
                case RxBusAction.ACTION_FILTER:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putBeautyFilterVal(tiFilterName, progress);
                    tiSDKManager.setBeautyFilter(tiFilterName, progress);
                    break;

                //脸型
                case RxBusAction.ACTION_FACE_SHAPE:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putFaceShapeVal(tiFaceShapeVal.name(), progress);
                    TiSharePreferences.getInstance().putFaceShapeRelated(tiFaceShapeVal, progress);
                    tiSDKManager.setFaceShape(tiFaceShapeVal.faceShapeEnum, progress);
//                    setFaceShapeValue(tiFaceShapeVal, progress);
                    enabledBtnReset();
                    break;

                //一键美颜
                case RxBusAction.ACTION_QUICK_BEAUTY:
                    styleNormal(progress);
                    TiSharePreferences.getInstance().putQuickBeautyVal(tiQuickBeautyVal.name(), progress);
                    TiSharePreferences.getInstance().putQuickBeautyRelated(tiQuickBeautyVal, progress);
                    tiSDKManager.setOnekeyBeauty(tiQuickBeautyVal.onekeyBeautyEnum, progress);
//                    setQuickBeautyValue(tiQuickBeautyVal, progress);
//                    enabledBtnReset();
                    break;

                //美妆
                case RxBusAction.ACTION_BLUSHER:
                    styleNormal(progress);
                    tiSDKManager.setBlusher(tiBlusherName, progress);
                    TiSharePreferences.getInstance().putBlusherVal(tiBlusherName, progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYELASH:
                    styleNormal(progress);
                    tiSDKManager.setEyeLash(tiEyelashName, progress);
                    TiSharePreferences.getInstance().putEyelashVal(tiEyelashName, progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYEBROW:
                    styleNormal(progress);
                    tiSDKManager.setEyeBrow(tiEyebrowName, progress);
                    TiSharePreferences.getInstance().putEyebrowVal(tiEyebrowName, progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYESHADOW:
                    styleNormal(progress);
                    tiSDKManager.setEyeShadow(tiEyeshadowName, progress);
                    TiSharePreferences.getInstance().putEyeshadowVal(tiEyeshadowName, progress);
                    enabledBtnReset();
                    break;
                case RxBusAction.ACTION_EYELINE:
                    styleNormal(progress);
                    tiSDKManager.setEyeLine(tiEyelineName, progress);
                    TiSharePreferences.getInstance().putEyelineVal(tiEyelineName, progress);
                    enabledBtnReset();
                    break;

                //美发
                case RxBusAction.ACTION_HAIR:
                    styleNormal(progress);
                    tiSDKManager.setHair(tiHairEnum, progress);
                    break;

                //绿幕抠图编辑
                case RxBusAction.ACTION_GREEN_SCREEN_SIMILARITY:
                    styleTransform(progress);
                    tiGreenScreenSimilarityValue = progress - 50;
                    tiSDKManager.setGreenScreen(tiGreenScreenName, tiGreenScreenSimilarityValue, tiGreenScreenSmoothnessValue, tiGreenScreenAlphaValue);
                    TiSharePreferences.getInstance().putGreenScreenSimilarityVal(progress - 50);
                    enabledGreenScreenRestore();
                    break;
                case RxBusAction.ACTION_GREEN_SCREEN_SMOOTHNESS:
                    tiGreenScreenSmoothnessValue = progress;
                    styleNormal(progress);
                    tiSDKManager.setGreenScreen(tiGreenScreenName, tiGreenScreenSimilarityValue, tiGreenScreenSmoothnessValue, tiGreenScreenAlphaValue);
                    TiSharePreferences.getInstance().putGreenScreenSmoothnessVal(progress);
                    enabledGreenScreenRestore();
                    break;
                case RxBusAction.ACTION_GREEN_SCREEN_ALPHA:
                    tiGreenScreenAlphaValue = progress;
                    styleNormal(progress);
                    tiSDKManager.setGreenScreen(tiGreenScreenName, tiGreenScreenSimilarityValue, tiGreenScreenSmoothnessValue, tiGreenScreenAlphaValue);
                    TiSharePreferences.getInstance().putGreenScreenAlphaVal(progress);
                    enabledGreenScreenRestore();
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            tiBubbleTV.setVisibility(VISIBLE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            tiBubbleTV.setVisibility(GONE);
        }
    };

    public TiBarView(Context context) {
        super(context);
    }

    public TiBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TiBarView init(TiSDKManager tiSDKManager) {
        this.tiSDKManager = tiSDKManager;

        RxBus.get().register(this);

        initView();

        initData();

        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (RxBus.get().hasRegistered(this)) {
            if(RxBus.get().hasRegistered(this)) { RxBus.get().unregister(this); }
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_bar, this);

        tiBubbleTV = findViewById(R.id.tiBubbleTV);
        tiNumberTV = findViewById(R.id.tiNumberTV);
        tiSeekBar = findViewById(R.id.tiSeekBar);
        tiProgressV = findViewById(R.id.tiProgressV);
        tiMiddleV = findViewById(R.id.tiMiddleV);
        tiRenderEnableIV = findViewById(R.id.tiRenderEnableIV);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initData() {

        tiSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        tiSeekBar.setProgress(0);

        tiRenderEnableIV.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tiSDKManager.renderEnable(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        tiSDKManager.renderEnable(true);
                        return true;
                }
                return false;
            }
        });

    }

    private void styleNormal(final int progress) {
        tiMiddleV.setVisibility(GONE);

        final CharSequence percent = new StringBuilder().append(progress).append("%");

        tiNumberTV.setText(percent);

        //防止第一次获取不到tiSeekBar的宽度
        if (tiSeekBarWidth <= 0) {
            tiSeekBar.post(new Runnable() {
                @Override
                public void run() {
                    tiSeekBarWidth = tiSeekBar.getWidth();

                    float width = tiSeekBar.getWidth() - (getContext().getResources().getDisplayMetrics().density * 34 + 0.5f);

                    tiBubbleTV.setText(percent);
                    tiBubbleTV.setX(width / 100 * progress + (getContext().getResources().getDisplayMetrics().density * 1f + 0.5f));

                    tiProgressV.setVisibility(VISIBLE);
                    ViewGroup.LayoutParams layoutParams = tiProgressV.getLayoutParams();
                    layoutParams.width = (int) (width / 100 * progress);
                    tiProgressV.setLayoutParams(layoutParams);
                }
            });
        } else {
            float width = tiSeekBarWidth - (getContext().getResources().getDisplayMetrics().density * 34 + 0.5f);

            tiBubbleTV.setText(percent);
            tiBubbleTV.setX(width / 100 * progress + (getContext().getResources().getDisplayMetrics().density * 1f + 0.5f));

            tiProgressV.setVisibility(VISIBLE);
            ViewGroup.LayoutParams layoutParams = tiProgressV.getLayoutParams();
            tiProgressV.setX(getContext().getResources().getDisplayMetrics().density * 16f + 0.5f);
            layoutParams.width = (int) (width / 100 * progress);
            tiProgressV.setLayoutParams(layoutParams);
        }

    }

    private void styleTransform(int progress) {
        tiMiddleV.setVisibility((progress > 48 && progress < 52) ? GONE : VISIBLE);

        CharSequence percent = new StringBuilder().append(progress - 50).append("%");

        tiNumberTV.setText(percent);

        float width = tiSeekBar.getWidth() - (getContext().getResources().getDisplayMetrics().density * 34 + 0.5f);

        tiBubbleTV.setText(percent);
        tiBubbleTV.setX(width / 100 * progress + (getContext().getResources().getDisplayMetrics().density * 1f + 0.5f));

        tiProgressV.setVisibility(VISIBLE);
        ViewGroup.LayoutParams layoutParams = tiProgressV.getLayoutParams();

        if (progress < 51) {
            tiProgressV.setX(width / 100 * progress + (getContext().getResources().getDisplayMetrics().density * 16f + 0.5f));
            layoutParams.width = (int) (width *  (50 - progress) / 100);
        } else {
            tiProgressV.setX(width / 2 + (getContext().getResources().getDisplayMetrics().density * 16f + 0.5f));
            layoutParams.width = (int) (width * (progress - 50) / 100);
        }

        tiProgressV.setLayoutParams(layoutParams);
    }

    public void selectQuickBeauty(boolean takeEffect) {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(true);

        if (takeEffect) {
            busCurrentQuickBeauty(tiQuickBeautyVal);
        } else {
            showQuickBeautyProgressOnly(tiQuickBeautyVal);
        }
    }

    public void selectBeauty() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(tiSDKManager.isBeautyEnable());

        busCurrentAction(currentBeautyAction);
    }

    public void selectFaceShape(boolean takeEffect) {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(true);

        if (takeEffect) {
            busCurrentFaceShape(tiFaceShapeVal);
        } else {
            showFaceShapeProgressOnly(tiFaceShapeVal);
        }
    }

    public void selectFaceTrim() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(tiSDKManager.isFaceTrimEnable());

        busCurrentAction(currentFaceTrimAction);
    }

    public void selectFilter() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(true);

        showFilterProgressOnly(tiFilterName);
//        busCurrentFilter(tiFilterEnum);
    }

    public void selectMakeupBlusher() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(TiSharePreferences.getInstance().isMakeupEnable());

        busCurrentBlusher(tiBlusherName);
    }

    public void selectMakeupEyelash() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(TiSharePreferences.getInstance().isMakeupEnable());

        busCurrentEyelash(tiEyelashName);
    }

    public void selectMakeupEyebrow() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(TiSharePreferences.getInstance().isMakeupEnable());

        busCurrentEyebrow(tiEyebrowName);
    }

    public void selectMakeupEyeshadow() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(TiSharePreferences.getInstance().isMakeupEnable());

        busCurrentEyeshadow(tiEyeshadowName);
    }

    public void selectMakeupEyeline() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(TiSharePreferences.getInstance().isMakeupEnable());

        busCurrentEyeline(tiEyelineName);
    }

    public void selectHair() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(true);

        busCurrentHair(tiHairEnum);
    }

    public void selectGreenScreenEdit() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(true);

        busCurrentAction(currentGreenScreenEditAction);
    }

    public void hideSeekBar() {
        tiNumberTV.setVisibility(INVISIBLE);
        tiSeekBar.setVisibility(INVISIBLE);
        tiProgressV.setVisibility(GONE);
        tiMiddleV.setVisibility(GONE);
    }

    public void showNormalSeekBar() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);
        tiProgressV.setVisibility(VISIBLE);
        tiMiddleV.setVisibility(GONE);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_QUICK_BEAUTY))
    public void busCurrentQuickBeauty(TiQuickBeautyVal quickBeautyVal) {
        currentAction = RxBusAction.ACTION_QUICK_BEAUTY;
        tiQuickBeautyVal = quickBeautyVal;
        tiFilterName = tiQuickBeautyVal.filterName;

        showNormalSeekBar();
        int progress = TiSharePreferences.getInstance().getQuickBeautyVal(tiQuickBeautyVal.name());
        tiSeekBar.setProgress(progress);
        styleNormal(progress);
        tiSDKManager.setOnekeyBeauty(tiQuickBeautyVal.onekeyBeautyEnum, progress);
        TiSharePreferences.getInstance().putQuickBeautyRelated(tiQuickBeautyVal, progress);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_FACE_SHAPE))
    public void busCurrentFaceShape(TiFaceShapeVal faceShapeVal) {
        currentAction = RxBusAction.ACTION_FACE_SHAPE;
        tiFaceShapeVal = faceShapeVal;

        showNormalSeekBar();
        int progress = TiSharePreferences.getInstance().getFaceShapeVal(tiFaceShapeVal.name());
        tiSeekBar.setProgress(progress);
        styleNormal(progress);
        tiSDKManager.setFaceShape(tiFaceShapeVal.faceShapeEnum, progress);
        TiSharePreferences.getInstance().putFaceShapeRelated(tiFaceShapeVal, progress);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_FILTER))
    public void busCurrentFilter(String filterName) {
        currentAction = RxBusAction.ACTION_FILTER;
        tiFilterName = filterName;

        if (filterName.equals("")) {
            hideSeekBar();
            tiSDKManager.setBeautyFilter(filterName, 0);
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getBeautyFilterVal(filterName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setBeautyFilter(filterName, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_HAIR))
    public void busCurrentHair(TiHairEnum hairEnum) {
        currentAction = RxBusAction.ACTION_HAIR;
        tiHairEnum = hairEnum;

        if (tiHairEnum == TiHairEnum.NO_HAIR) {
            hideSeekBar();
            tiSDKManager.setHair(tiHairEnum, 0);
        } else {
            showNormalSeekBar();
            int progress = tiSDKManager.getHairProgress(tiHairEnum);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setHair(tiHairEnum, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_BLUSHER))
    public void busCurrentBlusher(String blusherName) {
        currentAction = RxBusAction.ACTION_BLUSHER;
        tiBlusherName = blusherName;

        if (tiBlusherName.equals(TiMakeup.NO_MAKEUP.getName())) {
            hideSeekBar();
            tiSDKManager.setBlusher(tiBlusherName, 0);
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getBlusherVal(tiBlusherName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setBlusher(tiBlusherName, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_EYELASH))
    public void busCurrentEyelash(String eyelashName) {
        currentAction = RxBusAction.ACTION_EYELASH;
        tiEyelashName = eyelashName;

        if (tiEyelashName.equals(TiMakeup.NO_MAKEUP.getName())) {
            hideSeekBar();
            tiSDKManager.setEyeLash(tiEyelashName, 0);
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getEyelashVal(tiEyelashName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setEyeLash(tiEyelashName, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_EYEBROW))
    public void busCurrentEyebrow(String eyebrowName) {
        currentAction = RxBusAction.ACTION_EYEBROW;
        tiEyebrowName = eyebrowName;

        if (tiEyebrowName.equals(TiMakeup.NO_MAKEUP.getName())) {
            hideSeekBar();
            tiSDKManager.setEyeBrow(tiEyebrowName, 0);
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getEyebrowVal(tiEyebrowName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setEyeBrow(tiEyebrowName, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_EYESHADOW))
    public void busCurrentEyeshadow(String eyeshadowName) {
        currentAction = RxBusAction.ACTION_EYESHADOW;
        tiEyeshadowName = eyeshadowName;

        if (tiEyeshadowName.equals(TiMakeup.NO_MAKEUP.getName())) {
            hideSeekBar();
            tiSDKManager.setEyeShadow(tiEyeshadowName, 0);
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getEyeshadowVal(tiEyeshadowName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setEyeShadow(tiEyeshadowName, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_EYELINE))
    public void busCurrentEyeline(String eyelineName) {
        currentAction = RxBusAction.ACTION_EYELINE;
        tiEyelineName = eyelineName;

        if (tiEyelineName.equals(TiMakeup.NO_MAKEUP.getName())) {
            hideSeekBar();
            tiSDKManager.setEyeLine(tiEyelineName, 0);
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getEyelineVal(tiEyelineName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setEyeLine(tiEyelineName, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_GREEN_SCREEN))
    public void busCurrentGreenScreen(String greenScreenName) {
        tiGreenScreenName = greenScreenName;
        tiGreenScreenSimilarityValue = TiSharePreferences.getInstance().getGreenScreenSimilarityVal();
        tiGreenScreenSmoothnessValue = TiSharePreferences.getInstance().getGreenScreenSmoothnessVal();
        tiGreenScreenAlphaValue = TiSharePreferences.getInstance().getGreenScreenAlphaVal();
        tiSDKManager.setGreenScreen(greenScreenName, tiGreenScreenSimilarityValue, tiGreenScreenSmoothnessValue, tiGreenScreenAlphaValue);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_RESTORE_GREEN_SCREEN))
    public void restoreGreenScreen(Boolean bool) {
        tiSDKManager.setGreenScreen(tiGreenScreenName, 0, 0, 0);
        busCurrentAction(currentGreenScreenEditAction);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void busCurrentAction(String currentAction) {
        this.currentAction = currentAction;
        switch (currentAction) {

            case RxBusAction.ACTION_SKIN_WHITENING:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getWhiteningVal());
                styleNormal(TiSharePreferences.getInstance().getWhiteningVal());
                currentBeautyAction = RxBusAction.ACTION_SKIN_WHITENING;
                break;

            case RxBusAction.ACTION_SKIN_BLEMISH_REMOVAL:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBlemishRemovalVal());
                styleNormal(TiSharePreferences.getInstance().getBlemishRemovalVal());
                currentBeautyAction = RxBusAction.ACTION_SKIN_BLEMISH_REMOVAL;
                tiSDKManager.setSkinBlemishRemoval(TiSharePreferences.getInstance().getBlemishRemovalVal());
                break;

            case RxBusAction.ACTION_SKIN_PRECISE_BEAUTY:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getPreciseBeautyVal());
                styleNormal(TiSharePreferences.getInstance().getPreciseBeautyVal());
                currentBeautyAction = RxBusAction.ACTION_SKIN_PRECISE_BEAUTY;
                tiSDKManager.setSkinPreciseBeauty(TiSharePreferences.getInstance().getPreciseBeautyVal());
                break;

            case RxBusAction.ACTION_SKIN_TENDERNESS:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getTendernessVal());
                styleNormal(TiSharePreferences.getInstance().getTendernessVal());
                currentBeautyAction = RxBusAction.ACTION_SKIN_TENDERNESS;
                break;

//            case RxBusAction.ACTION_SKIN_SATURATION:
//                tiSeekBar.setProgress(tiSDKManager.getSkinSaturation() + 50);
//                styleTransform(tiSDKManager.getSkinSaturation() + 50);
//                currentBeautyAction = RxBusAction.ACTION_SKIN_SATURATION;
//                break;

            case RxBusAction.ACTION_SKIN_BRIGHTNESS:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBrightnessVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getBrightnessVal() + 50);
                currentBeautyAction = RxBusAction.ACTION_SKIN_BRIGHTNESS;
                break;

            case RxBusAction.ACTION_SKIN_SHARPNESS:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getSharpnessVal());
                styleNormal(TiSharePreferences.getInstance().getSharpnessVal());
                currentBeautyAction = RxBusAction.ACTION_SKIN_SHARPNESS;
                break;

            case RxBusAction.ACTION_EYE_MAGNIFYING: //大眼
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getEyeMagnifyingVal());
                styleNormal(TiSharePreferences.getInstance().getEyeMagnifyingVal());
                currentFaceTrimAction = RxBusAction.ACTION_EYE_MAGNIFYING;
                break;

            case RxBusAction.ACTION_CHIN_SLIMMING:  //瘦脸
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getChinSlimmingVal());
                styleNormal(TiSharePreferences.getInstance().getChinSlimmingVal());
                currentFaceTrimAction = RxBusAction.ACTION_CHIN_SLIMMING;
                break;

            case RxBusAction.ACTION_FACE_NARROWING: //窄脸
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getFaceNarrowingVal());
                styleNormal(TiSharePreferences.getInstance().getFaceNarrowingVal());
                currentFaceTrimAction = RxBusAction.ACTION_FACE_NARROWING;
                break;

            case RxBusAction.ACTION_CHEEKBONE_SLIMMING: //瘦颧骨
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getCheekboneSlimmingVal());
                styleNormal(TiSharePreferences.getInstance().getCheekboneSlimmingVal());
                currentFaceTrimAction = RxBusAction.ACTION_CHEEKBONE_SLIMMING;
                break;

            case RxBusAction.ACTION_JAWBONE_SLIMMING: //瘦下颌
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getJawboneSlimmingVal());
                styleNormal(TiSharePreferences.getInstance().getJawboneSlimmingVal());
                currentFaceTrimAction = RxBusAction.ACTION_JAWBONE_SLIMMING;
                break;

            case RxBusAction.ACTION_JAW_TRANSFORMING:   //下巴
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getJawTransformingVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getJawTransformingVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_JAW_TRANSFORMING;
                break;

            case RxBusAction.ACTION_JAW_SLIMMING:   //削下巴
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getJawboneSlimmingVal());
                styleNormal(TiSharePreferences.getInstance().getJawSlimmingVal());
                currentFaceTrimAction = RxBusAction.ACTION_JAW_SLIMMING;
                break;

            case RxBusAction.ACTION_FOREHEAD_TRANSFORMING:  //额头
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getForeheadTransformingVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getForeheadTransformingVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_FOREHEAD_TRANSFORMING;
                break;

            case RxBusAction.ACTION_EYE_INNER_CORNERS:    //内眼角
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getEyeInnerCornersVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getEyeInnerCornersVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_EYE_INNER_CORNERS;
                break;

            case RxBusAction.ACTION_EYE_OUTER_CORNERS:    //外眼尾
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getEyeOuterCornersVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getEyeOuterCornersVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_EYE_OUTER_CORNERS;
                break;

            case RxBusAction.ACTION_EYE_SPACING:    //眼间距
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getEyeSpacingVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getEyeSpacingVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_EYE_SPACING;
                break;

            case RxBusAction.ACTION_EYE_CORNERS:    //眼角
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getEyeCornersVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getEyeCornersVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_EYE_CORNERS;
                break;

            case RxBusAction.ACTION_NOSE_MINIFYING: //瘦鼻
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getNoseMinifyingVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getNoseMinifyingVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_NOSE_MINIFYING;
                break;

            case RxBusAction.ACTION_NOSE_ELONGATING:    //长鼻
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getNoseElongatingVal());
                styleNormal(TiSharePreferences.getInstance().getNoseElongatingVal());
                currentFaceTrimAction = RxBusAction.ACTION_NOSE_ELONGATING;
                break;

            case RxBusAction.ACTION_MOUTH_TRANSFORMING: //嘴型
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getMouthTransformingVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getMouthTransformingVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_MOUTH_TRANSFORMING;
                break;

            case RxBusAction.ACTION_MOUTH_HEIGHT: //嘴高低
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getMouthHeightVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getMouthHeightVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_MOUTH_HEIGHT;
                break;

            case RxBusAction.ACTION_MOUTH_LIP_SIZE: //唇厚薄
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getMouthLipSizeVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getMouthLipSizeVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_MOUTH_LIP_SIZE;
                break;

            case RxBusAction.ACTION_MOUTH_SMILING:    //扬嘴角
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getMouthSmilingVal());
                styleNormal(TiSharePreferences.getInstance().getMouthSmilingVal());
                currentFaceTrimAction = RxBusAction.ACTION_MOUTH_SMILING;
                break;

            case RxBusAction.ACTION_BROW_HEIGHT: //眉高低
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBrowHeightVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getBrowHeightVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_BROW_HEIGHT;
                break;

            case RxBusAction.ACTION_BROW_LENGTH: //眉长短
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBrowLengthVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getBrowLengthVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_BROW_LENGTH;
                break;

            case RxBusAction.ACTION_BROW_SPACE: //眉间距
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBrowSpaceVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getBrowSpaceVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_BROW_SPACE;
                break;

            case RxBusAction.ACTION_BROW_SIZE: //眉粗细
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBrowSizeVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getBrowSizeVal() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_BROW_SIZE;
                break;

            case RxBusAction.ACTION_BROW_CORNER:    //提眉尾
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getBrowCornerVal());
                styleNormal(TiSharePreferences.getInstance().getBrowCornerVal());
                currentFaceTrimAction = RxBusAction.ACTION_BROW_CORNER;
                break;

//            case RxBusAction.ACTION_TEETH_WHITENING:
//                tiSeekBar.setProgress(tiSDKManager.getTeethWhitening());
//                styleNormal(tiSDKManager.getTeethWhitening());
//                currentFaceTrimAction = RxBusAction.ACTION_TEETH_WHITENING;
//                break;

            //绿幕编辑
            case RxBusAction.ACTION_GREEN_SCREEN_SIMILARITY:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getGreenScreenSimilarityVal() + 50);
                styleTransform(TiSharePreferences.getInstance().getGreenScreenSimilarityVal() + 50);
                currentGreenScreenEditAction = RxBusAction.ACTION_GREEN_SCREEN_SIMILARITY;
                break;
            case RxBusAction.ACTION_GREEN_SCREEN_SMOOTHNESS:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getGreenScreenSmoothnessVal());
                styleNormal(TiSharePreferences.getInstance().getGreenScreenSmoothnessVal());
                currentGreenScreenEditAction = RxBusAction.ACTION_GREEN_SCREEN_SMOOTHNESS;
                break;
            case RxBusAction.ACTION_GREEN_SCREEN_ALPHA:
                tiSeekBar.setProgress(TiSharePreferences.getInstance().getGreenScreenAlphaVal());
                styleNormal(TiSharePreferences.getInstance().getGreenScreenAlphaVal());
                currentGreenScreenEditAction = RxBusAction.ACTION_GREEN_SCREEN_ALPHA;
                break;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag("ACTION_CHANGE_BAR_VIEW_COLOR")
    })
    public void changeViewColor(Boolean is1v1) {
        if (is1v1) {
            tiNumberTV.setTextColor(getResources().getColor(R.color.ti_unselected_tab));
            tiBubbleTV.setTextColor(getResources().getColor(R.color.white));
            tiBubbleTV.setBackground(getResources().getDrawable(R.drawable.ic_ti_bubble_black));
            tiSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_ti_seek_bar_1v1));
            tiSeekBar.setThumb(getResources().getDrawable(R.drawable.ic_ti_seek_bar_thumb_1v1));
            tiProgressV.setBackground(getResources().getDrawable(R.drawable.bg_ti_bar_process_1v1));
            tiMiddleV.setBackground(getResources().getDrawable(R.drawable.ic_ti_seek_bar_middle_1v1));
            tiRenderEnableIV.setImageResource(R.drawable.ic_ti_render_black);
        } else {
            tiNumberTV.setTextColor(getResources().getColor(R.color.white));
            tiBubbleTV.setTextColor(getResources().getColor(R.color.ti_bubble_gray));
            tiBubbleTV.setBackground(getResources().getDrawable(R.drawable.ic_ti_bubble_white));
            tiSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_ti_seek_bar));
            tiSeekBar.setThumb(getResources().getDrawable(R.drawable.ic_ti_seek_bar_thumb));
            tiProgressV.setBackground(getResources().getDrawable(R.drawable.bg_ti_bar_process));
            tiMiddleV.setBackground(getResources().getDrawable(R.drawable.ic_ti_seek_bar_middle));
            tiRenderEnableIV.setImageResource(R.drawable.ic_ti_render_white);
        }
    }

    public void showQuickBeautyProgressOnly(TiQuickBeautyVal quickBeautyVal) {
        currentAction = RxBusAction.ACTION_QUICK_BEAUTY;
        tiQuickBeautyVal = quickBeautyVal;

        showNormalSeekBar();
        int progress = TiSharePreferences.getInstance().getQuickBeautyVal(tiQuickBeautyVal.name());
        tiSeekBar.setProgress(progress);
        styleNormal(progress);
    }

    public void showFaceShapeProgressOnly(TiFaceShapeVal faceShapeVal) {
        currentAction = RxBusAction.ACTION_FACE_SHAPE;
        tiFaceShapeVal = faceShapeVal;

        showNormalSeekBar();
        int progress = TiSharePreferences.getInstance().getFaceShapeVal(tiFaceShapeVal.name());
        tiSeekBar.setProgress(progress);
        styleNormal(progress);
    }

    private void showFilterProgressOnly(String filterName) {
        currentAction = RxBusAction.ACTION_FILTER;

        if (filterName.equals("")) {
            hideSeekBar();
        } else {
            showNormalSeekBar();
            int progress = TiSharePreferences.getInstance().getBeautyFilterVal(filterName);
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
        }
    }

    private void enabledBtnReset() {
        RxBus.get().post(RxBusAction.ACTION_ENABLED_BTN_RESET, true);
    }

    private void enabledGreenScreenRestore() {
        RxBus.get().post(RxBusAction.ACTION_ENABLE_GREEN_SCREEN_RESTORE, true);
    }



}


