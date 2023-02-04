package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.adapter.GiftViewPagerAdapter
import com.aiwujie.shengmo.timlive.bean.GiftCoinListInfo
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import razerdp.basepopup.BasePopupWindow

class LivePkTopAudiencePop(context:Context): BasePopupWindow(context) {
    lateinit var flTitleUs:FrameLayout
    lateinit var tvTitleUs:TextView
    lateinit var ivDotUs:ImageView
    lateinit var rvTopUs:RecyclerView

    lateinit var flTitleOther:FrameLayout
    lateinit var tvTitleOther:TextView
    lateinit var ivDotOther:ImageView
    lateinit var rvTopOther:RecyclerView

    lateinit var anchorId:String
    lateinit var otherAnchorId:String
    private var isUs:Boolean = false

    constructor(context: Context,anchorId:String,otherAnchorId:String,isUs:Boolean):this(context) {
        this.anchorId = anchorId
        this.otherAnchorId = otherAnchorId
        this.isUs = isUs
        initView()
        getTopData()
    }

    fun initView() {
        flTitleUs = findViewById(R.id.fl_title_us)
        tvTitleUs = findViewById(R.id.tv_title_us)
        ivDotUs = findViewById(R.id.iv_dot_us)
        rvTopUs = findViewById(R.id.rv_pk_top_audience_us)

        flTitleOther = findViewById(R.id.fl_title_other)
        tvTitleOther = findViewById(R.id.tv_title_other)
        ivDotOther = findViewById(R.id.iv_dot_other)
        rvTopOther = findViewById(R.id.rv_pk_top_audience_other)

        refreshTab()

        flTitleOther.setOnClickListener {
            isUs = !isUs
            refreshTab()
        }
        flTitleUs.setOnClickListener {
            isUs = !isUs
            refreshTab()
        }
    }


    private fun refreshTab() {
        if (isUs) {
            ivDotUs.visibility = View.VISIBLE
            tvTitleUs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            tvTitleUs.typeface = Typeface.DEFAULT_BOLD
            rvTopUs.visibility = View.VISIBLE

            ivDotOther.visibility = View.GONE
            tvTitleOther.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            tvTitleOther.typeface = Typeface.DEFAULT
            rvTopOther.visibility = View.GONE
        } else {
            ivDotUs.visibility = View.GONE
            tvTitleUs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            tvTitleUs.typeface = Typeface.DEFAULT
            rvTopUs.visibility = View.GONE

            ivDotOther.visibility = View.VISIBLE
            tvTitleOther.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            tvTitleOther.typeface = Typeface.DEFAULT_BOLD
            rvTopOther.visibility = View.VISIBLE
        }
    }

    private fun getTopData() {
        HttpHelper.getInstance().getPkTopData(anchorId,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val usTopData = GsonUtil.GsonToBean(data, GiftCoinListInfo::class.java)
                usTopData?.data?.let {
                    val usAdapter = GiftViewPagerAdapter(context,it.rankingList,object :GiftViewPagerAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int, data: GiftCoinListInfo.DataBean.RankingListBean?) {
                            var intent = Intent(context,UserInfoActivity::class.java)
                            intent.putExtra("uid",data?.uid)
                            context.startActivity(intent)
                        }
                    })
                    rvTopUs.layoutManager = LinearLayoutManager(context)
                    rvTopUs.adapter = usAdapter
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }

        })

        HttpHelper.getInstance().getPkTopData(otherAnchorId,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val otherTopData = GsonUtil.GsonToBean(data, GiftCoinListInfo::class.java)
                otherTopData?.data?.let {
                    val otherAdapter = GiftViewPagerAdapter(context,it.rankingList,null)
                    rvTopOther.layoutManager = LinearLayoutManager(context)
                    rvTopOther.adapter = otherAdapter
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
            }

        })
    }


    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_pk_top_audience)
    }
}