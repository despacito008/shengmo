package com.aiwujie.shengmo.tim.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.Shan_yaActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.bean.DynamicMessageBean;
import com.aiwujie.shengmo.tim.bean.FlashMessageBean;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

public class DynamicUIController {

    public static void onDraw(ICustomMessageViewGroup parent, final String data,final MessageInfo info, final int position) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_dynamic, null, false);
        parent.addMessageContentView(view);
        final DynamicMessageBean dynamicMessageBean = GsonUtil.GsonToBean(data, DynamicMessageBean.class);
        TextView tvTitle = view.findViewById(R.id.tv_message_tim_dynamic_title);
        TextView tvContent = view.findViewById(R.id.tv_message_tim_dynamic_content);
        ImageView ivCover = view.findViewById(R.id.iv_message_tim_dynamic);

        tvTitle.setText(dynamicMessageBean.getContentDict().getContTitle());
        tvContent.setText(dynamicMessageBean.getContentDict().getContent());
        if(dynamicMessageBean.getContentDict().getShareType() == 1) { //分享动态
            Glide.with(MyApp.getInstance()).load(dynamicMessageBean.getContentDict().getIcon()).centerCrop().error(R.mipmap.applogo).into(ivCover);
        } else {
            Glide.with(MyApp.getInstance()).load(dynamicMessageBean.getContentDict().getIcon()).circleCrop().error(R.mipmap.applogo).into(ivCover);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dynamicMessageBean.getContentDict().getShareType() == 1) { //分享动态
                    Intent intent = new Intent(MyApp.getInstance(), DynamicDetailActivity.class);
                    intent.putExtra("uid", dynamicMessageBean.getContentDict().getUserId());
                    intent.putExtra("did", dynamicMessageBean.getContentDict().getNewid());
                    intent.putExtra("pos", 1);
                    intent.putExtra("showwhat", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApp.getInstance().startActivity(intent);
                } else { //分享好友
                    Intent intent = new Intent(MyApp.getInstance(), UserInfoActivity.class);
                    intent.putExtra("uid", dynamicMessageBean.getContentDict().getNewid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApp.getInstance().startActivity(intent);
                }
            }
        });

        final MessageLayout.OnItemLongClickListener onItemLongClickListener = ((MessageBaseHolder) parent).getOnItemClickListener();

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onMessageLongClick(v,position,info);
                }
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
