package com.aiwujie.shengmo.kt.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.showToast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.app_activity_simple_anim.view.*
import kotlin.math.roundToInt

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: SimpleAnimActivity
 * @Author: xmf
 * @CreateDate: 2022/5/5 10:08
 * @Description:
 */
class SimpleAnimActivity:Activity() {
    private val ivAnim:ImageView by lazy { findViewById<ImageView>(R.id.iv_simple_anim) }

    companion object {
        fun start(context: Context,url:String) {
            val intent = Intent(context,SimpleAnimActivity::class.java)
            intent.putExtra(IntentKey.URL,url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_simple_anim)
        Glide.with(this)
                .load(intent.getStringExtra(IntentKey.URL))
                .listener(object :RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        startAnim()
                        return false
                    }
                })
                .into(ivAnim)
    }

    fun startAnim() {
        val anim1 = ValueAnimator.ofFloat(1f,0.5f,1f,1.5f,1f)
        anim1.duration = 3000
        anim1.repeatCount = 0
        var w = ivAnim.width
        if (w == 0) {
            w = 150.dp
        }
        anim1.addUpdateListener {
            val animValue = it.animatedValue  as Float
            ivAnim.layoutParams.apply {
                this.width = ((w * animValue).roundToInt())
                this.height = this.width
                ivAnim.layoutParams = this
            }
        }
        anim1.start()
        anim1.addListener(object :Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

        })
    }
}
