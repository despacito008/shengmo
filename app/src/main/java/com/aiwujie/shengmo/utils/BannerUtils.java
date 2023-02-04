package com.aiwujie.shengmo.utils;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

/**
 * Created by 290243232 on 2017/10/24.
 */

public class BannerUtils {
    public static void setBannerView(Banner mMyBanner){
        //设置图片加载器
        mMyBanner.setImageLoader(new GlideImageLoader());
        //设置轮播时间
        mMyBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mMyBanner.setIndicatorGravity(BannerConfig.CENTER);
//        不显示指示器
//        mMyBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mMyBanner.setBannerAnimation(Transformer.Default);
    }
}
