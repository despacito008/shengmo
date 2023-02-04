package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.CarListModel
import com.aiwujie.shengmo.decoration.GridItemDecoration
import com.aiwujie.shengmo.kt.adapter.CarInfoAdapter
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.gloading.Gloading

/**
 * @program: newshengmo
 * @description: 商城座驾
 * @author: whl
 * @create: 2022-05-24 16:29
 **/
class AllCarListActivity : BaseActivity() {

    private val recycleView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recycleView) }
    private val ivBack: ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val tvNormalTitleMore: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_more) }
    private val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }

    private var carinfoAdapter: CarInfoAdapter? = null
    private lateinit var list: ArrayList<CarListModel.DataBean>
    private lateinit var gloading: Gloading.Holder
    private  var gridItemDecoration:GridItemDecoration?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.showLightStatusBar(this)
        setContentView(R.layout.app_activity_car_list)
        list = ArrayList()
        tvTitle.text = "全部座驾"
        tvNormalTitleMore.text = "我的车库"
        gloading = Gloading.getDefault().wrap(recycleView)
        ivBack.setOnClickListener {
            finish()
        }
        tvNormalTitleMore.setOnClickListener {
            startActivity(Intent(this,MyCarListActivity::class.java))
        }
            
        getData()
    }




    private fun getData() {
        HttpHelper.getInstance().getCarList(object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                var model = GsonUtil.GsonToBean(data, CarListModel::class.java)
                gloading.showLoadSuccess()
                model?.data?.run {
                    list.clear()
                    list.addAll(this)
                    carinfoAdapter = CarInfoAdapter(this@AllCarListActivity, list)
                    with(recycleView) {
                        adapter = carinfoAdapter
                        layoutManager = GridLayoutManager(this@AllCarListActivity, 2)
                            if (gridItemDecoration == null){
                                gridItemDecoration =GridItemDecoration(20)
                                addItemDecoration(gridItemDecoration)
                        }
                    }
                    carinfoAdapter?.onSimpleItemListener = OnSimpleItemListener {

                        startActivity(Intent(this@AllCarListActivity, CarInfoActivity::class.java).putExtra(IntentKey.ID, list[it].animation_id))

                    }

                }

            }

            override fun onFail(code: Int, msg: String?) {
                if (code == 4000) {
                    gloading.showEmpty()
                }
                msg?.showToast()
            }
        })


    }


}