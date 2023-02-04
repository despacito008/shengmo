package com.aiwujie.shengmo.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mac on 16/9/19.
 */
public class MyViewpager extends ViewPager{
    private boolean scrollble = false; //默认禁止滑动

    public MyViewpager(Context context) {
        super(context);
    }

    public MyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isScrollble()) {
            return false;
        } else {
            return super.onInterceptTouchEvent(arg0);
        }
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);//表示切换的时候，不需要切换时间。
    }

}
