package com.tencent.liteav.custom;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.liteav.ui.TRTCAudioCallActivity;
import com.tencent.liteav.ui.TRTCVideoCallActivity;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayout;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayoutManager;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayout;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayoutManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 视频悬浮窗服务
 */

/**
 * 视频悬浮窗服务
 */
public class FloatVideoWindowService2 extends Service {
    private String currentBigUserId;
    //浮动布局view
    private View mFloatingLayout;
    //容器父布局
    private TXCloudVideoView mTXCloudVideoView;
    private ImageView mFloatImageView;
    private TextView mFloatTextView;
    private String TAG = getClass().getSimpleName();
    private int type;
    private String userUrl;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("logUtil","service onCreate");
        initWindow();//设置悬浮窗基本参数（位置、宽高等）
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event<String> event) {
        if (event.getCode() == 1000) {
            findVideoView(event.getData());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        currentBigUserId = intent.getStringExtra("userId");
        userUrl = intent.getStringExtra("userUrl");
        type = intent.getIntExtra("type",-1);
        initFloating();//悬浮框点击事件的处理
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public FloatVideoWindowService2 getService() {
            return FloatVideoWindowService2.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mFloatingLayout != null) {
            FloatWindow.destroy();
            // 移除悬浮窗口
            //mWindowManager.removeView(mFloatingLayout);
            mFloatingLayout = null;
            Constents.isShowAnchorFloatWindow = false;
        }
    }

    /**
     * 设置悬浮框基本参数（位置、宽高等）
     */
    private void initWindow() {
        mFloatingLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_float_video_layout, null);
    }

    private void initFloating() {
        Log.d("logUtil","init Floating");
        mTXCloudVideoView = mFloatingLayout.findViewById(R.id.float_videoview);
        mFloatImageView = mFloatingLayout.findViewById(R.id.float_imageview);
        mFloatTextView = mFloatingLayout.findViewById(R.id.float_textview);
        mFloatTextView.setText("直播中");
//        if (type == 2) {
//            mFloatTextView.setText("语音通话中");
//           // findAudioView(currentBigUserId);
//        } else {
//            mFloatTextView.setText("视频通话中");
//           // findVideoView(currentBigUserId);
//        }
        Constents.isShowAnchorFloatWindow = true;
        if (Constents.mAnchorTextureView != null) {
            mTXCloudVideoView.removeVideoView();
            TXCGLSurfaceView mTXCGLSurfaceView = Constents.mAnchorTextureView;
            ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
            mTXCloudVideoView.addVideoView(mTXCGLSurfaceView);
        } else {
            if (Constents.mAnchorViewView != null) {
                mTXCloudVideoView = Constents.mAnchorViewView;
            }
        }

        mFloatingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.toastShortMessage("回到主播端");
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.ANCHOR_RESUME_LIVING,"",""));
//                if (type == 2) {
//                    //在这里实现点击重新回到Activity
//                    Intent intent = new Intent(FloatVideoWindowService2.this, TRTCAudioCallActivity.class);
//                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } else {
//                    //在这里实现点击重新回到Activity
//                    Intent intent = new Intent(FloatVideoWindowService2.this, TRTCVideoCallActivity.class);
//                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
            }
        });

        FloatWindow
                .with(getApplicationContext())
                .setView(mFloatingLayout)
                .setWidth((int) UIUtil.dp2px(this,96))                               //设置控件宽高
                .setHeight((int)UIUtil.dp2px(this,136))
                .setMoveType(MoveType.slide,0,0)
                .setX(0)                                   //设置控件初始位置
                .setY(Screen.height,0.3f)
                .setDesktopShow(true)                        //桌面显示
                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
//                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();

//        View mViewRoot = LayoutInflater.from(this).inflate( R.layout.live_layout_float_window, null);
//        FloatWindowLayout.FloatWindowRect rect = new FloatWindowLayout.FloatWindowRect(300, 0, (int) UIUtil.dp2px(this,96), (int)UIUtil.dp2px(this,136));
////
//        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        mWindowParams = new WindowManager.LayoutParams();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//            mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        }
//        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        mWindowParams.gravity = Gravity.CENTER_VERTICAL;
//        mWindowParams.format = PixelFormat.TRANSLUCENT;
//        mWindowParams.x = rect.x;
//        mWindowParams.y = rect.y;
//        mWindowParams.width = rect.width;
//        mWindowParams.height = rect.height;
//        mWindowManager.addView(mFloatingLayout, mWindowParams);
//
//        mFloatingLayout.setOnTouchListener(new FloatingOnTouchListener());

    }
    ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int i, int i1) {
            if(mFloatingLayout == null){
                return;
            }
            int screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
            if(i == 0){
                mFloatingLayout.setBackgroundResource(R.drawable.bg_round_float);
            } else {
                if(i == screenWidth - mFloatingLayout.getWidth()){
                    mFloatingLayout.setBackgroundResource(R.drawable.bg_round_float);
                } else {
                    mFloatingLayout.setBackgroundResource(R.drawable.bg_round_float);
                }
            }
        }

        @Override
        public void onShow() {

        }

        @Override
        public void onHide() {

        }

        @Override
        public void onDismiss() {

        }

        @Override
        public void onMoveAnimStart() {

        }

        @Override
        public void onMoveAnimEnd() {

        }

        @Override
        public void onBackToDesktop() {

        }
    };

    private void findVideoView(String userId) {
        mTXCloudVideoView.removeVideoView();
        TRTCVideoLayoutManager mTRTCVideoViewLayout = Constents.mVideoViewLayout;
        TRTCVideoLayout lVideoViewLayout = mTRTCVideoViewLayout.findCloudViewView(userId);
        TXCloudVideoView mLocalVideoView =lVideoViewLayout.getVideoView();
        if (mLocalVideoView == null) {
            mLocalVideoView = mTRTCVideoViewLayout.allocCloudVideoView(userId).getVideoView();
        }
        if (Constents.currentUserID.equals(userId)) {
            TXCGLSurfaceView mTXCGLSurfaceView = mLocalVideoView.getGLSurfaceView();
            if (mTXCGLSurfaceView != null && mTXCGLSurfaceView.getParent() != null) {
                ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
                mTXCloudVideoView.addVideoView(mTXCGLSurfaceView);
            }
        } else {
            TextureView mTextureView = mLocalVideoView.getVideoView();
            if (mTextureView != null && mTextureView.getParent() != null) {
                ((ViewGroup) mTextureView.getParent()).removeView(mTextureView);
                mTXCloudVideoView.addVideoView(mTextureView);
            }
        }
    }

    private void findAudioView(String userId) {
        mTXCloudVideoView.removeVideoView();
//        TRTCVideoLayoutManager mTRTCVideoViewLayout = Constents.mVideoViewLayout;
//        TRTCVideoLayout lVideoViewLayout = mTRTCVideoViewLayout.findCloudViewView(userId);
//        TXCloudVideoView mLocalVideoView =lVideoViewLayout.getVideoView();
//        if (mLocalVideoView == null) {
//            mLocalVideoView = mTRTCVideoViewLayout.allocCloudVideoView(userId).getVideoView();
//        }

        TRTCAudioLayoutManager mTRTCAudioViewLayout = Constents.mAudioViewLayout;
        TRTCAudioLayout audioCallLayout = mTRTCAudioViewLayout.findAudioCallLayout(userId);
        GlideEngine.loadImage(mFloatImageView,userUrl);

        //ImageView imageView = audioCallLayout.getImageView();
       // FrameLayout frameLayout = mFloatingLayout.findViewById(R.id.f);
        //frameLayout.addView(imageView);

//        if (Constents.currentUserID.equals(userId)) {
//            TXCGLSurfaceView mTXCGLSurfaceView = mLocalVideoView.getGLSurfaceView();
//            if (mTXCGLSurfaceView != null && mTXCGLSurfaceView.getParent() != null) {
//                ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
//                mTXCloudVideoView.addVideoView(mTXCGLSurfaceView);
//            }
//        } else {
//            TextureView mTextureView = mLocalVideoView.getVideoView();
//            if (mTextureView != null && mTextureView.getParent() != null) {
//                ((ViewGroup) mTextureView.getParent()).removeView(mTextureView);
//                mTXCloudVideoView.addVideoView(mTextureView);
//            }
//        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int startX;
        private int startY;
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    startX = x;
                    startY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mWindowParams.x = mWindowParams.x + movedX;
                    mWindowParams.y = mWindowParams.y + movedY;
                    mWindowManager.updateViewLayout(view, mWindowParams);
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) < 5 && Math.abs(y - startY) < 5) {//手指没有滑动视为点击，回到窗口模式
                        //startActivity();
//                        if(mOnTouchFloatWindowListener != null){
//                            mOnTouchFloatWindowListener.onTouchUpFloatWindow();
//                        }
                        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.ANCHOR_RESUME_LIVING,"",""));
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
