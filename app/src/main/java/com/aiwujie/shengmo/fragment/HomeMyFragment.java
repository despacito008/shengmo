package com.aiwujie.shengmo.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.RedWomenActivity;
import com.aiwujie.shengmo.activity.SettingActivity;
import com.aiwujie.shengmo.activity.StampActivity;
import com.aiwujie.shengmo.activity.newui.DynamicPushActivity;
import com.aiwujie.shengmo.activity.newui.InvitationRewardActivity;
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.bean.CheckUserTopBean;
import com.aiwujie.shengmo.bean.HighAuthInfoBean;
import com.aiwujie.shengmo.bean.PersonData;
import com.aiwujie.shengmo.bean.RedwomenMarkerData;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.ui.activity.AllCarListActivity;
import com.aiwujie.shengmo.kt.ui.activity.ConversationCallActivity;
import com.aiwujie.shengmo.kt.ui.activity.FansClubListActivity;
import com.aiwujie.shengmo.kt.ui.activity.HighFollowAndFansActivity;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.kt.ui.activity.HighEndAuthActivity;
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity;
import com.aiwujie.shengmo.kt.ui.activity.LiveChatSettingActivity;
import com.aiwujie.shengmo.kt.ui.activity.ValueAddServicesActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.CollectedDynamicActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.HighServiceOpenActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.PurchaseRecordActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.MyAuthActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeAndGiftActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.UserRelationShipActivity;
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.GroupSquareActivity;
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.RechargeGiftActivity;
import com.aiwujie.shengmo.kt.ui.fragment.LazyFragment;
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView;
import com.aiwujie.shengmo.kt.ui.view.DailyAttendancePop;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.ui.view.RechargeBeanPop;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.kt.util.SpKey;
import com.aiwujie.shengmo.kt.util.livedata.CountDownManager;
import com.aiwujie.shengmo.kt.util.livedata.GlobalLiveData;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.ServiceInfoAdapter;
import com.aiwujie.shengmo.timlive.kt.ui.activity.LiveRoomAudienceActivity;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NetWorkUtils;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.ShareSuccessUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hjq.shape.layout.ShapeConstraintLayout;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeMyFragment extends LazyFragment {
    @BindView(R.id.tv_my_collection)
    TextView tvMyCollection;
    @BindView(R.id.ll_my_sign_in)
    LinearLayout llMySignIn;
    @BindView(R.id.iv_my_icon)
    CircleImageView ivMyIcon;
    @BindView(R.id.guide_line_1_1)
    Guideline guideLine11;
    @BindView(R.id.guide_line_1_2)
    Guideline guideLine12;
    @BindView(R.id.guide_line_1_3)
    Guideline guideLine13;
    @BindView(R.id.tv_my_follow_num)
    TextView tvMyFollowNum;
    @BindView(R.id.tv_my_fans_num)
    TextView tvMyFansNum;
    @BindView(R.id.tv_my_group_num)
    TextView tvMyGroupNum;
    @BindView(R.id.iv_my_vip_sign)
    ImageView ivMyVipSign;
    @BindView(R.id.cl_my_buy_vip)
    ConstraintLayout clMyBuyVip;
    @BindView(R.id.guide_line_2_1)
    Guideline guideLine21;
    @BindView(R.id.guide_line_2_2)
    Guideline guideLine22;
    @BindView(R.id.guide_line_2_3)
    Guideline guideLine23;
    @BindView(R.id.iv_my_gift)
    ImageView ivMyGift;
    @BindView(R.id.iv_my_gift_txt)
    TextView ivMyGiftTxt;
    @BindView(R.id.iv_my_vip)
    ImageView ivMyVip;
    @BindView(R.id.iv_my_vip_txt)
    TextView ivMyVipTxt;
    @BindView(R.id.iv_my_push)
    ImageView ivMyPush;
    @BindView(R.id.iv_my_push_txt)
    TextView ivMyPushTxt;
    @BindView(R.id.iv_my_stamp)
    ImageView ivMyStamp;
    @BindView(R.id.iv_my_stamp_txt)
    TextView ivMyStampTxt;
    @BindView(R.id.guide_line_3_1)
    Guideline guideLine31;
    @BindView(R.id.guide_line_3_2)
    Guideline guideLine32;
    @BindView(R.id.guide_line_3_3)
    Guideline guideLine33;
    @BindView(R.id.iv_my_auth)
    ImageView ivMyAuth;
    @BindView(R.id.iv_my_auth_txt)
    TextView ivMyAuthTxt;
    @BindView(R.id.iv_my_share_app)
    ImageView ivMyShareApp;
    @BindView(R.id.iv_my_share_app_txt)
    TextView ivMyShareAppTxt;
    @BindView(R.id.iv_my_setting)
    ImageView ivMySetting;
    @BindView(R.id.iv_my_setting_txt)
    TextView ivMySettingTxt;
    @BindView(R.id.guide_line_4_1)
    Guideline guideLine41;
    @BindView(R.id.iv_my_customer_service)
    ImageView ivMyCustomerService;
    @BindView(R.id.iv_my_customer_service_txt)
    TextView ivMyCustomerServiceTxt;
    Unbinder unbinder;
    @BindView(R.id.nested_scroll_container)
    NestedScrollView nestedScrollContainer;
    @BindView(R.id.rlv_service_info)
    RecyclerView rlvServiceInfo;
    @BindView(R.id.tv_my_nick_name)
    TextView tvMyNickName;
    @BindView(R.id.tv_my_follow_num_txt)
    TextView tvMyFollowNumTxt;
    @BindView(R.id.tv_my_fans_num_txt)
    TextView tvMyFansNumTxt;
    @BindView(R.id.tv_my_group_num_txt)
    TextView tvMyGroupNumTxt;
    @BindView(R.id.tv_my_home_page)
    TextView tvMyHomePage;
    @BindView(R.id.iv_my_invitation_reward)
    ImageView ivMyInvitationReward;
    @BindView(R.id.iv_my_invitation_reward_txt)
    TextView ivMyInvitationRewardTxt;
    @BindView(R.id.iv_my_one_link)
    ImageView ivMyOneLink;
    @BindView(R.id.tv_my_one_link)
    TextView tvMyOneLink;
    @BindView(R.id.iv_my_purchase_record)
    ImageView ivMyPurchaseRecord;
    @BindView(R.id.tv_my_purchase_record)
    TextView tvMyPurchaseRecord;

    @BindView(R.id.iv_high)
    ImageView ivHigh;
    @BindView(R.id.tv_high)
    TextView tvHigh;

    @BindView(R.id.iv_my_high)
    ImageView ivMyHigh;
    @BindView(R.id.tv_my_high)
    TextView tvMyHigh;

    @BindView(R.id.iv_my_red)
    ImageView ivMyRed;
    @BindView(R.id.tv_my_red)
    TextView tvMyRed;

    @BindView(R.id.iv_my_car)
    ImageView ivMyCar;
    @BindView(R.id.tv_my_car)
    TextView tvMyCar;

    @BindView(R.id.iv_my_call)
    ImageView ivMyCall;
    @BindView(R.id.tv_my_call)
    TextView tvMyCall;

    @BindView(R.id.iv_my_fensi)
    ImageView ivMyFensi;
    @BindView(R.id.tv_my_fensi)
    TextView tvMyFensi;

    @BindView(R.id.ad_view_my)
    AdvertisementView adViewMy;

    @BindView(R.id.contrainlayout_high)
    ShapeConstraintLayout contrainlayoutHigh;

    @BindView(R.id.iv_Highicon)
    ImageView ivHighicon;
    @BindView(R.id.tv_high_nick_name)
    TextView tvHighNickName;
    @BindView(R.id.tv_high_home_page)
    TextView tvHighHomePage;

    @BindView(R.id.tv_high_follow_num)
    TextView tvHighFollowNum;

    @BindView(R.id.tv_high_fans_num)
    TextView tvHighFansNum;

    @BindView(R.id.tv_high_follow_num_txt)
    TextView tvHighFollowNumTxt;

    @BindView(R.id.tv_high_fans_num_txt)
    TextView tvHighFansNumTxt;

    private String headpic;
    private int retcode;
    private SharedPop sharedPop;
    private Tencent mTencent;
    private Bundle params;
    Handler handler = new Handler();
    private Unbinder bind;
    ServiceInfoAdapter serviceInfoAdapter;
    List<RedwomenMarkerData.DataBean> list = new ArrayList<>();
    MyIUiListener myIUiListener;
    private String topUid;
    private String isOver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
//        myIUiListener = new MyIUiListener();
//        //广告轮播
//        BannerUtils.setBannerView(mMyBanner);
//        setListener();
//        getBanner();
        return view;
    }

    void setListener() {
        View.OnClickListener userInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.start(getActivity(), MyApp.uid);
            }
        };
        View.OnClickListener followInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRelationShipActivity.Companion.start(getActivity(), MyApp.uid, 0);
            }
        };
        View.OnClickListener fansInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRelationShipActivity.Companion.start(getActivity(), MyApp.uid, 1);
            }
        };
        View.OnClickListener groupInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), GroupSquareActivity.class);
//                intent.putExtra("groupFlag", 1);
//                startActivity(intent);
                GroupSquareActivity.Companion.startActivity(getActivity(), 1);
            }
        };
        View.OnClickListener giftInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), MyPurseActivity.class);
                // Intent intent = new Intent(getActivity(), RechargeAndGiftActivity.class);
                Intent intent = new Intent(getActivity(), RechargeGiftActivity.class);
                startActivity(intent);
            }
        };
        View.OnClickListener vipInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), VipCenterActivity.class);
                Intent intent = new Intent(getActivity(), VipMemberCenterActivity.class);
                //Intent intent = new Intent(getActivity(), RewardRankingActivity.class);
                intent.putExtra("headpic", headpic);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                //ActivityJumpUtil.toActivity(getActivity(),intent);
            }
        };
        View.OnClickListener pushInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), Ejection_Activity.class);
                Intent intent = new Intent(getActivity(), DynamicPushActivity.class);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
            }
        };
        View.OnClickListener stampInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StampActivity.class);
                startActivity(intent);
//                ValueAddServicesActivity.Companion.start(getActivity());
            }
        };
        View.OnClickListener matchmakerInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RedWomenActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener authInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), PhotoRzActivity.class);
                Intent intent = new Intent(getActivity(), MyAuthActivity.class);
                intent.putExtra("retcode", retcode);
                startActivity(intent);
            }
        };

        View.OnClickListener shareAppListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享路径
                //showShareWay();
                showSharePop();
//                Intent intent = new Intent(getActivity(), RedWomenActivity.class);
//                startActivity(intent);
                // String url = "http://image.aiwujie.com.cn/invite.png?x-oss-process=image/watermark,text_MSAyIDMgNCA1IDY=,color_DB57F3,size_35,g_se,x_152,y_92";
                // WechatShareManager.getInstance(getActivity()).shareWeChatImage(getActivity(),0,url);
//                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "_shengmo");
//                String imageFileName = "share_qq_" + MyApp.uid + ".jpg";
//                final File imageFile = new File(storageDir, imageFileName);
//                if (imageFile.exists()) {
//                    //com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("图片已保存");
//                    QqShareManager.getInstance(getActivity()).shareImageToQQZoneByPath(imageFile.getPath());
//                } else {
//                    Glide.with(getActivity()).downloadOnly().load(url).into(new SimpleTarget<File>() {
//                        @Override
//                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//                            FileUtil.copy(resource, imageFile);
//                            QqShareManager.getInstance(getActivity()).shareImageToQQByPath(resource.getPath());
//                        }
//                    });
//                }


            }
        };

        View.OnClickListener settingInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener collectInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), CollectDynamicActivity.class);
                Intent intent = new Intent(getActivity(), CollectedDynamicActivity.class);
                //intent = new Intent(getActivity(), TopicListActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener signEveryDayListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetWorkUtils.isNetworkConnected(getContext().getApplicationContext())) {
                    //new SignDialogNew(getActivity());
                    showSignPop();
                    //showRechargePop();
                } else {
                    ToastUtil.show(getActivity().getApplicationContext(), "请检查网络...");
                }
            }
        };

        View.OnClickListener inviteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InvitationRewardActivity.class);
                //intent = new Intent(getActivity(), TopicListActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener linkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LiveChatSettingActivity.class);

                startActivity(intent);
            }
        };

        View.OnClickListener recordListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PurchaseRecordActivity.class);
                startActivity(intent);

                //LiveRoomAudienceActivity.Companion.start(getActivity());
            }
        };
        View.OnClickListener highListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTopId();
            }
        };

        View.OnClickListener highFriendListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HighServiceOpenActivity.class));
            }
        };

        View.OnClickListener highUserListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ("1".equals(isOver)) {
//                    ToastUtil.show(getActivity(), "高端服务已到期");
//                } else {
                startActivity(new Intent(getActivity(), HighEndUserActivity.class).putExtra(IntentKey.UID, topUid));
//                }


            }
        };
        View.OnClickListener buyCarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AllCarListActivity.class));
            }
        };

        View.OnClickListener callListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationCallActivity.Companion.start(getActivity());
            }
        };

        View.OnClickListener fensiGroupListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FansClubListActivity.Companion.start(getActivity());
            }
        };

        View.OnClickListener highFollowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighFollowAndFansActivity.Companion.start(getActivity(), topUid, 0);
            }
        };

        View.OnClickListener highFansListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighFollowAndFansActivity.Companion.start(getActivity(), topUid, 1);
            }
        };

        ivMyIcon.setOnClickListener(userInfoListener);
        tvMyNickName.setOnClickListener(userInfoListener);
        tvMyHomePage.setOnClickListener(userInfoListener);
        tvMyFollowNum.setOnClickListener(followInfoListener);
        tvMyFollowNumTxt.setOnClickListener(followInfoListener);
        tvMyFansNum.setOnClickListener(fansInfoListener);
        tvMyFansNumTxt.setOnClickListener(fansInfoListener);
        tvMyGroupNum.setOnClickListener(groupInfoListener);
        tvMyGroupNumTxt.setOnClickListener(groupInfoListener);
        ivMyGift.setOnClickListener(giftInfoListener);
        ivMyGiftTxt.setOnClickListener(giftInfoListener);
        ivMyVip.setOnClickListener(vipInfoListener);
        ivMyVipTxt.setOnClickListener(vipInfoListener);
        clMyBuyVip.setOnClickListener(vipInfoListener);
        ivMyStamp.setOnClickListener(stampInfoListener);
        ivMyStampTxt.setOnClickListener(stampInfoListener);
        ivMyPush.setOnClickListener(pushInfoListener);
        ivMyPushTxt.setOnClickListener(pushInfoListener);
        ivMyAuth.setOnClickListener(authInfoListener);
        ivMyAuthTxt.setOnClickListener(authInfoListener);
        ivMyShareApp.setOnClickListener(shareAppListener);
        ivMyShareAppTxt.setOnClickListener(shareAppListener);
        ivMySetting.setOnClickListener(settingInfoListener);
        ivMySettingTxt.setOnClickListener(settingInfoListener);
        llMySignIn.setOnClickListener(signEveryDayListener);
        tvMyCollection.setOnClickListener(collectInfoListener);
        ivMyInvitationReward.setOnClickListener(inviteListener);
        ivMyInvitationRewardTxt.setOnClickListener(inviteListener);
        ivMyOneLink.setOnClickListener(linkListener);
        tvMyOneLink.setOnClickListener(linkListener);
        ivMyPurchaseRecord.setOnClickListener(recordListener);
        tvMyPurchaseRecord.setOnClickListener(recordListener);
        ivHigh.setOnClickListener(highListener);
        tvHigh.setOnClickListener(highListener);
        ivMyHigh.setOnClickListener(highFriendListener);
        tvMyHigh.setOnClickListener(highFriendListener);
        ivMyRed.setOnClickListener(matchmakerInfoListener);
        tvMyRed.setOnClickListener(matchmakerInfoListener);
        contrainlayoutHigh.setOnClickListener(highUserListener);
        ivMyCar.setOnClickListener(buyCarListener);
        tvMyCar.setOnClickListener(buyCarListener);
        ivMyCall.setOnClickListener(callListener);
        tvMyCall.setOnClickListener(callListener);
        ivMyFensi.setOnClickListener(fensiGroupListener);
        tvMyFensi.setOnClickListener(fensiGroupListener);
        tvHighFansNumTxt.setOnClickListener(highFansListener);
        tvHighFansNum.setOnClickListener(highFansListener);
        tvHighFollowNumTxt.setOnClickListener(highFollowListener);
        tvHighFollowNum.setOnClickListener(highFollowListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showShareWay() {
        sharedPop = new SharedPop(getActivity(), HttpUrl.SMaddress, "分享了一个链接", getActivity().getResources().getString(R.string.share_content), HttpUrl.NetPic() + "Uploads/logo.png", 1, 3, "", "", "", "");
        sharedPop.showAtLocation(nestedScrollContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        sharedPop.setOnShareListener(new SharedPop.OnShareListener() {
            @Override
            public void qqShare() {
                helloEventBus(0);
            }

            @Override
            public void qqZoneShare() {
                helloEventBus(1);
            }
        });

    }

    private void showSharePop() {
        NormalShareBean normalShareBean = new NormalShareBean(0, "",
                "分享一个链接",
                "",
                getActivity().getResources().getString(R.string.share_content),
                HttpUrl.SMaddress,
                SharedPreferencesUtils.geParam(getActivity(), SpKey.IMAGE_HOST, "http://image.aiwujie.com.cn/") + "Uploads/logo.png");
        NormalSharePop normalSharePop = new NormalSharePop(getActivity(), normalShareBean, false);
        normalSharePop.showPopupWindow();

//        GlobalLiveData.Companion.getInstance().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                tvHigh.setText(s);
//            }
//        });
//
//        GlobalLiveData.Companion.getInstance().startCount(50L);
    }

    private void getBanner() {
        HttpHelper.getInstance().getBanner(4, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                BannerNewData bannerNewData = GsonUtil.GsonToBean(data, BannerNewData.class);
                adViewMy.initData(bannerNewData);
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void checkTopId() {

        HttpHelper.getInstance().getHighAuthInfo("1", new HttpCodeMsgListener() {

            @Override
            public void onSuccess(String data, String msg) {
                HighAuthInfoBean model = GsonUtil.GsonToBean(data, HighAuthInfoBean.class);
                Intent intent = new Intent(getActivity(), HighEndAuthActivity.class);
                if (model.data.user_top_data != null) {
                    intent.putExtra("wealth", model.data.user_top_data.getTop_cc_status());
                    intent.putExtra("health", model.data.user_top_data.getTop_jk_status());
                    intent.putExtra("education", model.data.user_top_data.getTop_xl_status());
                    intent.putExtra("skill", model.data.user_top_data.getTop_jn_status());
                    intent.putExtra("other", model.data.user_top_data.getTop_qt_status());
                }

                intent.putExtra(IntentKey.FLAG, model.data.is_top);
                startActivity(intent);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(getActivity(), msg);
            }
        });
    }


    private void getServiceInfo() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.NewGetServiceInfo, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (rlvServiceInfo == null) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("retcode") == 2000) {
                        RedwomenMarkerData data = new Gson().fromJson(response, RedwomenMarkerData.class);
                        list = data.getData();
                        if (serviceInfoAdapter == null) {
                            serviceInfoAdapter = new ServiceInfoAdapter(getContext(), list);
                            serviceInfoAdapter.setmOnItemClickListener(new ServiceInfoAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getContext(), PesonInfoActivity.class);
                                    intent.putExtra("uid", list.get(position).getUid());
                                    getContext().startActivity(intent);
                                }
                            });
                            rlvServiceInfo.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false) {
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            });
                            //解决数据加载不完的问题
                            rlvServiceInfo.setNestedScrollingEnabled(false);
                            rlvServiceInfo.setHasFixedSize(true);
                            rlvServiceInfo.setAdapter(serviceInfoAdapter);
                        } else {
                            serviceInfoAdapter.notifyDataSetChanged();
                        }


                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                try {
                    PersonData data = new Gson().fromJson(response, PersonData.class);
                    headpic = data.getData().getHead_pic();
                    // GlideImgManager.glideLoader(getActivity(), headpic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivMyIcon, 0);
                    ImageLoader.loadCircleImageWithoutHolder(getActivity(), headpic, ivMyIcon);
                    tvMyNickName.setText(data.getData().getNickname());
                    tvMyFollowNum.setText(data.getData().getFollow_num());
                    tvMyFansNum.setText(data.getData().getFans_num());
                    tvMyGroupNum.setText(data.getData().getGroup_num());
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "headurl", headpic);
                    //RongIM.getInstance().setCurrentUserInfo(new UserInfo(MyApp.uid, data.getData().getNickname(), Uri.parse(headpic)));
                    isOver = data.getData().getIs_over();
                    if ("1".equals(data.getData().getIs_top())) {
                        topUid = data.getData().getTop_id();
                        contrainlayoutHigh.setVisibility(View.VISIBLE);
                        GlideImgManager.glideLoader(getActivity(), data.getData().getTop_head_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivHighicon, 0);
                        tvHighNickName.setText(data.getData().getSerial_id());
                    } else {
                        contrainlayoutHigh.setVisibility(View.GONE);
                    }

                    tvHighFollowNum.setText(data.getData().getTop_follow_num());
                    tvHighFansNum.setText(data.getData().getTop_fans_num());

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
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
                                    // mFragmentRz.setText(obj.getString("msg"));
                                    //mFragmentRz.setTextColor(Color.parseColor("#b73acb"));
//                                    JSONObject obj1 = obj.getJSONObject("data");
//                                    picurl = obj1.getString("card_face");
                                    break;
                                case 2001:
                                    // mFragmentRz.setTextColor(Color.parseColor("#777777"));
                                    // mFragmentRz.setText(obj.getString("msg"));
                                    //mFragmentLlRz.setEnabled(false);
                                    break;
                                case 2002:
                                    // mFragmentRz.setTextColor(Color.parseColor("#777777"));
                                    // mFragmentRz.setText(obj.getString("msg"));
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
                mTencent.shareToQQ(getActivity(), params, myIUiListener);
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
        if (message.equals("qq_share")) {
            ToastUtil.show("qq share");
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("HomeMyFragment", "onResume");
        getOwnerMsg();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void helloEventBus(int type) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(MyApp.QQAPP_ID, getActivity().getApplicationContext());
        }
        switch (type) {
            case 0:
                shareToQQ();
                break;
            case 1:
                shareToQZone();
                break;
        }
    }

    void showSignPop() {
        // SignInPop signInPop = new SignInPop(getActivity());
        // signInPop.showPopupWindow();
        DailyAttendancePop dailyAttendancePop = new DailyAttendancePop(getActivity());
        dailyAttendancePop.showPopupWindow();
    }

    void showRechargePop() {
        RechargeBeanPop rechargeBeanPop = new RechargeBeanPop(getActivity());
        rechargeBeanPop.showPopupWindow();
        //LiveLotteryDrawPop liveLotteryDrawPop = new LiveLotteryDrawPop(getActivity());
        //liveLotteryDrawPop.showPopupWindow();
    }

    @Override
    public void loadData() {
        myIUiListener = new MyIUiListener();
        setListener();
        getBanner();
        getIdstate();
        getServiceInfo();
        getOwnerMsg();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.v("HomeMyFragment", "setUserVisibleHint");
            getOwnerMsg();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.app_fragment_my;
    }

    @Override
    public void initView(@NotNull View rootView) {

    }
}
