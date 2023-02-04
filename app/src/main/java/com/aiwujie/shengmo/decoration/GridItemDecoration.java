package com.aiwujie.shengmo.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiwujie.shengmo.utils.LogUtil;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    protected int mSpace = 10;

    private boolean mIncludeEdge = true;

    public GridItemDecoration(int space) {
        this.mSpace = space;
    }

    public GridItemDecoration(int space, boolean includeEdge) {
        this.mSpace = space;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        //列数
        int spanCount = gridLayoutManager.getSpanCount();
        int position = parent.getChildLayoutPosition(view);
        int column = (position) % spanCount;
        if (mIncludeEdge) {
            outRect.left = mSpace - column * mSpace / spanCount;
            outRect.right = (column + 1) * mSpace / spanCount;
            if (position < spanCount) {
                outRect.top = mSpace;
            }
            outRect.bottom = mSpace;
        } else {
            outRect.left = column * mSpace / spanCount;
            outRect.right = mSpace - (column + 1) * mSpace / spanCount;
            if (position >= spanCount) {
                outRect.top = mSpace;
            }
        }
        LogUtil.d(outRect.left + "-" + outRect.right + "-" + outRect.top + "-" + outRect.bottom);
    }
}
