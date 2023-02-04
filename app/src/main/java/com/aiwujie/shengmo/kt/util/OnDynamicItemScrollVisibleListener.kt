package com.aiwujie.shengmo.kt.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aiwujie.shengmo.bean.DynamicListData
import com.aiwujie.shengmo.customview.CustomViewPage

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.util
 * @ClassName: OnItemScrollVisibleListener
 * @Author: xmf
 * @CreateDate: 2022/4/11 11:16
 * @Description: 动态列表 短视频item滑动监听
 */
abstract class OnDynamicItemScrollVisibleListener(val dynamicList:List<DynamicListData.DataBean>,val headCount:Int) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == CustomViewPage.SCROLL_STATE_IDLE) {
            with(recyclerView?.layoutManager) {
                if (this is LinearLayoutManager) {
                    val tempArray = IntArray(2)
                    for (i in findLastVisibleItemPosition() downTo findFirstVisibleItemPosition()) {
                        //去掉头布局的index
                        val trueIndex = i - headCount
                        if (trueIndex >= 0 && dynamicList[trueIndex].playUrl.isNotEmpty()) {
                            val childView = findViewByPosition(i)
                            childView?.getLocationOnScreen(tempArray)
                            //屏幕最下方的item 距离屏幕上面的高度
                            if (tempArray[1] < 1650) {
                                onItemScrollVisible(i)
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    abstract fun onItemScrollVisible(index: Int)
}
