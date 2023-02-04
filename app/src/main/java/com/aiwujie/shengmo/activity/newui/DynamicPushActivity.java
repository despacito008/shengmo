package com.aiwujie.shengmo.activity.newui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.Ejection_mingxi_Activity;
import com.aiwujie.shengmo.adapter.DynamicPushRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.EjectionshengyuBean;
import com.aiwujie.shengmo.bean.PayResultData;
import com.aiwujie.shengmo.bean.PushGoodsBean;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.PushTopCardDetailActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AppIsInstallUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.alipay.sdk.app.PayTask;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

public class DynamicPushActivity extends AppCompatActivity {
    @BindView(R.id.iv_dynamic_push_return)
    ImageView ivDynamicPushReturn;
    @BindView(R.id.tv_dynamic_push_detailed)
    TextView tvDynamicPushDetailed;
    @BindView(R.id.ll_dynamic_push_title_bar)
    LinearLayout llDynamicPushTitleBar;
    @BindView(R.id.ll_dynamic_push_num)
    LinearLayout llDynamicPushNum;
    @BindView(R.id.rv_item_dynamic_push)
    RecyclerView rvItemDynamicPush;
    @BindView(R.id.tv_dynamic_push_num)
    TextView tvDynamicPushNum;
    @BindView(R.id.tv_dynamic_push_buy_money)
    TextView tvDynamicPushBuyMoney;
    @BindView(R.id.tv_vip_dynamic_push_beans)
    TextView tvVipDynamicPushBeans;

    private String num = "";
    private String beansNum;

    public static void start(Context context) {
        Intent intent = new Intent(context,DynamicPushActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_dynamic_push);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        getData();
        getusertopcardinfo();
        setListener();
    }

    private void getData() {
        HttpHelper.getInstance().getDynamicPushList(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                PushGoodsBean pushGoodsBean = GsonUtil.GsonToBean(data,PushGoodsBean.class);
                if(pushGoodsBean != null) {
                    final List<PushGoodsBean.DataBean> pushGoodsList = pushGoodsBean.getData();
                    DynamicPushRecyclerAdapter pushRecyclerAdapter = new DynamicPushRecyclerAdapter(DynamicPushActivity.this, pushGoodsList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DynamicPushActivity.this);
                    rvItemDynamicPush.setLayoutManager(linearLayoutManager);
                    rvItemDynamicPush.setAdapter(pushRecyclerAdapter);
                    pushRecyclerAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                        @Override
                        public void onItemListener(int position) {
                            num = pushGoodsList.get(position).getPush_num();

                        }
                    });
                    num = pushGoodsList.get(0).getPush_num();
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(DynamicPushActivity.this,msg);
            }
        });
//        PushGoodsBean pushGoodsBean = new PushGoodsBean();
//        final List<PushGoodsBean.DataBean> pushGoodsList = new ArrayList<>();
//        pushGoodsList.add(new PushGoodsBean.DataBean("1", "1张", "30", "30", "30", ""));
//        pushGoodsList.add(new PushGoodsBean.DataBean("3", "3张", "86", "90", "28.6", "9.5折"));
//        pushGoodsList.add(new PushGoodsBean.DataBean("10", "10张", "270", "300", "27", "9折"));
//        pushGoodsList.add(new PushGoodsBean.DataBean("30", "30张", "765", "900", "25.5", "8.5折"));
//        pushGoodsList.add(new PushGoodsBean.DataBean("90", "90张", "2160", "2700", "24", "8折"));
//        pushGoodsList.add(new PushGoodsBean.DataBean("270", "270张", "6075", "8100", "22.5", "7.5折"));
//        pushGoodsBean.setRetcode(2000);
//        pushGoodsBean.setMsg("");
//        pushGoodsBean.setData(pushGoodsList);
//        LogUtil.d(GsonUtil.GsonString(pushGoodsBean));


    }

    //推顶卡余额
    public void getusertopcardinfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Gson gson = new Gson();
                EjectionshengyuBean ejectionBean = gson.fromJson(response, EjectionshengyuBean.class);
                tvDynamicPushNum.setText(ejectionBean.getData().getWallet_topcard());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void setListener() {
        ivDynamicPushReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvDynamicPushDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(DynamicPushActivity.this, Ejection_mingxi_Activity.class));
                startActivity(new Intent(DynamicPushActivity.this, PushTopCardDetailActivity.class));
            }
        });
        tvDynamicPushBuyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayMenu();
            }
        });
        tvVipDynamicPushBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBeansMenu();
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

    private void getOrderByAli() {
        HttpHelper.getInstance().getOrderIdBeforePay("1", num, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                payByAli(data);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void getOrderByWeChat() {
        HttpHelper.getInstance().getOrderIdBeforePay("2", num, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                payByWeChat(data);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void payByBeans() {
        HttpHelper.getInstance().stampByBeans(num, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
                Integer retcode = jsonObject.getInteger("retcode");
                if(retcode == 2000) {
                    ToastUtil.show(DynamicPushActivity.this, "兑换成功");
                    getusertopcardinfo();
                } else {
                    ToastUtil.show(DynamicPushActivity.this, "兑换失败，请重试");
                }
            }

            @Override
            public void onFail(String msg) {

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
                PayTask alipay = new PayTask(DynamicPushActivity.this);
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
        if (AppIsInstallUtils.isWeChatAvailable(DynamicPushActivity.this)) {
            api = WXAPIFactory.createWXAPI(DynamicPushActivity.this, APP_ID);
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
                        ToastUtil.show(DynamicPushActivity.this.getApplicationContext(),"支付成功");
                        EventBus.getDefault().post(new BuyVipSucces());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(DynamicPushActivity.this.getApplicationContext(),"支付失败");
                    }
                    break;
                }

            }
        };
    };

}
