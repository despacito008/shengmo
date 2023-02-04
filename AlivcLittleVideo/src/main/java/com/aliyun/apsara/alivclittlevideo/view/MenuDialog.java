package com.aliyun.apsara.alivclittlevideo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.constants.AnimationParam;
import com.aliyun.svideo.common.utils.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * 底部导航栏菜单 dialog
 * 包含 视频编辑和视频录制的入口
 *
 * @author xlx
 */
public class MenuDialog extends Dialog implements View.OnClickListener {

    private WeakReference<Context> weakReference;
    private ImageView ivClose;
    private LinearLayout llEditor;
    private LinearLayout llRecorder;

    /**
     * close 图标的右侧和底部位置
     */
    private int closeRight;
    private int closeBottom;

    /**
     * 当前dialog状态, 用于控制不同动画
     */
    private CurrentState currentState = CurrentState.State_Open;

    /**
     * 菜单item 点击
     */
    private OnMenuItemClickListener onMenuItemClickListener;

    public MenuDialog(@NonNull Context context) {
        super(context, R.style.menuDialog);
        weakReference = new WeakReference<Context>(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        init();
    }

    private void init() {
        initWindow();
        initView();
    }

    private void initView() {
        Context context = weakReference.get();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_menu_dialog, null, false);
        ivClose = view.findViewById(R.id.iv_close);
        llEditor = view.findViewById(R.id.ll_editor);
        llRecorder = view.findViewById(R.id.ll_recorder);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeViewDissmiss();
                chooseEditorAnimator();
                chooseRecorderAnimator();
            }
        });

        llEditor.setOnClickListener(this);
        llRecorder.setOnClickListener(this);

        setContentView(view);
    }

    private void initWindow() {
        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        Context context = weakReference.get();
        if (context != null) {
            int screenWidth = ScreenUtils.getWidth(weakReference.get());
            int screenHeight = ScreenUtils.getHeight(weakReference.get());
            params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
            getWindow().setAttributes(params);
            setCanceledOnTouchOutside(true);
        }
    }


    @Override
    public void show() {
        super.show();
        closeViewShow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rect = new Rect();
        ivClose.getLocalVisibleRect(rect);
        closeRight = rect.right;
        closeBottom = rect.bottom;
        chooseEditorAnimator();
        chooseRecorderAnimator();
    }

    /**
     * 录制按钮动画
     */
    private void chooseRecorderAnimator() {
        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = null;
        ScaleAnimation scaleAnimation = null;
        if (currentState == CurrentState.State_Open) {
            // 显示的平移动画
            translateAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, -closeRight,
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0,
                TranslateAnimation.ABSOLUTE, closeBottom,
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0);

            // 显示的缩放动画
            scaleAnimation = new ScaleAnimation(AnimationParam.Dialog.SCALE_TIMES_0, AnimationParam.Dialog.SCALE_TIMES_1, AnimationParam.Dialog.SCALE_TIMES_0, AnimationParam.Dialog.SCALE_TIMES_1, ScaleAnimation.RELATIVE_TO_SELF,

                                                AnimationParam.Dialog.PIVOT_X_HALF_SELF, ScaleAnimation.RELATIVE_TO_SELF,

                                                AnimationParam.Dialog.PIVOT_Y_HALF_SELF);
        } else if (currentState == CurrentState.State_Close) {
            // 隐藏的平移动画
            translateAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0,
                TranslateAnimation.ABSOLUTE, (int)(1.5 * -closeRight),
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0,
                TranslateAnimation.ABSOLUTE, (int)(1.5 * closeBottom));

            // 隐藏的缩放动画
            scaleAnimation = new ScaleAnimation(AnimationParam.Dialog.SCALE_TIMES_1, AnimationParam.Dialog.SCALE_TIMES_0, AnimationParam.Dialog.SCALE_TIMES_1, AnimationParam.Dialog.SCALE_TIMES_0, ScaleAnimation.RELATIVE_TO_SELF,
                                                AnimationParam.Dialog.PIVOT_X_HALF_SELF, ScaleAnimation.RELATIVE_TO_SELF,
                                                AnimationParam.Dialog.PIVOT_Y_HALF_SELF);
        }
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        animationSet.setDuration(AnimationParam.Dialog.ANIMATION_SET_DURATION);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new OvershootInterpolator(AnimationParam.Dialog.INTERCEPTOR_TENSION));
        llEditor.clearAnimation();
        llEditor.setAnimation(animationSet);
    }

    /**
     * 编辑按钮动画
     */
    private void chooseEditorAnimator() {
        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = null;
        ScaleAnimation scaleAnimation = null;
        if (currentState == CurrentState.State_Open) {
            // 显示的平移动画
            translateAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, closeRight,
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0,
                TranslateAnimation.ABSOLUTE, closeBottom,
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0);

            // 显示的缩放动画
            scaleAnimation = new ScaleAnimation(AnimationParam.Dialog.SCALE_TIMES_0, AnimationParam.Dialog.SCALE_TIMES_1, AnimationParam.Dialog.SCALE_TIMES_0, AnimationParam.Dialog.SCALE_TIMES_1, ScaleAnimation.RELATIVE_TO_SELF,
                                                AnimationParam.Dialog.PIVOT_X_HALF_SELF, ScaleAnimation.RELATIVE_TO_SELF,
                                                AnimationParam.Dialog.PIVOT_Y_HALF_SELF);
        } else if (currentState == CurrentState.State_Close) {
            // 隐藏的平移动画
            translateAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0,
                TranslateAnimation.ABSOLUTE, (int)(1.5 * closeRight),
                TranslateAnimation.ABSOLUTE, AnimationParam.Dialog.TRANSLATE_VALUE_0,
                TranslateAnimation.ABSOLUTE, (int)(1.5 * closeBottom));

            // 隐藏的缩放动画
            scaleAnimation = new ScaleAnimation(AnimationParam.Dialog.SCALE_TIMES_1, AnimationParam.Dialog.SCALE_TIMES_0, AnimationParam.Dialog.SCALE_TIMES_1, AnimationParam.Dialog.SCALE_TIMES_0, ScaleAnimation.RELATIVE_TO_SELF,
                                                AnimationParam.Dialog.PIVOT_X_HALF_SELF, ScaleAnimation.RELATIVE_TO_SELF,
                                                AnimationParam.Dialog.PIVOT_Y_HALF_SELF);
        }
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        animationSet.setDuration(AnimationParam.Dialog.ANIMATION_SET_DURATION);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new OvershootInterpolator(AnimationParam.Dialog.INTERCEPTOR_TENSION));
        llRecorder.clearAnimation();
        llRecorder.setAnimation(animationSet);
    }

    /**
     * 关闭按钮显示动画
     */
    private void closeViewShow() {
        currentState = CurrentState.State_Open;
        RotateAnimation rotateAnimation = new RotateAnimation(AnimationParam.Dialog.ROTATION_DEGREES_0, AnimationParam.Dialog.ROTATION_DEGREES_90,
                RotateAnimation.RELATIVE_TO_SELF, AnimationParam.Dialog.PIVOT_X_HALF_SELF,
                RotateAnimation.RELATIVE_TO_SELF, AnimationParam.Dialog.PIVOT_Y_HALF_SELF);
        rotateAnimation.setDuration(AnimationParam.Dialog.CLOSE_BUTTON_DURATION_SHOW);
        rotateAnimation.setFillAfter(true);
        ivClose.setAnimation(rotateAnimation);
    }

    /**
     * 关闭按钮隐藏动画
     */
    private void closeViewDissmiss() {
        currentState = CurrentState.State_Close;
        ivClose.clearAnimation();
        RotateAnimation rotateAnimation = new RotateAnimation(AnimationParam.Dialog.ROTATION_DEGREES_90, AnimationParam.Dialog.ROTATION_DEGREES_0,
                RotateAnimation.RELATIVE_TO_SELF, AnimationParam.Dialog.PIVOT_X_HALF_SELF,
                RotateAnimation.RELATIVE_TO_SELF, AnimationParam.Dialog.PIVOT_Y_HALF_SELF);
        rotateAnimation.setDuration(AnimationParam.Dialog.CLOSE_BUTTON_DURATION_DISMISS);
        rotateAnimation.setFillAfter(true);
        ivClose.setAnimation(rotateAnimation);
        rotateAnimation.start();

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_editor) {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onEditroClick();
            }
            dismiss();
        } else if (v.getId() == R.id.ll_recorder) {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onRecorderClick();
            }
            dismiss();
        }
    }

    /**
     * 设置菜单item点击listener
     * @param listener OnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.onMenuItemClickListener = listener;
    }

    /**
     * dialog 状态, 用于控制动画
     */
    enum CurrentState {
        /**
         * 显示状态
         */
        State_Open,
        /**
         * 关闭状态
         */
        State_Close
    }

    /**
     * 菜单item点击 listener
     */
    public interface OnMenuItemClickListener {
        /**
         * 视频编辑click
         */
        void onEditroClick();

        /**
         * 视频录制click
         */
        void onRecorderClick();
    }
}
