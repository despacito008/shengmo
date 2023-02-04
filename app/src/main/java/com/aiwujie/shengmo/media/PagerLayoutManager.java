package com.aiwujie.shengmo.media;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 视频播放的 LayoutManager
 */
public class PagerLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
//public class PagerLayoutManager extends LinearLayoutManager implements RecyclerView.OnScrollChangeListener {
    private PagerSnapHelper mPagerSnapHelper;
    private OnPageChangeListener mOnPageChangeListener;
    private int currentPostion;
    private boolean haveSelect;

    public static void setMaxFlingVelocity(PagerSnapHelper snapHelper, int velocity) {
        try {
            Field field = snapHelper.getClass().getDeclaredField("MAX_SCROLL_ON_FLING_DURATION");
            field.setAccessible(true);
            field.set(snapHelper, velocity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PagerLayoutManager(Context context) {
        super(context);
        mPagerSnapHelper = new PagerSnapHelper();
        setMaxFlingVelocity(mPagerSnapHelper,1000);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (!haveSelect) {
            haveSelect = true;
            currentPostion = getPosition(view);
            mOnPageChangeListener.onPageSelected(currentPostion, view);
        }
    }

    @Override
    public void onChildViewDetachedFromWindow( View view) {
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View view = mPagerSnapHelper.findSnapView(this);
            if (view != null && mOnPageChangeListener != null) {
                int position = getPosition(view);
                if (currentPostion != position) {
                    currentPostion = position;
                    mOnPageChangeListener.onPageSelected(currentPostion, view);
                }
            }
        }
        super.onScrollStateChanged(state);
    }





    @Override
    public boolean canScrollVertically() {
        return true;
    }

    void setOnPageChangeListener(OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

}