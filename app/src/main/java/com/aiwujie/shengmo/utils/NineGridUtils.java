package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.DynamicDetailData;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.SinglePictureParameter;
import com.aiwujie.shengmo.customview.NineGridViewWrapper;
import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 290243232 on 2018/1/10.
 */

public class NineGridUtils {
    public static void loadPic(final Context context, final Map<Integer,SinglePictureParameter> map, final int position, final DynamicListData.DataBean data, final ArrayList<String> urls, final NineGridView nineGridView, final NineGridViewWrapper imagePic) {
        nineGridView.setVisibility(View.GONE);
        imagePic.setImageResource(R.mipmap.default_error);
        imagePic.setVisibility(View.VISIBLE);
        if (urls.size() == 1) {
            if(map.containsKey(position)){
                //处理视频封面合成图片可以在这设置，在前页map中添加数据
                int height=map.get(position).getHeight();
                int width=map.get(position).getWidth();
                imagePic.setLayoutParams(setPara(imagePic,width,height));
                imagePic.setImageBitmap(map.get(position).getBitmap());
                imagePic.setVisibility(View.VISIBLE);

            }else {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop();
                requestOptions.transform(new RoundedCorners(15));
                //获取图片真正的宽高
                Glide.with(context)
                        .asBitmap()//制Glide返回一个Bitmap对象
                        .load(data.getSypic().get(0))
                        .apply(requestOptions)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                int i = dp2px(300);
                                if (height>i){
                                    height=i;
                                }
                                imagePic.setLayoutParams(setPara(imagePic,width,height));
                                imagePic.setImageBitmap(bitmap);
                                map.put(position,new SinglePictureParameter(height,width,bitmap));
                            }

                        });
            }
            final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            final ImageInfo info = new ImageInfo();
            info.setThumbnailUrl(data.getSypic().get(0));
            if (data.getSypic().size() != 0) {
                info.setBigImageUrl(data.getSypic().get(0));
            } else {
                info.setBigImageUrl(urls.get(0));
            }
            imageInfo.add(info);
            imagePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.imageViewWidth = imagePic.getWidth();
                    info.imageViewHeight = imagePic.getHeight();
                    int[] points = new int[2];
                    imagePic.getLocationInWindow(points);
                    info.imageViewX = points[0];
                    info.imageViewY = points[1] - getStatusHeight(context);
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                }
            });
        } else {
            imagePic.setVisibility(View.GONE);
            nineGridView.setVisibility(View.VISIBLE);
            if (urls.size() != 0) {
                final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(urls.get(i));
                    if (data.getSypic().size() != 0) {
                        info.setBigImageUrl(data.getSypic().get(i));
                    } else {
                        info.setBigImageUrl(urls.get(i));
                    }
                    imageInfo.add(info);
                }
                nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
//            nineGridView.setAdapter(new OwnerDisplayAdapter(context, imageInfo));
            } else {
                nineGridView.setVisibility(View.GONE);
            }
        }
    }

    public static void loadPics(final Context context, final DynamicDetailData.DataBean data, final ArrayList<String> urls, final NineGridView nineGridView, final NineGridViewWrapper imagePic){
        if(urls.size()==1){
            imagePic.setVisibility(View.VISIBLE);
            nineGridView.setVisibility(View.GONE);
            //获取图片真正的宽高
            Glide.with(context)
                    .asBitmap()//制Glide返回一个Bitmap对象
                    .load(data.getSypic().get(0))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            imagePic.setLayoutParams(setPara(imagePic,width,height));
                            imagePic.setImageBitmap(bitmap);
                            final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                            final ImageInfo info = new ImageInfo();
                            info.setThumbnailUrl(data.getSypic().get(0));
                            if (data.getSypic().size() != 0) {
                                info.setBigImageUrl(data.getSypic().get(0));
                            } else {
                                info.setBigImageUrl(urls.get(0));
                            }
                            imageInfo.add(info);
                            imagePic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    info.imageViewWidth = imagePic.getWidth();
                                    info.imageViewHeight = imagePic.getHeight();
                                    int[] points = new int[2];
                                    imagePic.getLocationInWindow(points);
                                    info.imageViewX = points[0];
                                    info.imageViewY = points[1] - getStatusHeight(context);
                                    Intent intent = new Intent(context, ImagePreviewActivity.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(0, 0);
                                }
                            });
                        }

                    });
        }else {
            imagePic.setVisibility(View.GONE);
            if (urls.size() != 0) {
                nineGridView.setVisibility(View.VISIBLE);
                final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(urls.get(i));
                    if (data.getSypic().size() != 0) {
                        info.setBigImageUrl(data.getSypic().get(i));
                    } else {
                        info.setBigImageUrl(urls.get(i));
                    }
                    imageInfo.add(info);
                }
                nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
//            nineGridView.setAdapter(new OwnerDisplayAdapter(context, imageInfo));
            } else {
                nineGridView.setVisibility(View.GONE);
            }
        }
    }


    public static void loadPics(final Context context, final ArrayList<String> spics, final ArrayList<String> urls, final NineGridView nineGridView, final ImageView imagePic){
        if(urls.size()==1){
            imagePic.setVisibility(View.VISIBLE);
            nineGridView.setVisibility(View.GONE);
            //获取图片真正的宽高
            Glide.with(context)
                    .asBitmap()//制Glide返回一个Bitmap对象
                    .load(spics.get(0))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            imagePic.setLayoutParams(setPara(imagePic,width,height));
                            imagePic.setImageBitmap(bitmap);
                            final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                            final ImageInfo info = new ImageInfo();
                            info.setThumbnailUrl(spics.get(0));
                            if (spics.size() != 0) {
                                info.setBigImageUrl(spics.get(0));
                            } else {
                                info.setBigImageUrl(urls.get(0));
                            }
                            imageInfo.add(info);
                            imagePic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    info.imageViewWidth = imagePic.getWidth();
                                    info.imageViewHeight = imagePic.getHeight();
                                    int[] points = new int[2];
                                    imagePic.getLocationInWindow(points);
                                    info.imageViewX = points[0];
                                    info.imageViewY = points[1] - getStatusHeight(context);
                                    Intent intent = new Intent(context, ImagePreviewActivity.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(0, 0);
                                }
                            });
                        }

                    });
        }else {
            imagePic.setVisibility(View.GONE);
            if (urls.size() != 0) {
                nineGridView.setVisibility(View.VISIBLE);
                final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(urls.get(i));
                    if (spics.size() != 0) {
                        info.setBigImageUrl(spics.get(i));
                    } else {
                        info.setBigImageUrl(urls.get(i));
                    }
                    imageInfo.add(info);
                }
                nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
//            nineGridView.setAdapter(new OwnerDisplayAdapter(context, imageInfo));
            } else {
                nineGridView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置图片比例
     * @param imagePic
     * @param width
     * @param height
     * @return
     */
    private static ViewGroup.LayoutParams setPara(NineGridViewWrapper imagePic, int width, int height) {
        ViewGroup.LayoutParams para = imagePic.getLayoutParams();
        if (width > 1500 || height > 1500) {
            para.height = height * 2 / 5;
            para.width = width * 2 / 5;
        } else {
            if(width>1000||height>1000){
                para.height = height * 3 / 5;
                para.width = width * 3 / 5;
            }else {
                if(width>500||height>500){
                    para.height = height * 3 / 4;
                    para.width = width * 3 / 4;
                }else {
                    if (width < 200 || height < 200) {
                        para.height = height + height / 3;
                        para.width = width + width / 3;
                    } else {
                        para.height = height;
                        para.width = width;
                    }
                }
            }
        }
        return para;
    }


    /**
     * 设置图片比例
     * @param imagePic
     * @param width
     * @param height
     * @return
     */
    public static ViewGroup.LayoutParams setPara(View imagePic, int width, int height) {
        ViewGroup.LayoutParams para = imagePic.getLayoutParams();
        if (width > 1500 || height > 1500) {
            para.height = height * 2 / 5;
            para.width = width * 2 / 5;
        } else {
            if(width>1000||height>1000){
                para.height = height * 3 / 5;
                para.width = width * 3 / 5;
            }else {
                if(width>500||height>500){
                    para.height = height * 3 / 4;
                    para.width = width * 3 / 4;
                }else {
                    if (width < 200 || height < 200) {
                        para.height = height + height / 3;
                        para.width = width + width / 3;
                    } else {
                        para.height = height;
                        para.width = width;
                    }
                }
            }
        }
        return para;
    }


    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static int dp2px(float dpValue){
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
