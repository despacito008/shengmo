package com.aiwujie.shengmo.timlive.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean
import com.aiwujie.shengmo.bean.SearchUserData
import com.aiwujie.shengmo.timlive.kt.adapter.LiveUserAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil.*
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.feezu.liuli.timeselector.Utils.TextUtil

class SearchLiveUserActivity : BaseActivity() {
    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var rvLiveUser :RecyclerView
    lateinit var etSearch: EditText
    lateinit var tvSearch: TextView
    lateinit var ivReturn: ImageView
    lateinit var ivTitleRight: ImageView
    lateinit var tvTitle: TextView
    var page = 1
    lateinit var userList: ArrayList<ScenesRoomInfoBean>
    var userAdapter: LiveUserAdapter? = null
    var isLoading : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_search_live_user)
        StatusBarUtil.showLightStatusBar(this)
        smartRefreshLayout = findViewById(R.id.smart_refresh_search_live_user)
        rvLiveUser = findViewById(R.id.rv_search_live_user)
        etSearch = findViewById(R.id.et_search)
        tvSearch = findViewById(R.id.tv_search)
        ivReturn = findViewById(R.id.iv_normal_title_back)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitle.text = "搜索成员"
        etSearch.hint = "输入主播名称"
        ivTitleRight.visibility = View.INVISIBLE
        setListener()
        userList = ArrayList()
    }

    fun setListener() {
        tvSearch.setOnClickListener {
            if (TextUtil.isEmpty(etSearch.text.toString())) {
                return@setOnClickListener
            }
            page = 1
            getSearchData()
        }
        ivReturn.setOnClickListener {
            finish()
        }
        smartRefreshLayout.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getSearchData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 1
                getSearchData()
            }
        })
    }

    private fun getSearchData() {
        if (isLoading) return
        isLoading = true
        HttpHelper.getInstance().searchLiveUser(etSearch.text.toString(),page,object :HttpCodeListener{
            override fun onSuccess(data: String?) {
                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.setNoMoreData(false)
                isLoading = false
                val bean = GsonToBean(data, SearchUserData::class.java)
                val tempList = bean.data
                if (page == 1) {
                    userList.clear()
                    userList.addAll(tempList)
                    userAdapter = LiveUserAdapter(this@SearchLiveUserActivity, userList)
                    val linearLayoutManager = LinearLayoutManager(this@SearchLiveUserActivity)
                    rvLiveUser.layoutManager = linearLayoutManager
                    rvLiveUser.adapter = userAdapter
                    userAdapter!!.setOnSimpleItemListener(OnSimpleItemListener {
                        if (userList[it].is_live == "1") {
                            val roomInfo:ArrayList<ScenesRoomInfoBean> = ArrayList()
                            roomInfo.add(userList[it])
                            RoomManager.enterRoom(this@SearchLiveUserActivity,roomInfo,0,"")
                        } else {
                            var intent = Intent(this@SearchLiveUserActivity,UserInfoActivity::class.java)
                            intent.putExtra("uid",userList[it].uid)
                            startActivity(intent)
                        }
                    })
                } else{
                    val temp = userList.size
                    userList.addAll(tempList)
                    userAdapter?.notifyItemRangeInserted(temp,tempList.size)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.finishLoadMore()
                isLoading = false
                ToastUtil.show(this@SearchLiveUserActivity,msg)
                if (code == 4001) {
                    smartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        })
    }

}