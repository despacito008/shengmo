package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.bean.ChatAnchorBean
import com.aiwujie.shengmo.bean.LinkStatuskMolde
import com.aiwujie.shengmo.bean.LiveChatSettingBean
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.adapter.FeeLinkUserAdapter
import com.aiwujie.shengmo.kt.ui.activity.LiveChatSettingActivity
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.TextViewUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener


class FeeLinkUserFragment : NormalListFragment() {
    override fun loadData() {
        sexScreen = SpKey.LINK_SCREEN.getSpValue("0")
        sortType = SpKey.LINK_SORT.getSpValue("1")
        gLoadHolder.showLoading()
        getChatList()
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        frameLayout = rootView.findViewById(R.id.fl_normal_list)

        initTopLine()
        anchorList = ArrayList()
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 0
                getChatList()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                page++
                getChatList()
            }
        })
    }

    lateinit var anchorList: ArrayList<ChatAnchorBean.DataBean>
    private var feeLinkUserAdapter: FeeLinkUserAdapter? = null
    private var page = 0
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private var sexScreen = "0"
    private var sortType = "0"
    private fun getChatList() {
        HttpHelper.getInstance().getChatAnchorList(sexScreen, sortType, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, ChatAnchorBean::class.java)
                showLoadComplete()
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            anchorList.clear()
                            anchorList.addAll(this)
                            feeLinkUserAdapter = FeeLinkUserAdapter(activity, anchorList)
                            with(rvSearchResult) {
                                adapter = feeLinkUserAdapter
                                layoutManager = GridLayoutManager(activity, 2)
                                if (itemDecoration == null) {
                                    itemDecoration = GridItemDecoration(20)
                                    addItemDecoration(itemDecoration)
                                }
                            }
                            feeLinkUserAdapter?.itemListener = OnSimpleItemListener { index ->
                                val intent = Intent(activity, UserInfoActivity::class.java)
                                intent.putExtra("uid", anchorList[index].uid)
                                startActivity(intent)
                            }
                        }
                        else -> {
                            val tempIndex = anchorList.size
                            anchorList.addAll(this)
                            feeLinkUserAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                showLoadError(code != 4000, page == 0, msg)
            }
        })
    }

    var tvAll: TextView? = null
    var tvBoy: TextView? = null
    var tvGirl: TextView? = null
    var tvCdt: TextView? = null
    var tvTime: TextView? = null
    var tvTimes: TextView? = null
    var tvBeans: TextView? = null
    private lateinit var tvLinkSettingSatus: TextView
    private lateinit var  rlLinkSetting: RelativeLayout

    fun showMenu() {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.app_layout_fee_link_screen, null)
        val mPopWindow = PopupWindow(contentView)
        with(mPopWindow) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            setOnDismissListener { activity.alphaBackground(1f) }
        }
        activity.alphaBackground(0.5f)
        tvAll = contentView.findViewById(R.id.tv_link_screen_all)
        tvBoy = contentView.findViewById(R.id.tv_link_screen_boy)
        tvGirl = contentView.findViewById(R.id.tv_link_screen_girl)
        tvCdt = contentView.findViewById(R.id.tv_link_screen_cdt)
        tvTime = contentView.findViewById(R.id.tv_link_screen_time)
        tvTimes = contentView.findViewById(R.id.tv_link_screen_times)
        tvBeans = contentView.findViewById(R.id.tv_link_screen_beans)
        tvLinkSettingSatus = contentView.findViewById(R.id.tv_link_setting_status)
        rlLinkSetting = contentView.findViewById(R.id.ll_link_setting)
        getLinkStatus()
//        if (!TextUtils.isEmpty(linkSatus)) {
//           if ("1" == linkSatus){
//               tvLinkSettingSatus.text ="已开启"
//               tvLinkSettingSatus.setTextColor(ContextCompat.getColor(activity,R.color.red))
//           }else {
//               tvLinkSettingSatus.text ="未开启"
//               tvLinkSettingSatus.setTextColor(ContextCompat.getColor(activity,R.color.link_status))
//           }
//        }
        rlLinkSetting.setOnClickListener {
            startActivity(Intent(activity,LiveChatSettingActivity::class.java))
            mPopWindow.dismiss()
        }

        refreshMenuView()

        tvTime?.clickDelay {
            changeSort("1")
            mPopWindow.dismiss()
        }
        tvTimes?.clickDelay {
            changeSort("2")
            mPopWindow.dismiss()
        }
        tvBeans?.clickDelay {
            changeSort("3")
            mPopWindow.dismiss()
        }

        tvAll?.setOnClickListener {
            changeSex("0")
            mPopWindow.dismiss()
        }

        tvBoy?.setOnClickListener {
            changeSex("1")
            mPopWindow.dismiss()
        }

        tvGirl?.setOnClickListener {
            changeSex("2")
            mPopWindow.dismiss()
        }

        tvCdt?.setOnClickListener {
            changeSex("3")
            mPopWindow.dismiss()
        }
        mPopWindow.showAsDropDown(topBtn)
    }

    private fun changeSex(sex: String) {
        if (sexScreen != sex) {
            sexScreen = sex
            page = 0
            getChatList()
            SpKey.LINK_SCREEN.saveSpValue(sexScreen)
            refreshMenuView()
        }
    }

    private fun changeSort(sort: String) {
        if (sortType != sort) {
            sortType = sort
            page = 0
            getChatList()
            SpKey.LINK_SORT.saveSpValue(sexScreen)
            refreshMenuView()
        }
    }

    private fun refreshMenuView() {
        resetMenuView(tvAll)
        resetMenuView(tvBoy)
        resetMenuView(tvGirl)
        resetMenuView(tvCdt)
        resetMenuView(tvTime)
        resetMenuView(tvTimes)
        resetMenuView(tvBeans)
        when (sexScreen) {
            "1" -> {
                tvBoy?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
            "2" -> {
                tvGirl?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
            "3" -> {
                tvCdt?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
            else -> {
                tvAll?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
        }

        when (sortType) {
            "1" -> {
                tvTime?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
            "2" -> {
                tvTimes?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
            else -> {
                tvBeans?.setTextColor(ContextCompat.getColor(activity, R.color.purple_main))
            }
        }
    }

    private fun resetMenuView(textView: TextView?) {
        textView?.setTextColor(ContextCompat.getColor(activity, R.color.normalGray))
    }


    lateinit var topBtn: View
    lateinit var frameLayout: FrameLayout
    private fun initTopLine() {
        topBtn = ImageButton(activity)
        val params = FrameLayout.LayoutParams(300.dp, 1.dp)
        params.gravity = Gravity.TOP or Gravity.END
        topBtn.layoutParams = params
        frameLayout.addView(topBtn)
    }


    private var linkSatus = ""
    private fun getLinkStatus() {
        HttpHelper.getInstance().getLiveChat(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, LiveChatSettingBean::class.java)
                tempData?.data?.run {
                    when (chat_status) {
                        "0" -> {
                            tvLinkSettingSatus.text ="未开启"
                            tvLinkSettingSatus.setTextColor(ContextCompat.getColor(activity,R.color.normalGray))
                        }
                        "1" -> {
                            tvLinkSettingSatus.text ="已开启"
                            tvLinkSettingSatus.setTextColor(ContextCompat.getColor(activity,R.color.red))
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }
}