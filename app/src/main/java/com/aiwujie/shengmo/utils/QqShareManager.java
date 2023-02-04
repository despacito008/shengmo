package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.tencent.connect.avatar.QQAvatar;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/4/18.
 */

public class QqShareManager {
    private Tencent mTencent;// 新建Tencent实例用于调用分享方法
    private Context mContext;
    private static QqShareManager mInstance;
    private Bundle params;

    private QqShareManager(Context context) {
        this.mContext = context;
        //初始化数据
        //初始化QQ分享代码
        initQqShareManager(context);
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     *
     * @return
     */
    public static QqShareManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new QqShareManager(context);
        }
        return mInstance;
    }

    private void initQqShareManager(Context context) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(MyApp.QQAPP_ID, context.getApplicationContext());
        }
    }

    class MyIUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.i("qqshared", "onComplete: " + o.toString());
            //EventBus.getDefault().post("qq_share");
            ShareSuccessUtils.Shared(new Handler());
        }

        @Override
        public void onError(UiError uiError) {
            Log.i("qqshared", "onError: " + uiError.toString());
        }

        @Override
        public void onCancel() {

        }
    }

    public void shareToQQ(String address, String title, String summary, String headurl) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, address);// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, headurl);// 网络图片地址　
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ((Activity) mContext, params, new MyIUiListener());
                Log.i("qqlogindocomplete", "run: " + "!1111");
                mInstance = null;
            }
        });
    }

    public void shareToQZone(String address, String title, String summary, String headurl) {
        params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, address);// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
        ArrayList<String> imgUrlList = new ArrayList<>();
        if (headurl.equals("")) {
//            imgUrlList.add("http://59.110.28.150:888/Uploads/Picture/2017-04-18/20170418155251622.jpg");
            imgUrlList.add(NetPic() + "Uploads/Picture/2017-04-18/20170418155251622.jpg");//http://59.110.28.150:888/
        } else {
            imgUrlList.add(headurl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);// 图片地址
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone((Activity) mContext, params, new MyIUiListener());
                Log.i("qqlogindocomplete", "run: " + "!1111");
                mInstance = null;
            }
        });
    }

    public void shareImageToQQ(String url) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ((Activity) mContext, params, new MyIUiListener());
                Log.i("qqlogindocomplete", "run: " + "!1111");
                mInstance = null;
            }
        });
    }

    public void shareImageToQQByPath(String path) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ((Activity) mContext, params, new MyIUiListener());
                Log.i("qqlogindocomplete", "run: " + "!1111");
                mInstance = null;
            }
        });
    }

    public void shareImageToQQZoneByPath(String path) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圣魔");// 应用名称
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone((Activity) mContext, params, new MyIUiListener());
                Log.i("qqlogindocomplete", "run: " + "!1111");
                mInstance = null;
            }
        });
    }

    // 1)保存图片到本地

    /**
     * 保存图片到本地
     */
    String imagePath = "";
    public void saveBitmap(Bitmap bm) {
        isHaveSDCard();
        File file;
        if (isHaveSDCard()) {
            file  = Environment.getExternalStorageDirectory();
        } else {
            file = Environment.getDataDirectory();
        }
        File newFile  = new File(file.getPath() + "/shengmo/data/");
        if (!newFile.isDirectory()) {
            newFile.delete();
            newFile.mkdirs();
        }
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        writeBitmap(newFile.getPath(), "invite_img" + MyApp.uid + ".png" , bm);
        imagePath = newFile.getPath() + "invite_img" + MyApp.uid + ".png";
    }

    //2)保存图片到本地

    /**
     * 保存图片
     *
     * @param path
     * @param name
     * @param bitmap
     */

    public static void writeBitmap(String path, String name, Bitmap bitmap) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        File _file = new File(path + name);
        if (_file.exists()) {
            _file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_file);
            if (name != null && !"".equals(name)) {
                int index = name.lastIndexOf(".");
                if (index != -1 && (index + 1) < name.length()) {
                    String extension = name.substring(index + 1).toLowerCase();
                    if ("png".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } else if ("jpg".equals(extension)
                            || "jpeg".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //三、是否有sd卡

    public boolean isHaveSDCard() {
        String SDState = android.os.Environment.getExternalStorageState();
        if (SDState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

}
