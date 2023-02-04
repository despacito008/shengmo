package com.aiwujie.shengmo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.utils.AppIsInstallUtils;
import com.aiwujie.shengmo.utils.QqShareManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WechatShareManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;

import java.io.File;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class CopyLinkPop extends BasePopupWindow {

    @BindView(R.id.view_pop_share_pic_bottom)
    View viewPopSharePicBottom;
    @BindView(R.id.tv_pop_share_pic_bottom_tips)
    TextView tvPopSharePicBottomTips;
    @BindView(R.id.ll_pop_share_wechat)
    LinearLayout llPopShareWechat;
    @BindView(R.id.ll_pop_share_wechat_circle)
    LinearLayout llPopShareWechatCircle;
    @BindView(R.id.ll_pop_share_qq)
    LinearLayout llPopShareQq;
    @BindView(R.id.ll_pop_share_qq_zone)
    LinearLayout llPopShareQqZone;
    @BindView(R.id.ll_pop_share_download)
    LinearLayout llPopShareDownload;
    @BindView(R.id.ll_pop_share_way)
    LinearLayout llPopShareWay;
    Context context;
    String content = "";
    @BindView(R.id.tv_pop_share_pic_cancel)
    TextView tvPopSharePicCancel;

    public CopyLinkPop(Context context, String content) {
        super(context);
        this.context = context;
        this.content = content;
        initView();
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_share_copy_link);
        ButterKnife.bind(this, view);
        return view;
    }

    void initView() {
        llPopShareWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppIsInstallUtils.isWeChatAvailable(context)) {
                    Intent lan = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(lan.getComponent());
                    context.startActivity(intent);
                    ToastUtil.showLong(context,"邀请文案已复制 请粘贴给好友");
                }else{
                    ToastUtil.show(context,"您没有安装微信");
                }
            }
        });

        llPopShareWechatCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppIsInstallUtils.isWeChatAvailable(context)) {
                    Intent lan = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(lan.getComponent());
                    context.startActivity(intent);
                    ToastUtil.showLong(context,"邀请文案已复制 请粘贴给好友");
                }else{
                    ToastUtil.show(context,"您没有安装微信");
                }
            }
        });

        llPopShareQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppIsInstallUtils.isQQClientAvailable(context)) {
                    Intent lan = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(lan.getComponent());
                    context.startActivity(intent);
                    ToastUtil.showLong(context,"邀请文案已复制 请粘贴给好友");
                } else {
                    ToastUtil.show(context,"您没有安装qq");
                }
            }
        });

        llPopShareQqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppIsInstallUtils.isQQClientAvailable(context)) {
                    Intent lan = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(lan.getComponent());
                    context.startActivity(intent);
                    ToastUtil.showLong(context,"邀请文案已复制 请粘贴给好友");
                } else {
                    ToastUtil.show(context,"您没有安装qq");
                }
            }
        });

        llPopShareDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppIsInstallUtils.isWeiBoAvailable(context)) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("sinaweibo://sendweibo?content="+ URLEncoder.encode(content)));
                    context.startActivity(intent);
                    ToastUtil.showLong(context,"邀请文案已复制 请粘贴给好友");
                }else{
                    ToastUtil.show(context,"您没有安装微博");
                }
            }
        });

        tvPopSharePicCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
