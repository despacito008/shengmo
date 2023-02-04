package com.aiwujie.shengmo.fragment.redwomenfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.RedwomenPersonCenterActivity;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.RedWomenApplyEvent;
import com.aiwujie.shengmo.eventbus.RedWomenStateEvent;
import com.aiwujie.shengmo.utils.AnimationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/8/11.
 */

public class RedWomenBeautifulFragment extends Fragment {

    @BindView(R.id.mRedWomen_beautiful_tabs)
    TabLayout mRedWomenBeautifulTabs;
    @BindView(R.id.mRedWomen_beautiful_viewpager)
    ViewPager mRedWomenBeautifulViewpager;
    @BindView(R.id.mRedWomen_beautiful_apply)
    ImageView mRedWomenBeautifulApply;

    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    private String redState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_redwomen_beautiful, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("才俊");
        mTitleList.add("佳丽");
        //添加页卡视图
        mViewList.add(new RedWomenBeautifulManFragment());
        mViewList.add(new RedWomenBeautifulGirlFragment());
        mRedWomenBeautifulTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mRedWomenBeautifulTabs.addTab(mRedWomenBeautifulTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mRedWomenBeautifulTabs.addTab(mRedWomenBeautifulTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getChildFragmentManager(), mTitleList, mViewList);
        mRedWomenBeautifulViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mRedWomenBeautifulTabs.setupWithViewPager(mRedWomenBeautifulViewpager);//将TabLayout和ViewPager关联起来。

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(RedWomenApplyEvent event) {
        switch (event.getFlag()) {
            case 0:
                if (mRedWomenBeautifulApply.getVisibility() == View.VISIBLE) {
                    mRedWomenBeautifulApply.setVisibility(View.GONE);
                    mRedWomenBeautifulApply.setAnimation(AnimationUtil.moveToViewBottom());
                }
                break;
            case 1:
                if (mRedWomenBeautifulApply.getVisibility() == View.GONE) {
                    mRedWomenBeautifulApply.setVisibility(View.VISIBLE);
                    mRedWomenBeautifulApply.setAnimation(AnimationUtil.moveToViewLocation());
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(RedWomenStateEvent event) {
        redState=event.getRedState();
        if (event.getRedState().equals("0")) {
            mRedWomenBeautifulApply.setImageResource(R.mipmap.sqhnfw);
        } else {
            mRedWomenBeautifulApply.setImageResource(R.mipmap.redgrzx);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            isFirstLoad = false;
            setData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.mRedWomen_beautiful_apply)
    public void onViewClicked() {
        Intent intent=new Intent(getActivity(),RedwomenPersonCenterActivity.class);
        intent.putExtra("uid", MyApp.uid);
        intent.putExtra("redState",redState);
        startActivity(intent);
    }
}
