package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import com.aiwujie.shengmo.bean.FansClubRankBean
import com.aiwujie.shengmo.kt.adapter.FansClubRankAdapter
import com.aiwujie.shengmo.kt.ui.activity.normallist.NormalListActivity
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: FansClubRankActivity
 * @Author: xmf
 * @CreateDate: 2022/6/1 12:04
 * @Description:
 */
class FansClubRankActivity: NormalListActivity() {

    companion object {
        fun start(context:Context) {
            val intent = Intent(context,FansClubRankActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getPageTitle(): String {
        return "粉丝团排行"
    }

    override fun loadData() {
        getRankData()
    }

    override fun initViewComplete() {
        super.initViewComplete()
        refreshLayout.setBackgroundColor(Color.parseColor("#11000000"))
        refreshLayout.setOnRefreshListener {
            page = 0
            getRankData()
        }
        refreshLayout.setOnLoadMoreListener {
            page++
            getRankData()
        }
    }

    var page = 0
    private val clubList:ArrayList<FansClubRankBean.DataBean> by lazy { ArrayList<FansClubRankBean.DataBean>() }
    var rankAdapter:FansClubRankAdapter? = null
    private fun getRankData() {
        HttpHelper.getInstance().getFansClubRankList(page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,FansClubRankBean::class.java)
                tempData?.data?.run {
                    when(page) {
                        0 -> {
                            clubList.clear()
                            clubList.addAll(this)
                            rankAdapter = FansClubRankAdapter(this@FansClubRankActivity,clubList)
                            with(rvList) {
                                adapter = rankAdapter
                                layoutManager = LinearLayoutManager(this@FansClubRankActivity)
                            }
                            rankAdapter?.onSimpleItemListener = OnSimpleItemListener {
                                FansClubInfoActivity.start(this@FansClubRankActivity,clubList[it].uid)
                            }
                        }
                        else -> {
                            val tempIndex = clubList.size
                            clubList.addAll(this)
                            rankAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
}
