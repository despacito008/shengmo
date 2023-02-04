package com.aiwujie.shengmo.view.luckydraw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.LogUtil;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-11-25.
 * Email:  warkey1991@gmail.com
 */
public class LuckyDrawLayout extends RelativeLayout {
    private static final String TAG = "LuckyDrawLayout";
    private Paint bgPaint;
    private int mWidth, mHeight;
    private int radiusBg;
    private RectF rectF = new RectF();
    private Bitmap smallGreenBitmap;
    private Bitmap smallRedBitmap;
    private int ballWidth, ballHeight;
    private int redBallWidth, redBallHeight;
    private RectF ballRectf;
    private int innerPadding = dip2px(15);
    private boolean isChanged = true;
    private int eachRow = 10;
    Context context;
    public LuckyDrawLayout(Context context) {
        this(context, null);
    }

    public LuckyDrawLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyDrawLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setStrokeJoin(Paint.Join.ROUND);
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setColor(0xFFFF356B);

       smallGreenBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_small_green);
        smallRedBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_small_red);
//        ballWidth = smallGreenBitmap.getWidth();
//        ballHeight = smallGreenBitmap.getHeight();
//        redBallWidth = smallRedBitmap.getWidth();
//        redBallHeight = smallRedBitmap.getHeight();
        //smallGreenBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user_vip);
        //smallRedBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user_svip);
        ballWidth = 50;
        ballHeight = 50;
        redBallWidth = 50;
        redBallHeight = 50;
        //LogUtil.d(ballWidth + "--" + ballHeight + "--" + redBallWidth + "--" + redBallHeight);
        ballRectf = new RectF();

        setWillNotDraw(false);
        changeBall();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(mWidth, mHeight);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        radiusBg = mWidth / 40;
        rectF.set(0, 0, mWidth, mHeight);
        bgPaint.setColor(0xFFFF356B);
        bgPaint.setColor(0xFF66ccff);
       // canvas.drawRoundRect(rectF, radiusBg, radiusBg, bgPaint);

//        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_lottery_draw_small_middle);
//        Matrix matrix2 = new Matrix();
//        float scale_w2 = ((float) rectF.width()) / bitmap2.getWidth();
//        float scale_h2 = ((float) rectF.height()) / bitmap2.getHeight();
//        matrix2.postScale(scale_w2, scale_h2);
//        Bitmap bihuanbmp2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2,true);
//        canvas.drawBitmap(bihuanbmp2, rectF.left, rectF.top, bgPaint);



        rectF.set(innerPadding, innerPadding, mWidth - innerPadding, mHeight - innerPadding);
        bgPaint.setColor(0xFFCE0037);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_lottery_draw_small_bg);
        Matrix matrix = new Matrix();
        float scale_w = ((float) rectF.width()) / bitmap.getWidth();
        float scale_h = ((float) rectF.height()) / bitmap.getHeight();
        matrix.postScale(scale_w, scale_h);
        Bitmap bihuanbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,true);
        canvas.drawBitmap(bihuanbmp, rectF.left, rectF.top, bgPaint);





       // canvas.drawRoundRect(rectF, radiusBg, radiusBg, bgPaint);

        drawFourCorner(canvas);
        int ballGapDp = (mWidth - innerPadding * 2) / eachRow;
        for (int i = 0; i < eachRow; i++) {
            if (getGreen(i)) {
                //上
                ballRectf.set(innerPadding * 2 + i * ballGapDp - ballWidth / 2, innerPadding / 2 - ballHeight / 2, innerPadding * 2 + ballWidth / 2 + i * ballGapDp, innerPadding / 2 + ballHeight / 2);
                canvas.drawBitmap(smallGreenBitmap, null, ballRectf, null);
                //下
                ballRectf.set(innerPadding * 2 + i * ballGapDp - ballWidth / 2, mHeight - (innerPadding / 2 + ballHeight / 2), innerPadding * 2 + ballWidth / 2 + i * ballGapDp, mHeight - (innerPadding / 2 - ballHeight / 2));
                canvas.drawBitmap(smallGreenBitmap, null, ballRectf, null);
                //左
                ballRectf.set(innerPadding / 2 - ballWidth / 2, innerPadding * 2 + i * ballGapDp - ballHeight / 2, innerPadding / 2 + ballWidth / 2, innerPadding * 2 + i * ballGapDp + ballHeight / 2);
                canvas.drawBitmap(smallGreenBitmap, null, ballRectf, null);
                //右
                ballRectf.set(mWidth - (innerPadding / 2 + ballWidth / 2), innerPadding * 2 + i * ballGapDp - ballHeight / 2, mWidth - (innerPadding / 2 - ballWidth / 2), innerPadding * 2 + i * ballGapDp + ballHeight / 2);
                canvas.drawBitmap(smallGreenBitmap, null, ballRectf, null);
            } else {
                ballRectf.set(innerPadding * 2 + i * ballGapDp - redBallWidth / 2, innerPadding / 2 - redBallHeight / 2, innerPadding * 2 + redBallWidth / 2 + i * ballGapDp, innerPadding / 2 + redBallHeight / 2);
                canvas.drawBitmap(smallRedBitmap, null, ballRectf, null);

                ballRectf.set(innerPadding * 2 + i * ballGapDp - redBallWidth / 2, mHeight - (innerPadding / 2 + redBallHeight / 2), innerPadding * 2 + redBallWidth / 2 + i * ballGapDp, mHeight - (innerPadding / 2 - redBallHeight / 2));
                canvas.drawBitmap(smallRedBitmap, null, ballRectf, null);

                ballRectf.set(innerPadding / 2 - redBallWidth / 2, innerPadding * 2 + i * ballGapDp - redBallHeight / 2, innerPadding / 2 + redBallWidth / 2, innerPadding * 2 + i * ballGapDp + redBallHeight / 2);
                canvas.drawBitmap(smallRedBitmap, null, ballRectf, null);

                ballRectf.set(mWidth - (innerPadding / 2 + redBallWidth / 2), innerPadding * 2 + i * ballGapDp - redBallHeight / 2, mWidth - (innerPadding / 2 - redBallWidth / 2), innerPadding * 2 + i * ballGapDp + redBallHeight / 2);
                canvas.drawBitmap(smallRedBitmap, null, ballRectf, null);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int innerPP = (int) (innerPadding * 1.5f);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof LuckyDrawView) {
                child.layout(innerPP, innerPP, getWidth() - innerPP, getHeight() - innerPP);
            }
        }
    }

    private void changeBall() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isChanged = !isChanged;
                Log.d(TAG, "run: changeBall()");
                invalidate();
                postDelayed(this, 300);
            }
        }, 300);
    }

    private boolean getGreen(int i) {
        if (isChanged) {
            return i % 2 != 0;
        } else {
            return i % 2 == 0;
        }
    }

    private void drawFourCorner(Canvas canvas) {
        if (isChanged) {
            RectF leftTopRectf = new RectF(innerPadding / 2 - ballWidth / 2, innerPadding / 2 - ballHeight / 2, innerPadding / 2 + ballWidth / 2, innerPadding / 2 + ballHeight / 2);
            canvas.drawBitmap(smallGreenBitmap, null, leftTopRectf, null);
            RectF rightTopRectf = new RectF(mWidth - (innerPadding / 2 + ballWidth / 2), innerPadding / 2 - ballHeight / 2, mWidth - (innerPadding / 2 - ballWidth / 2), innerPadding / 2 + ballHeight / 2);
            canvas.drawBitmap(smallGreenBitmap, null, rightTopRectf, null);
            RectF leftBottomRectf = new RectF(innerPadding / 2 - ballWidth / 2, mHeight - (innerPadding / 2 + ballHeight / 2), innerPadding / 2 + ballWidth / 2, mHeight - (innerPadding / 2 - ballHeight / 2));
            canvas.drawBitmap(smallGreenBitmap, null, leftBottomRectf, null);
            RectF rightBottomRectf = new RectF(mWidth - (innerPadding / 2 + ballWidth / 2), mHeight - (innerPadding / 2 + ballHeight / 2), mWidth - (innerPadding / 2 - ballWidth / 2), mHeight - (innerPadding / 2 - ballHeight / 2));
            canvas.drawBitmap(smallGreenBitmap, null, rightBottomRectf, null);
        } else {
            RectF leftTopRectf = new RectF(innerPadding / 2 - redBallWidth / 2, innerPadding / 2 - redBallHeight / 2, innerPadding / 2 + redBallWidth / 2, innerPadding / 2 + redBallHeight / 2);
            canvas.drawBitmap(smallRedBitmap, null, leftTopRectf, null);
            RectF rightTopRectf = new RectF(mWidth - (innerPadding / 2 + redBallWidth / 2), innerPadding / 2 - redBallHeight / 2, mWidth - (innerPadding / 2 - redBallWidth / 2), innerPadding / 2 + redBallHeight / 2);
            canvas.drawBitmap(smallRedBitmap, null, rightTopRectf, null);
            RectF leftBottomRectf = new RectF(innerPadding / 2 - redBallWidth / 2, mHeight - (innerPadding / 2 + redBallHeight / 2), innerPadding / 2 + redBallWidth / 2, mHeight - (innerPadding / 2 - redBallHeight / 2));
            canvas.drawBitmap(smallRedBitmap, null, leftBottomRectf, null);
            RectF rightBottomRectf = new RectF(mWidth - (innerPadding / 2 + redBallWidth / 2), mHeight - (innerPadding / 2 + redBallHeight / 2), mWidth - (innerPadding / 2 - redBallWidth / 2), mHeight - (innerPadding / 2 - redBallHeight / 2));
            canvas.drawBitmap(smallRedBitmap, null, rightBottomRectf, null);
        }
    }


    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
