package com.aiwujie.shengmo.kt

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.aiwujie.shengmo.utils.BitmapUtils
import com.aiwujie.shengmo.utils.LogUtil
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition

class TransformationUtil(var target: ImageView) : ImageViewTarget<Bitmap>(target) {

    override fun setResource(resource: Bitmap?) {

    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        super.onResourceReady(resource, transition)

        view.setImageBitmap(resource)
        var width = resource!!.width
        var height = resource!!.height
        LogUtil.d("$width    $height")
        target.layoutParams = BitmapUtils.suitImgPara(target, width, height)

    }

}