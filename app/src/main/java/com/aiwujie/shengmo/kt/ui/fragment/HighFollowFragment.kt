package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.SearchfriendsActivity
import com.aiwujie.shengmo.bean.BannerNewData
import com.aiwujie.shengmo.bean.HighBeanModel
import com.aiwujie.shengmo.bean.HighUserBean
import com.aiwujie.shengmo.kt.adapter.HomeHighUserAdapter
import com.aiwujie.shengmo.kt.adapter.HomeHighUserFollowAdapter
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity
import com.aiwujie.shengmo.kt.util.IntentKey
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


/**
 * @program: newshengmo
 * @description: 高端-关注列表
 * @author: whl
 * @create: 2022-06-14 16:32
 **/
class HighFollowFragment : LazyFragment() {
    companion object {
        var fragment: HighFollowFragment? = null
        fun newInstance(): HighFollowFragment {
            if (fragment == null) {
                fragment = HighFollowFragment()
            }
            return fragment as HighFollowFragment
        }

    }

    private var page = 0
    private var keyWords = ""
    lateinit var mList: ArrayList<HighUserBean>
    private var homeHighUserAdapter: HomeHighUserFollowAdapter? = null
    lateinit var gLoadHolder: Gloading.Holder
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var recycleview: RecyclerView
    lateinit var editText: EditText
    lateinit var llEtContent: LinearLayout
    override fun getContentViewId(): Int {
        return R.layout.app_fragment_home_high
    }


    override fun initView(rootView: View) {

        mList = ArrayList()
        rootView.run {
            refreshLayout = findViewById(R.id.smartRefresh)
            recycleview = findViewById(R.id.recycleview)
            gLoadHolder = Gloading.getDefault().wrap(recycleview)
            llEtContent = findViewById(R.id.ll_etContent)
            editText = findViewById(R.id.mGroupSearchKeyWord_et_sou)
            llEtContent.visibility = View.VISIBLE
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

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                keyWords = editText.text.toString()
                getData()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    override fun loadData() {
        gLoadHolder.showLoading()
        getData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    fun getData() {
        HttpHelper.getInstance().highFollowList(page.toString(), keyWords, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, HighBeanModel::class.java)
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()

                model?.data?.run {
                    when (page) {
                        0 -> {
                            if (page == 0 && model.data?.size == 0) {
                                gLoadHolder.showEmpty()
                            } else {
                                gLoadHolder.showLoadSuccess()
                            }
                            mList.clear()
                            mList.addAll(this)

                            with(recycleview) {

                                homeHighUserAdapter = HomeHighUserFollowAdapter(context, mList)

                                adapter = homeHighUserAdapter
                                layoutManager = LinearLayoutManager(activity)

                                homeHighUserAdapter?.setOnSimpleItemListener(OnSimpleItemListener {
                                    startActivity(Intent(context, HighEndUserActivity::class.java).putExtra(IntentKey.UID, mList[it].top_id))
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


}