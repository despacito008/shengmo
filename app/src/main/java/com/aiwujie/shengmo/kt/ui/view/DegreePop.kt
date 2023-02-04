package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.customview.WheelView
import razerdp.basepopup.BasePopupWindow

class DegreePop : BasePopupWindow {

    var tvCancel: TextView = findViewById(R.id.tv_cancel)
    var tvConfirm: TextView = findViewById(R.id.tv_select)
    var wvHeight: WheelView = findViewById(R.id.wheel_view_height)
    private val list = arrayListOf<String>("高中及以下", "大专", "本科", "双学士", "硕士", "博士", "博士后")

    constructor(context: Context) : super(context) {
        tvCancel.setOnClickListener {
            dismiss()
        }


        with(wvHeight) {
            offset = 2
            setSeletion(2)
            setItems(list)
            onWheelViewListener = object : WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String?) {
//                    super.onSelected(selectedIndex, item)
                    onDegreeListener?.onComplete(item?: "本科")
                }
            }
        }

        tvConfirm.setOnClickListener {
            onDegreeListener?.onComplete(wvHeight.seletedItem)
            dismiss()
        }

    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_degree)
    }

    interface OnDegreeListener {
        fun onComplete(str: String?)
    }

    var onDegreeListener: OnDegreeListener? = null


}