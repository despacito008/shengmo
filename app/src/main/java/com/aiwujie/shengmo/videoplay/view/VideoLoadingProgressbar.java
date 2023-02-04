package com.aiwujie.shengmo.videoplay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


/**
 * 视频加载进度条
 */
public class VideoLoadingProgressbar extends View {
    /*
     * view width and height
     */
    private int mWidth, mHeight;
    /*
     * current width
     */
    private int mProgressWidth = 100;
    /*
     * minWidth
     */
    private int mMinProgressWidth = 100;
    /*
     * color of progress
     */
    private int mColor = 0xffffffff;

    private Paint mPaint,mPaint1;
    private Handler mHandler;
    private int mTimePeriod = 20;

    private boolean isLoading= true;
    private int progressWidth = 0;
    public VideoLoadingProgressbar(Context context) {
        this(context, null);
    }

    public VideoLoadingProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoLoadingProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint1 = new Paint();

        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(mColor);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                invalidate();
                this.sendEmptyMessageDelayed(1, mTimePeriod);
            }
        };

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mHandler != null) {
            if (isLoading && visibility == VISIBLE) {
                mHandler.sendEmptyMessageDelayed(1, mTimePeriod);
            } else {
                mHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;

        mPaint.setStrokeWidth(h);
        mPaint1.setStrokeWidth(h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLoading){
            mProgressWidth += 30;
            if (mProgressWidth > mWidth) {
                mProgressWidth = mMinProgressWidth;
            }
            // 计算透明度
            int alph = 255 - 255 * mProgressWidth / mWidth;
            if (alph < 30) {
                alph = 30;
            }
            mPaint.setAlpha(alph);
            canvas.drawLine(mWidth / 2f - mProgressWidth / 2f, 0, mWidth / 2f + mProgressWidth / 2f, 0, mPaint);

        }else {
            mPaint.setAlpha(255);
            canvas.drawLine(0, 0, progressWidth, 0, mPaint1);

        }

    }

    public void setProgress(float mProgressWidth) {
        this.progressWidth = (int) (getMeasuredWidth()*mProgressWidth);
        invalidate();
    }

    public void setIIsLoading(boolean isLoading){
        this.isLoading=isLoading;
        if (mHandler != null) {
            if (isLoading) {
                mHandler.sendEmptyMessageDelayed(1, mTimePeriod);
            } else {
                mHandler.removeCallbacksAndMessages(null);
            }
        }
    }


}