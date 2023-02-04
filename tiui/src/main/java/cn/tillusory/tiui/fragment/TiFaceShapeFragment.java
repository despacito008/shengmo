package cn.tillusory.tiui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiFaceShapeAdapter;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiFaceShape;

/**
 * Created by Anko on 2018/12/1.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiFaceShapeFragment extends LazyFragment {

    private TiFaceShapeAdapter adapter;
    private List<TiFaceShape> list = new ArrayList<>();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

        RxBus.get().register(this);

        setContentView(R.layout.fragment_ti_recyclerview);

        list.clear();
        list.addAll(Arrays.asList(TiFaceShape.values()));

        RecyclerView tiFaceShapeRV = (RecyclerView) findViewById(R.id.tiRecyclerView);
        adapter = new TiFaceShapeAdapter(list);
        tiFaceShapeRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tiFaceShapeRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();

        if(RxBus.get().hasRegistered(this)) { RxBus.get().unregister(this); }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_RESET_BEAUTY))
    public void notifyChanged(Boolean bool) {
        if (adapter != null) {
            adapter.setSelectedPosition(0);
        }
    }


}


