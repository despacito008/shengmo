package cn.tillusory.tiui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.tillusory.sdk.bean.TiMakeup;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiBlusherAdapter;
import cn.tillusory.tiui.adapter.TiEyeBrowAdapter;
import cn.tillusory.tiui.adapter.TiEyeLineAdapter;
import cn.tillusory.tiui.adapter.TiEyeShadowAdapter;
import cn.tillusory.tiui.adapter.TiEyelashAdapter;
import cn.tillusory.tiui.custom.DrawableTextView;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiSelectedPosition;
import cn.tillusory.tiui.model.TiMakeupText;

/**
 * Created by Anko on 2019-09-06.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiMakeupView extends LinearLayout {
    private static final int MODE_BLUSHER = 10;
    private static final int MODE_EYELASH = 20;
    private static final int MODE_EYEBROW = 30;
    private static final int MODE_EYESHADOW = 40;
    private static final int MODE_EYELINE = 50;

    private int tiMakeupMode = MODE_BLUSHER;

    private View tiMakeupRoot;
    private ImageView tiMakeupBackIV;
    private TextView tiMakeupTV;
    private DrawableTextView tiBtnNone;
    private RecyclerView tiMakeupRV;
    private TiBlusherAdapter blusherAdapter;
    private TiEyelashAdapter eyelashAdapter;
    private TiEyeBrowAdapter eyeBrowAdapter;
    private TiEyeShadowAdapter eyeShadowAdapter;
    private TiEyeLineAdapter eyeLineAdapter;
    private List<TiMakeup> blusherList = new ArrayList<>();
    private List<TiMakeup> eyelashList = new ArrayList<>();
    private List<TiMakeup> eyebrowList = new ArrayList<>();
    private List<TiMakeup> eyeshadowList = new ArrayList<>();
    private List<TiMakeup> eyelineList = new ArrayList<>();
    private List<TiMakeupText> blusherTextList = new ArrayList<>();
    private List<TiMakeupText> eyelashTextList = new ArrayList<>();
    private List<TiMakeupText> eyebrowTextList = new ArrayList<>();
    private List<TiMakeupText> eyeshadowTextList = new ArrayList<>();
    private List<TiMakeupText> eyelineTextList = new ArrayList<>();

    public TiMakeupView(Context context) {
        super(context);
    }

    public TiMakeupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiMakeupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TiMakeupView init() {
        RxBus.get().register(this);

        initView();

        initData();

        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(RxBus.get().hasRegistered(this)) { RxBus.get().unregister(this); }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_makeup, this);

        tiMakeupRoot = findViewById(R.id.tiMakeupRoot);
        tiMakeupBackIV = findViewById(R.id.tiMakeupBackIV);
        tiMakeupTV = findViewById(R.id.tiMakeupTV);
        tiBtnNone = findViewById(R.id.tiBtnNone);
        tiMakeupRV = findViewById(R.id.tiMakeupRV);
    }


    private void initData() {
        tiMakeupBackIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(RxBusAction.ACTION_MAKEUP_BACK);
            }
        });

        tiBtnNone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tiBtnNone.isSelected()) {
                    tiBtnNone.setSelected(true);

                    switch (tiMakeupMode) {
                        case MODE_BLUSHER:
                            RxBus.get().post(RxBusAction.ACTION_BLUSHER, "");
                            blusherAdapter.setSelectedPosition(-1);
                            break;
                        case MODE_EYELASH:
                            RxBus.get().post(RxBusAction.ACTION_EYELASH, "");
                            eyelashAdapter.setSelectedPosition(-1);
                            break;
                        case MODE_EYEBROW:
                            RxBus.get().post(RxBusAction.ACTION_EYEBROW, "");
                            eyeBrowAdapter.setSelectedPosition(-1);
                            break;
                        case MODE_EYESHADOW:
                            RxBus.get().post(RxBusAction.ACTION_EYESHADOW, "");
                            eyeShadowAdapter.setSelectedPosition(-1);
                            break;
                        case MODE_EYELINE:
                            RxBus.get().post(RxBusAction.ACTION_EYELINE, "");
                            eyeLineAdapter.setSelectedPosition(-1);
                            break;
                    }
                }

            }
        });

        blusherList.clear();
//        blusherList.add(TiMakeup.NO_MAKEUP);
        if (TiMakeup.getAllMakeups(getContext()).size() >= 6) {
            blusherList.addAll(TiMakeup.getAllMakeups(getContext()).subList(0, 6));
        }
        blusherTextList.clear();
//        blusherTextList.add(TiMakeupText.NO_MAKEUP);
        blusherTextList.addAll(Arrays.asList(TiMakeupText.values()).subList(1, 7));
        blusherAdapter = new TiBlusherAdapter(blusherList, blusherTextList);

        eyelashList.clear();
//        eyelashList.add(TiMakeup.NO_MAKEUP);
        eyelashList.addAll(TiMakeup.getAllMakeups(getContext()).subList(12, 18));
        eyelashTextList.clear();
//        eyelashTextList.add(TiMakeupText.NO_MAKEUP);
        eyelashTextList.addAll(Arrays.asList(TiMakeupText.values()).subList(13, 19));
        eyelashAdapter = new TiEyelashAdapter(eyelashList, eyelashTextList);

        eyebrowList.clear();
//        eyebrowList.add(TiMakeup.NO_MAKEUP);
        eyebrowList.addAll(TiMakeup.getAllMakeups(getContext()).subList(6, 12));
        eyebrowTextList.clear();
//        eyebrowTextList.add(TiMakeupText.NO_MAKEUP);
        eyebrowTextList.addAll(Arrays.asList(TiMakeupText.values()).subList(7, 13));
        eyeBrowAdapter = new TiEyeBrowAdapter(eyebrowList, eyebrowTextList);

        eyeshadowList.clear();
//        eyeshadowList.add(TiMakeup.NO_MAKEUP);
        eyeshadowList.addAll(TiMakeup.getAllMakeups(getContext()).subList(18, 24));
        eyeshadowTextList.clear();
//        eyeshadowTextList.add(TiMakeupText.NO_MAKEUP);
        eyeshadowTextList.addAll(Arrays.asList(TiMakeupText.values()).subList(19, 25));
        eyeShadowAdapter = new TiEyeShadowAdapter(eyeshadowList, eyeshadowTextList);

        eyelineList.clear();
//        eyelineList.add(TiMakeup.NO_MAKEUP);
        eyelineList.addAll(TiMakeup.getAllMakeups(getContext()).subList(24, 31));
        eyelineTextList.clear();
//        eyelineTextList.add(TiMakeupText.NO_MAKEUP);
        eyelineTextList.addAll(Arrays.asList(TiMakeupText.values()).subList(25, 32));
        eyeLineAdapter = new TiEyeLineAdapter(eyelineList, eyelineTextList);


        tiMakeupRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void changeViewByRatio(boolean isFull) {
        if (isFull) {
            tiMakeupRoot.setBackgroundColor(getResources().getColor(R.color.ti_bg_cute));
            tiMakeupTV.setTextColor(getResources().getColor(R.color.white));
            tiBtnNone.setTopDrawable(getResources().getDrawable(R.drawable.ic_ti_none_makeup_full));
            tiBtnNone.setTextColor(getResources().getColorStateList(R.color.color_ti_selector_full));
            tiMakeupBackIV.setImageResource(R.drawable.ic_ti_mode_back_white);
        } else {
            tiMakeupRoot.setBackgroundColor(getResources().getColor(R.color.white));
            tiMakeupTV.setTextColor(getResources().getColor(R.color.ti_unselected));
            tiBtnNone.setTopDrawable(getResources().getDrawable(R.drawable.ic_ti_none_makeup));
            tiBtnNone.setTextColor(getResources().getColorStateList(R.color.color_ti_selector_not_full));
            tiMakeupBackIV.setImageResource(R.drawable.ic_ti_mode_back_black);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void selectMakeup(String action) {
        switch (action) {
            case RxBusAction.ACTION_BLUSHER:
                tiBtnNone.setSelected(TiSelectedPosition.POSITION_BLUSHER == -1);
                tiMakeupMode = MODE_BLUSHER;
                tiMakeupTV.setText(R.string.blusher);
                tiMakeupRV.setAdapter(blusherAdapter);
                break;
            case RxBusAction.ACTION_EYELASH:
                tiBtnNone.setSelected(TiSelectedPosition.POSITION_EYELASH == -1);
                tiMakeupMode = MODE_EYELASH;
                tiMakeupTV.setText(R.string.eyelash);
                tiMakeupRV.setAdapter(eyelashAdapter);
                break;
            case RxBusAction.ACTION_EYEBROW:
                tiBtnNone.setSelected(TiSelectedPosition.POSITION_EYEBROW == -1);
                tiMakeupMode = MODE_EYEBROW;
                tiMakeupTV.setText(R.string.eyebrow);
                tiMakeupRV.setAdapter(eyeBrowAdapter);
                break;
            case RxBusAction.ACTION_EYESHADOW:
                tiBtnNone.setSelected(TiSelectedPosition.POSITION_EYESHADOW == -1);
                tiMakeupMode = MODE_EYESHADOW;
                tiMakeupTV.setText(R.string.eyeshadow);
                tiMakeupRV.setAdapter(eyeShadowAdapter);
                break;
            case RxBusAction.ACTION_EYELINE:
                tiBtnNone.setSelected(TiSelectedPosition.POSITION_EYELINE == -1);
                tiMakeupMode = MODE_EYELINE;
                tiMakeupTV.setText(R.string.eyeline);
                tiMakeupRV.setAdapter(eyeLineAdapter);
                break;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(RxBusAction.ACTION_BLUSHER),
            @Tag(RxBusAction.ACTION_EYELASH),
            @Tag(RxBusAction.ACTION_EYEBROW),
            @Tag(RxBusAction.ACTION_EYESHADOW),
            @Tag(RxBusAction.ACTION_EYELINE)
    })
    public void busAllMakeup(String name) {
        if (name.equals(TiMakeup.NO_MAKEUP.getName())) {
            return;
        }
        if (tiBtnNone.isSelected()) {
            tiBtnNone.setSelected(false);
        }
    }




}


