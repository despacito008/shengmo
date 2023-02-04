package com.aiwujie.shengmo.utils;

/**
 * Created by AMing on 16/4/14.
 * Company RongCloud
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class BitmapUtils {

    public static int dip2pix(Context context, int dips) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        return (dips * densityDpi) / 160;
    }

    public static int pix2dip(Context context, int pixs) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        return (pixs * 160) / densityDpi;
    }

    /**
     * 将图片保存在指定路径中
     *
     * @param bitmap
     * @param descPath
     */
    public static void saveBitmap(Bitmap bitmap, String descPath) {
        File file = new File(descPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                bitmap.compress(CompressFormat.JPEG, 30, new FileOutputStream(
                                    file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    // -------------------------------------------------------------------------------------------------------------------
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                                            bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }

        return output;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap
     *            传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                                  (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                                  (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
        return output;
    }

    /**
     * @param “将图片内容解析成字节数组”
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    /**
     * @param "将字节数组转换为ImageView可调用的Bitmap对象"
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                                                     opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * @param "图片缩放"
     * @param bitmap
     *            对象
     * @param w
     *            要缩放的宽度
     * @param h
     *            要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (float) w / (float) width;
        float scaleHeight = (float) h / (float) height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                                            matrix, true);
        return newBmp;
    }

    public static Bitmap zoomBMP(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (float) w / (float) width;
        float scaleHeight = (float) h / (float) height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                                            matrix, true);
        return newBmp;
    }

    public static File saveBitmap(String path, Bitmap bitmap) {
        return getFileFromBytes(Bitmap2Bytes(bitmap), path);
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);

        if (bm != null) {
            bm.recycle();
            bm = null;
        }

        return baos.toByteArray();
    }

    @SuppressLint("NewApi")
    public static String getBitmapStrBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String data = Base64.encodeToString(bytes, 0, bytes.length,
                                            Base64.DEFAULT);

        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }

        return data;
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                        .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                            : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                           drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;

    }

    public static byte[] compressBitmap(int maxNumOfPixels, String imgpath) {
        double maxSize = 100.00;
        Bitmap bitmap = loadBitmap(maxNumOfPixels, imgpath);
        if (null != bitmap) {
            byte[] bBitmap = convertBitmap(bitmap);
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
            double mid = bBitmap.length / 1024;
            if (mid > maxSize) {
                double i = mid / maxSize;
                bBitmap = compressBitmap((int) (maxNumOfPixels / Math.abs(i)),
                                         imgpath);
            }
            return bBitmap;
        } else {
            return null;
        }
    }

    public static byte[] convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        int options = 100;
//		LogUtil.e("===baos.toByteArray().length===" + baos.toByteArray().length);
//		LogUtil.e("===baos.size===" + baos.size());
        while (baos.size() / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        try {
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        return baos.toByteArray();

    }

    public static Bitmap loadBitmap(int maxNumOfPixels, String imgpath) {
        Bitmap bitmap = null;
        try {
            FileInputStream f = new FileInputStream(new File(imgpath));

            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgpath, options);
            // 调用上面定义的方法计算inSampleSize值
            if (0 == maxNumOfPixels) {
                maxNumOfPixels = 128 * 128;
            }
            options.inSampleSize = computeSampleSize(options, -1,
                                   maxNumOfPixels);
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(f, null, options);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options,

                                        int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength,

                          maxNumOfPixels);

        int roundedSize;

        if (initialSize <= 8) {

            roundedSize = 1;

            while (roundedSize < initialSize) {

                roundedSize <<= 1;

            }

        } else {

            roundedSize = (initialSize + 7) / 8 * 8;

        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,

            int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;

        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :

                         (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == -1) ? 128 :

                         (int) Math.min(Math.floor(w / minSideLength),

                                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {

            // return the larger one when there is no overlapping zone.

            return lowerBound;

        }

        if ((maxNumOfPixels == -1) &&

                (minSideLength == -1)) {

            return 1;

        } else if (minSideLength == -1) {

            return lowerBound;

        } else {

            return upperBound;

        }
    }

//	public static String saveBitmap2file(Bitmap bmp, String filename) {
//		CompressFormat format = Bitmap.CompressFormat.JPEG;
//		int quality = 100;
//		OutputStream stream = null;
//		try {
//			stream = new FileOutputStream(filename);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		bmp.compress(format, quality, stream);
//		return filename;
//	}

    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static String saveFile(Bitmap bm, String fileName) throws IOException {
        String path = getSDPath() + "/revoeye/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile.getPath();
    }
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
        }
        return sdDir.toString();
    }
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    /**
     * 设置图片比例
     * @param imagePic
     * @param width
     * @param height
     * @return
     */
    public static ViewGroup.LayoutParams suitImgPara(View imagePic, int width, int height) {
        ViewGroup.LayoutParams para = imagePic.getLayoutParams();
        int minWidth = DensityUtil.dp2px(120);
        int minHeight = DensityUtil.dp2px(180);
        int maxWidth = DensityUtil.dp2px(200);
        int maxHeight = DensityUtil.dp2px(300);
        float rate = (float) height / width;
        if (rate > 1.5) {
            if(height < minHeight) {
                height = minHeight;
            } else if (height > maxHeight) {
                height = maxHeight;
            }
            width =  (int) (height / rate);
        } else {
            if(width < minWidth) {
                width = minWidth;
            } else if (width > maxWidth) {
                width = maxWidth;
            }
            height = (int) (width * rate);
        }


//        if (width > 1500 || height > 1500) {
//            para.height = height * 2 / 5;
//            para.width = width * 2 / 5;
//        } else {
//            if(width>1000||height>1000){
//                para.height = height * 3 / 5;
//                para.width = width * 3 / 5;
//            }else {
//                if(width>500||height>500){
//                    para.height = height * 3 / 4;
//                    para.width = width * 3 / 4;
//                }else {
//                    if (width < 200 || height < 200) {
//                        para.height = height + height / 3;
//                        para.width = width + width / 3;
//                    } else {
//                        para.height = height;
//                        para.width = width;
//                    }
//                }
//            }
//        }
        para.height = height;
        para.width = width;
      //  LogUtil.d(width + "--------" + height);
//        para.height = (int) (para.height * 1.2);
//        para.width = (int) (para.width * 1.2);
        return para;
    }


    /**
     * 设置图片比例
     * @param imagePic
     * @param width
     * @param height
     * @return
     */
    public static Bitmap suitImgPara(View imagePic, int width, int height,Bitmap bitmap) {
        ViewGroup.LayoutParams para = imagePic.getLayoutParams();
        if (width > 1500 || height > 1500) {
            para.height = height * 2 / 5;
            para.width = width * 2 / 5;
        } else {
            if(width>1000||height>1000){
                para.height = height * 3 / 5;
                para.width = width * 3 / 5;
            }else {
                if(width>500||height>500){
                    para.height = height * 3 / 4;
                    para.width = width * 3 / 4;
                }else {
                    if (width < 200 || height < 200) {
                        para.height = height + height / 3;
                        para.width = width + width / 3;
                    } else {
                        para.height = height;
                        para.width = width;
                    }
                }
            }
        }
        para.height = (int) (para.height * 1.2);
        para.width = (int) (para.width * 1.2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.setWidth(para.width);
            bitmap.setHeight(para.height);
        }
        return bitmap;
    }

    /**
     * 设置视频比例
     * @param imagePic
     * @param width
     * @param height
     * @return
     */
    public static ViewGroup.LayoutParams suitVideoPara(View imagePic, int width, int height) {
        ViewGroup.LayoutParams para = imagePic.getLayoutParams();

       // LogUtil.d("before" + width + " --- " + height);

        int minWidth = DensityUtil.dp2px(120);
        int minHeight = DensityUtil.dp2px(180);
        int maxWidth = DensityUtil.dp2px(200);
        int maxHeight = DensityUtil.dp2px(300);
        float rate = (float) height / width;
        if (rate > 1.5) {
            if(height < minHeight) {
                height = minHeight;
            } else if (height > maxHeight) {
                height = maxHeight;
            }
            width =  (int) (height / rate);
        } else {
            if(width < minWidth) {
                width = minWidth;
            } else if (width > maxWidth) {
                width = maxWidth;
            }
            height = (int) (width * rate);
        }
//        if (width > 1500 || height > 1500) {
//            para.height = height * 2 / 5;
//            para.width = width * 2 / 5;
//        } else {
//            if(width>1000||height>1000){
//                para.height = height * 3 / 5;
//                para.width = width * 3 / 5;
//            }else {
//                if(width>500||height>500){
//                    para.height = height * 3 / 4;
//                    para.width = width * 3 / 4;
//                }else {
//                    if (width < 200 || height < 200) {
//                        para.height = height + height / 3;
//                        para.width = width + width / 3;
//                    } else {
//                        para.height = height;
//                        para.width = width;
//                    }
//                }
//            }
//        }
       // LogUtil.d("after" + width + " --- " + height);
        para.height = height;
        para.width = width;
//        para.height = (int) (para.height * 1.5);
//        para.width = (int) (para.width * 1.5);
        return para;
    }

    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getNewBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }


    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float temp = ((float) height) / ((float) width);
        int newHeight = (int) ((newWidth) * temp);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }


    /**
     * @author yukaida
     * @param absolutePath 照片的绝对路劲
     * @return 重新调整方向之后的bitmap图片
     */
    public static Bitmap orientation(String absolutePath){
        Bitmap bitmap_or=BitmapFactory.decodeFile(absolutePath);
        try {

            ExifInterface exif = new ExifInterface(absolutePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.d("EXIF", "Exif: " + orientation);

            Matrix matrix = new Matrix();

            if (orientation == 6) {

                matrix.postRotate(90);

            }

            else if (orientation == 3) {

                matrix.postRotate(180);

            }

            else if (orientation == 8) {

                matrix.postRotate(270);

            }

            bitmap_or= Bitmap.createBitmap(bitmap_or, 0, 0, bitmap_or.getWidth(), bitmap_or.getHeight(), matrix, true); // rotating bitmap
            return bitmap_or;
        }

        catch (Exception e) {

        }
        return null;
    }

}
