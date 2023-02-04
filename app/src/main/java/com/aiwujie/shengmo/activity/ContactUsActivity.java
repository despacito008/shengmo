package com.aiwujie.shengmo.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ContactData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends AppCompatActivity {

    Handler handler = new Handler();
    @BindView(R.id.mContact_return)
    ImageView mContactReturn;
    @BindView(R.id.mContact_SmQQ)
    TextView mContactSmQQ;
    @BindView(R.id.mContact_ll_SmQQ)
    LinearLayout mContactLlSmQQ;
    @BindView(R.id.mContact_SmWeChat)
    TextView mContactSmWeChat;
    @BindView(R.id.mContact_ll_SmWeChat)
    LinearLayout mContactLlSmWeChat;
    @BindView(R.id.mContact_mobile)
    TextView mContactMobile;
    @BindView(R.id.mContact_ll_mobile)
    LinearLayout mContactLlMobile;
    @BindView(R.id.mContact_email)
    TextView mContactEmail;
    @BindView(R.id.mContact_ll_email)
    LinearLayout mContactLlEmail;
    @BindView(R.id.mContact_public_num)
    TextView mContactPublicNum;
    @BindView(R.id.mContact_ll_public_num)
    LinearLayout mContactLlPublicNum;
    @BindView(R.id.mContact_weiBo)
    TextView mContactWeiBo;
    @BindView(R.id.mContact_ll_weiBo)
    LinearLayout mContactLlWeiBo;
    @BindView(R.id.mContact_net)
    TextView mContactNet;
    @BindView(R.id.mContact_ll_net)
    LinearLayout mContactLlNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        getCallAct();
        mContactReturn.setOnClickListener(v -> finish());
    }

    private void getCallAct() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.GetCall, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ContactData data = new Gson().fromJson(response, ContactData.class);
                            disPlayInfo(mContactLlEmail,mContactEmail,data.getData().getEMAIL());
                            disPlayInfo2(mContactLlMobile,mContactMobile,data.getData().getKFDH());
                            disPlayInfo(mContactLlSmQQ,mContactSmQQ,data.getData().getSMQQ());
                            disPlayInfo(mContactLlSmWeChat,mContactSmWeChat,data.getData().getKFWX());
                            disPlayInfo(mContactLlPublicNum,mContactPublicNum,data.getData().getOFFICIAL_ACCOUNT());
                            disPlayInfo(mContactLlWeiBo,mContactWeiBo,data.getData().getWEI_BO());
                            disPlayInfo(mContactLlNet,mContactNet,data.getData().getWANG_ZHI());
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

    public void disPlayInfo(View view,TextView textView, String text) {
        if (TextUtil.isEmpty(text)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtil.show(ContactUsActivity.this, "已复制到剪贴板");
                }
            });
        }
    }

    public void disPlayInfo2(View view,TextView textView, String text) {
        if (TextUtil.isEmpty(text)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + text);
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }
    }

}
