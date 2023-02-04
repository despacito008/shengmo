package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.clearscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

public class ClearScreenConstraintLayout extends ConstraintLayout {
    private int mDownX;     // 手指按下的x轴位置
    private int mDownY;     // 手指按下的y轴位置
    private int startX;     // 滑动开始时x轴偏移量
    private int translateX; // 当前x轴偏移量
    private int endX;       // 动画结束时x轴偏移量
    private boolean ifCleared; // 是否已清屏
    private VelocityTracker mVelocityTracker; // 计算滑动速度
    private ValueAnimator mAnimator; // 动画

    private boolean leftSlide = true; // true-左滑清屏 false-右滑清屏
    private OnSlideClearListener slideClearListener; // 清屏监听器
    private ArrayList<View> listClearViews = new ArrayList<>(); // 需要清除的View

    public ClearScreenConstraintLayout(Context context) {
        this(context, null);
    }

    public ClearScreenConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearScreenConstraintLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setClickable(true);
        addView(view, 0);

        mVelocityTracker = VelocityTracker.obtain();
        mAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(200);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                translateChild((int) (translateX + value * (endX - translateX)));
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (ifCleared && translateX == 0) {
                    if (slideClearListener != null) {
                        slideClearListener.onRestored();
                    }
                    ifCleared = !ifCleared;
                } else if (!ifCleared && Math.abs(translateX) == getTranslateDistance()) {
                    if (slideClearListener != null) {
                        slideClearListener.onCleared();
                    }
                    ifCleared = !ifCleared;
                }
            }
        });
    }

    /**
     * 滑动监听
     */
    public void setOnSlideListener(OnSlideClearListener slideListener) {
        this.slideClearListener = slideListener;
    }

    /**
     * 滑动方向（左滑 or 右滑）
     */
    public void setSlideDirection(SlideDirection direction) {
        leftSlide = direction == SlideDirection.LEFT;
    }

    /**
     * 添加需要清屏的view
     */
    public void addClearViews(View... views) {
        for (View cell : views) {
            if (!listClearViews.contains(cell)) {
                listClearViews.add(cell);
            }
        }
    }

    /**
     * 移除需要清屏的view
     */
    public void removeClearViews(View... views) {
        for (View cell : views) {
            listClearViews.remove(cell);
        }
    }

    /**
     * 移除所有需要清屏的view
     */
    public void removeAllClearViews() {
        listClearViews.clear();
    }

    /**
     * 清屏（有动画）
     */
    public void clearWithAnim() {
        if (ifCleared) {
            return;
        } else if (leftSlide) {
            endX = -getTranslateDistance();
        } else {
            endX = getTranslateDistance();
        }
        mAnimator.start();
    }

    /**
     * 清屏（无动画）
     */
    public void clearWithoutAnim() {
        if (ifCleared) {
            return;
        } else if (leftSlide) {
            endX = -getTranslateDistance();
        } else {
            endX = getTranslateDistance();
        }
        translateChild(endX);
        ifCleared = true;
    }

    /**
     * 还原（有动画）
     */
    public void restoreWithAnim() {
        if (!ifCleared) {
            return;
        }
        endX = 0;
        mAnimator.start();
    }

    /**
     * 还原（无动画）
     */
    public void restoreWithoutAnim() {
        if (!ifCleared) {
            return;
        }
        endX = 0;
        translateChild(endX);
        ifCleared = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = (int) event.getX();
                    mDownY = (int) event.getY();
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int endX = (int) event.getX();
                    int endY = (int) event.getY();
                    int distanceX = Math.abs(endX - mDownX);
                    int distanceY = Math.abs(endY - mDownY);
                    if (distanceX >= distanceY) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // 两次点击按钮之间的点击间隔不能大于300毫秒
    private static final int MIN_CLICK_DELAY_TIME = 300;
    long lastTime = 0;
    private float deltaX = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = x;
                    mDownY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                   // Log.d("slide", x - mDownX + "..");
                    if (!mAnimator.isRunning() && Math.abs(x - mDownX) > Math.abs(y - mDownY)) {
                        startX = translateX;
                        if (x - mDownX  > 300 && !ifCleared) {
                            if (onOtherEventListener != null) {
                                onOtherEventListener.onLeftBack();
                            }
                        }
//                        if (Math.abs(x - mDownX) > 10) {
//                            if ((leftSlide && ifCleared) || (!leftSlide && !ifCleared)) {
//                                return true;
//                            }
//                        }
                        if (x - mDownX < -10) {
                            if ((leftSlide && !ifCleared) || (!leftSlide && ifCleared)) {
                                return true;
                            }
                        } else if (x - mDownX > 10) {
                            if ((leftSlide && ifCleared) || (!leftSlide && !ifCleared)) {
                                return true;
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    deltaX = event.getRawX() - mDownX;
                    if (Math.abs(deltaX) < 6) {
                        long curClickTime = System.currentTimeMillis();
                        if(curClickTime - lastTime < MIN_CLICK_DELAY_TIME){ //双击事件
                            if(onOtherEventListener != null) {
                                onOtherEventListener.onDoubleClick();
                            }
                        }else{
                            lastTime =  System.currentTimeMillis();
                        }
                    }
                    break;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            mVelocityTracker.addMovement(event);
            final int x = (int) event.getX();
            final int offsetX = x - mDownX;
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    translateChild(startX + offsetX);
                    return true;
                case MotionEvent.ACTION_UP:
                    if (translateX != 0) {
                        mVelocityTracker.computeCurrentVelocity(10);
                        if (Math.abs(offsetX) > getTranslateDistance() / 3 ||
                                (mVelocityTracker.getXVelocity() > 20 && !leftSlide && !ifCleared) ||
                                (mVelocityTracker.getXVelocity() > 20 && leftSlide && ifCleared) ||
                                (mVelocityTracker.getXVelocity() < -20 && !leftSlide && ifCleared) ||
                                (mVelocityTracker.getXVelocity() < -20 && leftSlide && !ifCleared)) {
                            if (ifCleared) {
                                endX = 0;
                            } else if (leftSlide) {
                                endX = -getTranslateDistance();
                            } else {
                                endX = getTranslateDistance();
                            }
                        } else {
                            endX = startX;
                        }
                    }
                    mAnimator.start();
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }


    public int getTranslateDistance() {
        return Math.max(getWidth(),getHeight());
    }


    private void translateChild(int translate) {

        if ((leftSlide && translate > 0) || ((!leftSlide && translate < 0))) {
            translate = 0;
        }

        if (leftSlide && translate < -10) {

        }

        translateX = translate;
        for (int i = 0; i < listClearViews.size(); i++) {
            listClearViews.get(i).setTranslationX(translate);
        }
    }

    public interface OnSlideClearListener {
        void onCleared();

        void onRestored();
    }

    public interface OnOtherEventListener {
        void onLeftBack();

        void onDoubleClick();
    }

    private OnOtherEventListener onOtherEventListener;

    public void setOnOtherEventListener(OnOtherEventListener onOtherEventListener) {
        this.onOtherEventListener = onOtherEventListener;
    }

    public boolean getIsClear() {
        return ifCleared;
    }
}
