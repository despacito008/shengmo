package com.aiwujie.shengmo.kt.ui.activity.normaldeal

import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toPicStr
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper

/**
 * 用户主页 - 官方备注
 */
class ManagerMarkActivity : NormalDealActivity(){

    override fun getPageTitle(): String {
        return "官方备注"
    }

    override fun isShowCheckBox(): Boolean {
        return false
    }

    override fun initPicList(): ArrayList<String> {
        return intent.getStringArrayListExtra("sypic") ?: ArrayList()
    }

    override fun initViewComplete() {
        super.initViewComplete()
        limitLayout.setContent(intent.getStringExtra("name") ?: "")
    }

    override fun doCommit() {
        val uid = intent.getStringExtra("fuid") ?: ""
        HttpHelper.getInstance().setManagerMark(uid,limitLayout.getContent(),picList.toPicStr(),object:HttpCodeMsgListener {
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