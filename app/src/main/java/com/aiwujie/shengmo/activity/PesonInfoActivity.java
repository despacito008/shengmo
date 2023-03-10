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
    //??????????????????Textview??????
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
    //????????????   1.????????? 2. ????????????.3.????????????
    private String followState;
    private String media;
    private String[] moneys = {"2??????", "6??????", "10??????", "38??????", "99??????", "88??????", "123??????", "166??????", "199??????", "520??????", "666??????", "250??????", "777??????", "888??????", "999??????", "1314??????", "1666??????", "1999??????", "666??????", "999??????", "1888??????", "2899??????", "3899??????", "6888??????", "9888??????", "52000??????", "99999??????", "1??????", "3??????", "5??????", "10??????", "8??????", "??????", "??????", "??????", "??????", "??????"};
    private boolean isFirst = true;
    //????????????????????????????????????  0?????????1?????????
    private int timePoorState;
    private AlertDialog alertDialog;
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicPersonMsgListviewAdapter dynamicAdapter;
    private EditText etPsw;
    //??????????????????????????????????????????
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
    //?????????????????? 1??????????????????
    private String is_likeliar;
    private TextView mPersonInfoRealnamePhoto;
    //????????????1???vip?????????0???????????????
    private String realpicstate;
    private String card_face;
    private String status;
    private String dynamicstatus;
    private String infostatus;
    private String chatstatus;
    private String devicestatus;
    private  String joinGroupStatus; //??????????????????
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
    private String qiaoqiaoattention = "????????????(svip)";
    private String fenzutention = "????????????(svip)";
    private String friend_quiet = "0";
    private String bkvip;
    private String blvip;
    private LinearLayout ll_fenzu;
    private TextView tv_fenzu;
    private TextView tvBanToDynamic;//??????
    private TextView tvBannedToPost;//??????
    private TextView tvBanToInformation;//??????
    private TextView tvBan; //??????
    private TextView tvComplain;//??????
    private TextView tvBeComplained;//?????????
    private TextView tvBannedEquipment;//??????
    private TextView tvAccount;//??????
    private PercentLinearLayout llGetOutOfLine;
    /**
     * ??????Activity??????????????????
     */
    View decorView;

    /**
     * ????????????????????????
     */
    float downX, downY;

    /**
     * ??????????????????????????????
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
//        // ??????decorView
//        decorView = getWindow().getDecorView();
//        // ???????????????????????????????????????????????????
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
                        ToastUtil.show(getApplicationContext(), "Ta??????????????????????????????");
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
        //Android4.0???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        mPersonInfoTopTv.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spanableInfo = new SpannableString("???????????????????????????????????????????????????");
//        SpannableString spanableInfo = new SpannableString("????????????????????????????????????????????????????????????????????????");
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
        mPersonInfo_header_text_more = view.findViewById(R.id.mPersonInfo_header_text_more);//??????  ??????
        mPersonHeaderInfoBottomLL02 = (AutoRelativeLayout) view.findViewById(R.id.mPersonInfo_header_bottom_ll02);
        mPersonHeaderInfoBottomTv02 = (TextView) view.findViewById(R.id.mPersonInfo_header_bottom_tv02);
        mPersonInfo_header_bottom_more02 = view.findViewById(R.id.mPersonInfo_header_bottom_more02);////??????  ??????
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
                ToastUtil.show(getApplicationContext(), "????????????");
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

        //??????????????????????????????
        String timeMilliss = String.valueOf(System.currentTimeMillis());
        String millis = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "personTimeMillis", "0");
//        String personUid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "personUid", "0");
        //????????????????????????????????????????????????????????????
//        if(!personUid.equals(uid)){
//            //???????????????????????????
//
//        }else{
        if ((Long.parseLong(timeMilliss) - Long.parseLong(millis)) > (5 * 1 * 1000)) {
            //???????????????????????????
            writeVisitRecord();
        }
//        }
    }

    private void setBlackView() {
        blackView = View.inflate(this, R.layout.item_person_info_black_header, null);
        mPersonInfoBlackTopTv = (TextView) blackView.findViewById(R.id.mPersonInfo_black_top_tv);
        //Android4.0???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        mPersonInfoBlackTopTv.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spanableInfo = new SpannableString("????????????????????????????????????????????????????????????????????????");
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
                    VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "??????/svip ?????????????????????");
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

    private void getUserInfo() {//??????????????????
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
                                        qiaoqiaoattention = "??????????????????";
                                    } else {
                                        qiaoqiaoattention = "????????????(svip)";
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
                                        mPersonInfo_beizhu.setText("???" + nickname + "???");
                                        mPersonInfoName.setText(datas.getMarkname());
                                    } else {
                                        mPersonInfoName.setText(nickname);
                                        layout_beizhu.setVisibility(View.INVISIBLE);
                                    }


                                    //??????
                                    photo_rule = datas.getPhoto_rule();//???????????????
                                    //??????
                                    dynamic_rule = datas.getDynamic_rule();//???????????????
                                    //??????
                                    comment_rule = datas.getComment_rule();//???????????????
                                    followState = datas.getFollow_state();//?????????????????????
//                                    jb
                                    String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                                    String svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
                                    mPersonInfo_header_text_more.setText("???????????????");
                                    mPersonInfo_header_bottom_more02.setText("???????????????");
                                    if (dynamic_rule.equals("1")) {
                                        mPersonInfo_header_text_more.setText("??????/????????????");
                                    }
                                    if (comment_rule.equals("1")) {
                                        mPersonInfo_header_bottom_more02.setText("??????/????????????");
                                    }


                                    if (!followState.equals("3")) {

                                    }

                                    is_likeliar = datas.getIs_likeliar();
                                    if (is_likeliar.equals("1")) {
                                        mPersonInfoTopTv.setVisibility(View.VISIBLE);
                                    } else {
                                        mPersonInfoTopTv.setVisibility(View.GONE);
                                    }
                                    //?????????????????????
                                    realpicstate = datas.getRealpicstate();
                                    joinGroupStatus=datas.getJoin_group_status();
                                    if (!PesonInfoActivity.this.uid.equals(MyApp.uid)) {
                                        if (datas.getRealname().equals("1")) {
                                            if (realpicstate.equals("0")) {
                                                mPersonInfoRealnamePhoto.setText("??????????????????");
                                            } else {
                                                mPersonInfoRealnamePhoto.setText("?????????vip??????");
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
                                            mPersonInfoTime.setText("???Ta????????????");
                                        } else {
                                            mPersonInfoTime.setText(logintime);
                                        }
                                    } else {
                                        mPersonInfoTime.setText("??????");
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
                                        //??????????????????
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
                                        mPersonInfoRole.setText("???");
                                    } else if (role.equals("M")) {
                                        mPersonInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        mPersonInfoRole.setText("???");
                                    } else if (role.equals("SM")) {
                                        mPersonInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        mPersonInfoRole.setText("???");
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
                                        mPersonInfoCity.setText("??????");
                                    } else {

                                        if (mCity.equals("")) {
                                            mPersonInfoCity.setVisibility(View.INVISIBLE);
                                        } else {
                                            if (!("").equals(mProvince)) {
                                                if (mProvince.equals(mCity)) {
                                                    if (mProvince.contains("???")) {
                                                        mPersonInfoCity.setText(mProvince.substring(0, mProvince.length() - 1));
                                                    } else {
                                                        mPersonInfoCity.setText(mProvince);
                                                    }
                                                } else {
                                                    if (mProvince.contains("???")) {
                                                        mProvince = mProvince.substring(0, mProvince.length() - 1);
                                                    }
                                                    if (mCity.contains("???")) {
                                                        mCity = mCity.substring(0, mCity.length() - 1);
                                                    }
                                                    mPersonInfoCity.setText(mProvince + " " + mCity);
                                                }
                                            } else {
                                                if (mCity.contains("???")) {
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
//                                        mPersonInfoIntroduceAdminTv.setText(clickUtils.addClickablePart("??????????????????????????????VIP??????", "???????????????", "??????"), TextView.BufferType.SPANNABLE);
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
                                    mPersonInfoHeight.setText("??????:  " + tall);
                                    mPersonInfoWeight.setText("??????:  " + weight);
                                    mPersonInfoStar.setText("??????:  " + datas.getStarchar());
                                    mPersonInfoQuxiang.setText("??????:  " + datas.getSexual());
                                    mPersonInfoJiechu.setText("??????:  " + datas.getAlong());
                                    mPersonInfoShijian.setText("??????:  " + datas.getExperience());
                                    mPersonInfoLevel.setText("??????:  " + datas.getLevel());
                                    mPersonInfoWant.setText("??????:  " + datas.getWant());
                                    mPersonInfoCulture.setText("??????:  " + datas.getCulture());
                                    mPersonInfoMoney.setText("??????:  " + datas.getMonthly());
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
                                        if (PesonInfoActivity.this.uid.equals(MyApp.uid)) {    //???????????? ???????????????????????????
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
                                        } else {//??????????????????
                                            //?????????????????????
                                            if (datas.getPhoto_lock().equals("1")) {               //1?????????????????????    2????????????
                                                if (photo_rule.equals("0")) {   //????????????????????????????????????  0??????
                                                    adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 0);//??????
                                                } else {
                                                    //??????????????????
                                                    vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                                                    svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
                                                    if (vip.equals("1") || svip.equals("1") || followState.equals("3")) {//?????????????????????
                                                        adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 0);
                                                    } else {//??????????????????
                                                        adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 1);//?????????????????????
                                                    }
                                                }
                                            } else {
                                                adapter = new RecyclerViewAdapter(photos, PesonInfoActivity.this, 1);//??????
                                            }


                                            mPersonInfoRecyclerview.setAdapter(adapter);
                                            //////////////////////////////////////////////// ///////////////////////////////////////////////////////?????????????????????
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
                                                        if (datas.getPhoto_lock().equals("1")) {       //?????????????????????   1????????????
                                                            //?????????????????????
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
                                                                    seePhotoVip("TA???????????????????????????VIP????????????~");
                                                                }
                                                            }
                                                        } else {
                                                            showInputPwdDialog();//???????????????
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
//                                                        if (datas.getPhoto_lock().equals("1")) {//?????????????????????   1????????????
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
//                                                                    seePhotoVip("TA???????????????????????????VIP????????????~");
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
////                                                        //vip???????????????????????????
////                                                        seePhotoVip("TA???????????????????????????VIP????????????~");
////                                                    } else {
////                                                        if (datas.getPhoto_lock().equals("1")) {//???????????????
////                                                            if (photo_rule.equals("1")){
////                                                                //??????????????????
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
                                        mPersonHeaderInfoBottomTv.setText("??????????????????" + datas.getDynamic_num() + "???");
                                    }

                                    if (datas.getComment_num().equals("0")) {
                                        mPersonHeaderInfoBottomLL02.setVisibility(View.GONE);
                                    } else {
                                        mPersonHeaderInfoBottomLL02.setVisibility(View.VISIBLE);
                                        mPersonHeaderInfoBottomLL02.animate().alpha(1).setStartDelay(50).start();
                                        mPersonHeaderInfoBottomTv02.setText("??????????????????" + datas.getComment_num() + "???");
                                    }
                                    //????????????
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
                                    //???????????????????????????
                                    is_likeliar = datass.getIs_likeliar();
                                    if ("1".equals(is_likeliar)) {
                                        mPersonInfoBlackTopTv.setVisibility(View.VISIBLE);
                                    } else {
                                        mPersonInfoBlackTopTv.setVisibility(View.GONE);
                                    }
                                    blackState = datass.getBlack_state();
                                    if ("0".equals(blackState)) {
                                        mPersonInfoBlackBlack.setText("??????");
                                    } else {
                                        mPersonInfoBlackBlack.setText("????????????");
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
                                        mPersonInfoBlackRole.setText("???");
                                    } else if ("M".equals(role)) {
                                        mPersonInfoBlackRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        mPersonInfoBlackRole.setText("???");
                                    } else if ("SM".equals(role)) {
                                        mPersonInfoBlackRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        mPersonInfoBlackRole.setText("???");
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
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//???????????????,???????????????????????????????????????,???????????????margin?????????
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//??????dialog?????????,??????????????????
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        etPsw = (EditText) window.findViewById(R.id.item_photo_inpsw_edittext);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_photo_inpsw_confim);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPsw.getText().toString();
                if (TextUtils.equals(password, "")) {
                    ToastUtil.show(getApplicationContext(), "???????????????");
                } else {
                    //??????????????????
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
                            String str03 = "????????????";
                            String str04 = "????????????";
                            String str05 = "????????????";
                            String str06 = "????????????";
                            String str07 = "??????????????????";
                            String str08 = "??????";
                            String str09 = "??????";
                            String str10;
                            String str11 = "????????????(svip)";
                            String str12 = "????????????(??????/vip)";
                            String str13 = "????????????";
                            String str14 = "????????????(??????/svip)";
                            String str15 = "????????????";
                            if (blackState.equals("0")) {
                                str10 = "??????";
                                if (status.equals("1")) {
                                   // str01 = "????????????";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "????????????";
                                    } else {
                                        str02 = "??????????????????";
                                    }
                                } else {
                                  //  str01 = "????????????";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "????????????";
                                    } else {
                                        str02 = "??????????????????";
                                    }
                                }
                            } else {
                                str10 = "????????????";
                                if (status.equals("1")) {
                                  //  str01 = "????????????";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "????????????";
                                    } else {
                                        str02 = "??????????????????";
                                    }
                                } else {
                                 //   str01 = "????????????";
                                    if (is_likeliar.equals("0")) {
                                        str02 = "????????????";
                                    } else {
                                        str02 = "??????????????????";
                                    }
                                }
                            }
                            //????????????????????????
                            if ("0".equals(joinGroupStatus)){
                                str15="????????????";
                            }else {
                                str15="????????????";
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
//                ToastUtil.show(getApplicationContext(),"?????????????????????~");
                break;
            case R.id.mPersonInfo_guanzhu:
                mPersonInfoGuanzhu.setEnabled(false);
                if (followState.equals("1")) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage("?????????????????????,??????????????????????")
                            .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPersonInfoGuanzhu.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            overfollow();
                        }
                    }).create().show();
                } else if (followState.equals("2")) {
                    follow();
                } else if (followState.equals("3")) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage("??????Ta????????????,??????????????????????")
                            .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPersonInfoGuanzhu.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            overfollow();
                        }
                    }).create().show();
                } else if (followState.equals("4")) {
                    follow();
                }
                break;
            case R.id.mPersonInfo_chat:  //????????????
                //????????????
                String isSpeak = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "nospeak", "1");
                if (isSpeak.equals("0")) {
                    ToastUtil.show(getApplicationContext(), "???????????????????????????????????????????????????????????????????????????");
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
                                    ToastUtil.show(getApplicationContext(), "??????????????????,???????????????...");
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
                            SpannableStringBuilder builder = new SpannableStringBuilder(myPresentData.getData().getAllnum() + "???");
                            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                            builder.setSpan(purSpan, 0, myPresentData.getData().getAllnum().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mPersonInfoPresentCount.setText(builder);
                            if (myPresentData.getData().getGiftArr().size() != 0) {
                                mPersonInfoPresentLl.setVisibility(View.VISIBLE);
                                mPersonInfoPresentGridview.setClickable(false);// ????????????????????????
                                mPersonInfoPresentGridview.setPressed(false);
                                mPersonInfoPresentGridview.setEnabled(false);
                                if (myPresentData.getData().getGiftArr().size() != 0) {
                                    List<MyPresentData.DataBean.GiftArrBean> gifts = myPresentData.getData().getGiftArr();
                                    List<MyPresentData.DataBean.GiftArrBean> giftss = new ArrayList<MyPresentData.DataBean.GiftArrBean>();
                                    //??????????????????????????????
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
        ToastUtil.show(getApplicationContext(), "????????????,?????????...");
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
            ToastUtil.show(getApplicationContext(), "Ta????????????????????????");
        }
    }

    private void isOpenChat() {//?????????????????????
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
                            //??????????????????svip
//                            if(svip.equals("1")){//?????????svip???????????????
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
                                //????????????????????????????????????
                                case 2000:
                                    //????????????????????????
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
                                case 3001://????????????
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

    //??????????????????
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
                                    if (char_rule.equals("1")) {  //1????????????
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
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//???????????????,???????????????????????????????????????,???????????????margin?????????
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
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//???????????????,???????????????????????????????????????,???????????????margin?????????
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView message = (TextView) window.findViewById(R.id.item_open_vip_message);
        TextView follow = (TextView) window.findViewById(R.id.item_open_vip_follow);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_vip_buy);
        follow.setText("????????????");
        message.setText("TA??????????????????????????????????????????VIP????????????~");
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
                    new VipDialog(this, "??????????????????Ta??????????????????~");
                }
            }
        }
    }


    private void ownerOperation() {
        new AlertView(null, null, "??????", null,
                new String[]{"??????", "????????????(svip)", "????????????(??????/svip)"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void ownerOperation1() {
        new AlertView(null, null, "??????", null,
                new String[]{"??????", "????????????(svip)", "????????????(??????/svip)", "????????????"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void operation() {
        new AlertView(null, null, "??????", null,
                new String[]{"??????", qiaoqiaoattention, fenzutention, "????????????(svip)", "????????????(??????/vip)", "????????????(??????/svip)", "??????", "??????"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void cancelOperation() {
        new AlertView(null, null, "??????", null,
                new String[]{"??????", qiaoqiaoattention, fenzutention, "????????????(svip)", "????????????(??????/vip)", "????????????(??????/svip)", "??????", "????????????"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void adminOperation( String str02, String str13, String str03, String str04, String str05, String str06, String str07, String str12, String str11, String str08, String str09, String str10, String str14,String str15) {
        new AlertView(null, null, "??????", null,
                new String[]{ str02, str03, str04, str05, str06, str15,str13, str07, qiaoqiaoattention, fenzutention, str11, str12, str14, str08, str09, str10},
                this, AlertView.Style.ActionSheet, this).show();
    }


    private void adminDeleteOperation(String str01, String str02, String str03, String str04, String str05) {
        new AlertView(null, null, "??????", null,
                new String[]{str01, str02, str03, str04, str05},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void adminDaysOperation() {
        new AlertView(null, null, "??????", null,
                new String[]{"1???", "3???", "1???", "2???", "1???", "??????"},
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
                        //????????????
                        Intent intent1 = new Intent(this, XiangXiBeiZhuActivity.class);
                        intent1.putExtra("name", lmarkname);
                        intent1.putExtra("fuid", uid);
                        startActivity(intent1);
                        break;
                    case 3:
                        //????????????
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
                        //????????????
                        setqiaoqiaoattention();

                        break;
                    case 2:
                        //????????????
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
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "??????/vip ???????????????");
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
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "??????/svip ?????????????????????");
                        }
                        break;
                    case 6:
                        Intent intent = new Intent(this, ReportActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        break;
                    case 7:
                        if (blackState.equals("0")) {
                            //??????
                            if (admin.equals("1")) {
                                ToastUtil.show(this, "???V???????????????");
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
                            //??????????????????
                            setOneLiker();
                        } else {
                            //??????????????????
                            removeOneLiker();
                        }
                        break;
                    case 1:
                        //????????????
                        delIllegallyUserInfo("1");
                        break;

                    case 2:
                        //????????????
                        delIllegallyUserInfo("4");
                        break;
                    case 3:
                        //????????????
                        delIllegallyUserInfo("2");
                        break;
                    case 4:
                        //????????????
                        delIllegallyUserInfo("3");
                        break;
                    case 5:
                        //????????????
                        if ("0".equals(joinGroupStatus)){
                            joinTheGroupOperation("5");
                        } else {
                            //??????
                            recoverAllUserStatus("5");
                        }
                        break;
                    case 6:
                        //????????????
                        Intent intent1 = new Intent(this, EditGuanliBeizhuActivity.class);
                        intent1.putExtra("fuid", uid);
                        intent1.putExtra("name", admin_mark);
                        intent1.putExtra("sypic", sypic);
                        startActivity(intent1);
                        break;
                    case 7:
                        //???????????????
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
                                    str01 = "????????????";
                                } else {
                                    str01 = "????????????";
                                }
                                if (dynamicstatus.equals("1")) {
                                    str02 = "????????????";
                                } else {
                                    str02 = "????????????";
                                }
                                if (chatstatus.equals("1")) {
                                    str03 = "????????????";
                                } else {
                                    str03 = "????????????";
                                }
                                if (infostatus.equals("1")) {
                                    str04 = "????????????";
                                } else {
                                    str04 = "????????????";
                                }
                                if (devicestatus.equals("1")) {
                                    str05 = "????????????";
                                } else {
                                    str05 = "????????????";
                                }
                                adminDeleteOperation(str01, str02, str03, str04, str05);
                            }
                        }, 500);

                        break;
                    case 8:
                        //????????????
                        setqiaoqiaoattention();

                        break;
                    case 9:
                        //????????????
                        setfenzutention();
                        break;
                    case 10:
                        //????????????
                        if (uid.equals(MyApp.uid)) {
                            Intent intent = new Intent(PesonInfoActivity.this, history_nameActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        } else {
                            toHhistoryNameActivity();
                        }

                        break;
                    case 11:
                        //????????????
                        if (haoyou.equals("2001") || !vip.equals("0")) {
                            Intent intent2 = new Intent(this, EditBeizhuActivity.class);
                            intent2.putExtra("name", marknamea);
                            intent2.putExtra("fuid", uid);
                            startActivity(intent2);
                        } else {
                            //ToastUtil.show(this,"");
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "??????/vip ???????????????");
                        }
                        break;
                    case 12:
                        //????????????
                        if (haoyou.equals("2001") || !svip.equals("0")) {
                            Intent intent3 = new Intent(this, XiangXiBeiZhuActivity.class);
                            intent3.putExtra("name", lmarkname);
                            intent3.putExtra("fuid", uid);
                            startActivity(intent3);
                        } else {
                            //ToastUtil.show(this,"");
                            VipDialog vipDialog = new VipDialog(PesonInfoActivity.this, "??????/svip ?????????????????????");
                        }
                        break;
                    case 13:
                        //??????
                        showShareWay();
                        break;
                    case 14:
                        //??????
                        Intent intent = new Intent(this, ReportActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        break;
                    case 15:

                        if (blackState.equals("0")) {
                            //??????
                            if (admin.equals("1")) {
                                ToastUtil.show(this, "???V???????????????");
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
                            //??????
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
                            //????????????
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
                            //????????????
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
                            //????????????
                            recoverAllUserStatus("3");
                        }
                        break;
                    case 4:
                        if (devicestatus.equals("1")) {
                            //????????????
//                            changeDeviceStatus("forbid");
                            Intent intent = new Intent(this, SendReasonActivity.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("method", "forbid");
                            startActivity(intent);
                        } else {
                            //????????????
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
                    BindSvipDialog.bindAlertDialog(PesonInfoActivity.this, "?????????????????????svip??????");
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

    //??????
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

    //??????????????????
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
        SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic()+HttpUrl.ShareUserDetail + uid, nickname + " ????????????????????????????????????Ta~", mProvince + " " + mCity + "\n" + age + "/" + tall + "/" + weight + "/" + role, headurl, 0, 2, uid, nickname, headurl, "");
        sharedPop.showAtLocation(activityPesonInfo, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //?????????Popupwindow????????????????????????
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //??????Popupwindow??????????????????Popupwindow?????????????????????1f
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
                                    mPersonInfoBlackBlack.setText("??????");
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
                                    mPersonInfoBlackBlack.setText("????????????");
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
                                    ToastUtil.show(getApplicationContext(), "????????????????????????~");
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
//                mPersonInfoIntroduceAdminTv.setText(clickUtils.addClickablePart("??????????????????????????????VIP??????", "???????????????", "??????"), TextView.BufferType.SPANNABLE);
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
            //????????????????????????
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
            //????????????????????????
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //????????????????????????
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
//                Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
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
                    //????????????
                    mediaPlayStart();
                } else {
                    ToastUtil.show(getApplicationContext(), "Ta????????????????????????~");
                }
                break;
            case R.id.mPersonInfo_vip:
                if ("0".equals(volunteer)) {
                    if (admin.equals("1")) {
                        intent = new Intent(this, VipWebActivity.class);
                        intent.putExtra("title", "????????????");
                        intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.Recruit);
                    } else {
                        intent = new Intent(this, VipCenterActivity.class);
                        intent.putExtra("headpic", headurl);
                        intent.putExtra("uid", uid);
                    }
                } else {
                    intent = new Intent(this, VipWebActivity.class);
                    intent.putExtra("title", "???????????????");
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
                //??????????????????
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
//                        //vip????????????????????????????????????
//                        seeGroupVipAndRealname();
//                    } else {
                    intent = new Intent(this, OtherGroupActivity.class);
                    intent.putExtra("otherUid", uid);
                    startActivity(intent);
//                    }
                }
                break;
            case R.id.mPersonInfo_header_bottom_ll://????????????????????? ??????uid??????
                if ((!MyApp.uid.equals(uid)) && (vip.equals("0") && svip.equals("0") && !followState.equals("3")) && dynamic_rule.equals("1")) {   //????????????uid????????????uid???????????? ?????????????????????
                    seePhotoVip("TA?????????????????????????????????VIP????????????~");
                } else {
                    intent = new Intent(this, PersonInfoDynamicActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("nickname", nickname);
                    startActivity(intent);
                }
                break;
            case R.id.mPersonInfo_header_bottom_ll02://???????????????
                if ((!MyApp.uid.equals(uid)) && (vip.equals("0") && svip.equals("0") && !followState.equals("3")) && comment_rule.equals("1")) {   //????????????uid????????????uid???????????? ?????????????????????
                    seePhotoVip("TA?????????????????????????????????VIP????????????~");
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
                        new VipDialog(this, "????????????VIP????????????");
                    }
                } else {
                    new CommonDialog(this, "???????????????????????????");
                }
                break;
            case R.id.mPersonInfo_black_report:
                intent = new Intent(this, ReportActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.mPersonInfo_black_black:
                if (blackState.equals("0")) {
                    //??????
                    if (admin.equals("1")) {
                        ToastUtil.show(this, "???V???????????????");
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
            case R.id.tv_ban_to_dynamic: //??????

                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);
                break;
            case R.id.tv_banned_to_post: //??????
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","3");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_ban_to_information: //??????
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","4");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_ban:  //??????
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_banned_equipment:  //??????
                intent  = new Intent(this,GetOutOfLineActivity.class);
                intent.putExtra("type","5");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);

                break;
            case R.id.tv_complain:  //??????
                intent  = new Intent(this,ComplaintInformationActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);
                break;
            case R.id.tv_be_complained://?????????
                intent  = new Intent(this,ComplaintInformationActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name",nickname);
                startActivity(intent);
                break;
            case R.id.tv_account://??????
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
//                    "status": "1",(???????????????1??????????????????0??????????????????)
//                    "dynamicstatus": "1",??????????????????
//                    "infostatus": "1",??????????????????
//                    "chatstatus": "1",??????????????????
//                    "devicestatus": "1"??????????????????
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
         * ????????????????????????
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * ????????????updateDrawState??????  ???????????????TextView??????????????????,??????????????????...
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
//                    //??????????????????
//                    getBindingState();
//                    //????????????????????????????????????
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
            BindSvipDialog.bindAlertDialog(PesonInfoActivity.this, "???????????????svip??????");
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
                            qiaoqiaoattention = "??????????????????";
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
                            qiaoqiaoattention = "????????????(svip)";
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
                            //??????0/??????0/??????0/??????0/??????0/??????0
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    tvBanToDynamic.setText("??????" + data.getDynamicstatusouttimes());
                                    tvBannedToPost.setText("/??????" + data.getChatstatusouttimes());
                                    tvBanToInformation.setText( "/??????" + data.getInfostatusouttimes());
                                    tvBan.setText("/??????" + data.getStatusouttimes());
                                    tvBannedEquipment.setText("/??????" + data.getDevicestatusouttimes());
                                    tvComplain.setText("/??????" + data.getReporttimes());
                                    tvBeComplained.setText("/??????" + data.getBereportedtimesright());
                                    tvAccount.setText("/??????"+data.getAccountnumbercount());
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
            BindSvipDialog.bindAlertDialog(PesonInfoActivity.this, "???????????????svip??????");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getfenzushuaxin(String str) {
        if (str.equals("gerenzhuyefenzushuaxin")) {
            getfriendgrouplist();
        }
    }

    //????????????
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
                                            fenzustr += data.get(i).getFgname() + "???";
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
        //??????????????????
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
        //????????????????????????????????????MOVE?????????????????????DOWN???????????????????????????
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
