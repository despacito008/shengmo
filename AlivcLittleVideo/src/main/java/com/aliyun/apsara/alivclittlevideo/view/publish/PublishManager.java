package com.aliyun.apsara.alivclittlevideo.view.publish;

import android.content.Context;
import android.util.Log;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.qupai.editor.AliyunIComposeCallBack;
import com.aliyun.qupai.editor.AliyunIVodCompose;
import com.aliyun.qupai.editor.impl.AliyunComposeFactory;

import java.io.File;

/**
 * @author zsy_18 data:2018/12/29
 * 对合成上传进行封装，仅支持单任务，不支持多任务同时进行
 */
public class PublishManager {

    private static final String TAG = "PublishManager";
    /**
     * 合成、上传核心类
     */
    private AliyunIVodCompose mCompose;
    /**
     * 配置文件路径
     */
    private String mConfigPath;
    /**
     * 合成输出地址
     */
    private String mOutputFilePath;
    /**
     * 视频id
     */
    private String mVideoId;
    /**
     * 视频上传地址
     */
    private String mVideoAddress;
    /**
     * 视频上传凭证
     */
    private String mVideoAuth;
    /**
     * 视频描述
     */
    private String mVideoDescribe;
    /**
     * 封面本地路径
     */
    private String mThumbnailPath;
    /**
     * 封面id
     */
    private String mImageId;
    /**
     * 封面访问地址
     */
    private String mImageUrl;
    /**
     * 封面上传地址
     */
    private String mImageAddress;
    /**
     * 封面上传凭证
     */
    private String mImageAuth;

    /**
     * 图片文件大小
     */
    private long mImageFileSize;
    /**
     * 视频文件大小
     */
    private long mVideoFileSize;
    /**
     * 任务状态
     */
    private ComposeStatus mComposeStatus = ComposeStatus.NONE;
    private MyComposeListener mComposeListener;
    private Context mContext;

    public PublishManager(Context context) {
        this.mContext = context;
        this.mCompose = AliyunComposeFactory.createAliyunVodCompose();
        mCompose.init(context);
    }

    /**
     * 开始任务
     */
    public void startComposeAndUpload(String config,
                                      String outputFilePath,
                                      String videoId,
                                      String videoAddress,
                                      String videoAuth,
                                      String videoDescribe,
                                      String thumbnailPath,
                                      String imageId,
                                      String imageUrl,
                                      String imageAddress,
                                      String imageAuth,
                                      MyComposeListener composeListener) {
        cancel();
        mConfigPath = config;
        mOutputFilePath = outputFilePath;
        mVideoId = videoId;
        mVideoAddress = videoAddress;
        mVideoAuth = videoAuth;
        mVideoDescribe = videoDescribe;
        mThumbnailPath = thumbnailPath;
        //获取图片文件大小
        File imageFile = new File(mThumbnailPath);
        if (imageFile.exists()) {
            mImageFileSize = imageFile.length();
        }

        mImageId = imageId;
        mImageUrl = imageUrl;
        mImageAddress = imageAddress;
        mImageAuth = imageAuth;
        mComposeListener = composeListener;
        startCompose();
    }

    /**
     * 合成、上传失败后，重新尝试任务。
     */
    public void retryTask() {
        switch (mComposeStatus) {
        case NONE:
            startCompose();
            break;
        case COMPOSE_ERROR:
            startCompose();
            break;
        case IMAGE_UPLOAD_FAILED:
            startImageUpload();
            break;
        case VIDEO_UPLOAD_FAILED:
            startVideoUpload();
            break;
        default:
            break;
        }
    }

    /**
     * 取消任务
     */
    public void cancel() {
        switch (mComposeStatus) {
        case COMPOSE:
            mCompose.cancelCompose();
            break;
        case IMAGE_UPLOADING:
            mCompose.cancelUpload();
            break;
        default:
            break;
        }
        mComposeStatus = ComposeStatus.NONE;
    }

    /**
     * 开始合成
     */
    private void startCompose() {
        //任务开始，状态置为合成中
        mComposeStatus = ComposeStatus.COMPOSE;
        if (mComposeListener != null) {
            mComposeListener.onComposeStart();
        }
        mCompose.compose(mConfigPath, mOutputFilePath, new AliyunIComposeCallBack() {
            @Override
            public void onComposeError(int i) {
                //合成失败
                mComposeStatus = ComposeStatus.COMPOSE_ERROR;
                if (mComposeListener != null) {
                    mComposeListener.onComposeError(i);
                }
            }

            @Override
            public void onComposeProgress(int i) {
                Log.e(TAG, "onComposeProgress" );
                if (mComposeListener != null) {
                    mComposeListener.onComposeProgress(i);
                }
            }

            @Override
            public void onComposeCompleted() {
                if (mComposeListener != null) {
                    mComposeListener.onComposeCompleted();
                }
                //获取合成文件大小
                File videoFile = new File(mOutputFilePath);
                if (videoFile.exists()) {
                    mVideoFileSize = videoFile.length();
                }
                startImageUpload();
            }
        });
    }

    /**
     * 开始上传封面
     */
    private void startImageUpload() {
        mComposeStatus = ComposeStatus.IMAGE_UPLOADING;
        //上传封面
        int code = mCompose.uploadImageWithVod(
            //封面本地路径
            mThumbnailPath,
            //封面上传地址
            mImageAddress,
            //上传凭证
            mImageAuth,
            //进度回调
            aliyunIVodUploadCallBack );
        if(code!=0&&mComposeListener!=null){
            mComposeListener.onUploadFailed(""+code, mContext.getResources().getString(R.string.alivc_little_main_picture_upload_error), true);
            mComposeStatus = ComposeStatus.IMAGE_UPLOAD_FAILED;
        }

    }
    private void startVideoUpload() {
        mComposeStatus = ComposeStatus.VIDEO_UPLOADING;
        //上传视频
        int code = mCompose.uploadVideoWithVod(
            //视频本地地址
            mOutputFilePath,
            //视频上传地址
            mVideoAddress,
            //上传凭证
            mVideoAuth,
            //进度回调
            aliyunIVodUploadCallBack);
        if(code!=0&&mComposeListener!=null){
            mComposeListener.onUploadFailed(""+code, mContext.getResources().getString(R.string.alivc_little_main_video_upload_error), false);
            mComposeStatus = ComposeStatus.VIDEO_UPLOAD_FAILED;
        }

    }
    /**
     * 清除mCompose
     */
    public void releaseCompose() {
        cancel();
        mCompose.release();
    }
    AliyunIVodCompose.AliyunIVodUploadCallBack aliyunIVodUploadCallBack = new AliyunIVodCompose.AliyunIVodUploadCallBack() {


        @Override
        public void onUploadFailed(String s, String s1) {
            boolean isImageUpload;
            if (mCompose.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                isImageUpload = true;
                mComposeStatus = ComposeStatus.IMAGE_UPLOAD_FAILED;
            } else {
                isImageUpload = false;
                mComposeStatus = ComposeStatus.VIDEO_UPLOAD_FAILED;
            }
            if (mComposeListener != null) {
                if ("Http.Abnormal".equals(s)) {
                    mComposeListener.onUploadFailed(s, mContext.getResources().getString(R.string.alivc_little_main_net_error), isImageUpload);
                } else {
                    mComposeListener.onUploadFailed(s, s1, isImageUpload);
                }

            }
        }
        @Override
        public void onUploadProgress(long l, long l1) {
            if (mCompose.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                mImageFileSize = l1;
                if (mComposeListener != null) {
                    mComposeListener.onUploadProgress(l, mImageFileSize + mVideoFileSize);
                }
            } else {
                mVideoFileSize = l1;
                if (mComposeListener != null) {
                    mComposeListener.onUploadProgress(l + mImageFileSize, mImageFileSize + mVideoFileSize);
                }
            }
        }

        @Override
        public void onUploadRetry(String s, String s1) {

        }

        @Override
        public void onUploadRetryResume() {

        }

        @Override
        public void onUploadSucceed() {
            if (mCompose.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                startVideoUpload();
            } else {
                mComposeStatus = ComposeStatus.NONE;
                if (mComposeListener != null) {
                    mComposeListener.onUploadSucceed(mVideoId, mImageUrl, mVideoDescribe);
                }
            }
        }

        @Override
        public void onUploadTokenExpired() {
            boolean isImageUpload;
            if (mCompose.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                isImageUpload = true;
                mComposeStatus = ComposeStatus.IMAGE_AUTH_EXPIRED;
            } else {
                isImageUpload = false;
                mComposeStatus = ComposeStatus.VIDEO_AUTH_EXPIRED;
            }
            if (mComposeListener != null) {
                mComposeListener.onUploadAuthExpired(isImageUpload, mVideoId);
            }
        }
    };
    /**
     * 刷新视频上传凭证
     * @param imageId 图片id
     * @param imageUrl 图片访问url
     * @param imageAddress 图片上传地址
     * @param imageAuth 图片上传凭证
     */
    public void refreshImageAuth(String imageId,
                                 String imageUrl,
                                 String imageAddress,
                                 String imageAuth) {
        mImageId = imageId;
        mImageUrl = imageUrl;
        mImageAddress = imageAddress;
        mImageAuth = imageAuth;
        if (mComposeStatus == ComposeStatus.IMAGE_UPLOADING) {
            startImageUpload();
        }
    }
    /**
     * 刷新视频上传凭证
     * @param videoAddress 视频上传地址
     * @param videoAuth 视频上传凭证
     */
    public void refreshVideoAuth(String videoAddress,
                                 String videoAuth) {
        mVideoAddress = videoAddress;
        mVideoAuth = videoAuth;
        if (mComposeStatus == ComposeStatus.VIDEO_UPLOADING) {
            mComposeStatus = ComposeStatus.VIDEO_UPLOADING;
            mCompose.refreshWithUploadAuth(videoAuth);
        }
    }
    public interface MyComposeListener {
        /**
         * 开始合成任务
         */
        void onComposeStart();

        /**
         * 合成中
         *
         * @param i
         */
        void onComposeProgress(int i);

        /**
         * 合成完成
         */
        void onComposeCompleted();

        /**
         * 合成失败
         *
         * @param i
         */
        void onComposeError(int i);

        /**
         * 开始上传
         */
        void onUploadStart();

        /**
         * 上传成功
         *
         * @param videoId  视频id
         * @param imageUrl 封面地址
         */
        void onUploadSucceed(String videoId, String imageUrl, String describe);

        /**
         * 上传失败
         *
         * @param code
         * @param message
         */
        void onUploadFailed(String code, String message, boolean isImageUpload);

        /**
         * 上传进度
         *
         * @param uploadedSize
         * @param totalSize
         */
        void onUploadProgress(long uploadedSize, long totalSize);

        /**
         * stsToken过期，外部重新获取sts并调用
         */
        /**
         * 上传凭证过期
         * @param isImageUpload false:视频凭证过期，true图片凭证过期
         * @param videoId 视频id
         */
        void onUploadAuthExpired(boolean isImageUpload, String videoId);


    }

    enum ComposeStatus {
        /**
         * 暂无任务
         */
        NONE,
        /**
         * 合成中
         */
        COMPOSE,
        /**
         * 合成失败
         */
        COMPOSE_ERROR,
        /**
         * 图片上传中
         */
        IMAGE_UPLOADING,
        /**
         * 图片上传失败
         */
        IMAGE_UPLOAD_FAILED,
        /**
         * 图片凭证过期
         */
        IMAGE_AUTH_EXPIRED,
        /**
         * 视频上传中
         */
        VIDEO_UPLOADING,
        /**
         * 视频上传失败
         */
        VIDEO_UPLOAD_FAILED,
        /**
         * 视频凭证过期
         */
        VIDEO_AUTH_EXPIRED

    }

    public String getThumbnail() {
        return mThumbnailPath;
    }

    /**
     * 是否正在发布过程中
     * @return
     */
    public boolean isPublishing() {
        if (mComposeStatus == ComposeStatus.COMPOSE || mComposeStatus == ComposeStatus.IMAGE_UPLOADING || mComposeStatus == ComposeStatus.VIDEO_UPLOADING) {
            return true;
        } else {
            return false;
        }
    }

}
