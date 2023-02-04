package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.AllUserStates
import com.aiwujie.shengmo.customview.WheelView
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.utils.LogUtil
import razerdp.basepopup.BasePopupWindow

class AdminBanPop(context: Context, userState: AllUserStates.DataBean) : BasePopupWindow(context) {
    var tvCancel:TextView = findViewById(R.id.tv_cancel)
    var tvConfirm:TextView = findViewById(R.id.tv_select)
    var tvTitle:TextView = findViewById(R.id.tv_title)
    var wvOperate:WheelView = findViewById(R.id.wheel_view_height)
    var wvDay:WheelView = findViewById(R.id.wheel_view_weight)
    var operateStr = ""
    var dayStr = ""
    var operateIndex = 0
    var dayIndex = 0

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_height_and_weight)
    }

    interface OnOperateListener {
        fun doChooseComplete(operate:Int,day:Int,oStr:String,dStr:String)
    }

    var onOperateListener:OnOperateListener? = null

    init {
        tvCancel.setOnClickListener {
            dismiss()
        }
        val operateList = ArrayList<String>()
        with(operateList) {
            if (userState.dynamicstatus != "1") add("启动动态") else add("封禁动态")
            if (userState.chatstatus != "1") add("启动消息") else add("封禁消息")
            if (userState.infostatus != "1") add("启动资料") else add("封禁资料")
            if (userState.status != "1") add("启动账号") else add("封禁账号")
            if (userState.devicestatus != "1") add("启动设备") else add("封禁设备")
        }
        operateStr = "封禁动态"
        dayStr = "1天"
        val dayList = ArrayList<String>()
        with(dayList) {
            add("1天")
            add("3天")
            add("1周")
            add("2周")
            add("1月")
            add("永久")
        }
        tvTitle.text = "封禁操作"
        with(wvOperate) {
            offset = 2
            setSeletion(0)
            setItems(operateList)
            onWheelViewListener = object: WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String?) {
                    super.onSelected(selectedIndex, item)
                    operateStr = item ?: ""
                    operateIndex = selectedIndex - offset
                }
            }
        }
        with(wvDay) {
            offset = 2
            setSeletion(0)
            setItems(dayList)
            onWheelViewListener = object: WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String?) {
                    super.onSelected(selectedIndex, item)
                    dayStr = item ?: ""
                    dayIndex = selectedIndex - offset
                }
            }
        }
        tvConfirm.setOnClickListener {
            dismiss()
            LogUtil.d(operateIndex.toString() + operateStr)
            LogUtil.d(dayIndex.toString() + dayStr)
            onOperateListener?.doChooseComplete(operateIndex,dayIndex,operateStr,dayStr)
        }
    }
}