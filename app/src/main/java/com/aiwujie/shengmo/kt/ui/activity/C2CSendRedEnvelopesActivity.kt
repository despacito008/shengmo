package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.tim.helper.MessageSendHelper
import com.aiwujie.shengmo.utils.ToastUtil
import com.gyf.immersionbar.ImmersionBar
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import java.util.*

class C2CSendRedEnvelopesActivity:BaseActivity() {

    private lateinit var etBeans:EditText
    private lateinit var etTips:EditText
    private lateinit var tvSend:TextView
    private lateinit var ivBack:ImageView
    private lateinit var cbIsOpen:CheckBox

    lateinit var toUid:String
    var orderId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendredbaoperson)
        ImmersionBar.with(this).transparentBar().init()
        toUid = intent.getStringExtra("targetid")!!
        ivBack = findViewById(R.id.backya)
        etBeans = findViewById(R.id.zong_doudou)
        etTips = findViewById(R.id.et_message)
        tvSend = findViewById(R.id.send_red)
        ivBack.setOnClickListener {
            finish()
        }
        tvSend.clickDelay {
            if (etBeans.text.toString().isEmpty()) {
                ToastUtil.show(this,"红包不能为空")
                return@clickDelay
            }

            orderId = MyApp.uid + toUid + Date().time
            sendRedEnvelopes()
        }
        cbIsOpen = findViewById(R.id.cb_red_bag_isOpen)
    }

    private fun sendRedEnvelopes() {
        HttpHelper.getInstance().sendC2CRedBag(toUid,etBeans.text.toString(),etTips.text.toString(),orderId,cbIsOpen.isChecked,object: HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                sendIMMessage()
            }

            override fun onFail(code: Int, msg: String?) {
               ToastUtil.show(this@C2CSendRedEnvelopesActivity,msg)
            }
        })
    }

    fun sendIMMessage() {
        MessageSendHelper.getInstance().sendTipMessage("你发送了一个红包", "你收到一个红包")
        MessageSendHelper.getInstance().sendRedEnvelopsMessage(orderId,etTips.text.toString(),object:IUIKitCallBack {
            override fun onSuccess(data: Any?) {
                finish()
            }

            override fun onError(module: String?, errCode: Int, errMsg: String?) {}
        })
    }
}