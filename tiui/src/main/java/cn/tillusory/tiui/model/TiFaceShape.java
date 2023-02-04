package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2018/11/25.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public enum TiFaceShape {
    CLASSIC(R.string.classic, TiFaceShapeVal.CLASSIC_FACE_SHAPE, R.drawable.ic_ti_classic_face_shape, R.drawable.ic_ti_classic_face_shape_full),
    SQUARE_FACE(R.string.square_face, TiFaceShapeVal.SQUARE_FACE_SHAPE, R.drawable.ic_ti_square_face_shape, R.drawable.ic_ti_square_face_shape_full),
    LONG_FACE(R.string.long_face, TiFaceShapeVal.LONG_FACE_SHAPE, R.drawable.ic_ti_long_face_shape, R.drawable.ic_ti_long_face_shape_full),
    ROUND_FACE(R.string.round_face, TiFaceShapeVal.ROUND_FACE_SHAPE, R.drawable.ic_ti_round_face_shape, R.drawable.ic_ti_round_face_shape_full),
    THIN_FACE(R.string.thin_face, TiFaceShapeVal.THIN_FACE_SHAPE, R.drawable.ic_ti_thin_face_shape, R.drawable.ic_ti_thin_face_shape_full);

    private int stringId;
    private TiFaceShapeVal faceShapeVal;
    private int imageId;
    private int fullImgId;

    TiFaceShape(int stringId, TiFaceShapeVal faceShapeVal, int imageId, int fullImgId) {
        this.stringId = stringId;
        this.faceShapeVal = faceShapeVal;
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

    public TiFaceShapeVal getFaceShapeVal() {
        return faceShapeVal;
    }


}


