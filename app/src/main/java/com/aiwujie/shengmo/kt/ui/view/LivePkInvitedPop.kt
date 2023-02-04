package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.PkInviteInfoBean
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.utils.UserIdentityUtils
import com.bumptech.glide.Glide
import razerdp.basepopup.BasePopupWindow

class LivePkInvitedPop(context: Context):BasePopupWindow(context) {
    lateinit var ivPopIcon: ImageView
    lateinit var ivPopLevel: ImageView
    lateinit var tvPopName:TextView
    lateinit var cbAutoRefuse:CheckBox
    lateinit var tvPopWatchNum:TextView
    lateinit var tvPopRewardBean:TextView
    lateinit var tvPopRefuse:TextView
    lateinit var tvPopAccept:TextView

    private lateinit var ivItemSex: ImageView
    private lateinit var llItemSexAndAge: LinearLayout
    private lateinit var tvItemAge: TextView
    private lateinit var tvItemRole: TextView
    private lateinit var llItemWealth: LinearLayout
    private lateinit var llItemCharm: LinearLayout
    private lateinit var tvItemWealth: TextView
    private lateinit var tvItemCharm: TextView
    private lateinit var tvItemRoomType:TextView

    lateinit var anchorId:String
    var applyPkInvite = false
    constructor(context:Context,uid:String):this(context) {
        anchorId = uid
        popupGravity = Gravity.BOTTOM
        initView()
        getPkAnchorId()
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.live_dialog_pk_invite)
    }

    fun initView() {
        ivPopIcon = findViewById(R.id.iv_dialog_pk_invite_icon)
        tvPopName = findViewById(R.id.tv_dialog_pk_invite_name)
        ivPopLevel = findViewById(R.id.iv_dialog_pk_invite_level)
        tvItemRoomType = findViewById(R.id.tv_dialog_pk_invite_room_type)
        tvPopWatchNum = findViewById(R.id.tv_dialog_pk_invite_watch_sum)
        tvPopRewardBean = findViewById(R.id.tv_dialog_pk_invite_reward_bean)
        tvPopRefuse = findViewById(R.id.tv_dialog_pk_invite_refuse)
        tvPopAccept = findViewById(R.id.tv_dialog_pk_invite_accept)

        ivItemSex = findViewById(R.id.iv_layout_user_normal_info_sex)
        llItemSexAndAge = findViewById(R.id.ll_layout_user_normal_info_sex_age)
        tvItemAge = findViewById(R.id.tv_layout_user_normal_info_age)
        tvItemRole = findViewById(R.id.tv_layout_user_normal_info_role)
        llItemWealth = findViewById(R.id.ll_layout_user_normal_info_wealth)
        llItemCharm = findViewById(R.id.ll_layout_user_normal_info_charm)
        tvItemWealth = findViewById(R.id.tv_layout_user_normal_info_wealth)
        tvItemCharm = findViewById(R.id.tv_layout_user_normal_info_charm)


        cbAutoRefuse = findViewById(R.id.cb_dialog_pk_invite)

        tvPopRefuse.setOnClickListener {
            pkListener?.doPkRefuse()
            applyPkInvite = true
            dismiss()
        }

        tvPopAccept.setOnClickListener {
            pkListener?.doPkAccept()
            applyPkInvite = true
            dismiss()
        }

        cbAutoRefuse.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (!buttonView!!.isPressed) {
                    return
                }
                pkListener?.doAutoPkRefuse(isChecked)
            }

        })
    }

    private fun getPkAnchorId() {
        HttpHelper.getInstance().getPkAnchorInfo(anchorId,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                var pkInfo = GsonUtil.GsonToBean(data,PkInviteInfoBean::class.java)
                pkInfo?.data?.let {
                    Glide.with(context).load(it.head_pic).into(ivPopIcon)
                    UserIdentityUtils.showUserIdentity(context,it.level_role.toInt(),ivPopLevel)
                    tvPopName.text = it.nickname
                    when(it.sex) {
                        "1" -> {
                            llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                            ivItemSex.setImageResource(R.mipmap.nan)
                        }
                        "2" -> {
                            llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                            ivItemSex.setImageResource(R.mipmap.nv)
                        }
                        else -> {
                            llItemSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                            ivItemSex.setImageResource(R.mipmap.san)
                        }
                    }
                    tvItemAge.text = it.age
                    when(it.role) {
                        "S" -> {
                            tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                            tvItemRole.text = "斯"
                        }
                        "M" -> {
                            tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                            tvItemRole.text = "慕"
                        }
                        "SM" -> {
                            tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                            tvItemRole.text = "双"
                        }

                        else -> {
                            tvItemRole.setBackgroundResource(R.drawable.bg_user_info_sex_other)
                            tvItemRole.text = it.role
                        }
                    }

                    if ("0" == it.wealth_val) {
                        llItemWealth.visibility = View.GONE
                    } else {
                        llItemWealth.visibility = View.VISIBLE
                        if ("1" == it.wealth_val_switch) {
                            tvItemWealth.text = "密"
                        } else {
                            tvItemWealth.text = it.wealth_val
                        }
                    }

                    if ("0" == it.charm_val) {
                        llItemCharm.visibility = View.GONE
                    } else {
                        llItemCharm.visibility = View.VISIBLE
                        if ("1" == it.charm_val_switch) {
                            tvItemCharm.text = "密"
                        } else {
                            tvItemCharm.text = it.charm_val
                        }
                    }

                    if (TextUtils.isEmpty(it.room_type_name)) {
                        tvItemRoomType.visibility = View.GONE
                    } else {
                        tvItemRoomType.visibility = View.VISIBLE
                        tvItemRoomType.text = it.room_type_name
                    }

                    tvPopWatchNum.text = "累计人数 ${it.watch_sum}"
                    tvPopRewardBean.text = "本场魔豆 ${it.beans_current_count}"
                    startRefuseCountTime()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(context,msg)
                dismiss()
            }
        })
    }
    lateinit var handler:Handler
    lateinit var runnable:Runnable
    var limitTime = 8
    fun startRefuseCountTime() {
        handler = Handler(Looper.myLooper())
        runnable = object : Runnable {
            override fun run() {
                //LogUtil.d("time = " + limitTime)
                tvPopRefuse.text = "拒绝（${limitTime}s）"
                limitTime--
                if (limitTime < 0) {
                    if (isShowing) {
                        pkListener?.doPkRefuse()
                        applyPkInvite = true
                        dismiss()
                    }
                } else {
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    override fun dismiss() {
        handler.removeCallbacks(runnable)
        if (!applyPkInvite) {
            pkListener?.doPkRefuse()
        }
        super.dismiss()
    }

    interface OnPkInviteListener {
        fun doPkRefuse()
        fun doPkAccept()
        fun doAutoPkRefuse(autoRefuse:Boolean)
    }

    var pkListener:OnPkInviteListener? = null
}