package com.aiwujie.shengmo.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

@SuppressLint("NewApi")
public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {

    private float mRadius;

    public TextureVideoViewOutlineProvider(float mRadius) {
        this.mRadius = mRadius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin,topMargin,
                rect.right - rect.left - leftMargin,
                rect.bottom - rect.top - topMargin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outline.setRoundRect(selfRect,mRadius);
        }
    }
}
