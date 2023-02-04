package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2018/11/22.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public enum TiBeauty {
    WHITENING(cn.tillusory.sdk.R.string.skin_whitening, R.drawable.ic_ti_whitening, R.drawable.ic_ti_whitening_full),
    BLEMISH_REMOVAL(cn.tillusory.sdk.R.string.skin_blemish_removal, R.drawable.ic_ti_blemish_removal, R.drawable.ic_ti_blemish_removal_full),
    //PRECISE_BEAUTY(R.string.skin_precise, R.drawable.ic_ti_precise_beauty, R.drawable.ic_ti_precise_beauty_full),
    TENDERNESS(cn.tillusory.sdk.R.string.skin_tenderness, R.drawable.ic_ti_tenderness, R.drawable.ic_ti_tenderness_full),
    SHARPNESS(R.string.skin_sharpness, R.drawable.ic_ti_sharpness, R.drawable.ic_ti_sharpness_full),
    BRIGHTNESS(cn.tillusory.sdk.R.string.skin_brightness, R.drawable.ic_ti_brightness, R.drawable.ic_ti_brightness_full);

    private int stringId;
    private int imageId;
    private int fullImgId;

    TiBeauty(@StringRes int stringId, @DrawableRes int imageId, @DrawableRes int fullImgId) {
        this.stringId = stringId;
        this.imageId = imageId;
        this.fullImgId = fullImgId;
    }

    public String getString(@NonNull Context context) {
        return context.getResources().getString(stringId);
    }

    public Drawable getImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(imageId);
    }

    public Drawable getFullImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(fullImgId);
    }



}


