package com.aiwujie.shengmo.kt.ui.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.eventbus.SessionEvent2
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.*
import com.aiwujie.shengmo.utils.SafeCheckUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.aiwujie.shengmo.view.VerifyAccountPop
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 修改密码
 * */

class EditPasswordActivity : BaseActivity() {

    var phone: String  = ""
    var email: String = ""
    lateinit var etOldText: EditText
    lateinit var etNewText: EditText
    lateinit var confirmButton: Button

    // 标题栏
    lateinit var tvTitle: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_edit_pwd)
        StatusBarUtil.showLightStatusBar(this)
        EventBus.getDefault().register(this)
        phone = intent.getStringExtra("phone")?: ""
        email = intent.getStringExtra("email")?: ""

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        ivTitleRight.visibility = View.INVISIBLE
        tvTitle.text = "修改密码"
        etOldText = findViewById(R.id.et_old_pwd)
        etNewText = findViewById(R.id.et_new_pwd)
        confirmButton = findViewById(R.id.mEditpwd_confirm)


        if (intent.getStringExtra("passwordStatus").isNotEmpty() && "0" == intent.getStringExtra("passwordStatus")) {
            tvTitle.text = "设置密码"
            etOldText.hint = "请输入密码"
            etNewText.hint = "请再次确认密码"
        } else {
            tvTitle.text = "修改密码"
            etOldText.hint = "请输入原密码"
            etNewText.hint = "请输入新密码"
        }
        setOnLisitener()
    }

    var verifyAccountPop: VerifyAccountPop? = null

    fun showVerifyPop(account: String?) {
        verifyAccountPop = VerifyAccountPop(this@EditPasswordActivity, account, "安全验证", "为了保障您的账户安全，需要进行验证")
        verifyAccountPop!!.showPopupWindow()
        verifyAccountPop!!.setOnVerifyCodeCheckListener { code -> editPwd(code) }
    }

    fun showVerifyTypePop() {
        if (phone.isNotEmpty()  && email.isNotEmpty()) {
            AlertView("请选择验证方式", null, "取消", null, arrayOf("手机号", "邮箱"),
                    this, AlertView.Style.ActionSheet, OnItemClickListener { _, position, _ ->
                if (position == 0) {
                    showVerifyPop(phone)
                } else {
                    showVerifyPop(email)
                }
            }).show()
        } else if (phone.isNotEmpty()) {
            showVerifyPop(phone)
        } else if (email.isNotEmpty()) {
            showVerifyPop(email)
        }
    }

    fun setOnLisitener() {
        ivTitleBack.setOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {
            if (etOldText.text.toString().isNotEmpty() && etNewText.text.toString().isNotEmpty()) {
                showVerifyTypePop()
            } else {
                "请填写密码!".showToast();
            }
        }

        etOldText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (etOldText.text.toString().isNotEmpty() && etNewText.text.toString().isNotEmpty()) {
                    confirmButton.setBackgroundResource(R.drawable.bg_enable_edit_pwd)
//                    confirmButton.setTextColor(ContextCompat.getColor(this@EditPasswordActivity,R.color.white))
                } else {
                    confirmButton.setBackgroundResource(R.drawable.item_login_btn2)
//                    confirmButton.setTextColor(ContextCompat.getColor(this@EditPasswordActivity,R.color.white))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        etNewText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (etOldText.text.toString().isNotEmpty() && etNewText.text.toString().isNotEmpty()) {
                    confirmButton.setBackgroundResource(R.drawable.bg_enable_edit_pwd)
//                    confirmButton.setTextColor(ContextCompat.getColor(this@EditPasswordActivity,R.color.white))
                } else {
                    confirmButton.setBackgroundResource(R.drawable.item_login_btn2)
//                    confirmButton.setTextColor(ContextCompat.getColor(this@EditPasswordActivity,R.color.white))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }


    private fun editPwd(code: String) {
        HttpHelper.getInstance().EditPassWord(etOldText.text.toString(), etNewText.text.toString(), code, object : HttpCodeMsgListener {

            override fun onSuccess(data: String?, msg: String?) {
                if (SafeCheckUtil.isActivityFinish(this@EditPasswordActivity)) {
                    return
                }
                msg?.showToast()
                finish()

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun helloEventBus(sessionEvent2: SessionEvent2) {
        val session = sessionEvent2.sessionId
        val bitmap = BitmapFactory.decodeByteArray(sessionEvent2.picByte, 0, sessionEvent2.picByte.size)
        if (verifyAccountPop != null) {
            verifyAccountPop!!.setSession(session)
            verifyAccountPop!!.showImg(bitmap)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}