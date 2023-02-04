package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import cn.tillusory.tiui.R;

public enum TiGreenScreenEdit {
    SIMILARITY(R.string.green_screen_similarity, R.drawable.ic_ti_green_screen_similarity),
    SMOOTHNESS(R.string.green_screen_smoothness, R.drawable.ic_ti_green_screen_smoothness),
    ALPHA(R.string.green_screen_alpha, R.drawable.ic_ti_green_screen_alpha);

    private int stringId;
    private int imageId;

    TiGreenScreenEdit(@StringRes int stringId, @DrawableRes int imageId) {
        this.stringId = stringId;
        this.imageId = imageId;
    }

    public String getString(@NonNull Context context) {
        return context.getResources().getString(stringId);
    }

    public Drawable getImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(imageId);
    }
}
