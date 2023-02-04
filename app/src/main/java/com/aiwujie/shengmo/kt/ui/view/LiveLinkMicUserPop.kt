package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.ui.adapter.LiveOnlineUserAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.bean.LiveOnlineUserListInfo
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.qcloud.tim.tuikit.live.bean.LinkMicStateBean
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent
import razerdp.basepopup.BasePopupWindow

class LiveLinkMicUserPop(context: Context):BasePopupWindow(context) {

    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var rvLiveUser: RecyclerView
    lateinit var tvLiveLink: TextView
    lateinit var tvLiveCamera:TextView
    var userAdapter:LiveOnlineUserAdapter? = null
    var page = 1
    var uid = "";
    lateinit var linkMicBean:LinkMicStateBean
    lateinit var liveUserList:ArrayList<LiveOnlineUserListInfo.DataBean>
    constructor(context: Context,linkMicStateBean: LinkMicStateBean):this(context) {
        this.linkMicBean = linkMicStateBean
        this.uid = linkMicBean.anchorId
        initView()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_link_mic)
    }

    fun initView() {
        smartRefreshLayout = findViewById(R.id.smart_refresh_live_online_user)
        rvLiveUser = findViewById(R.id.rv_live_online_user)
        tvLiveLink = findViewById(R.id.tv_pop_live_link_link)
        tvLiveCamera = findViewById(R.id.tv_pop_live_link_camera)
        findViewById<TextView>(R.id.tv_live_online_user).text = "连麦用户"
        liveUserList = ArrayList()
        getLinkMicList()
        smartRefreshLayout.setOnRefreshListener {
            getLinkMicList()
        }

        showDataView()

        tvLiveLink.setOnClickListener {
            //this.dismiss()
            tvLiveLink.isClickable = false
            onLiveUserListener?.onLiveUserLink(linkMicBean.linkState)
//            when(linkMicBean.linkState) {
//                1 -> {
//                    linkMicBean.linkState = 2
//                }
//                2 -> {
//                    linkMicBean.linkState = 1
//                }
//                3-> {
//                   linkMicBean.linkState = 1
//                }
//            }
//            showDataView()
        }
        tvLiveCamera.setOnClickListener {
            //this.dismiss()
            onLiveUserListener?.onLiveUserDoCamera(!linkMicBean.isCameraOpen)
            linkMicBean.isCameraOpen = !linkMicBean.isCameraOpen
            showDataView()
        }
    }

    fun doLinkRequestSuc() {
        tvLiveLink.isClickable = true
    }

    fun refreshLinkState(state:Int) {
        linkMicBean.linkState = state
        showDataView()
    }

    private fun showDataView() {
        when(linkMicBean.linkState) {
            1 -> {
                tvLiveLink.text = "申请连麦"
            }
            2 -> {
                tvLiveLink.text = "取消申请"
            }
            3-> {
                tvLiveLink.text = "关闭连麦"
            }
        }

        when(linkMicBean.isCameraOpen) {
            true -> {
                tvLiveCamera.text = "关闭摄像头"
            }
            false -> {
                tvLiveCamera.text = "开启摄像头"
            }
        }
    }

    private fun refreshStateInfo() {

    }


    private fun getLinkMicList() {
        HttpHelper.getInstance().getLiveLinkMicList(uid,object :HttpCodeListener {
            override fun onSuccess(data: String?) {
                var tempData = GsonUtil.GsonToBean(data,LiveOnlineUserListInfo::class.java)
                tempData?.let {
                    var tempList = it.data
                    smartRefreshLayout.finishRefresh()
                    liveUserList.clear()
                    liveUserList.addAll(tempList)
                    userAdapter = LiveOnlineUserAdapter(context, liveUserList,0)
                    var linearLayoutManager = LinearLayoutManager(context)
                    rvLiveUser.adapter = userAdapter
                    rvLiveUser.layoutManager = linearLayoutManager
                    userAdapter?.setLiveUserListener(object : LiveOnlineUserAdapter.OnLiveUserListener {
                        override fun doUseItemClick(index: Int) {
                            var tcm = TimCustomMessage()
                            tcm.uid = liveUserList[index].uid
                            tcm.anchor_uid = uid
                            tcm.is_group_admin = "0"
                            onLiveUserListener?.onLiveUserClick(tcm)
                        }

                        override fun doUserLink(index: Int) {

                        }

                        override fun doUserKitOut(index: Int) {
                            var tcm = TimCustomMessage()
                            tcm.uid = liveUserList[index].uid
                            tcm.anchor_uid = uid
                            tcm.is_group_admin = "0"
                            onLiveUserListener?.onLiveUserKickMic(tcm)
                            rvLiveUser.postDelayed({
                                getLinkMicList()
                            },3000)
                           // onLiveUserListener?.onLiveUserClick(tcm)
                        }
                    })
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }
        })
    }

    interface OnLiveUserListener {
        fun onLiveUserClick(tcm: TimCustomMessage)
        fun onLiveUserLink(type:Int);
        fun onLiveUserDoCamera(isOpen:Boolean)
        fun onLiveUserKickMic(tcm:TimCustomMessage)
    }
    private var onLiveUserListener:OnLiveUserListener? = null

    fun setOnLiveUserListener(onLiveUserListener:OnLiveUserListener) {
        this.onLiveUserListener = onLiveUserListener
    }
}