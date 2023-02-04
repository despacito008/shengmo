package com.aiwujie.shengmo.tim.helper;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.bean.DynamicMessageBean;
import com.aiwujie.shengmo.tim.bean.LiveAnchorMessageBean;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class LiveAnchorUIController {

    public static void onDraw(ICustomMessageViewGroup parent, final String data, final MessageInfo info, final int position) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_live_anchor, null, false);
        parent.addMessageContentView(view);
        final LiveAnchorMessageBean anchorMessageBean = GsonUtil.GsonToBean(data, LiveAnchorMessageBean.class);
        TextView tvTitle = view.findViewById(R.id.tv_tim_live_anchor);
        ImageView ivCover = view.findViewById(R.id.iv_tim_live_anchor);
        final ImageView ivLiving = view.findViewById(R.id.iv_tim_live_living);
        final TextView tvLiving = view.findViewById(R.id.tv_tim_live_living);
        Glide.with(MyApp.getInstance()).load(anchorMessageBean.getContentDict().getLivePoster()).into(ivCover);
       // Glide.with(MyApp.getInstance()).load(anchorMessageBean.getContentDict().getLivePoster()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(ivCover);
       // Glide.with(MyApp.getInstance()).load(anchorMessageBean.getContentDict().getLivePoster()).apply(RequestOptions.bitmapTransform(new BlurTransformation(100))).into(ivCover);

        tvTitle.setText(anchorMessageBean.getContentDict().getLiveTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApp.getInstance(), UserInfoActivity.class);
                intent.putExtra("uid", anchorMessageBean.getContentDict().getAnchorId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstance().startActivity(intent);
            }
        });

        final MessageLayout.OnItemLongClickListener onItemLongClickListener = ((MessageBaseHolder) parent).getOnItemClickListener();

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onMessageLongClick(v, position, info);
                }
                return true;
            }
        });

        boolean isAnchorLiving = false;
        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(anchorMessageBean.getContentDict().getAnchorId()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                Map<String, byte[]> customInfo = v2TIMUserFullInfos.get(0).getCustomInfo();
                byte[] live_role = customInfo.get("LiveRole");
                Log.d("logUtil","liveRole == null" + (live_role == null));
                if (live_role != null) {
                    String liveRole = new String(live_role);
                    //Log.d("logUtil","id = " + v2TIMUserFullInfos.get(0).getUserID() +",role = " + liveRole);
                    switch (liveRole) {
                        case "":
                        case "0":
                        case "1":
                            tvLiving.setText("已下播");
                            ivLiving.setVisibility(View.GONE);
                            break;
                        case "2":
                            tvLiving.setText("直播中");
                            ivLiving.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }



}
