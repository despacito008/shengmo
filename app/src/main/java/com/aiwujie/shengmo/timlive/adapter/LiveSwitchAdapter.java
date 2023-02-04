package com.aiwujie.shengmo.timlive.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.WelcomeActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.view.LiveGiftRankPop;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.timlive.helper.LiveHttpRequest;
import com.aiwujie.shengmo.timlive.bean.LiveRoomInfo;
import com.aiwujie.shengmo.timlive.bean.RewardGiftInfo;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity;
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop;
import com.aiwujie.shengmo.timlive.view.GiftViewPagerPop;
import com.aiwujie.shengmo.timlive.view.MoreLivePop;
import com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAudienceLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.TXRoomService;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop.TAG_AUDIENCE;
import static org.greenrobot.greendao.generator.PropertyType.Int;

/**
 * 上下轮播recycleView-adapter
 */
public class LiveSwitchAdapter extends LiveSlideAdapter<LiveSwitchAdapter.ViewHolder> {
    private static final String TAG = LiveSwitchAdapter.class.getSimpleName();
    private final FragmentManager mFragmentManager;
    private final int mIndex;
    private Activity mContext;
    private List<ScenesRoomInfoBean> list;
    private ViewHolder mHolder;
    private int mPosition;
    private String page = "0";

    public LiveSwitchAdapter(Activity mContext, List<ScenesRoomInfoBean> list, FragmentManager fragmentManager, int index) {
        this.mContext = mContext;
        this.list = list;
        this.mFragmentManager = fragmentManager;
        this.mIndex = index;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_switch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mPosition = position;
        mHolder = holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onPageSelected(final int itemPosition, View itemView) {
        LogUtil.d("加入直播间" + itemPosition);
        TXRoomService.getInstance().setIsEnterRoom(false);
        mHolder = new ViewHolder(itemView);
        mPosition = itemPosition;
        mHolder.bind(list, mPosition);
        if (onLiveRoomChangedListener != null) {
            onLiveRoomChangedListener.onLiveRoomChanged(itemPosition);
        }
    }

    @Override
    public void onPageUnSelected(int itemPosition, View itemView) {
        LogUtil.d("关闭直播间" + itemPosition);
        TUILiveRoomAudienceLayout mTUILiveRoomAudienceLayout = itemView.findViewById(R.id.layout_room_audience);
        if (mTUILiveRoomAudienceLayout != null) {
            LogUtil.d("退出直播间 -- " + itemPosition);
            LiveRoomAudienceFragment audienceFragment = mTUILiveRoomAudienceLayout.getFragment();
            TRTCLiveRoom.sharedInstance(audienceFragment.getContext()).exitRoom(new TRTCLiveRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    if (code == 0) {
                        LogUtil.d("退出成功 -- ");
                    }
                }
            });
        }
    }

    //回退键-展示浮动窗口
    public void onBackPressed(final int pos) {
        if (mHolder != null) {
            mHolder.mTUILiveRoomAudienceLayout.onBackPressed();
            if (mHolder.mTUILiveRoomAudienceLayout.getFragment() == null) {
                return;
            }
            mHolder.mTUILiveRoomAudienceLayout.getFragment().setmOnTouchFloatWindowListener(new FloatWindowLayout.OnTouchFloatWindowListener() {
                @Override
                public void onTouchUpFloatWindow() {
                    RoomManager.enterRoom(mContext, list, pos, FloatWindowLayout.class.getSimpleName());
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TUILiveRoomAudienceLayout mTUILiveRoomAudienceLayout;

        ViewHolder(View itemView) {
            super(itemView);
            mTUILiveRoomAudienceLayout = itemView.findViewById(R.id.layout_room_audience);
        }

        /**
         * 绑定数据
         */
        public void bind(final List<ScenesRoomInfoBean> list, int pos) {
            LogUtil.e("mCurrentPosition: " + pos);
            String cdnUrl = "livepull.aiwujie.com.cn";
            if (!TextUtil.isEmpty((String) SharedPreferencesUtils.getParam(mContext, "pull_host", ""))) {
                cdnUrl = (String) SharedPreferencesUtils.getParam(mContext, "pull_host", "");
            }
            mHolder.mTUILiveRoomAudienceLayout.initWithRoomId(mFragmentManager, Integer.valueOf(list.get(pos).getRoom_id()), list.get(pos).getUid(), true,
                    cdnUrl,
                    list.get(pos).getPullAddress(), LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList));
            mHolder.mTUILiveRoomAudienceLayout.setOnLiveOperateClickListener(onLiveOperateClickListener);
            //xxx进入直播间
            mHolder.mTUILiveRoomAudienceLayout.getFragment().setOnAnchorEnterListener(new LiveRoomAudienceFragment.OnAnchorEnterListener() {
                @Override
                public void OnAnchorEnter(final String anchor_uid) {
                    LiveHttpHelper.getInstance().joinLiveRoom(anchor_uid, new HttpListener() {
                        @Override
                        public void onSuccess(String data) {
                        }

                        @Override
                        public void onFail(String msg) {
                            Log.e(TAG, msg);
                        }
                    });
                }
            });
        }

        TUILiveRoomAudienceLayout.OnLiveOperateClickListener onLiveOperateClickListener = new TUILiveRoomAudienceLayout.OnLiveOperateClickListener() {
            @Override
            public void onMoreLiveClick() {
                showPopupWindow();
            }


            @Override
            public void onLiveSetHeadViewInfo(TopToolBarLayout mLayoutTopToolBar, String mAnchorId) {
                //获取主播信息
                getAnchorInfo(list.get(mPosition).getUid(), mLayoutTopToolBar, mAnchorId);
            }

            @Override
            public void onLiveStarNumber(View view, String anchorId) {
//                GiftViewPagerPop pop = new GiftViewPagerPop(mContext,R.style.BottomFullDialog,anchorId);
//                pop.show();
//                pop.setOnPopListener(new GiftViewPagerPop.OnPopListener() {
//                    @Override
//                    public void doItemClick(String uid) {
//                        if (getLiveRoomAudienceFragment() != null) {
//                            getLiveRoomAudienceFragment().showUserCard(uid);
//                        }
//                    }
//                });

                LiveGiftRankPop liveGiftRankPop = new LiveGiftRankPop();
                liveGiftRankPop.setData(mContext, R.style.BottomFullDialog, anchorId,mFragmentManager);
                liveGiftRankPop.show(mFragmentManager,"LiveGfit");


            }

            @Override
            public void onClickAnchorAvatar(String uid, String anchorId) {
                if (list == null) return;
                TimCustomMessage tb = new TimCustomMessage();
                tb.setUid(uid);
                tb.setAnchor_uid(anchorId);
                final LiveRoomHeadPop pop = new LiveRoomHeadPop(mContext, tb, TAG_AUDIENCE);
                initPopListener(pop, tb.getUid());
                pop.showPopupWindow();

            }

            @Override
            public void onClickAudience(final TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
                TimCustomMessage tb = new TimCustomMessage();
                tb.setUid(audienceInfo.userId);
                tb.setAnchor_uid(list.get(mPosition).getUid());
                final LiveRoomHeadPop pop = new LiveRoomHeadPop(mContext, tb, TAG_AUDIENCE);
                initPopListener(pop, list.get(mPosition).getUid());
                pop.showPopupWindow();
            }

            @Override
            public void followAnchor(String uid, TopToolBarLayout mLayoutTopToolBar) {
                LiveHttpRequest.followUserById(mContext, list.get(mPosition).getUid(), mLayoutTopToolBar);
            }

            @Override
            public void showGiftPanel() {
                String token = SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "");
                mTUILiveRoomAudienceLayout.getFragment().showGiftPanel(LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList), token);
            }

            @Override
            public void reportUser(String mSelfUserId, String uid) {
                Intent intent = new Intent(mContext, ReportActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("source_type", 1);
                mContext.startActivity(intent);
            }

            @Override
            public void onClickContestNo(View v, String anchor_id) {
                GiftRankingListPop pop = new GiftRankingListPop(mContext, R.style.BottomFullDialog, anchor_id);
                pop.show();
            }

            @Override
            public void clickLikeFrequencyControl(final String anchor_id) {
                clickLikeAnchor(anchor_id);
            }

            @Override
            public void closeLive(String anchor_id) {
            }
        };

        //初始化PopupWindow
        private void showPopupWindow() {
            if (!ClickUtils.isFastClick(getLayoutPosition())) {
                MoreLivePop pop = new MoreLivePop(mContext);
                pop.showPopupWindow();
            }
        }
    }

    //触屏点赞
    private void clickLikeAnchor(String anchor_id) {
        LiveHttpHelper.getInstance().likeanchor(anchor_id, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.e(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.e(msg);
            }
        });
    }


    /**
     * 初始化pop
     *
     * @param headPop
     * @param uid
     */
    private void initPopListener(final LiveRoomHeadPop headPop, final String uid) {
        headPop.setOnPopOperateListener(new LiveRoomHeadPop.OnPopOperateListener() {

            @Override
            public void onFollow(TextView tvAnchorFollow, int follow_state, String uid) {
                LiveHttpRequest.followAnchor(tvAnchorFollow, mContext, uid, follow_state);
            }

            @Override
            public void onAt(String uid, String name) {
                headPop.dismiss();
                String sender = !TextUtil.isEmpty(name) ? name : uid;
                String at = "@" + sender;
                mHolder.mTUILiveRoomAudienceLayout.getFragment().onAt(at);
//                LiveHttpHelper.getInstance().getAnchorInfo(uid, new HttpListener() {
//                    @Override
//                    public void onSuccess(String data) {
//                        com.aiwujie.shengmo.timlive.bean.LiveRoomInfo liveRoomInfo = GsonUtil.GsonToBean(data, com.aiwujie.shengmo.timlive.bean.LiveRoomInfo.class);
//                        String  sender = !TextUtil.isEmpty(liveRoomInfo.getData().getNickname()) ? liveRoomInfo.getData().getNickname() : liveRoomInfo.getData().getUid();
//                        String at = "@" + sender;
//                        mHolder.mTUILiveRoomAudienceLayout.getFragment().onAt(at);
//                    }
//
//                    @Override
//                    public void onFail(String msg) {
//                        LogUtil.d(msg);
//                    }
//                });
            }

            @Override
            public void onReport(String mySelfId) {
                Intent intent = new Intent(mContext, ReportActivity.class);
                intent.putExtra("source_type", 1);
                intent.putExtra("uid", mySelfId);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 进入直播间获取消息
     *
     * @param selfUserId
     */
    private void getAnchorInfo(String selfUserId, final TopToolBarLayout mLayoutTopToolBar, final String mAnchorId) {
        LiveHttpHelper.getInstance().getAnchorInfo(selfUserId, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject obj = new JSONObject(data);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            Log.e(TAG, data);
                            LiveRoomInfo liveRoomInfo = GsonUtil.GsonToBean(data, LiveRoomInfo.class);
                            Log.e(TAG, liveRoomInfo.toString());
                            //类型：Number  必有字段  备注：关注状态 1已关注 2没关注 3互相关注 4被关注
                            if ("1".equals(liveRoomInfo.getData().getFollow_state()) || "3".equals(liveRoomInfo.getData().getFollow_state())) {
                                mLayoutTopToolBar.setHasFollowed(true);
                            } else if ("2".equals(liveRoomInfo.getData().getFollow_state()) || "4".equals(liveRoomInfo.getData().getFollow_state())) {
                                mLayoutTopToolBar.setHasFollowed(false);
                            } else {
                                mLayoutTopToolBar.setHasFollowed(true);
                                ;//默认隐藏关注按钮
                            }
                            //设置信息-从最新的接口获取的
                            TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
                            if (liveRoomInfo.getData().getUid().equals(mAnchorId)) {
                                mAnchorInfo.userId = !TextUtil.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : list.get(mIndex).getUid();
                                mAnchorInfo.userName = !TextUtil.isEmpty(liveRoomInfo.getData().getNickname()) ? liveRoomInfo.getData().getNickname() : list.get(mIndex).getNickname();
                                mAnchorInfo.avatarUrl = !TextUtil.isEmpty(liveRoomInfo.getData().getHead_pic()) ? liveRoomInfo.getData().getHead_pic() : list.get(mIndex).getHead_pic();
                                mAnchorInfo.sex = !TextUtil.isEmpty(liveRoomInfo.getData().getSex()) ? liveRoomInfo.getData().getSex() : list.get(mIndex).getSex();
                                mAnchorInfo.age = !TextUtil.isEmpty(liveRoomInfo.getData().getAge()) ? liveRoomInfo.getData().getAge() : list.get(mIndex).getAge();
                                mAnchorInfo.type = !TextUtil.isEmpty(liveRoomInfo.getData().getRole()) ? liveRoomInfo.getData().getRole() : list.get(mIndex).getRole();
                                mAnchorInfo.watchNo = !TextUtil.isEmpty(liveRoomInfo.getData().getWatchsum()) ? liveRoomInfo.getData().getWatchsum() : "0";
                                mLayoutTopToolBar.setAnchorInfo(mAnchorInfo);
                                if (!liveRoomInfo.getData().getNickname().equals(liveRoomInfo.getData().getLive_title())) {
                                    mLayoutTopToolBar.updateLiveTitle(liveRoomInfo.getData().getLive_title());
                                }
                                mLayoutTopToolBar.updateCurrentBeansCount(liveRoomInfo.getData().getBeans_current_count());
                            }
                            break;
                        default:
                            ToastUtil.show(mContext, obj.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {
                Log.e(TAG, msg);
                if (list == null || list.get(mPosition) == null) {
                    return;
                }
                //设置信息-从之前的列表里边拿到的值
                TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
                if (list.get(mIndex).getUid().equals(mAnchorId)) {
                    mAnchorInfo.userId = list.get(mIndex).getUid();
                    mAnchorInfo.userName = list.get(mIndex).getNickname();
                    mAnchorInfo.avatarUrl = list.get(mIndex).getHead_pic();
                    mAnchorInfo.sex = list.get(mIndex).getSex();
                    mAnchorInfo.age = list.get(mIndex).getAge();
                    mAnchorInfo.type = list.get(mIndex).getRole();
                    mLayoutTopToolBar.setAnchorInfo(mAnchorInfo);
                }
            }
        });
    }

    //at用户
    public void onAt(String at) {
        if (!TextUtil.isEmpty(at) && mHolder.mTUILiveRoomAudienceLayout.getFragment() != null) {
            mHolder.mTUILiveRoomAudienceLayout.getFragment().onAt(at);
        }
    }

    //获取fragment实例
    public LiveRoomAudienceFragment getLiveRoomAudienceFragment() {
        if (mHolder != null && mHolder.mTUILiveRoomAudienceLayout != null) {
            return mHolder.mTUILiveRoomAudienceLayout.getFragment();
        } else {
            return null;
        }
    }

    public interface OnLiveRoomChangedListener {
        void onLiveRoomChanged(int position);
    }

    public OnLiveRoomChangedListener onLiveRoomChangedListener;

    public void setOnLiveRoomChangedListener(OnLiveRoomChangedListener onLiveRoomChangedListener) {
        this.onLiveRoomChangedListener = onLiveRoomChangedListener;
    }
}
