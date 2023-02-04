package com.aiwujie.shengmo.fragment.redwomenfragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.RedWomenEditMsgActivity;
import com.aiwujie.shengmo.adapter.RecyclerViewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.RedWomenStep2Data;
import com.aiwujie.shengmo.eventbus.RedWomenMatchStateEvent;
import com.aiwujie.shengmo.eventbus.RedwomenPhotoEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/12/15.
 */

public class RedWomenCenterTwo extends Fragment {
    @BindView(R.id.mRedwomen_recyclerview)
    RecyclerView mRedwomenRecyclerview;
    @BindView(R.id.mRedwomen_message)
    PercentLinearLayout mRedwomenMessage;
    @BindView(R.id.mRedwomen_none)
    PercentLinearLayout mRedwomenNone;
    @BindView(R.id.mRedwomen_edit_msg_state)
    ImageView mRedwomenEditMsgState;
    @BindView(R.id.mRedwomen_none_iv)
    ImageView mRedwomenNoneIv;
    @BindView(R.id.mRedwomen_intro)
    TextView mRedwomenIntro;
    @BindView(R.id.mRedwomen_recyclerview_ll)
    AutoRelativeLayout mRedwomenRecyclerviewLl;
    @BindView(R.id.mRedwomen_no_photo)
    ImageView mRedwomenNoPhoto;
    @BindView(R.id.mRedwomen_ck_ll)
    PercentLinearLayout mRedwomenCkLl;
    @BindView(R.id.mFragment_redwomen_switch_01)
    ImageView mFragmentRedwomenSwitch01;
    @BindView(R.id.mFragment_redwomen_switch_ll01)
    PercentLinearLayout mFragmentRedwomenSwitchLl01;
    @BindView(R.id.mFragment_redwomen_switch_02)
    ImageView mFragmentRedwomenSwitch02;
    @BindView(R.id.mFragment_redwomen_switch_ll02)
    PercentLinearLayout mFragmentRedwomenSwitchLl02;
    @BindView(R.id.mFragment_redwomen_switch_03)
    ImageView mFragmentRedwomenSwitch03;
    @BindView(R.id.mFragment_redwomen_switch_ll03)
    PercentLinearLayout mFragmentRedwomenSwitchLl03;
    private String uid;
    private String match_state;
    private String admin;
    private Handler handler = new Handler();
    private List<PercentLinearLayout> buttons;
    private List<ImageView> imageViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_redwomen_two, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setData();
        getStep2Info();
        return view;
    }

    private void setData() {
        buttons = new ArrayList<>();
        buttons.add(mFragmentRedwomenSwitchLl01);
        buttons.add(mFragmentRedwomenSwitchLl02);
        buttons.add(mFragmentRedwomenSwitchLl03);
        imageViews=new ArrayList<>();
        imageViews.add(mFragmentRedwomenSwitch01);
        imageViews.add(mFragmentRedwomenSwitch02);
        imageViews.add(mFragmentRedwomenSwitch03);
        uid = getActivity().getIntent().getStringExtra("uid");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRedwomenRecyclerview.setLayoutManager(linearLayoutManager);
        admin = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "admin", "0");
    }

    private void getStep2Info() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("loginuid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetStep2Info, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("step2response", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RedWomenStep2Data data = new Gson().fromJson(response, RedWomenStep2Data.class);
                            RedWomenStep2Data.DataBean datas = data.getData();
                            if (datas.getMatch_introduce().equals("") && datas.getMatch_photo().size() == 0) {
                                mRedwomenMessage.setVisibility(View.GONE);
                                mRedwomenNone.setVisibility(View.VISIBLE);
                            } else {
                                mRedwomenMessage.setVisibility(View.VISIBLE);
                                mRedwomenNone.setVisibility(View.GONE);
                                mRedwomenIntro.setText(datas.getMatch_introduce());
                                if (datas.getMatch_photo().size() == 0) {
                                    mRedwomenRecyclerviewLl.setVisibility(View.GONE);
                                    mRedwomenCkLl.setVisibility(View.GONE);
                                    mRedwomenNoPhoto.setVisibility(View.VISIBLE);
                                } else {
                                    mRedwomenRecyclerviewLl.setVisibility(View.VISIBLE);
                                    mRedwomenCkLl.setVisibility(View.VISIBLE);
                                    mRedwomenNoPhoto.setVisibility(View.GONE);
                                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(datas.getMatch_photo(), getActivity(), 0);
                                    mRedwomenRecyclerview.setAdapter(adapter);
                                    //设置开关
                                    setImageViewsSelect(Integer.parseInt(datas.getMatch_photo_lock()));
                                    match_state = datas.getMatch_state();
                                    if (admin.equals("1")) {
                                        mRedwomenEditMsgState.setImageResource(R.mipmap.redxg);
                                    } else {
                                        if (match_state.equals("1")) {
                                            mRedwomenEditMsgState.setImageResource(R.mipmap.redxg);
                                        } else if (match_state.equals("2")) {
                                            mRedwomenEditMsgState.setImageResource(R.mipmap.redshz);
                                        } else if (match_state.equals("3")) {
                                            mRedwomenEditMsgState.setImageResource(R.mipmap.redxg);
                                        } else if (match_state.equals("4")) {
                                            mRedwomenEditMsgState.setVisibility(View.GONE);
                                        } else if (match_state.equals("5")) {
                                            mRedwomenEditMsgState.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {
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


    @OnClick({R.id.mRedwomen_edit_msg_state, R.id.mRedwomen_none, R.id.mRedwomen_none_iv, R.id.mFragment_redwomen_switch_ll01, R.id.mFragment_redwomen_switch_ll02, R.id.mFragment_redwomen_switch_ll03})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mRedwomen_edit_msg_state:
                if (match_state.equals("1") || match_state.equals("3") || admin.equals("1")) {
                    intent = new Intent(getActivity(), RedWomenEditMsgActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
                break;
            case R.id.mRedwomen_none_iv:
                intent = new Intent(getActivity(), RedWomenEditMsgActivity.class);
                intent.putExtra("editFlag",1);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.mFragment_redwomen_switch_ll01:
                setImageViewsSelect(0);
                setPhotoLock("0");
                break;
            case R.id.mFragment_redwomen_switch_ll02:
                setImageViewsSelect(1);
                setPhotoLock("1");
                break;
            case R.id.mFragment_redwomen_switch_ll03:
                setImageViewsSelect(2);
                setPhotoLock("2");
                break;
        }
    }

    private void setPhotoLock(final String method) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetPhotoLock, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setImageViewsSelect(int currentFlag){
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImageResource(R.mipmap.pursewxz);
        }
        imageViews.get(currentFlag).setImageResource(R.mipmap.pursexz);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(RedWomenMatchStateEvent event) {
        getStep2Info();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(RedwomenPhotoEvent event) {
        setImageViewsSelect(event.getMatch_photo_lock());
        setPhotoLock(String.valueOf(event.getMatch_photo_lock()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
