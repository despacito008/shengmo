package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;



import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/4/18.
 */

public class WeiboShareManager {
    private IWeiboShareAPI mWeiboShareAPI;// 新建Tencent实例用于调用分享方法
    private Context mContext;
    private static WeiboShareManager mInstance;
    private int WeiboShareFlag = 0;
    private Handler handler = new Handler();

    private WeiboShareManager(Context context) {
        this.mContext = context;
        //初始化数据
        //初始化微博分享代码
        initWeiboShareManager(context);
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     *
     * @return
     */
    public static WeiboShareManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WeiboShareManager(context);
        }
        return mInstance;
    }

    private void initWeiboShareManager(Context context) {
        if (mWeiboShareAPI == null) {
            // 创建微博分享接口实例
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, MyApp.SINA_APPKEY);
            // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
            // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
            // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
            mWeiboShareAPI.registerApp();
        }
    }

    //webpageobject一定要配置全,否则就会掉不起来界面。。。（大坑）
    public void WeiboShare(final Context context, final String sharedContent, final String address, final String headurl) {

        if (TextUtil.isEmpty(headurl)) {
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
//        WeiboMessage weiboMessage = new WeiboMessage();
            weiboMessage.textObject = getTextObj(sharedContent, address);
            ImageObject imageObject = new ImageObject();
            WeiboShareFlag = 1;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.applogo);
            imageObject.imageData = Bitmap2Bytes(bitmap);
            weiboMessage.mediaObject = imageObject;
            SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
            request.transaction = String.valueOf(System.currentTimeMillis());
            request.multiMessage = weiboMessage;
            boolean isSuccess = mWeiboShareAPI.sendRequest((Activity) context, request);
            if (isSuccess) {
                if (WeiboShareFlag == 1) {
                    WeiboShareFlag = 0;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShareSuccessUtils.Shared(handler);
                        }
                    }, 5000);
                }
            }
            //分享成功
            mInstance = null;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bmp = Glide.with(context)
                                .asBitmap()
                                .load(headurl)
                                .submit(960, 960)
                                .get();
                        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
                        weiboMessage.textObject = getTextObj(sharedContent, address);
                        ImageObject imageObject = new ImageObject();
                        WeiboShareFlag = 1;
                        imageObject.imageData = Bitmap2Bytes(bmp);
                        weiboMessage.mediaObject = imageObject;
                        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                        request.transaction = String.valueOf(System.currentTimeMillis());
                        request.multiMessage = weiboMessage;
                        boolean isSuccess = mWeiboShareAPI.sendRequest((Activity) context, request);
                        if (isSuccess) {
                            if (WeiboShareFlag == 1) {
                                WeiboShareFlag = 0;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShareSuccessUtils.Shared(handler);
                                    }
                                }, 5000);
                            }
                        }
                        //分享成功
                        mInstance = null;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                    }

                }
            }).start();

        }

    }

    /**
     * 获取文本信息对象
     *
     * @return TextObject
     */
    private TextObject getTextObj(String sharedContent, String address) {
        TextObject textObject = new TextObject();
//        textObject.text = "圣魔斯慕——国内首个专业亚文化交友APP诞生了！快去看看吧~...http://shengmo.org";//这里输入文本
        textObject.text = sharedContent + address;//这里输入文本
        return textObject;
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
//    public  Bitmap getBitmap(String imageUrl) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        return ImageLoader.getInstance().loadImageSync(imageUrl, options);
//    }

    private Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

}
