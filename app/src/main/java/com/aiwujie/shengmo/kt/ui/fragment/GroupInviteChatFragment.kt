package com.aiwujie.shengmo.kt.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.aiwujie.shengmo.kt.adapter.GroupInviteChatAdapter
import com.aiwujie.shengmo.viewmodel.GroupInviteShareViewModel
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback

class GroupInviteChatFragment:NormalListFragment() {
    var groupId = ""
    override fun loadData() {
        gLoadHolder.showLoading()
        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setEnableRefresh(false)
        groupId = arguments.getString("groupId")!!
        getChatData()

    }
    var inviteAdapter:GroupInviteChatAdapter? = null
    private fun getChatData() {
        val conversationList = ArrayList<V2TIMConversation>()
        V2TIMManager.getConversationManager().getConversationList(0, Int.MAX_VALUE,object :V2TIMValueCallback<V2TIMConversationResult> {
            override fun onSuccess(p0: V2TIMConversationResult?) {
                gLoadHolder.showLoadSuccess()
                p0?.run {
                    for (v2TimConversation in this.conversationList) {
                        if (v2TimConversation.type == V2TIMConversation.V2TIM_GROUP || v2TimConversation.userID.toInt() > 13) {
                            if (v2TimConversation.type == V2TIMConversation.V2TIM_GROUP && !TextUtils.isEmpty(groupId) && groupId == v2TimConversation.groupID) {
                                continue
                            }
                            conversationList.add(v2TimConversation)
                        }
                    }
                    inviteAdapter = GroupInviteChatAdapter(activity,conversationList)
                    with(rvSearchResult) {
                        adapter = inviteAdapter
                        layoutManager = LinearLayoutManager(activity)
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
                gLoadHolder.showEmpty()
            }
        })
    }

    fun getChooseUserId():List<String> {
        return inviteAdapter?.getChooseUserId()?:ArrayList()
    }

    fun getChooseGroupId():List<String> {
        return inviteAdapter?.getChooseGroupId()?:ArrayList()
    }
}