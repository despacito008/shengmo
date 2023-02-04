package com.aliyun.apsara.alivclittlevideo.constants;

/**
 * 动画参数
 * @author xlx
 */
public class AnimationParam {

    /**
     * dialog中的动画参数
     */
    public static class Dialog {
        /**
         * 视频编辑和视频录制按钮的动画时长
         */
        public static final int ANIMATION_SET_DURATION = 800;

        /**
         * close 按钮的动画时长---显示
         */
        public static final int CLOSE_BUTTON_DURATION_SHOW = 500;

        /**
         * close 按钮的动画时长---隐藏
         */
        public static final int CLOSE_BUTTON_DURATION_DISMISS = 500;


        /**
         * pivotXValue
         */
        public static final float PIVOT_X_HALF_SELF = 0.5f;

        /**
         * pivotYValue
         */
        public static final float PIVOT_Y_HALF_SELF = 0.5f;

        /**
         * Interpolator 参数
         */
        public static final int INTERCEPTOR_TENSION = 1;

        /**
         * 旋转动画角度
         */
        public static final int ROTATION_DEGREES_0 = 0;
        public static final int ROTATION_DEGREES_90 = 90;

        /**
         * 缩放值
         */
        public static final float SCALE_TIMES_0 = 0f;
        public static final float SCALE_TIMES_1 = 1.0f;

        /**
         * 位移值
         */
        public static final float TRANSLATE_VALUE_0 = 0f;
        public static final float TRANSLATE_VALUE_1 = 1.0f;

        /**
         * 透明度
         */
        public static final float ALPHA_VALUE_0 = 0f;
        public static final float ALPHA_VALUE_1 = 1f;
    }

    /**
     * 视频列表动画参数
     */
    public static class VideoList {
        /**
         * 动画执行时间
         */
        public static final int ANIMATION_DURATION = 200;

        /**
         * 透明度动画最大值
         */
        public static final float ANIMATION_ALPHA_MAX_VALUE = 1.0f;

        /**
         * 透明度动画最小值
         */
        public static final float ANIMATION_ALPHA_MIN_VALUE = 0f;
    }
}
