package com.aiwujie.shengmo.utils.glideprogress;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by Alexey on 06.12.15.
 */
@com.bumptech.glide.annotation.GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context,builder);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    //    @Override
//    public void registerComponents(Context context, Glide glide) {
//        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(GlideProgressListener.getGlideOkHttpClient()));
//    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(GlideProgressListener.getGlideOkHttpClient()));
        super.registerComponents(context,glide,registry);
    }
}
