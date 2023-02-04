package com.aiwujie.shengmo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * @author loumingxuan
 * @date 2020/4/30.
 * email：lmxjw3@gmail.com
 * description：
 */
public class vRecyclerView extends RecyclerView {
    private float downX, downY;

    public vRecyclerView(@NonNull Context context) {
        super(context);
    }

    public vRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public vRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
