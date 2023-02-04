package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.PesonInfoActivity
import com.aiwujie.shengmo.bean.WarningListData
import com.aiwujie.shengmo.kt.adapter.WarningUserAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class IllegalUserFragment :NormalListFragment() {


    companion object{
        fun newInstance(type:String):IllegalUserFragment {
            val args = Bundle()
            args.putString("type",type)
            val fragment =IllegalUserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var mList: ArrayList<WarningListData.DataBean>
    lateinit var  warningUserAdapter:WarningUserAdapter
    var page = 0
    var mType:String? =  null

    override fun initView(rootView: View) {
        super.initView(rootView)
        mList = ArrayList()
        mType= arguments.getString("type")
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getData()
            }
        })

    }

    fun getData() {
        HttpHelper.getInstance().getWarnUserData(page, mType,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                var model = GsonUtil.GsonToBean(data, WarningListData::class.java)
                model?.data?.run {
                    when (page) {
                        0 -> {
                            mList.clear()
                            mList.addAll(this)
                            warningUserAdapter = WarningUserAdapter(context, mList, mType != "2",mType!!)
                            with(rvSearchResult){
                                adapter =warningUserAdapter
                                layoutManager =LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            }
                            warningUserAdapter.onUserClick =object :WarningUserAdapter.OnUserClick{
                                override fun onUserClick(position: Int) {
                                    val intent = Intent(activity, PesonInfoActivity::class.java)
                                    intent.putExtra("uid", mList[position].uid)
                                    startActivity(intent)
                                }

                            }

                        }
                        else -> {
                            val tempSize = mList.size
                            mList.addAll(this)
                            warningUserAdapter.notifyItemRangeInserted(tempSize,this.size)
                        }

                    }


                }

            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 3000) {
                    if (page == 0) {
                        gLoadHolder.showEmpty()
                    } else {
                        gLoadHolder.showLoadSuccess()
                    }
                } else {
                    msg?.showToast()
                    gLoadHolder.showLoadSuccess()
                }
            }
        })
    }

    override fun loadData() {
        gLoadHolder.showLoading()
        getData()
    }
}