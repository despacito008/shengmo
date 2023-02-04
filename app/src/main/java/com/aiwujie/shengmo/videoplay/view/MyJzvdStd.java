package com.aiwujie.shengmo.videoplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aiwujie.shengmo.R;

import cn.jzvd.JzvdStd;

public class MyJzvdStd extends JzvdStd {

    public MyJzvdStd(Context context) {
        super(context);
    }

    public MyJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void gotoFullscreen() {
       if (onFullscreenListener != null) {
           screen = SCREEN_FULLSCREEN;
           onFullscreenListener.doFullScreen();
           CONTAINER_LIST.add((ViewGroup)getParent());
       } else {
          // Jzvd.VIDEO_IMAGE_DISPLAY_TYPE = Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP;
            super.gotoFullscreen();
       }
    }


    @Override
    public void gotoNormalScreen() {
        if (onFullscreenListener != null) {
            screen = SCREEN_NORMAL;
            onFullscreenListener.doNormalScreen();
            CONTAINER_LIST.clear();
        } else {
           // Jzvd.VIDEO_IMAGE_DISPLAY_TYPE = Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER;
            super.gotoNormalScreen();
        }

    }

    @Override
    public int getLayoutId() {
        return super.getLayoutId();
    }

    @Override
    public void startVideo() {
        super.startVideo();
        if (onVideoListener != null) {
            onVideoListener.onVideoStart();
        }
    }




    public interface OnFullscreenListener {
        void doFullScreen();
        void doNormalScreen();
    }

    OnFullscreenListener onFullscreenListener;

    public void setOnFullscreenListener(OnFullscreenListener onFullscreenListener) {
        this.onFullscreenListener = onFullscreenListener;
    }

    public interface OnMenuListener {
        void doMenuShow();
        void doMenuDismiss();
    }

    OnMenuListener onMenuListener;

    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    public interface OnVideoListener {
        void onVideoStart();
    }

    OnVideoListener onVideoListener;

    public void setOnVideoListener(OnVideoListener onVideoListener) {
        this.onVideoListener = onVideoListener;
    }

    public void onClickUiToggle() {
        super.onClickUiToggle();
        //ToastUtil.show("显示");
        if (onMenuListener != null) {
            onMenuListener.doMenuShow();
        }
    }

    public void dissmissControlView() {
        super.dissmissControlView();
        //ToastUtil.show("隐藏");

        if (onMenuListener != null) {
            onMenuListener.doMenuDismiss();
        }
    }
    boolean mAllowMove = true;
    //设置是否允许左右滑动拖动进度条
    public void setMoveAllowed(boolean isAllowed) {
        mAllowMove = isAllowed;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.surface_container) {
            if(event.getAction() == MotionEvent.ACTION_UP && mChangePosition){
                if (!mAllowMove) {
                    //拖动进度条不做处理
                    return true;
                } else {
                    return super.onTouch(v, event);
                }
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        //不显示进度框
        if (mAllowMove) {
            super.showProgressDialog(deltaX,seekTime,seekTimePosition,totalTime,totalTimeDuration);
        }
    }

}
