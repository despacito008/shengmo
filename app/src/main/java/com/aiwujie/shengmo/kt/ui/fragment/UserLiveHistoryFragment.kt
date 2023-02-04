package com.aiwujie.shengmo.kt.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.VipCenterActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.LiveHistoryBean
import com.aiwujie.shengmo.bean.UserInfoBean
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity
import com.aiwujie.shengmo.kt.ui.adapter.LiveHistoryAdapter
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil


/**
 * 个人主页 - 直播历史
 */
class UserLiveHistoryFragment : LazyFragment() {
    private lateinit var rvHistory: RecyclerView
    private lateinit var scrollHistory: NestedScrollView
    private lateinit var btVip: TextView
    private lateinit var tvUserInfoAddFriend: TextView
    private lateinit var llEmpty: LinearLayout
    private lateinit var tvTips: TextView

    companion object {
        fun newInstance(aUid: String) = UserLiveHistoryFragment().apply {
            arguments = Bundle(2).apply {
                putString(IntentKey.UID, aUid)
            }
        }
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        EventBus.getDefault().register(this@UserLiveHistoryFragment)
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveHistoryList = ArrayList()
        view!!.let {
            rvHistory = it.findViewById(R.id.rv_fragment_user_live_history)
            btVip = it.findViewById(R.id.tv_user_info_buy_vip)
            tvUserInfoAddFriend = it.findViewById(R.id.tv_user_info_add_friend)
            scrollHistory = it.findViewById(R.id.scroll_user_info_live_history)
            llEmpty = it.findViewById(R.id.ll_live_history_empty)
            tvTips = it.findViewById(R.id.tv_live_history_tips)
        }
        initListener()
    }

    var uid = ""
    var page = 1
    var hasMore = true
    lateinit var liveHistoryList: ArrayList<LiveHistoryBean.DataBean.ListBean>
    var historyAdapter: LiveHistoryAdapter? = null
    var headAdapter: HeaderViewAdapter? = null
    override fun loadData() {
        // getLiveHistoryData()
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_user_live_history
    }

    override fun initView(rootView: View) {

    }

    var isLoading = false
    fun initListener() {
        rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                    if (hasMore && !isLoading) {
                        LogUtil.d("加载下一页")
                        page++
                        getLiveHistoryData()
                    }
                }
            }
        })

    }

    fun getLiveHistoryData() {
        isLoading = true
        uid = arguments.getString("uid").toString()
        HttpHelper.getInstance().getUserLiveHistory(uid, page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                isLoading = false
                val liveHistoryBean = GsonUtil.GsonToBean(data, LiveHistoryBean::class.java)
                activity?.let {
                    liveHistoryBean?.data?.run {
                        hasMore = has_more == "1"
                        when (page) {
                            1 -> {
                                liveHistoryList.clear()
                                liveHistoryList.addAll(this.list)
                                historyAdapter = LiveHistoryAdapter(activity, liveHistoryList,this@UserLiveHistoryFragment)
                                headAdapter = HeaderViewAdapter(historyAdapter)
                                headAdapter?.addHeaderView(getLiveHeadView(content))
                                with(rvHistory) {
                                    adapter = headAdapter
                                    layoutManager = LinearLayoutManager(activity)
                                }
                                historyAdapter?.onItemHistoryListener = object : LiveHistoryAdapter.OnItemHistoryListener {
                                    override fun doItemClick(id: String) {
                                        PlayBackVideoActivity.start(activity,id,uid)
                                    }
                                }
                            }
                            else -> {
                                val tempSize = liveHistoryList.size
                                liveHistoryList.addAll(this.list)
                                historyAdapter?.notifyItemRangeInserted(tempSize, this.list.size)
                            }
                        }
                        //tvTips.visibility = View.VISIBLE
                        //tvTips.text = it.content
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                isLoading = false
                activity?.let {
                    if (page != 0) {
                        ToastUtil.show(activity, msg)
                    }
                }
            }

        })
    }

    fun showData(userInfo: UserInfoBean.DataBean) {
        val vip = SharedPreferencesUtils.getParam(activity, "vip", "0") as String
        val svip = SharedPreferencesUtils.getParam(activity, "svip", "0") as String
//        if ("0" == userInfo.comment_num) {
//            llUserInfoComment.setVisibility(View.VISIBLE)
//            tvUserInfoCommentTitle.setText("")
//            tvUserInfoCommentInfo.setText("")
//            tvUserInfoCommentNum.setText(userInfo.comment_num)
//            clUserInfoCommentBtn.setVisibility(View.GONE)
//            return
//        }
        llEmpty.visibility = View.VISIBLE
        if ("0" == userInfo.live_rule) { //公开
//            llUserInfoComment.setVisibility(View.GONE)
//            tvUserInfoCommentTitle.setText("(所有人可见)")
//            tvUserInfoCommentNum.setText(userInfo.comment_num)
//            getCommentList()
            llEmpty.visibility = View.GONE
            getLiveHistoryData()
        } else { //仅好友 会员可见
//            tvUserInfoCommentTitle.setText("(好友/会员可见)")
//            tvUserInfoCommentNum.setText(userInfo.comment_num)
            if (MyApp.uid == userInfo.uid || "1" == vip || "1" == svip || 3 == userInfo.follow_state) {
                llEmpty.visibility = View.GONE
                getLiveHistoryData()
            } else {
                llEmpty.visibility = View.VISIBLE
//                val builder = SpannableStringBuilder("该用户已将参与的评论设为好友/会员可见")
//                val purSpan = ForegroundColorSpan(Color.parseColor("#db57f3"))
//                builder.setSpan(purSpan, 12, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                tvUserInfoCommentInfo.setText(builder)
                when (userInfo.follow_state) {
                    0 -> {
                        tvUserInfoAddFriend.text = "加好友"
                    }
                    1 -> {
                        tvUserInfoAddFriend.text = "已关注"
                    }
                    2 -> {
                        tvUserInfoAddFriend.text = "关注"
                    }
                    4 -> {
                        tvUserInfoAddFriend.text = "被关注"
                    }
                }
            }
            tvUserInfoAddFriend.setOnClickListener {
                when (userInfo.follow_state) {
                    0 -> {
                        (activity as UserInfoActivity).follow()
                    }
                    1 -> {
                        (activity as UserInfoActivity).overfollow()
                    }
                    2 -> {
                        (activity as UserInfoActivity).follow()
                    }
                    4 -> {
                        (activity as UserInfoActivity).follow()
                    }
                }
            }
            btVip.setOnClickListener {
                val headpic = SharedPreferencesUtils.getParam(activity, "headurl", "") as String
                val intent = Intent(activity, VipCenterActivity::class.java)
                intent.putExtra("uid", MyApp.uid)
                intent.putExtra("headpic", headpic)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("InflateParams")
    fun getLiveHeadView(tip: String): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.app_layout_live_history_detail, null)
        val tvTip: TextView = view.findViewById(R.id.tv_live_history_tips)
        tvTip.text = tip
        return view
    }

    fun refreshData() {
        page = 1
        getLiveHistoryData()
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//     fun onMessage(event: UserLiveHistoryRefreshBean?) {
//        refreshData()
//    }
}