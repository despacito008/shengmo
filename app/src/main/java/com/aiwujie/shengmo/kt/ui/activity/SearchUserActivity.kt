package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.NormalUserAdapter
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.HomeNewListData
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 搜索用户页面
 */
class SearchUserActivity : BaseActivity() {

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var etSearchUser: EditText
    private lateinit var refreshSearchUser: SmartRefreshLayout
    private lateinit var rvSearchUser: RecyclerView
    lateinit var imm: InputMethodManager
    lateinit var gLoadingHolder: Gloading.Holder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_search_user)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        etSearchUser = findViewById(R.id.et_search_user)
        refreshSearchUser = findViewById(R.id.smart_refresh_search_user)
        rvSearchUser = findViewById(R.id.rv_search_user)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initTitleBar()
        initListener()
        userList = ArrayList()
        gLoadingHolder = Gloading.getDefault().wrap(rvSearchUser)
    }

    private fun initTitleBar() {
        StatusBarUtil.showLightStatusBar(this)
        tvTitle.text = "搜索用户"
        ivTitleBack.setOnClickListener {
            finish()
        }
    }

    fun initListener() {
        refreshSearchUser.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                searchUser()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                searchUser()
            }
        })

        etSearchUser.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    if (imm.isActive) {
                        imm.hideSoftInputFromWindow(
                                v!!.applicationWindowToken, 0)
                    }
                    refreshSearchUser.autoRefresh()
                    return true
                }
                return false
            }
        })
    }

    var page = 0
    lateinit var userList: ArrayList<HomeNewListData.DataBean>
    var userAdapter: NormalUserAdapter? = null
    fun searchUser() {
        HttpHelper.getInstance().searchUser(etSearchUser.text.toString(), page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                gLoadingHolder.showLoadSuccess()
                refreshSearchUser.finishLoadMore()
                refreshSearchUser.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, HomeNewListData::class.java)
                tempData?.data?.let {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(it)
                            userAdapter = NormalUserAdapter(this@SearchUserActivity, userList)
                            rvSearchUser.layoutManager = LinearLayoutManager(this@SearchUserActivity)
                            rvSearchUser.adapter = userAdapter
                            userAdapter?.setOnSimpleItemListener { index ->
                                val intent = Intent(this@SearchUserActivity, UserInfoActivity::class.java)
                                intent.putExtra("uid", userList[index].uid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val temp = userList.size
                            userList.addAll(it)
                            userAdapter?.notifyItemRangeInserted(temp, it.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshSearchUser.finishRefresh()
                refreshSearchUser.finishLoadMore()
                if (code == 4001) {
                    refreshSearchUser.finishLoadMoreWithNoMoreData()
                    if (page == 0) {
                        gLoadingHolder.showEmpty()
                    }
                }
            }
        })
    }
}