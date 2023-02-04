package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.WithdrawLogBean
import com.aiwujie.shengmo.kt.ui.adapter.WithdrawLogAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.GsonUtil.*
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * 提现明细页面
 */
class WithdrawDetailActivity : BaseActivity() {
    private lateinit var smartRefresh: SmartRefreshLayout
    private lateinit var recyclerWithdraw: RecyclerView

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView

    private lateinit var llEmptyView:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_withdraw_list)
        StatusBarUtil.showLightStatusBar(this)
        smartRefresh = findViewById(R.id.smart_refresh_width_draw_list)
        recyclerWithdraw = findViewById(R.id.rv_width_draw_list)

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        llEmptyView = findViewById(R.id.layout_normal_empty)

        tvTitle.text = "提现明细"
        ivTitleRight.visibility = View.INVISIBLE
        llEmptyView.visibility = View.GONE
        smartRefresh.finishLoadMoreWithNoMoreData()
        smartRefresh.setOnRefreshListener {
            getWithdrawDetail()
        }
        ivTitleBack.setOnClickListener {
            finish()
        }
        getWithdrawDetail()
    }

    private fun getWithdrawDetail() {
        HttpHelper.getInstance().getWithdrawLog(object :HttpCodeListener {
            override fun onSuccess(data: String?) {
                LogUtil.d(data)
                smartRefresh.finishRefresh()
                val logBean = GsonToBean(data,WithdrawLogBean::class.java)
                val withdrawList = logBean.data
                withdrawList?.let {
                    if (it.size == 0) {
                        llEmptyView.visibility = View.VISIBLE
                    } else {
                        llEmptyView.visibility = View.GONE
                        recyclerWithdraw.apply {
                            adapter = WithdrawLogAdapter(this@WithdrawDetailActivity, it)
                            layoutManager = LinearLayoutManager(this@WithdrawDetailActivity)
                        }
                    }
                }

            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@WithdrawDetailActivity,msg)
            }

        })
    }
}