package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.SystemConfigBean
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener

class LiveAdminOperateActivity : BaseActivity() {

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    private lateinit var etContent: EditText
    private lateinit var rvItem:RecyclerView
    var uid:String? = null
    var operateType = 1

    companion object {
        fun start(context:Context,uid:String) {
            val intent = Intent(context,LiveAdminOperateActivity::class.java)
            intent.putExtra(IntentKey.UID,uid)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_live_admin_operate)
        operateType = intent.getIntExtra("type",1)
        initView()
    }

    fun initView() {
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        tvTitle.text = if (operateType == 1) "警告主播" else "封禁主播"
        ivTitleRight.visibility = View.GONE
        tvTitleRight.visibility = View.VISIBLE
        tvTitleRight.text = "提交"
        etContent = findViewById(R.id.et_admin_live_operate)
        rvItem = findViewById(R.id.rv_admin_live_operate)
        StatusBarUtil.showLightStatusBar(this)
        getWaringData()
    }

    private fun getWaringData() {
        HttpHelper.getInstance().getSystemConfig(object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val config = GsonUtil.GsonToBean(data,SystemConfigBean::class.java)
                config?.data?.run {
                    initWaringData(liveWarningConfig)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                initWaringData()
            }
        })
    }

    private fun initWaringData(waringData:List<String> = arrayListOf("请勿穿着暴露","请不要有不雅动作","请文明用语","请注意聊天内容","请勿推销","请勿抽烟")) {
        val warnAdapter = WarningAdapter(waringData)
        val linearLayoutManager = LinearLayoutManager(this@LiveAdminOperateActivity)
        rvItem.adapter = warnAdapter
        rvItem.layoutManager = linearLayoutManager
        tvTitleRight.setOnClickListener {
            if (operateType == 1) {
                warnAnchor()
            } else {
               showBanDayMenu()
            }
        }
        uid = intent.getStringExtra("uid")
    }


    inner class WarningAdapter(private var waringList:List<String>): RecyclerView.Adapter<WarningAdapter.WaringHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WarningAdapter.WaringHolder {
            return WaringHolder(LayoutInflater.from(this@LiveAdminOperateActivity).inflate(R.layout.app_item_simple_text,parent,false))
        }

        override fun getItemCount(): Int {
            return waringList.size
        }

        override fun onBindViewHolder(holder: WarningAdapter.WaringHolder?, position: Int) {
            holder!!.display(position)
        }

        inner class WaringHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
            private var tvItemWaring: TextView = itemView.findViewById(R.id.tv_item_simple_text)
            fun display(index:Int) {
                tvItemWaring.text = waringList[index]
                tvItemWaring.setOnClickListener {
                    etContent.setText(waringList[index])
                }
            }
        }
    }

    private fun warnAnchor() {
        if (etContent.text.isEmpty()) {
            ToastUtil.show(this@LiveAdminOperateActivity,"请选择理由")
            return
        }
        HttpHelper.getInstance().adminOperateLive(1,uid,etContent.text.toString(),1,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(this@LiveAdminOperateActivity,"警告成功")
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@LiveAdminOperateActivity,msg)
            }

        });
    }

    private fun showBanDayMenu() {
        AlertView(null, null, "取消", null, arrayOf("一天","三天","一周","两周","一个月","永久"),this, AlertView.Style.ActionSheet,
                OnItemClickListener {
                    o, position, data ->
                    when (position) {
                        0 -> banAnchor(1)
                        1 -> banAnchor(3)
                        2 -> banAnchor(7)
                        3 -> banAnchor(14)
                        4 -> banAnchor(31)
                        5 -> banAnchor(99)
                    }
                }).show()
    }

    private fun banAnchor(day:Int) {
        if (etContent.text.isEmpty()) {
           etContent.setText("")
        }
        HttpHelper.getInstance().adminOperateLive(2,uid,etContent.text.toString(),day,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                ToastUtil.show(this@LiveAdminOperateActivity,"封禁成功")
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                ToastUtil.show(this@LiveAdminOperateActivity,msg)
            }
        });
    }
}