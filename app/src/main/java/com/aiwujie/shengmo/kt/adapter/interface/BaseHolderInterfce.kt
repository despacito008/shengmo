package com.aiwujie.shengmo.kt.adapter.`interface`

import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout

interface BaseHolderInterfce {


    fun onPictureAdd(layoutPosition:Int)

    fun onDeleteAdapterItem(layoutPosition:Int)

    fun onAdapterNotify()


    fun onNinePictureItemClick(layoutPosition:Int,position: Int,models :ArrayList<String>,photoLayout: BGASortableNinePhotoLayout)


}