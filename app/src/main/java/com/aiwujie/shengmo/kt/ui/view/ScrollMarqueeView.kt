package com.aiwujie.shengmo.kt.ui.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.dp


class ScrollMarqueeView : LinearLayout {
    private var tvBannerOne: TextView
    private lateinit var runnable: Runnable
    private var mHandler = Handler()
    private var list: ArrayList<String>
    private var position = 0
    private var offsetY = 100
    private var hasPostRunnable = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.app_layout_scroll_text_marquee, this)
        tvBannerOne = rootView.findViewById(R.id.tv_layout_scroll_text_one)
        list = ArrayList()
        runnable = Runnable {
            tvBannerOne.text = list[position]
            val anim1 = ObjectAnimator.ofFloat(tvBannerOne, "translationY", 0f, -offsetY.toFloat()).setDuration(500)
            val anim2 = ObjectAnimator.ofFloat(tvBannerOne, "translationY", offsetY.toFloat(), 0f).setDuration(500)
            anim1.start()
            anim1.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }
                override fun onAnimationCancel(animation: Animator?) {

                }
                override fun onAnimationStart(animation: Animator?) {

                }
                override fun onAnimationEnd(animation: Animator?) {
                    tvBannerOne.text = list[++position]
                    if (position == list.size - 1) {
                        position = 0
                    }
                    anim2.start()
                }

            })
            anim2.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mHandler.postDelayed(runnable, 4000)
                }
            })
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        offsetY = measuredHeight
    }


    private fun initBannerTextView(textView: TextView) {
        with(textView) {
            textSize = 12.dp.toFloat()
            setSingleLine()
            setTextColor(ContextCompat.getColor(context, R.color.normalGray))
        }
    }

    fun getList(): List<String?>? {
        return list
    }

    fun setList(aList: ArrayList<String>) {
        list = aList
        //处理最后一条数据切换到第一条数据 太快的问题
        if (list.size > 1) {
            list.add(list[0])
        }
    }

    fun startScroll() {
        if (list.size > 1) {
            tvBannerOne.text = list[0]
            if (!hasPostRunnable) {
                hasPostRunnable = true
                mHandler.postDelayed(runnable, 3000)
            }
        } else {
            hasPostRunnable = false
        }
    }

    fun stopScroll() {
        mHandler.removeCallbacks(runnable)
        hasPostRunnable = false
    }

}