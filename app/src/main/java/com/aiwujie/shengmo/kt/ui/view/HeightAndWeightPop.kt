package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.customview.WheelView
import razerdp.basepopup.BasePopupWindow

class HeightAndWeightPop:BasePopupWindow {
    var tvCancel:TextView = findViewById(R.id.tv_cancel)
    var tvConfirm:TextView = findViewById(R.id.tv_select)
    var wvHeight:WheelView = findViewById(R.id.wheel_view_height)
    var wvWeight:WheelView = findViewById(R.id.wheel_view_weight)
    var heightStr = "170cm"
    var weightStr = "60kg"
    constructor(context: Context) : super(context) {
        tvCancel.setOnClickListener {
            dismiss()
        }

        val heightList = ArrayList<String>()
        for (h in 120..220) {
            heightList.add("$h cm")
        }

        val weightList = ArrayList<String>()
        for (w in 30..130) {
            weightList.add("$w kg")
        }

        with(wvHeight) {

            offset = 2
            setSeletion(50)
            setItems(heightList)
            onWheelViewListener = object: WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String?) {
                    super.onSelected(selectedIndex, item)
                    heightStr = item ?: "170cm"
                }
            }
        }
        with(wvWeight) {
            offset = 2
            setSeletion(30)
            setItems(weightList)
            onWheelViewListener = object: WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String?) {
                    super.onSelected(selectedIndex, item)
                    weightStr = item ?: "60kg"
                }
            }
        }

        tvConfirm.setOnClickListener {
            dismiss()
            onHeightAndWeightListener?.doChooseComplete(heightStr,weightStr)
        }

    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_height_and_weight)
    }

    interface OnHeightAndWeightListener {
        fun doChooseComplete(height:String,weight:String)
    }

    var onHeightAndWeightListener:OnHeightAndWeightListener? = null
}