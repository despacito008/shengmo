package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2019-09-05.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public enum TiMakeupType {
    BLUSHER_MAKEUP(R.string.blusher, R.drawable.ic_ti_blusher_normal, R.drawable.ic_ti_blusher_normal_full),
//    EYELASH_MAKEUP(R.string.eyelash, R.drawable.ic_ti_eyelash_normal, R.drawable.ic_ti_eyelash_normal_full),
    EYEBROW_MAKEUP(R.string.eyebrow, R.drawable.ic_ti_eyebrow_normal, R.drawable.ic_ti_eyebrow_normal_full),
    EYESHADOW_MAKEUP(R.string.eyeshadow, R.drawable.ic_ti_eyeshadow_normal, R.drawable.ic_ti_eyeshadow_normal_full);
//    EYELINE_MAKEUP(R.string.eyeline, R.drawable.ic_ti_eyeline_normal, R.drawable.ic_ti_eyeline_normal_full);

    private int stringId;
    private int imageId;
    private int fullImgId;

    TiMakeupType(int stringId, int imageId, int fullImgId) {
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


