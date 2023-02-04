package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.MyPurseActivity
import com.aiwujie.shengmo.adapter.NormalViewPagerAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.NormalGiftBean
import com.aiwujie.shengmo.bean.SendGiftResultBean
import com.aiwujie.shengmo.kt.adapter.BlindBoxPanelAdapter
import com.aiwujie.shengmo.kt.adapter.GiftPanelAdapter
import com.aiwujie.shengmo.kt.adapter.KnapsackGiftPanelAdapter
import com.aiwujie.shengmo.kt.listener.OnGiftPanelListener
import com.aiwujie.shengmo.kt.ui.activity.SVGAAnimActivity
import com.aiwujie.shengmo.kt.ui.activity.SimpleAnimActivity
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.RechargeGiftActivity
import com.aiwujie.shengmo.kt.util.addOnPageChangeListenerDsl
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.tim.bean.GiftMessageBean
import com.aiwujie.shengmo.tim.helper.MessageSendHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.indicator.CircleIndicator
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import com.tencent.imsdk.v2.V2TIMValueCallback
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftViewPagerAdapter
import razerdp.basepopup.BasePopupWindow
import java.util.*
import kotlin.math.min

//礼物弹窗
class GiftPanelPop2(context: Context?) : BasePopupWindow(context) {
    var type: Int = 0
    lateinit var pid: String
    private lateinit var tvWealth: TextView
    private lateinit var tvFree: TextView
    lateinit var tvBeans: TextView
    private lateinit var tvBuy: TextView
    private lateinit var ivBuy: ImageView
   // lateinit var vpGift: ViewPager
    //lateinit var vpKnapsack: ViewPager
    private lateinit var lottieView: LottieAnimationView
    lateinit var indicator: CircleIndicator
    lateinit var indicatorKnapsack: CircleIndicator
    var isPackageGift: Boolean = false
    private val vpPanel:ViewPager by lazy { findViewById<ViewPager>(R.id.vp_pop_gift_panel) }
    private val tvBlindBox:TextView by lazy { findViewById<TextView>(R.id.tv_pop_gift_panel_blind_box) }
    constructor(context: Context?, type: Int, pid: String) : this(context) {
        this.type = type
        this.pid = pid
        initView()
        initViewPager()
    }

    var uid: String? = null

    constructor(context: Context?, type: Int, pid: String, uid: String) : this(context) {
        this.type = type
        this.pid = pid
        this.uid = uid
        initView()
        initViewPager()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_gift_panel2)
    }

    lateinit var vpGift:ViewPager
    lateinit var idGift:CircleIndicator
    lateinit var vpKnapsack:ViewPager
    lateinit var idKnapsack:CircleIndicator
    lateinit var rvBlindBox:RecyclerView
    fun initViewPager() {
        val views = ArrayList<View>()
        //礼物
        val giftView = LayoutInflater.from(context).inflate(R.layout.app_layout_panel_gift, vpPanel, false)
        vpGift = giftView.findViewById<ViewPager>(R.id.vp_layout_gift)
        idGift = giftView.findViewById<CircleIndicator>(R.id.indicator_layout_gift)

        //背包
        val knapsackView = LayoutInflater.from(context).inflate(R.layout.app_layout_panel_gift, vpPanel, false)
        vpKnapsack = knapsackView.findViewById<ViewPager>(R.id.vp_layout_gift)
        idKnapsack = knapsackView.findViewById<CircleIndicator>(R.id.indicator_layout_gift)

        //盲盒
        val blindBoxView = LayoutInflater.from(context).inflate(R.layout.app_layout_panel_blind_box, vpPanel, false)
        rvBlindBox = blindBoxView.findViewById<RecyclerView>(R.id.rv_gift_panel_blind_box)

        views.add(giftView)
        views.add(knapsackView)
        views.add(blindBoxView)
        val giftViewPagerAdapter = GiftViewPagerAdapter(views)
        vpPanel.adapter = giftViewPagerAdapter
        vpPanel.currentItem = 0
        vpPanel.offscreenPageLimit = views.size
        vpPanel.addOnPageChangeListenerDsl {
            onPageSelected {
                when (it) {
                    0 -> {
                        chooseTopTab(tvWealth)
                        unChooseTopTab(tvFree)
                        unChooseTopTab(tvBlindBox)
                    }
                    1 -> {
                        unChooseTopTab(tvWealth)
                        chooseTopTab(tvFree)
                        unChooseTopTab(tvBlindBox)
                    }
                    2 -> {
                        unChooseTopTab(tvWealth)
                        unChooseTopTab(tvFree)
                        chooseTopTab(tvBlindBox)
                    }
                }
            }
        }
    }

    fun initView() {
        popupGravity = Gravity.CENTER
        tvWealth = findViewById(R.id.tv_pop_gift_panel_wealth)
        tvFree = findViewById(R.id.tv_pop_gift_panel_free)
        tvBeans = findViewById(R.id.tv_pop_gift_panel_beans)
        vpGift = findViewById(R.id.view_pager_pop_gift_panel)
        //vpKnapsack = findViewById(R.id.view_pager_pop_gift_knapsack)
        tvBuy = findViewById(R.id.tv_pop_gift_panel_buy)
        ivBuy = findViewById(R.id.iv_pop_gift_panel_buy)
        lottieView = findViewById(R.id.lottie_view)
        indicator = findViewById(R.id.indicator_pop_send_gift)
        indicatorKnapsack = findViewById(R.id.indicator_pop_send_knapsack)
        //vpKnapsack.visibility = View.INVISIBLE
        vpGift.visibility = View.VISIBLE
        indicatorKnapsack.visibility = View.INVISIBLE
        indicator.visibility = View.VISIBLE
//        uid?.run {
//            if (this == MyApp.uid) {
//                tvFree.visibility = View.INVISIBLE
//            }
//        }
        initData()

        tvBuy.setOnClickListener {
            //val intent = Intent(context, MyPurseActivity::class.java)
            //context.startActivity(intent)
            RechargeGiftActivity.start(context)
            this.dismiss()
        }

        ivBuy.setOnClickListener {
            //val intent = Intent(context, MyPurseActivity::class.java)
            //context.startActivity(intent)
            RechargeGiftActivity.start(context)
            this.dismiss()
        }

        tvWealth.setOnClickListener {
            isPackageGift = false
            //vpKnapsack.visibility = View.INVISIBLE
//            vpGift.visibility = View.VISIBLE
//            indicatorKnapsack.visibility = View.INVISIBLE
//            indicator.visibility = View.VISIBLE
//            tvWealth.setTextColor(ContextCompat.getColor(context, R.color.white))
//            tvFree.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
            vpPanel.currentItem = 0
        }

        tvFree.setOnClickListener {
            isPackageGift = true
            //vpKnapsack.visibility = View.VISIBLE
//            vpGift.visibility = View.INVISIBLE
//            indicatorKnapsack.visibility = View.VISIBLE
//            indicator.visibility = View.INVISIBLE
//            tvWealth.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
//            tvFree.setTextColor(ContextCompat.getColor(context, R.color.white))
            vpPanel.currentItem = 1
        }

        tvBlindBox.setOnClickListener {
            vpPanel.currentItem = 2
        }

    }

    private fun chooseTopTab(textView: TextView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.purple_main))
    }

    private fun unChooseTopTab(textView: TextView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
    }

    fun initData() {
        //initMyWallet()
        //initMyPresent()
        initGiftData()
    }

    var lottieUrl = ""
    var svgaUrl = ""
    private fun initGiftData() {
        val inflater = LayoutInflater.from(context)
        HttpHelper.getInstance().getGiftList(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val giftData = GsonUtil.GsonToBean(data, NormalGiftBean::class.java)
                giftData?.apply {
                    wallet = giftData.data.rich_beans.let {
                        if (TextUtils.isEmpty(it)) 0 else it.toInt()
                    }
                    tvBeans.text = wallet.toString()
                    val goodList = this.data.goods
                    val mPagerList = ArrayList<View>()
                    goodList?.let {
                        for (i in 0..goodList.size / 9) {
                            val tempList = goodList.subList(i * 9, min(i * 9 + 9, goodList.size))
                            if (tempList.size > 0) {
                                val rvPanel: RecyclerView = inflater.inflate(R.layout.app_view_gift_panel, vpGift, false) as RecyclerView
                                val giftPanelAdapter = GiftPanelAdapter(context, tempList)
                                val layoutManager = GridLayoutManager(context, 3)
                                rvPanel.adapter = giftPanelAdapter
                                rvPanel.layoutManager = layoutManager
                                giftPanelAdapter.setOnSimpleItemListener(OnSimpleItemListener { position ->
                                    showSendGiftPanel(tempList[position])
                                    lottieUrl = tempList[position].gift_lottieurl
                                    svgaUrl = tempList[position].gift_svgaurl
                                    //showGiftAnim(tempList[position].gift_lottieurl)
                                })
                                mPagerList.add(rvPanel)
                            }
                        }
                        vpGift.adapter = NormalViewPagerAdapter(mPagerList)
                        //indicator.setViewPager(vpGift)
                        idGift.setViewPager(vpGift)
                    }

                    val knapsackList = this.data.knapsack
                    val mKnPagerList = ArrayList<View>()
                    knapsackList?.let {
                        for (i in 0..knapsackList.size / 9) {
                            val tempList = knapsackList.subList(i * 9, min(i * 9 + 9, knapsackList.size))
                            if (tempList.size > 0) {
                                val rvPanel: RecyclerView = inflater.inflate(R.layout.app_view_gift_panel, vpKnapsack, false) as RecyclerView
                                val knapsackPanelAdapter = KnapsackGiftPanelAdapter(context, tempList)
                                val layoutManager = GridLayoutManager(context, 3)
                                rvPanel.adapter = knapsackPanelAdapter
                                rvPanel.layoutManager = layoutManager
//                                knapsackPanelAdapter.setOnSimpleItemListener(OnSimpleItemListener {
//                                    position -> //showSendGiftPanel(tempList[position])
//                                    //showGiftAnim(tempList[position].gift_lottieurl)
//                                    showSendKnapsackGiftPanel(tempList[position])
//                                    lottieUrl = tempList[position].gift_lottieurl
//                                })

                                knapsackPanelAdapter.setOnRefreshNum(object : KnapsackGiftPanelAdapter.OnRefreshGiftNumListener {
                                    override fun onRefresh(position: Int, textView: TextView) {
                                        knaspackItemTextView = textView
                                        showSendKnapsackGiftPanel(tempList[position], textView, knapsackPanelAdapter, position)
                                        lottieUrl = tempList[position].gift_lottieurl
                                        svgaUrl = tempList[position].gift_svgaurl
                                    }
                                })
                                mKnPagerList.add(rvPanel)
                            }
                        }
                        vpKnapsack.adapter = NormalViewPagerAdapter(mKnPagerList)
                        //indicatorKnapsack.setViewPager(vpKnapsack)
                        idKnapsack.setViewPager(vpKnapsack)

                        val gridLayoutManager = GridLayoutManager(context, 3)
                        val boxAdapter = BlindBoxPanelAdapter(context,this.data.blindbox)
                        rvBlindBox.adapter = boxAdapter
                        rvBlindBox.layoutManager = gridLayoutManager
                        boxAdapter.blindBoxListener = object :BlindBoxPanelAdapter.OnBlindBoxListener{
                            override fun doBlindBoxClick(index: Int) {
                                showSendBlindBoxPanel(this@apply.data.blindbox[index])
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                LogUtil.d("$code  $msg")
            }
        })
    }

    var knaspackItemTextView:TextView? = null

    var wallet: Int = 0
    lateinit var giftId: String
    lateinit var giftName: String
    lateinit var giftUrl: String
    var num: Int = 0
    var totalPrice: Int = 0
    var isOpen: Boolean = false
    lateinit var giftbean: NormalGiftBean.DataBean.GoodsBean
    private var sendGiftPanelPop: SendGiftPanelPop? = null
    //打赏方式  1-动态 2-个人主页 3-回放 4-私聊 5-群聊
    fun showSendGiftPanel(goodsBean: NormalGiftBean.DataBean.GoodsBean) {
        sendGiftPanelPop = SendGiftPanelPop(context, goodsBean)
        sendGiftPanelPop?.showPopupWindow()
        sendGiftPanelPop?.setOnPanelListener(object : OnGiftPanelListener {
            override fun OnGiftPanelChoose(goodsBean: NormalGiftBean.DataBean.GoodsBean, num: Int, totalPrice: Int, isOpen: Boolean) {
                  giftbean = goodsBean
//                giftId = goodsBean.gift_id
                giftName = goodsBean.gift_name
//                giftUrl = goodsBean.gift_image
//                this@GiftPanelPop2.num = num
//                this@GiftPanelPop2.totalPrice = totalPrice
//                this@GiftPanelPop2.isOpen = isOpen
//                imageUrl = goodsBean.gift_image
//                when (type) {
//                    4, 2 -> {
//                        sendGiftToPerson(false, null, null, 0)
//                    }
//                    1 -> {
//                        sendGiftToDynamic(false, null, null, 0)
//                    }
//                    5 -> {
//                        sendGiftToGroup(false, null, null, 0)
//                    }
//                }
                sendGift(type,0,pid,goodsBean.gift_id,num,isOpen)
            }
        })
    }

    var mKnapsackGiftPanelAdapter:KnapsackGiftPanelAdapter? = null

    var mKnapsackGiftPanelIndex:Int = 0

    fun showSendKnapsackGiftPanel(knapsackBean: NormalGiftBean.DataBean.KnapsackBean, textView: TextView, adapter: KnapsackGiftPanelAdapter, position: Int) {
        mKnapsackGiftPanelAdapter = adapter
        mKnapsackGiftPanelIndex = position
        val goodsBean = NormalGiftBean.DataBean.GoodsBean()
        knapsackBean.let {
            goodsBean.gift_id = it.gift_id
            goodsBean.gift_image = it.gift_image
            goodsBean.gift_name = it.gift_name
            goodsBean.gift_beans = it.gift_beans
            goodsBean.gift_lottieurl = it.gift_lottieurl
            goodsBean.num = it.num
        }
        sendGiftPanelPop = SendGiftPanelPop(context, goodsBean, knapsackBean.num.toInt())
        sendGiftPanelPop?.showPopupWindow()
        sendGiftPanelPop?.setOnPanelListener(object : OnGiftPanelListener {
            override fun OnGiftPanelChoose(goodsBean: NormalGiftBean.DataBean.GoodsBean, num: Int, totalPrice: Int, isOpen: Boolean) {
                giftbean = goodsBean
//                giftId = goodsBean.gift_id
                giftName = goodsBean.gift_name
//                this@GiftPanelPop2.num = num
//                this@GiftPanelPop2.totalPrice = totalPrice
//                this@GiftPanelPop2.isOpen = isOpen
//                imageUrl = goodsBean.gift_image
//                when (type) {
//                    4, 2 -> {
//                        sendGiftToPerson(true, textView, adapter, position)
//                    }
//                    1 -> {
//                        sendGiftToDynamic(true, textView, adapter, position)
//                    }
//                    5 -> {
//                        sendGiftToGroup(true, textView, adapter, position)
//                    }
//                }
                sendGift(type,1,pid,goodsBean.gift_id,num,isOpen)
            }
        })
    }


    var blindBoxPanel:SendBlindBoxPanelPop? = null
    fun showSendBlindBoxPanel(blind: NormalGiftBean.DataBean.BlindBoxBean) {
        blindBoxPanel = SendBlindBoxPanelPop(context,blind)
        blindBoxPanel?.setOnPanelListener(object :SendBlindBoxPanelPop.OnBlindBoxPanelListener {
            override fun doSendBlind(blindId: String, num: Int, isOpen: Boolean) {
                sendGift(type,2,pid,blindId,num,isOpen)
            }
        })
        blindBoxPanel?.showPopupWindow()
    }

    fun sendGift(type:Int,giftType:Int,id:String,giftId:String,num:Int,isOpen:Boolean) {
        HttpHelper.getInstance().sendGift(type,id,giftType,giftId,num.toString(),isOpen,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val result = GsonUtil.GsonToBean(data,SendGiftResultBean::class.java)
                result?.data?.run {
                    msg?.showToast()
                    if (blindBoxPanel?.isShowing == true) {
                        blindBoxPanel?.dismiss()
                    }
                    if (sendGiftPanelPop?.isShowing == true) {
                        sendGiftPanelPop?.dismiss()
                    }
                    if (gift_svgaurl.isNotEmpty()) {
                        SVGAAnimActivity.start(context,gift_svgaurl)
                    } else if (gift_image.isNotEmpty()) {
                        SimpleAnimActivity.start(context,gift_image)
                    }
                    //礼物类型
                    when (giftType) {
                        1 -> {
                            if (giftbean.num.toInt() == num){ //单个礼物送完了
                                refreshPackageGift()
                            } else { //礼物没送完
                                knaspackItemTextView?.text = "$giftName * ${giftbean.num.toInt() - num}"
                                mKnapsackGiftPanelAdapter?.run {
                                    giftList[mKnapsackGiftPanelIndex].num = (giftbean.num.toInt() - num).toString()
                                    notifyDataSetChanged()
                                }
                            }
                        }
                        else -> {
                            refreshBalance(this.wallet)
                        }

                    }

                    //打赏位置 1.动态 4.私聊 5.群聊
                    when (type) {
                        1 -> {
                            onGiftSendSucListener.onGiftSendSuc("", (gift_beans.toInt() * num))
                        }

                        3 -> {
                            onGiftSendSucListener.onGiftSendSuc("", (gift_beans.toInt() * num))
                        }

                        4 -> {
                            getGiftTipsMessage(1)
                            sendIMGiftMessage(this,num)
                        }
                        5 -> {
                            getGiftTipsMessage(2)
                            sendIMGiftMessage(this,num)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    fun sendGiftToDynamic(isKnapsack: Boolean, textView: TextView?, adapter: KnapsackGiftPanelAdapter?, position: Int) {
        HttpHelper.getInstance().sendGiftToDynamic(isKnapsack, pid, giftId, num, totalPrice, isOpen, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                sendGiftPanelPop?.dismiss()
                showGiftAnim()
                refreshGift(textView, adapter, position)
                onGiftSendSucListener.onGiftSendSuc("", totalPrice)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context, msg)
            }
        })
    }

    fun sendGiftToPerson(isKnapsack: Boolean, textView: TextView?, adapter: KnapsackGiftPanelAdapter?, position: Int) {
        HttpHelper.getInstance().sendGiftToPerson(isKnapsack, pid, giftId, num, totalPrice, isOpen, if (type == 1) "1" else "2", object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                sendGiftPanelPop?.dismiss()
                onGiftSendSucListener.onGiftSendSuc("", 0)
                refreshGift(textView, adapter, position)
                if (type == 2) { //私聊发送礼物 额外发送礼物消息
                    val members: MutableList<String> = ArrayList()
                    members.add(MyApp.uid)
                    V2TIMManager.getInstance().getUsersInfo(members, object : V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                        override fun onError(code: Int, desc: String) {
                            MessageSendHelper.getInstance().sendTipMessage("你给ta送了一个礼物", "ta给你送了一个礼物")
                            val contentDictBean = GiftMessageBean.ContentDictBean()
                            contentDictBean.number = num.toString()
                            contentDictBean.orderid = ""
                            contentDictBean.giftText = giftbean.gift_name + " X " + num.toString()
                            contentDictBean.imageName = giftbean.gift_name
                            contentDictBean.giftUrl = giftbean.gift_image
                            contentDictBean.lottieUrl = giftbean.gift_lottieurl
                            contentDictBean.svgaUrl = giftbean.gift_svgaurl
                            MessageSendHelper.getInstance().sendGiftMessage(contentDictBean)
                            onGiftSendSucListener.onGiftSendSuc("", 0)
                            showGiftAnim()
                        }

                        override fun onSuccess(v2TIMUserFullInfos: List<V2TIMUserFullInfo>) {
                            val name = v2TIMUserFullInfos[0].nickName
                            MessageSendHelper.getInstance().sendTipMessage("你给ta送了一个礼物", name + "给你送了一个礼物")
                            val contentDictBean = GiftMessageBean.ContentDictBean()
                            contentDictBean.number = num.toString()
                            contentDictBean.orderid = ""
                            contentDictBean.giftText = giftbean.gift_name + " X " + num.toString()
                            contentDictBean.imageName = giftbean.gift_name
                            contentDictBean.giftUrl = giftbean.gift_image
                            contentDictBean.lottieUrl = giftbean.gift_lottieurl
                            contentDictBean.svgaUrl = giftbean.gift_svgaurl
                            MessageSendHelper.getInstance().sendGiftMessage(contentDictBean)
                            onGiftSendSucListener.onGiftSendSuc("", 0)
                            showGiftAnim()
                        }
                    })
                } else {
                    showGiftAnim()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    fun sendGiftToGroup(isKnapsack: Boolean, textView: TextView?, adapter: KnapsackGiftPanelAdapter?, position: Int) {
        val orderid = MyApp.uid + SystemClock.currentThreadTimeMillis()
        HttpHelper.getInstance().sendGiftToGroup(isKnapsack, pid, giftId, num, totalPrice, isOpen, orderid, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                sendGiftPanelPop?.dismiss()
                refreshGift(textView, adapter, position)
                val members: MutableList<String> = ArrayList()
                members.add(MyApp.uid)
                V2TIMManager.getGroupManager().getGroupMembersInfo(pid, members, object : V2TIMValueCallback<List<V2TIMGroupMemberFullInfo>> {
                    override fun onError(code: Int, desc: String) {
                        MessageSendHelper.getInstance().sendTipMessage("你给大家送了礼物", "ta给大家打赏了礼物")
                        val contentDictBean = GiftMessageBean.ContentDictBean()
                        contentDictBean.number = num.toString()
                        contentDictBean.orderid = orderid
                        contentDictBean.giftText = giftbean.gift_name + " X " + num.toString()
                        contentDictBean.imageName = giftbean.gift_name
                        contentDictBean.giftUrl = giftbean.gift_image
                        contentDictBean.lottieUrl = giftbean.gift_lottieurl
                        contentDictBean.svgaUrl = giftbean.gift_svgaurl
                        MessageSendHelper.getInstance().sendGiftMessage(contentDictBean)
                        onGiftSendSucListener.onGiftSendSuc(orderid, 0)
                        showGiftAnim()

                    }

                    override fun onSuccess(v2TIMGroupMemberFullInfos: List<V2TIMGroupMemberFullInfo>) {
                        var name = v2TIMGroupMemberFullInfos[0].nameCard
                        if (name.isEmpty()) {
                            name = v2TIMGroupMemberFullInfos[0].nickName
                        }
                        MessageSendHelper.getInstance().sendTipMessage("你给大家送了礼物", name + "给大家打赏了礼物")
                        val contentDictBean = GiftMessageBean.ContentDictBean()
                        contentDictBean.number = num.toString()
                        contentDictBean.orderid = orderid
                        contentDictBean.giftText = giftbean.gift_name + " X " + num.toString()
                        contentDictBean.imageName = giftbean.gift_name
                        contentDictBean.giftUrl = giftbean.gift_image
                        contentDictBean.lottieUrl = giftbean.gift_lottieurl
                        contentDictBean.svgaUrl = giftbean.gift_svgaurl
                        MessageSendHelper.getInstance().sendGiftMessage(contentDictBean)
                        onGiftSendSucListener.onGiftSendSuc(orderid, 0)
                        showGiftAnim()
                    }
                })
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    var imageUrl = ""
    fun showGiftAnim() {
        when {
            svgaUrl.isNotEmpty() -> {
                val intent = Intent(context, SVGAAnimActivity::class.java)
                intent.putExtra("url", svgaUrl)
                context.startActivity(intent)
            }
//            lottieUrl.isNotEmpty() -> {
//                val intent = Intent(context, LottieAnimActivity::class.java)
//                intent.putExtra("url", lottieUrl)
//                context.startActivity(intent)
//            }
            else -> {
                SimpleAnimActivity.start(context,imageUrl)
            }
        }
    }

    private fun refreshGift(textView: TextView?, adapter: KnapsackGiftPanelAdapter?, position: Int) {
        if (vpPanel.currentItem == 1){ //背包
            if (giftbean.num.toInt() == num){ //单个礼物送完了
                refreshPackageGift()
            } else { //礼物没送完
                //textView!!.text = giftName + "*" + (giftbean.num.toInt() - num)
                knaspackItemTextView?.text = "$giftName * ${giftbean.num.toInt() - num}"
                adapter!!.giftList[position].num = (giftbean.num.toInt() - num).toString();
                adapter!!.notifyDataSetChanged()
            }
        }else {
            refreshYue()
        }

    }


    private fun refreshPackageGift(){
        val inflater = LayoutInflater.from(context)
        HttpHelper.getInstance().getGiftList(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val giftData = GsonUtil.GsonToBean(data, NormalGiftBean::class.java)
                giftData?.apply {
                    val knapsackList = this.data.knapsack
                    val mKnPagerList = ArrayList<View>()
                    knapsackList?.let {
                        for (i in 0..knapsackList.size / 9) {
                            val tempList = knapsackList.subList(i * 9, min(i * 9 + 9, knapsackList.size))
                            if (tempList.size > 0) {
                                val rvPanel: RecyclerView = inflater.inflate(R.layout.app_view_gift_panel, vpKnapsack, false) as RecyclerView
                                val knapsackPanelAdapter = KnapsackGiftPanelAdapter(context, tempList)
                                val layoutManager = GridLayoutManager(context, 3)
                                rvPanel.adapter = knapsackPanelAdapter
                                rvPanel.layoutManager = layoutManager
                                rvPanel.adapter.notifyDataSetChanged()
                                knapsackPanelAdapter.setOnRefreshNum(object : KnapsackGiftPanelAdapter.OnRefreshGiftNumListener {
                                    override fun onRefresh(position: Int, textView: TextView) {
                                        showSendKnapsackGiftPanel(tempList[position], textView, knapsackPanelAdapter, position)
                                        lottieUrl = tempList[position].gift_lottieurl
                                    }
                                })
                                mKnPagerList.add(rvPanel)
                            }
                        }
                        vpKnapsack.adapter = NormalViewPagerAdapter(mKnPagerList)
                        indicatorKnapsack.setViewPager(vpKnapsack)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                LogUtil.d("$code  $msg")
            }
        })
    }

    private fun refreshYue(){
        HttpHelper.getInstance().getGiftList(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val giftData = GsonUtil.GsonToBean(data, NormalGiftBean::class.java)
                giftData?.apply {
                    wallet = giftData.data.rich_beans.let {
                        if (TextUtils.isEmpty(it)) 0 else it.toInt()
                    }
                    tvBeans.text = wallet.toString()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                LogUtil.d("$code  $msg")
            }
        })
    }

    private fun refreshBalance(balance:String) {
        tvBeans.text = balance
    }

    interface OnGiftSendSucListener {
        fun onGiftSendSuc(orderId: String, giftReward: Int)
    }

    private lateinit var onGiftSendSucListener: OnGiftSendSucListener

    fun setOnGiftSendSucListener(onGiftSendSucListener: OnGiftSendSucListener) {
        this.onGiftSendSucListener = onGiftSendSucListener
    }

    fun getGiftTipsMessage(type:Int) {
        when (type) {
            1 -> {
                V2TIMManager.getInstance().getUsersInfo(arrayListOf(MyApp.uid),object :V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                    override fun onSuccess(p0: List<V2TIMUserFullInfo>?) {
                        if (p0?.size?:0 > 0) {
                            p0?.run {
                                this[0].run {
                                    sendGiftTipsMessage("你给ta打赏了礼物","${nickName}给你打赏了礼物")
                                }
                            }
                        } else {
                            sendGiftTipsMessage("你给ta打赏了礼物","ta给你打赏了礼物")
                        }
                    }

                    override fun onError(p0: Int, p1: String?) {
                        sendGiftTipsMessage("你给ta打赏了礼物","ta给你打赏了礼物")
                    }
                })
            }
            2 -> {
                V2TIMManager.getGroupManager().getGroupMembersInfo(pid, arrayListOf(MyApp.uid), object : V2TIMValueCallback<List<V2TIMGroupMemberFullInfo>> {
                    override fun onSuccess(p0: List<V2TIMGroupMemberFullInfo>?) {
                        if (p0?.size?:0 > 0) {
                            p0?.run {
                                this[0].run {
                                    sendGiftTipsMessage("你给大家打赏了礼物", "${if (nameCard.isNotEmpty()) nameCard else nickName} 给大家打赏了礼物")
                                }
                            }
                        } else {
                            sendGiftTipsMessage("你给大家打赏了礼物","ta给大家打赏了礼物")
                        }
                    }

                    override fun onError(p0: Int, p1: String?) {
                        sendGiftTipsMessage("你给大家打赏了礼物","ta给大家打赏了礼物")
                    }
                })
            }
        }
    }

    fun sendGiftTipsMessage(self:String,other:String) {
        MessageSendHelper.getInstance().sendTipMessage(self, other)
    }

    fun sendIMGiftMessage(gift: SendGiftResultBean.DataBean, num:Int) {
        val contentDictBean = GiftMessageBean.ContentDictBean()
        contentDictBean.apply {
            number = num.toString()
            orderid = gift.orderid
            giftText = gift.gift_name + " X " + num.toString()
            imageName = gift.gift_name
            giftUrl = gift.gift_image
            lottieUrl = gift.gift_lottieurl
            svgaUrl = gift.gift_svgaurl
            MessageSendHelper.getInstance().sendGiftMessage(this)
        }

    }
}