package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by 290243232 on 2017/7/20.
 */

public class ImageSpanUtils {

    /**
     * 获取图片并插入EditText
     */
    public static void insertEditText(String str, Context context, EditText editText){

        Bitmap imgBitmap = createBitmap("#"+str+"健健康康接口#");
        if(imgBitmap != null){

            //根据Bitmap对象创建ImageSpan对象
            ImageSpan imageSpan = new ImageSpan(context, imgBitmap);
            //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
            SpannableString spannableString = new SpannableString("[name]"+str+"[/name]");
            //  用ImageSpan对象替换face
            spannableString.setSpan(imageSpan, 0, ("[name]"+str+"[/name]").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //将选择的图片追加到EditText中光标所在位置
            int index = editText.getSelectionStart(); //获取光标所在位置
            Editable edit_text = editText.getEditableText();
            if(index <0 || index >= edit_text.length()){
                edit_text.append(spannableString);
            }else{
                edit_text.insert(edit_text.length(), spannableString);
            }
        }else{
            Log.i("MainActivity", "插入失败");
        }
    }


    /**
     * 字符串转换成图片
     * @param str
     * @return
     */
    public static Bitmap createBitmap(String str) {
        Bitmap bp = Bitmap.createBitmap(500, 100, Bitmap.Config.ARGB_8888); //画布大小
        Canvas c = new Canvas(bp);
//        Paint paint1 = new Paint();
//        paint1.setColor(Color.parseColor("#b73acb"));
        c.drawColor(Color.TRANSPARENT);//画布颜色
//
//        Paint paint2 = new Paint();//画姓名前边的间隔
//        paint2.setColor(Color.WHITE);
//        paint2.setStrokeWidth(1f);
//        c.drawLine(0, 0, 0, 0, paint2);

        Paint paint = new Paint();
        paint.setTextSize(35);//字体大小
        paint.setColor(Color.parseColor("#b73acb"));//字体大小
        paint.setFakeBoldText(true); //粗体
//        paint.setTextSkewX(0);//斜度
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(str, 250,50, paint);//文字位置
//        c.save(Canvas.ALL_SAVE_FLAG);//保存
        c.save();//保存
        //看博客修改https://blog.csdn.net/u010117864/article/details/89207881
        c.restore();//
        return bp;
    }
}
