package com.tencent.qcloud.tim.tuikit.live.component.gift;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;

public class XmLottieAnimationView extends LottieAnimationView {
    public XmLottieAnimationView(Context context) {
        super(context);
    }

    public XmLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XmLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            super.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playAnimation() {
        super.playAnimation();
    }
}
