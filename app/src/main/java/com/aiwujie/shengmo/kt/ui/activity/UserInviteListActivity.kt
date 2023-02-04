package com.aiwujie.shengmo.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.UserInviteListBean
import com.aiwujie.shengmo.kt.adapter.UserInviteListAdapter
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.ToastUtil

/**
 * 用户邀请列表页面
 */
class UserInviteListActivity : BaseActivity(){
    var uid:String = ""
    lateinit var rvUserInvite:RecyclerView

    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_user_invite_list)
        rvUserInvite = findViewById(R.id.rv_user_invite_list)


        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
//        tvTitleRight = findViewById(R.id.tv_normal_title_more)

        ivTitleRight.visibility = View.INVISIBLE
        tvTitle.text = "邀请列表"
        ivTitleBack.setOnClickListener {
            finish()
        }

        uid = intent.getStringExtra("uid")
        getData()
    }

    fun getData() {
        HttpHelper.getInstance().getUserInviteList(uid,object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                var inviteBean =  GsonUtil.GsonToBean(data,UserInviteListBean::class.java)
                inviteBean.data?.run {
                    var inviteAdapter = UserInviteListAdapter(this@UserInviteListActivity,this)
                    var layoutManager = LinearLayoutManager(this@UserInviteListActivity)
                    rvUserInvite.adapter = inviteAdapter
                    rvUserInvite.layoutManager = layoutManager
                    inviteAdapter.simpleListener = OnSimpleItemListener { position ->
                        var intent = Intent(this@UserInviteListActivity,UserInfoActivity::class.java)
                        intent.putExtra("uid",this@run[position].uid)
                        startActivity(intent)
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
               ToastUtil.show(this@UserInviteListActivity,msg)
            }
        })
    }
}