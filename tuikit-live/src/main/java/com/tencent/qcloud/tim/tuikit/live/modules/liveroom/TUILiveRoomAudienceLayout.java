package com.tencent.qcloud.tim.tuikit.live.modules.liveroom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import android.support.annotation.NonNull;
import android.widget.TextView;


import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRoomInfo;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;


public class TUILiveRoomAudienceLayout extends FrameLayout {

    private static final String TAG = "LiveAudienceLayout";

    private TUILiveRoomAudienceDelegate mLiveRoomAudienceDelegate;
    private FragmentManager mFragmentManager;
    private LiveRoomAudienceFragment    mLiveRoomAudienceFragment;

    //初始化fragment单例
    public LiveRoomAudienceFragment getFragment(){
        if(mLiveRoomAudienceFragment  == null)
            mLiveRoomAudienceFragment = new LiveRoomAudienceFragment();
            return mLiveRoomAudienceFragment;
    }

    public TUILiveRoomAudienceLayout(@NonNull Context context) {
        super(context);
    }

    public TUILiveRoomAudienceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.live_layout_live_room_audience, this);
        //mLiveRoomAudienceFragment = new LiveRoomAudienceFragment();
    }

    /**
     * 通过 roomId 初始化观众端
     *
     * @param fragmentManager 用于管理fragment，activity 请通过 getSupportFragmentManager() 传入
     * @param roomId 观众端会自动进入该房间
     * @param anchorId
     * @param useCdn 是否使用CDN进行播放
     * @param cdnURL CDN 播放链接，您可以在直播[控制台](https://console.cloud.tencent.com/live) 配置您的播放域名。
     * @param pullAddress  "http://livepull.aiwujie.com.cn/live/.flv"
     */
    public LiveRoomAudienceFragment initWithRoomId(FragmentManager fragmentManager, int roomId, String anchorId, boolean useCdn, String cdnURL,String pullAddress,String giftUrl) {
        mLiveRoomAudienceFragment = new LiveRoomAudienceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ROOM_ID, roomId);
        bundle.putString(Constants.ANCHOR_ID, anchorId);
        bundle.putBoolean(Constants.USE_CDN, useCdn);
        bundle.putString(Constants.CDN_URL, cdnURL);
        bundle.putString(Constants.PUSHER_ADDRESS,pullAddress);
        bundle.putString(Constants.GIFT_URL,giftUrl);
        mLiveRoomAudienceFragment.setArguments(bundle);

        mFragmentManager = fragmentManager;
        mFragmentManager.beginTransaction()
                .replace(R.id.live_audience_container, mLiveRoomAudienceFragment, "tuikit-live-audience-fragment")
                .commitAllowingStateLoss();
        mLiveRoomAudienceFragment.setOnLiveOperateClickListener(new LiveRoomAudienceFragment.OnLiveOperateClickListener() {
            @Override
            public void onMoreLiveClick() {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.onMoreLiveClick();
                }
            }

            @Override
            public void onLiveSetHeadViewInfo(TopToolBarLayout mLayoutTopToolBar,String mAnchorId) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.onLiveSetHeadViewInfo(mLayoutTopToolBar,mAnchorId);
                }
            }


            @Override
            public void onLiveStarNumber(View view, String anchor_id) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.onLiveStarNumber(view,anchor_id);
                }
            }

            @Override
            public void onClickAnchorAvatar(String uid,String anchorId) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.onClickAnchorAvatar(uid,anchorId);
                }
            }

            @Override
            public void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.onClickAudience(audienceInfo);
                }
            }

            @Override
            public void followAnchor(String uid,TopToolBarLayout mLayoutTopToolBar) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.followAnchor(uid,mLayoutTopToolBar);
                }
            }

            @Override
            public void showGiftPanel() {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.showGiftPanel();
                }
            }

            @Override
            public void reportUser(String mSelfUserId, String uid) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.reportUser(mSelfUserId,uid);
                }
            }

            @Override
            public void onClickContestNo(View v, String anchor_id) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.onClickContestNo(v,anchor_id);
                }
            }

            @Override
            public void clickLikeFrequencyControl(String anchor_id) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.clickLikeFrequencyControl(anchor_id);
                }
            }

            @Override
            public void closeLive(String uid) {
                if(onLiveOperateClickListener != null){
                    onLiveOperateClickListener.closeLive(uid);
                }
            }
        });
        return  mLiveRoomAudienceFragment;
    }


    /**
     * 设置 UI 回调接口
     * @param liveRoomAudienceDelegate
     */
    public void setLiveRoomAudienceDelegate(TUILiveRoomAudienceDelegate liveRoomAudienceDelegate) {
        mLiveRoomAudienceDelegate = liveRoomAudienceDelegate;
        mLiveRoomAudienceFragment.setLiveRoomAudienceDelegate(mLiveRoomAudienceDelegate);
    }


    /**
     * 请在 Activity 的 onBackPress 函数中调用该函数，会主动结束房间
     */
    public void onBackPressed() {
        if (mLiveRoomAudienceFragment != null) {
            mLiveRoomAudienceFragment.onBackPressed();
        }
    }

    public interface TUILiveRoomAudienceDelegate {
        /**
         * 点击界面中的关闭按钮等会回调该通知，可以在Activity中调用finish方法
         */
        void onClose();

        /**
         * UI 组件内部产生错误会通过该接口回调出来
         * @param errorCode 错误码
         * @param message 错误信息
         */
        void onError(int errorCode, String message);
    }

    public interface OnLiveOperateClickListener{
        void onMoreLiveClick();
        void onLiveSetHeadViewInfo(TopToolBarLayout mLayoutTopToolBar,String mAnchorId);
        void onLiveStarNumber(View view,String anchorId);
        void onClickAnchorAvatar(String uid,String anchorId);
        void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo);
        void followAnchor(String uid,TopToolBarLayout mLayoutTopToolBar);

        void showGiftPanel();

        void reportUser(String mSelfUserId, String uid);

        void onClickContestNo(View v, String anchor_id);

        void clickLikeFrequencyControl(String anchor_id);

        void closeLive(String anchor_id);
    }

    public void setOnLiveOperateClickListener(OnLiveOperateClickListener onLiveOperateClickListener) {
        this.onLiveOperateClickListener = onLiveOperateClickListener;
    }

    public OnLiveOperateClickListener onLiveOperateClickListener;

}
