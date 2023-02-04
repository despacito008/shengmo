package com.tencent.qcloud.tim.uikit.component.photoview;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.imsdk.v2.V2TIMDownloadCallback;
import com.tencent.imsdk.v2.V2TIMElem;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.ImageUtil;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;


public class PhotoViewActivity extends Activity {

    private static final String DOWNLOAD_ORIGIN_IMAGE_PATH = "downloadOriginImagePath";
    private static final String BROADCAST_DOWNLOAD_COMPLETED_ACTION = "PhotoViewActivityDownloadOriginImageCompleted";
    public static V2TIMImageElem.V2TIMImage mCurrentOriginalImage;
    private PhotoView mPhotoView;
    private Matrix mCurrentDisplayMatrix = null;
    private TextView mViewOriginalBtn;
    private BroadcastReceiver downloadReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_view);
        String previewPath = getIntent().getStringExtra(TUIKitConstants.IMAGE_PREVIEW_PATH);
        Uri uri = FileUtil.getUriFromPath(previewPath);
        boolean isOrigin = getIntent().getBooleanExtra(TUIKitConstants.IS_ORIGIN_IMAGE, false);
        mCurrentDisplayMatrix = new Matrix();
        mPhotoView = findViewById(R.id.photo_view);
        mPhotoView.setDisplayMatrix(mCurrentDisplayMatrix);
        mPhotoView.setOnMatrixChangeListener(new MatrixChangeListener());
        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());
        mPhotoView.setOnSingleFlingListener(new SingleFlingListener());
        mPhotoView.setOnLongClickListener(new SavePhotoListener());
        mViewOriginalBtn = findViewById(R.id.view_original_btn);
        // 如果是原图就直接显示原图， 否则显示缩略图，点击查看原图按钮后下载原图显示
        mPhotoView.setImageURI(uri);
        if (!isOrigin) {
            mViewOriginalBtn.setVisibility(View.VISIBLE);
            mViewOriginalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentOriginalImage != null) {
                        final String path = ImageUtil.generateImagePath(mCurrentOriginalImage.getUUID(), mCurrentOriginalImage.getType());
                        mCurrentOriginalImage.downloadImage(path, new V2TIMDownloadCallback() {
                            @Override
                            public void onProgress(V2TIMElem.V2ProgressInfo progressInfo) {
                                long percent = Math.round(100 * (progressInfo.getCurrentSize() * 1.0d) / progressInfo.getTotalSize());
                                if (mViewOriginalBtn.getVisibility() != View.INVISIBLE && mViewOriginalBtn.getVisibility() != View.GONE) {
                                    mViewOriginalBtn.setText(percent + "%");
                                }
                            }

                            @Override
                            public void onError(int code, String desc) {
                                ToastUtil.toastLongMessage("Download origin image failed , errCode = " + code + ", " + desc);
                            }

                            @Override
                            public void onSuccess() {
                                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPhotoView.setImageURI(FileUtil.getUriFromPath(path));
                                        mViewOriginalBtn.setText(getString(R.string.completed));
                                        mViewOriginalBtn.setOnClickListener(null);
                                        mViewOriginalBtn.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent();
                                        intent.setAction(BROADCAST_DOWNLOAD_COMPLETED_ACTION);
                                        intent.putExtra(DOWNLOAD_ORIGIN_IMAGE_PATH, path);
                                        LocalBroadcastManager.getInstance(PhotoViewActivity.this).sendBroadcast(intent);
                                    }
                                });
                            }
                        });
                    }
                }
            });

        } else {
            // 因为图片还没下载下来 ， 加载失败, 接收下载成功的广播来刷新显示
            if (mPhotoView.getDrawable() == null) {
                ToastUtil.toastShortMessage("Downloading , please wait.");
                downloadReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (BROADCAST_DOWNLOAD_COMPLETED_ACTION.equals(action)) {
                            String originImagePath = intent.getStringExtra(DOWNLOAD_ORIGIN_IMAGE_PATH);
                            if (originImagePath != null) {
                                mPhotoView.setImageURI(FileUtil.getUriFromPath(originImagePath));
                            }
                        }
                    }
                };
                IntentFilter filter = new IntentFilter();
                filter.addAction(BROADCAST_DOWNLOAD_COMPLETED_ACTION);
                LocalBroadcastManager.getInstance(this).registerReceiver(downloadReceiver, filter);
            }
        }
        findViewById(R.id.photo_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            finish();
        }
    }


    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

    private class SingleFlingListener implements OnSingleFlingListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadReceiver);
            downloadReceiver = null;
        }
    }

    private class SavePhotoListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(PhotoViewActivity.this).setMessage("是否保存图片？")
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tryToDownLoadPic();
                }
            }).create().show();
            return true;
        }
    }

    void tryToDownLoadPic() {
        if (!PermissionUtils.checkPermission(PhotoViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.toastShortMessage("没有存储权限");
            return ;
        }
        if (!PermissionUtils.checkPermission(PhotoViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ToastUtil.toastShortMessage("没有读取权限");
            return ;
        }
        if (mCurrentOriginalImage != null) {
            final String imagePath = TUIKitConstants.IMAGE_DOWNLOAD_DIR + mCurrentOriginalImage.getUUID() + "_" + mCurrentOriginalImage.getType();
            final File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                mCurrentOriginalImage.downloadImage(imagePath, new V2TIMDownloadCallback() {
                    @Override
                    public void onProgress(V2TIMElem.V2ProgressInfo progressInfo) {

                    }

                    @Override
                    public void onSuccess() {
                        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Log.d("logUtil","图片存储路径 = " + path);
                                mPhotoView.setImageURI(FileUtil.getUriFromPath(imagePath));
                                mViewOriginalBtn.setText(getString(R.string.completed));
                                mViewOriginalBtn.setOnClickListener(null);
                                mViewOriginalBtn.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent();
                                intent.setAction(BROADCAST_DOWNLOAD_COMPLETED_ACTION);
                                intent.putExtra(DOWNLOAD_ORIGIN_IMAGE_PATH, imagePath);
                                LocalBroadcastManager.getInstance(PhotoViewActivity.this).sendBroadcast(intent);
                                downloadPic(imagePath);
                            }
                        });
                    }

                    @Override
                    public void onError(int code, String desc) {

                    }
                });
            } else {
                downloadPic(imagePath);
            }
        }
    }

    void downloadPic(String imagePath) {
        Uri uriFromPath = FileUtil.getUriFromPath(imagePath);
        String imageFileName = mCurrentOriginalImage.getUUID();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "_shengmo");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            Log.d("logutil","imageName = " + imageFileName);

            if (!imageFileName.endsWith(".jpg") && imageFileName.endsWith(".gif")) {
                imageFileName = imageFileName + ".jpg";
            }
//            else if (!imageFileName.endsWith(".png")) {
//                imageFileName = imageFileName + ".jpg";
//            }
            final File imageFile = new File(storageDir, imageFileName);
            if (imageFile.exists()) {
                ToastUtil.toastShortMessage("图片已保存 路径:" + imageFile.getAbsolutePath());
            } else {
                if (PhotoViewActivity.this == null || PhotoViewActivity.this.isFinishing()) {
                    return;
                }
                Glide.with(PhotoViewActivity.this).downloadOnly().load(uriFromPath).into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        FileUtil.copy(resource, imageFile);
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(imageFile);
                        intent.setData(uri);
                        sendBroadcast(intent);
                        ToastUtil.toastShortMessage("图片保存成功，路径:" + imageFile.getAbsolutePath());
                        Log.d("logutil","路径 = " + imageFile.getAbsolutePath());
                    }
                });
            }
        }
    }
}
