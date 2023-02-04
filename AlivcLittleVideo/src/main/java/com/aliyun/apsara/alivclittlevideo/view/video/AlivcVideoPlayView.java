package com.aliyun.apsara.alivclittlevideo.view.video;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.constants.LittleVideoParamConfig;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo;
import com.aliyun.apsara.alivclittlevideo.sts.StsTokenInfo;
import com.aliyun.apsara.alivclittlevideo.view.ShareDialog;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.AlivcVideoListView;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.IVideoSourceModel;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.OnTimeExpiredErrorListener;
import com.aliyun.common.utils.DensityUtil;
import com.aliyun.downloader.AliDownloaderFactory;
import com.aliyun.downloader.AliMediaDownloader;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.nativeclass.MediaInfo;
import com.aliyun.player.nativeclass.TrackInfo;
import com.aliyun.player.source.StsInfo;
import com.aliyun.player.source.VidSts;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.base.widget.CircleProgressBar;

import com.aliyun.svideo.common.utils.ToastUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo.VideoListBean.STATUS_CENSOR_FAIL;
import static com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo.VideoListBean.STATUS_CENSOR_ON;
import static com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo.VideoListBean.STATUS_CENSOR_WAIT;


/**
 * 播放界面, 负责initPlayerSDK以及各种view
 *
 * @author xlx
 */
public class AlivcVideoPlayView extends FrameLayout {


    private static final String TAG = "AlivcVideoPlayView";
    private Context context;
    private AlivcVideoListView videoListView;

    /**
     * 刷新数据listener (下拉刷新和上拉加载)
     */
    private AlivcVideoListView.OnRefreshDataListener onRefreshDataListener;
    /**
     * 视频缓冲加载view
     */
    private LoadingView mLoadingView;


    /**
     * 分享选择提示窗
     *
     * @param context
     */

    private ShareDialog mShareDialog;



    /**
     * 视频删除点击事件
     */
    private OnVideoDeleteListener mOutOnVideoDeleteListener;
    private LittleVideoListAdapter mVideoAdapter;

    public AlivcVideoPlayView(@NonNull Context context) {
        this(context, null);
    }

    public AlivcVideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        initPlayListView();
        initLoadingView();
    }
    private Dialog mDownloadDialog;
    private CircleProgressBar mProgressBar;
    private TextView mTvProgress;
    private FrameLayout mDownloadContent;

    private void showDownloadDialog() {
        if (mDownloadDialog == null) {
            mDownloadDialog = new Dialog(getContext(), com.aliyun.svideo.common.R.style.CustomDialogStyle);



            View view = View.inflate(context, R.layout.alivc_little_dialog_progress, null);
            mProgressBar = view.findViewById(R.id.alivc_little_progress);
            mProgressBar.setProgress(0);
            mTvProgress = view.findViewById(R.id.alivc_little_tv_progress);
            mTvProgress.setText("0%");
            mDownloadContent = view.findViewById(R.id.alivc_tittle_fl_download_content);
            view.findViewById(R.id.alivc_little_iv_cancel_download).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDownloadProgress();

                    if (mDownloadManager != null) {
                        mDownloadManager.stop();
                    }
                }
            });
            mDownloadDialog.setCancelable(false);
            mDownloadDialog.setContentView(view);
            Window dialogWindow = mDownloadDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
            lp.width = d.widthPixels; // 高度设置为屏幕的高
            lp.height = d.heightPixels; // 高度设置为屏幕的宽
            lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            dialogWindow.setAttributes(lp);
        }
        mDownloadDialog.show();

    }



    /**
     * 下载sdk核心类
     */
    private AliMediaDownloader mDownloadManager;

    private int mClickPosition;

    /**
     * 初始化视频列表
     */
    private void initPlayListView() {
        videoListView = new AlivcVideoListView(context);
        //创建adapter，需要继承BaseVideoListAdapter
        mVideoAdapter = new LittleVideoListAdapter(context);
        mVideoAdapter.setItemBtnClick(new LittleVideoListAdapter.OnItemBtnClick() {
            @Override
            public void onDownloadClick(int position) {
                mClickPosition = position;

                if (mShareDialog == null) {
                    mShareDialog = new ShareDialog();
                    mShareDialog.setItemSelectedListenr(new ShareDialog.OnItemSelectedListener() {
                        @Override
                        public void onDownloadClick() {
                            // 2018/12/3 开始下载
                            BaseVideoSourceModel video = mVideoAdapter.getDataList().get(mClickPosition);
                            if (video instanceof LittleMineVideoInfo.VideoListBean) {
                                if (STATUS_CENSOR_ON.equals(((LittleMineVideoInfo.VideoListBean)video).getCensorStatus()) || STATUS_CENSOR_WAIT.equals(((LittleMineVideoInfo.VideoListBean)video).getCensorStatus())) {
                                    ToastUtils.show(context, getResources().getString(R.string.alivc_little_play_tip_reviewing));
                                } else if (STATUS_CENSOR_FAIL.equals(((LittleMineVideoInfo.VideoListBean)video).getCensorStatus())) {
                                    ToastUtils.show(context, getResources().getString(R.string.alivc_little_play_tip_not_pass));
                                } else {
                                    //每次下载都调用该方法
                                    if (mDownloadManager == null) {
                                        initDownloadManager();
                                    }
                                    VidSts vidSts = video.getVidStsSource();
                                    vidSts.setRegion("cn-shanghai");
                                    mDownloadManager.prepare(vidSts);
                                }
                            }

                        }

                        @Override
                        public void onDeleteClick() {
                            if (mOutOnVideoDeleteListener != null) {
                                List<BaseVideoSourceModel> dataList = mVideoAdapter.getDataList();
                                BaseVideoSourceModel video = mVideoAdapter.getDataList().get(mClickPosition);
                                if (video instanceof LittleMineVideoInfo.VideoListBean) {
                                    if (dataList != null && dataList.size() > 0 && mClickPosition >= 0
                                            && mClickPosition < dataList.size()) {
                                        mOutOnVideoDeleteListener.onDeleteClick(
                                            (LittleMineVideoInfo.VideoListBean)video);
                                    }
                                }

                            }
                        }

                    });
                }
                BaseVideoSourceModel video = mVideoAdapter.getDataList().get(mClickPosition);
                mShareDialog.show(getFragmentManager(), "ShareDialog");
                if (video.getUser().getUserId().equals(AlivcLittleUserManager.getInstance().getUserInfo(getContext()).getUserId())) {
                    mShareDialog.setDeleteVisible(true);
                } else {
                    mShareDialog.setDeleteVisible(false);
                }

            }
        });
        //给AlivcVideoListView实例化对象添加adapter
        videoListView.setAdapter(mVideoAdapter);
        videoListView.setVisibility(VISIBLE);
        //设置sdk播放器实例化对象数量
        videoListView.setPlayerCount(1);
        //设置下拉、上拉监听进行加载数据
        videoListView.setOnRefreshDataListener(new AlivcVideoListView.OnRefreshDataListener() {
            @Override
            public void onRefresh() {
                if (onRefreshDataListener != null) {
                    onRefreshDataListener.onRefresh();
                }
            }

            @Override
            public void onLoadMore() {
                if (onRefreshDataListener != null) {
                    onRefreshDataListener.onLoadMore();
                }
            }
        });
        //设置视频缓冲监听
        videoListView.setLoadingListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                mLoadingView.start();
            }

            @Override
            public void onLoadingEnd() {
                mLoadingView.cancle();
            }

            @Override
            public void onLoadingProgress(int var1, float var2) {

            }
        });
        //设置鉴权过期监听，刷新鉴权信息
        videoListView.setTimeExpiredErrorListener(new OnTimeExpiredErrorListener() {

            @Override
            public void onTimeExpiredError() {
                if (mStsInfoExpiredListener != null) {
                    mStsInfoExpiredListener.onTimeExpired();
                }
            }
        });
        //添加到布局中
        addSubView(videoListView);
    }

    /**
     * 播放、下载、上传过程中鉴权过期时提供的回调消息
     */
    private OnStsInfoExpiredListener mStsInfoExpiredListener;

    public void setOnStsInfoExpiredListener(
        OnStsInfoExpiredListener mTimeExpiredErrorListener) {
        this.mStsInfoExpiredListener = mTimeExpiredErrorListener;
    }

    /**
     * 初始化下载管理器
     */
    private void initDownloadManager() {
        mDownloadManager = AliDownloaderFactory.create(context);


        mDownloadManager.setOnPreparedListener(new AliMediaDownloader.OnPreparedListener() {
            @Override
            public void onPrepared(MediaInfo mediaInfo) {
                List<TrackInfo> mTrackInfo = mediaInfo.getTrackInfos();
                if (mTrackInfo == null || mTrackInfo.size() == 0) {
                    Toast.makeText(context, getResources().getString(R.string.alivc_little_play_tip_resource_none), Toast.LENGTH_SHORT).show();
                    dismissDownloadProgress();
                    return;
                }
                mDownloadManager.selectItem(pickDownloadQualityMedia(mTrackInfo));
                //File videoFile = new File(mDownloadManager.getFilePath());
                //if (videoFile.exists()) {
                //    Toast.makeText(context, "视频已存在", Toast.LENGTH_SHORT).show();
                //    dismissDownloadProgress();
                //    return;
                //}
                mDownloadManager.start();
                showDownloadDialog();
            }
        });
        mDownloadManager.setOnProgressListener(new AliMediaDownloader.OnProgressListener() {
            @Override
            public void onDownloadingProgress(int i) {
                Log.d(TAG,  "onDownloadingProgress:" + i);
                mTvProgress.setText(i + "%");
                mProgressBar.setProgress(i);
            }

            @Override
            public void onProcessingProgress(int i) {
                Log.d(TAG,  "onProcessingProgress:" + i);
            }
        });
        mDownloadManager.setOnCompletionListener(new AliMediaDownloader.OnCompletionListener() {
            @Override
            public void onCompletion() {
                Log.d(TAG, "onCompletion");
                if (context != null) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        saveVideoToMediaStore(context,mDownloadManager.getFilePath());
                    }else{
                        MediaScannerConnection.scanFile(context, new String[] {mDownloadManager.getFilePath()},
                                new String[] {"video/*"},
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                    }
                                });
                    }

                    dismissDownloadProgress();
                }

                Toast.makeText(context, getResources().getString(R.string.alivc_little_play_tip_downloaded), Toast.LENGTH_SHORT).show();
            }
        });
        mDownloadManager.setOnErrorListener(new AliMediaDownloader.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {

                dismissDownloadProgress();
                Toast.makeText(context, errorInfo.getMsg(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError" + mDownloadManager.getFilePath() + errorInfo.getMsg());
            }
        });

        //设置保存路径。请确保有SD卡访问权限。
        File file = new File(Constants.SDCardConstants.getDir(getContext()) + LittleVideoParamConfig.DIR_DOWNLOAD);
        if (!file.exists()) {
            file.mkdir();
        }
        mDownloadManager.setSaveDir(file.getAbsolutePath());
    }

    private void dismissDownloadProgress() {
        if (mDownloadDialog != null) {
            mDownloadDialog.dismiss();
            mTvProgress.setText("0%");
            mProgressBar.setProgress(0);
        }
    }

    /**
     * AndroidQ 保存视频到媒体库
     * @param context       Context
     * @param fileName      要刷新的视频路径
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void saveVideoToMediaStore(Context context, String fileName) {

        long startTime = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        String name = startTime + "-little-download-video.mp4";
        values.put(MediaStore.Video.Media.DISPLAY_NAME, name);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.IS_PENDING, 1);

        ContentResolver resolver = context.getContentResolver();
        Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri item = resolver.insert(collection, values);

        try (ParcelFileDescriptor pfd = resolver.openFileDescriptor(item, "w", null)) {
            // Write data into the pending video.
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
            ParcelFileDescriptor.AutoCloseOutputStream outputStream = new ParcelFileDescriptor.AutoCloseOutputStream(pfd);
            BufferedOutputStream bot = new BufferedOutputStream(outputStream);
            byte[] bt = new byte[2048];
            int len;
            while ((len = bin.read(bt)) >= 0) {
                bot.write(bt, 0, len);
                bot.flush();
            }
            bin.close();
            bot.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        values.clear();
        values.put(MediaStore.Video.Media.IS_PENDING, 0);
        resolver.update(item, values, null, null);
        //打印写入时间
        Log.i(TAG, "duration : " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 获取视频质量最高的视频
     *
     * @param list
     * @return
     */
    private int pickDownloadQualityMedia(List<TrackInfo> list) {
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            TrackInfo trackInfo = list.get(i);
            if ("LD".equals(trackInfo.getVodDefinition())) {
                index = trackInfo.getIndex();
            }
        }
        return index;
    }

    private void initLoadingView() {
        mLoadingView = new LoadingView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                5);
        params.setMargins(0, 0, 0, DensityUtil.dip2px(getContext(), 4));
        params.gravity = Gravity.BOTTOM;
        addView(mLoadingView, params);
    }

    /**
     * addSubView 添加子view到布局中
     *
     * @param view 子view
     */
    private void addSubView(View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        addView(view, params);
    }

    /**
     * 刷新视频列表数据
     *
     * @param datas
     */
    public void refreshVideoList(List<? extends BaseVideoSourceModel> datas) {
        List<IVideoSourceModel> videoList = new ArrayList<>();
        videoList.addAll(datas);
        videoListView.refreshData(videoList);
        //取消加载loading
        mLoadingView.cancle();

    }

    /**
     * 刷新视频列表数据
     *
     * @param datas
     * @param position
     */
    public void refreshVideoList(List<LittleMineVideoInfo.VideoListBean> datas, int position) {
        List<IVideoSourceModel> videoList = new ArrayList<>();
        videoList.addAll(datas);
        videoListView.refreshData(videoList, position);
        //取消加载loading
        mLoadingView.cancle();
    }



    /**
     * 添加更多视频
     *
     * @param datas
     */
    public void addMoreData(List<? extends BaseVideoSourceModel> datas) {
        List<IVideoSourceModel> videoList = new ArrayList<>();
        videoList.addAll(datas);
        videoListView.addMoreData(videoList);
        //取消加载loading
        mLoadingView.cancle();
    }

    /**
     * 设置下拉刷新数据listener
     *
     * @param listener OnRefreshDataListener
     */
    public void setOnRefreshDataListener(AlivcVideoListView.OnRefreshDataListener listener) {
        this.onRefreshDataListener = listener;
    }

    public void onStart() {

    }

    public void onResume() {
        videoListView.setOnBackground(false);

    }

    public void onStop() {
        mLoadingView.cancle();
    }

    public void onPause() {

        videoListView.setOnBackground(true);

    }

    public void onDestroy() {
        context = null;
        if (mDownloadManager != null) {
            mDownloadManager.setOnCompletionListener(null);
            mDownloadManager.setOnErrorListener(null);
            mDownloadManager.setOnProgressListener(null);
            mDownloadManager.setOnPreparedListener(null);
            mDownloadManager.release();
            mDownloadManager = null;
        }
    }

    /**
     * 视频列表获取失败
     */
    public void loadFailure() {
        mLoadingView.cancle();
        videoListView.loadFailure();
    }



    private FragmentActivity mActivity;

    private FragmentManager getFragmentManager() {
        FragmentManager fm = null;
        if (mActivity != null) {
            fm = mActivity.getSupportFragmentManager();
        } else {
            Context mContext = getContext();
            if (mContext instanceof FragmentActivity) {
                fm = ((FragmentActivity)mContext).getSupportFragmentManager();
            }
        }
        return fm;
    }

    /**
     * 刷新sts信息
     *
     * @param tokenInfo
     */
    public void refreshStsInfo(StsTokenInfo tokenInfo) {
        if(videoListView != null){
            String currentUid = videoListView.getCurrentUid();
            if(!TextUtils.isEmpty(currentUid) && tokenInfo != null){
                StsInfo stsInfo = new StsInfo();
                stsInfo.setAccessKeyId(tokenInfo.getAccessKeyId());
                stsInfo.setAccessKeySecret(tokenInfo.getAccessKeySecret());
                stsInfo.setSecurityToken(tokenInfo.getSecurityToken());
                videoListView.moveTo(currentUid,stsInfo);
            }
        }
    }

    /**
     * 删除按钮点击listener
     */
    public interface OnVideoDeleteListener {
        /**
         * 删除视频
         *
         * @param videoId 视频id
         */
        void onDeleteClick(LittleMineVideoInfo.VideoListBean videoId);
    }

    public void setOnVideoDeleteListener(
        OnVideoDeleteListener mOutOnVideoDeleteListener) {
        this.mOutOnVideoDeleteListener = mOutOnVideoDeleteListener;
    }
    /**
     * 移除当前播放的视频
     */
    public void removeCurrentPlayVideo() {
        videoListView.removeCurrentPlayVideo();
    }

}
