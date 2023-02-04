package com.aiwujie.shengmo.timlive.kt.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.media.VideoPlayAdapter
import com.aiwujie.shengmo.timlive.kt.ui.activity.LiveRoomAudienceActivity
import com.aiwujie.shengmo.utils.LogUtil
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoManagerLayout

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.timlive.kt.adapter
 * @ClassName: liveRoomAdapter
 * @Author: xmf
 * @CreateDate: 2022/5/17 14:44
 * @Description:
 */
class LiveRoomAdapter : VideoPlayAdapter<LiveRoomAdapter.LiveRoomHolder> {
    lateinit var mContext: Activity

    constructor(activity: Activity) {
        mContext = activity
    }

    inner class LiveRoomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var liveVideoManager: LiveVideoManagerLayout = itemView.findViewById(R.id.live_video_manager)
        fun display(index: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveRoomHolder {
        return LiveRoomHolder(LayoutInflater.from(mContext).inflate(R.layout.app_layout_tim_live_room, parent, false))
    }

    override fun onBindViewHolder(holder: LiveRoomHolder?, position: Int) {
        holder?.display(position)
    }

    override fun getItemCount(): Int {
        return 10
    }
    var llM:LiveVideoManagerLayout? = null
    override fun onPageSelected(itemPosition: Int, itemView: View?) {
        itemView?.run {
            val holder = LiveRoomHolder(itemView)
            llM = holder.liveVideoManager
            TRTCLiveRoom.sharedInstance(mContext).exitRoom(null)
            holder.liveVideoManager.postDelayed({
//                TRTCLiveRoom.sharedInstance(mContext).enterRoom(333227,true,SpKey.PULL_HOST) {
//                    code, msg ->
//                    LogUtil.d("$code  1 $msg")
//                    if (code == 0) {
//                        holder.liveVideoManager.postDelayed({
//                            holder.liveVideoManager.startAnchorVideo("250385",true) {
//                                code, msg ->
//                                LogUtil.d("$code 2 $msg")
//                            }
//                        },1000)
//                    }
//                }
                (mContext as LiveRoomAudienceActivity).enterRoom()
            },1000)


//            holder.liveVideoManager.postDelayed({
//                holder.liveVideoManager.startAnchorVideo("250385", true) { code, msg ->
//                    LogUtil.d("$code 2 $msg")
//                }
//            }, 5000)

//            holder.liveVideoManager.startAnchorVideo("250385", true) { code, msg ->
//                LogUtil.d("$code 2 $msg")
//            }
//            TRTCLiveRoom.sharedInstance(mContext).enterRoom(333227,true,SpKey.PULL_HOST) {
//                    code, msg ->
//                    if (code == 0) {
//                        holder.liveVideoManager.startAnchorVideo("250385", true) { code, msg ->
//                            LogUtil.d("$code 2 $msg")
//                        }
//                    }
//            }
//            holder.liveVideoManager.startAnchorVideo("250385", true) { code, msg ->
//                LogUtil.d("$code 2 $msg")
//            }

        }
    }


    fun startLive() {
        llM?.startAnchorVideo("250385", true) { code, msg ->
            LogUtil.d("$code 2 $msg")
        }




    }
}
