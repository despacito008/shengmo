package com.aiwujie.shengmo.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

/**
 * Created by 290243232 on 2017/3/21.
 */

public class AnimationUtil {
    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }
    /**
     * 从控件所在位置移动到控件的顶部
     *
     * @return
     */
    public static TranslateAnimation moveToViewTop() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(0,0,0,-100);
        mHiddenAction.setFillAfter(true);
        mHiddenAction.setDuration(350);
        return mHiddenAction;
    }

    /**
     * 从控件顶部移动在控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation1() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(0,0,-100,0);
        mHiddenAction.setFillAfter(true);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }


    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static TranslateAnimation horizontalRightShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static TranslateAnimation horizontalRightHide() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;

    }

    //列表宫格动画
    public static LayoutAnimationController getController(){
        AnimationSet set = new AnimationSet(false);
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,//飞入效果
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
//        Animation animation = new AlphaAnimation(0,1);   //AlphaAnimation 控制渐变透明的动画效果
        animation.setDuration(150);     //动画时间毫秒数
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 1);
        return controller;
    }
    //动态回到顶部动画效果
    public static AlphaAnimation ViewToGone() {
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(300);
        return alpha;
    }
    //动态回到顶部动画效果
    public static AlphaAnimation ViewToVisible() {
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(300);
        return alpha;
    }

    public static TranslateAnimation hideTabAnimation(View view) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(0,0,0,-view.getHeight());
        mHiddenAction.setFillAfter(true);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    public static TranslateAnimation showTabAnimation(View view) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(0,0,-view.getHeight(),0);
        mHiddenAction.setFillAfter(true);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    public static TranslateAnimation bottomHideTabAnimation(View view) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(0,0,0,-view.getHeight());
        mHiddenAction.setFillAfter(true);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    public static TranslateAnimation bottomShowTabAnimation(View view) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(0,0,-view.getHeight(),0);
        mHiddenAction.setFillAfter(true);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }
}
