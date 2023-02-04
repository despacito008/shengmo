package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.AddBankCardEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WithdrawalsActivity extends AppCompatActivity {

    @BindView(R.id.mWithdrawals_return)
    ImageView mWithdrawalsReturn;
    @BindView(R.id.mWithdrawals_ll_card)
    PercentLinearLayout mWithdrawalsLlCard;
    @BindView(R.id.mWithdrawals_et_money)
    EditText mWithdrawalsEtMoney;
    @BindView(R.id.mWithdrawals_confirm)
    Button mWithdrawalsConfirm;
    Handler handler=new Handler();
    private AlertDialog alertDialog;
    public static TextView mWithdrawalsCard;
    private int modou;
    private AlertDialog tixianAlertDialog;
    private double rate;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mWithdrawalsCard= (TextView) findViewById(R.id.mWithdrawals_card);
        EventBus.getDefault().register(this);
        Intent intent=getIntent();
        modou=Integer.parseInt(intent.getStringExtra("modou"));
        rate=intent.getDoubleExtra("rate",-1);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getbankcard();
    }

    private void getbankcard() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getbankcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    if(alertDialog!=null){
                                        alertDialog.dismiss();
                                    }
                                    break;
                                case 4002:
                                    //设置银行卡
                                    setBackCardDialog();
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

    private void setBackCardDialog() {
        alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_setcard_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_setcard_set);
        TextView tvCancel= (TextView) window.findViewById(R.id.item_setcard_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WithdrawalsActivity.this,SetBankcardActivity.class);
                startActivity(intent);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @OnClick({R.id.mWithdrawals_return, R.id.mWithdrawals_ll_card, R.id.mWithdrawals_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mWithdrawals_return:
                finish();
                break;
            case R.id.mWithdrawals_ll_card:
                Intent intent=new Intent(this,SetBankcardActivity.class);
                intent.putExtra("operation","add");
                startActivity(intent);
                break;
            case R.id.mWithdrawals_confirm:
                if(SetBankcardActivity.bid!=null) {
                    if(!mWithdrawalsEtMoney.getText().toString().equals("")) {
                        if (modou >= Integer.parseInt(mWithdrawalsEtMoney.getText().toString())) {
                            if (Integer.parseInt(mWithdrawalsEtMoney.getText().toString()) >= 2000) {
                                mWithdrawalsConfirm.setEnabled(false);
                                tixianConfirm();
                            } else {
                                ToastUtil.show(getApplicationContext(), "您的魔豆不满足提现要求...");
                            }
                        } else {
                            ToastUtil.show(getApplicationContext(), "您的可用魔豆余额不足...");
                        }
                    }else{
                        ToastUtil.show(getApplicationContext(),"请输入魔豆数量...");
                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"请选择提现的银行卡...");
                }
                break;
        }
    }

    private void tixianConfirm() {
        tixianAlertDialog = new android.app.AlertDialog.Builder(this).create();
        tixianAlertDialog.show();
        tixianAlertDialog.setCanceledOnTouchOutside(false);
        tixianAlertDialog.setCancelable(false);
        Window window = tixianAlertDialog.getWindow();
        window.setContentView(R.layout.item_tixian_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_tixian_confirm);
        TextView tvCancel= (TextView) window.findViewById(R.id.item_tixian_cancel);
        TextView tvMoney= (TextView) window.findViewById(R.id.item_tixian_money);
        TextView tvModou= (TextView) window.findViewById(R.id.item_tixian_modou);
        final double tixianMoney=Integer.parseInt(mWithdrawalsEtMoney.getText().toString())/10*rate;
        BigDecimal decimal = new BigDecimal(tixianMoney);
        final double tixianMoney01 = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        int shengDou=Integer.parseInt(mWithdrawalsEtMoney.getText().toString())%10;
        final int amount=Integer.parseInt(mWithdrawalsEtMoney.getText().toString())-(Integer.parseInt(mWithdrawalsEtMoney.getText().toString())%10);
        tvMoney.setText("提现金额:"+tixianMoney01+"元");
        tvModou.setText("剩余魔豆:"+shengDou+"个");
        final JSONObject withdrawalsObj=new JSONObject();
        try {
            withdrawalsObj.put("uid",MyApp.uid);
            withdrawalsObj.put("bid",SetBankcardActivity.bid);
            withdrawalsObj.put("money",amount+"");
            withdrawalsObj.put("amount",tixianMoney01+"");
        }catch (JSONException e){
            e.printStackTrace();
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WithdrawalsTask().execute(new WithDrawalsRequest(withdrawalsObj.toString()));
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWithdrawalsConfirm.setEnabled(true);
                tixianAlertDialog.dismiss();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void helloEventBus(AddBankCardEvent event){
        getbankcard();
    }


    class WithdrawalsTask extends AsyncTask<WithDrawalsRequest, Void, String> {

        @Override
        protected String doInBackground(WithDrawalsRequest... params) {
            WithDrawalsRequest withDrawalsRequest = params[0];
            String data = null;
            String json = withDrawalsRequest.withdrawlsObjectStr;
            try {
                data = postJson(HttpUrl.TixianNew, json);
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
                    case 2000:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        finish();
                        break;
                    case 4545:
                    case 7705:
                    case 4002:
                    case 4044:
                    case 4003:
                    case 4004:
                    case 6666:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 50001:
                    case 50002:
                        EventBus.getDefault().post(new TokenFailureEvent());
                        break;
                }
                tixianAlertDialog.dismiss();
                mWithdrawalsConfirm.setEnabled(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
        private  String postJson(String url, String json) throws IOException {
            MediaType type = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(type, json);
            Request request = new Request.Builder().url(HttpUrl.NetPic()+url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(),"url_token","")).post(body).build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        class WithDrawalsRequest {
            String withdrawlsObjectStr;
            public WithDrawalsRequest(String withdrawlsObjectStr) {
                this.withdrawlsObjectStr = withdrawlsObjectStr;
            }
        }


        @Override
        protected void onResume() {
            super.onResume();
            getbankcard();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
            SetBankcardActivity.bid=null;
        }
    }
