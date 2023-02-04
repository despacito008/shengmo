package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest;
import com.tencent.qcloud.tim.tuikit.live.base.TUILiveRequestCallback;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveBlindBoxBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveLevelProcessBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelBlindBoxAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.IGiftPanelView;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftPanelAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftViewPagerAdapter;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveGiftChangedEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

/**
 * 送礼物的panelView
 */
public class GiftPanelViewImp extends BottomSheetDialog implements IGiftPanelView, View.OnClickListener {
    private static final String TAG = "GiftPanelViewImp";
    private static List<GiftInfo> giftInfoList;
    private static List<GiftInfo> fansGiftList;
    private static int auspiciousMaxBeans;
    private final String tokenApp;
    private TextView giftTitle, freeGiftTitle;
    private int COLUMNS = 4;
    private int ROWS = 2;
    private String GIFT_DATA_URL;
    private Context mContext;
    private List<View> mGiftViews;     //每页显示的礼物view
    private GiftController mGiftController;
    private LayoutInflater mInflater;
    private LinearLayout mDotsLayout;
    private ViewPager mViewpager;
    private GiftPanelDelegate mGiftPanelDelegate;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private TextView giftCoin;
    private TextView btnCoin;
    private TextView mSpinnerShowGift;
    private Handler mHandler = new Handler();
    private GiftAdapter mGiftAdapter;
    private int presentType = 1;
    private static List<GiftBeanPlatform.KnapsackBean> knapsacks;
    private GiftPanelPopImp giftPanelView;
    private LinearLayout mLineShowGiftArray;
    private ImageView ivShowGiftArray;
    private TextView tvCurrentLevel, tvNextLevel, tvLevelTip;
    private LinearLayout llLevelProgress;
    private ImageView ivCurrentLevel, ivNextLevel;
    private ProgressBar pbLevel;
    private CheckBox cbIsShowLaBa;
    private TextView tvIsShowLaBa;
    private TextView mTvRedEnvelopes;
    private TextView mTvBlindBox;

    private ViewPager mViewPagerGift;

    private boolean isAnchor = false;

    public GiftPanelViewImp(Context context, String url, String token) {
        super(context, R.style.live_action_sheet_theme);
        mContext = context;
        GIFT_DATA_URL = url;
        tokenApp = token;
        mGiftViews = new ArrayList<>();
        setContentView(R.layout.live_dialog_send_gift_panel);
        initView();
        getMyUserLevelDetail();
    }

    public GiftPanelViewImp(Context context, String url, String token, boolean isAnchor) {
        super(context, R.style.live_action_sheet_theme);
        mContext = context;
        this.isAnchor = isAnchor;
        GIFT_DATA_URL = url;
        tokenApp = token;
        mGiftViews = new ArrayList<>();
        setContentView(R.layout.live_dialog_send_gift_panel);
        initView();
        getMyUserLevelDetail();
    }

    String mAnchorId, mRoomId;

    public GiftPanelViewImp(Context context, String url, String token, String anchorId, String roomId) {
        super(context, R.style.live_action_sheet_theme);
        mContext = context;
        mRoomId = roomId;
        GIFT_DATA_URL = url;
        tokenApp = token;
        mAnchorId = anchorId;
        mGiftViews = new ArrayList<>();
        setContentView(R.layout.live_dialog_send_gift_panel);
        initView();
        getMyUserLevelDetail();
    }

    public GiftPanelViewImp(Context context, String url, String token, String anchorId, String roomId, boolean isAnchor) {
        super(context, R.style.live_action_sheet_theme);
        mContext = context;
        this.isAnchor = isAnchor;
        GIFT_DATA_URL = url;
        tokenApp = token;
        mAnchorId = anchorId;
        mRoomId = roomId;
        mGiftViews = new ArrayList<>();
        setContentView(R.layout.live_dialog_send_gift_panel);
        initView();
        getMyUserLevelDetail();
    }

    private void initView() {
        mInflater = LayoutInflater.from(mContext);
        mViewpager = findViewById(R.id.gift_panel_view_pager);
        mViewPagerGift = findViewById(R.id.view_pager_gift);
        mDotsLayout = findViewById(R.id.dots_container);
        giftTitle = findViewById(R.id.dashang_dialog_title);
        freeGiftTitle = findViewById(R.id.dashang_dialog_titletwo);
        mTvRedEnvelopes = findViewById(R.id.dashang_dialog_red_envelopes);
        mTvBlindBox = findViewById(R.id.dashang_dialog_blind_box);
        btnCoin = findViewById(R.id.tv_coin);
        giftCoin = findViewById(R.id.tv_coin_number);
        giftTitle.setOnClickListener(this);
        freeGiftTitle.setOnClickListener(this);
        btnCoin.setOnClickListener(this);
        mSpinnerShowGift = findViewById(R.id.tv_show_gift_array);
        ivShowGiftArray = findViewById(R.id.iv_show_gift_array);
        mLineShowGiftArray = findViewById(R.id.line_show_gift_array);
        if (isAnchor) {
            freeGiftTitle.setVisibility(View.GONE);
        }
        giftPanelView = new GiftPanelPopImp(getContext());
        mLineShowGiftArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        giftPanelView.show(v, mSpinnerShowGift, ivShowGiftArray);
                    }
                });
            }
        });
//        findViewById(R.id.btn_charge).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mGiftPanelDelegate != null) {
//                    Log.d(TAG, "on charge btn click");
//                    mGiftPanelDelegate.onChargeClick();
//                }
//            }
//        });
        findViewById(R.id.btn_send_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGiftController == null) {
                    return;
                }
                GiftInfo giftInfo = mGiftController.getSelectGiftInfo();
                if (giftInfo != null) {
                    if (mSpinnerShowGift != null && !TextUtils.isEmpty(mSpinnerShowGift.getText().toString().trim())) {
                        giftInfo.count = Integer.valueOf(mSpinnerShowGift.getText().toString().trim());
                    } else {
                        giftInfo.count = 1;
                    }
                    if (presentType != 0) {
                        giftInfo.presentType = presentType;
                    }
                }
                if (giftInfo != null && mGiftPanelDelegate != null) {
                    Log.d(TAG, "onGiftItemClick: " + giftInfo);
                    // mGiftPanelDelegate.onGiftItemClick(giftInfo);
                    sendGift(giftInfo);
                } else {
                    ToastUtil.toastShortMessage("请选择礼物");
                }

            }
        });
        setCanceledOnTouchOutside(true);
        // BottomSheetBehavior behavior = new BottomSheetBehavior.from(GiftPanelViewImp.this);

        tvCurrentLevel = findViewById(R.id.tv_gift_current_level);
        tvNextLevel = findViewById(R.id.tv_gift_next_level);
        tvLevelTip = findViewById(R.id.tv_gift_next_level_tips);
        ivCurrentLevel = findViewById(R.id.iv_gift_current_level);
        ivNextLevel = findViewById(R.id.iv_gift_next_level);
        llLevelProgress = findViewById(R.id.ll_live_gift_level_process);
        pbLevel = findViewById(R.id.pb_gift_level_progress);
        llLevelProgress.setOnClickListener(this);


        if (isAnchor) {
            ivCurrentLevel.setImageResource(R.drawable.ic_item_live_anchor_level);
            ivNextLevel.setImageResource(R.drawable.ic_item_live_anchor_level);
            pbLevel.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.pg_anchor_level));
        } else {
            ivCurrentLevel.setImageResource(R.drawable.ic_item_live_audience_level);
            ivNextLevel.setImageResource(R.drawable.ic_item_live_audience_level);
            pbLevel.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.pg_audience_level));
        }

        cbIsShowLaBa = findViewById(R.id.cb_live_gift_is_show);
        tvIsShowLaBa = findViewById(R.id.tv_live_gift_is_show);
        tvIsShowLaBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbIsShowLaBa.setChecked(!cbIsShowLaBa.isChecked());
            }
        });

        mTvRedEnvelopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerGift.setCurrentItem(isAnchor ? 1 : 2);
            }
        });
        mTvBlindBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerGift.setCurrentItem(isAnchor? 2: 3);
            }
        });
        mViewPagerGift.post(new Runnable() {
            @Override
            public void run() {
                //R.id.design_bottom_sheet基本是固定的,不用担心后面API的更改
                BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.design_bottom_sheet));
                if (behavior != null) {
                    behavior.setHideable(false);//此处设置表示禁止BottomSheetBehavior的执行
                }
            }
        });
    }

    /**
     * 初始化礼物面板
     */
    private void initGiftData(List<GiftInfo> giftInfoList) {
        if (mGiftController == null) {
            mGiftController = new GiftController();
            mGiftController.setOnGiftControllerDelegate(new GiftController.OnGiftControllerDelegate() {
                @Override
                public void onClickDataChanged(GiftInfo giftInfo, int position) {
                    onDataChanged(giftInfo);
                }

                @Override
                public void onPopDismiss() {
                    if (giftPanelView != null) {
                        giftPanelView.dismiss();
                    }
                }
            });
        }
        int pageSize = mGiftController.getPagerCount(giftInfoList.size(), COLUMNS, ROWS);
        // 获取页数
        for (int i = 0; i < pageSize; i++) {
            mGiftViews.add(mGiftController.viewPagerItem(mContext, i, giftInfoList, COLUMNS, ROWS));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
            if (pageSize > 1) {
                mDotsLayout.addView(dotsItem(i), params);
            }
        }
        if (pageSize > 1) {
            mDotsLayout.setVisibility(View.VISIBLE);
        } else {
            mDotsLayout.setVisibility(View.INVISIBLE);
        }
        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViews);
        mViewpager.setAdapter(giftViewPagerAdapter);
        mViewpager.addOnPageChangeListener(new PageChangeListener());
        mViewpager.setCurrentItem(0);
        if (pageSize > 1) {
            mDotsLayout.getChildAt(0).setSelected(true);
        }
    }

    void initViewPager() {
        View mGiftPanel = LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_container, mViewpager, false);
        View mknapsackPanel = LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_container, mViewpager, false);
        ViewPager giftViewPager = mGiftPanel.findViewById(R.id.gift_panel_view_pager);
        LinearLayout giftDot = mGiftPanel.findViewById(R.id.dots_container);

        ViewPager knapsackViewPager = mknapsackPanel.findViewById(R.id.gift_panel_view_pager);
        LinearLayout knapsackDot = mknapsackPanel.findViewById(R.id.dots_container);

        List<GiftInfo> knapsackList = new ArrayList<>();
        for (GiftBeanPlatform.KnapsackBean k : knapsacks) {
            GiftInfo giftInfo = new GiftInfo();
            giftInfo.count = Integer.parseInt(TextUtils.isEmpty(k.num) ? k.num : "0");
            giftInfo.giftPicUrl = k.gift_image;
            giftInfo.title = k.gift_name;
            giftInfo.lottieUrl = k.gift_lottieurl;
            giftInfo.giftId = k.gift_id;
            giftInfo.giftBeans = k.gift_beans;
            giftInfo.num = k.num;
            giftInfo.presentType = 2;
            knapsackList.add(giftInfo);
        }

        initGiftData(giftInfoList, giftViewPager, giftDot, 1);
        initGiftData(knapsackList, knapsackViewPager, knapsackDot, 2);

        //粉丝团
        View mFansPanel = LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_container, mViewpager, false);
        ViewPager fansViewPager = mFansPanel.findViewById(R.id.gift_panel_view_pager);
        LinearLayout fansDot = mFansPanel.findViewById(R.id.dots_container);





        //红包
        View mRedEnvelopesPanel =  LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_red_envelopes, mViewpager, false);
        ImageView ivRedEnvelopesPanel = mRedEnvelopesPanel.findViewById(R.id.iv_layout_gift_panel_red_envelopes);
        ivRedEnvelopesPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_SEND_LIVE_RED_ENVELOPES, "", mRoomId));
                if (onNewGiftListener != null) {
                    onNewGiftListener.OnClickRedEnvelop();
                }
                dismiss();
            }
        });

        //盲盒
        final View mBindBoxPanel =  LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_blind_box, mViewpager, false);
       // RecyclerView rvBox = mBindBoxPanel.findViewById(R.id.rv_gift_panel_blind_box);
        //initBlindBox(rvBox);


        List<View> mPanelView = new ArrayList<>();
        mPanelView.add(mGiftPanel);
        if (!isAnchor) {
            mPanelView.add(mknapsackPanel);
        }
        mPanelView.add(mRedEnvelopesPanel);
        mPanelView.add(mBindBoxPanel);
        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mPanelView);
        mViewPagerGift.setAdapter(giftViewPagerAdapter);
        mViewPagerGift.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (isAnchor) {
                    if (i == 0) { //礼物
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        presentType = 1;
                    } else if (i == 1) { //红包
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        presentType = 2;
                        clearPanelSelected(0);
                    } else if (i == 2) {
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        clearPanelSelected(0);
                    }
                } else {
                    if (i == 0) { //礼物
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearPanelSelected(1);
                    } else if (i == 1) { //免费
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearPanelSelected(0);
                    } else if (i == 2) { //红包
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearPanelSelected(1);
                        clearPanelSelected(0);
                    } else if (i == 3) {
                        giftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        clearPanelSelected(1);
                        clearPanelSelected(0);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPagerGift.setCurrentItem(0);
//        mDotsLayout.getChildAt(0).setSelected(true);




    }

    HashMap<Integer, List<View>> viewMap;

    private void initGiftData(List<GiftInfo> giftInfoList, ViewPager mViewpager, LinearLayout mDotsLayout, int type) {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        if (mGiftController == null) {
            mGiftController = new GiftController();
            mGiftController.setOnGiftControllerDelegate(new GiftController.OnGiftControllerDelegate() {
                @Override
                public void onClickDataChanged(GiftInfo giftInfo, int position) {
                    onDataChanged(giftInfo);
                }

                @Override
                public void onPopDismiss() {
                    if (giftPanelView != null) {
                        giftPanelView.dismiss();
                    }
                }
            });
        }
        List<View> mGiftViews = new ArrayList<>();
        int pageSize = mGiftController.getPagerCount(giftInfoList.size(), COLUMNS, ROWS);
        // 获取页数
        for (int i = 0; i < pageSize; i++) {
            mGiftViews.add(mGiftController.viewPagerItem(mContext, i, giftInfoList, COLUMNS, ROWS));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
            if (pageSize > 1) {
                mDotsLayout.addView(dotsItem(i), params);
            }
        }
        if (pageSize > 1) {
            mDotsLayout.setVisibility(View.VISIBLE);
        } else {
            mDotsLayout.setVisibility(View.INVISIBLE);
        }
        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViews);
        mViewpager.setAdapter(giftViewPagerAdapter);
        mViewpager.addOnPageChangeListener(new PageChangeListener2(mDotsLayout, mGiftViews));
        mViewpager.setCurrentItem(0);
        if (pageSize > 1) {
            mDotsLayout.getChildAt(0).setSelected(true);
        }
        viewMap.put(type, mGiftViews);
    }

    /**
     * 当魔豆大于100时候，不显示下拉列表
     *
     * @param giftInfo
     */
    private void onDataChanged(GiftInfo giftInfo) {
        if (giftInfo.price > auspiciousMaxBeans) {//价格大于100魔豆
            mSpinnerShowGift.setTextColor(getResources().getColor(R.color.text_gray1));
            mSpinnerShowGift.setText("1");
            mSpinnerShowGift.setClickable(false);
            mLineShowGiftArray.setClickable(false);
        } else {
            mSpinnerShowGift.setTextColor(getResources().getColor(R.color.black));
            mSpinnerShowGift.setClickable(true);
            mLineShowGiftArray.setClickable(true);
        }
    }

    /**
     * 礼物页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = mInflater.inflate(R.layout.live_layout_gift_dot, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dashang_dialog_title) {
            // freeGiftTitle.setBackground(null);
            //giftTitle.setBackgroundResource(R.drawable.live_confirm_btn_red_bg);
            giftTitle.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
            freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            switchPresent(1);
        } else if (id == R.id.dashang_dialog_titletwo) {
            //freeGiftTitle.setBackgroundResource(R.drawable.live_confirm_btn_red_bg);
            // giftTitle.setBackground(null);
            giftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
            switchPresent(2);
        } else if (id == R.id.tv_coin) {
            dismiss();
            if (onGiftPanelOperateListener != null) {
                onGiftPanelOperateListener.OnGiftPanel();
            }
            if (mGiftPanelDelegate != null) {
                mGiftPanelDelegate.onChargeClick();
            }
        } else if (id == R.id.ll_live_gift_level_process) {
            if (onGiftPanelOperateListener != null) {
                onGiftPanelOperateListener.OnGiftLevelClick();
            }
        }
    }

    //切换系统礼物
    private void switchPresent(final int presentType) {
        mViewPagerGift.setCurrentItem(presentType-1);
        this.presentType = presentType;
        if (true) {
            return;
        }
        //清除页面加载项
        mGiftViews.clear();
        mDotsLayout.removeAllViews();
        if (presentType == 1) {//收费礼物
            if (giftInfoList == null) {
                ToastUtil.toastShortMessage("您还没有收费礼物哦...");
                return;
            }
            initGiftData(giftInfoList);
        } else {//免费礼物
            if (knapsacks == null) {
                ToastUtil.toastShortMessage("您还没有系统礼物哦...");
                return;
            }
            List<GiftInfo> giftInfoList = new ArrayList<>();
            for (GiftBeanPlatform.KnapsackBean k : knapsacks) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.count = Integer.valueOf(TextUtils.isEmpty(k.num) ? k.num : "0");
                giftInfo.giftPicUrl = k.gift_image;
                giftInfo.title = k.gift_name;
                giftInfo.lottieUrl = k.gift_lottieurl;
                giftInfo.giftId = k.gift_id;
                giftInfo.giftBeans = k.gift_beans;
                giftInfo.num = k.num;
                giftInfo.presentType = presentType;
                giftInfoList.add(giftInfo);
            }
            initGiftData(giftInfoList);
        }
    }

    /**
     * 礼物页改变时，dots效果也要跟着改变
     */
    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(position).setSelected(true);
            for (int i = 0; i < mGiftViews.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) mGiftViews.get(i);
                GiftPanelAdapter adapter = (GiftPanelAdapter) view.getAdapter();
                if (mGiftController != null) {
                    int selectPageIndex = mGiftController.getSelectPageIndex();
                    adapter.clearSelection(selectPageIndex);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 礼物页改变时，dots效果也要跟着改变
     */
    class PageChangeListener2 implements ViewPager.OnPageChangeListener {
        LinearLayout mDotsLayout;
        List<View> mGiftViews;

        public PageChangeListener2(LinearLayout dotLayout, List<View> giftView) {
            mDotsLayout = dotLayout;
            mGiftViews = giftView;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(position).setSelected(true);
            for (int i = 0; i < mGiftViews.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) mGiftViews.get(i);
                GiftPanelAdapter adapter = (GiftPanelAdapter) view.getAdapter();
                if (mGiftController != null) {
                    int selectPageIndex = mGiftController.getSelectPageIndex();
                    adapter.clearSelection(selectPageIndex);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void init(GiftInfoDataHandler giftInfoDataHandler, GiftAdapter giftAdapter) {
        mGiftInfoDataHandler = giftInfoDataHandler;
        mGiftAdapter = giftAdapter;
    }

    @Override
    public void show() {
        super.show();
        Log.d(TAG, "==================show=================");
        if (onCoinNumberChangeListener != null) {
            onCoinNumberChangeListener.onCoinNumberChanged(giftCoin);
        }
        if (mGiftInfoDataHandler != null) {
            mGiftInfoDataHandler.queryGiftInfoList(mGiftAdapter, new GiftInfoDataHandler.GiftQueryCallback() {
                @Override
                public void onQuerySuccess(final List<GiftInfo> giftInfoList, final List<GiftBeanPlatform.KnapsackBean> knapsacks, List<GiftBeanPlatform.Auspicious> mAuspicious, final int giftCoinNo, final int auspiciousMaxBeans) {
                    GiftPanelViewImp.knapsacks = knapsacks;
                    GiftPanelViewImp.giftInfoList = giftInfoList;
                    GiftPanelViewImp.auspiciousMaxBeans = auspiciousMaxBeans;
                    giftPanelView.setmAuspicious(mAuspicious);
                    //确保更新UI数据在主线程中执行
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            giftTitle.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                            freeGiftTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                            // initGiftData(giftInfoList);
                            initViewPager();
                            giftCoin.setText(giftCoinNo + "");
                        }
                    });
                }

                @Override
                public void onQueryFailed(String errorMsg) {
                    Log.d(TAG, "request data failed, the message:" + errorMsg);
                }

                @Override
                public void onQueryDataGet(String data) {

                }
            }, GIFT_DATA_URL, tokenApp);
        }
    }

    @Override
    public void hide() {
        dismiss();
    }

    @Override
    public void setGiftPanelDelegate(GiftPanelDelegate delegate) {
        mGiftPanelDelegate = delegate;
    }

    public void setOnGiftPanelOperateListener(OnGiftPanelOperateListener onGiftPanelOperateListener) {
        this.onGiftPanelOperateListener = onGiftPanelOperateListener;
    }

    public void setOnCoinNumberChangeListener(OnCoinNumberChangeListener onCoinNumberChangeListener) {
        this.onCoinNumberChangeListener = onCoinNumberChangeListener;
    }

    private OnGiftPanelOperateListener onGiftPanelOperateListener;
    private OnCoinNumberChangeListener onCoinNumberChangeListener;

    public interface OnGiftPanelOperateListener {
        void OnGiftPanel();
        void OnGiftLevelClick();
    }

    public interface OnCoinNumberChangeListener {
        void onCoinNumberChanged(TextView giftCoin);
    }

    public void notifyGiftController(int changedNo, GiftInfo changedInfo) {
        mGiftController.notifyItem(changedNo, changedInfo);
    }

    void getMyUserLevelDetail() {
        String url = "/Api/userInfo/getLevelProgress";
        Map<String, String> map = new HashMap<>();
        map.put("type", isAnchor ? "1" : "2");
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                String data = code + desc;
                // Log.d("liveLog",data);
            }

            @Override
            public void onSuccess(Object o) {
                String data = o.toString();
                LiveLevelProcessBean liveLevelProcessBean = GsonUtil.GsonToBean(data, LiveLevelProcessBean.class);
                if (liveLevelProcessBean != null && liveLevelProcessBean.getData() != null) {
                    LiveLevelProcessBean.DataBean processBean = liveLevelProcessBean.getData();
                    tvCurrentLevel.setText(String.valueOf(processBean.getLevel()));
                    tvNextLevel.setText(String.valueOf(processBean.getLevel() + 1));
                    pbLevel.setProgress(processBean.getProgress());
                    tvLevelTip.setText(processBean.getDiff_tips());
                }
            }
        });
    }

    public void refreshUserLevelProgress() {
        getMyUserLevelDetail();
    }

    public void refreshBalance(String balance) {
        giftCoin.setText(balance);
    }

    public void sendGift(final GiftInfo giftInfo) {
        String url = "/Api/Gift/reward";
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", mAnchorId);
        map.put("gift_id", giftInfo.giftId);
        map.put("gift_count", String.valueOf(giftInfo.count));
        map.put("gift_type", String.valueOf(giftInfo.presentType));
        map.put("dalaba", cbIsShowLaBa.isChecked() ? "1" : "2");
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage(desc);
            }

            @Override
            public void onSuccess(Object o) {
                //更新免费礼物的数量
                if (giftInfo.presentType == 2) {
                    int changeNum = Integer.parseInt(giftInfo.num) - giftInfo.count;
                    giftInfo.num = String.valueOf(changeNum);
                    EventBus.getDefault().post(new AppLiveGiftChangedEvent(changeNum, giftInfo));
                }
            }
        });
    }

    void clearPanelSelected(int index) {
        if (viewMap == null) {
            return;
        }
        if (mGiftController != null) {
            mGiftController.clearSelect();
        }
        if (index == 0) {
            List<View> giftViews = viewMap.get(1);
            if (giftViews == null) {
                return;
            }
            for (int i = 0; i < giftViews.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) giftViews.get(i);
                GiftPanelAdapter adapter = (GiftPanelAdapter) view.getAdapter();
                adapter.clearSelectState();
                adapter.notifyDataSetChanged();
            }
        } else if (index == 1) {
            List<View> giftViews = viewMap.get(2);
            if (giftViews == null) {
                return;
            }
            for (int i = 0; i < giftViews.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) giftViews.get(i);
                GiftPanelAdapter adapter = (GiftPanelAdapter) view.getAdapter();
                adapter.clearSelectState();
                adapter.notifyDataSetChanged();
            }
        }
    }

    public interface OnNewGiftListener {
        void OnClickRedEnvelop();
    }
    OnNewGiftListener onNewGiftListener;
    public void setOnNewGiftListener(OnNewGiftListener onNewGiftListener) {
        this.onNewGiftListener = onNewGiftListener;
    }

    void initBlindBox(RecyclerView rvBox) {
        final List<LiveBlindBoxBean.DataBean> boxList = new ArrayList<>();
        boxList.add(new LiveBlindBoxBean.DataBean("1","","神奇礼盒","5魔豆"));
        boxList.add(new LiveBlindBoxBean.DataBean("1","","高级神奇礼盒","38魔豆"));
        boxList.add(new LiveBlindBoxBean.DataBean("1","","超级神奇礼盒","388魔豆"));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,4);
//        GiftPanelBlindBoxAdapter boxAdapter = new GiftPanelBlindBoxAdapter(mContext,boxList);
//        rvBox.setAdapter(boxAdapter);
//        rvBox.setLayoutManager(gridLayoutManager);
//        boxAdapter.setOnBlindBoxListener(new GiftPanelBlindBoxAdapter.OnBlindBoxListener() {
//            @Override
//            public void doBlindBoxClick(int index) {
//                ToastUtil.toastLongMessage("click " + boxList.get(index).getName());
//            }
//        });
    }
}
