package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ApplyLiveAuthData;
import com.aiwujie.shengmo.bean.LiveAnchorData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/6/5.
 */

public class ApplyForLiveActivity extends AppCompatActivity {

    @BindView(R.id.upload_photo)
    PercentLinearLayout uploadPhoto;
    @BindView(R.id.bind_phone)
    PercentLinearLayout bindPhone;
    @BindView(R.id.bind_card)
    PercentLinearLayout bindCard;
    @BindView(R.id.upload_idcard)
    PercentLinearLayout uploadIdcard;
    @BindView(R.id.photo_tv)
    TextView photoTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.bankcard_tv)
    TextView bankCardTv;
    @BindView(R.id.idcard_tv)
    TextView idCardTv;
    @BindView(R.id.apply_btn)
    Button applyBtn;

    private ApplyLiveAuthData data;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyforlive);
        ButterKnife.bind(this);
        init();
        getLiveAuth();
    }

    private void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLiveAuth();
    }

    @OnClick({R.id.upload_photo, R.id.bind_phone, R.id.bind_card, R.id.upload_idcard,R.id.apply_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_photo:
                startActivity(new Intent(this, PhotoRzActivity.class));
                break;
            case R.id.upload_idcard:
                startActivity(new Intent(this, PhotoRzActivity.class));
                break;
            case R.id.bind_phone:
                startActivity(new Intent(this, BindingMobileActivity.class));
                break;
            case R.id.bind_card:
                Intent intent = new Intent(this, AddBackCardActivity.class);
                intent.putExtra("operation","add");
                startActivity(intent);
                break;
            case R.id.apply_btn:

                break;
        }
    }

    private void getLiveAuth() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);

        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetLiveAuth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("follow_live", response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            data = new Gson().fromJson(response, ApplyLiveAuthData.class);
                            if (data.getRetcode() == 2000) {
                                ToastUtil.show(ApplyForLiveActivity.this, response);
                                //??????????????????
                                if (data.getData().getIdcardStatus().equals("2")) {
                                    photoTv.setText("?????????");
                                    uploadPhoto.setClickable(false);
                                } else if (data.getData().getIdcardStatus().equals("1")) {
                                    photoTv.setText("?????????");
                                    uploadPhoto.setClickable(false);
                                } else {
                                    photoTv.setText("????????????");
                                    uploadPhoto.setClickable(true);
                                }
                                //?????????????????????
                                if (data.getData().getBindingMobileStatus().equals("0")) {
                                    phoneTv.setText("????????????");
                                    bindPhone.setClickable(true);
                                } else if (data.getData().getIdcardStatus().equals("1")) {
                                    phoneTv.setText("?????????");
                                    bindPhone.setClickable(false);
                                }
                                //?????????????????????
                                if (data.getData().getBankCardStatus().equals("1")) {
                                    bankCardTv.setText("?????????");
                                    bindCard.setClickable(false);
                                } else {
                                    bankCardTv.setText("????????????");
                                    bindCard.setClickable(true);
                                }
                                //???????????????
                                if (data.getData().getRealidcardStatus().equals("2")) {
                                    idCardTv.setText("?????????");
                                    uploadIdcard.setClickable(false);
                                } else if (data.getData().getIdcardStatus().equals("1")) {
                                    idCardTv.setText("?????????");
                                    uploadIdcard.setClickable(false);
                                } else {
                                    idCardTv.setText("????????????");
                                    uploadIdcard.setClickable(true);
                                }

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
}
