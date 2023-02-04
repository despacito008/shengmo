package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.*
import com.aiwujie.shengmo.eventbus.ChangeHighLayoutEvent
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent
import com.aiwujie.shengmo.kt.adapter.HomeHighUserAdapter
import com.aiwujie.shengmo.kt.adapter.HomeHighUserGridAdapter
import com.aiwujie.shengmo.kt.ui.activity.HighEndAuthActivity
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.showToast
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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomePageHighFragment : LazyFragment() {

    companion object {
        var fragment: HomePageHighFragment? = null
        fun newInstance(): HomePageHighFragment {
            if (fragment == null) {
                fragment = HomePageHighFragment()
            }
            return fragment as HomePageHighFragment
        }

    }

    private var page = 0
    lateinit var mList: ArrayList<HighUserBean>
    private var homeHighUserAdapter: HomeHighUserAdapter? = null
    private var homeHighUserGridAdapter: HomeHighUserGridAdapter? = null
    private var layout = HomePageNearFragment.LayOutType.List
    lateinit var gLoadHolder: Gloading.Holder
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var recycleview: RecyclerView
    private var ToSex = ""
    override fun getContentViewId(): Int {
        return R.layout.app_fragment_home_high
    }

    lateinit var headerView: View
    private fun initHeaderView() {
        headerView = View.inflate(activity, R.layout.app_item_high_header, null)
        initBannerView(headerView)
    }


    lateinit var bannerView: AdvertisementView
    private fun initBannerView(rootView: View) {
        bannerView = rootView.findViewById(R.id.ad_view_recommend) as AdvertisementView
    }

    override fun initView(rootView: View) {
        EventBus.getDefault().register(this)
        initHeaderView()
        val modle = SpKey.MODLE.getSpValue("0")
        ToSex = "filterSex".getSpValue("")
        layout = if (modle == "1") HomePageNearFragment.LayOutType.Grid else HomePageNearFragment.LayOutType.List

        mList = ArrayList()
        rootView.run {
            refreshLayout = findViewById(R.id.smartRefresh)
            recycleview = findViewById(R.id.recycleview)
            gLoadHolder = Gloading.getDefault().wrap(recycleview)

        }
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
        getBanner()
    }

    private fun getBanner() {
        HttpHelper.getInstance().getBanner(15, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                //bannerLayout.visibility = View.VISIBLE
                val tempData = GsonUtil.GsonToBean(data, BannerNewData::class.java)
                tempData?.data?.run {
                    bannerView.initData(tempData)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                LogUtil.d("banner path hide ")
            }
        })
    }


    fun getData() {
        HttpHelper.getInstance().getHighList(ToSex, this.page.toString(), "", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, HighBeanModel::class.java)
                if (model?.data == null && page == 0) {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    gLoadHolder.showEmpty()
                    return
                }
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (page == 0 && model?.data?.size == 0) {
                    gLoadHolder.showEmpty()
                } else {
                    gLoadHolder.showLoadSuccess()
                }
                model?.data?.run {
                    when (page) {
                        0 -> {
                            mList.clear()
                            mList.addAll(this)

                            with(recycleview) {
//                                layoutManager =  HeaderViewGridLayoutManager(activity,1,headerAdapter)
                                when (layout) {
                                    HomePageNearFragment.LayOutType.List -> {
                                        homeHighUserAdapter = HomeHighUserAdapter(context, mList)
                                        val headerAdapter = HeaderViewAdapter(homeHighUserAdapter)
                                        headerAdapter.addHeaderView(headerView)
                                        adapter = headerAdapter
                                        layoutManager = HeaderViewGridLayoutManager(activity, 1, headerAdapter)

                                        homeHighUserAdapter?.setOnSimpleItemListener(OnSimpleItemListener {
                                            startActivity(Intent(context, HighEndUserActivity::class.java).putExtra(IntentKey.UID, mList[it].top_id))
                                        })

                                    }
                                    HomePageNearFragment.LayOutType.Grid -> {
                                        homeHighUserGridAdapter = HomeHighUserGridAdapter(context, mList)
                                        val headerAdapter = HeaderViewAdapter(homeHighUserGridAdapter)
                                        headerAdapter.addHeaderView(headerView)
                                        adapter = headerAdapter
                                        layoutManager = HeaderViewGridLayoutManager(activity, 2, headerAdapter)

                                        homeHighUserGridAdapter?.setOnSimpleItemListener(OnSimpleItemListener {
                                            startActivity(Intent(context, HighEndUserActivity::class.java).putExtra(IntentKey.UID, mList[it].top_id))
                                        })
                                    }
                                }

                            }
                        }
                        else -> {

                            val tempIndex = mList.size
                            mList.addAll(this)
                            when (layout) {
                                HomePageNearFragment.LayOutType.List -> {
                                    homeHighUserAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                                }
                                HomePageNearFragment.LayOutType.Grid -> {
                                    homeHighUserGridAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                                }
                            }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: ChangeLayoutEvent) {
        layout = if (event.falg == 1) {
            HomePageNearFragment.LayOutType.Grid
        } else {
            HomePageNearFragment.LayOutType.List
        }
        page = 0
        getData()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: SharedprefrenceEvent) {
        //  当在@ 页面是 不做处理
        ToSex = event.sex
        page = 0
        getData()

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}