package com.aiwujie.shengmo.kt.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.FriendGroupListBean
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.hb.dialog.myDialog.MyAlertInputDialog
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class FriendSetGroupActivity : AppCompatActivity() {

    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    lateinit var ivMore: ImageView
    lateinit var tvMore: TextView
    lateinit var tagLayout: TagFlowLayout
    lateinit var tagAdapter: TagAdapter<String>
    private lateinit var mList: ArrayList<String>
    var mDatalist: ArrayList<FriendGroupListBean.DataBean>? = null

    var fuid: String? = null
    var astr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.showLightStatusBar(this)
        setContentView(R.layout.app_activity_set_friend_group)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivBack = findViewById(R.id.iv_normal_title_back)
        ivMore = findViewById(R.id.iv_normal_title_more)
        tvMore = findViewById(R.id.tv_normal_title_more)
        tagLayout = findViewById(R.id.id_flowlayout)

        fuid = intent.getStringExtra("fuid")

        tvTitle.text = "设置分组"
        tvMore.visibility = View.VISIBLE
        ivMore.visibility = View.GONE
        tvMore.text = "增组"
        mList = ArrayList()
        mDatalist = ArrayList()

        setListener()
        getGroupList()
    }

    fun setListener() {
        ivBack.setOnClickListener {
//            finish()
            addUserGroup()
        }
        tvMore.setOnClickListener {
            val myAlertInputDialog = MyAlertInputDialog(this@FriendSetGroupActivity).builder()
                    .setTitle("新建分组")
                    .setEditText("")
            myAlertInputDialog.setPositiveButton("确认") {
                if (myAlertInputDialog.result != "") {
                    val length = myAlertInputDialog.result.length
                    if (length > 4) {
                        "分组名称限四字以内！".showToast()
                    } else {
                        addGroup(myAlertInputDialog.result)
                        myAlertInputDialog.dismiss()
                    }
                } else {
                    "分组名称不能为空".showToast()
                }
            }.setNegativeButton("取消") { myAlertInputDialog.dismiss() }
            myAlertInputDialog.show()
        }


        tagAdapter = object : TagAdapter<String>(mList) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val tvText = LayoutInflater.from(this@FriendSetGroupActivity).inflate(R.layout.tv_liu_item, tagLayout, false) as TextView
                tvText.text = t
                return tvText
            }

            override fun unSelected(position: Int, view: View?) {
                super.unSelected(position, view)
                mDatalist?.get(position)?.id?.let { getdelfgusers(it, fuid!!) }
            }

            override fun setSelected(position: Int, t: String?): Boolean {
                return super.setSelected(position, t)
            }
        }
        tagLayout.setOnSelectListener(object : TagFlowLayout.OnSelectListener {
            override fun onSelected(selectPosSet: MutableSet<Int>?) {
                astr = ""
//                val it: Iterator<Int> = selectPosSet!!.iterator()
//                while (it.hasNext()) {
//                    val str = it.next()
//                    val s: String? = mDatalist?.get(str)?.id
//                    astr += "$s,"
//                }
//                if (selectPosSet.size > 0) {
//                    astr = astr!!.substring(0, astr!!.length - 1)
//                }
                selectPosSet?.run {
                    this.toList().map {
                        mDatalist?.get(it)?.id
                    }.forEach {
                        astr += "$it,"
                    }.run {
                        if (astr.endsWith(",")) {
                            astr = astr.substring(0, astr.length - 1)
                        }
                    }
                }
                LogUtil.d(astr)
            }
        })
        tagLayout.adapter = tagAdapter
    }


    private fun getGroupList() {
        HttpHelper.getInstance().getFriendGrouping(fuid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, FriendGroupListBean::class.java)
                model?.data?.run {
                    mDatalist?.addAll(this)
                    mList.clear()
                    this.forEach {
                        mList.add(it.fgname)
                    }
                    val indexList = this.filter {
                        it.is_select == "1"
                    }.map {
                        this.indexOf(it)
                    }.toSet()

                    tagAdapter.setSelectedList(indexList)
                }

            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    private fun addUserGroup() {
        if (TextUtils.isEmpty(astr)) {
            finish()
            return
        }
        HttpHelper.getInstance().addUserToGroup(fuid, astr, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                EventBus.getDefault().post("gerenzhuyefenzushuaxin")
                msg?.showToast()
                setResult(200)
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                finish()
            }
        })
    }

    private fun addGroup(string: String) {
        HttpHelper.getInstance().addFriendGrouping(string, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                getGroupList()
                EventBus.getDefault().post("gerenzhuyefenzushuaxin")
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        addUserGroup()
    }

    //将好友移除分组
    private fun getdelfgusers(fgid: String, fuid: String) {
        HttpHelper.getInstance().delGroupingMember(fgid, fuid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                EventBus.getDefault().post("gerenzhuyefenzushuaxin")
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


}