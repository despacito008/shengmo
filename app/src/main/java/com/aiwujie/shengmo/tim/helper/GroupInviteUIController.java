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
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.tim.bean.DynamicMessageBean;
import com.aiwujie.shengmo.tim.bean.GroupInviteMessageBean;
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

import java.util.List;

public class GroupInviteUIController {

    public static void onDraw(ICustomMessageViewGroup parent, final String data, final MessageInfo info, final int position) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_group_invite, null, false);
        parent.addMessageContentView(view);
        final GroupInviteMessageBean groupInviteMessageBean = GsonUtil.GsonToBean(data, GroupInviteMessageBean.class);
        TextView tvTitle = view.findViewById(R.id.tv_message_tim_group_invite_title);
        TextView tvContent = view.findViewById(R.id.tv_message_tim_group_invite_content);
        ImageView ivCover = view.findViewById(R.id.iv_message_tim_group_invite);

        tvTitle.setText(groupInviteMessageBean.getContentDict().getTitle());
        tvContent.setText(groupInviteMessageBean.getContentDict().getContent());
        Glide.with(MyApp.getInstance()).load(groupInviteMessageBean.getContentDict().getImageUri()).centerCrop().error(R.mipmap.applogo).into(ivCover);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MyApp.getInstance(), GroupInfoActivity.class);
//                intent.putExtra("groupId",groupInviteMessageBean.getContentDict().getGid());
//                intent.putExtra(IntentKey.GROUP_INVITE,groupInviteMessageBean.getContentDict().getGroupState());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//                MyApp.getInstance().startActivity(intent);
                GroupDetailActivity.Companion.start(MyApp.getInstance(),
                        groupInviteMessageBean.getContentDict().getGid(),
                        groupInviteMessageBean.getContentDict().getGroupState(),false);
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
    //判断是否查看过
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
