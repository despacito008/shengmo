package com.aiwujie.shengmo.kt.util

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView

class TextViewMatcher {
    fun TextView.matcherView(keyword:String,color:Int) {
        val tv = this
        val tvStr = tv.text
        var spannableStringBuilder = SpannableStringBuilder(tv.text)
        val index = tvStr.indexOf(keyword)
        if (index != -1) {
            spannableStringBuilder.setSpan(ForegroundColorSpan(color),index,index + keyword.length,SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tv.text = spannableStringBuilder
    }
}