package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RechargeBeanAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.StampGoodsBean
import com.aiwujie.shengmo.bean.WalletData
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.view.KeyBoardListenerHelper
import org.json.JSONObject
import razerdp.basepopup.BasePopupWindow

class RechargeBeanPop(context: Context) : BasePopupWindow(context) {
    var rvGoods: RecyclerView = findViewById(R.id.rv_recharge_bean)
    var tvPay: TextView = findViewById(R.id.tv_recharge_bean_pay)
    var tvWeChat:TextView = findViewById(R.id.tv_recharge_bean_type_we_chat)
    var tvWeAliPay:TextView = findViewById(R.id.tv_recharge_bean_type_ali_pay)
    val tvBalance:TextView by lazy { findViewById<TextView>(R.id.tv_recharge_bean_balance) }
    private val etCustom:EditText by lazy { findViewById<EditText>(R.id.et_fragment_recharge_buy_custom) }
    private val llCustomPay: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_custom_pay) }

    var keyboardHelper:KeyBoardListenerHelper? = null

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_recharge_bean)
    }
    var payType = 1

    init {

        getWallet()
        getRechargeItem()
        tvPay.setOnClickListener {
            when (payType) {
                1 -> payByWeChat()
                2 -> payByAli()
            }
        }

        tvWeChat.setOnClickListener {
            payType = 1
            with(tvWeChat) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_pay_weChat))
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            with(tvWeAliPay) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_pay_normal))
                setTextColor(ContextCompat.getColor(context, R.color.normalGray))
            }
        }

        tvWeAliPay.setOnClickListener {
            payType = 2
            with(tvWeChat) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_pay_normal))
                setTextColor(ContextCompat.getColor(context, R.color.normalGray))
            }
            with(tvWeAliPay) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_pay_aliPay))
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }

        etCustom.addTextChangedListenerDsl {
            afterTextChanged {
                if (it?.length ?:0 > 0) {
                    amount = it.toString()?.toInt()
                    tvPay.text = "确认支付 ${amount}元"
                } else {
                    amount = 0
                    tvPay.text = "确认支付 ${amount}元"
                }
            }
        }

        llCustomPay.setOnClickListener {
            chooseCustomPay()
        }

        keyboardHelper = KeyBoardListenerHelper(context as Activity)
        keyboardHelper?.setOnKeyBoardChangeListener() {
            isShow, _ ->
            if (isShow) {
                chooseCustomPay()
            }
        }
    }

    var amount = 0

    var goodsAdapter: RechargeBeanAdapter? = null
    var goodsSubject:String =""
    private fun getRechargeItem() {
        HttpHelper.getInstance().getBeansList(object : HttpListener {
            override fun onSuccess(data: String?) {
                val tempData = GsonUtil.GsonToBean(data, StampGoodsBean::class.java)
                tempData?.data?.run {
                    goodsAdapter = RechargeBeanAdapter(context, this)
//                    val customGoods = StampGoodsBean.DataBean()
//                    with(customGoods) {
//                        subject = "0"
//                        modou = "自定义"
//                        money = "--- "
//                    }
//                    this.add(customGoods)
                    with(rvGoods) {
                        adapter = goodsAdapter
                        layoutManager = GridLayoutManager(context, 3)
                        addItemDecoration(GridItemDecoration(15))
                    }
                    goodsAdapter?.setOnSimpleItemListener { index ->
                        unChooseCustomPay()
                        if (this[index].subject == "0") {
                            goodsSubject = this[index].subject
                            tvPay.text = "确认支付 ${this[index].money}元"
                            //showCustomEdit()
                        } else {
                            goodsSubject = this[index].subject
                            tvPay.text = "确认支付 ${this[index].money}元"
                           // hideCustomEdit()
                        }
                    }
                    goodsSubject = this[0].subject
                    tvPay.text = "确认支付 ${this[0].money}元"
                }
            }

            override fun onFail(msg: String?) {

            }
        })
    }

    private fun getWallet() {
        HttpHelper.getInstance().getMyWallet(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val walletData = GsonUtil.GsonToBean(data, WalletData::class.java)
                walletData?.data?.run {
                    tvBalance.text = "我的魔豆: $wallet"
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun payByWeChat() {
        val payObject = JSONObject()
        payObject.put("uid", MyApp.uid)
        payObject.put("subject", goodsSubject)
        payObject.put("appName", "shengmo")
        if (goodsSubject == "0") {
            if (amount < 10) {
                "自定义充值 最低需充值10元".showToast()
                return
            }
            if (amount > 100000) {
                "自定义充值 最高充值100000元".showToast()
                return
            }
            payObject.put("amount", amount)
        }
        WxPayMentTaskManager(context as Activity?, HttpUrl.WXCzcharge, payObject.toString())
    }

    private fun payByAli() {
        val payObject = JSONObject()
        payObject.put("uid", MyApp.uid)
        payObject.put("subject", goodsSubject)
        if (goodsSubject == "0") {
            if (amount < 10) {
                "自定义充值 最低需充值10元".showToast()
                return
            }
            if (amount > 100000) {
                "自定义充值 最高充值100000元".showToast()
                return
            }
            payObject.put("amount", amount)
        }
        AliPayMentTaskManager(context as Activity?, HttpUrl.ALIPAYCzcharge, payObject.toString())
       // payListener?.doAliPay(payObject.toString())
    }

    fun showCustomEdit() {
        if (etCustom.visibility == View.GONE) {
            etCustom.visibility = View.VISIBLE
        }
    }

    fun hideCustomEdit() {
        if (etCustom.visibility == View.VISIBLE) {
            etCustom.visibility = View.GONE
        }
    }

    interface PayListener {
        fun doAliPay(payJson: String)
    }

    var payListener:PayListener? = null

    fun unChooseCustomPay() {
        if (goodsSubject == "0") {
            llCustomPay.setBackgroundResource(R.drawable.bg_vip_center_item_normal)
//            /*隐藏软键盘*/if (imm.isActive()) {
//                imm.hideSoftInputFromWindow(etFragmentRechargeBuyCustom.getApplicationWindowToken(), 0)
//            }
        }
    }

    private fun chooseCustomPay() {
        if (goodsSubject != "0") {
            llCustomPay.setBackgroundResource(R.drawable.bg_vip_center_item_choose)
            goodsSubject = "0"
            goodsAdapter?.changeChooseItem(-1)
            tvPay.text = "确认支付 ${if (etCustom.text.length > 0) etCustom.text.toString() else 0 }元"

        }
    }

    override fun onDismiss() {
        super.onDismiss()
        keyboardHelper?.destroy()
    }
}