package com.aiwujie.shengmo.kt.ui.fragment

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.PhotoAuthenticationActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.kt.ui.activity.SelfieAuthActivity
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.ImageLoader
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import org.json.JSONObject

class MyPhotoAuthFragment:LazyFragment() {
    lateinit var ivIcon:ImageView
    lateinit var ivLevel:ImageView
    lateinit var btnAuth: Button

    override fun getContentViewId(): Int {
        return R.layout.wdrz_zprz_fraag
    }

    override fun initView(rootView: View) {
        ivIcon = rootView.findViewById(R.id.mPhotorz_icon)
        ivLevel = rootView.findViewById(R.id.mPhotorz_vip)
        btnAuth = rootView.findViewById(R.id.mPhotorz_btn)

        val headUrl = SharedPreferencesUtils.getParam(activity.applicationContext, "headurl", "") as String
        val vip = SharedPreferencesUtils.getParam(activity.applicationContext, SpKey.VIP, "") as String
        val superVip = SharedPreferencesUtils.getParam(activity.applicationContext, SpKey.SVIP, "") as String
        ImageLoader.loadCircleImage(this, headUrl, ivIcon, R.mipmap.morentouxiang)
        when {
            superVip == "1" -> {
                ivLevel.setImageResource(R.drawable.user_svip)
            }
            vip == "1" -> {
                ivLevel.setImageResource(R.drawable.user_vip)
            }
            else -> {
                ivLevel.setImageResource(R.drawable.user_normal)
            }
        }

        btnAuth.setOnClickListener {
            when (mCode) {
                2000 -> {
                    //val intent = Intent(context, PhotoAuthenticationActivity::class.java)
                    val intent = Intent(context, SelfieAuthActivity::class.java)
                    intent.putExtra("retcode", mCode)
                    intent.putExtra("status", status)
                    intent.putExtra("card_face", cardFace)
                    startActivity(intent)
                }
                2001 -> {
                    "审核中，请等待审核结果".showToast()
                }
                2002 ->{
                    //val intent = Intent(context, PhotoAuthenticationActivity::class.java)
                    val intent = Intent(context, SelfieAuthActivity::class.java)
                    intent.putExtra("retcode", mCode)
                    startActivity(intent)
                }
                else -> {
                    "未检测到您的认证状态".showToast()
                }
            }
        }

    }
    var mCode = 0
    var status = ""
    var cardFace = ""
    override fun loadData() {

    }

    private fun getPhotoAuthState() {
        HttpHelper.getInstance().getUserAuthPhoto(MyApp.uid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val obj = JSONObject(data)
                status = obj.optString("status")
                cardFace = obj.optString("card_face")
                when (obj.optInt("retcode").also { mCode = it }) {
                    2000 -> {
                        btnAuth.text = "已认证，重新认证"
                    }
                    2001 -> {
                        btnAuth.text = "审核中"
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                mCode = code
                if (code == 2002) {
                    btnAuth.text = "立即认证"
                } else {
                    msg?.showToast()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        getPhotoAuthState()
    }
}