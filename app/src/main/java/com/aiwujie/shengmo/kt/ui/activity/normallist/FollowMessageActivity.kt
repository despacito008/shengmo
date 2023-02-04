package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.FollowMsgData
import com.aiwujie.shengmo.kt.adapter.FollowMessageAdapter
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 关注提醒
 */
class FollowMessageActivity : NormalListActivity() {
    override fun getPageTitle(): String {
        return "关注提醒"
    }

    override fun loadData() {
        loadingHolder.showLoading()
        getFollowData()
    }

    var page = 0
    lateinit var msgList: ArrayList<FollowMsgData.DataBean>
    var msgAdapter: FollowMessageAdapter? = null
    private fun getFollowData() {
        HttpHelper.getInstance().getFollowMessage(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, FollowMsgData::class.java)
                loadingHolder.showLoadSuccess()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            msgList.clear()
                            msgList.addAll(this)
                            msgAdapter = FollowMessageAdapter(this@FollowMessageActivity, msgList)
                            with(rvList) {
                                adapter = msgAdapter
                                layoutManager = LinearLayoutManager(this@FollowMessageActivity)
                            }
                            msgAdapter?.onSimpleItemListener = OnSimpleItemListener { index ->
                                val intent = Intent(this@FollowMessageActivity, UserInfoActivity::class.java)
                                intent.putExtra("uid", msgList[index].uid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val tempIndex = msgList.size
                            msgList.addAll(this)
                            msgAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 3000 && page == 0) {
                    loadingHolder.showEmpty()
                } else {
                    loadingHolder.showLoadSuccess()
                }
            }
        })
    }

    override fun initViewComplete() {
        super.initViewComplete()
        tvNormalTitleMore.text = "清空"
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        msgList = ArrayList()
        tvNormalTitleMore.setOnClickListener {
            if (msgList.size != 0) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("确认清空吗?")
                        .setPositiveButton("否") { dialog, _ ->
                            dialog.dismiss()
                        }.setNegativeButton("是") { dialog, _ ->
                                clearMsg()
                                dialog.dismiss()
                        }.create().show()
            }
        }
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getFollowData()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getFollowData()
            }
        })
    }

    private fun clearMsg() {
        HttpHelper.getInstance().deleteFollowMessage(object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@FollowMessageActivity,msg)
                loadingHolder.showLoading()
                page = 0
                getFollowData()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@FollowMessageActivity,msg)
            }
        })
    }
}