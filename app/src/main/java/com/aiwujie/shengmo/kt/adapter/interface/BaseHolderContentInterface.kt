package com.aiwujie.shengmo.kt.adapter.`interface`

import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import java.util.ArrayList

interface BaseHolderContentInterface {


    fun onAddItem(layoutPosition: Int)

    fun onDeleteItem(layoutPosition: Int)

    fun onAdapterNotify()

    fun onAdapterNotify(layoutPosition: Int)

    fun onNineClickItem(layoutPosition: Int, position: Int, models: ArrayList<String>, photoLayout: BGASortableNinePhotoLayout)


}