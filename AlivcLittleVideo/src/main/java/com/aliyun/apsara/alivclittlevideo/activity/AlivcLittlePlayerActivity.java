package com.aliyun.apsara.alivclittlevideo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.constants.IntentExtraKey;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.NetWatchdog;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.sts.OnStsResultListener;
import com.aliyun.apsara.alivclittlevideo.sts.StsInfoManager;
import com.aliyun.apsara.alivclittlevideo.sts.StsTokenInfo;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;
import com.aliyun.apsara.alivclittlevideo.view.video.AlivcVideoPlayView;
import com.aliyun.apsara.alivclittlevideo.view.video.OnStsInfoExpiredListener;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.AlivcVideoListView;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.common.widget.AlivcCustomAlertDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AlivcLittlePlayerActivity extends AppCompatActivity {
    private AlivcVideoPlayView videoPlayView;
    private ArrayList<LittleMineVideoInfo.VideoListBean> videoList;
    private int position;
    private int mLastVideoId;
    private NetWatchdog netWatchdog;
    /**
     * 是否重新加载数据
     */
    private boolean isRefreshData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_little_activity_video_detail);
        initData();
        initView();
        // 网络监听
        initNetWatchDog();
    }

    protected void initView() {
        videoPlayView = findViewById(R.id.video_play_detail_view);
        findViewById(R.id.fl_video_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        videoPlayView.refreshVideoList(videoList, position);

        videoPlayView.setOnRefreshDataListener(new MyRefreshDataListener(this));
        videoPlayView.setOnStsInfoExpiredListener(new OnStsInfoExpiredListener() {
            @Override
            public void onTimeExpired() {
                //刷新获取STS
                StsInfoManager.getInstance().refreshStsToken(new MyStsResultListener(AlivcLittlePlayerActivity.this));
            }

            @Override
            public StsTokenInfo refreshSts() {
                return StsInfoManager.getInstance().refreshStsToken();
            }
        });

        videoPlayView.setOnVideoDeleteListener(new AlivcVideoPlayView.OnVideoDeleteListener() {
            @Override
            public void onDeleteClick(LittleMineVideoInfo.VideoListBean video) {
                showDeleteConfirmDialog(video.getVideoId());
            }
        });
    }

    protected void initData() {
        Bundle videoData = getIntent().getBundleExtra(IntentExtraKey.KEY_VIDEO_DETAIL_DATA);
        videoList = videoData.getParcelableArrayList(IntentExtraKey.KEY_VIDEO_DETAIL_DATA_LIST);
        position = videoData.getInt(IntentExtraKey.KEY_VIDEO_DETAIL_POSITION);
        if (videoList != null && videoList.size() > 0) {
            mLastVideoId = videoList.get(videoList.size() - 1).getId();
        } else {
            mLastVideoId = -1;
        }


    }

    private static class MyStsResultListener implements OnStsResultListener {
        WeakReference<AlivcLittlePlayerActivity> weakReference;

        MyStsResultListener(AlivcLittlePlayerActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(StsTokenInfo tokenInfo) {

            AlivcLittlePlayerActivity videoListActivity = weakReference.get();
            // videoListActivity.videoPlayView刷新sts信息
            videoListActivity.videoPlayView.refreshStsInfo(tokenInfo);

        }

        @Override
        public void onFail() {

        }
    }
    /**
     * 显示删除视频确认提示窗
     */
    private void showDeleteConfirmDialog(final String videoId) {
        new AlivcCustomAlertDialog.Builder(this)
        .setNoIcon(true)
        .setMessage(getResources().getString(R.string.alivc_little_main_dialog_content_delete))
        .setDialogClickListener("", "", new AlivcCustomAlertDialog.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                requestDeleteVideo(videoId);
            }

            @Override
            public void onCancel() {

            }
        })
        .create()
        .show();
    }

    /**
     * 请求服务器删除指定视频
     *
     * @param videoId 视频id
     */
    private void requestDeleteVideo(String videoId) {
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(AlivcLittlePlayerActivity.this);
        LittleHttpManager.getInstance().requestDeleteVideo(userInfo.getToken(), userInfo.getUserId(), videoId,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleMyVideoListResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleMyVideoListResponse data) {
                if (result) {
                    videoPlayView.removeCurrentPlayVideo();
                    AlivcLittlePlayerActivity.this.setResult(RESULT_OK);
                } else {
                    ToastUtils.show(AlivcLittlePlayerActivity.this, message);
                }
            }
        });
    }

    private static class MyRefreshDataListener implements AlivcVideoListView.OnRefreshDataListener {
        WeakReference<AlivcLittlePlayerActivity> weakReference;

        MyRefreshDataListener(AlivcLittlePlayerActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onRefresh() {
            Log.e("AlivcLittlePlayer", "onRefresh");

            if (weakReference == null) {
                return;
            }
            AlivcLittlePlayerActivity activity = weakReference.get();
            if (activity != null) {
                activity.loadPlayList(activity.mLastVideoId = -1);
                activity.isRefreshData = true;
            }
        }

        @Override
        public void onLoadMore() {
            Log.e("AlivcLittlePlayer", "onLoadMore");
            if (weakReference == null) {
                return;
            }
            AlivcLittlePlayerActivity activity = weakReference.get();
            if (activity != null) {
                activity.loadPlayList(activity.mLastVideoId);
                activity.isRefreshData = false;
            }
        }
    }

    private void loadPlayList(int id) {
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "R.string.alivc_little_no_user:" + R.string.alivc_little_tip_no_user, Toast.LENGTH_SHORT)
            .show();
            return;
        }
        LittleHttpManager.getInstance().requestMineVideoList(userInfo.getToken(), 1, AlivcLittleHttpConfig.DEFAULT_PAGE_SIZE, id,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleMyVideoListResponse>() {
            @Override
            public void onResponse(final boolean result, String message,
                                   final LittleHttpResponse.LittleMyVideoListResponse data) {

                if (result) {
                    List<LittleMineVideoInfo.VideoListBean> videoList = data.data.getVideoList();
                    if (videoList != null) {
                        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(
                                                      AlivcLittlePlayerActivity.this);
                        LittleMineVideoInfo.VideoListBean.UserBean userBean
                            = new LittleMineVideoInfo.VideoListBean.UserBean(userInfo.getUserId(),
                                    userInfo.getNickName(), userInfo.getAvatarUrl());
                        int size = videoList.size();
                        for (int i = 0; i < size; i++) {
                            videoList.get(i).setUser(userBean);
                            if (i == size - 1) {
                                mLastVideoId = videoList.get(i).getId();
                            }
                        }
                    }
                    if (isRefreshData) {
                        videoPlayView.refreshVideoList(data.data.getVideoList());
                        AlivcLittlePlayerActivity.this.setResult(RESULT_OK);
                    } else {
                        videoPlayView.addMoreData(data.data.getVideoList());
                    }
                } else {
                    if (videoPlayView != null) {
                        videoPlayView.loadFailure();
                    }
                    ToastUtils.show(AlivcLittlePlayerActivity.this, message );
                }

            }
        });
    }
    /**
     * 网络监听
     */
    public void initNetWatchDog() {
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetConnectedListener(new AlivcLittlePlayerActivity.MyNetConnectedListener(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        videoPlayView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        videoPlayView.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        netWatchdog.startWatch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        netWatchdog.stopWatch();
    }

    @Override
    protected void onDestroy() {
        videoPlayView.onDestroy();
        super.onDestroy();
    }
    private static class MyNetConnectedListener implements NetWatchdog.NetConnectedListener {
        private WeakReference<AlivcLittlePlayerActivity> weakReference;

        MyNetConnectedListener(AlivcLittlePlayerActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onReNetConnected(boolean isReconnect) {
            if (isReconnect) {
                //刷新获取STS
                StsInfoManager.getInstance().refreshStsToken(new AlivcLittlePlayerActivity.MyStsResultListener(weakReference.get()));
                //网络重连
                Log.e("Test", "onReNetConnected......");
            }
        }

        @Override
        public void onNetUnConnected() {
            //网络断开
            Log.e("Test", "onNetUnConnected......");
        }
    }

}
