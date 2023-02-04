package com.aiwujie.shengmo.kt.ui.fragment

import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.adapter.ShareGroupRecyclerAdapter
import com.aiwujie.shengmo.bean.GroupData
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.tim.bean.DynamicMessageBean
import com.aiwujie.shengmo.tim.bean.LiveAnchorMessageBean
import com.aiwujie.shengmo.tim.helper.MessageHelper
import com.aiwujie.shengmo.tim.helper.MessageSendHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.google.gson.Gson
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack

class ShareRelationGroupFragment:NormalListFragment() {
    override fun loadData() {
        groupList = ArrayList()
        initData()
        getGroupList()
    }
    var page = 0
    lateinit var groupList:ArrayList<GroupData.DataBean>
    var groupAdapter:ShareGroupRecyclerAdapter? = null
    private fun getGroupList() {
        HttpHelper.getInstance().getUserGroupList(page, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val groupData = Gson().fromJson(data, GroupData::class.java)
                groupData?.data?.run {
                    when (page) {
                        0 -> {
                            groupList.clear()
                            groupList.addAll(this)
                            groupAdapter = ShareGroupRecyclerAdapter(activity,groupList)
                            with(rvSearchResult) {
                                adapter = groupAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            groupAdapter?.setOnSimpleItemListener {
                                index ->
                                showShareUserTip(groupList[index])
                            }
                        }
                        else -> {
                            val tempIndex = groupList.size
                            groupList.addAll(this)
                            groupAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    fun showShareUserTip(user: GroupData.DataBean) {
        AlertDialog.Builder(activity).apply {
            setTitle("提示")
            when (sType) {
                1 -> setMessage("分享动态给${user.groupname}?")
                2 -> setMessage("分享同好给${user.groupname}?")
                4 -> setMessage("分享直播给${user.groupname}?")
            }
            setPositiveButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton("确认") { dialog, _ ->
                dialog.dismiss()
                share(user.gid)
            }
            show()
        }
    }

    private var uid:String = ""
    private var did:String = ""
    private var sType = 0
    private var pic = ""
    private var content = ""
    fun initData() {
        activity?.intent?.run {
            sType = getIntExtra("leixing", 0)
            uid = getStringExtra("id")?:""
            did = getStringExtra("did")?:""
            content = getStringExtra("content")?:""
            pic = getStringExtra("pic")?:""
        }
    }

    private fun share(gid: String) {
        when (sType) {
            1, 2 -> shareUser(gid)
            4 -> shareLive(gid)
        }
    }

    private fun shareUser(groupId: String) {
        val dynamicMessageBean = DynamicMessageBean()
        val contentDictBean = DynamicMessageBean.ContentDictBean()
        contentDictBean.shareType = sType
        if (sType == 1) {
            contentDictBean.contTitle = "hi,给你推荐一个动态"
        } else {
            contentDictBean.contTitle = "hi,给你推荐一位同好"
        }
        contentDictBean.userId = uid
        contentDictBean.newid = uid
        contentDictBean.icon = pic
        contentDictBean.content = content
        dynamicMessageBean.contentDict = contentDictBean
        val customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(dynamicMessageBean))
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, true, groupId, object : IUIKitCallBack {
            override fun onSuccess(data: Any) {
                "分享成功".showToast()
                activity.finish()
            }

            override fun onError(module: String, errCode: Int, errMsg: String) {
                "分享失败 $errMsg".showToast()
            }
        })
    }

    private fun shareLive(groupId: String) {
        val anchorMessageBean = LiveAnchorMessageBean()
        val contentDictBean = LiveAnchorMessageBean.ContentDictBean()
//        contentDictBean.setAnchorId(activity.intent.getStringExtra("anchorId"))
//        contentDictBean.setLiveTitle(activity.intent.getStringExtra("anchorName"))
//        contentDictBean.setLivePoster(activity.intent.getStringExtra("anchorCover"))
        contentDictBean.setAnchorId(uid)
        contentDictBean.setLiveTitle(content)
        contentDictBean.setLivePoster(pic)
        anchorMessageBean.contentDict = contentDictBean
        val customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(anchorMessageBean))
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, true, groupId, object : IUIKitCallBack {
            override fun onSuccess(data: Any) {
                "分享成功".showToast()
                activity.finish()
            }

            override fun onError(module: String, errCode: Int, errMsg: String) {
                "分享失败 $errMsg".showToast()
            }
        })
    }
}