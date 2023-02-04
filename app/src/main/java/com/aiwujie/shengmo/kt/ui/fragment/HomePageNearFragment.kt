package com.aiwujie.shengmo.kt.ui.fragment

import android.Manifest
import android.app.AppOpsManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.AppOpsManagerCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.HomePageUserGridAdapter
import com.aiwujie.shengmo.adapter.HomePageUserListAdapter
import com.aiwujie.shengmo.bean.FilterData
import com.aiwujie.shengmo.bean.HomeNewListData
import com.aiwujie.shengmo.bean.HomePageUserScreen
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.PermissionHelper
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class HomePageNearFragment : NormalListFragment(){
    enum class LayOutType {
        List, Grid
    }


    override fun loadData() {
        if (arguments.getInt("type") == 1) {
            requestPermission()
        } else {
            rvSearchResult.visibility = View.VISIBLE
            rlLocation.visibility = View.GONE
            gLoadHolder.showLoading()
            getUserList()
        }
//        checkPermisssion()
//        gLoadHolder.showLoading()
//        getUserList()
    }


    companion object {
        val perms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        fun newInstance(type: Int): HomePageNearFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = HomePageNearFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var type = 0
    lateinit var userList: ArrayList<HomeNewListData.DataBean>
    var userAdapter: HomePageUserListAdapter? = null
    var gridAdapter: HomePageUserGridAdapter? = null
    var layout = LayOutType.List
    lateinit var tvOpen: TextView
     var permissionsList:MutableList<String>? =null
    var finalToastStr ="";
    override fun initView(rootView: View) {
        super.initView(rootView)
        EventBus.getDefault().register(this)
        if (arguments.getInt("type") == 1){
            addView()
        }
        userList = ArrayList()
        initScreenData()
        type = arguments.getInt("type")
        val modle = SpKey.MODLE.getSpValue("0")
        layout = if (modle == "1") LayOutType.Grid else LayOutType.List
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getUserList()
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getUserList()
            }
        })

    }

    fun   addView(){
        val  view =LayoutInflater.from(context).inflate(R.layout.layout_unopen_location,null)
        tvOpen = view.findViewById(R.id.tv_open)
        val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.CENTER_IN_PARENT , RelativeLayout.TRUE);
        view.layoutParams =lp
        rlLocation.addView(view);//RelativeLayout添加子View

        tvOpen.setOnClickListener {
            showPermissionTipDialog(finalToastStr, permissionsList)
        }
    }



    private fun requestPermission() {
        XXPermissions.with(this)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {

                        if (all) {
                            refreshLayout.setEnableRefresh(true)
                            rvSearchResult.visibility = View.VISIBLE
                            rlLocation.visibility = View.GONE
                            gLoadHolder.showLoading()
                            getUserList()
                        } else {
                            refreshLayout.setEnableRefresh(false)
                            rvSearchResult.visibility = View.GONE
                            rlLocation.visibility = View.VISIBLE
                        }
                    }


                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        permissionsList =  permissions
                        refreshLayout.setEnableRefresh(false)
                        rvSearchResult.visibility = View.GONE
                        rlLocation.visibility = View.VISIBLE
                        //super.onDenied(permissions, never)
                        var toastStr = StringBuilder().append("权限:")
                        with(toastStr) {
                            if (permissions?.contains(Manifest.permission.ACCESS_COARSE_LOCATION) == true && permissions?.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                append("使用定位 ")
                            }
                            append("被拒绝")
                        }
                        finalToastStr = toastStr.toString()
                        if (never) {
//                            toastStr.append("，请手动授予权限")
//                            showPermissionTipDialog(toastStr.toString(), permissions)
                        } else {
                            toastStr.append("，无法使用功能")
                            toastStr.toString().showToast()
                        }
                    }
                })
    }

    private fun showPermissionTipDialog(info: String, permissions: MutableList<String>?) {
        var builder = AlertDialog.Builder(context)
        builder.setTitle("提示")
        builder.setMessage(info)
        builder.setPositiveButton("去授予") { dialog, _ ->
            dialog.dismiss()
            XXPermissions.startPermissionActivity(this, permissions)
        }
        builder.setNegativeButton("取消") { dialog, _ ->
            dialog.dismiss()
        }
        var dialog: AlertDialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            XXPermissions.REQUEST_CODE -> {
                if (XXPermissions.isGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                        XXPermissions.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    "权限授予成功，可以使用定位功能了".showToast()
                    refreshLayout.setEnableRefresh(true)
                    rvSearchResult.visibility = View.VISIBLE
                    rlLocation.visibility = View.GONE
                    gLoadHolder.showLoading()
                    getUserList()
                }
            }
        }

    }

    private lateinit var userScreen: HomePageUserScreen
    private fun initScreenData() {
        val onlinestate = SharedPreferencesUtils.getParam(activity.applicationContext, "filterLine", "") as String
        val realname = SharedPreferencesUtils.getParam(activity.applicationContext, "filterAuthen", "") as String
        val age = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpAge", "") as String
        val sex = SharedPreferencesUtils.getParam(activity.applicationContext, "filterSex", "") as String
        val sexual = SharedPreferencesUtils.getParam(activity.applicationContext, "filterQx", "") as String
        val role = SharedPreferencesUtils.getParam(activity.applicationContext, "filterRole", "") as String
        val culture = SharedPreferencesUtils.getParam(activity.applicationContext, "filterCulture", "") as String
        val monthly = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpMoney", "") as String
        val upxzya = SharedPreferencesUtils.getParam(activity.applicationContext, "filterUpxzya", "") as String
        userScreen = HomePageUserScreen()
        with(userScreen) {
            this.age = age
            this.sex = sex
            this.sexual = sexual
            this.role = role
            salary = monthly
            auth = realname
            online = onlinestate
            edu = culture
            want = upxzya
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }



    var page = 0
    private fun getUserList() {
        HttpHelper.getInstance().getHomepageUser(type, page, userScreen, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                gLoadHolder.showLoadSuccess()
                val tempIndex = GsonUtil.GsonToBean(data, HomeNewListData::class.java)
                tempIndex?.data?.run {
                    when (page) {
                        0 -> {
                            userList.clear()
                            userList.addAll(this)
                            when (layout) {
                                LayOutType.List -> {
                                    userAdapter = HomePageUserListAdapter(activity, userList, type == 7)
                                    with(rvSearchResult) {
                                        adapter = userAdapter
                                        layoutManager = LinearLayoutManager(activity)
                                    }
                                    userAdapter?.setOnSimpleItemListener { index ->
                                        gotoUserInfoPage(userList[index].uid)
                                    }
                                }
                                LayOutType.Grid -> {
                                    gridAdapter = HomePageUserGridAdapter(activity, userList)
                                    with(rvSearchResult) {
                                        adapter = gridAdapter
                                        layoutManager = GridLayoutManager(activity, 3)
                                    }
                                    gridAdapter?.setOnSimpleItemListener { index ->
                                        gotoUserInfoPage(userList[index].uid)
                                    }
                                }
                            }

                        }
                        else -> {
                            val tempSize = userList.size
                            userList.addAll(this)
                            when (layout) {
                                LayOutType.List -> {
                                    userAdapter?.notifyItemRangeInserted(tempSize, this.size)
                                }
                                LayOutType.Grid -> {
                                    gridAdapter?.notifyItemRangeInserted(tempSize, this.size)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
                if (code == 4001 && page == 0) {
                    gLoadHolder.showEmpty()
                    //检测用户是否将当前应用的定位权限拒绝
                    val checkResult = PermissionHelper.checkOp(context, 2, AppOpsManager.OPSTR_FINE_LOCATION) //其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
                    val checkResult2 = PermissionHelper.checkOp(context, 1, AppOpsManager.OPSTR_FINE_LOCATION)
                    if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
                        val permissionHelper = PermissionHelper(context)
                        permissionHelper.showLocIgnoredDialog(context)
                    } else {
                        if (!EasyPermissions.hasPermissions(context, *perms)) { //检查是否获取该权限
                            //第二个参数是被拒绝后再次申请该权限的解释
                            //第三个参数是请求码
                            //第四个参数是要申请的权限
                            EasyPermissions.requestPermissions(activity, "需要开启位置权限，以便正常使用全部功能（如您想隐藏位置，可在APP我的-设置-隐私中关闭位置）", 0, *perms)
                        }
                    }
                } else {
                    gLoadHolder.showLoadSuccess()
                }
            }
        })

    }

    var perms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: SharedprefrenceEvent) {
        with(userScreen) {
            this.age = event.age
            this.sex = event.sex
            this.sexual = event.sexual
            this.role = event.role
            salary = event.monthly
            auth = event.realname
            online = event.onlinestate
            edu = event.culture
            want = event.upxzya
        }
        page = 0
        getUserList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: ChangeLayoutEvent) {
        layout = if (event.falg == 1) {
            LayOutType.Grid
        } else {
            LayOutType.List
        }
        page = 0
        getUserList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(data: FilterData) {
        initScreenData()
        getUserList()
    }


    fun gotoUserInfoPage(uid: String) {
        UserInfoActivity.start(activity, uid)
    }
}