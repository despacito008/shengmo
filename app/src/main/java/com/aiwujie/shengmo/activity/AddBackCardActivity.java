package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.AddBankCardEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.sina.weibo.sdk.openapi.models.Status;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBackCardActivity extends AppCompatActivity {

    @BindView(R.id.mAddBackCard_return)
    ImageView mAddBackCardReturn;
    @BindView(R.id.mAddBackCard_name)
    EditText mAddBackCardName;
    @BindView(R.id.mAddBackCard_card)
    EditText mAddBackCardCard;
    @BindView(R.id.mAddBackCard_bank)
    TextView mAddBackCardBank;
    @BindView(R.id.mAddBackCard_confirm)
    Button mAddBackCardConfirm;
    Handler handler = new Handler();
    @BindView(R.id.mAddBackCard_title_name)
    TextView mAddBackCardTitleName;
    private String bankname;
    private String operation;
    private String bid;

    private String payStatus = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_back_card);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(AddBackCardActivity.this);
        Intent intent = getIntent();
        operation = intent.getStringExtra("operation");
        if (operation.equals("add")) {
            mAddBackCardTitleName.setText("添加银行卡");
        } else {
            mAddBackCardTitleName.setText("修改银行卡");
            mAddBackCardName.setText(intent.getStringExtra("realname"));
            mAddBackCardBank.setText(intent.getStringExtra("bankname"));
            mAddBackCardCard.setText(intent.getStringExtra("bankcard"));
            mAddBackCardName.setSelection(intent.getStringExtra("realname").length());
            mAddBackCardCard.setSelection(intent.getStringExtra("bankcard").length());
            bid = intent.getStringExtra("bid");
        }
        mAddBackCardCard.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mAddBackCard_return, R.id.mAddBackCard_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mAddBackCard_return:
                finish();
                break;
            case R.id.mAddBackCard_confirm:
                if (operation.equals("add")) {
                    addBankCard();
                } else {
                    editBankCard();
                }
                break;
        }
    }

    private void editBankCard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("bid", bid);
        map.put("bankcard", mAddBackCardCard.getText().toString().trim());
        map.put("bankname", mAddBackCardBank.getText().toString().trim());
        map.put("realname", mAddBackCardName.getText().toString().trim());
        map.put("pay_status",payStatus);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Addbankcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), "修改成功");
                                    EventBus.getDefault().post(new AddBankCardEvent());
                                    finish();
                                    break;
                                case 4001:
                                case 4003:
                                case 4088:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4006:
                                    ToastUtil.show(getApplicationContext(), "修改失败");
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

    private void addBankCard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("bankcard", mAddBackCardCard.getText().toString().trim());
        map.put("bankname", mAddBackCardBank.getText().toString().trim());
        map.put("realname", mAddBackCardName.getText().toString().trim());
        map.put("pay_status",payStatus);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Addbankcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), "添加成功");
                                    EventBus.getDefault().post(new AddBankCardEvent());
                                    finish();
                                    break;
                                case 4001:
                                case 4003:
                                case 4088:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4006:
                                    ToastUtil.show(getApplicationContext(), "添加失败");
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

    private void getBanknameByBanknum() {
        Map<String, String> map = new HashMap<>();
        map.put("bankcard", mAddBackCardCard.getText().toString().trim());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBanknameByBanknum, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("addbackcard", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            bankname = obj.getString("data");
                            mAddBackCardBank.setText(bankname);
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

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (temp.length() <= 21) {
                mAddBackCardBank.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() >= 16) {
                getBanknameByBanknum();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }

}
