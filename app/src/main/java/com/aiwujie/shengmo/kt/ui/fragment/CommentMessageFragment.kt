package com.aiwujie.shengmo.kt.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.AllRewardActivity
import com.aiwujie.shengmo.activity.DynamicDetailActivity
import com.aiwujie.shengmo.activity.OtherReasonActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.AllCommentData
import com.aiwujie.shengmo.bean.UnreadMessageData
import com.aiwujie.shengmo.fragment.message.HomeMessageFragment
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.adapter.UnReadCommentAdapter
import com.aiwujie.shengmo.kt.ui.activity.normallist.AtUsActivity
import com.aiwujie.shengmo.kt.ui.activity.normallist.PushTopUsActivity
import com.aiwujie.shengmo.kt.ui.activity.normallist.RewardUsActivity
import com.aiwujie.shengmo.kt.ui.activity.normallist.ThumbUpUsActivity
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageMessageFragment
import com.aiwujie.shengmo.kt.util.alphaBackground
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.*
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.SafeCheckUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.OperateCommentPopup
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class CommentMessageFragment : NormalListFragment() {
    override fun loadData() {
        gLoadHolder.showLoading()
        getUnreadCount()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        commentList = ArrayList()
        frameLayout = rootView.findViewById(R.id.fl_normal_list)
        initTopLine()
        initHeadView()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getUnreadCount()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getUnReadMessage()
            }
        })
    }

    lateinit var headerView: View
    lateinit var thumbView: View
    lateinit var rewardView: View
    lateinit var pushTopView: View
    lateinit var atView: View
    lateinit var tvThumb: TextView
    lateinit var tvReward: TextView
    lateinit var tvPushTop: TextView
    lateinit var tvAtView: TextView
    lateinit var tvNewView: TextView
    private fun initHeadView() {
        headerView = View.inflate(activity, R.layout.item_dynamic_messageheadview, null)
        thumbView = headerView.findViewById(R.id.view_message_thumb_bg)
        rewardView = headerView.findViewById(R.id.view_message_reword_bg)
        pushTopView = headerView.findViewById(R.id.view_message_recommend_bg)
        atView = headerView.findViewById(R.id.view_message_alite_bg)
        tvThumb = headerView.findViewById(R.id.mDynamic_message_zancount)
        tvReward = headerView.findViewById(R.id.mDynamic_message_dscount)
        tvPushTop = headerView.findViewById(R.id.mDynamic_tuiding_atcount)
        tvAtView = headerView.findViewById(R.id.mDynamic_message_atcount)
        tvNewView = headerView.findViewById(R.id.mDynamic_message_tvHeadview2)
        thumbView.setOnClickListener {
            startActivity(Intent(activity, ThumbUpUsActivity::class.java))
            laudCount = 0
            tvThumb.visibility = View.GONE
            changeUnreadNoticeNum()
        }
        rewardView.setOnClickListener {
            startActivity(Intent(activity, RewardUsActivity::class.java))
            rewardCount = 0
            tvReward.visibility = View.GONE
            changeUnreadNoticeNum()
        }
        pushTopView.setOnClickListener {
            startActivity(Intent(activity, PushTopUsActivity::class.java))
            topCount = 0
            tvPushTop.visibility = View.GONE
            changeUnreadNoticeNum()
        }
        atView.setOnClickListener {
            startActivity(Intent(activity, AtUsActivity::class.java))
            atCount = 0
            tvAtView.visibility = View.GONE
            changeUnreadNoticeNum()
        }
    }

    var page = 0
    lateinit var commentList: ArrayList<AllCommentData.DataBean>
    var commentAdapter: UnReadCommentAdapter? = null
    private fun getUnReadMessage() {
        HttpHelper.getInstance().getUnreadComment(page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, AllCommentData::class.java)
                gLoadHolder.showLoadSuccess()
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            commentList.clear()
                            commentList.addAll(this)
                            commentAdapter = UnReadCommentAdapter(activity, this@CommentMessageFragment, commentList, newCommentNum)
                            val headerAdapter = HeaderViewAdapter(commentAdapter)
                            headerAdapter.addHeaderView(headerView)
                            with(rvSearchResult) {
                                adapter = headerAdapter
                                layoutManager = HeaderViewGridLayoutManager(activity, 1, headerAdapter)
                            }
                            commentAdapter?.onCommentListener = object : UnReadCommentAdapter.OnCommentListener {
                                override fun doCommentClick(index: Int) {
                                    if (commentList.size <= index) {
                                        return
                                    }
                                    val intent = Intent(activity, DynamicDetailActivity::class.java)
                                    intent.putExtra("uid", commentList[index].duid)
                                    intent.putExtra("did", commentList[index].did)
                                    intent.putExtra("pos", index)
                                    intent.putExtra("showwhat", 1)
                                    clearCommentNum()
                                    startActivity(intent)
                                }

                                override fun doCommentApply(index: Int) {
                                    commentList[index].let {
                                        cmid = it.cmid
                                        did = it.did
                                        ouid = it.uid
                                        applyname = it.nickname
                                        showCommentApplyPop()
                                    }

                                }

                                override fun doCommentLongClick(index: Int) {
                                    showCommentMenu(index)
                                }
                            }
                        }
                        else -> {
                            val tempIndex = commentList.size
                            commentList.addAll(this)
                            commentAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                gLoadHolder.showLoadSuccess()
                if (page == 0) {
                    commentList.clear()
                    commentAdapter = UnReadCommentAdapter(activity, this@CommentMessageFragment, commentList, 0)
                    val headerAdapter = HeaderViewAdapter(commentAdapter)
                    headerAdapter.addHeaderView(headerView)
                    with(rvSearchResult) {
                        adapter = headerAdapter
                        layoutManager = HeaderViewGridLayoutManager(activity, 1, headerAdapter)
                    }
                }
            }
        })
    }

    private var laudCount = 0
    private var rewardCount = 0
    private var topCount = 0
    private var atCount = 0
    private var newCommentNum = 0
    private fun getUnreadCount() {
        HttpHelper.getInstance().getUnReadMessage(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val unreadMessageData = Gson().fromJson(data, UnreadMessageData::class.java)
                unreadMessageData?.data?.run {
                    tvThumb.text = laudnum
                    tvReward.text = rewardnum
                    tvPushTop.text = topnum
                    tvAtView.text = atnum
                    laudCount = laudnum.toInt()
                    rewardCount = rewardnum.toInt()
                    topCount = topnum.toInt()
                    atCount = atnum.toInt()
                    tvThumb.visibility = if (laudnum == "0") View.GONE else View.VISIBLE
                    tvReward.visibility = if (rewardnum == "0") View.GONE else View.VISIBLE
                    tvPushTop.visibility = if (topnum == "0") View.GONE else View.VISIBLE
                    tvAtView.visibility = if (atnum == "0") View.GONE else View.VISIBLE
                    tvNewView.text = "刚刚有 $comnum 条评论"
                    newCommentNum = comnum.toInt()
                    getUnReadMessage()
                    changeUnreadNoticeNum()
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }


    lateinit var applyView: View
    var applyPop: PopupWindow? = null
    private lateinit var mInputManager: InputMethodManager
    var comment = ""
    var did = ""
    var cmid = ""
    var ouid = ""
    var applyname = ""
    lateinit var btApply: TextView

    @SuppressLint("ClickableViewAccessibility", "WrongConstant")
    private fun showCommentApplyPop() {
        applyView = LayoutInflater.from(activity).inflate(R.layout.comment_popupwindow, null)
        val etComment: EditText = applyView.findViewById(R.id.et_discuss)
        btApply = applyView.findViewById(R.id.btn_confirm)
        val rlContainer: View = applyView.findViewById(R.id.rl_input_container)
        etComment.postDelayed({
            etComment.isFocusable = true
            etComment.isFocusableInTouchMode = true
            etComment.requestFocus()
            mInputManager = activity.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputManager.showSoftInput(etComment, 0)
            if (comment.isNotEmpty()) etComment.setText(comment)
            if (applyname.isNotEmpty()) etComment.hint = "回复 $applyname"
        }, 200)
        applyPop = PopupWindow(applyView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false)
        with(applyPop!!) {
            isTouchable = true
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(0x000000))
            setTouchInterceptor { _, event ->
                if (event.action == MotionEvent.ACTION_OUTSIDE) dismiss()
                false
            }
            //softInputMode = PopupWindow.INPUT_METHOD_NEEDED
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            showAtLocation(applyView, Gravity.BOTTOM, 0, 0)
            update()
            setOnDismissListener {
                if (mInputManager.isActive) {
                    applyPop = null
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }
        rlContainer.setOnClickListener {
            if (mInputManager.isActive) {
                mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }
            applyPop?.dismiss()
        }
        btApply.setOnClickListener {
            comment = etComment.text.toString()
            if (comment.isNotEmpty()) {
                btApply.isEnabled = false
                sendComment()
                if (mInputManager.isActive) {
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                }
                applyPop?.dismiss()
            }
        }
    }

    private fun sendComment() {
        HttpHelper.getInstance().sendChildComment(did, cmid, comment, ouid, object : HttpListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(activity, "回复成功")
                btApply.visibility = View.VISIBLE
                comment = ""
            }

            override fun onFail(msg: String?) {
                ToastUtil.show(activity, msg)
            }
        })
    }

    fun showMenu() {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.item_notice_pop, null)
        val mPopWindow = PopupWindow(contentView)
        with(mPopWindow) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            setOnDismissListener { activity.alphaBackground(1f) }
        }
        activity.alphaBackground(0.5f)
        val ll01 = contentView.findViewById<View>(R.id.item_notice_pop_ll01) as LinearLayout
        ll01.setOnClickListener {
            mPopWindow.dismiss()
            clearUnreadNum()
        }
        mPopWindow.showAsDropDown(topBtn)
    }

    private fun showCommentMenu(index: Int) {
        commentList[index].let {
            val content = it.ccontent
            val operatePop = OperateCommentPopup(activity, 0)
            operatePop.setOnCommentOperateListener(object : OperateCommentPopup.OnCommentOperateListener {
                override fun onCommentCopy() {
                    //获取剪贴板管理器：
                    val cm = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", content)
                    // 将ClipData内容放到系统剪贴板里。
                    cm.primaryClip = mClipData
                    ToastUtil.show(activity, "已复制到剪贴板")
                }

                override fun onCommentReport() {
                    val intent = Intent(activity, OtherReasonActivity::class.java)
                    intent.putExtra("uid", MyApp.uid)
                    intent.putExtra("did", it.did)
                    intent.putExtra("cmid", it.cmid)
                    startActivity(intent)
                    operatePop.dismiss()
                }

                override fun onCommentDelete() {
                    operatePop.dismiss()
                    AlertDialog.Builder(activity).apply {
                        setMessage("确认删除吗?")
                        setPositiveButton("否") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setNegativeButton("是") { dialog, _ ->
                            deleteComment(it)
                            dialog.dismiss()
                        }
                        show()
                    }
                }
            })
            operatePop.showPopupWindow()
        }
    }

    private fun clearUnreadNum() {
        val map: MutableMap<String, String> = HashMap()
        map["uid"] = MyApp.uid
        val manager = RequestFactory.getRequestManager()
        manager.post(HttpUrl.ClearUnreadNumAll, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
                if (SafeCheckUtil.isActivityFinish(activity)) {
                    return
                }
                tvAtView.visibility = View.GONE
                tvReward.visibility = View.GONE
                tvPushTop.visibility = View.GONE
                tvThumb.visibility = View.GONE
                changeUnreadNoticeNum(0)
            }

            override fun onFailure(throwable: Throwable) {}
        })
    }

    private fun changeUnreadNoticeNum() {
        changeUnreadNoticeNum( rewardCount + topCount + atCount + newCommentNum)
    }

    fun changeUnreadNoticeNum(num: Int) {
        //(parentFragment as HomeMessageFragment).refreshNoticeMessage(num)
        (parentFragment as HomePageMessageFragment).showCommentRedDot(num)
    }

    private fun deleteComment(comment: AllCommentData.DataBean) {
        HttpHelper.getInstance().deleteComment(comment.did, comment.cmid, object : HttpListener {
            override fun onSuccess(data: String) {
                ToastUtil.show(activity, "删除成功")
                page = 0
                getUnReadMessage()
            }

            override fun onFail(msg: String) {
                ToastUtil.show(activity, msg)
            }
        })
    }

    lateinit var topBtn: View
    lateinit var frameLayout: FrameLayout
    private fun initTopLine() {
        topBtn = ImageButton(activity)
        val params = FrameLayout.LayoutParams(300.dp, 1.dp)
        params.gravity = Gravity.TOP or Gravity.END
        topBtn.layoutParams = params
        frameLayout.addView(topBtn)
    }

    //清除评论消息数量
    private fun clearCommentNum() {
        newCommentNum = 0
        tvNewView.text = "刚刚有 $newCommentNum 条评论"
        changeUnreadNoticeNum()
        changeUnreadNoticeNum()
    }

}