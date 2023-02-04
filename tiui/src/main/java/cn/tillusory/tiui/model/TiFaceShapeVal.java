package cn.tillusory.tiui.model;

import cn.tillusory.sdk.bean.TiFaceShapeEnum;

public enum TiFaceShapeVal {
    CLASSIC_FACE_SHAPE(TiFaceShapeEnum.CLASSIC_FACE_SHAPE, 60, 60, 15, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    SQUARE_FACE_SHAPE(TiFaceShapeEnum.SQUARE_FACE_SHAPE, 50, 65, 0, -10, 0, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    LONG_FACE_SHAPE(TiFaceShapeEnum.LONG_FACE_SHAPE, 50, 30, 0, -9, 0, 10, 25, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    ROUND_FACE_SHAPE(TiFaceShapeEnum.ROUNDED_FACE_SHAPE, 20, 45, 5, 0, 0, 10, 20, 20, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    THIN_FACE_SHAPE(TiFaceShapeEnum.SLIM_FACE_SHAPE, 10, 30, 0, 0, 0, 5, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public TiFaceShapeEnum faceShapeEnum;
    public int eyeMagnify;
    public int chinSlim;
    public int faceNarrow;
    public int jawTransform;
    public int foreheadTransform;
    public int mouthTransform;
    public int noseMinify;
    public int teethWhiten;
    public int eyeSpace;
    public int noseElongate;
    public int eyeCorner;

    public int cheekboneSlim;
    public int jawboneSlim;
    public int jawSlim;
    public int eyeInnerCorners;
    public int eyeOuterCorners;
    public int mouthHeight;
    public int mouthLipSize;
    public int mouthSmiling;
    public int browHeight;
    public int browLength;
    public int browSpace;
    public int browSize;
    public int browCorner;

    TiFaceShapeVal(TiFaceShapeEnum faceShapeEnum, int eyeMagnify, int chinSlim, int faceNarrow, int jawTransform, int foreheadTransform, int mouthTransform, int noseMinify, int teethWhiten, int eyeSpace, int noseElongate, int eyeCorner, int cheekboneSlim, int jawboneSlim, int jawSlim, int eyeInnerCorners, int eyeOuterCorners, int mouthHeight, int mouthLipSize, int mouthSmiling, int browHeight, int browLength, int browSpace, int browSize, int browCorner) {
        this.faceShapeEnum = faceShapeEnum;
        this.eyeMagnify = eyeMagnify;
        this.chinSlim = chinSlim;
        this.faceNarrow = faceNarrow;
        this.jawTransform = jawTransform;
        this.foreheadTransform = foreheadTransform;
        this.mouthTransform = mouthTransform;
        this.noseMinify = noseMinify;
        this.teethWhiten = teethWhiten;
        this.eyeSpace = eyeSpace;
        this.noseElongate = noseElongate;
        this.eyeCorner = eyeCorner;
        this.cheekboneSlim = cheekboneSlim;
        this.jawboneSlim = jawboneSlim;
        this.jawSlim = jawSlim;
        this.eyeInnerCorners = eyeInnerCorners;
        this.eyeOuterCorners = eyeOuterCorners;
        this.mouthHeight = mouthHeight;
        this.mouthLipSize = mouthLipSize;
        this.mouthSmiling = mouthSmiling;
        this.browHeight = browHeight;
        this.browLength = browLength;
        this.browSpace = browSpace;
        this.browSize = browSize;
        this.browCorner = browCorner;
    }
}


