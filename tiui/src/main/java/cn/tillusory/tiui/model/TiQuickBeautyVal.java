package cn.tillusory.tiui.model;

import cn.tillusory.sdk.bean.TiFilterEnum;
import cn.tillusory.sdk.bean.TiOnekeyBeautyEnum;

public enum TiQuickBeautyVal {
   // STANDARD_QUICK_BEAUTY(TiOnekeyBeautyEnum.STANDARD_ONEKEY_BEAUTY, 70, 70, 0, 40, 60, 0, 60, 60, 15, 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    STANDARD_QUICK_BEAUTY(TiOnekeyBeautyEnum.STANDARD_ONEKEY_BEAUTY, 30, 50, 0, 20, 100, 0, 60, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    DELICATE_QUICK_BEAUTY(TiOnekeyBeautyEnum.DELICATE__ONEKEY_BEAUTY, 50, 50, 0, 20, 60, 0, 50, 40, 30, 10, -20, 10, 10, 0, -10, 0, 0, "niunai", 100, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), //素肌-艾瑟瑞尔
    CUTE_QUICK_BEAUTY(TiOnekeyBeautyEnum.LOVELY_ONEKEY_BEAUTY, 40, 40, 0, 70, 60, 0, 50, 20, 20, 15, 0, 10, 0, -20, -10, -10, 0, "fenxia", 100, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), //蜜桃-拜尔斯
    CELEBRITY_QUICK_BEAUTY(TiOnekeyBeautyEnum.INTERNET_CELEBRITY_ONEKEY_BEAUTY, 50, 60, 0, 30, 60, 0, 70, 60, 40, 30, -40, 20, 20, -20, -30, -5, 0, "zhigan", 100, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),  //奶茶-阿瓦
    NATURAL_QUICK_BEAUTY(TiOnekeyBeautyEnum.NATURAL_ONEKEY_BEAUTY, 40, 50, 0, 50, 60, 0, 40, 30, 30, 5, 0, 10, 0, 0, 0, 0, 0, "suyan", 100, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),   //浅茶-姜戈
    LOLITA_QUICK_BEAUTY(TiOnekeyBeautyEnum.LOLITA_ONEKEY_BEAUTY, 50, 50, 0, 80, 60, 0, 70, 20, 20, 10, -15, 15, 0, -20, -30, -10, 0, "qiangwei", 100, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),   //初夏-那什维尔
    ELEGANT_QUICK_BEAUTY(TiOnekeyBeautyEnum.ELEGANT_ONEKEY_BEAUTY, 50, 45, 0, 20, 60, 0, 30, 25, 25, 0, 0, 15, 10, -15, 0, 0, 0, "naixing", 100, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),    //桔梗
    FIRST_LOVE_QUICK_BEAUTY(TiOnekeyBeautyEnum.FIRST_LOVE_ONEKEY_BEAUTY, 40, 50, 0, 40, 60, 0, 50, 25, 20, 10, 0, 10, 0, -10, -20, -5, 0, "mitaowulong", 100, 34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),  //氧气
    GODDESS_QUICK_BEAUTY(TiOnekeyBeautyEnum.GODNESS_ONEKEY_BEAUTY, 60, 40, 0, 30, 60, 0, 60, 40, 30, 10, -30, 20, 10, -20, -30, -10, 0, "zhongxiameng", 100, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), //可可
    SENIOR_QUICK_BEAUTY(TiOnekeyBeautyEnum.ADVANCED_ONEKEY_BEAUTY, 30, 30, 0, 10, 60, 0, 25, 50, 20, 0, 20, 10, 10, -30, 30, 5, 0, "huidiao", 100, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),   //高级灰-小新
    LOW_END_QUICK_BEAUTY(TiOnekeyBeautyEnum.LOW_END_ONEKEY_BEAUTY, 0, 0, 0, 0, 0, 0, 70, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public TiOnekeyBeautyEnum onekeyBeautyEnum;

    public int whitening;
    public int blemishRemoval;
    public int preciseBeauty;
    public int tenderness;
    public int sharpness;
    public int brightness;

    public int eyeMagnify;
    public int chinSlim;
    public int faceNarrow;
    public int jawTransform;
    public int foreheadTransform;
    public int noseMinify;
    public int noseElongate;
    public int eyeSpace;
    public int eyeCorner;
    public int mouthTransform;
    public int teethWhiten;

    public String filterName;
    public int filterVal;
    public int filterPosition;

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

    TiQuickBeautyVal(TiOnekeyBeautyEnum onekeyBeautyEnum, int whitening, int blemishRemoval, int preciseBeauty, int tenderness, int sharpness, int brightness, int eyeMagnify, int chinSlim, int faceNarrow, int jawTransform, int foreheadTransform, int noseMinify, int noseElongate, int eyeSpace, int eyeCorner, int mouthTransform, int teethWhiten, String filterName, int filterVal, int filterPosition, int cheekboneSlim, int jawboneSlim, int jawSlim, int eyeInnerCorners, int eyeOuterCorners, int mouthHeight, int mouthLipSize, int mouthSmiling, int browHeight, int browLength, int browSpace, int browSize, int browCorner) {
        this.onekeyBeautyEnum = onekeyBeautyEnum;
        this.whitening = whitening;
        this.blemishRemoval = blemishRemoval;
        this.preciseBeauty = preciseBeauty;
        this.tenderness = tenderness;
        this.sharpness = sharpness;
        this.brightness = brightness;
        this.eyeMagnify = eyeMagnify;
        this.chinSlim = chinSlim;
        this.faceNarrow = faceNarrow;
        this.jawTransform = jawTransform;
        this.foreheadTransform = foreheadTransform;
        this.noseMinify = noseMinify;
        this.noseElongate = noseElongate;
        this.eyeSpace = eyeSpace;
        this.eyeCorner = eyeCorner;
        this.mouthTransform = mouthTransform;
        this.teethWhiten = teethWhiten;
        this.filterName = filterName;
        this.filterVal = filterVal;
        this.filterPosition = filterPosition;
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



