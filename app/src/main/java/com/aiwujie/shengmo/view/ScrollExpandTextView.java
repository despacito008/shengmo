package com.aiwujie.shengmo.view;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import com.aiwujie.shengmo.utils.DensityUtil;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * 在超长文本后面加一个view，用来控制文本展示区域，当展开状态时超出的文本将可垂直滚动
 * 非长文本内容就像TextView一样
 *
 * @author ly
 * date 2020/3/9 10:22
 */
public class ScrollExpandTextView extends ScrollView implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final int MAX_LINE_COLLAPSE = 2;//收起时最大展示行数
    private static final String TEXT_EXPAND = "展开";
    private static final String TEXT_COLLAPSE = "收起";
    private static final int MAX_H = 3 * DensityUtil.dp2px(50);
    private int w;
    private int minH;
    private int curH;
    private int arrowSize;
    private TextView tv;
    private TextView tvMore;
    private State state = State.COLLAPSE;
    private CharSequence text;
    private LayoutParams paramsMore;
    private int space;
    Context context;

    public enum State {
        EXPAND, COLLAPSE
    }

    public ScrollExpandTextView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ScrollExpandTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ScrollExpandTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(w, curH);//固定宽高
    }

    private void init() {
        w = (int) (0.7 * ScreenUtil.getScreenWidth(context));
        minH = DensityUtil.dp2px(50);
        curH = minH;
        arrowSize = DensityUtil.dp2px(7);
        space = DensityUtil.dp2px(3);

        FrameLayout flContainer = new FrameLayout(getContext());
        tv = new TextView(getContext());
        tv.setTextSize(14);
        tv.setMaxHeight(DensityUtil.dp2px(50));
        tv.setMaxLines(MAX_LINE_COLLAPSE);
        tv.setIncludeFontPadding(true);
        tv.setLineSpacing(1.0f, 1.2f);
        tvMore = new TextView(getContext());
       // tvMore.setBackgroundResource(R.drawable.normal_back);
        tvMore.setMaxLines(1);
        tvMore.setTextSize(12);
        tvMore.setGravity(Gravity.CENTER);
        tvMore.setVisibility(GONE);
        tvMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.COLLAPSE) {
                    state = State.EXPAND;
                    tv.setMaxLines(Integer.MAX_VALUE);
                    tv.setText(text);
                } else {
                    state = State.COLLAPSE;
                    tv.setMaxLines(MAX_LINE_COLLAPSE);
                }
                ScrollExpandTextView.this.setMoreViewPosition();
            }
        });

        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        paramsMore = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvMore.setLayoutParams(paramsMore);
        flContainer.addView(tv);
        flContainer.addView(tvMore);
        addView(flContainer);

        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        setMoreTextColor(ContextCompat.getColor(getContext(), R.color.white));

        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);
    }

    private void setMoreViewPosition() {
        Layout layout = tv.getLayout();
        if (layout == null)
            return;
        int lineCount = layout.getLineCount();
        int lineH = layout.getLineBottom(0) - layout.getLineTop(0);
        minH = MAX_LINE_COLLAPSE * lineH;
        curH = lineCount * lineH;
        if (text == null || lineCount <= MAX_LINE_COLLAPSE && tv.length() == text.length()) {
            tvMore.setVisibility(GONE);
        } else {
            if (state == State.COLLAPSE) {
                curH = minH;

                float lineWidth = layout.getLineWidth(MAX_LINE_COLLAPSE - 1);
                //获取第2行最后一个字符的下标
                int lineEnd = layout.getLineEnd(MAX_LINE_COLLAPSE - 1);
                //计算每个字符占的宽度
                float widthPerChar = layout.getLineWidth(MAX_LINE_COLLAPSE - 1) / (lineEnd + 1);
                float diff = lineWidth + tvMore.getMeasuredWidth() + space - (getWidth() - getPaddingLeft() - getPaddingRight());
                //第二行展示不下，去掉第二行最后几个字符，用来放展开按钮
                if (diff > 0) {
                    int removeCount = (int) (diff / widthPerChar);
                    if (lineEnd > removeCount) {
                        CharSequence t = text.subSequence(0, lineEnd - removeCount) + "...";
                        setTextAndRefresh(t);
                        return;//setText会重新触发onGlobalLayout
                    }
                }
                //获取第二行字符的坐标，设置展开按钮的margin，使展开按钮在文本后面
                paramsMore.leftMargin = (int) layout.getLineRight(MAX_LINE_COLLAPSE - 1) + space;
                paramsMore.topMargin = lineH + tv.getPaddingTop() - space;

                tvMore.setText(TEXT_EXPAND);
               // drawRight4MoreView(R.drawable.normal_back);
            } else {
                if (curH > MAX_H)
                    curH = MAX_H;

                float lineWidth = layout.getLineWidth(lineCount - 1);
                if (lineWidth + tvMore.getMeasuredWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()) > 0) {//最后一行显示不下，将最后一行换行
                    if (text.length() > 2) {
                        //分两个字符到tvMore那一行，更协调
                        String tmp = text.subSequence(0, text.length() - 2) + "\n" + text.subSequence(text.length() - 2, text.length());
                        setTextAndRefresh(tmp);
                        return;//setText会重新触发onGlobalLayout
                    }
                }
                tvMore.setText(TEXT_COLLAPSE);
               // drawRight4MoreView(R.drawable.normal_back);

                paramsMore.leftMargin = (int) layout.getSecondaryHorizontal(layout.getLineEnd(lineCount - 1)) + space;
                paramsMore.topMargin = layout.getHeight() - tv.getPaddingBottom() - lineH + DensityUtil.dp2px(2);
            }
            tvMore.setVisibility(VISIBLE);
        }
        getLayoutParams().height = curH;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMoreViewPosition();
    }

    @Override
    public void onGlobalLayout() {
        //为保证TextView.getLayout()!=null，在这里再执行相关逻辑
        setMoreViewPosition();
        //记得移除，不然会一直回调
        tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public void setText(final CharSequence text) {
        this.text = text;
        setTextAndRefresh(text);
    }

    public void setTextAndRefresh(CharSequence text) {
        tv.getViewTreeObserver().addOnGlobalLayoutListener(this);
        tv.setText(text);
    }

    private void drawRight4MoreView(int icRes) {
        Drawable drawable = getResources().getDrawable(icRes);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(arrowSize / 3, 0, arrowSize, arrowSize / 3);
        tvMore.setCompoundDrawables(null, null, drawable, null);
    }

    public void setTextColor(int color) {
        tv.setTextColor(color);
    }

    public void setMoreTextColor(int color) {
        tvMore.setTextColor(color);
    }


    //如果不需要处理滑动冲突，去掉下面的代码即可
    private int startX, startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int disX = Math.abs(endX - startX);
                int disY = Math.abs(endY - startY);
                if (disX > disY) {
                    getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(startX - endX));
                } else {
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(startY - endY));
                }
                break;
            default:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

