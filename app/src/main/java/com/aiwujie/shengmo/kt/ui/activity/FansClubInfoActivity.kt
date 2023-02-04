package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.Group
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.ClubMemberListBean
import com.aiwujie.shengmo.bean.FanClubInfoBean
import com.aiwujie.shengmo.kt.adapter.FansClubMemberAdapter
import com.aiwujie.shengmo.kt.bean.NormalMenuItem
import com.aiwujie.shengmo.kt.ui.view.GroupBuyTicketPop
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.NormalEditPop
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import java.sql.Ref

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: FansClubInfoActivity
 * @Author: xmf
 * @CreateDate: 2022/5/31 16:05
 * @Description:
 */
class FansClubInfoActivity : BaseActivity() {

    private val refreshLayout: SmartRefreshLayout by lazy { findViewById<SmartRefreshLayout>(R.id.refresh_fans_club) }
    private val rvClub: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_fans_club) }

    private val ivBack: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val ivMore: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_more) }
    private val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val tvMore: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_more) }
    private val tvJoinClub: TextView by lazy { findViewById<TextView>(R.id.tv_join_club) }


    companion object {
        fun start(context: Context, uid: String) {
            val intent = Intent(context, FansClubInfoActivity::class.java)
            intent.putExtra(IntentKey.UID, uid)
            context.startActivity(intent)
        }
    }

    var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_fans_club_info)
        StatusBarUtil.showLightStatusBar(this)
        uid = intent.getStringExtra(IntentKey.UID)
        initFansClubHeader()
        getFansClubData()
        tvTitle.text = "粉丝团详情"
        ivMore.visibility = View.GONE
        tvMore.visibility = View.VISIBLE
        tvMore.text = "设置"
        initListener()
    }

    private fun initListener() {
        ivBack.setOnClickListener {
            finish()
        }
        tvMore.setOnClickListener {
            if (MyApp.uid == uid) {
                showOwnerMenu()
            } else {
                showMemberMenu()
            }
        }
        tvJoinClub.setOnClickListener {
            showJoinFansGroupPop()
        }

        tvClubRank.setOnClickListener {
            FansClubRankActivity.start(this)
        }

        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getClubMemberList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getClubMemberList()
            }
        })
    }

    var mHideCardStatus = -1
    var mFansClubMark  = ""
    private fun getFansClubData() {
        HttpHelper.getInstance().getFanClubInfo(uid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, FanClubInfoBean::class.java)
                tempData?.data?.run {
                    mHideCardStatus = is_hidden_fanclubcard.toInt()
                    ImageLoader.loadImage(this@FansClubInfoActivity, head_pic, civIcon)
                    tvClubName.text = fanclub_name
                    tvClubMark.text = "团名:$fanclub_card"
                    tvClubRank.text = "排名 $fanclub_rank"
                    tvClubNum.text = "粉丝团成员:${member}"
                    mFansClubMark = fanclub_card
                    mGroupId = gid
                    if (is_add_fanclub != 1) {
                        tvJoinClub.visibility = View.VISIBLE
                        tvMore.visibility = View.INVISIBLE
                        viewInfo.visibility = View.GONE
                    } else {
                        tvJoinClub.visibility = View.GONE
                        tvMore.visibility = View.VISIBLE
                        if (MyApp.uid != uid) {
                            viewInfo.visibility = View.VISIBLE
                        }
                    }
                    tvItemRank.text = "$member_rank"
                }
                getClubMemberList()
            }

            override fun onFail(code: Int, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                msg?.showToast()
            }
        })
    }

    lateinit var headerView: View
    lateinit var civIcon: ImageView
    lateinit var tvClubName: TextView
    lateinit var tvClubMark: TextView
    lateinit var tvClubNum: TextView
    lateinit var tvClubRank: TextView
    lateinit var groupClub: Group
    lateinit var viewInfo:View

    lateinit var tvItemRank:TextView
    lateinit var llItemCard:View
    lateinit var ivItemIcon:ImageView
    lateinit var tvItemMark:TextView
    lateinit var tvItemLevel:TextView
    lateinit var tvItemName:TextView

    private fun initFansClubHeader() {
        headerView = View.inflate(this, R.layout.app_layout_fans_club_info, null)
        groupClub = headerView.findViewById(R.id.group_club_info)
        civIcon = headerView.findViewById(R.id.civ_fans_club_icon)
        tvClubName = headerView.findViewById(R.id.tv_fans_club_name)
        tvClubMark = headerView.findViewById(R.id.tv_fans_club_mark)
        tvClubNum = headerView.findViewById(R.id.tv_fans_club_num)
        tvClubRank = headerView.findViewById(R.id.tv_fans_club_rank)
        viewInfo = headerView.findViewById(R.id.ll_item_fans_club_member)
        val viewMyClub: View = headerView.findViewById(R.id.cl_my_fans_club)
        viewMyClub.setOnClickListener {
            UserInfoActivity.start(this,uid)
        }

        ivItemIcon = headerView.findViewById(R.id.civ_item_icon)
        tvItemName = headerView.findViewById(R.id.tv_item_name)
        tvItemMark = headerView.findViewById(R.id.tv_item_fans_name)
        tvItemLevel = headerView.findViewById(R.id.tv_item_fans_level)
        tvItemRank = headerView.findViewById(R.id.tv_item_rank)
        llItemCard = headerView.findViewById(R.id.ll_item_club_card)
        llItemCard.visibility = View.GONE
    }



    var page = 0
    private val memberList: ArrayList<ClubMemberListBean.DataBean> by lazy { ArrayList<ClubMemberListBean.DataBean>() }
    private var memberAdapter: FansClubMemberAdapter? = null
    private fun getClubMemberList() {
        HttpHelper.getInstance().getFanClubMemberList(uid, page, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data, ClubMemberListBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            memberList.clear()
                            memberList.addAll(this)
                            memberAdapter = FansClubMemberAdapter(this@FansClubInfoActivity, memberList)
                            val headerViewAdapter = HeaderViewAdapter(memberAdapter)
                            headerViewAdapter.addHeaderView(headerView)
                            with(rvClub) {
                                adapter = headerViewAdapter
                                layoutManager = HeaderViewGridLayoutManager(context, 1, headerViewAdapter)
                            }
                            memberAdapter?.onSimpleItemListener = OnSimpleItemListener {
                                UserInfoActivity.start(this@FansClubInfoActivity, memberList[it].uid)
                            }
                        }
                        else -> {
                            val tempIndex = memberList.size
                            memberList.addAll(this)
                            memberAdapter?.notifyItemRangeInserted(tempIndex, this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    lateinit var menuPop: NormalMenuPopup
    private fun showMemberMenu() {
        val itemList = ArrayList<NormalMenuItem>()
        if (mHideCardStatus == 0) {
            itemList.add(NormalMenuItem(0, "直播间隐藏粉丝徽章"))
        } else if (mHideCardStatus == 1) {
            itemList.add(NormalMenuItem(0, "直播间显示粉丝徽章"))
        }
        menuPop = NormalMenuPopup(this, itemList)
        menuPop.setOnSimpleItemListener(OnSimpleItemListener {
            when (it) {
                0 -> {
                    changeFansClub()
                }
            }
        })
        menuPop.showPopupWindow()
    }

    private fun showOwnerMenu() {
        val itemList = ArrayList<NormalMenuItem>()
        itemList.add(NormalMenuItem(0, "修改粉丝勋章名称"))
        menuPop = NormalMenuPopup(this, itemList)
        menuPop.setOnSimpleItemListener(OnSimpleItemListener {
            when (it) {
                0 -> {
                    showSetCardPop()
                    menuPop.dismiss()
                }
            }
        })
        menuPop.showPopupWindow()
    }

    private fun changeFansClub() {
        HttpHelper.getInstance().showOrHideFansClub(mGroupId,object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                mHideCardStatus = if (mHideCardStatus == 0) 1 else 0
                if (menuPop.isShowing) {
                    menuPop.dismiss()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
    var mGroupId = ""
    private fun showJoinFansGroupPop() {
        val buyTickPop = GroupBuyTicketPop(this,mGroupId,"")
        buyTickPop.showPopupWindow()
        buyTickPop.onTicketPopListener = object : GroupBuyTicketPop.OnTicketPopListener {
            override fun doPopBuySuc() {
                getFansClubData()
            }

            override fun doPopBuyFail(msg: String?) {
                msg?.showToast()
            }

            override fun doPopDismiss() {

            }

        }
    }

    lateinit var editPop:NormalEditPop
    private fun showSetCardPop() {
        editPop = NormalEditPop.Builder(this)
                .setInfo(mFansClubMark)
                .setTitle("设置勋章名称")
                .build()
        editPop.showPopupWindow()
        editPop.setOnPopClickListener(object :NormalEditPop.OnPopClickListener {
            override fun cancelClick() {
                editPop.dismiss()
            }

            override fun confirmClick(edit: String?) {
                if (edit.toString().isEmpty() || edit.toString().length > 3) {
                    "勋章名字为一到三个字".showToast()
                    return
                }
                changeFansClubName(edit.toString())
            }
        })
    }

    private fun changeFansClubName(name:String) {
        HttpHelper.getInstance().changeFansClubName(name,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                if (editPop.isShowing) {
                    editPop.dismiss()
                }
                getFansClubData()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

}
