package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.activity.EditPunishmentActivity
import com.aiwujie.shengmo.activity.newui.PunishmentDetailActivity
import com.aiwujie.shengmo.bean.PunishmentBean
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.PunishmentEditActivity
import com.aiwujie.shengmo.kt.ui.adapter.PunishmentAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.aiwujie.shengmo.kt.ui.adapter.PunishmentAdapter.OnPunishmentListener as OnPunishmentListener1

class PunishmentFragment : NormalListFragment() {


    companion object {
        fun newInstance(): PunishmentFragment {
            val fragment = PunishmentFragment()
            return fragment
        }
    }

    lateinit var mList: ArrayList<PunishmentBean.DataBean>
    lateinit var punishmentAdapter: PunishmentAdapter
    var page = 0


    override fun initView(rootView: View) {
        super.initView(rootView)
        mList = ArrayList()
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
        HttpHelper.getInstance().getPunishment(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showLoadComplete()
                var model = GsonUtil.GsonToBean(data, PunishmentBean::class.java)
                model?.data?.run {
                    when (page) {
                        0 -> {
                            mList.clear()
                            mList.addAll(this)
                            punishmentAdapter = PunishmentAdapter(context,mList, SharedPreferencesUtils.getParam(context, "admin", "0") == "1")
                            with(rvSearchResult){
                                adapter =punishmentAdapter
                                layoutManager =LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            }
                            punishmentAdapter.onPunishmentListener =object :OnPunishmentListener1{
                                override fun onItemEdit(position: Int) {
                                    //val intent = Intent(activity, EditPunishmentActivity::class.java)
                                    val intent = Intent(activity, PunishmentEditActivity::class.java)
                                    intent.putExtra("info", mList[position])
                                    startActivity(intent)
                                }


                                override fun onItemClick(position: Int) {

                                    val intent = Intent(activity, PunishmentDetailActivity::class.java)
                                    intent.putExtra("punish_data", GsonUtil.GsonString(mList[position]))
                                    startActivity(intent)
                                }
                            }
                        }
                        else -> {
                            val tempSize = mList.size
                            mList.addAll(this)
                            punishmentAdapter?.notifyItemRangeInserted(tempSize,this.size)
                        }

                    }


                }

            }

            override fun onFail(code: Int, msg: String?) {
             showLoadError(code!= 3000,page==0,msg)
            }
        })
    }

    override fun loadData() {
        gLoadHolder.showLoading()
        getData()
    }

}