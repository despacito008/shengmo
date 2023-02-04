package com.aiwujie.shengmo.kt.ui.fragment.atmember

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.*
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent
import com.aiwujie.shengmo.kt.adapter.AtMemberHighUserAdapter
import com.aiwujie.shengmo.kt.adapter.HomeHighUserAdapter
import com.aiwujie.shengmo.kt.adapter.HomeHighUserGridAdapter
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity
import com.aiwujie.shengmo.kt.ui.fragment.HomePageNearFragment
import com.aiwujie.shengmo.kt.ui.fragment.LazyFragment
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_get_out_of_line.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AtMemberHighFragment : NormalListFragment() {

    companion object {
        var fragment: AtMemberHighFragment? = null
        fun newInstance(): AtMemberHighFragment {
            if (fragment == null) {
                fragment = AtMemberHighFragment()
            }
            return fragment as AtMemberHighFragment
        }

    }

    private var page = 0
    lateinit var mList: ArrayList<HighUserBean>
    private var homeHighUserAdapter: AtMemberHighUserAdapter? = null
    lateinit var atBeanList:ArrayList<Atbean.DataBean>
    override fun initView(rootView: View) {
        super.initView(rootView)
        mList = ArrayList()
        atBeanList = (activity as AtMemberActivity).atBeanList
        refreshLayout.setNoMoreData(true)
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

    override fun loadData() {
        gLoadHolder.showLoading()
        getData()
    }


    fun getData() {
        HttpHelper.getInstance().getHighList("", this.page.toString(), "", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showLoadComplete()
                val model = GsonUtil.GsonToBean(data, HighBeanModel::class.java)
                model?.data?.run {
                    when (page) {
                        0 -> {
                            mList.clear()
                            mList.addAll(this)

                            with(rvSearchResult) {
//
                                homeHighUserAdapter = AtMemberHighUserAdapter(context, mList,atBeanList)
                                adapter = homeHighUserAdapter
                                layoutManager = LinearLayoutManager(activity)

                                homeHighUserAdapter?.setOnSimpleItemListener(OnSimpleItemListener {
                                    mList[it].top_id  .run {
                                        if (atBeanList.map {
                                                    it.uid
                                                }.contains(this)) {
                                            removeUser(this)
                                        }  else {
                                            val atBean = Atbean.DataBean()
                                            atBean.uid = this
                                            atBean.nickname = "[高端]"+mList[it].serial_id
                                            atBeanList.add(atBean)
                                        }
                                        homeHighUserAdapter?.notifyItemChanged(it)
                                    }

                                })

                            }

                        }
                        else -> {
                            val tempIndex = mList.size
                            mList.addAll(this)
                            homeHighUserAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 4001 && page == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoadSuccess()
                }
            }
        })
    }
    fun removeUser(uid:String) {
        (activity as AtMemberActivity).removeAtUser(uid)
    }

}