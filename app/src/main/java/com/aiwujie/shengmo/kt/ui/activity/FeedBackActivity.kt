package com.aiwujie.shengmo.kt.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.eventbus.TokenFailureEvent
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.X_SystemBarUI
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class FeedBackActivity : BaseActivity() {


    // 标题栏
    lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    lateinit var etContent: EditText
    lateinit var tvTextCount: TextView

    val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_feed_back)
        StatusBarUtil.showLightStatusBar(this)

        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        ivTitleRight.visibility = View.GONE
        tvTitleRight.visibility = View.VISIBLE
        tvTitleRight.text = "确定"
        tvTitleRight.textSize = 14F
        tvTitleRight.setTextColor(Color.parseColor("#4b4b4b"))
        tvTitle.text = "意见反馈"

        etContent = findViewById(R.id.mFeedback_content)
        tvTextCount = findViewById(R.id.mFeedback_count)

        setLisitener()

    }

    fun setLisitener() {
        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 256) {
                    "字数已经达到了限制！".showToast()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvTextCount.text = "(" + s!!.length + "/256)"
            }
        })


        ivTitleBack.setOnClickListener {
            finish()
        }
        tvTitleRight.setOnClickListener {
            HttpHelper.getInstance().feedBack(etContent.text.toString(), object : HttpCodeMsgListener {
                override fun onSuccess(data: String?, msg: String?) {
                    val obj = JSONObject(data)
                    handler.post(Runnable {
                        kotlin.run {
                            when (obj.optInt("retcode")) {
                                2000 -> {
                                    obj.optString("msg").showToast()
                                    finish()
                                }
                            }
                        }
                    })
                }

                override fun onFail(code: Int, msg: String?) {
                    when (code) {
                        4001, 4002 -> {
                            msg?.showToast()
                        }
                        50001, 50001 -> {
                            EventBus.getDefault().post(TokenFailureEvent())
                        }
                    }
                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }


}