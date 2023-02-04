package cn.tillusory.tiui.custom;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwangjr.rxbus.RxBus;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiFilterEnum;
import cn.tillusory.sdk.bean.TiOnekeyBeautyEnum;
import cn.tillusory.tiui.model.RxBusAction;
import cn.tillusory.tiui.model.TiBeautyFilter;
import cn.tillusory.tiui.model.TiFaceShapeVal;
import cn.tillusory.tiui.model.TiQuickBeautyVal;
import cn.tillusory.tiui.model.TiSelectedPosition;
import cn.tillusory.tiui.view.TiBarView;

public class TiSharePreferences {

    //美颜
    public static final int WHITE_DEFAULT = 30;
    public static final int BLEMISH_REMOVAL_DEFAULT = 50;
    public static final int PRECISE_BEAUTY_DEFAULT = 0;
    public static final int TENDERNESS_DEFAULT = 20;
    public static final int SHARPNESS_DEFAULT = 100;
    public static final int BRIGHTNESS_DEFAULT = 0;

    private static final String SP_KEY_WHITE = "SP_KEY_WHITE";
    private static final String SP_KEY_BLEMISH_REMOVAL = "SP_KEY_BLEMISH_REMOVAL";
    private static final String SP_KEY_PRECISE_BEAUTY = "SP_KEY_PRECISE_BEAUTY";
    private static final String SP_KEY_TENDERNESS = "SP_KEY_TENDERNESS";
    private static final String SP_KEY_SHARPNESS = "SP_KEY_SHARPNESS";
    private static final String SP_KEY_BRIGHTNESS = "SP_KEY_BRIGHTNESS";

    //美型
    public static final int EYE_MAGNIFY_DEFAULT = 60;
    public static final int CHIN_SLIM_DEFAULT = 30;
    public static final int FACE_NARROW_DEFAULT = 0;

    public static final int CHEEKBONE_SLIM_DEFAULT = 0;
    public static final int JAWBONE_SLIM_DEFAULT = 0;
    public static final int JAW_TRANSFORM_DEFAULT = 0;
    public static final int JAW_SLIM_DEFAULT = 0;
    public static final int FOREHEAD_TRANSFORM_DEFAULT = 0;

    public static final int EYE_INNER_CORNERS_DEFAULT = 0;
    public static final int EYE_OUTER_CORNERS_DEFAULT = 0;
    public static final int EYE_SPACE_DEFAULT = 0;
    public static final int EYE_CORNERS_DEFAULT = 0;

    public static final int NOSE_MINIFY_DEFAULT = 0;
    public static final int NOSE_ELONGATE_DEFAULT = 0;

    public static final int MOUTH_TRANSFORM_DEFAULT = 0;
    public static final int MOUTH_HEIGHT_DEFAULT = 0;
    public static final int MOUTH_LIP_SIZE_DEFAULT = 0;
    public static final int MOUTH_SMILING_DEFAULT = 0;

    public static final int BROW_HEIGHT_DEFAULT = 0;
    public static final int BROW_LENGTH_DEFAULT = 0;
    public static final int BROW_SPACE_DEFAULT = 0;
    public static final int BROW_SIZE_DEFAULT = 0;
    public static final int BROW_CORNER_DEFAULT = 0;

    private static final String SP_KEY_EYE_MAGNIFY = "SP_KEY_EYE_MAGNIFY";
    private static final String SP_KEY_CHIN_SLIM = "SP_KEY_CHIN_SLIM";
    private static final String SP_KEY_FACE_NARROW = "SP_KEY_FACE_NARROW";

    private static final String SP_KEY_CHEEKBONE_SLIM = "SP_KEY_CHEEKBONE_SLIM";
    private static final String SP_KEY_JAWBONE_SLIM = "SP_KEY_JAWBONE_SLIM";
    private static final String SP_KEY_JAW_TRANSFORM = "SP_KEY_JAW_TRANSFORM";
    private static final String SP_KEY_JAW_SLIM = "SP_KEY_JAW_SLIM";
    private static final String SP_KEY_FOREHEAD_TRANSFORM = "SP_KEY_FOREHEAD_TRANSFORM";

    private static final String SP_KEY_EYE_INNER_CORNERS = "SP_KEY_EYE_INNER_CORNERS";
    private static final String SP_KEY_EYE_OUTER_CORNERS = "SP_KEY_EYE_OUTER_CORNERS";
    private static final String SP_KEY_EYE_SPACE = "SP_KEY_EYE_SPACE";
    private static final String SP_KEY_EYE_CORNERS = "SP_KEY_EYE_CORNERS";

    private static final String SP_KEY_NOSE_MINIFY = "SP_KEY_NOSE_MINIFY";
    private static final String SP_KEY_NOSE_ELONGATE = "SP_KEY_NOSE_ELONGATE";

    private static final String SP_KEY_MOUTH_TRANSFORM = "SP_KEY_MOUTH_TRANSFORM";
    private static final String SP_KEY_MOUTH_HEIGHT = "SP_KEY_MOUTH_HEIGHT";
    private static final String SP_KEY_MOUTH_LIP_SIZE = "SP_KEY_MOUTH_LIP_SIZE";
    private static final String SP_KEY_MOUTH_SMILING = "SP_KEY_MOUTH_SMILING";

    private static final String SP_KEY_BROW_HEIGHT = "SP_KEY_BROW_HEIGHT";
    private static final String SP_KEY_BROW_LENGTH = "SP_KEY_BROW_LENGTH";
    private static final String SP_KEY_BROW_SPACE = "SP_KEY_BROW_SPACE";
    private static final String SP_KEY_BROW_SIZE = "SP_KEY_BROW_SIZE";
    private static final String SP_KEY_BROW_CORNER = "SP_KEY_BROW_CORNER";

    //一键美颜
    public static final int QUICK_BEAUTY_STANDARD_DEFAULT = 100;
    public static final int QUICK_BEAUTY_DELICATE_DEFAULT = 100;
    public static final int QUICK_BEAUTY_CUTE_DEFAULT = 100;
    public static final int QUICK_BEAUTY_CELEBRITY_DEFAULT = 100;
    public static final int QUICK_BEAUTY_NATURAL_DEFAULT = 100;

    //脸型
    public static final int FACE_SHAPE_CLASSIC_DEFAULT = 100;
    public static final int FACE_SHAPE_SQUARE_DEFAULT = 100;
    public static final int FACE_SHAPE_LONG_DEFAULT = 100;
    public static final int FACE_SHAPE_ROUND_DEFAULT = 100;
    public static final int FACE_SHAPE_THIN_DEFAULT = 100;

    //滤镜
    public static final int BEAUTY_FILTER_DEFAULT = 100;

    //绿幕抠图编辑
    private static final String SP_KEY_GREEN_SCREEN_SIMILARITY = "SP_KEY_GREEN_SCREEN_SIMILARITY";
    private static final String SP_KEY_GREEN_SCREEN_SMOOTHNESS = "SP_KEY_GREEN_SCREEN_SMOOTHNESS";
    private static final String SP_KEY_GREEN_SCREEN_ALPHA = "SP_KEY_GREEN_SCREEN_ALPHA";

    public static final int GREEN_SCREEN_SIMILARITY_DEFAULT = 0;
    public static final int GREEN_SCREEN_SMOOTHNESS_DEFAULT = 0;
    public static final int GREEN_SCREEN_ALPHA_DEFAULT = 0;

    private static TiSharePreferences instance;
    private SharedPreferences sharedPreferences;
    private TiSDKManager tiSDKManager;

    public static final String SP_KEY_UI_VERSION = "SP_KEY_UI_VERSION";

    private static final String SP_KEY_MAKEUP_ENABLE = "SP_KEY_MAKEUP_ENABLE";

    private static final String SP_KEY_RESET_MAKEUP_ENABLE = "SP_KEY_RESET_MAKEUP_ENABLE";
    private static final String SP_KEY_RESET_BEAUTY_ENABLE = "SP_KEY_RESET_BEAUTY_ENABLE";
    private static final String SP_KEY_RESTORE_GREEN_SCREEN_ENABLE = "SP_KEY_RESTORE_GREEN_SCREEN_ENABLE";

    public static TiSharePreferences getInstance() {
        if (instance == null) {
            synchronized (TiSharePreferences.class) {
                if (instance == null) {
                    instance = new TiSharePreferences();
                }
            }
        }
        return instance;
    }

    public void init(Context context, TiSDKManager tiSDKManager) {
        sharedPreferences = context.getSharedPreferences("TiSharePreferences", Context.MODE_PRIVATE);
        this.tiSDKManager = tiSDKManager;
    }

    private void putBooleanVal(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void putIntVal(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void putStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * UI版本
     */
    public void putUIVersion(String value) {
        putStringValue(SP_KEY_UI_VERSION, value);
    }

    public String getUIVersion() {
        return sharedPreferences.getString(SP_KEY_UI_VERSION, "");
    }

    /**
     * 一键美颜
     */
    public void putQuickBeautyVal(String key, int value) {
        putIntVal(key, value);
    }

    public int getQuickBeautyVal(String key) {
        return sharedPreferences.getInt(key, 100);
    }

    /**
     * 脸型
     */
    public void putFaceShapeVal(String key, int value) {
        putIntVal(key, value);
    }

    public int getFaceShapeVal(String key) {
        return sharedPreferences.getInt(key, 100);
    }

    /**
     * 美颜
     */
    public void putWhiteningVal(int value) {
        putIntVal(SP_KEY_WHITE, value);
    }

    public int getWhiteningVal() {
        return sharedPreferences.getInt(SP_KEY_WHITE, WHITE_DEFAULT);
    }

    public void putBlemishRemovalVal(int value) {
        putIntVal(SP_KEY_BLEMISH_REMOVAL, value);
    }

    public int getBlemishRemovalVal() {
        return sharedPreferences.getInt(SP_KEY_BLEMISH_REMOVAL, BLEMISH_REMOVAL_DEFAULT);
    }

    public void putPreciseBeautyVal(int value) {
        putIntVal(SP_KEY_PRECISE_BEAUTY, value);
    }

    public int getPreciseBeautyVal() {
        return sharedPreferences.getInt(SP_KEY_PRECISE_BEAUTY, PRECISE_BEAUTY_DEFAULT);
    }

    public void putTendernessVal(int value) {
        putIntVal(SP_KEY_TENDERNESS, value);
    }

    public int getTendernessVal() {
        return sharedPreferences.getInt(SP_KEY_TENDERNESS, TENDERNESS_DEFAULT);
    }

    public void putSharpnessVal(int value) {
        putIntVal(SP_KEY_SHARPNESS, value);
    }

    public int getSharpnessVal() {
        return sharedPreferences.getInt(SP_KEY_SHARPNESS, SHARPNESS_DEFAULT);
    }

    public void putBrightnessVal(int value) {
        putIntVal(SP_KEY_BRIGHTNESS, value);
    }

    public int getBrightnessVal() {
        return sharedPreferences.getInt(SP_KEY_BRIGHTNESS, BRIGHTNESS_DEFAULT);
    }

    /**
     * 美型
     */
    public void putEyeMagnifyingVal(int value) {
        putIntVal(SP_KEY_EYE_MAGNIFY, value);
    }

    public int getEyeMagnifyingVal() {
        return sharedPreferences.getInt(SP_KEY_EYE_MAGNIFY, EYE_MAGNIFY_DEFAULT);
    }

    public void putChinSlimmingVal(int value) {
        putIntVal(SP_KEY_CHIN_SLIM, value);
    }

    public int getChinSlimmingVal() {
        return sharedPreferences.getInt(SP_KEY_CHIN_SLIM, CHIN_SLIM_DEFAULT);
    }

    public void putFaceNarrowingVal(int value) {
        putIntVal(SP_KEY_FACE_NARROW, value);
    }

    public int getFaceNarrowingVal() {
        return sharedPreferences.getInt(SP_KEY_FACE_NARROW, FACE_NARROW_DEFAULT);
    }
    //脸部
    public void putCheekboneSlimmingVal(int value) {
        putIntVal(SP_KEY_CHEEKBONE_SLIM, value);
    }

    public int getCheekboneSlimmingVal() {
        return sharedPreferences.getInt(SP_KEY_CHEEKBONE_SLIM, CHEEKBONE_SLIM_DEFAULT);
    }

    public void putJawboneSlimmingVal(int value) {
        putIntVal(SP_KEY_JAWBONE_SLIM, value);
    }

    public int getJawboneSlimmingVal() {
        return sharedPreferences.getInt(SP_KEY_JAWBONE_SLIM, JAWBONE_SLIM_DEFAULT);
    }

    public void putJawTransformingVal(int value) {
        putIntVal(SP_KEY_JAW_TRANSFORM, value);
    }

    public int getJawTransformingVal() {
        return sharedPreferences.getInt(SP_KEY_JAW_TRANSFORM, JAW_TRANSFORM_DEFAULT);
    }

    public void putJawSlimmingVal(int value) {
        putIntVal(SP_KEY_JAW_SLIM, value);
    }

    public int getJawSlimmingVal() {
        return sharedPreferences.getInt(SP_KEY_JAW_SLIM, JAW_SLIM_DEFAULT);
    }

    public void putForeheadTransformingVal(int value) {
        putIntVal(SP_KEY_FOREHEAD_TRANSFORM, value);
    }

    public int getForeheadTransformingVal() {
        return sharedPreferences.getInt(SP_KEY_FOREHEAD_TRANSFORM, FOREHEAD_TRANSFORM_DEFAULT);
    }
    //眼部
    public void putEyeInnerCornersVal(int value) {
        putIntVal(SP_KEY_EYE_INNER_CORNERS, value);
    }

    public int getEyeInnerCornersVal() {
        return sharedPreferences.getInt(SP_KEY_EYE_INNER_CORNERS, EYE_INNER_CORNERS_DEFAULT);
    }

    public void putEyeOuterCornersVal(int value) {
        putIntVal(SP_KEY_EYE_OUTER_CORNERS, value);
    }

    public int getEyeOuterCornersVal() {
        return sharedPreferences.getInt(SP_KEY_EYE_OUTER_CORNERS, EYE_OUTER_CORNERS_DEFAULT);
    }

    public void putEyeSpacingVal(int value) {
        putIntVal(SP_KEY_EYE_SPACE, value);
    }

    public int getEyeSpacingVal() {
        return sharedPreferences.getInt(SP_KEY_EYE_SPACE, EYE_SPACE_DEFAULT);
    }

    public void putEyeCornersVal(int value) {
        putIntVal(SP_KEY_EYE_CORNERS, value);
    }

    public int getEyeCornersVal() {
        return sharedPreferences.getInt(SP_KEY_EYE_CORNERS, EYE_CORNERS_DEFAULT);
    }
    //鼻子
    public void putNoseMinifyingVal(int value) {
        putIntVal(SP_KEY_NOSE_MINIFY, value);
    }

    public int getNoseMinifyingVal() {
        return sharedPreferences.getInt(SP_KEY_NOSE_MINIFY, NOSE_MINIFY_DEFAULT);
    }

    public void putNoseElongatingVal(int value) {
        putIntVal(SP_KEY_NOSE_ELONGATE, value);
    }

    public int getNoseElongatingVal() {
        return sharedPreferences.getInt(SP_KEY_NOSE_ELONGATE, NOSE_ELONGATE_DEFAULT);
    }
    //嘴巴
    public void putMouthTransformingVal(int value) {
        putIntVal(SP_KEY_MOUTH_TRANSFORM, value);
    }

    public int getMouthTransformingVal() {
        return sharedPreferences.getInt(SP_KEY_MOUTH_TRANSFORM, MOUTH_TRANSFORM_DEFAULT);
    }

    public void putMouthHeightVal(int value) {
        putIntVal(SP_KEY_MOUTH_HEIGHT, value);
    }

    public int getMouthHeightVal() {
        return sharedPreferences.getInt(SP_KEY_MOUTH_HEIGHT, MOUTH_HEIGHT_DEFAULT);
    }

    public void putMouthLipSizeVal(int value) {
        putIntVal(SP_KEY_MOUTH_LIP_SIZE, value);
    }

    public int getMouthLipSizeVal() {
        return sharedPreferences.getInt(SP_KEY_MOUTH_LIP_SIZE, MOUTH_LIP_SIZE_DEFAULT);
    }

    public void putMouthSmilingVal(int value) {
        putIntVal(SP_KEY_MOUTH_SMILING, value);
    }

    public int getMouthSmilingVal() {
        return sharedPreferences.getInt(SP_KEY_MOUTH_SMILING, MOUTH_SMILING_DEFAULT);
    }
    //眉毛
    public void putBrowHeightVal(int value) {
        putIntVal(SP_KEY_BROW_HEIGHT, value);
    }

    public int getBrowHeightVal() {
        return sharedPreferences.getInt(SP_KEY_BROW_HEIGHT, BROW_HEIGHT_DEFAULT);
    }

    public void putBrowLengthVal(int value) {
        putIntVal(SP_KEY_BROW_LENGTH, value);
    }

    public int getBrowLengthVal() {
        return sharedPreferences.getInt(SP_KEY_BROW_LENGTH, BROW_LENGTH_DEFAULT);
    }

    public void putBrowSpaceVal(int value) {
        putIntVal(SP_KEY_BROW_SPACE, value);
    }

    public int getBrowSpaceVal() {
        return sharedPreferences.getInt(SP_KEY_BROW_SPACE, BROW_SPACE_DEFAULT);
    }

    public void putBrowSizeVal(int value) {
        putIntVal(SP_KEY_BROW_SIZE, value);
    }

    public int getBrowSizeVal() {
        return sharedPreferences.getInt(SP_KEY_BROW_SIZE, BROW_SIZE_DEFAULT);
    }

    public void putBrowCornerVal(int value) {
        putIntVal(SP_KEY_BROW_CORNER, value);
    }

    public int getBrowCornerVal() {
        return sharedPreferences.getInt(SP_KEY_BROW_CORNER, BROW_CORNER_DEFAULT);
    }

    /**
     * 滤镜
     */
    public void putBeautyFilterVal(String key, int value) {
        String filterKey = "filter_" + key;
        putIntVal(filterKey, value);
    }

    public int getBeautyFilterVal(String key) {
        String filterKey = "filter_" + key;
        return sharedPreferences.getInt(filterKey, BEAUTY_FILTER_DEFAULT);
    }

    /**
     * 美妆
     */
    public void putMakeupEnable(boolean value) {
        putBooleanVal(SP_KEY_MAKEUP_ENABLE, value);
    }

    public boolean isMakeupEnable() {
        return sharedPreferences.getBoolean(SP_KEY_MAKEUP_ENABLE, false);
    }

    public void putBlusherVal(String key, int value) {
        String blusherKey = "blusher_" + key;
        putIntVal(blusherKey, value);
    }

    public int getBlusherVal(String key) {
        String blusherKey = "blusher_" + key;
        return sharedPreferences.getInt(blusherKey, 100);
    }

    public void putEyelashVal(String key, int value) {
        String eyelashKey = "eyelash" + key;
        putIntVal(eyelashKey, value);
    }

    public int getEyelashVal(String key) {
        String eyelashKey = "eyelash" + key;
        return sharedPreferences.getInt(eyelashKey, 100);
    }

    public void putEyebrowVal(String key, int value) {
        String eyebrowKey = "eyebrow" + key;
        putIntVal(eyebrowKey, value);
    }

    public int getEyebrowVal(String key) {
        String eyebrowKey = "eyebrow" + key;
        return sharedPreferences.getInt(eyebrowKey, 100);
    }

    public void putEyeshadowVal(String key, int value) {
        String eyeshadowKey = "eyeshadow" + key;
        putIntVal(eyeshadowKey, value);
    }

    public int getEyeshadowVal(String key) {
        String eyeshadowKey = "eyeshadow" + key;
        return sharedPreferences.getInt(eyeshadowKey, 100);
    }

    public void putEyelineVal(String key, int value) {
        String eyelineKey = "eyeline" + key;
        putIntVal(eyelineKey, value);
    }

    public int getEyelineVal(String key) {
        String eyelineKey = "eyeline" + key;
        return sharedPreferences.getInt(eyelineKey, 100);
    }

    /**
     * 绿幕抠图编辑
     */
    public void putGreenScreenSimilarityVal(int value) {
        putIntVal(SP_KEY_GREEN_SCREEN_SIMILARITY, value);
    }

    public int getGreenScreenSimilarityVal() {
        return sharedPreferences.getInt(SP_KEY_GREEN_SCREEN_SIMILARITY, GREEN_SCREEN_SIMILARITY_DEFAULT);
    }

    public void putGreenScreenSmoothnessVal(int value) {
        putIntVal(SP_KEY_GREEN_SCREEN_SMOOTHNESS, value);
    }

    public int getGreenScreenSmoothnessVal() {
        return sharedPreferences.getInt(SP_KEY_GREEN_SCREEN_SMOOTHNESS, GREEN_SCREEN_SMOOTHNESS_DEFAULT);
    }

    public void putGreenScreenAlphaVal(int value) {
        putIntVal(SP_KEY_GREEN_SCREEN_ALPHA, value);
    }

    public int getGreenScreenAlphaVal() {
        return sharedPreferences.getInt(SP_KEY_GREEN_SCREEN_ALPHA, GREEN_SCREEN_ALPHA_DEFAULT);
    }

    /**
     * 美颜、美妆的重置按钮
     */
    public void putBeautyResetEnable(boolean value) {
        putBooleanVal(SP_KEY_RESET_BEAUTY_ENABLE, value);
    }

    public boolean isBeautyResetEnable() {
        return sharedPreferences.getBoolean(SP_KEY_RESET_BEAUTY_ENABLE, false);
    }

    public void putMakeupResetEnable(boolean value) {
        putBooleanVal(SP_KEY_RESET_MAKEUP_ENABLE, value);
    }

    public boolean isMakeupResetEnable() {
        return sharedPreferences.getBoolean(SP_KEY_RESET_MAKEUP_ENABLE, false);
    }

    /**
     * 脸型关联值
     */
    public void putFaceShapeRelated(TiFaceShapeVal faceShapeVal, int progress) {
        float ratio = progress / 100f;

        tiSDKManager.setFaceTrimEnable(true);

        putEyeMagnifyingVal((int) (faceShapeVal.eyeMagnify * ratio));
        putChinSlimmingVal((int) (faceShapeVal.chinSlim * ratio));
        putFaceNarrowingVal((int) (faceShapeVal.faceNarrow * ratio));
        //脸部
        putCheekboneSlimmingVal((int) (faceShapeVal.cheekboneSlim * ratio));
        putJawboneSlimmingVal((int) (faceShapeVal.jawboneSlim * ratio));
        putJawTransformingVal((int) (faceShapeVal.jawTransform * ratio));
        putJawSlimmingVal((int) (faceShapeVal.jawSlim * ratio));
        putForeheadTransformingVal((int) (faceShapeVal.foreheadTransform * ratio));
        //眼部
        putEyeInnerCornersVal((int) (faceShapeVal.eyeInnerCorners * ratio));
        putEyeOuterCornersVal((int) (faceShapeVal.eyeOuterCorners * ratio));
        putEyeSpacingVal((int) (faceShapeVal.eyeSpace * ratio));
        putEyeCornersVal((int) (faceShapeVal.eyeCorner * ratio));
        //鼻子
        putNoseMinifyingVal((int) (faceShapeVal.noseMinify * ratio));
        putNoseElongatingVal((int) (faceShapeVal.noseElongate * ratio));
        //嘴巴
        putMouthTransformingVal((int) (faceShapeVal.mouthTransform * ratio));
        putMouthHeightVal((int) (faceShapeVal.mouthHeight * ratio));
        putMouthLipSizeVal((int) (faceShapeVal.mouthLipSize * ratio));
        putMouthSmilingVal((int) (faceShapeVal.mouthSmiling * ratio));
        //眉毛
        putBrowHeightVal((int) (faceShapeVal.browHeight * ratio));
        putBrowLengthVal((int) (faceShapeVal.browLength * ratio));
        putBrowSpaceVal((int) (faceShapeVal.browSpace * ratio));
        putBrowSizeVal((int) (faceShapeVal.browSize * ratio));
        putBrowCornerVal((int) (faceShapeVal.browCorner * ratio));
    }


    /**
     * 一键美颜关联值
     */
    public void putQuickBeautyRelated(TiQuickBeautyVal quickBeautyVal, int progress) {
        float ratio = progress / 100f;

        tiSDKManager.setBeautyEnable(true);
        putWhiteningVal((int) (quickBeautyVal.whitening * ratio));
        putBlemishRemovalVal((int) (quickBeautyVal.blemishRemoval * ratio));
        putPreciseBeautyVal((int) (quickBeautyVal.preciseBeauty * ratio));
        putBrightnessVal((int) (quickBeautyVal.brightness * ratio));
        putTendernessVal((int) (quickBeautyVal.tenderness * ratio));
        putSharpnessVal((int) (quickBeautyVal.sharpness * ratio));

        tiSDKManager.setFaceTrimEnable(true);
        putEyeMagnifyingVal((int) (quickBeautyVal.eyeMagnify * ratio));
        putChinSlimmingVal((int) (quickBeautyVal.chinSlim * ratio));
        putFaceNarrowingVal((int) (quickBeautyVal.faceNarrow * ratio));
        //脸部
        putCheekboneSlimmingVal((int) (quickBeautyVal.cheekboneSlim * ratio));
        putJawboneSlimmingVal((int) (quickBeautyVal.jawboneSlim * ratio));
        putJawTransformingVal((int) (quickBeautyVal.jawTransform * ratio));
        putJawSlimmingVal((int) (quickBeautyVal.jawSlim * ratio));
        putForeheadTransformingVal((int) (quickBeautyVal.foreheadTransform * ratio));
        //眼部
        putEyeInnerCornersVal((int) (quickBeautyVal.eyeInnerCorners * ratio));
        putEyeOuterCornersVal((int) (quickBeautyVal.eyeOuterCorners * ratio));
        putEyeSpacingVal((int) (quickBeautyVal.eyeSpace * ratio));
        putEyeCornersVal((int) (quickBeautyVal.eyeCorner * ratio));
        //鼻子
        putNoseMinifyingVal((int) (quickBeautyVal.noseMinify * ratio));
        putNoseElongatingVal((int) (quickBeautyVal.noseElongate * ratio));
        //嘴巴
        putMouthTransformingVal((int) (quickBeautyVal.mouthTransform * ratio));
        putMouthHeightVal((int) (quickBeautyVal.mouthHeight * ratio));
        putMouthLipSizeVal((int) (quickBeautyVal.mouthLipSize * ratio));
        putMouthSmilingVal((int) (quickBeautyVal.mouthSmiling * ratio));
        //眉毛
        putBrowHeightVal((int) (quickBeautyVal.browHeight * ratio));
        putBrowLengthVal((int) (quickBeautyVal.browLength * ratio));
        putBrowSpaceVal((int) (quickBeautyVal.browSpace * ratio));
        putBrowSizeVal((int) (quickBeautyVal.browSize * ratio));
        putBrowCornerVal((int) (quickBeautyVal.browCorner * ratio));

        putBeautyFilterVal(quickBeautyVal.filterName, (int) (quickBeautyVal.filterVal * ratio));
        TiSelectedPosition.POSITION_FILTER = quickBeautyVal.filterPosition;

        boolean enable = quickBeautyVal != TiQuickBeautyVal.STANDARD_QUICK_BEAUTY || progress != 100;
        RxBus.get().post(RxBusAction.ACTION_ENABLED_BTN_RESET, enable);

    }

    /**
     * 重置美颜
     */
    public void resetBeauty() {
        putQuickBeautyVal(TiQuickBeautyVal.STANDARD_QUICK_BEAUTY.name(), QUICK_BEAUTY_STANDARD_DEFAULT);
        putQuickBeautyVal(TiQuickBeautyVal.DELICATE_QUICK_BEAUTY.name(), QUICK_BEAUTY_DELICATE_DEFAULT);
        putQuickBeautyVal(TiQuickBeautyVal.CUTE_QUICK_BEAUTY.name(), QUICK_BEAUTY_CUTE_DEFAULT);
        putQuickBeautyVal(TiQuickBeautyVal.CELEBRITY_QUICK_BEAUTY.name(), QUICK_BEAUTY_CELEBRITY_DEFAULT);
        putQuickBeautyVal(TiQuickBeautyVal.NATURAL_QUICK_BEAUTY.name(), QUICK_BEAUTY_NATURAL_DEFAULT);

        putFaceShapeVal(TiFaceShapeVal.CLASSIC_FACE_SHAPE.name(), FACE_SHAPE_CLASSIC_DEFAULT);
        putFaceShapeVal(TiFaceShapeVal.SQUARE_FACE_SHAPE.name(), FACE_SHAPE_SQUARE_DEFAULT);
        putFaceShapeVal(TiFaceShapeVal.LONG_FACE_SHAPE.name(), FACE_SHAPE_LONG_DEFAULT);
        putFaceShapeVal(TiFaceShapeVal.ROUND_FACE_SHAPE.name(), FACE_SHAPE_ROUND_DEFAULT);
        putFaceShapeVal(TiFaceShapeVal.THIN_FACE_SHAPE.name(), FACE_SHAPE_THIN_DEFAULT);

        putBeautyFilterVal(TiBeautyFilter.NIUNAI_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.FENXIA_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.ZHIGAN_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.SUYAN_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.QIANGWEI_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.NAIXING_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.MITAOWULONG_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.ZHONGXIAMENG_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);
        putBeautyFilterVal(TiBeautyFilter.HUIDIAO_FILTER.getFilterName(), BEAUTY_FILTER_DEFAULT);

        tiSDKManager.setOnekeyBeauty(TiOnekeyBeautyEnum.STANDARD_ONEKEY_BEAUTY, QUICK_BEAUTY_STANDARD_DEFAULT);
        putQuickBeautyRelated(TiQuickBeautyVal.STANDARD_QUICK_BEAUTY, QUICK_BEAUTY_STANDARD_DEFAULT);

        TiSelectedPosition.POSITION_QUICK_BEAUTY = 0;
        TiSelectedPosition.POSITION_FACE_SHAPE = 0;

        TiBarView.tiFilterName = "";

    }

    /**
     * 重置美妆
     */
    public void resetMakeup() {
        putBlusherVal("qingse", 100);
        putBlusherVal("riza", 100);
        putBlusherVal("tiancheng", 100);
        putBlusherVal("youya", 100);
        putBlusherVal("weixun", 100);
        putBlusherVal("xindong", 100);

        putEyelashVal("ziran", 100);
        putEyelashVal("rouhe", 100);
        putEyelashVal("nongmi", 100);
        putEyelashVal("meihuo", 100);
        putEyelashVal("babi", 100);
        putEyelashVal("wumei", 100);

        putEyebrowVal("biaozhunmei", 100);
        putEyebrowVal("jianmei", 100);
        putEyebrowVal("liuyemei", 100);
        putEyebrowVal("pingzhimei", 100);
        putEyebrowVal("liuxingmei", 100);
        putEyebrowVal("oushimei", 100);

        putEyeshadowVal("dadi", 100);
        putEyeshadowVal("nvtuan", 100);
        putEyeshadowVal("xiari", 100);
        putEyeshadowVal("taohua", 100);
        putEyeshadowVal("yanxun", 100);
        putEyeshadowVal("yuanqi", 100);

        putEyelineVal("suyan", 100);
        putEyelineVal("rouhua", 100);
        putEyelineVal("shensui", 100);
        putEyelineVal("meihei", 100);
        putEyelineVal("gexing", 100);
        putEyelineVal("wugu", 100);
        putEyelineVal("qingqiao", 100);

    }

    /**
     * 绿幕编辑恢复按钮
     */
    public void putGreenScreenRestoreEnable(boolean value) {
        putBooleanVal(SP_KEY_RESTORE_GREEN_SCREEN_ENABLE, value);
    }

    public boolean isGreenScreenRestoreEnable() {
        return sharedPreferences.getBoolean(SP_KEY_RESTORE_GREEN_SCREEN_ENABLE, false);
    }

    /**
     * 重置绿幕编辑
     */
    public void restoreGreenScreenEdit() {
        putGreenScreenSimilarityVal(GREEN_SCREEN_SIMILARITY_DEFAULT);
        putGreenScreenSmoothnessVal(GREEN_SCREEN_SMOOTHNESS_DEFAULT);
        putGreenScreenAlphaVal(GREEN_SCREEN_ALPHA_DEFAULT);

        putGreenScreenRestoreEnable(false);
    }



}


