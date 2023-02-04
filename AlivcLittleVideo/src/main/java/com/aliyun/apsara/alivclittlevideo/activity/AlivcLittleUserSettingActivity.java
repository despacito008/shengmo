package com.aliyun.apsara.alivclittlevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.common.utils.image.ImageLoaderImpl;
import com.aliyun.svideo.common.utils.image.ImageLoaderOptions;
import com.aliyun.svideo.common.widget.AlivcCustomAlertDialog;

/**
 * 用户设置界面, 刷新用户信息
 *
 * @author xlx
 */
public class AlivcLittleUserSettingActivity extends Activity {

    private ImageView userAvatar;
    private TextView mUserId, mNickName;
    private LittleUserInfo userInfo;
    /**
     * 修改昵称 request code
     */
    private static final int REQUEST_CHANGE_NICK_NAME = 2001;
    /**
     * 修改昵称 result code
     */
    private static final int RESULT_CHANGE_NICK_NAME = 1002;
    /**
     * 修改完昵称后,用于刷新用户信息
     */
    private static final String REFRESH_USER_INFO = "refresh";

    /**
     * 更新用户信息, 包括昵称, id, 头像等 result code
     */
    private static final int RESULT_CHANGE_USER_INFO = 1003;
    /**
     * 是否更换用户
     */
    private boolean isUserChange;
    /**
     * 已经取消用户更新
     */
    private boolean hasCancel;
    /**
     * 用户是否正在上传视频
     */
    private boolean isPublishing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_little_activity_user_setting);
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
                //finish();
                onBackPressed();

            }
        });

        findViewById(R.id.little_titlebar_finish).setVisibility(View.GONE);

        findViewById(R.id.tv_little_change_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AlivcLittleUserManager.getInstance().isAllowChangeUser()) {
                    showChangeUserConfirmDialog();
                } else {
                    ToastUtils.show(AlivcLittleUserSettingActivity.this, getResources().getString(R.string.alivc_little_user_setting_tip_change_user_fail) );
                }

            }
        });

        userAvatar = findViewById(R.id.iv_little_user_setting_avatar);
        mUserId = findViewById(R.id.tv_little_user_id);
        mNickName = findViewById(R.id.tv_little_username);
        // 昵称
        findViewById(R.id.little_change_userinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToUserChange();
            }
        });
        updateUserView(userInfo);

    }

    /**
     * 刷新用户确认提示窗
     */
    private void showChangeUserConfirmDialog() {
        new AlivcCustomAlertDialog.Builder(this)
        .setNoIcon(true)
        .setMessage(getResources().getString(R.string.alivc_little_user_setting_dialog_change_user_tip))
        .setDialogClickListener("", "", new AlivcCustomAlertDialog.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                newGuest();
            }

            @Override
            public void onCancel() {

            }
        })
        .create()
        .show();
    }

    /**
     * 生成新用户
     */
    private void newGuest() {
        LittleHttpManager.getInstance().newGuest(
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleUserInfoResponse>() {
            @Override
            public void onResponse(boolean result, String message, LittleHttpResponse.LittleUserInfoResponse data) {
                if (result && data != null && data.data != null && !hasCancel) {
                    LittleUserInfo userInfo = data.data;
                    isUserChange = true;
                    updateUserView(userInfo);
                    AlivcLittleUserManager.getInstance().setUserInfo(AlivcLittleUserSettingActivity.this,
                            userInfo);
                    AlivcLittleUserSettingActivity.this.setResult(RESULT_CHANGE_USER_INFO);

                } else {
                    ToastUtils.show(AlivcLittleUserSettingActivity.this, message);
                }
            }
        });
    }

    /**
     * 更换用户之后调用该方法进行刷新UI
     *
     * @param userInfo 用户信息
     */
    private void updateUserView(LittleUserInfo userInfo) {
        new ImageLoaderImpl().loadImage(this, userInfo.getAvatarUrl(),
                                        new ImageLoaderOptions.Builder().circle().build()).into(userAvatar);

        mUserId.setText(String.format("ID: %s", userInfo.getUserId()));
        mNickName.setText(userInfo.getNickName());
    }

    /**
     * 跳转到修改用户昵称的界面
     */
    private void jumpToUserChange() {
        Intent intent = new Intent(this, AlivcLittleUserChangeActivity.class);
        this.startActivityForResult(intent, REQUEST_CHANGE_NICK_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_NICK_NAME && resultCode == RESULT_CHANGE_NICK_NAME) {
            if (!isUserChange) {
                AlivcLittleUserSettingActivity.this.setResult(RESULT_CHANGE_NICK_NAME);
            }
            updateUserView(AlivcLittleUserManager.getInstance().getUserInfo(this));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hasCancel = true;
    }
}
