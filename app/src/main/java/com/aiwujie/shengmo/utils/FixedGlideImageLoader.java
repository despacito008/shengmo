package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import cn.bingoogolapple.baseadapter.BGABaseAdapterUtil;
import cn.bingoogolapple.photopicker.imageloader.BGAImageLoader;


public class FixedGlideImageLoader extends BGAImageLoader {


    @Override
    public void display(final ImageView imageView, String path, int loadingResId, int failResId, int width, int height, final DisplayDelegate delegate) {
        if(imageView == null) {
            return;
        }
        final String finalPath = getPath(path);
        Activity activity = getActivity(imageView);
        RequestOptions options = new RequestOptions()
                .placeholder(loadingResId)
                .error(failResId)
                .override(width,height)
                .dontAnimate();

        Glide.with(activity)
                .load(finalPath)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if(delegate != null) {
                            delegate.onSuccess(imageView, finalPath);
                        }
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public void download(String path, final DownloadDelegate delegate) {
        final String finalPath = getPath(path);
        Glide.with(BGABaseAdapterUtil.getApp())
                .asBitmap()
                .load(finalPath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if(delegate != null) {
                            delegate.onSuccess(finalPath, resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if(delegate != null) {
                            delegate.onFailed(finalPath);
                        }
                    }
                });

    }

    @Override
    public void pause(Activity activity) {
        if(activity != null) {
            Glide.with(activity).pauseRequests();
        }
    }

    @Override
    public void resume(Activity activity) {
        if(activity != null) {
            Glide.with(activity).resumeRequestsRecursive();
        }
    }
}
