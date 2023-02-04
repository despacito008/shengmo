package com.aiwujie.shengmo.view.luckydraw;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.LotteryDrawGiftBean;
import com.aiwujie.shengmo.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-11-25.
 * Email:  warkey1991@gmail.com
 */
public class LuckyDrawView extends View {
    private static final String TAG = "LuckyDrawView";
    //0->1->2->3->5->6->7->8

    //0-1-2-5-8-7-6-3
    private int currentPosition = 0;
    private int stopPosition = -1;
    private static int LOOP_COUNT = 4;
    private int currentLoopCount = 0;
    private Paint bgPaint;
    private int mWidth, mHeight;
    private int radiusBg;
    private Paint cellPaint;
    private Paint cellTextPaint;
    private int innerEachGap = dip2px(3);
    private int innerWidth, innerHeight;
    private int eachWidth, eachHeight;
    private boolean onTouchCenter = false;

    private RectF mCenterButtonRectF;
    //private String[] rewardTexts = {"$0.04", "$0.10", "$0.80", "$0.85", "", "$3.00", "$5.00", "$0.15", "$0.10"};
    private String[] rewardTexts = {"红酒", "蛋糕", "钻戒", "皇冠", "", "飞机", "游轮", "城堡", "幸运草"};
    private int[] positions = {0, 1, 2, 5, 8, 7, 6, 3}; //顺时针
    String start = "Start";
    float scale = 1.0f;
    private boolean isRuning = false;

    private Context context;

    public LuckyDrawView(Context context) {
        this(context, null);
    }

    public LuckyDrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setStrokeJoin(Paint.Join.ROUND);
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setColor(0xFFFF356B);

        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setStyle(Paint.Style.FILL);
        cellPaint.setColor(Color.WHITE);

        cellTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellTextPaint.setTextSize(sp2px(context, 26));
        cellTextPaint.setColor(Color.WHITE);
        cellTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        cellTextPaint.setAntiAlias(true);

        //initBitmap();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onTouchCenter = false;
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        if (mCenterButtonRectF.contains(x, y) && !isRuning) {
                            if (scale != 0.8f) {
                                scale = 0.8f;
                                invalidate();
                            }
                            onTouchCenter = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (onTouchCenter) {
                            //startLotteryDraw(1);
                            if (onLucKyDrawViewListener != null) {
                                onLucKyDrawViewListener.doLotteryStart();
                            }
                        }
                        onTouchCenter = false;
                        break;
                }
                return true;
            }
        });

        rectF = new RectF();
    }


    RectF rectF;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        radiusBg = mWidth / 40;
        innerWidth = mWidth - innerEachGap * 4;
        innerHeight = mHeight - innerEachGap * 4;
        eachWidth = innerWidth / 3;
        eachHeight = innerHeight / 3;
        drawNineCell(canvas);
    }

    private void drawNineCell(Canvas canvas) {
        int nums = 9;
        for (int i = 0; i < nums; i++) {
            int startX = innerEachGap + (i % 3) * (eachWidth + innerEachGap);
            int startY = innerEachGap + (i / 3) * (eachHeight + innerEachGap);
            rectF.set(startX, startY, startX + eachWidth, startY + eachHeight);
            if (i == nums / 2) {
                cellPaint.setColor(0xFFFEEABA);
                bgPaint.setColor(0xFFFF356B);
                rectF.set(rectF.left + rectF.left * (1 - scale) * 0.08f, rectF.top + rectF.top * (1 - scale) * 0.08f, rectF.right - rectF.right * (1 - scale) * 0.08f, rectF.bottom - rectF.bottom * (1 - scale) * 0.08f);
                canvas.drawRoundRect(rectF, radiusBg, radiusBg, cellPaint);
                mCenterButtonRectF = new RectF(rectF);
                rectF.set(rectF.left + dip2px(10), rectF.top + dip2px(10), rectF.right - dip2px(10), rectF.bottom - dip2px(10));
                canvas.drawRoundRect(rectF, radiusBg, radiusBg, bgPaint);
                rectF.set(startX, startY, startX + eachWidth, startY + eachHeight);
                //rectF.set(rectF.left + rectF.left * (1 - scale) * 0.08f, rectF.top + rectF.top * (1 - scale) * 0.08f, rectF.right - rectF.right * (1 - scale) * 0.08f, rectF.bottom - rectF.bottom * (1 - scale) * 0.08f);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_lottery_draw_start);
                Matrix matrix = new Matrix();
                float scale_w = ((float) eachWidth) / bitmap.getWidth();
                float scale_h = ((float) eachHeight) / bitmap.getHeight();
                matrix.postScale(scale_w, scale_h);
                Bitmap bihuanbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                canvas.drawBitmap(bihuanbmp, rectF.left, rectF.top, cellTextPaint);
            } else {
                if (positions[currentPosition] == i) {
                    cellPaint.setColor(0x66FBC01B);
                    cellTextPaint.setColor(Color.WHITE);
                } else {
                    cellPaint.setColor(0x00FFFFFF);
                    cellTextPaint.setColor(0xFFFF5A00);
                }
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_item_lottery_draw);
                Matrix matrix = new Matrix();
                float scale_w = ((float) eachWidth) / bitmap.getWidth();
                float scale_h = ((float) eachHeight) / bitmap.getHeight();
                matrix.postScale(scale_w, scale_h);
                Bitmap bihuanbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                canvas.drawBitmap(bihuanbmp, rectF.left, rectF.top, cellTextPaint);
                canvas.drawRoundRect(rectF, radiusBg, radiusBg, cellPaint);
                if (mBitmap != null && mBitmap.size() > i) {
                    canvas.drawBitmap(mBitmap.get(i), rectF.left, rectF.top, cellTextPaint);
                }
            }
        }
    }


    private float getTextDiffY(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return Math.abs(fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

    private void startLoop() {
        currentLoopCount = 0;
        new Thread(action).start();
    }

    private Runnable action = new Runnable() {
        @Override
        public void run() {
            while (true) {
                isRuning = true;
                if (currentLoopCount >= LOOP_COUNT) {
                    isRuning = false;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getContext(), "恭喜你抽中了position=" + stopPosition + "(" + rewardTexts[positions[stopPosition]] + ")", Toast.LENGTH_LONG).show();
                            if (onLucKyDrawViewListener != null) {
                                onLucKyDrawViewListener.doLotteryComplete();
                            }
                        }
                    }, 500);
                    break;
                }
                currentPosition++;
                if (currentPosition > 7) {
                    currentLoopCount++;
                    currentPosition = 0;
                }

                post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });

                if (currentLoopCount == LOOP_COUNT - 1) {
                    if (currentPosition % 8 == stopPosition) {
                        if (currentPosition == stopPosition) {
                            currentLoopCount = LOOP_COUNT;
                        }
                    }
                    SystemClock.sleep(100 * (currentPosition + 1));
                } else {
                    SystemClock.sleep(100);
                }
            }

        }
    };

    private void startPressScaleAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.8f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = ((float) animation.getAnimatedValue());
                invalidate();
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    List<Bitmap> mBitmap;

    public void initBitmap() {
        mBitmap = new ArrayList<>();
        this.post(new Runnable() {
            @Override
            public void run() {
                int mWidth = getWidth();
                int mHeight = getHeight();
                int innerWidth = mWidth - innerEachGap * 4;
                final int innerHeight = mHeight - innerEachGap * 4;
                int eachWidth = innerWidth / 3;
                int eachHeight = innerHeight / 3;
                Log.d("tagtag", "width1 = " + eachWidth + ",height1 = " + eachHeight);

                final Map<String, Bitmap> bbMap = new HashMap<>();
                for (int i = 0; i < imgUrls.length; i++) {
                    bbMap.put(imgUrls[i], null);
                }

                for (int i = 0; i < imgUrls.length; i++) {
                    final int finalI = i;
                    Glide.with(context).asBitmap().override(eachWidth, eachHeight).load(imgUrls[i]).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bbMap.put(imgUrls[finalI], resource);
                            mBitmap.add(resource);
                            if (mBitmap.size() > 8) {
                                //invalidate();
                                mBitmap.clear();
                                for (int i = 0; i < imgUrls.length; i++) {
                                    mBitmap.add(bbMap.get(imgUrls[i]));
                                }
                                invalidate();
                            }
                        }
                    });
                }
            }
        });


    }

    private String[] imgUrls = {"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew20.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew21.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew22.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew23.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew24.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew25.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew26.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew27.png",
            "http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew28.png"
    };

    void drawBitmap(final Canvas canvas, final RectF rectF, final int i) {
        Glide.with(context)
                .asBitmap()
                .load("http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew24.png")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        canvas.drawBitmap(resource, rectF.centerX(), rectF.centerY(), cellTextPaint);
                    }
                });
    }

    public void startLotteryDrawTimes(int type) {
        if (!isRuning) {
            onTouchCenter = true;
            startLotteryDraw(type);
        }
    }

    public void startLotteryDrawWithResult(int type, int index) {
        if (!isRuning) {
            onTouchCenter = true;
            stopPosition = index;
            startLotteryDraw(type);
        }
    }

    private void startLotteryDraw(int type) {
        if (onTouchCenter) {
            if (type == 1) {
                LOOP_COUNT = 4;
            } else if (type == 2) {
                LOOP_COUNT = 6;
            } else if (type == 3) {
                LOOP_COUNT = 8;
            }
            startPressScaleAnim();
            startLoop();
        }
        onTouchCenter = false;
    }

    public interface OnLucKyDrawViewListener {
        void doLotteryComplete();

        void doLotteryStart();
    }

    OnLucKyDrawViewListener onLucKyDrawViewListener;

    public void setOnLucKyDrawViewListener(OnLucKyDrawViewListener onLucKyDrawViewListener) {
        this.onLucKyDrawViewListener = onLucKyDrawViewListener;
    }
    HashMap<Integer,Bitmap> tempHashMap;
    public void initLotteryDrawItem(List<LotteryDrawGiftBean.DataBean.ListBean> itemList) {
        int mWidth = getWidth();
        int mHeight = getHeight();
        int innerWidth = mWidth - innerEachGap * 4;
        final int innerHeight = mHeight - innerEachGap * 4;
        int eachWidth = innerWidth / 3;
        int eachHeight = innerHeight / 3;
        mBitmap = new ArrayList<>();
        HashMap<String,Bitmap> tempHashMap = new LinkedHashMap<>();
        for (LotteryDrawGiftBean.DataBean.ListBean item : itemList) {
            tempHashMap.put(item.getGift_image(),null);
            if (tempHashMap.size() == 4) {
                tempHashMap.put("temp",null);
            }
        }
        for (LotteryDrawGiftBean.DataBean.ListBean item : itemList) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.app_layout_lottery_draw_item, null, false);
            TextView textView = itemView.findViewById(R.id.tv_item_lottery_draw_name);
            TextView tvBean = itemView.findViewById(R.id.tv_item_lottery_draw_bean);
            ImageView imageView = itemView.findViewById(R.id.iv_item_lottery_draw_icon);
            textView.setText(item.getGift_name());
            tvBean.setText(item.getGift_beans() + "魔豆");
            Glide.with(context)
                    .load(item.getGift_image())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                            Bitmap bitmap = createBitmap(itemView, eachWidth, eachHeight);
                            mBitmap.add(bitmap);
                            tempHashMap.put(item.getGift_image(),bitmap);
                            if (mBitmap.size() == 4) {
                                mBitmap.add(null);
                            } else if (mBitmap.size() > 8) {
                                mBitmap.clear();
                                mBitmap.addAll(tempHashMap.values());
                                invalidate();
                            }
                        }
                    });
        }
    }

    private void invalidateItem() {
        mBitmap.clear();
        for (Integer index:tempHashMap.keySet()) {
            mBitmap.add(tempHashMap.get(index));
        }
        invalidate();
    }

    public Bitmap createBitmap(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.TRANSPARENT);
        v.draw(c);
        return bmp;
    }
}
