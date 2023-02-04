package com.aliyun.apsara.alivclittlevideo.view.mine;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义NestedScrollView
 *
 * @author xlx
 */

public class TitleNestedScrollView extends NestedScrollView  {
    /**
     * 该控件滑动的高度，高于这个高度后交给子滑动控件
     */
    int mParentScrollHeight;
    int mScrollY;

    private RecyclerView recyclerView;

    public TitleNestedScrollView(Context context) {
        super(context);
    }

    public TitleNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMyScrollHeight(int scrollLength) {
        this.mParentScrollHeight = scrollLength;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return mScrollY < mParentScrollHeight;
    }

    /**
     * 子控件告诉父控件 开始滑动了
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed 如果有就返回true
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);

        if (target instanceof RecyclerView) {
            if (recyclerView == null) {
                recyclerView = (RecyclerView)target;
                recyclerView.setOnFlingListener(onFlingListener);
            }
        }
        if (mScrollY < mParentScrollHeight) {
            recyclerView.setNestedScrollingEnabled(false);
            consumed[1] = dy;
            scrollBy(0, dy);
            Log.e("Test", "..........");
            recyclerView.setNestedScrollingEnabled(true);
        }

    }
    RecyclerView.OnFlingListener onFlingListener = new RecyclerView.OnFlingListener() {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            return mScrollY < mParentScrollHeight;
        }
    };

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollY = t;
    }


    private int getStatuBarHeight() {
        int height = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }


}
