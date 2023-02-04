package com.tencent.qcloud.tim.tuikit.live.component.gift;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.liteav.demo.beauty.download.HttpFileHelper;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest;
import com.tencent.qcloud.tim.tuikit.live.base.TUILiveRequestCallback;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveBlindBoxBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveLevelProcessBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftBeanPlatform;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftPanelViewImp;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.NormalGiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftViewPagerAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.view.GiftNumSelectionView;
import com.tencent.qcloud.tim.tuikit.live.component.gift.view.GiftPanelView;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil;
import com.tencent.qcloud.tim.uikit.utils.NetWorkUtils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//直播间礼物弹窗
public class LiveGiftPanel extends BottomSheetDialog {
    private ViewPager mViewPagerGift;
    private LinearLayout mLineShowGiftArray;
    private ImageView ivShowGiftArray;
    private TextView tvCurrentLevel, tvNextLevel, tvLevelTip;
    private LinearLayout llLevelProgress;
    private ImageView ivCurrentLevel, ivNextLevel;
    private ProgressBar pbLevel;
    private CheckBox cbIsShowLaBa;
    private TextView tvIsShowLaBa;
    private TextView mTvGift, mTvPackage, mTvFans;
    private TextView mTvRedEnvelopes;
    private TextView mTvBlindBox;
    boolean isAnchor = false;
    private Context mContext;
    private String mRoomId;
    private String mAnchorId;
    private TextView mTvSendGift;
    private TextView mTvCoin;
    private TextView mTvRecharge;
    private LinearLayout mLlSendGift;
    private LinearLayout llGiftNum;
    private TextView tvGiftNum;
    private Button mBtSendBindBox;

    private LinearLayout llLivePublicScreen;
    private LinearLayout llLiveRule;
    private ImageView ivLiveRule;

    public LiveGiftPanel(@NonNull Context context, String roomId, String mAnchorId) {
        this(context, roomId, mAnchorId, false);
    }

    public LiveGiftPanel(@NonNull Context context, String roomId, String anchorId, boolean isAnchor) {
        super(context, R.style.live_action_sheet_theme);
        setContentView(R.layout.live_dialog_gift_panel);
        setCanceledOnTouchOutside(true);
        this.isAnchor = false;
        this.mRoomId = roomId;
        this.mAnchorId = anchorId;
        this.mContext = context;
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.design_bottom_sheet));
        if (behavior != null) {
            behavior.setHideable(false);//此处设置表示禁止BottomSheetBehavior的执行
        }
        initView();
    }

    GiftPanelView mGiftPanelView;
    GiftPanelView mPackageView;
    GiftPanelView mFansPanelView;
    View mBindBoxPanel;
    private Handler mHandler = new Handler();

    private void initView() {
        mTvGift = findViewById(R.id.tv_panel_tab_gift);
        mTvPackage = findViewById(R.id.tv_panel_tab_package);
        mTvFans = findViewById(R.id.tv_panel_tab_fans);
        mTvRedEnvelopes = findViewById(R.id.tv_panel_tab_red_envelopes);
        mTvBlindBox = findViewById(R.id.tv_panel_tab_blind_box);
        mTvSendGift = findViewById(R.id.btn_send_gift);
        mLlSendGift = findViewById(R.id.ll_send_gift);
        cbIsShowLaBa = findViewById(R.id.cb_live_gift_is_show);
        mTvCoin = findViewById(R.id.tv_coin_number);
        mTvRecharge = findViewById(R.id.tv_coin);
        llGiftNum = findViewById(R.id.ll_show_gift_array);
        tvGiftNum = findViewById(R.id.tv_show_gift_array);
        llLivePublicScreen = findViewById(R.id.ll_gift_public_screen);
        llLiveRule = findViewById(R.id.ll_live_gift_rule);
        ivLiveRule = findViewById(R.id.iv_live_gift_rule);
        initProgressView();
        initMenuView();
        mViewPagerGift = findViewById(R.id.view_pager_gift);
        final List<View> mGiftViews = new ArrayList<>();
        //普通礼物
        mGiftPanelView = new GiftPanelView(mViewPagerGift.getContext());
        mGiftViews.add(mGiftPanelView);

        //粉丝团
        mFansPanelView = new GiftPanelView(mViewPagerGift.getContext());
        mGiftViews.add(mFansPanelView);

        //背包
        mPackageView = new GiftPanelView(mViewPagerGift.getContext());
        if (!isAnchor) {
            mTvPackage.setVisibility(View.VISIBLE);
            mGiftViews.add(mPackageView);
        } else {
            mTvPackage.setVisibility(View.GONE);
        }


        //红包
        View mRedEnvelopesPanel = LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_red_envelopes, mViewPagerGift, false);
        ImageView ivRedEnvelopesPanel = mRedEnvelopesPanel.findViewById(R.id.iv_layout_gift_panel_red_envelopes);
        ivRedEnvelopesPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNewGiftListener != null) {
                    onNewGiftListener.doClickRedEnvelop();
                }
                dismiss();
            }
        });

        mBtSendBindBox = findViewById(R.id.btn_send_blind_box);
        //盲盒
        mBindBoxPanel = LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_panel_blind_box, mViewPagerGift, false);

        ivLiveRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNewGiftListener != null) {
                    onNewGiftListener.doOnclickRule();
                }
//                dismiss();
            }
        });

        mGiftViews.add(mRedEnvelopesPanel);
        mGiftViews.add(mBindBoxPanel);

        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViews);
        mViewPagerGift.setAdapter(giftViewPagerAdapter);
        mViewPagerGift.setOffscreenPageLimit(mGiftViews.size());
        initGiftView();
        getData();
        getMyUserLevelDetail();
        initListener();
    }

    void initProgressView() {
        tvCurrentLevel = findViewById(R.id.tv_gift_current_level);
        tvNextLevel = findViewById(R.id.tv_gift_next_level);
        tvLevelTip = findViewById(R.id.tv_gift_next_level_tips);
        ivCurrentLevel = findViewById(R.id.iv_gift_current_level);
        ivNextLevel = findViewById(R.id.iv_gift_next_level);
        llLevelProgress = findViewById(R.id.ll_live_gift_level_process);
        pbLevel = findViewById(R.id.pb_gift_level_progress);
        llLevelProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (isAnchor) {
            ivCurrentLevel.setImageResource(R.drawable.ic_item_live_anchor_level);
            ivNextLevel.setImageResource(R.drawable.ic_item_live_anchor_level);
            pbLevel.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.pg_anchor_level));
        } else {
            ivCurrentLevel.setImageResource(R.drawable.ic_item_live_audience_level);
            ivNextLevel.setImageResource(R.drawable.ic_item_live_audience_level);
            pbLevel.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.pg_audience_level));
        }
    }

    private GiftNumSelectionView mMenuView;

    void initMenuView() {
        mMenuView = new GiftNumSelectionView(mContext);
        //mMenuView.setVisibility(View.GONE);
        mMenuView.setOnNumSelectListener(new GiftNumSelectionView.OnNumSelectListener() {
            @Override
            public void doNumSelect(int num) {
                tvGiftNum.setText(String.valueOf(num));
            }
        });
    }

    void initListener() {
        mTvGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerGift.getCurrentItem() != 0) {
                    mViewPagerGift.setCurrentItem(0);
                    clearPackageChoose();
                    clearFansGiftChoose();
                }
            }
        });
        mTvFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerGift.getCurrentItem() != 1) {
                    mViewPagerGift.setCurrentItem(1);
                    clearGiftChoose();
                    clearPackageChoose();
                }
            }
        });
        mTvPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerGift.getCurrentItem() != 2) {
                    mViewPagerGift.setCurrentItem(2);
                    clearGiftChoose();
                    clearFansGiftChoose();
                }
            }
        });
        mTvRedEnvelopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerGift.getCurrentItem() != (isAnchor ? 2 : 3)) {
                    mViewPagerGift.setCurrentItem(isAnchor ? 2 : 3);
                    clearAllChoose();
                }
            }
        });
        mTvBlindBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerGift.getCurrentItem() != (isAnchor ? 3 : 4)) {
                    mViewPagerGift.setCurrentItem(isAnchor ? 3 : 4);
                    clearAllChoose();
                }
            }
        });
        mViewPagerGift.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (isAnchor) {
                    if (i == 0) { //礼物
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearPackageChoose();
                        clearFansGiftChoose();
                        showSendLayout();
                        showPublicScreenLayout();
                    } else if (i == 1) { //粉丝团
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearPackageChoose();
                        clearGiftChoose();
                        showSendLayout();
                        showPublicScreenLayout();
                    } else if (i == 2) { //红包
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearAllChoose();
                        hideSendLayout();
                        hidePublishScreenLayout();
                        hideRuleLayout();
                    } else if (i == 3) {
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        clearAllChoose();
                        hideSendLayout();
                        hidePublishScreenLayout();
                        showRuleLayout();
                    }
                } else {
                    if (i == 0) { //礼物
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearPackageChoose();
                        clearFansGiftChoose();
                        showPublicScreenLayout();
                        hideRuleLayout();
                    } else if (i == 1) { //粉丝团
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearGiftChoose();
                        clearPackageChoose();
                        showSendLayout();
                        showPublicScreenLayout();
                    } else if (i == 2) { //免费
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearGiftChoose();
                        clearFansGiftChoose();
                        showPublicScreenLayout();
                        hideRuleLayout();
                    } else if (i == 3) { //红包
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.white));
                        clearAllChoose();
                        hidePublishScreenLayout();
                        hideRuleLayout();
                    } else if (i == 4) {
                        mTvGift.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvFans.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvPackage.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvRedEnvelopes.setTextColor(mContext.getResources().getColor(R.color.white));
                        mTvBlindBox.setTextColor(mContext.getResources().getColor(R.color.colorGiftPanel));
                        clearAllChoose();
                        hidePublishScreenLayout();
                        showRuleLayout();
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mTvSendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChooseGiftIdList.size() > 0) {
                    sendGift();
                } else {
                    ToastUtil.toastShortMessage("请选择礼物");
                }
            }
        });

        mGiftPanelView.setOnGiftListener(new GiftPanelView.OnGiftListener() {
            @Override
            public void doGiftChoose(GiftInfoBean giftInfoBean) {
                mCurrentGiftInfoBean = giftInfoBean;
                tvGiftNum.setText("1");
            }
        });

        mPackageView.setOnGiftListener(new GiftPanelView.OnGiftListener() {
            @Override
            public void doGiftChoose(GiftInfoBean giftInfoBean) {
                mCurrentGiftInfoBean = giftInfoBean;
                tvGiftNum.setText("1");
            }
        });

        mFansPanelView.setOnGiftListener(new GiftPanelView.OnGiftListener() {
            @Override
            public void doGiftChoose(GiftInfoBean giftInfoBean) {
                mCurrentGiftInfoBean = giftInfoBean;
                tvGiftNum.setText("1");
            }
        });

        mTvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onNewGiftListener != null) {
                    onNewGiftListener.doRecharge();
                }
            }
        });
        llLevelProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNewGiftListener != null) {
                    onNewGiftListener.doClickProgress();
                }
            }
        });

        llGiftNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerGift.getCurrentItem() != 0) {
                    return;
                }
                if (isMultiChoose()) {
                    if (mMenuView.isShowing()) {
                        mMenuView.dismiss();
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mMenuView.show(llGiftNum);
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean isMultiChoose() {
        List<GiftInfoBean> giftBean = mGiftPanelView.getGiftBean();
        if (giftBean == null || giftBean.isEmpty() || mChooseGiftIdList.isEmpty()) {
            return false;
        }

        boolean isMulti = false;

        for (GiftInfoBean bean : giftBean) {
            if (bean.getGift_id().equals(mChooseGiftIdList.get(0))) {
                isMulti = Integer.parseInt(bean.getGift_beans()) <= 10;
            }
        }

        return isMulti;
    }

    GiftInfoBean mCurrentGiftInfoBean;

    private void initGiftView() {
        mChooseGiftIdList = new ArrayList<>();
    }

    private List<String> mChooseGiftIdList;

    private void getData() {
        String url = "/Api/Live/liveGoods";
        Map<String, String> map = new HashMap<>();
        map.put("uid", ProfileManager.getInstance().getUserId());
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                Log.d("hahaha", code + desc);
            }

            @Override
            public void onSuccess(Object o) {
                NormalGiftInfo normalGift = GsonUtil.GsonToBean(o.toString(), NormalGiftInfo.class);
                mGiftPanelView.initChooseId(mChooseGiftIdList);
                mGiftPanelView.initGiftData(normalGift.getData().getGoods());
                mPackageView.initChooseId(mChooseGiftIdList);
                mPackageView.initGiftData(normalGift.getData().getKnapsack());
                mFansPanelView.initChooseId(mChooseGiftIdList);
                mFansPanelView.initGiftData(normalGift.getData().getFanclub_gift());
                mTvCoin.setText(normalGift.getData().getRich_beans());
                Collections.reverse(normalGift.getData().getAuspicious());
                mMenuView.initData(normalGift.getData().getAuspicious());
                initBlindBox(normalGift.getData().getBlindbox());
            }
        });
    }

    void clearGiftChoose() {
        mChooseGiftIdList.clear();
        mGiftPanelView.refreshChooseState();
        showSendLayout();
        tvGiftNum.setText("1");
    }

    void clearPackageChoose() {
        mChooseGiftIdList.clear();
        mPackageView.refreshChooseState();
        showSendLayout();
        tvGiftNum.setText("1");
    }

    void clearFansGiftChoose() {
        mChooseGiftIdList.clear();
        mFansPanelView.refreshChooseState();
        showSendLayout();
        tvGiftNum.setText("1");
    }


    void clearAllChoose() {
        mChooseGiftIdList.clear();
        mGiftPanelView.refreshChooseState();
        mPackageView.refreshChooseState();
        mFansPanelView.refreshChooseState();
        hideSendLayout();
        tvGiftNum.setText("1");
    }

    void showSendLayout() {
        if (mLlSendGift.getVisibility() == View.INVISIBLE) {
            mLlSendGift.setVisibility(View.VISIBLE);
            tvGiftNum.setText("1");
        }
    }

    void hideSendLayout() {
        if (mLlSendGift.getVisibility() == View.VISIBLE) {
            mLlSendGift.setVisibility(View.INVISIBLE);
        }
    }

    void showSendBlindLayout() {
        if (mBtSendBindBox.getVisibility() == View.GONE) {
            mBtSendBindBox.setVisibility(View.VISIBLE);
        }
    }

    void hideSendBlindLayout() {
        if (mBtSendBindBox.getVisibility() == View.VISIBLE) {
            mBtSendBindBox.setVisibility(View.GONE);
        }
    }

    void showPublicScreenLayout() {
        if (llLivePublicScreen.getVisibility() == View.GONE) {
            llLivePublicScreen.setVisibility(View.VISIBLE);
        }
    }

    void hidePublishScreenLayout() {
        if (llLivePublicScreen.getVisibility() == View.VISIBLE) {
            llLivePublicScreen.setVisibility(View.GONE);
        }
    }

    void showRuleLayout() {
        if (llLiveRule.getVisibility() == View.GONE) {
            llLiveRule.setVisibility(View.VISIBLE);
        }
        showSendBlindLayout();
    }

    void hideRuleLayout() {
        if (llLiveRule.getVisibility() == View.VISIBLE) {
            llLiveRule.setVisibility(View.GONE);
        }
        hideSendBlindLayout();
    }

    public interface OnNewGiftListener {
        void doClickRedEnvelop();

        void doRecharge();

        void doClickProgress();

        void doOnclickRule();
    }

    OnNewGiftListener onNewGiftListener;

    public void setOnNewGiftListener(OnNewGiftListener onNewGiftListener) {
        this.onNewGiftListener = onNewGiftListener;
    }

    void initBlindBox(RecyclerView rvBox) {
        final List<LiveBlindBoxBean.DataBean> boxList = new ArrayList<>();
        boxList.add(new LiveBlindBoxBean.DataBean("1", "", "神奇礼盒", "5魔豆"));
        boxList.add(new LiveBlindBoxBean.DataBean("1", "", "高级神奇礼盒", "38魔豆"));
        boxList.add(new LiveBlindBoxBean.DataBean("1", "", "超级神奇礼盒", "388魔豆"));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
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

    int chooseBindIndex = 0;

    void initBlindBox(final List<NormalGiftInfo.DataBean.BlindBoxBean> boxList) {
        RecyclerView rvBox = mBindBoxPanel.findViewById(R.id.rv_gift_panel_blind_box);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        GiftPanelBlindBoxAdapter boxAdapter = new GiftPanelBlindBoxAdapter(mContext, boxList);
        rvBox.setAdapter(boxAdapter);
        rvBox.setLayoutManager(gridLayoutManager);
        boxAdapter.setOnBlindBoxListener(new GiftPanelBlindBoxAdapter.OnBlindBoxListener() {
            @Override
            public void doBlindBoxClick(int index) {
                //ToastUtil.toastLongMessage("click " + boxList.get(index).getBlindbox_name());
                //sendBlindBox(boxList.get(index).getId());
                chooseBindIndex = index;
            }
        });
        mBtSendBindBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBlindBox(boxList.get(chooseBindIndex).getId());
            }
        });
    }

    void sendGift() {
        if (mCurrentGiftInfoBean == null) {
            ToastUtil.toastShortMessage("未选择礼物，请选择礼物");
        }
        // GiftInfoBean tempGift = mCurrentGiftInfoBean.clone();
        String url = "/Api/Gift/reward";
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", mAnchorId);
        map.put("gift_id", mCurrentGiftInfoBean.getGift_id());
        map.put("gift_count", tvGiftNum.getText().toString());
        map.put("gift_type", mCurrentGiftInfoBean.getGift_type());
        map.put("source_type", mCurrentGiftInfoBean.getSource_type());
        map.put("dalaba", cbIsShowLaBa.isChecked() ? "1" : "2");
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage(desc);
            }

            @Override
            public void onSuccess(Object o) {
                if ("1".equals(mCurrentGiftInfoBean.getSource_type())) {
                    mCurrentGiftInfoBean.setNum("" + (Integer.parseInt(mCurrentGiftInfoBean.getNum()) - 1));
                    mPackageView.updateGiftNum(mCurrentGiftInfoBean);
                }
            }
        });
    }


    void sendPackageGift() {
        String url = "/api/gift/rewardBagGift";

    }


    void sendBlindBox(String id) {
        String url = "/api/gift/openBlingbox";
        Map<String, String> map = new HashMap<>();
        map.put("uid", mAnchorId);
        map.put("id", id);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage(desc);
            }

            @Override
            public void onSuccess(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    if (TextUtils.isEmpty(jsonObject.optString("msg"))) {
                        ToastUtil.toastShortMessage("赠送成功");
                    } else {
                        ToastUtil.toastShortMessage(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getMyUserLevelDetail() {
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

    public void updateCoin(String coin) {
        mTvCoin.setText(coin);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mMenuView != null) {
            mMenuView.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.design_bottom_sheet));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
