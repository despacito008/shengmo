package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.HighBeanModel
import com.aiwujie.shengmo.bean.HighUserBean
import com.aiwujie.shengmo.kt.adapter.HomeHighUserAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.OnSimpleItemListener
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.view.gloading.Gloading
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: SearchHighActivity
 * @Author: xmf
 * @CreateDate: 2022/5/10 16:52
 * @Description:
 */
class SearchHighUserActivity:BaseActivity() {
    private val ivBack:ImageView by lazy { findViewById<ImageView>(R.id.iv_normal_title_back) }
    private val tvTitle:TextView by lazy { findViewById<TextView>(R.id.tv_normal_title_title) }
    private val etKey:EditText by lazy { findViewById<EditText>(R.id.et_search_user) }
    private val refreshLayout:SmartRefreshLayout by lazy { findViewById<SmartRefreshLayout>(R.id.smart_refresh_search_user) }
    private val rvSearch:RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_search_user) }
    lateinit var gLoadingHolder: Gloading.Holder
    companion object {
        fun start(context: Context) {
            val intent = Intent(context,SearchHighUserActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_search_user)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle.text = "搜索高端用户"
        gLoadingHolder = Gloading.getDefault().wrap(rvSearch)
        initListener()
    }
    val imm: InputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    fun initListener() {
        ivBack.setOnClickListener { finish() }
        etKey.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    if (imm.isActive) {
                        imm.hideSoftInputFromWindow(
                                v!!.applicationWindowToken, 0)
                    }
                    refreshLayout.autoRefresh()
                    return true
                }
                return false
            }
        })
        refreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 0
                searchHighUser()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                searchHighUser()
            }
        })
    }

    var page = 0
    val mUserList: ArrayList<HighUserBean> by lazy { ArrayList<HighUserBean>() }
    private var homeHighUserAdapter: HomeHighUserAdapter? = null
    fun searchHighUser() {
        HttpHelper.getInstance().searchHighUser(etKey.text.trim().toString(),page,object :HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val model = GsonUtil.GsonToBean(data, HighBeanModel::class.java)
                if (model?.data == null) {
                    gLoadingHolder.showEmpty()
                    return
                }
                if (page == 0 && model.data?.size == 0) {
                    gLoadingHolder.showEmpty()
                } else {
                    gLoadingHolder.showLoadSuccess()
                }
                model.data?.run {
                    when (page) {
                        0 -> {
                            mUserList.clear()
                            mUserList.addAll(this)
                            homeHighUserAdapter = HomeHighUserAdapter(this@SearchHighUserActivity,mUserList)
                            with(rvSearch) {
                                adapter = homeHighUserAdapter
                                layoutManager = LinearLayoutManager(this@SearchHighUserActivity)
                            }
                            homeHighUserAdapter?.setOnSimpleItemListener(OnSimpleItemListener {
                                HighEndUserActivity.start(this@SearchHighUserActivity,mUserList[it].top_id)
                            })
                        }
                        else -> {
                            val tempIndex = mUserList.size
                            mUserList.addAll(this)
                            homeHighUserAdapter?.notifyItemRangeInserted(tempIndex,this.size)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

}
