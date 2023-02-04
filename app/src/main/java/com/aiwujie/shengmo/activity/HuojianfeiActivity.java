package com.aiwujie.shengmo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HuojianfeiActivity extends Activity{

    @BindView(R.id.huojianfei)
    ImageView mhuojianfei;
    @BindView(R.id.huojianfei2)
    ImageView mhuojianfei2;
    @BindView(R.id.huojianfei3)
    ImageView mhuojianfei3;
    @BindView(R.id.feill)
    PercentLinearLayout feill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huojianfei);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int feishu = intent.getIntExtra("feishu", 0);

        if(feishu==1){
            mhuojianfei.setVisibility(View.VISIBLE);
            mhuojianfei2.setVisibility(View.GONE);
            mhuojianfei3.setVisibility(View.VISIBLE);
            feill.setVisibility(View.GONE);
        }else if(feishu==2){
            mhuojianfei.setVisibility(View.GONE);
            mhuojianfei2.setVisibility(View.VISIBLE);
            mhuojianfei3.setVisibility(View.VISIBLE);
            feill.setVisibility(View.VISIBLE);
        }else if(feishu>=3){
            mhuojianfei.setVisibility(View.VISIBLE);
            mhuojianfei2.setVisibility(View.VISIBLE);
            mhuojianfei3.setVisibility(View.VISIBLE);
            feill.setVisibility(View.VISIBLE);
        }

        ObjectAnimator translationY = new ObjectAnimator().ofFloat(mhuojianfei, "translationY", 0, -50, 20, -300, -600, -900, -1200, -1500, -1800);
        ObjectAnimator translationY2 = new ObjectAnimator().ofFloat(mhuojianfei2, "translationY", 0, -50, 20, -300, -600, -900, -1200, -1500, -1800);
        ObjectAnimator translationY3 = new ObjectAnimator().ofFloat(mhuojianfei3, "translationY", 0, -50, 20, -300, -600, -900, -1200, -1500, -1800);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mhuojianfei.setVisibility(View.GONE);
                mhuojianfei2.setVisibility(View.GONE);
                mhuojianfei3.setVisibility(View.GONE);
                feill.setVisibility(View.GONE);
                finish();
            }
        });
        animatorSet.playTogether(translationY,translationY2,translationY3); //设置动画
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(1500);  //设置动画时间
        animatorSet.start(); //启动

    }
}
