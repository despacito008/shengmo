package com.aiwujie.shengmo.fragment.mainfragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.HelpCenterActivity;
import com.aiwujie.shengmo.activity.MoreActivity;
import com.aiwujie.shengmo.activity.More_news_Activity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.VipWebActivity;
import com.aiwujie.shengmo.adapter.CommonWealthGridviewTwoAdapter;
import com.aiwujie.shengmo.adapter.CommonWealthListviewAdapter;
import com.aiwujie.shengmo.adapter.DiscoveryAdapter;
import com.aiwujie.shengmo.adapter.RecyclerViewDynamicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.bean.BindingData;
import com.aiwujie.shengmo.bean.CommonWealthNewsData;
import com.aiwujie.shengmo.bean.DiscoveryData;
import com.aiwujie.shengmo.bean.RedwomenMarkerData;
import com.aiwujie.shengmo.bean.TopicHeaderData;
import com.aiwujie.shengmo.bean.WealthQuestionData;
import com.aiwujie.shengmo.customview.ContactAlertDialog;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.customview.MyListView;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/9/15.
 */

public class FragmentCommonWealth extends Fragment implements AdapterView.OnItemClickListener, OnBannerListener {
    @BindView(R.id.mFragmentDiscovery_gridview)
    MyGridview mFragmentDiscoveryGridview;
    @BindView(R.id.mCommenWealth_icon01)
    ImageView mCommenWealthIcon01;
    @BindView(R.id.mCommenWealth_name01)
    TextView mCommenWealthName01;
    @BindView(R.id.mCommenWealth_chat_ll01)
    PercentLinearLayout mCommenWealthChatLl01;
    @BindView(R.id.mCommenWealth_icon02)
    ImageView mCommenWealthIcon02;
    @BindView(R.id.mCommenWealth_name02)
    TextView mCommenWealthName02;
    @BindView(R.id.mCommenWealth_chat_ll02)
    PercentLinearLayout mCommenWealthChatLl02;
    @BindView(R.id.mCommenWealth_icon03)
    ImageView mCommenWealthIcon03;
    @BindView(R.id.mCommenWealth_name03)
    TextView mCommenWealthName03;
    @BindView(R.id.mCommenWealth_chat_ll03)
    PercentLinearLayout mCommenWealthChatLl03;
    @BindView(R.id.mCommenWealth_icon04)
    ImageView mCommenWealthIcon04;
    @BindView(R.id.mCommenWealth_name04)
    TextView mCommenWealthName04;
    @BindView(R.id.mCommenWealth_chat_ll04)
    PercentLinearLayout mCommenWealthChatLl04;
    @BindView(R.id.mCommenWealth_chat_all_ll)
    PercentLinearLayout mCommenWealthChatAllLl;
    @BindView(R.id.mCommenWealth_cellphone)
    TextView mCommenWealthCellphone;
    @BindView(R.id.mCommenWealth_telephone)
    TextView mCommenWealthTelephone;
    @BindView(R.id.ll_morenews)
    LinearLayout ll_morenews;
    @BindView(R.id.ll_moreproblems)
    LinearLayout ll_moreproblems;
    @BindView(R.id.mFragmentDiscovery_framlayout)
    PercentFrameLayout mFragmentDiscoveryFramlayout;
    @BindView(R.id.mFragmentDiscovery_recyclerview)
    RecyclerView mFragmentDiscoveryRecyclerview;
    @BindView(R.id.mFragmentDiscovery_listview)
    MyListView mFragmentDiscoveryListview;
    @BindView(R.id.mFragmentDiscovery_gridview02)
    MyGridview mFragmentDiscoveryGridview02;
    @BindView(R.id.mFragmentDiscovery_banner01)
    Banner mFragmentDiscoveryBanner01;
    @BindView(R.id.tv_topnew)
    TextView tv_topnew;
    @BindView(R.id.ll_topnew)
    LinearLayout ll_topnew;
    @BindView(R.id.mCommenWealth_QQ)
    TextView mCommenWealthQQ;
    private List<Integer> img;
    private List<String> titles;
    private List<String> content;
    private List<DiscoveryData> discoveryDatas;
    private String realmobile = "";
    private String mobile;
    private Handler handler = new Handler();
    private String markerUid01;
    private String markerUid02;
    private String markerUid03;
    private String markerUid04;
    private String markerNickname01;
    private String markerNickname02;
    private String markerNickname03;
    private String markerNickname04;
    private List<String> bannerTitle = new ArrayList<>();
    private List<String> bannerPath = new ArrayList<>();
    private List<String> bannerUrl = new ArrayList<>();
    private List<String> linkType = new ArrayList<>();
    private List<String> linkId = new ArrayList<>();
    private CommonWealthNewsData wealthNewsData;
    private WealthQuestionData wealthQuestionData;
    /**
     * ????????????????????????Fragment
     */
    private boolean hasStarted = false;
    private String topnewurl;
    private String toptitle;
    private String url_type;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_wealth, null);
        ButterKnife.bind(this, view);
        gettopnew();
        return view;
    }

    private void getBasicNews() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.GetBasicNews, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    wealthNewsData = new Gson().fromJson(response, CommonWealthNewsData.class);
                                    mFragmentDiscoveryListview.setAdapter(new CommonWealthListviewAdapter(getActivity(), wealthNewsData.getData()));
                                    break;
                                case 4000:
                                    mFragmentDiscoveryListview.setVisibility(View.GONE);
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

    private void getQuestions() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.GetQuestions, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    switch (object.getInt("retcode")) {
                                        case 2000:
                                            wealthQuestionData = new Gson().fromJson(response, WealthQuestionData.class);
                                            mFragmentDiscoveryGridview02.setAdapter(new CommonWealthGridviewTwoAdapter(getActivity(), wealthQuestionData.getData()));
                                            break;
                                        case 4000:
                                            mFragmentDiscoveryGridview02.setVisibility(View.GONE);
                                            break;
                                    }
                                    break;
                                case 4000:
                                    mFragmentDiscoveryGridview02.setVisibility(View.GONE);
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

    private void setData() {
        //????????????
        BannerUtils.setBannerView(mFragmentDiscoveryBanner01);
        mFragmentDiscoveryBanner01.setOnBannerListener(this);
        //??????????????????
        getBindingState();
        getBanner();
        getTopicHeader();
        getBasicNews();
        getQuestions();
        //????????????
        getService();
        img = new ArrayList<>();
        img.add(R.mipmap.faxian01);
        img.add(R.mipmap.shengmogongyi);
        img.add(R.mipmap.yuyuejiance);
        img.add(R.mipmap.faxian03);
        img.add(R.mipmap.faxian04);
        img.add(R.mipmap.faxian06);
        img.add(R.mipmap.faxian07);
        img.add(R.mipmap.gongyimujuan);
        //img.add(R.mipmap.faxian09);
        titles = new ArrayList<>();
        titles.add("????????????");
        titles.add("???????????????");
        titles.add("????????????");
        titles.add("????????????");
        titles.add("????????????");
        titles.add("?????????");
        titles.add("????????????");
        titles.add("????????????");
        //titles.add("????????????");
        content = new ArrayList<>();
        content.add("?????????????????????");
        content.add("????????????????????????????????????");
        content.add("???????????????????????????");
        content.add("??????????????? ????????????");
        content.add("??????????????????????????????");
        content.add("?????????????????????????????????");
        content.add("?????????????????????????????????");
        content.add("??????????????????????????????");
        //content.add("???????????? ????????????");
        discoveryDatas = new ArrayList<>();
        for (int i = 0; i < img.size(); i++) {
            discoveryDatas.add(new DiscoveryData(img.get(i), titles.get(i), content.get(i)));
        }
        mFragmentDiscoveryRecyclerview.setFocusable(false);
        mFragmentDiscoveryGridview.setFocusable(false);
        mFragmentDiscoveryListview.setFocusable(false);
        mFragmentDiscoveryGridview02.setFocusable(false);
        mFragmentDiscoveryGridview.setAdapter(new DiscoveryAdapter(getActivity(), discoveryDatas));
        mFragmentDiscoveryGridview.setOnItemClickListener(this);
        mFragmentDiscoveryListview.setOnItemClickListener(this);
        mFragmentDiscoveryGridview02.setOnItemClickListener(this);
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "3");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetSlideMore, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    BannerNewData data = new Gson().fromJson(response, BannerNewData.class);
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        bannerTitle.add(data.getData().get(i).getTitle());
                                        bannerPath.add(data.getData().get(i).getPath());
                                        bannerUrl.add(data.getData().get(i).getUrl());
                                        linkType.add(data.getData().get(i).getLink_type());
                                        linkId.add(data.getData().get(i).getLink_id());
                                    }
                                    //??????????????????
                                    mFragmentDiscoveryBanner01.setImages(bannerPath);
                                    mFragmentDiscoveryBanner01.start();
                                    mFragmentDiscoveryFramlayout.setVisibility(View.VISIBLE);
                                    break;
                                case 4000:
                                    mFragmentDiscoveryFramlayout.setVisibility(View.GONE);
                                    break;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

    private void getTopicHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("pid", "7");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetTopicEight, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("dynamictopictitle", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final TopicHeaderData data = new Gson().fromJson(response, TopicHeaderData.class);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mFragmentDiscoveryRecyclerview.setLayoutManager(linearLayoutManager);
                        if (data.getRetcode() == 2000) {
                            RecyclerViewDynamicAdapter recyclerAdapter = new RecyclerViewDynamicAdapter(data.getData(), getActivity());
                            mFragmentDiscoveryRecyclerview.setAdapter(recyclerAdapter);
                            recyclerAdapter.setHeaderView(View.inflate(getActivity(), R.layout.item_recycler_header, null));
                            recyclerAdapter.setOnItemClickLitener(new RecyclerViewDynamicAdapter.OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                                    intent.putExtra("tid", data.getData().get(position).getTid());
                                    intent.putExtra("topictitle", data.getData().get(position).getTitle());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            mFragmentDiscoveryRecyclerview.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getService() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.GetServiceInfo, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("commonwealth", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("retcode") == 2000) {
                                RedwomenMarkerData data = new Gson().fromJson(response, RedwomenMarkerData.class);
                                RedwomenMarkerData.DataBean data01 = data.getData().get(0);
                                RedwomenMarkerData.DataBean data02 = data.getData().get(1);
                                RedwomenMarkerData.DataBean data03 = data.getData().get(2);
                                RedwomenMarkerData.DataBean data04 = data.getData().get(3);
                                markerUid01 = data01.getUid();
                                markerUid02 = data02.getUid();
                                markerUid03 = data03.getUid();
                                markerUid04 = data04.getUid();
                                markerNickname01 = data01.getNickname();
                                markerNickname02 = data02.getNickname();
                                markerNickname03 = data03.getNickname();
                                markerNickname04 = data04.getNickname();
                                if (data01.getHead_pic().equals("") || data01.getHead_pic().equals("http://59.110.28.150:888/")) {
                                    mCommenWealthIcon01.setImageResource(R.mipmap.morentouxiang);
                                } else {
                                    GlideImgManager.glideLoader(getActivity(), data01.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mCommenWealthIcon01, 0);
                                }
                                if (data02.getHead_pic().equals("") || data02.getHead_pic().equals("http://59.110.28.150:888/")) {
                                    mCommenWealthIcon02.setImageResource(R.mipmap.morentouxiang);
                                } else {
                                    GlideImgManager.glideLoader(getActivity(), data02.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mCommenWealthIcon02, 0);
                                }
                                if (data03.getHead_pic().equals("") || data03.getHead_pic().equals("http://59.110.28.150:888/")) {
                                    mCommenWealthIcon03.setImageResource(R.mipmap.morentouxiang);
                                } else {
                                    GlideImgManager.glideLoader(getActivity(), data03.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mCommenWealthIcon03, 0);
                                }
                                if (data04.getHead_pic().equals("") || data04.getHead_pic().equals("http://59.110.28.150:888/")) {
                                    mCommenWealthIcon04.setImageResource(R.mipmap.morentouxiang);
                                } else {
                                    GlideImgManager.glideLoader(getActivity(), data04.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mCommenWealthIcon04, 0);
                                }
                                mCommenWealthName01.setText(data01.getNickname());
                                mCommenWealthName02.setText(data02.getNickname());
                                mCommenWealthName03.setText(data03.getNickname());
                                mCommenWealthName04.setText(data04.getNickname());
                            } else if (object.getInt("retcode") == 4000) {
                                mCommenWealthChatAllLl.setVisibility(View.GONE);
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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


    private void getBindingState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBindingState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    BindingData data = new Gson().fromJson(response, BindingData.class);
                    BindingData.DataBean datas = data.getData();
                    if (!datas.getMobile().equals("")) {
                        realmobile = datas.getMobile();
                        mobile = datas.getMobile().substring(0, 3) + "****" + datas.getMobile().substring(7, datas.getMobile().length());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void gettopnew() {
        Map<String, String> map = new HashMap<>();
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.topnew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    int retcode = object.getInt("retcode");
                    switch (retcode){
                        case 2000:
                            JSONObject data = object.getJSONObject("data");
                            toptitle = data.getString("title");
                            topnewurl = data.getString("url");
                            url_type = data.getString("url_type");
                            id = data.getString("id");
                            tv_topnew.setText(toptitle+"");
                            tv_topnew.setTextColor(0xffc450d6);
                            tv_topnew.setVisibility(View.VISIBLE);
                            ll_topnew.setVisibility(View.VISIBLE);
                            break;
                            default:
                                tv_topnew.setVisibility(View.GONE);
                                ll_topnew.setVisibility(View.GONE);
                                break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        String state;
        String titlename;
        if (parent == mFragmentDiscoveryGridview) {
            switch (position) {
                case 0:
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "????????????");
                    intent.putExtra("path", HttpUrl.NetPic()+"Home/info/greensm");
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "????????????");
                    intent.putExtra("path", HttpUrl.NetPic()+"Home/Info/Shengmosimu/id/16");
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "????????????");
                    intent.putExtra("path", HttpUrl.NetPic()+"Home/Info/Shengmosimu/id/18");
                    startActivity(intent);
                    break;
                case 3:
//                intent = new Intent(getActivity(), DiscoveryAboutSMActivity.class);
//                startActivity(intent);
                    state = "3";
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "????????????");
                    intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.Recruit);
                    startActivity(intent);
                    break;
                case 4:
                    state = "2";
                   /* titlename = "????????????";
                    intent = new Intent(getActivity(), WebDiscoveryActivity.class);
                    intent.putExtra("titlename", titlename);
                    intent.putExtra("state", state);
                    startActivity(intent);*/
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "????????????");
                    intent.putExtra("path", HttpUrl.NetPic()+"Home/Info/Shengmosimu/id/15");
                    startActivity(intent);

                    break;
                case 5:
                   /* state = "4";
                    titlename = "?????????";
                    intent = new Intent(getActivity(), WebDiscoveryActivity.class);
                    intent.putExtra("titlename", titlename);
                    intent.putExtra("state", state);
                    startActivity(intent);*/
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "?????????");
                    intent.putExtra("path", HttpUrl.NetPic()+"Home/Info/Shengmosimu/id/17");
                    startActivity(intent);
                    break;
                case 6:

                    intent = new Intent(getContext(), HelpCenterActivity.class);
                    startActivity(intent);
//                titlename = "????????????";
//                intent = new Intent(getActivity(), VipWebActivity.class);
//                intent.putExtra("title", titlename);
//                intent.putExtra("path", HttpUrl.PicTextHtml);
//                startActivity(intent);
                    break;
                case 7:
                    intent = new Intent(getActivity(), VipWebActivity.class);
                    intent.putExtra("title", "????????????");
                    intent.putExtra("path", HttpUrl.SMaddress);
                    startActivity(intent);
                  /*  if (realmobile.equals("")) {
                        intent = new Intent(getActivity(), BindingMobileActivity.class);
                        intent.putExtra("neworchange", "new");
                        startActivity(intent);
                    } else {
                        intent = new Intent(getActivity(), ChangeBindingMobileActivity.class);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("realmobile", realmobile);
                        startActivity(intent);
                    }*/
                    break;
                case 8:
                    intent = new Intent(getActivity(), PhotoRzActivity.class);
                    startActivity(intent);
                    break;
            }
        } else if (parent == mFragmentDiscoveryListview) {

            if ( wealthNewsData.getData().get(position).getUrl_type().equals("0")){

                intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "??????");
                if (!wealthNewsData.getData().get(position).getUrl().equals("")) {
                    intent.putExtra("path", wealthNewsData.getData().get(position).getUrl());
                } else {
//                intent.putExtra("path", "http://hao.shengmo.org:888/Home/Info/news/id/" + wealthNewsData.getData().get(position).getId());
                    //?????????url??????
                    intent.putExtra("path", HttpUrl.NetPic()+ "Home/Info/news/id/" + wealthNewsData.getData().get(position).getId());
                }

            }else  if ( wealthNewsData.getData().get(position).getUrl_type().equals("1")){

                intent = new Intent(getActivity(), DynamicDetailActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", wealthNewsData.getData().get(position).getUrl());
                intent.putExtra("pos", 1);
                intent.putExtra("showwhat", 1);

            }else  if ( wealthNewsData.getData().get(position).getUrl_type().equals("2")){

                intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtra("tid",  wealthNewsData.getData().get(position).getUrl());
                //intent.putExtra("topictitle", bannerTitle.get(position));

            }else  if ( wealthNewsData.getData().get(position).getUrl_type().equals("3")){

                intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent.putExtra("uid", wealthNewsData.getData().get(position).getUrl());
            }
            startActivity(intent);
        } else if (parent == mFragmentDiscoveryGridview02) {

            if ( wealthQuestionData.getData().get(position).getUrl_type().equals("0")){

                intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "??????");
                if (!wealthQuestionData.getData().get(position).getUrl().equals("")) {
                    intent.putExtra("path", wealthQuestionData.getData().get(position).getUrl());
                } else {
//                intent.putExtra("path", "http://hao.shengmo.org:888/Home/Info/news/id/" + wealthQuestionData.getData().get(position).getId());
                    intent.putExtra("path", HttpUrl.NetPic()+ "Home/Info/news/id/" + wealthQuestionData.getData().get(position).getId());

                }

            }else  if ( wealthQuestionData.getData().get(position).getUrl_type().equals("1")){

                intent = new Intent(getActivity(), DynamicDetailActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", wealthQuestionData.getData().get(position).getUrl());
                intent.putExtra("pos", 1);
                intent.putExtra("showwhat", 1);

            }else  if ( wealthQuestionData.getData().get(position).getUrl_type().equals("2")){

                intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtra("tid",  wealthQuestionData.getData().get(position).getUrl());
                //intent.putExtra("topictitle", bannerTitle.get(position));

            }else  if ( wealthQuestionData.getData().get(position).getUrl_type().equals("3")){

                intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent.putExtra("uid", wealthQuestionData.getData().get(position).getUrl());
            }

            startActivity(intent);
        }
    }


    @OnClick({R.id.tv_topnew,R.id.ll_morenews,R.id.ll_moreproblems,R.id.mCommenWealth_QQ, R.id.mCommenWealth_chat_ll01, R.id.mCommenWealth_chat_ll02, R.id.mCommenWealth_chat_ll03, R.id.mCommenWealth_chat_ll04, R.id.mCommenWealth_cellphone, R.id.mCommenWealth_telephone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCommenWealth_chat_ll01:
                Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent.putExtra("uid", markerUid01);
                startActivity(intent);
//                RongOpenConversationUtils.startPrivateChat(getActivity(), markerUid01, markerNickname01,
//                        new ChatFlagData("0", "0", "0", "0", "0", "0", "0"));
                break;
            case R.id.mCommenWealth_chat_ll02:
//                RongOpenConversationUtils.startPrivateChat(getActivity(), markerUid02, markerNickname02,
//                        new ChatFlagData("0", "0", "0", "0", "0", "0", "0"));
                 intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent.putExtra("uid", markerUid02);
                startActivity(intent);
                break;
            case R.id.mCommenWealth_chat_ll03:
//                RongOpenConversationUtils.startPrivateChat(getActivity(), markerUid03, markerNickname03,
//                        new ChatFlagData("0", "0", "0", "0", "0", "0", "0"));
                 intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent.putExtra("uid", markerUid03);
                startActivity(intent);
                break;
            case R.id.mCommenWealth_chat_ll04:
//                RongOpenConversationUtils.startPrivateChat(getActivity(), markerUid04, markerNickname04,
//                        new ChatFlagData("0", "0", "0", "0", "0", "0", "0"));
                 intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent.putExtra("uid", markerUid04);
                startActivity(intent);
                break;
            case R.id.mCommenWealth_cellphone:
//                ContactAlertDialog.openDialog("????????????", mCommenWealthCellphone.getText().toString(), getActivity());
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mCommenWealthCellphone.getText().toString());
                ToastUtil.show(getActivity().getApplicationContext(), "????????????");
                break;
            case R.id.mCommenWealth_telephone:
                ContactAlertDialog.openDialog("????????????", mCommenWealthTelephone.getText().toString(), getActivity());
                break;
            case R.id.ll_morenews:
                Intent intent1 = new Intent(getContext(), More_news_Activity.class);
                startActivity(intent1);
                break;
            case R.id.ll_moreproblems:
                Intent intent2 = new Intent(getContext(), MoreActivity.class);
                startActivity(intent2);
                break;
//            case R.id.mCommenWealth_QQ:
//
//                break;
            case R.id.tv_topnew:
               Intent intent11;
                if (url_type.equals("0")) {
                    intent11 = new Intent(getActivity(), BannerWebActivity.class);
                    intent11.putExtra("path", topnewurl);
                    intent11.putExtra("title","??????");
                } else if (url_type.equals("2")){
                    intent11 = new Intent(getActivity(), TopicDetailActivity.class);
                    intent11.putExtra("tid", topnewurl);
                    intent11.putExtra("topictitle",toptitle);
                } else if (url_type.equals("1")){
                    intent11 = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent11.putExtra("uid", MyApp.uid);
                    intent11.putExtra("did", topnewurl);
                    intent11.putExtra("pos", 1);
                    intent11.putExtra("showwhat", 1);
                }else {
                    intent11 = new Intent(getActivity(), PesonInfoActivity.class);
                    intent11.putExtra("uid", topnewurl);
                }
                startActivity(intent11);

                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = null;
        if (linkType.get(position).equals("0")) {
            intent = new Intent(getActivity(), BannerWebActivity.class);
            intent.putExtra("path", bannerUrl.get(position));
            intent.putExtra("title", bannerTitle.get(position));
        } else if (linkType.get(position).equals("1")){
            intent = new Intent(getActivity(), TopicDetailActivity.class);
            intent.putExtra("tid", linkId.get(position));
            intent.putExtra("topictitle", bannerTitle.get(position));
        } else if (linkType.get(position).equals("2")){
            intent = new Intent(getActivity(), DynamicDetailActivity.class);
            intent.putExtra("uid", MyApp.uid);
            intent.putExtra("did", linkId.get(position));
            intent.putExtra("pos", 1);
            intent.putExtra("showwhat", 1);
        }else {
            intent = new Intent(getActivity(), PesonInfoActivity.class);
            intent.putExtra("uid", linkId.get(position));
        }
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && hasStarted == false) {
            hasStarted = true;
            setData();
        }
    }

}
