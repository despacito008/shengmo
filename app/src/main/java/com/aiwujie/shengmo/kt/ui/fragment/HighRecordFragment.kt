package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.BillData
import com.aiwujie.shengmo.bean.HighDetailModle
import com.aiwujie.shengmo.kt.ui.adapter.HighRecordAdapter
import com.aiwujie.shengmo.kt.ui.adapter.RechargeRecordAdapter
import com.aiwujie.shengmo.kt.ui.fragment.topic.TopicFragment
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 充值明细 - 充值记录
 */
class HighRecordFragment : LazyFragment() {

    lateinit var smartRefresh: SmartRefreshLayout
    lateinit var rvRefresh: RecyclerView
    lateinit var gLoadHolder: Gloading.Holder
    var page = 0
    private var mType = ""
    private var topId = ""

    companion object {
        fun newInstance(pid: String): HighRecordFragment {
            val args = Bundle()
            args.putString("type", pid)
            val fragment = HighRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun loadData() {
        recordList = ArrayList()
        gLoadHolder.showLoading()
        getRechargeRecord()
        initListener()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_high_record_detail
    }


    override fun initView(rootView: View) {
        mType = arguments.getString("type", "")
//        topId = arguments.getString("topId", "")
        smartRefresh = rootView.findViewById(R.id.smart_refresh_simple_list)
        rvRefresh = rootView.findViewById(R.id.rv_simple_list)
        gLoadHolder = Gloading.getDefault().wrap(rvRefresh)
    }

    fun initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getRechargeRecord()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getRechargeRecord()
            }

        })
    }

    var recordAdapter: HighRecordAdapter? = null
    lateinit var recordList: ArrayList<HighDetailModle.DataBean>
    private fun getRechargeRecord() {
        HttpHelper.getInstance().getHighDetail(mType, page,  object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, HighDetailModle::class.java)
                tempData?.data?.run {
                    smartRefresh.finishRefresh()
                    smartRefresh.finishLoadMore()

                    if (page == 0 && this.size == 0) {
                        gLoadHolder.showEmpty()
                    } else {
                        gLoadHolder.showLoadSuccess()
                    }
                    when (page) {
                        0 -> {
                            recordList.clear()
                            recordList.addAll(this)
                            recordAdapter = HighRecordAdapter(activity, recordList)
                            var manager = LinearLayoutManager(activity)
                            with(rvRefresh){
                                layoutManager = manager
                                adapter = recordAdapter
                            }

                        }
                        else -> {
                            val tempIndex = recordList.size
                            recordList.addAll(this)
                            recordAdapter?.notifyItemChanged(tempIndex, this.size)

                        }
                    }


                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(activity, msg)
                gLoadHolder.showEmpty()
            }

        })
    }

}