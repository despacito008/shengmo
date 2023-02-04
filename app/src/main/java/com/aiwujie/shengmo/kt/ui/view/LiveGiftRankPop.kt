package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.kt.ui.fragment.LiveGiftRankFragment
import com.aiwujie.shengmo.timlive.view.GiftViewPagerPop
import com.aiwujie.shengmo.utils.TextViewUtil
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil


class LiveGiftRankPop() : DialogFragment(), View.OnClickListener {

    private val TAG = GiftViewPagerPop::class.java.simpleName

    private lateinit var viewpager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var mFragmentManger: FragmentManager

    private val titleList: MutableList<String> = ArrayList()
    private val fragmentList: MutableList<Fragment> = ArrayList()

    lateinit var mContext: Activity
    var mThemeResId: Int? = 0



    fun setData(context: AppCompatActivity, themeResId: Int, anchor_Id: String) {
        mContext = context
        mThemeResId = themeResId
        MyApp.anchor_id = anchor_Id

    }
    fun setData(context: Activity, themeResId: Int, anchor_Id: String,fragmentManger: FragmentManager) {
        mContext = context
        mThemeResId = themeResId
        MyApp.anchor_id = anchor_Id
        mFragmentManger = fragmentManger

    }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view =inflater?.inflate(R.layout.app_dialog_live_gift_rank,null)
//            initParams()
            initView(view !!)
            return view;
        }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    fun initParams( ) {
        val dialogWindow =  dialog.window!!
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        dialogWindow.setBackgroundDrawable(context.resources.getDrawable(R.drawable.bg_live_room_click_anchor))
        dialogWindow.setGravity(Gravity.BOTTOM)
        dialogWindow.setWindowAnimations(R.style.mypopwindow_anim_style) //添加动画

        val params: WindowManager.LayoutParams = dialogWindow.getAttributes()
        params.width = UIUtil.getScreenWidth(context)
        params.height =UIUtil.getScreenHeight(context)  / 3 * 2
        params.gravity = Gravity.BOTTOM
        dialogWindow.attributes = params
    }

    fun initView(view :View) {
        viewpager =view.findViewById(R.id.viewPager)
        tabLayout =view. findViewById(R.id.tablayout)
        tabLayout.setTabMode(TabLayout.MODE_FIXED) //设置tab模式，当前为系统默认模式


        titleList.add(context.getString(R.string.live_local))
        titleList.add("周榜")
        titleList.add("月榜")
        fragmentList.add(LiveGiftRankFragment.newInstance("1"))
        fragmentList.add(LiveGiftRankFragment.newInstance("4"))
        fragmentList.add(LiveGiftRankFragment.newInstance("2"))
        val tabView1 = getTabView(0)
        val tabView2 = getTabView(1)
        val tabView3 = getTabView(2)

        val mAdapter = RankMyPagerAdapter(childFragmentManager, titleList, fragmentList)
        viewpager.adapter = mAdapter
        tabLayout.setupWithViewPager(viewpager) //将TabLayout和ViewPager关联起来。
        tabLayout.getTabAt(0)!!.customView=tabView1
        tabLayout.getTabAt(1)!!.customView=tabView2
        tabLayout.getTabAt(2)!!.customView = tabView3
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                changeTab(position)
                viewpager.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewpager.setCurrentItem(0)
        changeTab(0)


    }


    override fun onClick(v: View?) {

    }

    fun getTabView(position: Int): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.app_tab_live_gift_rank, null)
        val txt_title = view.findViewById<View>(R.id.mFragmentLive_title_this_field) as TextView
        txt_title.setText(titleList.get(position))

        val viewTabBottom = view.findViewById<View>(R.id.mFragmentLive_local_dian)
        viewTabBottom.visibility = View.GONE
        return view
    }

    fun changeTab(index: Int) {
        for (i in 0 until tabLayout.getTabCount()) {
            val tabAt: TabLayout.Tab? = tabLayout.getTabAt(i)
            val tvTabTitle = tabAt!!.customView!!.findViewById<TextView>(R.id.mFragmentLive_title_this_field)
            val viewTabBottom = tabAt.customView!!.findViewById<View>(R.id.mFragmentLive_local_dian)
            if (i == index) {
                TextViewUtil.setBold(tvTabTitle)
                TextViewUtil.setTextSizeWithDp(tvTabTitle, 19)
                tvTabTitle.setTextColor(ContextCompat.getColor(mContext,R.color.titleBlack))
                viewTabBottom.visibility = View.VISIBLE
            } else {
                TextViewUtil.setNormal(tvTabTitle)
                TextViewUtil.setTextSizeWithDp(tvTabTitle, 15)
                tvTabTitle.setTextColor(ContextCompat.getColor(mContext,R.color.gray_999))
                viewTabBottom.visibility = View.GONE
            }
        }
    }



}


