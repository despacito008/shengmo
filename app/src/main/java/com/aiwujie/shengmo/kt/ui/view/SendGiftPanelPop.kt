package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.NormalGiftBean
import com.aiwujie.shengmo.bean.PushGoodsBean
import com.aiwujie.shengmo.kt.listener.OnGiftPanelListener
import com.aiwujie.shengmo.utils.ToastUtil
import com.bumptech.glide.Glide
import razerdp.basepopup.BasePopupWindow

class SendGiftPanelPop(context: Context?) : BasePopupWindow(context) {
    lateinit var goodsBean : NormalGiftBean.DataBean.GoodsBean
    var maxCount:Int = Int.MAX_VALUE

    private lateinit var tvGiftName: TextView
    private lateinit var tvGiftPrice: TextView
    private lateinit var tvGiftTotal: TextView
    private lateinit var tvGiftCancel: ImageView
    private lateinit var tvGiftSend: TextView
    private lateinit var ivGift: ImageView
    private lateinit var etGiftNum: EditText
    private lateinit var ivGiftOpen: ImageView
    private lateinit var llGiftOpen: LinearLayout


    constructor(context: Context?,goodsBean: NormalGiftBean.DataBean.GoodsBean) : this(context) {
        this.goodsBean = goodsBean
         initView()
    }

    constructor(context: Context?,goodsBean: NormalGiftBean.DataBean.GoodsBean,maxCount:Int) : this(context) {
        this.goodsBean = goodsBean
        this.maxCount = maxCount
        initView()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.dashang_count_layout)
    }
    var num = 1
    var totalPrice = 1
    var isOpen = true
    fun initView() {
        popupGravity = Gravity.CENTER
        tvGiftName = findViewById(R.id.dashang_count_name)
        tvGiftPrice = findViewById(R.id.dashang_count_price)
        tvGiftTotal = findViewById(R.id.dashang_count_allMoney)
        tvGiftCancel = findViewById(R.id.dashang_count_cancel)
        tvGiftSend = findViewById(R.id.dashang_count_zeng)
        etGiftNum = findViewById(R.id.dashang_count_et)
        ivGift = findViewById(R.id.dashang_count_iv)
        ivGiftOpen = findViewById(R.id.dashang_count_gonggao)
        llGiftOpen = findViewById(R.id.duoxuanya)
        tvGiftName.text = goodsBean.gift_name
        tvGiftPrice.text = goodsBean.gift_beans + "魔豆"
        Glide.with(context).load(goodsBean.gift_image).into(ivGift)
        totalPrice = goodsBean.gift_beans.toInt()
        tvGiftTotal.text = "$totalPrice 魔豆"
        etGiftNum.setText("1")
        etGiftNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s.toString())) {
                    num = s.toString().toInt()
                    if (num <= maxCount) {
                        totalPrice = num * goodsBean.gift_beans.toInt()
                        tvGiftTotal.text = "$totalPrice 魔豆"
                        checkPrice()
                    } else {
                        ToastUtil.show(context,"当前礼物最多可以赠送${maxCount}个")
                        etGiftNum.setText("1")
                        totalPrice = 1 * goodsBean.gift_beans.toInt()
                        tvGiftTotal.text = "$totalPrice 魔豆"
                        checkPrice()
                        etGiftNum.setSelection(1)
                    }
                } else {

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        tvGiftCancel.setOnClickListener {
            dismiss()
        }
        ivGiftOpen.setImageResource(R.mipmap.yuandiantaozi)
        llGiftOpen.setOnClickListener {
            isOpen = !isOpen
            if (isOpen) {
                ivGiftOpen.setImageResource(R.mipmap.yuandiantaozi)
            } else{
                ivGiftOpen.setImageResource(R.mipmap.yuandiantaohui)
            }
        }

        tvGiftSend.setOnClickListener {
            panelListener?.OnGiftPanelChoose(goodsBean,num,totalPrice,isOpen)
        }
    }

    fun checkPrice() {
        if (totalPrice >= 500) {
            isOpen = true
            ivGiftOpen.setImageResource(R.mipmap.yuandiantaozi)
        } else {
            isOpen = true
            ivGiftOpen.setImageResource(R.mipmap.yuandiantaohui)
        }
    }

     private var panelListener: OnGiftPanelListener? = null

    fun setOnPanelListener(panelListener: OnGiftPanelListener) {
        this.panelListener = panelListener
    }
}