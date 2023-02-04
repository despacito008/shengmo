package com.aiwujie.shengmo.activity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlertSettingActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.mAlert_Setting_return)
    ImageView mAlertSettingReturn;
    @BindView(R.id.mAlert_Setting_ckRing)
    RadioButton mAlertSettingCkRing;
    @BindView(R.id.mAlert_Setting_ckVibrator)
    RadioButton mAlertSettingCkVibrator;
    @BindView(R.id.mAlert_Setting_ckQuiet)
    RadioButton mAlertSettingCkQuiet;
    @BindView(R.id.mAlert_Setting_rg)
    RadioGroup mAlertSettingRg;
    @BindView(R.id.activity_alert_setting)
    PercentLinearLayout activityAlertSetting;
    @BindView(R.id.rb_sound_normal)
    RadioButton rbSoundNormal;
    @BindView(R.id.rb_sound_dog)
    RadioButton rbSoundDog;
    @BindView(R.id.rb_sound_cat)
    RadioButton rbSoundCat;
    @BindView(R.id.rb_sound_water)
    RadioButton rbSoundWater;
    @BindView(R.id.rg_sound)
    RadioGroup rgSound;
    @BindView(R.id.rb_sound_whip)
    RadioButton rbSoundWhip;

    private SoundPool soundPool;

    private int sound1, sound2, sound3, sound4, sound5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_setting);
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);
        setData();
        setListener();
        initSoundPool();
    }

    private void setData() {
        // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "alertflag", -1);
        switch (alertflag) {
            case 0:
                mAlertSettingCkRing.setChecked(true);
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 0);
                break;
            case 1:
                mAlertSettingCkVibrator.setChecked(true);
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 1);
                break;
            case 2:
                mAlertSettingCkQuiet.setChecked(true);
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 2);
                break;
            case -1:
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 0);
                mAlertSettingCkRing.setChecked(true);
                break;
        }

        int soundFlag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "soundFlag", -1);

        switch (soundFlag) {
            case -1:
            case 0:
                rbSoundNormal.setChecked(true);
                SharedPreferencesUtils.setParam(getApplicationContext(), "soundFlag", 0);
                break;
            case 1:
                rbSoundWater.setChecked(true);
                break;
            case 2:
                rbSoundDog.setChecked(true);
                break;
            case 3:
                rbSoundCat.setChecked(true);
                break;
            case 4:
                rbSoundWhip.setChecked(true);
                break;
        }

    }

    private void setListener() {
        mAlertSettingRg.setOnCheckedChangeListener(this);
        rgSound.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sound_normal:
                        SharedPreferencesUtils.setParam(getApplicationContext(), "soundFlag", 0);
                        if (isLoadComplete) {
                            playSound(0);
                        }
                        break;
                    case R.id.rb_sound_water:
                        SharedPreferencesUtils.setParam(getApplicationContext(), "soundFlag", 1);
                        if (isLoadComplete) {
                            playSound(1);
                        }
                        break;
                    case R.id.rb_sound_dog:
                        SharedPreferencesUtils.setParam(getApplicationContext(), "soundFlag", 2);
                        if (isLoadComplete) {
                            playSound(2);
                        }
                        break;
                    case R.id.rb_sound_cat:
                        SharedPreferencesUtils.setParam(getApplicationContext(), "soundFlag", 3);
                        if (isLoadComplete) {
                            playSound(3);
                        }
                        break;
                    case R.id.rb_sound_whip:
                        SharedPreferencesUtils.setParam(getApplicationContext(), "soundFlag", 4);
                        if (isLoadComplete) {
                            playSound(4);
                        }
                        break;
                }
            }
        });
    }

    @OnClick(R.id.mAlert_Setting_return)
    public void onClick() {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mAlert_Setting_ckRing:
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 0);
                break;
            case R.id.mAlert_Setting_ckVibrator:
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 1);

                break;
            case R.id.mAlert_Setting_ckQuiet:
                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 2);

                break;
        }
    }

    boolean isLoadComplete = false;
    void initSoundPool() {
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入最多播放音频数量,
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
        }
        sound1 = soundPool.load(getBaseContext(), R.raw.sound_wechat, 1);
        sound2 = soundPool.load(getBaseContext(), R.raw.sound_water, 2);
        sound3 = soundPool.load(getBaseContext(), R.raw.dog, 3);
        sound4 = soundPool.load(getBaseContext(), R.raw.sound_cat, 4);
        sound5 = soundPool.load(getBaseContext(), R.raw.sound_whip, 5);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoadComplete = true;
            }
        });
    }

    void playSound(int type) {
        switch (type) {
            case 0:
                soundPool.play(sound1, 1, 1, 1, 0, 1);
                break;
            case 1:
                soundPool.play(sound2, 1, 1, 1, 0, 1);
                break;
            case 2:
                soundPool.play(sound3, 1, 1, 1, 0, 1);
                break;
            case 3:
                soundPool.play(sound4, 1, 1, 1, 0, 1);
                break;
            case 4:
                soundPool.play(sound5, 1, 1, 1, 0, 1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
        }


    }
}
