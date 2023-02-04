package com.aiwujie.shengmo.kt.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText


@SuppressLint("AppCompatCustomView")
class ScrollEditText : EditText {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)
    private var mOffsetHeight = 0
    private var scrollFlag = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_DOWN) { scrollFlag = false }
        if (canVerticalScroll()) {
            if (!scrollFlag) { parent.requestDisallowInterceptTouchEvent(true) }
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }

    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        if (scrollY == mOffsetHeight || scrollY == 0) {
            parent.requestDisallowInterceptTouchEvent(false)
            scrollFlag = true
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     * @return true：可以滚动   false：不可以滚动
     */
    private fun canVerticalScroll(): Boolean {
        //滚动的距离
        val scrollY = this.scrollY
        //控件内容的总高度
        val scrollRange = this.layout.height
        //控件实际显示的高度
        val scrollExtent = this.height - this.totalPaddingTop - this.totalPaddingBottom
        //控件内容总高度与实际显示高度的差值
        val scrollDifference = scrollRange - scrollExtent
        mOffsetHeight = scrollDifference
        return if (scrollDifference == 0) { //editText未填满
            false
        } else scrollY > 0 || scrollY < scrollDifference - 1
    }

}