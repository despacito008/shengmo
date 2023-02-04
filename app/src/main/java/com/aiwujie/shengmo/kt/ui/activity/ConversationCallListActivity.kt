package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.CallUserBean
import com.aiwujie.shengmo.bean.ConversationCallStatusBean
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.adapter.ConversationCallListAdapter
import com.aiwujie.shengmo.kt.ui.view.ConversationCallBuyPop
import com.aiwujie.shengmo.kt.ui.view.ConversationCallBuyPop.OnBuyCallListener
import com.aiwujie.shengmo.kt.ui.view.ConversationCallUsePop
import com.aiwujie.shengmo.kt.ui.view.ConversationCallUsePop.OnCallPopListener
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.livedata.UnFlowLiveData
import com.aiwujie.shengmo.kt.util.saveSpValue
import com.aiwujie.shengmo.kt.util.showOrHideBottomAnim
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: ConversationCallListActivity
 * @Author: xmf
 * @CreateDate: 2022/5/25 15:23
 * @Description:
 */
class ConversationCallListActivity:BaseActivity() {
    private val tvTitle:TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val ivBack:ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val tvSortTime:TextView by lazy { findViewById<TextView>(R.id.tv_sort_smart) }
    private val tvSortDistance:TextView by lazy { findViewById<TextView>(R.id.tv_sort_distance) }
    private val refreshLayout:SmartRefreshLayout by lazy { findViewById<SmartRefreshLayout>(R.id.smart_refresh_call_list) }
    private val rvCallList:RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_call_list) }
    private val tvStartCall:TextView by lazy { findViewById<TextView>(R.id.tv_start_call) }
    private lateinit var loadingHolder:Gloading.Holder
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ConversationCallListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_coversation_call_list)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle.text = "此刻呼唤聊天"
        loadingHolder = Gloading.getDefault().wrap(rvCallList)
        sortType = SpKey.CALL_SORT.getSpValue("1")
        refreshSortTextViewState()
        loadingHolder.showLoading()
        getCallList()
        initListener()
    }

    var page = 0
    var sortType = "1"
    val callUserList:ArrayList<CallUserBean.DataBean> by lazy { ArrayList<CallUserBean.DataBean>() }
    var callUserAdapter:ConversationCallListAdapter? = null
    var itemDecoration:GridItemDecoration? = null

    private fun getCallList() {
        HttpHelper.getInstance().getCallList(page, sortType, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                val tempData = GsonUtil.GsonToBean(data, CallUserBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            if (this.size == 0) {
                                loadingHolder.showEmpty()
                            } else {
                                loadingHolder.showLoadSuccess()
                            }
                            callUserList.clear()
                            callUserList.addAll(this)
                            callUserAdapter = ConversationCallListAdapter(this@ConversationCallListActivity, callUserList)
                            with(rvCallList) {
                                adapter = callUserAdapter
                                layoutManager = GridLayoutManager(this@ConversationCallListActivity, 3)
                                if (itemDecoration == null) {
                                    itemDecoration = GridItemDecoration(10)
                                    addItemDecoration(itemDecoration)
                                }
                            }
                            callUserAdapter?.itemListener = OnSimpleItemListener {
                                UserInfoActivity.start(this@ConversationCallListActivity, callUserList[it].uid)
                            }
                        }
                        else -> {
                            val tempIndex = callUserList.size
                            callUserList.addAll(this)
                            callUserAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
            }
        })
    }

    private fun initListener() {
        ivBack.setOnClickListener { finish() }
        tvSortTime.setOnClickListener {
            if (sortType != "1") {
                sortType = "1"
                page = 0
                showOrHideButton(true)
                loadingHolder.showLoading()
                getCallList()
                refreshSortTextViewState()
                SpKey.CALL_SORT.saveSpValue(sortType)
            }
        }
        tvSortDistance.setOnClickListener {
            if (sortType != "2") {
                sortType = "2"
                page = 0
                showOrHideButton(false)
                loadingHolder.showLoading()
                getCallList()
                refreshSortTextViewState()
                SpKey.CALL_SORT.saveSpValue(sortType)
            }
        }
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                loadingHolder.showLoading()
                getCallList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getCallList()
            }
        })
        tvStartCall.setOnClickListener {
            startCall()
        }
    }

    private fun refreshSortTextViewState() {
        if (sortType == "1") {
            tvSortTime.setTextColor(ContextCompat.getColor(this, R.color.titleBlack))
            tvSortDistance.setTextColor(ContextCompat.getColor(this, R.color.lightGray))
        } else {
            tvSortTime.setTextColor(ContextCompat.getColor(this, R.color.lightGray))
            tvSortDistance.setTextColor(ContextCompat.getColor(this, R.color.titleBlack))
        }
    }

    private fun startCall() {
        getCallStatus()
    }

    private fun getCallStatus(needToast:Boolean = true) {
        HttpHelper.getInstance().getConversationCallStatus(object : HttpCodeMsgListener {
            override fun onSuccess(data: String, msg: String) {
                val conversationCallStatusBean = GsonUtil.GsonToBean(data, ConversationCallStatusBean::class.java)
                conversationCallStatusBean?.data?.run {
                    if (status != "0") {
                        if (needToast) {
                            ToastUtil.show(conversationCallStatusBean.data.toast_tip)
                        }
                    } else {
                        if ("0" != call_times) {
                            showUseCallPop()
                        } else {
                            showBuyCallPop()
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    fun showBuyCallPop() {
        val callBuyPop = ConversationCallBuyPop(this)
        callBuyPop.showPopupWindow()
        callBuyPop.onBuyCallListener = object : OnBuyCallListener {
            override fun doBuyCallSuc() {
                showUseCallPop()
            }
        }
    }

    fun showUseCallPop() {
        val useCallPop = ConversationCallUsePop(this)
        useCallPop.showPopupWindow()
        useCallPop.onCallPopListener = object : OnCallPopListener {
            override fun doUseCall() {
                getCallList()
            }
        }
    }

    private fun showOrHideButton(isShow: Boolean) {
       // tvStartCall.showOrHideBottomAnim(isShow)
    }
}
