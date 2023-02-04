package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.GroupMemberListAdapter
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.MemberData
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.NormalEditPop
import com.aiwujie.shengmo.view.gloading.Gloading
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class GroupMemberActivity : BaseActivity() {

    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var rvMember: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var tvSearch: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var memberList: ArrayList<MemberData.DataBean>
    private var memberAdapter: GroupMemberListAdapter? = null
    private var gid = ""
    private var state = ""  //自己身份 1-普通成员 2-管理员 3-群主
    private var memberFlag = "" //页面类型 1-群组成员 2-设置管理员

    companion object {
        private const val HOUR_SIX = "6小时"
        private const val HOUR_TWELVE = "12小时"
        private const val DAY_ONE = "1天"
        private const val DAY_THREE = "3天"
        private const val WEEK_ONE = "1周"
        fun start(context:Context,gid:String,state:String,flag:String) {
            val intent = Intent(context,GroupMemberActivity::class.java)
            intent.run {
                putExtra(IntentKey.GROUP_ID,gid)
                putExtra(IntentKey.STATE,state)
                putExtra(IntentKey.FLAG,flag)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_group_member)
        StatusBarUtil.showLightStatusBar(this)
        gid = intent.getStringExtra(IntentKey.GROUP_ID) ?: ""
        state = intent.getStringExtra(IntentKey.STATE) ?: ""
        memberFlag = intent.getStringExtra(IntentKey.FLAG) ?: ""
        memberList = ArrayList()
        initView()
        getSearchData()
    }

    lateinit var loadingHolder: Gloading.Holder
    fun initView() {
        refreshLayout = findViewById(R.id.refresh_group_member)
        rvMember = findViewById(R.id.rv_group_member)
        etSearch = findViewById(R.id.et_layout_normal_search_text)
        tvSearch = findViewById(R.id.tv_layout_normal_search_search)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        loadingHolder = Gloading.getDefault().wrap(rvMember)
        tvTitle.text = if (memberFlag == "2") "设置管理员" else "群组成员"
        ivTitleBack.setOnClickListener { finish() }

        etSearch.addTextChangedListenerDsl {
            afterTextChanged {
                keyWord = it.toString()
                page = 0
                getSearchData()
            }
        }

        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getSearchData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getSearchData()
            }
        })

    }

    var page = 0
    var keyWord = ""
    private fun getSearchData() {
        if (page == 0) {
            loadingHolder.showLoading()
        }
        HttpHelper.getInstance().getGroupMemberList(gid, keyWord, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, MemberData::class.java)
                tempData?.data?.run {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    loadingHolder.showLoadSuccess()
                    when (page) {
                        0 -> {
                            memberList.clear()
                            memberList.addAll(this)
                            memberAdapter = GroupMemberListAdapter(this@GroupMemberActivity, memberList, tempData.retcode)
                            with(rvMember) {
                                adapter = memberAdapter
                                layoutManager = LinearLayoutManager(this@GroupMemberActivity)
                            }
                            memberAdapter?.setOnGroupMemberListener(object : GroupMemberListAdapter.OnGroupMemberListener {
                                override fun doItemClick(index: Int) {
                                    gotoUserPage(memberList[index].uid)
                                }

                                override fun doItemLongClick(index: Int) {
                                    when (memberFlag) {
                                        "2" -> {
                                            if (state == "3") {
                                                showManageOperatePop(index)
                                            }
                                        }
                                        else -> {
                                            if (state.toInt() > memberList[index].state.toInt()) {
                                                showGroupOperatePop(index)
                                            }
                                        }
                                    }
                                }
                            })
                        }
                        else -> {
                            val tempIndex = memberList.size
                            memberList.addAll(this)
                            memberAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001 && page == 0) {
                    loadingHolder.showEmpty()
                } else {
                    loadingHolder.showLoadSuccess()
                }
            }
        })
    }

    fun gotoUserPage(uid: String) {
        UserInfoActivity.start(this,uid)
    }

    var chooseIndex = 0
    fun showGroupOperatePop(index: Int) {
        chooseIndex = index
        val member = memberList[index]
        val muteTip = if (member.gagstate == "0") "禁言该成员" else "解除禁言"
        val itemListener = OnItemClickListener { _, _, tip ->
            when (tip) {
                "设置群名片" -> {
                    showSetGroupCardView()
                }
                "禁言该成员" -> {
                    showMuteDayPop()
                }
                "解除禁言" -> {
                    cancelMuteGroupMember()
                }
                "删除该成员" -> {
                    showKickOutTip()
                }
            }
        }
        AlertView(null, null, "取消", null, arrayOf("设置群名片", muteTip, "删除该成员"), this, AlertView.Style.ActionSheet, itemListener).show()
    }

    fun showManageOperatePop(index: Int) {
        chooseIndex = index
        val member = memberList[chooseIndex]
        val managerTip = if (member.state == "2") "取消管理员" else "设置管理员"
        val itemListener = OnItemClickListener { _, _, tip ->
            when (tip) {
                "设置管理员" -> {
                    setGroupManager()
                }
                "取消管理员" -> {
                    cancelGroupManager()
                }
            }
        }
        AlertView(null, null, "取消", null, arrayOf(managerTip), this, AlertView.Style.ActionSheet, itemListener).show()
    }

    private fun showSetGroupCardView() {
        val normalEditPop = NormalEditPop.Builder(this).setTitle("设置群名片").setInfo(memberList[chooseIndex].cardname).build()
        normalEditPop.showPopupWindow()
        normalEditPop.setOnPopClickListener(object : NormalEditPop.OnPopClickListener {
            override fun confirmClick(edit: String?) {
                if (edit!!.length > 10) {
                    "群名片限十字以内！".showToast()
                } else {
                    normalEditPop.dismiss()
                    editGroupNickName(memberList[chooseIndex].uid, edit)
                }
            }

            override fun cancelClick() {
                normalEditPop.dismiss()
            }
        })
    }

    private fun editGroupNickName(uid: String, name: String) {
        HttpHelper.getInstance().editGroupUserNickName(gid, uid, name, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                memberList[chooseIndex].cardname = name
                memberAdapter?.notifyItemChanged(chooseIndex)
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    private fun showMuteDayPop() {
        val itemListener = OnItemClickListener { _, _, tip ->
            when (tip) {
                HOUR_SIX -> {
                    muteGroupMember(6 * 3600)
                }
                HOUR_TWELVE -> {
                    muteGroupMember(12 * 3600)
                }
                DAY_ONE -> {
                    muteGroupMember(24 * 3600)
                }
                DAY_THREE -> {
                    muteGroupMember(3 * 24 * 3600)
                }
                WEEK_ONE -> {
                    muteGroupMember(7 * 24 * 3600)
                }
            }
        }
        AlertView(null, null, "取消", null, arrayOf(HOUR_SIX, HOUR_TWELVE, DAY_ONE, DAY_THREE,WEEK_ONE), this, AlertView.Style.ActionSheet, itemListener).show()
    }

    private fun muteGroupMember(time: Long) {
        HttpHelper.getInstance().muteGroupUser(memberList[chooseIndex].ugid, time, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, "禁言成功")
                memberList[chooseIndex].gagstate = "1"
                memberAdapter?.notifyItemChanged(chooseIndex)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, msg)
            }
        })
    }

    private fun cancelMuteGroupMember() {
        HttpHelper.getInstance().cancelMuteGroupUser(memberList[chooseIndex].ugid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, "解除禁言")
                memberList[chooseIndex].gagstate = "0"
                memberAdapter?.notifyItemChanged(chooseIndex)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, msg)
            }
        })
    }

    private fun showKickOutTip() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("是否删除群成员?")
                .setPositiveButton("否") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("是") { dialog, _ -> //踢出某人
                    dialog.dismiss()
                    kickOutGroupMember()
                }.create().show()
    }

    private fun kickOutGroupMember() {
        HttpHelper.getInstance().kickOutGroupUser(memberList[chooseIndex].ugid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, "踢出成功")
                page = 0
                getSearchData()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, msg)
            }
        })
    }

    private fun setGroupManager() {
        HttpHelper.getInstance().setManagerGroupUser(memberList[chooseIndex].ugid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, "设置成功")
                memberList[chooseIndex].state = "2"
                memberAdapter?.notifyItemChanged(chooseIndex)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, msg)
            }
        })
    }

    private fun cancelGroupManager() {
        HttpHelper.getInstance().cancelManagerGroupUser(memberList[chooseIndex].ugid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, "取消成功")
                memberList[chooseIndex].state = "1"
                memberAdapter?.notifyItemChanged(chooseIndex)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupMemberActivity, msg)
            }
        })
    }
}