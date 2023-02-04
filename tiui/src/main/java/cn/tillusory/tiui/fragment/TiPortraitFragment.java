package cn.tillusory.tiui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiPortrait;
import cn.tillusory.sdk.bean.TiSticker;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiPortraitAdapter;
import cn.tillusory.tiui.adapter.TiStickerAdapter;

/**
 * Created by Anko on 2018/12/1.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiPortraitFragment extends LazyFragment implements Observer {

    private List<TiPortrait> portraitList = new ArrayList<>();
    private TiSDKManager tiSDKManager;
    private TiPortraitAdapter tiPortraitAdapter;

    public TiPortraitFragment setTiSDKManager(TiSDKManager tiSDKManager) {
        this.tiSDKManager = tiSDKManager;
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

        setContentView(R.layout.fragment_ti_sticker);

        TiSDK.addObserver(this);

        if (getContext() == null) return;

        portraitList.clear();
        portraitList.add(TiPortrait.NO_Portrait);
        portraitList.addAll(TiPortrait.getAllPortraits(getContext()));

        RecyclerView tiPortraitRV = (RecyclerView) findViewById(R.id.tiRecyclerView);
        tiPortraitAdapter = new TiPortraitAdapter(portraitList, tiSDKManager);
        tiPortraitRV.setLayoutManager(new GridLayoutManager(getContext(), 5));
        tiPortraitRV.setAdapter(tiPortraitAdapter);
    }

    @Override
    public void update(Observable o, Object arg) {

        if (getContext() == null) return;

        portraitList.clear();
        portraitList.add(TiPortrait.NO_Portrait);
        portraitList.addAll(TiPortrait.getAllPortraits(getContext()));

        if (tiPortraitAdapter != null)
            tiPortraitAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        TiSDK.deleteObserver(this);
    }
}
