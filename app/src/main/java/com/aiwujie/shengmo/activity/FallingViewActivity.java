package com.aiwujie.shengmo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.dingmouren.fallingview.FallingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FallingViewActivity extends Activity {

    @BindView(R.id.mFalling_view)
    FallingView mFallingView;
    @BindView(R.id.activity_falling_view)
    LinearLayout activityFallingView;
    private int fallingFlag;
    private int[] presents = {R.mipmap.presentnew01, R.mipmap.presentnew02, R.mipmap.presentnew03, R.mipmap.presentnew04, R.mipmap.presentnew05,
            R.mipmap.presentnew06, R.mipmap.presentnew07, R.mipmap.presentnew08, R.mipmap.presentnew09,
            R.mipmap.presentnew10, R.mipmap.presentnew11, R.mipmap.presentnew12, R.mipmap.presentnew13,
            R.mipmap.presentnew14, R.mipmap.presentnew15, R.mipmap.presentnew16, R.mipmap.presentnew17,
            R.mipmap.presentnew18, R.mipmap.presentnew19, R.mipmap.presentnew20, R.mipmap.presentnew21,
            R.mipmap.presentnew22, R.mipmap.presentnew23, R.mipmap.presentnew24, R.mipmap.presentnew25,
            R.mipmap.presentnew26, R.mipmap.presentnew27,R.mipmap.presentnew28,R.mipmap.presentnew29,R.mipmap.presentnew30,
            R.mipmap.presentnew31,R.mipmap.presentnew32};
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mFallingView.setVisibility(View.VISIBLE);
                    mFallingView.setAnimation(AnimationUtil.ViewToVisible());
                    mFallingView.setImageResource(presents[fallingFlag]);//设置碎片的图片,默认的图片是雪花
                    mFallingView.setDensity(45);//设置密度，数值越大，碎片越密集,默认值是80
                    mFallingView.setScale(3);//设置碎片的大小，数值越大，碎片越小，默认值是3
                    mFallingView.setDelay(0);//设置碎片飘落的速度，数值越大，飘落的越慢，默认值是10
                    handler.sendEmptyMessageDelayed(1,6000);
                    break;
                case 1:
                    mFallingView.setVisibility(View.GONE);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_view);
        ButterKnife.bind(this);
        fallingFlag=getIntent().getIntExtra("fallingFlag",-1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },100);
    }

    @OnClick(R.id.activity_falling_view)
    public void onViewClicked() {
        mFallingView.setVisibility(View.GONE);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            mFallingView.setVisibility(View.GONE);
            finish();
        }
        return false;
    }
}
