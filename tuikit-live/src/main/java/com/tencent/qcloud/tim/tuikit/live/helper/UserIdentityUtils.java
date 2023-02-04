package com.tencent.qcloud.tim.tuikit.live.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

public class UserIdentityUtils {
    /**
     * 获取vip等级值
     * @param role
     * @param view
     */
    public static void showUserIdentity(Context mContext, int role, ImageView view){
        view.setVisibility(View.VISIBLE);
        switch (role) {
            case 2:
            case 3:
            case 4:
                showIdentityImg(mContext,1,view);
                break;
            case 5:
                break;
            case 6:
                showIdentityImg(mContext,2,view);
                break;
            case 7:
                showIdentityImg(mContext,3,view);
                break;
            case 8:
                showIdentityImg(mContext,4,view);
                break;
            case 9:
                showIdentityImg(mContext,5,view);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }

    public static void showIdentityImg(Context mContext,int type,ImageView view) {
        Bitmap bmp = null;
        switch (type) {
            case 1:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), com.tencent.qcloud.tim.tuikit.live.R.drawable.user_manager);
                break;
            case 2:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), com.tencent.qcloud.tim.tuikit.live.R.drawable.user_svip_year);
                break;
            case 3:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), com.tencent.qcloud.tim.tuikit.live.R.drawable.user_svip);
                break;
            case 4:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), com.tencent.qcloud.tim.tuikit.live.R.drawable.user_vip_year);
                break;
            case 5:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), com.tencent.qcloud.tim.tuikit.live.R.drawable.user_vip);
                break;

        }
        if (bmp == null) {
            return;
        }
        int iconWidth = UIUtil.dp2px(mContext,50);
        Bitmap suitbmp = getBitmap(bmp, iconWidth, iconWidth);
        Drawable suitDrawable = new BitmapDrawable(suitbmp);
        view.setImageDrawable(suitDrawable);
        view.setImageDrawable(suitDrawable);
    }
    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bm, int newWidth, int newHeight) {
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



    //把布局转换成Drawable
    public static Drawable LayoutToDrawable(View viewHelp, int level){
        TextView textView = (TextView) viewHelp.findViewById(R.id.tv_anchor_leave);
        textView.setText(level + "");
        int size = (int)textView.getText().length();
        Bitmap snapshot = convertViewToBitmap(viewHelp, size);
        Drawable drawable = (Drawable)new BitmapDrawable(snapshot);
        return drawable;
    }

    //把view转成bitmap
    public static Bitmap convertViewToBitmap(View view, int size) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = size * 40 + 33 + 3; //小白图宽度 + 边界值
        view.layout(0, 0, width, view.getMeasuredHeight());  //根据字符串的长度显示view的宽度
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }



    /**
     * 把等级转换成bitmap
     */
    public static Drawable calcNameLevel(Context mContext,int level) {
        if(mContext == null){
            return null;
        }
        View  view = LayoutInflater.from(mContext).inflate(R.layout.layout_level_custom_msg,null);
        Drawable bitmap = LayoutToDrawable(view,level);
        return bitmap;
    }

    /**
     * 通过名称计算颜色
     */
    public static int calcNameColor(Context mContext,String strName) {
        if (strName == null) return 0;
        byte   idx     = 0;
        byte[] byteArr = strName.getBytes();
        for (int i = 0; i < byteArr.length; i++) {
            idx ^= byteArr[i];
        }

        switch (idx & 0x7) {
            case 1:
                return mContext.getResources().getColor(R.color.live_color_send_name1);
            case 2:
                return mContext.getResources().getColor(R.color.live_color_send_name2);
            case 3:
                return mContext.getResources().getColor(R.color.live_color_send_name3);
            case 4:
                return mContext.getResources().getColor(R.color.live_color_send_name4);
            case 5:
                return mContext.getResources().getColor(R.color.live_color_send_name5);
            case 6:
                return mContext.getResources().getColor(R.color.live_color_send_name6);
            case 7:
                return mContext.getResources().getColor(R.color.live_color_send_name7);
            case 0:
            default:
                return mContext.getResources().getColor(R.color.live_color_send_name0);
        }
    }
}
