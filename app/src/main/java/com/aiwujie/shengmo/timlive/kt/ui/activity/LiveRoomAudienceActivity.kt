package com.aiwujie.shengmo.timlive.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.getSpValue
import com.aiwujie.shengmo.media.VideoPlayRecyclerView
import com.aiwujie.shengmo.media.VideoPlayRecyclerView2
import com.aiwujie.shengmo.timlive.kt.adapter.LiveRoomAdapter
import com.aiwujie.shengmo.utils.LogUtil
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoManagerLayout

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.timlive.kt.ui.activity
 * @ClassName: LiveRoomAudienceActivity
 * @Author: xmf
 * @CreateDate: 2022/5/17 14:36
 * @Description:
 */
class LiveRoomAudienceActivity : AppCompatActivity() {

    private val rvLiveRoom: VideoPlayRecyclerView2 by lazy { findViewById<VideoPlayRecyclerView2>(R.id.vp_rv_audience) }
    private val ll: LiveVideoManagerLayout by lazy { findViewById<LiveVideoManagerLayout>(R.id.ll_test) }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LiveRoomAudienceActivity::class.java)
            context.startActivity(intent)
        }
    }
    lateinit var adapter: LiveRoomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_live_room_audience)
        //val liveRoom = TRTCLiveRoom.sharedInstance(this)
//        TRTCLiveRoom.sharedInstance(this@LiveRoomAudienceActivity).enterRoom(333227,true,SpKey.PULL_HOST.getSpValue(""),object :TRTCLiveRoomCallback.ActionCallback {
//            override fun onCallback(code: Int, msg: String?) {
//                LogUtil.d("$code $msg")
//                if (code == 0) {
//                    //val adapter = LiveRoomAdapter(this@LiveRoomAudienceActivity)
//                    //rvLiveRoom.setAdapter(adapter)
//                    ll.startAnchorVideo("250385", true) { code, msg ->
//                        LogUtil.d("$code 2 $msg")
//                    }
//                }
//            }
//        })
        adapter = LiveRoomAdapter(this@LiveRoomAudienceActivity)
        rvLiveRoom.setAdapter(adapter)

    }

    override fun onDestroy() {
        super.onDestroy()
        TRTCLiveRoom.sharedInstance(this).exitRoom(null)
    }

    fun enterRoom() {
        TRTCLiveRoom.sharedInstance(this@LiveRoomAudienceActivity).enterRoom(333227, true, SpKey.PULL_HOST.getSpValue(""), object : TRTCLiveRoomCallback.ActionCallback {
            override fun onCallback(code: Int, msg: String?) {
                if (code == 0) {
                    adapter.startLive()
                }
            }
        })

    }

    fun testLinkMic() {
        val mLiveRoom = TRTCLiveRoom.sharedInstance(this@LiveRoomAudienceActivity)
        mLiveRoom.startCameraPreview(true,null,object :TRTCLiveRoomCallback.ActionCallback {
            override fun onCallback(code: Int, msg: String?) {
                LogUtil.d("$code  4  $msg")
                if (code == 0) {
                    mLiveRoom.startPublish("",object :TRTCLiveRoomCallback.ActionCallback {
                        override fun onCallback(code: Int, msg: String?) {
                            LogUtil.d("$code  3  $msg")
                        }
                    })
                }
            }
        })
    }

}
