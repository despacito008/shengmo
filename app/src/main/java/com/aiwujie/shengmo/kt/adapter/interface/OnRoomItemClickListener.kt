package com.aiwujie.shengmo.kt.adapter.`interface`

import com.aiwujie.shengmo.bean.ScenesRoomInfoBean

interface OnRoomItemClickListener {

    fun  onRoomClickItem(possition:Int,model : ScenesRoomInfoBean)
}