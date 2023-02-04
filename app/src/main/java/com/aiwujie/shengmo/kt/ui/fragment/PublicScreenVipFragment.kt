package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.PublicScreenVipAdapter
import com.aiwujie.shengmo.bean.NoticePresentData
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 大喇叭 - 赠送会员
 */
class PublicScreenVipFragment : LazyFragment() {
    var page = 0
    var publicScreenAdapter: PublicScreenVipAdapter? = null
    lateinit var noticeList: ArrayList<NoticePresentData.DataBean>
    override fun loadData() {
        HttpHelper.getInstance().getNoticeData(1, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                activity?.let {
                    refreshPublicScreen.finishLoadMore()
                    refreshPublicScreen.finishRefresh()
                    val tempData = GsonUtil.GsonToBean(data, NoticePresentData::class.java)
                    tempData?.data?.let {
                        when (page) {
                            0 -> {
                                noticeList.clear()
                                noticeList.addAll(it)
                                publicScreenAdapter = PublicScreenVipAdapter(activity, noticeList,this@PublicScreenVipFragment)
                                rvPublicScreen.adapter = publicScreenAdapter
                                rvPublicScreen.layoutManager = LinearLayoutManager(activity)
                                publicScreenAdapter?.setOnNoticeItemClick(object :PublicScreenVipAdapter.OnNoticeItemClick {
                                    override fun doItemSendIconClick(index: Int) {
                                        val intent = Intent(activity,UserInfoActivity::class.java)
                                        intent.putExtra("uid",noticeList[index].uid)
                                        startActivity(intent)
                                    }

                                    override fun doItemReceiveIconClick(index: Int) {
                                        val intent = Intent(activity,UserInfoActivity::class.java)
                                        intent.putExtra("uid",noticeList[index].fuid)
                                        startActivity(intent)
                                    }
                                })
                            }
                            else -> {
                                val temp = noticeList.size
                                noticeList.addAll(it)
                                publicScreenAdapter?.notifyItemRangeInserted(temp, it.size)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshPublicScreen.finishLoadMore()
                refreshPublicScreen.finishRefresh()
            }
        })
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_public_screen
    }

    lateinit var rvPublicScreen: RecyclerView
    lateinit var refreshPublicScreen: SmartRefreshLayout
    lateinit var tvTip: TextView
    override fun initView(rootView: View) {
        noticeList = ArrayList()
        tvTip = rootView.findViewById(R.id.tv_fragment_public_screen)
        rvPublicScreen = rootView.findViewById(R.id.rv_fragment_public_screen)
        refreshPublicScreen = rootView.findViewById(R.id.smart_refresh_public_screen)
        tvTip.text = "送SVIP会员可上大喇叭"

        refreshPublicScreen.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                loadData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                loadData()
            }
        })
    }
}