package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.VipWebActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.CarInfoModel
import com.aiwujie.shengmo.bean.CarListModel
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.adapter.CarRuleAdapter
import com.aiwujie.shengmo.kt.adapter.OpenHighRuleAdapter
import com.aiwujie.shengmo.kt.ui.view.ChoosePayTypePop
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.*
import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * @program: newshengmo
 * @description: 座驾 详情
 * @author: whl
 * @create: 2022-05-25 10:43
 **/
class CarInfoActivity : BaseActivity() {

    private val ivBack: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val ivMore: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_more) }
    private val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val tvMore: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_more) }
    private val llQuestion: LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_call_question) }

    private val svgaImageView: SVGAImageView by lazy { findViewById<SVGAImageView>(R.id.svgaImageView) }
    private val ScreenSvgaView: SVGAImageView by lazy { findViewById<SVGAImageView>(R.id.svgaImageViewScreen) }
    private val tvName: TextView by lazy { findViewById<TextView>(R.id.tv_name) }
    private val tvContent: TextView by lazy { findViewById<TextView>(R.id.tv_content) }
    private val recycleView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recycleView) }
    private val tvBuy: TextView by lazy { findViewById<TextView>(R.id.tv_buy) }
    private var carId: String? = null
    private var buyCarId = ""
    private var animationUrl = ""
    private var questionUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.showLightStatusBar(this)
        setContentView(R.layout.app_activity_car_info)
        carId = intent.getStringExtra(IntentKey.ID)
        tvTitle.text = "购买座驾"
        ivMore.visibility = View.INVISIBLE
        tvMore.text ="试驾"
        tvMore.visibility =View.GONE

        ivBack.setOnClickListener {
            finish()
        }

        tvMore.setOnClickListener {
            ScreenPlaySvga()
        }

        tvBuy.setOnClickListener {
            showPayDialog()
        }

        llQuestion.setOnClickListener {
            if (TextUtils.isEmpty(questionUrl) ){
                return@setOnClickListener
            }
            startActivity(Intent(this,VipWebActivity::class.java).putExtra("path",questionUrl).putExtra("title","常见问题"))
        }

        getData()

    }

    private  fun  ScreenPlaySvga(){
        if (TextUtils.isEmpty(animationUrl)){
            return
        }
        showScreenAnim(animationUrl)
    }

    private fun getData() {
        HttpHelper.getInstance().getCarInfo(carId, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {

                var model = GsonUtil.GsonToBean(data, CarInfoModel::class.java)
                model?.data?.run {
                    showInfo(this)
                }


            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun showInfo(model: CarInfoModel.DataBean) {

        tvName.text = model.name
        tvContent.text = model.content
        animationUrl = model.animation
        questionUrl= model.commonProblemUrl
        var carRuleAdapter = CarRuleAdapter(this@CarInfoActivity, model.product)
        with(recycleView) {
            layoutManager = GridLayoutManager(this@CarInfoActivity, 3)
            adapter = carRuleAdapter
            if (model.product != null  &&  model.product.size >0){
                buyCarId = model.product[0].product_id
            }

            carRuleAdapter.setOnSimpleItemListener(OnSimpleItemListener {
                buyCarId = model.product[it].product_id
            })
        }


        showAnim(model.animation)
    }

    private fun  showPayDialog(){
       var pop= ChoosePayTypePop(this)
        pop.payTypeChooseListener = object : ChoosePayTypePop.OnPayTypeChooseListener{
            override fun doChoosePayType(type: Int) {
                if (type == 1){
                    exchangeCar()
                }else if (type == 2){
                    payByWeChat()
                }else if (type == 3){
                    payByAli()
                }

            }
        }
      pop.showPopupWindow()
    }

    private fun  exchangeCar(){
        HttpHelper.getInstance().exchangeCar(carId,buyCarId,object :HttpCodeListener{
            override fun onSuccess(data: String?) {
                "购买成功".showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }

        })
    }


        private fun payByWeChat() {
            try {
                val `object` = JSONObject()
                `object`.put("animation_id", carId)
                `object`.put("product_id", buyCarId)
                Log.v("WxPayMentManager Token", MyApp.token)
                WxPayMentTaskManager(this, HttpUrl.wechatBuyCar, `object`.toString(), "")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    private fun payByAli() {
        try {
            val `object` = JSONObject()

            `object`.put("animation_id", carId)
            `object`.put("product_id", buyCarId)
            Log.v("WxPayMentManager Token", MyApp.token)
            AliPayMentTaskManager(this, HttpUrl.aliBuyCar, `object`.toString(), "")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun showAnim(url: String) {

        val parser = SVGAParser.shareParser()
        try {
            parser.decodeFromURL(URL(url), object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    svgaImageView.setVideoItem(videoItem)
                    svgaImageView.loops = -1
                    svgaImageView.startAnimation()
                    svgaImageView.callback = object : SVGACallback {
                        override fun onPause() {}
                        override fun onFinished() {
                            svgaImageView.stopAnimation(true)
                        }

                        override fun onRepeat() {}
                        override fun onStep(i: Int, v: Double) {}
                    }
                }

                override fun onError() {
                }
            })
        } catch (e: Exception) {
        }

    }

    private fun showScreenAnim(url: String) {

        val parser = SVGAParser.shareParser()
        try {
            parser.decodeFromURL(URL(url), object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    if (svgaImageView.isAnimating){
                        svgaImageView.pauseAnimation()
                    }


                    ScreenSvgaView.setVideoItem(videoItem)
                    ScreenSvgaView.loops = 1
                    ScreenSvgaView.startAnimation()
                    ScreenSvgaView.callback = object : SVGACallback {
                        override fun onPause() {}
                        override fun onFinished() {
                            if (svgaImageView != null){
                                svgaImageView.startAnimation()
                            }
                            ScreenSvgaView.stopAnimation(true)
                        }

                        override fun onRepeat() {}
                        override fun onStep(i: Int, v: Double) {}
                    }
                }

                override fun onError() {
                }
            })
        } catch (e: Exception) {
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        svgaImageView.clear()
    }


}