package com.aiwujie.shengmo.kt.ui.fragment.tabtopbar

import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.ranking.NewLiveRankingActivity
import com.aiwujie.shengmo.bean.NewAnchorAuthBean
import com.aiwujie.shengmo.eventbus.HighPageTurnEvent
import com.aiwujie.shengmo.eventbus.MessageEvent
import com.aiwujie.shengmo.eventbus.PlayBackPageTurnEvent
import com.aiwujie.shengmo.kt.ui.activity.AnchorAuthActivity
import com.aiwujie.shengmo.kt.ui.fragment.LiveNewFragment
import com.aiwujie.shengmo.kt.ui.fragment.LiveRedHotFragment
import com.aiwujie.shengmo.kt.ui.fragment.NewLiveFragment
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.kt.util.dp
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.timlive.frag.FragmentLiveRedHot
import com.aiwujie.shengmo.timlive.frag.LiveRankFragment
import com.aiwujie.shengmo.timlive.kt.ui.fragment.LiveUserFollowFragment
import com.aiwujie.shengmo.timlive.net.RoomManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.fragment.tabtopbar
 * @ClassName: HomePageLiveFragment
 * @Author: xmf
 * @CreateDate: 2022/4/22 12:06
 * @Description: 首页 - 直播
 */
class HomePageLiveFragment: TabTopBarFragment() {
    override fun getFragmentList(): List<Fragment> {
//        return arrayListOf(FragmentLiveRedHot(), LiveNewFragment(), LiveUserFollowFragment(), LiveRankFragment())
        return arrayListOf(LiveRedHotFragment(), LiveNewFragment(), LiveUserFollowFragment(), LiveRankFragment())
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("热门", "最新", "关注", "榜单")
    }

    override fun initTabLeftView() {
        super.initTabLeftView()
        var ivRank = ImageView(activity)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivRank.setImageResource(R.drawable.ic_live_top_rank)
        ivRank.layoutParams = params
        ivRank.setPadding(12.dp, 12.dp, 12.dp, 12.dp)
        flLeft.addView(ivRank)
        ivRank.clickDelay {
            gotoRankPage()
        }
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        EventBus.getDefault().register(this)

    }

    override fun initTabRightView() {
        super.initTabRightView()
        var ivStartLive = ImageView(activity)
        val params = FrameLayout.LayoutParams(50.dp, 50.dp)
        params.gravity = Gravity.CENTER
        ivStartLive.setImageResource(R.drawable.ic_live_home_start_live)
        ivStartLive.layoutParams = params
        ivStartLive.setPadding(12.dp, 12.dp, 12.dp, 12.dp)
        flRight.addView(ivStartLive)
        ivStartLive.clickDelay {
            startLive()
        }
    }

    private fun gotoRankPage() {
        val intent = Intent(activity, NewLiveRankingActivity::class.java)
        startActivity(intent)
    }

    private fun startLive() {
        HttpHelper.getInstance().getNewLiveAuth(object : HttpCodeListener {
            override fun onSuccess(data: String) {
                val newAnchorAuthBean = GsonUtil.GsonToBean(data, NewAnchorAuthBean::class.java)
                if (newAnchorAuthBean != null && newAnchorAuthBean.data != null) {
                    if (newAnchorAuthBean.data.is_live == "0") {
                        val intent = Intent(activity, AnchorAuthActivity::class.java)
                        startActivity(intent)
                    } else {
                        RoomManager.createRoom(context, newAnchorAuthBean.data.room_id)
                    }
                }
            }

            override fun onFail(code: Int, msg: String) {
                ToastUtil.show(activity, msg)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: PlayBackPageTurnEvent) {
        Log.v("PlayBackPageTurnEvent",event.position.toString())
        vpTabTopBar.currentItem = event.position

    }
}
