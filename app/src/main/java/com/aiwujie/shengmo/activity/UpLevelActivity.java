package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class UpLevelActivity extends AppCompatActivity {

    @BindView(R.id.mUpLevel_return)
    ImageView mUpLevelReturn;
    @BindView(R.id.mUpLevel_name)
    TextView mUpLevelName;
    @BindView(R.id.mUpLevel_msg)
    TextView mUpLevelMsg;
    @BindView(R.id.mUpLevel_renzheng)
    TextView mUpLevelRenzheng;
    @BindView(R.id.mUpLevel_icon)
    ImageView mUpLevelIcon;
    private String groupIcon;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_level);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        EventBus.getDefault().register(this);
        groupIcon = getIntent().getStringExtra("groupIcon");
//        if(groupIcon.equals("http://59.110.28.150:888/")){
            if(groupIcon.equals(NetPic())){
            mUpLevelIcon.setImageResource(R.mipmap.qunmorentouxiang);
        }else {
            GlideImgManager.glideLoader(this, groupIcon, R.mipmap.qunmorentouxiang, R.mipmap.qunmorentouxiang, mUpLevelIcon, 0);
        }
        getIdstate();
    }

    @OnClick({R.id.mUpLevel_return, R.id.mUpLevel_renzheng})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mUpLevel_return:
                finish();
                break;
            case R.id.mUpLevel_renzheng:
                Intent intent = new Intent(this, PhotoRzActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void getIdstate() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getidstate, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int retcode = obj.getInt("retcode");
                            switch (retcode) {
                                case 2000:
                                    mUpLevelRenzheng.setText("已认证");
                                    mUpLevelRenzheng.setBackgroundResource(R.drawable.item_uplevel_conner_hui_bg);
                                    break;
                                case 2001:
                                    mUpLevelRenzheng.setText("认证中");
                                    mUpLevelRenzheng.setBackgroundResource(R.drawable.item_uplevel_conner_bg);
                                    break;
                                case 2002:
                                    mUpLevelRenzheng.setText("去认证");
                                    mUpLevelRenzheng.setBackgroundResource(R.drawable.item_uplevel_conner_bg);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message){
       if(message.equals("uploadSuccess")){
           getIdstate();
       }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
