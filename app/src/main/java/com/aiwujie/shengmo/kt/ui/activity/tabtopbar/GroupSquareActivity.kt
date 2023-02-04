package com.aiwujie.shengmo.kt.ui.activity.tabtopbar

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.BannerWebActivity
import com.aiwujie.shengmo.activity.GroupSearchKeywordActivity
import com.aiwujie.shengmo.bean.BannerNewData
import com.aiwujie.shengmo.eventbus.GroupSxEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.ui.fragment.GroupClaimFragment
import com.aiwujie.shengmo.kt.ui.fragment.MyGroupFragment
import com.aiwujie.shengmo.kt.ui.fragment.NormalGroupFragment
import com.aiwujie.shengmo.kt.ui.view.AdvertisementView
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.FilterGroupUtils
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity.tabtopbar
 * @ClassName: GroupSquareActivity
 * @Author: xmf
 * @CreateDate: 2022/6/7 19:46
 * @Description:
 */
class GroupSquareActivity : TabTopBarActivity() {

    companion object {
        fun startActivity(context: Context, flag: Int = 0) {
            val intent = Intent(context, GroupSquareActivity::class.java)
            intent.putExtra(IntentKey.FLAG, flag)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGroupHeaderView()
        initListener()
        if (intent.getIntExtra(IntentKey.FLAG,0) == 1) {
            vpTabTopBar.currentItem = 3
        } else {
            vpTabTopBar.currentItem = 0
        }
    }

    fun initListener() {
        vpTabTopBar.addOnPageChangeListenerDsl {
            onPageSelected {
                when (it) {
                    0 -> {
                        headerView.visibility = View.VISIBLE
                    }
                    else -> {
                        headerView.visibility = View.GONE
                    }
                }
                when (it) {
                    3 -> {
                        ivScreen.visibility = View.GONE
                    }
                    else -> {
                        ivScreen.visibility = View.VISIBLE
                    }
                }
            }
        }
        llSearch.setOnClickListener {
            val intent = Intent(this, GroupSearchKeywordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getFragmentList(): List<Fragment> {
        return arrayListOf(NormalGroupFragment.newInstance(0),
                NormalGroupFragment.newInstance(1),
                GroupClaimFragment(), MyGroupFragment())
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("热推", "附近", "认领", "我的")
    }

    override fun initTabLeftView() {
        super.initTabLeftView()
        var ivBack = ImageView(this)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivBack.setImageResource(R.drawable.normal_back)
        ivBack.layoutParams = params
        ivBack.setPadding(17.dp, 17.dp, 17.dp, 17.dp)
        flLeft.addView(ivBack)
        ivBack.setOnClickListener {
            finish()
        }
    }

    lateinit var ivScreen: ImageView
    override fun initTabRightView() {
        super.initTabRightView()
        ivScreen = ImageView(this)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivScreen.layoutParams = params
        ivScreen.setPadding(15.dp, 15.dp, 15.dp, 15.dp)
        flRight.addView(ivScreen)
        ivScreen.clickDelay {
            showMenu()
        }
        if (SpKey.GROUP_FLAG.getSpValue("0") == "0") {
            ivScreen.setImageResource(R.drawable.shuaixuan)
        } else {
            ivScreen.setImageResource(R.drawable.iv_home_select_done)
        }
    }

    lateinit var headerView: View
    lateinit var adView: AdvertisementView
    lateinit var llSearch: LinearLayout
    lateinit var tvRule: TextView
    private fun initGroupHeaderView() {
        headerView = View.inflate(this, R.layout.app_layout_group_square_header, null)
        adView = headerView.findViewById(R.id.ad_view_group)
        llSearch = headerView.findViewById(R.id.ll_normal_search)
        tvRule = headerView.findViewById(R.id.tv_group_rule)
        getBanner()
        llRoot.addView(headerView, 1)
        tvRule.setOnClickListener {
            BannerWebActivity.start(this,HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/21","圣魔")
        }
    }

    private fun getBanner() {
        HttpHelper.getInstance().getBanner(12, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val bannerData = GsonUtil.GsonToBean(data, BannerNewData::class.java)
                adView.initData(bannerData)
                adView.visibility = View.VISIBLE
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    lateinit var mMenuPop:PopupWindow
    private fun showMenu() {
        val menuView = layoutInflater.inflate(R.layout.item_group_pop, null)
        mMenuPop = PopupWindow(menuView)
        this.alphaBackground(0.5f)
        with(mMenuPop) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            setOnDismissListener {
                this@GroupSquareActivity.alphaBackground(1f)
            }
            showAsDropDown(tabLayout)
            animationStyle = R.style.AnimationPreview
        }
        val tvAll = menuView.findViewById<TextView>(R.id.item_group_pop_llAll)
        val tvSexual = menuView.findViewById<TextView>(R.id.item_group_pop_tvSexual)
        if (SpKey.GROUP_FLAG.getSpValue("0") == "0") {
            tvAll.setTextColor(ContextCompat.getColor(this, R.color.purpleColor))
        } else {
            tvSexual.setTextColor(ContextCompat.getColor(this, R.color.purpleColor))
        }
        tvAll.setOnClickListener {
            groupSx(false, "0")
            ivScreen.setImageResource(R.drawable.shuaixuan)
        }
        tvSexual.setOnClickListener {
            val groupSex = SharedPreferencesUtils.getParam(applicationContext, "mygroupSex", "") as String
            val groupSexual = SharedPreferencesUtils.getParam(applicationContext, "mygroupSexual", "") as String
            if (groupSex != "" && groupSexual != "") {
                val whatSexual = FilterGroupUtils.isWhatSexual(groupSex, groupSexual)
                groupSx(true, whatSexual)
                ivScreen.setImageResource(R.drawable.iv_home_select_done)
            } else {
                ToastUtil.show(applicationContext, "请您重新登录,才可以使用此功能...")
            }
        }
    }

    private fun groupSx(isOpen: Boolean, whatSexual: String) {
        SpKey.GROUP_FLAG.saveSpValue(whatSexual)
        if (isOpen) {
            "groupSwitch".saveSpValue("1")
        } else {
            "groupSwitch".saveSpValue("0")
        }
        EventBus.getDefault().post(GroupSxEvent(whatSexual))
        EventBus.getDefault().post("group sx event")
        mMenuPop.dismiss()
    }
}
