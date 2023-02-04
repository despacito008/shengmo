package com.aiwujie.shengmo.kt.ui.fragment.tabtopbar

import android.graphics.Color
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.eventbus.FeedLinkPageTurnEvent
import com.aiwujie.shengmo.eventbus.MessageEvent
import com.aiwujie.shengmo.kt.ui.fragment.CommentMessageFragment
import com.aiwujie.shengmo.kt.ui.fragment.FeeLinkUserFragment
import com.aiwujie.shengmo.kt.ui.fragment.HomePageConversationFragment
import com.aiwujie.shengmo.kt.ui.fragment.HomePageConversationTabFragment
import com.aiwujie.shengmo.kt.util.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment.tabtopbar
 * @ClassName: HomePageMessageFragment
 * @Author: xmf
 * @CreateDate: 2022/4/22 16:30
 * @Description: 首页 - 消息
 */
class HomePageMessageFragment : TabTopBarFragment() {
    lateinit var feeLinkUserFragment: FeeLinkUserFragment
    lateinit var commentMessageFragment: CommentMessageFragment

    //lateinit var conversationFragment: ConversationFragment
    lateinit var conversationFragment: HomePageConversationFragment

    private var times = 0
    lateinit var homePageConversationTabFragment: HomePageConversationTabFragment
    override fun getFragmentList(): List<Fragment> {
        feeLinkUserFragment = FeeLinkUserFragment()
        commentMessageFragment = CommentMessageFragment()
//        conversationFragment = HomePageConversationFragment()
        homePageConversationTabFragment = HomePageConversationTabFragment()
        return arrayListOf(feeLinkUserFragment, commentMessageFragment, homePageConversationTabFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("连线", "评论", "聊天")
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        EventBus.getDefault().register(this)

        tabLayout.addOnTabSelectedListenerDsl {
            onTabReselected {
                if (vpTabTopBar.currentItem == 2) {
                    homePageConversationTabFragment.showConversation()
                }
            }
            onTabSelected {
                Log.v("initView", "times:$times")
                Log.v("initView", "onTabSelected")

                    if (vpTabTopBar.currentItem == 2) {
                        if (times  > 1) {
                            if (homePageConversationTabFragment.getCurrentType() == 0) {
                                hideSettingMenu()
                            } else {
                                showSettingMenu()
                            }
                        }
                        times++
                    } else {
                        hideSettingMenu()
                    }

            }
        }
        vpTabTopBar.addOnPageChangeListenerDsl {
            onPageSelected {
                if (vpTabTopBar.currentItem == 2) {
                    Log.v("initView", "addOnPageChangeListenerDsl")
                    if (times >1 ) {
                        homePageConversationTabFragment.showConversation()
                    }
                    times++
                }
            }
        }
    }

    override fun loadData() {
        super.loadData()
        vpTabTopBar.currentItem = 2
        if (commentNum != 0) {
            showCommentRedDot(commentNum)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun initTabLeftView() {
        super.initTabLeftView()
        val tvGroup = TextView(activity)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        tvGroup.text = "群广场"
        tvGroup.textSize = 13f
        tvGroup.gravity = Gravity.CENTER
        tvGroup.layoutParams = params
        flLeft.addView(tvGroup)
        tvGroup.clickDelay {
            gotoGroupSquarePage()
        }
    }

    lateinit var tvMenu: TextView
    override fun initTabRightView() {
        super.initTabRightView()
        val ivMenu = ImageView(activity)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivMenu.setImageResource(R.mipmap.sandian)
        ivMenu.layoutParams = params
        ivMenu.setPadding(12.dp, 12.dp, 12.dp, 12.dp)
        flRight.addView(ivMenu)
        ivMenu.clickDelay {
            showMenu()
        }

        tvMenu = TextView(activity)
        val params2 = FrameLayout.LayoutParams(50.dp, 50.dp)
        params2.gravity = Gravity.CENTER
        tvMenu.text = "设置"
        tvMenu.textSize = 13f
        tvMenu.gravity = Gravity.CENTER
        tvMenu.setBackgroundColor(Color.parseColor("#ffffff"))
        tvMenu.layoutParams = params2
        tvMenu.setPadding(12.dp, 12.dp, 12.dp, 12.dp)
        flRight.addView(tvMenu)
    }

    var commentNum = 0
    fun showCommentRedDot(num: Int) {
        val view: View? = tabLayout.getTabAt(1)?.customView
        commentNum = num
        view?.run {
            val tvUnRead = view.findViewById(R.id.tv_tab_top_bar_unread) as TextView
            tvUnRead.text = num.toString()
            if (num == 0) {
                tvUnRead.visibility = View.GONE
            } else {
                tvUnRead.visibility = View.VISIBLE
            }
            EventBus.getDefault().post(MessageEvent(num, 1))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshMessageCount(event: MessageEvent) {
        if (event.type == 0) {
            val count = event.msgCount
            showChatMessageRedDot(count)

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: FeedLinkPageTurnEvent) {
        vpTabTopBar.currentItem = event.position
    }

    private fun showChatMessageRedDot(num: Int) {
        val view: View? = tabLayout.getTabAt(2)?.customView
        view?.run {
            val tvUnRead = view.findViewById(R.id.tv_tab_top_bar_unread) as TextView
            tvUnRead.text = num.toString()
            if (num == 0) {
                tvUnRead.visibility = View.GONE
            } else {
                tvUnRead.visibility = View.VISIBLE
            }
        }
    }

    private fun gotoGroupSquarePage() {
//        val intent = Intent(activity, GroupSquareActivity::class.java)
//        startActivity(intent)

        com.aiwujie.shengmo.kt.ui.activity.tabtopbar.GroupSquareActivity.startActivity(activity)
    }

    private fun showMenu() {
        when (vpTabTopBar.currentItem) {
            0 -> feeLinkUserFragment.showMenu()
            1 -> commentMessageFragment.showMenu()
            2 -> homePageConversationTabFragment.showMenu()
        }
    }

    fun showSettingMenu() {
        if (tvMenu.visibility == View.GONE) {
            tvMenu.visibility = View.VISIBLE
        }
    }

    fun hideSettingMenu() {
        if (tvMenu.visibility == View.VISIBLE) {
            tvMenu.visibility = View.GONE
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            homePageConversationTabFragment.refreshCallList()
        }
    }

}
