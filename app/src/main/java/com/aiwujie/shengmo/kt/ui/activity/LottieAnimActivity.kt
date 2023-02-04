package com.aiwujie.shengmo.kt.ui.activity

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieOnCompositionLoadedListener
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.utils.LogUtil


class LottieAnimActivity : Activity() {
    lateinit var lottieView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_actitivty_lottie_anim)
        lottieView = findViewById(R.id.lottie_view_anim)
        showAnim()
    }

    private fun showAnim() {
        val lottieUrl = intent.getStringExtra("url")
        LogUtil.d(lottieUrl)
        lottieView.setAnimationFromUrl(lottieUrl)
        lottieView.addAnimatorUpdateListener {
            //LogUtil.d("fraction" + it.animatedFraction)
            // 判断动画加载结束
            if (it.animatedFraction == 1f) {
                lottieView.clearAnimation()
                finish()
            }
        }
//        lottieView.addLottieOnCompositionLoadedListener(object :LottieOnCompositionLoadedListener {
//            override fun onCompositionLoaded(composition: LottieComposition?) {
//
//            }
//        })

        lottieView.addAnimatorListener(object :Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {
                finish()
            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

        })

//        lottieView.imageAssetsFolder = "images"
//        lottieView.setAnimation("svga.json")
//        lottieView.loop(true)
//        lottieView.playAnimation()
    }
}