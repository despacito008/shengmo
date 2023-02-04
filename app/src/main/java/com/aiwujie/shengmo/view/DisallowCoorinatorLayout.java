package com.aiwujie.shengmo.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisallowCoorinatorLayout extends CoordinatorLayout {

    public DisallowCoorinatorLayout(Context context) {
        super(context);
    }

    public DisallowCoorinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return false;
//            case MotionEvent.ACTION_MOVE:   //表示父类需要
//                return true;
//            case MotionEvent.ACTION_UP:
//                return true;
//            default:
//                break;
//        }
//        return false;    //如果设置拦截，除了down,其他都是父类处理
//    }


    int mx,my;
    int lastx,lasty;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取坐标点：
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (x > 50) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deletx = x - mx;
                int delety = y - my;
                if(Math.abs(deletx)>Math.abs(delety)) {
                    if (onLeftFillingListener != null) {
                        onLeftFillingListener.onLeftFilling();
                    }
                    //finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
        lastx = x;
        lasty = y;
        mx = x;
        my = y;
        return super.dispatchTouchEvent(ev);
    }

    public interface OnLeftFillingListener {
        void onLeftFilling();
    }

    OnLeftFillingListener onLeftFillingListener;

    public void setOnLeftFillingListener(OnLeftFillingListener onLeftFillingListener) {
        this.onLeftFillingListener = onLeftFillingListener;
    }
}
