package com.aiwujie.shengmo.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonLazyLoadFragment extends Fragment {

    private View mRootView;
    private boolean misVisibleToUser;
    private boolean mIsfirstgetdata = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(providelayoutId(),container,false);
        }
        initView(mRootView);
        return mRootView;
    }

    protected abstract void initView(View mRootView);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyloadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        misVisibleToUser = isVisibleToUser;
        lazyloadData();
    }


    public void lazyloadData() {
        if ((mIsfirstgetdata || setIsRealTimeRefresh())&&misVisibleToUser && mRootView!=null){
            mIsfirstgetdata=false;
            initData();
        }

    }

    protected abstract void initData();

    //fragment是否需要实时刷新，如果需要返回true，不需要返回false
    protected abstract boolean setIsRealTimeRefresh();

    protected abstract int providelayoutId();

}

