package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by 290243232 on 2017/2/6.
 * 将图片转化为圆形
 */
public class GlideCircleTransform extends BitmapTransformation {
//    Context context;
//    public GlideCircleTransform(Context context) {
//        this.context = context;
//    }
    public GlideCircleTransform() {

    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

//    @Override public String getId() {
//        return getClass().getName();
//    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}

//public class GlideCircleTransform extends CropTransformation {
//
//
//    public GlideCircleTransform(int width, int height) {
//        super(width, height);
//    }
//
//    public GlideCircleTransform(int width, int height, CropType cropType) {
//        super(width, height, cropType);
//    }
//
//    @Override
//    protected Bitmap transform(Context context, BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        return circleCrop(pool, toTransform);
//    }
//
//    //    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
////        return circleCrop(pool, toTransform);
////    }
//
//    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
//        if (source == null) return null;
//
//        int size = Math.min(source.getWidth(), source.getHeight());
//        int x = (source.getWidth() - size) / 2;
//        int y = (source.getHeight() - size) / 2;
//
//        // TODO this could be acquired from the pool too
//        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
//
//        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
//        if (result == null) {
//            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//        }
//
//        Canvas canvas = new Canvas(result);
//        Paint paint = new Paint();
//        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//        paint.setAntiAlias(true);
//        float r = size / 2f;
//        canvas.drawCircle(r, r, r, paint);
//        return result;
//    }
//
////    @Override public String getId() {
////        return getClass().getName();
////    }
//
//
//}
