package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.kt.util.NormalConstant;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupIntroActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_group_intro);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent=getIntent();
        groupid=intent.getStringExtra(IntentKey.GROUP_ID);
        //oldGroupIntro=intent.getStringExtra("groupintro");
        oldGroupIntro=intent.getStringExtra(IntentKey.INTRODUCE);
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
               // if(groupIntroduce.indexOf(minganText[0])==-1&&groupIntroduce.indexOf(minganText[1])==-1&&groupIntroduce.indexOf(minganText[2])==-1&&groupIntroduce.indexOf(minganText[3])==-1&&groupIntroduce.indexOf(minganText[4])==-1&&groupIntroduce.indexOf(minganText[5])==-1&&groupIntroduce.indexOf(minganText[6])==-1&&groupIntroduce.indexOf(minganText[7])==-1&&groupIntroduce.indexOf(minganText[8])==-1&&groupIntroduce.indexOf(minganText[9])==-1&&groupIntroduce.indexOf(minganText[10])==-1) {
                    if(!mGroupIntroEdittext.getText().toString().equals(oldGroupIntro)) {
                        if (temp.length()>500) {
                            ToastUtil.show(getApplicationContext(),"字数已经达到了限制！");
                            return;
                        }
                        //editGroupIntroByTim(groupIntroduce);
                        editGroupIntro();
                    }else{
                        Intent intent=new Intent();
                        intent.putExtra("groupintro",mGroupIntroEdittext.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                    }
//                }else{
//                    ToastUtil.show(getApplicationContext(), "很抱歉，您的群介绍包含有禁止使用的字，请重试~");
//                }
                break;
        }
    }

    private void editGroupIntro() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid",groupid);
        map.put("introduce",mGroupIntroEdittext.getText().toString());
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditGroupInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                //Log.i("groupintroissucc", "onSuccess: "+response);
                try {
                    JSONObject obj=new JSONObject(response);
                    switch (obj.getInt("retcode")){
                        case 2000:
                        case 4003:
                            Intent intent=new Intent();
                            intent.putExtra(IntentKey.INTRODUCE,mGroupIntroEdittext.getText().toString());
                            setResult(NormalConstant.RESULT_CODE_OK,intent);
                            finish();
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

    private void editGroupIntroByTim(final String intro) {
        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(groupid), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                for (V2TIMGroupInfoResult v2TIMGroupInfoResult : v2TIMGroupInfoResults) {
                    if (v2TIMGroupInfoResult.getGroupInfo().getGroupID().equals(groupid)) {
                        V2TIMGroupInfo groupInfo = v2TIMGroupInfoResult.getGroupInfo();
                        groupInfo.setIntroduction(intro);
                        V2TIMManager.getGroupManager().setGroupInfo(groupInfo, new V2TIMCallback() {
                            @Override
                            public void onSuccess() {
                                ToastUtil.show(GroupIntroActivity.this,"修改群介绍成功");
                                Intent intent=new Intent();
                                intent.putExtra("groupintro",mGroupIntroEdittext.getText().toString());
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                            @Override
                            public void onError(int code, String desc) {
                                ToastUtil.show(GroupIntroActivity.this,"修改失败 ：" + code + desc);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {

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
