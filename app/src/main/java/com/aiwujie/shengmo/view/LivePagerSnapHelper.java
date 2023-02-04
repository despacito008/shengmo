package com.aiwujie.shengmo.view;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiwujie.shengmo.utils.LogUtil;

public class LivePagerSnapHelper extends PagerSnapHelper {
    @Override
    public boolean onFling(int velocityX, int velocityY) {
        LogUtil.d("velocityY = " + velocityY);
//        if (Math.abs(velocityY) < 5000) {
//            if (isOnFling) {
//                isOnFling = false;
//            }
//            return false;
//        } else {
//            if (!isOnFling) {
//                isOnFling = true;
//            }
//        }
        return super.onFling(velocityX,velocityY);
    }


    boolean isOnFling = true;

    public boolean getFling() {
        return isOnFling;
    }

    /**
     * Helper method to facilitate for snapping triggered by a fling.
     *
     * @param layoutManager The {@link } associated with the attached
     *                      {@link RecyclerView}.
     * @param velocityX     Fling velocity on the horizontal axis.
     * @param velocityY     Fling velocity on the vertical axis.
     *
     * @return true if it is handled, false otherwise.
     */
    private boolean snapFromFling(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX,
                                  int velocityY) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return false;
        }

        RecyclerView.SmoothScroller smoothScroller = createScroller(layoutManager);
        if (smoothScroller == null) {
            return false;
        }

        int targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY);
        if (targetPosition == RecyclerView.NO_POSITION) {
            return false;
        }

        LogUtil.d("velocityY =" + velocityY);

        smoothScroller.setTargetPosition(targetPosition);
        layoutManager.startSmoothScroll(smoothScroller);
        return true;
    }


}
