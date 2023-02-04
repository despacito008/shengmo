package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoAlbumActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @BindView(R.id.mPhotoAlbum_return)
    ImageView mPhotoAlbumReturn;
    @BindView(R.id.mPhotoAlbum_bg)
    ImageView mPhotoAlbumBg;
    @BindView(R.id.mPhotoAlbum_isOpen)
    CheckBox mPhotoAlbumIsOpen;
    @BindView(R.id.jiabujiami)
    TextView jiabujiami;
    @BindView(R.id.mPhotoAlbum_resetPsw)
    TextView mPhotoAlbumResetPsw;
    private String photo_lock;
    Handler handler = new Handler();
    private AlertDialog alertDialog;
    private EditText etPsw;
    private boolean isSet = true;
    private AlertDialog resetAlertDialog;
    private EditText etnewPsw;
//    private EditText etrenewPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        ButterKnife.bind(this);
        mPhotoAlbumIsOpen.setEnabled(false);
        mPhotoAlbumResetPsw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mPhotoAlbumIsOpen.setOnCheckedChangeListener(this);
        Intent intent = getIntent();
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        photo_lock = intent.getStringExtra("photo_lock");
        if (photo_lock.equals("1")) {
            mPhotoAlbumIsOpen.setChecked(false);
            mPhotoAlbumBg.setImageResource(R.mipmap.simiweisuo);
            jiabujiami.setText("未加密");
        } else {
            mPhotoAlbumIsOpen.setChecked(true);
            mPhotoAlbumBg.setImageResource(R.mipmap.simisuo);
            jiabujiami.setText("加密");
        }
        judgePhotoPwd();
    }

    private void judgePhotoPwd() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.JudgePhotoPwd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 3000:
                                    isSet = true;
                                    break;
                                case 3001:
                                    isSet = false;
                                    passwordDialog();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mPhotoAlbumIsOpen.setEnabled(true);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void passwordDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_photo_setpsw_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        etPsw = (EditText) window.findViewById(R.id.item_photo_setpsw_edittext);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_photo_setpsw_confim);
        TextView tvCancel = (TextView) window.findViewById(R.id.item_photo_setpsw_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPsw.getText().toString().equals("")) {
                    ToastUtil.show(getApplicationContext(), "密码不能为空");
                } else {
                    addPhotoPwd();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isSet = false;
                photo_lock = "1";
            }
        });
    }
    private void resetpassDialog() {
        resetAlertDialog = new AlertDialog.Builder(this).create();
        resetAlertDialog.show();
        Window window = resetAlertDialog.getWindow();
        window.setContentView(R.layout.item_photo_resetpsw_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        etnewPsw = (EditText) window.findViewById(R.id.item_photo_resetpsw_newPass);
//        etrenewPsw = (EditText) window.findViewById(R.id.item_photo_resetpsw_newRePass);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_photo_resetpsw_confim);
        TextView tvCancel = (TextView) window.findViewById(R.id.item_photo_resetpsw_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resetPhotoPwd();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAlertDialog.dismiss();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {
            if (isSet == false) {
                passwordDialog();
//                mPhotoAlbumIsOpen.setChecked(true);
//                photo_lock = "1";
//                jiabujiami.setText("未加密");
//                mPhotoAlbumBg.setImageResource(R.mipmap.simiweisuo);
            } else {
                photo_lock = "2";
                jiabujiami.setText("加密");
                mPhotoAlbumBg.setImageResource(R.mipmap.simisuo);
            }
        } else {
            photo_lock = "1";
            jiabujiami.setText("未加密");
            mPhotoAlbumBg.setImageResource(R.mipmap.simiweisuo);
        }
    }

    private void addPhotoPwd() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("newpwd", etPsw.getText().toString().trim());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.AddPhotoPwd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("addphotopsw", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    alertDialog.dismiss();
                                    isSet = true;
                                    photo_lock = "2";
                                    jiabujiami.setText("加密");
                                    mPhotoAlbumBg.setImageResource(R.mipmap.simisuo);
                                    break;
                                case 4001:
                                case 4002:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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

    private void setSecretSit() {
        if (mPhotoAlbumIsOpen.isChecked()) {
            photo_lock = "2";
        } else {
            photo_lock = "1";
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("photo_lock", photo_lock);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("secretactivity", "onSuccess: " + response);
                EventBus.getDefault().post("setSecretSitSucc");
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void resetPhotoPwd() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("newpwd",etnewPsw.getText().toString().trim());
        IRequestManager manager=RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditPhotoPwd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    resetAlertDialog.dismiss();
                                    break;
                                case 4001:
                                case 4002:
                                case 4003:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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

    @Override
    protected void onDestroy() {
        setSecretSit();
        super.onDestroy();
    }

    @OnClick({R.id.mPhotoAlbum_return, R.id.mPhotoAlbum_resetPsw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPhotoAlbum_return:
                finish();
                break;
            case R.id.mPhotoAlbum_resetPsw:
                resetpassDialog();
                break;
        }
    }

}
