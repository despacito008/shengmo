package com.aliyun.apsara.alivclittlevideo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;
import com.aliyun.apsara.alivclittlevideo.view.video.AlivcVideoPlayView;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.AlivcVideoListView;
import com.aliyun.svideo.common.utils.ToastUtils;

import java.lang.ref.WeakReference;

public class AlivcLittleLiveActivity extends AppCompatActivity {
    private AlivcVideoPlayView videoPlayView;

    private int mPageIndex = 1;
    /**
     * 是否重新加载数据
     */
    private boolean isRefreshData = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_little_activity_video_detail);
        initView();
        loadPlayList(mPageIndex);
    }
    protected void initView() {
        videoPlayView = findViewById(R.id.video_play_detail_view);
        findViewById(R.id.fl_video_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        videoPlayView.setOnRefreshDataListener(new MyRefreshDataListener(this));
    }

    private static class MyRefreshDataListener implements AlivcVideoListView.OnRefreshDataListener {
        WeakReference<AlivcLittleLiveActivity> weakReference;

        MyRefreshDataListener(AlivcLittleLiveActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onRefresh() {
            Log.e("AlivcLittlePlayer", "onRefresh");

            if (weakReference == null) {
                return;
            }
            AlivcLittleLiveActivity activity = weakReference.get();
            if (activity != null) {
                activity.loadPlayList(activity.mPageIndex = 1);
                activity.isRefreshData = true;
            }
        }

        @Override
        public void onLoadMore() {
            Log.e("AlivcLittlePlayer", "onLoadMore");
            if (weakReference == null) {
                return;
            }
            AlivcLittleLiveActivity activity = weakReference.get();
            if (activity != null) {
                activity.loadPlayList(activity.mPageIndex);
                activity.isRefreshData = false;
            }
        }
    }
    private void loadPlayList(int pageIndex) {
        Log.e("AlivcLittleLiveActivity", "id:" + pageIndex);
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "R.string.alivc_little_no_user:" + R.string.alivc_little_tip_no_user, Toast.LENGTH_SHORT)
            .show();
            return;
        }
        LittleHttpManager.getInstance().requestLiveVideoList(userInfo.getToken(), pageIndex, AlivcLittleHttpConfig.DEFAULT_PAGE_SIZE,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleLiveListResponse>() {
            @Override
            public void onResponse(final boolean result, String message,
                                   final LittleHttpResponse.LittleLiveListResponse data) {

                if (result) {
                    mPageIndex++;
                    if (isRefreshData) {
                        videoPlayView.refreshVideoList(data.data.getLiveList());
                    } else {
                        videoPlayView.addMoreData(data.data.getLiveList());
                    }
                    Log.e("AlivcLittleLiveActivity", "isRefreshData:" + isRefreshData);
                } else {
                    if (videoPlayView != null) {
                        videoPlayView.loadFailure();
                    }
                    ToastUtils.show(AlivcLittleLiveActivity.this, message );
                }

            }
        });
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
    protected void onDestroy() {
        if (videoPlayView != null) {
            videoPlayView.onDestroy();
        }
        super.onDestroy();
    }

}
