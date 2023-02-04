package com.aiwujie.shengmo.tim.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.FallingViewActivity;
import com.aiwujie.shengmo.activity.Shan_yaActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.ui.activity.ShanYaActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.tim.bean.FlashMessageBean;
import com.aiwujie.shengmo.tim.bean.GiftMessageBean;
import com.aiwujie.shengmo.tim.bean.SystemTipsMessageBean;
import com.aiwujie.shengmo.tim.utils.DemoLog;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class FlashUIController {

    public static void onDraw(ICustomMessageViewGroup parent, final String data, final MessageInfo info, final int position) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_custom_flash, null, false);
        parent.addMessageContentView(view);
        final FlashMessageBean flashMessageBean = GsonUtil.GsonToBean(data, FlashMessageBean.class);
        final ImageView ivMain = view.findViewById(R.id.iv_message_tim_flash);
        if(!isShowed(flashMessageBean.getContentDict().getImageUrl())) {
            Glide.with(MyApp.getInstance()).load(flashMessageBean.getContentDict().getImageUrl()).apply(RequestOptions.bitmapTransform(new BlurTransformation(100))).into(ivMain);
        }
//        Glide.with(context).asBitmap().load(flashMessageBean.getContentDict().getImageUrl()).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                ivMain.setImageBitmap(ImageLoader.blurBitmap(context,resource,25));
//            }
//        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowed(flashMessageBean.getContentDict().getImageUrl())) {
//                    Intent intent = new Intent(MyApp.getInstance(), Shan_yaActivity.class);
                    Intent intent = new Intent(MyApp.getInstance(), ShanYaActivity.class);
                    intent.putExtra("shanurl",flashMessageBean.getContentDict().getImageUrl());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApp.getInstance().startActivity(intent);
                }
            }
        });
        final MessageLayout.OnItemLongClickListener onItemLongClickListener = ((MessageBaseHolder) parent).getOnItemClickListener();

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (onItemLongClickListener != null){
                    onItemLongClickListener.onMessageLongClick(v, position, info);
                }

//
//                if (info.isSelf()) {
//                    if (FinishActivityManager.getManager().currentActivity() instanceof Activity) {
//                        new AlertDialog.Builder(FinishActivityManager.getManager().currentActivity()).setMessage("是否撤回闪照？")
//                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                                V2TIMManager.getMessageManager().revokeMessage(info.getTimMessage(), new V2TIMCallback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(int code, String desc) {
//                                        ToastUtil.toastShortMessage("撤回失败" + code + desc);
//                                    }
//                                });
//                            }
//                        }).create().show();
//                    }
//                } else {
//                }
                return true;
            }
        });

    }
    //判断闪照是否查看过
     static boolean isShowed(String url) {
         List<String> flashImageCacheList = SharedPreferencesUtils.getDataList("flash_img_"+MyApp.uid);
         if(flashImageCacheList == null) {
             return false;
         } else {
             return flashImageCacheList.contains(url);
         }
    }

    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(6, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
