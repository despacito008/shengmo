package com.aiwujie.shengmo.kt.ui.activity.normaldeal


import com.aiwujie.shengmo.bean.PunishmentBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toPicStr
import com.aiwujie.shengmo.kt.util.toPicStr2
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil

class PunishmentEditActivity : NormalDealActivity() {
    override fun getPageTitle(): String {
        return "编辑警示信息"
    }

    override fun isShowCheckBox(): Boolean {
        return true
    }

    override fun initPicList(): ArrayList<String> {
        val punishBean = intent.getSerializableExtra("info") as PunishmentBean.DataBean
        val tempImage = ArrayList<String>()
        punishBean?.run {
            tempImage.addAll(image)
        }
        return tempImage
    }
    var punishId = ""
    override fun initViewComplete() {
        super.initViewComplete()
        cbIsPublic.isChecked = true
        val punishBean = intent.getSerializableExtra("info") as PunishmentBean.DataBean
        punishBean?.run {
            limitLayout.setContent(blockreason)
            punishId = pid
        }
    }

    override fun doCommit() {
        HttpHelper.getInstance().editPunishment(punishId, limitLayout.getContent(),picList.toPicStr2(),cbIsPublic.isChecked,object:HttpCodeMsgListener {
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