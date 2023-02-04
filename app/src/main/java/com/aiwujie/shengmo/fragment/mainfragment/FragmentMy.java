package com.aiwujie.shengmo.fragment.mainfragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ActivityCommonWealth;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.CollectDynamicActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.Ejection_Activity;
import com.aiwujie.shengmo.activity.GroupSquareActivity;
import com.aiwujie.shengmo.activity.GzFsHyActivity;
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.activity.RedWomenActivity;
import com.aiwujie.shengmo.activity.SeeActivity;
import com.aiwujie.shengmo.activity.SettingActivity;
import com.aiwujie.shengmo.activity.StampActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.TopicListActivity;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.bean.PersonData;
import com.aiwujie.shengmo.bean.RedwomenMarkerData;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.customview.SignDialogNew;
import com.aiwujie.shengmo.eventbus.ShareCallBackEvent;
import com.aiwujie.shengmo.eventbus.VisitEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.ServiceInfoAdapter;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NetWorkUtils;
import com.aiwujie.shengmo.utils.ShareSuccessUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;

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
public class FragmentMy extends Fragment implements View.OnClickListener, OnBannerListener {
    @BindView(R.id.mFragment_my_icon)
    ImageView mFragmentMyIcon;
    @BindView(R.id.mFragment_my_OwnerMsg)
    PercentLinearLayout mFragmentMyOwnerMsg;
    @BindView(R.id.mPersonMsg_guanzhuCount)
    TextView mPersonMsgGuanzhuCount;
    @BindView(R.id.mPersonMsg_fansCount)
    TextView mPersonMsgFansCount;
    //    @BindView(R.id.mPersonMsg_groupCount)
//    TextView mPersonMsgGroupCount;
//    @BindView(R.id.mFragment_my_Visit)
//    PercentLinearLayout mFragmentMyVisit;
    @BindView(R.id.mFragment_my_Purse)
    PercentLinearLayout mFragmentMyPurse;
    @BindView(R.id.mFragment_my_Vip)
    PercentLinearLayout mFragmentMyVip;
    @BindView(R.id.mFragment_my_Setting)
    PercentLinearLayout mFragmentMySetting;
    @BindView(R.id.mFragment_my_ll_gz)
    PercentLinearLayout mFragmentMyLlGz;
    @BindView(R.id.mFragment_my_ll_fs)
    PercentLinearLayout mFragmentMyLlFs;
    @BindView(R.id.mFragment_my_name)
    TextView mFragmentMyName;
    @BindView(R.id.mFragment_Rz)
    TextView mFragmentRz;
    @BindView(R.id.mFragment_ll_Rz)
    PercentLinearLayout mFragmentLlRz;
    @BindView(R.id.mMy_banner_close)
    ImageView mMyBannerClose;
    @BindView(R.id.mMy_banner_framlayout)
    PercentFrameLayout mMyBannerFramlayout;
    @BindView(R.id.mFragment_my_visitCount)
    TextView mFragmentMyVisitCount;
    @BindView(R.id.mFragment_ll_Share)
    PercentLinearLayout mFragmentLlShare;
    @BindView(R.id.mFragment_layout)
    PercentLinearLayout mFragmentLayout;
    @BindView(R.id.mFragment_my_Sign)
    TextView mFragmentMySign;
    @BindView(R.id.mFragment_my_collect)
    TextView mFragmentMyCollect;
    @BindView(R.id.mFragment_my_Stamp)
    PercentLinearLayout mFragmentMyStamp;
    @BindView(R.id.mFragment_my_tuiding)
    PercentLinearLayout mFragmentMyTuiding;
    @BindView(R.id.mFragment_my_ll_qz)
    PercentLinearLayout mFragmentMyLlQz;
    @BindView(R.id.mFragment_my_groupCount)
    TextView mFragmentMyGroupCount;
    @BindView(R.id.mMy_banner)
    Banner mMyBanner;
    @BindView(R.id.icon_iv1)
    ImageView icon_iv1;

    @BindView(R.id.mFragment_my_RedWommen)
    PercentLinearLayout mFragmentMyRedWommen;

    @BindView(R.id.mFragment_common_Wealth)
    PercentLinearLayout mFragmentCommonWealth;
    @BindView(R.id.rlv_service_info)
    RecyclerView rlvServiceInfo;
    private String headpic;
    private int retcode;
    private SharedPop sharedPop;
    private Tencent mTencent;
    private Bundle params;
    private List<String> bannerTitle;
    private List<String> bannerPath;
    private List<String> bannerUrl;
    private List<String> linkType;
    private List<String> linkId;
    Handler handler = new Handler();
    private Unbinder bind;
    ServiceInfoAdapter serviceInfoAdapter;
    List<RedwomenMarkerData.DataBean> list = new ArrayList<>();
    MyIUiListener myIUiListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        EventBus.getDefault().register(this);
        bind = ButterKnife.bind(this, view);
        myIUiListener = new MyIUiListener();
        //广告轮播
        BannerUtils.setBannerView(mMyBanner);
        mMyBanner.setOnBannerListener(this);
        getBanner();

        int visicount = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "visitcount", 0);
        if (visicount != 0) {
            mFragmentMyVisitCount.setVisibility(View.VISIBLE);
            mFragmentMyVisitCount.setText(visicount + "");
        }
        return view;
    }



    //
    @OnClick({R.id.mFragment_my_Visit, R.id.mFragment_my_RedWommen, R.id.mFragment_common_Wealth, R.id.mFragment_my_ll_qz, R.id.mFragment_my_tuiding, R.id.mFragment_my_Stamp, R.id.mFragment_my_collect, R.id.mFragment_my_Sign, R.id.mFragment_my_OwnerMsg, R.id.mFragment_my_Purse, R.id.mFragment_my_Vip, R.id.mFragment_my_Setting, R.id.mFragment_my_ll_gz, R.id.mFragment_my_ll_fs, R.id.mFragment_ll_Rz, R.id.mMy_banner_close, R.id.mFragment_ll_Share})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mFragment_my_tuiding:
                intent = new Intent(getActivity(), Ejection_Activity.class);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mFragment_my_OwnerMsg:
                //intent = new Intent(getActivity(), PesonInfoActivity.class);
                intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mFragment_my_Visit:
                mFragmentMyVisitCount.setVisibility(View.GONE);
                SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "visitcount", 0);
                EventBus.getDefault().post(new VisitEvent(0));
                intent = new Intent(getActivity(), SeeActivity.class);
                startActivity(intent);
                break;
            case R.id.mFragment_my_Purse:
                intent = new Intent(getActivity(), MyPurseActivity.class);
                startActivity(intent);
                break;
            case R.id.mFragment_my_Vip:
                intent = new Intent(getActivity(), VipCenterActivity.class);
                intent.putExtra("headpic", headpic);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mFragment_my_RedWommen:
                intent = new Intent(getActivity(), RedWomenActivity.class);
                startActivity(intent);
//                ToastUtil.show(getActivity(),"该功能暂不可用");
                break;
            case R.id.mFragment_common_Wealth:
                intent = new Intent(getActivity(), ActivityCommonWealth.class);
                startActivity(intent);
                break;
            case R.id.mFragment_my_Setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mFragment_my_ll_gz:
                intent = new Intent(getActivity(), GzFsHyActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("currentIndex", 0);
                intent.putExtra("who", 0);
                startActivity(intent);
                break;
            case R.id.mFragment_my_ll_fs:
                intent = new Intent(getActivity(), GzFsHyActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("currentIndex", 1);
                intent.putExtra("who", 0);
                startActivity(intent);
                break;
            case R.id.mFragment_my_ll_qz:
                intent = new Intent(getActivity(), GroupSquareActivity.class);
                intent.putExtra("groupFlag", 1);
                startActivity(intent);
                break;

            case R.id.mFragment_ll_Rz:
                intent = new Intent(getActivity(), PhotoRzActivity.class);
                intent.putExtra("retcode", retcode);
                startActivity(intent);
                break;
            case R.id.mMy_banner_close:
                mMyBannerFramlayout.setVisibility(View.GONE);
                break;
            case R.id.mFragment_ll_Share:
                //分享路径
                showShareWay();
                break;
            case R.id.mFragment_my_Sign:
                if (NetWorkUtils.isNetworkConnected(getContext().getApplicationContext())) {
                    new SignDialogNew(getActivity());
                } else {
                    ToastUtil.show(getActivity().getApplicationContext(), "请检查网络...");
                }
                break;
            case R.id.mFragment_my_collect:
                intent = new Intent(getActivity(), CollectDynamicActivity.class);
                //intent = new Intent(getActivity(), TopicListActivity.class);
                startActivity(intent);
                break;
            case R.id.mFragment_my_Stamp:
                intent = new Intent(getActivity(), StampActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void showShareWay() {
        sharedPop = new SharedPop(getActivity(), HttpUrl.NetPic()+HttpUrl.SMaddress, "分享了一个链接", getActivity().getResources().getString(R.string.share_content), HttpUrl.NetPic() + "Uploads/logo.png", 1, 3, "", "", "", "");
        sharedPop.showAtLocation(mFragmentLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getActivity().getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        getActivity().getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getActivity().getWindow().getAttributes();
                params[0].alpha = 1f;
                getActivity().getWindow().setAttributes(params[0]);
            }
        });
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "4");
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
                                    bannerTitle = new ArrayList<>();
                                    bannerPath = new ArrayList<>();
                                    bannerUrl = new ArrayList<>();
                                    linkType = new ArrayList<>();
                                    linkId = new ArrayList<>();
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        bannerTitle.add(data.getData().get(i).getTitle());
                                        bannerPath.add(data.getData().get(i).getPath());
                                        bannerUrl.add(data.getData().get(i).getUrl());
                                        linkType.add(data.getData().get(i).getLink_type());
                                        linkId.add(data.getData().get(i).getLink_id());
                                    }
                                    mMyBannerFramlayout.setVisibility(View.VISIBLE);
                                    //设置图片集合
                                    mMyBanner.setImages(bannerPath);
                                    mMyBanner.start();
                                    break;
                                case 4000:
                                    mMyBannerFramlayout.setVisibility(View.GONE);
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

    @Override
    public void onStart() {
        super.onStart();
        getServiceInfo();
    }

    private void getServiceInfo() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.NewGetServiceInfo, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("retcode") == 2000) {
                                RedwomenMarkerData data = new Gson().fromJson(response, RedwomenMarkerData.class);
                                list=data.getData();
                                if (serviceInfoAdapter==null){
                                 serviceInfoAdapter = new ServiceInfoAdapter(getContext(),list);
                                 serviceInfoAdapter.setmOnItemClickListener(new ServiceInfoAdapter.OnItemClickListener() {
                                     @Override
                                     public void onItemClick(View view,int position ) {
                                       Intent  intent = new Intent(getContext(), PesonInfoActivity.class);
                                         intent.putExtra("uid", list.get(position).getUid());
                                         getContext().startActivity(intent);
                                     }
                                 });
                                    rlvServiceInfo.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL,false){
                                        @Override
                                        public boolean canScrollVertically() {
                                            return false;
                                        }
                                    });
                                    //解决数据加载不完的问题
                                    rlvServiceInfo.setNestedScrollingEnabled(false);
                                    rlvServiceInfo.setHasFixedSize(true);
                                    rlvServiceInfo.setAdapter(serviceInfoAdapter);
                                }else {
                                    serviceInfoAdapter.notifyDataSetChanged();
                                }


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

    private void getOwnerMsg() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.OwnerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmentmymy", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PersonData data = new Gson().fromJson(response, PersonData.class);
                            headpic = data.getData().getHead_pic();
                            GlideImgManager.glideLoader(getActivity(), headpic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mFragmentMyIcon, 0);
                            mFragmentMyName.setText(data.getData().getNickname());
                            mPersonMsgGuanzhuCount.setText(data.getData().getFollow_num());
                            mPersonMsgFansCount.setText(data.getData().getFans_num());
                            mFragmentMyGroupCount.setText(data.getData().getGroup_num());
                            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "headurl", headpic);
                            //RongIM.getInstance().setCurrentUserInfo(new UserInfo(MyApp.uid, data.getData().getNickname(), Uri.parse(headpic)));
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

    private void getIdstate() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getidstate, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            retcode = obj.getInt("retcode");
                            switch (retcode) {
                                case 2000:
                                    mFragmentRz.setText(obj.getString("msg"));
                                    mFragmentRz.setTextColor(Color.parseColor("#b73acb"));
//                                    JSONObject obj1 = obj.getJSONObject("data");
//                                    picurl = obj1.getString("card_face");
                                    break;
                                case 2001:
                                    mFragmentRz.setTextColor(Color.parseColor("#777777"));
                                    mFragmentRz.setText(obj.getString("msg"));
                                    //mFragmentLlRz.setEnabled(false);
                                    break;
                                case 2002:
                                    mFragmentRz.setTextColor(Color.parseColor("#777777"));
                                    mFragmentRz.setText(obj.getString("msg"));
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

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void helloEventBus(ShareCallBackEvent event) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(MyApp.QQAPP_ID, getActivity().getApplicationContext());
        }
        switch (event.getWay()) {
            case 0:
                shareToQQ();
                break;
            case 1:
                shareToQZone();
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = null;
        if (linkType.get(position).equals("0")) {
            intent = new Intent(getActivity(), BannerWebActivity.class);
            intent.putExtra("path", bannerUrl.get(position)+"?uid="+MyApp.uid);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }


    class MyIUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.i("qqshared", "onComplete: " + o.toString());
            try {
                JSONObject object = new JSONObject(o.toString());
                if (object.getInt("ret") == 0) {
                    //分享成功
                    ShareSuccessUtils.Shared(handler);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            LogUtil.d(uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            LogUtil.d("onCancel");
        }
    }

    public void shareToQQ() {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "分享了一个链接");// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, getActivity().getResources().getString(R.string.share_content));// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, HttpUrl.SMaddress);// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, HttpUrl.NetPic() + "Uploads/logo.png");// 网络图片地址　
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(getActivity(), params,myIUiListener);
            }
        });
    }

    public void shareToQZone() {
        params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "分享了一个链接");// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, getActivity().getResources().getString(R.string.share_content));// 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, HttpUrl.SMaddress);// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
        ArrayList<String> imgUrlList = new ArrayList<>();
//        imgUrlList.add("http://59.110.28.150:888/Uploads/Picture/2017-04-18/20170418155251622.jpg");
        imgUrlList.add(HttpUrl.NetPic() + "Uploads/logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);// 图片地址
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone(getActivity(), params, new MyIUiListener());
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("editsuccess")) {
            getOwnerMsg();
        }
        if (message.equals("uploadSuccess")) {
            getIdstate();
        }

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessages(VisitEvent event) {
//        if (event.getVisitcount() != 0) {
//            mFragmentMyVisitCount.setVisibility(View.VISIBLE);
//            if (event.getVisitcount() > 99) {
//                mFragmentMyVisitCount.setText("99+");
//            } else {
//                mFragmentMyVisitCount.setText(event.getVisitcount() + "");
//            }
//        } else {
//            mFragmentMyVisitCount.setVisibility(View.INVISIBLE);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, myIUiListener);
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onResume() {
        super.onResume();
        getIdstate();
        getOwnerMsg();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
