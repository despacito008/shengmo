package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.kt.util.showText

class LimitEditLayout:LinearLayout {
    private var etLimit:EditText
    private var tvLimit:TextView
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.app_layout_limit_edit, this)
        rootView.run {
            etLimit = findViewById(R.id.et_limit_edit)
            tvLimit = findViewById(R.id.tv_limit_edit)
        }
        etLimit.addTextChangedListenerDsl {
            afterTextChanged {
                it?.let {
                    tvLimit.text = "${it.length}/${limitLength}"
                }
            }
        }
        // 解决scrollView中嵌套EditText导致不能上下滑动的问题
        etLimit.setOnTouchListener(View.OnTouchListener { v: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                mBottomFlag = false
            }
            //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
            if (canVerticalScroll(etLimit)) {
                if (!mBottomFlag) {
                    // 解决scrollView中嵌套EditText导致不能上下滑动的问题
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
                if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP) {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            etLimit.setOnScrollChangeListener(View.OnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (scrollY == mOffsetHeight || scrollY == 0) {
                    //这里触发父布局或祖父布局的滑动事件
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    mBottomFlag = true
                }
            })
        }
    }

    fun setContent(content: String) {
        if (content == "其他原因") {
            etLimit.showText("")
        } else {
            etLimit.showText(content)
        }
    }

    fun getContent():String {
        return etLimit.text.toString()
    }

    var limitLength = 1000
    set(value) {
        etLimit.filters = arrayOf(InputFilter.LengthFilter(value))
        tvLimit.text = "0/$value"
    }

    var textHint = ""
    set(value) {
        etLimit.hint = value
    }

    var mOffsetHeight = 0
    var mBottomFlag = false

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private fun canVerticalScroll(editText: EditText): Boolean {
        //滚动的距离
        val scrollY = editText.scrollY
        //控件内容的总高度
        val scrollRange = editText.layout.height
        //控件实际显示的高度
        //int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        val scrollExtent = editText.height - editText.totalPaddingTop - editText.totalPaddingBottom
        //控件内容总高度与实际显示高度的差值
        val scrollDifference = scrollRange - scrollExtent
        mOffsetHeight = scrollDifference
        return if (scrollDifference == 0) { //editText未填满
            false
        } else scrollY > 0 || scrollY < scrollDifference - 1
    }

}