package com.aiwujie.shengmo.kt.ui.fragment.addgroupingmember

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.kt.adapter.GroupingConversationAdapter
import com.aiwujie.shengmo.kt.ui.activity.statistical.GroupingAddMemberActivity
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.viewmodel.GroupingMemberShareViewModel
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback
import kotlin.coroutines.CoroutineContext

class GroupingMemberConversationFragment:NormalListFragment() {

    lateinit var conversationList:ArrayList<V2TIMConversation>
    lateinit var memberIdList:ArrayList<String>
    lateinit var selectIdList:ArrayList<String>
    override fun initView(rootView: View) {
        super.initView(rootView)
        conversationList = ArrayList()
        memberIdList = (activity as GroupingAddMemberActivity).memberList
        //selectIdList = (activity as GroupingAddMemberActivity).selectList
        selectIdList = ArrayList()

        inviteModel.getLiveData().observe(this, Observer<ArrayList<String>> {
            it?.run {
               // LogUtil.d("refresh user " + it.size)
                selectIdList.clear()
                selectIdList.addAll(this)
                memberAdapter?.notifyDataSetChanged()
            }
        })
    }
    private val inviteModel by lazy {  ViewModelProviders.of(activity).get(GroupingMemberShareViewModel::class.java)}
    var memberAdapter:GroupingConversationAdapter? = null
    override fun loadData() {
        V2TIMManager.getConversationManager().getConversationList(0,Int.MAX_VALUE,object:V2TIMValueCallback<V2TIMConversationResult> {
            override fun onSuccess(p0: V2TIMConversationResult?) {
                p0?.conversationList?.run {
                    for (user in this) {
                        if (user.type == V2TIMConversation.V2TIM_C2C && user.userID.toInt() > 13) {
                            conversationList.add(user)
                        }
                    }
                }
                memberAdapter = GroupingConversationAdapter(activity,conversationList,memberIdList,selectIdList)
                with(rvSearchResult) {
                    adapter = memberAdapter
                    layoutManager = LinearLayoutManager(activity)
                }
                memberAdapter?.simpleItemListener = OnSimpleItemListener {
                    conversationList[it].userID.run {
//                        if (selectIdList.contains(this)) {
//                            selectIdList.remove(this)
//                        }  else {
//                            selectIdList.add(this)
//                        }
//                        memberAdapter?.notifyItemChanged(it)
//                        (activity as GroupingAddMemberActivity).refresh(1)

                        if (selectIdList.contains(this)) {
                            inviteModel.removeUser(this)
                        }  else {
                            inviteModel.addUser(this)
                        }
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



}