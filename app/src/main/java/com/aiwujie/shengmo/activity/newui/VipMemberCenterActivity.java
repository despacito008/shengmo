package com.aiwujie.shengmo.activity.newui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.activity.VipWebActivity;
import com.aiwujie.shengmo.activity.Vip_BillActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.bean.PayResultData;
import com.aiwujie.shengmo.bean.VipTimeData;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.fragment.vipcenterfragment.FragmentSvip;
import com.aiwujie.shengmo.fragment.vipcenterfragment.SvipFragment;
import com.aiwujie.shengmo.fragment.vipcenterfragment.VipFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.VipDetailActivity;
import com.aiwujie.shengmo.kt.util.EasySpannableString;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AppIsInstallUtils;
import com.aiwujie.shengmo.utils.DensityUtil;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WxPayMentTaskManager;
import com.aiwujie.shengmo.view.ScrollViewPager;
import com.alipay.sdk.app.PayTask;
import com.aliyun.svideo.common.bottomnavigationbar.BottomNavigationEntity;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class VipMemberCenterActivity extends AppCompatActivity {


    @BindView(R.id.iv_vip_center_icon)
    ImageView ivVipCenterIcon;
    @BindView(R.id.tv_vip_center_txt)
    TextView tvVipCenterTxt;
    @BindView(R.id.tv_vip_center_due_date_vip)
    TextView tvVipCenterDueDateVip;
    @BindView(R.id.tv_vip_center_due_date_svip)
    TextView tvVipCenterDueDateSvip;
    @BindView(R.id.tab_layout_vip_center)
    TabLayout tabLayoutVipCenter;
    @BindView(R.id.view_pager_vip_center)
    ScrollViewPager viewPagerVipCenter;
    @BindView(R.id.iv_vip_center_return)
    ImageView ivVipCenterReturn;
    @BindView(R.id.tv_vip_center_detailed)
    TextView tvVipCenterDetailed;
    @BindView(R.id.ll_vip_center_title_bar)
    LinearLayout llVipCenterTitleBar;
    @BindView(R.id.tv_vip_center_total_price)
    TextView tvVipCenterTotalPrice;
    @BindView(R.id.tv_vip_center_rule)
    TextView tvVipCenterRule;
    @BindView(R.id.tv_vip_center_buy_money)
    TextView tvVipCenterBuyMoney;
    @BindView(R.id.tv_vip_center_buy_beans)
    TextView tvVipCenterBuyBeans;
    @BindView(R.id.ll_vip_center_tab_vip)
    LinearLayout llVipCenterTabVip;
    @BindView(R.id.ll_vip_center_tab_svip)
    LinearLayout llVipCenterTabSvip;
    @BindView(R.id.guide_line_center)
    Guideline guideLineCenter;
    @BindView(R.id.nested_scroll_vip_center)
    NestedScrollView nestedScrollVipCenter;
    @BindView(R.id.group_vip_center_us)
    Group groupVipCenterUs;
    @BindView(R.id.group_vip_center_other)
    Group groupVipCenterOther;
    @BindView(R.id.iv_vip_center_other_icon)
    ImageView ivVipCenterOtherIcon;
    @BindView(R.id.tv_vip_center_give_info)
    TextView tvVipCenterGiveInfo;
    @BindView(R.id.check_box_vip_center)
    AppCompatCheckBox checkBoxVipCenter;
    @BindView(R.id.ll_vip_center_give_tips)
    LinearLayout llVipCenterGiveTips;

    private String subject = "";
    private String price = "";
    private String beansNum = "";
    private String user_id = "";
    private String head_pic = "";
    private String isOpen = "1";
    private VipFragment vipFragment;
    private SvipFragment svipFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_vip_center);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        showUserView();
        initViewPager();
        setListener();
        getMyWallet();
    }

    void showUserView() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("uid");
        head_pic = intent.getStringExtra("headpic");
        String usHeadUrl = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", "");
        if (user_id.equals(MyApp.uid)) {
            groupVipCenterUs.setVisibility(View.VISIBLE);
            getVipOverTime();
        } else {
            groupVipCenterOther.setVisibility(View.VISIBLE);
            GlideImgManager.glideLoader(this, head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivVipCenterOtherIcon, 0);
        }
        GlideImgManager.glideLoader(this, usHeadUrl, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivVipCenterIcon, 0);

        EasySpannableString content = new EasySpannableString(VipMemberCenterActivity.this, "成为会员即表示与阅读并同意《圣魔会员协议》")
                .first("《圣魔会员协议》").onClick(tvVipCenterRule,
                        ContextCompat.getColor(VipMemberCenterActivity.this,R.color.purple_main),
                        () -> {
                    VipWebActivity.start(VipMemberCenterActivity.this, "圣魔会员协议", HttpUrl.NetPic() + HttpUrl.Vipregagreement);
                    return null;
                });
        tvVipCenterRule.setText(content);
    }

    private void getMyPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMyPresent, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("retcode") == 2000) {
                        MyPresentData myPresentData = new Gson().fromJson(response, MyPresentData.class);
                        //可使用的魔豆数量
                        beansNum = myPresentData.getData().getUseableBeans();
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

    private void getMyWallet() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getmywallet, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    WalletData data = new Gson().fromJson(response, WalletData.class);
                    beansNum = data.getData().getWallet();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getVipOverTime() {
        HttpHelper.getInstance().getVipOverTime(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                VipTimeData vipTimeData = GsonUtil.GsonToBean(data, VipTimeData.class);
                if ("1".equals(vipTimeData.getData().getVip())) {
                    tvVipCenterDueDateVip.setText("VIP到期时间：  " + vipTimeData.getData().getVipovertime());
                }
                if ("1".equals(vipTimeData.getData().getSvip())) {
                    tvVipCenterDueDateSvip.setText("SVIP到期时间：" + vipTimeData.getData().getSvipovertime());
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void initViewPager() {
        vipFragment = new VipFragment();
        svipFragment = new SvipFragment();
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(vipFragment);
        fragments.add(svipFragment);
        List<String> mTitles = new ArrayList<>();
        mTitles.add("VIP会员");
        mTitles.add("SVIP会员");
        FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

        };
        viewPagerVipCenter.setAdapter(fragmentStatePagerAdapter);
        selectTab(llVipCenterTabVip, true);
        selectTab(llVipCenterTabSvip, false);
        viewPagerVipCenter.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tvVipCenterBuyMoney.setBackgroundResource(R.drawable.bg_vip_center_buy_money);
                    selectTab(llVipCenterTabVip, true);
                    selectTab(llVipCenterTabSvip, false);
                    vipFragment.changePrice();
                } else {
                    tvVipCenterBuyMoney.setBackgroundResource(R.drawable.bg_svip_center_buy_money);
                    selectTab(llVipCenterTabVip, false);
                    selectTab(llVipCenterTabSvip, true);
                    svipFragment.changePrice();
                }

                if (!MyApp.uid.equals(user_id)) {
                    if (position == 0) {
                        llVipCenterGiveTips.setVisibility(View.GONE);
                    } else {
                        llVipCenterGiveTips.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        if (!user_id.equals(MyApp.uid)) {
//          viewPagerVipCenter.setCurrentItem(1);
//        }
        viewPagerVipCenter.setCurrentItem(1);
        if (!MyApp.uid.equals(user_id)) {
            llVipCenterGiveTips.setVisibility(View.VISIBLE);
        }
    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_vip_center, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_tab_layout_vip_center);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab_layout_vip_center);
        if (index == 0) {
            textView.setText("VIP会员");
            imageView.setImageResource(R.drawable.user_vip);
        } else {
            textView.setText("SVIP会员");
            imageView.setImageResource(R.drawable.user_svip);
        }
        return view;
    }


    void selectTab(View tabView, boolean isSelect) {
        ViewGroup.LayoutParams layoutParams = tabView.getLayoutParams();
        if (isSelect) {
            layoutParams.height = DensityUtil.dip2px(VipMemberCenterActivity.this, 40);

        } else {
            layoutParams.height = DensityUtil.dip2px(VipMemberCenterActivity.this, 30);
        }
        tabView.setLayoutParams(layoutParams);
    }

    void setListener() {

        ivVipCenterReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvVipCenterDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(VipMemberCenterActivity.this, Vip_BillActivity.class);
                Intent intent = new Intent(VipMemberCenterActivity.this, VipDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });

        llVipCenterTabVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVipCenterBuyMoney.setBackgroundResource(R.drawable.bg_vip_center_buy_money);
                viewPagerVipCenter.setCurrentItem(0);
            }
        });
        llVipCenterTabSvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVipCenterBuyMoney.setBackgroundResource(R.drawable.bg_svip_center_buy_money);
                viewPagerVipCenter.setCurrentItem(1);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollVipCenter.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                boolean showTitleBarColor = false;

                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //LogUtil.d(scrollX);
                    if (scrollY > 400) {
                        if (!showTitleBarColor) {
                            llVipCenterTitleBar.setBackgroundResource(R.color.vipTitleColor);
                            showTitleBarColor = true;
                        }
                    } else {
                        if (showTitleBarColor) {
                            llVipCenterTitleBar.setBackgroundResource(R.color.transparent);
                            showTitleBarColor = false;
                        }
                    }
                }
            });
        }

//        tvVipCenterRule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                VipWebActivity.start(VipMemberCenterActivity.this, "圣魔会员协议", HttpUrl.NetPic() + HttpUrl.Vipregagreement);
//            }
//        });

        tvVipCenterBuyBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBeansMenu();
            }
        });

        tvVipCenterBuyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayMenu();
            }
        });

        checkBoxVipCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isOpen = "1";
                } else {
                    isOpen = "2";
                }
            }
        });

        viewPagerVipCenter.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void changeItem(String subject, String price) {
        this.subject = subject;
        this.price = price;
        int p = Integer.parseInt(price);
        tvVipCenterTotalPrice.setText(p + "元/" + (p * 10) + "魔豆");
    }

    void showBeansMenu() {
        String beansStr = "金魔豆";
        if (!TextUtil.isEmpty(beansNum)) {
            beansStr += "(" + beansNum + ")";
        }
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{beansStr},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                payByBeans();
            }
        });
        alertView.show();
    }

    void showPayMenu() {
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{"微信", "支付宝"},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                if (data.equals("微信")) {
                    getOrderByWeChat();
                } else {
                    getOrderByAli();
                }
            }
        });
        alertView.show();
    }

    void getOrderByAli() {
        String type = String.valueOf(viewPagerVipCenter.getCurrentItem() + 1);
        HttpHelper.getInstance().getOrderIdBeforePay("1", type, subject, user_id, isOpen, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                payByAli(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d(msg);
            }
        });
    }

    void getOrderByWeChat() {
        String type = String.valueOf(viewPagerVipCenter.getCurrentItem() + 1);
        HttpHelper.getInstance().getOrderIdBeforePay("2", type, subject, user_id, isOpen, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                payByWeChat(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d(msg);
            }
        });
    }

    void payByBeans() {
        String type = String.valueOf(viewPagerVipCenter.getCurrentItem() + 1);
        HttpHelper.getInstance().payByBeans(type, subject, user_id, isOpen, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
                Integer retcode = jsonObject.getInteger("retcode");
                if (retcode == 2000) {
                    if (MyApp.uid.equals(user_id)) {
                        ToastUtil.show(VipMemberCenterActivity.this, "兑换成功");
                        getVipOverTime();
                    } else {
                        ToastUtil.show(VipMemberCenterActivity.this, "赠送成功");

                    }
                } else {

                    if (MyApp.uid.equals(user_id)) {
                        ToastUtil.show(VipMemberCenterActivity.this, "兑换失败，请重试");
                    } else {
                        ToastUtil.show(VipMemberCenterActivity.this, "赠送失败，请重试");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (MyApp.uid.equals(user_id)) {
                    ToastUtil.show(VipMemberCenterActivity.this, "兑换失败，请重试");
                } else {
                    ToastUtil.show(VipMemberCenterActivity.this, "赠送失败，请重试");
                }
            }
        });
    }

    void payByAli(final String data) {
        if (null == data) {
            return;
        }
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(VipMemberCenterActivity.this);
                Map<String, String> result = alipay.payV2(data, true);
                Message msg = new Message();
                msg.what = 10001;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    void payByWeChat(final String data) {
        if (null == data) {
            return;
        }
        IWXAPI api = null;
        String APP_ID = "wx0392b14b6a6f023c";
        if (AppIsInstallUtils.isWeChatAvailable(VipMemberCenterActivity.this)) {
            api = WXAPIFactory.createWXAPI(VipMemberCenterActivity.this, APP_ID);
        } else {
            ToastUtil.show(this.getApplicationContext(), "您未安装微信");
            return;
        }
        final IWXAPI finalApi = api;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(data);
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    req.extData = "app data"; // optional
                    Log.d("jim", "send return :" + finalApi.sendReq(req));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10001: {
                    PayResultData payResult = new PayResultData((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show(VipMemberCenterActivity.this.getApplicationContext(), "支付成功");
                        EventBus.getDefault().post(new BuyVipSucces());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(VipMemberCenterActivity.this.getApplicationContext(), "支付失败");
                    }
                    break;
                }

            }
        }

        ;
    };
}
