package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent
import razerdp.basepopup.BasePopupWindow

class LiveOnlineUserPop(context: Context):BasePopupWindow(context) {

    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var rvLiveUser: RecyclerView
    var userAdapter:LiveOnlineUserAdapter? = null
    var page = 1
    var uid = "";
    var liveRole = 0 //在群里的角色  0 - 普通用户 1 - 场控 2 - 主播 (0其实是黑v)
    lateinit var liveUserList:ArrayList<LiveOnlineUserListInfo.DataBean>
    constructor(context: Context,uid:String,liveRole:Int):this(context) {
        this.uid = uid
        this.liveRole = liveRole
        initView()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_online_user)
    }

    fun initView() {
        smartRefreshLayout = findViewById(R.id.smart_refresh_live_online_user)
        rvLiveUser = findViewById(R.id.rv_live_online_user)

        liveUserList = ArrayList()
        getData()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 1
                getData()
            }
        })
    }

    fun getData() {
        HttpHelper.getInstance().getLiveOnLineUser(uid,page,object: HttpCodeListener {
            override fun onSuccess(data: String?) {
                var tempData = GsonUtil.GsonToBean(data,LiveOnlineUserListInfo::class.java)
                smartRefreshLayout.setNoMoreData(false)
                tempData?.let {
                    var tempList = it.data
                    if (page == 1) {
                        smartRefreshLayout.finishRefresh()
                        liveUserList.clear()
                        liveUserList.addAll(tempList)
                        userAdapter = LiveOnlineUserAdapter(context,liveUserList,liveRole)
                        var linearLayoutManager = LinearLayoutManager(context)
                        rvLiveUser.adapter = userAdapter
                        rvLiveUser.layoutManager = linearLayoutManager
                        userAdapter?.setLiveUserListener(object : LiveOnlineUserAdapter.OnLiveUserListener {
                            override fun doUseItemClick(index: Int) {
                                var tcm = TimCustomMessage()
                                tcm.uid = liveUserList[index].uid
                                tcm.anchor_uid = uid
                                when(liveRole) {
                                    0,2 -> {
                                        tcm.is_group_admin = "0"
                                        //0在这里其实是黑v
                                        tcm.is_admin = "1"
                                    }
                                    1 -> {
                                        tcm.is_group_admin = "1"
                                    }
                                }
                                onLiveUserListener?.onLiveUserClick(tcm)
                            }

                            override fun doUserLink(index: Int) {
                                var tcm = TimCustomMessage()
                                tcm.uid = liveUserList[index].uid
                                tcm.anchor_uid = uid
                                when(liveRole) {
                                    0,2 -> {
                                        tcm.is_group_admin = "0"
                                    }
                                    1 -> {
                                        tcm.is_group_admin = "1"
                                    }
                                }
                                onLiveUserListener?.onLiveUserLink(tcm)
                            }

                            override fun doUserKitOut(index: Int) {
                                var tcm = TimCustomMessage()
                                tcm.uid = liveUserList[index].uid
                                tcm.anchor_uid = uid
                                when(liveRole) {
                                    0,2 -> {
                                        tcm.is_group_admin = "0"
                                    }
                                    1 -> {
                                        tcm.is_group_admin = "1"
                                    }
                                }
                                onLiveUserListener?.onLiveUserKitOut(tcm)
                            }
                        })
                    } else{
                        smartRefreshLayout.finishLoadMore()
                        var temp = liveUserList.size
                        liveUserList.addAll(tempList)
                        userAdapter?.notifyItemRangeInserted(temp,tempList.size)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
                if (code == 4001) {
                   smartRefreshLayout.finishLoadMoreWithNoMoreData()
                   smartRefreshLayout.finishRefresh()
                }
            }
        })
    }

    fun refreshData() {
        page = 1
        getData()
    }

    interface OnLiveUserListener {
        fun onLiveUserClick(tcm: TimCustomMessage)
        fun onLiveUserLink(tcm:TimCustomMessage);
        fun onLiveUserKitOut(tcm:TimCustomMessage)
    }
    private var onLiveUserListener:OnLiveUserListener? = null

    fun setOnLiveUserListener(onLiveUserListener:OnLiveUserListener) {
        this.onLiveUserListener = onLiveUserListener
    }
}