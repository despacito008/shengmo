package com.aiwujie.shengmo.kt.ui.activity.normallist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.LinkHistoryBean
import com.aiwujie.shengmo.kt.adapter.LinkHistoryAdapter
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.normallist
 * @ClassName: LinkHistoryActivity
 * @Author: xmf
 * @CreateDate: 2022/5/11 14:16
 * @Description:
 */
class LinkHistoryActivity : NormalListActivity() {

    companion object {
        fun start(context: Context, uid: String) {
            val intent = Intent(context, LinkHistoryActivity::class.java)
            intent.putExtra(IntentKey.UID, uid)
            context.startActivity(intent)
        }
    }

    override fun getPageTitle(): String {
        return "连线历史"
    }

    override fun loadData() {
        initHeaderView()
        getLinkHistory()
    }

    override fun initViewComplete() {
        super.initViewComplete()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getLinkHistory()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getLinkHistory()
            }
        })
    }

    var page = 0
    var linkHistoryAdapter: LinkHistoryAdapter? = null
    private val linkHistoryList: ArrayList<LinkHistoryBean.DataBean.ListBean> by lazy { ArrayList<LinkHistoryBean.DataBean.ListBean>() }
    private fun getLinkHistory() {
        HttpHelper.getInstance().getUserLinkHistory(intent.getStringExtra(IntentKey.UID), page, object : HttpCodeMsgListener {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, LinkHistoryBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            tvNum?.text = chattimes
                            tvLength?.text = all_time_length
                            tvBeans?.text = "${all_beans}豆"
                            linkHistoryList.clear()
                            linkHistoryList.addAll(this.list)
                            linkHistoryAdapter = LinkHistoryAdapter(this@LinkHistoryActivity, linkHistoryList)
                            val headerAdapter = HeaderViewAdapter(linkHistoryAdapter)
                            headerAdapter.addHeaderView(headerView)
                            with(rvList) {
                                adapter = headerAdapter
                                layoutManager = LinearLayoutManager(context)
                                val itemDecoration = DividerItemDecoration(this@LinkHistoryActivity, OrientationHelper.VERTICAL)
                                itemDecoration.setDrawable(ColorDrawable(ContextCompat.getColor(this@LinkHistoryActivity, R.color.colorGray)))
                                addItemDecoration(itemDecoration)
                            }
                            linkHistoryAdapter?.simpleItemListener = OnSimpleItemListener {
                                UserInfoActivity.start(this@LinkHistoryActivity, linkHistoryList[it].uid)
                            }
                        }
                        else -> {
                            val tempIndex = linkHistoryList.size
                            linkHistoryList.addAll(this.list)
                            linkHistoryAdapter?.notifyItemRangeInserted(tempIndex, this.list.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
            }
        })
    }

    lateinit var headerView:View
    var tvNum: TextView? = null
    var tvLength: TextView? = null
    var tvBeans: TextView? = null
    private fun initHeaderView() {
         headerView = View.inflate(this, R.layout.app_layout_link_history, null)
         tvNum = headerView.findViewById(R.id.tv_link_times)
         tvLength = headerView.findViewById(R.id.tv_link_length)
         tvBeans = headerView.findViewById(R.id.tv_link_beans)
    }
}
