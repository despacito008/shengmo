package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.FanClubInfoBean
import com.aiwujie.shengmo.bean.JoinFanClubListBean
import com.aiwujie.shengmo.kt.adapter.FansClubListAdapter
import com.aiwujie.shengmo.kt.ui.activity.normallist.NormalHeaderListActivity
import com.aiwujie.shengmo.kt.ui.activity.normallist.NormalListActivity
import com.aiwujie.shengmo.kt.ui.view.RequestBuildFansClubPop
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: FansClubListActivity
 * @Author: xmf
 * @CreateDate: 2022/5/30 19:20
 * @Description: 粉丝团
 */
class FansClubListActivity: NormalHeaderListActivity() {

    companion object {
        fun start(context:Context) {
            val intent = Intent(context,FansClubListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getPageTitle(): String {
        return "我的粉丝团"
    }

    override fun loadData() {
        refreshLayout.setBackgroundColor(Color.parseColor("#11000000"))
        initFansClubHeader()
        getMyFansData()
    }

    override fun initViewComplete() {
        super.initViewComplete()
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        tvNormalTitleMore.text = "规则"
        tvNormalTitleMore.setOnClickListener {
            BannerWebActivity.start(this,SpKey.API_HOST.getSpValue("") + "/Home/Info/agreementinfo/id/13","粉丝团规则")
        }
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                getMyFansData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getFansDataList()
            }
        })
    }

    private var fansAdapter:FansClubListAdapter? = null
    private val clubList:ArrayList<JoinFanClubListBean.DataBean> by lazy { ArrayList<JoinFanClubListBean.DataBean>() }
    var page = 0
    private fun getFansDataList() {
        HttpHelper.getInstance().getFanClubList(page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,JoinFanClubListBean::class.java)
                tempData?.data?.run {
                    when (page) {
                        0 -> {
                            clubList.clear()
                            clubList.addAll(this)
                            fansAdapter = FansClubListAdapter(this@FansClubListActivity,clubList)
                            var headerViewAdapter = HeaderViewAdapter(fansAdapter)
                            headerViewAdapter.addHeaderView(headerView)
                            if (this.size == 0) {
                                headerViewAdapter.addHeaderView(viewEmptyHeader)
                            }
                            with(rvList) {
                                adapter = headerViewAdapter
                                layoutManager = HeaderViewGridLayoutManager(this@FansClubListActivity,1,headerViewAdapter)
                            }
                            fansAdapter?.onSimpleItemListener = OnSimpleItemListener {
                                FansClubInfoActivity.start(this@FansClubListActivity,clubList[it].uid)
                            }
                        }
                        else -> {
                            val tempIndex = clubList.size
                            clubList.addAll(this)
                            fansAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }

                }

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }

        })
    }

    private fun getMyFansData() {
        HttpHelper.getInstance().getFanClubInfo(MyApp.uid,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,FanClubInfoBean::class.java)
                tempData?.data?.run {
                    ImageLoader.loadImage(this@FansClubListActivity,head_pic,civIcon)

                    when (is_anchor) {
                        "0" -> {
                            tvMyClub.visibility = View.GONE
                            clMyClub.visibility = View.GONE
                        }
                        "1" -> {
                            tvMyClub.visibility = View.VISIBLE
                            clMyClub.visibility = View.VISIBLE
                        }
                    }

                    when(is_fanclub) {
                        "0" -> {
                            tvClubEmpty.visibility = View.VISIBLE
                            groupClub.visibility = View.INVISIBLE
                        }
                        "1" -> {
                            tvClubEmpty.visibility = View.INVISIBLE
                            groupClub.visibility = View.VISIBLE
                            tvClubName.text = fanclub_name
                            tvClubMark.text = fanclub_card
                        }
                    }
                    getFansDataList()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    lateinit var headerView:View
    lateinit var civIcon:ImageView
    lateinit var tvClubName:TextView
    lateinit var tvClubMark:TextView
    lateinit var tvClubNum:TextView
    lateinit var tvClubRank:TextView
    lateinit var tvClubEmpty:TextView
    lateinit var groupClub:Group
    lateinit var tvMyClub:TextView
    lateinit var clMyClub:ConstraintLayout
    private fun initFansClubHeader() {
        headerView = View.inflate(this, R.layout.app_layout_fans_club_list, null)
        groupClub = headerView.findViewById(R.id.group_club_info)
        civIcon = headerView.findViewById(R.id.civ_fans_club_icon)
        tvClubName = headerView.findViewById(R.id.tv_fans_club_name)
        tvClubMark = headerView.findViewById(R.id.tv_fans_club_mark)
        tvClubNum = headerView.findViewById(R.id.tv_fans_club_num)
        tvClubRank = headerView.findViewById(R.id.tv_fans_club_rank)
        tvClubEmpty = headerView.findViewById(R.id.tv_fans_club_empty)
        clMyClub = headerView.findViewById(R.id.cl_my_fans_club)
        tvMyClub = headerView.findViewById(R.id.tv_my_fans_club)
        clMyClub.setOnClickListener {
            if (tvClubEmpty.visibility == View.VISIBLE) {
                showRequestBuildClub()
            } else {
                FansClubInfoActivity.start(this,MyApp.uid)
            }
        }
    }


    private fun showRequestBuildClub() {
        val requestPop = RequestBuildFansClubPop(this)
        requestPop.showPopupWindow()
        requestPop.onBuildClubListener = object :RequestBuildFansClubPop.OnBuildClubListener {
            override fun doBuildSuc() {
                getMyFansData()
            }
        }
    }

}
