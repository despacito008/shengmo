package com.tencent.qcloud.tim.tuikit.live.component.topbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    String TAG = "MarqueeTextView";
    String BLANK = " ";
    final int REPEAT_SINGLE = 1; //一次结束
    final int REPEAT_SINGLE_LOOP = 0; //单个循序
    final int REPEAT_FILL_LOOP = -1; // 填充后循环

    float speed = 1f;
    public void setSpeed(float speed) {
        if (speed <= 0) {
            this.speed = 0f;
        } else {
            this.speed = speed;
        }
    }

    String text = "";
    public void setText(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

    }

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
