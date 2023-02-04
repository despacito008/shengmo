package com.aiwujie.shengmo.view;

import android.content.Context;
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
import com.aiwujie.shengmo.utils.WeiboShareManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class SharePicPop extends BasePopupWindow {
    @BindView(R.id.iv_pop_share_pic)
    ImageView ivPopSharePic;
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
    @BindView(R.id.ll_pop_share_weibo)
    LinearLayout llPopShareWeibo;
    @BindView(R.id.ll_pop_share_way)
    LinearLayout llPopShareWay;
    @BindView(R.id.tv_pop_share_pic_save)
    TextView tvPopSharePicSave;
    @BindView(R.id.tv_pop_share_pic_cancel)
    TextView tvPopSharePicCancel;

    Context context;
    String url = "";
    String content = "";

    public SharePicPop(Context context, String url) {
        super(context);
        this.context = context;
        this.url = url;
        initView();
    }

    public SharePicPop(Context context, String url,String content) {
        super(context);
        this.context = context;
        this.url = url;
        this.context = context;
        initView();
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_share_pic);
        ButterKnife.bind(this, view);
        return view;
    }

    void initView() {
        Glide.with(context).load(url).into(ivPopSharePic);

        llPopShareWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppIsInstallUtils.isWeChatAvailable(context)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            WechatShareManager.getInstance(context).shareWeChatImage(context, 0, url);
                        }
                    }).start();
                } else {
                    ToastUtil.show(context, "您没有安装微信");
                }
            }
        });

        llPopShareWechatCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppIsInstallUtils.isWeChatAvailable(context)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            WechatShareManager.getInstance(context).shareWeChatImage(context, 1, url);
                        }
                    }).start();
                } else {
                    ToastUtil.show(context, "您没有安装微信");
                }
            }
        });

        llPopShareQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QqShareManager.getInstance(context).shareImageToQQ(url);
                shareQQ();
            }
        });

        llPopShareQqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // QqShareManager.getInstance(context).shareImageToQQ(url);
                shareQQ();
            }
        });

        llPopShareWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppIsInstallUtils.isWeiBoAvailable(context)) {
                    WeiboShareManager.getInstance(context).WeiboShare(context, "注册有礼", "", url);
                } else {
                    ToastUtil.show(context, "您没有安装微博");
                }
            }
        });

        tvPopSharePicSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPic();
            }
        });

        tvPopSharePicCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    void shareQQ() {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "_shengmo");
        String imageFileName = "share_qq_" + MyApp.uid + ".jpg";
        final File imageFile = new File(storageDir, imageFileName);
        if (imageFile.exists()) {
            //com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("图片已保存");
            QqShareManager.getInstance(context).shareImageToQQByPath(imageFile.getPath());
        } else {
            Glide.with(context).downloadOnly().load(url).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    FileUtil.copy(resource, imageFile);
                    QqShareManager.getInstance(context).shareImageToQQByPath(resource.getPath());
                }
            });
        }
    }

    void downloadPic() {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "_shengmo");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String imageFileName = "share_qq_" + MyApp.uid + ".jpg";
        final File imageFile = new File(storageDir, imageFileName);
        if (imageFile.exists()) {
            ToastUtil.show(context, "图片已保存 存储路径:" + imageFile.getPath());
            //ToastUtil.sho("图片已保存");
        } else {
            Glide.with(context).downloadOnly().load(url).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    FileUtil.copy(resource, imageFile);
                    ToastUtil.show(context, "图片已保存 存储路径:" + imageFile.getPath());
                }
            });
        }
    }
}
