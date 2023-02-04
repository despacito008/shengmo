package com.aiwujie.shengmo.timlive.kt.ui.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.LotteryDrawGiftBean
import com.aiwujie.shengmo.bean.LotteryDrawMarqueeBean
import com.aiwujie.shengmo.bean.LotteryDrawRecordBean
import com.aiwujie.shengmo.bean.LotteryDrawResultBean
import com.aiwujie.shengmo.kt.adapter.LotteryDrawRecordAdapter
import com.aiwujie.shengmo.kt.adapter.LotteryResultAdapter
import com.aiwujie.shengmo.kt.ui.view.GiftSynthesisView
import com.aiwujie.shengmo.kt.ui.view.ScrollMarqueeView
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.aiwujie.shengmo.view.luckydraw.LuckyDrawView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent
import org.greenrobot.eventbus.EventBus
import razerdp.basepopup.BasePopupWindow

class LiveLotteryDrawPop(context: Context) : BasePopupWindow(context) {
    private var mContext: Context = context
    private var lotteryDrawView: LuckyDrawView = findViewById(R.id.lucky_view)
    private var tvContinuousTen: TextView = findViewById(R.id.tv_start_lottery_draw_ten)
    private var tvContinuousHundred: TextView = findViewById(R.id.tv_start_lottery_draw_hundred)
    private var llResult: View = findViewById(R.id.ll_pop_lottery_draw_result)
    private var rvResult: RecyclerView = findViewById(R.id.rv_pop_lottery_draw_result)
    private var tvResult: TextView = findViewById(R.id.tv_pop_lottery_draw_result)
    private var tvResultTip: TextView = findViewById(R.id.tv_pop_lottery_draw_result_tip)
    private var marqueeView: ScrollMarqueeView = findViewById(R.id.marquee_view_lottery_draw)
    private var llBeans: View = findViewById(R.id.ll_pop_lottery_draw_bean)
    private var tvBeans: TextView = findViewById(R.id.tv_pop_lottery_draw_bean)
    private var tvSlogan: TextView = findViewById(R.id.tv_pop_lottery_draw_slogan)
    private var tvRecord:TextView = findViewById(R.id.tv_pop_lottery_draw_record)

    //记录
    private var rvRecord:RecyclerView = findViewById(R.id.rv_pop_lottery_draw_record)
    private var refreshRecord:SmartRefreshLayout = findViewById(R.id.refresh_pop_lottery_draw_record)
    private var flRecord:FrameLayout = findViewById(R.id.fl_pop_lottery_draw_record)
    private var llRecord:LinearLayout = findViewById(R.id.ll_pop_lottery_draw_record)
    private var ivRecordReturn:ImageView = findViewById(R.id.iv_pop_lottery_draw_record_return)

    //规则
    private var llRule:LinearLayout = findViewById(R.id.ll_pop_lottery_draw_rule)
    private var ivRuleReturn:ImageView = findViewById(R.id.iv_pop_lottery_draw_rule_return)
    private var tvRuleText:TextView = findViewById(R.id.tv_item_lottery_draw_rule)
    private var tvRule:TextView = findViewById(R.id.tv_pop_lottery_draw_rule)


    //合成
    private var tvSynthesis:TextView = findViewById(R.id.tv_pop_lottery_draw_synthesis)
    private var giftSynthesis:GiftSynthesisView = findViewById(R.id.gift_synthesis_view)

    private var isStart =false
    init {
        tvContinuousTen.setOnClickListener {
            if (isStart){
                return@setOnClickListener
            }
            lotteryDraw(2)
        }
        tvContinuousHundred.setOnClickListener {
            if (isStart){
                return@setOnClickListener
            }
            lotteryDraw(3)
        }
        lotteryDrawView.setOnLucKyDrawViewListener(object : LuckyDrawView.OnLucKyDrawViewListener {
            override fun doLotteryComplete() {
                isStart =false;
                llResult.visibility = View.VISIBLE
            }

            override fun doLotteryStart() {
                if (isStart){
                    return
                }
                lotteryDraw(1)
            }
        })
        llResult.setOnClickListener { }
        tvResult.setOnClickListener {
            llResult.visibility = View.GONE
        }


        llBeans.setOnClickListener {
            EventBus.getDefault().post(LiveMethodEvent("OnGiftPanel", "", ""))
        }

        tvRecord.setOnClickListener {

            showRecordView()
        }

        tvSynthesis.setOnClickListener {
            showLotteryDrawSynthesis()
        }

        initRuleView()
        initRecordView()
        initLotteryDrawSynthesis()
        getLotteryDrawInfo()
        getMarqueeData()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_lottery_draw)
    }

    override fun onDismiss() {
        super.onDismiss()
        marqueeView.stopScroll()
    }

    //抽奖
    fun lotteryDraw(type: Int) {
        //lotteryDrawView.startLotteryDrawWithResult(type,0)
        startLotteryDraw(type)
    }

    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 300)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, 1f, 300)
    }

    lateinit var lotteryGiftList: ArrayList<LotteryDrawGiftBean.DataBean.ListBean>
    private fun getLotteryDrawInfo() {
        lotteryGiftList = ArrayList()
        HttpHelper.getInstance().getLotteryDrawGiftList(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {

                val tempData = GsonUtil.GsonToBean(data, LotteryDrawGiftBean::class.java)
                tempData?.data?.run {
                    tvSlogan.text = config.tip
                    tvContinuousTen.text = "${config.button_left.tip} ${config.button_left.name}"
                    tvContinuousHundred.text = "${config.button_right.tip} ${config.button_right.name}"
                    tvBeans.text = "${config.wallet} 魔豆 "
                    tvRuleText.text = config.rule_text
                    lotteryGiftList.addAll(list)
                    lotteryDrawView.initLotteryDrawItem(list)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    lateinit var rewardResultList: ArrayList<LotteryDrawResultBean.DataBean.ListBean>
    private fun startLotteryDraw(type: Int) {
        rewardResultList = ArrayList()
        HttpHelper.getInstance().startLotteryDraw(type, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                isStart =true
                val tempData = GsonUtil.GsonToBean(data, LotteryDrawResultBean::class.java)
                tempData?.data?.run {
                    if (list.size > 0) {
                        rewardResultList.clear()
                        rewardResultList.addAll(list)
                        tvBeans.text = "${this.wallet} 魔豆 "
                        tvResultTip.text = this.tips
                        showResultView()
                        lotteryDrawView.startLotteryDrawWithResult(type, computeResultIndex(list[0].gift_id))
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                isStart =false;
                msg?.showToast()
            }
        })
    }

    private val positions = intArrayOf(0, 1, 2, 7, 3, 6, 5, 4) //顺时针
    private fun computeResultIndex(id: String): Int {
        val tempIndex = lotteryGiftList.map { it.gift_id }.indexOf(id)
        return if (tempIndex >= 0 && tempIndex < positions.size) positions[tempIndex] else 0
    }

    private fun showResultView() {
        with(rvResult) {
            adapter = LotteryResultAdapter(context,rewardResultList)
            layoutManager = GridLayoutManager(context,3)
        }
    }

    private fun initRuleView() {
        tvRule.setOnClickListener {
            llRule.visibility = View.VISIBLE
        }
        ivRuleReturn.setOnClickListener {
            llRule.visibility = View.GONE
        }
    }

    var recordPage = 0
    private fun initRecordView() {
        ivRecordReturn.setOnClickListener {
            llRecord.visibility = View.GONE
        }
        refreshRecord.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                recordPage = 0
                getLotteryRecord()
            }

            override fun onLoadMore(refreshlayout: RefreshLayout) {
                recordPage++
                getLotteryRecord()
            }
        })
    }
    var recordAdapter:LotteryDrawRecordAdapter? = null
    var recordList = ArrayList<LotteryDrawRecordBean.DataBean>()
    private fun showRecordView() {
        llRecord.visibility = View.VISIBLE
        getLotteryRecord()
    }

    private fun getLotteryRecord() {
        HttpHelper.getInstance().getLotteryDrawRecord(recordPage,object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshRecord.finishLoadMore()
                refreshRecord.finishRefresh()
                val tempData = GsonUtil.GsonToBean(data,LotteryDrawRecordBean::class.java)
                tempData?.data?.run {
                    when (recordPage) {
                        0 -> {
                            recordList.clear()
                            recordList.addAll(this)
                            recordAdapter = LotteryDrawRecordAdapter(context,recordList)
                            with(rvRecord) {
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
                refreshRecord.finishLoadMore()
                refreshRecord.finishRefresh()
            }
        })
    }

    private fun getMarqueeData() {
        HttpHelper.getInstance().getLotteryDrawMarquee(object:HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val tempData = GsonUtil.GsonToBean(data,LotteryDrawMarqueeBean::class.java)
                tempData?.data?.run {
                    val marqueeList = ArrayList<String>()
                    marqueeList.addAll(msgData)
                    marqueeView.setList(marqueeList)
                    marqueeView.startScroll()
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun showLotteryDrawSynthesis() {
       // flSynthesis.visibility = View.VISIBLE
        giftSynthesis.showSynthesisView()
    }

    private fun initLotteryDrawSynthesis() {
//        ivSynthesisReturn.setOnClickListener {
//            flSynthesis.visibility = View.GONE
//        }
    }
}