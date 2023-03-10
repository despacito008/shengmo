package com.aiwujie.shengmo.kt.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.LabelDefaultsActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.FriendGroupListBean
import com.aiwujie.shengmo.bean.GroupingConfigBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.recycleradapter.LabellistAdapter2
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.hb.dialog.myDialog.MyAlertInputDialog
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.app_activity_group_member.*
import kotlinx.android.synthetic.main.app_activity_grouping_detail.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

class GroupingDetailActivity:BaseActivity() {
    private lateinit var ivNormalTitleBack: ImageView
    private lateinit var tvNormalTitleTitle: TextView
    private lateinit var ivNormalTitleMore: ImageView
    private lateinit var tvNormalTitleMore: TextView
    private lateinit var llAddLabel:LinearLayout
    private lateinit var rvLabel: SwipeMenuRecyclerView
    private val cbShow:CheckBox by lazy { findViewById<CheckBox>(R.id.cb_grouping_show) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_grouping_detail)
        StatusBarUtil.showLightStatusBar(this)
        ivNormalTitleBack = findViewById(R.id.iv_normal_title_back)
        tvNormalTitleTitle = findViewById(R.id.tv_normal_title_title)
        tvNormalTitleMore = findViewById(R.id.tv_normal_title_more)
        ivNormalTitleMore = findViewById(R.id.iv_normal_title_more)
        tvNormalTitleTitle.text  = "????????????"
        ivNormalTitleBack.setOnClickListener {
            finish()
        }
        llAddLabel = findViewById(R.id.ll_add_label)
        rvLabel = findViewById(R.id.rv_grouping_label)
        labelList = ArrayList()
        initRecyclerView()
        getGroupingLabel()
        getGroupingConfig()
        llAddLabel.setOnClickListener {
            showAddGroupingDialog()
        }
        cbShow.setOnCheckedChangeListener { _, isChecked ->
            if (cbShow.isPressed) {
                changeGroupingConfig(isChecked)
            }
        }
    }
    var labelAdapter: LabellistAdapter2? = null
    lateinit var labelList:ArrayList<FriendGroupListBean.DataBean>
    private fun getGroupingLabel() {
        HttpHelper.getInstance().getFriendGrouping(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, FriendGroupListBean::class.java)
                tempData?.data?.run {
                    labelList.clear()
                    labelList.addAll(this)
                    labelAdapter = LabellistAdapter2(this@GroupingDetailActivity, labelList)
                    with(rvLabel) {
                        adapter = labelAdapter
                        layoutManager = LinearLayoutManager(this@GroupingDetailActivity)
                    }
//                    labelAdapter?.setOnLabelListener {
//                        showDelGroupingTip(it)
//                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
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
            showDelGroupingTip(it.adapterPosition)
        }
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                //????????????????????? ??????int???????????????????????????
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN //??????
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                //????????????
                Collections.swap(labelList, viewHolder!!.adapterPosition, target!!.adapterPosition)
                labelAdapter?.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                return false
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
                super.clearView(recyclerView, viewHolder)
                val sortStr = StringBuffer()
                labelList.map {
                    it.id
                }.forEach {
                    sortStr.append(it).append(",")
                }
                if (sortStr.isNotEmpty()) {
                    val sort = sortStr.toString().substring(0, sortStr.toString().length)
                    sortGrouping(sort)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(rvLabel)
    }

    var currSort = ""
    fun sortGrouping(sort: String) {
        if (currSort == sort) {
            return
        }
        HttpHelper.getInstance().sortFriendGrouping(sort, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                currSort = sort
                noticeGroupingRefresh()
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun showDelGroupingTip(index: Int) {
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setMessage("????????????${labelList[index].fgname}?")
                .setPositiveButton("???") { dialog, _ ->
                    dialog.dismiss()
                }.setNegativeButton("???") { dialog, _ ->
                    delGrouping(index)
                    dialog.dismiss()
                }.create().show()
    }

    private fun delGrouping(index: Int) {
        HttpHelper.getInstance().delFriendGrouping(labelList[index].id, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                getGroupingLabel()
                noticeGroupingRefresh()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun showAddGroupingDialog() {
        val addDialog = MyAlertInputDialog(this).builder().setTitle("????????????").setEditText("")
        addDialog.setPositiveButton("??????") {
            if (addDialog.result.isNotEmpty()) {
                if (addDialog.result.length > 4) {
                    "??????????????????4??????".showToast()
                } else {
                    addDialog.dismiss()
                    addGrouping(addDialog.result)
                }
            } else {
                "?????????????????????".showToast()
            }
        }
        addDialog.setNegativeButton("??????") { addDialog.dismiss() }
        addDialog.show()
    }

    private fun addGrouping(name: String) {
        HttpHelper.getInstance().addFriendGrouping(name, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                getGroupingLabel()
                noticeGroupingRefresh()
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        getGroupingLabel()
    }

    fun noticeGroupingRefresh() {
        EventBus.getDefault().post("groupingRefresh")
    }

    private fun getGroupingConfig() {
        HttpHelper.getInstance().getGroupingConfig(object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,GroupingConfigBean::class.java)
                tempData?.data?.run {
                    cbShow.isChecked = status == "1"
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun changeGroupingConfig(isShow:Boolean) {
        HttpHelper.getInstance().setGroupingConfig(if (isShow) "1" else "0",object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                noticeGroupingRefresh()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                cbShow.isChecked = !cbShow.isChecked
            }
        })
    }
}