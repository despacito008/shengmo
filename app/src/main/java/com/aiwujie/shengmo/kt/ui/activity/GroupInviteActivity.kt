package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.View
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteChatFragment
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteFollowFragment
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteNearFragment
import com.aiwujie.shengmo.kt.ui.activity.statistical.StatisticalDetailActivity
import com.aiwujie.shengmo.kt.ui.fragment.*
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.ToastUtil
import kotlinx.android.synthetic.main.dashang_count_layout.*

class GroupInviteActivity: StatisticalDetailActivity(){

    var gid = ""
    lateinit var inviteChatFragment: GroupInviteChatFragment
    lateinit var inviteNearFragment: GroupInviteNearFragment
    lateinit var inviteAttentionFragment: GroupInviteAttentionFragment
    override fun initViewComplete() {
        super.initViewComplete()
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        tvNormalTitleMore.text = "完成"
        gid = intent.getStringExtra("groupId")!!
        tvNormalTitleMore.setOnClickListener {
            sendInvite()
        }
    }

    override fun getFragmentList(): List<Fragment> {
        val fragmentList:ArrayList<Fragment> = ArrayList()
        inviteChatFragment = GroupInviteChatFragment()
        inviteNearFragment = GroupInviteNearFragment()
        inviteAttentionFragment = GroupInviteAttentionFragment()
        val bundle = Bundle()
        bundle.putString("groupId", gid)
        inviteChatFragment.arguments = bundle
        fragmentList.add(inviteChatFragment)
        fragmentList.add(inviteNearFragment)
        fragmentList.add(inviteAttentionFragment)
        return fragmentList
    }

    override fun getTitleList(): List<String> {
        val titleList:ArrayList<String> = ArrayList()
        titleList.add("聊天")
        titleList.add("附近")
        titleList.add("关注")
        return titleList
    }

    override fun getPageTitle(): String {
        return "邀请群成员"
    }

    private fun sendInvite() {
        val userIdList = ArrayList<String>()
        val groupIdList = ArrayList<String>()
        userIdList.addAll(inviteChatFragment.getChooseUserId())
        userIdList.addAll(inviteNearFragment.getChooseId())
        userIdList.addAll(inviteAttentionFragment.getChooseId())
        groupIdList.addAll(inviteChatFragment.getChooseGroupId())
        if (userIdList.isEmpty() && groupIdList.isEmpty()) {
            ToastUtil.show(this,"请至少选择一个")
            return
        }
        var toUid = ""
        var toGid = ""
        if (userIdList.isNotEmpty()) {
            for (uid in userIdList) {
                toUid += "$toUid$uid,"
            }
            toUid = toUid.substring(0,toUid.length-1)
        }
        if (groupIdList.isNotEmpty()) {
            for (gid in groupIdList) {
                toGid += "$toGid$gid,"
            }
            toGid = toGid.substring(0,toGid.length-1)
        }
        HttpHelper.getInstance().sendGroupInvite(gid,toUid,toGid,object: HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                ToastUtil.show(this@GroupInviteActivity,"已发送邀请至${userIdList.size}人 ${groupIdList.size}群")
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@GroupInviteActivity,msg)
            }
        })
    }
}