package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import cn.tillusory.sdk.bean.TiDistortionEnum;
import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2018/11/26.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public enum TiDistortion {

    NO_DISTORTION(TiDistortionEnum.NO_DISTORTION, R.drawable.ic_ti_none, R.drawable.ic_ti_none_full),
    ET_DISTORTION(TiDistortionEnum.ET_DISTORTION, R.drawable.ic_ti_et, R.drawable.ic_ti_et_full),
    PEAR_FACE_DISTORTION(TiDistortionEnum.PEAR_FACE_DISTORTION, R.drawable.ic_ti_pear_face, R.drawable.ic_ti_pear_face_full),
    SLIM_FACE_DISTORTION(TiDistortionEnum.SLIM_FACE_DISTORTION, R.drawable.ic_ti_slim_face, R.drawable.ic_ti_slim_face_full),
    SQUARE_FACE_DISTORTION(TiDistortionEnum.SQUARE_FACE_DISTORTION, R.drawable.ic_ti_square_face, R.drawable.ic_ti_square_face_full);

    private TiDistortionEnum distortionEnum;
    private int imageId;
    private int fullImgId;

    TiDistortion(TiDistortionEnum distortionEnum, int imageId, int fullImgId) {
        this.distortionEnum = distortionEnum;
        this.imageId = imageId;
        this.fullImgId = fullImgId;
    }

    public TiDistortionEnum getDistortionEnum() {
        return distortionEnum;
    }

    public String getString(@NonNull Context context) {
        return context.getResources().getString(distortionEnum.getStringId());
    }

    public Drawable getImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(imageId);
    }

    public Drawable getFullImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(fullImgId);
    }


}


