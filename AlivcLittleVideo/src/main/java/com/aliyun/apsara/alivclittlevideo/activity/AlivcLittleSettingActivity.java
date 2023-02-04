package com.aliyun.apsara.alivclittlevideo.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.common.global.Version;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.svideo.common.utils.image.ImageLoaderImpl;
import com.aliyun.svideo.common.utils.image.ImageLoaderOptions;


/**
 * 设置界面, 主要显示趣视频版本信息, 及所包含的sdk信息
 *
 * @author xlx
 */
public class AlivcLittleSettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_little_activity_setting);
        initView();
    }

    private int clickCount;
    private void initView() {
        findViewById(R.id.fl_setting_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imageView = findViewById(R.id.iv_version_info_icon);

        new ImageLoaderImpl().loadImage(this, R.mipmap.alivc_little_setting, new ImageLoaderOptions.Builder()
                                        .circle()//加载圆形
                                        .build())
        .into(imageView);

        TextView textView = findViewById(R.id.tv_sdk_version);
        textView.setText(String.format(getResources().getString(R.string.alivc_little_setting_sdk_version), Version.VERSION, AliPlayerFactory.getSdkVersion() ));
        TextView tvVersionTip = findViewById(R.id.tv_mine_id);
        String versionName = getVersionName(this);
        String tip = getResources().getString(R.string.alivc_little_setting_video_version_code) + versionName;
        tvVersionTip.setText(tip);

    }

    /**
     * get App versionName
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
