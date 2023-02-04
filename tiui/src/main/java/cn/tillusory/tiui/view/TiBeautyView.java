package cn.tillusory.tiui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.shizhefei.view.indicator.FragmentListPageAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.RecyclerIndicatorView;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiTypeEnum;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.TiPanelLayout;
import cn.tillusory.tiui.custom.DrawableTextView;
import cn.tillusory.tiui.custom.TiResetDialogFragment;
import cn.tillusory.tiui.custom.TiSharePreferences;
import cn.tillusory.tiui.fragment.TiBeautyFragment;
import cn.tillusory.tiui.fragment.TiDistortionFragment;
import cn.tillusory.tiui.fragment.TiFaceShapeFragment;
import cn.tillusory.tiui.fragment.TiFaceTrimFragment;
import cn.tillusory.tiui.fragment.TiFilterFragment;
import cn.tillusory.tiui.fragment.TiGestureFragment;
import cn.tillusory.tiui.fragment.TiGiftFragment;
import cn.tillusory.tiui.fragment.TiGreenScreenFragment;
import cn.tillusory.tiui.fragment.TiHairFragment;
import cn.tillusory.tiui.fragment.TiInteractionFragment;
import cn.tillusory.tiui.fragment.TiMakeupFragment;
import cn.tillusory.tiui.fragment.TiMaskFragment;
import cn.tillusory.tiui.fragment.TiPortraitFragment;
import cn.tillusory.tiui.fragment.TiQuickBeautyFragment;
import cn.tillusory.tiui.fragment.TiRockFragment;
import cn.tillusory.tiui.fragment.TiStickerFragment;
import cn.tillusory.tiui.fragment.TiWatermarkFragment;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiFaceShapeVal;
import cn.tillusory.tiui.model.TiMode;
import cn.tillusory.tiui.model.TiQuickBeautyVal;

/**
 * Created by Anko on 2018/5/12.
 * Copyright (c) 2018-2019 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiBeautyView extends LinearLayout {

    private TiSDKManager tiSDKManager;
    private RecyclerIndicatorView tiIndicatorView;

    private TiBarView tiBarView;
    private TiMakeupView tiMakeupView;
    private TiGreenScreenView tiGreenScreenView;

    private LinearLayout tiBeautyLL;
    private LinearLayout tiEnableLL;
    private TextView tiEnableTV;
    private ImageView tiEnableIV;
    private View tiDividerV;
    private ImageView tiBtnBack;
    private DrawableTextView tiBtnReset;
    private View tiBtnBottomContainer;
    private View tiIndicatorContainer;
    private View tiBtnBackCuteGroup;
    private View tiBtnBackCute;
    private ViewPager tiViewPager;

    private List<Integer> tiTabs = new ArrayList<>();

    private boolean isBeautyEnable = false;
    private boolean isFaceTrimEnable = false;
    private boolean isMakeupEnable = false;

    private boolean isFromHome = false;

    private int tiMode = TiMode.MODE_BEAUTY;
    private int tiSubMode = TiMode.SUB_MODE_QUICK_BEAUTY;
    private IndicatorViewPager indicatorViewPager;
    private IndicatorViewPager.IndicatorFragmentPagerAdapter fragmentPagerAdapter;

    public TiBeautyView(Context context) {
        super(context);
    }

    public TiBeautyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiBeautyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TiBeautyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TiBeautyView init(@NonNull TiSDKManager tiSDKManager) {
        this.tiSDKManager = tiSDKManager;

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
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_beauty, this);

        tiBarView = findViewById(R.id.tiBarView);
        tiBarView.init(tiSDKManager);

        tiMakeupView = findViewById(R.id.tiMakeupView);
        tiMakeupView.init();
        tiGreenScreenView = findViewById(R.id.tiGreenScreenView);
        tiGreenScreenView.init();

        tiIndicatorView = findViewById(R.id.tiIndicatorView);

        tiBeautyLL = findViewById(R.id.tiBeautyLL);
        tiEnableLL = findViewById(R.id.tiEnableLL);
        tiEnableTV = findViewById(R.id.tiEnableTV);
        tiEnableIV = findViewById(R.id.tiEnableIV);
        tiDividerV = findViewById(R.id.tiDividerV);
        tiBtnBack = findViewById(R.id.btn_back);
        tiBtnReset = findViewById(R.id.btn_reset);
        tiBtnBottomContainer = findViewById(R.id.btn_bottom_container);
        tiIndicatorContainer = findViewById(R.id.indicator_container);
        tiBtnBackCuteGroup = findViewById(R.id.btn_back_cute_group);
        tiBtnBackCute = findViewById(R.id.btn_back_cute);
        tiViewPager = findViewById(R.id.viewPager);
    }

    private void initData() {

        //屏蔽点击事件
        setOnClickListener(null);

        tiEnableLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiMode == TiMode.MODE_BEAUTY) {
                    if (tiSubMode == TiMode.SUB_MODE_BEAUTY) {
                        isBeautyEnable = !isBeautyEnable;
                        tiSDKManager.setBeautyEnable(isBeautyEnable);
                        tiEnableIV.setSelected(isBeautyEnable);
                        tiEnableTV.setSelected(isBeautyEnable);
                        tiEnableTV.setText(isBeautyEnable ? R.string.ti_beauty_on : R.string.ti_beauty_off);
                        tiBarView.selectBeauty();
                    } else if (tiSubMode == TiMode.SUB_MODE_FACE_TRIM) {
                        isFaceTrimEnable = !isFaceTrimEnable;
                        tiSDKManager.setFaceTrimEnable(isFaceTrimEnable);
                        tiEnableIV.setSelected(isFaceTrimEnable);
                        tiEnableTV.setSelected(isFaceTrimEnable);
                        tiEnableTV.setText(isFaceTrimEnable ? R.string.ti_face_trim_on : R.string.ti_face_trim_off);
                        tiBarView.selectFaceTrim();
                    }
                } else if (tiMode == TiMode.MODE_MAKEUP) {
                    isMakeupEnable = !isMakeupEnable;
                    tiSDKManager.enableMakeup(isMakeupEnable);
                    TiSharePreferences.getInstance().putMakeupEnable(isMakeupEnable);
                    tiEnableIV.setSelected(isMakeupEnable);
                    tiEnableTV.setSelected(isMakeupEnable);
                    tiEnableTV.setText(isMakeupEnable ? R.string.makeup_on : R.string.makeup_off);
                }
            }
        });

        tiBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(RxBusAction.ACTION_HIDE_MODE_CONTAINER, true);
            }
        });

        tiBtnBackCute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(RxBusAction.ACTION_HIDE_MODE_CONTAINER, true);
            }
        });

        tiBtnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) getContext();
                TiResetDialogFragment fragment = new TiResetDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(TiResetDialogFragment.BEAUTY_MODE, tiMode);
                fragment.setArguments(bundle);
                fragment.show(activity.getSupportFragmentManager(), "TiResetDialogFragment");
            }
        });

        tiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (tiMode == TiMode.MODE_BEAUTY) {
//                    switch (position) {
//                        case 0:
//                            chooseQuickBeauty();
//                            break;
//                        case 1:
//                            chooseBeauty();
//                            break;
//                        case 2:
//                            chooseFaceShape();
//                            break;
//                        case 3:
//                            chooseFaceTrim();
//                            break;
//                    }

                    switch (position) {
                        case 0:
                            chooseBeauty();
                            break;
                        case 1:
                            chooseFaceTrim();
                            break;
                    }
                } else if (tiMode == TiMode.MODE_FILTER) {
                    if (position == 0) {
                        tiBarView.selectFilter();
                    } else {
                        tiBarView.hideSeekBar();
                    }
                } else if (tiMode == TiMode.MODE_MAKEUP) {
                    if (position == 0) {
                        chooseMakeup();
                    } else {
                        chooseHair();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        tiTabs.clear();
//        tiTabs.add(R.string.quick_beauty);
//        tiTabs.add(TiTypeEnum.Beauty.getStringId());
//        tiTabs.add(R.string.face_shape);
//        tiTabs.add(TiTypeEnum.FaceTrim.getStringId());

        indicatorViewPager = new IndicatorViewPager(tiIndicatorView, tiViewPager);
        indicatorViewPager.setIndicatorScrollBar(new LayoutBar(getContext(), R.layout.layout_indicator_tab));
        indicatorViewPager.setPageOffscreenLimit(6);
        fragmentPagerAdapter = new IndicatorViewPager.IndicatorFragmentPagerAdapter(((FragmentActivity) getContext()).getSupportFragmentManager()) {
                    @Override
                    public int getCount() {
                        return tiTabs.size();
                    }

                    @Override
                    public View getViewForTab(int position, View convertView, ViewGroup container) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ti_tab, container, false);
                        ((TextView) convertView).setText(convertView.getContext().getResources().getString(tiTabs.get(position)));
                        return convertView;
                    }

                    @Override
                    public int getItemPosition(Object object) {
                        return FragmentListPageAdapter.POSITION_NONE;
                    }

                    @Override
                    public Fragment getFragmentForPage(int position) {
                        if (tiMode == TiMode.MODE_BEAUTY) {
//                            switch (position) {
//                                case 0:
//                                    return new TiQuickBeautyFragment();
//                                case 1:
//                                    return new TiBeautyFragment();
//                                case 2:
//                                    return new TiFaceShapeFragment();
//                                case 3:
//                                    if (isFromHome && tiSubMode == TiMode.SUB_MODE_FACE_SHAPE) {
//                                        tiViewPager.setCurrentItem(2);
//                                    }
//                                    return new TiFaceTrimFragment();
//                            }

                            switch (position) {
                                case 0:
                                    return new TiBeautyFragment();
                                case 1:
                                    if (isFromHome && tiSubMode == TiMode.SUB_MODE_FACE_SHAPE) {
                                        tiViewPager.setCurrentItem(2);
                                    }
                                    return new TiFaceTrimFragment();
                            }
                        } else if (tiMode == TiMode.MODE_FILTER) {
                            switch (position) {
                                case 0:
                                    return new TiFilterFragment();
                                case 1:
                                    return new TiRockFragment().setTiSDKManager(tiSDKManager);
                                case 2:
                                    return new TiDistortionFragment().setTiSDKManager(tiSDKManager);
                            }
                        } else if (tiMode == TiMode.MODE_CUTE) {
//                            switch (position) {
//                                case 0:
//                                    return new TiStickerFragment().setTiSDKManager(tiSDKManager);
//                                case 1:
//                                    return new TiInteractionFragment().setTiSDKManager(tiSDKManager);
//                                case 2:
//                                    return new TiMaskFragment().setTiSDKManager(tiSDKManager);
//                                case 3:
//                                    return new TiGiftFragment().setTiSDKManager(tiSDKManager);
//                                case 4:
//                                    return new TiWatermarkFragment().setTiSDKManager(tiSDKManager);
//                                case 5:
//                                    return new TiGreenScreenFragment().setTiSDKManager(tiSDKManager);
//                                case 6:
//                                    return new TiPortraitFragment().setTiSDKManager(tiSDKManager);
//                                case 7:
//                                    return new TiGestureFragment().setTiSDKManager(tiSDKManager);
//                            }

                            switch (position) {
                                case 0:
                                    return new TiStickerFragment().setTiSDKManager(tiSDKManager);
                                case 1:
                                    return new TiInteractionFragment().setTiSDKManager(tiSDKManager);
                                case 2:
                                    return new TiMaskFragment().setTiSDKManager(tiSDKManager);
                                case 3:
                                    return new TiWatermarkFragment().setTiSDKManager(tiSDKManager);
                                case 4:
                                    return new TiGreenScreenFragment().setTiSDKManager(tiSDKManager);
                                case 5:
                                    return new TiPortraitFragment().setTiSDKManager(tiSDKManager);
                            }
                        } else if (tiMode == TiMode.MODE_MAKEUP) {
                            switch (position) {
                                case 0:
                                    return new TiMakeupFragment();
                                case 1:
                                    return new TiHairFragment();
                            }
                        }
                        return null;
                    }
                };

        indicatorViewPager.setAdapter(fragmentPagerAdapter);
    }

    public void refreshData(int mode, boolean isFromHome, int subMode) {
        this.isFromHome = isFromHome;

        tiViewPager.setCurrentItem(0);
        tiIndicatorView.setCurrentItem(0);

        tiMode = mode;
        tiTabs.clear();
        if (tiMode == TiMode.MODE_BEAUTY) {
           // tiTabs.add(R.string.quick_beauty);
            tiTabs.add(TiTypeEnum.Beauty.getStringId());
            //tiTabs.add(R.string.face_shape);
            tiTabs.add(TiTypeEnum.FaceTrim.getStringId());
            chooseBeauty();
            //chooseQuickBeauty();
        } else if (tiMode == TiMode.MODE_FILTER) {
            tiTabs.add(TiTypeEnum.Filter.getStringId());
            tiTabs.add(TiTypeEnum.Rock.getStringId());
            tiTabs.add(TiTypeEnum.Distortion.getStringId());
            tiEnableLL.setVisibility(GONE);
            tiDividerV.setVisibility(GONE);
            tiBarView.selectFilter();
            if (onBeautyClickListener != null) {
                onBeautyClickListener.onBeautyClick();
            }
        } else if (tiMode == TiMode.MODE_CUTE) {
            tiTabs.add(TiTypeEnum.Sticker.getStringId());
            tiTabs.add(R.string.interaction);
            tiTabs.add(TiTypeEnum.Mask.getStringId());
           // tiTabs.add(TiTypeEnum.Gift.getStringId());
            tiTabs.add(TiTypeEnum.Watermark.getStringId());
            tiTabs.add(TiTypeEnum.GreenScreen.getStringId());
            tiTabs.add(TiTypeEnum.PORTRAIT.getStringId());
           // tiTabs.add(R.string.gesture);

            tiEnableLL.setVisibility(GONE);
            tiDividerV.setVisibility(GONE);

            tiBarView.hideSeekBar();

            if (onBeautyClickListener != null) {
                onBeautyClickListener.onBeautyClick();
            }
        } else if (tiMode == TiMode.MODE_MAKEUP) {
            tiTabs.add(TiTypeEnum.Makeup.getStringId());
            tiTabs.add(TiTypeEnum.HAIR.getStringId());

            isMakeupEnable = TiSharePreferences.getInstance().isMakeupEnable();
            tiSDKManager.enableMakeup(isMakeupEnable);
            tiEnableIV.setSelected(isMakeupEnable);
            tiEnableTV.setSelected(isMakeupEnable);
            tiEnableTV.setText(isMakeupEnable ? R.string.makeup_on : R.string.makeup_off);

            chooseMakeup();
        }

        //tiIndicatorView和背景是一致的
        if (!TiPanelLayout.isFullRatio && tiMode != TiMode.MODE_CUTE) {
            tiIndicatorView.setOnTransitionListener(new OnTransitionTextListener()
                    .setColor(getResources().getColor(R.color.ti_selected), getResources().getColor(R.color.ti_unselected)));
            changeViewByRatio(false);
        } else {
            tiIndicatorView.setOnTransitionListener(new OnTransitionTextListener()
                    .setColor(getResources().getColor(R.color.ti_selected), getResources().getColor(R.color.white)));
            changeViewByRatio(true);
        }
        controlBtnBottomContainer(tiMode);

        tiIndicatorView.getAdapter().notifyDataSetChanged();
        fragmentPagerAdapter.notifyDataSetChanged();

        if (isFromHome) {
            if (subMode != 0) {
                tiSubMode = subMode;
                switch (subMode) {
//                    case TiMode.SUB_MODE_FACE_SHAPE:
//                        tiViewPager.setCurrentItem(2);
//                        break;
                    case TiMode.SUB_MODE_PORTRAIT:
                        tiViewPager.setCurrentItem(6);
                        break;
                }
            }
        }
    }

    private void chooseQuickBeauty() {
        tiSubMode = TiMode.SUB_MODE_QUICK_BEAUTY;

        tiEnableLL.setVisibility(GONE);
        tiDividerV.setVisibility(GONE);

        tiBarView.selectQuickBeauty(false);
    }

    private void chooseBeauty() {
        tiSubMode = TiMode.SUB_MODE_BEAUTY;

        tiEnableLL.setVisibility(VISIBLE);
        tiDividerV.setVisibility(VISIBLE);

        tiBarView.selectBeauty();

        isBeautyEnable = tiSDKManager.isBeautyEnable();
        tiEnableIV.setSelected(isBeautyEnable);
        tiEnableTV.setSelected(isBeautyEnable);
        tiEnableTV.setText(isBeautyEnable ? R.string.ti_beauty_on : R.string.ti_beauty_off);
    }

    private void chooseFaceShape() {
        tiSubMode = TiMode.SUB_MODE_FACE_SHAPE;

        tiEnableLL.setVisibility(GONE);
        tiDividerV.setVisibility(GONE);

        tiBarView.selectFaceShape(false);
    }

    private void chooseFaceTrim() {
        tiSubMode = TiMode.SUB_MODE_FACE_TRIM;

        tiEnableLL.setVisibility(VISIBLE);
        tiDividerV.setVisibility(VISIBLE);

        tiBarView.selectFaceTrim();

        isFaceTrimEnable = tiSDKManager.isFaceTrimEnable();
        tiEnableIV.setSelected(isFaceTrimEnable);
        tiEnableTV.setSelected(isFaceTrimEnable);
        tiEnableTV.setText(isFaceTrimEnable ? R.string.ti_face_trim_on : R.string.ti_face_trim_off);
    }

    private void chooseMakeup() {
        tiBtnReset.setVisibility(VISIBLE);
        tiEnableLL.setVisibility(VISIBLE);
        tiDividerV.setVisibility(VISIBLE);

        tiBarView.hideSeekBar();
    }

    private void chooseHair() {
        tiBtnReset.setVisibility(GONE);
        tiEnableLL.setVisibility(GONE);
        tiDividerV.setVisibility(GONE);

        tiBarView.selectHair();
    }

    private void controlBtnBottomContainer(int mode) {
        if (mode == TiMode.MODE_FILTER) {
            tiBtnBottomContainer.setVisibility(VISIBLE);
            tiBtnReset.setVisibility(GONE);
            tiBtnBackCuteGroup.setVisibility(GONE);
        } else if (mode == TiMode.MODE_CUTE) {
            tiBtnBottomContainer.setVisibility(GONE);
            tiBtnBackCuteGroup.setVisibility(VISIBLE);
        } else {
            tiBtnBottomContainer.setVisibility(VISIBLE);
            tiBtnReset.setVisibility(VISIBLE);
            tiBtnBackCuteGroup.setVisibility(GONE);
            if (mode == TiMode.MODE_BEAUTY) {
                tiBtnReset.setEnabled(TiSharePreferences.getInstance().isBeautyResetEnable());
            } else {
                tiBtnReset.setEnabled(TiSharePreferences.getInstance().isMakeupResetEnable());
            }
        }
    }

    public boolean hideMakeupView() {
        if (tiMakeupView != null && tiMakeupView.getVisibility() == VISIBLE) {
            showMakeupView(RxBusAction.ACTION_MAKEUP_BACK);
            return true;
        }
        return false;
    }

    public boolean hideGreenScreenView() {
        if (tiGreenScreenView != null && tiGreenScreenView.getVisibility() == VISIBLE) {
            showGreenScreenView(false);
            return true;
        }
        return false;
    }

    public void changeViewByRatio(boolean isFull) {
        if (isFull) {
            tiIndicatorContainer.setBackgroundColor(getResources().getColor(R.color.ti_bg_cute2));
            tiBeautyLL.setBackgroundColor(getResources().getColor(R.color.ti_bg_cute2));
            tiEnableTV.setTextColor(getResources().getColorStateList(R.color.color_ti_selector_full));
            tiDividerV.setBackgroundColor(getResources().getColor(R.color.ti_divider_full));
            tiBtnBack.setImageResource(R.drawable.ic_ti_mode_back_white);
            tiBtnReset.setLeftDrawable(getResources().getDrawable(R.drawable.ic_ti_reset_full));
            tiBtnReset.setTextColor(getResources().getColorStateList(R.color.color_ti_selector_reset_full));
            //改变tiMakeupView背景
            tiMakeupView.changeViewByRatio(true);
        } else {
            tiIndicatorContainer.setBackgroundColor(getResources().getColor(R.color.white));
            tiBeautyLL.setBackgroundColor(getResources().getColor(R.color.white));
            tiEnableTV.setTextColor(getResources().getColorStateList(R.color.color_ti_selector_not_full));
            tiDividerV.setBackgroundColor(getResources().getColor(R.color.ti_divider));
            tiBtnBack.setImageResource(R.drawable.ic_ti_mode_back_black);
            tiBtnReset.setLeftDrawable(getResources().getDrawable(R.drawable.ic_ti_reset));
            tiBtnReset.setTextColor(getResources().getColorStateList(R.color.color_ti_selector_reset));
            //改变tiMakeupView背景
            tiMakeupView.changeViewByRatio(false);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_ENABLED_BTN_RESET))
    public void setBtnResetEnabled(Boolean enabled) {
        if (enabled) {
            if (!tiBtnReset.isEnabled()) {
                tiBtnReset.setEnabled(true);
                if (tiMode == TiMode.MODE_BEAUTY) {
                    TiSharePreferences.getInstance().putBeautyResetEnable(true);
                } else if (tiMode == TiMode.MODE_MAKEUP) {
                    TiSharePreferences.getInstance().putMakeupResetEnable(true);
                }
            }
        } else {
            if (tiBtnReset.isEnabled()) {
                tiBtnReset.setEnabled(false);
                if (tiMode == TiMode.MODE_BEAUTY) {
                    TiSharePreferences.getInstance().putBeautyResetEnable(false);
                    setBarViewProgressBeauty();
                } else if (tiMode == TiMode.MODE_MAKEUP) {
                    TiSharePreferences.getInstance().putMakeupResetEnable(false);
                    setBarViewProgressMakeup();
                }
            }
        }
    }

    private void setBarViewProgressBeauty() {
        RxBus.get().post(RxBusAction.ACTION_RESET_BEAUTY, true);
        switch (tiSubMode) {
            case TiMode.SUB_MODE_QUICK_BEAUTY:
                tiBarView.showQuickBeautyProgressOnly(TiQuickBeautyVal.STANDARD_QUICK_BEAUTY);
                break;
            case TiMode.SUB_MODE_BEAUTY:
                tiBarView.selectBeauty();
                break;
            case TiMode.SUB_MODE_FACE_SHAPE:
                tiBarView.showFaceShapeProgressOnly(TiFaceShapeVal.CLASSIC_FACE_SHAPE);
                break;
            case TiMode.SUB_MODE_FACE_TRIM:
                tiBarView.selectFaceTrim();
                break;
        }
    }

    private void setBarViewProgressMakeup() {
        tiBarView.selectMakeupBlusher();
        tiBarView.selectMakeupEyelash();
        tiBarView.selectMakeupEyebrow();
        tiBarView.selectMakeupEyeshadow();
        tiBarView.selectMakeupEyeline();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_SHOW_GREEN_SCREEN))
    public void showGreenScreenView(Boolean isShow) {
        if (isShow) {
            tiIndicatorContainer.setVisibility(GONE);
            tiBeautyLL.setVisibility(GONE);
            tiGreenScreenView.setVisibility(VISIBLE);
            tiBarView.selectGreenScreenEdit();
        } else {
            tiIndicatorContainer.setVisibility(VISIBLE);
            tiBeautyLL.setVisibility(VISIBLE);
            tiGreenScreenView.setVisibility(GONE);
            tiBarView.hideSeekBar();
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void showMakeupView(String action) {
        switch (action) {
            case RxBusAction.ACTION_MAKEUP_BACK:
                tiIndicatorContainer.setVisibility(VISIBLE);
                tiBeautyLL.setVisibility(VISIBLE);
                tiMakeupView.setVisibility(GONE);
                tiBarView.hideSeekBar();
                break;
            case RxBusAction.ACTION_BLUSHER:
                tiIndicatorContainer.setVisibility(GONE);
                tiBeautyLL.setVisibility(GONE);
                tiMakeupView.setVisibility(VISIBLE);
                tiBarView.selectMakeupBlusher();
                break;
            case RxBusAction.ACTION_EYELASH:
                tiIndicatorContainer.setVisibility(GONE);
                tiBeautyLL.setVisibility(GONE);
                tiMakeupView.setVisibility(VISIBLE);
                tiBarView.selectMakeupEyelash();
                break;
            case RxBusAction.ACTION_EYEBROW:
                tiIndicatorContainer.setVisibility(GONE);
                tiBeautyLL.setVisibility(GONE);
                tiMakeupView.setVisibility(VISIBLE);
                tiBarView.selectMakeupEyebrow();
                break;
            case RxBusAction.ACTION_EYESHADOW:
                tiIndicatorContainer.setVisibility(GONE);
                tiBeautyLL.setVisibility(GONE);
                tiMakeupView.setVisibility(VISIBLE);
                tiBarView.selectMakeupEyeshadow();
                break;
            case RxBusAction.ACTION_EYELINE:
                tiIndicatorContainer.setVisibility(GONE);
                tiBeautyLL.setVisibility(GONE);
                tiMakeupView.setVisibility(VISIBLE);
                tiBarView.selectMakeupEyeline();
                break;
        }
    }

    public interface OnBeautyClickListener {
        void onBeautyClick();
    }

    public OnBeautyClickListener onBeautyClickListener;

    public void setOnBeautyItemClick(OnBeautyClickListener onBeautyClickListener) {
        this.onBeautyClickListener = onBeautyClickListener;
    }


}


