package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.FragmentAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentTopic;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicActivity extends FragmentActivity {

    @BindView(R.id.mTopic_return)
    ImageView mTopicReturn;
    @BindView(R.id.mTopic_tabs)
    TabLayout mTopicTabs;
    @BindView(R.id.mTopic_viewpager)
    ViewPager mTopicViewpager;
    @BindView(R.id.mTopic_myTopic)
    ImageView mTopicMyTopic;
    private List<String> titles;
    private int currentIndex=0;
    String [] titleStr =new String []{"关注","杂谈","兴趣","爆照","交友","生活","情感","官方"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        currentIndex=getIntent().getIntExtra("pid",-1);
        initViewPager();
    }

    private void initViewPager() {
        titles = new ArrayList<>();
        mTopicViewpager.setOffscreenPageLimit(titles.size());
        for (int i = 0; i < titles.size(); i++) {
            mTopicTabs.addTab(mTopicTabs.newTab().setText(titles.get(i)));
            titles.add(titleStr[i]);
        }
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(FragmentTopic.newInstance(i,i));
        }
        FragmentAdapter mFragmentAdapteradapter =
                new FragmentAdapter(this, getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mTopicViewpager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        mTopicTabs.setupWithViewPager(mTopicViewpager);
        //给TabLayout设置适配器
        mTopicTabs.setTabsFromPagerAdapter(mFragmentAdapteradapter);

        if(currentIndex!=-1){
            mTopicViewpager.setCurrentItem(currentIndex);
            mTopicMyTopic.setVisibility(View.GONE);
        }else{
            mTopicMyTopic.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.mTopic_return, R.id.mTopic_myTopic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mTopic_return:
                finish();
                break;
            case R.id.mTopic_myTopic:
                Intent intent=new Intent(this,MyOrOtherTopicActivity.class);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
        }
    }
}
