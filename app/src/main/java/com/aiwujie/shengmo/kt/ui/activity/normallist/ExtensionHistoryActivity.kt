package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.bean.CallUseHistoryBean
import com.aiwujie.shengmo.kt.adapter.ExtensionHistoryAdapter
import com.aiwujie.shengmo.kt.ui.activity.ExtensionDetailActivity
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.normallist
 * @ClassName: ExtensionHistoryActivity
 * @Author: xmf
 * @CreateDate: 2022/6/6 19:11
 * @Description:
 */
class ExtensionHistoryActivity: NormalListActivity() {

    companion object {
        fun startActivity(context:Context) {
            val intent = Intent(context,ExtensionHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initViewComplete() {
        super.initViewComplete()
        refreshLayout.setBackgroundColor(Color.parseColor("#12333333"))
    }

    override fun getPageTitle(): String {
        return "推广明细"
    }

    override fun loadData() {
        page = 0
        getExtensionData()
    }

    var page = 0
    private val historyList: ArrayList<CallUseHistoryBean.DataBean> by lazy { ArrayList<CallUseHistoryBean.DataBean>() }
    var historyAdapter: ExtensionHistoryAdapter? = null
    private fun getExtensionData() {
        HttpHelper.getInstance().getCallUseHistory(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, CallUseHistoryBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            if (this.size == 0) {
                                loadingHolder.showEmpty()
                            } else {
                                loadingHolder.showLoadSuccess()
                            }
                            historyList.clear()
                            historyList.addAll(this)
                            historyAdapter = ExtensionHistoryAdapter(this@ExtensionHistoryActivity, historyList)
                            with(rvList) {
                                adapter = historyAdapter
                                layoutManager = LinearLayoutManager(this@ExtensionHistoryActivity)
                            }
                            historyAdapter?.simpleItemListener = OnSimpleItemListener {
                                ExtensionDetailActivity.start(this@ExtensionHistoryActivity,historyList[it].history_id)
                            }
                        }
                        else -> {
                            val tempIndex = historyList.size
                            historyList.addAll(this)
                            historyAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
}
