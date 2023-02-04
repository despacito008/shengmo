package com.aiwujie.shengmo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Administrator on 2020/6/2.
 */

public class PullToRefreshRecycleView extends PullToRefreshBase<RecyclerView> {

    private RecyclerView mRecyclerView;

    public PullToRefreshRecycleView(Context context) {
        super(context);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecycleView(Context context, PullToRefreshBase.Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecycleView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context,
                                                 AttributeSet attrs) {
        mRecyclerView = new RecyclerView(context, attrs);
        return mRecyclerView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return isLastItemVisible();
    }

    /**
     * @Description: 判断第一个条目是否完全可见
     *
     * @return boolean:
     * @version 1.0
     * @date 2015-9-23
     * @Author zhou.wenkai
     */
    private boolean isFirstItemVisible() {
        final RecyclerView.Adapter<?> adapter = getRefreshableView().getAdapter();

        // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            return true;

        } else {
            // 第一个条目完全展示,可以刷新
            if (getFirstVisiblePosition() == 0) {
                return mRecyclerView.getChildAt(0).getTop() >= mRecyclerView
                        .getTop();
            }
        }

        return false;
    }

    /**
     * @Description: 获取第一个可见子View的位置下标
     *
     * @return int: 位置
     * @version 1.0
     * @date 2015-9-23
     * @Author zhou.wenkai
     */
    private int getFirstVisiblePosition() {
        View firstVisibleChild = mRecyclerView.getChildAt(0);
        return firstVisibleChild != null ? mRecyclerView
                .getChildAdapterPosition(firstVisibleChild) : -1;
    }



    /**
     * @Description: 判断最后一个条目是否完全可见
     *
     * @return boolean:
     * @version 1.0
     * @date 2015-9-23
     * @Author zhou.wenkai
     */
    private boolean isLastItemVisible() {
        final RecyclerView.Adapter<?> adapter = getRefreshableView().getAdapter();

        // 如果未设置Adapter或者Adapter没有数据可以上拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            return true;

        } else {
            // 最后一个条目View完全展示,可以刷新
            int lastVisiblePosition = getLastVisiblePosition();
            if(lastVisiblePosition >= mRecyclerView.getAdapter().getItemCount()-1) {
                return mRecyclerView.getChildAt(
                        mRecyclerView.getChildCount() - 1).getBottom() <= mRecyclerView
                        .getBottom();
            }
        }

        return false;
    }

    /**
     * @Description: 获取最后一个可见子View的位置下标
     *
     * @return int: 位置
     * @version 1.0
     * @date 2015-9-23
     * @Author zhou.wenkai
     */
    private int getLastVisiblePosition() {
        View lastVisibleChild = mRecyclerView.getChildAt(mRecyclerView
                .getChildCount() - 1);
        return lastVisibleChild != null ? mRecyclerView
                .getChildAdapterPosition(lastVisibleChild) : -1;
    }


}
