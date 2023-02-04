package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.ui.adapter.LiveManageUserAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.net.LiveHttpHelper
import com.aiwujie.shengmo.timlive.bean.LiveOnlineUserListInfo
import com.aiwujie.shengmo.timlive.bean.ManagerList
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

class LiveManageUserPop(context: Context):BasePopupWindow(context) {

    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var rvLiveUser: RecyclerView
    var userAdapter:LiveManageUserAdapter? = null
    var page = 1
    var uid = "";
    var liveRole = 2 //在群里的角色  0 - 普通用户 1 - 场控 2 - 主播
    var anchor_id = "";
    lateinit var liveUserList:ArrayList<ManagerList.DataBean>
    constructor(context: Context,uid:String,anchor_id:String):this(context) {
        this.uid = uid
        this.anchor_id = anchor_id;
        initView()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_manage_user)
    }

    fun initView() {
        smartRefreshLayout = findViewById(R.id.smart_refresh_live_manage_user)
        rvLiveUser = findViewById(R.id.rv_live_manage_user)
        liveUserList = ArrayList()
        getData()
        smartRefreshLayout.setOnRefreshListener {
            getData()
        }
    }

    fun getData() {
        HttpHelper.getInstance().getLiveManageUser(anchor_id,object: HttpCodeListener {
            override fun onSuccess(data: String?) {
                var tempData = GsonUtil.GsonToBean(data,ManagerList::class.java)
                tempData?.let {
                    var tempList = it.data
                    smartRefreshLayout.setNoMoreData(false)
                    if (page == 1) {
                        smartRefreshLayout.finishRefresh()
                        liveUserList.clear()
                        liveUserList.addAll(tempList)
                        userAdapter = LiveManageUserAdapter(context,liveUserList)
                        val linearLayoutManager = LinearLayoutManager(context)
                        rvLiveUser.adapter = userAdapter
                        rvLiveUser.layoutManager = linearLayoutManager
                        userAdapter?.setManageUserListener(object : LiveManageUserAdapter.OnManageUserListener {
                            override fun onClickUser(index: Int) {
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
                                onLiveUserListener?.onLiveUserClick(tcm)
                            }

                            override fun onRemoveUser(index: Int) {
                                removeManage(index)
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

    fun removeManage(index:Int) {
         LiveHttpHelper.getInstance().setChatRoomAdmin("2",liveUserList[index].uid,anchor_id,object : HttpListener {
             override fun onSuccess(data: String?) {
                 ToastUtil.show(context,"已解除场控")
                 liveUserList.removeAt(index)
                 userAdapter?.notifyItemRemoved(index)
                 onLiveUserListener?.onLiveUserRefresh()
             }

             override fun onFail(msg: String?) {
                ToastUtil.show(context,msg)
             }
         });
    }

    interface OnLiveUserListener {
        fun onLiveUserClick(tcm: TimCustomMessage)
        fun onLiveUserRefresh()
    }
    private var onLiveUserListener:OnLiveUserListener? = null

    fun setOnLiveUserListener(onLiveUserListener:OnLiveUserListener) {
        this.onLiveUserListener = onLiveUserListener
    }
}