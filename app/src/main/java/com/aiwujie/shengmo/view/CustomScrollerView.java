package com.aiwujie.shengmo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import java.util.Calendar;


public class CustomScrollerView extends ScrollView {

    public Callbacks mCallbacks;
    public EndCallbacks mEndCallbacks;
    private boolean isEnd = false;
    private boolean isMoveUp = false;
    private long time = 0l;

    public CustomScrollerView(Context context) {
        super(context);
    }

    public CustomScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public void setEndCallbacks(EndCallbacks callbacks) {
        this.mEndCallbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
        View view = getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        isEnd = false;
        Log.e("scroll view height", view.getBottom() + "");
        Log.e("scroll way", d + "");
        if (t < oldt)
            isMoveUp = true;
        else
            isMoveUp = false;
        if (d <= 0 && (Calendar.getInstance().getTimeInMillis() - time) > 2000) {
            time = Calendar.getInstance().getTimeInMillis();
            if (mEndCallbacks != null) {
                mEndCallbacks.onScrollEnd();
                isEnd = true;
            }
        }
    }

    //定义接口用于回调
    public interface Callbacks {
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }

    public interface EndCallbacks {
        void onScrollEnd();
    }



//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (isMoveUp && isEnd)
//                    return false;
//                else
//                    return true;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}


