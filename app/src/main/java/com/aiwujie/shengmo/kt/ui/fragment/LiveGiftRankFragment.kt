package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle

import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper

import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.net.LiveHttpHelper
import com.aiwujie.shengmo.timlive.adapter.GiftViewPagerAdapter
import com.aiwujie.shengmo.timlive.bean.GiftCoinListInfo
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class LiveGiftRankFragment : NormalListFragment() {
    lateinit var list: ArrayList<GiftCoinListInfo.DataBean.RankingListBean>
    var adapter: GiftViewPagerAdapter? = null
    var mType: String? = null
    var page = 0

    //
    companion object {

        fun newInstance(type: String): LiveGiftRankFragment {
            var fragment: LiveGiftRankFragment? = null
            val args = Bundle()
            args.putString("type", type)
            if (fragment == null) {
                fragment = LiveGiftRankFragment()
            }
            fragment.arguments = args
            return fragment
        }
    }

//    companion object {
//        var   instance : LiveGiftRankFragment? = null
//        fun  getInStance(type: String?) :LiveGiftRankFragment{
//            if (instance == null){
//                instance = LiveGiftRankFragment()
//            }
//            var args =Bundle()
//            args.putString("type", type)
//            instance!!.arguments = args
//            return  instance!!
//        }
//    }


    private fun initData(type: String?) {
        HttpHelper.getInstance().getGiftRank(MyApp.anchor_id, type, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String,msg:String) {
                LogUtil.e(data)
                val giftListInfo = GsonUtil.GsonToBean(data, GiftCoinListInfo::class.java)
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                giftListInfo?.data?.run {
                    when (page) {
                        0 -> {
                            if (rankingList.size == 0) {
                                gLoadHolder.showEmpty()
                                return
                            }

                            list.clear()
                            list.addAll(rankingList)
                            with(rvSearchResult) {
                                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                                adapter = GiftViewPagerAdapter(context, list, GiftViewPagerAdapter.OnItemClickListener { _, data ->
                                    val intent = Intent(context, UserInfoActivity::class.java)
                                    intent.putExtra("uid", data!!.uid)
                                    context.startActivity(intent)
                                })
                            }
                        }
                        else -> {
                            val tempIndex = list.size
                            list.addAll(rankingList)
                            adapter?.notifyItemRangeInserted(tempIndex, rankingList.size)


                        }
                    }

                }

            }

            override fun onFail(code:Int,msg: String) {
                LogUtil.e(msg)
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                gLoadHolder.showEmpty()
                gLoadHolder.showLoadSuccess()

            }
        })
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        list = ArrayList()
        mType = arguments.getString("type")

        initData(mType)
        RefreshData()
    }


    private fun RefreshData() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                initData(mType)
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                initData(mType)
            }
        })
    }

    override fun loadData() {


    }

}