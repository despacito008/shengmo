package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.GroupInfoActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.PublicScreenRedBagAdapter
import com.aiwujie.shengmo.adapter.PublicScreenVipAdapter
import com.aiwujie.shengmo.bean.NoticePresentData
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 大喇叭 - 发红包
 */
class PublicScreenRedBagFragment : LazyFragment() {
    var page = 0
    var publicScreenAdapter: PublicScreenRedBagAdapter? = null
    lateinit var noticeList: ArrayList<NoticePresentData.DataBean>
    override fun loadData() {
        HttpHelper.getInstance().getNoticeRedBagData(page, object : HttpCodeMsgListener {
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
                                publicScreenAdapter = PublicScreenRedBagAdapter(activity, noticeList,this@PublicScreenRedBagFragment)
                                rvPublicScreen.adapter = publicScreenAdapter
                                rvPublicScreen.layoutManager = LinearLayoutManager(activity)
                                publicScreenAdapter?.setOnNoticeItemClick(object :PublicScreenRedBagAdapter.OnNoticeItemClick {
                                    override fun doItemSendIconClick(index: Int) {
                                        val intent = Intent(activity,UserInfoActivity::class.java)
                                        intent.putExtra("uid",noticeList[index].uid)
                                        startActivity(intent)
                                    }

                                    override fun doItemReceiveIconClick(index: Int) {
                                        noticeList[index].run {
                                            if (gid.isNullOrEmpty()) {
                                                val intent = Intent(activity,UserInfoActivity::class.java)
                                                intent.putExtra("uid",noticeList[index].fuid)
                                                startActivity(intent)
                                            } else {
//                                                val intent = Intent(activity,GroupInfoActivity::class.java)
//                                                intent.putExtra("groupId",noticeList[index].gid)
//                                                startActivity(intent)
                                                GroupDetailActivity.start(activity,noticeList[index].gid,0,false)
                                            }
                                        }
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
        tvTip.text = "发500魔豆以上红包可上大喇叭"

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