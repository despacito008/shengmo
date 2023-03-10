package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.LabelDefaultsActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.UserGroupingFriendAdapter
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.FenzuListData
import com.aiwujie.shengmo.kt.ui.activity.statistical.GroupingAddMemberActivity
import com.aiwujie.shengmo.kt.util.showText
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView

class GroupingSettingActivity : BaseActivity() {
    private lateinit var ivNormalTitleBack: ImageView
    private lateinit var tvNormalTitleTitle: TextView
    private lateinit var ivNormalTitleMore: ImageView
    private lateinit var tvNormalTitleMore: TextView
    private lateinit var llAddLabel: LinearLayout
    private lateinit var rvLabel: SwipeMenuRecyclerView
    private lateinit var etName: EditText
    private lateinit var refreshGroup:SmartRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_grouping_setting)
        StatusBarUtil.showLightStatusBar(this)
        ivNormalTitleBack = findViewById(R.id.iv_normal_title_back)
        tvNormalTitleTitle = findViewById(R.id.tv_normal_title_title)
        tvNormalTitleMore = findViewById(R.id.tv_normal_title_more)
        ivNormalTitleMore = findViewById(R.id.iv_normal_title_more)
        tvNormalTitleTitle.text = "????????????"
        tvNormalTitleMore.text = "??????"
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        ivNormalTitleBack.setOnClickListener {
            finish()
        }
        llAddLabel = findViewById(R.id.ll_add_member)
        rvLabel = findViewById(R.id.rv_grouping_member)
        initRecyclerView()
        gid = intent.getStringExtra("fgid") ?: ""
        memberIdList = ArrayList()
        userList = ArrayList()
        getGroupingMember()
        llAddLabel.setOnClickListener {
            val intent = Intent(this, GroupingAddMemberActivity::class.java)
            intent.putStringArrayListExtra("memberList", memberIdList)
            intent.putExtra("fgid",gid)
            startActivity(intent)
        }
        etName = findViewById(R.id.et_grouping_name)
        etName.showText(intent.getStringExtra("groupname")?:"")

        tvNormalTitleMore.setOnClickListener {
            updateGroupingName()
        }

        refreshGroup = findViewById(R.id.refresh_grouping_member)
        refreshGroup.setEnableLoadMore(false)
        refreshGroup.setOnRefreshListener {
            page = -1
            getGroupingMember()
        }
    }

    private fun initRecyclerView() {
        // 3. ??????????????????
        val swipeMenuCreator = SwipeMenuCreator { _, swipeRightMenu, _ ->
            val i = LabelDefaultsActivity.dp2px(75f)
            val deleteItem = SwipeMenuItem(this)
                    .setBackgroundColor(ContextCompat.getColor(this,R.color.red2)) // ????????????
                    .setText("??????") // ?????????
                    .setTextColor(Color.WHITE) // ???????????????
                    .setTextSize(16) // ???????????????
                    .setWidth(i) // ???
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT) //??????MATCH_PARENT??????Item???????????????????????? ?????????????????????
            swipeRightMenu.addMenuItem(deleteItem) // ???????????????????????????????????????
        }
        rvLabel.setSwipeMenuCreator(swipeMenuCreator)
        rvLabel.setSwipeMenuItemClickListener {
            it.closeMenu()
            showDelMemberTip(it.adapterPosition)
        }
    }

    private fun showDelMemberTip(index: Int) {
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setMessage("????????????${userList[index].nickname}?")
                .setPositiveButton("???") { dialog, _ ->
                    dialog.dismiss()
                }.setNegativeButton("???") { dialog, _ ->
                    delGroupingMember(index)
                    dialog.dismiss()
                }.create().show()
    }

    private fun delGroupingMember(index:Int) {
        HttpHelper.getInstance().delGroupingMember(gid,userList[index].uid,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                userList.removeAt(index)
                userFansAdapter?.notifyItemRemoved(index)
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    var gid = ""
    var page = -1
    lateinit var userList: ArrayList<FenzuListData.DataBean>
    var userFansAdapter: UserGroupingFriendAdapter? = null
    lateinit var memberIdList: ArrayList<String>
    private fun getGroupingMember() {
        HttpHelper.getInstance().getGroupingFriendList(gid, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, FenzuListData::class.java)
                tempData?.data?.run {
                    when (page) {
                        -1 -> {
                            userList.clear()
                            userList.addAll(this)
                            userFansAdapter = UserGroupingFriendAdapter(this@GroupingSettingActivity, userList, "", false)
                            with(rvLabel) {
                                adapter = userFansAdapter
                                layoutManager = LinearLayoutManager(this@GroupingSettingActivity)
                            }
                            memberIdList.addAll(userList.map { it.uid })
                            userFansAdapter?.setOnSimpleItemListener {
                                index ->
                                val intent = Intent(this@GroupingSettingActivity,UserInfoActivity::class.java)
                                intent.putExtra("uid",userList[index].uid)
                                startActivity(intent)
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

            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        getGroupingMember()
    }

    private fun updateGroupingName() {
        if (etName.text.length < 0 || etName.text.length > 4) {
            "???????????????1???4????????????".showToast()
            return
        }
        HttpHelper.getInstance().updateGroupingName(gid,etName.text.toString(),object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
}