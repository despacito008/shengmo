package com.aiwujie.shengmo.kt.ui.activity.normaldeal

import android.text.TextUtils
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toPicStr
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper

class ManagerBanActivity : NormalDealActivity() {
    override fun getPageTitle(): String {
        val title = intent.getStringExtra("title")
        return title ?: "账号处理"
    }

    override fun isShowCheckBox(): Boolean {
        return true
    }

    override fun initPicList(): ArrayList<String> {
        return ArrayList()
    }

    override fun doCommit() {
        val method = intent.getStringExtra("method")
        val day = intent.getStringExtra("days")
        val uid = intent.getStringExtra("uid")
        val activityType = intent.getStringExtra("activityType")
        var isShow = if (cbIsPublic.isChecked) {
            "1"
        } else {
            "0"
        }
        if (!TextUtils.isEmpty(activityType)) {
            HttpHelper.getInstance().adminBannedCahtAndInfo(uid, method, day, limitLayout.getContent(), isShow, picList.toPicStr(), object : HttpCodeMsgListener {
                override fun onSuccess(data: String?, msg: String?) {
                    msg?.showToast()
                    finish()
                }


                override fun onFail(code: Int, msg: String?) {
                    msg?.showToast()
                }
            })

        } else {
            HttpHelper.getInstance().banUserStatus(uid, method, day, limitLayout.getContent(), picList.toPicStr(), cbIsPublic.isChecked, object : HttpCodeMsgListener {
                override fun onSuccess(data: String?, msg: String?) {
                    msg?.showToast()
                    finish()
                }

                override fun onFail(code: Int, msg: String?) {
                    msg?.showToast()
                }
            })
        }


    }
}