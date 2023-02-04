package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.listener.SimpleCallback
import com.aiwujie.shengmo.kt.ui.adapter.LiveMuteUserAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.net.LiveHttpHelper
import com.aiwujie.shengmo.timlive.bean.LiveOnlineUserListInfo
import com.aiwujie.shengmo.timlive.bean.ManagerList
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import razerdp.basepopup.BasePopupWindow

class LiveMuteUserPop(context: Context): BasePopupWindow(context)  {
    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var rvMuteUser: RecyclerView
    lateinit var tvTitle:TextView

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_online_user)
    }
    lateinit var anchorId:String
    constructor(context: Context,anchorId:String):this(context) {
        this.anchorId = anchorId
        initView()
    }

    fun initView() {
        smartRefreshLayout = findViewById(R.id.smart_refresh_live_online_user)
        rvMuteUser = findViewById(R.id.rv_live_online_user)
        tvTitle = findViewById(R.id.tv_live_online_user)
        smartRefreshLayout.finishLoadMoreWithNoMoreData()
        tvTitle.text = "禁言列表"
        getMuteData()

        smartRefreshLayout.setOnRefreshListener {
            getMuteData()
        }
    }

    private fun getMuteData() {
        HttpHelper.getInstance().getMuteUserList(anchorId,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val muteBean = GsonUtil.GsonToBean(data, ManagerList::class.java)
                muteBean?.data?.let {
                    val muteAdapter = LiveMuteUserAdapter(context,it)
                    val layoutManager = LinearLayoutManager(context)
                    rvMuteUser.adapter = muteAdapter
                    rvMuteUser.layoutManager = layoutManager
                    muteAdapter.onMuteUserListener = object : LiveMuteUserAdapter.OnMuteUserListener {
                        override fun doUserCancelMute(index: Int) {
                            cancelMuteUser(it[index].uid,object : SimpleCallback {
                                override fun doSuc() {
                                    muteListener?.doCancelMute(it[index].uid)
                                    it.removeAt(index)
                                    muteAdapter.notifyItemRemoved(index)
                                }
                                override fun doFail() {}

                            })
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }

        })
    }

    fun cancelMuteUser(uid:String,callback:SimpleCallback) {
//        LiveHttpHelper.getInstance().setNoTalking("3",uid,anchorId,object : HttpListener {
//            override fun onSuccess(data: String?) {
//                ToastUtil.show(context,"解除成功")
//                callback.doSuc()
//            }
//
//            override fun onFail(msg: String?) {
//               ToastUtil.show(context,msg)
//            }
//        })
        HttpHelper.getInstance().muteUser(uid,anchorId,"0",object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(context,"解除成功")
                callback.doSuc()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }
        })
    }

    interface OnLiveMuteListener {
        fun doCancelMute(uid:String)
    }

    var muteListener:OnLiveMuteListener? = null
}