package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RedbagAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.base.BaseActivity;
import com.aiwujie.shengmo.bean.RedbagshouBean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedBaoshouActivity extends AppCompatActivity {
    List<RedbagshouBean.DataBean> list = new ArrayList<>();
    private String orderid;
    private ListView shouredbaolist;
    Handler handler = new Handler();
    private TextView sssdredbag;
    private TextView tv_asd;
    int show=0;
    private TextView tvHeadName,tvHeadInfo,tvHeadDesc;
    private ImageView ivHeadBack,ivHeadIcon,ivHeadOver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_baoshou);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        show = intent.getIntExtra("show", 0);
        sendredbao();
        ImmersionBar.with(this).transparentBar().init();
        //X_SystemBarUI.initSystemBar(this,R.color.redBaoOrange);
        shouredbaolist = (ListView) findViewById(R.id.shouredbaolist);
//        View view = View.inflate(this,R.layout.redbao_head,null);
//        shouredbaolist.addHeaderView(view);
//        sssdredbag = view.findViewById(R.id.sssdredbag);
//        tv_asd = view.findViewById(R.id.tv_asd);
        View view = View.inflate(this,R.layout.layout_red_bao_header,null);
        shouredbaolist.addHeaderView(view);
        ivHeadBack = view.findViewById(R.id.iv_red_bao_header_back);
        ivHeadIcon = view.findViewById(R.id.iv_red_bao_header_icon);
        tvHeadName = view.findViewById(R.id.tv_red_bao_header_name);
        tvHeadInfo = view.findViewById(R.id.tv_red_bao_header_info);
        tvHeadDesc = view.findViewById(R.id.tv_red_bao_header_desc);
        ivHeadOver = view.findViewById(R.id.iv_red_bao_header_over);
        tvHeadName.setText(getIntent().getStringExtra("name")+"的红包");
        Glide.with(RedBaoshouActivity.this).load(getIntent().getStringExtra("url")).transform(new CircleCrop()).into(ivHeadIcon);
        tvHeadInfo.setText(getIntent().getStringExtra("info"));
        ivHeadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }


    public void sendredbao(){
        Map<String, String> map = new HashMap<>();
        map.put("orderid", orderid);
        map.put("show", show+"");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.takeRedbag, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                LogUtil.d(response);
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                        case 2001:
                            Gson gson = new Gson();
                            final RedbagshouBean redbagshouBean = gson.fromJson(response, RedbagshouBean.class);
                            if (redbagshouBean.getData().getGettime().equals("0")){

                            }else {
                                list.add(redbagshouBean.getData());
                            }
                            signRedEnvelopes(orderid);
                            //sssdredbag.setText(redbagshouBean.getData().getU_nickname()+"的红包");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvHeadDesc.setText("1个红包共"+redbagshouBean.getData().getBeans()+"魔豆 已领取");
                                    if (redbagshouBean.getData().getGettime().equals("0")){
                                        tvHeadDesc.setText("1个红包共"+redbagshouBean.getData().getBeans()+"魔豆 待领取");
                                    }
                                    shouredbaolist.setAdapter(new RedbagAdapter(RedBaoshouActivity.this,list));
                                }
                            });
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

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

     void signRedEnvelopes(String orderId) {
        try {
            List<String> redEnvelopesCacheList = SharedPreferencesUtils.getDataList("red_envelopes_" + MyApp.uid);
            if (redEnvelopesCacheList == null) {
                redEnvelopesCacheList = new ArrayList<>();
            }
            redEnvelopesCacheList.add(orderId);
            SharedPreferencesUtils.addDataList("red_envelopes_" + MyApp.uid, redEnvelopesCacheList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
