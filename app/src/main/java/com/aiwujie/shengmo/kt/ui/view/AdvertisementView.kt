package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity
import com.aiwujie.shengmo.activity.newui.TopicDynamicActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.BannerNewData
import com.aiwujie.shengmo.utils.BannerUtils
import com.aiwujie.shengmo.utils.GlideImageLoader
import com.aiwujie.shengmo.utils.LogUtil
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: AdvertisementView
 * @Author: xmf
 * @CreateDate: 2022/4/18 16:36
 * @Description: 轮播广告
 */
class AdvertisementView : FrameLayout {
    var bannerView: Banner
    var ivClose: ImageView
    var mRootView:View
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.app_layout_normal_advertisement, this)
        bannerView = mRootView.findViewById(R.id.banner_advertisement)
        ivClose = mRootView.findViewById(R.id.iv_advertisement_close)
        ivClose.setOnClickListener {
            mRootView.visibility = View.GONE
        }
        bannerView.setOnBannerListener { index ->
            bannerData?.data?.get(index)?.run {
                when (link_type) {
                    "0" -> {
                        BannerWebActivity.start(context, url, title)
                    }
                    "1" -> {
                        TopicDynamicActivity.start(context, link_id, title)
                    }
                    "2" -> {
                        NewDynamicDetailActivity.start(context, link_id)
                    }
                    else -> {
                        UserInfoActivity.start(context, link_id)
                    }
                }
            }
        }
        with(bannerView) {
            //设置图片加载器
            setImageLoader(GlideImageLoader())
            //设置轮播时间
            setDelayTime(3000)
            //设置指示器位置（当banner模式中有指示器时）
            setIndicatorGravity(BannerConfig.CENTER)
            //不显示指示器
            //setBannerStyle(BannerConfig.NOT_INDICATOR);
            setBannerAnimation(Transformer.Default)
        }
        mRootView.visibility = View.GONE
    }

    private var bannerData: BannerNewData? = null
    public fun initData(data: BannerNewData) {
        bannerData = data
        LogUtil.d("banner data size = " + bannerData?.data?.size)
        bannerData?.data?.run {
            mRootView.visibility = View.VISIBLE
            bannerView.setImages(map { it.path })
            bannerView.start()
        }
    }
}
