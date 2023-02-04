package com.aiwujie.shengmo.view.indicator;

import android.support.annotation.AnimatorRes;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;

public class CircleIndicatorConfig {

    int width = -1;
    int height = -1;
    int margin = -1;
    @AnimatorRes
    int animatorResId = R.animator.scale_with_alpha;
    @AnimatorRes int animatorReverseResId = 0;
    @DrawableRes
    int backgroundResId = R.drawable.white_radius;
    @DrawableRes int unselectedBackgroundId;
    int orientation = LinearLayout.HORIZONTAL;
    int gravity = Gravity.CENTER;

    CircleIndicatorConfig() {
    }

    public static class Builder {

        private final CircleIndicatorConfig mConfig;

        public Builder() {
            mConfig = new CircleIndicatorConfig();
        }

        public Builder width(int width) {
            mConfig.width = width;
            return this;
        }

        public Builder height(int height) {
            mConfig.height = height;
            return this;
        }

        public Builder margin(int margin) {
            mConfig.margin = margin;
            return this;
        }

        public Builder animator(@AnimatorRes int animatorResId) {
            mConfig.animatorResId = animatorResId;
            return this;
        }

        public Builder animatorReverse(@AnimatorRes int animatorReverseResId) {
            mConfig.animatorReverseResId = animatorReverseResId;
            return this;
        }

        public Builder drawable(@DrawableRes int backgroundResId) {
            mConfig.backgroundResId = backgroundResId;
            return this;
        }

        public Builder drawableUnselected(@DrawableRes int unselectedBackgroundId) {
            mConfig.unselectedBackgroundId = unselectedBackgroundId;
            return this;
        }

        public Builder orientation(int orientation) {
            mConfig.orientation = orientation;
            return this;
        }

        public Builder gravity(int gravity) {
            mConfig.gravity = gravity;
            return this;
        }

        public CircleIndicatorConfig build() {
            return mConfig;
        }
    }
}