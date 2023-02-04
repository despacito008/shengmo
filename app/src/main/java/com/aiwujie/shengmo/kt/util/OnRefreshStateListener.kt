package com.aiwujie.shengmo.kt.util

import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener

abstract class OnRefreshStateListener: OnMultiPurposeListener{
    override fun onRefresh(refreshlayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshlayout: RefreshLayout) {

    }

    override fun onHeaderReleased(header: RefreshHeader?, headerHeight: Int, extendHeight: Int) {

    }

    override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
        if (isDragging) {
            doPullStart()
            isPulling = true
        } else {
            if (percent < 1f && isPulling) {
                doPullEnd()
                isPulling = false
            }
        }
    }

    override fun onFooterMoving(footer: RefreshFooter?, isDragging: Boolean, percent: Float, offset: Int, footerHeight: Int, maxDragHeight: Int) {

    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {

    }


    override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {

    }

    override fun onHeaderStartAnimator(header: RefreshHeader?, headerHeight: Int, extendHeight: Int) {

    }



    override fun onFooterReleased(footer: RefreshFooter?, footerHeight: Int, extendHeight: Int) {

    }



    override fun onFooterStartAnimator(footer: RefreshFooter?, footerHeight: Int, extendHeight: Int) {

    }

    override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {

    }
    var isPulling = false
    abstract fun doPullStart()
    abstract fun doPullEnd()
}