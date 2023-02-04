package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.takwolf.android.lock9.Lock9View;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Lock9ViewActivity extends AppCompatActivity implements Lock9View.CallBack {
    @BindView(R.id.lock_9_view_tv)
    TextView lock9ViewTv;
    @BindView(R.id.lock_9_view_forget)
    TextView lock9ViewForget;
    private int inPutCount = 1;
    private String gustureFlag;
    private int errorCount = 0;
    private boolean isQuit;
    private Timer timer=new Timer();
    String onePass;
    String twoPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock9_view);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        Lock9View lock9View = (Lock9View) findViewById(R.id.lock_9_view);
        gustureFlag = getIntent().getStringExtra("gustureFlag");
        if(gustureFlag.equals("input")){
            lock9ViewForget.setVisibility(View.VISIBLE);
        }
        lock9View.setCallBack(this);
    }

    @OnClick(R.id.lock_9_view_forget)
    public void onClick() {
        jumpLoginActivity();
    }

    private void jumpLoginActivity() {
        Intent intent = new Intent(Lock9ViewActivity.this, LoginActivity.class);
        SharedPreferencesUtils.setParam(getApplicationContext(), "uid", "");
        SharedPreferencesUtils.setParam(getApplicationContext(), "modle", "");
        SharedPreferencesUtils.setParam(getApplicationContext(), "token", "");
        SharedPreferencesUtils.setParam(getApplicationContext(), "gusturePsw","");
        SharedPreferencesUtils.setParam(getApplicationContext(), "gusture", "0");
        //RongIM.getInstance().logout();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(gustureFlag.equals("input")){
                if (isQuit==false){
                    isQuit=true;
                    ToastUtil.show(getApplicationContext(),"再按一次退出程序");
                    TimerTask task=null;
                    task=new TimerTask() {
                        @Override
                        public void run() {
                            isQuit=false;
                        }
                    };
                    timer.schedule(task,2000);
                }else {
                    //RongIM.getInstance().disconnect();
                    finish();
                    System.exit(0);
//                    Process.killProcess(Process.myPid());
                }
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFinish(String password) {
        if(gustureFlag.equals("set")&&password.length()<4){
            onePass = "";
            twoPass = "";
            lock9ViewTv.setText("请至少连接4个点,请重新绘制");
            lock9ViewTv.setTextColor(Color.parseColor("#ff1111"));
            return;
        }
        if (gustureFlag.equals("set")) {
            if (inPutCount == 1) {
                onePass = password;
                lock9ViewTv.setText("请再次绘制手势密码");
                lock9ViewTv.setTextColor(Color.parseColor("#888888"));
                inPutCount = 2;
            } else {
                twoPass = password;
                if (onePass.equals(twoPass)) {
                    SharedPreferencesUtils.setParam(getApplicationContext(), "gusturePsw", twoPass);
                    EventBus.getDefault().post("setGustureSuccess");
                    ToastUtil.show(getApplicationContext(), "设置成功");
                    finish();
                } else {
                    onePass = "";
                    twoPass = "";
                    lock9ViewTv.setText("请绘制手势锁密码");
                    lock9ViewTv.setTextColor(Color.parseColor("#888888"));
                    ToastUtil.show(getApplicationContext(), "两次绘制不同,请重新绘制");
                    inPutCount = 1;
                }
            }
        } else {
            String inputPass = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "gusturePsw", "");
            if (inputPass.equals(password)) {
                Intent intent = new Intent(Lock9ViewActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (errorCount < 4) {
                    errorCount++;
                    lock9ViewTv.setText("绘制错误" + errorCount + "次");
                    lock9ViewTv.setTextColor(Color.parseColor("#ff1111"));
                } else {
                    jumpLoginActivity();
                }
            }
        }
    }
}
