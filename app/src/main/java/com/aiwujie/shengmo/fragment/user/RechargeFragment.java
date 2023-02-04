package com.aiwujie.shengmo.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RechargeRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.StampGoodsBean;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.AliPayMentTaskManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WxPayMentTaskManager;
import com.aiwujie.shengmo.view.KeyBoardListenerHelper;
import com.bigkoo.alertview.AlertView;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 充值页面
 */
public class RechargeFragment extends Fragment {
    @BindView(R.id.tv_fragment_recharge_balance)
    TextView tvFragmentRechargeBalance;
    @BindView(R.id.rv_fragment_recharge)
    RecyclerView rvFragmentRecharge;
    @BindView(R.id.tv_fragment_recharge_buy_money)
    TextView tvFragmentRechargeBuyMoney;
    @BindView(R.id.tv_fragment_recharge_buy_custom)
    TextView tvFragmentRechargeBuyCustom;
    @BindView(R.id.et_fragment_recharge_buy_custom)
    TextView etFragmentRechargeBuyCustom;
    @BindView(R.id.ll_custom_pay)
    LinearLayout llCustomPay;
    Unbinder unbinder;
    private String modou;
    private String czsubject = "";
    private List<StampGoodsBean.DataBean> beansGoodsList;
    private InputMethodManager imm;
    private KeyBoardListenerHelper keyBoardListenerHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_recharge, container, false);
        unbinder = ButterKnife.bind(this, view);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyBoardListenerHelper = new KeyBoardListenerHelper(getActivity());
        getRechargeGoods();
        setListener();
        getMyWallet();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (keyBoardListenerHelper != null) {
            keyBoardListenerHelper.destroy();
        }
    }

    void setListener() {
        tvFragmentRechargeBuyMoney.setOnClickListener(v -> showPayMenu());

        //tvFragmentRechargeBuyCustom.setOnClickListener(v -> showPayMenu2());

        llCustomPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCustomPay();
            }
        });

        etFragmentRechargeBuyCustom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                chooseCustomPay();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tvFragmentRechargeBuyCustom.setText(Integer.parseInt(s.toString()) * 10 + "金魔豆");
                } else {
                    tvFragmentRechargeBuyCustom.setText("0金魔豆");
                }
            }
        });

        keyBoardListenerHelper.setOnKeyBoardChangeListener(new KeyBoardListenerHelper.OnKeyBoardChangeListener() {
            @Override
            public void OnKeyBoardChange(boolean isShow, int keyBoardHeight) {
                if (isShow) {
                    chooseCustomPay();
                }
            }
        });
    }
    RechargeRecyclerAdapter rechargeRecyclerAdapter;
    private void getRechargeGoods() {
        HttpHelper.getInstance().getBeansList(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                StampGoodsBean stampGoodsBean = GsonUtil.GsonToBean(data, StampGoodsBean.class);
                if(stampGoodsBean != null) {
                    beansGoodsList = stampGoodsBean.getData();
                    rechargeRecyclerAdapter = new RechargeRecyclerAdapter(getActivity(), beansGoodsList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    rvFragmentRecharge.setLayoutManager(gridLayoutManager);
                    rvFragmentRecharge.setAdapter(rechargeRecyclerAdapter);
                    rechargeRecyclerAdapter.setOnSimpleItemListener(
                            position -> {
                                unChooseCustomPay();
                                czsubject = beansGoodsList.get(position).getSubject();
                            }
                    );
                    czsubject = beansGoodsList.get(0).getSubject();
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });

    }

    private void getMyWallet() {
        HttpHelper.getInstance().getMyWallet(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                WalletData walletData = GsonUtil.GsonToBean(data, WalletData.class);
                if (walletData != null && walletData.getData() != null) {
                    modou = walletData.getData().getWallet();
                    tvFragmentRechargeBalance.setText(modou);
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    void showPayMenu() {
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{"微信", "支付宝"},
                getActivity(), AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener((o, position, data) -> {
            FloatWindowLayout.getInstance().clearAnchorFloat();
            if ("支付宝".equals(data)) {
                payByAli();
            } else {
                payByWeChat();
            }
        });
        alertView.show();
    }

    void showPayMenu2() {

        if (etFragmentRechargeBuyCustom.getText().length() <= 2) {
            ToastUtil.show("自定义充值 最低需充值100魔豆");
            return;
        }

        if (Integer.parseInt(etFragmentRechargeBuyCustom.getText().toString()) > 1000000) {
            ToastUtil.show("自定义充值 最高充值1000000魔豆");
            return;
        }

        /*隐藏软键盘*/
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(etFragmentRechargeBuyCustom.getApplicationWindowToken(), 0);
        }

        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{"微信", "支付宝"},
                getActivity(), AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener((o, position, data) -> {
            FloatWindowLayout.getInstance().clearAnchorFloat();
            if ("支付宝".equals(data)) {
                payByAli2();
            } else {
                payByWeChat2();
            }
        });
        alertView.show();
    }

    public void payByAli() {
        try {
            JSONObject object = new JSONObject();
            object.put("uid",MyApp.uid);
            object.put("subject",czsubject);
            if (czsubject.equals("0")) {
                object.put("amount",String.valueOf(amount));
                if (amount < 100) {
                    ToastUtil.show("自定义充值最低10元起");
                    return;
                }
                if (amount > 100000) {
                    ToastUtil.show("自定义充值一次最高充值100000元");
                    return;
                }
            }
            LogUtil.d(czsubject);
            new AliPayMentTaskManager(getActivity(), HttpUrl.ALIPAYCzcharge,object.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    int amount = 0;
    public void payByAli2() {
        try {
            JSONObject object = new JSONObject();
            object.put("uid",MyApp.uid);
            object.put("subject",0);
            object.put("amount",String.valueOf(amount));
            LogUtil.d(czsubject);
            new AliPayMentTaskManager(getActivity(), HttpUrl.ALIPAYCzcharge,object.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void payByWeChat() {
        try {
            JSONObject object = new JSONObject();
            object.put("uid",MyApp.uid);
            object.put("subject",czsubject);
            object.put("appName", "shengmo");
            if (czsubject.equals("0")) {
                object.put("amount",String.valueOf(amount));
                if (amount < 100) {
                    ToastUtil.show("自定义充值最低10元起");
                    return;
                }
                if (amount > 100000) {
                    ToastUtil.show("自定义充值一次最高充值100000元");
                    return;
                }
            }
            new WxPayMentTaskManager(getActivity(),HttpUrl.WXCzcharge,object.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void payByWeChat2() {
        try {
            JSONObject object = new JSONObject();
            object.put("uid",MyApp.uid);
            object.put("subject",0);
            object.put("appName", "shengmo");
            object.put("amount",String.valueOf(amount));
            new WxPayMentTaskManager(getActivity(),HttpUrl.WXCzcharge,object.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void unChooseCustomPay() {
        if (czsubject.equals("0")) {
            llCustomPay.setBackgroundResource(R.drawable.bg_vip_center_item_normal);
            /*隐藏软键盘*/
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(etFragmentRechargeBuyCustom.getApplicationWindowToken(), 0);
            }
        }
    }

    public void chooseCustomPay() {
        if (!czsubject.equals("0")) {
            llCustomPay.setBackgroundResource(R.drawable.bg_vip_center_item_choose);
            czsubject = "0";
            if (rechargeRecyclerAdapter != null) {
                rechargeRecyclerAdapter.changeChooseItem(-1);
            }
        }
    }
}
