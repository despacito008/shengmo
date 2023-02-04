package com.aiwujie.shengmo.kt.util.dsl

import android.support.design.widget.TabLayout

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.util.dsl
 * @ClassName: OnTabSelectedListenerImpl
 * @Author: xmf
 * @CreateDate: 2022/4/20 19:51
 * @Description:
 */
private typealias OnTabCallback = (tab: TabLayout.Tab?) -> Unit
class OnTabSelectedListenerImpl: TabLayout.OnTabSelectedListener {
    private var onTabReselectedCallback: OnTabCallback? = null
    private var onTabUnselectedCallback: OnTabCallback? = null
    private var onTabSelectedCallback: OnTabCallback? = null
    override fun onTabSelected(tab: TabLayout.Tab?) {
        onTabSelectedCallback?.invoke(tab) ?: Unit
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        onTabUnselectedCallback?.invoke(tab) ?: Unit
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        onTabReselectedCallback?.invoke(tab) ?: Unit
    }

    fun onTabReselected(callback: OnTabCallback) {
        onTabReselectedCallback = callback
    }

    fun onTabUnselected(callback: OnTabCallback) {
        onTabUnselectedCallback = callback
    }

    fun onTabSelected(callback: OnTabCallback) {
        onTabSelectedCallback = callback
    }
}
