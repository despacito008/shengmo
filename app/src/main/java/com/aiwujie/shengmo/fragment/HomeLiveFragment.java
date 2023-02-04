package com.aiwujie.shengmo.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.ranking.LiveRankingActivity;
import com.aiwujie.shengmo.activity.ranking.NewLiveRankingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomeLiveLabelBean;
import com.aiwujie.shengmo.bean.NewAnchorAuthBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.adapter.HomeLiveLabelAdapter;
import com.aiwujie.shengmo.kt.ui.activity.AnchorAuthActivity;
import com.aiwujie.shengmo.kt.ui.activity.LivePartitionActivity;
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity;
import com.aiwujie.shengmo.kt.ui.fragment.LiveNewFragment;
import com.aiwujie.shengmo.kt.ui.fragment.NewLiveFragment;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.qnlive.activity.QNLiveRoomAnchorActivity;
import com.aiwujie.shengmo.qnlive.utils.PermissionChecker;
import com.aiwujie.shengmo.qnlive.utils.ToastUtils;
import com.aiwujie.shengmo.timlive.bean.BannerLiveData;
import com.aiwujie.shengmo.timlive.bean.LiveAuthInfo;
import com.aiwujie.shengmo.timlive.frag.FragmentLiveNew;
import com.aiwujie.shengmo.timlive.frag.FragmentLiveRedHot;
import com.aiwujie.shengmo.timlive.frag.LiveRankFragment;
import com.aiwujie.shengmo.timlive.frag.LiveRankFragment2;
import com.aiwujie.shengmo.timlive.kt.ui.fragment.LiveFollowFragment;
import com.aiwujie.shengmo.timlive.kt.ui.fragment.LiveUserFollowFragment;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.timlive.ui.LiveRoomRegisterAuthActivity;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

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

import static com.aiwujie.shengmo.timlive.net.RoomManager.createRoom;


public class HomeLiveFragment extends Fragment {
    List<Fragment> fragmentsLive;
    @BindView(R.id.mFragmentLive_title_hot)
    TextView mFragmentLiveTitleHot;
    @BindView(R.id.mFragmentLive_hot_dian)
    ImageView mFragmentLiveHotDian;
    @BindView(R.id.mFragmentLive_title_new)
    TextView mFragmentLiveTitleNew;
    @BindView(R.id.mFragmentlive_new_dian)
    ImageView mFragmentLiveNewDian;
    @BindView(R.id.mFragmentLive_title_follow)
    TextView mFragmentLiveTitleFollow;
    @BindView(R.id.mFragmentLive_follow_dian)
    ImageView mFragmentLiveFollowDian;
    @BindView(R.id.mMy_banner_framlayout)
    FrameLayout mMyBannerFramlayout;
    @BindView(R.id.mMy_banner)
    Banner mMyBanner;
    @BindView(R.id.mMy_banner_close)
    ImageView mMyBannerClose;

    @BindView(R.id.view_pager_live)
    ViewPager viewPagerLive;
    Unbinder unbinder;
    @BindView(R.id.iv_fragment_live)
    ImageView ivFragmentLive;
    @BindView(R.id.iv_fragment_live_search)
    ImageView tvFragmentLiveSearch;
    @BindView(R.id.rv_live_partition)
    RecyclerView rvLivePartition;
    @BindView(R.id.cl_live_header)
    ConstraintLayout clLiveHeader;
    @BindView(R.id.mFragment_live_recommendCount)
    TextView mFragmentLiveRecommendCount;
    @BindView(R.id.mFragment_live_newCount)
    TextView mFragmentLiveNewCount;
    @BindView(R.id.mFragment_live_followCount)
    TextView mFragmentLiveFollowCount;
    @BindView(R.id.mFragmentLive_rank_dian)
    ImageView mFragmentLiveRankDian;
    @BindView(R.id.mFragmentLive_title_rank)
    TextView mFragmentLiveTitleRank;
    @BindView(R.id.mFragment_live_rankCount)
    TextView mFragmentLiveRankCount;
    @BindView(R.id.cl_fragment_message_title_bar)
    ConstraintLayout clFragmentMessageTitleBar;

    private List<TextView> titles;
    private List<ImageView> tvs;
    private List<String> bannerTitle;
    private List<String> bannerPath;
    private List<String> bannerUrl;
    private List<String> linkType;
    private List<String> linkId;
    /**
     * 判断是否是初始化Fragment
     */
    private boolean hasStarted = false;
    private boolean hasData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_live, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && hasStarted == false) {
            hasStarted = true;
            setData();
            BannerUtils.setBannerView(mMyBanner);
            //getBanner();
            //getPartitionData();
            setListener();
        }
    }

    void setData() {
        fragmentsLive = new ArrayList<>();
        FragmentLiveRedHot fragmentLiveHot = new FragmentLiveRedHot();
        //FragmentLiveNew fragmentLiveNew = new FragmentLiveNew();
        //NewLiveFragment newLiveFragment = new NewLiveFragment();
        LiveFollowFragment liveFollowFragment = new LiveFollowFragment();
        LiveRankFragment liveRankFragment = new LiveRankFragment();

        fragmentsLive.add(fragmentLiveHot);
        fragmentsLive.add(new LiveNewFragment());
        fragmentsLive.add(new LiveUserFollowFragment());
        fragmentsLive.add(liveRankFragment);

        viewPagerLive.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentsLive.get(position);
            }

            @Override
            public int getCount() {
                return fragmentsLive.size();
            }
        });
        viewPagerLive.setOffscreenPageLimit(3);
        viewPagerLive.setCurrentItem(0, false);
        mFragmentLiveTitleHot.setSelected(true);
        mFragmentLiveTitleHot.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = mFragmentLiveTitleHot.getPaint();
        tp.setFakeBoldText(true);
        mFragmentLiveHotDian.setVisibility(View.VISIBLE);
        titles = new ArrayList<>();
        titles.add(mFragmentLiveTitleHot);
        titles.add(mFragmentLiveTitleNew);
        titles.add(mFragmentLiveTitleFollow);
        titles.add(mFragmentLiveTitleRank);
        tvs = new ArrayList<>();
        tvs.add(mFragmentLiveHotDian);
        tvs.add(mFragmentLiveNewDian);
        tvs.add(mFragmentLiveFollowDian);
        tvs.add(mFragmentLiveRankDian);
    }

    void setListener() {
        mMyBannerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBannerFramlayout.setVisibility(View.GONE);
                hasData = false;
            }
        });
        mMyBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = null;
                if (linkType.get(position).equals("0")) {
                    intent = new Intent(getActivity(), BannerWebActivity.class);
                    intent.putExtra("path", bannerUrl.get(position) + "?uid=" + MyApp.uid);
                    intent.putExtra("title", bannerTitle.get(position));
                } else if (linkType.get(position).equals("1")) {
                    intent = new Intent(getActivity(), TopicDetailActivity.class);
                    intent.putExtra("tid", linkId.get(position));
                    intent.putExtra("topictitle", bannerTitle.get(position));
                } else if (linkType.get(position).equals("2")) {
                    intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra("uid", MyApp.uid);
                    intent.putExtra("did", linkId.get(position));
                    intent.putExtra("pos", 1);
                    intent.putExtra("showwhat", 1);
                } else {
                    intent = new Intent(getActivity(), PesonInfoActivity.class);
                    intent.putExtra("uid", linkId.get(position));
                }
                startActivity(intent);
            }
        });
        viewPagerLive.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < fragmentsLive.size(); i++) {
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
                    clLiveHeader.setVisibility(View.VISIBLE);
                    if (hasData == true) {
                        //mMyBannerFramlayout.setVisibility(View.VISIBLE);
                    }
                } else if (position == 1) {
                    clLiveHeader.setVisibility(View.GONE);
                    mMyBannerFramlayout.setVisibility(View.GONE);
                } else if (position == 2) {
                    clLiveHeader.setVisibility(View.GONE);
                    mMyBannerFramlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void Selected(int j) {
        for (int i = 0; i < fragmentsLive.size(); i++) {
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

        viewPagerLive.setCurrentItem(j, false);
    }


    @OnClick({R.id.mFragmentLive_title_hot, R.id.mFragmentLive_title_new, R.id.mFragmentLive_title_follow,R.id.mFragmentLive_title_rank, R.id.iv_fragment_live, R.id.iv_fragment_live_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mFragmentLive_title_hot:
                Selected(0);
                break;
            case R.id.mFragmentLive_title_new:
                Selected(1);
                break;
            case R.id.mFragmentLive_title_follow:
                Selected(2);
                break;
            case R.id.mFragmentLive_title_rank:
                Selected(3);
                break;
            case R.id.iv_fragment_live:
                if (!ClickUtils.isFastClick(R.id.iv_fragment_live)) {
                    //isAuth();
                    isAuthNew();
                }
                //createRoom(getContext(),MyApp.uid);
                break;
            case R.id.iv_fragment_live_search:
                //Intent intent = new Intent(getActivity(), LiveRoomSearchActivity.class);
                //Intent intent = new Intent(getActivity(), SearchLiveUserActivity.class);
                //Intent intent = new Intent(getActivity(), LiveRankingActivity.class);
                //Intent intent = new Intent(getActivity(), PlayBackVideoActivity.class);
                Intent intent = new Intent(getActivity(), NewLiveRankingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void isAuth() {
        LiveHttpHelper.getInstance().isAuth(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                Log.d("onSuccess", data);
                LiveAuthInfo liveAuthInfo = GsonUtil.GsonToBean(data, LiveAuthInfo.class);
                if (liveAuthInfo != null && liveAuthInfo.getData() != null) {
                    if (liveAuthInfo.getData().getIs_live().equals("0")) {
                        Intent intent = new Intent(getActivity(), LiveRoomRegisterAuthActivity.class);
                        startActivity(intent);
                    } else {
                        createRoom(getContext(), liveAuthInfo.getData().getRoom_id());
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(getActivity(), msg);
            }
        });
    }

    private void isAuthNew() {
        HttpHelper.getInstance().getNewLiveAuth(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                NewAnchorAuthBean newAnchorAuthBean = GsonUtil.GsonToBean(data, NewAnchorAuthBean.class);
                if (newAnchorAuthBean != null && newAnchorAuthBean.getData() != null) {
                    if (newAnchorAuthBean.getData().getIs_live().equals("0")) {
                        Intent intent = new Intent(getActivity(), AnchorAuthActivity.class);
                        startActivity(intent);
                    } else {
                        createRoom(getContext(), newAnchorAuthBean.getData().getRoom_id());
//                        if (isPermissionOK()) {
//                            Intent intent = new Intent(getActivity(), QNLiveRoomAnchorActivity.class);
//                            startActivity(intent);
//                        }
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(getActivity(), msg);
            }
        });
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "3");
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.getliveBanner, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (mMyBannerFramlayout == null) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            BannerLiveData data = new Gson().fromJson(response, BannerLiveData.class);
                            bannerTitle = new ArrayList<>();
                            bannerPath = new ArrayList<>();
                            bannerUrl = new ArrayList<>();
                            linkType = new ArrayList<>();
                            linkId = new ArrayList<>();
                            List<BannerLiveData.LiveBanner> liveBanners = data.getData().getLive_banner();
                            if (liveBanners != null && liveBanners.size() > 0) {
                                for (int i = 0; i < liveBanners.size(); i++) {
                                    bannerTitle.add(liveBanners.get(i).getTitle());
                                    bannerPath.add(liveBanners.get(i).getPath());
                                    bannerUrl.add(liveBanners.get(i).getUrl());
                                    linkType.add(liveBanners.get(i).getLink_type());
                                    linkId.add(liveBanners.get(i).getLink_id());
                                }
                                mMyBannerFramlayout.setVisibility(View.VISIBLE);
                                //设置图片集合
                                mMyBanner.setImages(bannerPath);
                                mMyBanner.start();
                                hasData = true;
                            } else {
                                mMyBannerFramlayout.setVisibility(View.GONE);
                                hasData = false;
                            }
                            break;
                        case 4000:
                            mMyBannerFramlayout.setVisibility(View.GONE);
                            hasData = false;
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                mMyBannerFramlayout.setVisibility(View.GONE);
                hasData = false;
            }
        });
    }

    public void getPartitionData() {
        HttpHelper.getInstance().getHomePageLiveLabel(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                HomeLiveLabelBean homeLiveLabelBean = GsonUtil.GsonToBean(data, HomeLiveLabelBean.class);
                final List<HomeLiveLabelBean.DataBean> labelList = homeLiveLabelBean.getData();
                HomeLiveLabelAdapter labelAdapter = new HomeLiveLabelAdapter(getActivity(), labelList);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
                rvLivePartition.setLayoutManager(layoutManager);
                rvLivePartition.setAdapter(labelAdapter);
                labelAdapter.setItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        //Intent intent = new Intent(getActivity(), PlayBackVideoActivity.class);
//                        Intent intent = new Intent(getActivity(), LivePartitionActivity.class);
//                        intent.putExtra("tid", labelList.get(position).getTid());
//                        intent.putExtra("part", labelList.get(position).getName());
//                        startActivity(intent);
                        RoomManager.gotoQiNiuRoom(getActivity(),"","");
                    }
                });
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean isPermissionOK() {
        PermissionChecker checker = new PermissionChecker(getActivity());
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission();
        if (!isPermissionOK) {
            ToastUtils.showShortToast(getActivity(),"开启直播需要授权权限");
        }
        return isPermissionOK;
    }


}
