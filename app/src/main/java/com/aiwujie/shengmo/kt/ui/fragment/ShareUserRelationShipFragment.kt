package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.UserRelationShipAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.GzFsHyListviewData
import com.aiwujie.shengmo.bean.VipAndVolunteerData
import com.aiwujie.shengmo.customview.BindSvipDialog
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.tim.bean.DynamicMessageBean
import com.aiwujie.shengmo.tim.bean.LiveAnchorMessageBean
import com.aiwujie.shengmo.tim.helper.MessageHelper
import com.aiwujie.shengmo.tim.helper.MessageSendHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.facebook.drawee.drawable.FadeDrawable
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack

class ShareUserRelationShipFragment:LazyFragment() {
    lateinit var etSearch:EditText
    lateinit var tvRule:TextView
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var rvUser:RecyclerView
    var keyWord = ""
    var type = 0
    lateinit var loadingHolder:Gloading.Holder
    private lateinit var imm: InputMethodManager
    override fun loadData() {
        loadingHolder.showLoading()
        getUserState()
        getUserFans()
    }

    companion object {
        fun newInstance(type: Int):ShareUserRelationShipFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = ShareUserRelationShipFragment()
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
        etSearch.visibility = View.VISIBLE
        tvRule.visibility = View.VISIBLE
        if (type == 0) {
            tvRule.visibility  = View.GONE
        }
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getUserFans()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getUserFans()
            }
        })
        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
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
                    return true
                }
                return false
            }
        })
        initData()
    }


    var page = 0
    private fun getUserFans() {
        HttpHelper.getInstance().getSocialUser(MyApp.uid, keyWord, type, page, object : HttpCodeMsgListener {
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
                            userFansAdapter = UserRelationShipAdapter(activity, userList, keyWord, false)
                            with(rvUser) {
                                adapter = userFansAdapter
                                layoutManager = LinearLayoutManager(activity)
                            }
                            userFansAdapter?.setOnSimpleItemListener { index ->
                                showShareUserTip(userList[index].userInfo)
                            }
                        }
                        else -> {
                            val tempIndex = userList.size
                            userList.addAll(this)
                            userFansAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                if (code == 4001 && page == 0) {
                    loadingHolder.showEmpty()
                } else {
                    loadingHolder.showLoadSuccess()
                }
            }
        })
    }
    var vip = 0
    private fun getUserState() {
        HttpHelper.getInstance().getMyOwnInfo(object : HttpListener {
            override fun onSuccess(data: String?) {
                val vipAndVolunteerData = GsonUtil.GsonToBean(data, VipAndVolunteerData::class.java)
                vip = vipAndVolunteerData.data.svip.toInt()
            }

            override fun onFail(msg: String?) {

            }
        })
    }

    fun showShareUserTip(user: GzFsHyListviewData.DataBean.UserInfoBean) {
        AlertDialog.Builder(activity).apply {
            setTitle("提示")
            when (sType) {
                1 -> setMessage("分享动态给${user.nickname}?")
                2 -> setMessage("分享同好给${user.nickname}?")
                4 -> setMessage("分享直播给${user.nickname}?")
            }
            setPositiveButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton("确认") { dialog, _ ->
                dialog.dismiss()
                share(user.uid)
            }
            show()
        }
    }

    var uid:String = ""
    //var did:String = ""
    var sType = 0
    var pic = ""
    var content = ""
    fun initData() {
        activity?.intent?.run {
            sType = getIntExtra("leixing", 0)
            uid = getStringExtra("id")?:""
            //did = getStringExtra("did")?:""
            content = getStringExtra("content")?:""
            pic = getStringExtra("pic")?:""
        }
    }

    private fun share(uid: String) {
        if ((type == 0 || type == 1) && vip == 0) {
            BindSvipDialog.bindAlertDialog(activity, "SVIP可分享至关注")
            return
        }
        when (sType) {
            1, 2 -> shareUser(uid)
            4 -> shareLive(uid)
        }
    }

    private fun shareUser(userId: String) {
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
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, false, userId, object : IUIKitCallBack {
            override fun onSuccess(data: Any) {
                "分享成功".showToast()
                activity.finish()
            }

            override fun onError(module: String, errCode: Int, errMsg: String) {
                "分享失败 $errMsg".showToast()
            }
        })
    }

    private fun shareLive(userId: String) {
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
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, false, userId, object : IUIKitCallBack {
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