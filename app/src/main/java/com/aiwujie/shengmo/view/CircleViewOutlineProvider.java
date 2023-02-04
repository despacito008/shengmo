package com.aiwujie.shengmo.view;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

@SuppressLint("NewApi")
public class CircleViewOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        int left = 0;
        int top = (view.getHeight() - view.getWidth()) / 2;
        int right = view.getWidth();
        int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outline.setOval(left, top, right, bottom);
        }
    }
}
