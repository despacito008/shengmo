package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.LiveSealBean
import com.aiwujie.shengmo.kt.ui.adapter.LiveSealAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 直播处罚记录
 */
class LiveSealDetailActivity : BaseActivity() {
    lateinit var smartRefresh: SmartRefreshLayout
    lateinit var rvLiveSeal: RecyclerView

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    lateinit var uid: String
    lateinit var type: String
    var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_live_seal_detail)
        StatusBarUtil.showLightStatusBar(this)
        uid = intent.getStringExtra("uid")!!
        type = intent.getStringExtra("type")!!
        sealList = ArrayList()
        initView()
        initListener()
        getSealData()
    }

    fun initView() {
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        tvTitle.text = "直播处罚记录"
        ivTitleRight.visibility = View.INVISIBLE
        ivTitleBack.setOnClickListener {
            finish()
        }


        smartRefresh = findViewById(R.id.smart_refresh_live_seal)
        rvLiveSeal = findViewById(R.id.rv_live_seal)

    }

    fun initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getSealData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getSealData()
            }

        })
    }

    lateinit var sealList: ArrayList<LiveSealBean.DataBean.ListBean>
    var sealAdapter: LiveSealAdapter? = null
    fun getSealData() {
        HttpHelper.getInstance().getLiveSeal(uid, type, page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val tempData = GsonUtil.GsonToBean(data, LiveSealBean::class.java)
                tempData?.data?.let {
                    smartRefresh.finishRefresh()
                    if (it.has_more == "1") {
                        smartRefresh.finishLoadMore()
                        smartRefresh.setNoMoreData(false)
                    } else {
                        smartRefresh.finishLoadMoreWithNoMoreData()
                    }
                    when (page) {
                        1 -> {
                            sealList.clear()
                            sealList.addAll(it.list)
                            sealAdapter = LiveSealAdapter(this@LiveSealDetailActivity,sealList)
                            rvLiveSeal.adapter = sealAdapter
                            rvLiveSeal.layoutManager = LinearLayoutManager(this@LiveSealDetailActivity)
                        }
                        else -> {
                            val temp = sealList.size
                            sealList.addAll(it.list)
                            sealAdapter?.notifyItemRangeInserted(temp,it.list.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                smartRefresh.finishRefresh()
                smartRefresh.finishLoadMoreWithNoMoreData()
                ToastUtil.show(this@LiveSealDetailActivity, msg)
            }

        })
    }
}