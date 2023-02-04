package com.aiwujie.shengmo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpByteListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.OkHttpSession;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class VerifyAccountPop extends BasePopupWindow {
    @BindView(R.id.tv_pop_verify_code_account)
    TextView tvPopVerifyCodeAccount;
    @BindView(R.id.et_pop_verify_code_img)
    EditText etPopVerifyCodeImg;
    @BindView(R.id.iv_pop_verify_code_img)
    ImageView ivPopVerifyCodeImg;
    @BindView(R.id.et_pop_verify_code_code)
    EditText etPopVerifyCodeCode;
    @BindView(R.id.tv_pop_verify_code_send)
    TextView tvPopVerifyCodeSend;
    @BindView(R.id.tv_pop_verify_code_commit)
    TextView tvPopVerifyCodeCommit;
    @BindView(R.id.iv_pop_verify_code_close)
    ImageView ivPopVerifyCodeClose;
    @BindView(R.id.tv_pop_verify_code_title)
    TextView tvPopVerifyCodeTitle;
    @BindView(R.id.tv_pop_verify_code_tips)
    TextView tvPopVerifyCodeTips;
    @BindView(R.id.ll_pop_verify_account_cancel)
    LinearLayout llPopVerifyAccountCancel;
    private Context context;
    private String account;
    private String title, info;
    private String sessions = "";
    private boolean retryLoad = false;
    int time = 60;
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<VerifyAccountPop> activityWeakReference;

        public MyHandler(VerifyAccountPop verifyAccountPop) {
            activityWeakReference = new WeakReference<>(verifyAccountPop);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VerifyAccountPop verifyAccountPop = activityWeakReference.get();
            if (verifyAccountPop == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    if (verifyAccountPop.time <= 0) {
                        verifyAccountPop.tvPopVerifyCodeSend.setText("发送验证码");
                        verifyAccountPop.tvPopVerifyCodeSend.setBackgroundResource(R.drawable.bg_round_verify_code);
                        verifyAccountPop.tvPopVerifyCodeSend.setTextColor(Color.parseColor("#FFFFFF"));
                        verifyAccountPop.tvPopVerifyCodeSend.setEnabled(true);
                        verifyAccountPop.time = 60;
                    } else {
                        verifyAccountPop.tvPopVerifyCodeSend.setText(verifyAccountPop.time-- + "秒后重新获取");
                        verifyAccountPop.tvPopVerifyCodeSend.setBackgroundResource(R.drawable.item_login_hui_btn);
                        verifyAccountPop.tvPopVerifyCodeSend.setTextColor(Color.parseColor("#DDDDDD"));
                        verifyAccountPop.tvPopVerifyCodeSend.setEnabled(false);
                        verifyAccountPop.handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    }


    private OkHttpSession okHttpSession;

    public VerifyAccountPop(Context context, String account) {
        super(context);
        this.context = context;
        this.account = account;
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setAdjustInputMethod(false);
        setOutSideDismiss(false);
        initView();
        getVerifyImg2();
        initListener();
    }

    public VerifyAccountPop(Context context, String account, String title, String info) {
        super(context);
        this.context = context;
        this.account = account;
        this.title = title;
        this.info = info;
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setAdjustInputMethod(false);
        setOutSideDismiss(false);
        initView();
        getVerifyImg2();
        initListener();
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_verify_account);
        ButterKnife.bind(this, view);
        okHttpSession = new OkHttpSession();
        return view;
    }

    void initView() {
        tvPopVerifyCodeAccount.setText("验证账号：" + account);
        if (!TextUtil.isEmpty(title)) {
            tvPopVerifyCodeTitle.setText(title);
        }
        if (!TextUtil.isEmpty(info)) {
            tvPopVerifyCodeTips.setText(info);
        }
    }

    void initListener() {
        ivPopVerifyCodeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyImg2();
            }
        });
        tvPopVerifyCodeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtil.isEmpty(etPopVerifyCodeImg.getText().toString())) {
                    ToastUtil.show(context, "请输入图形验证码");
                } else {
                    if (account.contains("@")) {
                        sendVerifyEmail();
                    } else {
                        sendVerifyCode();
                    }
                }
            }
        });

        tvPopVerifyCodeCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtil.isEmpty(tvPopVerifyCodeAccount.getText().toString())) {
                    if (onVerifyCodeCheckListener != null) {
                        onVerifyCodeCheckListener.OnVerifyCodeCheck(etPopVerifyCodeCode.getText().toString());
                    }
                }
            }
        });

        ivPopVerifyCodeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    void getVerifyImg() {
        HttpHelper.getInstance().getVerImg(new HttpByteListener() {
            @Override
            public void onSuccess(String session, byte[] data) {
                sessions = session;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                ivPopVerifyCodeImg.setImageBitmap(bitmap);
            }

            @Override
            public void onFail(String msg) {
                if (!retryLoad) {
                    retryLoad = true;
                    getVerifyImg();
                }
            }
        });


    }

    void getVerifyImg2() {
        okHttpSession.ChangeVercodeImage2(HttpUrl.NetPic() + HttpUrl.GetPicSession);
    }

    public void showImg(Bitmap bitmap) {
        ivPopVerifyCodeImg.setImageBitmap(bitmap);
    }

    public void setSession(String session) {
        sessions = session;
    }

    void sendVerifyCode() {
        HttpHelper.getInstance().sendVerCode(account, etPopVerifyCodeImg.getText().toString(), sessions, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(context, "验证码发送成功");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    void sendVerifyEmail() {
        HttpHelper.getInstance().sendVerEmail(account, etPopVerifyCodeImg.getText().toString(), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(context, "验证码发送成功");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }


    public interface OnVerifyCodeCheckListener {
        void OnVerifyCodeCheck(String code);
    }

    OnVerifyCodeCheckListener onVerifyCodeCheckListener;

    public void setOnVerifyCodeCheckListener(OnVerifyCodeCheckListener onVerifyCodeCheckListener) {
        this.onVerifyCodeCheckListener = onVerifyCodeCheckListener;
    }


}
