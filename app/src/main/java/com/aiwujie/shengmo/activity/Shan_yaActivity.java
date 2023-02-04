package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.zdyview.CountDownButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class Shan_yaActivity extends AppCompatActivity {

    private ImageView mivshan;
    int iya=5;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
//                if(iya<=0){
//                    signFlashImg();
//                    finish();
//                    return;
//                }
//                mcountbutton.setText(""+iya);
//                iya--;
//                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };
    private CountDownButton mcountbutton;
    private String shanurl;
    private ImageView iv_aishan;
    private TextView tvyayaya;
    private SimpleDraweeView iv_shan1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_shan_ya);

        Intent intent = getIntent();
        shanurl = intent.getStringExtra("shanurl");
        mcountbutton = (CountDownButton) findViewById(R.id.countdown2);
        mivshan = (ImageView) findViewById(R.id.iv_shan);
        iv_shan1 = (SimpleDraweeView) findViewById(R.id.iv_shan1);
        iv_aishan = (ImageView) findViewById(R.id.iv_aishan);
        tvyayaya = (TextView) findViewById(R.id.tvyayaya);


        iv_shan1.setImageURI(shanurl);
        showUrlBlur(iv_shan1,shanurl, 20, 30);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestOptions requestOptions = new RequestOptions();
//                requestOptions.placeholder(R.drawable.rc_image_error);
                requestOptions.error(R.mipmap.default_error);
//                requestOptions.transform(new GlideCircleTransform(mContext));
                Glide.with(getApplicationContext()).load(shanurl).apply(requestOptions).into(mivshan);
            }
        },500);

        mcountbutton.setOnCountClickListener(new CountDownButton.onCountClickListener() {
            @Override
            public void onCounting(double process) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        int temp =  (5 - (int)(process / 20f));
                        int temp = (5 - (int) (process *5));
                        mcountbutton.setText(""+temp);
                    }
                });
            }

            @Override
            public void onCountingClick() {

            }

            @Override
            public void onCountOverClick() {
                signFlashImg();
                finish();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                iv_shan1.setVisibility(View.GONE);
                mcountbutton.setVisibility(View.VISIBLE);
                tvyayaya.setVisibility(View.GONE);
                iv_aishan.setVisibility(View.GONE);
                mcountbutton.setTotalTime(5000);
                mcountbutton.startTimer();
                handler.sendEmptyMessage(1);
                break;
            case MotionEvent.ACTION_UP:
                signFlashImg();
                mcountbutton.cancle();
                finish();
                break;
        }


        return super.onTouchEvent(event);
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeMessages(1);
    }

    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(6, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void signFlashImg() {
        List<String> flashImageCacheList = SharedPreferencesUtils.getDataList("flash_img_"+ MyApp.uid);
        if(flashImageCacheList == null) {
            flashImageCacheList = new ArrayList<>();
        }
        flashImageCacheList.add(shanurl);
        SharedPreferencesUtils.addDataList("flash_img_"+MyApp.uid,flashImageCacheList);
    }
}
