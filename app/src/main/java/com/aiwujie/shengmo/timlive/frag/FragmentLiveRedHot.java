package com.aiwujie.shengmo.timlive.frag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomeLiveLabelBean;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.bean.SearchUserData;
import com.aiwujie.shengmo.fragment.HomeLiveFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.adapter.HomeLiveLabelAdapter;
import com.aiwujie.shengmo.kt.ui.activity.LivePartitionActivity;
import com.aiwujie.shengmo.kt.ui.activity.NewLivePartitionActivity;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.timlive.bean.BannerLiveData;
import com.aiwujie.shengmo.timlive.bean.HotRedBean;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.header.FunGameBattleCityHeader;
import com.scwang.smartrefresh.header.FunGameHitBlockHeader;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.header.fungame.FunGameBase;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
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
import butterknife.Unbinder;


/**
 * 红人榜/热门榜
 *
 * @author derikli
 */
public class FragmentLiveRedHot extends Fragment implements OnRefreshLoadMoreListener {
    @BindView(R.id.mMy_banner)
    Banner mMyBanner;
    @BindView(R.id.mMy_banner_close)
    ImageView mMyBannerClose;
    @BindView(R.id.mMy_banner_framlayout)
    FrameLayout mMyBannerFramlayout;
    @BindView(R.id.rv_live_partition)
    RecyclerView rvLivePartition;
    @BindView(R.id.cl_live_header)
    ConstraintLayout clLiveHeader;
    private boolean isLoading = false; //是否正在加载
    private static final String TAG = FragmentLiveRedHot.class.getSimpleName();
    private Handler handler = new Handler();
    @BindView(R.id.live_hot_swipe_refresh_layout_list)
    SmartRefreshLayout smartRefreshLayout;
    private int page = 1; //分页加载的页码
    private LinearLayout ivNoData;//没有数据
    private TextView tvNoData;
    private LiveRoomFragment topFragment, bottomFragment;
    private TextView tvRedList, tvHotList;
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {

    }

    private void onVisible() {
        if (isVisible) {
            lazyLoad();
        }
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            isFirstLoad = false;
            setListener();
        }
    }
    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_live_hot, container, false);
        unbinder = ButterKnife.bind(this, view);
        isPrepared = true;
        initView(view);
        BannerUtils.setBannerView(mMyBanner);
        setListener();
        getBanner();
        getPartitionData();
        return view;
    }

    private void setListener() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(this);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getScenesRoomInfos();
            }
        }, 100);


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
    }

    private void initView(View view) {
        topFragment = (LiveRoomFragment) getChildFragmentManager().findFragmentById(R.id.top_fragment);
        bottomFragment = (LiveRoomFragment) getChildFragmentManager().findFragmentById(R.id.bottom_fragment);
        ivNoData = view.findViewById(R.id.no_data);
        tvNoData = view.findViewById(R.id.tv_anchor_no_data);
        tvNoData.setText(getString(R.string.no_anchor_list));
        tvRedList = view.findViewById(R.id.tv_red_list);
        tvHotList = view.findViewById(R.id.tv_hot_list);
    }

    private void getScenesRoomInfos() {
        if (isLoading) return;
        isLoading = true;
//        LiveHttpHelper.getInstance().onairlist(page, new HttpListener() {
//            @Override
//            public void onSuccess(String data) {
//                Log.d(TAG, data);
//                if (SafeCheckUtil.isActivityFinish(getActivity())) {
//                    return;
//                }
//                isLoading = false;
//                smartRefreshLayout.finishLoadMore();
//                smartRefreshLayout.finishRefresh();
//                HotRedBean hotRedBean = new Gson().fromJson(data, HotRedBean.class);
//                if (hotRedBean != null && hotRedBean.getData() != null) {
//                    boolean isHadHotData = hotRedBean.getData().getHotAnchor() != null && hotRedBean.getData().getHotAnchor().size() > 0;
//                    boolean isHadRedData = hotRedBean.getData().getSensationAnchor() != null && hotRedBean.getData().getSensationAnchor().size() > 0;
//                    if (isHadHotData || isHadRedData) {//有数据
//                        ivNoData.setVisibility(View.GONE);
//                        loadData(hotRedBean);
//                    } else {//没有数据
//                        resetViewStatus();
//                        loadData();
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                Log.e(TAG, "onFailed:  " + ", msg -> " + msg);
//                if (SafeCheckUtil.isActivityFinish(getActivity())) {
//                    return;
//                }
//                resetViewStatus();
//                loadData();
//                ToastUtil.show(getActivity(), msg);
//            }
//        });
        HttpHelper.getInstance().getOnAirListNew(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                Log.d(TAG, data);
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                isLoading = false;
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();

                HotRedBean hotRedBean = new Gson().fromJson(data, HotRedBean.class);
                if (hotRedBean != null && hotRedBean.getData() != null) {
                    boolean isHadHotData = hotRedBean.getData().getHotAnchor() != null && hotRedBean.getData().getHotAnchor().size() > 0;
                    boolean isHadRedData = hotRedBean.getData().getSensationAnchor() != null && hotRedBean.getData().getSensationAnchor().size() > 0;
                    if (isHadHotData || isHadRedData) {//有数据
                        ivNoData.setVisibility(View.GONE);
                        loadData(hotRedBean);
                    } else {//没有数据
                        resetViewStatus();
                        loadData();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    private void getLiveLogRoomInfo() {
        HttpHelper.getInstance().getHotAnchorLiveLog(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                smartRefreshLayout.finishLoadMore();
                SearchUserData liveLogRoomInfoBean = GsonUtil.GsonToBean(data, SearchUserData.class);
                if (liveLogRoomInfoBean != null && liveLogRoomInfoBean.getData() != null && liveLogRoomInfoBean.getData().size() > 0) {
                    bottomFragment.notifyData(liveLogRoomInfoBean.getData(), page);
                }
            }

            @Override
            public void onFail(int code, String msg) {
                smartRefreshLayout.finishLoadMore();
            }
        });
    }

    //隐藏所有的view
    private void resetViewStatus() {
        tvHotList.setVisibility(View.GONE);
        tvRedList.setVisibility(View.GONE);
        ivNoData.setVisibility(View.VISIBLE);
    }

    /**
     * 加载数据
     *
     * @param hotRedBean
     */
    private void loadData(HotRedBean hotRedBean) {
        if (hotRedBean.getData().getHotAnchor() != null && hotRedBean.getData().getHotAnchor().size() > 0) {
            LogUtil.d("热门视频：" + hotRedBean.getData().getHotAnchor().toString());
            tvHotList.setVisibility(View.VISIBLE);
            bottomFragment.getInstance(2, hotRedBean.getData().getHotAnchor(), "hot");
            bottomFragment.notifyData(hotRedBean.getData().getHotAnchor(), page);
        }
        if (hotRedBean.getData().getSensationAnchor() != null && hotRedBean.getData().getSensationAnchor().size() > 0) {
            LogUtil.d("红人榜：" + hotRedBean.getData().getSensationAnchor().toString());
            tvRedList.setVisibility(View.VISIBLE);
            topFragment.getInstance(3, hotRedBean.getData().getSensationAnchor(), "red");
            topFragment.notifyData(hotRedBean.getData().getSensationAnchor(), page);
        }
    }

    /**
     * 加载空数据
     */
    private void loadData() {
        List<ScenesRoomInfoBean> hotAnchor = new ArrayList<>();
        List<ScenesRoomInfoBean> sensationAnchor = new ArrayList<>();
        topFragment.getInstance(3, hotAnchor, "red");
        bottomFragment.getInstance(2,sensationAnchor,"hot");
        bottomFragment.notifyData(hotAnchor, page);
        topFragment.notifyData(sensationAnchor, page);
    }


    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
       // page++;
       // getScenesRoomInfos();
       // smartRefreshLayout.finishLoadMoreWithNoMoreData();
        page++;
        getLiveLogRoomInfo();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        getScenesRoomInfos();
        getPartitionData();
        //刷新分区数据
       // ((HomeLiveFragment) getParentFragment()).getPartitionData();
    }

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
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                HomeLiveLabelBean homeLiveLabelBean = GsonUtil.GsonToBean(data, HomeLiveLabelBean.class);
                final List<HomeLiveLabelBean.DataBean> labelList = homeLiveLabelBean.getData();
                HomeLiveLabelAdapter labelAdapter = new HomeLiveLabelAdapter(getActivity(), labelList);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
                rvLivePartition.setLayoutManager(layoutManager);
                rvLivePartition.setAdapter(labelAdapter);
                labelAdapter.setItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
//                        Intent intent = new Intent(getActivity(), LivePartitionActivity.class);
                        Intent intent = new Intent(getActivity(), NewLivePartitionActivity.class);
                        intent.putExtra("tid", labelList.get(position).getTid());
                        intent.putExtra("part", labelList.get(position).getName());
                        startActivity(intent);
                        //RoomManager.gotoQiNiuRoom(getActivity(),"402583","402583");
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
}
