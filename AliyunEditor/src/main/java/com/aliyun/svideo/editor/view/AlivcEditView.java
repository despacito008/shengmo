package com.aliyun.svideo.editor.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.crop.AliyunCropCreator;
import com.aliyun.crop.struct.CropParam;
import com.aliyun.crop.supply.AliyunICrop;
import com.aliyun.crop.supply.CropCallback;
import com.aliyun.editor.EditorCallBack;
import com.aliyun.editor.EffectType;
import com.aliyun.editor.TimeEffectType;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunICanvasController;
import com.aliyun.qupai.editor.AliyunIEditor;
import com.aliyun.qupai.editor.AliyunPasterController;
import com.aliyun.qupai.editor.AliyunPasterManager;
import com.aliyun.qupai.editor.OnAnimationFilterRestored;
import com.aliyun.qupai.editor.OnPasterRestored;
import com.aliyun.qupai.editor.impl.AliyunEditorFactory;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.base.UIConfigManager;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.editor.R;
import com.aliyun.svideo.editor.bean.AlivcEditOutputParam;
import com.aliyun.svideo.editor.editor.AbstractPasterUISimpleImpl;
import com.aliyun.svideo.editor.editor.EditorActivity;
import com.aliyun.svideo.editor.editor.PasterUICaptionImpl;
import com.aliyun.svideo.editor.editor.PasterUIGifImpl;
import com.aliyun.svideo.editor.editor.PasterUITextImpl;
import com.aliyun.svideo.editor.editor.thumblinebar.OverlayThumbLineBar;
import com.aliyun.svideo.editor.editor.thumblinebar.ThumbLineBar;
import com.aliyun.svideo.editor.editor.thumblinebar.ThumbLineConfig;
import com.aliyun.svideo.editor.editor.thumblinebar.ThumbLineOverlay;
import com.aliyun.svideo.editor.effects.control.BaseChooser;
import com.aliyun.svideo.editor.effects.control.EditorService;
import com.aliyun.svideo.editor.effects.control.EffectInfo;
import com.aliyun.svideo.editor.effects.control.OnEffectActionLister;
import com.aliyun.svideo.editor.effects.control.OnEffectChangeListener;
import com.aliyun.svideo.editor.effects.control.OnTabChangeListener;
import com.aliyun.svideo.editor.effects.control.TabGroup;
import com.aliyun.svideo.editor.effects.control.TabViewStackBinding;
import com.aliyun.svideo.editor.effects.control.UIEditorPage;
import com.aliyun.svideo.editor.effects.control.ViewStack;
import com.aliyun.svideo.editor.effects.filter.AnimationFilterController;
import com.aliyun.svideo.editor.effects.transition.TransitionChooserView;
import com.aliyun.svideo.editor.msg.Dispatcher;
import com.aliyun.svideo.editor.msg.body.FilterTabClick;
import com.aliyun.svideo.editor.msg.body.LongClickAnimationFilter;
import com.aliyun.svideo.editor.msg.body.LongClickUpAnimationFilter;
import com.aliyun.svideo.editor.msg.body.SelectColorFilter;
import com.aliyun.svideo.editor.util.AlivcSnapshot;
import com.aliyun.svideo.editor.util.EditorCommon;
import com.aliyun.svideo.editor.util.FixedToastUtils;
import com.aliyun.svideo.editor.util.ThreadUtil;
import com.aliyun.svideo.editor.viewoperate.ViewOperator;
import com.aliyun.svideo.editor.widget.AliyunPasterWithImageView;
import com.aliyun.svideo.editor.widget.AliyunPasterWithTextView;
import com.aliyun.svideo.common.widget.AlivcCircleLoadingDialog;
import com.aliyun.svideo.sdk.external.struct.AliyunIClipConstructor;
import com.aliyun.svideo.sdk.external.struct.common.AliyunClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBase;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBean;
import com.aliyun.svideo.sdk.external.struct.effect.EffectCaption;
import com.aliyun.svideo.sdk.external.struct.effect.EffectFilter;
import com.aliyun.svideo.sdk.external.struct.effect.EffectPaster;
import com.aliyun.svideo.sdk.external.struct.effect.EffectText;
import com.aliyun.svideo.sdk.external.struct.effect.TransitionBase;
import com.aliyun.svideo.sdk.external.struct.effect.TransitionCircle;
import com.aliyun.svideo.sdk.external.struct.effect.TransitionFade;
import com.aliyun.svideo.sdk.external.struct.effect.TransitionFiveStar;
import com.aliyun.svideo.sdk.external.struct.effect.TransitionShutter;
import com.aliyun.svideo.sdk.external.struct.effect.TransitionTranslate;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunIThumbnailFetcher;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunThumbnailFetcherFactory;
import com.aliyun.svideo.common.utils.DensityUtils;
import com.aliyun.svideo.common.utils.FastClickUtil;
import com.duanqu.transcode.NativeParser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

/**
 * @author zsy_18 data:2018/8/24
 */
public class AlivcEditView extends RelativeLayout
    implements View.OnClickListener, OnEffectChangeListener, OnTabChangeListener,
    OnAnimationFilterRestored {
    private static final String TAG = AlivcEditView.class.getName();
    /**
     * ?????????????????????
     */
    private AliyunIEditor mAliyunIEditor;
    /**
     * ?????????????????????
     */
    private AliyunPasterManager mPasterManager;
    /**
     * ???????????????Controller?????????????????????????????????????????????
     */
    public AliyunICanvasController mCanvasController;
    /**
     * ????????????????????????
     */
    private AliyunIThumbnailFetcher mThumbnailFetcher;

    /**
     * ??????????????????????????????Gop????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private AliyunICrop mTranscoder;

    /**
     * ???????????????Controller??????,?????????????????????????????????????????????????????????
     */
    private AliyunPasterController mAliyunPasterController;

    private OverlayThumbLineBar mThumbLineBar;

    /**
     * ????????????item?????????ScrollView
     */
    private HorizontalScrollView mBottomLinear;
    /**
     * ???????????????????????????SurfaceView
     */
    private SurfaceView mSurfaceView;
    /**
     * ?????????????????????????????????
     */
    private TabGroup mTabGroup;
    /**
     * ??????????????????????????????
     */
    private ViewStack mViewStack;
    /**
     * ??????????????????????????????view?????????????????????????????????????????????????????????
     */
    private EditorService mEditorService;
    /**
     * ??????
     */
    private RelativeLayout mActionBar;
    private FrameLayout resCopy;
    private FrameLayout mTransCodeTip;
    private ProgressBar mTransCodeProgress;
    public FrameLayout mPasterContainer;
    private FrameLayout mGlSurfaceContainer;
    private ImageView mIvLeft;
    private TextView mTvRight;
    private LinearLayout mBarLinear;
    private TextView mPlayImage;
    private TextView mTvCurrTime;
    /**
     * ????????????
     */
    private int mScreenWidth;
    /**
     * ????????????
     */
    private Bitmap mWatermarkBitmap;
    /**
     * ????????????????????????
     */
    private AnimationFilterController mAnimationFilterController;
    /**
     * ???????????????????????????????????? ????????????????????????
     */
    private ThumbLineOverlay mTimeEffectOverlay;
    private ThumbLineOverlay.ThumbLineOverlayView mThumbLineOverlayView;
    /**
     * ?????????????????????????????????
     */
    private boolean mUseInvert = false;
    /**
     * ??????????????????????????????????????????
     */
    private boolean mUseAnimationFilter = false;
    /**
     * ??????????????????????????????????????????????????????true???????????????????????????
     */
    private boolean mCanAddAnimation = true;
    /**
     * ??????????????????????????????
     */
    private boolean mIsTranscoding = false;
    /**
     * ??????????????????????????????
     */
    private boolean mIsDestroyed = false;
    /**
     * ????????????????????????onStop??????
     */
    private boolean mIsStop = false;
    private boolean mWaitForReady = false;

    private AbstractPasterUISimpleImpl mCurrentEditEffect;
    /**
     * ??????
     */
    private int mVolume = 50;
    /**
     * ??????UI??????
     */
    private ViewOperator mViewOperate;
    private Point mPasterContainerPoint;
    private EffectBean lastMusicBean;
    //????????????thumbLineBar???????????????
    private ThumbLineBar.OnBarSeekListener mBarSeekListener;
    //?????????????????????????????????????????????????????????
    private PlayerListener mPlayerListener;
    private EffectInfo mLastMVEffect;
    private EffectInfo mLastSoundEffect;
    private ObjectAnimator animatorX;
    private Toast showToast;

    /**
     * ????????????Handler?????????
     */
    private AlivcEditHandler alivcEditHandler;
    /**
     * ?????????
     */
    private ExecutorService executorService;
    /**
     * ??????????????????
     */
    private final String PATH_THUMBNAIL = Constants.SDCardConstants.getDir(getContext()) + File.separator + "thumbnail1.jpg";
    /**
     * ??????????????????
     */
    private boolean isTakeFrame = false;
    /**
     * ????????????????????????
     */
    private boolean isTakeFrameSelected = false;
    /**
     * ????????????????????????
     */
    private boolean hasCaptureCover = false;
    /**
     * ???????????????????????????surface?????????
     */
    private AlivcSnapshot mSnapshop;
    /**
     * ????????????????????????
     */
    private boolean hasWaterMark;
    /**
     * ?????????????????????
     */
    private boolean mHasRecordMusic;
    /**
     * ??????????????????????????????
     */
    private boolean isReplaceMusic;
    /**
     * ???????????????????????????????????????
     */
    private boolean isMixRecord;

    private AlivcCircleLoadingDialog mLoadingDialog;


    public AlivcEditView(Context context) {
        this(context, null);
    }

    public AlivcEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlivcEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Dispatcher.getInstance().register(this);

        Point point = new Point();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_editor_view_edit, this, true);
        initView();
        initListView();
        add2Control();
        initThreadHandler();
        if (PermissionUtils.checkPermissionsGroup(getContext(), PermissionUtils.PERMISSION_STORAGE)) {
            copyAssets();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        resCopy = (FrameLayout) findViewById(R.id.copy_res_tip);
        mTransCodeTip = (FrameLayout) findViewById(R.id.transcode_tip);
        mTransCodeProgress = (ProgressBar) findViewById(R.id.transcode_progress);
        mBarLinear = (LinearLayout) findViewById(R.id.bar_linear);
        mBarLinear.bringToFront();
        mActionBar = (RelativeLayout) findViewById(R.id.action_bar);
        mActionBar.setBackgroundDrawable(null);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mTvRight = findViewById(R.id.tv_right);
        mIvLeft.setImageResource(R.mipmap.aliyun_svideo_back);
        //uiConfig????????????
        //UIConfigManager.setImageResourceConfig(mTvRight, R.attr.finishImage, R.mipmap.aliyun_svideo_complete_red);
        mIvLeft.setVisibility(View.VISIBLE);

        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
        mTvCurrTime = (TextView) findViewById(R.id.tv_curr_duration);

        mGlSurfaceContainer = (FrameLayout) findViewById(R.id.glsurface_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.play_view);
        mBottomLinear = findViewById(R.id.edit_bottom_tab);
        setBottomTabResource();
        mPasterContainer = (FrameLayout) findViewById(R.id.pasterView);

        mPlayImage = findViewById(R.id.play_button);
        mPlayImage.setOnClickListener(this);
        switchPlayStateUI(false);

        final GestureDetector mGesture = new GestureDetector(getContext(), new MyOnGestureListener());
        View.OnTouchListener pasterTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        };

        mPasterContainer.setOnTouchListener(pasterTouchListener);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomEditorView();
            }
        });

    }

    /**
     * ????????????????????????????????????
     */
    private void setBottomTabResource() {
        TextView[] textViews = {
            findViewById(R.id.tab_filter),
            findViewById(R.id.tab_effect_audio_mix),
            findViewById(R.id.tab_effect_overlay),
            findViewById(R.id.tab_effect_caption),
            findViewById(R.id.tab_effect_mv),
            findViewById(R.id.tab_effect_sound),
            findViewById(R.id.tab_effect_filter),
            findViewById(R.id.tab_effect_time),
            findViewById(R.id.tab_effect_transition),
            findViewById(R.id.tab_paint),
            findViewById(R.id.tab_cover)
        };
        int length = textViews.length;
        int[] index = new int[length];
        for (int i = 0; i < length; i++) {
            //???????????????????????????top
            index[i] = 1;
        }
        int[] attrs = {
            R.attr.filterImage,
            R.attr.musicImage,
            R.attr.pasterImage,
            R.attr.captionImage,
            R.attr.mvImage,
            R.attr.sound,//??????
            R.attr.effectImage,
            R.attr.timeImage,
            R.attr.translationImage,
            R.attr.paintImage,
            R.attr.coverImage
        };
        int[] defaultResourceIds = {
            R.mipmap.aliyun_svideo_filter,
            R.mipmap.aliyun_svideo_music,
            R.mipmap.aliyun_svideo_overlay,
            R.mipmap.aliyun_svideo_caption,
            R.mipmap.aliyun_svideo_mv,
            R.mipmap.aliyun_svideo_sound,//??????, ??????mv icon
            R.mipmap.alivc_svideo_effect,
            R.mipmap.aliyun_svideo_time,
            R.mipmap.aliyun_svideo_transition,
            R.mipmap.aliyun_svideo_paint,
            R.mipmap.aliyun_svideo_cover

        };
        UIConfigManager.setImageResourceConfig(textViews, index, attrs, defaultResourceIds);
        //??????????????????????????????
        int[] bottomItemMenuVisibleTags = getContext().getResources().getIntArray(R.array.bottomItemMenuVisibleTags);
        for (int i = 0; i < textViews.length; i++) {
            if (bottomItemMenuVisibleTags[i] == 0) {
                textViews[i].setVisibility(GONE);
            } else {
                textViews[i].setVisibility(VISIBLE);
            }

        }
    }

    public OverlayThumbLineBar getThumbLineBar() {
        return mThumbLineBar;
    }

    private void initGlSurfaceView() {
        if (mVideoParam == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGlSurfaceContainer.getLayoutParams();
        FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
        int outputWidth = mVideoParam.getOutputWidth();
        int outputHeight = mVideoParam.getOutputHeight();

        float percent;
        if (outputWidth >= outputHeight) {
            percent = (float) outputWidth / outputHeight;
        } else {
            percent = (float) outputHeight / outputWidth;
        }
        /*
          ??????surfaceView?????????????????????????????????????????????????????????????????????????????????????????????????????????
         */
        surfaceLayout.width = mScreenWidth;
        surfaceLayout.height = Math.round((float) outputHeight * mScreenWidth / outputWidth);
        mPasterContainerPoint = new Point(surfaceLayout.width, surfaceLayout.height);
        ViewGroup.MarginLayoutParams marginParams = null;
        if (layoutParams instanceof MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) surfaceLayout;
        } else {
            marginParams = new MarginLayoutParams(surfaceLayout);
        }
        if (percent < 1.5) {
            marginParams.setMargins(0,
                                    getContext().getResources().getDimensionPixelSize(R.dimen.alivc_svideo_title_height), 0, 0);
        } else {
            if (outputWidth > outputHeight) {
                marginParams.setMargins(0,
                                        getContext().getResources().getDimensionPixelSize(R.dimen.alivc_svideo_title_height) * 2, 0, 0);
            }
        }
        mGlSurfaceContainer.setLayoutParams(layoutParams);
        mPasterContainer.setLayoutParams(marginParams);
        mSurfaceView.setLayoutParams(marginParams);
        //mCanvasController = mAliyunIEditor.obtainCanvasController(getContext(),
        //                    marginParams.width, marginParams.height);
    }

    public float dip2px(Context paramContext, float paramFloat) {
        return 0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density;
    }

    private void initListView() {
        mViewOperate = new ViewOperator(this, mActionBar, mSurfaceView, mBottomLinear, mPasterContainer, mPlayImage);
        mViewOperate.setAnimatorListener(new ViewOperator.AnimatorListener() {
            @Override
            public void onShowAnimationEnd() {
                UIEditorPage index = UIEditorPage.get(mTabGroup.getCheckedIndex());
                switch (index) {
                case PAINT:
                    //2018/8/30 ??????????????????
                    if (mCanvasController == null) {
                        int width = mPasterContainer.getLayoutParams().width;
                        int height = mPasterContainer.getLayoutParams().height;
                        mCanvasController = mAliyunIEditor.obtainCanvasController(getContext(),
                                            width, height);
                        mCanvasController.setCurrentSize(dip2px(getContext(), 5));
                    }

                    mCanvasController.removeCanvas();
                    View canvasView = mCanvasController.getCanvas();
                    mPasterContainer.removeView(canvasView);
                    mPasterContainer.addView(canvasView, mPasterContainer.getWidth(), mPasterContainer.getHeight());
                    break;
                default:
                    break;
                }
            }

            @Override
            public void onHideAnimationEnd() {
                if (isTakeFrameSelected) {
                    isTakeFrame = true;
                    //????????????????????????
                    playingResume();
                    //??????????????????????????????
                    mPlayImage.setVisibility(VISIBLE);
                    isTakeFrameSelected = false;
                }
            }
        });
        mTvRight.setVisibility(View.VISIBLE);
        mEditorService = new EditorService();
        mTabGroup = new TabGroup();
        mViewStack = new ViewStack(getContext(), this, mViewOperate);
        mViewStack.setEditorService(mEditorService);
        mViewStack.setEffectChange(this);
        mViewStack.setOnEffectActionLister(mOnEffectActionLister);
        mViewStack.setOnTransitionPreviewListener(mOnTransitionPreviewListener);
        mTabGroup.addView(findViewById(R.id.tab_filter));
        mTabGroup.addView(findViewById(R.id.tab_effect_audio_mix));
        mTabGroup.addView(findViewById(R.id.tab_effect_overlay));
        mTabGroup.addView(findViewById(R.id.tab_effect_caption));
        mTabGroup.addView(findViewById(R.id.tab_effect_mv));
        mTabGroup.addView(findViewById(R.id.tab_effect_sound));
        mTabGroup.addView(findViewById(R.id.tab_effect_filter));
        mTabGroup.addView(findViewById(R.id.tab_effect_time));
        mTabGroup.addView(findViewById(R.id.tab_effect_transition));
        mTabGroup.addView(findViewById(R.id.tab_paint));
        mTabGroup.addView(findViewById(R.id.tab_cover));

    }

    private void add2Control() {
        TabViewStackBinding tabViewStackBinding = new TabViewStackBinding();
        tabViewStackBinding.setViewStack(mViewStack);
        mTabGroup.setOnCheckedChangeListener(tabViewStackBinding);
        mTabGroup.setOnTabChangeListener(this);
    }

    private void initEditor() {
        //??????onTextureRender????????????
        mEditorCallback.mNeedRenderCallback = EditorCallBack.RENDER_CALLBACK_TEXTURE;
        mAliyunIEditor = AliyunEditorFactory.creatAliyunEditor(mUri, mEditorCallback);
        initGlSurfaceView();
        {
            //?????????????????????????????????AliyunIEditor.init??????????????????????????????????????????????????????UI?????????????????????????????????????????????????????????????????????UI
            mPasterManager = mAliyunIEditor.createPasterManager();
            FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
            /*
              ???????????????????????????????????????mPasterManager.setDisplaySize??????????????????????????????????????????????????????????????????????????????????????????????????????
              ?????????????????????????????????wrapContent??????matchParent?????????????????????????????????view??????????????????????????????
             */
            try {
                mPasterManager.setDisplaySize(surfaceLayout.width, surfaceLayout.height);
            } catch (Exception e) {
                showToast = FixedToastUtils.show(getContext(), e.getMessage());
                ((Activity) getContext()).finish();
                return;
            }
            mPasterManager.setOnPasterRestoreListener(mOnPasterRestoreListener);
            mAnimationFilterController = new AnimationFilterController(getContext().getApplicationContext(),
                    mAliyunIEditor);
            mAliyunIEditor.setAnimationRestoredListener(AlivcEditView.this);
        }

        mTranscoder = AliyunCropCreator.createCropInstance(getContext());
        VideoDisplayMode mode = mVideoParam.getScaleMode();
        int ret = mAliyunIEditor.init(mSurfaceView, getContext().getApplicationContext());
        mAliyunIEditor.setDisplayMode(mode);
        mAliyunIEditor.setVolume(mVolume);
        mAliyunIEditor.setFillBackgroundColor(Color.BLACK);
        if (ret != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
            showToast = FixedToastUtils.show(getContext(),
                                             getResources().getString(R.string.alivc_editor_edit_tip_init_failed));
            ((Activity) getContext()).finish();
            return;
        }
        mEditorService.addTabEffect(UIEditorPage.MV, mAliyunIEditor.getMVLastApplyId());
        mEditorService.addTabEffect(UIEditorPage.FILTER_EFFECT, mAliyunIEditor.getFilterLastApplyId());
        mEditorService.addTabEffect(UIEditorPage.AUDIO_MIX, mAliyunIEditor.getMusicLastApplyId());
        mEditorService.setPaint(mAliyunIEditor.getPaintLastApply());

        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (FastClickUtil.isFastClickActivity(EditorActivity.class.getSimpleName())) {
                    return;
                }
                mTvRight.setEnabled(false);
                //?????????????????????????????????Demo?????????????????????????????????????????????????????????????????????
                mAliyunIEditor.saveEffectToLocal();
                //?????????????????????????????????????????????????????????????????????
                if (hasCaptureCover && mSnapshop.isSnapshotting()) {
                    alivcEditHandler.sendEmptyMessageDelayed(SAVE_COVER, 500);
                    if (mTransitionAnimation == null) {
                        //??????animation
                        mTransitionAnimation = new AlivcCircleLoadingDialog(getContext(), mPasterContainer.getHeight());
                    }
                    mTransitionAnimation.show();
                    return;
                }
                if (hasCaptureCover && !mSnapshop.isSnapshotting()) {
                    //????????????????????????????????????????????????????????????
                    jumpToNextActivity();
                } else {
                    //?????????????????????????????????????????????????????????????????????????????????loading???

                    if (mLoadingDialog == null) {
                        mLoadingDialog = new AlivcCircleLoadingDialog(getContext(), 0);
                        mLoadingDialog.show();
                    } else {
                        return;
                    }
                    final AliyunIThumbnailFetcher fetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
                    fetcher.fromConfigJson(mUri.getPath());
                    fetcher.setParameters(mAliyunIEditor.getVideoWidth(), mAliyunIEditor.getVideoHeight(),
                                          AliyunIThumbnailFetcher.CropMode.Mediate, VideoDisplayMode.SCALE, 1);
                    fetcher.requestThumbnailImage(new long[] {0}, new AliyunIThumbnailFetcher.OnThumbnailCompletion() {

                        @Override
                        public void onThumbnailReady(Bitmap bitmap, long l) {
                            FileOutputStream fileOutputStream = null;
                            try {
                                fileOutputStream = new FileOutputStream(PATH_THUMBNAIL);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                        fileOutputStream = null;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            jumpToNextActivity();
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                            fetcher.release();
                        }

                        @Override
                        public void onError(int errorCode) {
                            fetcher.release();
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                        }
                    });
                }

            }
        });

        mPlayerListener = new PlayerListener() {

            @Override
            public long getCurrDuration() {
                return mAliyunIEditor.getCurrentStreamPosition();
            }

            @Override
            public long getDuration() {
                long streamDuration = mAliyunIEditor.getStreamDuration();
                Log.d(TAG, "getDuration: " + streamDuration);
                return streamDuration;
            }

            @Override
            public void updateDuration(long duration) {
                mTvCurrTime.setText(convertDuration2Text(duration));
            }
        };

        mViewStack.setPlayerListener(mPlayerListener);
        //????????????????????????
        initThumbLineBar();
        //??????????????????
        mThumbLineBar.hide();
        File mWatermarkFile = new File(
            getContext().getExternalFilesDir("") + "/AliyunEditorDemo/tail/logo.png");
        if (mWatermarkFile.exists()) {
            if (mWatermarkBitmap == null || mWatermarkBitmap.isRecycled()) {
                mWatermarkBitmap = BitmapFactory.decodeFile(
                                       getContext().getExternalFilesDir("") + "/AliyunEditorDemo/tail/logo.png");
            }
            mSurfaceView.post(new Runnable() {
                @Override
                public void run() {
                    int outputWidth = mVideoParam.getOutputWidth();
                    int outputHeight = mVideoParam.getOutputHeight();
                    int mWatermarkBitmapWidth = DensityUtils.dip2px(getContext(), 30);
                    int mWatermarkBitmapHeight = DensityUtils.dip2px(getContext(), 30);
                    if (mWatermarkBitmap != null && !mWatermarkBitmap.isRecycled()) {
                        mWatermarkBitmapWidth = mWatermarkBitmap.getWidth();
                        mWatermarkBitmapHeight = mWatermarkBitmap.getHeight();
                    }
                    float posY = 0;
                    float percent = (float) outputHeight / outputWidth;
                    if (percent > 1.5) {
                        posY = 0f
                               + (float) (mWatermarkBitmapHeight / 2 + getContext().getResources().getDimensionPixelSize(
                                              R.dimen.alivc_svideo_title_height)) / 1.5f / mSurfaceView.getHeight();
                    } else {
                        posY = 0f + (float) mWatermarkBitmapHeight / 1.5f / mSurfaceView.getHeight() / 2;
                    }
                    /**
                     * ???????????? ?????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????????
                     * ?????????????????? ?????????????????????????????????????????????????????????????????????????????????0,0???????????????1,1????????????
                     *
                     */
                    if (hasWaterMark) {
                        mAliyunIEditor.applyWaterMark(
                            getContext().getExternalFilesDir("") + "/AliyunEditorDemo/tail/logo.png",
                            (float) mWatermarkBitmapWidth * 0.5f * 0.8f / mSurfaceView.getWidth(),
                            (float) mWatermarkBitmapHeight * 0.5f * 1.4f / mSurfaceView.getHeight(),
                            (float) mWatermarkBitmapWidth / 1.5f / mSurfaceView.getWidth() / 2,
                            posY);
                    }
                    //????????????
                    //ActionRotate actionRotate = new ActionRotate();
                    //actionRotate.setStartTime(0);
                    //actionRotate.setTargetId(id);
                    //actionRotate.setDuration(10 * 1000 * 1000);
                    //actionRotate.setRepeat(true);
                    //actionRotate.setDurationPerCircle(3 * 1000 * 1000);
                    //mAliyunIEditor.addFrameAnimation(actionRotate);

                    /* //????????????
                    EffectPicture effectPicture = new EffectPicture("/sdcard/1.png");
                    effectPicture.x = 0.5f;
                    effectPicture.y = 0.5f;
                    effectPicture.width = 0.5f;
                    effectPicture.height = 0.5f;
                    effectPicture.start = 0;
                    effectPicture.end = 10 * 1000 * 1000;
                    mAliyunIEditor.addImage(effectPicture);

                    ActionRotate actionRotateImg = new ActionRotate();
                    actionRotateImg.setStartTime(0);
                    actionRotateImg.setTargetId(effectPicture.getViewId());
                    actionRotateImg.setDuration(10 * 1000 * 1000);
                    actionRotateImg.setRepeat(true);
                    actionRotateImg.setDurationPerCircle(3 * 1000 * 1000);
                    mAliyunIEditor.addFrameAnimation(actionRotateImg);*/
                    if (hasTailAnimation) {
                        //????????????
                        mAliyunIEditor.addTailWaterMark(
                            getContext().getExternalFilesDir("") + "/AliyunEditorDemo/tail/logo.png",
                            (float) mWatermarkBitmapWidth / mSurfaceView.getWidth(),
                            (float) mWatermarkBitmapHeight / mSurfaceView.getHeight(), 0.5f, 0.5f, 2000 * 1000);
                    }

                }
            });
        }

        mAliyunIEditor.play();

    }

    /**
     * ?????????????????????
     */
    private void initThumbLineBar() {
        //??????????????????????????????
        int thumbnailSize = getResources().getDimensionPixelOffset(R.dimen.aliyun_editor_size_square_thumbnail);
        Point thumbnailPoint = new Point(thumbnailSize, thumbnailSize);

        //???????????????
        if (mThumbnailFetcher == null) {
            mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
            mThumbnailFetcher.fromConfigJson(mUri.getPath());
        } else if (mThumbnailFetcher.getTotalDuration() != mAliyunIEditor.getStreamDuration() / 1000) {
            //??????????????????????????????????????????
            Log.i(TAG, "initThumbLineBar: reset thumbLine");
            mAliyunIEditor.saveEffectToLocal();
            mThumbnailFetcher.release();
            mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
            mThumbnailFetcher.fromConfigJson(mUri.getPath());
        }

        //???????????????????????????
        ThumbLineConfig thumbLineConfig = new ThumbLineConfig.Builder()
        .thumbnailFetcher(mThumbnailFetcher)
        .screenWidth(mScreenWidth)
        .thumbPoint(thumbnailPoint)
        .thumbnailCount(10).build();

        if (mThumbLineBar == null) {
            mThumbLineBar = findViewById(R.id.simplethumblinebar);

            mBarSeekListener = new ThumbLineBar.OnBarSeekListener() {

                @Override
                public void onThumbLineBarSeek(long duration) {
                    mAliyunIEditor.seek(duration);
                    if (mThumbLineBar != null) {
                        mThumbLineBar.pause();
                    }
                    switchPlayStateUI(true);
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                        if (!mCurrentEditEffect.isVisibleInTime(duration)) {
                            //??????
                            mCurrentEditEffect.mPasterView.setVisibility(View.GONE);
                        } else {
                            //??????
                            mCurrentEditEffect.mPasterView.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mUseInvert) {
                        //???seek????????????????????????????????????
                        if (duration <= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = false;
                        } else {
                            mCanAddAnimation = true;
                        }
                    } else {
                        //???seek????????????????????????????????????
                        if (mAliyunIEditor.getDuration() - duration <= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = false;
                        } else {
                            mCanAddAnimation = true;
                        }
                    }

                }

                @Override
                public void onThumbLineBarSeekFinish(long duration) {
                    mAliyunIEditor.seek(duration);
                    if (mThumbLineBar != null) {
                        mThumbLineBar.pause();
                    }
                    switchPlayStateUI(true);
                    if (mUseInvert) {
                        //???seek????????????????????????????????????
                        if (duration <= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = false;
                        } else {
                            mCanAddAnimation = true;
                        }
                    } else {
                        //???seek????????????????????????????????????
                        if (mAliyunIEditor.getDuration() - duration >= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = true;
                        } else {
                            mCanAddAnimation = false;
                        }
                    }
                }
            };

            //Overlay??????View
            mThumbLineOverlayView = new ThumbLineOverlay.ThumbLineOverlayView() {
                View rootView = LayoutInflater.from(getContext()).inflate(
                                    R.layout.alivc_editor_view_timeline_overlay, null);
                View headView = rootView.findViewById(R.id.head_view);
                View tailView = rootView.findViewById(R.id.tail_view);
                View middleView = rootView.findViewById(R.id.middle_view);

                @Override
                public ViewGroup getContainer() {
                    return (ViewGroup) rootView;
                }

                @Override
                public View getHeadView() {
                    return headView;
                }

                @Override
                public View getTailView() {
                    return tailView;
                }

                @Override
                public View getMiddleView() {
                    return middleView;
                }
            };

        }

        mThumbLineBar.setup(thumbLineConfig, mBarSeekListener, mPlayerListener);

    }

    /**
     * ???????????????????????????????????? ?????????,?????????????????????: ????????????, ?????????????????????, mipmap/aliyun_svideo_pause ?????????,?????????????????????: ????????????, ????????????????????????,
     * mipmap/aliyun_svideo_play
     *
     * @param changeState, ?????????????????????,  true: ????????????, false: ????????????
     */
    public void switchPlayStateUI(boolean changeState) {
        if (changeState) {
            mPlayImage.setText(getResources().getString(R.string.alivc_editor_edit_play_start));
            UIConfigManager.setImageResourceConfig(mPlayImage, 0, R.attr.playImage, R.mipmap.aliyun_svideo_play);
        } else {
            mPlayImage.setText(getResources().getString(R.string.alivc_editor_edit_play_pause));
            UIConfigManager.setImageResourceConfig(mPlayImage, 0, R.attr.pauseImage, R.mipmap.aliyun_svideo_pause);
        }
    }

    private final OnPasterRestored mOnPasterRestoreListener = new OnPasterRestored() {

        @Override
        public void onPasterRestored(final List<AliyunPasterController> controllers) {

            Log.d(TAG, "onPasterRestored: " + controllers.size());

            mPasterContainer.post(new Runnable() {//????????????????????????????????????????????????????????????UI????????????????????????????????????????????????????????????UI??????????????????
                @Override
                public void run() {

                    if (mThumbLineBar != null && mThumbLineBar.getChildCount() != 0) {
                        //????????????????????????????????????????????????paster??????????????? ??????????????????????????????
                        mThumbLineBar.removeOverlayByPages(
                            UIEditorPage.CAPTION,
                            UIEditorPage.FONT,
                            UIEditorPage.OVERLAY
                        );
                    }

                    if (mPasterContainer != null) {
                        mPasterContainer.removeAllViews();
                    }
                    final List<AbstractPasterUISimpleImpl> aps = new ArrayList<>();
                    for (AliyunPasterController c : controllers) {
                        if (!c.isPasterExists()) {
                            continue;
                        }
                        if (c.getPasterStartTime() >= mAliyunIEditor.getStreamDuration()) {
                            //??????????????????????????????,??????
                            continue;
                        }
                        c.setOnlyApplyUI(true);
                        if (c.getPasterType() == EffectPaster.PASTER_TYPE_GIF) {
                            mCurrentEditEffect = addPaster(c);
                        } else if (c.getPasterType() == EffectPaster.PASTER_TYPE_TEXT) {
                            mCurrentEditEffect = addSubtitle(c, true);
                        } else if (c.getPasterType() == EffectPaster.PASTER_TYPE_CAPTION) {
                            mCurrentEditEffect = addCaption(c);
                        }

                        mCurrentEditEffect.showTimeEdit();
                        mCurrentEditEffect.getPasterView().setVisibility(View.INVISIBLE);
                        aps.add(mCurrentEditEffect);
                        mCurrentEditEffect.moveToCenter();
                        mCurrentEditEffect.hideOverlayView();

                    }

                    for (AbstractPasterUISimpleImpl pui : aps) {
                        pui.editTimeCompleted();
                        pui.getController().setOnlyApplyUI(false);
                    }

                }
            });
        }

    };

    @Override
    public void onEffectChange(final EffectInfo effectInfo) {
        Log.e("editor", "====== onEffectChange ");
        //??????????????????

        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        UIEditorPage type = effectInfo.type;

        Log.d(TAG, "effect path " + effectInfo.getPath());
        switch (type) {
        case AUDIO_MIX:
            if (!effectInfo.isAudioMixBar) {
                //??????mv??????????????????
                mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MIX);
                mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MV_AUDIO);
                if (lastMusicBean != null) {
                    mAliyunIEditor.removeMusic(lastMusicBean);
                }
                lastMusicBean = new EffectBean();
                lastMusicBean.setId(effectInfo.id);
                lastMusicBean.setPath(effectInfo.getPath());

                if (lastMusicBean.getPath() != null) {
                    //????????????seek???0?????????????????????????????????
                    lastMusicBean.setStartTime(effectInfo.startTime * 1000);//?????????us?????????x1000
                    //lastMusicBean.setDuration(effectInfo.endTime == 0 ? Integer.MAX_VALUE
                    //                          : (effectInfo.endTime - effectInfo.startTime) * 1000);//?????????us?????????x1000
                    lastMusicBean.setDuration(Integer.MAX_VALUE);//?????????????????????
                    lastMusicBean.setStreamStartTime(effectInfo.streamStartTime * 1000);
                    lastMusicBean.setStreamDuration(
                        (effectInfo.streamEndTime - effectInfo.streamStartTime) * 1000);//?????????us?????????x1000
                    effectInfo.mixId = mAliyunIEditor.applyMusic(lastMusicBean);
                    lastMusicBean.setWeight(effectInfo.musicWeight);
                } else {
                    //??????mv??????
                    if (mLastMVEffect != null) {
                        applyMVEffect(mLastMVEffect);
                    }
                }
            } else {
                effectInfo.mixId = mAliyunIEditor.getMusicLastApplyId();
            }
            if (isReplaceMusic) {
                mAliyunIEditor.applyMusicMixWeight(effectInfo.mixId, 100);
            } else {
                mAliyunIEditor.applyMusicMixWeight(effectInfo.mixId, effectInfo.musicWeight);
            }
            mAliyunIEditor.seek(0);
            // ????????????????????????
            playingResume();
            break;
        case FILTER_EFFECT:
            if (effect.getPath().contains("Vertigo")) {
                EffectFilter filter = new EffectFilter(effect.getPath());
                mAliyunIEditor.addAnimationFilter(filter);
            } else {
                mAliyunIEditor.applyFilter(effect);
            }
            break;

        case SOUND:
            // ??????

            List<AliyunClip> allClips = mAliyunIEditor.getSourcePartManager().getAllClips();
            int size = allClips.size();
            for (int i = 0; i < size; i++) {
                if (mLastSoundEffect != null) {
                    mAliyunIEditor.removeAudioEffect(allClips.get(i).getId(), mLastSoundEffect.audioEffectType);
                }
                mAliyunIEditor.audioEffect(allClips.get(i).getId(), effectInfo.audioEffectType, effectInfo.soundWeight);
            }
            mLastSoundEffect = effectInfo;
            Log.i("log_editor_sound_type", String.valueOf(effectInfo.audioEffectType));
            mAliyunIEditor.seek(0);
            mAliyunIEditor.play();
            switchPlayStateUI(false);

            break;
        case MV:
            //???????????????????????????MV???????????????????????????????????????MV?????????
            mLastMVEffect = effectInfo;
            applyMVEffect(effectInfo);

            break;
        case CAPTION:
            mAliyunPasterController = mPasterManager.addPaster(effectInfo.getPath());

            if (mAliyunPasterController != null) {
                //????????????????????????
                EffectBase effectBase = mAliyunPasterController.getEffect();
                if (effectBase instanceof EffectCaption) {
                    ((EffectCaption) effectBase).font = effectInfo.fontPath + "/font.ttf";
                    mAliyunPasterController.setEffect(effectBase);
                }

                mAliyunPasterController.setPasterStartTime(mAliyunIEditor.getCurrentStreamPosition());
                PasterUICaptionImpl cui = addCaption(mAliyunPasterController);
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                    //????????????????????????paster????????????remove
                    mCurrentEditEffect.removePaster();
                }
                playingPause();
                mCurrentEditEffect = cui;
                mCurrentEditEffect.showTimeEdit();
            } else {
                showToast = FixedToastUtils.show(getContext(), getResources().getString(R.string.alivc_editor_edit_tip_captions_fail));
            }
            break;
        case OVERLAY:
            mAliyunPasterController = mPasterManager.addPaster(effectInfo.getPath());
            if (mAliyunPasterController != null) {
                //add success
                mAliyunPasterController.setPasterStartTime(mAliyunIEditor.getCurrentStreamPosition());
                PasterUIGifImpl gifui = addPaster(mAliyunPasterController);

                if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                    //????????????????????????paster????????????remove
                    mCurrentEditEffect.removePaster();
                }
                playingPause();
                mCurrentEditEffect = gifui;
                mCurrentEditEffect.showTimeEdit();
            } else {
                //add failed
                showToast = FixedToastUtils.show(getContext(), getResources().getString(R.string.alivc_editor_edit_tip_gif_fail));
            }

            break;
        case FONT:
            mAliyunPasterController = mPasterManager.addSubtitle(null, effectInfo.fontPath + "/font.ttf");
            if (mAliyunPasterController != null) {
                mAliyunPasterController.setPasterStartTime(mAliyunIEditor.getCurrentStreamPosition());
                PasterUITextImpl textui = addSubtitle(mAliyunPasterController, false);
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                    //????????????????????????paster????????????remove
                    mCurrentEditEffect.removePaster();
                }
                playingPause();
                mCurrentEditEffect = textui;
                mCurrentEditEffect.showTimeEdit();
                textui.showTextEdit(mUseInvert);
            } else {
                showToast = FixedToastUtils.show(getContext(), getResources().getString(R.string.alivc_editor_edit_tip_word_fail));
            }
            //                mCurrentEditEffect.setImageView((ImageView) findViewById(R.id.test_image));

            break;
        case TIME:
            if (effectInfo.startTime < 0) {
                effectInfo.startTime = mAliyunIEditor.getCurrentStreamPosition();
            }
            if (mIsTranscoding) {
                showToast = FixedToastUtils.show(getContext(),
                                                 getResources().getString(R.string.alivc_editor_edit_tip_transcode_no_operate));
                return;
            }
            applyTimeEffect(effectInfo);
            break;
        case TRANSITION:
            setTransition(effectInfo);

            break;
        default:
            break;
        }
    }

    /**
     * ??????MV??????
     *
     * @param effectInfo
     */
    private void applyMVEffect(EffectInfo effectInfo) {
        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        UIEditorPage type = effectInfo.type;
        if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
            mCurrentEditEffect.editTimeCompleted();
        }

        String path = null;
        if (effectInfo.list != null) {
            path = EditorCommon.getMVPath(effectInfo.list, mVideoParam.getOutputWidth(),
                                          mVideoParam.getOutputHeight());
        }
        effect.setPath(path);
        int id;
        if (path != null && new File(path).exists()) {
            mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MIX);
            //?????????applyMV???????????????????????????getMvAudioId
            mAliyunIEditor.applyMV(effect);
            id = effect.getMvAudioId();
            Log.d(TAG, "editor resetEffect end:" + id);
            if (isReplaceMusic) {
                mAliyunIEditor.applyMusicMixWeight(id, 100);

            } else if (isMixRecord) {
                //???????????????????????????mv??????
                mAliyunIEditor.applyMusicWeight(id, 0);
            } else {
                mAliyunIEditor.applyMusicMixWeight(id, effect.getWeight());

            }
        } else {
            mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MV);
            if (lastMusicBean != null) {
                mAliyunIEditor.applyMusic(lastMusicBean);
                id = lastMusicBean.getMvAudioId();
                if (isReplaceMusic) {
                    mAliyunIEditor.applyMusicMixWeight(id, 100);
                } else if (isMixRecord) {
                    mAliyunIEditor.applyMusicWeight(id, 0);
                } else {
                    mAliyunIEditor.applyMusicMixWeight(id, lastMusicBean.getWeight());
                }
            } else {
                if (isReplaceMusic) {
                    //????????????
                    mAliyunIEditor.applyMusicMixWeight(0, 0);
                }
            }
        }
        //????????????????????????????????????????????????????????????
        if (mUseInvert) {
            mAliyunIEditor.seek(mAliyunIEditor.getStreamDuration());
        } else {
            mAliyunIEditor.seek(0);
        }
        mAliyunIEditor.resume();
        if (mThumbLineBar != null) {
            mThumbLineBar.resume();
        }
        switchPlayStateUI(false);
    }

    private void applyTimeEffect(EffectInfo effectInfo) {

        mUseInvert = false;
        if (mTimeEffectOverlay != null) {
            mThumbLineBar.removeOverlay(mTimeEffectOverlay);
        }
        mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_TIME);
        if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_NONE)) {
            playingResume();
        } else if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_RATE)) {
            if (effectInfo.isMoment) {

                mTimeEffectOverlay = mThumbLineBar.addOverlay(effectInfo.startTime, 1000 * 1000, mThumbLineOverlayView,
                                     0, false, UIEditorPage.TIME);
                //mAliyunIEditor.stop();
                //playingPause();
                mAliyunIEditor.stop();
                mAliyunIEditor.rate(effectInfo.timeParam, effectInfo.startTime / 1000, 1000, false);
                playingResume();
            } else {
                mTimeEffectOverlay = mThumbLineBar.addOverlay(0, 1000000000L, mThumbLineOverlayView, 0, false,
                                     UIEditorPage.TIME);
                //playingPause();
                mAliyunIEditor.stop();
                mAliyunIEditor.rate(effectInfo.timeParam, 0, 1000000000L, false);
                playingResume();

            }
        } else if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_INVERT)) {

            mUseInvert = true;
            mTimeEffectOverlay = mThumbLineBar.addOverlay(0, 1000000000L, mThumbLineOverlayView, 0, false,
                                 UIEditorPage.TIME);
            //mAliyunIEditor.stop();
            //playingPause();
            checkAndTranscode(TimeEffectType.TIME_EFFECT_TYPE_INVERT, 0, 0, 0, false);
        } else if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_REPEAT)) {
            mTimeEffectOverlay = mThumbLineBar.addOverlay(effectInfo.startTime, 1000 * 1000, mThumbLineOverlayView, 0,
                                 false, UIEditorPage.TIME);
            //mAliyunIEditor.stop();
            //playingPause();
            checkAndTranscode(TimeEffectType.TIME_EFFECT_TYPE_REPEAT, 3, effectInfo.startTime / 1000, 1000, false);
        }
        if (mTimeEffectOverlay != null) {
            mTimeEffectOverlay.switchState(ThumbLineOverlay.STATE_FIX);
        }
    }

    private boolean mIsTransitioning = false;
    private AlivcCircleLoadingDialog mTransitionAnimation;

    private void startTransitionAnimation() {
        mTransitionAnimation.show();
        mIsTransitioning = true;
    }

    private void stopTransitionAnimation() {
        mTransitionAnimation.dismiss();
        mIsTransitioning = false;
    }

    private void setTransition(final EffectInfo effectInfo) {

        if (mTransitionAnimation == null) {
            //??????animation
            mTransitionAnimation = new AlivcCircleLoadingDialog(getContext(), mPasterContainer.getHeight());
        }
        if (mIsTransitioning) {
            return;
        }
        startTransitionAnimation();

        if (effectInfo.mutiEffect == null) {
            //??????????????????
            final TransitionBase transition = getTransitionBase(effectInfo);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    mAliyunIEditor.saveEffectToLocal();
                    mAliyunIEditor.setTransition(effectInfo.clipIndex, transition);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("effectInfo", effectInfo);
                    Message message = new Message();
                    message.what = ADD_TRANSITION;
                    message.setData(bundle);
                    alivcEditHandler.sendMessage(message);
                    resetTimeLineLayout();
                }
            });
        } else if (effectInfo.mutiEffect.size() != 0) {
            //??????????????????
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    mAliyunIEditor.saveEffectToLocal();
                    Map<Integer, TransitionBase> hashMap = new HashMap<>();
                    for (EffectInfo info : effectInfo.mutiEffect) {
                        TransitionBase transitionBase = getTransitionBase(info);
                        hashMap.put(info.clipIndex, transitionBase);
                    }
                    mAliyunIEditor.setTransition(hashMap);

                    alivcEditHandler.sendEmptyMessage(REVERT_TRANSITION);
                    resetTimeLineLayout();

                }
            });

        } else {
            stopTransitionAnimation();
        }

    }

    /**
     * ?????????????????????Handler
     */
    private void initThreadHandler() {
        executorService = ThreadUtil.newDynamicSingleThreadedExecutor(new AlivcEditThread());
        alivcEditHandler = new AlivcEditHandler(this);
    }

    /**
     * ???????????????????????????
     *
     * @param isHashRecordMusic boolean
     */
    public void setHasRecordMusic(boolean isHashRecordMusic) {
        this.mHasRecordMusic = isHashRecordMusic;
    }

    /**
     * ???????????????????????????
     *
     * @return boolean
     */
    public boolean isHasRecordMusic() {
        return mHasRecordMusic;
    }

    /**
     * ??????????????????????????????
     */
    public void setIsMixRecord(boolean isMixRecord) {
        this.isMixRecord = isMixRecord;
    }

    /**
     * ?????????????????????
     */
    public boolean isMaxRecord() {
        return isMixRecord;
    }

    public static class AlivcEditThread implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("AlivcEdit Thread");
            return thread;
        }
    }

    private static final int ADD_TRANSITION = 1;
    private static final int REVERT_TRANSITION = 2;
    private static final int SAVE_COVER = 3;

    private static class AlivcEditHandler extends Handler {

        private WeakReference<AlivcEditView> reference;

        public AlivcEditHandler(AlivcEditView editView) {
            reference = new WeakReference<>(editView);
        }

        @Override
        public void handleMessage(Message msg) {
            AlivcEditView alivcEditView = reference.get();
            if (alivcEditView == null) {
                return;
            }
            switch (msg.what) {
            case REVERT_TRANSITION:
                alivcEditView.playingResume();
                alivcEditView.stopTransitionAnimation();
                alivcEditView.clickCancel();
                break;
            case ADD_TRANSITION:
                EffectInfo effectInfo = (EffectInfo) msg.getData().getSerializable("effectInfo");
                alivcEditView.addTransitionSuccess(effectInfo);
                break;

            case SAVE_COVER:
                //????????????????????????????????????????????????????????????????????????
                if (alivcEditView.mSnapshop.isSnapshotting()) {
                    sendEmptyMessageDelayed(SAVE_COVER, 500);
                } else {
                    removeMessages(SAVE_COVER);
                    alivcEditView.mTransitionAnimation.dismiss();
                    alivcEditView.jumpToNextActivity();
                }
                break;
            default:
                break;
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param effectInfo
     */
    private void addTransitionSuccess(EffectInfo effectInfo) {

        //????????????
        long advanceTime = 1000 * 1000;
        long clipStartTime = mAliyunIEditor.getClipStartTime(effectInfo.clipIndex + 1);

        advanceTime = clipStartTime - advanceTime >= 0 ? clipStartTime - advanceTime : 0;
        mAliyunIEditor.seek(advanceTime);
        playingResume();
        mWaitForReady = true;
        stopTransitionAnimation();
        Log.d(TAG, "onTransitionPreview: index = " + effectInfo.clipIndex
              + " ,clipStartTime = " + clipStartTime
              + " ,duration = " + mAliyunIEditor.getDuration()
              + " ,advanceTime = " + advanceTime
             );
    }

    @Nullable
    private TransitionBase getTransitionBase(EffectInfo effectInfo) {
        TransitionBase transition = null;
        long overlapDuration = 1000 * 1000;//????????????
        switch (effectInfo.transitionType) {
        case TransitionChooserView.EFFECT_NONE:
            break;
        case TransitionChooserView.EFFECT_RIGHT:
            transition = new TransitionTranslate();
            transition.setOverlapDuration(overlapDuration);
            ((TransitionTranslate) transition).setDirection(TransitionBase.DIRECTION_RIGHT);
            break;
        case TransitionChooserView.EFFECT_CIRCLE:
            transition = new TransitionCircle();
            transition.setOverlapDuration(overlapDuration);
            break;
        case TransitionChooserView.EFFECT_FADE:
            transition = new TransitionFade();
            transition.setOverlapDuration(overlapDuration);
            break;
        case TransitionChooserView.EFFECT_FIVE_STAR:
            transition = new TransitionFiveStar();
            transition.setOverlapDuration(overlapDuration);
            break;
        case TransitionChooserView.EFFECT_SHUTTER:
            transition = new TransitionShutter();
            transition.setOverlapDuration(overlapDuration);
            ((TransitionShutter) transition).setLineWidth(0.1f);
            ((TransitionShutter) transition).setOrientation(TransitionBase.ORIENTATION_HORIZONTAL);
            break;
        case TransitionChooserView.EFFECT_UP:
            transition = new TransitionTranslate();
            transition.setOverlapDuration(overlapDuration);
            ((TransitionTranslate) transition).setDirection(TransitionBase.DIRECTION_UP);
            break;
        case TransitionChooserView.EFFECT_DOWN:
            transition = new TransitionTranslate();
            transition.setOverlapDuration(overlapDuration);
            ((TransitionTranslate) transition).setDirection(TransitionBase.DIRECTION_DOWN);
            break;
        case TransitionChooserView.EFFECT_LEFT:
            transition = new TransitionTranslate();
            transition.setOverlapDuration(overlapDuration);
            ((TransitionTranslate) transition).setDirection(TransitionBase.DIRECTION_LEFT);
            break;
        default:
            break;
        }
        return transition;
    }

    /**
     * ??????Gop????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????1080, 1920???gop??????5???fps??????30
     * ???????????????????????????????????????????????????????????????1080, 1920???gop???5???fps???30???type??? ffmpeg???
     * <p>
     * ???????????????????????????????????????
     *
     * @param type         ???????????????????????????????????????
     * @param times        ??????????????????????????????
     * @param startTime    ?????????????????????
     * @param duration     ???????????????
     * @param needDuration ?????????????????????????????????
     */
    private void checkAndTranscode(final TimeEffectType type, final int times, final long startTime,
                                   final long duration, final boolean needDuration) {

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                AliyunClip clip = mAliyunIEditor.getSourcePartManager().getAllClips().get(0);
                final AtomicInteger flag = new AtomicInteger(0);
                if (clip == null) {
                    return null;
                }
                boolean ret = checkInvert(clip.getSource());
                if (!ret) {
                    mAliyunIEditor.saveEffectToLocal();
                    final CountDownLatch countDownLatch = new CountDownLatch(1);

                    CropParam param = new CropParam();
                    param.setGop(5);
                    param.setFrameRate(30);
                    param.setQuality(VideoQuality.SSD);
                    param.setInputPath(clip.getSource());
                    param.setVideoCodec(VideoCodecs.H264_SOFT_FFMPEG);
                    param.setCrf(19);
                    param.setOutputPath(clip.getSource() + "_invert_transcode");
                    Log.i(TAG, "log_editor_edit_transcode : " + param.getOutputPath());
                    int width = 0;
                    int height = 0;
                    int rotate = 0;
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    try {
                        mmr.setDataSource(clip.getSource());
                        rotate = Integer.parseInt(
                                     mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                        if (rotate == 90 || rotate == 270) {
                            height = Integer.parseInt(
                                         mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                            width = Integer.parseInt(
                                        mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                        } else {
                            width = Integer.parseInt(
                                        mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                            height = Integer.parseInt(
                                         mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                        }
                    } catch (Exception e) {
                        width = mVideoParam.getOutputWidth();
                        height = mVideoParam.getOutputHeight();
                    } finally {
                        mmr.release();
                    }
                    param.setOutputWidth(width);
                    param.setOutputHeight(height);
                    mTranscoder.setCropParam(param);
                    mTranscoder.setCropCallback(new CropCallback() {
                        @Override
                        public void onProgress(final int percent) {
                            Log.d(TAG, "percent" + percent);
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    mTransCodeProgress.setProgress(percent);
                                }
                            });
                        }

                        @Override
                        public void onError(int code) {
                            Log.d(TAG, "onError" + code);
                            flag.set(1);
                            countDownLatch.countDown();
                            mIsTranscoding = false;
                        }

                        @Override
                        public void onComplete(long duration) {
                            AliyunIClipConstructor clipConstructor = mAliyunIEditor.getSourcePartManager();
                            AliyunClip clip = clipConstructor.getMediaPart(0);
                            clip.setSource(clip.getSource() + "_invert_transcode");
                            clipConstructor.updateMediaClip(0, clip);
                            mAliyunIEditor.applySourceChange();
                            flag.set(2);
                            countDownLatch.countDown();
                            mIsTranscoding = false;
                        }

                        @Override
                        public void onCancelComplete() {
                            flag.set(3);
                            if (mIsDestroyed) {
                                mTranscoder.dispose();
                            }
                            countDownLatch.countDown();
                            mIsTranscoding = false;
                        }
                    });
                    mIsTranscoding = true;
                    int r = mTranscoder.startCrop();
                    if (r != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
                        return null;
                    }
                    post(new Runnable() {
                        @Override
                        public void run() {
                            mTransCodeTip.setVisibility(View.VISIBLE);
                            BaseChooser bottomView = mViewOperate.getBottomView();
                            if (bottomView != null) {
                                bottomView.setClickable(false);
                            }
                        }
                    });
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return flag;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (mIsDestroyed) {
                    return;
                }
                mTransCodeTip.setVisibility(View.GONE);
                mAliyunIEditor.stop();
                if (o instanceof AtomicInteger) {
                    if (((AtomicInteger)o).get() == 0 || ((AtomicInteger)o).get() == 2) {
                        if (type == TimeEffectType.TIME_EFFECT_TYPE_INVERT) {
                            mAliyunIEditor.invert();
                        } else if (type == TimeEffectType.TIME_EFFECT_TYPE_REPEAT) {
                            mAliyunIEditor.repeat(times, startTime, duration, needDuration);
                        }

                    }
                }
                //????????????????????????????????????stop???????????????????????????
                //?????????isNeedResume??????true
                if (!mIsStop) {
                    playingResume();
                } else {
                    isNeedResume = true;
                }

                //mAliyunIEditor.play();
                BaseChooser bottomView = mViewOperate.getBottomView();
                if (bottomView != null) {
                    bottomView.setClickable(true);
                }
            }
        } .execute(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTabChange() {
        Log.d(TAG, "onTabChange: ");
        UIEditorPage page = UIEditorPage.get(mTabGroup.getCheckedIndex());
        switch (page) {
        case AUDIO_MIX:
            playingPause();
            break;
        case SOUND:
            //?????????
            break;
        case FONT:
        case CAPTION:
        case OVERLAY:
            //case??????????????????paster????????????????????????
            mPasterEffecCachetList.clear();
            for (int i = 0; i < mPasterContainer.getChildCount(); i++) {
                View childAt = mPasterContainer.getChildAt(i);
                Object tag = childAt.getTag();
                if (tag == null || !(tag instanceof AbstractPasterUISimpleImpl)) {
                    //?????????pasterView???tag??????
                    continue;
                }
                AbstractPasterUISimpleImpl uiSimple = (AbstractPasterUISimpleImpl) tag;
                if (!isPasterTypeHold(page, uiSimple.getEditorPage())) {
                    //??????paster??????????????????????????????????????????
                    continue;
                }
                EffectBase effect = uiSimple.getController().getEffect();
                if (effect instanceof EffectCaption) {
                    EffectCaption src = (EffectCaption) effect;
                    EffectCaption copy = new EffectCaption("");
                    src.copy(copy);
                    mPasterEffecCachetList.add(copy);
                } else if (effect instanceof EffectText) {
                    EffectText src = (EffectText) effect;
                    EffectText copy = new EffectText("");
                    src.copy(copy);
                    mPasterEffecCachetList.add(copy);
                } else if (effect instanceof EffectPaster) {
                    EffectPaster src = (EffectPaster) effect;
                    EffectPaster copy = new EffectPaster("");
                    src.copy(copy);
                    mPasterEffecCachetList.add(copy);
                }
            }
            break;
        case COVER:
            //????????????????????????????????????
            playingPause();
            mPlayImage.setVisibility(GONE);
            break;

        default:
            break;
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param pageOne {@link UIEditorPage}
     * @param page2   {@link UIEditorPage}
     * @return boolean
     */
    private boolean isPasterTypeHold(UIEditorPage pageOne, UIEditorPage page2) {
        //???pageOne???????????????page2??????????????????true
        //???pageOne????????????????????????page2?????????????????????????????????true
        return pageOne == UIEditorPage.OVERLAY && page2 == UIEditorPage.OVERLAY
               || pageOne != UIEditorPage.OVERLAY && page2 != UIEditorPage.OVERLAY;
    }

    private List<EffectBase> mPasterEffecCachetList = new ArrayList<>();

    private void checkAndRemovePaster() {
        int count = mPasterContainer.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View pv = mPasterContainer.getChildAt(i);
            AbstractPasterUISimpleImpl uic = (AbstractPasterUISimpleImpl) pv.getTag();
            if (uic != null && !uic.isPasterExists()) {
                Log.e(TAG, "removePaster");
                uic.removePaster();
            }
        }
    }

    /**
     * ??????resetThumbLine?????????
     */
    private void resetTimeLineLayout() {
        Log.i(TAG, "resetTimeLineLayout");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initThumbLineBar();
            }
        });
    }

    protected void playingPause() {
        if (mAliyunIEditor.isPlaying()) {
            mAliyunIEditor.pause();
            if (mThumbLineBar != null) {
                mThumbLineBar.pause();
            }
            switchPlayStateUI(true);
        }
    }

    private void playingResume() {
        if (!mAliyunIEditor.isPlaying()) {
            if (mAliyunIEditor.isPaused()) {
                mAliyunIEditor.resume();
            } else {
                mAliyunIEditor.play();
            }
            if (mThumbLineBar != null) {
                mThumbLineBar.resume();
            }
            switchPlayStateUI(false);
        }
    }

    private PasterUIGifImpl addPaster(AliyunPasterController controller) {
        Log.d(TAG, "add GIF");
        AliyunPasterWithImageView pasterView = (AliyunPasterWithImageView) View.inflate(getContext(),
                                               R.layout.alivc_editor_view_paster_gif, null);

        mPasterContainer.addView(pasterView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return new PasterUIGifImpl(pasterView, controller, mThumbLineBar);
    }

    /**
     * ????????????
     *
     * @param controller
     * @return
     */
    private PasterUICaptionImpl addCaption(AliyunPasterController controller) {
        AliyunPasterWithImageView captionView = (AliyunPasterWithImageView) View.inflate(getContext(),
                                                R.layout.alivc_editor_view_paster_caption, null);
        mPasterContainer.addView(captionView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Log.d(TAG, "add ??????");
        return new PasterUICaptionImpl(captionView, controller, mThumbLineBar, mAliyunIEditor);
    }

    /**
     * ????????????
     *
     * @param controller
     * @param restore
     * @return
     */
    private PasterUITextImpl addSubtitle(AliyunPasterController controller, boolean restore) {
        Log.d(TAG, "add ??????");
        AliyunPasterWithTextView captionView = (AliyunPasterWithTextView) View.inflate(getContext(),
                                               R.layout.alivc_editor_view_paster_text, null);
        mPasterContainer.addView(captionView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return new PasterUITextImpl(captionView, controller, mThumbLineBar, mAliyunIEditor, restore);
    }

    @Override
    public void onClick(View view) {
        if (view == mPlayImage && mAliyunIEditor != null) {
            //?????????????????????????????????????????????
            if (mUseAnimationFilter) {
                return;
            }
            if (mAliyunIEditor.isPlaying()) {
                playingPause();
            } else {
                playingResume();
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                }
            }
        }
    }

    private void clickConfirm() {

        // ??????????????????????????????????????????
        int checkIndex = mTabGroup.getCheckedIndex();
        UIEditorPage page = UIEditorPage.get(checkIndex);
        if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
            mCurrentEditEffect.editTimeCompleted();
        }
        switch (page) {
        case COVER:
            //????????????????????????????????????mEditorCallback.onTextureRender?????????
            isTakeFrameSelected = true;
            hasCaptureCover = true;

            break;
        case PAINT:
            mCanvasController.confirm();
            mCanvasController.applyPaintCanvas();
            mPasterContainer.removeView(mCanvasController.getCanvas());
            break;
        default:
            break;
        }
        mViewOperate.hideBottomView();
    }

    /**
     * ???????????????????????????
     */
    private void clickCancel() {
        // ??????????????????????????????????????????
        int checkIndex = mTabGroup.getCheckedIndex();
        UIEditorPage page = UIEditorPage.get(checkIndex);
        if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
            mCurrentEditEffect.removePaster();
        }
        switch (page) {
        case COVER:
            isTakeFrameSelected = false;
            mPlayImage.setVisibility(VISIBLE);
            break;
        case AUDIO_MIX:
            playingResume();
            break;
        case PAINT:
            if (mCanvasController == null) {
                break;
            }
            //??????????????????
            mCanvasController.cancel();
            mCanvasController.applyPaintCanvas();
            mPasterContainer.removeView(mCanvasController.getCanvas());
            break;
        case SOUND:

            break;
        case FONT:
        case OVERLAY:
        case CAPTION:
            //?????????paster?????????????????????
            if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                mCurrentEditEffect.removePaster();
            }

            //???remove?????????????????????paster
            for (int i = 0; i < mPasterContainer.getChildCount(); i++) {
                View childAt = mPasterContainer.getChildAt(i);
                Object tag = childAt.getTag();
                if (tag == null || !(tag instanceof AbstractPasterUISimpleImpl)) {
                    continue;
                }
                AbstractPasterUISimpleImpl uiSimple = (AbstractPasterUISimpleImpl) tag;

                if (isPasterTypeHold(uiSimple.getEditorPage(), page)) {
                    // 1.Controller remove
                    // 2.pasterContainer remove
                    // 3.ThumbLBar remove
                    uiSimple.removePaster();
                    //????????????????????????????????????????????????????????????
                    i--;
                }

            }

            //???????????????????????????paster
            for (EffectBase effectBase : mPasterEffecCachetList) {
                AliyunPasterController pasterController;

                //???????????????controller???????????????????????????????????????????????????????????????????????????crash???
                if (effectBase instanceof EffectCaption && new File(effectBase.getPath()).exists()) {
                    EffectCaption effect = (EffectCaption) effectBase;
                    pasterController = mPasterManager.addPasterWithStartTime(effect.getPath(), effect.start,
                                       effect.end - effect.start);
                } else if (effectBase instanceof EffectText) {
                    EffectText effect = (EffectText) effectBase;
                    pasterController = mPasterManager.addSubtitleWithStartTime(effect.text, effect.font,
                                       effect.start, effect.end - effect.start);
                } else if (effectBase instanceof EffectPaster && new File(effectBase.getPath()).exists()) {
                    EffectPaster effect = (EffectPaster) effectBase;
                    pasterController = mPasterManager.addPasterWithStartTime(effect.getPath(), effect.start,
                                       effect.end - effect.start);
                } else {
                    continue;
                }
                pasterController.setEffect(effectBase);
                //??????????????????????????????effectBase??????????????????
                pasterController.setRevert(true);
                if (pasterController.getPasterType() == EffectPaster.PASTER_TYPE_GIF) {
                    mCurrentEditEffect = addPaster(pasterController);
                } else if (pasterController.getPasterType() == EffectPaster.PASTER_TYPE_TEXT) {
                    mCurrentEditEffect = addSubtitle(pasterController, true);
                } else if (pasterController.getPasterType() == EffectPaster.PASTER_TYPE_CAPTION) {
                    mCurrentEditEffect = addCaption(pasterController);
                }
                mCurrentEditEffect.showTimeEdit();
                mCurrentEditEffect.editTimeStart();
                mCurrentEditEffect.moveToCenter();
                mCurrentEditEffect.editTimeCompleted();
                pasterController.setRevert(false);
            }

            break;
        default:
            break;
        }

        mViewOperate.hideBottomView();
    }

    /**
     * ???????????????????????????
     */
    private void hideBottomEditorView() {

        int checkIndex = mTabGroup.getCheckedIndex();
        if (checkIndex == -1) {
            return;
        }
        UIEditorPage page = UIEditorPage.get(checkIndex);

        mViewOperate.hideBottomEditorView(page);

    }

    /**
     * ??????????????????UI??????????????????????????????????????????????????????
     *
     * @param animationFilters
     */
    @Override
    public void animationFilterRestored(final List<EffectFilter> animationFilters) {
        mPasterContainer.post(new Runnable() {
            @Override
            public void run() {
                mAnimationFilterController.setThumbLineBar(mThumbLineBar);
                if (mAnimationFilterController != null) {
                    mAnimationFilterController.restoreAnimationFilters(animationFilters);
                }
            }
        });
    }

    /**
     * ??????????????? ?????????paster????????????
     *
     * @param scaleSize ????????????
     */
    public void setPasterDisplayScale(float scaleSize) {
        mPasterManager.setDisplaySize((int) (mPasterContainerPoint.x * scaleSize),
                                      (int) (mPasterContainerPoint.y * scaleSize));
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        float mPosX;
        float mPosY;
        boolean shouldDrag = true;

        boolean shouldDrag() {
            return shouldDrag;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d(TAG, "onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(TAG, "onSingleTapConfirmed");

            if (!shouldDrag) {
                boolean outside = true;
                BaseChooser bottomView = null;
                if (mViewOperate != null) {
                    bottomView = mViewOperate.getBottomView();
                }
                if (bottomView != null) {

                    int count = mPasterContainer.getChildCount();
                    for (int i = count - 1; i >= 0; i--) {
                        View pv = mPasterContainer.getChildAt(i);
                        AbstractPasterUISimpleImpl uic = (AbstractPasterUISimpleImpl) pv.getTag();

                        if (uic != null && bottomView.isHostPaster(uic)) {
                            if (uic.isVisibleInTime(mAliyunIEditor.getCurrentStreamPosition())
                                    && uic.contentContains(e.getX(), e.getY())) {
                                outside = false;
                                if (mCurrentEditEffect != null && mCurrentEditEffect != uic && !mCurrentEditEffect
                                        .isEditCompleted()) {
                                    mCurrentEditEffect.editTimeCompleted();
                                }
                                mCurrentEditEffect = uic;
                                if (uic.isEditCompleted()) {
                                    playingPause();
                                    uic.editTimeStart();
                                }
                                break;
                            } else {
                                if (mCurrentEditEffect != uic && uic.isVisibleInTime(
                                            mAliyunIEditor.getCurrentStreamPosition())) {
                                    uic.editTimeCompleted();
                                }
                            }
                        }
                    }
                }

                if (outside) {
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                        mCurrentEditEffect.editTimeCompleted();
                    }
                    hideBottomEditorView();
                }
            } else {
                playingPause();
                mCurrentEditEffect.showTextEdit(mUseInvert);
            }
            //            if (mAliyunPasterController != null) {
            //                //??????????????????????????????
            //                ActionRotate actionRotate = new ActionRotate();
            //                actionRotate.setStartTime(0);
            //                actionRotate.setTargetId(mAliyunPasterController.getEffect().getViewId());
            //                actionRotate.setDuration(10 * 1000 * 1000);
            //                actionRotate.setRepeat(true);
            //                actionRotate.setDurationPerCircle(3 * 1000 * 1000);
            //                mAliyunIEditor.addFrameAnimation(actionRotate);
            //                if(mAliyunPasterController.getEffect() instanceof EffectCaption){
            //                    actionRotate = new ActionRotate();
            //                    actionRotate.setStartTime(0);
            //                    actionRotate.setDuration(10 * 1000 * 1000);
            //                    actionRotate.setRepeat(true);
            //                    actionRotate.setDurationPerCircle(3 * 1000 * 1000);
            //                    actionRotate.setTargetId(((EffectCaption) mAliyunPasterController.getEffect())
            // .gifViewId);
            //                    mAliyunIEditor.addFrameAnimation(actionRotate);
            //                }
            //            }
            return shouldDrag;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (shouldDrag()) {
                if (mPosX == 0 || mPosY == 0) {
                    mPosX = e1.getX();
                    mPosY = e1.getY();
                }
                float x = e2.getX();
                float y = e2.getY();

                mCurrentEditEffect.moveContent(x - mPosX, y - mPosY);

                mPosX = x;
                mPosY = y;

            } else {

            }

            return shouldDrag;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return shouldDrag;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown");
            if (mCurrentEditEffect != null && mCurrentEditEffect.isPasterRemoved()) {
                mCurrentEditEffect = null;
            }

            if (mCurrentEditEffect != null) {
                Log.d(TAG, "mCurrentEditEffect != null");
                shouldDrag = !mCurrentEditEffect.isEditCompleted()
                             && mCurrentEditEffect.contentContains(e.getX(), e.getY())
                             && mCurrentEditEffect.isVisibleInTime(mAliyunIEditor.getCurrentStreamPosition()

                                                                  );
            } else {
                shouldDrag = false;

            }

            mPosX = 0;
            mPosY = 0;
            return true;

        }
    }

    StringBuilder mDurationText = new StringBuilder(5);

    private String convertDuration2Text(long duration) {
        mDurationText.delete(0, mDurationText.length());
        float relSec = (float) duration / (1000 * 1000);// us -> s
        int min = (int) ((relSec % 3600) / 60);
        int sec = 0;
        sec = (int) (relSec % 60);
        if (min >= 10) {
            mDurationText.append(min);
        } else {
            mDurationText.append("0").append(min);
        }
        mDurationText.append(":");
        if (sec >= 10) {
            mDurationText.append(sec);
        } else {
            mDurationText.append("0").append(sec);
        }
        return mDurationText.toString();
    }

    private void copyAssets() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EditorCommon.copyAll(getContext(), resCopy);
            }
        });
    }

    public AliyunIEditor getEditor() {
        return this.mAliyunIEditor;
    }

    public void showMessage(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(id);
        builder.setNegativeButton(R.string.alivc_common_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * ???????????????????????????gop??????5???fps??????35 ???1080P??????????????????????????????????????????
     * @param filePath ????????????
     * @return true ?????????????????????????????????????????? otherwise ????????????
     */
    private boolean checkInvert(String filePath) {
        NativeParser parser = new NativeParser();
        if (parser.checkIfSupportedImage(filePath)) {
            parser.release();
            parser.dispose();
            return true;
        }
        parser.init(filePath);
        boolean gop = parser.getMaxGopSize() <= 5;
        boolean fps = false;
        try {
            fps = Float.parseFloat(parser.getValue(NativeParser.VIDEO_FPS)) <= 35;
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        parser.release();
        parser.dispose();
        return gop && fps;
    }

    /**
     * ???????????????????????????
     */
    private TransitionChooserView.OnPreviewListener mOnTransitionPreviewListener = new TransitionChooserView
    .OnPreviewListener() {
        @Override
        public void onPreview(int clipIndex, long leadTime, boolean isStop) {
            //????????????
            long advanceTime = 1000 * 1000;
            long clipStartTime = mAliyunIEditor.getClipStartTime(clipIndex + 1);
            advanceTime = clipStartTime - advanceTime >= 0 ? clipStartTime - advanceTime : 0;
            mAliyunIEditor.seek(advanceTime);
            playingResume();
            Log.d(TAG, "onTransitionPreview: index = " + clipIndex
                  + " ,clipStartTime = " + clipStartTime
                  + " ,duration = " + mAliyunIEditor.getDuration()
                  + " ,advanceTime = " + advanceTime
                 );
        }
    };

    private OnEffectActionLister mOnEffectActionLister = new OnEffectActionLister() {
        @Override
        public void onCancel() {
            clickCancel();
        }

        @Override
        public void onComplete() {
            clickConfirm();
        }
    };

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventColorFilterSelected(SelectColorFilter selectColorFilter) {
        EffectInfo effectInfo = selectColorFilter.getEffectInfo();
        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        mAliyunIEditor.applyFilter(effect);
    }

    /**
     * ???????????????????????????
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAnimationFilterLongClick(LongClickAnimationFilter filter) {
        if (!mUseAnimationFilter) {
            mUseAnimationFilter = true;
        }
        if (mCanAddAnimation) {
            playingResume();
        } else {
            playingPause();
        }

    }

    /**
     * ????????????????????????????????????
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAnimationFilterClickUp(LongClickUpAnimationFilter filter) {
        if (mUseAnimationFilter) {
            mUseAnimationFilter = false;
        }
        if (mAliyunIEditor.isPlaying()) {
            playingPause();

        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventFilterTabClick(FilterTabClick ft) {
        //??????????????????tab???????????????????????????????????????tab??????????????????
        if (mAliyunIEditor != null) {
            switch (ft.getPosition()) {
            case FilterTabClick.POSITION_ANIMATION_FILTER:
                if (mAliyunIEditor.isPlaying()) {
                    playingPause();
                }
                break;
            case FilterTabClick.POSITION_COLOR_FILTER:
                if (!mAliyunIEditor.isPlaying()) {
                    playingResume();
                }
                break;
            default:
                break;
            }
        }
    }

    private EditorCallBack mEditorCallback = new EditorCallBack() {
        @Override
        public void onEnd(int state) {

            post(new Runnable() {
                @Override
                public void run() {

                    if (!mUseAnimationFilter) {
                        //??????????????????????????????????????????????????????
                        mAliyunIEditor.replay();
                        mThumbLineBar.restart();
                    } else {
                        mCanAddAnimation = false;
                        switchPlayStateUI(true);

                    }

                }
            });
        }

        @Override
        public void onError(final int errorCode) {
            Log.e(TAG, "play error " + errorCode);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    switch (errorCode) {
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_WRONG_STATE:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_PROCESS_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_NO_FREE_DISK_SPACE:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CREATE_DECODE_GOP_TASK_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_AUDIO_STREAM_DECODER_INIT_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_VIDEO_STREAM_DECODER_INIT_FAILED:

                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_SPS_PPS_NULL:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_H264_PARAM_SET_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_HEVC_PARAM_SET_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_DECODER_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_STATE:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INPUT:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_NO_BUFFER_AVAILABLE:

                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_DECODE_SPS:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_CREATE_DECODER_FAILED:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_STATE:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_INPUT:
                    case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_NO_BUFFER_AVAILABLE:
                        showToast = FixedToastUtils.show(getContext(), errorCode + "");
                        ((Activity)getContext()).finish();
                        break;
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CACHE_DATA_SIZE_OVERFLOW:
                        showToast = FixedToastUtils.show(getContext(), errorCode + "");
                        mThumbLineBar.restart();
                        mAliyunIEditor.play();
                        break;
                    case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                        showToast = FixedToastUtils.show(getContext(),
                                                         getResources().getString(R.string.alivc_editor_error_tip_not_supported_audio));
                        ((Activity) getContext()).finish();
                        break;
                    case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                        showToast = FixedToastUtils.show(getContext(),
                                                         getResources().getString(R.string.alivc_editor_error_tip_not_supported_video));
                        ((Activity) getContext()).finish();
                        break;
                    case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_STREAM_NOT_EXISTS:
                    case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_PIXEL_FORMAT:
                        showToast = FixedToastUtils.show(getContext(),
                                                         getResources().getString(R.string.alivc_editor_error_tip_not_supported_pixel_format));
                        ((Activity) getContext()).finish();
                        break;
                    case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INTERRUPT:
                        showToast = FixedToastUtils.show(getContext(),
                                                         getResources().getString(R.string.alivc_editor_edit_tip_decoder_error_interrupt));
                        ((Activity) getContext()).finish();
                        break;
                    default:
                        showToast = FixedToastUtils.show(getContext(),
                                                         getResources().getString(R.string.alivc_editor_error_tip_play_video_error));
                        ((Activity) getContext()).finish();
                        break;
                    }
                }
            });

        }

        @Override
        public int onCustomRender(int srcTextureID, int width, int height) {
            return srcTextureID;
        }

        @Override
        public int onTextureRender(int srcTextureID, int width, int height) {
            if (isTakeFrame) {
                if (mSnapshop == null) {
                    mSnapshop = new AlivcSnapshot();
                }
                mSnapshop.useTextureIDGetFrame(srcTextureID, mSurfaceView, width, height, new File(PATH_THUMBNAIL));
                isTakeFrame = false;
            }

            return 0;
        }

        @Override
        public void onPlayProgress(final long currentPlayTime, final long currentStreamPlayTime) {
            post(new Runnable() {
                @Override
                public void run() {
                    long currentPlayTime = mAliyunIEditor.getCurrentPlayPosition();
                    if (mUseAnimationFilter
                            && mAliyunIEditor.getDuration() - currentPlayTime < USE_ANIMATION_REMAIN_TIME) {
                        mCanAddAnimation = false;
                    } else {
                        mCanAddAnimation = true;
                    }
                }
            });

        }

        private int c = 0;

        @Override
        public void onDataReady() {
            post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onDataReady received");
                    if (mWaitForReady && c > 0) {
                        Log.d(TAG, "onDataReady resume");
                        mWaitForReady = false;
                        mAliyunIEditor.resume();
                    }
                    c++;
                }
            });

        }
    };
    public static final int USE_ANIMATION_REMAIN_TIME = 300 * 1000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KEYCODE_VOLUME_DOWN:
            mVolume -= 5;
            if (mVolume < 0) {
                mVolume = 0;
            }
            mAliyunIEditor.setVolume(mVolume);
            return true;
        case KEYCODE_VOLUME_UP:
            mVolume += 5;
            if (mVolume > 100) {
                mVolume = 100;
            }
            mAliyunIEditor.setVolume(mVolume);
            return true;
        default:
            return super.onKeyDown(keyCode, event);
        }
    }

    private boolean isNeedResume = true;

    public void onStart() {
        mIsStop = false;
        if (mViewStack != null) {
            mViewStack.setVisibleStatus(true);
        }
    }

    public void onResume() {
        mTvRight.setEnabled(true);
        if (isNeedResume) {
            playingResume();
        }
        //??????????????????MV??????????????????????????????????????????????????????mv???????????????
        if (mLastMVEffect != null) {
            String path = EditorCommon.getMVPath(mLastMVEffect.list, mVideoParam.getOutputWidth(),
                                                 mVideoParam.getOutputHeight());

            if (!TextUtils.isEmpty(path) && !new File(path).exists()) {
                applyMVEffect(new EffectInfo());
            }
        }
        checkAndRemovePaster();
    }

    public void onPause() {
        isNeedResume = mAliyunIEditor.isPlaying();
        playingPause();
        mAliyunIEditor.saveEffectToLocal();
    }

    public void onStop() {

        if (mTvRight != null) {
            mTvRight.setEnabled(true);
        }
        mIsStop = true;
        if (mViewStack != null) {
            mViewStack.setVisibleStatus(false);
        }
        if (showToast != null) {
            showToast.cancel();
            showToast = null;
        }
    }

    public void onDestroy() {
        mIsDestroyed = true;
        Dispatcher.getInstance().unRegister(this);
        if (mAliyunIEditor != null) {
            mAliyunIEditor.onDestroy();
        }

        if (mAnimationFilterController != null) {
            mAnimationFilterController.destroyController();
        }

        if (mThumbLineBar != null) {
            mThumbLineBar.stop();
        }

        if (mThumbnailFetcher != null) {
            mThumbnailFetcher.release();
        }

        if (mCanvasController != null) {
            mCanvasController.release();
        }

        if (mTranscoder != null) {
            if (mIsTranscoding) {
                mTranscoder.cancel();
            } else {
                mTranscoder.dispose();
            }
        }

        if (mViewOperate != null) {
            mViewOperate.setAnimatorListener(null);
            mViewOperate = null;
        }

        if (animatorX != null) {
            animatorX.cancel();
            animatorX.addUpdateListener(null);
            animatorX.addListener(null);
            animatorX = null;
        }

        if (mWatermarkBitmap != null && !mWatermarkBitmap.isRecycled()) {
            mWatermarkBitmap.recycle();
            mWatermarkBitmap = null;
        }

        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewStack.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onBackPressed() {
        if (mIsTranscoding) {
            //???????????????????????????
            showToast = FixedToastUtils.show(getContext(),
                                             getResources().getString(R.string.alivc_editor_edit_tip_transcode_no_operate));
            return true;
        }
        if (mViewOperate != null) {
            boolean isShow = mViewOperate.isBottomViewShow();
            // ????????????
            if (isShow) {
                if (mViewOperate != null) {
                    mViewOperate.getBottomView().onBackPressed();
                    hideBottomEditorView();
                }
            }
            return isShow;
        } else {
            return false;
        }
    }

    private Uri mUri;
    private boolean hasTailAnimation = false;

    public void setParam(AliyunVideoParam mVideoParam, Uri mUri, boolean hasTailAnimation, boolean hasWaterMark) {
        this.hasTailAnimation = hasTailAnimation;
        this.mUri = mUri;
        this.mVideoParam = mVideoParam;
        this.hasWaterMark = false;
        initEditor();

    }

    public void setReplaceMusic(boolean replaceMusic) {
        isReplaceMusic = replaceMusic;
    }

    private AliyunVideoParam mVideoParam;

    /**
     * ???????????????????????????????????????
     */
    public interface PlayerListener {
        /**
         * ??????????????????????????????-->???????????????????????????
         *
         * @return ??????????????????
         */
        long getCurrDuration();

        /**
         * ?????????????????????
         *
         * @return ???????????????
         */
        long getDuration();

        /**
         * ???????????????-->?????????????????????
         *
         * @param duration ????????????
         */
        void updateDuration(long duration);
    }

    /**
     * ??????????????????????????????activity
     */
    private void jumpToNextActivity() {
        if (mOnFinishListener != null) {
            AlivcEditOutputParam outputParam = new AlivcEditOutputParam();
            outputParam.setConfigPath(mUri.getPath());
            outputParam.setOutputVideoHeight(mAliyunIEditor.getVideoHeight());
            outputParam.setOutputVideoWidth(mAliyunIEditor.getVideoWidth());
            outputParam.setVideoRatio(((float)mPasterContainerPoint.x) / mPasterContainerPoint.y);
            outputParam.setVideoParam(mVideoParam);
            outputParam.setThumbnailPath(PATH_THUMBNAIL);
            mOnFinishListener.onComplete(outputParam);
        }
    }

    public AlivcEditOutputParam getOutputParam() {
        AlivcEditOutputParam outputParam = new AlivcEditOutputParam();
        outputParam.setConfigPath(mUri.getPath());
        outputParam.setOutputVideoHeight(mAliyunIEditor.getVideoHeight());
        outputParam.setOutputVideoWidth(mAliyunIEditor.getVideoWidth());
        outputParam.setVideoRatio(((float)mPasterContainerPoint.x) / mPasterContainerPoint.y);
        outputParam.setVideoParam(mVideoParam);
        outputParam.setThumbnailPath(PATH_THUMBNAIL);
        return outputParam;
    }

    /**
     * ????????????????????????
     */
    public interface OnFinishListener {
        void onComplete(AlivcEditOutputParam outputParam);
    }
    private OnFinishListener mOnFinishListener;

    public OnFinishListener getOnFinishListener() {
        return mOnFinishListener;
    }

    public void setmOnFinishListener(OnFinishListener finishListener) {
        this.mOnFinishListener = finishListener;
    }

}

