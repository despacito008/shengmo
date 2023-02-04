package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 290243232 on 2017/2/6.
 */
public class GlideImgManager {
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
//        if(isDestroy((Activity)context)) {
//            return;
//        }
        //原生 API
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(emptyImg);
        requestOptions.error(erroImg);
//        requestOptions.transform(new BlurTransformation(75));
        Glide.with(context).load(url).apply(requestOptions).centerCrop().into(iv);
    }

    public static void glideBlurLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
//        if(isDestroy((Activity)context)) {
//            return;
//        }
        //原生 API
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(emptyImg)
                .error(erroImg)
                .transform(new BlurTransformation(80));

//        requestOptions.transform(new BlurTransformation(75));
        Glide.with(context).load(url).apply(requestOptions).into(iv);
    }


    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
//        if(isDestroy(context)) {
//            return;
//        }
        if (0 == tag) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(emptyImg);
            requestOptions.error(erroImg);
            requestOptions.transform(new GlideCircleTransform());
            Glide.with(context).load(Uri.parse((String) url)) .apply(requestOptions).into(iv);
        } else if (1 == tag) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(emptyImg);
            requestOptions.error(erroImg);
            requestOptions.transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(20)));
            Glide.with(context).load(Uri.parse((String) url)).apply(requestOptions).into(iv);
        } else if (2 == tag) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(erroImg);
            requestOptions.transform(new GlideCircleTransform());
            Glide.with(context).load(Uri.parse((String) url)).apply(requestOptions).into(iv);
        }
    }

    public static void load(Activity activity, String url, ImageView imageView) {
        if (isDestroy(activity)) {
            return;
        }
        Glide.with(activity).load(url).into(imageView);
    }

    /**
     * 判断Activity是否Destroy
     *
     * @param
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }


    public void showLivingGif(Context context, final ImageView imageView) {
//        Glide.with(context)
//                .load(R.drawable.ic_user_living)
//                .into(new SimpleTarget<GifDrawable>() {
//                          @Override
//                          public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
//                              try {
//                                  Field field = GifDecoder.class.getDeclaredField("header");
//                                  field.setAccessible(true);
//                                  GifHeader header = (GifHeader) field.get(resource.getBuffer());
//                                  Field field2 = GifHeader.class.getDeclaredField("frames");
//                                  field2.setAccessible(true);
//                                  List frames = (List) field2.get(header);
//                                  if (frames.size() > 0) {
//                                      Field delay = frames.get(0).getClass().getDeclaredField("delay");
//                                      delay.setAccessible(true);
//                                      for (Object frame : frames) {
//                                          delay.set(frame, 20);//这里直接给修改成了20
//                                      }
//                                  }
//                              } catch (NoSuchFieldException | IllegalAccessException e) {
//                                  e.printStackTrace();
//                              }
//                              imageView.setImageDrawable(resource);
//                              resource.setLoopCount(Integer.MAX_VALUE);
//                              resource.start();
//                          }
//                      }
//
//                );
    }

    public static void showNormalCircleIcon(Context context, String url, ImageView imageView) {
        GlideImgManager.glideLoader(context, url, R.mipmap.morentouxiang, R.mipmap.morentouxiang, imageView, 0);
    }
}
