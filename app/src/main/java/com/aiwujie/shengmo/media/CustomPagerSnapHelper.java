package com.aiwujie.shengmo.media;

import android.support.v7.widget.PagerSnapHelper;
import android.util.DisplayMetrics;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.media
 * @ClassName: CustomPagerSnapHelper
 * @Author: xmf
 * @CreateDate: 2022/5/17 18:18
 * @Description:
 */
class CustomPagerSnapHelper extends PagerSnapHelper {

    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return 10f / displayMetrics.densityDpi;
    }
}
