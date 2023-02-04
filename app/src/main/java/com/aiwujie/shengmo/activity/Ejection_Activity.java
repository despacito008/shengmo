package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.EjectionGridviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.EjectionaData;
import com.aiwujie.shengmo.bean.EjectionshengyuBean;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AliPayMentTaskManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WxPayMentTaskManager;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Ejection_Activity extends AppCompatActivity implements OnItemClickListener,AdapterView.OnItemClickListener,View.OnClickListener,View.OnFocusChangeListener {

    private EjectionGridviewAdapter ejectionGridviewAdapter;
    List<EjectionaData> ejectionaDataList = new ArrayList<>();
    private MyGridview myGridview;
    Handler handler = new Handler();
    private TextView mStamp_count;
    private ImageView mStamp_return;
    private TextView mStamp_zhangdan;
    private int stampNum = 0;
    private InputMethodManager mInputMethodManager;
    //EditText mStampEtCount;
    //private TextView mStamp_buy;
    //private TextView duoshao;
    private String czmodou = "0";
    private String lwmodou = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejection_);
        getMyPresent();
        getmywallet();

        //mStamp_buy = (TextView) findViewById(R.id.mStamp_buy);
        //mStampEtCount = (EditText) findViewById(R.id.mStamp_etCount);
        //duoshao = (TextView) findViewById(R.id.duoshao);
        mStamp_zhangdan = (TextView) findViewById(R.id.mStamp_zhangdan);
        mStamp_count = (TextView) findViewById(R.id.mStamp_count);
        mStamp_return = (ImageView) findViewById(R.id.mStamp_return);
        mStamp_return.setOnClickListener(this);
        mStamp_zhangdan.setOnClickListener(this);
        //mStamp_buy.setOnClickListener(this);

      /*  mStampEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = mStampEtCount.getText().toString();
                if(!s1.equals("") && s1!=null){
                    Integer integer = Integer.valueOf(s1);
                    duoshao.setText("￥"+integer*10+"/"+integer*150+"魔豆");
                }else {
                    duoshao.setText("￥0/0魔豆");
                }

            }
        });*/

        setdata();
        myGridview = (MyGridview) findViewById(R.id.mStamp_gridview);
        ejectionGridviewAdapter = new EjectionGridviewAdapter(this,ejectionaDataList);
        myGridview.setAdapter(ejectionGridviewAdapter);
        myGridview.setOnItemClickListener(this);
        //初始化输入法
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //mStampEtCount.setOnFocusChangeListener(this);
        getusertopcardinfo();


    }

    private void setdata() {
        ejectionaDataList.add(new EjectionaData("1","1张","￥30/300魔豆","",""));
        ejectionaDataList.add(new EjectionaData("3","3张(9.5折)","￥86/860魔豆","原￥90/900魔豆","（仅￥28.6/张）"));
        ejectionaDataList.add(new EjectionaData("10","10张(9折)","￥270/2700魔豆","原￥300/3000魔豆","（仅￥27/张）"));
        ejectionaDataList.add(new EjectionaData("30","30张(8.5折)","￥765/7650魔豆","原￥900/9000魔豆","（仅￥25.5/张）"));
        ejectionaDataList.add(new EjectionaData("90","90张(8折)","￥2160/21600魔豆","原￥2700/27000魔豆","（仅￥24/张）"));
        ejectionaDataList.add(new EjectionaData("270","270张(7.5折)","￥6075/60750魔豆","原￥8100/81000魔豆","（仅￥22.5/张）"));

    }
    //推顶卡余额
    public void getusertopcardinfo(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("----", "onSuccess: "+response );
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        EjectionshengyuBean ejectionBean = gson.fromJson(response, EjectionshengyuBean.class);
                        SpannableStringBuilder builder = new SpannableStringBuilder("共剩余 " + ejectionBean.getData().getWallet_topcard() + " 张推顶卡");
                        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                        builder.setSpan(purSpan, 3, ejectionBean.getData().getWallet_topcard().length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mStamp_count.setText(builder);
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ejectionGridviewAdapter.setSelectIndex(position);
        ejectionGridviewAdapter.notifyDataSetChanged();
      /*  if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(mStampEtCount.getWindowToken(), 0);// 隐藏输入键盘
        }
        mStampEtCount.clearFocus();
        mStampEtCount.setText("");*/
        stampNum = Integer.parseInt(ejectionaDataList.get(position).getNum());
        new AlertView(null, null, "取消", null,
                new String[]{"金魔豆("+czmodou+")", "支付宝", "微信"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onItemClick(Object o, int position,String data) {

        switch (position) {
            case 0:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* new AlertView(null, null, "取消", null,
                                new String[]{"充值魔豆（"+czmodou+" 魔豆）", "礼物魔豆（"+lwmodou+" 魔豆）"},
                                Ejection_Activity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                String channel;
                                switch (position) {
                                    case 0:
                                        channel = "0";
                                        //通过充值的魔豆购买邮票
                                        StampByMouDou(channel);
                                        break;
                                    case 1:
                                        //通过礼物的魔豆购买邮票
                                        channel = "1";
                                        StampByMouDou(channel);
                                        break;
                                }
                            }
                        }).show();*/
                        //通过充值的魔豆购买邮票
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
                    new AliPayMentTaskManager(this, HttpUrl.ALIPAYtopcardcharge, object.toString());
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
                    object.put("appName", "圣魔");
                    new WxPayMentTaskManager(this, HttpUrl.WXCtopcardcharge, object.toString());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mStamp_return:
                finish();
                break;
            case R.id.mStamp_zhangdan:
            startActivity(new Intent(this,Ejection_mingxi_Activity.class));
                break;
           /* case R.id.mStamp_buy:
                if (!TextUtil.isEmpty(mStampEtCount.getText().toString())) {
                    if (Integer.parseInt(mStampEtCount.getText().toString()) != 0) {
                        stampNum = Integer.parseInt(mStampEtCount.getText().toString());
                        new AlertView(null, null, "取消", null,
                                new String[]{"金魔豆("+czmodou+")", "支付宝", "微信"},
                                this, AlertView.Style.ActionSheet, this).show();
                    } else {
                        ToastUtil.show(getApplicationContext(), "请输入购买数量...");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入购买数量...");
                }
                break;*/
        }
    }

    private void StampByMouDou(String channel) {
        JSONObject stampObj = new JSONObject();
        try {
            stampObj.put("uid", MyApp.uid);
            stampObj.put("num", stampNum);
            stampObj.put("channel", channel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ExchangeTask().execute(new ExchangeRequest(stampObj.toString()));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    class ExchangeRequest {
        String rechareStr;

        public ExchangeRequest(String rechareStr) {
            this.rechareStr = rechareStr;
        }
    }

    private static String postJson(String url, String json) throws IOException {
//        Log.i("lvzhiweipingpp", "postJson: " + json);
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic()+url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(),"url_token","")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
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
                //向Your Ping++ Server SDK请求数据
                data = postJson(HttpUrl.topcard_baans, json);
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
                    case 3001:
                    case 3002:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 2000:
                        getmywallet();
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                       // mStampEtCount.setText("");
                        //mStampEtCount.clearFocus();
                        ejectionGridviewAdapter.setSelectIndex(-1);
                        ejectionGridviewAdapter.notifyDataSetChanged();
                        stampNum = 0;
                        break;

                    case 50001:
                    case 50002:
                        EventBus.getDefault().post(new TokenFailureEvent());
                        break;
                }
                getusertopcardinfo();
                getmywallet();
                getMyPresent();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void shuaxin(BuyVipSucces buyVipSucces){
        getusertopcardinfo();
    }
}
