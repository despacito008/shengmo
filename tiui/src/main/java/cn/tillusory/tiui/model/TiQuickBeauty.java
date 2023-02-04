package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import cn.tillusory.tiui.R;

public enum TiQuickBeauty {
    STANDARD(R.string.standard, TiQuickBeautyVal.STANDARD_QUICK_BEAUTY, R.drawable.ic_ti_standard),
    DELICATE(R.string.delicate, TiQuickBeautyVal.DELICATE_QUICK_BEAUTY, R.drawable.ic_ti_delicate),
    CUTE(R.string.cute, TiQuickBeautyVal.CUTE_QUICK_BEAUTY, R.drawable.ic_ti_cute),
    CELEBRITY(R.string.celebrity, TiQuickBeautyVal.CELEBRITY_QUICK_BEAUTY, R.drawable.ic_ti_celebrity),
    NATURAL(R.string.natural, TiQuickBeautyVal.NATURAL_QUICK_BEAUTY, R.drawable.ic_ti_natural),
    LOLITA(R.string.lolita, TiQuickBeautyVal.LOLITA_QUICK_BEAUTY, R.drawable.ic_ti_lolita),
    ELEGANT(R.string.elegant, TiQuickBeautyVal.ELEGANT_QUICK_BEAUTY, R.drawable.ic_ti_elegant),
    FIRST_LOVE(R.string.first_love, TiQuickBeautyVal.FIRST_LOVE_QUICK_BEAUTY, R.drawable.ic_ti_first_love),
    GODDESS(R.string.goddess, TiQuickBeautyVal.GODDESS_QUICK_BEAUTY, R.drawable.ic_ti_goddess),
    SENIOR(R.string.senior, TiQuickBeautyVal.SENIOR_QUICK_BEAUTY, R.drawable.ic_ti_senior),
    LOW_END(R.string.low_end, TiQuickBeautyVal.LOW_END_QUICK_BEAUTY, R.drawable.ic_ti_low_end);

    private int stringId;
    private TiQuickBeautyVal quickBeautyVal;
    private int imageId;

    TiQuickBeauty(int stringId, TiQuickBeautyVal quickBeautyVal, int imageId) {
        this.stringId = stringId;
        this.quickBeautyVal = quickBeautyVal;
        this.imageId = imageId;
    }

    public String getString(@NonNull Context context) {
        return context.getResources().getString(stringId);
    }

    public Drawable getImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(imageId);
    }

    public TiQuickBeautyVal getQuickBeautyVal() {
        return quickBeautyVal;
    }
}
