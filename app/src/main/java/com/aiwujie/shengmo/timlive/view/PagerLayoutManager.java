package com.aiwujie.shengmo.timlive.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.aiwujie.shengmo.timlive.adapter.OnPageUnSelectListener;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.view.LivePagerSnapHelper;


/**
 * 直播播放的 LayoutManager
 */
public class PagerLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
   // private LivePagerSnapHelper mPagerSnapHelper;
    private PagerSnapHelper mPagerSnapHelper;
    private int currentPosition;
    private boolean attached; //默认第一次加载子view
    private OnPageUnSelectListener mOnPageUnSelectListener;

    PagerLayoutManager(Context context) {
        super(context);
       // mPagerSnapHelper = new LivePagerSnapHelper();
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }


    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (!attached) {
            attached = true;
            currentPosition = getPosition(view);
            if(mOnPageUnSelectListener != null){
                mOnPageUnSelectListener.onPageSelected(currentPosition, view);
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
    }



    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View view = mPagerSnapHelper.findSnapView(this);
            if (view != null && mOnPageUnSelectListener != null) {
                int position = getPosition(view);
                LogUtil.d("scroll Start Index = " + position);
//                if (!mPagerSnapHelper.getFling()) {
//                    return;
//                }
                //滑动选中当前直播间
                if (currentPosition != position) {
                    LogUtil.d("切换直播间 oldIndex = " + currentPosition);
                    //退出上一个直播间
                    mOnPageUnSelectListener.onPageUnSelected(currentPosition, view);
                    currentPosition = position;
                    LogUtil.d("切换直播间 newIndex = " + currentPosition);
                    //进入当前直播间
                    mOnPageUnSelectListener.onPageSelected(currentPosition, view);
                }
            }
        }
        super.onScrollStateChanged(state);
    }



    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //LogUtil.d("dy = " + dy);
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    void setOnPageUnSelectListener(OnPageUnSelectListener mOnPageUnSelectListener) {
        this.mOnPageUnSelectListener = mOnPageUnSelectListener;
    }


}