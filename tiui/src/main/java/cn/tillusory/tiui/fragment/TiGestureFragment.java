package cn.tillusory.tiui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiGesture;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiGestureAdapter;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiSelectedPosition;

/**
 * Created by Anko.
 * Copyright (c) 2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiGestureFragment extends LazyFragment implements Observer {
    private List<TiGesture> gestureList = new ArrayList<>();
    private TiSDKManager tiSDKManager;
    private TiGestureAdapter tiGestureAdapter;

    public TiGestureFragment setTiSDKManager(TiSDKManager tiSDKManager) {
        this.tiSDKManager = tiSDKManager;
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_ti_sticker);

        TiSDK.addObserver(this);

        if (getContext() == null) return;

        gestureList.clear();
        gestureList.add(TiGesture.NO_Gesture);
        gestureList.addAll(TiGesture.getAllGestures(getContext()));

        RecyclerView gestureRV = (RecyclerView) findViewById(R.id.tiRecyclerView);
        tiGestureAdapter = new TiGestureAdapter(gestureList, tiSDKManager);
        gestureRV.setLayoutManager(new GridLayoutManager(getContext(), 5));
        gestureRV.setAdapter(tiGestureAdapter);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        RxBus.get().post(RxBusAction.ACTION_SHOW_GESTURE, gestureList.get(TiSelectedPosition.POSITION_GESTURE).getHint());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (getContext() == null) return;

        gestureList.clear();
        gestureList.add(TiGesture.NO_Gesture);
        gestureList.addAll(TiGesture.getAllGestures(getContext()));

        if (tiGestureAdapter != null) {
            tiGestureAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        TiSDK.deleteObserver(this);
    }
}


