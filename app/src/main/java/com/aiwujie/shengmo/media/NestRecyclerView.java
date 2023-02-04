package com.aiwujie.shengmo.media;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aiwujie.shengmo.utils.LogUtil;


/**
 * @author loumingxuan
 * @date 2020/4/30.
 * email：lmxjw3@gmail.com
 * description：
 */
public class NestRecyclerView extends RecyclerView {
    private float downX, downY;

    public NestRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                float xDistance = Math.abs(curX - downX);
                float yDistance = Math.abs(curY - downY);
                LogUtil.d(xDistance + " ---- " + yDistance);
                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
