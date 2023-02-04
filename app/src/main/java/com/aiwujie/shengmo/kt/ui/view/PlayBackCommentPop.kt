package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.PlayBackCommentBean
import com.aiwujie.shengmo.kt.adapter.PlayBackCommentAdapter
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.NormalReportActivity
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.TextViewUtil
import com.aiwujie.shengmo.view.CommentDialogFragment
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: PlayBackCommentPop
 * @Author: xmf
 * @CreateDate: 2022/6/13 9:40
 * @Description: 回放评论列表
 */
class PlayBackCommentPop(context: Context, private val logId: String) : BasePopupWindow(context) {
    private val tvNum: TextView by lazy { findViewById<TextView>(R.id.tv_pop_num) }
    private val tvComment: TextView by lazy { findViewById<TextView>(R.id.tv_pop_comment) }
    private val refreshComment: SmartRefreshLayout by lazy { findViewById<SmartRefreshLayout>(R.id.refresh_pop_comment) }
    private val rvComment: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_pop_comment) }
    private var loadingHolder: Gloading.Holder

    init {
        loadingHolder = Gloading.getDefault().wrap(rvComment)
        loadingHolder.showLoading()
        getCommentList()
        setAdjustInputMethod(false)
        tvComment.clickDelay {
            showAddCommitPop()
        }
        with(refreshComment) {
            setOnRefreshListener {
                page = 0
                getCommentList()
            }
            setOnLoadMoreListener {
                page++
                getCommentList()
            }
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_play_back_comment)
    }

    var page = 0
    var totalCommentNum = 0
    private val commentList: ArrayList<PlayBackCommentBean.DataBean.CommentListBean> by lazy { ArrayList<PlayBackCommentBean.DataBean.CommentListBean>() }
    private var commentAdapter: PlayBackCommentAdapter? = null
    private fun getCommentList() {
        HttpHelper.getInstance().getPlayBackCommentList(logId, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshComment.finishRefresh()
                refreshComment.finishLoadMore()
                val tempData = GsonUtil.GsonToBean(data, PlayBackCommentBean::class.java)
                tempData?.data?.run {
                    totalCommentNum = comment_total.toInt()
                    refreshTotalNum()
                    when (page) {
                        0 -> {
                            if (comment_list.size == 0) {
                                loadingHolder.showEmpty()
                                return@run
                            }
                            commentList.clear()
                            commentList.addAll(comment_list)
                            commentAdapter = PlayBackCommentAdapter(context, commentList)
                            with(rvComment) {
                                adapter = commentAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                            commentAdapter?.commentListener = object : PlayBackCommentAdapter.OnPlayBackCommentListener {
                                override fun doCommentHeaderClick(index: Int) {
                                    UserInfoActivity.start(context, commentList[index].user.uid)
                                }

                                override fun doCommentLongClick(index: Int) {
                                    isSelfComment = MyApp.uid == commentList[index].user.uid
                                    cid = commentList[index].comment.comment_id
                                    cIndex = index
                                    showCommentMenu()
                                }
                            }
                        }
                        else -> {
                            if (comment_list.size == 0) {
                                return@run
                            }
                            val tempIndex = commentList.size
                            commentList.addAll(comment_list)
                            commentAdapter?.notifyItemRangeInserted(tempIndex, comment_list.size)
                        }
                    }
                    loadingHolder.showLoadSuccess()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshComment.finishRefresh()
                refreshComment.finishLoadMore()
                loadingHolder.showLoadSuccess()
            }
        })
    }

    private val commentDialogFragment: CommentDialogFragment by lazy { CommentDialogFragment() }
    private fun showAddCommitPop() {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        commentDialogFragment.show(fragmentManager, "comment")
        commentDialogFragment.setOnCommentSendListener(object : CommentDialogFragment.OnCommentSendListener {
            override fun onCommentSend(content: String?) {
                if (content.isNullOrEmpty()) {
                    "请输入评论内容".showToast()
                    return
                }
                addComment(content)
            }

            override fun onCommentAt() {
            }

        })
    }

    private fun refreshTotalNum() {
        val numStr = "共有 $totalCommentNum 条评论"
        TextViewUtil.setSpannedColorText(tvNum, numStr, 3, 3 + totalCommentNum.toString().length, Color.parseColor("#66ccff"))
    }

    private fun addComment(content: String) {
        HttpHelper.getInstance().addPlayBackComment(logId, content, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                refreshComment.autoRefresh(200)
                commentDialogFragment.clearText()
                commentDialogFragment.dismiss()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private var isSelfPlayBack = false
    private var isSelfComment = false
    private var cid = ""
    private var cIndex = 0
    fun checkAnchor(isSelf: Boolean) {
        isSelfPlayBack = isSelf
    }

    private fun showCommentMenu() {
        val menuItemList = ArrayList<NormalMenuItem>()
        if (isSelfComment) {
            menuItemList.add(NormalMenuItem(0, "删除"))
        } else if (isSelfPlayBack || MyApp.isAdmin == "1") {
            menuItemList.add(NormalMenuItem(0, "删除"))
            menuItemList.add(NormalMenuItem(0, "举报"))
        } else {
            menuItemList.add(NormalMenuItem(0, "举报"))
        }
        val menuItemPop = NormalMenuPopup(context,menuItemList)
        menuItemPop.setOnSimpleItemListener(OnSimpleItemListener {
            when(menuItemList[it].content) {
                "删除" -> {
                    //"删除".showToast()
                    showDeleteTipPop()
                    menuItemPop.dismiss()
                }
                "举报" -> {
                    //"举报".showToast()
                    NormalReportActivity.start(context,1,cid)
                }
            }
        })
        menuItemPop.showPopupWindow()
    }

    private fun showDeleteTipPop() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("确认删除吗?")
                .setPositiveButton("否") {
                    dialog, which -> dialog.dismiss()
                }
                .setNegativeButton("是") {
                    dialog, which ->
                    deleteComment()
                }.create().show()
    }

    private fun deleteComment() {
        HttpHelper.getInstance().deletePlayBackComment(cid,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                commentList.removeAt(cIndex)
                commentAdapter?.notifyItemRemoved(cIndex)
                commentAdapter?.notifyItemRangeChanged(cIndex, commentList.size - cIndex)
                totalCommentNum--
                refreshTotalNum()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
}
