package com.aiwujie.shengmo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignGiftActivity extends Activity {

    @BindView(R.id.mSign_gift_iv)
    ImageView mSignGiftIv;
    @BindView(R.id.mSign_gift_tv)
    TextView mSignGiftTv;
    @BindView(R.id.activity_sign_gift)
    ConstraintLayout activitySignGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_gift);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        int giftFlag = getIntent().getIntExtra("giftFlag", -1);
        switch (giftFlag) {
            case 0:
               // mSignGiftIv.setImageResource(R.mipmap.signgiftone);
                mSignGiftIv.setImageResource(R.mipmap.presentnew28);
                mSignGiftTv.setText("已领取幸运草，可送人哦~");
                break;
            case 1:
                //mSignGiftIv.setImageResource(R.mipmap.signgifttwo);
                mSignGiftIv.setImageResource(R.mipmap.presentnew29);
                mSignGiftTv.setText("已领取糖果，可送人哦~");
                break;
            case 2:
                //mSignGiftIv.setImageResource(R.mipmap.signgiftthree);
                mSignGiftIv.setImageResource(R.mipmap.presentnew30);
                mSignGiftTv.setText("已领取玩具狗，可送人哦~");
                break;
            case 4:
                //mSignGiftIv.setImageResource(R.mipmap.signgiftfour);
                mSignGiftIv.setImageResource(R.mipmap.yptong);
                mSignGiftTv.setText("已领取通用邮票~");
                break;
            case 5:
                //mSignGiftIv.setImageResource(R.mipmap.signgiftfive);
                mSignGiftIv.setImageResource(R.mipmap.sanzhangyoupiao);
                mSignGiftTv.setText("已领取3张邮票~");
                break;
            case 2001:
                //mSignGiftIv.setImageResource(R.mipmap.signgiftxiaoone);
                mSignGiftIv.setImageResource(R.mipmap.presentnew32);
                mSignGiftTv.setText("恭喜你抽中TT(8魅力)");
                break;
            case 2002:
                //mSignGiftIv.setImageResource(R.mipmap.signgiftxiaotwo);
                mSignGiftIv.setImageResource(R.mipmap.presentnew31);
                mSignGiftTv.setText("恭喜你抽中内内(10魅力)");
                break;
            case 2003:
                //mSignGiftIv.setImageResource(R.mipmap.signgiftxiaowu);
                mSignGiftIv.setImageResource(R.drawable.ic_sign_empty);
                mSignGiftTv.setText("很遗憾，您什么都没抽到~");
                break;
            case 2004:
                mSignGiftIv.setImageResource(R.drawable.ic_sign_vip_year);
                mSignGiftTv.setText("恭喜您抽中VIP会员12个月！");
                break;
            case 2005:
                mSignGiftIv.setImageResource(R.drawable.ic_sign_vip);
                mSignGiftTv.setText("恭喜您抽中VIP会员1个月！");
                break;
            case 2006:
                mSignGiftIv.setImageResource(R.mipmap.signgiftdaone);
                mSignGiftTv.setText("恭喜您抽中6张邮票！");
                break;
        }
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        scaleAnimation.setStartOffset(50);//执行前的等待时间
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        activitySignGift.startAnimation(scaleAnimation);

    }

    @OnClick(R.id.activity_sign_gift)
    public void onViewClicked() {
        exitAnima();
    }

    public void exitAnima() {
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        activitySignGift.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //可以在这里先进行某些操作
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAnima();
        }
        return false;
    }
}
