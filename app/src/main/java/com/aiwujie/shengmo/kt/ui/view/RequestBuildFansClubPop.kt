package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.OpenClubInfoBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: RequestBuildFansClubPop
 * @Author: xmf
 * @CreateDate: 2022/5/31 9:18
 * @Description:
 */
class RequestBuildFansClubPop(context: Context):BasePopupWindow(context) {

    private val etCard:EditText by lazy { findViewById<EditText>(R.id.et_fan_club_card) }
    private val tvCommit:TextView by lazy { findViewById<TextView>(R.id.tv_pop_commit) }
    private val tvInfo:TextView by lazy { findViewById<TextView>(R.id.tv_pop_info) }

    init {
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED)

        tvCommit.setOnClickListener {
            if (etCard.text.isEmpty()) {
                "请输入你的徽章名".showToast()
                return@setOnClickListener
            }
            requestBuildFansClubPop()
        }

        getOpenInfo()
    }
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_request_build_fans_club)
    }

    private fun getOpenInfo() {
        HttpHelper.getInstance().getOpenClubInfo(object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,OpenClubInfoBean::class.java)
                tempData?.data?.run {
                    tvInfo.text = intro
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun requestBuildFansClubPop() {
        HttpHelper.getInstance().openFansClub(etCard.text.toString(),object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                onBuildClubListener?.doBuildSuc()
                dismiss()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    interface OnBuildClubListener {
        fun doBuildSuc()
    }

    var onBuildClubListener:OnBuildClubListener? = null
}
