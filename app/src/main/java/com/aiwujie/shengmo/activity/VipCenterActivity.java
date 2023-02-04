package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.bean.VipTimeData;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.customview.SlideCustomViewpager;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.eventbus.VipEvent;
import com.aiwujie.shengmo.fragment.vipcenterfragment.FragmentSvip;
import com.aiwujie.shengmo.fragment.vipcenterfragment.FragmentVip;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AliPayMentTaskManager;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WxPayMentTaskManager;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

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

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class VipCenterActivity extends AppCompatActivity implements OnItemClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.mVipCenter_return)
    ImageView mVipCenterReturn;
    @BindView(R.id.mVipCenter_date)
    TextView mVipCenterDate;
    @BindView(R.id.mVipCenter_svip_date)
    TextView mVipCenterSvipDate;
    @BindView(R.id.mVipCenter_xieyi)
    TextView mVipCenterXieyi;
    @BindView(R.id.mVipCenter_icon)
    ImageView mVipCenterIcon;
    @BindView(R.id.mVipCenter_header_my)
    PercentLinearLayout mVipCenterHeaderMy;
    @BindView(R.id.mVipCenter_icon_my)
    ImageView mVipCenterIconMy;
    @BindView(R.id.mVipCenter_icon_other)
    ImageView mVipCenterIconOther;
    @BindView(R.id.mVipCenter_header_sendVip)
    PercentLinearLayout mVipCenterHeaderSendVip;
    @BindView(R.id.mVipCenter_tabs)
    TabLayout mVipCenterTabs;
    @BindView(R.id.mVipCenter_viewpager)
    SlideCustomViewpager mVipCenterViewpager;
    @BindView(R.id.mVipCenter_btn_vip_buy)
    Button mVipCenterBtnVipBuy;
    @BindView(R.id.mVipCenter_bottom_tvMoney)
    TextView mVipCenterBottomTvMoney;
    @BindView(R.id.mMypurse_zhangdan)
    TextView mMypurse_zhangdan;
    @BindView(R.id.mVipCenter_bottom_tvMoDou)
    TextView mVipCenterBottomTvMoDou;
    @BindView(R.id.rl_up_lb)
    PercentLinearLayout rl_up_lb;
    @BindView(R.id.iv_up_lb)
    ImageView iv_up_lb;
    Handler handler = new Handler();
    private int buyamountVip = 30;
    private int buyamountSvip = 128;
    private int vipType = 1;
    private int buysubjectVip = 1;
    private int buysubjectSvip = 1;
    private String buyorder_id;
    private String user_id;
    private int ownerOrOther;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private List<Integer> tabIcons = new ArrayList<>();//页卡Icon集合
    private String CHANNEL;
    private String czmodou = "0";
    private String lwmodou = "0";
    String dalaba="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_center);
        ButterKnife.bind(this);
        gotoNewVipActivity();
//        X_SystemBarUI.initSystemBar(this, R.color.title_color);
//        EventBus.getDefault().register(this);
//        setData();
//        getMyPresent();
//        getmywallet();
    }

    void gotoNewVipActivity() {
        Intent intent = new Intent(VipCenterActivity.this, VipMemberCenterActivity.class);
        intent.putExtra("uid",getIntent().getStringExtra("uid"));
        intent.putExtra("headpic",getIntent().getStringExtra("headpic"));
        startActivity(intent);
        finish();
    }

    private void setData() {

        mMypurse_zhangdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(VipCenterActivity.this, Vip_BillActivity.class);
                startActivity(intent);
            }
        });
        SpannableStringBuilder builder = new SpannableStringBuilder(mVipCenterXieyi.getText().toString());
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
        builder.setSpan(purSpan, 13, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mVipCenterXieyi.setText(builder);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("uid");
        if (user_id.equals(MyApp.uid)) {
            mVipCenterHeaderMy.setVisibility(View.VISIBLE);
            if (intent.getStringExtra("headpic").equals("") || intent.getStringExtra("headpic").equals(NetPic())) {//"http://59.110.28.150:888/"
                mVipCenterIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(this, intent.getStringExtra("headpic"), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mVipCenterIcon, 0);
            }
            //获取vip时间
            getVipOverTime();
        } else {
            mVipCenterHeaderSendVip.setVisibility(View.VISIBLE);
            GlideImgManager.glideLoader(this, (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", ""), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mVipCenterIconMy, 0);
            if (intent.getStringExtra("headpic").equals("") || intent.getStringExtra("headpic").equals(NetPic())) {//"http://59.110.28.150:888/"
                mVipCenterIconOther.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(this, intent.getStringExtra("headpic"), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mVipCenterIconOther, 0);
            }
        }
        //添加页卡标题
        mTitleList.add("VIP会员");
        mTitleList.add("SVIP会员");
        //添加页卡标题Icon
        tabIcons.add(R.drawable.user_vip);
        tabIcons.add(R.drawable.user_svip);
        //添加页卡视图
        mViewList.add(new FragmentVip());
        mViewList.add(new FragmentSvip());
        mVipCenterTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
//        mVipCenterTabs.addTab(mVipCenterTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
//        mVipCenterTabs.addTab(mVipCenterTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mVipCenterViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mVipCenterTabs.setupWithViewPager(mVipCenterViewpager);//将TabLayout和ViewPager关联起来。
        mVipCenterTabs.getTabAt(0).setCustomView(getTabView(0));
        mVipCenterTabs.getTabAt(1).setCustomView(getTabView(1));
//        mVipCenterTabs.getTabAt(0).setIcon(tabIcons.get(0)).setText(mTitleList.get(0));
//        mVipCenterTabs.getTabAt(1).setIcon(tabIcons.get(1)).setText(mTitleList.get(1));
        //修改下划线的长度
        mVipCenterTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mVipCenterTabs, 40, 40);
            }
        });
        mVipCenterViewpager.addOnPageChangeListener(this);
        mVipCenterViewpager.setCurrentItem(1);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitleList.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.item_custom_tab_icon);
        img_title.setImageResource(tabIcons.get(position));
       /* TextView tv = (TextView) view.findViewById(R.id.item_custom_tab_new);
        if (position == 0) {
            tv.setVisibility(View.GONE);
        }*/
        return view;
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
                                //可使用的魔豆数量
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


    private void getVipOverTime() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetVipOverTimeNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("testsavevip", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            VipTimeData data = new Gson().fromJson(response, VipTimeData.class);
                            VipTimeData.DataBean datas = data.getData();
                            if (datas.getVip().equals("0") && datas.getSvip().equals("0")) {
                                mVipCenterDate.setVisibility(View.VISIBLE);
                                mVipCenterSvipDate.setVisibility(View.GONE);
                                mVipCenterDate.setText("点亮身份标识");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "0");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "svip", "0");
                            } else if (datas.getVip().equals("1") && datas.getSvip().equals("0")) {
                                mVipCenterDate.setVisibility(View.VISIBLE);
                                mVipCenterSvipDate.setVisibility(View.GONE);
                                mVipCenterDate.setText("VIP到期时间：" + datas.getVipovertime());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "1");
                            } else if (datas.getSvip().equals("1")) {
                                mVipCenterSvipDate.setVisibility(View.VISIBLE);
                                mVipCenterDate.setVisibility(View.VISIBLE);
                                mVipCenterDate.setText("VIP到期时间：" + datas.getVipovertime());
                                mVipCenterSvipDate.setText("SVIP到期时间：" + datas.getSvipovertime());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "1");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "svip", "1");
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

    @OnClick({R.id.mVipCenter_btn_vip_buy, R.id.mVipCenter_return, R.id.mVipCenter_xieyi,R.id.rl_up_lb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mVipCenter_return:
                finish();
                break;
            case R.id.mVipCenter_xieyi:
                Intent intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "圣魔会员协议");
                intent.putExtra("path",HttpUrl.NetPic()+ HttpUrl.Vipregagreement);
                startActivity(intent);
                break;
            case R.id.mVipCenter_btn_vip_buy:
                //选择什么方式购买
                selectChannel();
                break;
            case R.id.rl_up_lb:
                //上不上大喇叭
                if (dalaba.equals("1")){
                    iv_up_lb.setImageResource(R.mipmap.yuandiantaohui);
                    dalaba="2";
                }else {
                    iv_up_lb.setImageResource(R.mipmap.yuandiantaozi);
                    dalaba="1";
                }

                break;
        }
    }

    private void selectChannel() {
        //ownerOrOthrer为1时是自己充值，2是给他人充值。3是选择购买vip使用的魔豆类型
        ownerOrOther = 0;
        new AlertView(null, null, "取消", null,
                new String[]{"金魔豆("+czmodou+")", "支付宝", "微信"},
                this, AlertView.Style.ActionSheet, this).show();
    }


    @Override
    public void onItemClick(Object o, int position,String data) {
        if (ownerOrOther == 0) {
            switch (position) {
                case 0:
                    ownerOrOther = 1;
                    if (vipType == 1) {
                        //beanstype = "0";
                        //通过充值的魔豆购买会员
                        VipByMouDou("0");
                    } else {
                        //通过充值的魔豆购买超级会员
                        //channel = "1";
                        SvipByMouDou("1");
                    }
                    break;
                case 1:
                    CHANNEL = "alipay";
                    buyVipOrSvip(CHANNEL);
                    break;
                case 2:
                    CHANNEL = "wx";
                    buyVipOrSvip(CHANNEL);
                    break;
            }
        } else if (ownerOrOther == 1) {
            String beanstype;
            String channel;
            switch (position) {
                case 0:

                    break;
                case 1:
                    //通过礼物的魔豆购买会员
                    if (vipType == 1) {
                        beanstype = "1";
                        VipByMouDou(beanstype);
                    } else {
                        //通过礼物的魔豆购买超级会员
                        channel = "2";
                        SvipByMouDou(channel);
                    }
                    break;
            }
        }
    }

    private void buyVipOrSvip(String channel) {
        try {
            JSONObject object = new JSONObject();
            if (vipType == 1) {
                if (user_id.equals(MyApp.uid)) {
                    object.put("uid", MyApp.uid);
                    object.put("subject", buysubjectVip);
                } else {
                    object.put("uid", user_id);
                    object.put("subject", buysubjectVip);
                    object.put("guid", MyApp.uid);
                    object.put("dalaba", dalaba);
                }
                if (channel.equals("alipay")) {
                    new AliPayMentTaskManager(this, HttpUrl.ALIPAYvipcharge, object.toString());
                } else {
                    object.put("appName", "shengmo");
                    new WxPayMentTaskManager(this, HttpUrl.WXvipcharge, object.toString());
                }
            } else {
                if (user_id.equals(MyApp.uid)) {
                    object.put("uid", MyApp.uid);
                    object.put("subject", buysubjectSvip);
                } else {
                    object.put("uid", user_id);
                    object.put("subject", buysubjectSvip);
                    object.put("guid", MyApp.uid);
                    object.put("dalaba", dalaba);

                }
                if (channel.equals("alipay")) {
                    new AliPayMentTaskManager(this, HttpUrl.ALIPAYsvipcharge, object.toString());
                } else {
                    object.put("appName", "shengmo");
                    new WxPayMentTaskManager(this, HttpUrl.WXsvipcharge, object.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void SvipByMouDou(String channel) {
        final JSONObject svipObj = new JSONObject();
        try {
            svipObj.put("vuid", user_id);
            svipObj.put("login_uid", MyApp.uid);
            svipObj.put("channel", channel);
            svipObj.put("subject", buysubjectSvip);
            svipObj.put("dalaba", dalaba);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ModouSvipTask().execute(new MoudouSvipRequest(svipObj.toString()));
    }

    private void VipByMouDou(String beanstype) {
        JSONObject vipObj = new JSONObject();
        try {
            vipObj.put("uid", user_id);
            vipObj.put("viptype", buysubjectVip);
            vipObj.put("beanstype", beanstype);
            vipObj.put("login_uid", MyApp.uid);
            vipObj.put("dalaba", dalaba);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ModouVipTask().execute(new MoudouVipRequest(vipObj.toString()));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(final VipEvent event) {
        vipType = event.getType();
        switch (vipType) {
            case 1:
                buyamountVip = event.getBuyamount();
                buysubjectVip = event.getBuysubject();
                mVipCenterBottomTvMoney.setText(buyamountVip + "");
                switch (buysubjectVip) {
                    case 1:
                        mVipCenterBottomTvMoDou.setText("300");
                        break;
                    case 2:
                        mVipCenterBottomTvMoDou.setText("880");
                        break;
                    case 3:
                        mVipCenterBottomTvMoDou.setText("1680");
                        break;
                    case 4:
                        mVipCenterBottomTvMoDou.setText("2980");
                        break;
                }
                break;
            case 2:
                buyamountSvip = event.getBuyamount();
                mVipCenterBottomTvMoney.setText(buyamountSvip + "");
                buysubjectSvip = event.getBuysubject();
                switch (buysubjectSvip) {
                    case 1:
                        mVipCenterBottomTvMoDou.setText("1280");
                        break;
                    case 2:
                        mVipCenterBottomTvMoDou.setText("3480");
                        break;
                    case 3:
                        mVipCenterBottomTvMoDou.setText("8980");
                        break;
                    case 4:
                        mVipCenterBottomTvMoDou.setText("12980");
                        break;
                }
                break;
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rl_up_lb.setVisibility(View.INVISIBLE);
                vipType = 1;
                switch (buysubjectVip) {
                    case 1:
                        mVipCenterBottomTvMoney.setText("30");
                        mVipCenterBottomTvMoDou.setText("300");
                        break;
                    case 2:
                        mVipCenterBottomTvMoney.setText("88");
                        mVipCenterBottomTvMoDou.setText("880");
                        break;
                    case 3:
                        mVipCenterBottomTvMoney.setText("168");
                        mVipCenterBottomTvMoDou.setText("1680");
                        break;
                    case 4:
                        mVipCenterBottomTvMoney.setText("298");
                        mVipCenterBottomTvMoDou.setText("2980");
                        break;
                }
                break;
            case 1:
                rl_up_lb.setVisibility(View.VISIBLE);
                vipType = 2;
                switch (buysubjectSvip) {
                    case 1:
                        mVipCenterBottomTvMoney.setText("128");
                        mVipCenterBottomTvMoDou.setText("1280");
                        break;
                    case 2:
                        mVipCenterBottomTvMoney.setText("348");
                        mVipCenterBottomTvMoDou.setText("3480");
                        break;
                    case 3:
                        mVipCenterBottomTvMoney.setText("898");
                        mVipCenterBottomTvMoDou.setText("8980");
                        break;
                    case 4:
                        mVipCenterBottomTvMoney.setText("1298");
                        mVipCenterBottomTvMoDou.setText("12980");
                        break;
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ModouVipTask extends AsyncTask<MoudouVipRequest, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(MoudouVipRequest... pr) {

            MoudouVipRequest moudouVipRequest = pr[0];
            String data = null;
            String json = moudouVipRequest.vipStr;
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(HttpUrl.VipBeans, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                return;
            }
            try {
                JSONObject obj = new JSONObject(data);
                switch (obj.getInt("retcode")) {
                    case 4000:
                    case 4001:
                    case 4002:
                    case 4003:
                    case 4004:
                    case 4005:
                    case 4006:
                    case 5000:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 2000:
                        if (user_id.equals(MyApp.uid)) {
                            getVipOverTime();
                        }
                        getmywallet();
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class ModouSvipTask extends AsyncTask<MoudouSvipRequest, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(MoudouSvipRequest... pr) {

            MoudouSvipRequest moudouSvipRequest = pr[0];
            String data = null;
            String json = moudouSvipRequest.svipStr;
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(HttpUrl.SvipBeans, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
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
                    case 4001:
                    case 4002:
                    case 4003:
                    case 4004:
                    case 5000:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 2000:
                        if (user_id.equals(MyApp.uid)) {
                            getVipOverTime();
                        }
                        getmywallet();
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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


    private String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic()+url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(),"url_token","")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    class MoudouVipRequest {
        String vipStr;

        public MoudouVipRequest(String vipStr) {
            this.vipStr = vipStr;
        }
    }

    class MoudouSvipRequest {
        String svipStr;

        public MoudouSvipRequest(String svipStr) {
            this.svipStr = svipStr;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(BuyVipSucces event) {
        getVipOverTime();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
