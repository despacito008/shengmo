package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.adapter.MessageStampRecyclerAdapter;
import com.aiwujie.shengmo.adapter.StampGridviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.bean.RechargeData;
import com.aiwujie.shengmo.bean.StampData;
import com.aiwujie.shengmo.bean.StampGoodsBean;
import com.aiwujie.shengmo.bean.StampReceiveData;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.customview.RealnameDialog;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.eventbus.JumpDynamicEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.StampDetailActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AliPayMentTaskManager;
import com.aiwujie.shengmo.utils.GoToAppStoreUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.ShareSuccessUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WxPayMentTaskManager;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.connect.common.Constants;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StampActivity extends AppCompatActivity implements OnItemClickListener, AdapterView.OnItemClickListener, View.OnFocusChangeListener {

    @BindView(R.id.iv_stamp_return)
    ImageView ivStampReturn;
    @BindView(R.id.tv_stamp_detailed)
    TextView tvStampDetailed;
    @BindView(R.id.ll_stamp_title_bar)
    LinearLayout llStampTitleBar;
    @BindView(R.id.view_stamp_top_bg)
    View viewStampTopBg;
    @BindView(R.id.mStamp_count)
    TextView mStampCount;
    @BindView(R.id.tv_stamp_count_info)
    TextView tvStampCountInfo;
    @BindView(R.id.tv_stamp_count_tips)
    TextView tvStampCountTips;
    @BindView(R.id.rv_stamp)
    RecyclerView rvStamp;
    @BindView(R.id.tv_stamp_buy_money)
    TextView tvStampBuyMoney;
    @BindView(R.id.tv_vip_stamp_beans)
    TextView tvVipStampBeans;
    @BindView(R.id.iv_stamp_limit)
    ImageView ivStampLimit;
    @BindView(R.id.mStamp_give_stamp)
    TextView mStampGiveStamp;
    @BindView(R.id.tv_stamp_limit_tips)
    TextView tvStampLimitTips;
    @BindView(R.id.iv_stamp_limit_boy)
    ImageView ivStampLimitBoy;
    @BindView(R.id.iv_stamp_limit_girl)
    ImageView ivStampLimitGirl;
    @BindView(R.id.iv_stamp_limit_cdts)
    ImageView ivStampLimitCdts;
    @BindView(R.id.mStamp_manYpCount)
    TextView mStampManYpCount;
    @BindView(R.id.mStamp_womanYpCount)
    TextView mStampWomanYpCount;
    @BindView(R.id.mStamp_cdtsYpCount)
    TextView mStampCdtsYpCount;
    @BindView(R.id.tv_stamp_free_txt)
    TextView tvStampFreeTxt;
    @BindView(R.id.iv_stamp_auth)
    ImageView ivStampAuth;
    @BindView(R.id.mStamp_commenUser_tv)
    TextView mStampCommenUserTv;
    @BindView(R.id.mStamp_commenReceive)
    TextView mStampCommenReceive;
    @BindView(R.id.iv_stamp_vip)
    ImageView ivStampVip;
    @BindView(R.id.mStamp_viprealnameUser_tv)
    TextView mStampViprealnameUserTv;
    @BindView(R.id.mStamp_viprealnameReceive)
    TextView mStampViprealnameReceive;
    @BindView(R.id.tv_stamp_task_day_txt)
    TextView tvStampTaskDayTxt;
    @BindView(R.id.iv_stamp_thumb_up)
    ImageView ivStampThumbUp;
    @BindView(R.id.mStamp_laudCount_tv)
    TextView mStampLaudCountTv;
    @BindView(R.id.tv_stamp_task_thumb_up_tips)
    TextView tvStampTaskThumbUpTips;
    @BindView(R.id.mStamp_laudGo)
    TextView mStampLaudGo;
    @BindView(R.id.mStamp_laudCount)
    TextView mStampLaudCount;
    @BindView(R.id.iv_stamp_share)
    ImageView ivStampShare;
    @BindView(R.id.mStamp_shareCount_tv)
    TextView mStampShareCountTv;
    @BindView(R.id.tv_stamp_task_share_tips)
    TextView tvStampTaskShareTips;
    @BindView(R.id.mStamp_shareGo)
    TextView mStampShareGo;
    @BindView(R.id.mStamp_shareCount)
    TextView mStampShareCount;
    @BindView(R.id.iv_stamp_beans)
    ImageView ivStampBeans;
    @BindView(R.id.mStamp_mouDouCount_tv)
    TextView mStampMouDouCountTv;
    @BindView(R.id.tv_stamp_task_beans_tips)
    TextView tvStampTaskBeansTips;
    @BindView(R.id.mStamp_giveMouDouGo)
    TextView mStampGiveMouDouGo;
    @BindView(R.id.mStamp_giveMouDouCount)
    TextView mStampGiveMouDouCount;
    @BindView(R.id.tv_stamp_task_once_txt)
    TextView tvStampTaskOnceTxt;
    @BindView(R.id.iv_stamp_bind_phone)
    ImageView ivStampBindPhone;
    @BindView(R.id.mStamp_bingPhone_tv)
    TextView mStampBingPhoneTv;
    @BindView(R.id.tv_stamp_task_bind_phone_tips)
    TextView tvStampTaskBindPhoneTips;
    @BindView(R.id.mStamp_bingPhoneGo)
    TextView mStampBingPhoneGo;
    @BindView(R.id.iv_stamp_recommend)
    ImageView ivStampRecommend;
    @BindView(R.id.mStamp_commentApp_tv)
    TextView mStampCommentAppTv;
    @BindView(R.id.mStamp_commentAppGo)
    TextView mStampCommentAppGo;
    @BindView(R.id.mStamp_return)
    ImageView mStampReturn;
    @BindView(R.id.mStamp_zhangdan)
    TextView mStampZhangdan;
    @BindView(R.id.mStamp_count1)
    TextView mStampCount1;
    @BindView(R.id.mStamp_gridview)
    MyGridview mStampGridview;
    @BindView(R.id.mStamp_etCount)
    EditText mStampEtCount;
    @BindView(R.id.duoshao)
    TextView duoshao;
    @BindView(R.id.mStamp_buy)
    TextView mStampBuy;
    @BindView(R.id.mStamp_give_stamp1)
    TextView mStampGiveStamp1;
    @BindView(R.id.mStamp_manYpCount1)
    TextView mStampManYpCount1;
    @BindView(R.id.mStamp_womanYpCount1)
    TextView mStampWomanYpCount1;
    @BindView(R.id.mStamp_cdtsYpCount1)
    TextView mStampCdtsYpCount1;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.activity_stamp)
    LinearLayout activityStamp;
    private InputMethodManager mInputMethodManager;
    private StampGridviewAdapter stampGridviewAdapter;
    private int stampNum = 0;
    private RechargeData czdata;
    private List<RechargeData> czdatas = new ArrayList<>();
    private SharedPop sharedPop;
    private Tencent mTencent;
    private Bundle params;
    private Handler handler = new Handler();
    private String czmodou = "0";
    private String lwmodou = "0";
    private List<StampGoodsBean.DataBean> stampGoodsList;

    public static void start(Context context) {
        Intent intent = new Intent(context,StampActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_stamp);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        getMyPresent();
        getmywallet();
        setListener();
        duoshao = (TextView) findViewById(R.id.duoshao);
        mStampEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = mStampEtCount.getText().toString();
                if (!s1.equals("") && s1 != null) {
                    Integer integer = Integer.valueOf(s1);
                    duoshao.setText("???" + integer * 2 + "/" + integer * 20 + "??????");
                } else {
                    duoshao.setText("???0/0??????");
                }

            }
        });


        ivStampReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvStampDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(StampActivity.this, StampBillActivity.class);
                Intent intent = new Intent(StampActivity.this, StampDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setData() {
//        czdata = new RechargeData();
//        czdata.setMoney("1???");
//        czdata.setModou("2");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("3???");
//        czdata.setModou("6");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("5???");
//        czdata.setModou("10");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("10???");
//        czdata.setModou("20");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("30???");
//        czdata.setModou("60");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("50???");
//        czdata.setModou("100");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("100???");
//        czdata.setModou("200");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("200???");
//        czdata.setModou("400");
//        czdatas.add(czdata);
//        czdata = new RechargeData();
//        czdata.setMoney("300???");
//        czdata.setModou("600");
//        czdatas.add(czdata);
//        LogUtil.d(GsonUtil.GsonString(czdatas));
        SpannableStringBuilder builder = new SpannableStringBuilder(mStampLaudCountTv.getText().toString());
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#DB57F3"));
        builder.setSpan(purSpan, 10, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampLaudCountTv.setText(builder);
        SpannableStringBuilder builder3 = new SpannableStringBuilder(mStampShareCountTv.getText().toString());
        builder3.setSpan(purSpan, 10, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampShareCountTv.setText(builder3);
        SpannableStringBuilder builder4 = new SpannableStringBuilder(mStampMouDouCountTv.getText().toString());
        builder4.setSpan(purSpan, 13, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampMouDouCountTv.setText(builder4);
        SpannableStringBuilder builder5 = new SpannableStringBuilder(mStampBingPhoneTv.getText().toString());
        builder5.setSpan(purSpan, 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampBingPhoneTv.setText(builder5);
        SpannableStringBuilder builder6 = new SpannableStringBuilder(mStampCommentAppTv.getText().toString());
        builder6.setSpan(purSpan, 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampCommentAppTv.setText(builder6);
        SpannableStringBuilder builder7 = new SpannableStringBuilder(mStampCommenUserTv.getText().toString());
        builder7.setSpan(purSpan, 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampCommenUserTv.setText(builder7);
        SpannableStringBuilder builder8 = new SpannableStringBuilder(mStampViprealnameUserTv.getText().toString());
        builder8.setSpan(purSpan, 10, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStampViprealnameUserTv.setText(builder8);

        //??????????????????
//        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        stampGridviewAdapter = new StampGridviewAdapter(this, czdatas);
//        mStampGridview.setAdapter(stampGridviewAdapter);
//        mStampGridview.setOnItemClickListener(this);
//        mStampEtCount.setOnFocusChangeListener(this);

        HttpHelper.getInstance().getStampList(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                //LogUtil.d(data);
                StampGoodsBean stampGoodsBean = GsonUtil.GsonToBean(data, StampGoodsBean.class);
                if(stampGoodsBean != null) {
                    stampGoodsList = stampGoodsBean.getData();
                    MessageStampRecyclerAdapter stampRecyclerAdapter = new MessageStampRecyclerAdapter(StampActivity.this, stampGoodsList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(StampActivity.this, 3);
                    rvStamp.setAdapter(stampRecyclerAdapter);
                    rvStamp.setLayoutManager(gridLayoutManager);
                    stampRecyclerAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                        @Override
                        public void onItemListener(int position) {
                            stampNum = Integer.parseInt(stampGoodsList.get(position).getModou().substring(0, stampGoodsList.get(position).getModou().length() - 1));
                        }
                    });
                    stampNum = Integer.parseInt(stampGoodsList.get(0).getModou().substring(0, stampGoodsList.get(0).getModou().length() - 1));
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });


    }

    void setListener() {
        tvStampBuyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayMenu();
            }
        });
        tvVipStampBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBeansMenu();
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        stampGridviewAdapter.setSelectIndex(position);
        stampGridviewAdapter.notifyDataSetChanged();
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(mStampEtCount.getWindowToken(), 0);// ??????????????????
        }
        mStampEtCount.clearFocus();
        mStampEtCount.setText("");
        stampNum = Integer.parseInt(czdatas.get(position).getMoney().substring(0, czdatas.get(position).getMoney().length() - 1));
        new AlertView(null, null, "??????", null,
                new String[]{"?????????(" + czmodou + ")", "?????????", "??????"},
                StampActivity.this, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            stampGridviewAdapter.setSelectIndex(-1);
            stampGridviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                       /* new AlertView(null, null, "??????", null,
                                new String[]{"???????????????"+czmodou+" ?????????", "???????????????"+lwmodou+" ?????????"},
                                StampActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                String channel;
                                switch (position) {
                                    case 0:

                                        break;
                                    case 1:
                                        //?????????????????????????????????
                                        channel = "1";
                                        StampByMouDou(channel);
                                        break;
                                }
                            }
                        }).show();*/
                        //channel = "0";
                        //?????????????????????????????????
                        StampByMouDou("0");
                    }
                }, 500);
                break;
            case 1:
//                new PaymentTask().execute(new PaymentRequest(MyApp.uid, stampNum, CHANNEL));
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", MyApp.uid);
                    object.put("num", stampNum);
                    new AliPayMentTaskManager(this, HttpUrl.ALIPAYstampcharge, object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
//                new PaymentTask().execute(new PaymentRequest(MyApp.uid, stampNum, CHANNEL));
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", MyApp.uid);
                    object.put("num", stampNum);
                    object.put("appName", "shengmo");
                    new WxPayMentTaskManager(this, HttpUrl.WXCstampcharge, object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getmywallet() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getmywallet, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WalletData data = new Gson().fromJson(response, WalletData.class);
                            czmodou = data.getData().getWallet();

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

    private void getMyPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMyPresent, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fanhuibili", "run: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("retcode") == 2000) {
                                MyPresentData myPresentData = new Gson().fromJson(response, MyPresentData.class);
                                //????????????????????????
                                lwmodou = myPresentData.getData().getUseableBeans();

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


    @OnClick({R.id.mStamp_commenReceive, R.id.mStamp_viprealnameReceive, R.id.mStamp_return, R.id.mStamp_zhangdan, R.id.mStamp_buy, R.id.mStamp_laudGo, R.id.mStamp_shareGo, R.id.mStamp_giveMouDouGo, R.id.mStamp_bingPhoneGo, R.id.mStamp_commentAppGo})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mStamp_return:
                finish();
                break;
            case R.id.mStamp_zhangdan:
                intent = new Intent(this, StampBillActivity.class);
                startActivity(intent);
                break;
            case R.id.mStamp_buy:
                if (!TextUtil.isEmpty(mStampEtCount.getText().toString())) {
                    if (Integer.parseInt(mStampEtCount.getText().toString()) != 0) {
                        stampNum = Integer.parseInt(mStampEtCount.getText().toString());
                        new AlertView(null, null, "??????", null,
                                new String[]{"?????????(" + czmodou + ")", "?????????", "??????"},
                                this, AlertView.Style.ActionSheet, this).show();
                    } else {
                        ToastUtil.show(getApplicationContext(), "?????????????????????...");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "?????????????????????...");
                }
                break;
            case R.id.mStamp_laudGo:
            case R.id.mStamp_giveMouDouGo:
                EventBus.getDefault().post(new JumpDynamicEvent());
                finish();
                break;
            case R.id.mStamp_shareGo:
                showShareWay();
                break;
            case R.id.mStamp_bingPhoneGo:
                intent = new Intent(this, BindingMobileActivity.class);
                intent.putExtra("neworchange", "new");
                startActivity(intent);
                break;
            case R.id.mStamp_commentAppGo:
                GoToAppStoreUtils.goToMarket(this, "com.aiwujie.shengmo", handler);
                break;
            case R.id.mStamp_commenReceive:
                narmalUserGetBasicStamp();
                //String realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
                //????????????????????????
//                if (realname.equals("1")) {
//                    narmalUserGetBasicStamp();
//                } else {
//                    new RealnameDialog(this, "??????????????????????????????~");
//                }
                break;
            case R.id.mStamp_viprealnameReceive:
                //VIP??????????????????
                String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                if (vip.equals("1")) {
                    viplUserGetBasicStamp();
                } else {
                    new VipDialog(this, "???VIP?????????????????????~");
                }
                break;
        }
    }


    private void narmalUserGetBasicStamp() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.NarmalUserGetBasicStamp, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    mStampCommenReceive.setText("?????????");
                                    mStampCommenReceive.setClickable(false);
                                    mStampCommenReceive.setSelected(true);
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    //??????????????????
                                    getStampPageInfo();
                                    break;
                                case 3001:
                                    new RealnameDialog(getApplicationContext(), "??????????????????????????????~");
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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

    private void viplUserGetBasicStamp() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ViplUserGetBasicStamp, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmentStamp", "onSuccess: 11"+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    mStampViprealnameReceive.setText("?????????");
                                    mStampViprealnameReceive.setClickable(false);
                                    mStampViprealnameReceive.setSelected(true);
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    //??????????????????
                                    getStampPageInfo();
                                    break;
                                case 3001:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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

    private void StampByMouDou(String channel) {
        JSONObject stampObj = new JSONObject();
        try {
            stampObj.put("uid", MyApp.uid);
            stampObj.put("num", stampNum);
            stampObj.put("channel", channel);
            stampObj.put("beans", stampNum * 20);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ExchangeTask().execute(new ExchangeRequest(stampObj.toString()));
    }


    class ExchangeTask extends AsyncTask<ExchangeRequest, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(ExchangeRequest... pr) {

            ExchangeRequest exchangeRequest = pr[0];
            String data = null;
            String json = exchangeRequest.rechareStr;
            try {
                //???Your Ping++ Server SDK????????????
                data = postJson(HttpUrl.StampBaans, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * ??????????????????charge?????????ping++ sdk???
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                return;
            }
            try {
                JSONObject obj = new JSONObject(data);
                switch (obj.getInt("retcode")) {
                    case 3000:
                    case 3001:
                    case 3002:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 2000:
                        getmywallet();
                        getMyPresent();
                        getStampPageInfo();
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
//                        mStampEtCount.setText("");
//                        mStampEtCount.clearFocus();
//                        stampGridviewAdapter.setSelectIndex(-1);
//                        stampGridviewAdapter.notifyDataSetChanged();
//                        stampNum = 0;
                        break;
                    case 50001:
                    case 50002:
                        EventBus.getDefault().post(new TokenFailureEvent());
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private static String postJson(String url, String json) throws IOException {
//        Log.i("lvzhiweipingpp", "postJson: " + json);
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic() + url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class ExchangeRequest {
        String rechareStr;

        public ExchangeRequest(String rechareStr) {
            this.rechareStr = rechareStr;
        }
    }

    private void getStampPageInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetStampPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentStamp", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StampData data = new Gson().fromJson(response, StampData.class);
                            StampData.DataBean datas = data.getData();
//                            SpannableStringBuilder builder = new SpannableStringBuilder("????????? " + datas.getWallet_stamp() + " ?????????????????????????????????");
                            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#DB57F3"));
                            ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#ffffff"));
                            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(16, true);//???????????????????????????

//                            builder.setSpan(purSpan, 3, datas.getWallet_stamp().length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            mStampCount.setText(builder);
                            mStampCount.setText(datas.getWallet_stamp());
                            SpannableStringBuilder builder1 = new SpannableStringBuilder("" + (datas.getBasicstampX() + datas.getBasicstampY() + datas.getBasicstampZ()) + " ???");
                            builder1.setSpan(purSpan2, 0, String.valueOf(datas.getBasicstampX() + datas.getBasicstampY() + datas.getBasicstampZ()).length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            builder1.setSpan(new AbsoluteSizeSpan(30, true), 0, String.valueOf(datas.getBasicstampX() + datas.getBasicstampY() + datas.getBasicstampZ()).length() + 0, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); //???????????????????????????
                            mStampGiveStamp.setText(builder1);
                            SpannableStringBuilder builder2 = new SpannableStringBuilder( datas.getBasicstampX() + " ???");
                            builder2.setSpan(purSpan, 0, (datas.getBasicstampX() + "").length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            builder2.setSpan(sizeSpan, 0, (datas.getBasicstampX() + "").length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mStampManYpCount.setText(builder2);
                            SpannableStringBuilder builder3 = new SpannableStringBuilder(datas.getBasicstampY() + " ???");
                            builder3.setSpan(purSpan, 0, (datas.getBasicstampY() + "").length() +0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            builder3.setSpan(sizeSpan, 0, (datas.getBasicstampY() + "").length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            mStampWomanYpCount.setText(builder3);
                            SpannableStringBuilder builder4 = new SpannableStringBuilder( datas.getBasicstampZ() + " ???");
                            builder4.setSpan(purSpan, 0, (datas.getBasicstampZ() + "").length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            builder4.setSpan(sizeSpan, 0, (datas.getBasicstampZ() + "").length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mStampCdtsYpCount.setText(builder4);
                            if (datas.getLauddynamic() >= 20) {
                                mStampLaudCount.setText("20/20");
                                mStampLaudGo.setText("?????????");
                                mStampLaudGo.setClickable(false);
                                mStampLaudGo.setSelected(true);
                            } else {
                                mStampLaudCount.setText(datas.getLauddynamic() + "/20");
                                mStampLaudGo.setText("?????????");
                                mStampLaudGo.setClickable(true);
                                mStampLaudGo.setSelected(false);
                            }
                            if (datas.getShareapp() >= 1) {
                                mStampShareCount.setText("1/1");
                                mStampShareGo.setText("?????????");
                                mStampShareGo.setClickable(false);
                                mStampShareGo.setSelected(true);
                            } else {
                                mStampShareCount.setText(datas.getShareapp() + "/1");
                                mStampShareGo.setText("?????????");
                                mStampShareGo.setClickable(true);
                                mStampShareGo.setSelected(false);
                            }
                            if (datas.getRewarddynamic() >= 1) {
                                mStampGiveMouDouCount.setText("1/1");
                                mStampGiveMouDouGo.setText("?????????");
                                mStampGiveMouDouGo.setClickable(false);
                                mStampGiveMouDouGo.setSelected(true);
                            } else {
                                mStampGiveMouDouCount.setText(datas.getRewarddynamic() + "/1");
                                mStampGiveMouDouGo.setText("?????????");
                                mStampGiveMouDouGo.setClickable(true);
                                mStampGiveMouDouGo.setSelected(false);
                            }
                            if (!datas.getMobile().equals("")) {
                                mStampBingPhoneGo.setText("?????????");
                                mStampBingPhoneGo.setClickable(false);
                                mStampBingPhoneGo.setSelected(true);
                            } else {
                                mStampBingPhoneGo.setText("?????????");
                                mStampBingPhoneGo.setClickable(true);
                                mStampBingPhoneGo.setSelected(false);
                            }
                            if (datas.getCommentappstate().equals("0")) {
                                mStampCommentAppGo.setText("?????????");
                                mStampCommentAppGo.setClickable(true);
                                mStampCommentAppGo.setSelected(false);
                            } else {
                                mStampCommentAppGo.setText("?????????");
                                mStampCommentAppGo.setClickable(false);
                                mStampCommentAppGo.setSelected(true);
                            }
                            if (datas.getNarmaluser().equals("0")) {
                                mStampCommenReceive.setText("??????");
                                mStampCommenReceive.setClickable(true);
                                mStampCommenReceive.setSelected(false);
                            } else {
                                mStampCommenReceive.setText("?????????");
                                mStampCommenReceive.setClickable(false);
                                mStampCommenReceive.setSelected(true);
                            }

                            if (datas.getVipuser().equals("0")) {
                                mStampViprealnameReceive.setText("??????");
                                mStampViprealnameReceive.setClickable(true);
                                mStampViprealnameReceive.setSelected(false);
                            } else {
                                mStampViprealnameReceive.setText("?????????");
                                mStampViprealnameReceive.setClickable(false);
                                mStampViprealnameReceive.setSelected(true);
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

    private void showShareWay() {
        sharedPop = new SharedPop(this, HttpUrl.SMaddress, "?????????????????????", getResources().getString(R.string.share_content), HttpUrl.NetPic() + "Uploads/logo.png", 1, 3, "", "", "", "");
        sharedPop.showAtLocation(activityStamp, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //?????????Popupwindow????????????????????????
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //??????Popupwindow??????????????????Popupwindow?????????????????????1f
        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getWindow().getAttributes();
                params[0].alpha = 1f;
                getWindow().setAttributes(params[0]);
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


    // @Subscribe(threadMode = ThreadMode.MAIN) //???ui????????????
    public void helloEventBus(int type) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(MyApp.QQAPP_ID, getApplicationContext());
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

    class MyIUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.i("qqshared", "onComplete: " + o.toString());
            try {
                JSONObject object = new JSONObject(o.toString());
                if (object.getInt("ret") == 0) {
                    //????????????
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
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "?????????????????????");// ??????
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, getResources().getString(R.string.share_content));// ??????
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, HttpUrl.SMaddress);// ????????????
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, HttpUrl.NetPic() + "Uploads/logo.png");// ?????????????????????
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "??????");// ????????????
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "??????????????????");
        // ????????????????????????????????????
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(StampActivity.this, params, new MyIUiListener());
            }
        });
    }

    public void shareToQZone() {
        params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "?????????????????????");// ??????
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, getResources().getString(R.string.share_content));// ??????
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, HttpUrl.SMaddress);// ????????????
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "??????");// ????????????
        ArrayList<String> imgUrlList = new ArrayList<>();
//        imgUrlList.add("http://59.110.28.150:888/Uploads/Picture/2017-04-18/20170418155251622.jpg");
        imgUrlList.add(HttpUrl.NetPic() + "Uploads/logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);// ????????????
        // ????????????????????????????????????
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone(StampActivity.this, params, new MyIUiListener());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //??????????????????
        getStampPageInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // ???????????????????????????, ?????????????????????, ??????????????????!
        Tencent.onActivityResultData(requestCode, resultCode, data, new MyIUiListener());
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, new MyIUiListener());
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(BuyVipSucces event) {
        getmywallet();
        getMyPresent();
        getStampPageInfo();
    }

    void showBeansMenu() {
        String beansStr = "?????????";
        if (!TextUtil.isEmpty(czmodou)) {
            beansStr += "(" + czmodou + ")";
        }
        AlertView alertView = new AlertView(null, null, "??????", null,
                new String[]{beansStr},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
               StampByMouDou("0");
            }
        });
        alertView.show();
    }

    void showPayMenu() {
        AlertView alertView = new AlertView(null, null, "??????", null,
                new String[]{"??????", "?????????"},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                if (data.equals("?????????")) {
                    //getOrderByWeChat();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", MyApp.uid);
                        object.put("num", stampNum);
                        new AliPayMentTaskManager(StampActivity.this, HttpUrl.ALIPAYstampcharge, object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                   // getOrderByAli();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", MyApp.uid);
                        object.put("num", stampNum);
                        object.put("appName", "shengmo");
                        new WxPayMentTaskManager(StampActivity.this, HttpUrl.WXCstampcharge, object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertView.show();
    }



}
