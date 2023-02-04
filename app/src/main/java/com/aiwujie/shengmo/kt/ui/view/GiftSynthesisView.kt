package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LotteryDrawRecordBean
import com.aiwujie.shengmo.bean.MergeGiftDetailBean
import com.aiwujie.shengmo.bean.SynthesisGiftBean
import com.aiwujie.shengmo.bean.SynthesisResultBean
import com.aiwujie.shengmo.kt.adapter.LotteryDrawRecordAdapter
import com.aiwujie.shengmo.kt.ui.adapter.SynthesisGiftAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

class GiftSynthesisView : FrameLayout {
    private var flSynthesis: FrameLayout
    private var llSynthesis: LinearLayout
    private var ivSynthesisLeft: ImageView
    private var tvSynthesisLeftNum: TextView
    private var tvSynthesisLeftName: TextView
    private var tvSynthesisLeftPrice: TextView
    private var ivSynthesisRight: ImageView
    private var tvSynthesisRightNum: TextView
    private var tvSynthesisRightName: TextView
    private var tvSynthesisRightPrice: TextView
    private var tvSynthesisRightTips: TextView
    private var tvSynthesisConfirm: TextView
    private var flSynthesisCheck: FrameLayout
    private var ivSynthesisReturn: ImageView
    private var flSynthesisResult:FrameLayout
    private var ivSynthesisResult: ImageView
    private var tvSynthesisResultNum: TextView
    private var tvSynthesisResultName: TextView
    private var tvSynthesisResultPrice: TextView
    private var rvSynthesisGift:RecyclerView

    private var tvSynthesisRecord:TextView
    private var llSynthesisRecord:LinearLayout
    private var ivSynthesisRecordReturn:ImageView
    private var refreshSynthesisRecord:SmartRefreshLayout
    private var rvSynthesisRecord:RecyclerView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.app_layout_gift_synthesis, this)
        rootView.run {
            flSynthesis = findViewById(R.id.fl_pop_lottery_draw_synthesis)
            llSynthesis = findViewById(R.id.ll_pop_lottery_draw_synthesis)
            ivSynthesisLeft = findViewById(R.id.iv_gift_synthesis_left_icon)
            tvSynthesisLeftNum = findViewById(R.id.tv_gift_synthesis_left_num)
            tvSynthesisLeftName = findViewById(R.id.tv_gift_synthesis_left_name)
            tvSynthesisLeftPrice = findViewById(R.id.tv_gift_synthesis_left_bean)
            ivSynthesisRight = findViewById(R.id.iv_gift_synthesis_right_icon)
            tvSynthesisRightNum = findViewById(R.id.tv_gift_synthesis_right_num)
            tvSynthesisRightName = findViewById(R.id.tv_gift_synthesis_right_name)
            tvSynthesisRightPrice = findViewById(R.id.tv_gift_synthesis_right_bean)
            tvSynthesisRightTips = findViewById(R.id.tv_gift_synthesis_tips)
            tvSynthesisConfirm = findViewById(R.id.tv_gift_synthesis_confirm)
            flSynthesisCheck = findViewById(R.id.fl_pop_lottery_draw_synthesis_check)
            ivSynthesisReturn = findViewById(R.id.iv_pop_lottery_draw_synthesis_return)
            flSynthesisResult = findViewById(R.id.fl_pop_lottery_draw_synthesis_result)
            ivSynthesisResult = findViewById(R.id.iv_gift_synthesis_result_icon)
            tvSynthesisResultNum = findViewById(R.id.tv_gift_synthesis_result_num)
            tvSynthesisResultName = findViewById(R.id.tv_gift_synthesis_result_name)
            tvSynthesisResultPrice = findViewById(R.id.tv_gift_synthesis_result_bean)
            rvSynthesisGift = findViewById(R.id.rv_pop_lottery_draw_synthesis)

            tvSynthesisRecord = findViewById(R.id.tv_synthesis_record)
            llSynthesisRecord = findViewById(R.id.ll_synthesis_record)
            ivSynthesisRecordReturn = findViewById(R.id.iv_synthesis_record_return)
            refreshSynthesisRecord = findViewById(R.id.refresh_synthesis_record)
            rvSynthesisRecord = findViewById(R.id.rv_synthesis_record)

            ivSynthesisReturn.setOnClickListener {
                hideSynthesisView()
            }

            flSynthesisCheck.setOnClickListener {
                hideCheckView()
            }

            flSynthesisResult.setOnClickListener {
                hideResultView()
            }

            tvSynthesisRecord.setOnClickListener {
                showRecordView()
            }


            ivSynthesisRecordReturn.setOnClickListener {
                hideRecordView()
            }

        }
    }

    fun showSynthesisView() {
        flSynthesis.visibility = View.VISIBLE
        getGiftList()
    }

    private fun hideSynthesisView() {
        flSynthesis.visibility = View.GONE
    }

    private fun showCheckView() {
        flSynthesisCheck.visibility = View.VISIBLE
    }

    private fun hideCheckView() {
        flSynthesisCheck.visibility = View.GONE
    }

    private fun showResultView() {
        flSynthesisResult.visibility = View.VISIBLE
    }

    private fun hideResultView() {
        flSynthesisResult.visibility = View.GONE
    }

    private fun showRecordView() {
        llSynthesisRecord.visibility = View.VISIBLE
        initRecordPage()
        getSynthesisRecord()
    }

    private fun initRecordPage() {
        refreshSynthesisRecord.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                recordPage = 0
                getSynthesisRecord()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                recordPage++
                getSynthesisRecord()
            }
        })
    }

    private fun hideRecordView() {
        llSynthesisRecord.visibility = View.GONE
    }

    private fun getGiftList() {
        HttpHelper.getInstance().getSynthesisGiftList(object: HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,SynthesisGiftBean::class.java)
                tempData?.data?.list?.run {
                    val giftList = ArrayList<SynthesisGiftBean.DataBean.ListBean>()
                    giftList.addAll(this)
                    val synthesisAdapter = SynthesisGiftAdapter(context,giftList)
                     with(rvSynthesisGift) {
                         adapter = synthesisAdapter
                         layoutManager = GridLayoutManager(context,4)
                     }
                    synthesisAdapter.itemListener = OnSimpleItemListener {
                        index ->
                        getSynthesisDetail(giftList[index].gift_id)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun getSynthesisDetail(gid:String) {
        HttpHelper.getInstance().getMergeGiftInfo(gid,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showCheckView()
                val tempData = GsonUtil.GsonToBean(data,MergeGiftDetailBean::class.java)
                tempData?.data?.run {
                    ImageLoader.loadImage(context,gift.gift_image,ivSynthesisLeft)
                    ImageLoader.loadImage(context,merge_gift.gift_image,ivSynthesisRight)
                    tvSynthesisLeftName.text = gift.gift_name
                    tvSynthesisRightName.text = merge_gift.gift_name
                    tvSynthesisLeftNum.text = "x ${gift.level_num}"
                    tvSynthesisRightNum.text = "x ${merge_gift.level_num}"
                    tvSynthesisLeftPrice.text = "${gift.gift_beans} 魔豆"
                    tvSynthesisRightPrice.text = "${merge_gift.gift_beans} 魔豆"
                    if (is_can == 0) {
                        tvSynthesisConfirm.alpha = 0.6f
                        tvSynthesisRightTips.text = tips
                        tvSynthesisConfirm.isClickable = false
                    } else {
                        tvSynthesisConfirm.alpha = 1f
                        tvSynthesisRightTips.text = ""
                        tvSynthesisConfirm.isClickable = true
                        tvSynthesisConfirm.setOnClickListener {
                            synthesisGift(gid)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun synthesisGift(gid:String) {
        HttpHelper.getInstance().synthesisGift(gid,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                showResultView()
                hideCheckView()
                getGiftList()
                val tempData = GsonUtil.GsonToBean(data,SynthesisResultBean::class.java)
                tempData?.data?.run {
                    ImageLoader.loadImage(context,gift_image,ivSynthesisResult)
                    tvSynthesisResultName.text = gift_name
                    tvSynthesisResultNum.text = "x $num"
                    tvSynthesisResultPrice.text = "$gift_beans 魔豆"
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    var recordPage = 0
    var recordAdapter:LotteryDrawRecordAdapter? = null
    var recordList = ArrayList<LotteryDrawRecordBean.DataBean>()
    private fun getSynthesisRecord() {
        HttpHelper.getInstance().getSynthesisRecord(recordPage,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshSynthesisRecord.finishLoadMore()
                refreshSynthesisRecord.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data, LotteryDrawRecordBean::class.java)
                tempData?.data?.run {
                    when (recordPage) {
                        0 -> {
                            recordList.clear()
                            recordList.addAll(this)
                            recordAdapter = LotteryDrawRecordAdapter(context,recordList)
                            with(rvSynthesisRecord) {
                                adapter = recordAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }
                        else -> {
                            val tempIndex = recordList.size
                            recordList.addAll(this)
                            recordAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }
}