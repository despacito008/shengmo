package com.aiwujie.shengmo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class QCheckBox extends android.support.v7.widget.AppCompatCheckBox {
    public QCheckBox(Context context) {
        super(context);
    }

    public QCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        return callOnClick();
    }
}
