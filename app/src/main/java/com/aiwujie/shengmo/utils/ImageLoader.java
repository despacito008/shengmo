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

    //??????????????????
    private static final float BITMAP_SCALE = 0.4f;

    /**
     * ???????????????????????????
     *
     * @param context ???????????????
     * @param image   ?????????????????????
     * @return ????????????????????????
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // ??????????????????????????????
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // ?????????????????????????????????????????????
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // ????????????????????????????????????
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // ??????RenderScript????????????
        RenderScript rs = RenderScript.create(context);
        // ???????????????????????????RenderScript???????????????
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // ??????RenderScript???????????????VM???????????????,??????????????????Allocation?????????????????????????????????
        // ??????Allocation????????????????????????????????????,????????????copyTo()?????????????????????
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // ???????????????????????????, 25f??????????????????
        blurScript.setRadius(blurRadius);
        // ??????blurScript?????????????????????
        blurScript.setInput(tmpIn);
        // ???????????????????????????????????????
        blurScript.forEach(tmpOut);

        // ??????????????????Allocation???
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
