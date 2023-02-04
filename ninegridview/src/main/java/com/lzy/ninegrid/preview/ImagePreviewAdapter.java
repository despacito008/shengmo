package com.lzy.ninegrid.preview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.ninegrid.DragLayout;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.R;
import com.lzy.ninegrid.photoview.OnOutsidePhotoTapListener;
import com.lzy.ninegrid.photoview.OnPhotoTapListener;
import com.lzy.ninegrid.photoview.PhotoView;

import java.util.List;


/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePreviewAdapter extends PagerAdapter implements OnPhotoTapListener, OnOutsidePhotoTapListener {

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;

    public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (GestureImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);
        currentView = view;
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
        final GestureImageView imageView = (GestureImageView) view.findViewById(R.id.pv);
        final DragLayout dragLayout = (DragLayout) view.findViewById(R.id.dl_img);
        final ImageInfo info = this.imageInfo.get(position);
        imageView.getController().getSettings()
                .setMaxZoom(2f)
                .setDoubleTapZoom(-1f) // Falls back to max zoom level
                .setPanEnabled(true)
                .setZoomEnabled(true)
                .setDoubleTapEnabled(true)
                .setRotationEnabled(false)
                .setRestrictRotation(false)
                .setOverscrollDistance(0f, 0f)
                .setOverzoomFactor(2f)
                .setFillViewport(false)
                .setFitMethod(Settings.Fit.INSIDE)
                .setGravity(Gravity.CENTER);
        //imageView.setOnPhotoTapListener(this);
        //imageView.setOnOutsidePhotoTapListener(this);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertView(null, null, "取消", null, new String[]{"保存"},
                        context, AlertView.Style.ActionSheet, ((ImagePreviewActivity) context)).show();
                return true;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImagePreviewActivity)context).finishActivityAnim();
            }
        });
        //showExcessPic(info, imageView);

        //如果需要加载的loading,需要自己改写,不能使用这个方法
        NineGridView.getImageLoader().onDisplayImage(context, imageView, info.bigImageUrl);

//        Glide.with(context)
//                .asBitmap()
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .format(DecodeFormat.PREFER_ARGB_8888)//设置图片解码格式
//                .load(info.bigImageUrl)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        int height = resource.getHeight();
//                        int width = resource.getWidth();
//                        if (height > 5000) {
//                            int newHeight = 5000;
//                            int newWidth = (int) (newHeight * (width * 1f / height));
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                resource.setWidth(newWidth);
//                                resource.setHeight(newHeight);
//                            }
//                        }
//                        imageView.setImageBitmap(resource);
////                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivDynamicDetailPic.getLayoutParams();
////                        layoutParams.width = getScreenWidth(getApplicationContext()) - DensityUtil.dip2px(NewDynamicDetailActivity.this, 30);
////                        layoutParams.height = layoutParams.width * height / width;
////                        ivDynamicDetailPic.setLayoutParams(layoutParams);
////                        ivDynamicDetailPic.setImageBitmap(resource);
////                        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ivDynamicDetailPicRound.getLayoutParams();
////                        layoutParams2.width = getScreenWidth(getApplicationContext()) - DensityUtil.dip2px(NewDynamicDetailActivity.this, 30);
////                        layoutParams2.height = layoutParams.width * height / width;
////                        ivDynamicDetailPicRound.setLayoutParams(layoutParams2);
//                    }
//                });

        //Glide.with(context).load(info.bigImageUrl).into(imageView);

        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onDragFinished() {
                dragLayout.setBackgroundColor(Color.TRANSPARENT);
                ((ImagePreviewActivity) context).finishActivityAnim();
            }
        });

//        Glide.with(context).load(info.bigImageUrl)//
//                .placeholder(R.drawable.ic_default_image)//
//                .error(R.drawable.ic_default_image)//
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        pb.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        pb.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(imageView);

        container.addView(view);
        return view;
    }

    /** 展示过度图片 */
    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null) cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_color);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /** 单击屏幕关闭 */
    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((ImagePreviewActivity) context).finishActivityAnim();
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        ((ImagePreviewActivity) context).finishActivityAnim();
    }
}