package com.aiwujie.shengmo.fragment.mainfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicMessageActivity;
import com.aiwujie.shengmo.activity.DynamicNoticeActivity;
import com.aiwujie.shengmo.activity.MyOrOtherTopicActivity;
import com.aiwujie.shengmo.activity.SendDynamicActivity;
import com.aiwujie.shengmo.activity.ranking.ActiveAndPopularityRankingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.AtSomeOneEvent;
import com.aiwujie.shengmo.eventbus.BigHornEvent;
import com.aiwujie.shengmo.eventbus.ClearDynamicNewCount;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.DynamicMessageEvent;
import com.aiwujie.shengmo.eventbus.DynamicPageTurnEvent;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.eventbus.ReceiveDynamicMsgEvent;
import com.aiwujie.shengmo.eventbus.RedPointEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicNew;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicPlaza;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.fragment.FollowDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.HotDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.SquareDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.topic.DynamicTopicFragment;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2016/12/18.
 */
public class FragmentDynamic extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    @BindView(R.id.mFragmentDynamic_horn)
    ImageView mFragmentDynamicHorn;
    @BindView(R.id.ll_FragmentDynamic_horn)
    RelativeLayout llFragmentDynamicHorn;
    @BindView(R.id.mFragmentDynamic_labaPoint)
    TextView mFragmentDynamicLabaPoint;
    @BindView(R.id.mFragmentDynamic_title_hot)
    TextView mFragmentDynamicTitleHot;
    @BindView(R.id.mFragmentDynamic_hot_dian)
    ImageView mFragmentDynamicHotDian;
    //    @BindView(R.id.mFragmentDynamic_title_near)
//    TextView mFragmentDynamicTitleNear;
//    @BindView(R.id.mFragmentDynamic_near_dian)
//    ImageView mFragmentDynamicNearDian;
    @BindView(R.id.mFragment_dynamic_newCount)
    TextView mFragmentDynamicNewCount;
    @BindView(R.id.mFragmentDynamic_title_follow)
    TextView mFragmentDynamicTitleFollow;
    @BindView(R.id.mFragmentDynamic_follow_dian)
    ImageView mFragmentDynamicFollowDian;
    @BindView(R.id.mFragment_dynamic_followCount)
    TextView mFragmentDynamicFollowCount;
    @BindView(R.id.mFragmentDynamic_title_my)
    TextView mFragmentDynamicTitleMy;
    @BindView(R.id.mFragmentDynamic_my_dian)
    ImageView mFragmentDynamicMyDian;
    @BindView(R.id.mFragmentDynamic_Sx)
    ImageView mFragmentDynamicSx;
    @BindView(R.id.mFragmentDynamic_ckpinglun)
    PercentRelativeLayout mFragmentDynamicCkpinglun;
    @BindView(R.id.mFragmentDynamic_line)
    TextView mFragmentDynamicLine;
    @BindView(R.id.mFragmentDynamic_newContent)
    TextView mFragmentDynamicNewContent;
    @BindView(R.id.mFragmentDynamic_ll_new)
    PercentLinearLayout mFragmentDynamicLlNew;
    @BindView(R.id.mFragment_dynamic_viewpager)
    ViewPager mFragmentDynamicViewpager;
    @BindView(R.id.mFragment_dynamic_recommendCount)
    TextView mFragmentDynamicRecommendCount;
    @BindView(R.id.mFragmentDynamic_sendDynamic)
    ImageView mFragmentDynamicSendDynamic;
    @BindView(R.id.mFragmentDynamic_title_topic)
    TextView mFragmentDynamicTitleTopic;
    @BindView(R.id.mFragmentDynamic_topic_dian)
    ImageView mFragmentDynamicTopicDian;
    @BindView(R.id.mFragmentDynamic_myOrJoinTopic)
    ImageView mFragmentDynamicMyOrJoinTopic;
    @BindView(R.id.fl_home_notice_active)
    FrameLayout flHomeNoticeActive;
    private List<Fragment> fragments;
    private List<TextView> titles;
    private List<ImageView> tvs;
    Handler handler = new Handler();
    private String dynamiccount = "0";
    /**
     * 判断是否是初始化Fragment
     */
    private boolean hasStarted = false;
    private int atcount = 0;
    private boolean isSend = false;
    private String toastStr;
    private PopupWindow mPopWindow;
    private String dynamicSex;
    private String dynamicSexual;
    //vip  1.为vip 0.不是vip
    private String vipflag;
    private int dynamicRetcode;
    private FragmentDynamicPlaza fragmentDynamicPlaza;
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
//        setData();
//        setListener();
        int labaallnum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "labaallnum", 0);
        if (labaallnum > 0) {
            mFragmentDynamicLabaPoint.setText(labaallnum + "");
            mFragmentDynamicLabaPoint.setVisibility(View.VISIBLE);
        }
        return view;
    }

    FragmentDynamicNew fragmentDynamicNew;
    SquareDynamicFragment squareDynamicFragment;
    FollowDynamicFragment followDynamicFragment;
    DynamicTopicFragment dynamicTopicFragment;
    HotDynamicFragment hotDynamicFragment;
    private void setData() {
        fragments = new ArrayList<>();
        //fragmentDynamicNew = new FragmentDynamicNew();
        squareDynamicFragment = new SquareDynamicFragment();
        followDynamicFragment = new FollowDynamicFragment();
        dynamicTopicFragment = new DynamicTopicFragment();
        hotDynamicFragment = new HotDynamicFragment();
        fragments.add(hotDynamicFragment);
        fragments.add(squareDynamicFragment);
        fragments.add(followDynamicFragment);
        fragments.add(dynamicTopicFragment);
        mFragmentDynamicViewpager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mFragmentDynamicViewpager.setOffscreenPageLimit(4);
        mFragmentDynamicViewpager.setCurrentItem(0, false);
        mFragmentDynamicTitleHot.setSelected(true);
        mFragmentDynamicTitleHot.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = mFragmentDynamicTitleHot.getPaint();
        tp.setFakeBoldText(true);
        mFragmentDynamicHotDian.setVisibility(View.VISIBLE);
        titles = new ArrayList<>();
        titles.add(mFragmentDynamicTitleHot);
//        titles.add(mFragmentDynamicTitleNear);
        titles.add(mFragmentDynamicTitleFollow);
        titles.add(mFragmentDynamicTitleMy);
        titles.add(mFragmentDynamicTitleTopic);
        tvs = new ArrayList<>();
        tvs.add(mFragmentDynamicHotDian);
//        tvs.add(mFragmentDynamicNearDian);
        tvs.add(mFragmentDynamicFollowDian);
        tvs.add(mFragmentDynamicMyDian);
        tvs.add(mFragmentDynamicTopicDian);
        mFragmentDynamicSendDynamic.animate().alpha(1).setStartDelay(500).start();
        showSxState();
    }

    private void setListener() {
        mFragmentDynamicViewpager.addOnPageChangeListener(this);
    }


    //R.id.mFragmentDynamic_title_near,
    @OnClick({R.id.mFragmentDynamic_myOrJoinTopic, R.id.mFragmentDynamic_title_topic, R.id.mFragmentDynamic_title_my, R.id.mFragmentDynamic_ll_new, R.id.ll_FragmentDynamic_horn, R.id.mFragmentDynamic_sendDynamic,
            R.id.mFragmentDynamic_title_hot, R.id.mFragmentDynamic_title_follow, R.id.mFragmentDynamic_Sx,R.id.fl_home_notice_active})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mFragmentDynamic_sendDynamic:
                intent = new Intent(getActivity(), SendDynamicActivity.class);
                startActivity(intent);
//                if (dynamicRetcode != 3001) {
//                    if (isSend) {
//                        intent = new Intent(getActivity(), SendDynamicActivity.class);
//                        startActivity(intent);
//                    } else {
//                        ToastUtil.show(getActivity().getApplicationContext(), toastStr);
//                    }
//                } else {
//                    BindPhoneAlertDialog.bindAlertDialog(getActivity(), toastStr);
//                }
                break;
            case R.id.mFragmentDynamic_ll_new:
                intent = new Intent(getActivity(), DynamicMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.mFragmentDynamic_title_hot:
                mFragmentDynamicFollowCount.setVisibility(View.GONE);
                Selected(0);
                break;
//            case R.id.mFragmentDynamic_title_near:
//                Selected(1);
//                break;
            case R.id.mFragmentDynamic_title_follow:
                mFragmentDynamicNewCount.setVisibility(View.GONE);
                EventBus.getDefault().post(new ClearRedPointEvent("ClearDynamicRedPoint", 4));
                Selected(1);
                break;
            case R.id.mFragmentDynamic_title_my:
                mFragmentDynamicFollowCount.setVisibility(View.GONE);
                Selected(2);
                break;
            case R.id.mFragmentDynamic_title_topic:
                Selected(3);
                break;
            case R.id.ll_FragmentDynamic_horn:
                intent = new Intent(getActivity(), DynamicNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.mFragmentDynamic_Sx:
                showOption();
                break;
            case R.id.mFragmentDynamic_myOrJoinTopic:
                intent = new Intent(getActivity(), MyOrOtherTopicActivity.class);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.item_dynamic_pop_llAll:
                if (vipflag.equals("1")) {
                    dynamicSex = "";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, false);
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSxSexual", "0");
                    mFragmentDynamicSx.setImageResource(R.drawable.shuaixuan);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llNan:
                if (vipflag.equals("1")) {
                    dynamicSex = "1";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                    mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llNv:
                if (vipflag.equals("1")) {
                    dynamicSex = "2";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                    mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llCdts:
                if (vipflag.equals("1")) {
                    dynamicSex = "3";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                    mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llSexual:
                if (vipflag.equals("1")) {
                    dynamicSex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "mydynamicSex", "");
                    dynamicSexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "mydynamicSexual", "");
                    if (!dynamicSex.equals("") && !dynamicSexual.equals("")) {
                        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSxSexual", "1");
                        dynamicSx(dynamicSex, dynamicSexual, true);
                        //设置沙漏为紫色
//                        mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                        mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
                    } else {
                        ToastUtil.show(getActivity().getApplicationContext(), "请您重新登录,才可以使用此功能...");
                    }
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.fl_home_notice_active:
                intent = new Intent(getActivity(), ActiveAndPopularityRankingActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < fragments.size(); i++) {
            titles.get(i).setSelected(false);
            tvs.get(i).setVisibility(View.GONE);
            titles.get(i).setEnabled(true);
            titles.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            TextPaint tp = titles.get(i).getPaint();
            tp.setFakeBoldText(false);
        }
        titles.get(position).setSelected(true);
        tvs.get(position).setVisibility(View.VISIBLE);
        titles.get(position).setEnabled(false);
        titles.get(position).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = titles.get(position).getPaint();
        tp.setFakeBoldText(true);
        if (position == 0) {
            EventBus.getDefault().post(new ViewIsVisibleEvent(1));
            mFragmentDynamicSx.setVisibility(View.VISIBLE);//显示筛选图标
            mFragmentDynamicMyOrJoinTopic.setVisibility(View.GONE);//隐藏发布话题图标
            EventBus.getDefault().post("shifouyincangshalou");
        } else if (position == 1) {
            EventBus.getDefault().post(new ViewIsVisibleEvent(1));
            mFragmentDynamicMyOrJoinTopic.setVisibility(View.GONE);
            mFragmentDynamicSx.setVisibility(View.VISIBLE);
        } else if (position == 2) {

            mFragmentDynamicMyOrJoinTopic.setVisibility(View.GONE);
            mFragmentDynamicSx.setVisibility(View.GONE);
        } else if (position == 3) {
//            mFragmentDynamicMyOrJoinTopic.setVisibility(View.VISIBLE);
            mFragmentDynamicSx.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void yincangxianshi(String str) {
//        if (str.equals("yincangshalou")){
//            mFragmentDynamicSx.setVisibility(View.GONE);
//        }
//        if (str.equals("xianshishalou")){
//            mFragmentDynamicSx.setVisibility(View.VISIBLE);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicPageTurnEvent event) {
        Selected(event.getPosition());
        if (event.getPosition() == 1) {
           // fragmentDynamicPlaza.setCheckedTab(1);
            squareDynamicFragment.setCheckedTab(1);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void dynamicSx(String dynamicSex, String dynamicSexual, boolean isOpen) {
        if (isOpen) {
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSwitch", "1");
        } else {
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSwitch", "0");
        }
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSex", dynamicSex);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dynamicSexual", dynamicSexual);
        EventBus.getDefault().post(new DynamicSxEvent(dynamicSex, dynamicSexual));
        mPopWindow.dismiss();
    }

    public void vipDialog(String toastStr) {
        new VipDialog(getActivity(), toastStr);
    }

    private void getUnRead() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "1");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetUnreadNum, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmentdynamicUnread", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("retcode") == 2000) {
                                JSONObject json = obj.getJSONObject("data");
                                dynamiccount = json.getString("allnum");
                                String atjson = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "atjson", "");
                                if (!atjson.equals("")) {
                                    if (atjson.contains("@-@")) {
                                        atcount = atjson.split("@-@").length;
                                    } else {
                                        atcount = 1;
                                    }
                                } else {
                                    atcount = 0;
                                }
                                if (Integer.parseInt(dynamiccount) == 0 && atcount == 0) {
                                    mFragmentDynamicLlNew.setVisibility(View.GONE);
                                    EventBus.getDefault().post(new DynamicMessageEvent(0, 0));
                                    mFragmentDynamicLlNew.setAnimation(AnimationUtil.ViewToGone());
                                } else {
                                    mFragmentDynamicLlNew.setVisibility(View.GONE);
                                    mFragmentDynamicLlNew.setAnimation(AnimationUtil.ViewToVisible());
                                    if (Integer.parseInt(dynamiccount) != 0) {
                                        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "dongtaixiaoxirednum", Integer.parseInt(dynamiccount));
                                        EventBus.getDefault().post(new DynamicMessageEvent(1, Integer.parseInt(dynamiccount)));
                                    } else {
                                        EventBus.getDefault().post(new DynamicMessageEvent(0, 0));
                                    }
                                    mFragmentDynamicNewContent.setText(Integer.parseInt(dynamiccount) + atcount + "条新消息");
                                }
                            }

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

    private void getJudgeDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.JudgeDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            dynamicRetcode = json.getInt("retcode");
                            switch (dynamicRetcode) {
                                case 2000:
                                    isSend = true;
                                    break;
                                case 3001:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 4003:
                                case 4004:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 5000:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;

                            }
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


    private void Selected(int j) {
        for (int i = 0; i < fragments.size(); i++) {
            titles.get(i).setSelected(false);
            tvs.get(i).setVisibility(View.GONE);
            titles.get(i).setEnabled(true);
            titles.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            TextPaint tp = titles.get(i).getPaint();
            tp.setFakeBoldText(false);
        }
        titles.get(j).setSelected(true);
        tvs.get(j).setVisibility(View.VISIBLE);
        titles.get(j).setEnabled(false);
        titles.get(j).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = titles.get(j).getPaint();
        tp.setFakeBoldText(true);
        if (j == 0 || j == 2) {
            mFragmentDynamicMyOrJoinTopic.setVisibility(View.GONE);
            mFragmentDynamicSx.setVisibility(View.GONE);
        } else if (j == 1) {
            mFragmentDynamicMyOrJoinTopic.setVisibility(View.GONE);
            mFragmentDynamicSx.setVisibility(View.VISIBLE);

        } else if (j == 3) {
//            mFragmentDynamicMyOrJoinTopic.setVisibility(View.VISIBLE);
            mFragmentDynamicSx.setVisibility(View.VISIBLE);
        }
        mFragmentDynamicViewpager.setCurrentItem(j, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && hasStarted == false) {
            hasStarted = true;
            setData();
            setListener();
        }
        //getJudgeDynamic();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ReceiveDynamicMsgEvent event) {
        if (event.getFlag() == 0) {
            getUnRead();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(BigHornEvent event) {
        if (event.getIsRedPoint() == 1) {
            int labaallnum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "labaallnum", 0);
            if (labaallnum > 0) {
                mFragmentDynamicLabaPoint.setText(labaallnum + "");
                mFragmentDynamicLabaPoint.setVisibility(View.VISIBLE);
            }
        } else {
            mFragmentDynamicLabaPoint.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(AtSomeOneEvent event) {
        if (event.getFlag() == 1) {
            String atjson = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "atjson", "");
//            Log.i("fragmentdynamicat", "onCreateView: " + atjson);
            if (!atjson.equals("")) {
                mFragmentDynamicLlNew.setVisibility(View.GONE);
                mFragmentDynamicLlNew.setAnimation(AnimationUtil.ViewToVisible());
                if (atjson.contains("@-@")) {
                    String[] atArray = atjson.split("@-@");
                    atcount = atArray.length;
                    mFragmentDynamicNewContent.setText(atArray.length + Integer.parseInt(dynamiccount) + "条新消息");
                } else {
                    mFragmentDynamicNewContent.setText(1 + Integer.parseInt(dynamiccount) + "条新消息");
                }
            } else if (Integer.parseInt(dynamiccount) != 0) {
                mFragmentDynamicLlNew.setVisibility(View.GONE);
                mFragmentDynamicLlNew.setAnimation(AnimationUtil.ViewToVisible());
                mFragmentDynamicNewContent.setText(Integer.parseInt(dynamiccount) + "条新消息");
            } else {
                mFragmentDynamicLlNew.setVisibility(View.GONE);
                mFragmentDynamicLlNew.setAnimation(AnimationUtil.ViewToGone());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(RedPointEvent event) {
        if (event.getFlag() == 4) {
            mFragmentDynamicNewCount.setVisibility(View.VISIBLE);
            if (event.getRedCount() > 99) {
                mFragmentDynamicNewCount.setText("99+");
            } else {
                mFragmentDynamicNewCount.setText(event.getRedCount() + "");
            }
        }
        if (event.getFlag() == 5) {
            mFragmentDynamicFollowCount.setVisibility(View.VISIBLE);
            if (event.getRedCount() > 99) {
                mFragmentDynamicFollowCount.setText("99+");
            } else {
                mFragmentDynamicFollowCount.setText(event.getRedCount() + "");
            }
        }
        if (event.getFlag() == 6) {
            mFragmentDynamicRecommendCount.setVisibility(View.GONE);
            if (event.getRedCount() > 99) {
                mFragmentDynamicRecommendCount.setText("99+");
            } else {
                mFragmentDynamicRecommendCount.setText(event.getRedCount() + "");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ClearRedPointEvent event) {
        switch (event.getFlag()) {
            case 4:
                mFragmentDynamicNewCount.setVisibility(View.GONE);
                break;
            case 6:
                mFragmentDynamicFollowCount.setVisibility(View.GONE);
                break;
            case 8:
                mFragmentDynamicRecommendCount.setVisibility(View.GONE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ViewIsVisibleEvent event) {
        switch (event.getIsVisible()) {
            case 0:
                if (mFragmentDynamicSendDynamic.getVisibility() == View.VISIBLE) {
                    mFragmentDynamicSendDynamic.setVisibility(View.GONE);
                    mFragmentDynamicSendDynamic.setAnimation(AnimationUtil.moveToViewBottom());
                }
                break;
            case 1:
                if (mFragmentDynamicSendDynamic.getVisibility() == View.GONE) {
                    mFragmentDynamicSendDynamic.setVisibility(View.VISIBLE);
                    mFragmentDynamicSendDynamic.setAnimation(AnimationUtil.moveToViewLocation());
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ClearDynamicNewCount event) {
        mFragmentDynamicLlNew.setVisibility(View.GONE);
    }

    private void showOption() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dynamic_pop, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(mFragmentDynamicLine);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        TextView llNan = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llNan);
        TextView llNv = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llNv);
        TextView llNao = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llCdts);
        TextView llAll = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llAll);
        TextView tvSexual = (TextView) contentView.findViewById(R.id.item_dynamic_pop_tvSexual);
        PercentLinearLayout llSexual = (PercentLinearLayout) contentView.findViewById(R.id.item_dynamic_pop_llSexual);
        String sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSex", "");
        String sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSexual", "");
        String dynamicSexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSxSexual", "");
        vipflag = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "0");
        if (sex.equals("") && sexual.equals("")) {
            llAll.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("1") && sexual.equals("")) {
            llNan.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("2") && sexual.equals("")) {
            llNv.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("3") && sexual.equals("")) {
            llNao.setTextColor(Color.parseColor("#b73acb"));
        }
        if (dynamicSexual.equals("1")) {
            tvSexual.setTextColor(Color.parseColor("#b73acb"));
        }
        llNan.setOnClickListener(this);
        llNv.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llNao.setOnClickListener(this);
        llSexual.setOnClickListener(this);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        getUnRead();
//        MyApp.uid = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "uid", "0");
       // getJudgeDynamic();
        //getUnRead();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void showSxState() {
        String sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSex", "");
        String sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSexual", "");
        String dynamicSexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSxSexual", "");
        //vipflag = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "0");
        if (sex.equals("") && sexual.equals("")) {
            mFragmentDynamicSx.setImageResource(R.drawable.shuaixuan);
        }
        if (sex.equals("1") && sexual.equals("")) {
            mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
        }
        if (sex.equals("2") && sexual.equals("")) {
            mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
        }
        if (sex.equals("3") && sexual.equals("")) {
            mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
        }
        if (dynamicSexual.equals("1")) {
            mFragmentDynamicSx.setImageResource(R.drawable.iv_home_select_done);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RedPointEvent event) {
        LogUtil.d("收到推顶消息");
    }
}