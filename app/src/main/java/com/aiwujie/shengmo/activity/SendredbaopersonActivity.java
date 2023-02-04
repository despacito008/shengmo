package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class SendredbaopersonActivity extends AppCompatActivity {


    private EditText mzong_doudou;
    private ImageView mbackya;
    private Button msend_red;
    private EditText met_message;
    private String content="";
    private String targetid;
    private String modou;
    String orderid="";
    private String wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendredbaoperson);
        //X_SystemBarUI.initSystemBar(this, R.color.redBaoOrange);
        ImmersionBar.with(this).transparentBar().init();
        Intent intent = getIntent();
        getmywallet();
        targetid = intent.getStringExtra("targetid");
        mbackya =  findViewById(R.id.backya);
        met_message = (EditText) findViewById(R.id.et_message);
        mzong_doudou = (EditText) findViewById(R.id.zong_doudou);
        msend_red = (Button) findViewById(R.id.send_red);
        mzong_doudou.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        msend_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean fastClick = isFastClick();
                if (!fastClick){
                    return;
               }

                content = met_message.getText().toString().trim();
                if(content.equals("")||content==null){
                    content="";
                }
                modou = mzong_doudou.getText().toString().trim();
                if(modou.equals("")||modou==null||modou.equals("0")){
                    return;
                }

                Long banlance = Long.valueOf(wallet);
                Long cost = Long.valueOf(modou);

                if(cost > banlance){
                    ToastUtil.show(SendredbaopersonActivity.this,"魔豆余额不足");
                    return;
                }
                if(cost < 10){
                    ToastUtil.show(SendredbaopersonActivity.this,"单个红包不低于10魔豆");
                    return;
                }

                Date date = new Date();
                long l = date.getTime();
                orderid = MyApp.uid+targetid+l;
                sendredbao();
            }
        });
        mbackya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


    }


    public void sendredbao(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", targetid);
        map.put("num", modou);
        map.put("content", content);
        map.put("orderid", orderid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.giveRedbag, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            //ToastUtil.show(SendredbaopersonActivity.this, object.getString("msg"));
                            sendPhone(SendredbaopersonActivity.this.content, targetid);
                            break;
                        case 3000:
                        case 3001:
                        case 4000:
                        case 4001:
                        case 4002:
                        case 4003:
                        case 4004:
                        case 4005:
                        case 4006:
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

    private void getmywallet() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getmywallet, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            WalletData data = new Gson().fromJson(response, WalletData.class);
                            wallet = data.getData().getWallet();

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void sendPhone(String message1,String chatUserid){

//        final Message_Red_bao info =new Message_Red_bao();
//        info.setMessage(message1);
//        info.setOrderid(orderid);
//        info.setIsopen("0");
        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.PRIVATE,info);
//        message.setExtra("0");
//        RongIM.getInstance().sendMessage(message, info.toString(), null, new IRongCallback.ISendMessageCallback() {
//            @Override //表示消息添加到本地数据库
//            public void onAttached(Message message) {
//
//            }
//            @Override//消息发送成功
//            public void onSuccess(Message message) {
//                hideInput();
//                finish();
//            }
//            @Override //消息发送失败
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });


        MessageSendHelper.getInstance().sendTipMessage("你发送了一个红包","你收到一个红包");
        MessageSendHelper.getInstance().sendRedEnvelopsMessage(orderid, message1, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });

    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 两次点击间隔不能少于500ms
     */
    private static final int MIN_DELAY_TIME = 1000;
    private static long lastClickTime=0;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) <= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
