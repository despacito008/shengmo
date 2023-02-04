package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.bumptech.glide.Glide
import com.donkingliang.labels.LabelsView
import com.donkingliang.labels.LabelsView.*
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean.DataBean.LabelBean
import razerdp.basepopup.BasePopupWindow
import java.util.*

class LiveInfoPop(context: Context) : BasePopupWindow(context) {
    lateinit var ivPoster: ImageView
    lateinit var etTitle: EditText
    lateinit var labelView: LabelsView
    lateinit var tvLabelTips: TextView
    lateinit var tvConfirm: TextView
    lateinit var ivTitleEdit: ImageView
    lateinit var uid: String

    lateinit var tvTicketPrice: TextView
    lateinit var cbTicket: CheckBox
    lateinit var seekbarTicket: SeekBar
    lateinit var groupTicket: Group

    lateinit var etLiveTicket: EditText

    lateinit var consPwd:ConstraintLayout
    lateinit var cbPwd:CheckBox
    lateinit var etPwd:EditText

    var ticketBeans: String = ""

    constructor(context: Context, uid: String) : this(context) {
        this.uid = uid;
        initView()
        popupGravity = Gravity.BOTTOM
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_live_info)
    }

    fun initView() {
        etTitle = findViewById(R.id.et_pop_live_info_title)
        tvConfirm = findViewById(R.id.tv_pop_live_info_confirm)
        ivPoster = findViewById(R.id.iv_pop_live_info_icon)
        labelView = findViewById(R.id.label_pop_live_info)
        tvLabelTips = findViewById(R.id.tv_pop_live_info_label_tip)
        ivTitleEdit = findViewById(R.id.iv_pop_live_info_edit_title)
        tvTicketPrice = findViewById(R.id.tv_start_live_ticket_price)
        cbTicket = findViewById(R.id.cb_start_live_ticket)
        seekbarTicket = findViewById(R.id.seek_bar_ticket)
        groupTicket = findViewById(R.id.group_start_live_ticket)
        etLiveTicket = findViewById(R.id.et_pop_live_info_ticket)
        consPwd = findViewById(R.id.layout_live_info_password)
        cbPwd = findViewById(R.id.cb_start_live_password)
        etPwd = findViewById(R.id.et_live_room_password)

        this.setAdjustInputMethod(true)
        getLiveCardInfo()
        initListener()
    }

    fun initListener() {
        tvConfirm.setOnClickListener {
            changeLiveCardInfo()
        }
        etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                liveTitle = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        ivTitleEdit.setOnClickListener {
            etTitle.isFocusable = true
            etTitle.isFocusableInTouchMode = true
            etTitle.requestFocus()
            etTitle.setSelection(etTitle.text.length)
            val inputManager: InputMethodManager = etTitle.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(etTitle, 0)
        }

        cbTicket.setOnCheckedChangeListener { checkbox, isChecked ->
            if (checkbox.isPressed) {
                if (!ticketPermission) {
                    if (TextUtils.isEmpty(ticketTip)) {
                        ToastUtil.show(context, "您暂时无法使用此功能")
                    } else {
                        ToastUtil.show(context, ticketTip)
                    }
                    cbTicket.isChecked = false
                } else {
                    if (isChecked) {
                        groupTicket.visibility = View.VISIBLE
                        tvTicketPrice.text = "（" + ticketBeans + "魔豆）"
                        removePasswordRoom()
                    } else {
                        groupTicket.visibility = View.GONE
                        tvTicketPrice.text = "（" + "非必选" + "）"
                    }
                }
            }
        }

        seekbarTicket.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvTicketPrice.text = "（" + progress + "魔豆）"
                ticketBeans = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        etLiveTicket.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())) {
                    ticketBeans = 0.toString()
                    //etLiveTicket.setText(ticketBeans)
                    //etLiveTicket.setSelection(etLiveTicket.getText().toString().length)
                } else {
                    val ticketBeansNum = s.toString().toInt()
                    ticketBeans = s.toString()
                    if (ticketBeansNum > 1000) {
                        // ToastUtil.show(context,"门票房最高价格为1000魔豆")
                        ticketBeans = "1000"
                        etLiveTicket.setText(ticketBeans)
                        etLiveTicket.setSelection(etLiveTicket.getText().toString().length)
                    }
                }
                tvTicketPrice.text = "（" + ticketBeans + "魔豆）"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        cbPwd.setOnCheckedChangeListener { buttonView, isChecked ->
            if (cbPwd.isPressed) {
                if (isChecked) {
                    etPwd.visibility = View.VISIBLE
                    removeTicketRoom()
                } else {
                    etPwd.visibility = View.GONE
                    etPwd.setText("")
                }
            }
        }

    }

    var anchorLabelBean: AnchorLiveCardBean.DataBean? = null
    var liveTitle = ""
    var livePoster = ""
    var ticketPermission = false;
    var ticketTip = ""
    private fun getLiveCardInfo() {
        HttpHelper.getInstance().getAnchorCard(uid, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val anchorCard = GsonUtil.GsonToBean(data, AnchorLiveCardBean::class.java)
                anchorCard.data?.run {
                    etTitle.setText(live_title)
                    Glide.with(context).load(live_poster).into(ivPoster)
                    showLiveLabel(this)
                    liveTitle = live_title
                    livePoster = live_poster
                    ticketPermission = anchor_status == "2" || anchor_status == "3"
                    if (is_ticket == "1") {
                        cbTicket.isChecked = true
                        groupTicket.visibility = View.VISIBLE
                        ticketBeans = ticketBeans
                        tvTicketPrice.text = "（" + ticketBeans + "魔豆）"
                        seekbarTicket.progress = ticketBeans.toInt()
                        etLiveTicket.setText(ticketBeans)
                    } else {
                        cbTicket.isChecked = false
                        groupTicket.visibility = View.GONE
                        tvTicketPrice.text = "（" + "非必选" + "）"
                        ticketBeans = "0";
                    }
                    ticketTip = ticket_tips
                    if (is_apply_pwd == "1") {
                        consPwd.visibility = View.VISIBLE
                        if (is_pwd == "1") {
                            cbPwd.isChecked = true
                            etPwd.setText(room_pwd)
                            etPwd.visibility = View.VISIBLE
                        } else {
                            cbPwd.isChecked = false
                        }
                    } else {
                        consPwd.visibility = View.GONE
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context, msg)
            }

        })
    }

    private fun removeTicketRoom() {
        cbTicket.isChecked = false;
        groupTicket.visibility = View.GONE
        tvTicketPrice.text = "（" + "非必选" + "）"
    }

    private fun removePasswordRoom() {
        cbPwd.isChecked = false
        etPwd.visibility = View.GONE
        etPwd.setText("")
    }

    private fun changeLiveCardInfo(isNeedDismiss: Boolean = true) {
        if (cbTicket.isChecked) {
            if (ticketBeans == "0") {
                ToastUtil.show(context, "开启门票房前请输入价格")
                return
            }
        }
        if (cbPwd.isChecked) {
            if (etPwd.text.toString() == "") {
                ToastUtil.show(context,"开启密码房前请输入密码")
                return
            }
        }
        if (anchorLabelBean == null) {
            ToastUtil.show(context, "未获取到直播标签,请稍等一会")
            return
        }
        HttpHelper.getInstance().editAnchorCard(uid, liveTitle, livePoster, getChooseLabel(), if (cbTicket.isChecked) "1" else "0", ticketBeans,
                if (cbPwd.isChecked) "1" else "0",etPwd.text.toString(),object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(context, "修改成功")
                if (isNeedDismiss) {
                    this@LiveInfoPop.dismiss()
                }
                popListener?.doLiveInfoRefresh(liveTitle, livePoster)
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context, msg);
            }

        })
    }

    fun showLiveLabel(labelBean: AnchorLiveCardBean.DataBean) {
        this.anchorLabelBean = labelBean
        labelView.maxSelect = labelBean.max_label_num
        tvLabelTips.text = "标签(最多选择${labelBean.max_label_num}个)"
        val selectIndexList: MutableList<Int> = ArrayList()
        labelView.setLabels(labelBean.label, LabelTextProvider<LabelBean> { _, position, data ->
            if ("1" == data.is_default) {
                selectIndexList.add(position)
            }
            data.name
        })
        labelView.setSelects(selectIndexList)
        labelView.setOnSelectChangeIntercept(OnSelectChangeIntercept { _, _, _, _, position ->
            "1" == labelBean.label[position].is_lock
        })
        labelView.setOnLabelSelectChangeListener(OnLabelSelectChangeListener { _, _, isSelect, position ->
            val bean: LabelBean = anchorLabelBean!!.label[position]
            if (isSelect) {
                bean.is_default = "1"
            } else {
                bean.is_default = "0"
            }
            anchorLabelBean!!.label[position] = bean
        })

        labelView.setOnLabelClickListener(OnLabelClickListener { _, _, position ->
            if ("1" == anchorLabelBean!!.label[position].is_lock) {
                ToastUtil.show(context, "该分区已锁定,无法操作")
            }
        })

        ivPoster.setOnClickListener {
            onSimpleListener?.doSimplePop()
        }
    }

    private fun getChooseLabel(): String? {
        val tid = StringBuilder("")
        for (i in anchorLabelBean!!.label.indices) {
            if ("1" == anchorLabelBean!!.label[i].is_default) {
                tid.append(anchorLabelBean!!.label[i].tid)
                tid.append(",")
            }
        }
        return if (tid.toString().endsWith(",")) tid.substring(0, tid.length - 1) else tid.toString()
    }

    var onSimpleListener: OnSimplePopListener? = null

    fun refreshImageUrl(url: String) {
        livePoster = url;
        Glide.with(context).load(livePoster).into(ivPoster)
        changeLiveCardInfo(false);
    }

    interface OnLiveInfoPopListener {
        fun doLiveInfoRefresh(title: String, cover: String)
    }

    var popListener: OnLiveInfoPopListener? = null
}