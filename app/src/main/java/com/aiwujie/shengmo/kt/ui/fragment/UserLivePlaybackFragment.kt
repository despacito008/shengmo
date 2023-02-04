package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.transition.Fade
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.bean.LiveHistoryBean
import com.aiwujie.shengmo.bean.UserLivePlaybackListBean
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity
import com.aiwujie.shengmo.kt.ui.adapter.LiveHistoryAdapter
import com.aiwujie.shengmo.kt.ui.adapter.LivePlaybackAdapter
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.facebook.drawee.drawable.FadeDrawable
import kotlin.math.acos

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment
 * @ClassName: UserLivePlaybackFragment
 * @Author: xmf
 * @CreateDate: 2022/5/24 15:48
 * @Description:
 */
class UserLivePlaybackFragment: LazyFragment() {

    companion object {
        fun newInstance(aUid: String,aIsRecord:String) = UserLivePlaybackFragment().apply {
            arguments = Bundle(2).apply {
                putString(IntentKey.UID, aUid)
                putString("is_record",aIsRecord)
            }
        }
    }

    override fun loadData() {
        getLivePlayBackData()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_user_live_play_back
    }

    lateinit var rvHistory:RecyclerView
    lateinit var nestedScrollPlayback:NestedScrollView
    lateinit var llHistoryEmpty: LinearLayout
    lateinit var tvApply:TextView
    override fun initView(rootView: View) {
        rvHistory = rootView.findViewById(R.id.rv_fragment_user_live_play_back)
        nestedScrollPlayback = rootView.findViewById(R.id.nested_scroll_user_play_back)
        llHistoryEmpty = rootView.findViewById(R.id.ll_user_play_back_empty)
        initListener()
        val isRecord = arguments.getString("is_record","")
        if (isRecord == "0") {
            llHistoryEmpty.visibility = View.VISIBLE
        } else {
            llHistoryEmpty.visibility = View.GONE
        }
        tvApply = rootView.findViewById(R.id.tv_apply_recommend_anchor)
        tvApply.setOnClickListener {
            BannerWebActivity.start(context, "http://api.aiwujie.com.cn:8082/admin/smpay/verifyanchor/aid/11", "加点主播")
        }
    }

    fun initListener() {
        rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                    if (hasMore && !isLoading) {
                        LogUtil.d("加载下一页")
                        page++
                        getLivePlayBackData()
                    }
                }
            }
        })

    }
    var hasMore = true
    var isLoading = false
    var page = 1
    var uid = ""
    val liveHistoryList: ArrayList<UserLivePlaybackListBean.DataBean> by lazy { ArrayList<UserLivePlaybackListBean.DataBean>() }
    var historyAdapter: LivePlaybackAdapter? = null
    fun getLivePlayBackData() {
        isLoading = true
        uid = arguments.getString("uid").toString()
        HttpHelper.getInstance().getAnchorRecordList(uid,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                isLoading = false
                val tempData = GsonUtil.GsonToBean(data,UserLivePlaybackListBean::class.java)
                tempData?.data?.run {
                    when(page) {
                        1 -> {
                            if (this.size == 0) {
                                nestedScrollPlayback.visibility == View.VISIBLE
                                hasMore = false
                                return@run
                            } else {
                                nestedScrollPlayback.visibility = View.GONE
                                hasMore = true
                            }
                            liveHistoryList.clear()
                            liveHistoryList.addAll(this)
                            historyAdapter = LivePlaybackAdapter(activity,liveHistoryList)
                            with(rvHistory) {
                                adapter = historyAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            historyAdapter?.onItemHistoryListener = object :LiveHistoryAdapter.OnItemHistoryListener, LivePlaybackAdapter.OnItemHistoryListener {
                                override fun doItemClick(id: String) {
                                    PlayBackVideoActivity.start(activity,id,uid)
                                }
                            }
                        }
                        else -> {
                            if (this.size == 0) {
                                hasMore = false
                                return@run
                            } else {
                                hasMore = true
                            }
                            val tempIndex = liveHistoryList.size
                            liveHistoryList.addAll(this)
                            historyAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                isLoading = false
                msg?.showToast()
                if (page == 1) {
                    nestedScrollPlayback.visibility = View.VISIBLE
                }
            }
        })
    }
}
