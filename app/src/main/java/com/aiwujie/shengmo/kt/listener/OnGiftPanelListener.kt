package com.aiwujie.shengmo.kt.listener

import com.aiwujie.shengmo.bean.NormalGiftBean

public interface OnGiftPanelListener {
    fun OnGiftPanelChoose(goodsBean: NormalGiftBean.DataBean.GoodsBean, num:Int, totalPrice:Int, isOpen:Boolean)
}