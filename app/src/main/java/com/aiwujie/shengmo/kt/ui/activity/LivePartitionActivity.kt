package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.PartitionTipBean
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.decoration.GridHeaderItemDecoration
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.decoration.SpaceItemDecoration
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter2
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.gloading.GlobalLoadingStatusView
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.liteav.login.ProfileManager

/**
 * 直播 - 分区
 */
class LivePartitionActivity: BaseActivity() {

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var smartRefreshLayout: SmartRefreshLayout
    private lateinit var rvPartition:RecyclerView
    private lateinit var tvPartition:TextView

    private var page = 0
    private var tid = ""
    private var partName = ""

    lateinit var anchorList:ArrayList<ScenesRoomInfoBean>

    var onItemClickListener = RoomListAdapter2.OnItemClickListener {
        position, roomInfo ->
        var index = position
        if(position > 0) {
            index--
        }
        val selfUserId = ProfileManager.getInstance().userModel.userId
        if (roomInfo.uid != selfUserId && "1" == roomInfo.is_live) {
            RoomManager.enterLiveRoom(this,roomInfo.uid,roomInfo.room_id)
        } else {
            val intent = Intent(this, UserInfoActivity::class.java)
            intent.putExtra("uid", roomInfo.uid)
            startActivity(intent)
        }
    }
    lateinit var gLoadingHolder: Gloading.Holder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_live_partition)
        StatusBarUtil.showLightStatusBar(this)
        tid = intent.getStringExtra("tid")?:""
        partName = intent.getStringExtra("part")?:"分区直播"
        anchorList = ArrayList()
        initView()
    }

    fun initView() {
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        ivTitleRight.visibility = View.INVISIBLE
        tvTitle.text = partName

        smartRefreshLayout = findViewById(R.id.smart_refresh_activity_live_partition)
        rvPartition = findViewById(R.id.rv_activity_live_partition)
        tvPartition = findViewById(R.id.tv_activity_live_partition)
        gLoadingHolder = Gloading.getDefault().wrap(rvPartition)
        initLivePartHeadView()
        getLivePartitionTip()
        getLiveData()
        initListener()
    }

    fun initListener() {
        ivTitleBack.setOnClickListener {
            finish()
        }

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getLiveData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
               page = 0
               getLiveData()
            }

        })
    }

    var anchorAdapter:RoomListAdapter2? = null
    var headAdapter:HeaderViewAdapter? = null
    var itemDecoration: RecyclerView.ItemDecoration? = null
    private fun getLiveData() {
        HttpHelper.getInstance().getLabelAnchorList(tid,page,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.setNoMoreData(false)
                gLoadingHolder.showLoadSuccess()
                val scenesRoomInfo = Gson().fromJson(data, SearchUserData::class.java)
                scenesRoomInfo?.data?.let {
                    when (page) {
                        0 -> {
                            anchorList.clear()
                            anchorList.addAll(it)
                            anchorAdapter = RoomListAdapter2(this@LivePartitionActivity,anchorList,onItemClickListener)
                            headAdapter = HeaderViewAdapter(anchorAdapter)
                            headAdapter?.addHeaderView(partHeadView)
                            val headLayoutManager = HeaderViewGridLayoutManager(this@LivePartitionActivity,2,headAdapter)
                            with(rvPartition) {
                                adapter = headAdapter
                                layoutManager = headLayoutManager
                                if (itemDecoration == null) {
                                    itemDecoration = GridHeaderItemDecoration(15,1)
                                    addItemDecoration(itemDecoration)
                                }
                            }
                        }

                        else -> {
                            val temp = anchorList.size
                            anchorList.addAll(it)
                            anchorAdapter?.notifyItemRangeInserted(temp,it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                if (code == 4000) {
                    when(page) {
                        0 -> {
                            smartRefreshLayout.finishRefresh()
                            gLoadingHolder.showLoadingStatus(GlobalLoadingStatusView.STATUS_LIVE_EMPTY)
                        }
                        else -> {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData()
                        }
                    }
                } else {
                    ToastUtil.show(this@LivePartitionActivity,msg)
                }
            }
        })
    }


    private fun getLivePartitionTip() {
        HttpHelper.getInstance().getLabelIntroduce(tid,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val tipBean = GsonUtil.GsonToBean(data,PartitionTipBean::class.java)
                tipBean?.data?.let {
                   // tvPartition.text = it.introduce
                    //tvPartition.visibility = if (TextUtils.isEmpty(it.introduce)) View.GONE else View.VISIBLE
                    tvTip?.text = it.introduce
                    tvTip?.visibility = if (TextUtils.isEmpty(it.introduce)) View.GONE else View.VISIBLE
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@LivePartitionActivity,msg)
            }
        })
    }


    var tvTip:TextView? = null
    lateinit var partHeadView:View
    private fun initLivePartHeadView() {
        partHeadView = LayoutInflater.from(this@LivePartitionActivity).inflate(R.layout.app_layout_live_part_header, null)
        tvTip = partHeadView.findViewById(R.id.tv_live_part_tips)
    }
}