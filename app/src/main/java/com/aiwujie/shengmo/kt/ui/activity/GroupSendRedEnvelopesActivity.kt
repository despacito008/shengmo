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
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.tim.helper.MessageSendHelper
import com.aiwujie.shengmo.utils.ToastUtil
import com.gyf.immersionbar.ImmersionBar
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import java.util.*

class GroupSendRedEnvelopesActivity:BaseActivity() {

    private lateinit var etNum:EditText
    private lateinit var etBeans:EditText
    private lateinit var etTips:EditText
    private lateinit var tvSend:TextView
    private lateinit var ivBack:ImageView
    private lateinit var cbIsOpen:CheckBox

    lateinit var toUid:String
    var orderId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendredbao)
        ImmersionBar.with(this).transparentBar().init()
        toUid = intent.getStringExtra("targetid")!!
        ivBack = findViewById(R.id.backya)
        etNum = findViewById(R.id.num_doudou)
        etBeans = findViewById(R.id.zong_doudou)
        etTips = findViewById(R.id.et_message)
        tvSend = findViewById(R.id.send_red)
        ivBack.setOnClickListener {
            finish()
        }
        tvSend.clickDelay {
            if (etNum.text.toString().isEmpty()) {
                ToastUtil.show(this,"红包个数不能为空")
                return@clickDelay
            }
            if (etBeans.text.toString().isEmpty()) {
                ToastUtil.show(this,"红包金额不能为空")
                return@clickDelay
            }
            orderId = MyApp.uid + toUid + Date().time
            sendRedEnvelopes()
        }
        cbIsOpen = findViewById(R.id.cb_red_bag_isOpen)
    }

    private fun sendRedEnvelopes() {
        HttpHelper.getInstance().sendGroupRedBag(toUid,etNum.text.toString(),etBeans.text.toString(),etTips.text.toString(),orderId,cbIsOpen.isChecked,object: HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                sendIMMessage()
            }

            override fun onFail(code: Int, msg: String?) {
               ToastUtil.show(this@GroupSendRedEnvelopesActivity,msg)
            }
        })
    }

    fun sendIMMessage() {
        //MessageSendHelper.getInstance().sendTipMessage("你发送了一个红包", "你收到一个红包")
        MessageSendHelper.getInstance().sendRedEnvelopsMessage(orderId,etTips.text.toString(),object:IUIKitCallBack {
            override fun onSuccess(data: Any?) {
                finish()
            }

            override fun onError(module: String?, errCode: Int, errMsg: String?) {
                "红包消息发送失败，魔豆会在稍后会退回到您的账户中".showToast()
            }
        })
    }
}