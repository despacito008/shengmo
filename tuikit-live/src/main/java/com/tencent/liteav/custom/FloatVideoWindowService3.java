package com.tencent.liteav.custom;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayout;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayoutManager;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayout;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayoutManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 视频悬浮窗服务
 */

/**
 * 视频悬浮窗服务
 */
public class FloatVideoWindowService3 extends Service {
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
        initWindow();//设置悬浮窗基本参数（位置、宽高等）
        //EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        initFloating();//悬浮框点击事件的处理
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public FloatVideoWindowService3 getService() {
            return FloatVideoWindowService3.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
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
        mFloatingLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_float_screen_layout, null);
    }

    private void initFloating() {
        mFloatingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.ANCHOR_RESUME_LIVING,"",""));
            }
        });

        FloatWindow
                .with(getApplicationContext())
                .setView(mFloatingLayout)
                .setWidth((int) UIUtil.dp2px(this,80))                               //设置控件宽高
                .setHeight((int)UIUtil.dp2px(this,80))
                .setMoveType(MoveType.slide,0,0)
                .setX(0)                                   //设置控件初始位置
                .setY(Screen.height,0.3f)
                .setDesktopShow(true)                        //桌面显示
                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
//                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();
    }
    ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int i, int i1) {
            if(mFloatingLayout == null){
                return;
            }
//            int screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
//            if(i == 0){
//                mFloatingLayout.setBackgroundResource(R.drawable.bg_round_float);
//            } else {
//                if(i == screenWidth - mFloatingLayout.getWidth()){
//                    mFloatingLayout.setBackgroundResource(R.drawable.bg_round_float);
//                } else {
//                    mFloatingLayout.setBackgroundResource(R.drawable.bg_round_float);
//                }
//            }
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
