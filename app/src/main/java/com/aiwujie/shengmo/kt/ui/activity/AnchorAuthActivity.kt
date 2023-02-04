package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.PhotoRzActivity
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.NewAnchorAuthBean
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.net.LiveHttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import org.json.JSONObject

class AnchorAuthActivity: BaseActivity() {
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var llBindPhone: ConstraintLayout
    private lateinit var llAuthPhoto: ConstraintLayout
    private lateinit var tvBindPhone: TextView
    private lateinit var tvAuthPhoto: TextView

    private lateinit var btStartAuth: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_anchor_auth)
        StatusBarUtil.showLightStatusBar(this)
        initView()
    }

    fun initView() {
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        llBindPhone = findViewById(R.id.ll_bind_phone)
        llAuthPhoto = findViewById(R.id.ll_auth_photo)
        tvBindPhone = findViewById(R.id.tv_bind_phone_start)
        tvAuthPhoto = findViewById(R.id.tv_auth_photo_start)

        btStartAuth = findViewById(R.id.btn_start_auth)

        tvTitle.text = "申请主播"
        ivTitleRight.visibility = View.INVISIBLE
        ivTitleBack.setOnClickListener { finish() }

        initListener()
        getAuthState()
    }

    fun initListener() {
        tvBindPhone.setOnClickListener {
            var intent = Intent(this, BindingMobileActivity::class.java)
            intent.putExtra("neworchange", "new")
            startActivity(intent)
        }
        tvAuthPhoto.setOnClickListener {
            var intent = Intent(this, PhotoRzActivity::class.java)
            startActivity(intent)
        }
        btStartAuth.setOnClickListener {
            applyAnchor()
        }
    }

    //申请主播
    private fun applyAnchor() {
        LiveHttpHelper.getInstance().applyAnchorNew(object : HttpListener {
            override fun onSuccess(data: String) {
                try {
                    val jsonObject = JSONObject(data)
                    ToastUtil.show(this@AnchorAuthActivity, jsonObject.getString("msg"))
                    //刷新一下页面
                    getAuthState()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail(msg: String) {
                ToastUtil.show(this@AnchorAuthActivity, msg)
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        getAuthState()
    }

    var isAuthPhoto:Boolean = false
    var isBindPhone:Boolean = false
    private fun getAuthState() {
        HttpHelper.getInstance().getNewLiveAuth(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                var authBean:NewAnchorAuthBean? = GsonUtil.GsonToBean(data, NewAnchorAuthBean::class.java)
                var anchorAuthBean:NewAnchorAuthBean.DataBean? = authBean?.data
                anchorAuthBean?.let {
                    when (it.idcardStatus) {
                        "0" -> {
                            tvAuthPhoto.text = "未申请"
                            isAuthPhoto = false
                            tvAuthPhoto.isClickable = true
                            tvAuthPhoto.setTextColor(resources.getColor(R.color.purpleColor))
                        }
                        "1" -> {
                            tvAuthPhoto.text = "已通过"
                            isAuthPhoto = true
                            tvAuthPhoto.isClickable = false
                            tvAuthPhoto.setTextColor(resources.getColor(R.color.lightGray))
                        }
                        "2" -> {
                            tvAuthPhoto.text = "审核中"
                            isAuthPhoto = false
                            tvAuthPhoto.isClickable = false
                            tvAuthPhoto.setTextColor(resources.getColor(R.color.lightGray))
                        }
                    }
                    when (it.bindingMobileStatus) {
                        "0" -> {
                            tvBindPhone.text = "未绑定"
                            isBindPhone = false
                            tvBindPhone.isClickable = true
                            tvBindPhone.setTextColor(resources.getColor(R.color.purpleColor))
                        }
                        "1" -> {
                            tvBindPhone.text = "已绑定"
                            isBindPhone = true
                            tvBindPhone.isClickable = true
                            tvBindPhone.setTextColor(resources.getColor(R.color.lightGray))
                        }
                    }
                    //备注：是否申请过主播认证 0待审核 1 审核通过 2 审核不通过 3 未申请
                    btStartAuth.isClickable = false
                    btStartAuth.setTextColor(resources.getColor(R.color.lightGray))
                    btStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_nor)
                    when(it.applicationAnchorStatus) {
                        "0" -> {
                            btStartAuth.text = "审核中"
                        }
                        "1" -> {
                            btStartAuth.text = "审核通过"
                        }
                        "2" -> {
                            btStartAuth.text = "审核不通过"
                        }
                        "3" -> {
                            btStartAuth.text = "立即申请"
                            if (isAuthPhoto && isBindPhone) {
                                btStartAuth.isClickable = true
                                btStartAuth.setTextColor(resources.getColor(R.color.white))
                                btStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_pre)
                            } else {
                                btStartAuth.isClickable = false
                                btStartAuth.setTextColor(resources.getColor(R.color.lightGray))
                                btStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_nor)
                            }
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@AnchorAuthActivity,msg)
            }
        })
    }

    fun checkAuthState() {
        if (isAuthPhoto && isBindPhone) {
            btStartAuth.isClickable = true
            btStartAuth.setTextColor(resources.getColor(R.color.white))
            btStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_pre)
        } else {
            btStartAuth.isClickable = false
            btStartAuth.setTextColor(resources.getColor(R.color.lightGray))
            btStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_nor)
        }
    }


}