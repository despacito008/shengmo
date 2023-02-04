package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.wxapi.WXEntryActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;



import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/4/18.
 */

public class WechatShareManager {

    public static final int WECHAT_SHARE_WAY_WEBPAGE = 3;  //链接
    public static final int WECHAT_SHARE_WAY_IMAGE = 2;  //图片
    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;  //会话
    public static final int WECHAT_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline; //朋友圈

    private static WechatShareManager mInstance;
    private ShareContent mShareContentWebpag;
    private IWXAPI mWXApi;
    private Context mContext;

    private WechatShareManager(Context context) {
        this.mContext = context;
        //初始化数据
        //初始化微信分享代码
        initWechatShare(context);
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     *
     * @return
     */
    public static WechatShareManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WechatShareManager(context);
        }
        return mInstance;
    }

    private void initWechatShare(Context context) {
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(context, WXEntryActivity.APP_ID, true);
        }
        mWXApi.registerApp(WXEntryActivity.APP_ID);
    }

    /**
     * 通过微信分享
     *
     * @param shareContent 分享的方式（文本、图片、链接）
     * @param shareType    分享的类型（朋友圈，会话）
     */
    public void shareByWebchat(final ShareContent shareContent, final int shareType, final String headurl) {
        switch (shareContent.getShareWay()) {
            case WECHAT_SHARE_WAY_WEBPAGE:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        shareWebPage(shareContent, shareType, headurl);
                    }
                }.start();
                break;
            case WECHAT_SHARE_WAY_IMAGE :

                break;

        }
    }

    public abstract class ShareContent {
        protected abstract int getShareWay();

        protected abstract String getContent();

        protected abstract String getTitle();

        protected abstract String getURL();

        protected abstract String getPictureResource();
    }


    /**
     * 设置分享链接的内容
     *
     * @author chengcj1
     */
    public class ShareContentWebpage extends ShareContent {
        private String title;
        private String content;
        private String url;
        private String pictureResource;

        public ShareContentWebpage(String title, String content, String url, String pictureResource) {
            this.title = title;
            this.content = content;
            this.url = url;
            this.pictureResource = pictureResource;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_WEBPAGE;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return title;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected String getPictureResource() {
            return pictureResource;
        }
    }

    /*
     * 获取网页分享对象
     */
    public ShareContent getShareContentWebpag(String title, String content, String url, String pictureResource) {
        if (mShareContentWebpag == null) {
            mShareContentWebpag = new ShareContentWebpage(title, content, url, pictureResource);
        }
        return (ShareContentWebpage) mShareContentWebpag;
    }

    /*
     * 分享链接
     */
    private void shareWebPage(ShareContent shareContent, int shareType, final String headurl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContent.getURL();
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();
        if (headurl.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.sharedicon);
            msg.thumbData = Bitmap2Bytes(bitmap);
        } else {
            Bitmap thumbs = null;
            if (headurl.equals(NetPic())) {//"http://59.110.28.150:888/"
                thumbs = getBitmap(HttpUrl.NetPic()+"nopeople.png");
            } else {
                Bitmap bitmap = getBitmap(headurl);
                if(bitmap != null) {
                    thumbs = createBitmapThumbnail(bitmap);
                }
            }
            if(thumbs != null) {
                msg.setThumbImage(thumbs);
            }
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
        mInstance = null;
    }

    //分享图片   shareType = 0-好友   1-朋友圈
    public void shareWeChatImage(Context context,int shareType,String url) {
        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit(960, 960)
                    .get();


            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            //设置缩略图
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
            bmp.recycle();
            msg.thumbData = bmpToByteArray(thumbBmp, true);

            //构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = shareType;
            //req.userOpenId = getOpenId();
            //调用api接口，发送数据到微信
            mWXApi.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp == null) {
            return;
        }
       // Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
        //初始化 WXImageObject 和 WXMediaMessage 对象

    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @param
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        byte[] bts = output.toByteArray();
        Bitmap bitmaps = BitmapFactory.decodeByteArray(bts, 0, bts.length);
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    //压缩图片
    public Bitmap createBitmapThumbnail(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 99;
        int newHeight = 99;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        return newBitMap;
    }


    public Bitmap getBitmap(String imageUrl) {
//         options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(imageUrl)
                    .submit(360, 480).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
       // return ImageLoader.getInstance().loadImageSync(imageUrl, options);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }
}
