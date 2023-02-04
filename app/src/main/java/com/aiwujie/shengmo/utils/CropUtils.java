package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.aiwujie.shengmo.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

/**
 * Created by 290243232 on 2017/4/5.
 */

public class CropUtils {
    public static void startUCrop(Activity activity, Uri sourceUri,
                                    int requestCode, float aspectRatioX, float aspectRatioY ,int maxWidth ,int maxHeight) {
//        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        Log.i("ucropuriuri", "startUCrop: "+sourceUri+","+destinationUri);
        Log.i("ucropuriuri", "startUCrop: "+cameraScalePath);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
//        options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.purple_main));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.purple_main));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);
        //设置裁剪的图片质量，取值0-100
//        options.setCompressionQuality(100);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        uCrop.withMaxResultSize(maxWidth, maxHeight);
        //跳转裁剪页面
        uCrop.start(activity, requestCode);
//        return cameraScalePath;
    }
}
