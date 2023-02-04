package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class SendredbaoChatroomActivity extends AppCompatActivity {


    private EditText mzong_doudou;
    private EditText mnum_doudou;
    private TextView mbackya;
    private Button msend_red;
    private EditText met_message;
    private String message="";
    String orderid="";
    private String num;
    private String zongmodou;
    private String targetid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        targetid = intent.getStringExtra("targetid");
        setContentView(R.layout.activity_sendredbao);
        mbackya = (TextView) findViewById(R.id.backya);
        met_message = (EditText) findViewById(R.id.et_message);
        mzong_doudou = (EditText) findViewById(R.id.zong_doudou);
        mnum_doudou = (EditText) findViewById(R.id.num_doudou);
        msend_red = (Button) findViewById(R.id.send_red);
        mzong_doudou.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mnum_doudou.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        msend_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean fastClick = isFastClick();
                if (!fastClick){
                    return;
                }

                message = met_message.getText().toString().trim();
                num = mnum_doudou.getText().toString().trim();
                zongmodou = mzong_doudou.getText().toString().trim();

                if (null == zongmodou || num==null){
                    return;
                }

                if (Integer.valueOf(zongmodou)/10<Integer.valueOf(num)){
                    return;
                }

                if(message.equals("")|| message ==null){
                   message ="";
                }

                Date date = new Date();
                long l = date.getTime();
                orderid = MyApp.uid+ targetid +l;
                sendredbao();
            }
        });
        mbackya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mnum_doudou.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = mnum_doudou.getText().toString().trim();
                if (trim.equals("")||trim==null){
                    mzong_doudou.setHint("10金魔豆起发");
                    return;
                }
                Integer integer = Integer.valueOf(trim);
                mzong_doudou.setHint(integer*10+"金魔豆起发");
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
        map.put("num", zongmodou);
        map.put("nums", num);
        map.put("orderid", orderid);
        map.put("groupid", targetid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.qunGiveRedbag, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            //ToastUtil.show(SendredbaopersonActivity.this, object.getString("msg"));
                            sendPhone(message, targetid);
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


    public void sendPhone(String message1,String chatUserid){

//        final Message_Red_bao info =new Message_Red_bao();
//        info.setMessage(message1);
//        info.setOrderid(orderid);
//        info.setIsopen("0");
        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.CHATROOM,info);
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
