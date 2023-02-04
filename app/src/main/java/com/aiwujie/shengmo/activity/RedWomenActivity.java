package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.RedWomenStateEvent;
import com.aiwujie.shengmo.fragment.redwomenfragment.RedWomenBeautifulFragment;
import com.aiwujie.shengmo.fragment.redwomenfragment.RedWomenServiceFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RedWomenActivity extends AppCompatActivity {

    @BindView(R.id.mRedWomen_return)
    ImageView mRedWomenReturn;
    @BindView(R.id.mRedWomen_tabs)
    TabLayout mRedWomenTabs;
    @BindView(R.id.mRedWomen_viewpager)
    ViewPager mRedWomenViewpager;
    @BindView(R.id.activity_red_women)
    PercentLinearLayout activityRedWomen;
    @BindView(R.id.mRedWomen_applyService)
    TextView mRedWomenApplyService;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private Handler handler=new Handler();
    private String redState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_women);
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);
        setData();
        getMatchState();
    }


    private void setData() {
        //添加页卡标题
//        mTitleList.add("服务介绍");
//        mTitleList.add("才俊佳丽");
        //添加页卡视图
        mViewList.add(new RedWomenServiceFragment());
//        mViewList.add(new RedWomenBeautifulFragment());
//        mRedWomenTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
//        mRedWomenTabs.addTab(mRedWomenTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
//        mRedWomenTabs.addTab(mRedWomenTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mRedWomenViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
//        mRedWomenTabs.setupWithViewPager(mRedWomenViewpager);//将TabLayout和ViewPager关联起来。
////        修改下划线的长度
//        mRedWomenTabs.post(new Runnable() {
//            @Override
//            public void run() {
//                TablayoutLineWidthUtils.setIndicator(mRedWomenTabs, 60, 60);
//            }
//        });
    }

    private void getMatchState() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMatchState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object=new JSONObject(response);
                            JSONObject obj=object.getJSONObject("data");
                            //是否是牵线会员
                            redState=obj.getString("match_state");
                            if (redState.equals("0")) {
                                SharedPreferencesUtils.setParam(getApplicationContext(), "match_state", "0");
                                mRedWomenApplyService.setText("申请服务");
                            } else {
                                SharedPreferencesUtils.setParam(getApplicationContext(), "match_state", "1");
                                mRedWomenApplyService.setText("个人中心");
                            }
//                            mRedWomenApplyService.setVisibility(View.VISIBLE);
                            EventBus.getDefault().post(new RedWomenStateEvent(redState));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @OnClick({R.id.mRedWomen_return,R.id.mRedWomen_applyService})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRedWomen_return:
                finish();
                break;
            case R.id.mRedWomen_applyService:
                Intent intent=new Intent(this,RedwomenPersonCenterActivity.class);
                intent.putExtra("uid",MyApp.uid);
                startActivity(intent);
//                Intent intent = new Intent(RedWomenActivity.this, PesonInfoActivity.class);
//                intent.putExtra("uid", markerUid01);
//                startActivity(intent);
                break;
        }
    }
}
