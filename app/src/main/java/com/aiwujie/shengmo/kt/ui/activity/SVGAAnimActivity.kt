package com.aiwujie.shengmo.kt.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.IntentKey
import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.net.URL


class SVGAAnimActivity : Activity() {
    companion object {
        fun start(context:Context,url:String) {
            val intent = Intent(context,SVGAAnimActivity::class.java)
            intent.putExtra(IntentKey.URL,url)
            context.startActivity(intent)
        }
    }


    lateinit var mSVGAImage: SVGAImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_svga_anim)
        mSVGAImage = findViewById(R.id.svga_view_anim)
        showAnim()
    }
    var svgaParser: SVGAParser? = null
    private fun showAnim() {
        val svgaUrl = intent.getStringExtra(IntentKey.URL)
        val parser = SVGAParser.shareParser()
        //val parser = SVGAParser(this)
        try {
            parser.decodeFromURL(URL(svgaUrl), object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    mSVGAImage.setVideoItem(videoItem)
                    mSVGAImage.loops = 1
                    mSVGAImage.startAnimation()
                    mSVGAImage.callback = object : SVGACallback {
                        override fun onPause() {}
                        override fun onFinished() {
                            mSVGAImage.stopAnimation(true)
                            finish()
                        }
                        override fun onRepeat() {}
                        override fun onStep(i: Int, v: Double) {}
                    }
                }

                override fun onError() {
                    finish()
                }
            })
        }catch (e:Exception) {
            finish()
        }

    }
}