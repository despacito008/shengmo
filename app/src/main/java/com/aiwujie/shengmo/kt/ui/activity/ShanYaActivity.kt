package com.aiwujie.shengmo.kt.ui.activity

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.zdyview.CountDownButton
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import java.util.*

class ShanYaActivity : AppCompatActivity() {
    private lateinit var mcountbutton: CountDownButton
    private lateinit var shanurl: String
    private lateinit var iv_aishan: ImageView
    private lateinit var tvyayaya: TextView
    private lateinit var iv_shan1: SimpleDraweeView
    private lateinit var mivshan: ImageView
    var handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shan_ya)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        val intent = intent
        shanurl = intent.getStringExtra("shanurl")
        mcountbutton = findViewById(R.id.countdown2)
        mivshan = findViewById(R.id.iv_shan)
        iv_shan1 = findViewById(R.id.iv_shan1)
        iv_aishan = findViewById(R.id.iv_aishan)
        tvyayaya = findViewById(R.id.tvyayaya)

        iv_shan1.setImageURI(shanurl)
        showUrlBlur(iv_shan1, shanurl, 20, 30)
        handler.postDelayed(runnable, 500)
        mcountbutton.setOnCountClickListener(object : CountDownButton.onCountClickListener {
            override fun onCounting(process: Double) {
                runOnUiThread { //                        int temp =  (5 - (int)(process / 20f));
                    val temp = 5 - (process * 5).toInt()
                    mcountbutton.text = "" + temp
                }
            }

            override fun onCountingClick() {}
            override fun onCountOverClick() {
                signFlashImg()
                finish()
            }
        })
    }

    val  runnable =Runnable {
        val requestOptions = RequestOptions()
        //                requestOptions.placeholder(R.drawable.rc_image_error);
        requestOptions.error(R.mipmap.default_error)
        //                requestOptions.transform(new GlideCircleTransform(mContext));
        Glide.with(applicationContext).load(shanurl).apply(requestOptions).into(mivshan)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                iv_shan1.visibility = View.GONE
                mcountbutton.visibility = View.VISIBLE
                tvyayaya.visibility = View.GONE
                iv_aishan.visibility = View.GONE
                mcountbutton.totalTime = 5000
                mcountbutton.startTimer()

            }
            MotionEvent.ACTION_UP -> {
                signFlashImg()
                mcountbutton.cancle()
                finish()
            }
        }
        return super.onTouchEvent(event)
    }
    override fun finish() {
        super.finish()


    }

    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    fun showUrlBlur(draweeView: SimpleDraweeView, url: String?, iterations: Int, blurRadius: Int) {
        try {
            val uri = Uri.parse(url)
            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(IterativeBoxBlurPostProcessor(6, blurRadius))
                    .build()
            val controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.controller)
                    .setImageRequest(request)
                    .build()
            draweeView.controller = controller
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun signFlashImg() {
        var flashImageCacheList = SharedPreferencesUtils.getDataList<String?>("flash_img_" + MyApp.uid)
        if (flashImageCacheList == null) {
            flashImageCacheList = ArrayList()
        }
        flashImageCacheList.add(shanurl)
        SharedPreferencesUtils.addDataList("flash_img_" + MyApp.uid, flashImageCacheList)
    }


}