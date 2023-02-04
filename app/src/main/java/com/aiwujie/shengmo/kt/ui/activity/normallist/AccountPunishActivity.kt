package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.adapter.GetOutOfLineAdapter
import com.aiwujie.shengmo.bean.PunishmentBean
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.normallist
 * @ClassName: AccountPunishActivity
 * @Author: xmf
 * @CreateDate: 2022/5/11 20:35
 * @Description:
 */
class AccountPunishActivity: NormalListActivity() {

    companion object {
        fun start(context: Context, uid: String, type:String,name:String) {
            val intent = Intent(context, AccountPunishActivity::class.java)
            intent.putExtra(IntentKey.UID, uid)
            intent.putExtra(IntentKey.TYPE,type)
            intent.putExtra(IntentKey.NAME,name)
            context.startActivity(intent)
        }
    }

    override fun getPageTitle(): String {
        return intent.getStringExtra(IntentKey.NAME) ?: "处罚详情"
    }

    override fun loadData() {
        getData()
    }

    var page = 0
    val type by lazy { intent.getStringExtra(IntentKey.TYPE) ?:"" }
    val uid by lazy {  intent.getStringExtra(IntentKey.UID) ?: "" }
    var punishmentAdapter:GetOutOfLineAdapter? = null
    private val punishmentList:ArrayList<PunishmentBean.DataBean> by lazy { ArrayList<PunishmentBean.DataBean>() }
    private fun getData() {
        HttpHelper.getInstance().getBlockingList(uid,type,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, PunishmentBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            punishmentList.clear()
                            punishmentList.addAll(this)
                            punishmentAdapter = GetOutOfLineAdapter(punishmentList,this@AccountPunishActivity)
                            with(rvList) {
                                adapter = punishmentAdapter
                                layoutManager = LinearLayoutManager(this@AccountPunishActivity)
                            }
                        }
                        else -> {
                            val tempIndex = punishmentList.size
                            punishmentList.addAll(this)
                            punishmentAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }
}
