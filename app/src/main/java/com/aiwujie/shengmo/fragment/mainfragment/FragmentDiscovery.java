package com.aiwujie.shengmo.fragment.mainfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DiscoveryAboutSMActivity;
import com.aiwujie.shengmo.activity.VipWebActivity;
import com.aiwujie.shengmo.activity.WebDiscoveryActivity;
import com.aiwujie.shengmo.bean.BannerData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2016/12/18.
 */
public class FragmentDiscovery extends Fragment {

    //    @BindView(R.id.mDiscovery_ll01)
//    PercentLinearLayout mDiscoveryLl01;
//    @BindView(R.id.mDiscovery_ll02)
//    PercentLinearLayout mDiscoveryLl02;
//    @BindView(R.id.mDiscovery_ll03)
//    PercentLinearLayout mDiscoveryLl03;
    @BindView(R.id.mDiscovery_ll04)
    PercentLinearLayout mDiscoveryLl04;
    //    @BindView(R.id.mDiscovery_ll05)
//    PercentLinearLayout mDiscoveryLl05;
    @BindView(R.id.mDiscovery_ll06)
    PercentLinearLayout mDiscoveryLl06;
    @BindView(R.id.mDiscovery_ll07)
    PercentLinearLayout mDiscoveryLl07;
    Handler handler = new Handler();
    @BindView(R.id.mDiscover_name)
    TextView mDiscoverName;
    @BindView(R.id.mDiscover_banner)
    ImageView mDiscoverBanner;
    @BindView(R.id.mDiscover_banner_close)
    ImageView mDiscoverBannerClose;
    @BindView(R.id.mDiscover_banner_framlayout)
    PercentFrameLayout mDiscoverBannerFramlayout;
    //    @BindView(R.id.mFragment_discovery_groupCount)
//    TextView mFragmentDiscoveryGroupCount;
    @BindView(R.id.mDiscoveryAboutSm_ll06)
    PercentLinearLayout mDiscoveryAboutSmLl06;
    @BindView(R.id.mDiscoveryAboutSm_ll05)
    PercentLinearLayout mDiscoveryAboutSmLl05;
    @BindView(R.id.mDiscoveryAboutSm_ll02)
    PercentLinearLayout mDiscoveryAboutSmLl02;
    @BindView(R.id.mDiscoveryAboutSm_ll07)
    PercentLinearLayout mDiscoveryAboutSmLl07;
    private String bannerPath;
    private String bannerTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, null);
        ButterKnife.bind(this, view);
//        EventBus.getDefault().register(this);
        getBanner();
        return view;
    }

    //R.id.mDiscovery_ll02,  R.id.mDiscovery_ll03, ,R.id.mDiscovery_ll01,R.id.mDiscovery_ll05
    @OnClick({R.id.mDiscoveryAboutSm_ll02,R.id.mDiscoveryAboutSm_ll07,R.id.mDiscoveryAboutSm_ll06, R.id.mDiscover_banner, R.id.mDiscovery_ll04, R.id.mDiscoveryAboutSm_ll05, R.id.mDiscovery_ll06, R.id.mDiscovery_ll07, R.id.mDiscover_banner_close})
    public void onClick(View view) {
        Intent intent;
        String state;
        String titlename;
        switch (view.getId()) {
            case R.id.mDiscover_banner:
                intent = new Intent(getActivity(), BannerWebActivity.class);
                intent.putExtra("path", bannerPath);
                intent.putExtra("title", bannerTitle);
                startActivity(intent);
                break;
//            case R.id.mDiscovery_ll01:
//                intent = new Intent(getActivity(), MapActivity.class);
//                intent.putExtra("mapflag", "1");
//                startActivity(intent);
//                break;
//            case R.id.mDiscovery_ll02:
////                EventBus.getDefault().post(new ClearRedPointEvent("clearGroupRedPoint", 2));
//                intent = new Intent(getActivity(), GroupSquareActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.mDiscovery_ll03:
//                state = "6";
//                titlename = "银河文摘";
//                intent = new Intent(getActivity(), WebDiscoveryActivity.class);
//                intent.putExtra("titlename", titlename);
//                intent.putExtra("state", state);
//                startActivity(intent);
////                intent=new Intent(getActivity(), RedWomenActivity.class);
////                startActivity(intent);
//                break;
            case R.id.mDiscovery_ll04:
                intent = new Intent(getActivity(), DiscoveryAboutSMActivity.class);
                startActivity(intent);
                break;
//            case R.id.mDiscovery_ll05:
//                intent = new Intent(getActivity(), SoundLoveActivity.class);
//                startActivity(intent);
//                break;
            case R.id.mDiscovery_ll06:
                intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "绿色圣魔");
                intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.GreenSM);
                startActivity(intent);
                break;
            case R.id.mDiscovery_ll07:
                state = "5";
                titlename = "圣魔公益";
                intent = new Intent(getActivity(), WebDiscoveryActivity.class);
                intent.putExtra("titlename", titlename);
                intent.putExtra("state", state);
                startActivity(intent);
                break;
            case R.id.mDiscover_banner_close:
                mDiscoverBannerFramlayout.setVisibility(View.GONE);
                break;
            case R.id.mDiscoveryAboutSm_ll05:
                intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "全职招聘");
                intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.Recruit);
                startActivity(intent);
                break;
            case R.id.mDiscoveryAboutSm_ll06:
                intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "志愿者招聘");
                intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.Volunteer);
                startActivity(intent);
                break;
            case R.id.mDiscoveryAboutSm_ll02:
                state = "3";
                titlename = "圣魔文化节";
                intent = new Intent(getActivity(), WebDiscoveryActivity.class);
                intent.putExtra("titlename", titlename);
                intent.putExtra("state", state);
                startActivity(intent);
                break;
            case R.id.mDiscoveryAboutSm_ll07:
                state = "4";
                titlename = "创始人";
                intent = new Intent(getActivity(), WebDiscoveryActivity.class);
                intent.putExtra("titlename", titlename);
                intent.putExtra("state", state);
                startActivity(intent);
                break;
        }
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "3");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetBanner, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BannerData data = new Gson().fromJson(response, BannerData.class);
                            bannerPath = data.getData().getUrl();
                            bannerTitle = data.getData().getTitle();
                            if (data.getRetcode() == 2000) {
                                mDiscoverBannerFramlayout.setVisibility(View.VISIBLE);
                                GlideImgManager.glideLoader(getActivity().getApplicationContext(), data.getData().getPath(), R.mipmap.whitebg, R.mipmap.whitebg, mDiscoverBanner);
//                                x.image().bind(mDiscoverBanner, data.getData().getPath());
                            } else {
                                mDiscoverBannerFramlayout.setVisibility(View.GONE);
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


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessages(RedPointEvent event) {
//        switch (event.getFlag()) {
//            case 3:
//                mFragmentDiscoveryGroupCount.setVisibility(View.VISIBLE);
//                if (event.getRedCount() > 99) {
//                    mFragmentDiscoveryGroupCount.setText("99+");
//                } else {
//                    mFragmentDiscoveryGroupCount.setText(event.getRedCount() + "");
//                }
//                break;
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessages(ClearRedPointEvent event) {
//        switch (event.getFlag()) {
//            case 2:
//                mFragmentDiscoveryGroupCount.setVisibility(View.INVISIBLE);
//                break;
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

}