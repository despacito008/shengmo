package com.aiwujie.shengmo.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.adapter.CommentLevelOneAdapter;
import com.aiwujie.shengmo.adapter.LaudListviewAdapter;
import com.aiwujie.shengmo.adapter.RewardCommentAdapter;
import com.aiwujie.shengmo.adapter.ThumbUpCommentAdapter;
import com.aiwujie.shengmo.adapter.TuidingCommentAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.bean.DynamicDetailData;
import com.aiwujie.shengmo.bean.EjectionshengyuBean;
import com.aiwujie.shengmo.bean.LaudListData;
import com.aiwujie.shengmo.bean.RewardData;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.customview.DashangDialogNew;
import com.aiwujie.shengmo.customview.NineGridViewWrapper;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.CallBackEvent;
import com.aiwujie.shengmo.eventbus.CommentDetailEvent;
import com.aiwujie.shengmo.eventbus.DynamicDetailEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicMarke;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.DynamicTopEvent;
import com.aiwujie.shengmo.eventbus.ReportEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.utils.NineGridUtils;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.view.KeyBoardListenerHelper;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.aiwujie.shengmo.view.PushTopCardPop;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.aiwujie.shengmo.zdyview.TopcardDialog;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//动态详情
public class DynamicDetailActivity extends FragmentActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2, OnItemClickListener, ListView.OnItemLongClickListener {
// 这个地方的逻辑 谁改的 我想骂人。。。 特别是点击弹出的选项卡这块。你真的是写代码的嘛？


    @BindView(R.id.mDynamicDetail_return)
    ImageView mDynamicDetailReturn;
    @BindView(R.id.mDynamicDetail_sandian)
    ImageView mDynamicDetailSandian;
    @BindView(R.id.mDynamicDetail_title)
    PercentRelativeLayout mDynamicDetailTitle;
    @BindView(R.id.mDynamicDetail_vip)
    ImageView mDynamicDetailVip;
    @BindView(R.id.mDynamicDetail_name)
    TextView mDynamicDetailName;
    @BindView(R.id.mDynamicDetail_online)
    ImageView mDynamicDetailOnline;
    @BindView(R.id.mDynamicDetail_shiming)
    ImageView mDynamicDetailShiming;
    @BindView(R.id.mDynamicDetail_Sex)
    TextView mDynamicDetailSex;
    @BindView(R.id.mDynamicDetail_role)
    TextView mDynamicDetailRole;
    @BindView(R.id.mDynamicDetail_distance)
    TextView mDynamicDetailDistance;
    @BindView(R.id.iv_no_distance)
    ImageView ivNoDistance;

    @BindView(R.id.mDynamicDetail_time)
    TextView mDynamicDetailTime;
    @BindView(R.id.mDynamicDetail_intro)
    TextView mDynamicDetailIntro;
    @BindView(R.id.mDynamicDetail_tabs)
    TabLayout mDynamicDetailTabs;
    @BindView(R.id.mDynamicDetail_bottom_ll_zan)
    PercentLinearLayout mDynamicDetailBottomLlZan;
    @BindView(R.id.mDynamicDetail_bottom_ll_pinglun)
    PercentLinearLayout mDynamicDetailBottomLlPinglun;
    @BindView(R.id.mDynamicDetail_bottom_ll_dashang)
    PercentLinearLayout mDynamicDetailBottomLlDashang;
    @BindView(R.id.mDynamicDetail_bottom_ll_tuiding)
    PercentLinearLayout mDynamicDetailBottomLlTuiding;
    @BindView(R.id.mDynamicDetail_bottom_tabs)
    PercentLinearLayout mDynamicDetailBottomTabs;
    @BindView(R.id.mDynamicDetail_liulancount)
    TextView mDynamicDetailreadtimes;
    @BindView(R.id.mDynamicDetail_et)
    EditText mDynamicDetailEt;
    @BindView(R.id.mDynamicDetail_tvSend)
    TextView mDynamicDetailTvSend;
    @BindView(R.id.mDynamicDetail_ll_et)
    PercentLinearLayout mDynamicDetailLlEt;
    @BindView(R.id.mDynamicDetail_all_layout)
    PercentRelativeLayout mDynamicDetailAllLayout;
    //    @BindView(R.id.mDynamicDetail_listview)
//    MyListView mDynamicDetailListview;
//    @BindView(R.id.mDynamicDetail_pullToRefreshScrollView)
//    PullToRefreshScrollView mDynamicDetailPullToRefreshScrollView;
    @BindView(R.id.mDynamicDetail_bottom_dianzan)
    TextView mDynamicDetailBottomDianzan;
    @BindView(R.id.mDynamicDetail_icon)
    ImageView mDynamicDetailIcon;
    @BindView(R.id.mDynamic_nineGrid)
    NineGridView mDynamicNineGrid;
    @BindView(R.id.mDynamicDetail_hongniang)
    ImageView mDynamicDetailHongniang;
    @BindView(R.id.mDynamicDetail_bottom_ll)
    PercentLinearLayout mDynamicDetailBottomLl;
    @BindView(R.id.mDynamicDetail_header_layout)
    PercentLinearLayout mDynamicDetailHeaderLayout;
    @BindView(R.id.mDynamicDetail_richCount)
    TextView mDynamicDetailRichCount;
    @BindView(R.id.mDynamicDetail_ll_richCount)
    PercentLinearLayout mDynamicDetailLlRichCount;
    @BindView(R.id.mDynamicDetail_beautyCount)
    TextView mDynamicDetailBeautyCount;
    @BindView(R.id.mDynamicDetail_ll_beautyCount)
    PercentLinearLayout mDynamicDetailLlBeautyCount;
    @BindView(R.id.mDynamicDetail_recommendCount)
    TextView mDynamicDetailRecommendCount;
    @BindView(R.id.mDynamic_pic)
    NineGridViewWrapper mDynamicPic;
    @BindView(R.id.video_cover_image)
    ImageView videoCoverImg;
    @BindView(R.id.video_player_layout)
    PercentRelativeLayout videoLayout;
    @BindView(R.id.rv_dynamic_detail_comment)
    RecyclerView rvDynamicDetailComment;
    @BindView(R.id.mDynamicDetail_pullToRefreshScrollView)
    NestedScrollView mDynamicDetailPullToRefreshScrollView;
    @BindView(R.id.pai_tv)
    TextView paiTv;
    @BindView(R.id.pai_ll)
    LinearLayout paiLl;
    @BindView(R.id.pai_iv)
    ImageView paiIv;
    @BindView(R.id.video_cover_round)
    ImageView videoCoverRound;
    @BindView(R.id.play_btn)
    ImageView playBtn;

    private String did;
    private String headpic;
    private String name;
    private int isonline;
    private ArrayList<String> pics;
    private String content;
    private String time;
    private double distance;
    private String role;
    private String age;
    private String sex;
    private String readtimes;
    private String shiming;
    private String isvip;
    private int zanstate;
    private int dynamicPos;
    private String zancount;
    private int count;
    private ArrayList<String> titles = new ArrayList<>();
    private String pingluncount;
    private String dashangcount;
    String tuidingcount = "";
    private InputMethodManager imm;
    private String otheruid = "0";
    private String uid;
    int page = 0;
    int type = 1;
    private ArrayList<LaudListData.DataBean> datas = new ArrayList<>();
    private ArrayList<CommentData.DataBean> commentdatas = new ArrayList<>();
    private ArrayList<RewardData.DataBean> rewarddatas = new ArrayList<>();
    private ArrayList<DynamicDetailBean.DataBean> topcardysedrsdatas = new ArrayList<>();
    private LaudListviewAdapter adapter;
    //private CommentAdapter commentadapter;
    private CommentLevelOneAdapter commentLevelOneAdapter;

    private int operationflag;
    private int showwhat;
    private android.app.AlertDialog dialog;
    private boolean isSend = false;
    private String toastStr;
    //举报状态
    private String reportstate;
    private String volunteer;
    private String tid;
    private String topictile;
    private String vip; // 是否是VIP
    private String svip;//  是否是SVIP
    private String admin;// 是否是管理员
    private String realname;
    private String recommend;//推荐
    private String stickstat; //VIP置顶状态
    private String collectstate;
    private ArrayList<String> sypics;
    private ArrayList<String> slpics;
    private String fuid;
    private String fvolunteer;
    private String fadmin;
    private String fsvip;
    private String fsvipannual;
    private String fvip;
    private String fvipannual;
    private int retcode;
    private String charmval;
    private String wealthwal;
    private int dynamicRetcode;
    private String is_hidden; // 是否是隐藏状态
    private String recommendAll; // 管理员置顶状态
    Handler handler = new Handler();
    private TextView mStamp_count;
    private String wallet_topcard = "0";
    int first = 0;
    private String atuid;
    private String atuname;
    private String platuid;
    private String platuname;
    private ArrayList<String> didpic;
    private String usetopnums;
    private String alltopnums;
    private String auditMark = "标记";
    private String bkvip;
    private String blvip;
    private String isyincang = "隐藏";
    private String iszhiding = "置顶(vip)";
    private String mybkvip;
    private String myblvip;
    List<String> atuidlist = new ArrayList<>();
    List<String> atunamelist = new ArrayList<>();

    private String bk_vip_role; // 顾问具体权限
    private String bl_vip_role; // 蓝钻具体权限

    private Display defaultDisplay;

    //评论id
    private String pid = "";
    private TuidingCommentAdapter tuidingCommentAdapter;
    private RewardCommentAdapter rewardCommentAdapter;
    private ThumbUpCommentAdapter thumbAdapter;

    private boolean noMore = false;

    private int sortType = 1;


    int currentIndex = -1;
    String otherName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        gotoNewDynamicDetail();
//        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
//        StatusBarUtil.showLightStatusBar(this);
//        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
//        defaultDisplay = wm.getDefaultDisplay();
//        //初始化数据
//        getData();
//        getusertopcardinfo();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //获取动态详情
//                getDynamicDetail();
//
//            }
//        }, 200);
//
//        mDynamicDetailBottomLlTuiding.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TopcardDialog.dialogShow(DynamicDetailActivity.this, did, 10000);
//                showTopCardPop(did,10000);
//            }
//        });
//
//        initLoadMoreListener();
    }

    void gotoNewDynamicDetail() {
        Intent intent = new Intent(DynamicDetailActivity.this, NewDynamicDetailActivity.class);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));
        intent.putExtra("did", getIntent().getStringExtra("did"));
        intent.putExtra("pos", getIntent().getIntExtra("pos", -1));
        intent.putExtra("showwhat", getIntent().getIntExtra("showwhat", -1));
        startActivity(intent);
        finish();
    }

    void showTopCardPop(String did, int pos) {
        PushTopCardPop pushTopCardPop = new PushTopCardPop(DynamicDetailActivity.this, did, pos);
        pushTopCardPop.showPopupWindow();
    }


    private void getData() {
        Intent intent = getIntent();
        showwhat = intent.getIntExtra("showwhat", -1);
        uid = intent.getStringExtra("uid");
        did = intent.getStringExtra("did");
        dynamicPos = intent.getIntExtra("pos", -1);
        volunteer = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "volunteer", "0");
        vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
        admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
        mybkvip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "bkvip", "0");
        myblvip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "blvip", "0");
        realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
        // 顾问，蓝钻的具体权限情况
        bk_vip_role = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "bk_vip_role", "");
        bl_vip_role = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "bl_vip_role", "");

        sortType = (int) SharedPreferencesUtils.getParam(getApplicationContext(), SharedPreferencesUtils.COMMENT_SORT, 1);

        if (sortType == 1) {
            paiTv.setText("时间排序");
        } else {
            paiTv.setText("热度排序");
        }
        paiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortType == 1) {
                    paiTv.setText("热度排序");
                    sortType = 2;
                } else {
                    paiTv.setText("时间排序");
                    sortType = 1;
                }
                page = 0;
                SharedPreferencesUtils.setParam(DynamicDetailActivity.this, SharedPreferencesUtils.COMMENT_SORT, sortType);
                getComList(page);
            }
        });

        paiIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortType == 1) {
                    paiTv.setText("热度排序");
                    sortType = 2;
                } else {
                    paiTv.setText("时间排序");
                    sortType = 1;
                }
                page = 0;
                getComList(page);
            }
        });


        KeyBoardListenerHelper keyBoardListenerHelper = new KeyBoardListenerHelper(this);
        keyBoardListenerHelper.setOnKeyBoardChangeListener(new KeyBoardListenerHelper.OnKeyBoardChangeListener() {
            @Override
            public void OnKeyBoardChange(boolean isShow, int keyBoardHeight) {
                if (isShow && mDynamicDetailBottomLl.getVisibility() == View.VISIBLE) {
                    mDynamicDetailBottomLl.setVisibility(View.GONE);
                } else if (!isShow && mDynamicDetailBottomLl.getVisibility() == View.GONE) {
                    mDynamicDetailBottomLl.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initViewPager() {
        titles.add("赞 " + zancount);
        titles.add("评论 " + pingluncount);
        titles.add("打赏 " + dashangcount);
        if ("0".equals(alltopnums)) {
            titles.add("推顶 " + alltopnums);
        } else {
            if (usetopnums.equals(alltopnums)) {
                titles.add("推顶 " + usetopnums);
            } else {
                titles.add("推顶 " + usetopnums + "/" + alltopnums);
            }

        }


        for (int i = 0; i < titles.size(); i++) {
            View tabView = getTabView(i);
            mDynamicDetailTabs.addTab(mDynamicDetailTabs.newTab().setCustomView(tabView), false);
        }

        mDynamicDetailTabs.getTabAt(1).select();
        mDynamicDetailTabs.setScrollPosition(1, 0, true);
        commentdatas.clear();
        getComList(0);
        View tabView = mDynamicDetailTabs.getTabAt(1).getCustomView();
        setCheckedSytle(tabView, 16, R.color.titleBlack, Typeface.BOLD, true);
        if (showwhat == 2) {
            mDynamicDetailLlEt.setVisibility(View.VISIBLE);
            mDynamicDetailEt.setText("");

        }
        mDynamicDetailTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                setCheckedSytle(tabView, 16, R.color.titleBlack, Typeface.BOLD, true);
                showdialog();
                mDynamicDetailLlEt.setVisibility(View.GONE);
                page = 0;
                noMore = false;
                switch (tab.getPosition()) {
                    case 0:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 0;
                                datas.clear();
                                paiLl.setVisibility(View.GONE);
                                getLaudList(page);
                                //mDynamicDetailListview.setOnItemClickListener(null);
                                //mDynamicDetailListview.setOnItemLongClickListener(null);

                            }
                        }, 300);
                        break;
                    case 1:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 1;

                                commentdatas.clear();
                                paiLl.setVisibility(View.VISIBLE);
                                getComList(page);
                                //mDynamicDetailListview.setOnItemClickListener(DynamicDetailActivity.this);
                                //mDynamicDetailListview.setOnItemLongClickListener(DynamicDetailActivity.this);

                            }
                        }, 300);
                        break;
                    case 2:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 2;

                                rewarddatas.clear();
                                paiLl.setVisibility(View.GONE);
                                getRewardList(page);
                                //mDynamicDetailListview.setOnItemClickListener(null);
                                //mDynamicDetailListview.setOnItemLongClickListener(null);

                            }
                        }, 300);
                        break;
                    case 3:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 3;
                                topcardysedrsdatas.clear();
                                paiLl.setVisibility(View.GONE);
                                gettopcardchargeList(page);
                                //mDynamicDetailListview.setOnItemClickListener(null);
                                //mDynamicDetailListview.setOnItemLongClickListener(null);

                            }
                        }, 300);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                setCheckedSytle(tabView, 14, R.color.normalGray, Typeface.NORMAL, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                showdialog();
                page = 0;
                switch (tab.getPosition()) {
                    case 0:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 0;
                                datas.clear();
                                getLaudList(page);
                                //mDynamicDetailListview.setOnItemClickListener(null);
                                //mDynamicDetailListview.setOnItemLongClickListener(null);

                            }
                        }, 300);
                        break;
                    case 1:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 1;
                                commentdatas.clear();
                                getComList(page);
                                //mDynamicDetailListview.setOnItemClickListener(DynamicDetailActivity.this);
                                //mDynamicDetailListview.setOnItemLongClickListener(DynamicDetailActivity.this);

                            }
                        }, 300);
                        break;
                    case 2:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 2;
                                rewarddatas.clear();
                                getRewardList(page);
                                //mDynamicDetailListview.setOnItemClickListener(null);
                                //mDynamicDetailListview.setOnItemLongClickListener(null);
                            }
                        }, 300);
                        break;
                    case 3:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                type = 3;
                                topcardysedrsdatas.clear();
                                gettopcardchargeList(page);
                                //mDynamicDetailListview.setOnItemClickListener(null);
                                //mDynamicDetailListview.setOnItemLongClickListener(null);
                            }
                        }, 300);
                        break;
                }
            }
        });

        mDynamicDetailEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.length() > start)) {
                    return;
                }
                if ('@' == s.charAt(start) && count == 1) {
//                    Intent intent = new Intent(DynamicDetailActivity.this, AtFansActivity.class);
                    Intent intent = new Intent(DynamicDetailActivity.this, AtMemberActivity.class);
                    startActivityForResult(intent, 100);
                    return;
                }

                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
                    if ('@' == s.charAt(start - 1)) {
                        mDynamicDetailEt.getText().delete(start - 1, start);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mDynamicDetailEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = mDynamicDetailEt.getSelectionStart();

                ATSpan[] atSpans = mDynamicDetailEt.getText().getSpans(0, mDynamicDetailEt.getText().length(), ATSpan.class);
                int length = atSpans.length;

                if (0 == length) {
                    return;

                }

                for (ATSpan atSpan : atSpans) {
                    int start = mDynamicDetailEt.getText().getSpanStart(atSpan);
                    int end = mDynamicDetailEt.getText().getSpanEnd(atSpan);
                    if (selectionStart >= start && selectionStart <= end) {
                        mDynamicDetailEt.setSelection(end);
                        return;
                    }
                }
            }
        });

        mDynamicDetailEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = mDynamicDetailEt.getSelectionStart();

                    ATSpan[] atSpans = mDynamicDetailEt.getText().getSpans(0, mDynamicDetailEt.getText().length(), ATSpan.class);
                    int length = atSpans.length;

                    if (0 == length) {
                        return false;
                    }

                    for (ATSpan atSpan : atSpans) {
                        int start = mDynamicDetailEt.getText().getSpanStart(atSpan);
                        int end = mDynamicDetailEt.getText().getSpanEnd(atSpan);
                        if (selectionStart >= start + 1 && selectionStart <= end) {
                            mDynamicDetailEt.getText().delete(start + 1, end);
                            for (int i = 0; i < atuidlist.size(); i++) {
                                String value = atSpan.getValue();
                                if (atuidlist.get(i).equals(value)) {
                                    atuidlist.remove(i);
                                    atunamelist.remove(i);
                                    break;
                                }
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        });


    }

    private void setCheckedSytle(View tabView, int textSize, int color, int bold, boolean isChecked) {
        final View view = tabView.findViewById(R.id.tab_item_textview);
        final View tabItemBottom = tabView.findViewById(R.id.tab_item_bottom);
        if (null != view && view instanceof TextView) {
            ((TextView) view).setTextSize(textSize);
            ((TextView) view).setTextColor(ContextCompat.getColor(DynamicDetailActivity.this, color));
            ((TextView) view).setTypeface(Typeface.defaultFromStyle(bold));
        }
        if (isChecked) {
            tabItemBottom.post(new Runnable() {
                @Override
                public void run() {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tabItemBottom.getLayoutParams();
                    lp.width = view.getMeasuredWidth();
                    tabItemBottom.setLayoutParams(lp);
                    tabItemBottom.setVisibility(View.VISIBLE);
                }
            });

        } else {
            tabItemBottom.setVisibility(View.INVISIBLE);

        }

    }


    //自定义Tab的View
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_dynamic_detail, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(titles.get(currentPosition));
        return view;
    }


    private void getDynamicDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicdetailFive, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("dynamicdetaildata", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            retcode = obj.getInt("retcode");
                            if (retcode == 2000 || retcode == 2001) {
                                final DynamicDetailData data = new Gson().fromJson(response, DynamicDetailData.class);
                                final DynamicDetailData.DataBean datas = data.getData();
                                if ("0".equals(datas.getAuditMark())) {
                                    auditMark = "标记";
                                } else {
                                    auditMark = "取消标记";
                                }

                                topictile = datas.getTopictitle();
                                tid = datas.getTid();
                                headpic = datas.getHead_pic();
                                name = datas.getNickname();
                                isonline = datas.getOnlinestate();
                                shiming = datas.getRealname();
                                readtimes = datas.getReadtimes();
                                sex = datas.getSex();
                                age = datas.getAge();
                                role = datas.getRole();
                                distance = datas.getDistance();
                                time = datas.getAddtime();
                                content = datas.getContent();
                                pics = datas.getPic();
                                zanstate = datas.getLaudstate();
                                zancount = datas.getLaudnum();
                                pingluncount = datas.getComnum();
                                usetopnums = datas.getUsetopnums();
                                alltopnums = datas.getAlltopnums();
                                dashangcount = datas.getRewardnum();
                                sypics = datas.getSypic();
                                slpics = datas.getPic();
                                isvip = datas.getVip();
                                bkvip = datas.getBkvip();
                                blvip = datas.getBlvip();
                                //1：已举报  0：未举报
                                reportstate = datas.getReportstate();
                                //推荐
                                recommend = datas.getRecommend();
                                //管理员置顶状态
                                recommendAll = datas.getRecommendall();
                                //VIP置顶状态
                                stickstat = datas.getStickstate();
                                //收藏状态
                                collectstate = datas.getCollectstate();
                                //隐藏状态
                                is_hidden = datas.getIs_hidden();
                                //右上角显示图标。


                                if (is_hidden.equals("1")) {
                                    mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaiyinsi);
                                } else {
                                    if (stickstat.equals("1")) {
                                        mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaizhiding);
                                    } else {
                                        if (!recommend.equals("0")) {
                                            mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.jian);
                                        }
                                    }
                                }
                                //是否显示浏览数
                                if (!recommend.equals("0")) {
                                    mDynamicDetailreadtimes.setVisibility(View.VISIBLE);
                                    if (!readtimes.equals("")) {
                                        mDynamicDetailreadtimes.setText(readtimes);
                                    }

                                } else {
                                    mDynamicDetailreadtimes.setVisibility(View.GONE);
                                }


                                try {
                                    GlideImgManager.glideLoader(DynamicDetailActivity.this, headpic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mDynamicDetailIcon, 0);
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
                                mDynamicDetailName.setText(name);
                                mDynamicDetailSex.setText(age);
//                                mDynamicDetailreadtimes.setText("浏览 " + readtimes);
                                if (retcode == 2000) {

                                    if (datas.getLocation_switch().equals("1")) {
                                        ivNoDistance.setVisibility(View.VISIBLE);
                                        mDynamicDetailDistance.setVisibility(View.INVISIBLE);
                                    } else {
                                        ivNoDistance.setVisibility(View.GONE);
                                        mDynamicDetailDistance.setVisibility(View.VISIBLE);
                                        mDynamicDetailDistance.setText(distance + "km");
                                    }
                                } else {
                                    ivNoDistance.setVisibility(View.VISIBLE);
                                    mDynamicDetailDistance.setVisibility(View.INVISIBLE);
                                }
                                mDynamicDetailTime.setText(time);
                                if (!content.equals("")) {
                                    if (!datas.getTopictitle().equals("")) {
                                        mDynamicDetailIntro.setHighlightColor(getResources().getColor(android.R.color.transparent));
                                        SpannableString spanableInfo = new SpannableString("#" + datas.getTopictitle() + "#" + content);
                                        spanableInfo.setSpan(new Clickable(clickListener), 0, datas.getTopictitle().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        mDynamicDetailIntro.setOnTouchListener(new LinkMovementMethodOverride());
                                        atuid = datas.getAtuid();
                                        atuname = datas.getAtuname();
                                        if (!"".equals(atuname) && null != atuname && !"".equals(atuid) && null != atuid) {
                                            String[] split = atuid.split(",");
                                            String[] split1 = atuname.split(",");
                                            for (int i = 0; i < split1.length; i++) {
                                                if (split1[i].trim().contains("*")) {
                                                    split1[i] = split1[i].trim().replace("*", "\\*");
                                                }
                                                String patten = Pattern.quote(split1[i].trim());
                                                Pattern compile = Pattern.compile(patten);
                                                Matcher matcher = compile.matcher("#" + datas.getTopictitle() + "#" + content);
                                                while (matcher.find()) {
                                                    int start = matcher.start();
                                                    if (split1[i].trim().contains("*")) {
                                                        split1[i] = split1[i].trim().replace("\\", "");
                                                    }
                                                    spanableInfo.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                            }
                                        }
                                        //mDynamicDetailIntro.setMovementMethod(LinkMovementMethod.getInstance());
                                        mDynamicDetailIntro.setText(spanableInfo);
                                      /*  mDynamicDetailIntro.setMovementMethod(LinkMovementMethod.getInstance());
                                        mDynamicDetailIntro.setText(spanableInfo);
                                        content = content.replace("\n","<br />");
                                        mDynamicDetailIntro.append(Html.fromHtml(content,null,new GameTagHandler()));*/

                                    } else {
                                        atuid = datas.getAtuid();
                                        atuname = datas.getAtuname();
                                        SpannableString spannableString = new SpannableString(content);
                                        if (!"".equals(atuname) && null != atuname && !"".equals(atuid) && null != atuid) {
                                            String[] split = atuid.split(",");
                                            String[] split1 = atuname.split(",");
                                            for (int i = 0; i < split1.length; i++) {
                                                if (split1[i].trim().contains("*")) {
                                                    split1[i] = split1[i].trim().replace("*", "\\*");
                                                }
                                                String patten = Pattern.quote(split1[i].trim());
                                                Pattern compile = Pattern.compile(patten);
                                                Matcher matcher = compile.matcher(content);
                                                while (matcher.find()) {
                                                    int start = matcher.start();
                                                    if (split1[i].trim().contains("*")) {
                                                        split1[i] = split1[i].trim().replace("\\", "");
                                                    }
                                                    spannableString.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                            }
                                        }
                                        mDynamicDetailIntro.setOnTouchListener(new LinkMovementMethodOverride());
                                        //mDynamicDetailIntro.setMovementMethod(LinkMovementMethod.getInstance());
                                        mDynamicDetailIntro.setText(spannableString);

                                    /*    content = content.replace("\n","<br />");
                                        mDynamicDetailIntro.setMovementMethod(LinkMovementMethod.getInstance());
                                        mDynamicDetailIntro.setText(Html.fromHtml(content,null,new GameTagHandler()));*/
                                    }
                                } else {
                                    mDynamicDetailIntro.setVisibility(View.GONE);
                                }
                                if (zanstate == 0) {
                                    Drawable drawable = getResources().getDrawable(R.mipmap.dianzanda);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailBottomDianzan.setCompoundDrawables(drawable, null, null, null);
                                } else {
                                    Drawable drawable = getResources().getDrawable(R.mipmap.zanhong);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailBottomDianzan.setCompoundDrawables(drawable, null, null, null);
                                }
                                if (isonline == 0) {
                                    mDynamicDetailOnline.setVisibility(View.GONE);
                                } else {
                                    mDynamicDetailOnline.setVisibility(View.VISIBLE);
                                }
                                if (shiming.equals("0")) {
                                    mDynamicDetailShiming.setVisibility(View.GONE);
                                } else {
                                    mDynamicDetailShiming.setVisibility(View.VISIBLE);
                                }
                                if (sex.equals("1")) {
                                    mDynamicDetailSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                    Drawable drawable = getResources().getDrawable(R.mipmap.nan);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailSex.setCompoundDrawables(drawable, null, null, null);
                                } else if (sex.equals("2")) {
                                    mDynamicDetailSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                    Drawable drawable = getResources().getDrawable(R.mipmap.nv);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailSex.setCompoundDrawables(drawable, null, null, null);
                                } else if (sex.equals("3")) {
                                    mDynamicDetailSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                                    Drawable drawable = getResources().getDrawable(R.mipmap.san);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailSex.setCompoundDrawables(drawable, null, null, null);
                                }
                                if (role.equals("S")) {
                                    mDynamicDetailRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                    mDynamicDetailRole.setText("斯");
                                } else if (role.equals("M")) {
                                    mDynamicDetailRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                    mDynamicDetailRole.setText("慕");
                                } else if (role.equals("SM")) {
                                    mDynamicDetailRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                                    mDynamicDetailRole.setText("双");
                                } else {
                                    mDynamicDetailRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                                    mDynamicDetailRole.setText(role);
                                }
                                //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
                                fuid = datas.getUid();
                                fvolunteer = datas.getIs_volunteer();
                                fadmin = datas.getIs_admin();
                                fsvip = datas.getSvip();
                                fsvipannual = datas.getSvipannual();
                                fvip = datas.getVip();
                                fvipannual = datas.getVipannual();
                                UserIdentityUtils.showIdentity(DynamicDetailActivity.this, datas.getHead_pic(), datas.getUid(), datas.getIs_volunteer(), datas.getIs_admin(), datas.getSvipannual(), datas.getSvip(), datas.getVipannual(), datas.getVip(), datas.getBkvip(), datas.getBlvip(), mDynamicDetailVip);
                                if (datas.getIs_hand().equals("0")) {
                                    mDynamicDetailHongniang.setVisibility(View.INVISIBLE);
                                } else {
                                    mDynamicDetailHongniang.setVisibility(View.VISIBLE);
                                }
                                didpic = datas.getPic();
                                if (datas.getPlayUrl().length() > 0) {
                                    //TODO

                                    float width = defaultDisplay.getWidth();
                                    float height = defaultDisplay.getHeight();
                                    if (!"0".equals(datas.getWidth()) && !"0".equals(datas.getHeight())) {
                                        float videoWidth = Float.parseFloat(datas.getWidth());
                                        float videoHeight = Float.parseFloat(datas.getHeight());
                                        height = width * (videoHeight / videoWidth);
                                    }

                                    ViewGroup.LayoutParams layoutParams = videoCoverImg.getLayoutParams();
                                    ViewGroup.LayoutParams layoutParams1 = videoCoverRound.getLayoutParams();
                                    if (layoutParams != null) {
                                        layoutParams.height = (int) height;
                                        layoutParams.width = (int) width;
                                        layoutParams1.height = (int) height;
                                        layoutParams1.width = (int) width;
                                        videoCoverImg.setLayoutParams(layoutParams);
                                        videoCoverRound.setLayoutParams(layoutParams1);
                                    }

                                    final String url = datas.getPlayUrl();
                                    final String coverUrl = datas.getCoverUrl();
                                    mDynamicNineGrid.setVisibility(View.GONE);
                                    videoLayout.setVisibility(View.VISIBLE);

                                    videoLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DynamicDetailActivity.this, VideoPlayerActivity.class);
                                            intent.putExtra("video_url", url);
                                            intent.putExtra("cover_url", coverUrl);
                                            startActivity(intent);
                                        }
                                    });

                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.error(R.drawable.rc_image_error);
                                    requestOptions.placeholder(R.drawable.rc_image_error);
                                    Glide.with(DynamicDetailActivity.this)
                                            .asBitmap()//制Glide返回一个Bitmap对象
                                            .load(datas.getCoverUrl())
                                            .apply(requestOptions)
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                                    int width = bitmap.getWidth();
                                                    int height = bitmap.getHeight();
                                                    suitView(videoCoverImg, width, height);
                                                    suitView(videoCoverRound, width, height);
                                                    videoCoverImg.setImageBitmap(bitmap);
                                                }

                                            });
                                } else if (datas.getPic().size() == 1) {
//                                    float width = defaultDisplay.getWidth();
//                                    float height = defaultDisplay.getHeight();
//                                    if (!"0".equals(datas.getWidth()) && !"0".equals(datas.getHeight())) {
//                                        float videoWidth = Float.parseFloat(datas.getWidth());
//                                        float videoHeight = Float.parseFloat(datas.getHeight());
//                                        height = width * (videoHeight / videoWidth);
//                                    }
//                                    ViewGroup.LayoutParams layoutParams = videoCoverImg.getLayoutParams();
//                                    ViewGroup.LayoutParams layoutParams1 = videoCoverRound.getLayoutParams();
//                                    if (layoutParams != null) {
//                                        layoutParams.height = (int) height;
//                                        layoutParams.width = (int) width;
//                                        layoutParams1.height = (int) height;
//                                        layoutParams1.width = (int) width;
//                                        videoCoverImg.setLayoutParams(layoutParams);
//                                        videoCoverRound.setLayoutParams(layoutParams1);
//                                    }

                                    final String url = datas.getPlayUrl();
                                    playBtn.setVisibility(View.GONE);
                                    mDynamicNineGrid.setVisibility(View.GONE);
                                    videoLayout.setVisibility(View.VISIBLE);
                                    videoLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                                            ImageInfo info = new ImageInfo();
                                            info.setThumbnailUrl(datas.getPic().get(0));
                                            info.setBigImageUrl(datas.getPic().get(0));
                                            imageInfo.add(info);
                                            Intent intent = new Intent(DynamicDetailActivity.this, ImagePreviewActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                                            bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.error(R.drawable.rc_image_error);
                                    requestOptions.placeholder(R.drawable.rc_image_error);
                                    Glide.with(DynamicDetailActivity.this)
                                            .asBitmap()//制Glide返回一个Bitmap对象
                                            .load(datas.getPic().get(0))
                                            .apply(requestOptions)
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                                    int width = bitmap.getWidth();
                                                    int height = bitmap.getHeight();
                                                    suitView(videoCoverImg, width, height);
                                                    suitView(videoCoverRound, width, height);
                                                    videoCoverImg.setImageBitmap(bitmap);
                                                }

                                            });

//                                    Glide.with(DynamicDetailActivity.this)//制Glide返回一个Bitmap对象
//                                            .load(datas.getPic().get(0)).into(videoCoverImg);
                                } else {
                                    mDynamicNineGrid.setVisibility(View.VISIBLE);
                                    videoLayout.setVisibility(View.GONE);
                                    NineGridUtils.loadPics(DynamicDetailActivity.this, datas, datas.getPic(), mDynamicNineGrid, mDynamicPic);
                                }
                                charmval = datas.getCharm_val();
                                if (!charmval.equals("0")) {
                                    mDynamicDetailLlBeautyCount.setVisibility(View.VISIBLE);
                                    mDynamicDetailBeautyCount.setText(charmval);
                                } else {
                                    mDynamicDetailLlBeautyCount.setVisibility(View.GONE);
                                }
                                wealthwal = datas.getWealth_val();
                                if (!wealthwal.equals("0")) {
                                    mDynamicDetailLlRichCount.setVisibility(View.VISIBLE);
                                    mDynamicDetailRichCount.setText(wealthwal);
                                } else {
                                    mDynamicDetailLlRichCount.setVisibility(View.GONE);
                                }

                                if (admin.equals("1")) {
                                    mDynamicDetailRecommendCount.setVisibility(View.VISIBLE);
                                    mDynamicDetailRecommendCount.setText("发" + datas.getDynamic_num() + "推" + datas.getRdynamic_num() + "隐" + datas.getHidedstatustimes() + "封" + datas.getDynamicstatusouttimes());
                                }


                            } else if (retcode == 4002) {
                                ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                mDynamicDetailSandian.setVisibility(View.INVISIBLE);
                                mDynamicDetailBottomLl.setVisibility(View.INVISIBLE);
                                mDynamicDetailIcon.setVisibility(View.INVISIBLE);
                            }
                            if (first == 0) {
                                //初始化viewpager
                                initViewPager();
                                first = 1;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getRewardList(final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetRewardListNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Log.i("fragmentdynamicdetail", "run: " + response);
                        RewardData data = new Gson().fromJson(response, RewardData.class);
                        if (data.getData().size() == 0) {
                            if (page != 0) {
                                noMore = true;
                                ToastUtil.show(getApplicationContext(), "没有更多");
                            } else {
                                //mDynamicDetailListview.setVisibility(View.INVISIBLE);
                                rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            rvDynamicDetailComment.setVisibility(View.VISIBLE);
                            //mDynamicDetailListview.setVisibility(View.VISIBLE);
                            if (page == 0) {
                                rewarddatas.addAll(data.getData());

                                rewardCommentAdapter = new RewardCommentAdapter(DynamicDetailActivity.this, rewarddatas, null);
                                rvDynamicDetailComment.setAdapter(rewardCommentAdapter);
                            } else {
                                int temp = rewarddatas.size();
                                rewarddatas.addAll(data.getData());
                                rewardCommentAdapter.notifyItemRangeInserted(temp, data.getData().size());
                            }
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        //mDynamicDetailPullToRefreshScrollView.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                getRewardList(0);
            }
        });
    }

    private void getLaudList(final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetLaudListNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LaudListData data = new Gson().fromJson(response, LaudListData.class);
//                        if (data.getRetcode()==2000) {
//                            if (data.getData().size() == 0) {
//                                if (page != 0) {
//
//                                } else {
//                                    //mDynamicDetailListview.setVisibility(View.INVISIBLE);
//                                    rvDynamicDetailComment.setVisibility(View.INVISIBLE);
//                                }
//                                noMore = true;
//                                ToastUtil.show(DynamicDetailActivity.this.getApplicationContext(), "没有更多");
//                            } else {
//                                //mDynamicDetailListview.setVisibility(View.VISIBLE);
//                                rvDynamicDetailComment.setVisibility(View.VISIBLE);
//                                if (page ==0) {
//                                    datas.addAll(data.getData());
//                                    thumbAdapter = new ThumbUpCommentAdapter(DynamicDetailActivity.this, datas,null);
//                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DynamicDetailActivity.this);
//                                    rvDynamicDetailComment.setAdapter(thumbAdapter);
//                                    rvDynamicDetailComment.setLayoutManager(linearLayoutManager);
//                                } else {
//                                    int temp = datas.size();
//                                    datas.addAll(data.getData());
//                                    thumbAdapter.notifyItemRangeInserted(temp, data.getData().size());
//                                }
//                            }
//                            if (dialog != null) {
//                                dialog.dismiss();
//                            }
//                        }else {
//                            if (dialog != null) {
//                                dialog.dismiss();
//                            }
//                            ToastUtil.show(getApplicationContext(),data.getMsg());
//                            if(page == 0) {
//                                rvDynamicDetailComment.setVisibility(View.INVISIBLE);
//                            }
//                        }
                        //mDynamicDetailPullToRefreshScrollView.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                // getLaudList(page);
                if (page == 0) {
                    rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void getComList(final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("order", sortType == 1 ? "time" : "hot");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCommentListNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CommentData data = new Gson().fromJson(response, CommentData.class);
                        if (2000 == data.getRetcode()) {
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    noMore = true;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                } else {
                                    //mDynamicDetailListview.setVisibility(View.INVISIBLE);
                                    rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                // mDynamicDetailListview.setVisibility(View.VISIBLE);
                                rvDynamicDetailComment.setVisibility(View.VISIBLE);
                                if (page == 0) {
                                    commentdatas.clear();
                                    commentdatas.addAll(data.getData());
                                    commentLevelOneAdapter = new CommentLevelOneAdapter(DynamicDetailActivity.this, commentdatas, did, 1);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DynamicDetailActivity.this);
                                    rvDynamicDetailComment.setAdapter(commentLevelOneAdapter);
                                    rvDynamicDetailComment.setLayoutManager(linearLayoutManager);
                                    commentLevelOneAdapter.setOnCommentClickListener(new CommentLevelOneAdapter.OnCommentClickListener() {
                                        @Override
                                        public void onItemClick(View view) {
                                            int index = rvDynamicDetailComment.getChildAdapterPosition(view);
                                            //回复评论
                                            currentIndex = index;
                                            otheruid = commentdatas.get(index).getUid();
                                            otherName = commentdatas.get(index).getNickname();
                                            if (mDynamicDetailLlEt.getVisibility() == View.GONE) {
                                                mDynamicDetailLlEt.setVisibility(View.VISIBLE);
                                                mDynamicDetailEt.setText("");
                                                mDynamicDetailEt.requestFocus();
                                                imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
                                                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                                                mDynamicDetailEt.setHint("回复 " + commentdatas.get(index).getNickname() + ":");
                                                pid = commentdatas.get(index).getCmid();
                                                mDynamicDetailBottomLl.setVisibility(View.GONE);
                                            } else {
                                                mDynamicDetailLlEt.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onItemThumbUp(View view) {
                                            final int index = rvDynamicDetailComment.getChildAdapterPosition(view);
                                            if ("0".equals(commentdatas.get(index).getIs_like())) {
                                                HttpHelper.getInstance().thumbUpComment(commentdatas.get(index).getCmid(), new HttpListener() {
                                                    @Override
                                                    public void onSuccess(String data) {
                                                        ToastUtil.show(DynamicDetailActivity.this, "点赞成功");
                                                        commentdatas.get(index).setIs_like("1");
                                                        commentdatas.get(index).setLikenum(String.valueOf(Integer.parseInt(commentdatas.get(index).getLikenum()) + 1));
                                                        commentLevelOneAdapter.notifyItemChanged(index);
                                                    }

                                                    @Override
                                                    public void onFail(String msg) {
                                                        ToastUtil.show(DynamicDetailActivity.this, msg);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onItemHeadViewClick(View view) {
                                            int index = rvDynamicDetailComment.getChildAdapterPosition(view);
                                            Intent intent = new Intent(DynamicDetailActivity.this, PesonInfoActivity.class);
                                            intent.putExtra("uid", commentdatas.get(index).getUid());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onItemLongClick(View view) {
                                            int index = rvDynamicDetailComment.getChildAdapterPosition(view);
                                            showCommentOperatePop(index);
                                        }
                                    });

                                } else {
                                    int temp = commentdatas.size();
                                    commentdatas.addAll(data.getData());
                                    if (commentLevelOneAdapter != null) {
                                        commentLevelOneAdapter.notifyItemRangeInserted(temp, data.getData().size());
                                    }
                                    //commentadapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            ToastUtil.show(getApplicationContext(), data.getMsg());
                            if (page == 0) {
                                rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                            }
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        //mDynamicDetailPullToRefreshScrollView.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                getComList(0);
            }
        });
    }

    private void gettopcardchargeList(final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("did", did);
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardUsedRs, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Log.i("fragmentdynamicdetail", "run: " + response);
                        DynamicDetailBean data = new Gson().fromJson(response, DynamicDetailBean.class);
                        if (data.getRetcode() == 2000) {
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    noMore = true;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                } else {
                                    // mDynamicDetailListview.setVisibility(View.INVISIBLE);
                                    rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                //mDynamicDetailListview.setVisibility(View.VISIBLE);
                                rvDynamicDetailComment.setVisibility(View.VISIBLE);
                                if (page == 0) {
                                    topcardysedrsdatas.addAll(data.getData());

                                    tuidingCommentAdapter = new TuidingCommentAdapter(DynamicDetailActivity.this, topcardysedrsdatas);
                                    rvDynamicDetailComment.setAdapter(tuidingCommentAdapter);
                                } else {
                                    int temp = topcardysedrsdatas.size();
                                    topcardysedrsdatas.addAll(data.getData());
                                    tuidingCommentAdapter.notifyItemRangeInserted(temp, data.getData().size());
                                }
                            }
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            //mDynamicDetailPullToRefreshScrollView.onRefreshComplete();
                        } else {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            ToastUtil.show(getApplicationContext(), data.getMsg());
                            if (page == 0) {
                                rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (page == 0) {
                    rvDynamicDetailComment.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.mDynamicDetail_return, R.id.mDynamicDetail_sandian, R.id.mDynamicDetail_icon, R.id.mDynamicDetail_bottom_ll_zan, R.id.mDynamicDetail_bottom_ll_pinglun, R.id.mDynamicDetail_bottom_ll_dashang, R.id.mDynamicDetail_tvSend, R.id.mDynamicDetail_all_layout, R.id.mDynamicDetail_vip})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mDynamicDetail_return:
                finish();
                break;
            case R.id.mDynamicDetail_sandian:  //点击右上角三点
                String delete_vs_report = uid.equals(MyApp.uid) ? "删除" : "举报";
                String tab4 = stickstat.equals("0") ? "置顶（vip）" : "取消置顶(vip)"; // VIP置顶状态
                String tab5 = is_hidden.equals("0") ? "隐藏" : "取消隐藏"; // 判断是否隐藏
                String tab6 = collectstate.equals("0") ? "收藏" : "取消收藏"; // 判断是否收藏
                String tab1 = recommend.equals("0") ? "" : "取消推荐";
                String tab2 = recommendAll.equals("0") ? "" : "取消置顶";
                // 根据是否是查看本人帖子判断 删除 还是举报
                String[] tabs = null;
                if (admin.equals("1")) {
                    // 身份判断  管理员本人
                    if (uid.equals(MyApp.uid)) {
                        tabs = new String[]{"置顶", "推荐", tab1, tab2, tab5, "编辑", "分享", tab4, tab6, delete_vs_report};
                    } else {
                        tabs = new String[]{"标记", "置顶", "推荐", tab1, tab2, tab5, "编辑", "分享", tab6, delete_vs_report};
                    }
                    setAdminOperation(tabs);

                } else if ("1".equals(mybkvip)) {
                    // 顾问
                    List<String> list_tabs = new ArrayList<>();
                    // 是顾问，但是具体的权限写活通过  bk_vip_role 返回情况判断
                    if (!TextUtil.isEmpty(bk_vip_role)) {
                        String[] split = bk_vip_role.split(",");
                        // 1隐藏动态 2编辑话题 3推荐动态 4 取消推荐动态
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals("1")) {
                                list_tabs.add("隐藏");
                            } else if (split[x].equals("2")) {
                                list_tabs.add("编辑");
                            } else if (split[x].equals("3")) {
                                list_tabs.add("推荐");
                            } else if (split[x].equals("4")) {
                                list_tabs.add(tab1);
                            }
                        }
                    }
                    list_tabs.add(delete_vs_report);
                    list_tabs.add(tab6);
                    list_tabs.add("分享");

                    if (uid.equals(MyApp.uid)) {
                        list_tabs.add(0, tab4);
                        tabs = list_tabs.toArray(new String[list_tabs.size()]);
                    } else {
                        tabs = list_tabs.toArray(new String[list_tabs.size()]);
                    }

                    setAdminOperation(tabs);

                } else if ("1".equals(myblvip)) {
                    // 蓝V
                    List<String> list_tabs = new ArrayList<>();
                    if (!TextUtil.isEmpty(bl_vip_role)) {
                        String[] split = bl_vip_role.split(",");
                        // 1隐藏动态 2编辑话题 3推荐动态 4 取消推荐动态
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals("1")) {
                                list_tabs.add("隐藏");
                            } else if (split[x].equals("2")) {
                                list_tabs.add("编辑");
                            } else if (split[x].equals("3")) {
                                list_tabs.add("推荐");
                            } else if (split[x].equals("4")) {
                                list_tabs.add(tab1);
                            }
                        }
                    }
                    list_tabs.add(delete_vs_report);
                    list_tabs.add(tab6);
                    list_tabs.add("分享");

                    if (uid.equals(MyApp.uid)) {
                        list_tabs.add(0, tab4);
                        tabs = list_tabs.toArray(new String[list_tabs.size()]);
                    } else {
                        tabs = list_tabs.toArray(new String[list_tabs.size()]);
                    }
                    setAdminOperation(tabs);

                } else if ("1".equals(svip) || "1".equals(vip)) {
                    if (uid.equals(MyApp.uid)) {
                        //判断当前是不是我的页面
                        tabs = new String[]{"编辑（svip）", tab4, "分享", tab6, delete_vs_report};
                    } else {
                        tabs = new String[]{"分享", tab6, delete_vs_report};
                    }
                    setAdminOperation(tabs);
                } else {
                    // 普通身份
                    tabs = new String[]{"分享", tab6, delete_vs_report};
                    setAdminOperation(tabs);
                }
                break;
            case R.id.mDynamicDetail_icon:
                intent = new Intent(this, PesonInfoActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.mDynamicDetail_bottom_ll_zan:
                if (mDynamicDetailLlEt.getVisibility() == View.VISIBLE) {
                    mDynamicDetailLlEt.setVisibility(View.GONE);
                }
                page = 0;
                datas.clear();
                mDynamicDetailBottomLlZan.setEnabled(false);
                if (zanstate == 0) {
                    laudDynamic(dynamicPos);
                }
//                else {
//                    cancelLaud(dynamicPos);
//                }
                break;
            case R.id.mDynamicDetail_bottom_ll_pinglun:
                sendapplyfor();
                break;
            case R.id.mDynamicDetail_bottom_ll_dashang:
                if (mDynamicDetailLlEt.getVisibility() == View.VISIBLE) {
                    mDynamicDetailLlEt.setVisibility(View.GONE);
                }
                /*if (uid.equals(MyApp.uid)) {
                    ToastUtil.show(getApplicationContext(), "您不可以打赏自己...");
                } else {
                    new DashangDialogNew(this, did, dynamicPos, dashangcount);
                }*/
                new DashangDialogNew(this, did, dynamicPos, dashangcount, uid);
                break;
            case R.id.mDynamicDetail_tvSend:
                if (dynamicRetcode != 3001) {
                    if (isSend) {
//                    String timeMilliss=String.valueOf(System.currentTimeMillis());
//                    String millis= (String) SharedPreferencesUtils.getParam(getApplicationContext(),"commentTimeMillis","0");
//                    if((Long.parseLong(timeMilliss)-Long.parseLong(millis))>(60*1*1000)){
                        if (!mDynamicDetailTvSend.getText().toString().equals("")) {
                            mDynamicDetailTvSend.setEnabled(false);
                            sendComment();
                        } else {
                            ToastUtil.show(getApplicationContext(), "说点什么吧...");
                        }
//                    }else{
//                        ToastUtil.show(getApplicationContext(), "您评论太快了，请1分钟后再评论~");
//                    }

                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;
            case R.id.mDynamicDetail_vip:
                intent = new Intent(this, VipCenterActivity.class);
                intent.putExtra("headpic", (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", ""));
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
        }
    }


    public void setAdminOperation(String[] tabs) {
        new AlertView(null, null, "取消", null,
                tabs, this, AlertView.Style.ActionSheet, this).show();
    }

    private void sendComment() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("content", mDynamicDetailEt.getText().toString());
        map.put("otheruid", otheruid);
        CharSequence hintStr = mDynamicDetailEt.getHint();
        if (hintStr != null && hintStr.toString().startsWith("回复")) {
            map.put("pid", pid);
        }
        String substring = "";
        String substring2 = "";
        platuid = "";
        platuname = "";
        for (int i = 0; i < atuidlist.size(); i++) {
            platuid += atuidlist.get(i) + ",";
            platuname += atunamelist.get(i) + ",";
        }
        if (atuidlist.size() > 0) {
            substring = platuid.substring(0, platuid.length() - 1);
            substring2 = platuname.substring(0, platuname.length() - 1);
        }

        map.put("atuid", substring);
        map.put("atuname", substring2);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendCommentNewred, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("dynamicdetailComment", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
////                                    SharedPreferencesUtils.setParam(getApplicationContext(),"commentTimeMillis",String.valueOf(System.currentTimeMillis()));
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    pingluncount = String.valueOf(Integer.parseInt(pingluncount) + 1);
                                    atuidlist.clear();
                                    ((TextView) mDynamicDetailTabs.getTabAt(1).getCustomView().findViewById(R.id.tab_item_textview)).setText("评论 " + pingluncount);
                                    mDynamicDetailTabs.getTabAt(1).select();

//                                    imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
////                                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                                    imm.hideSoftInputFromWindow(mDynamicDetailEt.getWindowToken(), 0);
//                                    mDynamicDetailEt.setText("");
//                                    mDynamicDetailLlEt.setVisibility(View.GONE);
//                                    commentdatas.clear();
//                                    getComList();
////                                    EventBus.getDefault().post(new DynamicCommentEvent(dynamicPos, (Integer.parseInt(pingluncount) + 1)));
//                                    pingluncount = (Integer.parseInt(pingluncount) + 1) + "";
                                    imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                                    imm.hideSoftInputFromWindow(mDynamicDetailEt.getWindowToken(), 0);
                                    CharSequence hintStr = mDynamicDetailEt.getHint();
                                    if (hintStr != null && hintStr.toString().startsWith("回复")) {
                                        List<CommentData.DataBean.SubsetcommentBean> subsetcomment = commentdatas.get(currentIndex).getSubsetcomment();
                                        if (subsetcomment == null) {
                                            subsetcomment = new ArrayList<>();
                                        }
                                        CommentData.DataBean.SubsetcommentBean temp = new CommentData.DataBean.SubsetcommentBean();
                                        temp.setOthernickname(otherName);
                                        temp.setContent(content);
                                        temp.setNickname("我");
                                        temp.setUid(MyApp.uid);
                                        temp.setOtheruid(otheruid);
                                        subsetcomment.add(temp);
                                        commentdatas.get(currentIndex).setSubsetcomment(subsetcomment);
                                        commentLevelOneAdapter.notifyItemChanged(currentIndex);
                                    } else {
                                        page = 0;
                                        getComList(page);
                                        mDynamicDetailTabs.setScrollPosition(1, 0, true);
                                    }
                                    break;
                                case 3000:
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            mDynamicDetailLlEt.setVisibility(View.GONE);
                            mDynamicDetailTvSend.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void laudDynamic(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.LaudDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("laudDynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
//                                    ToastUtil.show(context.getApplicationContext(),obj.getString("msg"));
//                                    list.get(pos).setLaudnum((Integer.parseInt(list.get(pos).getLaudnum())+1)+"");
                                    zanstate = 1;
                                    count = Integer.parseInt(zancount) + 1;
                                    zancount = count + "";
                                    //mDynamicDetailTabs.getTabAt(0).setText("赞 " + zancount);
                                    Drawable drawable = getResources().getDrawable(R.mipmap.zanhong);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailBottomDianzan.setCompoundDrawables(drawable, null, null, null);
                                    mDynamicDetailTabs.setScrollPosition(0, 0, true);
                                    getLaudList(0);
                                    EventBus.getDefault().post(new DynamicEvent(dynamicPos, zanstate, Integer.parseInt(zancount)));
                                    ((TextView) mDynamicDetailTabs.getTabAt(0).getCustomView().findViewById(R.id.tab_item_textview)).setText("赞 " + zancount);


//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                case 4003:
                                case 4004:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            mDynamicDetailBottomLlZan.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void cancelLaud(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelLaud, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("cancelDynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
//                                    ToastUtil.show(context.getApplicationContext(),obj.getString("msg"));
//                                    list.get(pos).setLaudnum((Integer.parseInt(list.get(pos).getLaudnum())-1)+"");
                                    zanstate = 0;
                                    count = Integer.parseInt(zancount) - 1;
                                    zancount = count + "";
                                    ((TextView) mDynamicDetailTabs.getTabAt(0).getCustomView().findViewById(R.id.tab_item_textview)).setText("赞 " + zancount);


                                    Drawable drawable = getResources().getDrawable(R.mipmap.dianzanda);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mDynamicDetailBottomDianzan.setCompoundDrawables(drawable, null, null, null);
                                    mDynamicDetailTabs.setScrollPosition(0, 0, true);
                                    getLaudList(page);
                                    EventBus.getDefault().post(new DynamicEvent(dynamicPos, zanstate, Integer.parseInt(zancount)));
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4003:
                                case 4004:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            mDynamicDetailBottomLlZan.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.mDynamicDetail_listview:
//                currentIndex = position;
//                //回复评论
//                otheruid = commentdatas.get(position).getUid();
//                otherName = commentdatas.get(position).getOthernickname();
//                if (!otheruid.equals(MyApp.uid)) {
//                    if (mDynamicDetailLlEt.getVisibility() == View.GONE) {
//                        mDynamicDetailLlEt.setVisibility(View.VISIBLE);
//                        mDynamicDetailEt.setText("");
//                        mDynamicDetailEt.requestFocus();
//                        imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                        mDynamicDetailEt.setHint("回复 " + commentdatas.get(position).getNickname() + ":");
//                        pid = commentdatas.get(position).getCmid();
//                    } else {
//                        mDynamicDetailLlEt.setVisibility(View.GONE);
//                    }
//                }
//                Log.i("commentuid", "onItemClick: " + commentdatas.get(position).getOtheruid() + "," + commentdatas.get(position).getUid());
                break;
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.mDynamicDetail_listview:
                final OperateCommentPopup operateCommentPopup = new OperateCommentPopup(DynamicDetailActivity.this);
                operateCommentPopup.setOnCommentOperateListener(new OperateCommentPopup.OnCommentOperateListener() {
                    @Override
                    public void onCommentCopy() {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", commentdatas.get(position).getContent());
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        ToastUtil.show(DynamicDetailActivity.this, "已复制到剪贴板");
                    }

                    @Override
                    public void onCommentReport() {
                        Intent intent = new Intent(DynamicDetailActivity.this, OtherReasonActivity.class);
                        intent.putExtra("uid", MyApp.uid);
                        intent.putExtra("did", did);
                        intent.putExtra("cmid", commentdatas.get(position).getCmid());
                        startActivity(intent);
                        operateCommentPopup.dismiss();
                    }

                    @Override
                    public void onCommentDelete() {

                    }
                });
                operateCommentPopup.showPopupWindow();
                break;
        }
        return true;
    }

    private void getJudgeDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.JudgeDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            dynamicRetcode = json.getInt("retcode");
                            switch (dynamicRetcode) {
                                case 2000:
                                    isSend = true;
                                    break;
                                case 3001:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 4003:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 4004:
                                    isSend = true;
                                    break;
                                case 5000:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void showdialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        ProgressBar bar = new ProgressBar(this);
        builder.setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        switch (type) {
            case 0:
                getLaudList(page);
                break;
            case 1:
                getComList(page);
                break;
            case 2:
                getRewardList(page);
                break;
            case 3:
                gettopcardchargeList(page);
                break;
        }
    }


    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (data) {
            case "删除":
                //删除动态
                deleteDynamic();
                break;
            case "举报":
                if (reportstate.equals("0")) {
                    Intent intent = new Intent(this, OtherReasonActivity.class);
                    intent.putExtra("uid", MyApp.uid);
                    intent.putExtra("did", did);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getApplicationContext(), "只能举报一次哦~");
                }
                break;
            case "置顶（vip）":
                //vip动态置顶 仅作用自己的动态
                setStickDynamic();
                break;
            case "取消置顶(vip)":
                //vip动态取消置顶 仅作用自己的动态
                cancelSetStickDynamic();
                break;
            case "隐藏":
                //隐藏动态
                dynamicForbid();
                break;
            case "取消隐藏":
                //取消隐藏动态
                dynamicResume();
                break;
            case "收藏":
                collectDynamic();
                break;
            case "取消收藏":
                cancelCollectDynamic();
                break;
            case "取消推荐":
                adminCancelDynamicRecommend();
                break;
            case "推荐":
                //管理员推荐

                adminSetDynamicRecommend();
                break;
            case "取消置顶":
                //管理员取消置顶
                adminCannelDynamicTop();
                break;
            case "置顶":
                //管理员置顶
                adminSetDynamicTop();
                break;
            case "标记":
                //管理员推标记
                getsetMark();
                break;
            case "分享":
                //分享
                showShareWay();
                break;
            case "编辑（svip）":
                //编辑动态
                if ("1".equals(svip)) {
                    editDynamic("不限制");
                } else if ("1".equals(vip)) {
                    // 弹出 开通svip 弹框
                    new VipDialog(this, "编辑功能限SVIP会员可用~");
                }
                break;
            case "编辑":
                //编辑动态
                String is_display = "";
                if (admin.equals("1") || uid.equals(MyApp.uid)) {
                    is_display = "不限制";
                    // 管理员的编辑可以编辑所有内容  编辑自己的文章 可以编辑所有内容
                } else {
                    // 顾问，黑V 的编辑可以编辑话题
                    is_display = "限制";
                }
                editDynamic(is_display);

                break;

        }

    }


    private void deleteDynamic() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定删除动态吗?")
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delDynamic();
            }
        }).create().show();
    }

    private void adminSetDynamicTop() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "1");
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetDynamicRerommend, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    recommendAll = "1";
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    private void adminCannelDynamicTop() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "1");
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelDynamicRerommend, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    recommendAll = "0";
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void showAdminRecommendPop() {

    }

    private void adminSetDynamicRecommend() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "0");
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetDynamicRerommend, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("SetDynamicRerommend", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    recommend = "1";
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void adminCancelDynamicRecommend() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "0");
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelDynamicRerommend, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("SetDynamicRerommend", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    recommend = "0";
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void dynamicForbid() {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DynamicForbid, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    is_hidden = "1";
                                    //右上角显示图标。
                                    if (is_hidden.equals("1")) {
                                        mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaiyinsi);
                                    } else {
                                        if (stickstat.equals("1")) {
                                            mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaizhiding);
                                        } else {
                                            if (!recommend.equals("0")) {
                                                mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.jian);
                                            }
                                        }
                                    }
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void dynamicResume() {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DynamicResume, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    is_hidden = "0";
                                    //右上角显示图标。
                                    if (is_hidden.equals("1")) {
                                        mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaiyinsi);
                                    } else {
                                        if (stickstat.equals("1")) {
                                            mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaizhiding);
                                        } else {
                                            if (!recommend.equals("0")) {
                                                mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.jian);
                                            }
                                        }
                                    }
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void recommendDynamic() {
        if (!realname.equals("0") || !vip.equals("0")) {
            if (recommend.equals("0")) {
                Intent intent = new Intent(this, RecommendReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                startActivity(intent);
            } else {
                ToastUtil.show(getApplicationContext(), "该动态已经被推荐了...");
            }
        } else {
            ToastUtil.show(getApplicationContext(), "请您开通会员或者自拍认证...");
        }
    }

    private void editDynamic(String is_display) {
        Intent intent = new Intent(this, EditDynamicActivity.class);
        intent.putExtra("did", did);
        intent.putExtra("headurl", headpic);
        intent.putExtra("name", name);
        intent.putExtra("online", isonline);
        intent.putExtra("shiming", shiming);
        intent.putExtra("readtimes", readtimes);
        intent.putExtra("age", age);
        intent.putExtra("role", role);
        intent.putExtra("sex", sex);
        intent.putExtra("distance", distance + "");
        intent.putExtra("addtime", time);
        intent.putExtra("content", content);
        intent.putStringArrayListExtra("sypic", sypics);
        intent.putStringArrayListExtra("slpic", slpics);
        intent.putExtra("fuid", fuid);
        intent.putExtra("fvipannual", fvipannual);
        intent.putExtra("fvolunteer", fvolunteer);
        intent.putExtra("fadmin", fadmin);
        intent.putExtra("fsvip", fsvip);
        intent.putExtra("fsvipannual", fsvipannual);
        intent.putExtra("fvip", fvip);
        intent.putExtra("retcode", retcode);
        intent.putExtra("charmval", charmval);
        intent.putExtra("wealthwal", wealthwal);
        intent.putExtra("dynamicPos", dynamicPos);
        intent.putExtra("tid", tid);
        intent.putExtra("topictitle", topictile);
        intent.putExtra("atuid", atuid);
        intent.putExtra("atuname", atuname);
        intent.putExtra("bkvip", bkvip);
        intent.putExtra("blvip", blvip);
        intent.putExtra("is_display", is_display); // 区分是否是管理员编辑
        startActivity(intent);
    }

    //取消收藏
    private void cancelCollectDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelCollectDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    collectstate = "0";
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void collectDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CollectDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("collectdynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    collectstate = "1";
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 3000:
                                case 4001:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void cancelSetStickDynamic() {//quxiao 置顶
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetUnStickDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("cancelsetStickDynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    stickstat = "0";
                                    if (!recommend.equals("0")) {
                                        mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.jian);
                                    } else {
                                        mDynamicDetailHeaderLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post(new DynamicTopEvent());
                                    break;
                                case 4001:
                                    new VipDialog(DynamicDetailActivity.this, "会员才能使用置顶功能哦~");
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setStickDynamic() {//置顶
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetStickDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("setStickDynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    stickstat = "1";
                                    mDynamicDetailHeaderLayout.setBackgroundResource(R.drawable.dongtaizhiding);
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post(new DynamicTopEvent());
                                    break;
                                case 4001:
                                    new VipDialog(DynamicDetailActivity.this, "会员才能使用置顶功能哦~");
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    private void showShareWay() {
        String pic = "";
        if (didpic.size() > 0) {
            pic = didpic.get(0);
        }
        SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic() + HttpUrl.ShareDynamicDetail + did, "来自圣魔的动态", name + " 在圣魔发布了精彩的动态。", headpic, 0, 1, did, content, pic, uid);
        sharedPop.showAtLocation(mDynamicDetailAllLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getWindow().getAttributes();
                params[0].alpha = 1f;
                getWindow().setAttributes(params[0]);
            }
        });
    }

    private void delDynamic() {
        String httpurl;
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        IRequestManager manager = RequestFactory.getRequestManager();
        if (uid.equals(MyApp.uid)) {
            httpurl = HttpUrl.DelDynamic;
        } else {
            if (admin.equals("1")) {
                httpurl = HttpUrl.AdminDelDynamic;
            } else {
                httpurl = HttpUrl.DelDynamic;
            }
        }
        manager.post(httpurl, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("deletesuccess", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post(new CallBackEvent());
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DynamicDetailActivity.this, TopicDetailActivity.class);
            intent.putExtra("tid", tid);
            intent.putExtra("topictitle", topictile);
            startActivity(intent);
        }
    };


    class Clickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.purple_main));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rewardEventBus(DynamicRewardEvent event) {
        ((TextView) mDynamicDetailTabs.getTabAt(2).getCustomView().findViewById(R.id.tab_item_textview)).setText("打赏" + event.getRewardcount());

        mDynamicDetailTabs.setScrollPosition(2, 0, true);
        rewarddatas.clear();
        page = 0;
        getRewardList(page);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rewardEventBus(String message) {
        if (message.equals("deleteSuccess")) {
            ((TextView) mDynamicDetailTabs.getTabAt(1).getCustomView().findViewById(R.id.tab_item_textview)).setText("评论 " + (Integer.parseInt(pingluncount) - 1));
            pingluncount = Integer.parseInt(pingluncount) - 1 + "";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rewardEventBus(ReportEvent event) {
        reportstate = "1";
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(DynamicDetailEvent event) {
        getDynamicDetail();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentListChange(CommentDetailEvent event) {
        if (event.getType() == 1) {
            page = 0;
            getComList(page);
        } else if (event.getType() == 2) {
            commentdatas.remove(event.getPosition());
            commentLevelOneAdapter.notifyItemRemoved(event.getPosition());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MyApp.uid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", "");
//        getJudgeDynamic();
//        getusertopcardinfo();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //推顶卡余额
    public void getusertopcardinfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("----", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    Gson gson = new Gson();
                                    EjectionshengyuBean ejectionBean = gson.fromJson(response, EjectionshengyuBean.class);
                                    wallet_topcard = ejectionBean.getData().getWallet_topcard();
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void sendapplyfor() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.getGirlState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                case 5001:
                                    if (mDynamicDetailLlEt.getVisibility() == View.GONE) {
                                        mDynamicDetailLlEt.setVisibility(View.VISIBLE);
                                        mDynamicDetailEt.setText("");
                                        mDynamicDetailEt.setHint("");
                                        otheruid = "0";
//                                        mDynamicDetailEt.requestFocus();
//                                        imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
//                                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                                    } else {
                                        mDynamicDetailLlEt.setVisibility(View.GONE);
                                    }
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                                default:
                                    ToastUtil.show(DynamicDetailActivity.this, object.getString("msg") + "");
                                    break;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void getsetMark() {
        // 标记
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        if (auditMark.equals("标记")) {
            map.put("type", "1");
        } else {
            map.put("type", "0");
        }
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.setDynamicAuditMark, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:

                                    if (auditMark.equals("标记")) {
                                        EventBus.getDefault().post(new DynamicMarke(dynamicPos, 1));
                                        auditMark = "取消标记";
                                    } else {
                                        EventBus.getDefault().post(new DynamicMarke(dynamicPos, 0));
                                        auditMark = "标记";
                                    }
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                                default:

                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gethaoyouliebiao(Message message) {
        if (message.arg2 == 11) {
            Atbean atbean = (Atbean) message.obj;
            if (mDynamicDetailEt.getText().toString().equals("@")) {
                mDynamicDetailEt.setText(" ");
            }
            List<Atbean.DataBean> dataBean = atbean.getDataBean();
            for (int i = 0; i < dataBean.size(); i++) {
                String uid = dataBean.get(i).getUid();
                atuidlist.add(uid);
                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");

//                ATSpan atSpan = new ATSpan(uid);

                SpannableString span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
                if (dataBean.get(i).getNickname().contains("[群]")) {
                    ATSpan atSpan = new ATSpan(uid, "群");
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } else if (dataBean.get(i).getNickname().contains("[高端]")) {
                    ATSpan atSpan = new ATSpan(uid, "高端");
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    ATSpan atSpan = new ATSpan(uid);
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
//                span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mDynamicDetailEt.append(span);
            }
            mDynamicDetailEt.setSelection(mDynamicDetailEt.getText().length());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

//        imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
////                                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//        imm.hideSoftInputFromWindow(mDynamicDetailEt.getWindowToken(), 0);

    }

    void showCommentOperatePop(final int index) {
        final String cmid = commentdatas.get(index).getCmid();
        String otheruid = commentdatas.get(index).getUid();
        final String content = commentdatas.get(index).getContent();
        final OperateCommentPopup operateCommentPopup;

        if (MyApp.uid.equals(uid) || "1".equals(admin) || MyApp.uid.equals(otheruid)) {
            operateCommentPopup = new OperateCommentPopup(DynamicDetailActivity.this, 0);
        } else {
            operateCommentPopup = new OperateCommentPopup(DynamicDetailActivity.this);
        }
        operateCommentPopup.setOnCommentOperateListener(new OperateCommentPopup.OnCommentOperateListener() {
            @Override
            public void onCommentCopy() {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", content);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.show(DynamicDetailActivity.this, "已复制到剪贴板");
            }

            @Override
            public void onCommentReport() {
                Intent intent = new Intent(DynamicDetailActivity.this, OtherReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                intent.putExtra("cmid", cmid);
                startActivity(intent);
                operateCommentPopup.dismiss();
            }

            @Override
            public void onCommentDelete() {
                operateCommentPopup.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(DynamicDetailActivity.this);
                builder.setMessage("确认删除吗?")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HttpHelper.getInstance().deleteComment(did, cmid, new HttpListener() {
                            @Override
                            public void onSuccess(String data) {
                                ToastUtil.show(DynamicDetailActivity.this, "删除成功");
                                commentdatas.remove(index);
                                commentLevelOneAdapter.notifyItemRemoved(index);
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtil.show(DynamicDetailActivity.this, msg);
                            }
                        });
                    }
                }).create().show();

            }

        });
        operateCommentPopup.showPopupWindow();

    }

    void initLoadMoreListener() {
        mDynamicDetailPullToRefreshScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //到底了
                    //加载更多
                    if (noMore) {
                        return;
                    }
                    page = page + 1;
                    switch (type) {
                        case 0:
                            getLaudList(page);
                            break;
                        case 1:
                            getComList(page);
                            break;
                        case 2:
                            getRewardList(page);
                            break;
                        case 3:
                            gettopcardchargeList(page);
                            break;
                    }
                }
            }
        });
    }


    void suitView(View view, int width, int height) {
        float w = defaultDisplay.getWidth();
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.width = (int) w;
        para.height = (int) (w * height / width);
        view.setLayoutParams(para);
    }

    int mx, my;
    int lastx, lasty;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取坐标点：
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (x > 50)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deletx = x - mx;
                int delety = y - my;
                if (Math.abs(deletx) > Math.abs(delety)) {
                    finish();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
        lastx = x;
        lasty = y;
        mx = x;
        my = y;
        return super.dispatchTouchEvent(ev);
    }


}
