package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.AtGroupMemberData
import com.aiwujie.shengmo.kt.ui.adapter.AtMemberListViewAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback


/**
 * 消息 - 聊天 - @群成员
 */
class AtGroupMemberActivity : BaseActivity() {

    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    lateinit var ivMore: ImageView
    lateinit var etContent: EditText
    lateinit var refreshlayout: SmartRefreshLayout
    lateinit var recycleView: RecyclerView

    lateinit var mList: ArrayList<AtGroupMemberData.DataBean>
    lateinit var memberAdapter: AtMemberListViewAdapter
    private var grouId: String? = null
    private var searchContent: String = ""
    private var page = 0
    lateinit var gLoadingHolder: Gloading.Holder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_at_group_member)
        StatusBarUtil.showLightStatusBar(this)

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivBack = findViewById(R.id.iv_normal_title_back)
        ivMore = findViewById(R.id.iv_normal_title_more)
        etContent = findViewById(R.id.mAtGroup_member_search)
        refreshlayout = findViewById(R.id.refreshLayout)
        recycleView = findViewById(R.id.recycleView)
        gLoadingHolder = Gloading.getDefault().wrap(recycleView)
        ivMore.visibility = View.INVISIBLE
        tvTitle.text = "@群组成员"


        grouId = intent.getStringExtra("groupId")
        mList = ArrayList()
        setListener()

        getData()
    }

    fun setListener() {
        ivBack.setOnClickListener {
            finish()
        }
        refreshlayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getData()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                etContent.setText("")
                mList.clear()
                getData()
            }

        })

        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchContent = s.toString().trim()
                mList.clear()
                page = 0
                getData()
            }
        })

    }


    private fun getData() {
        HttpHelper.getInstance().getGroupMemberList(grouId, searchContent, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshlayout.finishRefresh()
                refreshlayout.finishLoadMore()
                gLoadingHolder.showLoadSuccess()
                val data = GsonUtil.GsonToBean(data, AtGroupMemberData::class.java)
                data?.data?.run {
                    when (page) {
                        0 -> {
                            mList.clear()
                            mList.addAll(this)
                            with(recycleView) {
                                layoutManager = LinearLayoutManager(this@AtGroupMemberActivity)
                                memberAdapter = AtMemberListViewAdapter(this@AtGroupMemberActivity, mList)
                                memberAdapter.setAtGroupClickListener(object : AtMemberListViewAdapter.OnAtGroupClick {
                                    override fun OnClick(position: Int, uid: String, nickName: String, cardName: String, headPic: String) {
                                        sendUser(position)
                                    }
                                })
                                adapter = memberAdapter
                            }
                        }
                        else -> {
                            var tempIndex = mList.size
                            mList.addAll(this)
                            memberAdapter.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

                if (code ==  4001 ){
                    refreshlayout.finishLoadMoreWithNoMoreData()
                    if (page == 0){
                        gLoadingHolder.showEmpty()
                    }
                }
                refreshlayout.finishRefresh()
                refreshlayout.finishLoadMore()

            }
        })


    }



    fun sendUser(position: Int) {
        var uid: String? = null
        var name: String? = null
        var headpic: String? = null

        uid = mList.get(position).uid
        val nickname: String = mList.get(position).nickname
        val cardname: String = mList.get(position).cardname
        name = if ("" != cardname && null != cardname) {
            cardname
        } else {
            nickname
        }
        headpic = mList.get(position).head_pic

        val intent = Intent()
        intent.putExtra("name", name)
        intent.putExtra("id", uid.toString())
        setResult(200, intent)
        finish()
    }











    //    private fun getSearchData(): ArrayList<AtGroupMemberData.DataBean> {
//        mList?.map {
//            if (it.nickname.contains(searchContent.trim()) || it.cardname.contains(searchContent.trim())) {
//                searchList.add(it)
//            }
//        }
//        searchList?.run {
//            when (page) {
//                0 -> {
//                    with(recycleView) {
//                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@AtGroupMemberActivity)
//                        searchMemberAdapter = com.aiwujie.shengmo.kt.ui.adapter.AtMemberListViewAdapter(this@AtGroupMemberActivity, searchList)
//                        searchMemberAdapter.setAtGroupClickListener(object : AtMemberListViewAdapter.OnAtGroupClick {
//                            override fun OnClick(position: Int, uid: String, nickName: String, cardName: String, headPic: String) {
//                                sendUser(position)
//                            }
//                        })
//                        adapter = searchMemberAdapter
//                    }
//                }
//                else -> {
//                    var tempIndex = searchList.size
//                    searchList.addAll(this)
//                    searchMemberAdapter.notifyItemRangeInserted(tempIndex, this.size)
//                }
//
//            }
//
//        }
//        return searchList

//    }

}