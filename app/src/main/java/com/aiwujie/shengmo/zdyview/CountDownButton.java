package com.aiwujie.shengmo.zdyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.aiwujie.shengmo.utils.LogUtil;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义圆环view 倒计时控件
 */

public class CountDownButton extends AppCompatButton implements View.OnClickListener{

    private Context mContext ;
    private onCountClickListener mCountClickListener ;

    private static final int DEFAULT_PROGRESS_BG_COLOR = Color.LTGRAY;
    private static final int DEFAULT_PROGRESS_COLOR = Color.BLUE ;
    private static final float DEFAULT_PROGRESS_LINE_WIDTH = 5 ;
    private static final int DEFAULT_CIRCLE_BG_COLOR = Color.WHITE ;
    private static final int DEFAULT_TOTAL_TIME = 5000 ;
    //定义刷新间隔时长ms
    private static final int DELAY = 50 ;

    private int mProgressBgColor = DEFAULT_PROGRESS_BG_COLOR ;
    private int mProgressColor = DEFAULT_PROGRESS_COLOR ;
    private float mProgressLineWidth = DEFAULT_PROGRESS_LINE_WIDTH ;
    private int mCircleBgColor = DEFAULT_CIRCLE_BG_COLOR ;
    private int mTotalTime = DEFAULT_TOTAL_TIME ;
    private int mDelayTime = DELAY ;

    private double mProgress ;
    private int mCountingVal ;
    private int mCountPer ;
    private int mCircleRadius ;

    //定义中心坐标
    private int mCenterX;
    private int mCenterY;

    //定义画笔、计时器等
    private Paint mPaint ;
    private Timer mTimer ;
    private Rect mRect ;
    private RectF mRectF ;

    //定义正在进行计时标志
    private boolean isCounting = false ;

    public CountDownButton(Context context) {
        this(context , null , 0);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context ;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray mTypeArry = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mProgressBgColor = mTypeArry.getColor(R.styleable.MyTextView_progressBgColor , DEFAULT_PROGRESS_BG_COLOR) ;
        mProgressColor = mTypeArry.getColor(R.styleable.MyTextView_progressColor , DEFAULT_PROGRESS_COLOR) ;
        mProgressLineWidth = mTypeArry.getDimension(R.styleable.MyTextView_progressLineWidth , DEFAULT_PROGRESS_LINE_WIDTH);
        mCircleBgColor = mTypeArry.getColor(R.styleable.MyTextView_circleBgColr , DEFAULT_CIRCLE_BG_COLOR)  ;
        mTotalTime = mTypeArry.getInt(R.styleable.MyTextView_totalTime , DEFAULT_TOTAL_TIME) ;
        mDelayTime = mTypeArry.getInt(R.styleable.MyTextView_delayTime , DELAY) ;
        mTypeArry.recycle();

        mTimer = new Timer();
        mRect = new Rect();
        mRectF = new RectF();
        mPaint = new Paint();

        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件的宽高
        int width = getMeasuredWidth() ;
        int height = getMeasuredHeight();

        //确定为正方形布局
        if(width > height){
            height = width ;
        }else if(width < height){
            width = height;
        }

        //确定圆半径为控件宽/高一半
        mCircleRadius = width/2 ;

        //设置画布大小
        setMeasuredDimension(width,height);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);//不要继承父类的方法，避免部分属性设置不成功
        //获取正在绘制的控件边界
        getDrawingRect(mRect);

        //确定控件的中心坐标
        mCenterX = mRect.centerX();
        mCenterY = mRect.centerY();

        //开始绘制实心圆
        mPaint.setColor(mCircleBgColor);
        mPaint.setAntiAlias(true);//去锯齿
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX , mCenterY ,mCircleRadius ,mPaint);

        //开始绘制进度条圆环
        mPaint.setColor(mProgressBgColor);
        mPaint.setStyle(Paint.Style.STROKE);//设置空心效果
        mPaint.setStrokeWidth(mProgressLineWidth);
        canvas.drawCircle(mCenterX , mCenterY ,mCircleRadius-mProgressLineWidth/2 ,mPaint);

        //绘制文字（必须使用getPaint()）
        getPaint().setColor(getCurrentTextColor());
        getPaint().setTextAlign(Paint.Align.CENTER);
        getPaint().setAntiAlias(true);  //防锯齿
        float textY = mCenterY-(getPaint().descent()+getPaint().ascent())/2 ;
        canvas.drawText(getText().toString(), mCenterX , textY , getPaint());

        //开始绘制进度条
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mProgressLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔刷类型
        mRectF.set(mRect.left + mProgressLineWidth/2, mRect.top + mProgressLineWidth/2,
                mRect.right - mProgressLineWidth/2, mRect.bottom - mProgressLineWidth/2);
        canvas.drawArc(mRectF, -90, (float)mProgress* 360, false, mPaint);
    }


    public int getProgressBgColor() {
        return mProgressBgColor;
    }

    public void setProgressBgColor(int mProgressBgColor) {
        this.mProgressBgColor = mProgressBgColor;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    public float getmProgressWidth() {
        return mProgressLineWidth;
    }

    public void setProgressWidth(float mProgressWidth) {
        this.mProgressLineWidth = mProgressWidth;
    }

    public int getCircleBgColor() {
        return mCircleBgColor;
    }

    public void setCircleBgColor(int mCircleBgColor) {
        this.mCircleBgColor = mCircleBgColor;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(int mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public int getDelayTime() {
        return mDelayTime;
    }

    public void setDelayTime(int mDelayTime) {
        this.mDelayTime = mDelayTime;
    }

    public void cancle(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
            mProgress = 0f;
            mCountingVal = 0;
        }
    }

    public void startTimer(){
        mCountPer = mTotalTime/mDelayTime;
        mTimer = new Timer();
        //Log.d("cdb", "开始" + System.currentTimeMillis());
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mProgress += 1f/mCountPer;
                mCountingVal += mDelayTime;
                //Log.d("cdb", "ing " + mCountingVal + mCountPer);
                isCounting = true ;
                postInvalidate();
               // LogUtil.d(mCountingVal + " -- " + mTotalTime);
                if (mCountingVal >= mTotalTime) {
                    // Log.d("cdb", "结束" + System.currentTimeMillis());
                    post(new Runnable() {
                        @Override
                        public void run() {
                            isCounting = false ;
                            cancle();
                            if(mCountClickListener != null){
                                mCountClickListener.onCountOverClick();
                            }
                        }
                    });
                } else {
                    if(((int)mProgress)%10 != 0) {
                        return;
                    }
                    if(mCountClickListener != null) {
                        mCountClickListener.onCounting(mProgress );
                         Log.d("mCountClickListener", "mProgress" + mProgress);
                    }
                }
            }
        }, 0, mDelayTime);
    }

    public void setOnCountClickListener(onCountClickListener mCountClickListener){
        this.mCountClickListener = mCountClickListener;
    }

    @Override
    public void onClick(View view) {
        if(mCountClickListener != null){
            if(isCounting){
                cancle();
                mCountClickListener.onCountingClick();
            } else{
                mCountClickListener.onCountOverClick();
            }
        }
    }

    public interface onCountClickListener{
        void onCounting(double process);
        void onCountingClick();
        void onCountOverClick();
    };
}
