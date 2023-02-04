package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.FinishGroupInfoEvent;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.tim.bean.RefreshMessageBean;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.tencent.imsdk.v2.V2TIMGroupAtInfo.*;

public class SendMetionAllMsgActivity extends AppCompatActivity {

    @BindView(R.id.mSend_metion_return)
    ImageView mSendMetionReturn;
    @BindView(R.id.mSend_metion_send)
    TextView mSendMetionSend;
    @BindView(R.id.mSend_metion_edittext)
    EditText mSendMetionEdittext;
    private  Handler handler=new Handler();
    private String mTargetId;

    public static void start(Context context,String gid) {
        Intent intent = new Intent(context,SendMetionAllMsgActivity.class);
        intent.putExtra(IntentKey.GROUP_ID,gid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_metion_all_msg);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        mTargetId=getIntent().getStringExtra(IntentKey.GROUP_ID);
    }

    @OnClick({R.id.mSend_metion_return, R.id.mSend_metion_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSend_metion_return:
                finish();
                break;
            case R.id.mSend_metion_send:
                if(!mSendMetionEdittext.getText().toString().equals("")){
                    isSend();
                }else{
                    ToastUtil.show(getApplicationContext(),"请您输入要发送的内容...");
                }
                break;
        }
    }
    private void isSend() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您发送的内容将会被所有群组成员收到,确认发送吗?")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List< String > atUserList  = new ArrayList<>();
                atUserList.add(AT_ALL_TAG);
                V2TIMMessage textAtMessage = V2TIMManager.getMessageManager().createTextAtMessage(getBaseContext().getString(R.string.group_notice_prefix)+mSendMetionEdittext.getText().toString(), atUserList);
                V2TIMManager.getMessageManager().sendMessage(textAtMessage,null,mTargetId,1,false,null, new V2TIMSendCallback<V2TIMMessage>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(V2TIMMessage v2TIMMessage) {
                        ToastUtil.show(getApplicationContext(), "发送成功");
                        EventBus.getDefault().post(new FinishGroupInfoEvent());
                        EventBus.getDefault().post(new RefreshMessageBean());
                        finish();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });

                dialog.dismiss();
            }
        }).create().show();
    }
}
