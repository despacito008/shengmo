package com.aiwujie.shengmo.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aiwujie.shengmo.R

class FlowLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //记录总共多少行
    private val mAllLines = mutableListOf<List<View>>()

    //记录每一行的最大高度，用于Layout阶段使用
    private val mLineHeight = mutableListOf<Int>()

    //水平间距
    private val mHorizontalSpacing = dp2px(16)

    //垂直间距
    private val mVerticalSpacing = dp2px(8)


    //自定义ViewGroup一般重新onMeasure onLayout
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //注意要点：由于测量过程是从父控件到子控件递归调用的，所以onMeasure可能被调用多次，这里参考FrameLayout源码进行清除工作
        mAllLines.clear()
        mLineHeight.clear()

        /**
         * 思路:1.先确定子控件的childWidthMeasureSpec与childHeightMeasureSpec(重点)
         *     2.在根据childWidthMeasureSpec与childHeightMeasureSpec测量子控件
         *     3.根据流式布局的算法计算出最大行宽和行高
         *     4.获取自身的测量模式，再根据子View的测量结果来确定自身的最终宽高
         */

        //获取父控件给我的宽度
        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        //获取父控件给我的宽度
        val selfHeight = MeasureSpec.getSize(heightMeasureSpec)

        //每一行已使用的高度
        var lineWidthUsed = 0
        //每一行的最大高度
        var lineHeight = 0

        //自身所需的宽度
        var parentNeedWidth = 0
        //自身所需的高度
        var parentNeedHeight = 0

        //记录每行控件的个数
        var lineViews = mutableListOf<View>()

        for (i in 0 until childCount) {

            val childView = getChildAt(i)
            //对应xml布局参数
            val layoutParams = childView.layoutParams
            //父控件的MeasureSpec、父控件已使用的Padding、layoutParams共同确定子控件的MeasureSpec
            //MeasureSpec包含mode(高两位)和size(低30位)
            val childWidthMeasureSpec = getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingLeft + paddingRight,
                    layoutParams.width
            )
            val childHeightMeasureSpec = getChildMeasureSpec(
                    heightMeasureSpec,
                    paddingTop + paddingBottom,
                    layoutParams.height
            )
            //测量子控件
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            //测量完成后可获取测量的宽高
            val childMeasuredWidth = childView.measuredWidth
            val childMeasuredHeight = childView.measuredHeight

            //判断是否需要换行
            if (lineWidthUsed + childMeasuredWidth + mHorizontalSpacing > selfWidth) {
                //记录行数
                mAllLines.add(lineViews)
                //记录行高
                mLineHeight.add(lineHeight)

                //在每次换行时计算自身所需的宽高
                parentNeedWidth = parentNeedWidth.coerceAtLeast(lineWidthUsed + mHorizontalSpacing)
                parentNeedHeight += lineHeight + mVerticalSpacing

                //重置参数
                lineViews = mutableListOf()
                lineWidthUsed = 0
                lineHeight = 0
            }

            //记录每一行存放控件
            lineViews.add(childView)
            //记录每一行已使用的高度
            lineWidthUsed += childMeasuredWidth + mHorizontalSpacing
            //记录每一行的最大高度
            lineHeight = lineHeight.coerceAtLeast(childMeasuredHeight)

        }

        //判断是否需要换行
        if (lineViews.size != 0) {
            //记录行数
            mAllLines.add(lineViews)
            //记录行高
            mLineHeight.add(lineHeight)

            //在每次换行时计算自身所需的宽高
            parentNeedWidth = parentNeedWidth.coerceAtLeast(lineWidthUsed + mHorizontalSpacing)
            parentNeedHeight += lineHeight + mVerticalSpacing

            //重置参数
            lineViews = mutableListOf()
            lineWidthUsed = 0
            lineHeight = 0
        }


        //先获取自身的测量模式以及大小，再根据子View的测量结果来确定自身的宽高
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //确定最终的宽高
        setMeasuredDimension(
                if (widthMode == MeasureSpec.EXACTLY) selfWidth else parentNeedWidth,
                if (heightMode == MeasureSpec.EXACTLY) selfHeight else parentNeedHeight
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var curL = paddingLeft
        var curT = paddingTop

        //逐个将每一行的控件进行摆放
        for (i in 0 until mAllLines.size) {

            val lineViews = mAllLines[i]
            val lineHeight = mLineHeight[i]

            lineViews.forEach { view ->
                val left = curL
                val top = curT
                val right = left + view.measuredWidth
                val bottom = top + view.measuredHeight
                //注意要点：在onMeasure之后能够获取measuredWidth或measuredHeight，但获取width/height无效，必须在view.layout之后才生效
                view.layout(left, top, right, bottom)
                //计算下一个控件的left
                curL = right + mHorizontalSpacing
            }
            //计算下一行控件的Left
            curL = paddingLeft
            //计算下一行控件的Top
            curT += lineHeight + mVerticalSpacing
        }
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                Resources.getSystem().displayMetrics
        ).toInt()
    }

    fun initData(list: List<String?>) {
        this.removeAllViews()
        val count = list.size
        for (i in 0 until count) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.layout_item_fl_tv, this, false)
            val tv: TextView = view.findViewById<View>(R.id.tv_item_fl) as TextView
            tv.text = list[i]
            tv.setOnClickListener {
                list[i]?.let { it1 -> flowItemClickListener?.onFlowItemClick(it1) }
            }
            this.addView(view)
        }
    }

    interface OnFlowItemClickListener {
        fun onFlowItemClick(str: String)
    }

    private var flowItemClickListener : OnFlowItemClickListener? = null

    fun setOnFlowItemClickListener(flowItemClickListener:OnFlowItemClickListener) {
        this.flowItemClickListener = flowItemClickListener
    }
}