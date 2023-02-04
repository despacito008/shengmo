package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.CarListModel
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.adapter.MyCarInfoAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.NormalTipsPop

/**
 * @program: newshengmo
 * @description: 已购买座驾
 * @author: whl
 * @create: 2022-05-26 10:54
 **/
class MyCarListActivity : BaseActivity() {

    private val tvvEmpty: TextView by lazy { findViewById<TextView>(R.id.tv_empty) }
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recycleView) }
    private val tvMore: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_more) }
    private val ivBack: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private var   carInfoAdapter :MyCarInfoAdapter ?= null
    private  var gridItemDecoration:GridItemDecoration?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_my_car)
        StatusBarUtil.showLightStatusBar(this)
        tvMore.setOnClickListener {
            startActivity(Intent(this,CarRecordActivity::class.java))
        }
        ivBack.setOnClickListener {
            finish()
        }
        getData()
    }


    private fun getData() {
        HttpHelper.getInstance().getMyCar(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, CarListModel::class.java)
                model?.data?.run {
                    if (this.size > 0) {
                        tvvEmpty.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                        carInfoAdapter=  MyCarInfoAdapter(this@MyCarListActivity, model.data)
                        with(recyclerView){
                            layoutManager =GridLayoutManager(this@MyCarListActivity,2)
                            adapter =  carInfoAdapter
                            if (gridItemDecoration == null){
                                gridItemDecoration =GridItemDecoration(20)
                                addItemDecoration(gridItemDecoration)
                            }

                        }

                        carInfoAdapter?.onSimpleItemListener = OnSimpleItemListener {
                            if (this[it].is_default == "1"){
                                showDialog("是否取消当前座驾",model.data[it].id)
                            }else {
                                showDialog("是否使用当前座驾",model.data[it].id)
                            }

                        }


                    } else {
                        tvvEmpty.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE

                    }

                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }


    private fun showDialog(str:String ,carId: String){

        val normalTipsPop = NormalTipsPop.Builder(this)
                .setTitle("提示")
                .setInfo(str)
                .setCancelStr("取消")
                .setConfirmStr("确认")
                .build()
        normalTipsPop.showPopupWindow()
        normalTipsPop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
            override fun cancelClick() {
                normalTipsPop.dismiss()
            }

            override fun confirmClick() {
                normalTipsPop.dismiss()
               changeCar(carId)
            }
        })
    }


    private fun changeCar(carId:String){

        HttpHelper.getInstance().switchCar(carId,object :HttpCodeMsgListener{
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
               getData()

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }


}