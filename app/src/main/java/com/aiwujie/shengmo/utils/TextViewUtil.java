package com.aiwujie.shengmo.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.widget.TextView;

public class TextViewUtil {
    public static void setBold(TextView textView) {
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    public static void setNormal(TextView textView) {
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    public static void setTextSizeWithDp(TextView textView,int dp) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,dp);
    }

    public static void setSpannedSizeText(TextView textView,String content,int start,int end,int size) {
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size, true);//设置前面的字体大小
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(sizeSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static void setSpannedSizeBoldText(TextView textView,String content,int start,int end,int size) {
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size, true);//设置前面的字体大小
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(sizeSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体
        textView.setText(builder);
    }

    public static void setSpannedColorText(TextView textView,String content,int start,int end,int color) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(colorSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static void setSpannedColorAndSizeText(TextView textView,String content,int start,int end,int color,int size) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size, true);//设置前面的字体大小
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(colorSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(sizeSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }
}
