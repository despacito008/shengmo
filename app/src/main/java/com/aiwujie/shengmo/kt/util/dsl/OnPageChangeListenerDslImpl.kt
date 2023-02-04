package com.aiwujie.shengmo.kt.util.dsl

import android.support.v4.view.ViewPager

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.util.dsl
 * @ClassName: OnPageChangeListenerDslImpl
 * @Author: xmf
 * @CreateDate: 2022/4/20 20:24
 * @Description:
 */
private typealias PageScrolledCallback = (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit
private typealias PageSelectedCallback = (position: Int) -> Unit
private typealias PageScrollStateChanged = (state: Int) -> Unit
class OnPageChangeListenerDslImpl:ViewPager.OnPageChangeListener {
    private var pageScrolledCallback:PageScrolledCallback? = null
    private var pageSelectedCallback:PageSelectedCallback? = null
    private var pageScrollStateChanged:PageScrollStateChanged? = null

    fun onPageSelected(callback: PageSelectedCallback) {
        pageSelectedCallback = callback
    }

    fun onPageScrolled(callback: PageScrolledCallback) {
        pageScrolledCallback = callback
    }

    fun onPageScrollStateChanged(callback: PageScrolledCallback) {
        pageScrolledCallback = callback
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pageScrolledCallback?.invoke(position,positionOffset,positionOffsetPixels) ?: Unit
    }

    override fun onPageSelected(position: Int) {
        pageSelectedCallback?.invoke(position) ?: Unit
    }

    override fun onPageScrollStateChanged(state: Int) {
        pageScrollStateChanged?.invoke(state) ?: Unit
    }
}
