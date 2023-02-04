package com.tencent.qcloud.tim.uikit.modules.search.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;



public class PageRecycleView extends RecyclerView {

    protected OnLoadMoreHandler mHandler;

    public PageRecycleView(Context context) {
        super(context);
    }

    public PageRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLoadMoreMessageHandler(OnLoadMoreHandler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            if (mHandler != null) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                int firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastPosition == getAdapter().getItemCount() -1 && !mHandler.isListEnd(lastPosition)){
                    /*if (getAdapter() instanceof MessageListAdapter) {
                        ((MessageListAdapter) getAdapter()).showLoading();
                    }*/
                    mHandler.loadMore();
                }
            }
        }
    }

    public interface OnLoadMoreHandler {
        void loadMore();
        boolean isListEnd(int postion);
    }
}
