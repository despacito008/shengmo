package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.view.View
import com.aiwujie.shengmo.kt.ui.fragment.addgroupingmember.GroupingMemberConversationFragment
import com.aiwujie.shengmo.kt.ui.fragment.addgroupingmember.GroupingMemberRelationShipFragment
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.viewmodel.GroupingMemberShareViewModel

class GroupingAddMemberActivity : StatisticalDetailActivity() {
    lateinit var conversationFragment: GroupingMemberConversationFragment
    lateinit var attentionFragment: GroupingMemberRelationShipFragment
    lateinit var fansFragment: GroupingMemberRelationShipFragment
    override fun getFragmentList(): List<Fragment> {
        conversationFragment = GroupingMemberConversationFragment();
        attentionFragment = GroupingMemberRelationShipFragment.newInstance(0);
        fansFragment = GroupingMemberRelationShipFragment.newInstance(1);
        return arrayListOf(conversationFragment, attentionFragment, fansFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("聊天", "关注", "粉丝")
    }

    override fun getPageTitle(): String {
        return "分组添加成员"
    }

    lateinit var memberList: ArrayList<String>
    lateinit var selectList: ArrayList<String>
    override fun initViewComplete() {
        super.initViewComplete()
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        tvNormalTitleMore.text = "完成"
        memberList = intent.getStringArrayListExtra("memberList") ?: ArrayList()
        selectList = ArrayList()
        tvNormalTitleMore.setOnClickListener {
            if (selectList.size == 0) {
                "请至少选择一位好友".showToast()
            } else {
                val ids = StringBuffer()
                selectList.forEach {
                    ids.append(it).append(",")
                }
                if (ids.isNotEmpty()) {
                    val idStr = ids.toString().substring(0, ids.toString().length)
                    addMember(idStr)
                }
            }
        }
        val inviteModel = ViewModelProviders.of(this).get(GroupingMemberShareViewModel::class.java)
        inviteModel.initData(ArrayList<String>())
        inviteModel.getLiveData().observe(this, Observer<ArrayList<String>> {
            it?.run {
                selectList.clear()
                selectList.addAll(this)
            }
        })
    }

    private fun addMember(ids: String) {
        HttpHelper.getInstance().addGroupingMember(intent.getStringExtra("fgid"), ids, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }

    fun refresh(type: Int) {
        when (type) {
            1 -> {
                attentionFragment.refresh()
                fansFragment.refresh()
            }
            2 -> {
                conversationFragment.refresh()
                fansFragment.refresh()
            }
            3 -> {
                conversationFragment.refresh()
                attentionFragment.refresh()
            }
        }
    }
}