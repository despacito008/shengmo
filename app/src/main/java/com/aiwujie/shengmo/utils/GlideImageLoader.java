package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 290243232 on 2017/10/24.
 */

public class GlideImageLoader extends ImageLoader {
    private boolean isBlur=false;

    public GlideImageLoader() {
    }

    public GlideImageLoader(boolean isBlur) {
        this.isBlur = isBlur;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        if(isBlur){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.default_error);
//                requestOptions.error(R.drawable.rc_image_error);
            requestOptions.transform(new BlurTransformation(75));
            Glide.with(context).load(Uri.parse((String) path)).apply(requestOptions)
                    .into(imageView);
        }else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.default_error);
//                requestOptions.error(R.drawable.rc_image_error);
//            requestOptions.transform(new BlurTransformation(75));
            Glide.with(context).load(Uri.parse((String) path)).apply(requestOptions).into(imageView);
            Uri uri = Uri.parse((String) path);
            imageView.setImageURI(uri);
        }
    }
}
