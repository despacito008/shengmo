package com.aiwujie.shengmo.bean;

import android.graphics.Bitmap;

/**
 * Created by 290243232 on 2018/1/16.
 */

public class SinglePictureParameter {
    private int height;
    private int width;
    private Bitmap bitmap;

    public SinglePictureParameter(int height, int width, Bitmap bitmap) {
        this.height = height;
        this.width = width;
        this.bitmap = bitmap;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
