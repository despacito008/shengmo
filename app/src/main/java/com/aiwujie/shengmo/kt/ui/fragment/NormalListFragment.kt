package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.finishLoadMoreWithMoreData
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.internal.operators.maybe.MaybeIsEmpty

abstract class NormalListFragment : LazyFragment() {
    override fun getContentViewId(): Int {
        return R.layout.app_layout_normal_list
    }

    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var rvSearchResult: RecyclerView
    lateinit var gLoadHolder: Gloading.Holder
    lateinit var rlLocation: RelativeLayout

    override fun initView(rootView: View) {
        refreshLayout = rootView.findViewById(R.id.smart_refresh_normal_list)
        rvSearchResult = rootView.findViewById(R.id.rv_normal_list)
        gLoadHolder = Gloading.getDefault().wrap(rvSearchResult)
        rlLocation = rootView.findViewById(R.id.rl_location)
    }







    fun showLoadComplete() {
        gLoadHolder.showLoadSuccess()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMoreWithMoreData()
    }

    fun showLoadError(hashMore: Boolean, isFirstPage: Boolean, msg: String?) {
        refreshLayout.finishRefresh()
        if (hashMore) {
            refreshLayout.finishLoadMore()
            msg?.showToast()
            if (isFirstPage) {
                gLoadHolder.showLoadFailed()
            } else {
                gLoadHolder.showLoadSuccess()
            }
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData()
            if (isFirstPage) {
                gLoadHolder.showEmpty()
            } else {
                gLoadHolder.showLoadSuccess()
            }
        }
    }

}