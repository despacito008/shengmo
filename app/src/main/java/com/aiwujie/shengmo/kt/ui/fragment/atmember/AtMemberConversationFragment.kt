package com.aiwujie.shengmo.kt.ui.fragment.atmember

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.bean.AtData
import com.aiwujie.shengmo.bean.AtUserData
import com.aiwujie.shengmo.bean.Atbean
import com.aiwujie.shengmo.kt.adapter.AtConversationAdapter
import com.aiwujie.shengmo.kt.adapter.GroupingConversationAdapter
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.GroupingAddMemberActivity
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback
import kotlinx.android.synthetic.main.activity_applyforlive.*

/**
 * 提醒谁看 - 聊天
 */
class AtMemberConversationFragment:NormalListFragment() {

    lateinit var conversationList:ArrayList<V2TIMConversation>
    lateinit var atBeanList:ArrayList<Atbean.DataBean>
    override fun initView(rootView: View) {
        super.initView(rootView)
        conversationList = ArrayList()
        atBeanList = (activity as AtMemberActivity).atBeanList
    }

    var memberAdapter:AtConversationAdapter? = null
    override fun loadData() {
        V2TIMManager.getConversationManager().getConversationList(0,Int.MAX_VALUE,object:V2TIMValueCallback<V2TIMConversationResult> {
            override fun onSuccess(p0: V2TIMConversationResult?) {
                p0?.conversationList?.run {
                    for (user in this) {
                        if (user.type == V2TIMConversation.V2TIM_C2C && user.userID.toInt() > 13) {
                            conversationList.add(user)
                        } else if (user.type == V2TIMConversation.V2TIM_GROUP) {
                            //  过滤高端群组
                            if (!user.groupID  .startsWith("top")){
                                conversationList.add(user)
                            }

                        }
                    }
                }
                memberAdapter = AtConversationAdapter(activity,conversationList,atBeanList)
                with(rvSearchResult) {
                    adapter = memberAdapter
                    layoutManager = LinearLayoutManager(activity)
                }
                memberAdapter?.simpleItemListener = OnSimpleItemListener {
                    conversationList[it].run {
                        when (type) {
                            V2TIMConversation.V2TIM_C2C -> {
                                if (atBeanList.map { it.uid }.contains(userID)) {
                                    removeUser(userID)
                                }  else {
                                    val atBean = Atbean.DataBean()
                                    atBean.uid = userID
                                    atBean.nickname = conversationList[it].showName
                                    atBeanList.add(atBean)
                                }
                            }
                            V2TIMConversation.V2TIM_GROUP -> {
                                if (atBeanList.map { it.uid }.contains(groupID)) {
                                    removeUser(groupID)
                                }  else {
                                    val atBean = Atbean.DataBean()
                                    atBean.uid = groupID
                                    atBean.nickname = "[群]" + conversationList[it].showName
                                    atBeanList.add(atBean)
                                }
                            }
                        }
                        memberAdapter?.notifyItemChanged(it)
                        (activity as AtMemberActivity).refresh(1)
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {

            }
        })
    }

    fun refresh() {
        memberAdapter?.notifyDataSetChanged()
    }

    fun removeUser(uid:String) {
        (activity as AtMemberActivity).removeAtUser(uid)
    }


}