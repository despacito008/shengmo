package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import com.aiwujie.shengmo.kt.ui.adapter.NormalMenuAdapter
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import razerdp.basepopup.BasePopupWindow

class NormalMenuPopup (context: Context?) : BasePopupWindow(context) {
    lateinit var menuList:List<NormalMenuItem>
    constructor(context: Context?,menuList:List<NormalMenuItem>) : this(context) {
        this.menuList = menuList
        initView()
    }
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.layout_pop_normal_menu)
    }

    fun initView() {
        popupGravity = Gravity.BOTTOM
        var tvCancel = findViewById<TextView>(R.id.tv_pop_normal_menu_cancel)
        var rvMenuItem = findViewById<RecyclerView>(R.id.rv_pop_normal_menu)
        var menuAdapter = NormalMenuAdapter(context, menuList)
        var linearlayoutManager = LinearLayoutManager(context)
        rvMenuItem.adapter = menuAdapter
        rvMenuItem.layoutManager = linearlayoutManager
        tvCancel.setOnClickListener {
            this.dismiss()
        }
        menuAdapter.setOnSimpleItemListener(OnSimpleItemListener {
            onSimpleItemListener?.onItemListener(it)
        })
    }
    private  var onSimpleItemListener : OnSimpleItemListener? = null

    fun setOnSimpleItemListener(onSimpleItemListener : OnSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener
    }
}