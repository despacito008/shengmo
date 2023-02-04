package com.tencent.liteav.custom;

import android.view.TextureView;
import android.view.View;

//import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayoutManager;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayout;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayoutManager;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class Constents {
    /**
     * 1对1语音通话
     */
    public final static String ONE_TO_ONE_AUDIO_CALL = "1";
    /**
     * 1对多语音通话
     */
    public final static String ONE_TO_MULTIPE_AUDIO_CALL = "2";
    /**
     * 1对1视频通话
     */
    public final static String ONE_TO_ONE_VIDEO_CALL = "3";

    /**
     * 1对多视频通话
     */
    public final static String ONE_TO_MULTIPE_VIDEO_CALL = "4";

    /**
     * 实时语音通话消息描述内容
     */
    public final static String AUDIO_CALL_MESSAGE_DESC = "AUDIO_CALL_MESSAGE_DESC";
    /**
     * 实时视频通话消息描述内容
     */
    public final static String VIDEO_CALL_MESSAGE_DESC = "VIDEO_CALL_MESSAGE_DESC";

    /**
     * 实时语音通话消息拒接
     */
    public final static String AUDIO_CALL_MESSAGE_DECLINE_DESC = "AUDIO_CALL_MESSAGE_DECLINE_DESC";
    /**
     * 实时视频通话消息拒接
     */
    public final static String VIDEO_CALL_MESSAGE_DECLINE_DESC = "VIDEO_CALL_MESSAGE_DECLINE_DESC";

    /**
     * 悬浮窗与TRTCVideoActivity共享的视频View
     */
    public static TRTCVideoLayoutManager mVideoViewLayout;

    /**
     * 悬浮窗与TRTCVideoActivity共享的视频View
     */
    public static TRTCAudioLayoutManager mAudioViewLayout;

    /**
     * 悬浮窗与主播端共享的视频View
     */
    public static TXCloudVideoView mAnchorViewView;

    /**
     * 悬浮窗与主播端共享的视频View
     */
    public static TXCGLSurfaceView mAnchorTextureView;

    /**
     * 悬浮窗是否开启
     */
    public static boolean isShowFloatWindow = false;

    /**
     * 悬浮窗是否开启 - 主播端
     */
    public static boolean isShowAnchorFloatWindow = false;

    /**
     * 语音通话开始计时时间（悬浮窗要显示时间在这里记录开始值）
     */
    public static long audioCallStartTime;


    public static String currentUserID = "";

    //悬浮窗与观众端共享的view
    public static View liveView;

    /**
     * 悬浮窗是否开启 - 观众端
     */
    public static boolean isShowAudienceFloatWindow = false;
}
