package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.CommentDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoader {

    public static void loadImageWithoutError(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(true).centerCrop().into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().error(R.mipmap.default_error)).centerCrop().into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView,int defaultImg) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(defaultImg).error(R.mipmap.default_error)).centerCrop().into(imageView);
    }

    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(R.mipmap.default_error).error(R.mipmap.default_error).transform(new GlideCircleTransform())).into(imageView);
    }

    public static void loadCircleImage(Context context, String url, ImageView imageView,int defaultImg) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(defaultImg).error(defaultImg).transform(new GlideCircleTransform())).into(imageView);
    }

    public static void loadCircleBorderImage(Context context, String url, ImageView imageView,int defaultImg) {
        Glide.with(context)
                .load(url)
                .skipMemoryCache(true)
                .apply(new RequestOptions()
                        .placeholder(defaultImg)
                        .error(defaultImg)
                        .transform(new GlideCircleTransformWithBorder(context,3, Color.parseColor("#CFBAFA"))))
                .into(imageView);
    }


    public static void loadRoundImage(Context context, String url, ImageView imageView,int defaultImg) {
        RequestOptions requestOptions = new RequestOptions();
        if (defaultImg != -1) {
            requestOptions.placeholder(defaultImg);
            requestOptions.error(defaultImg);
        }
        requestOptions.transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(20)));
        Glide.with(context).load(Uri.parse((String) url)).skipMemoryCache(true).apply(requestOptions).into(imageView);
    }

    public static void loadImageWithScale(Context context, String url, ImageView imageView) {

    }

    //图片缩放比例
    private static final float BITMAP_SCALE = 0.4f;

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static void loadImage(Fragment context, String url, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(R.mipmap.default_error).error(R.mipmap.default_error)).centerCrop().into(imageView);
    }

    public static void loadImage(Fragment context, String url, ImageView imageView,int defaultImg) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(defaultImg).error(R.mipmap.default_error)).centerCrop().into(imageView);
    }

    public static void loadCircleImage(Fragment context, String url, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(R.mipmap.default_error).error(R.mipmap.default_error).transform(new GlideCircleTransform())).into(imageView);
    }

    public static void loadCircleImage(Fragment context, String url, ImageView imageView,int defaultImg) {
        Glide.with(context).load(url).skipMemoryCache(true).apply(new RequestOptions().placeholder(defaultImg).error(defaultImg).transform(new GlideCircleTransform())).into(imageView);
    }

    public static void loadRoundImage(Fragment context, String url, ImageView imageView,int defaultImg) {
        RequestOptions requestOptions = new RequestOptions();
        if (defaultImg != -1) {
            requestOptions.placeholder(defaultImg);
            requestOptions.error(defaultImg);
        }
        requestOptions.transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(20)));
        Glide.with(context).load(Uri.parse((String) url)).skipMemoryCache(true).apply(requestOptions).into(imageView);
    }

    public static void loadImageWithoutHolder(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(true).error(R.mipmap.default_error).centerCrop().into(imageView);
    }

    public static void loadCircleImageWithoutHolder(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).error(R.mipmap.default_error).centerCrop().into(imageView);
    }
}
