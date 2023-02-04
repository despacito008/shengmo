package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.customview.CustomViewPage;
import com.aiwujie.shengmo.utils.SaveImageUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.utils.glideprogress.GlideProgressListener;
import com.aiwujie.shengmo.utils.glideprogress.ProgressListener;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.ninegrid.photoview.PhotoView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoomActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.zoom_in_viewpager)
    CustomViewPage zoomInViewpager;
    @BindView(R.id.zoom_in_progressBar)
    ProgressBar zoomInProgressBar;
    @BindView(R.id.zoom_in_progressTv)
    TextView zoomInProgressTv;
    private int picposition;
    private ArrayList<String> pics;
    private String imageurl;
    private PhotoView photoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent = getIntent();
        picposition = intent.getIntExtra("position", -1);
        pics = intent.getStringArrayListExtra("pics");
//        Log.i("ZoomActivity", "onCreate: " + pics.toString());
        zoomInViewpager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
//        zoomInViewpager.setOffscreenPageLimit(pics.size());
        zoomInViewpager.setAdapter(new MyPageAdapter());
        zoomInViewpager.setCurrentItem(picposition);
    }

    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pics.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            new AsyncDownloadTask().execute();
            zoomInProgressBar.setVisibility(View.VISIBLE);
            zoomInProgressTv.setVisibility(View.VISIBLE);
            photoview = new PhotoView(container.getContext());
//            photoview.enable();
            imageurl = pics.get(position);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.default_error);
            requestOptions.error(R.mipmap.default_error);
            Glide.with(ZoomActivity.this)
                    .load(imageurl)
                    .apply(requestOptions)
//                    .crossFade()
                    .into(photoview);
//            photoview.enableRotate();
            container.addView(photoview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertView(null, null, "取消", null, new String[]{"保存"},
                            ZoomActivity.this, AlertView.Style.ActionSheet, ZoomActivity.this).show();
                    return true;
                }
            });
            photoview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return photoview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    public void onItemClick(Object o, final int position, String data) {
        switch (position) {
            case 0:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
//                            Log.i("ZoomActivity", "run: " + imageurl);
                             Bitmap myBitmap = null;
                                    Glide.with(getApplicationContext())//上下文
                                    .load(imageurl)//url
                                    .into(new SimpleTarget<Drawable>(750,750){

                                        @Override
                                        public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                                           final Bitmap tempBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                                                    drawable.getIntrinsicHeight(),
                                                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                            : Bitmap.Config.RGB_565);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SaveImageUtils.saveImageToGallerys(ZoomActivity.this, tempBitmap);
                                                }
                                            });
                                        }
                                    });

//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    SaveImageUtils.saveImageToGallerys(ZoomActivity.this, myBitmap);
//                                }
//                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
        }
    }

    private class AsyncDownloadTask extends AsyncTask<Void, Long, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ProgressListener listener = new ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    publishProgress(bytesRead, contentLength);
                }
            };
            GlideProgressListener.addGlideProgressListener(listener);
            try {
                Glide.with(ZoomActivity.this)
                        .load(imageurl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            GlideProgressListener.removeGlideProgressListener(listener);
//            Glide.get(ZoomActivity.this).clearDiskCache();
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
//            Log.i("ZoomAprogress", "onProgressUpdate:" + ((100L * values[0]) / values[1]));
            try {
                zoomInProgressTv.setText(((100L * values[0]) / values[1]) + "%");
                if (zoomInProgressBar != null) {
                    zoomInProgressBar.setProgress((int) ((100L * values[0]) / values[1]));
                    zoomInProgressBar.setMax(100);
                }
            } catch (ArithmeticException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            zoomInProgressBar.setVisibility(View.GONE);
            zoomInProgressTv.setVisibility(View.GONE);
            zoomInProgressTv.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
