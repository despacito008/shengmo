package com.aiwujie.shengmo.activity.user;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AccountActivity;
import com.aiwujie.shengmo.activity.EditBeizhuActivity;
import com.aiwujie.shengmo.activity.EditPersonMsgActivity;
import com.aiwujie.shengmo.activity.GetOutOfLineActivity;
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.activity.OtherGroupActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.activity.PresentListActivity;
import com.aiwujie.shengmo.activity.RecorderActivity;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.SendDynamicActivity;
import com.aiwujie.shengmo.activity.history_nameActivity;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity;
import com.aiwujie.shengmo.adapter.RecyclerViewAdapter;
import com.aiwujie.shengmo.adapter.UserInfoAdminMaskRecyclerAdapter;
import com.aiwujie.shengmo.adapter.UserInfoGiftRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllUserStates;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.DialogStampData;
import com.aiwujie.shengmo.bean.LiveAnchorStateBean;
import com.aiwujie.shengmo.bean.LiveLinkChatStateBean;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.bean.UserIdCardModel;
import com.aiwujie.shengmo.bean.UserInfoBean;
import com.aiwujie.shengmo.bean.ViolationBean;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.customview.BindSvipDialog;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.customview.StampDialogNew;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.eventbus.UserLiveHistoryRefreshBean;
import com.aiwujie.shengmo.fragment.user.UserCommentFragment;
import com.aiwujie.shengmo.fragment.user.UserDynamicFragment;
import com.aiwujie.shengmo.fragment.user.UserInfoFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener;
import com.aiwujie.shengmo.kt.ui.activity.ComplaintInfoActivity;
import com.aiwujie.shengmo.kt.ui.activity.EditPersonInfoActivity;
import com.aiwujie.shengmo.kt.ui.activity.FriendSetGroupActivity;
import com.aiwujie.shengmo.kt.ui.activity.LiveAdminOperateActivity;
import com.aiwujie.shengmo.kt.ui.activity.LiveChatSettingActivity;
import com.aiwujie.shengmo.kt.ui.activity.LiveSealDetailActivity;
import com.aiwujie.shengmo.kt.ui.activity.MyLiveLevelActivity;
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.ManagerBanActivity;
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.ManagerMarkActivity;
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.UserMarkActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.UserGroupListActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.UserRelationShipActivity;
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.GroupSquareActivity;
import com.aiwujie.shengmo.kt.ui.fragment.UserLiveHistoryFragment;
import com.aiwujie.shengmo.kt.ui.fragment.UserLivePlaybackFragment;
import com.aiwujie.shengmo.kt.ui.view.AdminBanPop;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop2;
import com.aiwujie.shengmo.kt.ui.view.LinkUserTypePop;
import com.aiwujie.shengmo.kt.ui.view.LiveInfoPop;
import com.aiwujie.shengmo.kt.ui.view.LiveManageUserPop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.ui.view.SignAnchorPop;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.kt.util.SpKey;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.AppBarStateChangeListener;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.ImageViewUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.PermissionHelper;
import com.aiwujie.shengmo.utils.PhotoRemoteUtil;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.DisallowViewPager;
import com.aiwujie.shengmo.view.NormalEditPop;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.aiwujie.shengmo.view.ScrollExpandTextView;
import com.aiwujie.shengmo.view.SendGiftPop;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.ui.TRTCAudioCallActivity;
import com.tencent.liteav.ui.TRTCVideoCallActivity;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class UserInfoActivity extends AppCompatActivity {
    private static  String TAG =UserInfoActivity.class.getSimpleName();

    @BindView(R.id.tv_user_info_admin_mark)
    TextView tvUserInfoAdminMark;
    @BindView(R.id.rv_user_info_admin_mark)
    RecyclerView rvUserInfoAdminMark;
    @BindView(R.id.ll_user_info_admin_mark)
    LinearLayout llUserInfoAdminMark;
    @BindView(R.id.tv_user_info_desc)
    TextView tvUserInfoDesc;
    @BindView(R.id.rv_user_info_desc)
    RecyclerView rvUserInfoDesc;
    @BindView(R.id.ll_user_info_desc)
    LinearLayout llUserInfoDesc;
    @BindView(R.id.tv_user_info_f_groups)
    TextView tvUserInfoFGroups;
    @BindView(R.id.ll_user_info_f_groups)
    LinearLayout llUserInfoFGroups;
    @BindView(R.id.ll_user_info_mark)
    LinearLayout llUserInfoMark;
    @BindView(R.id.view_user_info_cover)
    ImageView viewUserInfoCover;
    @BindView(R.id.view_user_info_mask)
    View viewUserInfoMask;
    @BindView(R.id.view_user_info_cover_bottom)
    View viewUserInfoCoverBottom;
    @BindView(R.id.tv_user_info_register_time)
    TextView tvUserInfoRegisterTime;
    @BindView(R.id.tv_user_info_like_liar_tip)
    TextView tvUserInfoLikeLiarTip;
    @BindView(R.id.iv_user_info_voice)
    ImageView ivUserInfoVoice;
    @BindView(R.id.tv_user_info_voice)
    TextView tvUserInfoVoice;
    @BindView(R.id.ll_user_info_voice)
    LinearLayout llUserInfoVoice;
    @BindView(R.id.iv_user_info_recording)
    ImageView ivUserInfoRecording;
    @BindView(R.id.tv_user_info_give_vip)
    TextView tvUserInfoGiveVip;
    @BindView(R.id.iv_user_avatar_state)
    ImageView ivUserAvatarState;
    @BindView(R.id.iv_user_avatar_icon)
    ImageView ivUserAvatarIcon;
    @BindView(R.id.iv_user_avatar_level)
    ImageView ivUserAvatarLevel;
    @BindView(R.id.iv_user_avatar_online)
    ImageView ivUserAvatarOnline;
    @BindView(R.id.layout_user_avatar)
    ConstraintLayout layoutUserAvatar;
    @BindView(R.id.iv_user_info_auth_photo)
    ImageView ivUserInfoAuthPhoto;
    @BindView(R.id.tv_user_info_name)
    TextView tvUserInfoName;
    @BindView(R.id.iv_user_info_photo_auth)
    ImageView ivUserInfoPhotoAuth;
    @BindView(R.id.iv_user_info_video_auth)
    ImageView ivUserInfoVideoAuth;
    @BindView(R.id.iv_user_info_id_card_auth)
    ImageView ivUserInfoIdCardAuth;
    @BindView(R.id.tv_user_info_history_name)
    TextView tvUserInfoHistoryName;
    @BindView(R.id.ll_user_info_name)
    LinearLayout llUserInfoName;
    @BindView(R.id.tv_user_info_mark_name)
    TextView tvUserInfoMarkName;
    @BindView(R.id.tv_user_info_sex_and_age)
    TextView tvUserInfoSexAndAge;
    @BindView(R.id.tv_user_info_role)
    TextView tvUserInfoRole;
    @BindView(R.id.tv_user_info_wealth)
    TextView tvUserInfoWealth;
    @BindView(R.id.ll_user_info_wealth)
    LinearLayout llUserInfoWealth;
    @BindView(R.id.tv_user_info_charm)
    TextView tvUserInfoCharm;
    @BindView(R.id.ll_user_info_charm)
    LinearLayout llUserInfoCharm;
    @BindView(R.id.ll_user_info_base)
    LinearLayout llUserInfoBase;
    @BindView(R.id.iv_user_info_login_time)
    ImageView ivUserInfoLoginTime;
    @BindView(R.id.tv_user_info_login_time)
    TextView tvUserInfoLoginTime;
    @BindView(R.id.ll_user_info_online_time)
    LinearLayout llUserInfoOnlineTime;
    @BindView(R.id.iv_user_info_distance)
    ImageView ivUserInfoDistance;
    @BindView(R.id.tv_user_info_distance)
    TextView tvUserInfoDistance;
    @BindView(R.id.ll_user_info_location)
    LinearLayout llUserInfoLocation;
    @BindView(R.id.iv_user_info_address)
    ImageView ivUserInfoAddress;
    @BindView(R.id.tv_user_info_address)
    TextView tvUserInfoAddress;
    @BindView(R.id.ll_user_info_address)
    LinearLayout llUserInfoAddress;
    @BindView(R.id.ll_user_info_time)
    LinearLayout llUserInfoTime;
    @BindView(R.id.tv_user_info_ban_to_dynamic)
    TextView tvUserInfoBanToDynamic;
    @BindView(R.id.tv_user_info_banned_to_post)
    TextView tvUserInfoBannedToPost;
    @BindView(R.id.tv_user_info_ban_to_information)
    TextView tvUserInfoBanToInformation;
    @BindView(R.id.tv_user_info_ban_account)
    TextView tvUserInfoBanAccount;
    @BindView(R.id.tv_user_info_banned_equipment)
    TextView tvUserInfoBannedEquipment;
    @BindView(R.id.tv_user_info_complain)
    TextView tvUserInfoComplain;
    @BindView(R.id.tv_user_info_be_complained)
    TextView tvUserInfoBeComplained;
    @BindView(R.id.tv_user_info_account)
    TextView tvUserInfoAccount;
    @BindView(R.id.ll_user_info_account_info)
    LinearLayout llUserInfoAccountInfo;
    @BindView(R.id.tv_user_info_follow_num_txt)
    TextView tvUserInfoFollowNumTxt;
    @BindView(R.id.tv_user_info_follow_num)
    TextView tvUserInfoFollowNum;
    @BindView(R.id.tv_user_info_fans_num_txt)
    TextView tvUserInfoFansNumTxt;
    @BindView(R.id.tv_user_info_fans_num)
    TextView tvUserInfoFansNum;
    @BindView(R.id.tv_user_info_group_num_txt)
    TextView tvUserInfoGroupNumTxt;
    @BindView(R.id.tv_user_info_group_num)
    TextView tvUserInfoGroupNum;
    @BindView(R.id.ll_user_info_relationship)
    LinearLayout llUserInfoRelationship;
    @BindView(R.id.tv_user_info_gift_num)
    TextView tvUserInfoGiftNum;
    @BindView(R.id.rv_user_info_gift)
    RecyclerView rvUserInfoGift;
    @BindView(R.id.ll_user_info_gift)
    LinearLayout llUserInfoGift;
    @BindView(R.id.tv_user_info_sign)
    TextView tvUserInfoSign;
    @BindView(R.id.tv_user_info_sign_hideOrShow)
    TextView tvUserInfoSignHideOrShow;
    @BindView(R.id.expand_text_user_info_sign)
    ScrollExpandTextView expandTextUserInfoSign;
    @BindView(R.id.rv_user_info_photo)
    RecyclerView rvUserInfoPhoto;
    @BindView(R.id.ll_user_info_info)
    LinearLayout llUserInfoInfo;
    @BindView(R.id.tv_user_info_block_info)
    TextView tvUserInfoBlockInfo;
    @BindView(R.id.tv_user_info_block_report)
    TextView tvUserInfoBlockReport;
    @BindView(R.id.tv_user_info_block_cancel)
    TextView tvUserInfoBlockCancel;
    @BindView(R.id.ll_user_info_block)
    LinearLayout llUserInfoBlock;
    @BindView(R.id.iv_user_info_return)
    ImageView ivUserInfoReturn;
    @BindView(R.id.tv_tool_bar_title)
    TextView tvToolBarTitle;
    @BindView(R.id.iv_user_info_edit)
    ImageView ivUserInfoEdit;
    @BindView(R.id.iv_user_info_more)
    ImageView ivUserInfoMore;
    @BindView(R.id.collapsingToolbarLayout_user_info)
    CollapsingToolbarLayout collapsingToolbarLayoutUserInfo;
    @BindView(R.id.tab_user_info)
    TabLayout tabUserInfo;
    @BindView(R.id.app_bar_user_info)
    AppBarLayout appBarUserInfo;
    @BindView(R.id.vp_user_info)
    DisallowViewPager vpUserInfo;
    @BindView(R.id.coordinator_user_info)
    CoordinatorLayout coordinatorUserInfo;
    @BindView(R.id.smart_refresh_user_info)
    SmartRefreshLayout smartRefreshUserInfo;
    @BindView(R.id.ll_user_info_send_dynamic)
    LinearLayout llUserInfoSendDynamic;
    @BindView(R.id.ll_user_info_send_gift)
    LinearLayout llUserInfoSendGift;
    @BindView(R.id.tv_user_info_attention)
    TextView tvUserInfoAttention;
    @BindView(R.id.ll_user_info_attention)
    LinearLayout llUserInfoAttention;
    @BindView(R.id.iv_user_info_chat_status)
    ImageView ivUserInfoChatStatus;
    @BindView(R.id.tv_user_info_chat)
    TextView tvUserInfoChat;
    @BindView(R.id.ll_user_info_chat)
    LinearLayout llUserInfoChat;
    @BindView(R.id.ll_user_info_bottom)
    LinearLayout llUserInfoBottom;
    @BindView(R.id.lottie_user_avatar_state)
    LottieAnimationView lottieUserAvatarState;
    @BindView(R.id.tv_live_anchor_level)
    TextView tvLiveAnchorLevel;
    @BindView(R.id.view_live_anchor_level)
    View viewLiveAnchorLevel;
    @BindView(R.id.iv_live_anchor_level)
    ImageView ivLiveAnchorLevel;
    @BindView(R.id.constraint_layout_anchor_level)
    ConstraintLayout constraintLayoutAnchorLevel;
    @BindView(R.id.tv_live_audience_level)
    TextView tvLiveAudienceLevel;
    @BindView(R.id.view_live_audience_level)
    View viewLiveAudienceLevel;
    @BindView(R.id.iv_live_audience_level)
    ImageView ivLiveAudienceLevel;
    @BindView(R.id.constraint_layout_audience_level)
    ConstraintLayout constraintLayoutAudienceLevel;
    @BindView(R.id.tv_user_info_live_warning)
    TextView tvUserInfoLiveWarning;
    @BindView(R.id.tv_user_info_live_kick_out)
    TextView tvUserInfoLiveKickOut;
    @BindView(R.id.tv_user_info_be_live_ban)
    TextView tvUserInfoBeLiveBan;
    @BindView(R.id.tv_link_user_state)
    TextView tvLinkUserState;
    @BindView(R.id.ll_link_user)
    LinearLayout llLinkUser;
    @BindView(R.id.tv_user_info_uid)
    TextView tvUserInfoUid;
    @BindView(R.id.view_layout_avatar_bg)
    View viewLayoutAvatarBg;
    @BindView(R.id.view_link_user_state)
    View viewLinkUserState;
    @BindView(R.id.iv_user_info_idcard_photo)
    ImageView ivUserInfoIdCardPhoto;

    private List<String> titles;
    private UserInfoBean userInfoBean;
    private UserInfoFragment userInfoFragment;
    private UserDynamicFragment userDynamicFragment;
    private UserCommentFragment userCommentFragment;
    private UserLiveHistoryFragment userLiveHistoryFragment;
    private UserLivePlaybackFragment userLivePlayBackFragment;
    private String uid;
    private String nickname;
    private String vip;
    private String svip;
    private String admin;
    private RecyclerViewAdapter photoAdapter;
    private int position;
    private OnItemClickListener alertItemListener;
    private OnItemClickListener adminItemListener;
    private AllUserStates.DataBean userBanStatusBean;

    public static void start(Context context, String uid) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(IntentKey.UID, uid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_user_info);
        ButterKnife.bind(this);
        uid = getIntent().getStringExtra(IntentKey.UID);
        position = getIntent().getIntExtra("position", -1);
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
        getMyOwnInfo();
        initStatusBar();
        setListener();
        EventBus.getDefault().register(this);
        //Glide.with(this).load(R.drawable.ic_user_living).into(ivUserAvatarState);
        // Glide.with(this).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
        //EventBus.getDefault().post("2333");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        uid = getIntent().getStringExtra("uid");
        getMyOwnInfo();
//        if (vpUserInfo.getCurrentItem() == 1 && userDynamicFragment != null) {
//            userDynamicFragment.refreshData();
//        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume");
        getData();
    }

    void getData() {
        getUserInfo();
        getUserPresent();
    }

    public void refreshData() {
        getUserInfo();
        getUserPresent();
        if (vpUserInfo.getCurrentItem() == 1 && userDynamicFragment != null) {
            userDynamicFragment.refreshData();
        } else if (vpUserInfo.getCurrentItem() == 2 && userCommentFragment != null) {
            userCommentFragment.refreshData();
        } else if (vpUserInfo.getCurrentItem() == 3 && userLiveHistoryFragment != null) {
            userLiveHistoryFragment.refreshData();
        }
    }

    void initStatusBar() {
        ImmersionBar.with(this).navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();
    }

    void initViewPage(UserInfoBean.DataBean userInfo) {
        final List<Fragment> fragments = new ArrayList<>();
        userInfoFragment = new UserInfoFragment();
        userDynamicFragment = new UserDynamicFragment();
        userCommentFragment = new UserCommentFragment();

        fragments.add(userInfoFragment);
        fragments.add(userDynamicFragment);
        fragments.add(userCommentFragment);
        titles = new ArrayList<>();
        titles.add("资料");
        titles.add("动态(" + userInfo.getDynamic_num() + ")");
        titles.add("评论(" + userInfo.getComment_num() + ")");
        //主播
        if (!TextUtil.isEmpty(userInfo.getAnchor_room_id()) && !"0".equals(userInfo.getAnchor_room_id())) {
            userLiveHistoryFragment = UserLiveHistoryFragment.Companion.newInstance(uid);
            fragments.add(userLiveHistoryFragment);
            titles.add("直播(" + userInfo.getLive_prohibition() + ")");
        }

        //是主播 是否有回放
        if (!TextUtil.isEmpty(userInfo.getAnchor_room_id()) && !"0".equals(userInfo.getAnchor_room_id())) {
            if (MyApp.uid.equals(uid) || !TextUtil.isEmpty(userInfo.getLive_record_times()) && !"0".equals(userInfo.getLive_record_times())) {
                userLivePlayBackFragment = userLivePlayBackFragment.Companion.newInstance(uid, isRecord);
                fragments.add(userLivePlayBackFragment);
                titles.add("回放(" + userInfo.getLive_record_times() + ")");
            }
        }

        commentNum = Integer.parseInt(userInfo.getComment_num());
        FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            //覆写此函数返回为空，这样FragmentManager.getFragment函数中就不满足第一个判断条件，不会执行后续代码也不会抛出异常了。
            @Override
            public Parcelable saveState() {
                return null;
            }
        };
        vpUserInfo.setAdapter(fragmentStatePagerAdapter);
        vpUserInfo.setOffscreenPageLimit(fragments.size());
        tabUserInfo.setupWithViewPager(vpUserInfo);
        //TablayoutLineWidthUtils.setIndicator(tabUserInfo,30,30);
        for (int i = 0; i < tabUserInfo.getTabCount(); i++) {
            TabLayout.Tab tab = tabUserInfo.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        tabUserInfo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                View view = tabView.findViewById(R.id.tab_item_textview);
                View bottomView = tabView.findViewById(R.id.tab_item_bottom);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(18);
                    ((TextView) view).setTextColor(ContextCompat.getColor(UserInfoActivity.this, R.color.titleBlack));
                    ((TextView) view).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    bottomView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                View view = tabView.findViewById(R.id.tab_item_textview);
                View bottomView = tabView.findViewById(R.id.tab_item_bottom);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(14);
                    ((TextView) view).setTextColor(ContextCompat.getColor(UserInfoActivity.this, R.color.normalGray));
                    ((TextView) view).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    bottomView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        showUserBaseInfo(userInfo);
    }


    //自定义Tab的View
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        View bottomView = view.findViewById(R.id.tab_item_bottom);
        textView.setText(titles.get(currentPosition));
        if (currentPosition == 0) {
            textView.setTextSize(18);
            textView.setTextColor(ContextCompat.getColor(UserInfoActivity.this, R.color.titleBlack));
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            bottomView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    void getMyOwnInfo() {
        //vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        //svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
        vip = "0";
        svip = "0";
        admin = "0";
        HttpHelper.getInstance().getMyOwnInfo(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                VipAndVolunteerData vipAndVolunteerData = GsonUtil.GsonToBean(data, VipAndVolunteerData.class);
                vip = vipAndVolunteerData.getData().getVip();
                svip = vipAndVolunteerData.getData().getSvip();
                admin = vipAndVolunteerData.getData().getIs_admin();
                SharedPreferencesUtils.setParam(UserInfoActivity.this, "vip", vip);
                SharedPreferencesUtils.setParam(UserInfoActivity.this, "svip", svip);
                SharedPreferencesUtils.setParam(UserInfoActivity.this, "admin", admin);
                if ("1".equals(admin)) {
                    getUserAccountInfo();
                    getUserBanStatus();
                    getLiveAnchorState();
                    tvUserInfoUid.setVisibility(View.VISIBLE);
                    tvUserInfoUid.setText("uid:" + uid);

                }
                showUserOwnView();
                getData();
            }

            @Override
            public void onFail(String msg) {
                getData();
            }
        });


    }

    void showUserOwnView() {
        Log.v("showUserOwnView",SharedPreferencesUtils.geParam(this,"uid","")+"----"+uid);
        if (SharedPreferencesUtils.geParam(this,"uid","").equals(uid)) {
            ivUserInfoEdit.setVisibility(View.VISIBLE);
            llUserInfoChat.setVisibility(View.GONE);
            llUserInfoAttention.setVisibility(View.GONE);
            llUserInfoSendGift.setVisibility(View.GONE);
            llUserInfoSendDynamic.setVisibility(View.VISIBLE);
            ivUserInfoRecording.setVisibility(View.VISIBLE);
            tvUserInfoGiveVip.setVisibility(View.GONE);
            llLinkUser.setVisibility(View.VISIBLE);
        } else {
            if ("1".equals(admin)) {
                ivUserInfoEdit.setVisibility(View.VISIBLE);
            } else {
                ivUserInfoEdit.setVisibility(View.GONE);
            }
            llUserInfoChat.setVisibility(View.VISIBLE);
            llUserInfoAttention.setVisibility(View.VISIBLE);
            llUserInfoSendGift.setVisibility(View.VISIBLE);
            llUserInfoSendDynamic.setVisibility(View.GONE);
            ivUserInfoRecording.setVisibility(View.GONE);
            tvUserInfoGiveVip.setVisibility(View.VISIBLE);
        }
    }

    void getUserInfo() {
        //String uid = MyApp.uid;
        smartRefreshUserInfo.setEnableRefresh(false);
        HttpHelper.getInstance().getUserInfo(uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(UserInfoActivity.this)) {
                    return;
                }
                smartRefreshUserInfo.setEnableRefresh(true);
                smartRefreshUserInfo.finishRefresh();

                //LogUtil.d(data);
                userInfoBean = GsonUtil.GsonToBean(data, UserInfoBean.class);
                if (userInfoBean != null) {
                    showUserInfo();
                }
            }

            @Override
            public void onFail(String msg) {
                smartRefreshUserInfo.setEnableRefresh(true);
                smartRefreshUserInfo.finishRefresh();
            }
        });
    }

    void getUserPresent() {
        HttpHelper.getInstance().getUserPresent2(uid, "1", new HttpListener() {
            @Override
            public void onSuccess(String data) {
                MyPresentData myPresentData = GsonUtil.GsonToBean(data, MyPresentData.class);
                List<MyPresentData.DataBean.GiftArrBean> giftArr = myPresentData.getData().getGiftArr();
                if (giftArr != null && giftArr.size() > 0) {
                    llUserInfoGift.setVisibility(View.VISIBLE);
                    tvUserInfoGiftNum.setText(myPresentData.getData().getAllnum() + "份");
                    UserInfoGiftRecyclerAdapter userInfoGiftRecyclerAdapter = new UserInfoGiftRecyclerAdapter(UserInfoActivity.this, giftArr);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserInfoActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rvUserInfoGift.setAdapter(userInfoGiftRecyclerAdapter);
                    rvUserInfoGift.setLayoutManager(linearLayoutManager);
                    userInfoGiftRecyclerAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                        @Override
                        public void onItemListener(int position) {
                            Intent intent;
                            if (uid.equals(MyApp.uid)) {
                                intent = new Intent(UserInfoActivity.this, MyPurseActivity.class);
                                intent.putExtra("openPresentPage", 1);
                                startActivity(intent);
                            } else {
                                intent = new Intent(UserInfoActivity.this, PresentListActivity.class);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    llUserInfoGift.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(String msg) {
                llUserInfoGift.setVisibility(View.GONE);
            }
        });
    }

    void getUserAccountInfo() {
        llUserInfoAccountInfo.setVisibility(View.GONE);
        if (MyApp.uid.equals(uid)) {
            return;
        }
        HttpHelper.getInstance().getAdminDeviceInfo(uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ViolationBean violationBean = GsonUtil.GsonToBean(data, ViolationBean.class);
                showUserAccountInfo(violationBean.getData());
                llUserInfoAccountInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void getUserBanStatus() {
        if (!"1".equals(admin)) {
            return;
        }
        HttpHelper.getInstance().getBanUserStatus(uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                AllUserStates alluserStates = GsonUtil.GsonToBean(data, AllUserStates.class);
                userBanStatusBean = alluserStates.getData();
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void showUserAccountInfo(ViolationBean.DataBean data) {
        tvUserInfoBanToDynamic.setText("禁动" + data.getDynamicstatusouttimes());
        tvUserInfoBannedToPost.setText("/ 禁言" + data.getChatstatusouttimes());
        tvUserInfoBanToInformation.setText("/ 禁资" + data.getInfostatusouttimes());
        tvUserInfoBanAccount.setText("/ 封号" + data.getStatusouttimes());
        tvUserInfoBannedEquipment.setText("/ 封设" + data.getDevicestatusouttimes());
        tvUserInfoComplain.setText("投诉" + data.getReporttimes());
        tvUserInfoBeComplained.setText("/ 被投" + data.getBereportedtimesright());
        tvUserInfoAccount.setText("/ 账号" + data.getAccountnumbercount());
        tvUserInfoLiveWarning.setText("/ 警告" + data.getAnchor_warning());
        tvUserInfoLiveKickOut.setText("/ 下播" + data.getMontior_kick());
        tvUserInfoBeLiveBan.setText("/ 禁播" + data.getAnchor_montior());
    }

    String isRecord = "";

    void showUserInfo() {
        if (SafeCheckUtil.isActivityFinish(UserInfoActivity.this)) {
            return;
        }
        UserInfoBean.DataBean userInfo = userInfoBean.getData();
        isRecord = userInfo.getIs_record();
        nickname = userInfoBean.getData().getNickname();
        if (!TextUtil.isEmpty(userInfo.getMarkname())) {
            tvUserInfoName.setText(userInfo.getMarkname());
            tvUserInfoMarkName.setVisibility(View.VISIBLE);
            tvUserInfoMarkName.setText("昵称：" + userInfo.getNickname());
        } else {
            tvUserInfoName.setText(userInfo.getNickname());
            tvUserInfoMarkName.setVisibility(View.GONE);
        }
        if ("0".equals(userInfo.getChar_rule()) && "1".equals(userInfo.getSvip())) {
            ivUserInfoChatStatus.setImageResource(R.drawable.ic_user_info_bottom_chat);
        } else {
            ivUserInfoChatStatus.setImageResource(R.drawable.ic_user_info_chat_status);
        }
        if (!admin.equals("1") && !TextUtil.isEmpty(userInfo.getFollow_list_switch()) && "1".equals(userInfo.getFollow_list_switch())) {
            tvUserInfoFollowNum.setText("※");
        } else {
            tvUserInfoFollowNum.setText(userInfo.getFollow_num());
        }
        if (!admin.equals("1") && !TextUtil.isEmpty(userInfo.getFans_list_switch()) && "1".equals(userInfo.getFans_list_switch())) {
            tvUserInfoFansNum.setText("※");
        } else {
            tvUserInfoFansNum.setText(userInfo.getFans_num());
        }
        tvUserInfoGroupNum.setText(userInfo.getGroup_num());
        if (TextUtil.isEmpty(removeSpace(userInfo.getIntroduce()))) {
            expandTextUserInfoSign.setVisibility(View.GONE);
        } else {
            expandTextUserInfoSign.setVisibility(View.GONE);
            expandTextUserInfoSign.setText(removeSpace(userInfo.getIntroduce()));
        }
        expandTextUserInfoSign.setTextColor(getResources().getColor(R.color.darkGray));
        expandTextUserInfoSign.setMoreTextColor(getResources().getColor(R.color.redOrange));
        tvUserInfoRegisterTime.setText("注册时间: " + userInfo.getReg_time());
        if (!SafeCheckUtil.isActivityFinish(UserInfoActivity.this)) {
            ImageLoader.loadCircleImage(UserInfoActivity.this, userInfo.getHead_pic(), ivUserAvatarIcon, R.mipmap.morentouxiang);
        }
        //GlideImgManager.glideLoader(this, userInfo.getHead_pic(), R.drawable.default_error, R.drawable.default_error, ivUserInfoAvatar, 0);
        if (!this.isFinishing()) {
            Glide.with(this).load(userInfo.getHead_pic()).centerCrop().into(viewUserInfoCover);
        }

        if (!TextUtil.isEmpty(userInfo.getAnchor_room_id()) && !"0".equals(userInfo.getAnchor_room_id())) {
            ivUserAvatarState.setVisibility(View.VISIBLE);
            if ("0".equals(userInfo.getAnchor_is_live())) {
                if (!SafeCheckUtil.isActivityFinish(UserInfoActivity.this)) {
                    Glide.with(UserInfoActivity.this).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
                }
            } else {
                //Glide.with(UserInfoActivity.this).load(R.drawable.ic_user_living).into(ivUserAvatarState);
                lottieUserAvatarState.setImageAssetsFolder("images");
                lottieUserAvatarState.setAnimation("user_living.json");
                lottieUserAvatarState.loop(true);
                lottieUserAvatarState.playAnimation();
                Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.live_icon_scale);
                ivUserAvatarIcon.setAnimation(mAnimation);
                mAnimation.start();
            }
        } else {
            ivUserAvatarState.setVisibility(View.INVISIBLE);
        }

        showUserRoleInfo(userInfo);
        showUserMark(userInfo);
        showUserIdentity(userInfo);
        showUserAuth(userInfo);
        showUserLiveLevel(userInfo);
        showUserRelationShip(userInfo);
        showUserImage(userInfo, false);
        showUserSign(userInfo);
        if (!"3".equals(userInfo.getBlack_val())) {
            llUserInfoInfo.setVisibility(View.GONE);
            llUserInfoBlock.setVisibility(View.VISIBLE);
            tabUserInfo.setVisibility(View.INVISIBLE);
            vpUserInfo.setVisibility(View.INVISIBLE);
            llUserInfoBottom.setVisibility(View.GONE);
            llLinkUser.setVisibility(View.GONE);
            tvUserInfoRegisterTime.setVisibility(View.INVISIBLE);
            showBlockInfo(userInfo);
            return;
        } else {
            llUserInfoInfo.setVisibility(View.VISIBLE);
            llUserInfoBlock.setVisibility(View.GONE);
            tabUserInfo.setVisibility(View.VISIBLE);
            vpUserInfo.setVisibility(View.VISIBLE);
            llUserInfoBottom.setVisibility(View.VISIBLE);
            tvUserInfoRegisterTime.setVisibility(View.VISIBLE);
            llLinkUser.setVisibility(View.VISIBLE);
        }
        getLiveChatState();
        showUserLoginState(userInfo);
        if (titles == null) {
            initViewPage(userInfo);
        }else  {
            showUserBaseInfo(userInfo);
        }
    }

    private void showUserSign(UserInfoBean.DataBean userInfo) {

        if (TextUtil.isEmpty(removeSpace(userInfo.getIntroduce()))) {
            tvUserInfoSign.setVisibility(View.GONE);
        } else {
            tvUserInfoSign.setVisibility(View.VISIBLE);
            tvUserInfoSign.setText(removeSpace(userInfo.getIntroduce()));
        }
//        tvUserInfoSign.post(new Runnable() {
//            @Override
//            public void run() {
//                LogUtil.d("行数 = " + tvUserInfoSign.getLineCount());
//                if(tvUserInfoSign.getLineCount() > 2) {
//                    tvUserInfoSign.setEllipsize(TextUtils.TruncateAt.END);
//                    tvUserInfoSignHideOrShow.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    void showUserLoginState(UserInfoBean.DataBean userInfo) {
        tvUserInfoSexAndAge.setText(userInfo.getAge());
        if ("0".equals(userInfo.getCharm_val_new())) {
            llUserInfoCharm.setVisibility(View.GONE);
        } else {
            llUserInfoCharm.setVisibility(View.VISIBLE);
            if ("1".equals(userInfo.getCharm_val_switch())) {
                tvUserInfoCharm.setText("密");
            } else {
                tvUserInfoCharm.setText(userInfo.getCharm_val_new());
            }
        }
        if ("0".equals(userInfo.getWealth_val_new())) {
            llUserInfoWealth.setVisibility(View.GONE);
        } else {
            llUserInfoWealth.setVisibility(View.VISIBLE);
            if ("1".equals(userInfo.getWealth_val_switch())) {
                tvUserInfoWealth.setText("密");
            } else {
                tvUserInfoWealth.setText(userInfo.getWealth_val_new());
            }
        }
        if ("0".equals(userInfo.getLogin_time_switch())) { //是否允许查看登录时间
            ivUserInfoLoginTime.setImageResource(R.drawable.ic_has_time);
            tvUserInfoLoginTime.setText(userInfo.getLast_login_time());
        } else {
            ivUserInfoLoginTime.setImageResource(R.drawable.ic_no_time);
            tvUserInfoLoginTime.setText("隐身");
        }
        if ("0".equals(userInfo.getLocation_city_switch())) { //是否允许查看地址
            ivUserInfoAddress.setImageResource(R.drawable.ic_has_location);
            if (userInfo.getProvince() != null) {
                if (userInfo.getProvince().equals(userInfo.getCity())) {
                    tvUserInfoAddress.setText(userInfo.getProvince());
                } else {
                    tvUserInfoAddress.setText(userInfo.getProvince() + userInfo.getCity());
                }
            }
        } else {
            ivUserInfoAddress.setImageResource(R.drawable.ic_no_location);
            tvUserInfoAddress.setText("隐身");
        }

        if ("0".equals(userInfo.getLocation_switch())) { //是否允许查看距离
            ivUserInfoDistance.setImageResource(R.drawable.ic_has_distance);
            tvUserInfoDistance.setText(userInfo.getDistance());
        } else {
            ivUserInfoDistance.setImageResource(R.drawable.ic_no_distance);
            tvUserInfoDistance.setText("隐身");
        }

        if (userInfo.getOnlinestate() == 1) {
            ivUserAvatarOnline.setVisibility(View.VISIBLE);
        } else {
            ivUserAvatarOnline.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(userInfo.getMedia())) {
            llUserInfoVoice.setVisibility(View.VISIBLE);
            tvUserInfoVoice.setText(userInfo.getMediaalong() + "s");
            ivUserInfoVoice.setImageResource(R.drawable.user_info_play);
        } else {
            llUserInfoVoice.setVisibility(View.GONE);
        }

    }

    void showUserRoleInfo(UserInfoBean.DataBean userInfo) {
        if ("1".equals(userInfo.getSex())) { //男
            viewUserInfoMask.setBackgroundResource(R.drawable.bg_user_info_mask_boy);
            Drawable drawable = getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvUserInfoSexAndAge.setCompoundDrawables(drawable, null, null, null);
            tvUserInfoSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            llUserInfoGift.setBackgroundResource(R.drawable.bg_user_info_gift_boy);
            tvUserInfoHistoryName.setBackgroundResource(R.drawable.bg_user_info_gift_boy);
            tvUserInfoHistoryName.setTextColor(getResources().getColor(R.color.boyColor));
            collapsingToolbarLayoutUserInfo.setContentScrim(getResources().getDrawable(R.drawable.bg_user_info_mask_boy));
        } else if ("2".equals(userInfo.getSex())) { //女
            viewUserInfoMask.setBackgroundResource(R.drawable.bg_user_info_mask_girl);
            Drawable drawable = getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvUserInfoSexAndAge.setCompoundDrawables(drawable, null, null, null);
            tvUserInfoSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            llUserInfoGift.setBackgroundResource(R.drawable.bg_user_info_gift_girl);
            tvUserInfoHistoryName.setBackgroundResource(R.drawable.bg_user_info_gift_girl);
            tvUserInfoHistoryName.setTextColor(getResources().getColor(R.color.girlColor));
            collapsingToolbarLayoutUserInfo.setContentScrim(getResources().getDrawable(R.drawable.bg_user_info_mask_girl));
        } else { //其他
            viewUserInfoMask.setBackgroundResource(R.drawable.bg_user_info_mask_cdts);
            Drawable drawable = getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvUserInfoSexAndAge.setCompoundDrawables(drawable, null, null, null);
            tvUserInfoSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            llUserInfoGift.setBackgroundResource(R.drawable.bg_user_info_gift_cdts);
            tvUserInfoHistoryName.setBackgroundResource(R.drawable.bg_user_info_gift_cdts);
            tvUserInfoHistoryName.setTextColor(getResources().getColor(R.color.cdtColor));
            collapsingToolbarLayoutUserInfo.setContentScrim(getResources().getDrawable(R.drawable.bg_user_info_mask_cdts));
        }
        if ("S".equals(userInfo.getRole())) {
            tvUserInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            tvUserInfoRole.setText("斯");
        } else if ("M".equals(userInfo.getRole())) {
            tvUserInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            tvUserInfoRole.setText("慕");
        } else if ("SM".equals(userInfo.getRole())) {
            tvUserInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            tvUserInfoRole.setText("双");
        } else {
            tvUserInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
            tvUserInfoRole.setText(userInfo.getRole());
        }

    }

    void showUserMark(UserInfoBean.DataBean userInfo) {
        llUserInfoMark.setVisibility(View.GONE);
        llUserInfoDesc.setVisibility(View.GONE);
        llUserInfoFGroups.setVisibility(View.GONE);
        llUserInfoAdminMark.setVisibility(View.GONE);
        if (!TextUtil.isEmpty(userInfo.getFgroups())) {
            llUserInfoMark.setVisibility(View.VISIBLE);
            llUserInfoFGroups.setVisibility(View.VISIBLE);
            tvUserInfoFGroups.setText(userInfo.getFgroups());
        }
        if (!TextUtil.isEmpty(userInfo.getLmarkname())) {
            llUserInfoMark.setVisibility(View.VISIBLE);
            llUserInfoDesc.setVisibility(View.VISIBLE);
            tvUserInfoDesc.setText(userInfo.getLmarkname());
        }
        if (userInfo.getLsypic() != null && userInfo.getLsypic().size() > 0) {
            llUserInfoMark.setVisibility(View.VISIBLE);
            llUserInfoDesc.setVisibility(View.VISIBLE);
            UserInfoAdminMaskRecyclerAdapter descMarkAdapter = new UserInfoAdminMaskRecyclerAdapter(userInfo.getLsypic(), UserInfoActivity.this, 0);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
            rvUserInfoDesc.setLayoutManager(gridLayoutManager);
            rvUserInfoDesc.setAdapter(descMarkAdapter);
            final ArrayList<String> lsypic = userInfo.getLsypic();
            descMarkAdapter.setOnItemClickLitener(new UserInfoAdminMaskRecyclerAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    List<ImageInfo> imageInfo = new ArrayList<>();
                    for (int i = 0; i < lsypic.size(); i++) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(lsypic.get(i));
                        info.setBigImageUrl(lsypic.get(i));
                        imageInfo.add(info);
                    }
                    Intent intent = new Intent(UserInfoActivity.this, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        if ("1".equals(admin) && (!TextUtil.isEmpty(userInfo.getAdmin_mark()) || (userInfo.getSypic() != null && userInfo.getSypic().size() > 0))) {
            llUserInfoAdminMark.setVisibility(View.VISIBLE);
            tvUserInfoAdminMark.setVisibility(View.VISIBLE);
            tvUserInfoAdminMark.setText(userInfo.getAdmin_mark());
            llUserInfoMark.setVisibility(View.VISIBLE);
            if (userInfo.getSypic() != null && userInfo.getSypic().size() > 0) {
                rvUserInfoAdminMark.setVisibility(View.VISIBLE);
                UserInfoAdminMaskRecyclerAdapter adminMarkAdapter = new UserInfoAdminMaskRecyclerAdapter(userInfo.getSypic(), UserInfoActivity.this, 0);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
                rvUserInfoAdminMark.setAdapter(adminMarkAdapter);
                rvUserInfoAdminMark.setLayoutManager(gridLayoutManager);
                final ArrayList<String> sypic = userInfo.getSypic();
                adminMarkAdapter.setOnItemClickLitener(new UserInfoAdminMaskRecyclerAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        List<ImageInfo> imageInfo = new ArrayList<>();
                        for (int i = 0; i < sypic.size(); i++) {
                            ImageInfo info = new ImageInfo();
                            info.setThumbnailUrl(sypic.get(i));
                            info.setBigImageUrl(sypic.get(i));
                            imageInfo.add(info);
                        }
                        Intent intent = new Intent(UserInfoActivity.this, ImagePreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } else {
                rvUserInfoAdminMark.setVisibility(View.GONE);
            }
        }
        if ("1".equals(userInfo.getSex())) { //男
            llUserInfoMark.setBackgroundResource(R.drawable.bg_user_info_mask_boy);
        } else if ("2".equals(userInfo.getSex())) { //女
            llUserInfoMark.setBackgroundResource(R.drawable.bg_user_info_mask_girl);
        } else { //其他
            llUserInfoMark.setBackgroundResource(R.drawable.bg_user_info_mask_cdts);
        }

    }

    void showUserIdentity(final UserInfoBean.DataBean userInfo) {
        ivUserAvatarLevel.setVisibility(View.VISIBLE);
        if ("1".equals(userInfo.getIs_admin())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_manager);
        } else if ("1".equals(userInfo.getSvipannual())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_svip_year);
        } else if ("1".equals(userInfo.getSvip())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_svip);
        } else if ("1".equals(userInfo.getVipannual())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_vip_year);
        } else if ("1".equals(userInfo.getVip())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_vip);
        } else {
            ivUserAvatarLevel.setImageResource(R.drawable.user_normal);
        }
        if ("1".equals(userInfo.getIs_likeliar())) {
            tvUserInfoLikeLiarTip.setVisibility(View.VISIBLE);
            if (MyApp.uid.equals(uid)) {
                tvUserInfoLikeLiarTip.setText("可疑用户，【自拍认证】后消失");
                tvUserInfoLikeLiarTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInfoActivity.this, PhotoRzActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                tvUserInfoLikeLiarTip.setText("请注意，该用户为可疑用户");
                tvUserInfoLikeLiarTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLikeLiarTip(userInfo.getLikeliar_info());
                    }
                });
            }
        } else {
            tvUserInfoLikeLiarTip.setVisibility(View.GONE);
        }
    }

    void showLikeLiarTip(String tips) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop.Builder(UserInfoActivity.this)
                .setTitle("可疑用户提醒")
                .setInfo(tips)
                .setCancelStr("")
                .setConfirmStr("好的")
                .build();
        normalTipsPop.setOutSideTouchable(false);
        normalTipsPop.update();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {

            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
            }
        });
        normalTipsPop.showPopupWindow();
    }

    void showUserAuth(UserInfoBean.DataBean userInfo) {
        if ("1".equals(userInfo.getRealname())) {
            ivUserInfoPhotoAuth.setVisibility(View.VISIBLE);
            ivUserInfoAuthPhoto.setVisibility(View.VISIBLE);
            if ("1".equals(userInfo.getRealpicstate())) {
                ivUserInfoAuthPhoto.setImageResource(R.drawable.ic_auth_photo_vip);
            } else {
                ivUserInfoAuthPhoto.setImageResource(R.drawable.ic_auth_photo_closed);
            }
        } else {
            ivUserInfoPhotoAuth.setVisibility(View.GONE);
            ivUserInfoAuthPhoto.setVisibility(View.GONE);
        }
        if ("1".equals(userInfo.getRealids())) {
            if ("1".equals(admin)){
                ivUserInfoIdCardPhoto.setVisibility(View.VISIBLE);
            }else {
                ivUserInfoIdCardPhoto.setVisibility(View.INVISIBLE);
            }

            ivUserInfoIdCardAuth.setVisibility(View.VISIBLE);

        } else {
            ivUserInfoIdCardAuth.setVisibility(View.GONE);
        }

        if ("1".equals(userInfo.getVideo_auth_status())) {
            ivUserInfoVideoAuth.setVisibility(View.VISIBLE);
        } else {
            ivUserInfoVideoAuth.setVisibility(View.GONE);
        }

        ivUserInfoPhotoAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, PhotoRzActivity.class);
                startActivity(intent);
            }
        });

        ivUserInfoIdCardAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, PhotoRzActivity.class);
                intent.putExtra("xuan", 2);
                startActivity(intent);
            }
        });

    }

    void showUserLiveLevel(UserInfoBean.DataBean userInfo) {
        if (TextUtil.isEmpty(userInfo.getUser_level()) || "0".equals(userInfo.getUser_level())) {
            constraintLayoutAudienceLevel.setVisibility(View.GONE);
        } else {
            constraintLayoutAudienceLevel.setVisibility(View.VISIBLE);
            tvLiveAudienceLevel.setText(userInfo.getUser_level());
        }
        if (TextUtil.isEmpty(userInfo.getAnchor_level()) || "0".equals(userInfo.getAnchor_level())) {
            constraintLayoutAnchorLevel.setVisibility(View.GONE);
        } else {
            constraintLayoutAnchorLevel.setVisibility(View.VISIBLE);
            tvLiveAnchorLevel.setText(userInfo.getAnchor_level());
        }
        constraintLayoutAudienceLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MyLiveLevelActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });

        constraintLayoutAnchorLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MyLiveLevelActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
    }

    void showUserRelationShip(UserInfoBean.DataBean userInfo) {
        int followState = userInfo.getFollow_state();
        if (followState == 1) {
            tvUserInfoAttention.setText("已关注");
        } else if (followState == 2) {
            tvUserInfoAttention.setText("关注");
        } else if (followState == 3) {
            tvUserInfoAttention.setText("互为好友");
        } else if (followState == 4) {
            tvUserInfoAttention.setText("被关注");
        }
    }

    void showUserImage(final UserInfoBean.DataBean userInfo, boolean checkPassWord) {
        final List<String> photoList = userInfo.getPhoto();
        if (photoList == null || photoList.size() <= 0) {
            rvUserInfoPhoto.setVisibility(View.GONE);
            return;
        }
        rvUserInfoPhoto.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        boolean isNeedPassword = true;
        if (checkPassWord || userInfo.getUid().equals(MyApp.uid)) { //输入过密码的 和 自己不需要判断
            photoAdapter = new RecyclerViewAdapter(photoList, UserInfoActivity.this, 0);
            isNeedPassword = false;
        } else {
            //判断有没有密码
            if (userInfo.getPhoto_lock().equals("1")) {               //1是以开放无密码    2是未开放
                if (userInfo.getPhoto_rule().equals("0")) {   //判断相册查看权限是否开启  0开启
                    photoAdapter = new RecyclerViewAdapter(photoList, UserInfoActivity.this, 0);
                    isNeedPassword = false;
                } else {
                    //判断相册权限
                    if ("1".equals(vip) || "1".equals(svip) || userInfo.getFollow_state() == 3) {//会员好友也清晰
                        photoAdapter = new RecyclerViewAdapter(photoList, UserInfoActivity.this, 0);
                        isNeedPassword = false;
                    } else {//否则就不清晰
                        photoAdapter = new RecyclerViewAdapter(photoList, UserInfoActivity.this, 1);
                        isNeedPassword = true;
                    }
                }
            } else {
                photoAdapter = new RecyclerViewAdapter(photoList, UserInfoActivity.this, 1);
                isNeedPassword = true;
            }
        }
        rvUserInfoPhoto.setLayoutManager(linearLayoutManager);
        rvUserInfoPhoto.setAdapter(photoAdapter);
        final boolean finalIsNeedPassword = isNeedPassword;
        photoAdapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (finalIsNeedPassword) {
                    if (userInfo.getPhoto_lock().equals("1")) {              //1是以开放无密码    2是未开放
                        seePhotoVip("TA的相册限互为好友、VIP会员可见~");
                    } else {
                        showInputPwdDialog();
                    }
                } else {
                    List<ImageInfo> imageInfo = new ArrayList<>();
                    for (int i = 0; i < photoList.size(); i++) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(photoList.get(i));
                        info.setBigImageUrl(photoList.get(i));
                        imageInfo.add(info);
                    }
                    Intent intent = new Intent(UserInfoActivity.this, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    void setListener() {
        initAlertListener();
        initMarkListener();
        setUserAccountListener();
        ivUserAvatarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                if ("1".equals(userInfoBean.getData().getAnchor_is_live())) {
                    RoomManager.enterLiveRoom(UserInfoActivity.this, userInfoBean.getData().getUid(), Integer.parseInt(userInfoBean.getData().getAnchor_room_id()));
                } else {
                    ImageViewUtil.previewImage(UserInfoActivity.this, userInfoBean.getData().getHead_pic());
                }
            }
        });
        ivUserInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UserInfoActivity.this, EditPersonMsgActivity.class);
                getEditPermission();
            }
        });
//        appBarUserInfo.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() - 100) {
//                    if (tvToolBarTitle.getVisibility() == View.INVISIBLE) {
//                        tvToolBarTitle.setText(tvUserInfoName.getText());
//                        tvToolBarTitle.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (tvToolBarTitle.getVisibility() == View.VISIBLE) {
//                        tvToolBarTitle.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        });
        View.OnClickListener onDistanceListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(MyApp.lat);
                if (TextUtil.isEmpty(MyApp.lat)) {
                    checkLocationPermisstion();
                }
            }
        };

        tvUserInfoAddress.setOnClickListener(onDistanceListener);
        tvUserInfoDistance.setOnClickListener(onDistanceListener);


        appBarUserInfo.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            int oldY = 0;

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    if (tvToolBarTitle.getVisibility() == View.VISIBLE) {
                        tvToolBarTitle.setVisibility(View.INVISIBLE);
                    }
                } else if (state == State.COLLAPSED) {
                    if (tvToolBarTitle.getVisibility() == View.INVISIBLE) {
                        tvToolBarTitle.setText(tvUserInfoName.getText());
                        tvToolBarTitle.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onStateOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(oldY - i) > 10) {
                    if (oldY - i < 0) {
                        showOrHideTopBottomBar(true);
                    } else {
                        showOrHideTopBottomBar(false);
                    }
                }
                oldY = i;
            }

        });

        ivUserInfoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvUserInfoHistoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeHistoryNickName();
            }
        });
        View.OnClickListener followNumClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionToWatchList(tvUserInfoFollowNum)) {
                    UserRelationShipActivity.Companion.start(UserInfoActivity.this, uid, 0);
                } else {
                    ToastUtil.show(UserInfoActivity.this, "该用户未开放查看权限");
                }
            }
        };
        tvUserInfoFollowNum.setOnClickListener(followNumClick);
        tvUserInfoFollowNumTxt.setOnClickListener(followNumClick);
        View.OnClickListener fansNumClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionToWatchList(tvUserInfoFansNum)) {
                    UserRelationShipActivity.Companion.start(UserInfoActivity.this, uid, 1);
                } else {
                    ToastUtil.show(UserInfoActivity.this, "该用户未开放查看权限");
                }
            }
        };
        tvUserInfoFansNum.setOnClickListener(fansNumClick);
        tvUserInfoFansNumTxt.setOnClickListener(fansNumClick);
        View.OnClickListener groupNumClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.equals(MyApp.uid)) {
//                    Intent intent = new Intent(UserInfoActivity.this, GroupSquareActivity.class);
//                    intent.putExtra("groupFlag", 1);
//                    startActivity(intent);
                    GroupSquareActivity.Companion.startActivity(UserInfoActivity.this, 1);
                } else {
//                    Intent intent = new Intent(UserInfoActivity.this, OtherGroupActivity.class);
//                    intent.putExtra("otherUid", uid);
//                    startActivity(intent);
                    UserGroupListActivity.Companion.start(UserInfoActivity.this, uid);
                }
            }
        };
        tvUserInfoGroupNum.setOnClickListener(groupNumClick);
        tvUserInfoGroupNumTxt.setOnClickListener(groupNumClick);
        llUserInfoSendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      showSendGiftPop();
//                DashangDialogNew dashangDialogNew = new DashangDialogNew(UserInfoActivity.this, uid);
//                dashangDialogNew.setOnSimpleItemListener(new OnSimpleItemListener() {
//                    @Override
//                    public void onItemListener(int position) {
//                        getUserPresent();
//                    }
//                });

                GiftPanelPop2 giftPanelPop = new GiftPanelPop2(UserInfoActivity.this, 2, uid);
                giftPanelPop.showPopupWindow();
                giftPanelPop.setOnGiftSendSucListener(new GiftPanelPop2.OnGiftSendSucListener() {
                    @Override
                    public void onGiftSendSuc(@NotNull String orderId, int giftReward) {
                        //ToastUtil.show(UserInfoActivity.this, "赠送成功");
                        getUserPresent();
                    }
                });

            }
        });
        llUserInfoAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                llUserInfoAttention.setEnabled(false);
                int followState = userInfoBean.getData().getFollow_state();
                if (followState == 1) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserInfoActivity.this);
                    builder.setMessage("您已经关注此人,确认取消关注吗?")
                            .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    llUserInfoAttention.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            overfollow();
                        }
                    }).create().show();
                } else if (followState == 2) {
                    follow();
                } else if (followState == 3) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserInfoActivity.this);
                    builder.setMessage("您和Ta互为好友,确认取消关注吗?")
                            .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    llUserInfoAttention.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            overfollow();
                        }
                    }).create().show();
                } else if (followState == 4) {
                    follow();
                }
            }
        });
        llUserInfoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否禁言
                String isSpeak = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "nospeak", "1");
                if (isSpeak.equals("0")) {
                    ToastUtil.show(getApplicationContext(), "您因违规被系统禁用聊天功能，如有疑问请与客服联系！");
                } else {
                    isOpenChat();
                }
            }
        });
        ivUserInfoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                if (uid.equals(MyApp.uid)) {
                    // showMineNormalMenu();
                    showMineNormalMenuPop();
                } else {
                    if ("1".equals(admin)) {
                        //showAdminNormalMenu();
                        showAdminNormalMenuPop();
                    } else {
                        //showUserNormalMenu();
                        showUserNormalMenuPop();
                    }
                }
            }
        });
        llUserInfoVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                if (!TextUtil.isEmpty(userInfoBean.getData().getMedia()) && !isPlaying) {
                    playVoice();
                }
            }
        });
        ivUserInfoRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, RecorderActivity.class);
                startActivity(intent);
            }
        });

        smartRefreshUserInfo.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshData();
            }
        });
        ivUserAvatarLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                //Intent intent = new Intent(UserInfoActivity.this, VipCenterActivity.class);
                Intent intent = new Intent(UserInfoActivity.this, VipMemberCenterActivity.class);
                intent.putExtra("headpic", userInfoBean.getData().getHead_pic());
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        tvUserInfoSignHideOrShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvUserInfoSignHideOrShow.getText().toString().equals("查看更多")) {
                    tvUserInfoSign.setMaxLines(100);
                    tvUserInfoSignHideOrShow.setText("收起");
                } else {
                    tvUserInfoSign.setMaxLines(2);
                    tvUserInfoSignHideOrShow.setText("查看更多");
                }
            }
        });
        tvUserInfoBlockReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });

        tvUserInfoGiveVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                Intent intent = new Intent(UserInfoActivity.this, VipMemberCenterActivity.class);
                intent.putExtra("headpic", userInfoBean.getData().getHead_pic());
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        llUserInfoSendDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSendDynamicPermission();
            }
        });

//        vpUserInfo.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent ev) {
//                //v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        vpUserInfo.setOnLeftFillingListener(new DisallowViewPager.OnLeftFillingListener() {
            @Override
            public void onLeftFilling() {
                if (vpUserInfo.getCurrentItem() == 0) {
                    finish();
                }
            }
        });
//
        coordinatorUserInfo.setOnTouchListener(new View.OnTouchListener() {
            int mx, my;
            int lastx, lasty;

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                //获取坐标点：
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (x > 50) {
                    return UserInfoActivity.super.onTouchEvent(ev);
                }
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deletx = x - mx;
                        int delety = y - my;
                        if (Math.abs(deletx) > Math.abs(delety)) {
//                            if (onLeftFillingListener != null) {
//                                onLeftFillingListener.onLeftFilling();
//                            }
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
                return false;
            }
        });

//        coordinatorUserInfo.setOnLeftFillingListener(new DisallowCoorinatorLayout.OnLeftFillingListener() {
//            @Override
//            public void onLeftFilling() {
//                finish();
//            }
//        });

        ivUserInfoAuthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(admin)) {
                    getUserAuthPhoto();
                } else {
                    if ("1".equals(userInfoBean.getData().getRealpicstate())) {
                        if ("1".equals(vip)) {
                            if (userInfoBean == null || userInfoBean.getData() == null) {
                                return;
                            }
                            getUserAuthPhoto();
                        } else {
                            ToastUtil.show(UserInfoActivity.this, "用户认证照设置为仅VIP可见");
                        }
                    } else {
                        ToastUtil.show(UserInfoActivity.this, "用户未公开认证照");
                    }
                }
            }
        });


        ivUserInfoIdCardPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserIdCardPhoto();
            }
        });


    }

    void setUserAccountListener() {
        tvUserInfoBanToDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGetLineActivity(2);
            }
        });
        tvUserInfoBannedToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGetLineActivity(3);
            }
        });
        tvUserInfoBanToInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGetLineActivity(4);
            }
        });
        tvUserInfoBanAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGetLineActivity(1);
            }
        });
        tvUserInfoBannedEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGetLineActivity(5);
            }
        });
        tvUserInfoComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(UserInfoActivity.this, ComplaintInformationActivity.class);
                Intent intent = new Intent(UserInfoActivity.this, ComplaintInfoActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name", nickname);
                startActivity(intent);
            }
        });
        tvUserInfoBeComplained.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(UserInfoActivity.this, ComplaintInformationActivity.class);
                Intent intent = new Intent(UserInfoActivity.this, ComplaintInfoActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("uid", uid);
                intent.putExtra("user_name", nickname);
                startActivity(intent);
            }
        });
        tvUserInfoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, AccountActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user_name", nickname);
                startActivity(intent);
            }
        });
        tvUserInfoLiveWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, LiveSealDetailActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
        tvUserInfoBeLiveBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, LiveSealDetailActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });
        tvUserInfoLiveKickOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, LiveSealDetailActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "3");
                startActivity(intent);
            }
        });


    }

    void gotoGetLineActivity(int type) {
        Intent intent = new Intent(this, GetOutOfLineActivity.class);
        intent.putExtra("type", String.valueOf(type));
        intent.putExtra("uid", uid);
        intent.putExtra("user_name", nickname);
        startActivity(intent);
    }

    void showUserBaseInfo(UserInfoBean.DataBean userInfo) {
        userInfoFragment.showData(userInfo);
        userDynamicFragment.showData(userInfo);
        userCommentFragment.showData(userInfo);
        if (userLiveHistoryFragment != null) {
            userLiveHistoryFragment.showData(userInfo);
        }
    }

    private void showInputPwdDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
        final EditText etPsw = (EditText) window.findViewById(R.id.item_photo_inpsw_edittext);
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
                    alertDialog.dismiss();
                }
            }
        });
        TextView tvCancel = (TextView) window.findViewById(R.id.item_photo_inpsw_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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
                //Intent intent = new Intent(UserInfoActivity.this, VipCenterActivity.class);
                Intent intent = new Intent(UserInfoActivity.this, VipMemberCenterActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("headpic", headpic);
                startActivity(intent);
                alertDialog.dismiss();
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
                if (userInfoBean == null || userInfoBean.getData() == null) {
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            showUserImage(userInfoBean.getData(), true);
                            break;
                        case 4001:
                            ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //   alertDialog.dismiss();

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void follow() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            tvUserInfoFansNum.setText(Integer.parseInt(userInfoBean.getData().getFans_num()) + 1 + "");
                            userInfoBean.getData().setFans_num(tvUserInfoFansNum.getText().toString());
                            String followFlag = obj.getString("data");
                            if (followFlag.equals("1")) {
                                tvUserInfoAttention.setText("互为好友");
                                userInfoBean.getData().setFollow_state(3);
                                //followState = "3";
                                EventBus.getDefault().post(new FollowEvent(3, position));
                                //visibleIntro();
                            } else {
                                tvUserInfoAttention.setText("已关注");
                                userInfoBean.getData().setFollow_state(1);
                                //followState = "1";
                                EventBus.getDefault().post(new FollowEvent(1, position));
                            }
                            showUserBaseInfo(userInfoBean.getData());
                            ToastUtil.showLong(getApplicationContext(), obj.getString("msg"));
                            break;
                        case 4002:
                        case 8881:
                        case 8882:
                            BindGuanzhuDialog.bindAlertDialog(UserInfoActivity.this, obj.getString("msg"));
                            break;
                        case 4787:
                            ToastUtil.show(getApplicationContext(), "您已经关注了对方~");
                            break;
                        case 5000:
                            ToastUtil.show(getApplicationContext(), obj.getString("msg") + "");
                            break;

                    }
                    llUserInfoAttention.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("guanzhu_failure", throwable.getMessage());
            }
        });
    }

    public void overfollow() {
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
                            tvUserInfoFansNum.setText(Integer.parseInt(userInfoBean.getData().getFans_num()) - 1 + "");
                            userInfoBean.getData().setFans_num(tvUserInfoFansNum.getText().toString());
                            if (obj.getString("data").equals("0")) {
                                tvUserInfoAttention.setText("关注");
                                userInfoBean.getData().setFollow_state(2);
                                EventBus.getDefault().post(new FollowEvent(2, position));
                            } else if (obj.getString("data").equals("1")) {
                                tvUserInfoAttention.setText("被关注");
                                userInfoBean.getData().setFollow_state(4);
                                EventBus.getDefault().post(new FollowEvent(4, position));
                            }
                            showUserBaseInfo(userInfoBean.getData());
                            ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                            break;
                        case 4000:
                        case 4001:
                            ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                            break;
                    }
                    llUserInfoAttention.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void isOpenChat() {//打开聊天的方法
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("otheruid", uid);
        final IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetOpenChatRestrictAndInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    int chatRetcode = object.getInt("retcode");
                    switch (chatRetcode) {
                        //以使用过邮票可以打开会话
                        case 2000:
                            //互相关注打开会话
                        case 2001:
                            toChatActivity(response);
                            break;
                        case 3001://邮票弹窗
                            if (userInfoBean == null || userInfoBean.getData() == null) {
                                return;
                            }
                            DialogStampData data = new Gson().fromJson(response, DialogStampData.class);
                            DialogStampData.DataBean datas = data.getData();
                            StampDialogNew stampDialogNew = new StampDialogNew(UserInfoActivity.this, uid, userInfoBean.getData().getNickname(), datas.getWallet_stamp(), datas.getBasicstampX() + "", datas.getBasicstampY() + "", datas.getBasicstampZ() + "", datas.getSex());
                            //StampDialogNew stampDialogNew = new StampDialogNew(UserInfoActivity.this, uid, userInfoBean.getData().getNickname(), "0", datas.getBasicstampX() + "", datas.getBasicstampY() + "", datas.getBasicstampZ() + "", datas.getSex());
                            stampDialogNew.setOnSimpleItemListener(new OnSimpleItemListener() {
                                @Override
                                public void onItemListener(int position) {
                                    isOpenChat();
                                }
                            });
                            break;
                        case 4005:
                            ToastUtil.show(getApplicationContext(), object.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("TAG", throwable.toString());
            }

        });
    }

    private void toChatActivity(String userData) {
        if (userInfoBean == null) {
            return;
        }
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(userInfoBean.getData().getUid());
        chatInfo.setChatName(userInfoBean.getData().getNickname());
        Intent intent = new Intent(MyApp.getInstance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.putExtra("userInfo", userData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.instance().startActivity(intent);
    }

    MediaPlayer player;
    boolean isPlaying;

    //播放音频
    private void playVoice() {
        player = new MediaPlayer();
        player.reset();
        try {
            player.setDataSource(userInfoBean.getData().getMedia());
            player.prepare();
            player.start();
            isPlaying = true;
            ivUserInfoVoice.setImageResource(R.drawable.user_info_pause);
            tvUserInfoVoice.setText("播放中");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                ivUserInfoVoice.setImageResource(R.drawable.user_info_play);
                tvUserInfoVoice.setText(userInfoBean.getData().getMediaalong() + "s");
            }
        });
    }

    private void initAlertListener() {
        alertItemListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                if (data.equals("分享")) {
                    showShareWay();
                } else if (data.equals("举报")) {
                    report();
                } else if (data.contains("悄悄关注")) {
                    attentionByQuiet();
                } else if (data.contains("分组")) {
                    subGroup();
                } else if (data.contains("历史昵称")) {
                    seeHistoryNickName();
                } else if (data.contains("昵称备注")) {
                    setRemark();
                } else if (data.contains("描述")) {
                    setDesc();
                } else if (data.contains("拉黑")) {
                    editBlock();
                } else if (data.contains("可疑")) {
                    editLikeLiar();
                } else if (data.contains("头像")) {
                    delIllegallyUserInfo(1);
                } else if (data.contains("相册")) {
                    delIllegallyUserInfo(4);
                } else if (data.contains("昵称")) {
                    delIllegallyUserInfo(2);
                } else if (data.contains("签名")) {
                    delIllegallyUserInfo(3);
                } else if (data.contains("加群")) {
                    editJoinGroup();
                } else if (data.contains("管理备注")) {
                    adminMark();
                } else if (data.contains("权限")) {
                    ivUserInfoReturn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showAdminOperateMenu();
                        }
                    }, 300);
                }
            }
        };
        adminItemListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                //入参method 1-账号 2-动态 3-资料 4-聊天 forbid-封禁设备
                if (data.contains("账号")) {
                    if (!"1".equals(userBanStatusBean.getStatus())) {
                        removeBanUserStatus("1");
                    } else {
                        ivUserInfoReturn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDaysMenu("1");
                            }
                        }, 300);
                    }
                } else if (data.contains("动态")) {
                    if (!"1".equals(userBanStatusBean.getDynamicstatus())) {
                        removeBanUserStatus("2");
                    } else {
                        ivUserInfoReturn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDaysMenu("2");
                            }
                        }, 300);
                    }
                } else if (data.contains("资料")) {
                    if (!"1".equals(userBanStatusBean.getInfostatus())) {
                        removeBanUserStatus("3");
                    } else {
                        ivUserInfoReturn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDaysMenu("3");
                            }
                        }, 300);
                    }
                } else if (data.contains("消息")) {
                    if (!"1".equals(userBanStatusBean.getChatstatus())) {
                        removeBanUserStatus("4");
                    } else {
                        ivUserInfoReturn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDaysMenu("4");
                            }
                        }, 300);
                    }
                } else if (data.contains("设备")) {
                    if (!"1".equals(userBanStatusBean.getDevicestatus())) {
                        removeBanUserStatus("resume");
                    } else {
                        ivUserInfoReturn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDaysMenu("forbid");
                                //punishment("forbid", "");
                            }
                        }, 300);
                    }
                }
            }
        };
    }

    private void initMarkListener() {
        tvUserInfoBlockCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBlock();
            }
        });
        tvUserInfoDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDesc();
            }
        });
        llUserInfoFGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subGroup();
            }
        });
        llUserInfoAdminMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminMark();
            }
        });
    }

    private void showUserNormalMenu() {
        String shareStr = "分享";
        String attentionStr = "悄悄关注(svip)";
        String groupStr = "设置分组(svip)";
        String historyNameStr = "历史昵称(svip)";
        String remarkNameStr = "昵称备注(好友/vip)";
        String descStr = "详细描述(好友/svip)";
        String reportStr = "举报";
        String blockStr = "拉黑";
        if ("1".equals(userInfoBean.getData().getFriend_quiet())) { //是否悄悄关注
            attentionStr = "取消悄悄关注";
        }
        if ("2".equals(userInfoBean.getData().getBlack_val()) || "4".equals(userInfoBean.getData().getBlack_val())) {
            blockStr = "取消拉黑";
        }
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{shareStr, attentionStr, groupStr, historyNameStr, remarkNameStr, descStr, reportStr, blockStr},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(alertItemListener);
        alertView.show();
    }

    private void showMineNormalMenu() {
        String shareStr = "分享";
        String historyNameStr = "历史昵称(svip)";
        String descStr = "详细描述(好友/svip)";
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{shareStr, historyNameStr, descStr},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(alertItemListener);
        alertView.show();
    }

    private void showAdminNormalMenu() {
        String suspiciousStr = "可疑用户";
        String violationIconStr = "违规头像";
        String violationPhotoStr = "违规相册";
        String violationNicknameStr = "违规昵称";
        String violationSignStr = "违规签名";
        String groupOperateStr = "禁止加群";
        String markManagerStr = "管理备注";
        String adminStr = "★管理员权限";
        String attentionStr = "悄悄关注(svip)";
        String groupStr = "设置分组(svip)";
        String historyNameStr = "历史昵称(svip)";
        String remarkNameStr = "昵称备注(好友/vip)";
        String descStr = "详细描述(好友/svip)";
        String shareStr = "分享";
        String reportStr = "举报";
        String blockStr = "拉黑";
        if ("1".equals(userInfoBean.getData().getFriend_quiet())) { //是否悄悄关注
            attentionStr = "取消悄悄关注";
        }
        if ("2".equals(userInfoBean.getData().getBlack_val()) || "4".equals(userInfoBean.getData().getBlack_val())) {
            blockStr = "取消拉黑";
        }
        if ("1".equals(userInfoBean.getData().getJoin_group_status())) {
            groupOperateStr = "启用加群";
        }
        if ("1".equals(userInfoBean.getData().getIs_likeliar())) {
            suspiciousStr = "取消可疑用户";
        }
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{suspiciousStr, violationIconStr, violationPhotoStr, violationNicknameStr, violationSignStr,
                        groupOperateStr, markManagerStr, adminStr,
                        attentionStr, groupStr, historyNameStr, remarkNameStr, descStr, shareStr, reportStr, blockStr},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(alertItemListener);
        alertView.show();
    }

    private void showAdminOperateMenu() {
        String dynamicStr = "封禁动态";
        String messageStr = "封禁消息";
        String dataStr = "封禁资料";
        String accountStr = "封禁账号";
        String equipmentStr = "封禁设备";

        if (userBanStatusBean != null) {
            if (!"1".equals(userBanStatusBean.getStatus())) {
                accountStr = "启用账号";
            }
            if (!"1".equals(userBanStatusBean.getDynamicstatus())) {
                dynamicStr = "启动动态";
            }
            if (!"1".equals(userBanStatusBean.getChatstatus())) {
                messageStr = "启动消息";
            }
            if (!"1".equals(userBanStatusBean.getInfostatus())) {
                dataStr = "启动资料";
            }
            if (!"1".equals(userBanStatusBean.getDevicestatus())) {
                equipmentStr = "启动设备";
            }
            AlertView alertView = new AlertView(null, null, "取消", null,
                    new String[]{dynamicStr, messageStr, dataStr, accountStr, equipmentStr},
                    this, AlertView.Style.ActionSheet, null);
            alertView.setOnItemClickListener(adminItemListener);
            alertView.show();
        }
    }


    private void showDaysMenu(final String method) {
        final String day01 = "1天";
        final String day03 = "3天";
        final String day07 = "1周";
        final String day14 = "2周";
        final String day30 = "1月";
        final String day__ = "永久";
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{"1天", "3天", "1周", "2周", "1月", "永久"},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                switch (data) {
                    case day01:
                        punishment(method, "1");
                        break;
                    case day03:
                        punishment(method, "3");
                        break;
                    case day07:
                        punishment(method, "7");
                        break;
                    case day14:
                        punishment(method, "14");
                        break;
                    case day30:
                        punishment(method, "30");
                        break;
                    case day__:
                        punishment(method, "0");
                        break;
                }
            }
        });
        alertView.show();
    }

    //分享
    private void showShareWay() {

        NormalShareBean normalShareBean = new NormalShareBean(2, uid,
                "同好推荐",
                nickname,
                "向你推荐一位同好:" + nickname + "，快来圣魔关注Ta~",
                HttpUrl.NetPic() + HttpUrl.ShareUserDetail + uid,
                userInfoBean.getData().getHead_pic());
        NormalSharePop normalSharePop = new NormalSharePop(UserInfoActivity.this, normalShareBean, true);
        normalSharePop.showPopupWindow();
    }

    //举报
    private void report() {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    //悄悄关注
    private void attentionByQuiet() {
        if (!"1".equals(svip)) {
            BindSvipDialog.bindAlertDialog(UserInfoActivity.this, "悄悄关注限svip可用");
        } else {
            boolean state;
            if ("1".equals(userInfoBean.getData().getFriend_quiet())) {
                state = false;
            } else {
                state = true;
            }
            HttpHelper.getInstance().quietFollow(uid, state, new HttpCodeMsgListener() {
                @Override
                public void onSuccess(String data, String msg) {
                    if (tvUserInfoName == null) {
                        return;
                    }
                    ToastUtil.show(UserInfoActivity.this, msg);
                    if ("0".equals(userInfoBean.getData().getFriend_quiet())) {
                        userInfoBean.getData().setFriend_quiet("1");
                    } else {
                        userInfoBean.getData().setFriend_quiet("0");
                    }
                }

                @Override
                public void onFail(int code, String msg) {
                    ToastUtil.show(UserInfoActivity.this, msg);
                }
            });
        }
    }

    //分组
    private void subGroup() {
        if (svip.equals("1")) {
//            Intent intent = new Intent(this, FriendsetGroupActivity.class);
            Intent intent = new Intent(this, FriendSetGroupActivity.class);
            intent.putExtra("fuid", uid);
            startActivityForResult(intent, 500);
        } else {
            BindSvipDialog.bindAlertDialog(UserInfoActivity.this, "设置分组限svip可用");
        }
    }

    //查看历史昵称
    private void seeHistoryNickName() {
        if (uid.equals(MyApp.uid)) {
            Intent intent = new Intent(UserInfoActivity.this, history_nameActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } else {
            if (userInfoBean == null || userInfoBean.getData() == null) {
                return;
            }
            if (userInfoBean.getData().getNickname_rule().equals("1")) {
                ToastUtil.show(getApplicationContext(), "Ta的历史昵称仅自己可见");
            } else {
                if ("0".equals(svip)) {
                    BindSvipDialog.bindAlertDialog(UserInfoActivity.this, "查看历史昵称限svip可用");
                } else {
                    Intent intent = new Intent(UserInfoActivity.this, history_nameActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            }
        }
    }

    //设置备注
    private void setRemark() {
        if (userInfoBean.getData().getFollow_state() == 3 || !vip.equals("0")) {
            Intent intent2 = new Intent(this, EditBeizhuActivity.class);
            intent2.putExtra("name", userInfoBean.getData().getMarkname());
            intent2.putExtra("fuid", uid);
            startActivityForResult(intent2, 100);
        } else {
            VipDialog vipDialog = new VipDialog(UserInfoActivity.this, "好友/vip 可设置昵称备注");
        }
    }

    //详细描述
    private void setDesc() {
        if (userInfoBean.getData().getFollow_state() == 3 || !svip.equals("0")) {
            //Intent intent3 = new Intent(this, EditVipBeizhuActivity.class);
            Intent intent3 = new Intent(this, UserMarkActivity.class);
            intent3.putExtra("name", userInfoBean.getData().getLmarkname());
            intent3.putExtra("fuid", uid);
            intent3.putExtra("sypic", userInfoBean.getData().getLsypic());
            startActivityForResult(intent3, 100);
        } else {
            VipDialog vipDialog = new VipDialog(UserInfoActivity.this, "好友/svip 可设置详细描述");
        }
    }

    //拉黑
    private void editBlock() {
        if ("1".equals(userInfoBean.getData().getIs_admin())) {
            ToastUtil.show(UserInfoActivity.this, "黑v账号无法操作");
            return;
        }
        final boolean state;
        if ("2".equals(userInfoBean.getData().getBlack_val()) || "4".equals(userInfoBean.getData().getBlack_val())) {
            state = false;
        } else {
            state = true;
        }
        HttpHelper.getInstance().blockUser(uid, state, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(UserInfoActivity.this)) {
                    return;
                }
                ToastUtil.show(UserInfoActivity.this, "操作成功");
                if (state) { //拉黑
                    if ("3".equals(userInfoBean.getData().getBlack_val())) {
                        userInfoBean.getData().setBlack_val("2");
                    } else if ("1".equals(userInfoBean.getData().getBlack_val())) {
                        userInfoBean.getData().setBlack_val("4");
                    }
                } else { //取消拉黑
                    if ("2".equals(userInfoBean.getData().getBlack_val())) {
                        userInfoBean.getData().setBlack_val("3");
                    } else if ("4".equals(userInfoBean.getData().getBlack_val())) {
                        userInfoBean.getData().setBlack_val("1");
                    }
                }
                getUserInfo();
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(UserInfoActivity.this)) {
                    return;
                }
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    //im 拉黑
    void editBlockWithTim(boolean state) {
        if (state) {
            V2TIMManager.getInstance().getFriendshipManager().addToBlackList(Arrays.asList(uid), new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {

                }
            });
        } else {
            V2TIMManager.getInstance().getFriendshipManager().deleteFromBlackList(Arrays.asList(uid), new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {

                }
            });
        }
    }

    //可疑
    private void editLikeLiar() {
        final int type;
        if ("1".equals(userInfoBean.getData().getIs_likeliar())) {
            type = 0;
        } else {
            type = 1;
        }
        HttpHelper.getInstance().editLikeLiar(uid, type, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (rvUserInfoGift == null) {
                    return;
                }
                if (type == 0) {
                    userInfoBean.getData().setIs_likeliar("0");
                } else {
                    userInfoBean.getData().setIs_likeliar("1");
                }
                showUserInfo();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    private void delIllegallyUserInfo(int type) {
        HttpHelper.getInstance().delIllegallyUserInfo(uid, type, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                getUserInfo();
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void adminMark() {
        //Intent intent1 = new Intent(this, EditGuanliBeizhuActivity.class);
        Intent intent1 = new Intent(this, ManagerMarkActivity.class);
        intent1.putExtra("fuid", uid);
        intent1.putExtra("name", userInfoBean.getData().getAdmin_mark());
        intent1.putExtra("sypic", userInfoBean.getData().getSypic());
        startActivityForResult(intent1, 100);
    }

    private void editJoinGroup() {
        if ("0".equals(userInfoBean.getData().getJoin_group_status())) {
            banJoinGroup();
        } else {
            removeBanJoinGroup();
        }
    }

    void banJoinGroup() {
        HttpHelper.getInstance().banUserStatus(uid, 5, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(UserInfoActivity.this, "操作成功");
                getUserInfo();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    void removeBanJoinGroup() {
        HttpHelper.getInstance().removeBanUserStatus(uid, "5", new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(UserInfoActivity.this, "操作成功");
                getUserInfo();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    //入参method 1-账号 2-动态 3-资料 4-聊天 resume-封禁设备
    void removeBanUserStatus(String method) {
        HttpHelper.getInstance().removeBanUserStatus(uid, method, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(UserInfoActivity.this, "操作成功");
                getUserBanStatus();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    void punishment(String method, String days) {
        //Intent intent = new Intent(this, SendReasonActivity.class);
        Intent intent = new Intent(this, ManagerBanActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("method", method);
        if (!TextUtil.isEmpty(days)) {
            intent.putExtra("days", days);
        }
        if (!TextUtil.isEmpty(oStr) && !TextUtil.isEmpty(dStr)) {
            intent.putExtra("title", oStr + "_" + dStr);
        }
        startActivityForResult(intent, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
        }
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 200) {
            getUserBanStatus();
        } else if (requestCode == 10000) {
            if (data != null) {
                //获取选择器返回的数据
                ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                for (int i = 0; i < images.size(); i++) {
                    LogUtil.d(images.get(i));
                    uploadImage(images.get(i));
                }
            }
        } else {
            if (resultCode == 200) {
                getData();
            }
        }

    }

    public void showBlockInfo(UserInfoBean.DataBean userInfo) {

        if ("1".equals(userInfo.getBlack_val())) { //被拉黑
            tvUserInfoBlockInfo.setText("该用户已将你拉黑！");
            tvUserInfoBlockCancel.setVisibility(View.GONE);
        } else if ("2".equals(userInfo.getBlack_val())) { //拉黑
            tvUserInfoBlockInfo.setText("你已将该用户拉黑！");
            tvUserInfoBlockCancel.setVisibility(View.VISIBLE);
            tvUserInfoBlockCancel.setText("取消拉黑");
        } else if ("4".equals(userInfo.getBlack_val())) { //相互拉黑
            tvUserInfoBlockInfo.setText("双方互相拉黑！");
            tvUserInfoBlockCancel.setVisibility(View.VISIBLE);
            tvUserInfoBlockCancel.setText("取消拉黑");
        }

    }

    public void getSendDynamicPermission() {
        HttpHelper.getInstance().getUserSendDynamicPermission(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                Intent intent = new Intent(UserInfoActivity.this, SendDynamicActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    int commentNum = 0;

    public void notifyCommentDelete() {
        View view = tabUserInfo.getTabAt(2).getCustomView();
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        commentNum--;
        textView.setText("评论(" + commentNum + ")");
    }

//        int mx,my;
//    int lastx,lasty;
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //获取坐标点：
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//        if (x > 50) {
//            return super.dispatchTouchEvent(ev);
//        }
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int deletx = x - mx;
//                int delety = y - my;
//                if(Math.abs(deletx)>Math.abs(delety)) {
//                    finish();
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            default:
//                break;
//        }
//        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
//        lastx = x;
//        lasty = y;
//        mx = x;
//        my = y;
//        return super.dispatchTouchEvent(ev);
//    }

    void showSendGiftPop() {
        SendGiftPop sendGiftPop = new SendGiftPop(UserInfoActivity.this);
        sendGiftPop.showPopupWindow();
    }

    void checkLocationPermisstion() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        //检测用户是否将当前应用的定位权限拒绝
        int checkResult = PermissionHelper.checkOp(UserInfoActivity.this, 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
        int checkResult2 = PermissionHelper.checkOp(UserInfoActivity.this, 1, AppOpsManager.OPSTR_FINE_LOCATION);
        if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
            PermissionHelper permissionHelper = new PermissionHelper(UserInfoActivity.this);
            permissionHelper.showLocIgnoredDialog(UserInfoActivity.this);
        } else {
            PermissionHelper permissionHelper = new PermissionHelper(UserInfoActivity.this);
            permissionHelper.showLocIgnoredDialog(UserInfoActivity.this);
//            if (!EasyPermissions.hasPermissions(UserInfoActivity.this, perms)) {//检查是否获取该权限
//                //第二个参数是被拒绝后再次申请该权限的解释
//                //第三个参数是请求码
//                //第四个参数是要申请的权限
//                EasyPermissions.requestPermissions(UserInfoActivity.this, "需要开启位置权限，以便正常使用全部功能（如您想隐藏位置，可在APP我的-设置-隐私中关闭位置）", 0, perms);
//            } else {
//                UpLocationUtils.LogintimeAndLocation();
//            }
        }
    }

    void getUserAuthPhoto() {
        HttpHelper.getInstance().getUserAuthPhoto(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject obj = new JSONObject(data);
                    JSONObject object = obj.getJSONObject("data");
                    String card_face = object.getString("card_face");
                    ImageViewUtil.previewImage(UserInfoActivity.this, card_face);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    void getUserIdCardPhoto() {
        HttpHelper.getInstance().getIdCardPhoto(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                UserIdCardModel model = GsonUtil.GsonToBean(data, UserIdCardModel.class);
                if (!TextUtil.isEmpty(model.getData().getCard_pic())) {
                    ImageViewUtil.previewImage(UserInfoActivity.this, model.getData().getCard_pic());
                } else {
                    String[]  imgs = {model.getData().getCard_hand(),model.getData().getCard_z(),model.getData().getCard_f()};
                    ImageViewUtil.previewImage(UserInfoActivity.this, imgs);
                }

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    public String removeSpace(String str) {
        if (TextUtil.isEmpty(str)) {
            return "";
        }
        while (str.startsWith("\n")) {
            str = str.replaceFirst("\n", "").trim();
        }
        while (str.endsWith("\n")) {
            str = replaceLast(str, "\n", "").trim();
        }
        return str;
    }


    // 替换字符串里最后出现的元素
    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    void showAdminNormalMenuPop() {
        final List<NormalMenuItem> menuItemList = new ArrayList<>();
//        if ("1".equals(userInfoBean.getData().getIs_likeliar())) {
//            menuItemList.add(new NormalMenuItem(0, "取消可疑用户"));
//        } else {
//            menuItemList.add(new NormalMenuItem(0, "可疑用户"));
//        }
//        menuItemList.add(new NormalMenuItem(0, "违规头像"));
//        menuItemList.add(new NormalMenuItem(0, "违规相册"));
//        menuItemList.add(new NormalMenuItem(0, "违规昵称"));
//        menuItemList.add(new NormalMenuItem(0, "违规签名"));
//        if ("1".equals(userInfoBean.getData().getJoin_group_status())) {
//            menuItemList.add(new NormalMenuItem(0, "启用加群"));
//        } else {
//            menuItemList.add(new NormalMenuItem(0, "禁止加群"));
//        }
        menuItemList.add(new NormalMenuItem(0, "★管理备注"));
        menuItemList.add(new NormalMenuItem(0, "★封禁管理"));
        menuItemList.add(new NormalMenuItem(0, "★资料管理"));
        menuItemList.add(new NormalMenuItem(0, "★直播管理"));
        menuItemList.add(new NormalMenuItem(0, "★官方充值"));
        if ("1".equals(userInfoBean.getData().getFriend_quiet())) {
            menuItemList.add(new NormalMenuItem(2, "取消悄悄关注"));
        } else {
            menuItemList.add(new NormalMenuItem(2, "悄悄关注"));
        }
        menuItemList.add(new NormalMenuItem(2, "设置分组"));
        menuItemList.add(new NormalMenuItem(2, "历史昵称"));
        menuItemList.add(new NormalMenuItem(1, "昵称备注(好友)"));
        menuItemList.add(new NormalMenuItem(2, "详细描述(好友)"));
        menuItemList.add(new NormalMenuItem(0, "分享"));
        menuItemList.add(new NormalMenuItem(0, "举报"));
        if ("2".equals(userInfoBean.getData().getBlack_val()) || "4".equals(userInfoBean.getData().getBlack_val())) {
            menuItemList.add(new NormalMenuItem(0, "取消拉黑"));
        } else {
            menuItemList.add(new NormalMenuItem(0, "拉黑"));
        }

        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(UserInfoActivity.this, menuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                normalMenuPopup.dismiss();
                String data = menuItemList.get(position).getContent();
                if (data.equals("分享")) {
                    showShareWay();
                } else if (data.equals("举报")) {
                    report();
                } else if (data.contains("悄悄关注")) {
                    attentionByQuiet();
                } else if (data.contains("分组")) {
                    subGroup();
                } else if (data.contains("历史昵称")) {
                    seeHistoryNickName();
                } else if (data.contains("昵称备注")) {
                    setRemark();
                } else if (data.contains("描述")) {
                    setDesc();
                } else if (data.contains("拉黑")) {
                    editBlock();
                } else if (data.contains("可疑")) {
                    editLikeLiar();
                } else if (data.contains("头像")) {
                    delIllegallyUserInfo(1);
                } else if (data.contains("相册")) {
                    delIllegallyUserInfo(4);
                } else if (data.contains("昵称")) {
                    delIllegallyUserInfo(2);
                } else if (data.contains("签名")) {
                    delIllegallyUserInfo(3);
                } else if (data.contains("加群")) {
                    editJoinGroup();
                } else if (data.contains("管理备注")) {
                    adminMark();
                } else if (data.contains("直播管理")) {
                    showAdminLiveOperateMenu();
                } else if (data.contains("资料管理")) {
                    showAdminDataOperateMenu();
                } else if (data.contains("封禁管理")) {
                    ivUserInfoReturn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //showAdminOperateMenu();
                            showAdminBanPop();
                        }
                    }, 300);
                } else if (data.contains("官方充值")) {
                    showAdminRechargeMenu();
                }
            }
        });
    }

    void deleteLiveInfo(String uid, String type) {
        HttpHelper.getInstance().deleteLiveTitleOrCover(uid, type, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    void showUserNormalMenuPop() {
        final List<NormalMenuItem> menuItemList = new ArrayList<>();
        if ("1".equals(userInfoBean.getData().getFriend_quiet())) {
            menuItemList.add(new NormalMenuItem(2, "取消悄悄关注"));
        } else {
            menuItemList.add(new NormalMenuItem(2, "悄悄关注"));
        }
        menuItemList.add(new NormalMenuItem(2, "设置分组"));
        menuItemList.add(new NormalMenuItem(2, "历史昵称"));
        menuItemList.add(new NormalMenuItem(1, "昵称备注(好友)"));
        menuItemList.add(new NormalMenuItem(2, "详细描述(好友)"));
        menuItemList.add(new NormalMenuItem(0, "分享"));
        menuItemList.add(new NormalMenuItem(0, "举报"));
        if ("2".equals(userInfoBean.getData().getBlack_val()) || "4".equals(userInfoBean.getData().getBlack_val())) {
            menuItemList.add(new NormalMenuItem(0, "取消拉黑"));
        } else {
            menuItemList.add(new NormalMenuItem(0, "拉黑"));
        }
        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(UserInfoActivity.this, menuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                normalMenuPopup.dismiss();
                String data = menuItemList.get(position).getContent();
                if (data.equals("分享")) {
                    showShareWay();
                } else if (data.equals("举报")) {
                    report();
                } else if (data.contains("悄悄关注")) {
                    attentionByQuiet();
                } else if (data.contains("分组")) {
                    subGroup();
                } else if (data.contains("历史昵称")) {
                    seeHistoryNickName();
                } else if (data.contains("昵称备注")) {
                    setRemark();
                } else if (data.contains("描述")) {
                    setDesc();
                } else if (data.contains("拉黑")) {
                    editBlock();
                }
            }
        });
    }

    void showMineNormalMenuPop() {
        final List<NormalMenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(new NormalMenuItem(0, "分享"));
        menuItemList.add(new NormalMenuItem(0, "历史昵称"));
        menuItemList.add(new NormalMenuItem(0, "详细描述"));
        if (!TextUtil.isEmpty(userInfoBean.getData().getAnchor_room_id()) && !"0".equals(userInfoBean.getData().getAnchor_room_id())) {
            menuItemList.add(new NormalMenuItem(0, "直播场控列表"));
        }
        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(UserInfoActivity.this, menuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                normalMenuPopup.dismiss();
                String data = menuItemList.get(position).getContent();
                if (data.equals("分享")) {
                    showShareWay();
                } else if (data.contains("历史昵称")) {
                    seeHistoryNickName();
                } else if (data.contains("昵称备注")) {
                    setRemark();
                } else if (data.contains("描述")) {
                    setDesc();
                } else if (data.contains("场控")) {
                    showMyLiveManagerPop();
                }
            }
        });
    }

    private boolean isPermissionToWatchList(TextView textView) {
        return !textView.getText().toString().equals("※");
    }

    boolean isAnimShowing = false;

    public void showOrHideTopBottomBar(boolean isShow) {
        if (isAnimShowing) {
            return;
        }
        if (isShow) {
            if (llUserInfoBottom.getVisibility() == View.GONE) {
                llUserInfoBottom.setVisibility(View.VISIBLE);
                llLinkUser.setVisibility(View.VISIBLE);
                //llUserInfoBottom.setAnimation(AnimationUtil.moveToViewLocation());
                TranslateAnimation translateAnimation = AnimationUtil.moveToViewLocation();
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isAnimShowing = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isAnimShowing = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                llUserInfoBottom.setAnimation(translateAnimation);
                TranslateAnimation translateAnimation2 = AnimationUtil.horizontalRightShow();
                llLinkUser.setAnimation(translateAnimation2);
            }
        } else {
            if (llUserInfoBottom.getVisibility() == View.VISIBLE) {
                llUserInfoBottom.setVisibility(View.GONE);
                llLinkUser.setVisibility(View.GONE);
                //llUserInfoBottom.setAnimation(AnimationUtil.moveToViewBottom());
                TranslateAnimation translateAnimation = AnimationUtil.moveToViewBottom();
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isAnimShowing = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isAnimShowing = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                llUserInfoBottom.setAnimation(translateAnimation);
                TranslateAnimation translateAnimation2 = AnimationUtil.horizontalRightHide();
                llLinkUser.setAnimation(translateAnimation2);
            }
        }
    }

    //强制主播下播
    void kickOutLiver() {
        HttpHelper.getInstance().adminOperateLive(4, uid, "你已被官方强制下播", 0, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(UserInfoActivity.this, "强制下播成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    //解禁主播
    private void relieveBanLiver() {
        HttpHelper.getInstance().adminOperateLive(3, uid, "", 0, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(UserInfoActivity.this, "解禁成功");
                userInfoBean.getData().setLive_status("0");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    boolean isAnchor() {
        if (!TextUtil.isEmpty(userInfoBean.getData().getAnchor_room_id()) && !"0".equals(userInfoBean.getData().getAnchor_room_id())) {
            return true;
        } else {
            return false;
        }
    }

    boolean isLiveAnchorRecommend = false;
    boolean isLiveAnchorBan = false;
    boolean isLiveAnchorSign = false;

    void getLiveAnchorState() {
        HttpHelper.getInstance().getLiveAnchorState(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveAnchorStateBean liveAnchorStateBean = GsonUtil.GsonToBean(data, LiveAnchorStateBean.class);
                isLiveAnchorRecommend = "1".equals(liveAnchorStateBean.getData().getRecommend_status());
                isLiveAnchorBan = "1".equals(liveAnchorStateBean.getData().getProhibition_status());
                isLiveAnchorSign = "2".equals(liveAnchorStateBean.getData().getAnchor_status());
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    private void recommendAnchor(final boolean isRecommend) {
        HttpHelper.getInstance().recommendOrCancel(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                isLiveAnchorRecommend = !isRecommend;
                ToastUtil.show(UserInfoActivity.this, "操作成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    public void banUserWatchLive() {
        showBanWatchLivePop();
    }

    public void resetUserWatchLive() {
        removeBanUserStatus("6");
    }

    void showBanWatchLivePop() {
        List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "一天"));
        normalMenuItemList.add(new NormalMenuItem(0, "三天"));
        normalMenuItemList.add(new NormalMenuItem(0, "一周"));
        normalMenuItemList.add(new NormalMenuItem(0, "两周"));
        normalMenuItemList.add(new NormalMenuItem(0, "一月"));
        normalMenuItemList.add(new NormalMenuItem(0, "永久"));
        NormalMenuPopup kickOutPop = new NormalMenuPopup(UserInfoActivity.this, normalMenuItemList);
        kickOutPop.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (position) {
                    case 0:
                        punishment("6", "1");
                        break;
                    case 1:
                        punishment("6", "3");
                        break;
                    case 2:
                        punishment("6", "7");
                        break;
                    case 3:
                        punishment("6", "14");
                        break;
                    case 4:
                        punishment("6", "30");
                        break;
                    case 5:
                        punishment("6", "0");
                        break;
                }
            }
        });
        kickOutPop.showPopupWindow();
    }


    //资料管理
    void showAdminDataOperateMenu() {
        final List<NormalMenuItem> menuItemList = new ArrayList<>();
        if ("1".equals(userInfoBean.getData().getIs_likeliar())) {
            menuItemList.add(new NormalMenuItem(0, "取消可疑用户"));
        } else {
            menuItemList.add(new NormalMenuItem(0, "可疑用户"));
        }
        menuItemList.add(new NormalMenuItem(0, "违规头像"));
        menuItemList.add(new NormalMenuItem(0, "违规相册"));
        menuItemList.add(new NormalMenuItem(0, "违规昵称"));
        menuItemList.add(new NormalMenuItem(0, "违规签名"));
        if ("1".equals(userInfoBean.getData().getJoin_group_status())) {
            menuItemList.add(new NormalMenuItem(0, "启用加群"));
        } else {
            menuItemList.add(new NormalMenuItem(0, "禁止加群"));
        }
        //menuItemList.add(new NormalMenuItem(0, "管理备注"));
        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(UserInfoActivity.this, menuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                normalMenuPopup.dismiss();
                String data = menuItemList.get(position).getContent();
                if (data.contains("可疑")) {
                    editLikeLiar();
                } else if (data.contains("头像")) {
                    delIllegallyUserInfo(1);
                } else if (data.contains("相册")) {
                    delIllegallyUserInfo(4);
                } else if (data.contains("昵称")) {
                    delIllegallyUserInfo(2);
                } else if (data.contains("签名")) {
                    delIllegallyUserInfo(3);
                } else if (data.contains("加群")) {
                    editJoinGroup();
                } else if (data.contains("管理备注")) {
                    adminMark();
                }
            }
        });
    }

    private void showAdminLiveOperateMenu() {
        final String warningStr = "警告主播";
        final String updateStr = "资料修改";
        final String kickOutStr = "强制下播";
        String banStr = "封禁主播";
        if (isLiveAnchorBan) {
            banStr = "解禁主播";
        } else {
            banStr = "封禁主播";
        }
        String banWatchLiveStr = "禁看直播";
        if (!"1".equals(userBanStatusBean.getLivestatus())) {
            banWatchLiveStr = "解禁看播";
        }

        String recommendLiveStr = "推荐主播";
        if (isLiveAnchorRecommend) {
            recommendLiveStr = "取消推荐";
        } else {
            recommendLiveStr = "推荐主播";
        }

        String signStr = "";
        if (isLiveAnchorSign) {
            signStr = "取消签约";
        } else {
            signStr = "签约主播";
        }


        final AlertView alertView;
        if (isAnchor()) {
            alertView = new AlertView(null, null, "取消", null, new String[]{recommendLiveStr, signStr, updateStr, "删除直播封面", "删除直播标题", warningStr, kickOutStr, banStr, banWatchLiveStr}, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position, String data) {
                    switch (data) {
                        case "警告主播":
                            Intent intent = new Intent(UserInfoActivity.this, LiveAdminOperateActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            break;
                        case "强制下播":
                            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserInfoActivity.this);
                            builder.setMessage("是否强制主播下播?")
                                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    kickOutLiver();
                                }
                            }).create().show();
                            break;
                        case "封禁主播":
                            Intent intent2 = new Intent(UserInfoActivity.this, LiveAdminOperateActivity.class);
                            intent2.putExtra("uid", uid);
                            intent2.putExtra("type", 2);
                            startActivity(intent2);
                            break;
                        case "解禁主播":
                            relieveBanLiver();
                            break;
                        case "推荐主播":
                            recommendAnchor(true);
                            break;
                        case "取消推荐":
                            recommendAnchor(false);
                            break;
                        case "禁看直播":
                            banUserWatchLive();
                            break;
                        case "解禁看播":
                            resetUserWatchLive();
                            break;
                        case "资料修改":
                            showLiveCardPop(uid);
                            break;
                        case "取消签约":
                        case "签约主播":
                            if (!isLiveAnchorSign) {
                                showSignPop();
                            } else {
                                signAnchor(uid, !isLiveAnchorSign);
                            }
                            break;
                        case "删除直播封面":
                            deleteLiveInfo(uid, "2");
                            break;
                        case "删除直播标题":
                            deleteLiveInfo(uid, "1");
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            alertView = new AlertView(null, null, "取消", null, new String[]{banWatchLiveStr}, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position, String data) {
                    switch (data) {
                        case "禁看直播":
                            banUserWatchLive();
                            break;
                        case "解禁看播":
                            resetUserWatchLive();
                            break;
                    }
                }
            });
        }
        alertView.show();
    }

    LiveInfoPop liveInfoPop;

    void showLiveCardPop(String uid) {
        liveInfoPop = new LiveInfoPop(this, uid);
        liveInfoPop.showPopupWindow();
        liveInfoPop.setOnSimpleListener(new OnSimplePopListener() {
            @Override
            public void doSimplePop() {
                showChoosePhoto();
            }
        });
    }

    void showChoosePhoto() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "从相册选择"));
        final NormalMenuPopup menuPopup = new NormalMenuPopup(UserInfoActivity.this, normalMenuItemList);
        menuPopup.showPopupWindow();
        menuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                requestPhotoPermission();
                menuPopup.dismiss();
            }
        });
    }

    void requestPhotoPermission() {
        XXPermissions.with(UserInfoActivity.this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        choosePhotoBySelector();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            showPhotoPermissionDialog();
                        } else {
                            ToastUtil.show("图片选择需要以下权限:\n\n1.访问设备上的照片");
                        }
                    }
                });
    }

    void showPhotoPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("提示")
                .setMessage("图片选择需要以下权限:\n\n1.访问设备上的照片")
                .setPositiveButton("去授予", (dialog, which) -> {
                    dialog.dismiss();
                    XXPermissions.startPermissionActivity(UserInfoActivity.this);
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    void choosePhotoBySelector() {
        ImageSelector.builder()
                .useCamera(false)
                .setSingle(false)
                .setMaxSelectCount(1)
                .canPreview(true)
                .setCrop(true)
                .setCropRatio(1f)
                .start(UserInfoActivity.this, 10000);
    }

    int picFlag;

    void uploadImage(String path) {
//        picFlag = 1;
//        Bitmap loadbitmap = BitmapFactory.decodeFile(path, getBitmapOption(2));
//        Bitmap rotaBitmap = PhotoRemoteUtil.rotaingImageView(PhotoRemoteUtil.getBitmapDegree(path), loadbitmap);
//        ByteArrayInputStream is = new ByteArrayInputStream(Bitmap2Bytes(rotaBitmap));
//        PhotoUploadTask put = new PhotoUploadTask(
//                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
//                , is,
//                this, new MyHandler(UserInfoActivity.this));
//        put.start();

        HttpHelper.getInstance().uploadImage(path, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                BeanIcon beanicon = GsonUtil.GsonToBean(data, BeanIcon.class);
                if (beanicon != null && beanicon.getData() != null) {
                    String imgPre = SharedPreferencesUtils.geParam(UserInfoActivity.this, SpKey.IMAGE_HOST, "");
                    String imgUrl = imgPre + beanicon.getData();
                    if (liveInfoPop != null) {
                        liveInfoPop.refreshImageUrl(imgUrl);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });

    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    static class MyHandler extends Handler {
        WeakReference<UserInfoActivity> weakReference;

        public MyHandler(UserInfoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserInfoActivity activity = weakReference.get();
            if (msg.what == 152) {
                String s = (String) msg.obj;
                BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                String imgpre = SharedPreferencesUtils.geParam(activity, "image_host", "");
                String imgUrl = imgpre + beanicon.getData();
                if (activity.liveInfoPop != null) {
                    activity.liveInfoPop.refreshImageUrl(imgUrl);
                }
            }
        }
    }

    void showSignPop() {
        SignAnchorPop signAnchorPop = new SignAnchorPop(UserInfoActivity.this, uid);
        signAnchorPop.showPopupWindow();
        signAnchorPop.setOnSignAnchorListener(new SignAnchorPop.OnSignAnchorListener() {
            @Override
            public void doSignAnchor() {
                signAnchor(uid, true);
            }
        });
    }

    //签约/取消签约
    private void signAnchor(String uid, final boolean isSign) {
        HttpHelper.getInstance().adminOperateLive(isSign ? 5 : 6, uid, "", 0, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(UserInfoActivity.this, isSign ? "签约成功" : "取消签约");
                isLiveAnchorSign = isSign;
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void showMyLiveManagerPop() {
        LiveManageUserPop liveManageUserPop = new LiveManageUserPop(this, MyApp.uid, MyApp.uid);
        liveManageUserPop.showPopupWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(UserLiveHistoryRefreshBean event) {
        //LogUtil.d("okhttp","current item "+ vpUserInfo.getCurrentItem());
        if (vpUserInfo.getCurrentItem() == 3 && userLiveHistoryFragment != null) {
            userLiveHistoryFragment.refreshData();
        }
    }

    void showAdminRechargeMenu() {
        final List<NormalMenuItem> menuItemList = new ArrayList<>();

        menuItemList.add(new NormalMenuItem(0, "赠送SVIP"));
        if ("1".equals(userInfoBean.getData().getSex())) {
            menuItemList.add(new NormalMenuItem(0, "开通高端交友"));
            menuItemList.add(new NormalMenuItem(0, "开通红娘服务"));
        } else if ("2".equals(userInfoBean.getData().getSex())) {
            menuItemList.add(new NormalMenuItem(0, "开通高端交友"));
        }
        NormalMenuPopup normalMenuPopup = new NormalMenuPopup(UserInfoActivity.this, menuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (position) {
                    case 0:
                        showGiveSVIP();
                        break;
                    case 1:
                        showServiceDaysMenu("1");
                        normalMenuPopup.dismiss();
                        break;
                    case 2:
                        showServiceDaysMenu("2");
                        normalMenuPopup.dismiss();
                        break;
                }
            }
        });
    }


    private void showServiceDaysMenu(String type) {
        String[] arr = null;
        if ("1".equals(type)) {
            if ("1".equals(userInfoBean.getData().getSex())) {
                arr = new String[]{"1年", "2年", "3年", "永久"};
            } else if ("2".equals(userInfoBean.getData().getSex())) {
                arr = new String[]{"永久"};
            }
        } else {
            arr = new String[]{"1年", "2年", "3年", "永久"};
        }

        AlertView alertView = new AlertView(null, null, "取消", null, arr, this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                if ("1".equals(type)) {
                    openHigh(userInfoBean.getData().getUid(), (position + 1) + "");
                } else if ("2".equals(type)) {
                    openRedService(userInfoBean.getData().getUid(), (position + 1) + "");
                }
            }
        });
        alertView.show();
    }


    private void openHigh(String targetUid, String type) {
        HttpHelper.getInstance().adminOpenHigh(targetUid, type, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    private void openRedService(String targetUid, String type) {
        HttpHelper.getInstance().adminOpenRedService(targetUid, type, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    void showGiveSVIP() {
        NormalEditPop normalEditPop = new NormalEditPop.Builder(UserInfoActivity.this)
                .setTitle("赠送SVIP")
                .setHint("请选择赠送的天数")
                .setConfirmStr("赠送")
                .build();
        normalEditPop.showPopupWindow();
        normalEditPop.setOnPopClickListener(new NormalEditPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalEditPop.dismiss();
            }

            @Override
            public void confirmClick(String info) {
                normalEditPop.dismiss();
                if (!TextUtil.isEmpty(info)) {
                    giveSvip(info);
                }
            }
        });
    }

    public void giveSvip(String day) {
        HttpHelper.getInstance().giveSvip(uid, day, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(UserInfoActivity.this, msg);
            }
        });
    }

    public void showLinkTypePop() {
        //Intent intent = new Intent(UserInfoActivity.this, LiveChatSettingActivity.class);
        //startActivity(intent);
        LinkUserTypePop linkUserTypePop = new LinkUserTypePop(this, uid);
        linkUserTypePop.showPopupWindow();
        linkUserTypePop.setOnLinkUserTypeListener(new LinkUserTypePop.OnLinkUserTypeListener() {
            @Override
            public void doLinkAudio() {
                doLinkUser(1);
                linkUserTypePop.dismiss();
            }

            @Override
            public void doLinkVideo() {
                doLinkUser(2);
                linkUserTypePop.dismiss();
            }
        });
    }

    void doLinkUser(int type) {
        HttpHelper.getInstance().startLinkUser(uid, type, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                startLinkUser(type);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    void startLinkUser(int type) {
        ProfileManager.getInstance().getUserInfoByUserId(uid, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {
                List<UserModel> list = new ArrayList<>();
                list.add(model);
                ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(UserInfoActivity.this)).setLink(true);
                if (type == 2) {
                    TRTCVideoCallActivity.startCallSomeone(TUIKitLive.getAppContext(), list);
                } else {
                    TRTCAudioCallActivity.startCallSomeone(TUIKitLive.getAppContext(), list);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }


    public void getLiveChatState() {
        if (MyApp.uid.equals(uid)) {
            return;
        }
        HttpHelper.getInstance().getAnchorChatAuth(uid, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                LiveLinkChatStateBean liveLinkChatStateBean = GsonUtil.GsonToBean(data, LiveLinkChatStateBean.class);
                if (liveLinkChatStateBean.getData().getIs_anchor() == 1) {
                    switch (liveLinkChatStateBean.getData().getLive_chat_status()) {
                        case 0:
                            tvLinkUserState.setText("勿扰");
                            viewLinkUserState.setBackgroundResource(R.drawable.dot_red);
                            break;
                        case 1:
                            tvLinkUserState.setText("可连线");
                            viewLinkUserState.setBackgroundResource(R.drawable.dot_green);
                            break;
                        case 2:
                            tvLinkUserState.setText("直播中");
                            viewLinkUserState.setBackgroundResource(R.drawable.dot_orange);
                            break;
                        case 3:
                            tvLinkUserState.setText("连线中");
                            viewLinkUserState.setBackgroundResource(R.drawable.dot_orange);
                            break;
                    }
                    llLinkUser.setAlpha(1f);
                    llLinkUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (MyApp.uid.equals(uid)){
                                Intent intent = new Intent(UserInfoActivity.this, LiveChatSettingActivity.class);
                                startActivity(intent);
                            }else {
                                showLinkTypePop();
                            }

                        }
                    });
                } else {
                    llLinkUser.setAlpha(0f);
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    String oStr, dStr;

    //入参method 1-账号 2-动态 3-资料 4-聊天 forbid-封禁设备
    public void showAdminBanPop() {
        AdminBanPop adminBanPop = new AdminBanPop(UserInfoActivity.this, userBanStatusBean);
        adminBanPop.showPopupWindow();
        adminBanPop.setOnOperateListener(new AdminBanPop.OnOperateListener() {
            @Override
            public void doChooseComplete(int operate, int day, String operateStr, String dayStr) {
                oStr = operateStr;
                dStr = dayStr;
                String tempDay = "";
                switch (day) {
                    case 0:
                        tempDay = "1";
                        break;
                    case 1:
                        tempDay = "3";
                        break;
                    case 2:
                        tempDay = "7";
                        break;
                    case 3:
                        tempDay = "14";
                        break;
                    case 4:
                        tempDay = "30";
                        break;
                    case 5:
                        tempDay = "0";
                        break;
                }
                String tempMethod = "";
                switch (operate) {
                    case 0: //动态
                        tempMethod = "2";
                        banOperate(!"1".equals(userBanStatusBean.getDynamicstatus()), tempMethod, tempDay);
                        break;
                    case 2: //资料
                        tempMethod = "3";
                        banOperate(!"1".equals(userBanStatusBean.getInfostatus()), tempMethod, tempDay);
                        break;
                    case 1: //聊天
                        tempMethod = "4";
                        banOperate(!"1".equals(userBanStatusBean.getChatstatus()), tempMethod, tempDay);
                        break;
                    case 3: //账号
                        tempMethod = "1";
                        banOperate(!"1".equals(userBanStatusBean.getStatus()), tempMethod, tempDay);
                        break;
                    case 4: //设备
                        if (!"1".equals(userBanStatusBean.getDevicestatus())) {
                            tempMethod = "resume";
                            banOperate(true, tempMethod, tempDay);
                        } else {
                            tempMethod = "forbid";
                            banOperate(false, tempMethod, tempDay);
                        }
                        break;
                }
            }
        });
    }

    public void banOperate(boolean banState, String method, String day) {
        if (banState) {
            removeBanUserStatus(method);
        } else {
            punishment(method, day);
        }
    }


    public void getEditPermission() {
        HttpHelper.getInstance().getEditPermission(uid, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                Intent intent = new Intent(UserInfoActivity.this, EditPersonInfoActivity.class);
                intent.putExtra("otheruid", uid);
                startActivityForResult(intent, 1010);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

}
