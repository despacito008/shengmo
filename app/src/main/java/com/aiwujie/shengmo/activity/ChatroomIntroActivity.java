package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatroomIntroActivity extends AppCompatActivity {

    @BindView(R.id.mGroupIntro_return)
    ImageView mGroupIntroReturn;
    @BindView(R.id.mGroupIntro_Tijiao)
    TextView mGroupIntroTijiao;
    @BindView(R.id.mGroupIntro_edittext)
    EditText mGroupIntroEdittext;
    @BindView(R.id.mGroupIntro_count)
    TextView mGroupIntroCount;
    @BindView(R.id.activity_group_intro)
    PercentLinearLayout activityGroupIntro;
    private String groupid;
    private String oldGroupIntro;
    private String[] minganText ={"奴"};
    private CharSequence temp;//监听前的文本
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom_intro);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent=getIntent();
        groupid=intent.getStringExtra("chatRoomId");
        oldGroupIntro=intent.getStringExtra("chatroomcount");
        mGroupIntroEdittext.setText(oldGroupIntro);
        mGroupIntroEdittext.addTextChangedListener(new EditTitleChangedListener());
        mGroupIntroEdittext.setSelection(mGroupIntroEdittext.getText().length());
    }

    @OnClick({R.id.mGroupIntro_return, R.id.mGroupIntro_Tijiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mGroupIntro_return:
                finish();
                break;
            case R.id.mGroupIntro_Tijiao:
                String groupIntroduce=mGroupIntroEdittext.getText().toString();
                if(groupIntroduce.indexOf(minganText[0])==-1&&groupIntroduce.indexOf(minganText[1])==-1&&groupIntroduce.indexOf(minganText[2])==-1&&groupIntroduce.indexOf(minganText[3])==-1&&groupIntroduce.indexOf(minganText[4])==-1&&groupIntroduce.indexOf(minganText[5])==-1&&groupIntroduce.indexOf(minganText[6])==-1&&groupIntroduce.indexOf(minganText[7])==-1&&groupIntroduce.indexOf(minganText[8])==-1&&groupIntroduce.indexOf(minganText[9])==-1&&groupIntroduce.indexOf(minganText[10])==-1) {
                    if(!mGroupIntroEdittext.getText().toString().equals(oldGroupIntro)) {
                        if (temp.length()>500) {
                            ToastUtil.show(getApplicationContext(),"字数已经达到了限制！");
                            return;
                        }
                        editGroupIntro();
                    }else{
                        Intent intent=new Intent();
                        intent.putExtra("groupintro",mGroupIntroEdittext.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }else{
                    ToastUtil.show(getApplicationContext(), "很抱歉，您的群介绍包含有禁止使用的字，请重试~");
                }
                break;
        }
    }

    private void editGroupIntro() {
        Map<String,String> map=new HashMap<>();
        map.put("roomid",groupid);
        map.put("count",mGroupIntroEdittext.getText().toString());
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.editChatinfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("groupintroissucc", "onSuccess: "+response);
                try {
                    JSONObject obj=new JSONObject(response);
                    switch (obj.getInt("retcode")){
                        case 2000:
                        case 4003:
                            Intent intent=new Intent();
                            intent.putExtra("groupintro",mGroupIntroEdittext.getText().toString());
                            setResult(RESULT_OK,intent);
                            EventBus.getDefault().post("liaotianshixinxishuaxin");
                            finish();
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

    class EditTitleChangedListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mGroupIntroCount.setText("("+s.length()+"/500)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length()>500) {
                ToastUtil.show(getApplicationContext(),"字数已经达到了限制！");
            }
        }
    };
}
