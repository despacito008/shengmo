package com.aliyun.apsara.alivclittlevideo.view.publish;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.view.video.OnUploadCompleteListener;
import com.aliyun.common.utils.DensityUtil;

import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.common.utils.UriUtils;
import com.aliyun.svideo.common.utils.image.ImageLoaderImpl;
import com.aliyun.svideo.common.utils.image.ImageLoaderOptions;
import com.aliyun.svideo.common.widget.AlivcCustomAlertDialog;

;

;

public class AlivcVideoPublishView extends FrameLayout {
    /**
     * 时间戳错误
     */
    private static final String TEIM_STAMP_ERROR = "InvalidTimeStamp.Expired";
    private ProgressBar mProgressBar;
    private ImageView mIvThumbnail;
    private TextView mTvTip;
    /**
     * 封面图在屏幕中的宽度
     */
    private float mIvWidth = 67.5f;
    /**
     * 封面图在屏幕中的高度
     */
    private float mIvHeight = 120f;
    /**
     * 合成视频输出文件路径
     */
    private String mOutputFilePath;

    public AlivcVideoPublishView(@NonNull Context context) {
        super(context);
        initView();
    }

    public AlivcVideoPublishView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlivcVideoPublishView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.vertical_progress_bar));
        mProgressBar.setProgress(100);
        mIvThumbnail = new ImageView(getContext());
        mIvThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvThumbnail.setBackgroundColor(getResources().getColor(R.color.alivc_common_bg_black_alpha_80));
        mTvTip = new TextView(getContext());
        mTvTip.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mTvTip.setText(R.string.alivc_little_main_publish);
        mTvTip.setPadding(0, 0, 0, DensityUtil.dip2px(getContext(), 8 ));
        mTvTip.setTextColor(getResources().getColor(R.color.alivc_common_font_white));
        mTvTip.setTextSize(12);
    }

    public void showSuccess() {
        Log.d("UploadView", "showSuccess");
        //

        post(new Runnable() {
            @Override
            public void run() {
                clearUploadProgress();
                TextView textView = new TextView(getContext());
                textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.alivc_common_bg_black_alpha_80));
                textView.setHeight(DensityUtil.dip2px(getContext(), 40));
                textView.setWidth(DensityUtil.dip2px(getContext(), 180));
                textView.setText(R.string.alivc_little_main_tip_upload_success);
                textView.setGravity(Gravity.CENTER);
                Toast toast = new Toast(getContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 70);
                toast.setView(textView);
                toast.show();
            }
        });
    }

    public void showFailed(final String message) {

        post(new Runnable() {
            @Override
            public void run() {
                clearUploadProgress();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProgress(final int progress) {
        post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(100 - progress);
            }
        });
    }

    public void showProgress(String filePath) {
        new ImageLoaderImpl().loadImage(getContext(), filePath, new ImageLoaderOptions.Builder()
                                        //上传显示加载本地图片，路径可能会相同，这里需要跳过缓存
                                        .skipDiskCacheCache()
                                        .skipMemoryCache()
                                        .build()
                                       ).into(mIvThumbnail);
        LayoutParams layoutParams = new LayoutParams(DensityUtil.dip2px(getContext(), mIvWidth),
                DensityUtil.dip2px(getContext(), mIvHeight));
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight1 = DensityUtil.dip2px(getContext(), 24);
        }
        layoutParams.setMargins(DensityUtil.dip2px(getContext(), 20), statusBarHeight1, 0, 0);
        if (mIvThumbnail.getParent() == null) {
            addView(mIvThumbnail, layoutParams);

        }
        if (mProgressBar.getParent() == null) {
            addView(mProgressBar, layoutParams);
        }
        if (mTvTip.getParent() == null) {
            addView(mTvTip, layoutParams);
        }
    }

    public void clearUploadProgress() {
        removeView(mProgressBar);
        removeView(mIvThumbnail);
        removeView(mTvTip);
        mProgressBar.setProgress(100);
    }
    /**
     * 合成、上传管理类
     */
    private PublishManager mPublishManager;

    /**
     * 开始上传
     */
    public void startUpload(String config,
                            String outputFilePath,
                            String videoId,
                            String videoAddress,
                            String videoAuth,
                            String videoDescribe,
                            String thumbnailPath,
                            String imageId,
                            String imageUrl,
                            String imageAddress,
                            String imageAuth) {
        this.mOutputFilePath = outputFilePath;

        if (mPublishManager != null) {
            mPublishManager.releaseCompose();
        }
        mPublishManager = new PublishManager(getContext());
        mPublishManager.startComposeAndUpload(config,
                                              outputFilePath,
                                              videoId,
                                              videoAddress,
                                              videoAuth,
                                              videoDescribe,
                                              thumbnailPath,
                                              imageId,
                                              imageUrl,
                                              imageAddress,
                                              imageAuth,
                                              mUploadCallback);
    }
    private PublishManager.MyComposeListener mUploadCallback = new PublishManager.MyComposeListener() {

        @Override
        public void onComposeStart() {
            showProgress(mPublishManager.getThumbnail());
        }

        @Override
        public void onComposeProgress(int i) {
            updateProgress(i / 2);
        }

        @Override
        public void onComposeCompleted() {
            if(!TextUtils.isEmpty(mOutputFilePath)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //适配android Q
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            UriUtils.saveVideoToMediaStore(getContext().getApplicationContext(), mOutputFilePath);
                        }
                    });
                } else {
                    MediaScannerConnection.scanFile(getContext().getApplicationContext(),
                            new String[] {mOutputFilePath}, new String[] {"video/mp4"}, null);
                }
            }
        }

        @Override
        public void onComposeError(int i) {
            if (mOutOnUploadCompleteListener != null) {
                mOutOnUploadCompleteListener.onFailure(i + "", getResources().getString(R.string.alivc_little_main_creation_failed));
            }
        }

        @Override
        public void onUploadStart() {

        }

        @Override
        public void onUploadSucceed(String videoId, String imageUrl, String describe) {
            if (mOutOnUploadCompleteListener != null) {
                mOutOnUploadCompleteListener.onSuccess(videoId, imageUrl, describe);
            }
            if (mPublishManager != null) {
                mPublishManager.releaseCompose();
                mPublishManager = null;
            }
        }

        @Override
        public void onUploadFailed(String code, String message, boolean isImageUpload) {
            if (TEIM_STAMP_ERROR.equals(code)) {
                if (mOutOnUploadCompleteListener != null) {
                    mOutOnUploadCompleteListener.onFailure(code, message);
                }
                return;
            }
            showRetryDialog(code, message );
            if (mPublishManager != null) {
                mPublishManager.releaseCompose();
                mPublishManager = null;
            }
        }

        @Override
        public void onUploadProgress(long uploadedSize, long totalSize) {
            int progress = (int)((uploadedSize * 100) / totalSize) + 50;
            Log.e("onUploadProgress", "progress" + progress);
            updateProgress(progress);
        }

        @Override
        public void onUploadAuthExpired(boolean isImageUpload, String videoId) {
            if (mAuthInfoExpiredListener != null) {
                if (isImageUpload) {
                    mAuthInfoExpiredListener.onImageAuthInfoExpired();
                } else {
                    mAuthInfoExpiredListener.onVideoAuthInfoExpired(videoId);
                }
            }
        }

    };
    public void showRetryDialog(final String code, final String message) {
        post(new Runnable() {
            @Override
            public void run() {
                clearUploadProgress();
                AlivcCustomAlertDialog dialog = new AlivcCustomAlertDialog.Builder(getContext())
                .setMessage(message + "," + getResources().getString(R.string.alivc_little_main_dialog_upload_failed))
                .setDialogClickListener(getResources().getString(R.string.alivc_little_main_dialog_retry), getResources().getString(R.string.alivc_little_main_dialog_close), new AlivcCustomAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        mPublishManager.retryTask();
                        showProgress(mPublishManager.getThumbnail());
                    }

                    @Override
                    public void onCancel() {
                        if (mOutOnUploadCompleteListener != null) {
                            mOutOnUploadCompleteListener.onFailure(code, message);
                        }
                    }
                })
                .create();
                dialog.setCancelable(false);
                dialog.show();

            }
        });
    }

    /**
     * 视频、图片上传凭证过期监听
     */
    private OnAuthInfoExpiredListener mAuthInfoExpiredListener;
    public void setOnAuthInfoExpiredListener(OnAuthInfoExpiredListener authInfoExpiredListener) {
        mAuthInfoExpiredListener = authInfoExpiredListener;
    }
    /**
     * 刷新视频上传凭证
     * @param imageId 图片id
     * @param imageUrl 图片访问url
     * @param imageAddress 图片上传地址
     * @param imageAuth 图片上传凭证
     */
    public void refreshImageUploadAuthInfo(String imageId,
                                           String imageUrl,
                                           String imageAddress,
                                           String imageAuth) {
        if (mPublishManager != null) {
            mPublishManager.refreshImageAuth(imageId, imageUrl, imageAddress, imageAuth);
        }

    }
    /**
     * 刷新视频上传凭证
     * @param videoAddress 视频上传地址
     * @param videoAuth 视频上传凭证
     */
    public void refreshVideoAuth(String videoAddress,
                                 String videoAuth) {
        if (mPublishManager != null) {
            mPublishManager.refreshVideoAuth(videoAddress, videoAuth);
        }
    }
    public void onDestroy() {
        if (mPublishManager != null) {
            mPublishManager.releaseCompose();
        }
    }
    private OnUploadCompleteListener mOutOnUploadCompleteListener;
    /**
     * 设置上传完成的listener
     *
     * @param listener OnUploadCompleteListener
     */
    public void setOnUploadCompleteListener(OnUploadCompleteListener listener) {
        this.mOutOnUploadCompleteListener = listener;
    }

    /**
     * 是否正在发布过程中
     * @return
     */
    public boolean isPublishing() {
        if (mPublishManager == null) {
            return false;
        } else {
            return mPublishManager.isPublishing();
        }
    }
}
