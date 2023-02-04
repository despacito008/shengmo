package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.UserRelationShipAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.GzFsHyListviewData
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class RelationShipFansFragment:LazyFragment() {
    lateinit var etSearch:EditText
    lateinit var tvRule:TextView
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var rvUser:RecyclerView
    lateinit var uid:String
    var keyWord = ""
    var type = 0
    lateinit var loadingHolder:Gloading.Holder
    private lateinit var imm: InputMethodManager
    override fun loadData() {
        loadingHolder.showLoading()
        getUserFans()
    }

    companion object {
        fun newInstance(type:Int):RelationShipFansFragment {
            val args = Bundle()
            args.putInt("type",type)
            val fragment = RelationShipFansFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_user_relationship_fans
    }
    lateinit var userList:ArrayList<GzFsHyListviewData.DataBean>
    var userFansAdapter:UserRelationShipAdapter? = null
    override fun initView(rootView: View) {
        uid = activity.intent.getStringExtra("uid")!!
        type = arguments.getInt("type")
        userList = ArrayList()
        imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        rootView.run {
            etSearch = findViewById(R.id.et_user_relationship_search)
            tvRule = findViewById(R.id.tv_user_relationship_rule)
            refreshLayout = findViewById(R.id.smart_refresh_user_relationship)
            rvUser = findViewById(R.id.rv_refresh_user_relationship)
            loadingHolder = Gloading.getDefault().wrap(rvUser)
        }
        if (uid == MyApp.uid) {
            etSearch.visibility = View.VISIBLE
            tvRule.visibility = View.VISIBLE
        } else {
            etSearch.visibility = View.GONE
            tvRule.visibility = View.GONE
        }
        if (type == 0) {
            tvRule.visibility  = View.GONE
        }
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getUserFans()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getUserFans()
            }
        })
        etSearch.setOnEditorActionListener(object :TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    if (imm.isActive) {
                        imm.hideSoftInputFromWindow(
                                v!!.applicationWindowToken, 0)
                    }
                    val s: String = etSearch.text.toString()
                    keyWord = s
                    page = 0
                    loadingHolder.showLoading()
                    getUserFans()
//                    val intent = Intent(context, SearchfriendsActivity::class.java)
//                    intent.putExtra("name", s)
//                    intent.putExtra("type", "1")
//                    startActivity(intent)
//                    etSearch.setText("")
                    return true
                }
                return false
            }
        })
    }
    var page = 0
    private fun getUserFans() {
        HttpHelper.getInstance().getSocialUser(uid,keyWord,type,page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                loadingHolder.showLoadSuccess()
                val tempData = GsonUtil.GsonToBean(data, GzFsHyListviewData::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(this)
                            userFansAdapter = UserRelationShipAdapter(activity,userList,keyWord,uid == MyApp.uid)
                            with(rvUser) {
                                adapter = userFansAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            userFansAdapter?.setOnSimpleItemListener {
                                index ->
                                val intent = Intent(activity,UserInfoActivity::class.java)
                                intent.putExtra("uid",userList[index].uid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val tempIndex = userList.size
                            userList.addAll(this)
                            userFansAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 4001 && page == 0) {
                    loadingHolder.showEmpty()
                }  else {
                    loadingHolder.showLoadSuccess()
                }
            }
        })
    }
}