package com.aiwujie.shengmo.utils;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class IsListviewSlideBottom {
    public static boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        };
        return result;
    }
}
