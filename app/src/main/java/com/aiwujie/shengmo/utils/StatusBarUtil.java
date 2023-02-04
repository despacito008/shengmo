package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import com.aiwujie.shengmo.R;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.OnKeyboardListener;

public class StatusBarUtil {
    public static void init(Activity activity) {
        ImmersionBar.with(activity)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
                .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .autoDarkModeEnable(true) //自动状态栏字体和导航栏图标变色，必须指定状态栏颜色和导航栏颜色才可以自动变色哦
                .autoStatusBarDarkModeEnable(true,0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .autoNavigationBarDarkModeEnable(true,0.2f) //自动导航栏图标变色，必须指定导航栏颜色才可以自动变色哦
                //.flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                //.hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
                //.addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
                //.titleBar(view)    //解决状态栏和布局重叠问题，任选其一
                //.titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
                //.statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .supportActionBar(true) //支持ActionBar使用
                //.statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
                //.navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
                //.barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
                //.removeSupportView(toolbar)  //移除指定view支持
                .removeSupportAllView() //移除全部view支持
                .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
                //.fixMarginAtBottom(true)   //已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
                .reset()  //重置所以沉浸式参数
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                       // LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                })
                .init();  //必须调用方可沉浸式
    }

    public static void showLightStatusBar(Activity activity) {
        ImmersionBar.with(activity)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
    }

    public static void showLightStatusBar(Activity activity, int viewId) {
        ImmersionBar.with(activity)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .titleBarMarginTop(viewId)
                .init();
    }

    public static void showLightStatusBar(Fragment fragment, int viewId) {
        ImmersionBar.with(fragment)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .titleBarMarginTop(viewId)
                .init();
    }

    public static void showLightStatusBar(Fragment fragment, View view) {
        ImmersionBar.with(fragment)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .titleBarMarginTop(view)
                .init();
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
