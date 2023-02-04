package com.aliyun.apsara.alivclittlevideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;
import com.aliyun.svideo.common.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修改用户昵称
 *
 * @author xlx
 */
public class AlivcLittleUserChangeActivity extends AppCompatActivity {

    private TextView finishChange;
    private EditText etNickName;
    private LittleUserInfo userInfo;

    /**
     * 修改昵称 result code
     */
    private static final int RESULT_CHANGE_NICK_NAME = 1002;

    /**
     * 修改完昵称后,用于刷新用户信息
     */
    private static final String REFRESH_USER_INFO = "refresh";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_little_activity_user_change);

        initUserInfo();
        initView();

    }

    private void initUserInfo() {
        userInfo = AlivcLittleUserManager.getInstance().getUserInfo(this);
    }

    private void initView() {
        findViewById(R.id.fl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finishChange = findViewById(R.id.little_titlebar_finish);
        etNickName = findViewById(R.id.et_nickname);
        //名字长度限制字符为12个
        etNickName.setFilters(new InputFilter[] {new NameLengthFilter(12)});
        finishChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputNickName = etNickName.getText().toString().trim();

                if (TextUtils.isEmpty(inputNickName)) {
                    return;
                }

                requestChangeNickName(inputNickName);
            }
        });
    }

    private void requestChangeNickName(final String inputNickName) {
        LittleHttpManager.getInstance().requestChangeNickName(userInfo.getUserId(), userInfo.getToken(), inputNickName,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleUserInfoResponse>() {
            @Override
            public void onResponse(boolean result, String message, LittleHttpResponse.LittleUserInfoResponse data) {
                if (result) {
                    userInfo.setNickName(inputNickName);
                    AlivcLittleUserManager.getInstance().setUserInfo(AlivcLittleUserChangeActivity.this, userInfo);
                    Intent intent = AlivcLittleUserChangeActivity.this.getIntent();
                    intent.putExtra(REFRESH_USER_INFO, REFRESH_USER_INFO);
                    AlivcLittleUserChangeActivity.this.setResult(RESULT_CHANGE_NICK_NAME, intent);
                    AlivcLittleUserChangeActivity.this.finish();
                } else {
                    ToastUtils.show(AlivcLittleUserChangeActivity.this, message);
                }
            }
        });
    }
    private class NameLengthFilter implements InputFilter {
        /**
         *  最大英文/数字长度 一个汉字算两个字母
         */
        private int nameLength;
        /**
         * unicode编码，判断是否为汉字
         */
        String regEx = "[\\u4e00-\\u9fa5]";
        public NameLengthFilter(int nameLength) {
            super();
            this.nameLength = nameLength;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int destCount = dest.toString().length() + getChineseCount(dest.toString());
            int sourceCount = source.toString().length()
                              + getChineseCount(source.toString());
            if (destCount + sourceCount > nameLength) {
                ToastUtils.show(AlivcLittleUserChangeActivity.this, getResources().getString(R.string.alivc_little_user_setting_word_limit) );
                return "";
            } else {
                return source;
            }
        }

        private int getChineseCount(String str) {
            int count = 0;
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }
            return count;
        }
    }
}
