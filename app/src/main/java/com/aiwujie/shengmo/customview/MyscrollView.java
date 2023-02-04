package com.aiwujie.shengmo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class MyscrollView extends ScrollView {
    private ScrollListener mListener;

    public static interface ScrollListener {
        public void scrollOritention(int oritention);
    }

    /**
     * ScrollView正在向上滑动 
     */
    public static final int SCROLL_UP = 0x01;

    /**
     * ScrollView正在向下滑动 
     */
    public static final int SCROLL_DOWN = 0x10;

    /**
     * 最小的滑动距离 
     */
    private static final int SCROLLLIMIT = 30;

    public MyscrollView(Context context) {
        super(context, null);
    }

    public MyscrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyscrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (oldt > t && oldt - t > SCROLLLIMIT) {// 向下  
            if (mListener != null)
                mListener.scrollOritention(SCROLL_DOWN);
        } else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向上  
            if (mListener != null)
                mListener.scrollOritention(SCROLL_UP);
        }
    }

    public void setScrollListener(ScrollListener l) {
        this.mListener = l;
    }
}
