package com.aiwujie.shengmo.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.videoplay.view.VideoLoadingProgressbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2020/5/19.
 */

public class VideoPlayerActivity extends SwipeBackActivity {

    @BindView(R.id.mVideo_Player)
    VideoView mVideoView;
    @BindView(R.id.mAllZan_return)
    ImageView backBtn;
    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.pbLoading)
    VideoLoadingProgressbar pbLoading;

    MediaController mediaController;
    Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            if (mVideoView.isPlaying()) {
                float current = mVideoView.getCurrentPosition();
                float sumTime = mVideoView.getDuration();

                pbLoading.setProgress(current/sumTime);
            }
            handler.postDelayed(runnable, 500);
        }
    };
    private int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        initVideoView();
        new Thread(runnable).start();
    }


    private void initVideoView(){
        mediaController = new MediaController(this);
        pbLoading.setIIsLoading(false);
        pbLoading.setProgress(0);
        mediaController.setVisibility(View.GONE);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoPath(getIntent().getStringExtra("video_url"));//设置视频文件

        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("cover_url")).apply(options).into(ivCover);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //视频加载完成,准备好播放视频的回调
                mp.setLooping(true);
                mp.start();

                ivCover.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivCover.setVisibility(View.GONE);
                    }
                },300);
            }


        });



        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //视频播放完成后的回调

            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //异常回调

                return false;//如果方法处理了错误，则为true；否则为false。返回false或根本没有OnErrorListener，将导致调用OnCompletionListener。
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //信息回调
//                what 对应返回的值如下
//                public static final int MEDIA_INFO_UNKNOWN = 1;  媒体信息未知
//                public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700; 媒体信息视频跟踪滞后
//                public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3; 媒体信息\视频渲染\开始
//                public static final int MEDIA_INFO_BUFFERING_START = 701; 媒体信息缓冲启动
//                public static final int MEDIA_INFO_BUFFERING_END = 702; 媒体信息缓冲结束
//                public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703; 媒体信息网络带宽（703）
//                public static final int MEDIA_INFO_BAD_INTERLEAVING = 800; 媒体-信息-坏-交错
//                public static final int MEDIA_INFO_NOT_SEEKABLE = 801; 媒体信息找不到
//                public static final int MEDIA_INFO_METADATA_UPDATE = 802; 媒体信息元数据更新
//                public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901; 媒体信息不支持字幕
//                public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902; 媒体信息字幕超时

                return false; //如果方法处理了信息，则为true；如果没有，则为false。返回false或根本没有OnInfoListener，将导致丢弃该信息。
            }
        });
    }

    @OnClick(R.id.mAllZan_return)
    public void onViewClicked(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        handler.removeCallbacks(runnable);
    }


    int mx,my;
    int lastx,lasty;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取坐标点：
        int x= (int) ev.getX();
        int y= (int) ev.getY();

        if (y>200&&y<height-200)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deletx=x-mx;
                int delety=y-my;
                if(Math.abs(deletx)<Math.abs(delety))
                {
                    finish();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
        lastx=x;
        lasty=y;
        mx=x;
        my=y;
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void finish() {
        super.finish();

    }
};
