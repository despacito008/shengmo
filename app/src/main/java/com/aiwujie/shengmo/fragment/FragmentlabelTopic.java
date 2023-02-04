package com.aiwujie.shengmo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.FragmentAdapter2;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FriendGroupListBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.google.gson.Gson;

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
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2017/11/24.
 */

public class FragmentlabelTopic extends Fragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.mDynamic_topic_tabs)
    TabLayout mDynamicTopicTabs;
    @BindView(R.id.mDynamic_topic_viewpager)
    ViewPager mDynamicTopicViewpager;
    List<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private List<String> titles = new ArrayList<>();
    boolean addlis = true;
    boolean lisis = true;
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
    List<View> views = new ArrayList<>();
    private View tabView1;
    Handler handler = new Handler();
    private FragmentAdapter2 mFragmentAdapteradapter;
    private Context context;
    private List<FriendGroupListBean.DataBean> fenzulistdata;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_fg_topic, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        isPrepared = true;
        this.context = getContext();
        lazyLoad();
        ivLayoutNormalEmpty.setImageResource(R.drawable.ic_empty_group);
        tvLayoutNormalEmpty.setText("暂无分组");
        layoutNormalEmpty.setBackgroundResource(R.color.white);
        return view;
    }

    private void initViewPager() {


        for (int i = 0; i < titles.size(); i++) {
            fragments.add(FragmentFriendTopic.newInstance(Integer.valueOf(fenzulistdata.get(i).getId())));
        }
        mFragmentAdapteradapter = new FragmentAdapter2(getActivity(), getChildFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mDynamicTopicViewpager.setAdapter(mFragmentAdapteradapter);
        mDynamicTopicViewpager.addOnPageChangeListener(this);
        //将TabLayout和ViewPager关联起来。
        mDynamicTopicTabs.setupWithViewPager(mDynamicTopicViewpager);
        mDynamicTopicViewpager.setOffscreenPageLimit(fragments.size());
        for (int i = 0; i < titles.size(); i++) {
            mDynamicTopicTabs.getTabAt(i).setCustomView(getTabView(i));
        }
        chooseTab(0);
        //addtablinsiter();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void liebiaoshuaxin(String str) {
        if (str.equals("fenzuliebiaoshuaxin")) {
            if (null != mFragmentAdapteradapter) {
                getfriendgrouplist();
            }
        }
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
            //initViewPager();
            getfriendgrouplist();
        }

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_custom_tab_layout4, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(titles.get(position));
        txt_title.setTextColor(getResources().getColor(R.color.normalGray));
        txt_title.setBackgroundResource(R.drawable.bg_round_send_comment);
        return view;
    }

    public void selectTab(int position) {
        for (int i = 0; i < titles.size(); i++) {
            unChooseTab(i);
        }
        for (int i = 0; i < titles.size(); i++) {
            if (i == position) {
                chooseTab(i);
            }
        }
    }

    public void chooseTab(int position) {
        View view = mDynamicTopicTabs.getTabAt(position).getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setTextColor(getResources().getColor(R.color.white));
        txt_title.setBackgroundResource(R.drawable.bg_round_follow);
    }

    public void chooseTab(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view == null) {
            return;
        }
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setTextColor(getResources().getColor(R.color.white));
        txt_title.setBackgroundResource(R.drawable.bg_round_follow);
    }

    public void unChooseTab(int position) {
        View view = mDynamicTopicTabs.getTabAt(position).getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setTextColor(getResources().getColor(R.color.normalGray));
        txt_title.setBackgroundResource(R.drawable.bg_round_send_comment);
    }

    public void unChooseTab(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view == null) {
            return;
        }
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setTextColor(getResources().getColor(R.color.normalGray));
        txt_title.setBackgroundResource(R.drawable.bg_round_send_comment);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //查询分组
    public void getfriendgrouplist() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.friendgrouplist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mDynamicTopicTabs.setVisibility(View.VISIBLE);
                                    mDynamicTopicViewpager.setVisibility(View.VISIBLE);
                                    Gson gson = new Gson();
                                    FriendGroupListBean friendGroupListBean = gson.fromJson(response, FriendGroupListBean.class);
                                    fenzulistdata = friendGroupListBean.getData();
                                    titles.clear();
                                    fragments.clear();
                                    for (int i = 0; i < fenzulistdata.size(); i++) {
                                        titles.add("" + fenzulistdata.get(i).getFgname());
                                        //fragments.add(FragmentFriendTopic.newInstance(Integer.valueOf(fenzulistdata.get(i).getId())));
                                    }

//                                    for (int i = 1; i < titles.size(); i++) {
//                                        View tabView = getTabView(i);
//                                        mDynamicTopicTabs.getTabAt(i).setCustomView(tabView);
//                                    }

                                    if (titles.size() > 0) {
                                        layoutNormalEmpty.setVisibility(View.GONE);
//                                        tabView1 = getTabView(0);
//                                        TextView txt_title = tabView1.findViewById(R.id.item_custom_tab_tv);
//                                        txt_title.setTextColor(0xffffffff);
//                                        LinearLayout ll = tabView1.findViewById(R.id.ll);
//                                        GradientDrawable drawable = new GradientDrawable();
//                                        drawable.setCornerRadius(50);
//                                        drawable.setColor(Color.parseColor("#db57f3"));
//                                        ll.setBackgroundDrawable(drawable);
//                                        lisis = false;
                                        initViewPager();
                                        //chooseTab(0);
                                        //mFragmentAdapteradapter.notifyDataSetChanged();
                                        //mDynamicTopicTabs.getTabAt(0).setCustomView(tabView1);
                                    }

//                                    lisis = true;
//                                    addtablinsiter();
//                                    if (null != mFragmentAdapteradapter) {
//                                        // mFragmentAdapteradapter.notifyDataSetChanged();
//                                        mDynamicTopicViewpager.setCurrentItem(0);
//                                    }
                                }
                            });
                            break;
                        default:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                                    mDynamicTopicTabs.setVisibility(View.GONE);
                                    mDynamicTopicViewpager.setVisibility(View.GONE);
                                }
                            });

                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void addtablinsiter() {
        mDynamicTopicTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chooseTab(tab);
//                if (lisis) {
//                    View customView = tab.getCustomView();
//                    TextView txt_title = customView.findViewById(R.id.item_custom_tab_tv);
//                    txt_title.setTextColor(0xffffffff);
//                    LinearLayout ll = customView.findViewById(R.id.ll);
//                    GradientDrawable drawable = new GradientDrawable();
//                    drawable.setCornerRadius(50);
//                    drawable.setColor(Color.parseColor("#333333"));
//                    ll.setBackgroundDrawable(drawable);
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //unChooseTab(tab);
//                if (lisis) {
//                    View customView = tab.getCustomView();
//                    TextView txt_title2 = customView.findViewById(R.id.item_custom_tab_tv);
//                    txt_title2.setTextColor(0xff999999);
//                    LinearLayout ll2 = customView.findViewById(R.id.ll);
//                    GradientDrawable drawable2 = new GradientDrawable();
//                    drawable2.setCornerRadius(50);
//                    drawable2.setColor(Color.parseColor("#ffffff"));
//                    ll2.setBackgroundDrawable(drawable2);
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
