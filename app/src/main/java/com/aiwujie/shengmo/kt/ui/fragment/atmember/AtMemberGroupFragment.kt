package com.aiwujie.shengmo.kt.ui.fragment.atmember

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aiwujie.shengmo.adapter.AtGroupAdapter
import com.aiwujie.shengmo.bean.AtData
import com.aiwujie.shengmo.bean.AtUserData
import com.aiwujie.shengmo.bean.Atbean
import com.aiwujie.shengmo.bean.GroupData
import com.aiwujie.shengmo.kt.adapter.AtConversationAdapter
import com.aiwujie.shengmo.kt.adapter.GroupingConversationAdapter
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity
import com.aiwujie.shengmo.kt.ui.activity.statistical.GroupingAddMemberActivity
import com.aiwujie.shengmo.kt.ui.fragment.NormalListFragment
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback
import kotlinx.android.synthetic.main.activity_applyforlive.*
/**
 * 提醒谁看 - 群组
 */
class AtMemberGroupFragment:NormalListFragment() {

    lateinit var groupList:ArrayList<GroupData.DataBean>
    lateinit var atBeanList:ArrayList<Atbean.DataBean>
    override fun initView(rootView: View) {
        super.initView(rootView)
        groupList = ArrayList()
        atBeanList = (activity as AtMemberActivity).atBeanList
    }

    var memberAdapter:AtGroupAdapter? = null
    override fun loadData() {
       getGroupData()
    }
    var page = 0
    private fun getGroupData() {
        HttpHelper.getInstance().getUserGroupList(page,object: HttpCodeListener {
            override fun onSuccess(data: String?) {
                val tempData = GsonUtil.GsonToBean(data,GroupData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            groupList.clear()
                            groupList.addAll(this)
                            memberAdapter = AtGroupAdapter(activity,groupList,atBeanList)
                            with(rvSearchResult) {
                                adapter = memberAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            memberAdapter?.setOnSimpleItemListener {
                                index ->
                                groupList[index].gid.run {
                                    if (atBeanList.map { it.uid }.contains(this)) {
                                        removeUser(this)
                                    } else {
                                        val atBean = Atbean.DataBean()
                                        atBean.uid = this
                                        atBean.nickname = "[群]" + groupList[index].groupname
                                        atBeanList.add(atBean)
                                    }
                                }
                                memberAdapter?.notifyItemChanged(index)
                                (activity as AtMemberActivity).refresh(4)
                            }
                        }
                        else -> {
                            val tempIndex = groupList.size
                            groupList.addAll(this)
                            memberAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

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