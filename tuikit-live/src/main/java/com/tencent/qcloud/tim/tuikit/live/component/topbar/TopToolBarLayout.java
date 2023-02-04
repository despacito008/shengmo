package com.tencent.qcloud.tim.tuikit.live.component.topbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveAnnouncementBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.redenvelopes.LiveRedEnvelopAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.adapter.LiveTitleAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.adapter.TopAudienceListAdapter;
import com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.utils.AnimationUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.ClickUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;
import com.yhao.floatwindow.IFloatWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TopToolBarLayout extends LinearLayout {
    private static final String TAG = "TopAnchorInfoLayout";

    private Context mContext;
    private LinearLayout mLayoutRoot;
    private CircleImageView mImageAnchorIcon;
    private TextView mTextAnchorName;
    private TextView mTextAnchorSex;
    private TextView mTextAnchorType;
    private TextView mTextAnchorWatchNo;
    private TextView mButtonAnchorFollow;
    private TextView mAudienceNumber;
    private RecyclerView mRecycleAudiences;
    private ImageView llAudiencesClose;

    private LinearLayout llLiveTopMenu;
    private TopAudienceListAdapter mTopAudienceListAdapter;
    private TopToolBarDelegate mTopToolBarDelegate;
    private TRTCLiveRoomDef.LiveAnchorInfo mLiveAnchorInfo;
    private TextView tvLiveContestNo;
    private LinearLayout LLLiveContestStarNo; //退出直播间
    private TextView mLiveCoins;              //直播收益
    private TextView mLiveTimeDuration;       //直播时长
    private int duration;                         //计时器

    private LinearLayout llLiveOnlineNum;
    private TextView tvLiveOnlineNum;//当前在线人数

    private ImageView ivIdentity;
    private ImageView ivTopRank;


    private TextView marqueeTitle;
    private TextView marqueeAnnouncement;

    private FrameLayout mFlRedEnvelopes;
    private ImageView mIvRedEnvelopesIcon;
    private TextView mTvRedEnvelopesDot;

    private RecyclerView mRvRedEnvelopes;

    public TopToolBarLayout(Context context) {
        super(context);
        initView(context);
    }

    public TopToolBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TopToolBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutRoot = (LinearLayout) inflate(context, R.layout.live_layout_top_tool_bar, this);
        llLiveTopMenu = mLayoutRoot.findViewById(R.id.ll_layout_top_tool_bar_menu);
        initAnchorInfoView();
        initAudienceRecyclerView();
        initAudienceNumberView();
        initRedEnvelopesView();
        //updateAudienceNumber();

        LinearLayout llRewardBean = findViewById(R.id.ll_alert_gift_list);
        LinearLayout llHourRank = findViewById(R.id.ll_live_contest_number);

        llRewardBean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTopToolBarListener != null) {
                    onTopToolBarListener.doRewardBeanClick();
                }
            }
        });

        llHourRank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTopToolBarListener != null) {
                    onTopToolBarListener.doHourRankClick();
                }
            }
        });

    }

    List<LiveRedEnvelopesBean.DataBean> redList;
    LiveRedEnvelopAdapter redEnvelopAdapter;
    private void initRedEnvelopesView() {
        mFlRedEnvelopes = findViewById(R.id.fl_live_red_envelopes);
        mIvRedEnvelopesIcon = findViewById(R.id.civ_live_red_envelopes_icon);
        mTvRedEnvelopesDot = findViewById(R.id.tv_live_red_envelopes_num);
        mFlRedEnvelopes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTopToolBarListener != null) {
                    onTopToolBarListener.doLiveRedEnvelopesClick(0);
                }
            }
        });
        mRvRedEnvelopes = findViewById(R.id.rv_live_red_envelopes);
        redList = new LinkedList();
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,8);
        //layoutManager.setOrientation(HORIZONTAL);
        redEnvelopAdapter = new LiveRedEnvelopAdapter(mContext,redList);
        mRvRedEnvelopes.setVisibility(View.VISIBLE);
        mRvRedEnvelopes.setLayoutManager(layoutManager);
        mRvRedEnvelopes.setAdapter(redEnvelopAdapter);
        redEnvelopAdapter.setOnRedEnvelopesListener(new LiveRedEnvelopAdapter.OnRedEnvelopesListener() {
            @Override
            public void doRedEnvelopesClick(int index,boolean isReceive) {
                if (onTopToolBarListener != null) {
                    if (isReceive) {
                        onTopToolBarListener.doLiveRedEnvelopesShowResult(index);
                    } else {
                        onTopToolBarListener.doLiveRedEnvelopesClick(index);
                    }
                }
            }
        });
    }


    private void initAnchorInfoView() {
        mImageAnchorIcon = mLayoutRoot.findViewById(R.id.iv_anchor_head);
        mTextAnchorName = mLayoutRoot.findViewById(R.id.tv_anchor_name);
        mTextAnchorSex = mLayoutRoot.findViewById(R.id.tv_live_top_tool_bar_sex);
        mTextAnchorType = mLayoutRoot.findViewById(R.id.tv_live_top_tool_bar_type);
        mTextAnchorWatchNo = mLayoutRoot.findViewById(R.id.tv_audience_number);
        mButtonAnchorFollow = mLayoutRoot.findViewById(R.id.btn_anchor_follow);
        mLiveCoins = mLayoutRoot.findViewById(R.id.tv_live_coins);
        mLiveTimeDuration = mLayoutRoot.findViewById(R.id.tv_time_duration);
        llLiveOnlineNum = mLayoutRoot.findViewById(R.id.ll_live_Online_num);
        tvLiveOnlineNum = mLayoutRoot.findViewById(R.id.tv_live_Online_num);
        ivIdentity = mLayoutRoot.findViewById(R.id.item_identity_icon);
        ivTopRank = mLayoutRoot.findViewById(R.id.iv_live_top_rank);

        mLiveCoins.setText(0 + "");
        // 处理主播头像点击事件
        mImageAnchorIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickAnchorAvatar();
                }
            }
        });

        // 处理主播关注按钮点击事件
        mButtonAnchorFollow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.isFastClick(v.getId())) return;
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickFollow(mLiveAnchorInfo);
                }
            }
        });

        tvLiveOnlineNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickOnlineUser();
                }
            }
        });

        ivTopRank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickTopRank();
                }
            }
        });
        llAudiencesClose = mLayoutRoot.findViewById(R.id.ll_audiences_close);
        llAudiencesClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickClose();
                }
            }
        });

        marqueeTitle = mLayoutRoot.findViewById(R.id.marquee_tv_live_title);
        marqueeTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTopToolBarListener != null) {
                    onTopToolBarListener.doLiveNoticeClick();
                }
            }
        });
//        marqueeTitle.startScroll();
//        marqueeTitle.setOnMarqueeListener(new MarqueeText.OnMarqueeListener() {
//            @Override
//            public void onMarqueeComplete() {
//                if (marqueeTitle.getMyContext().equals("123456")) {
//                    marqueeTitle.setMyContext("alskdjflksajdflksahfhlkdsajflkdsajlkfajlksdjflkjsdlk");
//                } else {
//                    marqueeTitle.setMyContext("123456");
//                }
//            }
//        });

        marqueeAnnouncement = mLayoutRoot.findViewById(R.id.marquee_tv_live_announcement);
        //marqueeAnnouncement.setL2r(false);
       // marqueeAnnouncement.setMySpeed(1);
        marqueeAnnouncement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (announcementList.isEmpty()) {
                    return;
                }
                if (onTopToolBarListener != null) {
                    onTopToolBarListener.doAnnouncementClick(announcementList.getFirst());
                }
            }
        });

    }

    private void initAudienceRecyclerView() {
        mRecycleAudiences = mLayoutRoot.findViewById(R.id.rv_audiences);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleAudiences.setLayoutManager(layoutManager);
        //mRecycleAudiences.addItemDecoration(new SpacesDecoration(mContext, 3, SpacesDecoration.HORIZONTAL));

        //DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        //defaultItemAnimator.setAddDuration(500);
        //defaultItemAnimator.setRemoveDuration(500);
        //mRecycleAudiences.setItemAnimator(defaultItemAnimator);

        mTopAudienceListAdapter = new TopAudienceListAdapter(mContext, new ArrayList<TRTCLiveRoomDef.TRTCLiveUserInfo>(), new TopAudienceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickAudience(audienceInfo);
                }
            }
        });
        mRecycleAudiences.setAdapter(mTopAudienceListAdapter);
    }

    private void initAudienceNumberView() {
        mAudienceNumber = mLayoutRoot.findViewById(R.id.tv_audience_number);
        tvLiveContestNo = mLayoutRoot.findViewById(R.id.tv_live_contest_number);
        LLLiveContestStarNo = mLayoutRoot.findViewById(R.id.ll_live_contest_star_number);
        mAudienceNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopToolBarDelegate != null) {
                    mTopToolBarDelegate.onClickOnlineNum();
                }
            }
        });
    }

    public void setHasFollowed(boolean followed) {
        if (followed) {
            mButtonAnchorFollow.setVisibility(GONE);
        } else {
            mButtonAnchorFollow.setVisibility(VISIBLE);
        }
    }

    boolean isShowTitle = false;
    public void updateLiveTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            marqueeTitle.setVisibility(View.GONE);
        } else {
            isShowTitle = true;
            marqueeTitle.setVisibility(View.VISIBLE);
            rePlayMarqueeTitle(title);
        }
    }

    public void rePlayMarqueeTitle(String title) {
        String content = title.replaceAll("\\n"," ");
        marqueeTitle.setText(content);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(UIUtil.dp2px(mContext, (float) (12 * getLength(content))) + UIUtil.dp2px(mContext,30),UIUtil.dp2px(mContext,25));
        marqueeTitle.setLayoutParams(params1);
        startMarqueeAnim();
    }

    public void setAnchorInfo(TRTCLiveRoomDef.LiveAnchorInfo anchorInfo) {
        mLiveAnchorInfo = anchorInfo;
        mTextAnchorName.setText(!TextUtils.isEmpty(anchorInfo.userName) ? anchorInfo.userName : anchorInfo.userId);
        mTextAnchorWatchNo.setText(!TextUtils.isEmpty(anchorInfo.watchNo) ? anchorInfo.watchNo + "人" : "");

        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(anchorInfo.userId), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                UserIdentityUtils.showUserIdentity(mContext, v2TIMUserFullInfos.get(0).getRole(), ivIdentity);
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
        mTextAnchorSex.setText(!TextUtils.isEmpty(anchorInfo.age) ? anchorInfo.age : "");
        if (("1").equals(anchorInfo.sex)) {
            mTextAnchorSex.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            mTextAnchorSex.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nan), null, null, null);
        } else if ("2".equals(anchorInfo.sex)) {
            mTextAnchorSex.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            mTextAnchorSex.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nv), null, null, null);
        } else if ("3".equals(anchorInfo.sex)) {
            mTextAnchorSex.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            mTextAnchorSex.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.san), null, null, null);
        }
        if ("S".equals(anchorInfo.type)) {
            mTextAnchorType.setText("斯");
            mTextAnchorType.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
        } else if ("M".equals(anchorInfo.type)) {
            mTextAnchorType.setText("慕");
            mTextAnchorType.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
        } else if ("SM".equals(anchorInfo.type)) {
            mTextAnchorType.setText("双");
            mTextAnchorType.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
        } else {
            mTextAnchorType.setBackgroundResource(R.drawable.bg_user_info_sex_other);
            mTextAnchorType.setText(anchorInfo.type);
        }


        if (!TextUtils.isEmpty(anchorInfo.avatarUrl)) {
            Glide.with(TUIKitLive.getAppContext()).load(anchorInfo.avatarUrl).into(mImageAnchorIcon);
        } else {
            Glide.with(TUIKitLive.getAppContext()).load(R.drawable.live_default_head_img).into(mImageAnchorIcon);
        }
    }

    public TRTCLiveRoomDef.LiveAnchorInfo getAnchorInfo() {
        return mLiveAnchorInfo;
    }

    public void updateTopAudienceInfoFromPlatform(List<String> mAnchorUserIdList, List<String> mTopIdList) {
        if (mAnchorUserIdList != null && mAnchorUserIdList.size() > 0) {
            addAudienceListUser(mAnchorUserIdList);
            setAudienceTopListUser(mTopIdList);
        }
    }

    public void addAudienceListUser(List<String> userInfoList) {
        mTopAudienceListAdapter.addAudienceUser(userInfoList);
        //updateAudienceNumber();
    }

    public void addAudienceListUser(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mTopAudienceListAdapter.addAudienceUser(userInfo);
        //updateAudienceNumber();
    }

    public void removeAudienceUser(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mTopAudienceListAdapter.removeAudienceUser(userInfo);
        //updateAudienceNumber();
    }

    /**
     * 累计观看人数
     *
     * @param size
     */
    public void updateAudienceNumber(int size) {
        //int size = mTopAudienceListAdapter.getAudienceListSize();
        Log.d(TAG, "setOnlineNum number = " + size);
        //String audienceNum = mContext.getString(R.string.live_on_line_number, size < 0 ? 0 : size);
        mAudienceNumber.setText((size < 0 ? 0 : size) + "人");
    }

    /**
     * 热度排名
     *
     * @param contestNo
     */
    public void updateContestNo(int contestNo) {
        if (contestNo == 0) {
            tvLiveContestNo.setText(mContext.getResources().getString(R.string.live_top_not_on_the_list));
        } else {
            String contestNum = mContext.getString(R.string.live_contest_number, contestNo);
            tvLiveContestNo.setText(contestNum);
        }
    }

    long startTime;

    /**
     * 累计观看时长
     *
     * @param time
     */
    public void updateDurationTime(long time) {
        if (time != 0) {
            //时间戳 = 当前时间 - 起始时间
            startTime = time;
            long currentTime = System.currentTimeMillis() / 1000;
            duration = (int) (currentTime - startTime);
            setupTime(mLiveTimeDuration);
        } else {
            mLiveTimeDuration.setText("0");
        }
    }

    public void startDurationTime(long time) {
        if (time != 0) {
            //时间戳 = 当前时间 - 起始时间
            startTime = System.currentTimeMillis() / 1000;
            long currentTime = System.currentTimeMillis() / 1000;
            duration = (int) (currentTime - startTime);
            setupTime(mLiveTimeDuration);
        } else {
            mLiveTimeDuration.setText("0");
        }
    }

    Timer timer;

    /**
     * 简易计时器
     *
     * @param timeView
     */
    private void setupTime(final TextView timeView) {
        final Handler handler = new Handler(Looper.getMainLooper());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // int i = duration++;
                        int i = (int) (((System.currentTimeMillis() / 1000)) - startTime);
                        //Log.d("logutil","直播时间" + i);
                        timeView.setText(DateTimeUtil.formatSecondsTo00(i));
                    }
                });
            }
        };

        if (timer == null) {
            timer = new Timer();
            timer.schedule(task, 0, 1000);
        }
    }

    public void removeCountDown() {
        if (timer != null) {
            timer.cancel();
        }
    }

    //当前在线人数
    public void updateOnlineNum(String num) {
        llLiveOnlineNum.setVisibility(VISIBLE);
        tvLiveOnlineNum.setText(num + "麦");
    }

    public void hideOnlineNum() {
        if (llLiveOnlineNum.getVisibility() != GONE) {
            llLiveOnlineNum.setVisibility(GONE);
        }
    }

    public void setTopToolBarDelegate(TopToolBarDelegate delegate) {
        mTopToolBarDelegate = delegate;
    }

    public void updateCurrentBeansCount(String liveProfit) {
        if (!TextUtils.isEmpty(liveProfit) && mLiveCoins != null) {
            mLiveCoins.setText(liveProfit + "");
        }
    }

    public void setAudienceTopListUser(List<String> list) {
        mTopAudienceListAdapter.setAudienceTopListUser(list);
    }

    public interface TopToolBarDelegate {
        void onClickAnchorAvatar();

        void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo);

        void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo);

        void onClickOnlineNum();

        void onClickOnlineUser();

        void onClickTopRank();

        void onClickClose();
    }

    public interface OnTopToolBarListener {
        void doRewardBeanClick();
        void doHourRankClick();
        void doAnnouncementClick(LiveAnnouncementBean.MessageBean messageBean);
        void doLiveNoticeClick();
        void doLiveRedEnvelopesClick(int index);
        void doLiveRedEnvelopesShowResult(int index);
    }

    OnTopToolBarListener onTopToolBarListener;

    public void setOnTopToolBarListener(OnTopToolBarListener onTopToolBarListener) {
        this.onTopToolBarListener = onTopToolBarListener;
    }

    boolean isShowAnnouncement = false;
    private LinkedList<LiveAnnouncementBean.MessageBean> announcementList;

    public void showLiveAnnouncement(LiveAnnouncementBean.MessageBean messageBean) {
        if (announcementList == null) {
            announcementList = new LinkedList<>();
        }
        announcementList.addLast(messageBean);
        if (!isShowAnnouncement) {
            doAnnouncementViewShow();
            isShowAnnouncement = true;
            marqueeAnnouncement.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doInAnim();
                }
            },1000);
        }
    }

    void doInAnim() {
        final LiveAnnouncementBean.MessageBean announcement = announcementList.getFirst();
        //marqueeAnnouncement.setMyContext(announcement.getContent());
       // marqueeAnnouncement.resetScroll();
        Log.d("anim text",announcement.getContent());
        Log.d("anim text"," "+ getLength(announcement.getContent()));
        marqueeAnnouncement.setText(announcement.getContent());
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(UIUtil.dp2px(mContext, (float) (12 * getLength(announcement.getContent()))) + 30,UIUtil.dp2px(mContext,25));
        marqueeAnnouncement.setLayoutParams(params1);

        final int marginLeft = (mLayoutRoot.getWidth() - marqueeAnnouncement.getWidth()) / 2 - UIUtil.dp2px(mContext,30);
        ObjectAnimator inAnim = AnimationUtils.createFadesInFromLtoR(
                marqueeAnnouncement, mLayoutRoot.getMeasuredWidth(), 0, 6000, new AccelerateDecelerateInterpolator());
        inAnim.start();
        inAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (announcement.getContent().length() > 20) {
                    // marqueeAnnouncement.startScroll();
                }
                marqueeAnnouncement.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doOutAnim();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    void doOutAnim() {
       // final int marginLeft = (mLayoutRoot.getWidth() - marqueeAnnouncement.getWidth()) / 2 - UIUtil.dp2px(mContext,30);
        int contentWidth = UIUtil.dp2px(mContext, (float) (13 * getLength(marqueeAnnouncement.getText().toString())) + 30);
        ObjectAnimator outAnim = AnimationUtils.createFadesInFromLtoR(
                marqueeAnnouncement, 0, (-contentWidth), 6000, new AccelerateDecelerateInterpolator());
        outAnim.start();
        outAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                announcementList.removeFirst();
                if (announcementList.isEmpty()) {
                    isShowAnnouncement = false;
                    doAnnouncementViewHide();
                } else {
                    doInAnim();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    void doAnnouncementViewShow() {
        marqueeTitle.setVisibility(View.GONE);
        marqueeAnnouncement.setTranslationX(-mLayoutRoot.getWidth());
        marqueeAnnouncement.setVisibility(View.VISIBLE);
    }

    void doAnnouncementViewHide() {
        marqueeAnnouncement.setVisibility(View.GONE);
        if (isShowTitle && !isClear) {
            marqueeTitle.setVisibility(View.VISIBLE);
        }
    }

    public void hideLiveNoticeView() {
        marqueeTitle.setVisibility(View.GONE);
        isShowTitle = false;
    }

    boolean isClear;
    public void hideLiveNoticeViewWithClear() {
        marqueeTitle.setVisibility(View.GONE);
        llLiveTopMenu.setVisibility(View.GONE);
        isClear = true;
    }

    public void showLiveNoticeViewWithClear() {
        if (isShowTitle) {
            marqueeTitle.setVisibility(View.VISIBLE);
        }
        llLiveTopMenu.setVisibility(View.VISIBLE);
        isClear = false;
    }

    public void refreshLiveNoticeTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            marqueeTitle.setVisibility(View.GONE);
        } else {
            if (isShowTitle) {
              rePlayMarqueeTitle(title);
            }
        }
    }

    public void showLiveRedEnvelopesView(String url,int num) {
//        if (num > 0) {
//            mFlRedEnvelopes.setVisibility(View.VISIBLE);
//            Glide.with(mContext).load(url).error(R.drawable.ic_live_red_envelopes_get).into(mIvRedEnvelopesIcon);
//            if (num > 1) {
//                mTvRedEnvelopesDot.setVisibility(View.VISIBLE);
//                mTvRedEnvelopesDot.setText(String.valueOf(num));
//            } else {
//                mTvRedEnvelopesDot.setVisibility(View.GONE);
//            }
//        }
    }

    public void showLiveRedEnvelopesView(List<LiveRedEnvelopesBean.DataBean> envelopesList) {
        redList.clear();
        redList.addAll(envelopesList);
        redEnvelopAdapter.notifyDataSetChanged();
    }

    public List<LiveRedEnvelopesBean.DataBean> getRedList() {
        return redList;
    }

    public void hideLiveRedEnvelopesView() {
        mFlRedEnvelopes.setVisibility(View.GONE);
        mRvRedEnvelopes.setVisibility(View.GONE);
    }
    ObjectAnimator outAnim;
    public void startMarqueeAnim() {
        if (outAnim != null) {
            outAnim.cancel();
        }
        float marqueeWidth = (float) (UIUtil.dp2px(mContext,12) * getLength(marqueeTitle.getText().toString()));
        outAnim = AnimationUtils.createFadesInFromLtoR(
                marqueeTitle, mLayoutRoot.getWidth(),marqueeWidth > mLayoutRoot.getMeasuredWidth() ? -marqueeWidth * 1.6f : -mLayoutRoot.getMeasuredWidth() * 1.6f, 100*(marqueeTitle.getText().toString().length()) + 15000, new LinearInterpolator());
        outAnim.start();
        outAnim.setRepeatCount(ValueAnimator.INFINITE);
    }

    //判断字符串的真实长度
    public double getLength(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        String chineseZifu = "[\uff01-\uff10]";
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese) || temp.matches(chineseZifu)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        //进位取整
        return Math.ceil(valueLength);
    }

    //使用UnicodeBlock方法判断
    public boolean isChineseByBlock(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT) {
            return true;
        } else {
            return false;
        }
    }

}
