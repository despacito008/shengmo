package com.aiwujie.shengmo.fragment.mypursefragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.Ejection_Activity;
import com.aiwujie.shengmo.activity.StampActivity;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.adapter.CzGridviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.RechargeData;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.customview.PayWayPopupWindow;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AliPayMentTaskManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WxPayMentTaskManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
 * Created by 290243232 on 2017/6/23.
 */

public class FragmentRecharge extends Fragment implements AdapterView.OnItemClickListener, PayWayPopupWindow.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.mMypurse_Moudou)
    TextView mMypurseMoudou;
    @BindView(R.id.mChongzhi_gridview)
    MyGridview mChongzhiGridview;
    @BindView(R.id.mChongzhi_layout)
    PercentLinearLayout mChongzhiLayout;
    @BindView(R.id.mChongzhi_exchangVip)
    ImageView mChongzhiExchangVip;
    @BindView(R.id.mChongzhi_exchangYp)
    ImageView mChongzhiExchangYp;
    @BindView(R.id.mChongzhi_tuidingka)
    ImageView mChongzhiExchangtuidig;
    private CzGridviewAdapter adapter;
    private int czsubject = 0;
    private int czamount = 0;
    private PayWayPopupWindow mPop;
    private String modou;
    private RechargeData czdata;
    private List<RechargeData> czdatas = new ArrayList<>();
    private Handler handler = new Handler();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_recharge, null);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        czdata = new RechargeData();
        czdata.setMoney("10");
        czdata.setModou("100");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("30");
        czdata.setModou("300");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("50");
        czdata.setModou("500");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("100");
        czdata.setModou("1000");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("300");
        czdata.setModou("3000");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("500");
        czdata.setModou("5000");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("1000");
        czdata.setModou("10000");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("5000");
        czdata.setModou("50000");
        czdatas.add(czdata);
        czdata = new RechargeData();
        czdata.setMoney("10000");
        czdata.setModou("100000");
        czdatas.add(czdata);
        adapter = new CzGridviewAdapter(getActivity(), czdatas);
        mChongzhiGridview.setAdapter(adapter);
        mChongzhiGridview.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectIndex(position);
        adapter.notifyDataSetChanged();
        czamount = Integer.parseInt(czdatas.get(position).getMoney());
        czsubject = position + 7;
        mPop = new PayWayPopupWindow(getActivity());
        setBackgroundAlpha(0.5f);
        mPop.setOnDismissListener(this);
        mPop.setOnItemClickListener(this);
        //设置PopupWindow中的位置
        mPop.showAtLocation(getActivity().findViewById(R.id.mChongzhi_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * PopUpWindow消失监听
     */
    @Override
    public void onDismiss() {
        // popupWindow隐藏时恢复屏幕正常透明度
        setBackgroundAlpha(1.0f);
    }

    /**
     * popupwindow的监听
     *
     * @param v
     */
    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()) {
            case R.id.item_payway_zfb:
                if (czamount != 0 || czsubject != 0) {
                    try {
                        JSONObject object=new JSONObject();
                        object.put("uid",MyApp.uid);
                        object.put("subject",czsubject);
                        new AliPayMentTaskManager(getActivity(), HttpUrl.ALIPAYCzcharge,object.toString());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.show(getActivity().getApplicationContext(), "请选择充值金额");
                }
                break;
            case R.id.item_payway_wx:
                if (czamount != 0 || czsubject != 0) {
                    try {
                        JSONObject object=new JSONObject();
                        object.put("uid",MyApp.uid);
                        object.put("subject",czsubject);
                        object.put("appName", "shengmo");
                        new WxPayMentTaskManager(getActivity(),HttpUrl.WXCzcharge,object.toString());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.show(getActivity().getApplicationContext(), "请选择充值金额");
                }
                break;
        }
        mPop.dismiss();

    }


    @OnClick({R.id.mChongzhi_exchangVip, R.id.mChongzhi_exchangYp,R.id.mChongzhi_tuidingka})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChongzhi_exchangVip:
                Intent intent = new Intent(getActivity(), VipCenterActivity.class);
                intent.putExtra("headpic", (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "headurl", ""));
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mChongzhi_exchangYp:
                intent = new Intent(getActivity(), StampActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.mChongzhi_tuidingka:
                intent = new Intent(getActivity(), Ejection_Activity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */


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
                            modou = data.getData().getWallet();
                            SpannableStringBuilder builder = new SpannableStringBuilder("余额 " + modou + " 金魔豆");
                            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                            builder.setSpan(purSpan, 3, data.getData().getWallet().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mMypurseMoudou.setText(builder);
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


    @Override
    public void onResume() {
        super.onResume();
        getmywallet();
    }

}
