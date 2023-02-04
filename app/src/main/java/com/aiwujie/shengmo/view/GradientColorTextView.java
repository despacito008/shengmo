package com.aiwujie.shengmo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class GradientColorTextView extends TextView {
    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mViewHeight = 0;
    private Rect mTextBound = new Rect();
    private int startColor = 0xffb1e6;
    private int endColor = 0xffdb57f3;

    public GradientColorTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int startColor,int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, 0, mViewHeight, new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }


}
