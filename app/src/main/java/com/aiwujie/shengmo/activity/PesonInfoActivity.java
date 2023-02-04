package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.activity.binding.ChangeBindingMobileActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.BeizhuImgAdapter;
import com.aiwujie.shengmo.adapter.DynamicPersonMsgListviewAdapter;
import com.aiwujie.shengmo.adapter.PersonPresentGridViewAdapter;
import com.aiwujie.shengmo.adapter.RecyclerViewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllUserStates;
import com.aiwujie.shengmo.bean.BindingData;
import com.aiwujie.shengmo.bean.ChatFlagData;
import com.aiwujie.shengmo.bean.DialogStampData;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.FriendGroupListBean;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.bean.TopcardyesBean;
import com.aiwujie.shengmo.bean.UserInfoData;
import com.aiwujie.shengmo.bean.ViolationBean;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.bean.VipSecretSitData;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.customview.BindSvipDialog;
import com.aiwujie.shengmo.customview.CommonDialog;
import com.aiwujie.shengmo.customview.DashangDialogNew;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.customview.StampDialogNew;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.DynamicUpMediaEditDataEvent;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.eventbus.OwnerPresentEvent;
import com.aiwujie.shengmo.eventbus.RecorderEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.view.FitGridView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PesonInfoActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener, View.OnTouchListener {

    @BindView(R.id.mPersonInfo_return)
    ImageView mPersonInfoReturn;
    @BindView(R.id.mPersonInfo_sandian)
    ImageView mPersonInfoSandian;
    @BindView(R.id.mPersonInfo_title)
    PercentRelativeLayout mPersonInfoTitle;
    @BindView(R.id.mPersonInfo_listview)
    ListView mPersonInfoListview;
    @BindView(R.id.mPersonInfo_bottom_ll)
    PercentLinearLayout mPersonInfoBottomLl;
    @BindView(R.id.activity_peson_info)
    PercentRelativeLayout activityPesonInfo;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.mPersonInfo_edit)
    ImageView mPersonInfoEdit;
    ImageView mPersonInfoIvBg;
    ImageView mPersonInfoIcon;
    ImageView mPersonInfoVip;
    ImageView mPersonInfoHongniang;
    TextView mPersonInfoName;
    ImageView mPersonInfoRenzheng;
    ImageView mPersonInfoOnline;
    TextView mPersonInfoSex;
    TextView mPersonInfoRole;
    TextView mPersonInfoRichCount;
    PercentLinearLayout mPersonInfoLlRichCount;
    TextView mPersonInfoBeautyCount;
    PercentLinearLayout mPersonInfoLlBeautyCount;
    ImageView mPersonInfoYuyin;
    ImageView mPersonInfoLuyin;
    TextView mPersonInfoTime;
    TextView mPersonInfoDistance;
    TextView mPersonInfoCity;
    TextView mPersonInfoAlpha03;
    TextView mPersonInfoRegistTime;
    TextView mPersonInfoDonate;
    TextView mPersonInfoGuanzhuCount;
    PercentLinearLayout mPersonInfoLlGz;
    TextView mPersonInfoFansCount;
    PercentLinearLayout mPersonInfoLlFs;
    TextView mPersonInfoGroupCount;
    PercentLinearLayout mPersonInfoLlQz;
    TextView mPersonInfoPresentCount;
    MyGridview mPersonInfoPresentGridview;
    PercentLinearLayout mPersonInfoPresentLl;
    RecyclerView mPersonInfoRecyclerview;
    RelativeLayout mPersonInfoPhotoLl;
    ImageView mPersonInfoAddIv;
    TextView mPersonInfoPhotoLine;
    TextView mPersonInfoIntroduce;
    TextView mPersonInfoEditSign;
    PercentRelativeLayout mPersonInfoIntroduceLl;
    TextView mPersonInfoHeight;
    TextView mPersonInfoJiechu;
    TextView mPersonInfoWeight;
    TextView mPersonInfoShijian;
    TextView mPersonInfoStar;
    TextView mPersonInfoLevel;
    TextView mPersonInfoCulture;
    TextView mPersonInfoQuxiang;
    TextView mPersonInfoMoney;
    TextView mPersonInfoWant;
    @BindView(R.id.mPersonInfo_sendDynamic)
    ImageView mPersonInfoSendDynamic;
    @BindView(R.id.mPersonInfo_present)
    ImageView mPersonInfoPresent;
    @BindView(R.id.mPersonInfo_guanzhu)
    ImageView mPersonInfoGuanzhu;
    @BindView(R.id.mPersonInfo_chat)
    ImageView mPersonInfoChat;
    //    @BindView(R.id.gridlist)
    FitGridView imgGridView;
    private String uid;
    //个人资料简介Textview集合
    private List<TextView> datatvs;
    private Handler handler = new Handler();
    private String headurl;
    private String logintime;
    private ArrayList<String> photos;
    private String login_time_switch;
    private boolean isSend = false;
    private String toastStr;
    private String age;
    private String role;
    private String tall;
    private String weight;
    private String mProvince;
    private String mCity;
    private String volunteer;
    private String admin;
    private String nickname;
    private String blackState;
    private String setMessage_flag;
    //关注状态   1.已关注 2. 关注好友.3.互为好友
    private String followState;
    private String media;
    private String[] moneys = {"2魔豆", "6魔豆", "10魔豆", "38魔豆", "99魔豆", "88魔豆", "123魔豆", "166魔豆", "199魔豆", "520魔豆", "666魔豆", "250魔豆", "777魔豆", "888魔豆", "999魔豆", "1314魔豆", "1666魔豆", "1999魔豆", "666魔豆", "999魔豆", "1888魔豆", "2899魔豆", "3899魔豆", "6888魔豆", "9888魔豆", "52000魔豆", "99999魔豆", "1魔豆", "3魔豆", "5魔豆", "10魔豆", "8魔豆", "未知", "未知", "未知", "未知", "未知"};
    private boolean isFirst = true;
    //判断是否显示什么时间在线  0显示、1不显示
    private int timePoorState;
    private AlertDialog alertDialog;
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicPersonMsgListviewAdapter dynamicAdapter;
    private EditText etPsw;
    //判断操作是自己的还是其他人的
    private int operaFlag;
    private MediaPlayer player;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private TextView mPersonInfoMediaTime;
    private String mediatime;
    private ImageView mPersonInfoMediaIv;
    private String loginAdmin;
    private AutoRelativeLayout mPersonHeaderInfoBottomLL;
    private TextView mPersonHeaderInfoBottomTv;
    private AutoLinearLayout mPersonInfoIntroduceAdminLl;
    //    private TextView mPersonInfoSendTopicLine;
    private int dynamicRetcode;
    private String personIntro;
    private TextView mPersonInfoTopTv;
    private String realmobile = "";
    private String mobile;
    private AutoRelativeLayout mPersonHeaderInfoBottomLL02;
    private TextView mPersonHeaderInfoBottomTv02;
    //可疑用户状态 1为可疑用户。
    private String is_likeliar;
    private TextView mPersonInfoRealnamePhoto;
    //认证照（1：vip可见，0：不可见）
    private String realpicstate;
    private String card_face;
    private String status;
    private String dynamicstatus;
    private String infostatus;
    private String chatstatus;
    private String devicestatus;
    private  String joinGroupStatus; //是否允许加群
    private View view;
    private View blackView;
    private TextView mPersonInfoBlackTopTv;
    private PercentLinearLayout mPersonInfoBlackLlBeautyCount;
    private TextView mPersonInfoBlackBeautyCount;
    private PercentLinearLayout mPersonInfoBlackLlRichCount;
    private TextView mPersonInfoBlackRichCount;
    private TextView mPersonInfoBlackRole;
    private TextView mPersonInfoBlackSex;
    private ImageView mPersonInfoBlackOnline;
    private ImageView mPersonInfoBlackRenzheng;
    private TextView mPersonInfoBlackName;
    private ImageView mPersonInfoBlackVip;
    private ImageView mPersonInfoBlackIcon;
    private ImageView mPersonInfoBlackIvBg;
    private ImageView mPersonInfoBlackHongniang;
    private boolean firstLoad = true;
    private TextView mPersonInfoBlackReport;
    private TextView mPersonInfoBlackBlack;
    private TextView mPersonInfoPhotoLine2;
    private int who;
    private String photo_rule;
    private String dynamic_rule;
    private String comment_rule;
    private TextView mPersonInfo_header_text_more;
    private TextView mPersonInfo_header_bottom_more02;
    private TextView laheineirong;
    private TextView mPersonInfo_top_guanlibeizhu;
    private TextView mPersonInfo_beizhu;
    private PercentLinearLayout layout_beizhu;
    private String marknamea = "";
    private String lmarkname = "";
    private String haoyou = "";
    String vip = "";
    String svip = "";
    String myadmin = "";
    private String admin_mark;
    private ArrayList<String> sypic;
    private TextView xiangxibeizhu;
    private LinearLayout ll_xiangxi;
    private ImageView mPersonInfosfzrenzheng;
    private String nickname_rule;
    private String qiaoqiaoattention = "悄悄关注(svip)";
    private String fenzutention = "设置分组(svip)";
    private String friend_quiet = "0";
    private String bkvip;
    private String blvip;
    private LinearLayout ll_fenzu;
    private TextView tv_fenzu;
    private TextView tvBanToDynamic;//禁动
    private TextView tvBannedToPost;//禁言
    private TextView tvBanToInformation;//禁资
    private TextView tvBan; //封号
    private TextView tvComplain;//投诉
    private TextView tvBeComplained;//被投诉
    private TextView tvBannedEquipment;//封设
    private TextView tvAccount;//封设
    private PercentLinearLayout llGetOutOfLine;
    /**
     * 整个Activity视图的根视图
     */
    View decorView;

    /**
     * 手指按下时的坐标
     */
    float downX, downY;

    /**
     * 手机屏幕的宽度和高度
     */
    float screenWidth, screenHeight;
    private int position;
    private UserInfoData.DataBean datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peson_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
//        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
//        position = intent.getIntExtra("position",-1);
        gotoNewUserInfoActivity();
        //RongCloudEvent.getUserInfoById(uid);
       // isSVIP();
//        loginAdmin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
//        setData();
//        setBlackView();
//        setListener();
//        isFriend();
//        // 获得decorView
//        decorView = getWindow().getDecorView();
//        // 获得手机屏幕的宽度和高度，单位像素
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        screenWidth = metrics.widthPixels;
//        screenHeight = metrics.heightPixels;

    }

    void gotoNewUserInfoActivity() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
        finish();
    }

    private void setListener() {
        mPersonInfoListview.setOnTouchListener(this);
        mPersonInfoListview.setFocusable(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gettopcardyn(TopcardyesBean topcardyesBean) {
        if (dynamicAdapter != null)
        if (dynamicAdapter != null)
            dynamicAdapter.getTopcardyn(topcardyesBean);
    }

    private void setData() {
        view = View.inflate(this, R.layout.item_person_info_header, null);
        ImageView ckls = view.findViewById(R.id.ck_ls);
        imgGridView = view.findViewById(R.id.my_gridlist);
        ckls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.equals(MyApp.uid) || "1".equals(myadmin)) {
                    Intent intent = new Intent(PesonInfoActivity.this, history_nameActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                } else {
                    if (nickname_rule.equals("1")) {
                        ToastUtil.show(getApplicationContext(), "Ta的历史昵称仅自己可见");
                    } else {
                        toHhistoryNameActivity();
                    }

                }

            }
        });
        ll_fenzu = view.findViewById(R.id.ll_fenzu);
        tv_fenzu = view.findViewById(R.id.tv_fenzu);
        getfriendgrouplist();
        ll_fenzu.setOnClickListener(this);
        mPersonInfo_beizhu = view.findViewById(R.id.mPersonInfo_beizhu);
        xiangxibeizhu = view.findViewById(R.id.xiangxi_beizhu);
        ll_xiangxi = view.findViewById(R.id.ll_xiangxi);
        layout_beizhu = view.findViewById(R.id.layout_beizhu);
        mPersonInfoTopTv = (TextView) view.findViewById(R.id.mPersonInfo_top_tv);
        mPersonInfo_top_guanlibeizhu = view.findViewById(R.id.mPersonInfo_top_guanlibeizhu);
        //Android4.0以上默认是淡绿色，低版本的是黄色。解决方法就是通过重新设置文字背景为透明色
        mPersonInfoTopTv.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spanableInfo = new SpannableString("可疑用户！【自拍认证】后此提醒消失");
//        SpannableString spanableInfo = new SpannableString("可疑用户！【绑定手机】或【自拍认证】后此提醒消失");
//        spanableInfo.setSpan(new Clickable(bindPhoneListener), 5, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new Clickable(realNameListener), 5, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPersonInfoTopTv.setText(spanableInfo);
        mPersonInfoTopTv.setMovementMethod(LinkMovementMethod.getInstance());
        mPersonInfoMediaIv = (ImageView) view.findViewById(R.id.mPersonInfo_mediaIv);
        mPersonInfoMediaTime = (TextView) view.findViewById(R.id.mPersonInfo_mediaTime);
        mPersonInfoIvBg = (ImageView) view.findViewById(R.id.mPersonInfo_ivBg);
        mPersonInfoIcon = (ImageView) view.findViewById(R.id.mPersonInfo_icon);
        mPersonInfoVip = (ImageView) view.findViewById(R.id.mPersonInfo_vip);
        mPersonInfoHongniang = (ImageView) view.findViewById(R.id.mPersonInfo_hongniang);
        mPersonInfoName = (TextView) view.findViewById(R.id.mPersonInfo_name);
        mPersonInfoRenzheng = (ImageView) view.findViewById(R.id.mPersonInfo_renzheng);
        mPersonInfosfzrenzheng = view.findViewById(R.id.mPersonInfo_sfzrenzheng);
        mPersonInfoOnline = (ImageView) view.findViewById(R.id.mPersonInfo_online);
        mPersonInfoSex = (TextView) view.findViewById(R.id.mPersonInfo_Sex);
        mPersonInfoRole = (TextView) view.findViewById(R.id.mPersonInfo_role);
        mPersonInfoRichCount = (TextView) view.findViewById(R.id.mPersonInfo_richCount);
        mPersonInfoLlRichCount = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_ll_richCount);
        mPersonInfoBeautyCount = (TextView) view.findViewById(R.id.mPersonInfo_beautyCount);
        mPersonInfoLlBeautyCount = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_ll_beautyCount);
        mPersonInfoYuyin = (ImageView) view.findViewById(R.id.mPersonInfo_yuyin);
        mPersonInfoLuyin = (ImageView) view.findViewById(R.id.mPersonInfo_luyin);
        mPersonInfoTime = (TextView) view.findViewById(R.id.mPersonInfo_time);
        mPersonInfoDistance = (TextView) view.findViewById(R.id.mPersonInfo_distance);
        mPersonInfoCity = (TextView) view.findViewById(R.id.mPersonInfo_city);
        mPersonInfoAlpha03 = (TextView) view.findViewById(R.id.mPersonInfo_alpha03);
        mPersonInfoRegistTime = (TextView) view.findViewById(R.id.mPersonInfo_registTime);
        mPersonInfoDonate = (TextView) view.findViewById(R.id.mPersonInfo_donate);
        mPersonInfoRealnamePhoto = (TextView) view.findViewById(R.id.mPersonInfo_see_realnamePhoto);
        mPersonInfoGuanzhuCount = (TextView) view.findViewById(R.id.mPersonInfo_guanzhuCount);
        mPersonInfoLlGz = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_ll_gz);
        mPersonInfoFansCount = (TextView) view.findViewById(R.id.mPersonInfo_fansCount);
        mPersonInfoLlFs = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_ll_fs);
        mPersonInfoGroupCount = (TextView) view.findViewById(R.id.mPersonInfo_groupCount);
        mPersonInfoLlQz = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_ll_qz);
        mPersonInfoPresentCount = (TextView) view.findViewById(R.id.mPersonInfo_presentCount);
        mPersonInfoPresentGridview = (MyGridview) view.findViewById(R.id.mPersonInfo_present_gridview);
        mPersonInfoPresentLl = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_present_ll);
        mPersonInfoRecyclerview = (RecyclerView) view.findViewById(R.id.mPersonInfo_recyclerview);
        mPersonInfoPhotoLl = (RelativeLayout) view.findViewById(R.id.mPersonInfo_photo_ll);
        mPersonInfoAddIv = (ImageView) view.findViewById(R.id.mPersonInfo_addIv);
        mPersonInfoPhotoLine = (TextView) view.findViewById(R.id.mPersonInfo_photo_line);
        mPersonInfoPhotoLine2 = (TextView) view.findViewById(R.id.mPersonInfo_photo_line2);
        mPersonInfoIntroduce = (TextView) view.findViewById(R.id.mPersonInfo_introduce);
        mPersonInfoEditSign = (TextView) view.findViewById(R.id.mPersonInfo_editSign);
        mPersonInfoIntroduceLl = (PercentRelativeLayout) view.findViewById(R.id.mPersonInfo_introduce_ll);
        mPersonInfoIntroduceAdminLl = (AutoLinearLayout) view.findViewById(R.id.mPersonInfo_introduce_admin_ll);
        mPersonInfoHeight = (TextView) view.findViewById(R.id.mPersonInfo_height);
        mPersonInfoJiechu = (TextView) view.findViewById(R.id.mPersonInfo_jiechu);
        mPersonInfoWeight = (TextView) view.findViewById(R.id.mPersonInfo_weight);
        mPersonInfoShijian = (TextView) view.findViewById(R.id.mPersonInfo_shijian);
        mPersonInfoStar = (TextView) view.findViewById(R.id.mPersonInfo_star);
        mPersonInfoLevel = (TextView) view.findViewById(R.id.mPersonInfo_level);
        mPersonInfoCulture = (TextView) view.findViewById(R.id.mPersonInfo_culture);
        mPersonInfoQuxiang = (TextView) view.findViewById(R.id.mPersonInfo_quxiang);
        mPersonInfoMoney = (TextView) view.findViewById(R.id.mPersonInfo_money);
        mPersonInfoWant = (TextView) view.findViewById(R.id.mPersonInfo_want);
        llGetOutOfLine = view.findViewById(R.id.ll_get_out_of_line);
        tvBanToDynamic = view.findViewById(R.id.tv_ban_to_dynamic);
        tvBannedToPost = view.findViewById(R.id.tv_banned_to_post);
        tvBanToInformation = view.findViewById(R.id.tv_ban_to_information);
        tvBan = view.findViewById(R.id.tv_ban);
        tvBannedEquipment = view.findViewById(R.id.tv_banned_equipment);
        tvComplain = view.findViewById(R.id.tv_complain);
        tvBeComplained = view.findViewById(R.id.tv_be_complained);
        tvAccount = view.findViewById(R.id.tv_account);

        mPersonHeaderInfoBottomLL = (AutoRelativeLayout) view.findViewById(R.id.mPersonInfo_header_bottom_ll);
        mPersonHeaderInfoBottomTv = (TextView) view.findViewById(R.id.mPersonInfo_header_bottom_tv);
        mPersonInfo_header_text_more = view.findViewById(R.id.mPersonInfo_header_text_more);//更多  文字
        mPersonHeaderInfoBottomLL02 = (AutoRelativeLayout) view.findViewById(R.id.mPersonInfo_header_bottom_ll02);
        mPersonHeaderInfoBottomTv02 = (TextView) view.findViewById(R.id.mPersonInfo_header_bottom_tv02);
        mPersonInfo_header_bottom_more02 = view.findViewById(R.id.mPersonInfo_header_bottom_more02);////更多  文字
        mPersonInfoIcon.setOnClickListener(this);
        mPersonInfoVip.setOnClickListener(this);
        mPersonInfoYuyin.setOnClickListener(this);
        mPersonInfoRenzheng.setOnClickListener(this);
        mPersonInfosfzrenzheng.setOnClickListener(this);
        mPersonInfoLuyin.setOnClickListener(this);
        mPersonInfoTime.setOnClickListener(this);
        mPersonInfoDonate.setOnClickListener(this);
        mPersonInfoRealnamePhoto.setOnClickListener(this);
        mPersonInfoLlGz.setOnClickListener(this);
        mPersonInfoLlFs.setOnClickListener(this);
        mPersonInfoLlQz.setOnClickListener(this);
        mPersonInfoPresentLl.setOnClickListener(this);
        mPersonInfoAddIv.setOnClickListener(this);
        mPersonInfoEditSign.setOnClickListener(this);
        mPersonHeaderInfoBottomLL.setOnClickListener(this);
        mPersonHeaderInfoBottomLL02.setOnClickListener(this);
        tvBanToDynamic.setOnClickListener(this);
        tvBannedToPost.setOnClickListener(this);
        tvBanToInformation.setOnClickListener(this);
        tvBan.setOnClickListener(this);
        tvBannedEquipment.setOnClickListener(this);
        tvComplain.setOnClickListener(this);
        tvBeComplained.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
        mPersonInfoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mPersonInfoName.getText().toString());
                ToastUtil.show(getApplicationContext(), "复制成功");
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPersonInfoRecyclerview.setLayoutManager(linearLayoutManager);
        if (uid.equals(MyApp.uid)) {
            mPersonInfoBottomLl.setVisibility(View.GONE);
            mPersonInfoEditSign.setVisibility(View.VISIBLE);
            mPersonInfoLuyin.setVisibility(View.VISIBLE);
            mPersonInfoDonate.setVisibility(View.GONE);
            mPersonInfoEdit.setVisibility(View.VISIBLE);
            who = 0;
        } else {
            if ("1".equals(loginAdmin)) {
                mPersonInfoEdit.setVisibility(View.VISIBLE);
            } else {
                mPersonInfoEdit.setVisibility(View.GONE);
            }
            mPersonInfoSendDynamic.setVisibility(View.GONE);
            mPersonInfoLuyin.setVisibility(View.GONE);
            who = 1;
        }
        mPersonInfoGuanzhu.setEnabled(false);
        mPersonInfoListview.setFocusable(false);
        datatvs = new ArrayList<>();
        datatvs.add(mPersonInfoHeight);
        datatvs.add(mPersonInfoWeight);
        datatvs.add(mPersonInfoStar);
        datatvs.add(mPersonInfoQuxiang);
        datatvs.add(mPersonInfoJiechu);
        datatvs.add(mPersonInfoShijian);
        datatvs.add(mPersonInfoLevel);
        datatvs.add(mPersonInfoWant);
        datatvs.add(mPersonInfoCulture);
        datatvs.add(mPersonInfoMoney);

        //是否调用访问量的接口
        String timeMilliss = String.valueOf(System.currentTimeMillis());
        String millis = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "personTimeMillis", "0");
//        String personUid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "personUid", "0");
        //防止一直多次访问同一个人多次调用访问接口
//        if(!personUid.equals(uid)){
//            //调用增加访问量接口
//
//        }else{
        if ((Long.parseLong(timeMilliss) - Long.parseLong(millis)) > (5 * 1 * 1000)) {
            //调用增加访问量接口
            writeVisitRecord();
        }
//        }
    }

    private void setBlackView() {
        blackView = View.inflate(this, R.layout.item_person_info_black_header, null);
        mPersonInfoBlackTopTv = (TextView) blackView.findViewById(R.id.mPersonInfo_black_top_tv);
        //Android4.0以上默认是淡绿色，低版本的是黄色。解决方法就是通过重新设置文字背景为透明色
        mPersonInfoBlackTopTv.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spanableInfo = new SpannableString("可疑用户！【绑定手机】或【自拍认证】后此提醒消失");
        spanableInfo.setSpan(new Clickable(bindPhoneListener), 5, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new Clickable(realNameListener), 12, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPersonInfoBlackTopTv.setText(spanableInfo);
        mPersonInfoBlackTopTv.setMovementMethod(LinkMovementMethod.getInstance());
        mPersonInfoBlackIvBg = (ImageView) blackView.findViewById(R.id.mPersonInfo_black_ivBg);
        mPersonInfoBlackIcon = (ImageView) blackView.findViewById(R.id.mPersonInfo_black_icon);
        mPersonInfoBlackVip = (ImageView) blackView.findViewById(R.id.mPersonInfo_black_vip);
        mPersonInfoBlackName = (TextView) blackView.findViewById(R.id.mPersonInfo_black_name);
        mPersonInfoBlackRenzheng = (ImageView) blackView.findViewById(R.id.mPersonInfo_black_renzheng);
        mPersonInfoBlackOnline = (ImageView) blackView.findViewById(R.id.mPersonInfo_black_online);
        mPersonInfoBlackSex = (TextView) blackView.findViewById(R.id.mPersonInfo_black_Sex);
        mPersonInfoBlackRole = (TextView) blackView.findViewById(R.id.mPersonInfo_black_role);
        mPersonInfoBlackRichCount = (TextView) blackView.findViewById(R.id.mPersonInfo_black_richCount);
        mPersonInfoBlackLlRichCount = (PercentLinearLayout) blackView.findViewById(R.id.mPersonInfo_black_ll_richCount);
        mPersonInfoBlackBeautyCount = (TextView) blackView.findViewById(R.id.mPersonInfo_black_beautyCount);
        mPersonInfoBlackLlBeautyCount = (PercentLinearLayout) blackView.findViewById(R.id.mPersonInfo_black_ll_beautyCount);
        mPersonInfoBlackHongniang = (ImageView) blackView.findViewById(R.id.mPersonInfo_black_hongniang);
        mPersonInfoBlackReport = (TextView) blackView.findViewById(R.id.mPersonInfo_black_report);
        laheineirong = blackView.findViewById(R.id.laheineirong);
        mPersonInfoBlackBlack = (TextView) blackView.findViewById(R.id.mPersonInfo_black_black);
        mPersonInfoBlackReport.setOnClickListener(this);
        mPersonInfoBlackBlack.setOnClickListener(this);
        ll_xiangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2001".equals(haoyou) || !"0".equals(svip)) {
                    Intent intent1 = new Intent(PesonInfoActivity.this, XiangXiBeiZhuActivity.class);
                    intent1.putExtra("name", lmarkname);
                    intent1.putExtra("fuid", uid);
                    startActivity(intent1);
                } else {
                    //ToastUtil.show(this,"");
                    VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "好友/svip 可设置详细描述");
                }
            }
        });
    }

    private void writeVisitRecord() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.WriteVisitRecord, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
//                Log.i("writeRecord", "onSuccess: " + response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getUserInfo() {//得到主页信息
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetUserInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("PersonInfo", "onSuccess: " + response);

                        try {
                            JSONObject object = new JSONObject(response);
                            UserInfoData data = new Gson().fromJson(response, UserInfoData.class);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    if (firstLoad) {
                                        mPersonInfoListview.addHeaderView(view);
                                    }

                                    datas = data.getData();

                                    nickname_rule = datas.getNickname_rule();
                                    friend_quiet = datas.getFriend_quiet();
                                    if ("1".equals(friend_quiet)) {
                                        qiaoqiaoattention = "取消悄悄关注";
                                    } else {
                                        qiaoqiaoattention = "悄悄关注(svip)";
                                    }

                                    if ("1".equals(datas.getRealids())) {
                                        mPersonInfosfzrenzheng.setVisibility(View.GONE);
                                        mPersonInfosfzrenzheng.setImageResource(R.mipmap.sfzrz_lan);
                                    } else {
                                        if (uid.equals(MyApp.uid)) {
                                            mPersonInfosfzrenzheng.setVisibility(View.GONE);
                                            mPersonInfosfzrenzheng.setImageResource(R.mipmap.sfzrz_hui);
                                        } else {
                                            mPersonInfosfzrenzheng.setVisibility(View.GONE);
                                        }
                                    }
                                    String char_rule = datas.getChar_rule();
                                    if (char_rule.equals("1")) {
                                        mPersonInfoChat.setImageResource(R.mipmap.lioatianloudou);
                                    }
                                    lmarkname = datas.getLmarkname();
                                    if (!lmarkname.equals("") && lmarkname != null) {
                                        ll_xiangxi.setVisibility(View.VISIBLE);
                                        xiangxibeizhu.setText(lmarkname + "");
                                    } else {
                                        ll_xiangxi.setVisibility(View.GONE);
                                    }

                                    nickname = datas.getNickname();
                                    if (!"".equals(datas.getMarkname()) && datas.getMarkname() != null) {
                                        marknamea = datas.getMarkname();
                                    }

                                    if (!marknamea.equals("") && marknamea != null) {
                                        layout_beizhu.setVisibility(View.VISIBLE);
                                        mPersonInfo_beizhu.setText("（" + nickname + "）");
                                        mPersonInfoName.setText(datas.getMarkname());
                                    } else {
                                        mPersonInfoName.setText(nickname);
                                        layout_beizhu.setVisibility(View.INVISIBLE);
                                    }


                                    //相册
                                    photo_rule = datas.getPhoto_rule();//相册的权限
                                    //动态
                                    dynamic_rule = datas.getDynamic_rule();//动态的权限
                                    //评论
                                    comment_rule = datas.getComment_rule();//评论的权限
                                    followState = datas.getFollow_state();//关注状态的权限
//                                    jb
                                    String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                                    String svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
                                    mPersonInfo_header_text_more.setText("所有人可见");
                                    mPersonInfo_header_bottom_more02.setText("所有人可见");
                                    if (dynamic_rule.equals("1")) {
                                        mPersonInfo_header_text_more.setText("好友/会员可见");
                                    }
                                    if (comment_rule.equals("1")) {
                                        mPersonInfo_header_bottom_more02.setText("好友/会员可见");
                                    }


                                    if (!followState.equals("3")) {

                                    }

                                    is_likeliar = datas.getIs_likeliar();
                                    if (is_likeliar.equals("1")) {
                                        mPersonInfoTopTv.setVisibility(View.VISIBLE);
                                    } else {
                                        mPersonInfoTopTv.setVisibility(View.GONE);
                                    }
                                    //认证照是否公开
                                    realpicstate = datas.getRealpicstate();
                                    joinGroupStatus=datas.getJoin_group_status();
                                    if (!PesonInfoActivity.this.uid.equals(MyApp.uid)) {
                                        if (datas.getRealname().equals("1")) {
                                            if (realpicstate.equals("0")) {
                                                mPersonInfoRealnamePhoto.setText("认证照未公开");
                                            } else {
                                                mPersonInfoRealnamePhoto.setText("认证照vip可见");
                                            }
                                        } else {
                                            mPersonInfoRealnamePhoto.setVisibility(View.GONE);
                                        }
                                    } else {
                                        mPersonInfoRealnamePhoto.setVisibility(View.GONE);
                                    }
                                    mediatime = datas.getMediaalong();
                                    if (!mediatime.equals("0")) {
                                        mPersonInfoMediaTime.setText(mediatime + "\"");
                                    } else {
                                        mPersonInfoMediaTime.setVisibility(View.GONE);
                                    }
                                    headurl = datas.getHead_pic();
                                    try {
                                        GlideImgManager.glideLoader(PesonInfoActivity.this, headurl, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mPersonInfoIcon, 0);
                                        GlideImgManager.glideLoader(PesonInfoActivity.this, headurl, R.mipmap.default_error, R.mipmap.default_error, mPersonInfoIvBg);
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                    mPersonInfoRegistTime.setText(datas.getReg_time());
                                    timePoorState = datas.getTimePoorState();
                                    logintime = datas.getLast_login_time();

                                    login_time_switch = datas.getLogin_time_switch();
                                    if (login_time_switch.equals("0")) {
                                        if (timePoorState == 0) {
                                            mPersonInfoTime.setText("看Ta登陆时间");
                                        } else {
                                            mPersonInfoTime.setText(logintime);
                                        }
                                    } else {
                                        mPersonInfoTime.setText("隐身");
                                    }

                                    volunteer = datas.getIs_volunteer();
                                    admin = datas.getIs_admin();
                                    bkvip = datas.getBkvip();
                                    blvip = datas.getBlvip();

                                    if (myadmin.equals("1") && !datas.getAdmin_mark().equals("") && datas.getAdmin_mark() != null) {
                                        mPersonInfo_top_guanlibeizhu.setVisibility(View.VISIBLE);
                                        admin_mark = datas.getAdmin_mark();
                                        sypic = datas.getSypic();
                                        mPersonInfo_top_guanlibeizhu.setText(datas.getAdmin_mark() + "");
                                        if(sypic.size()>0) {
                                            BeizhuImgAdapter pictureAdapter = new BeizhuImgAdapter(sypic, PesonInfoActivity.this);
                                            imgGridView.setVisibility(View.VISIBLE);
                                            imgGridView.setAdapter(pictureAdapter);
                                            imgGridView.setNumColumns(5);
                                        }
                                    } else {
                                        mPersonInfo_top_guanlibeizhu.setVisibility(View.GONE);
                                    }


                                    if (admin.equals("1")) {
                                        mPersonInfoVip.setImageResource(R.mipmap.guanfangbiaozhi);
                                    } else {
                                        if ("1".equals(bkvip)) {
                                            mPersonInfoVip.setImageResource(R.mipmap.guanfangbiaozhigui);
                                        } else {
                                            if ("1".equals(blvip)) {
                                                mPersonInfoVip.setImageResource(R.mipmap.guanfangbiaozhilan);
                                            } else {
                                                if (volunteer.equals("1")) {
                                                    mPersonInfoVip.setImageResource(R.mipmap.zhiyuanzhevip);
                                                } else {
                                                    if (datas.getSvipannual().equals("1")) {
                                                        mPersonInfoVip.setImageResource(R.mipmap.svipnian);
                                                    } else {
                                                        if (datas.getSvip().equals("1")) {
                                                            mPersonInfoVip.setImageResource(R.mipmap.svip);
                                                        } else {
                                                            if (datas.getVipannual().equals("1")) {
                                                                mPersonInfoVip.setImageResource(R.mipmap.vipnian);
                                                            } else {
                                                                if (datas.getVip().equals("1")) {
                                                                    mPersonInfoVip.setImageResource(R.mipmap.vip);
                                                                } else {
//                                                    if (uid.equals(MyApp.uid)) {
                                                                    mPersonInfoVip.setImageResource(R.mipmap.gaojisousuohui);
//                                                    } else {
//                                                        mPersonInfoVip.setVisibility(View.GONE);
//                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (datas.getIs_hand().equals("0")) {
                                        mPersonInfoHongniang.setVisibility(View.INVISIBLE);
                                    } else {
                                        mPersonInfoHongniang.setVisibility(View.VISIBLE);
                                    }


                                    blackState = datas.getBlack_state();
                                    if (datas.getRealname().equals("1")) {
                                        mPersonInfoRenzheng.setImageResource(R.mipmap.renzheng);
                                        //获取认证照片
                                        getIdstate();
                                    } else {
                                        if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {
                                            mPersonInfoRenzheng.setImageResource(R.mipmap.renzhenghui);
                                        } else {
                                            mPersonInfoRenzheng.setVisibility(View.GONE);
                                        }
                                    }
                                    age = datas.getAge();
                                    mPersonInfoSex.setText(age);
                                    role = datas.getRole();
                                    if (datas.getSex().equals("1")) {
                                        mPersonInfoSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.nan);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mPersonInfoSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
//                                    datatvs.get(i).setTextColor(Color.parseColor("#40ABE4"));
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_blue_rectangle_bg);
                                        }
                                    } else if (datas.getSex().equals("2")) {
                                        mPersonInfoSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.nv);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mPersonInfoSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
//                                    datatvs.get(i).setTextColor(Color.parseColor("#DC7773"));
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_pink_rectangle_bg);
                                        }
                                    } else {
                                        mPersonInfoSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.san);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mPersonInfoSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
//                                    datatvs.get(i).setTextColor(Color.parseColor("#b73acb"));
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_purple_rectangle_bg);
                                        }
                                    }
                                    if (role.equals("S")) {
                                        mPersonInfoRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                        mPersonInfoRole.setText("斯");
                                    } else if (role.equals("M")) {
                                        mPersonInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        mPersonInfoRole.setText("慕");
                                    } else if (role.equals("SM")) {
                                        mPersonInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        mPersonInfoRole.setText("双");
                                    } else if (role.equals("~")) {
                                        mPersonInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                                        mPersonInfoRole.setText("~");
                                    }
                                    media = datas.getMedia();
                                    if (media.equals("")) {
                                        mPersonInfoYuyin.setImageResource(R.mipmap.yuyintiaohui);
                                    } else {
                                        mPersonInfoYuyin.setImageResource(R.mipmap.yuyintiao);
                                    }
                                    String distance = datas.getDistance();
                                    mPersonInfoDistance.setText(distance);
                                    mProvince = datas.getProvince();
                                    mCity = datas.getCity();

                                    String location_city_switch = datas.getLocation_city_switch();
                                    if ("1".equals(location_city_switch)) {
                                        mPersonInfoCity.setText("隐身");
                                    } else {

                                        if (mCity.equals("")) {
                                            mPersonInfoCity.setVisibility(View.INVISIBLE);
                                        } else {
                                            if (!("").equals(mProvince)) {
                                                if (mProvince.equals(mCity)) {
                                                    if (mProvince.contains("市")) {
                                                        mPersonInfoCity.setText(mProvince.substring(0, mProvince.length() - 1));
                                                    } else {
                                                        mPersonInfoCity.setText(mProvince);
                                                    }
                                                } else {
                                                    if (mProvince.contains("省")) {
                                                        mProvince = mProvince.substring(0, mProvince.length() - 1);
                                                    }
                                                    if (mCity.contains("市")) {
                                                        mCity = mCity.substring(0, mCity.length() - 1);
                                                    }
                                                    mPersonInfoCity.setText(mProvince + " " + mCity);
                                                }
                                            } else {
                                                if (mCity.contains("市")) {
                                                    mPersonInfoCity.setText(mCity.substring(0, mCity.length() - 1));
                                                }
                                            }
                                        }
                                    }

                                    mPersonInfoGuanzhuCount.setText(datas.getFollow_num());
                                    mPersonInfoFansCount.setText(datas.getFans_num());
                                    mPersonInfoGroupCount.setText(datas.getGroup_num());
                                    followState = datas.getFollow_state();
                                    if (followState.equals("1")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.personygz);
                                    } else if (followState.equals("2")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.persongz);
                                    } else if (followState.equals("3")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.personhwhy);
                                    } else if (followState.equals("4")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.personbgz);
                                    }
                                    personIntro = datas.getIntroduce();
                                    if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {
                                        if (personIntro.equals("")) {
                                            mPersonInfoEditSign.setVisibility(View.VISIBLE);
                                            mPersonInfoIntroduce.setText("");
                                        } else {
                                            mPersonInfoIntroduce.setText(personIntro);
                                            mPersonInfoEditSign.setVisibility(View.GONE);
                                        }
                                        mPersonInfoSendDynamic.setVisibility(View.VISIBLE);
                                        mPersonInfoSendDynamic.animate().alpha(1).setStartDelay(50).start();
                                    } else {
                                        if (personIntro.equals("")) {
                                            mPersonInfoIntroduceLl.setVisibility(View.GONE);
                                            mPersonInfoPhotoLine.setVisibility(View.GONE);
                                        } else {
//                                    String realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
//                                    String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
//                                    if (realname.equals("0") && vip.equals("0") && (followState.equals("1") || followState.equals("2") || followState.equals("4"))) {
//                                        mPersonInfoIntroduce.setVisibility(View.GONE);
//                                        mPersonInfoIntroduceAdminLl.setVisibility(View.VISIBLE);
//                                        mPersonInfoIntroduceAdminTv.setMovementMethod(LinkMovementMethod.getInstance());
//                                        TextMoreClickUtils clickUtils = new TextMoreClickUtils(PesonInfoActivity.this, uid, handler);
//                                        mPersonInfoIntroduceAdminTv.setText(clickUtils.addClickablePart("互为好友、认证用户、VIP会员", "自我介绍限", "可见"), TextView.BufferType.SPANNABLE);
//                                    } else {
                                            mPersonInfoPhotoLine.setVisibility(View.VISIBLE);
                                            mPersonInfoIntroduce.setText(personIntro);
//                                    }
                                        }
                                        mPersonInfoBottomLl.setVisibility(View.VISIBLE);
                                        mPersonInfoBottomLl.animate().alpha(1).setStartDelay(50).start();
                                    }
                                    tall = datas.getTall();
                                    weight = datas.getWeight();
                                    mPersonInfoHeight.setText("身高:  " + tall);
                                    mPersonInfoWeight.setText("体重:  " + weight);
                                    mPersonInfoStar.setText("星座:  " + datas.getStarchar());
                                    mPersonInfoQuxiang.setText("取向:  " + datas.getSexual());
                                    mPersonInfoJiechu.setText("接触:  " + datas.getAlong());
                                    mPersonInfoShijian.setText("实践:  " + datas.getExperience());
                                    mPersonInfoLevel.setText("程度:  " + datas.getLevel());
                                    mPersonInfoWant.setText("想找:  " + datas.getWant());
                                    mPersonInfoCulture.setText("学历:  " + datas.getCulture());
                                    mPersonInfoMoney.setText("月薪:  " + datas.getMonthly());
//                            mPersonInfoDayFk.setText(String.valueOf(datas.getDvisit_num()));
//                            mPersonInfoAllFk.setText(String.valueOf(datas.getVisit_num()));

                                    if (datas.getOnlinestate() == 0) {
                                        mPersonInfoOnline.setVisibility(View.INVISIBLE);
                                    } else {
                                        mPersonInfoOnline.setVisibility(View.VISIBLE);
                                    }
                                    mPersonInfoGuanzhu.setEnabled(true);
                                    photos = datas.getPhoto();
                                    if (datas.getPhoto().size() == 0) {
                                        mPersonInfoPhotoLl.setVisibility(View.GONE);
//                                        mPersonInfoPhotoLine.setVisibility(View.GONE);
                                        if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {
                                            mPersonInfoAddIv.setVisibility(View.VISIBLE);
                                        } else {
                                            mPersonInfoAddIv.setVisibility(View.GONE);
                                        }
                                    } else {
                                        mPersonInfoPhotoLl.setVisibility(View.VISIBLE);
                                        mPersonInfoAddIv.setVisibility(View.GONE);
                                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        RecyclerViewAdapter adapter;
                                        if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {    //首先判断 是不是去自己的页面
                                            adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 0);
                                            mPersonInfoRecyclerview.setAdapter(adapter);
                                            adapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
                                                    intent.putStringArrayListExtra("pics", photos);
                                                    intent.putExtra("position", position);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {//去别人的页面
                                            //判断有没有密码
                                            if (datas.getPhoto_lock().equals("1")) {               //1是以开放无密码    2是未开放
                                                if (photo_rule.equals("0")) {   //判断相册查看权限是否开启  0开启
                                                    adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 0);//清楚
                                                } else {
                                                    //判断相册权限
                                                    vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                                                    svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
                                                    if (vip.equals("1") || svip.equals("1") || followState.equals("3")) {//会员好友也清晰
                                                        adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 0);
                                                    } else {//否则就不清晰
                                                        adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 1);//相册清晰不清晰
                                                    }
                                                }
                                            } else {
                                                adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 1);//模糊
                                            }


                                            mPersonInfoRecyclerview.setAdapter(adapter);
                                            //////////////////////////////////////////////// ///////////////////////////////////////////////////////点击相册子条目
                                            adapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                                                    String svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");

                                                    if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {
                                                        Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
                                                        intent.putStringArrayListExtra("pics", photos);
                                                        intent.putExtra("position", position);
                                                        startActivity(intent);
                                                    } else {
                                                        if (datas.getPhoto_lock().equals("1")) {       //相册有密码状态   1是无密码
                                                            //判断相册的权限
                                                            if (photo_rule.equals("0")) {
                                                                Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
                                                                intent.putStringArrayListExtra("pics", photos);
                                                                intent.putExtra("position", position);
                                                                startActivity(intent);
                                                            } else {
                                                                if (vip.equals("1") || svip.equals("1") || followState.equals("3")) {
                                                                    Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
                                                                    intent.putStringArrayListExtra("pics", photos);
                                                                    intent.putExtra("position", position);
                                                                    startActivity(intent);
                                                                } else {
                                                                    seePhotoVip("TA的相册限互为好友、VIP会员可见~");
                                                                }
                                                            }
                                                        } else {
                                                            showInputPwdDialog();//相册有密码
                                                        }
                                                    }
                                                }
                                            });
//                                            adapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {
//                                                @Override
//                                                public void onItemClick(View view, int position) {

//                                                    if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {
//                                                        Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
//                                                        intent.putStringArrayListExtra("pics", photos);
//                                                        intent.putExtra("position", position);
//                                                        startActivity(intent);
//                                                    } else {
//                                                        if (datas.getPhoto_lock().equals("1")) {//相册有密码状态   1是无密码
//
//                                                            if (photo_rule.equals("0")) {
//                                                                Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
//                                                                intent.putStringArrayListExtra("pics", photos);
//                                                                intent.putExtra("position", position);
//                                                                startActivity(intent);
//                                                            } else {
//                                                                if (vip.equals("1") || svip.equals("1") || followState.equals("3")) {
//                                                                    Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
//                                                                    intent.putStringArrayListExtra("pics", photos);
//                                                                    intent.putExtra("position", position);
//                                                                    startActivity(intent);
//                                                                } else {
//                                                                    seePhotoVip("TA的相册限互为好友、VIP会员可见~");
//                                                                }
//                                                            }
//                                                        } else {
//                                                            showInputPwdDialog();
//                                                        }
//
//
//                                                    }
//
//
////                                                    if (vip.equals("0") && (followState.equals("1") || followState.equals("2") || followState.equals("4"))) {
////                                                        //vip才可以查看相册大图
////                                                        seePhotoVip("TA的相册限互为好友、VIP会员可见~");
////                                                    } else {
////                                                        if (datas.getPhoto_lock().equals("1")) {//设置里的锁
////                                                            if (photo_rule.equals("1")){
////                                                                //隐私里的状态
////                                                                Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
////                                                                intent.putStringArrayListExtra("pics", photos);
////                                                                intent.putExtra("position", position);
////                                                                startActivity(intent);
////                                                            }
////
//////                                                            Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
//////                                                            intent.putStringArrayListExtra("pics", photos);
//////                                                            intent.putExtra("position", position);
//////                                                            startActivity(intent);
////
////                                                        } else {
////                                                            showInputPwdDialog();
////                                                        }
////                                                    }
//                                                }
//                                            });
                                        }
                                    }
                                    if (!datas.getCharm_val().equals("0")) {
                                        mPersonInfoLlBeautyCount.setVisibility(View.VISIBLE);
                                        mPersonInfoBeautyCount.setText(datas.getCharm_val());
                                    } else {
                                        mPersonInfoLlBeautyCount.setVisibility(View.GONE);
                                    }

                                    if (!datas.getWealth_val().equals("0")) {
                                        mPersonInfoLlRichCount.setVisibility(View.VISIBLE);
                                        mPersonInfoRichCount.setText(datas.getWealth_val());
                                    } else {
                                        mPersonInfoLlRichCount.setVisibility(View.GONE);
                                    }

                                    if (datas.getDynamic_num().equals("0")) {
                                        mPersonHeaderInfoBottomLL.setVisibility(View.GONE);
                                    } else {
                                        mPersonHeaderInfoBottomLL.setVisibility(View.VISIBLE);
                                        mPersonHeaderInfoBottomLL.animate().alpha(1).setStartDelay(50).start();
                                        mPersonHeaderInfoBottomTv.setText("发布的动态（" + datas.getDynamic_num() + "）");
                                    }

                                    if (datas.getComment_num().equals("0")) {
                                        mPersonHeaderInfoBottomLL02.setVisibility(View.GONE);
                                    } else {
                                        mPersonHeaderInfoBottomLL02.setVisibility(View.VISIBLE);
                                        mPersonHeaderInfoBottomLL02.animate().alpha(1).setStartDelay(50).start();
                                        mPersonHeaderInfoBottomTv02.setText("参与的评论（" + datas.getComment_num() + "）");
                                    }
                                    //淡入淡出
                                    viewAlpha();
                                    break;
                                case 2001:
                                    if (firstLoad) {
                                        mPersonInfoListview.addHeaderView(blackView);
                                        mPersonInfoBottomLl.setVisibility(View.GONE);
                                        mPersonInfoPresent.setVisibility(View.GONE);
                                        mPersonInfoGuanzhu.setVisibility(View.GONE);
                                        mPersonInfoChat.setVisibility(View.GONE);
                                        mPersonInfoBlackReport.setVisibility(View.VISIBLE);
                                        mPersonInfoBlackBlack.setVisibility(View.VISIBLE);
                                        laheineirong.setText("" + data.getMsg());
                                    }
                                    UserInfoData.DataBean datass = new Gson().fromJson(response, UserInfoData.class).getData();
                                    headurl = datass.getHead_pic();
                                    try {
                                        GlideImgManager.glideLoader(PesonInfoActivity.this, headurl, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mPersonInfoBlackIcon, 0);
                                        GlideImgManager.glideLoader(PesonInfoActivity.this, headurl, R.mipmap.default_error, R.mipmap.default_error, mPersonInfoBlackIvBg);
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                    if (datass.getOnlinestate() == 0) {
                                        mPersonInfoBlackOnline.setVisibility(View.INVISIBLE);
                                    } else {
                                        mPersonInfoBlackOnline.setVisibility(View.VISIBLE);
                                    }
                                    volunteer = datass.getIs_volunteer();
                                    admin = datass.getIs_admin();
                                    bkvip = datass.getBkvip();
                                    blvip = datass.getBlvip();


                                    if ("1".equals(admin)) {
                                        mPersonInfoBlackVip.setImageResource(R.mipmap.guanfangbiaozhi);
                                    } else {
                                        if ("1".equals(bkvip)) {
                                            mPersonInfoBlackVip.setImageResource(R.mipmap.guanfangbiaozhigui);
                                        } else {
                                            if ("1".equals(blvip)) {
                                                mPersonInfoBlackVip.setImageResource(R.mipmap.guanfangbiaozhilan);
                                            } else {
                                                if ("1".equals(volunteer)) {
                                                    mPersonInfoBlackVip.setImageResource(R.mipmap.zhiyuanzhevip);
                                                } else {
                                                    if ("1".equals(datass.getSvipannual())) {
                                                        mPersonInfoBlackVip.setImageResource(R.mipmap.svipnian);
                                                    } else {
                                                        if ("1".equals(datass.getSvip())) {
                                                            mPersonInfoBlackVip.setImageResource(R.mipmap.svip);
                                                        } else {
                                                            if ("1".equals(datass.getVipannual())) {
                                                                mPersonInfoBlackVip.setImageResource(R.mipmap.vipnian);
                                                            } else {
                                                                if ("1".equals(datass.getVip())) {
                                                                    mPersonInfoBlackVip.setImageResource(R.mipmap.vip);
                                                                } else {
                                                                    mPersonInfoBlackVip.setImageResource(R.mipmap.gaojisousuohui);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if ("0".equals(datass.getIs_hand())) {
                                        mPersonInfoBlackHongniang.setVisibility(View.INVISIBLE);
                                    } else {
                                        mPersonInfoBlackHongniang.setVisibility(View.VISIBLE);
                                    }
                                    nickname = datass.getNickname();
                                    mPersonInfoBlackName.setText(nickname);
                                    //是否显示可疑用户。
                                    is_likeliar = datass.getIs_likeliar();
                                    if ("1".equals(is_likeliar)) {
                                        mPersonInfoBlackTopTv.setVisibility(View.VISIBLE);
                                    } else {
                                        mPersonInfoBlackTopTv.setVisibility(View.GONE);
                                    }
                                    blackState = datass.getBlack_state();
                                    if ("0".equals(blackState)) {
                                        mPersonInfoBlackBlack.setText("拉黑");
                                    } else {
                                        mPersonInfoBlackBlack.setText("取消拉黑");
                                    }
                                    if ("1".equals(datass.getRealname())) {
                                        mPersonInfoBlackRenzheng.setImageResource(R.mipmap.renzheng);
                                    } else {
                                        mPersonInfoBlackRenzheng.setImageResource(R.mipmap.renzhenghui);
                                    }
                                    age = datass.getAge();
                                    mPersonInfoBlackSex.setText(age);
                                    role = datass.getRole();
                                    if ("1".equals(datass.getSex())) {
                                        mPersonInfoBlackSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.nan);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mPersonInfoBlackSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
//                                    datatvs.get(i).setTextColor(Color.parseColor("#40ABE4"));
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_blue_rectangle_bg);
                                        }
                                    } else if ("2".equals(datass.getSex())) {
                                        mPersonInfoBlackSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.nv);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mPersonInfoBlackSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
//                                    datatvs.get(i).setTextColor(Color.parseColor("#DC7773"));
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_pink_rectangle_bg);
                                        }
                                    } else {
                                        mPersonInfoBlackSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.san);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mPersonInfoBlackSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
//                                    datatvs.get(i).setTextColor(Color.parseColor("#b73acb"));
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_purple_rectangle_bg);
                                        }
                                    }
                                    if ("S".equals(role)) {
                                        mPersonInfoBlackRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                        mPersonInfoBlackRole.setText("斯");
                                    } else if ("M".equals(role)) {
                                        mPersonInfoBlackRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        mPersonInfoBlackRole.setText("慕");
                                    } else if ("SM".equals(role)) {
                                        mPersonInfoBlackRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        mPersonInfoBlackRole.setText("双");
                                    } else if ("~".equals(role)) {
                                        mPersonInfoBlackRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                                        mPersonInfoBlackRole.setText("~");
                                    }
                                    if (!"0".equals(datass.getCharm_val())) {
                                        mPersonInfoBlackLlBeautyCount.setVisibility(View.VISIBLE);
                                        mPersonInfoBlackBeautyCount.setText(datass.getCharm_val());
                                    } else {
                                        mPersonInfoBlackLlBeautyCount.setVisibility(View.GONE);
                                    }

                                    if (!"0".equals(datass.getWealth_val())) {
                                        mPersonInfoBlackLlRichCount.setVisibility(View.VISIBLE);
                                        mPersonInfoBlackRichCount.setText(datass.getWealth_val());
                                    } else {
                                        mPersonInfoBlackLlRichCount.setVisibility(View.GONE);
                                    }
                                    break;
                            }
                            firstLoad = false;
                            spinKit.setVisibility(View.GONE);
                            dynamicAdapter = new DynamicPersonMsgListviewAdapter(PesonInfoActivity.this, dynamics, 0);
                            mPersonInfoListview.setAdapter(dynamicAdapter);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "personTimeMillis", String.valueOf(System.currentTimeMillis()));
                            SharedPreferencesUtils.setParam(getApplicationContext(), "personUid", uid);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void showInputPwdDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_photo_inpsw_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        etPsw = (EditText) window.findViewById(R.id.item_photo_inpsw_edittext);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_photo_inpsw_confim);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPsw.getText().toString();
                if (TextUtils.equals(password, "")) {
                    ToastUtil.show(getApplicationContext(), "请输入密码");
                } else {
                    //验证相册密码
                    chargePhotoPwd(password);
                }
            }
        });

    }

    private void chargePhotoPwd(String password) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("photo_pwd", password);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChargePhotoPwd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 1);
                                    mPersonInfoRecyclerview.setAdapter(adapter);
                                    adapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Intent intent = new Intent(PesonInfoActivity.this, ZoomActivity.class);
                                            intent.putStringArrayListExtra("pics", photos);
                                            intent.putExtra("position", position);
                                            startActivity(intent);
                                        }
                                    });
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4001:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        alertDialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    @OnClick({R.id.mPersonInfo_edit, R.id.mPersonInfo_return, R.id.mPersonInfo_sandian, R.id.mPersonInfo_present, R.id.mPersonInfo_guanzhu,//
            R.id.mPersonInfo_chat, R.id.mPersonInfo_sendDynamic})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mPersonInfo_return:
                finish();
                break;
            case R.id.mPersonInfo_sandian:
                if (uid.equals(MyApp.uid)) {
                    operaFlag = 1;
                    if (admin.equals("1")) {
                        ownerOperation1();
                    } else {
                        ownerOperation();
                    }
                } else {
                    if (loginAdmin.equals("0")) {
                        operaFlag = 2;
                        if (blackState != null) {
                            if (blackState.equals("0")) {
                                operation();
                            } else {
                                cancelOperation();
                            }
                        }
                    } else {
                        operaFlag = 3;
                        if (blackState != null) {
                            String str01;
                            String str02;
                            String str03 = "违规头像";
                            String str04 = "违规相册";
                            String str05 = "违规昵称";
                            String str06 = "违规签名";
                            String str07 = "★管理员权限";
                            String str08 = "分享";
                            String str09 = "举报";
                            String str10;
                            String str11 = "历史昵称(svip)";
                            String str12 = "设置备注(好友/vip)";
                            String str13 = "管理备注";
                            String str14 = "详细描述(好友/svip)";
                            String str15 = "禁止加群";
                            if (blackState.equals("0")) {
                                str10 = "拉黑";
                                if (status.equals("1")) {
                                   // str01 = "永久封号";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "可疑用户";
                                    } else {
                                        str02 = "取消可疑用户";
                                    }
                                } else {
                                  //  str01 = "启用账号";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "可疑用户";
                                    } else {
                                        str02 = "取消可疑用户";
                                    }
                                }
                            } else {
                                str10 = "取消拉黑";
                                if (status.equals("1")) {
                                  //  str01 = "永久封号";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "可疑用户";
                                    } else {
                                        str02 = "取消可疑用户";
                                    }
                                } else {
                                 //   str01 = "启用账号";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "可疑用户";
                                    } else {
                                        str02 = "取消可疑用户";
                                    }
                                }
                            }
                            //判断是否可以加群
                            if ("0".equals(joinGroupStatus)){
                                str15="禁止加群";
                            }else {
                                str15="启用加群";
                            }
                            adminOperation( str02, str13, str03, str04, str05, str06, str07, str12, str11, str08, str09, str10, str14,str15);
                        }
                    }
                }
                break;
            case R.id.mPersonInfo_edit:
                intent = new Intent(this, EditPersonMsgActivity.class);
                intent.putExtra("otheruid", uid);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_present:
                new DashangDialogNew(this, uid);
//                ToastUtil.show(getApplicationContext(),"该功能暂未开放~");
                break;
            case R.id.mPersonInfo_guanzhu:
                mPersonInfoGuanzhu.setEnabled(false);
                if (followState.equals("1")) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage("您已经关注此人,确认取消关注吗?")
                            .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPersonInfoGuanzhu.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            overfollow();
                        }
                    }).create().show();
                } else if (followState.equals("2")) {
                    follow();
                } else if (followState.equals("3")) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage("您和Ta互为好友,确认取消关注吗?")
                            .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPersonInfoGuanzhu.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            overfollow();
                        }
                    }).create().show();
                } else if (followState.equals("4")) {
                    follow();
                }
                break;
            case R.id.mPersonInfo_chat:  //聊天按钮
                //是否禁言
                String isSpeak = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "nospeak", "1");
                if (isSpeak.equals("0")) {
                    ToastUtil.show(getApplicationContext(), "您因违规被系统禁用聊天功能，如有疑问请与客服联系！");
                } else {
                    isOpenChat();
                }
                break;
            case R.id.mPersonInfo_sendDynamic:
                if (dynamicRetcode != 3001) {
                    if (isSend) {
                        intent = new Intent(this, SendDynamicActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;
        }
    }


    public void viewAlpha() {
        mPersonInfoIcon.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoIvBg.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoName.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoRenzheng.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoOnline.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoSex.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoRole.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoYuyin.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLuyin.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoTime.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoDistance.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoCity.animate().alpha(1).setStartDelay(50).start();
//        mPersonInfoDayFk.animate().alpha(1).setStartDelay(50).start();
//        mPersonInfoAllFk.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoRegistTime.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLlGz.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLlFs.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLlQz.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoIntroduce.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoHeight.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoWeight.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoJiechu.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoShijian.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoStar.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLevel.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoCulture.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoQuxiang.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoMoney.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoWant.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoPhotoLl.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoListview.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoDonate.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoRealnamePhoto.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoVip.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLlBeautyCount.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoLlRichCount.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoAlpha03.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoPresentLl.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoIntroduceLl.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoEditSign.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoMediaTime.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoPhotoLine2.animate().alpha(1).setStartDelay(50).start();
        mPersonInfoPhotoLine.animate().alpha(1).setStartDelay(50).start();
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
                                case 4000:
                                    ToastUtil.show(getApplicationContext(), "用户身份缺失,请退出重试...");
                                    break;
                                case 5000:
                                    isSend = false;
                                    toastStr = json.getString("msg");
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

    private void getPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMyPresent, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyPresentData myPresentData = new Gson().fromJson(response, MyPresentData.class);
                            SpannableStringBuilder builder = new SpannableStringBuilder(myPresentData.getData().getAllnum() + "份");
                            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                            builder.setSpan(purSpan, 0, myPresentData.getData().getAllnum().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mPersonInfoPresentCount.setText(builder);
                            if (myPresentData.getData().getGiftArr().size() != 0) {
                                mPersonInfoPresentLl.setVisibility(View.VISIBLE);
                                mPersonInfoPresentGridview.setClickable(false);// 屏蔽主动获得点击
                                mPersonInfoPresentGridview.setPressed(false);
                                mPersonInfoPresentGridview.setEnabled(false);
                                if (myPresentData.getData().getGiftArr().size() != 0) {
                                    List<MyPresentData.DataBean.GiftArrBean> gifts = myPresentData.getData().getGiftArr();
                                    List<MyPresentData.DataBean.GiftArrBean> giftss = new ArrayList<MyPresentData.DataBean.GiftArrBean>();
                                    //按照魔豆排序大到小。
                                    Collections.sort(gifts, new Comparator<MyPresentData.DataBean.GiftArrBean>() {
                                        @Override
                                        public int compare(MyPresentData.DataBean.GiftArrBean o1, MyPresentData.DataBean.GiftArrBean o2) {
                                            if (Integer.parseInt(moneys[Integer.parseInt(o1.getType()) - 10].substring(0, moneys[Integer.parseInt(o1.getType()) - 10].length() - 2)) > Integer.parseInt(moneys[Integer.parseInt(o2.getType()) - 10].substring(0, moneys[Integer.parseInt(o2.getType()) - 10].length() - 2))) {
                                                return -1;
                                            }
                                            if (Integer.parseInt(moneys[Integer.parseInt(o1.getType()) - 10].substring(0, moneys[Integer.parseInt(o1.getType()) - 10].length() - 2)) == Integer.parseInt(moneys[Integer.parseInt(o2.getType()) - 10].substring(0, moneys[Integer.parseInt(o2.getType()) - 10].length() - 2))) {
                                                return 0;
                                            }
                                            return 1;
                                        }
                                    });
                                    if (gifts.size() > 4) {
                                        for (int i = 0; i < 4; i++) {
                                            giftss.add(myPresentData.getData().getGiftArr().get(i));
                                        }
                                    } else {
                                        giftss = gifts;
                                    }
                                    mPersonInfoPresentGridview.setAdapter(new PersonPresentGridViewAdapter(PesonInfoActivity.this, giftss));
                                } else {
                                    mPersonInfoPresentLl.setVisibility(View.GONE);
                                }
//                                mPersonInfoPresentLl.animate().alpha(1).setStartDelay(150).start();
                            }
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

    private void mediaPlayStart() {
        mPersonInfoYuyin.setEnabled(false);
        mPersonInfoYuyin.setImageResource(R.mipmap.yuyintiaostop);
        mPersonInfoMediaTime.setVisibility(View.GONE);
        mPersonInfoMediaIv.setVisibility(View.VISIBLE);
        mPersonInfoMediaIv.setImageResource(R.drawable.animation_voice);
        AnimationDrawable animationDrawable = (AnimationDrawable) mPersonInfoMediaIv.getDrawable();
        animationDrawable.start();
        ToastUtil.show(getApplicationContext(), "正在缓冲,请稍后...");
        if (!media.equals("")) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        player = new MediaPlayer();
                        player.reset();
                        player.setDataSource(media);
                        player.prepare();
                        player.start();
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPersonInfoYuyin.setImageResource(R.mipmap.yuyintiao);
                                        mPersonInfoMediaIv.setVisibility(View.GONE);
                                        if (!mediatime.equals("0")) {
                                            mPersonInfoMediaTime.setVisibility(View.VISIBLE);
                                            mPersonInfoMediaTime.setText(mediatime + "\"");
                                        } else {
                                            mPersonInfoMediaTime.setVisibility(View.GONE);
                                        }
                                        mPersonInfoYuyin.setEnabled(true);
                                    }
                                });
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            ToastUtil.show(getApplicationContext(), "Ta还没有语音介绍哦");
        }
    }

    private void isOpenChat() {//打开聊天的方法
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("otheruid", uid);
        final IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetOpenChatRestrictAndInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("isopenchat", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            int chatRetcode = object.getInt("retcode");
//                            getOpenChatData getOpenChatData = new Gson().fromJson(response, getOpenChatData.class);
//                            String svip = getOpenChatData.getData().getInfo().getSvip();
                            String svip_my = (String) SharedPreferencesUtils.getParam(PesonInfoActivity.this, "svip", "");
                            //看对方是不是svip
//                            if(svip.equals("1")){//对方是svip就发起会话
//                                            JSONObject obj = object.getJSONObject("data");
//                                            JSONObject obj1 = obj.getJSONObject("info");
//                                            RongOpenConversationUtils.startPrivateChat(PesonInfoActivity.this, uid, nickname,
//                                                    new ChatFlagData(obj.getString("filiation"),
//                                                            obj1.getString("vip"), obj1.getString("vipannual"),
//                                                            obj1.getString("svip"), obj1.getString("svipannual"),
//                                                            obj1.getString("is_volunteer"),
//                                                            obj1.getString("is_admin")));
//                            }else{
                            switch (chatRetcode) {
                                //以使用过邮票可以打开会话
                                case 2000:
                                    //互相关注打开会话
                                case 2001:
                                    String liaotiannicheng = "";
                                    liaotiannicheng = marknamea;
                                    if ("".equals(liaotiannicheng) || null == liaotiannicheng) {
                                        liaotiannicheng = nickname;
                                    }
                                    JSONObject obj = object.getJSONObject("data");
                                    JSONObject obj1 = obj.getJSONObject("info");
//                                    RongOpenConversationUtils.startPrivateChat(PesonInfoActivity.this, uid, liaotiannicheng,
//                                            new ChatFlagData(obj.getString("filiation"),
//                                                    obj1.getString("vip"), obj1.getString("vipannual"),
//                                                    obj1.getString("svip"), obj1.getString("svipannual"),
//                                                    obj1.getString("is_volunteer"),
//                                                    obj1.getString("is_admin"), obj1.getString("bkvip"), obj1.getString("blvip")));
                                    toChatActivity(response);
                                    break;
                                case 3001://邮票弹窗
                                    DialogStampData data = new Gson().fromJson(response, DialogStampData.class);
                                    DialogStampData.DataBean datas = data.getData();
                                    Log.d("TAG", "" + uid + "--------------------" + nickname + "--------------------" + datas.getWallet_stamp() + "--------------------" + datas.getBasicstampX() + "--------------------" + datas.getBasicstampY() + "--------------------" + datas.getBasicstampZ() + "------" + datas.getSex());
                                    new StampDialogNew(PesonInfoActivity.this, uid, nickname, datas.getWallet_stamp(), datas.getBasicstampX() + "", datas.getBasicstampY() + "", datas.getBasicstampZ() + "", datas.getSex());

//                                    new StampDialogNew(PesonInfoActivity.this, uid, nickname, "",  "",  "", "","" );
                                    break;
                                case 4005:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                            }
//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("TAG", throwable.toString());
            }

        });
    }

    //查看消息设置
    private void getSecreSit(String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetVipSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            VipSecretSitData vipSecretSitData = new Gson().fromJson(response, VipSecretSitData.class);
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    String char_rule = vipSecretSitData.getData().getChar_rule();
                                    if (char_rule.equals("1")) {  //1是被限制
                                        setMessage_flag = "1";
                                    } else {
                                        setMessage_flag = "0";
                                    }
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

    private void seePhotoVip(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_open_friend_vip_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView message = (TextView) window.findViewById(R.id.item_open_vip_message);
        TextView follow = (TextView) window.findViewById(R.id.item_open_vip_follow);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_vip_buy);
        message.setText(s);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headpic = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", "");
                Intent intent = new Intent(PesonInfoActivity.this, VipCenterActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("headpic", headpic);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

    }

    private void seeGroupVipAndRealname() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_open_friend_vip_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView message = (TextView) window.findViewById(R.id.item_open_vip_message);
        TextView follow = (TextView) window.findViewById(R.id.item_open_vip_follow);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_vip_buy);
        follow.setText("免费认证");
        message.setText("TA的群组限互为好友、认证用户、VIP会员可见~");
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesonInfoActivity.this, PhotoRzActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headpic = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", "");
                Intent intent = new Intent(PesonInfoActivity.this, VipCenterActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("headpic", headpic);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

    }

    private void seeLoginTime() {
        String isvip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "");
        if (login_time_switch.equals("0")) {
            if (timePoorState == 0) {
                if (isvip.equals("1")) {
                    mPersonInfoTime.setText(logintime);
                } else {
                    new VipDialog(this, "会员才能查看Ta的登录时间哦~");
                }
            }
        }
    }


    private void ownerOperation() {
        new AlertView(null, null, "取消", null,
                new String[]{"分享", "历史昵称(svip)", "详细描述(好友/svip)"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void ownerOperation1() {
        new AlertView(null, null, "取消", null,
                new String[]{"分享", "历史昵称(svip)", "详细描述(好友/svip)", "管理备注"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void operation() {
        new AlertView(null, null, "取消", null,
                new String[]{"分享", qiaoqiaoattention, fenzutention, "历史昵称(svip)", "设置备注(好友/vip)", "详细描述(好友/svip)", "举报", "拉黑"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void cancelOperation() {
        new AlertView(null, null, "取消", null,
                new String[]{"分享", qiaoqiaoattention, fenzutention, "历史昵称(svip)", "设置备注(好友/vip)", "详细描述(好友/svip)", "举报", "取消拉黑"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void adminOperation( String str02, String str13, String str03, String str04, String str05, String str06, String str07, String str12, String str11, String str08, String str09, String str10, String str14,String str15) {
        new AlertView(null, null, "取消", null,
                new String[]{ str02, str03, str04, str05, str06, str15,str13, str07, qiaoqiaoattention, fenzutention, str11, str12, str14, str08, str09, str10},
                this, AlertView.Style.ActionSheet, this).show();
    }


    private void adminDeleteOperation(String str01, String str02, String str03, String str04, String str05) {
        new AlertView(null, null, "取消", null,
                new String[]{str01, str02, str03, str04, str05},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void adminDaysOperation() {
        new AlertView(null, null, "取消", null,
                new String[]{"1天", "3天", "1周", "2周", "1月", "永久"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onItemClick(Object o, int position, String data) {
        String method;
        String days = "";
        switch (operaFlag) {
            case 1:
                switch (position) {
                    case 0:
                        showShareWay();
                        break;
                    case 1:
                        if (uid == MyApp.uid) {
                            Intent intent = new Intent(PesonInfoActivity.this, history_nameActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        } else {
                            toHhistoryNameActivity();
                        }
                        break;
                    case 2:
                        //详细描述
                        Intent intent1 = new Intent(this, XiangXiBeiZhuActivity.class);
                        intent1.putExtra("name", lmarkname);
                        intent1.putExtra("fuid", uid);
                        startActivity(intent1);
                        break;
                    case 3:
                        //管理备注
                        Intent intent2 = new Intent(this, EditGuanliBeizhuActivity.class);
                        intent2.putExtra("fuid", uid);
                        intent2.putExtra("name", admin_mark);
                        intent2.putExtra("sypic", sypic);
                        startActivity(intent2);
                        break;
                }
                break;
            case 2:
                switch (position) {
                    case 0:
                        showShareWay();
                        break;
                    case 1:
                        //悄悄关注
                        setqiaoqiaoattention();

                        break;
                    case 2:
                        //设置分组
                        setfenzutention();

                        break;
                    case 3:
                        if (uid == MyApp.uid) {
                            Intent intent = new Intent(PesonInfoActivity.this, history_nameActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        } else {
                            toHhistoryNameActivity();
                        }
                        break;
                    case 4:
                        if (haoyou.equals("2001") || !vip.equals("0")) {
                            Intent intent1 = new Intent(this, EditBeizhuActivity.class);
                            intent1.putExtra("name", marknamea);
                            intent1.putExtra("fuid", uid);
                            startActivity(intent1);
                        } else {
                            //ToastUtil.show(this,"");
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "好友/vip 可设置备注");
                        }
                        break;
                    case 5:
                        if (haoyou.equals("2001") || !svip.equals("0")) {
                            Intent intent1 = new Intent(this, XiangXiBeiZhuActivity.class);
                            intent1.putExtra("name", lmarkname);
                            intent1.putExtra("fuid", uid);
                            startActivity(intent1);
                        } else {
                            //ToastUtil.show(this,"");
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "好友/svip 可设置详细描述");
                        }
                        break;
                    case 6:
                        Intent intent = new Intent(this, ReportActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        break;
                    case 7:
                        if (blackState.equals("0")) {
                            //拉黑
                            if (admin.equals("1")) {
                                ToastUtil.show(this, "黑V无法被拉黑");
                                return;
                            } else {
                                setOneToBlacklist();
                            }
                        } else {
                            cancelBlackState();
                        }
                        break;
                }
                break;
            case 3:
                switch (position) {
                    case 0:
                        if (is_likeliar.equals("0")) {
                            //设置可疑用户
                            setOneLiker();
                        } else {
                            //取消可疑用户
                            removeOneLiker();
                        }
                        break;
                    case 1:
                        //违规头像
                        delIllegallyUserInfo("1");
                        break;

                    case 2:
                        //违规相册
                        delIllegallyUserInfo("4");
                        break;
                    case 3:
                        //违规昵称
                        delIllegallyUserInfo("2");
                        break;
                    case 4:
                        //违规签名
                        delIllegallyUserInfo("3");
                        break;
                    case 5:
                        //禁止加群
                        if ("0".equals(joinGroupStatus)){
                            joinTheGroupOperation("5");
                        } else {
                            //解封
                            recoverAllUserStatus("5");
                        }
                        break;
                    case 6:
                        //管理备注
                        Intent intent1 = new Intent(this, EditGuanliBeizhuActivity.class);
                        intent1.putExtra("fuid", uid);
                        intent1.putExtra("name", admin_mark);
                        intent1.putExtra("sypic", sypic);
                        startActivity(intent1);
                        break;
                    case 7:
                        //管理员权限
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String str01;
                                String str02;
                                String str03;
                                String str04;
                                String str05;
                                operaFlag = 4;
                                if (status.equals("1")) {
                                    str01 = "封禁账号";
                                } else {
                                    str01 = "启用账号";
                                }
                                if (dynamicstatus.equals("1")) {
                                    str02 = "封禁动态";
                                } else {
                                    str02 = "启用动态";
                                }
                                if (chatstatus.equals("1")) {
                                    str03 = "封禁消息";
                                } else {
                                    str03 = "启用消息";
                                }
                                if (infostatus.equals("1")) {
                                    str04 = "封禁资料";
                                } else {
                                    str04 = "启用资料";
                                }
                                if (devicestatus.equals("1")) {
                                    str05 = "封禁设备";
                                } else {
                                    str05 = "启用设备";
                                }
                                adminDeleteOperation(str01, str02, str03, str04, str05);
                            }
                        }, 500);

                        break;
                    case 8:
                        //悄悄关注
                        setqiaoqiaoattention();

                        break;
                    case 9:
                        //设置分组
                        setfenzutention();
                        break;
                    case 10:
                        //历史昵称
                        if (uid.equals(MyApp.uid)) {
                            Intent intent = new Intent(PesonInfoActivity.this, history_nameActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        } else {
                            toHhistoryNameActivity();
                        }

                        break;
                    case 11:
                        //设置备注
                        if (haoyou.equals("2001") || !vip.equals("0")) {
                            Intent intent2 = new Intent(this, EditBeizhuActivity.class);
                            intent2.putExtra("name", marknamea);
                            intent2.putExtra("fuid", uid);
                            startActivity(intent2);
                        } else {
                            //ToastUtil.show(this,"");
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "好友/vip 可设置备注");
                        }
                        break;
                    case 12:
                        //详细描述
                        if (haoyou.equals("2001") || !svip.equals("0")) {
                            Intent intent3 = new Intent(this, XiangXiBeiZhuActivity.class);
                            intent3.putExtra("name", lmarkname);
                            intent3.putExtra("fuid", uid);
                            startActivity(intent3);
                        } else {
                            //ToastUtil.show(this,"");
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "好友/svip 可设置详细描述");
                        }
                        break;
                    case 13:
                        //分享
                        showShareWay();
                        break;
                    case 14:
                        //举报
                        Intent intent = new Intent(this, ReportActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        break;
                    case 15:

                        if (blackState.equals("0")) {
                            //拉黑
                            if (admin.equals("1")) {
                                ToastUtil.show(this, "黑V无法被拉黑");
                                return;
                            } else {
                                setOneToBlacklist();
                            }
                        } else {
                            cancelBlackState();
                        }
                        break;

                }

                break;
            case 4:
                switch (position) {
                    case 0:
                        if (status.equals("1")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    operaFlag = 5;
                                    adminDaysOperation();
                                }
                            }, 500);
                        } else {
                            //解封
                            recoverAllUserStatus("1");
                        }
                        break;
                    case 1:
                        if (dynamicstatus.equals("1")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    operaFlag = 6;
                                    adminDaysOperation();
                                }
                            }, 500);
                        } else {
                            //解封动态
                            recoverAllUserStatus("2");
                        }
                        break;
                    case 2:
                        if (chatstatus.equals("1")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    operaFlag = 7;
                                    adminDaysOperation();
                                }
                            }, 500);
                        } else {
                            //解封消息
                            recoverAllUserStatus("4");
                        }
                        break;
                    case 3:
                        if (infostatus.equals("1")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    operaFlag = 8;
                                    adminDaysOperation();
                                }
                            }, 500);
                        } else {
                            //解封资料
                            recoverAllUserStatus("3");
                        }
                        break;
                    case 4:
                        if (devicestatus.equals("1")) {
                            //封禁设备
//                            changeDeviceStatus("forbid");
                            Intent intent = new Intent(this, SendReasonActivity.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("method", "forbid");
                            startActivity(intent);
                        } else {
                            //解封设备
                            changeDeviceStatus("resume");
                        }

                        break;

                }

                break;
            case 5:
                method = "1";
                switch (position) {
                    case 0:
                        days = "1";
                        break;
                    case 1:
                        days = "3";
                        break;
                    case 2:
                        days = "7";
                        break;
                    case 3:
                        days = "14";
                        break;
                    case 4:
                        days = "30";
                        break;
                    case 5:
                        days = "0";
                        break;
                }
                if (position != -1) {
//                    adminBanOperation(method, days);
                    Intent intent = new Intent(this, SendReasonActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("method", method);
                    intent.putExtra("days", days);
                    startActivity(intent);

                }
                break;
            case 6:
                method = "2";
                switch (position) {
                    case 0:
                        days = "1";
                        break;
                    case 1:
                        days = "3";
                        break;
                    case 2:
                        days = "7";
                        break;
                    case 3:
                        days = "14";
                        break;
                    case 4:
                        days = "30";
                        break;
                    case 5:
                        days = "0";
                        break;
                }
                if (position != -1) {
//                    adminBanOperation(method, days);
                    Intent intent = new Intent(this, SendReasonActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("method", method);
                    intent.putExtra("days", days);
                    startActivity(intent);

                }
                break;
            case 7:
                method = "4";
                switch (position) {
                    case 0:
                        days = "1";
                        break;
                    case 1:
                        days = "3";
                        break;
                    case 2:
                        days = "7";
                        break;
                    case 3:
                        days = "14";
                        break;
                    case 4:
                        days = "30";
                        break;
                    case 5:
                        days = "0";
                        break;
                }
                if (position != -1) {
//                    adminBanOperation(method, days);
                    Intent intent = new Intent(this, SendReasonActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("method", method);
                    intent.putExtra("days", days);
                    startActivity(intent);

                }
                break;
            case 8:
                method = "3";
                switch (position) {
                    case 0:
                        days = "1";
                        break;
                    case 1:
                        days = "3";
                        break;
                    case 2:
                        days = "7";
                        break;
                    case 3:
                        days = "14";
                        break;
                    case 4:
                        days = "30";
                        break;
                    case 5:
                        days = "0";
                        break;
                }
                if (position != -1) {
//                    adminBanOperation(method, days);
                    Intent intent = new Intent(this, SendReasonActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("method", method);
                    intent.putExtra("days", days);
                    startActivity(intent);

                }
                break;
        }
    }

    private void toHhistoryNameActivity() {
        if (svip.equals("0")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    BindSvipDialog.bindAlertDialog(PesonInfoActivity.this, "查看历史昵称限svip可用");
                }
            });

        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PesonInfoActivity.this, history_nameActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            });

        }
    }

    private void changeDeviceStatus(String method) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChangeDeviceStatus, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:

                                        getAllUserStatus();
                                        getUserInfo();

                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
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

    //加群
    private void joinTheGroupOperation(final String method) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChangeAllUserStatus, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    joinGroupStatus = "1";
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    getAllUserStatus();
                                    getUserInfo();
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


    private void recoverAllUserStatus(final String method) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.RecoverAllUserStatus, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    if ("5".equals(method)){
                                        joinGroupStatus = "0";
                                    }
                                    getAllUserStatus();
                                    getUserInfo();
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

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    private void setOneLiker() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetOneLiker, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    getAllUserStatus();
                                    getUserInfo();
                                    break;
                                case 4001:
                                case 4002:
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void removeOneLiker() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.RemoveOneLiker, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    getAllUserStatus();
                                    getUserInfo();
                                    break;
                                case 4001:
                                case 4002:
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    //判断是否好友
    private void isFriend() {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid", MyApp.uid);
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetAchievePower, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2001:
                                    haoyou = "2001";
                                    break;
                                case 2002:
                                    haoyou = "2002";
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void delIllegallyUserInfo(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("type", type);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.delIllegallyUserInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    getAllUserStatus();
                                    getUserInfo();
                                    break;
                                case 4001:
                                case 4002:
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void showShareWay() {
        SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic()+HttpUrl.ShareUserDetail + uid, nickname + " 的个人主页，快来圣魔关注Ta~", mProvince + " " + mCity + "\n" + age + "/" + tall + "/" + weight + "/" + role, headurl, 0, 2, uid, nickname, headurl, "");
        sharedPop.showAtLocation(activityPesonInfo, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    private void cancelBlackState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelBlackState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    blackState = "0";
                                    mPersonInfoBlackBlack.setText("拉黑");
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    getAllUserStatus();
                                    getUserInfo();
                                    break;
                                case 4000:
                                case 4001:
                                case 4004:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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

    private void setOneToBlacklist() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetOneToBlacklist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("blacklist", "onSuccess: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    blackState = "1";
                                    mPersonInfoBlackBlack.setText("取消拉黑");
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    isSVIP();
                                    loginAdmin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
                                    getAllUserStatus();
                                    getUserInfo();
                                    setData();
                                    setBlackView();
                                    setListener();
                                    isFriend();
                                    break;
                                case 4002:
                                case 4003:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void follow() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("personmsgfollow", "onSuccess: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    String followFlag = obj.getString("data");
                                    if (followFlag.equals("1")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.personhwhy);
                                        followState = "3";
                                        EventBus.getDefault().post(new FollowEvent(3,position));
                                        visibleIntro();
                                    } else {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.personygz);
                                        followState = "1";
                                        EventBus.getDefault().post(new FollowEvent(1,position));
                                    }
                                    ToastUtil.showLong(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4002:
                                case 8881:
                                case 8882:
                                    BindGuanzhuDialog.bindAlertDialog(PesonInfoActivity.this, obj.getString("msg"));
                                    break;
                                case 4787:
                                    ToastUtil.show(getApplicationContext(), "您已经关注了对方~");
                                    break;
                                case 5000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg") + "");
                                    break;

                            }
                            mPersonInfoGuanzhu.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("guanzhu_failure",throwable.getMessage());
            }
        });
    }

    private void overfollow() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.OverFollow, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    if (obj.getString("data").equals("0")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.persongz);
                                        followState = "2";
                                        EventBus.getDefault().post(new FollowEvent(2,position));
                                    } else if (obj.getString("data").equals("1")) {
                                        mPersonInfoGuanzhu.setImageResource(R.mipmap.personbgz);
                                        followState = "4";
                                        EventBus.getDefault().post(new FollowEvent(4,position));
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                            mPersonInfoGuanzhu.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    private void visibleIntro() {
        if (personIntro.equals("")) {
            mPersonInfoIntroduceLl.setVisibility(View.GONE);
        } else {
//            String realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
//            String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
//            if (realname.equals("0") && vip.equals("0") && (followState.equals("1") || followState.equals("2") || followState.equals("4"))) {
//                mPersonInfoIntroduce.setVisibility(View.GONE);
//                mPersonInfoIntroduceAdminLl.setVisibility(View.VISIBLE);
//                mPersonInfoIntroduceAdminTv.setMovementMethod(LinkMovementMethod.getInstance());
//                TextMoreClickUtils clickUtils = new TextMoreClickUtils(PesonInfoActivity.this, uid, handler);
//                mPersonInfoIntroduceAdminTv.setText(clickUtils.addClickablePart("互为好友、认证用户、VIP会员", "自我介绍限", "可见"), TextView.BufferType.SPANNABLE);
//            } else {
            mPersonInfoIntroduce.setVisibility(View.VISIBLE);
            mPersonInfoIntroduceAdminLl.setVisibility(View.GONE);
            mPersonInfoIntroduce.setText(personIntro);
//            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(FollowEvent event) {
        if (event.getFollowFlag().equals("1")) {
            followState = event.getFollowState();
            mPersonInfoGuanzhu.setImageResource(R.mipmap.personhwhy);
            //是否显示个人签名
//            visibleIntro();
        } else if (event.getFollowFlag().equals("2")) {
            mPersonInfoGuanzhu.setImageResource(R.mipmap.personygz);
            followState = event.getFollowState();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(RecorderEvent event) {
        if (event.getFlag() == 1) {
            getAllUserStatus();
            getUserInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(DynamicUpMediaEditDataEvent event) {
        getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(DynamicRewardEvent event) {
        dynamics.get(event.getPosition()).setRewardnum(event.getRewardcount() + "");
        dynamicAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(OwnerPresentEvent event) {
        getPresent();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (!uid.equals(MyApp.uid)) {
                if (y1 - y2 > 3) {
                    if (mPersonInfoBottomLl.getVisibility() == View.VISIBLE) {
                        mPersonInfoBottomLl.setVisibility(View.GONE);
                        mPersonInfoBottomLl.setAnimation(AnimationUtil.moveToViewBottom());
                    }
                } else if (y2 - y1 > 3) {
                    if (mPersonInfoBottomLl.getVisibility() == View.GONE) {
                        mPersonInfoBottomLl.setVisibility(View.VISIBLE);
                        mPersonInfoBottomLl.setAnimation(AnimationUtil.moveToViewLocation());
                    }
                }
            } else {
                if (y1 - y2 > 3) {
                    if (mPersonInfoSendDynamic.getVisibility() == View.VISIBLE) {
                        mPersonInfoSendDynamic.setVisibility(View.GONE);
                        mPersonInfoSendDynamic.setAnimation(AnimationUtil.moveToViewBottom());
                    }
                } else if (y2 - y1 > 3) {
                    if (mPersonInfoSendDynamic.getVisibility() == View.GONE) {
                        mPersonInfoSendDynamic.setVisibility(View.VISIBLE);
                        mPersonInfoSendDynamic.setAnimation(AnimationUtil.moveToViewLocation());
                    }
                }
            }
//            else if(x1 - x2 > 50) {
//                Toast.makeText(this, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(this, "向右滑", Toast.LENGTH_SHORT).show();
//            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        String svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
        switch (v.getId()) {
            case R.id.mPersonInfo_icon:
                ArrayList<String> ivlist = new ArrayList<>();
                ivlist.add(headurl);
                intent = new Intent(this, ZoomActivity.class);
                intent.putExtra("pics", ivlist);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_yuyin:
                if (!media.equals("")) {
                    //语音播放
                    mediaPlayStart();
                } else {
                    ToastUtil.show(getApplicationContext(), "Ta还没有录制语音哦~");
                }
                break;
            case R.id.mPersonInfo_vip:
                if ("0".equals(volunteer)) {
                    if (admin.equals("1")) {
                        intent = new Intent(this, VipWebActivity.class);
                        intent.putExtra("title", "全职招聘");
                        intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.Recruit);
                    } else {
                        intent = new Intent(this, VipCenterActivity.class);
                        intent.putExtra("headpic", headurl);
                        intent.putExtra("uid", uid);
                    }
                } else {
                    intent = new Intent(this, VipWebActivity.class);
                    intent.putExtra("title", "志愿者招聘");
                    intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.Volunteer);
                }
                startActivity(intent);
                break;
            case R.id.mPersonInfo_renzheng:
                intent = new Intent(this, PhotoRzActivity.class);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_sfzrenzheng:
                intent = new Intent(this, PhotoRzActivity.class);
                intent.putExtra("xuan", 2);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_time:
                //查看登录时间
                seeLoginTime();
                break;
            case R.id.mPersonInfo_addIv:
                intent = new Intent(this, EditPersonMsgActivity.class);
                intent.putExtra("otheruid", uid);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_editSign:
                intent = new Intent(this, EditPersonMsgActivity.class);
                intent.putExtra("otheruid", uid);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_donate:
                intent = new Intent(this, VipCenterActivity.class);
                intent.putExtra("headpic", headurl);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_present_ll:
                if (uid.equals(MyApp.uid)) {
                    intent = new Intent(this, MyPurseActivity.class);
                    intent.putExtra("openPresentPage", 1);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, PresentListActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
                break;
            case R.id.mPersonInfo_luyin:
                intent = new Intent(this, RecorderActivity.class);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_ll_gz:
                intent = new Intent(this, GzFsHyActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("currentIndex", 0);
                intent.putExtra("who", who);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_ll_fs:
                intent = new Intent(this, GzFsHyActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("currentIndex", 1);
                intent.putExtra("who", who);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_ll_qz:
                if (uid.equals(MyApp.uid)) {
                    intent = new Intent(this, GroupSquareActivity.class);
                    intent.putExtra("groupFlag", 1);
                    startActivity(intent);
                } else {
//                    String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
//                    String realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
//                    if (realname.equals("0") && vip.equals("0") && (followState.equals("1") || followState.equals("2") || followState.equals("4"))) {
//                        //vip和认证才可以查看他人群组
//                        seeGroupVipAndRealname();
//                    } else {
                    intent = new Intent(this, OtherGroupActivity.class);
                    intent.putExtra("otherUid", uid);
                    startActivity(intent);
//                    }
                }
                break;
            case R.id.mPersonInfo_header_bottom_ll://点击发布的动态 根据uid判断
                if ((!MyApp.uid.equals(uid)) && (vip.equals("0") && svip.equals("0") && !followState.equals("3")) && dynamic_rule.equals("1")) {   //判断我的uid跟请求的uid是否一致 一致就是我自己
                    seePhotoVip("TA发布的动态限互为好友、VIP会员可见~");
                } else {
                    intent = new Intent(this, PersonInfoDynamicActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("nickname", nickname);
                    startActivity(intent);
                }
                break;
            case R.id.mPersonInfo_header_bottom_ll02://参与的评论
                if ((!MyApp.uid.equals(uid)) && (vip.equals("0") && svip.equals("0") && !followState.equals("3")) && comment_rule.equals("1")) {   //判断我的uid跟请求的uid是否一致 一致就是我自己
                    seePhotoVip("TA参与的评论限互为好友、VIP会员可见~");
                } else {
                    intent = new Intent(this, PersonInfoCommentActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("nickname", nickname);
                    startActivity(intent);
                }
                break;
            case R.id.mPersonInfo_see_realnamePhoto:
                if (myadmin.equals("1")) {
                    ArrayList<String> realnamePhoto = new ArrayList<>();
                    realnamePhoto.add(card_face);
                    intent = new Intent(this, ZoomActivity.class);
                    intent.putExtra("pics", realnamePhoto);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                    return;
                }
                if (realpicstate.equals("1")) {

                    if (vip.equals("1")) {
                        ArrayList<String> realnamePhoto = new ArrayList<>();
                        realnamePhoto.add(card_face);
                        intent = new Intent(this, ZoomActivity.class);
                        intent.putExtra("pics", realnamePhoto);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    } else {
                        new VipDialog(this, "认证照限VIP会员可见");
                    }
                } else {
                    new CommonDialog(this, "该用户未公开认证照");
                }
                break;
            case R.id.mPersonInfo_black_report:
                intent = new Intent(this, ReportActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_black_black:
                if (blackState.equals("0")) {
                    //拉黑
                    if (admin.equals("1")) {
                        ToastUtil.show(this, "黑V无法被拉黑");
                        return;
                    } else {
                        setOneToBlacklist();
                    }
                } else {
                    cancelBlackState();
                }
                break;
            case R.id.ll_fenzu:
                setfenzutention();
                break;
            case R.id.tv_ban_to_dynamic: //禁动

                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);
                break;
            case R.id.tv_banned_to_post: //禁言
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","3");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_ban_to_information: //禁资
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","4");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_ban:  //封号
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_banned_equipment:  //封设
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","5");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_complain:  //投诉
                intent  = new Intent(this,ComplaintInformationActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);
                break;
            case R.id.tv_be_complained://被投诉
                intent  = new Intent(this,ComplaintInformationActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);
                break;
            case R.id.tv_account://账号
                intent  = new Intent(this, AccountActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;

        }
    }

    private void getIdstate() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getidstate, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int retcode = obj.getInt("retcode");
                            switch (retcode) {
                                case 2000:
                                    JSONObject object = obj.getJSONObject("data");
                                    card_face = object.getString("card_face");
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

    private void getBindingState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBindingState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    BindingData data = new Gson().fromJson(response, BindingData.class);
                    BindingData.DataBean datas = data.getData();
                    if (!datas.getMobile().equals("")) {
                        realmobile = datas.getMobile();
                        mobile = datas.getMobile().substring(0, 3) + "****" + datas.getMobile().substring(7, datas.getMobile().length());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getAllUserStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetAllUserStatus, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
//                    "status": "1",(账号状态：1表示未封禁，0表示封禁下同)
//                    "dynamicstatus": "1",（动态状态）
//                    "infostatus": "1",（资料状态）
//                    "chatstatus": "1",（聊天状态）
//                    "devicestatus": "1"（设备状态）
                    AllUserStates alluserStates = new Gson().fromJson(response, AllUserStates.class);
                    status = alluserStates.getData().getStatus();
                    dynamicstatus = alluserStates.getData().getDynamicstatus();
                    infostatus = alluserStates.getData().getInfostatus();
                    chatstatus = alluserStates.getData().getChatstatus();
                    devicestatus = alluserStates.getData().getDevicestatus();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


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
            ds.setColor(getResources().getColor(R.color.white));
        }
    }

    private View.OnClickListener bindPhoneListener = new View.OnClickListener() {
        Intent intent;

        @Override
        public void onClick(View v) {
            if (realmobile.equals("")) {
                intent = new Intent(getApplicationContext(), BindingMobileActivity.class);
                intent.putExtra("neworchange", "new");
                startActivity(intent);
            } else {
                intent = new Intent(getApplicationContext(), ChangeBindingMobileActivity.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("realmobile", realmobile);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener realNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), PhotoRzActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        if (uid.equals(MyApp.uid)) {
//            getJudgeDynamic();
//        }
//        if (isFirst) {
//            isFirst = false;
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getPresent();
//                    //获取绑定状态
//                    getBindingState();
//                    //获取用户所有权限封禁状态
//                    getUserInfo();
//                    getAllUserStatus();
//                }
//            }, 200);
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        if (player != null) {
//            player.stop();
//            player.release();
//        }
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            vip = data.getData().getVip();
                            svip = data.getData().getSvip();
                            myadmin = data.getData().getIs_admin();
                            PrintLogUtils.log(svip, "--");

                            if (myadmin.equals("1")) {
                                getviolationoperation();
                            }


                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    public void setqiaoqiaoattention() {

        if (svip.equals("1")) {
            if ("1".equals(friend_quiet)) {
                quxiaoqiaoqiaodguanzhu();
            } else {
                qiaoqiaodguanzhu();
            }
        } else {
            BindSvipDialog.bindAlertDialog(PesonInfoActivity.this, "悄悄关注限svip可用");
        }

    }

    public void qiaoqiaodguanzhu() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.followOneBoxQuiet, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            qiaoqiaoattention = "取消悄悄关注";
                            friend_quiet = "1";
                            ToastUtil.show(getApplicationContext(), object.getString("msg"));
                            getAllUserStatus();
                            getUserInfo();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void quxiaoqiaoqiaodguanzhu() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.overfollowquiet, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            qiaoqiaoattention = "悄悄关注(svip)";
                            friend_quiet = "0";
                            ToastUtil.show(getApplicationContext(), object.getString("msg"));
                            getAllUserStatus();
                            getUserInfo();

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }



    public void getviolationoperation() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.detailMobile, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            Gson gson = new Gson();
                            ViolationBean violationBean = gson.fromJson(response, ViolationBean.class);
                            final ViolationBean.DataBean data = violationBean.getData();
                            //禁动0/禁言0/禁资0/封号0/投诉0/被投0
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    tvBanToDynamic.setText("禁动" + data.getDynamicstatusouttimes());
                                    tvBannedToPost.setText("/禁言" + data.getChatstatusouttimes());
                                    tvBanToInformation.setText( "/禁资" + data.getInfostatusouttimes());
                                    tvBan.setText("/封号" + data.getStatusouttimes());
                                    tvBannedEquipment.setText("/封设" + data.getDevicestatusouttimes());
                                    tvComplain.setText("/投诉" + data.getReporttimes());
                                    tvBeComplained.setText("/被投" + data.getBereportedtimesright());
                                    tvAccount.setText("/账号"+data.getAccountnumbercount());
                                    llGetOutOfLine.setVisibility(View.VISIBLE);
                                }
                            });

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void setfenzutention() {
        if (svip.equals("1")) {
            Intent intent = new Intent(this, FriendsetGroupActivity.class);
            intent.putExtra("fuid", uid);
            startActivity(intent);
        } else {
            BindSvipDialog.bindAlertDialog(PesonInfoActivity.this, "设置分组限svip可用");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getfenzushuaxin(String str) {
        if (str.equals("gerenzhuyefenzushuaxin")) {
            getfriendgrouplist();
        }
    }

    //查询分组
    public void getfriendgrouplist() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.friendgrouplist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    String fenzustr = "";
                                    FriendGroupListBean friendGroupListBean = gson.fromJson(response, FriendGroupListBean.class);
                                    List<FriendGroupListBean.DataBean> data = friendGroupListBean.getData();
                                    for (int i = 0; i < data.size(); i++) {
                                        if (data.get(i).getIs_select().equals("1")) {
                                            fenzustr += data.get(i).getFgname() + "，";
                                        }
                                    }
                                    if (fenzustr.equals("")) {
                                        ll_fenzu.setVisibility(View.GONE);
                                    } else {
                                        fenzustr = fenzustr.substring(0, fenzustr.length() - 1);
                                        tv_fenzu.setText("" + fenzustr);
                                        ll_fenzu.setVisibility(View.VISIBLE);
                                    }

                                }
                            });
                            break;
                        default:
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    int mx,my;
    int lastx,lasty;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取坐标点：
        int x= (int) ev.getX();
        int y= (int) ev.getY();
        if (x>50)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deletx=x-mx;
                int delety=y-my;
                if(Math.abs(deletx)>Math.abs(delety))
                {
                    finish();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
        lastx=x;
        lasty=y;
        mx=x;
        my=y;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();

    }

    void toChatActivity(String userData) {
        if(datas == null) {
            return;
        }
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(datas.getUid());
        chatInfo.setChatName(datas.getNickname());
        Intent intent = new Intent(MyApp.getInstance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.putExtra("userInfo",userData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.instance().startActivity(intent);
    }
}
