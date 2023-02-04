package cn.tillusory.tiui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiHairAdapter;
import cn.tillusory.tiui.model.TiHair;

/**
 * Created by Anko on 2018/12/1.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiHairFragment extends LazyFragment {

    private List<TiHair> hairList = new ArrayList<>();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

        setContentView(R.layout.fragment_ti_recyclerview);

        hairList.clear();
        hairList.addAll(Arrays.asList(TiHair.values()));

        RecyclerView tiHairRV = (RecyclerView) findViewById(R.id.tiRecyclerView);
        TiHairAdapter hairAdapter = new TiHairAdapter(hairList);
        tiHairRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tiHairRV.setAdapter(hairAdapter);
    }

}
