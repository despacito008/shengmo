package com.aiwujie.shengmo.kt.adapter.holder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.HighAuthDataBean
import com.aiwujie.shengmo.bean.HighAuthInfoBean
import com.aiwujie.shengmo.kt.adapter.`interface`.BaseHolderContentInterface
import com.aiwujie.shengmo.kt.ui.activity.HighEndAuthActivity
import com.aiwujie.shengmo.kt.util.showToast

abstract class HighAuthBaseHolder : RecyclerView.ViewHolder {

    var baseHolderContentInterface: BaseHolderContentInterface? = null


    constructor(itemView: View) : super(itemView) {
        layoutViews()
    }


    abstract fun layoutViews()

    abstract fun layoutDatas(mode: HighAuthInfoBean.DataInfoBean)

    object BaseHolder {
        var baseHolder: HighAuthBaseHolder? = null
        fun getInstance(context: Context, parent: ViewGroup?, viewType: Int): HighAuthBaseHolder? {
            when (viewType) {
                1 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.app_view_high_end_auth_wealth, parent, false)
                    baseHolder = HighWealthHolder(view, context)

                }
                2 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.app_view_high_end_auth_health, parent, false)
                    baseHolder = HighHealthHolder(view, context)
                }
                3 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.app_view_high_end_auth_education, parent, false)
                    baseHolder = HighEducationHolder(view, context)
                }
                4 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.app_view_high_end_auth_skill, parent, false)
                    baseHolder = HighSkillHolder(view, context)
                }
                5 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.app_view_high_end_auth_other, parent, false)
                    baseHolder = HighOtherHolder(view, context)
                }

            }
            return baseHolder

        }

    }


    protected fun setCanNotEditAndClick(view: View) {
        view.isFocusable = false;
        view.isFocusableInTouchMode = false;
        view.setOnClickListener {
            if (HighEndAuthActivity.IsTopUser != "1") {
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
        }
    }

    protected fun setCanNotEditAndNotClick(view: View) {
        view.isFocusable = false;
        view.isFocusableInTouchMode = false;
        view.isEnabled = false;

    }

    protected fun setCanEdit(view: View?) {
        view?.isFocusable = true
        view?.isFocusableInTouchMode = true
        view?.isEnabled = true
    }
}