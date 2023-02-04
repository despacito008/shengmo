package com.aiwujie.shengmo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.aiwujie.shengmo.kt.listener.CompressCallback;
import com.aiwujie.shengmo.kt.listener.SimpleCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.utils
 * @ClassName: ImageUtils
 * @Author: xmf
 * @CreateDate: 2022/4/20 17:13
 * @Description:
 */
public class ImageUtils {

    /**
     * 压缩
     *
     * @param filePath 文件路径
     */
    public static void compressImage(final String filePath, CompressCallback callback) {
        Executors.newSingleThreadExecutor()
        .execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ImageUtils.compressImageFromFile(filePath, 1024f);// 按尺寸压缩图片
                //int size = bitmap.getByteCount();
               // Logger.d(bitmap);
                File file = ImageUtils.compressImage(bitmap);  //按质量压缩图片
                callback.doSuc(file.getAbsolutePath());
//                File file = ImageUtils.bitmap2File(compressBitmap);
               // String fileSize = FileUtils.getFileSize(file);
                //upLoadPhotos(file);
            }
        });
    }


    /**
     * 按尺寸压缩图片
     *
     * @param srcPath  图片路径
     * @param desWidth 压缩的图片宽度
     * @return Bitmap 对象
     */

    public static Bitmap compressImageFromFile(String srcPath, float desWidth) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float desHeight = desWidth * h / w;
        int be = 1;
        if (w > h && w > desWidth) {
            be = (int) (newOpts.outWidth / desWidth);
        } else if (w < h && h > desHeight) {
            be = (int) (newOpts.outHeight / desHeight);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

//        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param image
     */

    public static File compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;

        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
//        long length = baos.toByteArray().length;
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片


        File file = new File(Environment.getExternalStorageDirectory() + "/temp.png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }
}
