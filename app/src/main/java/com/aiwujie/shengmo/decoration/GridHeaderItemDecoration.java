package com.aiwujie.shengmo.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiwujie.shengmo.utils.LogUtil;

public class GridHeaderItemDecoration extends RecyclerView.ItemDecoration {
    protected int mSpace = 10;
    private int mHeadCount = 0;

    private boolean mIncludeEdge = true;

    public GridHeaderItemDecoration(int space,int headCount) {
        this.mSpace = space;
        this.mHeadCount = headCount;
    }

    public GridHeaderItemDecoration(int space,int headCount, boolean includeEdge) {
        this.mSpace = space;
        this.mIncludeEdge = includeEdge;
        this.mHeadCount = headCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        //列数
        int spanCount = gridLayoutManager.getSpanCount();
        int position = parent.getChildLayoutPosition(view);
        position = position - mHeadCount;
        int column = (position) % spanCount;
        if (mIncludeEdge && position >= 0) {
            outRect.left = mSpace - column * mSpace / spanCount;
            outRect.right = (column + 1) * mSpace / spanCount;
            if (position < spanCount) {
                outRect.top = mSpace;
            }
            outRect.bottom = mSpace;
        }
        LogUtil.d(outRect.left + "-" + outRect.right + "-" + outRect.top + "-" + outRect.bottom);
    }
}
