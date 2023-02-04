package com.aiwujie.shengmo.activity.newui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.EditDynamicActivity;
import com.aiwujie.shengmo.activity.OtherReasonActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.VideoPlayerActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicDetailData;
import com.aiwujie.shengmo.customview.NineGridViewWrapper;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.CallBackEvent;
import com.aiwujie.shengmo.eventbus.UserDynamicRefreshBean;
import com.aiwujie.shengmo.fragment.dynamic.DynamicDetailCommentFragment;
import com.aiwujie.shengmo.fragment.dynamic.DynamicDetailPushFragment;
import com.aiwujie.shengmo.fragment.dynamic.DynamicDetailRewordFragment;
import com.aiwujie.shengmo.fragment.dynamic.DynamicDetailThumbFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.ui.activity.MyLiveLevelActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop2;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.util.NormalUtilKt;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.utils.DensityUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.ImageViewUtil;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NineGridUtils;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.CommentDialogFragment;
import com.aiwujie.shengmo.view.KeyBoardListenerHelper;
import com.aiwujie.shengmo.view.PushTopCardPop;
import com.aiwujie.shengmo.view.gloading.Gloading;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.ninegrid.NineGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tencent.qcloud.tim.uikit.utils.ScreenUtil.getScreenWidth;

public class NewDynamicDetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.tv_normal_title_more)
    TextView tvNormalTitleMore;
    @BindView(R.id.layout_normal_titlebar)
    LinearLayout layoutNormalTitlebar;
    @BindView(R.id.view_layout_avatar_bg)
    View viewLayoutAvatarBg;
    @BindView(R.id.lottie_user_avatar_state)
    LottieAnimationView lottieUserAvatarState;
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
    @BindView(R.id.tv_layout_user_normal_info_name)
    TextView tvLayoutUserNormalInfoName;
    @BindView(R.id.iv_layout_user_normal_info_auth_pic)
    ImageView ivLayoutUserNormalInfoAuthPic;
    @BindView(R.id.iv_layout_user_normal_info_auth_card)
    ImageView ivLayoutUserNormalInfoAuthCard;
    @BindView(R.id.tv_live_audience_level)
    TextView tvLiveAudienceLevel;
    @BindView(R.id.view_live_audience_level)
    View viewLiveAudienceLevel;
    @BindView(R.id.iv_live_audience_level)
    ImageView ivLiveAudienceLevel;
    @BindView(R.id.constraint_layout_audience_level)
    ConstraintLayout constraintLayoutAudienceLevel;
    @BindView(R.id.tv_live_anchor_level)
    TextView tvLiveAnchorLevel;
    @BindView(R.id.view_live_anchor_level)
    View viewLiveAnchorLevel;
    @BindView(R.id.iv_live_anchor_level)
    ImageView ivLiveAnchorLevel;
    @BindView(R.id.constraint_layout_anchor_level)
    ConstraintLayout constraintLayoutAnchorLevel;
    @BindView(R.id.iv_layout_user_normal_info_sex)
    ImageView ivLayoutUserNormalInfoSex;
    @BindView(R.id.tv_layout_user_normal_info_age)
    TextView tvLayoutUserNormalInfoAge;
    @BindView(R.id.ll_layout_user_normal_info_sex_age)
    LinearLayout llLayoutUserNormalInfoSexAge;
    @BindView(R.id.tv_layout_user_normal_info_role)
    TextView tvLayoutUserNormalInfoRole;
    @BindView(R.id.iv_layout_user_normal_info_wealth)
    ImageView ivLayoutUserNormalInfoWealth;
    @BindView(R.id.tv_layout_user_normal_info_wealth)
    TextView tvLayoutUserNormalInfoWealth;
    @BindView(R.id.ll_layout_user_normal_info_wealth)
    LinearLayout llLayoutUserNormalInfoWealth;
    @BindView(R.id.iv_layout_user_normal_info_charm)
    ImageView ivLayoutUserNormalInfoCharm;
    @BindView(R.id.tv_layout_user_normal_info_charm)
    TextView tvLayoutUserNormalInfoCharm;
    @BindView(R.id.ll_layout_user_normal_info_charm)
    LinearLayout llLayoutUserNormalInfoCharm;
    @BindView(R.id.layout_user_normal_info)
    LinearLayout layoutUserNormalInfo;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.iv_recommend)
    ImageView ivRecommend;
    @BindView(R.id.iv_ding)
    ImageView ivDing;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    @BindView(R.id.iv_item_dynamic_read_count)
    ImageView ivItemDynamicReadCount;
    @BindView(R.id.item_listview_dynamic_readcount)
    TextView itemListviewDynamicReadcount;
    @BindView(R.id.tv_item_dynamic_topic)
    TextView tvItemDynamicTopic;
    @BindView(R.id.ll_item_dynamic_topic)
    LinearLayout llItemDynamicTopic;
    @BindView(R.id.tv_dynamic_detail_content)
    TextView tvDynamicDetailContent;
    @BindView(R.id.iv_dynamic_detail_pic)
    NineGridViewWrapper ivDynamicDetailPic;
    @BindView(R.id.iv_nine_dynamic_detail_pic)
    NineGridView ivNineDynamicDetailPic;
    @BindView(R.id.iv_dynamic_detail_video_play)
    ImageView ivDynamicDetailVideoPlay;
    @BindView(R.id.iv_dynamic_detail_pic_round)
    ImageView ivDynamicDetailPicRound;
    @BindView(R.id.fl_dynamic_detail_content)
    FrameLayout flDynamicDetailContent;
    @BindView(R.id.iv_dynamic_detail_distance)
    ImageView ivDynamicDetailDistance;
    @BindView(R.id.tv_dynamic_detail_distance)
    TextView tvDynamicDetailDistance;
    @BindView(R.id.tv_dynamic_detail_time)
    TextView tvDynamicDetailTime;
    @BindView(R.id.tab_dynamic_detail)
    TabLayout tabDynamicDetail;
    @BindView(R.id.app_bar_dynamic_detail)
    AppBarLayout appBarDynamicDetail;
    @BindView(R.id.view_pager_dynamic_detail)
    ViewPager viewPagerDynamicDetail;
    @BindView(R.id.coord_dynamic_detail)
    CoordinatorLayout coordDynamicDetail;
    @BindView(R.id.srl_dynamic_detail)
    SmartRefreshLayout srlDynamicDetail;
    @BindView(R.id.tv_dynamic_detail_thumb_up)
    TextView tvDynamicDetailThumbUp;
    @BindView(R.id.ll_dynamic_detail_thumb_up)
    LinearLayout llDynamicDetailThumbUp;
    @BindView(R.id.ll_dynamic_detail_comment)
    LinearLayout llDynamicDetailComment;
    @BindView(R.id.ll_dynamic_detail_reward)
    LinearLayout llDynamicDetailReward;
    @BindView(R.id.ll_dynamic_detail_push_top)
    LinearLayout llDynamicDetailPushTop;
    @BindView(R.id.ll_dynamic_detail_bottom)
    LinearLayout llDynamicDetailBottom;
    @BindView(R.id.tv_layout_user_normal_info_tip)
    TextView tvLayoutUserNormalInfoTip;
    @BindView(R.id.tv_likeliar)
    TextView tvLikeLiar;
    private DynamicDetailData.DataBean dynamicBean;
    private ArrayList<String> titles;
    private DynamicDetailThumbFragment thumbFragment;
    private DynamicDetailCommentFragment commentFragment;
    private DynamicDetailRewordFragment rewordFragment;
    private DynamicDetailPushFragment pushFragment;
    private String uid;
    private OnItemClickListener alertItemListener;
    private int retcode;
    private boolean isFirstLoad = true;

    private String thumbStr, commentStr, rewordStr, pushTopStr;

    private Gloading.Holder gloadingHolder;

    public static void start(Context context,String did) {
        Intent intent = new Intent(context,NewDynamicDetailActivity.class);
        intent.putExtra("uid", MyApp.uid);
        intent.putExtra("did", did);
        intent.putExtra("pos", 1);
        intent.putExtra("showwhat", 1);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_dynamic_detail);
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
        ButterKnife.bind(this);
        gloadingHolder = Gloading.getDefault().wrap(coordDynamicDetail);
        StatusBarUtil.showLightStatusBar(this);
        tvNormalTitleTitle.setText("动态详情");
        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        defaultDisplay = wm.getDefaultDisplay();
        gloadingHolder.showLoading();
        getData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
    }

    void getData() {
        did = getIntent().getStringExtra("did");
        HttpHelper.getInstance().getDynamicDetail(did, new HttpListener() {
            @Override
            public void onSuccess(String response) {
                if (NewDynamicDetailActivity.this.isFinishing()) {
                    return;
                }
                gloadingHolder.showLoadSuccess();
                DynamicDetailData dynamicDetailBean = GsonUtil.GsonToBean(response, DynamicDetailData.class);
                retcode = dynamicDetailBean.getRetcode();
                if (dynamicDetailBean != null && dynamicDetailBean.getData() != null) {
                    dynamicBean = dynamicDetailBean.getData();
                    showUserInfo();
                    showDynamicInfo();
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(NewDynamicDetailActivity.this, msg);
                gloadingHolder.showEmpty();
            }
        });

    }

    void showUserInfo() {
        if (!SafeCheckUtil.isActivityFinish(NewDynamicDetailActivity.this)) {
            ImageLoader.loadCircleImage(NewDynamicDetailActivity.this, dynamicBean.getHead_pic(), ivUserAvatarIcon, R.mipmap.morentouxiang);
        }
        tvLayoutUserNormalInfoName.setText(dynamicBean.getNickname());
        tvLayoutUserNormalInfoAge.setText(dynamicBean.getAge());
        uid = dynamicBean.getUid();
        if (dynamicBean.getOnlinestate() == 1) {
            ivUserAvatarOnline.setVisibility(View.VISIBLE);
        } else {
            ivUserAvatarOnline.setVisibility(View.INVISIBLE);
        }
        if ("1".equals(dynamicBean.getSex())) {
            llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
        } else if ("2".equals(dynamicBean.getSex())) {
            llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
        } else if ("3".equals(dynamicBean.getSex())) {
            llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
        }
        if ("S".equals(dynamicBean.getRole())) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            tvLayoutUserNormalInfoRole.setText("斯");
        } else if ("M".equals(dynamicBean.getRole())) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            tvLayoutUserNormalInfoRole.setText("慕");
        } else if ("SM".equals(dynamicBean.getRole())) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            tvLayoutUserNormalInfoRole.setText("双");
        } else {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            tvLayoutUserNormalInfoRole.setText(dynamicBean.getRole());
        }
        if (!"0".equals(dynamicBean.getWealth_val())) {
            tvLayoutUserNormalInfoWealth.setText(dynamicBean.getWealth_val());
        } else {
            llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
        }
        if (!"0".equals(dynamicBean.getCharm_val())) {
            tvLayoutUserNormalInfoCharm.setText(dynamicBean.getCharm_val());
        } else {
            llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
        }
        itemListviewDynamicReadcount.setText(dynamicBean.getReadtimes());
        ivItemDynamicReadCount.setVisibility(View.VISIBLE);
        if (Integer.parseInt(dynamicBean.getReadtimes()) > 10000) {
            itemListviewDynamicReadcount.setTextColor(0xffff7857);
        } else if (Integer.parseInt(dynamicBean.getReadtimes()) > 99999) {
            itemListviewDynamicReadcount.setText("100000+");
        }

        if (!"0".equals(dynamicBean.getRecommend())) {
            ivRecommend.setVisibility(View.VISIBLE);
        } else {
            ivRecommend.setVisibility(View.GONE);
        }
        if (!"0".equals(dynamicBean.getTopnum())) {
            ivTop.setVisibility(View.VISIBLE);
        } else {
            ivTop.setVisibility(View.GONE);
        }

        if (MyApp.uid.equals(uid)) {  //自己看自己
            if (!"0".equals(dynamicBean.getStickstate())) {
                ivDing.setVisibility(View.VISIBLE);
            } else {
                ivDing.setVisibility(View.GONE);
            }
        } else {
            if (!"0".equals(dynamicBean.getRecommendall())) {
                ivDing.setVisibility(View.VISIBLE);
            } else {
                ivDing.setVisibility(View.GONE);
            }
        }

        if (!"0".equals(dynamicBean.getIs_hidden())) {
            String admin = (String) SharedPreferencesUtils.getParam(NewDynamicDetailActivity.this, "admin", "0");
            if ("1".equals(admin) || MyApp.uid.equals(uid)) {
                ivLock.setVisibility(View.VISIBLE);
            }
        } else {
            ivLock.setVisibility(View.GONE);
        }

        if ("1".equals(dynamicBean.getIs_admin())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_manager);
        } else if ("1".equals(dynamicBean.getSvipannual())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_svip_year);
        } else if ("1".equals(dynamicBean.getSvip())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_svip);
        } else if ("1".equals(dynamicBean.getVipannual())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_vip_year);
        } else if ("1".equals(dynamicBean.getVip())) {
            ivUserAvatarLevel.setImageResource(R.drawable.user_vip);
        } else {
            ivUserAvatarLevel.setVisibility(View.INVISIBLE);
        }
        String admin = (String) SharedPreferencesUtils.getParam(NewDynamicDetailActivity.this, "admin", "0");
        if ("1".equals(admin)) {
            tvLayoutUserNormalInfoTip.setVisibility(View.VISIBLE);
            tvLayoutUserNormalInfoTip.setText("发" + dynamicBean.getDynamic_num() + "推" + dynamicBean.getRdynamic_num() + "隐" + dynamicBean.getHidedstatustimes() + "封" + dynamicBean.getDynamicstatusouttimes());
        }
        if (TextUtil.isEmpty(dynamicBean.getUser_level()) || "0".equals(dynamicBean.getUser_level())) {
            constraintLayoutAudienceLevel.setVisibility(View.GONE);
        } else {
            constraintLayoutAudienceLevel.setVisibility(View.VISIBLE);
            tvLiveAudienceLevel.setText(dynamicBean.getUser_level());
        }

        if (TextUtil.isEmpty(dynamicBean.getAnchor_level()) || "0".equals(dynamicBean.getAnchor_level())) {
            constraintLayoutAnchorLevel.setVisibility(View.GONE);
        } else {
            constraintLayoutAnchorLevel.setVisibility(View.VISIBLE);
            tvLiveAnchorLevel.setText(dynamicBean.getAnchor_level());
        }


        if (!TextUtil.isEmpty(dynamicBean.getAnchor_room_id()) && !"0".equals(dynamicBean.getAnchor_room_id())) {
            if ("0".equals(dynamicBean.getAnchor_is_live())) {
                ivUserAvatarState.setVisibility(View.VISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
                Glide.with(NewDynamicDetailActivity.this).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
            } else {
                ivUserAvatarState.setVisibility(View.INVISIBLE);
                lottieUserAvatarState.setVisibility(View.VISIBLE);
                lottieUserAvatarState.setImageAssetsFolder("images");
                lottieUserAvatarState.setAnimation("user_living.json");
                lottieUserAvatarState.setRepeatMode(LottieDrawable.RESTART);
                lottieUserAvatarState.setRepeatCount(LottieDrawable.INFINITE);
                lottieUserAvatarState.playAnimation();
                Animation mAnimation = AnimationUtils.loadAnimation(NewDynamicDetailActivity.this, R.anim.live_icon_scale);
                ivUserAvatarIcon.setAnimation(mAnimation);
                mAnimation.start();
            }
        } else {
            ivUserAvatarState.setVisibility(View.INVISIBLE);
            lottieUserAvatarState.setVisibility(View.INVISIBLE);

        }




    }

    void showDynamicInfo() {
        if (isFirstLoad) {
            isFirstLoad = false;
            showDynamicTab();
        }
        showDynamicContent();
        showDynamicPic();

        if ("1".equals(dynamicBean.getLocation_switch())) {
            ivDynamicDetailDistance.setVisibility(View.VISIBLE);
            tvDynamicDetailDistance.setVisibility(View.GONE);
        } else {
            ivDynamicDetailDistance.setVisibility(View.GONE);
            tvDynamicDetailDistance.setVisibility(View.VISIBLE);
            tvDynamicDetailDistance.setText(dynamicBean.getDistance() + "km");
        }
        tvDynamicDetailTime.setText(dynamicBean.getAddtime());


        if ("1".equals(dynamicBean.getIs_likeliar())) {
            tvLikeLiar.setVisibility(View.VISIBLE);
            Log.v("NewDynamicLike",dynamicBean.getIs_likeliar() +"--"+  (tvLikeLiar.getVisibility() == View.VISIBLE));
        } else {
            tvLikeLiar.setVisibility(View.INVISIBLE);
            Log.v("NewDynamicLike1",dynamicBean.getIs_likeliar() +"--"+  (tvLikeLiar.getVisibility() == View.VISIBLE));
        }
    }

    void showDynamicContent() {
        if (!TextUtil.isEmpty(dynamicBean.getTopictitle())) {
            llItemDynamicTopic.setVisibility(View.VISIBLE);
            tvItemDynamicTopic.setText(dynamicBean.getTopictitle());
            llItemDynamicTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewDynamicDetailActivity.this, TopicDetailActivity.class);
                    intent.putExtra("tid", dynamicBean.getTid());
                    intent.putExtra("topictitle", dynamicBean.getTopictitle());
                    startActivity(intent);
                }
            });
        } else {
            llItemDynamicTopic.setVisibility(View.GONE);
        }
        if (!TextUtil.isEmpty(dynamicBean.getContent())) {
            tvDynamicDetailContent.setOnTouchListener(new LinkMovementMethodOverride());
            SpannableString spannableString = NormalUtilKt.toDynamicSpannable(dynamicBean.getContent(),
                    dynamicBean.getAtuname(),dynamicBean.getAtuid());
            tvDynamicDetailContent.setText(spannableString);
        } else {
            tvDynamicDetailContent.setVisibility(View.GONE);
        }
    }

    void showDynamicPic() {
        if (!TextUtil.isEmpty(dynamicBean.getPlayUrl())) {
            ivDynamicDetailVideoPlay.setVisibility(View.VISIBLE);
            showImageCover(dynamicBean.getCoverUrl());
        } else {
            ivDynamicDetailVideoPlay.setVisibility(View.GONE);
            if (dynamicBean.getPic().size() == 0) {
                flDynamicDetailContent.setVisibility(View.GONE);
            } else {
                flDynamicDetailContent.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.placeholder(R.drawable.rc_image_error);
                if (dynamicBean.getPic().size() == 1) {
                    ivNineDynamicDetailPic.setVisibility(View.GONE);
                    ivDynamicDetailPic.setVisibility(View.VISIBLE);
                    showImageCover(dynamicBean.getSypic().get(0));
                } else {
                    ivNineDynamicDetailPic.setVisibility(View.VISIBLE);
                    ivDynamicDetailPic.setVisibility(View.GONE);
                    NineGridUtils.loadPics(NewDynamicDetailActivity.this, dynamicBean.getSypic(), dynamicBean.getPic(), ivNineDynamicDetailPic, ivDynamicDetailPic);
                }
            }
        }

    }

    void showDynamicTab() {
        thumbFragment = DynamicDetailThumbFragment.newInstance();
        commentFragment = DynamicDetailCommentFragment.newInstance();
        rewordFragment = DynamicDetailRewordFragment.newInstance();
        pushFragment = DynamicDetailPushFragment.newInstance();
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(thumbFragment);
        fragments.add(commentFragment);
        fragments.add(rewordFragment);
        fragments.add(pushFragment);
        FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

        };
        viewPagerDynamicDetail.setAdapter(fragmentStatePagerAdapter);
        tabDynamicDetail.setupWithViewPager(viewPagerDynamicDetail);
        titles = new ArrayList<>();
        titles.add("点赞 " + dynamicBean.getLaudnum());
        titles.add("评论 " + dynamicBean.getComnum());
        titles.add("打赏 " + dynamicBean.getRewardnum());
        thumbStr = dynamicBean.getLaudnum();
        commentStr = dynamicBean.getComnum();
        rewordStr = dynamicBean.getRewardnum();
        if (dynamicBean.getTopnum().equals(dynamicBean.getAlltopnums())) {
            titles.add("推顶 " + dynamicBean.getTopnum());
            pushTopStr = dynamicBean.getTopnum();
        } else {
            titles.add("推顶 " + dynamicBean.getTopnum() + "/" + dynamicBean.getAlltopnums());
            pushTopStr = dynamicBean.getTopnum() + "/" + dynamicBean.getAlltopnums();
        }
        for (int i = 0; i < tabDynamicDetail.getTabCount(); i++) {
            tabDynamicDetail.getTabAt(i).setCustomView(getTabView(i));
        }
        tabDynamicDetail.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                View view = tabView.findViewById(R.id.tab_item_textview);
                View bottomView = tabView.findViewById(R.id.tab_item_bottom);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(16);
                    ((TextView) view).setTextColor(ContextCompat.getColor(NewDynamicDetailActivity.this, R.color.titleBlack));
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
                    ((TextView) view).setTextColor(ContextCompat.getColor(NewDynamicDetailActivity.this, R.color.normalGray));
                    ((TextView) view).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    bottomView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerDynamicDetail.setCurrentItem(1);
        viewPagerDynamicDetail.setOffscreenPageLimit(4);
        if (dynamicBean.getLaudstate() == 1) {
            Drawable drawable = getResources().getDrawable(R.drawable.user_thumb_up_done);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDynamicDetailThumbUp.setCompoundDrawables(drawable, null, null, null);
            tvDynamicDetailThumbUp.setTextColor(getResources().getColor(R.color.purple_main));
        }
    }

    class Clickable extends ClickableSpan {

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NewDynamicDetailActivity.this, TopicDetailActivity.class);
            intent.putExtra("tid", dynamicBean.getTid());
            intent.putExtra("topictitle", dynamicBean.getTopictitle());
            startActivity(intent);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.purple_main));
            ds.setTypeface(Typeface.DEFAULT);
        }

    }

    private Display defaultDisplay;

    void suitView(View view, int width, int height) {
        float w = defaultDisplay.getWidth();
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.width = (int) w;
        para.height = (int) (w * height / width);
        view.setLayoutParams(para);
    }

    View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_dynamic_detail, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(titles.get(index));
        return view;
    }

    void setListener() {
        initAlertListener();
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        srlDynamicDetail.setEnableLoadMore(false);
        srlDynamicDetail.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (thumbFragment != null) {
                    thumbFragment.refreshData();
                }
                if (commentFragment != null) {
                    commentFragment.refreshData();
                }
                if (rewordFragment != null) {
                    rewordFragment.refreshData();
                }
                if (pushFragment != null) {
                    pushFragment.refreshData();
                }
                //srlDynamicDetail.finishRefresh(500);
                srlDynamicDetail.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dynamicBean == null) {
                            getData();
                        }
                        srlDynamicDetail.finishRefresh();
                    }
                }, 500);
            }
        });

        llDynamicDetailComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicBean == null) {
                    return;
                }
                setOtherInfo("", "", "");
                // showPopupcomment();
                showCommentDialogFragment();
            }
        });

        llDynamicDetailThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicBean == null) {
                    return;
                }
                thumbUp();
            }
        });

        llDynamicDetailReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicBean == null) {
                    return;
                }
                showRewardPop();
            }
        });

        llDynamicDetailPushTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicBean == null) {
                    return;
                }
                showTopCardPop(did, 0);
            }
        });

        ivDynamicDetailPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicBean == null) {
                    return;
                }
                if (!TextUtil.isEmpty(dynamicBean.getPlayUrl())) {
                    Intent intent = new Intent(NewDynamicDetailActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("video_url", dynamicBean.getPlayUrl());
                    intent.putExtra("cover_url", dynamicBean.getCoverUrl());
                    startActivity(intent);
                } else {
                    ImageViewUtil.previewImage(NewDynamicDetailActivity.this, dynamicBean.getSypic().get(0));
                }
            }
        });

        ivNormalTitleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicBean == null) {
                    return;
                }
                showMoreOption();
            }
        });

        ivUserAvatarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDynamicDetailActivity.this, UserInfoActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        KeyBoardListenerHelper keyBoardListenerHelper = new KeyBoardListenerHelper(this);
        keyBoardListenerHelper.setOnKeyBoardChangeListener(new KeyBoardListenerHelper.OnKeyBoardChangeListener() {
            @Override
            public void OnKeyBoardChange(boolean isShow, int keyBoardHeight) {
                if (isShow && llDynamicDetailBottom.getVisibility() == View.VISIBLE) {
                    llDynamicDetailBottom.setVisibility(View.GONE);
                } else if (!isShow && llDynamicDetailBottom.getVisibility() == View.GONE) {
                    llDynamicDetailBottom.setVisibility(View.VISIBLE);
                }
            }
        });


        constraintLayoutAudienceLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDynamicDetailActivity.this, MyLiveLevelActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });

        constraintLayoutAnchorLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDynamicDetailActivity.this, MyLiveLevelActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
    }

    //快捷回复相关
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private View rl_input_container;
    private InputMethodManager mInputManager;
    private String did, cmid, currentContent, currentOuid, currentName;
    List<String> atunamelist = new ArrayList<>();
    List<String> atuidlist = new ArrayList<>();
    boolean isSetKeyListener = false;

    @SuppressLint("WrongConstant")
    public void showPopupcomment() {
        if (popupView == null) {
            //加载评论框的资源文件
            popupView = LayoutInflater.from(NewDynamicDetailActivity.this).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        if (!TextUtil.isEmpty(currentName)) {
            inputComment.setHint("回复" + currentName);
        } else {
            inputComment.setHint("点击输入你的评论");
        }
        btn_submit = (TextView) popupView.findViewById(R.id.btn_confirm);
        //inputComment.setText("");
        rl_input_container = popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputComment.setFocusable(true);
                        inputComment.setFocusableInTouchMode(true);
                        inputComment.requestFocus();
                        mInputManager = (InputMethodManager) NewDynamicDetailActivity.this.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (mInputManager != null && mInputManager.isActive()) {
                            mInputManager.showSoftInput(inputComment, 0);
                        }
                    }
                });

            }
        }, 200);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    popupWindow.dismiss();
                return false;
            }
        });
        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(llDynamicDetailBottom, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {
                if (mInputManager != null && mInputManager.isActive()) {
                    popupWindow = null;
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputManager != null && mInputManager.isActive()) {
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                popupWindow.dismiss();
            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nInputContentText = inputComment.getText().toString().trim();
                if (nInputContentText == null || "".equals(nInputContentText)) {
                    return;
                }
                currentContent = nInputContentText;
                sendComment();
                inputComment.setText("");
                if (mInputManager != null && mInputManager.isActive()) {
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                popupWindow.dismiss();
            }
        });


        setKeyListener();

    }

    private void setKeyListener() {
        if (isSetKeyListener) {
            return;
        } else {
            isSetKeyListener = true;
        }

        inputComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.length() > start)) {
                    return;
                }
                if ('@' == s.charAt(start) && count == 1) {
//                    Intent intent = new Intent(NewDynamicDetailActivity.this, AtFansActivity.class);
                    Intent intent = new Intent(NewDynamicDetailActivity.this, AtMemberActivity.class);
                    intent.putExtra("type", "commentDetail");
                    startActivityForResult(intent, 100);
                    return;
                }

                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
                    if ('@' == s.charAt(start - 1)) {
                        inputComment.getText().delete(start - 1, start);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        inputComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = inputComment.getSelectionStart();

                ATSpan[] atSpans = inputComment.getText().getSpans(0, inputComment.getText().length(), ATSpan.class);
                int length = atSpans.length;

                if (0 == length) {
                    return;

                }

                for (ATSpan atSpan : atSpans) {
                    int start = inputComment.getText().getSpanStart(atSpan);
                    int end = inputComment.getText().getSpanEnd(atSpan);
                    if (selectionStart >= start && selectionStart <= end) {
                        inputComment.setSelection(end);
                        return;
                    }
                }
            }
        });

        inputComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = inputComment.getSelectionStart();

                    ATSpan[] atSpans = inputComment.getText().getSpans(0, inputComment.getText().length(), ATSpan.class);
                    int length = atSpans.length;

                    if (0 == length) {
                        return false;
                    }

                    for (ATSpan atSpan : atSpans) {
                        int start = inputComment.getText().getSpanStart(atSpan);
                        int end = inputComment.getText().getSpanEnd(atSpan);
                        if (selectionStart >= start + 1 && selectionStart <= end) {
                            inputComment.getText().delete(start + 1, end);
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

    public void setOtherInfo(String currentName, String otherId, String cmid) {
        this.currentName = currentName;
        this.currentOuid = otherId;
        this.cmid = cmid;
    }

    void showTopCardPop(String did, int pos) {
        final PushTopCardPop pushTopCardPop = new PushTopCardPop(NewDynamicDetailActivity.this, did, pos);
        pushTopCardPop.showPopupWindow();
        pushTopCardPop.setOnPushTopCardSuccessListener(new PushTopCardPop.OnPushTopCardSuccessListener() {
            @Override
            public void onPushTopCardSuc(int num, int allNum) {
                int topNum = Integer.parseInt(dynamicBean.getTopnum()) + num;
                int topAllNum = Integer.parseInt(dynamicBean.getAlltopnums()) + allNum;
                dynamicBean.setTopnum(String.valueOf(topNum));
                dynamicBean.setAlltopnums(String.valueOf(topAllNum));
                if (topNum == topAllNum) {
                    ((TextView) tabDynamicDetail.getTabAt(3).getCustomView().findViewById(R.id.tab_item_textview)).setText("推顶 " + dynamicBean.getTopnum());
                    pushTopStr = dynamicBean.getTopnum();
                } else {
                    ((TextView) tabDynamicDetail.getTabAt(3).getCustomView().findViewById(R.id.tab_item_textview)).setText("推顶 " + dynamicBean.getTopnum() + "/" + dynamicBean.getAlltopnums());
                    pushTopStr = dynamicBean.getTopnum() + "/" + dynamicBean.getAlltopnums();
                }
                pushFragment.refreshData();
            }
        });
    }

    void showRewardPop() {
//        final int dynamicPos = getIntent().getIntExtra("pos", -1);
//        DashangDialogNew dashangDialogNew = new DashangDialogNew(this, did, dynamicPos, "0", uid);
//        dashangDialogNew.setOnSimpleItemListener(new OnSimpleItemListener() {
//            @Override
//            public void onItemListener(int position) {
//                dynamicBean.setRewardnum(String.valueOf(Integer.parseInt(dynamicBean.getRewardnum()) + position));
//                rewordStr = dynamicBean.getRewardnum();
//                ((TextView) tabDynamicDetail.getTabAt(2).getCustomView().findViewById(R.id.tab_item_textview)).setText("打赏 " + dynamicBean.getRewardnum());
//                rewordFragment.refreshData();
//            }
//        });
            GiftPanelPop2 giftPanelPop = new GiftPanelPop2(this, 1, did, uid);
        giftPanelPop.showPopupWindow();
        giftPanelPop.setOnGiftSendSucListener(new GiftPanelPop2.OnGiftSendSucListener() {
            @Override
            public void onGiftSendSuc(@NotNull String orderId, @NotNull int value) {
                //ToastUtil.show("赠送成功");
                dynamicBean.setRewardnum(String.valueOf(Integer.parseInt(dynamicBean.getRewardnum()) + value));
                rewordStr = dynamicBean.getRewardnum();
                ((TextView) tabDynamicDetail.getTabAt(2).getCustomView().findViewById(R.id.tab_item_textview)).setText("打赏 " + dynamicBean.getRewardnum());
                rewordFragment.refreshData();
            }
        });

    }

    void sendComment() {
        //btn_submit.setEnabled(false);
        //String content = inputComment.getText().toString();
        if (commentDialogFragment == null) {
            return;
        }
        commentDialogFragment.getSendBtn().setEnabled(false);
        String content = commentDialogFragment.getInputText().getText().toString();
        String platuid = "";
        String platuname = "";
        for (int i = 0; i < atuidlist.size(); i++) {
            platuid += atuidlist.get(i) + ",";
            platuname += atunamelist.get(i) + ",";
        }
        if (atuidlist.size() > 0) {
            platuid = platuid.substring(0, platuid.length() - 1);
            platuname = platuname.substring(0, platuname.length() - 1);
        }
        HttpHelper.getInstance().sendComment(content, did, cmid, currentOuid, platuid, platuname, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(NewDynamicDetailActivity.this)) {
                    return;
                }
                //btn_submit.setEnabled(true);
                ToastUtil.show(NewDynamicDetailActivity.this, "发送成功");
                dynamicBean.setComnum(String.valueOf(Integer.parseInt(dynamicBean.getComnum()) + 1));
                commentStr = dynamicBean.getComnum();
                ((TextView) tabDynamicDetail.getTabAt(1).getCustomView().findViewById(R.id.tab_item_textview)).setText("评论 " + dynamicBean.getComnum());
                commentFragment.refreshData();
                //inputComment.setText("");
                if (commentDialogFragment != null) {
                    if (commentDialogFragment.getSendBtn() != null) {
                        commentDialogFragment.getSendBtn().setEnabled(true);
                    }
                    if (commentDialogFragment.getInputText() != null) {
                        commentDialogFragment.getInputText().setText("");
                    }
                    commentDialogFragment.dismiss();
                }
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(NewDynamicDetailActivity.this)) {
                    return;
                }
                ToastUtil.show(NewDynamicDetailActivity.this, msg);
                if (commentDialogFragment != null) {
                    commentDialogFragment.getSendBtn().setEnabled(true);
                }
                // btn_submit.setEnabled(true);
            }
        });
    }

    void thumbUp() {
        if (dynamicBean == null) {
            return;
        }
        if (dynamicBean.getLaudstate() == 0) {
            HttpHelper.getInstance().thumbUpDynamic(did, new HttpListener() {
                @Override
                public void onSuccess(String data) {
                    dynamicBean.setLaudnum(String.valueOf(Integer.parseInt(dynamicBean.getLaudnum()) + 1));
                    thumbStr = dynamicBean.getLaudnum();
                    ((TextView) tabDynamicDetail.getTabAt(0).getCustomView().findViewById(R.id.tab_item_textview)).setText("点赞 " + dynamicBean.getLaudnum());
                    Drawable drawable = getResources().getDrawable(R.drawable.user_thumb_up_done);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvDynamicDetailThumbUp.setCompoundDrawables(drawable, null, null, null);
                    tvDynamicDetailThumbUp.setTextColor(getResources().getColor(R.color.purple_main));
                    if (thumbFragment != null) {
                        thumbFragment.refreshData();
                    }
                }

                @Override
                public void onFail(String msg) {

                }
            });
        } else {
            ToastUtil.show(NewDynamicDetailActivity.this, "您已经点赞过了");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
//            intent.putExtra("atStr", uidstr);
//            intent.putExtra("atSize", list.size()+"");
            if (SafeCheckUtil.isActivityFinish(NewDynamicDetailActivity.this)) {
                return;
            }
            LogUtil.d(data.getStringExtra("atStr"));
            LogUtil.d(data.getStringExtra("atNameStr"));
            LogUtil.d(data.getStringExtra("atSize"));
            String atIds = data.getStringExtra("atStr");
            String atNames = data.getStringExtra("atNameStr");
            String[] ids = atIds.split(",");
            String[] names = atNames.split(",");
            if (commentDialogFragment == null) {
                return;
            }

//            if (inputComment.getText().toString().equals("@")) {
//                inputComment.setText(" ");
//            }
            if ("@".equals(commentDialogFragment.getInputText().getText().toString())) {
                commentDialogFragment.getInputText().setText(" ");
            }
            for (int i = 0; i < ids.length; i++) {
                if (!TextUtil.isEmpty(ids[i])) {
                    atuidlist.add(ids[i]);
                }
            }
            for (int i = 0; i < names.length; i++) {
                if (!TextUtil.isEmpty(names[i])) {
                    atunamelist.add("@" + names[i]);
//                    ATSpan atSpan = new ATSpan(uid);
                    SpannableString span = new SpannableString("@" + names[i] + " ");
                    //  whl  群组颜色
                    if (names[i].contains("[群]")){
                        ATSpan atSpan = new ATSpan(uid,"群");
                        span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }else if (names[i].contains("[高端]")){
                        ATSpan atSpan = new ATSpan(uid,"高端");
                        span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }else {
                        ATSpan atSpan = new ATSpan(uid);
                        span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
//                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    // inputComment.append(span);
                    commentDialogFragment.getInputText().append(span);
                }
            }
            // inputComment.setSelection(inputComment.getText().length());
            commentDialogFragment.getInputText().setSelection(commentDialogFragment.getInputText().getText().length());
        }
    }

    void showMoreOption() {
        String admin = (String) SharedPreferencesUtils.getParam(NewDynamicDetailActivity.this, "admin", "0");
        if ("1".equals(admin)) {
            //showAdminOption();
            showAdminMenuPop();
        } else {
            if (uid.equals(MyApp.uid)) {
                //showMineOption();
                showMineMenuPop();
            } else {
                //showNormalOption();
                showUserMenuPop();
            }
        }
    }

    private void initAlertListener() {
        alertItemListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                if (data.contains("置顶(vip)")) {
                    pushTopDynamic();
                } else if (data.contains("话题置顶")) {
                    pushTopTopic();
                } else if (data.contains("置顶")) {
                    dingDynamic(24);
                } else if (data.contains("推荐")) {
                    recommendDynamic();
                } else if (data.contains("隐藏")) {
                    hideDynamic();
                } else if (data.contains("编辑")) {
                    editDynamic();
                } else if (data.contains("分享")) {
                    //shareDynamic();
                    showSharePop();
                } else if (data.contains("收藏")) {
                    collectDynamic();
                } else if (data.contains("举报")) {
                    reportDynamic();
                } else if (data.contains("删除")) {
                    deleteDynamic();
                }
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    void showMineOption() {
        String pushStr = "置顶(vip)";
        String editStr = "编辑(svip)";
        String shareStr = "分享";
        String collectStr = "收藏";
        String deleteStr = "删除";
        if ("1".equals(dynamicBean.getStickstate())) {
            pushStr = "取消置顶(vip)";
        }
        if ("1".equals(dynamicBean.getCollectstate())) {
            collectStr = "取消收藏";
        }

        new AlertView(null, null, "取消", null,
                new String[]{pushStr, editStr, shareStr, collectStr, deleteStr}, this, AlertView.Style.ActionSheet, alertItemListener).show();
    }

    void showNormalOption() {
        String shareStr = "分享";
        String collectStr = "收藏";
        String reportStr = "举报";
        if ("1".equals(dynamicBean.getCollectstate())) {
            collectStr = "取消收藏";
        }
        new AlertView(null, null, "取消", null,
                new String[]{shareStr, collectStr, reportStr}, this, AlertView.Style.ActionSheet, alertItemListener).show();
    }

    void showAdminOption() {
        String signStr = "标记";
        String pushStr = "动态置顶";
        String pushTopicStr = "话题置顶";
        String recommendStr = "推荐";
        String hideStr = "隐藏";
        String editStr = "编辑";
        String shareStr = "分享";
        String collectStr = "收藏";
        String deleteStr = "删除";
        String pushTopStr = "置顶(vip)";
        if (dynamicBean == null) {
            return;
        }
        if ("1".equals(dynamicBean.getStickstate())) {
            pushTopStr = "取消置顶(vip)";
        }
        if ("1".equals(dynamicBean.getCollectstate())) {
            collectStr = "取消收藏";
        }
        if (!"0".equals(dynamicBean.getRecommendall())) {
            pushStr = "取消动态置顶";
        }
        if ("2".equals(dynamicBean.getTopic_topping_status())) {
            pushTopicStr = "取消话题置顶";
        }
        if (!"0".equals(dynamicBean.getRecommend())) {
            recommendStr = "取消推荐";
        }
        if ("1".equals(dynamicBean.getIs_hidden())) {
            hideStr = "取消隐藏";
        }
        if (uid.equals(MyApp.uid)) {
            deleteStr = "删除";
        } else {
            deleteStr = "举报";
        }
        if (MyApp.uid.equals(uid)) {
            new AlertView(null, null, "取消", null,
                    new String[]{signStr, pushStr, recommendStr, pushTopicStr, hideStr, pushTopStr, editStr, shareStr, collectStr, deleteStr},
                    this, AlertView.Style.ActionSheet, alertItemListener).show();
        } else {
            new AlertView(null, null, "取消", null,
                    new String[]{signStr, pushStr, recommendStr, pushTopicStr, hideStr, editStr, shareStr, collectStr, deleteStr},
                    this, AlertView.Style.ActionSheet, alertItemListener).show();
        }
    }

    public void showAdminRecommendPop() {
        new AlertView("选择置顶时间", null, "取消", null,
                new String[]{"1小时", "2小时", "3小时", "6小时", "12小时", "24小时"}, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                switch (position) {
                    case 0:
                        dingDynamic(1);
                        break;
                    case 1:
                        dingDynamic(2);
                        break;
                    case 2:
                        dingDynamic(3);
                        break;
                    case 3:
                        dingDynamic(6);
                        break;
                    case 4:
                        dingDynamic(12);
                        break;
                    case 5:
                        dingDynamic(24);
                        break;
                }
            }
        }).show();
    }

    public void dingDynamic(int time) {
        HttpHelper.getInstance().recommendAllDynamic(did, "0".equals(dynamicBean.getRecommendall()), time, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if ("0".equals(dynamicBean.getRecommendall())) {
                    showToast("置顶成功");
                    dynamicBean.setRecommendall("1");
                } else {
                    showToast("取消置顶");
                    dynamicBean.setRecommendall("0");
                }
                EventBus.getDefault().post(new UserDynamicRefreshBean());
            }

            @Override
            public void onFail(String msg) {
                showToast(msg);
            }
        });
    }

    public void pushTopDynamic() {
        String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        if ("0".equals(vip)) {
            new VipDialog(NewDynamicDetailActivity.this, "会员才能使用置顶功能哦~");
            return;
        }
        HttpHelper.getInstance().pushTopDynamic(did, "0".equals(dynamicBean.getStickstate()), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if ("0".equals(dynamicBean.getStickstate())) {
                    showToast("置顶成功");
                    dynamicBean.setStickstate("1");
                } else {
                    showToast("取消置顶");
                    dynamicBean.setStickstate("0");
                }
                getData();
                EventBus.getDefault().post(new UserDynamicRefreshBean());
            }

            @Override
            public void onFail(String msg) {
                if (msg.contains("VIP")) {
                    new VipDialog(NewDynamicDetailActivity.this, "会员才能使用置顶功能哦~");
                } else {
                    ToastUtil.show(NewDynamicDetailActivity.this, msg);
                }
            }
        });
    }

    public void recommendDynamic() {
        HttpHelper.getInstance().recommendDynamic(did, "0".equals(dynamicBean.getRecommend()), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if ("0".equals(dynamicBean.getRecommend())) {
                    showToast("推荐成功");
                    dynamicBean.setRecommend("1");
                } else {
                    showToast("取消推荐");
                    dynamicBean.setRecommend("0");
                }
                EventBus.getDefault().post(new UserDynamicRefreshBean());
            }

            @Override
            public void onFail(String msg) {
                showToast(msg);
            }
        });
    }

    public void collectDynamic() {
        HttpHelper.getInstance().collectDynamic(did, "0".equals(dynamicBean.getCollectstate()), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if ("0".equals(dynamicBean.getCollectstate())) {
                    showToast("收藏成功");
                    dynamicBean.setCollectstate("1");
                } else {
                    showToast("取消收藏");
                    dynamicBean.setCollectstate("0");
                }
            }

            @Override
            public void onFail(String msg) {
                showToast(msg);
            }
        });
    }

    private void deleteDynamic() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (dynamicBean.getTopnum().equals(dynamicBean.getAlltopnums())) {
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
        } else {
            builder.setMessage("推顶任务尚未结束，删除后剩余未生效推顶卡将失效，确定删除动态吗?")
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
    }

    public void editDynamic() {
        String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        String svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
        String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
        //编辑动态
        if ("1".equals(admin) || "1".equals(svip)) {
            gotoEditDynamic();
        } else {
            new VipDialog(this, "编辑功能限SVIP会员可用~");
        }
    }

    void gotoEditDynamic() {
        Intent intent = new Intent(this, EditDynamicActivity.class);
        intent.putExtra("did", did);
        intent.putExtra("headurl", dynamicBean.getHead_pic());
        intent.putExtra("name", dynamicBean.getNickname());
        intent.putExtra("online", dynamicBean.getOnlinestate());
        intent.putExtra("shiming", dynamicBean.getRealname());
        intent.putExtra("readtimes", dynamicBean.getReadtimes());
        intent.putExtra("age", dynamicBean.getAge());
        intent.putExtra("role", dynamicBean.getRole());
        intent.putExtra("sex", dynamicBean.getSex());
        intent.putExtra("distance", dynamicBean.getDistance() + "");
        intent.putExtra("addtime", dynamicBean.getAddtime());
        intent.putExtra("content", dynamicBean.getContent());
        intent.putStringArrayListExtra("sypic", dynamicBean.getSypic());
        intent.putStringArrayListExtra("slpic", dynamicBean.getPic());
        intent.putExtra("fuid", dynamicBean.getUid());
        intent.putExtra("fvipannual", dynamicBean.getVipannual());
        intent.putExtra("fvolunteer", dynamicBean.getIs_volunteer());
        intent.putExtra("fadmin", dynamicBean.getIs_admin());
        intent.putExtra("fsvip", dynamicBean.getSvip());
        intent.putExtra("fsvipannual", dynamicBean.getSvipannual());
        intent.putExtra("fvip", dynamicBean.getVip());
        intent.putExtra("retcode", retcode);
        intent.putExtra("charmval", dynamicBean.getCharm_val());
        intent.putExtra("wealthwal", dynamicBean.getWealth_val());
        intent.putExtra("dynamicPos", intent.getIntExtra("pos", -1));
        intent.putExtra("tid", dynamicBean.getTid());
        intent.putExtra("topictitle", dynamicBean.getTopictitle());
        intent.putExtra("atuid", dynamicBean.getAtuid());
        intent.putExtra("atuname", dynamicBean.getAtuname());
        intent.putExtra("bkvip", dynamicBean.getBkvip());
        intent.putExtra("blvip", dynamicBean.getBlvip());
        intent.putExtra("is_display", "不限制"); // 区分是否是管理员编辑
        startActivity(intent);
    }

    public void shareDynamic() {
        showSharePop();
//        String pic = "";
//        if (dynamicBean.getPic().size() > 0) {
//            pic = dynamicBean.getPic().get(0);
//        }
//        SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic() + HttpUrl.ShareDynamicDetail + did, "来自圣魔的动态", dynamicBean.getNickname() + " 在圣魔发布了精彩的动态。", dynamicBean.getHead_pic(), 0, 1, did, dynamicBean.getContent(), pic, uid);
//        sharedPop.showAtLocation(llDynamicDetailBottom, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
//        //当弹出Popupwindow时，背景变半透明
//        params[0].alpha = 0.7f;
//        getWindow().setAttributes(params[0]);
//        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                params[0] = getWindow().getAttributes();
//                params[0].alpha = 1f;
//                getWindow().setAttributes(params[0]);
//            }
//        });
    }

    public void reportDynamic() {
        if ("0".equals(dynamicBean.getReportstate())) {
            Intent intent = new Intent(this, OtherReasonActivity.class);
            intent.putExtra("uid", MyApp.uid);
            intent.putExtra("did", did);
            startActivity(intent);
        } else {
            showToast("只能举报一次哦~");
        }
    }

    void delDynamic() {
        String admin = (String) SharedPreferencesUtils.getParam(NewDynamicDetailActivity.this, "admin", "0");
        HttpHelper.getInstance().deleteDynamic(did, MyApp.uid.equals(uid), "1".equals(admin), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                showToast("删除成功");
                EventBus.getDefault().post(new CallBackEvent());
                finish();
            }

            @Override
            public void onFail(String msg) {
                showToast(msg);
            }
        });
    }

    public void hideDynamic() {
        HttpHelper.getInstance().hideDynamic(did, "0".equals(dynamicBean.getIs_hidden()), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if ("0".equals(dynamicBean.getIs_hidden())) {
                    showToast("隐藏成功");
                    dynamicBean.setIs_hidden("1");
                    getData();
                } else {
                    showToast("取消隐藏");
                    dynamicBean.setIs_hidden("0");
                    getData();
                }
                EventBus.getDefault().post(new UserDynamicRefreshBean());
            }

            @Override
            public void onFail(String msg) {
                showToast(msg);
            }
        });
    }

    public void pushTopTopic() {
        if ("0".equals(dynamicBean.getTopic_topping_status())) {
            ToastUtil.show(NewDynamicDetailActivity.this, "无法置顶话题");
            return;
        }
        HttpHelper.getInstance().pushTopTopic("1".equals(dynamicBean.getTopic_topping_status()) ? "1" : "0", did, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(NewDynamicDetailActivity.this)) {
                    return;
                }
                if ("1".equals(dynamicBean.getTopic_topping_status())) {
                    ToastUtil.show(NewDynamicDetailActivity.this, "置顶话题成功");
                    dynamicBean.setTopic_topping_status("2");
                } else {
                    ToastUtil.show(NewDynamicDetailActivity.this, "取消置顶话题");
                    dynamicBean.setTopic_topping_status("1");
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (SafeCheckUtil.isActivityFinish(NewDynamicDetailActivity.this)) {
                    return;
                }
                ToastUtil.show(NewDynamicDetailActivity.this, msg);
            }
        });
    }

    void showToast(String msg) {
        ToastUtil.show(NewDynamicDetailActivity.this, msg);
    }

    void showImageCover(String url) {
        Glide.with(NewDynamicDetailActivity.this)
                .asBitmap()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_ARGB_8888)//设置图片解码格式
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int height = resource.getHeight();
                        int width = resource.getWidth();
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivDynamicDetailPic.getLayoutParams();
                        layoutParams.width = getScreenWidth(getApplicationContext()) - DensityUtil.dip2px(NewDynamicDetailActivity.this, 30);
                        layoutParams.height = layoutParams.width * height / width;
                        ivDynamicDetailPic.setLayoutParams(layoutParams);
                        ivDynamicDetailPic.setImageBitmap(resource);
                        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ivDynamicDetailPicRound.getLayoutParams();
                        layoutParams2.width = getScreenWidth(getApplicationContext()) - DensityUtil.dip2px(NewDynamicDetailActivity.this, 30);
                        layoutParams2.height = layoutParams.width * height / width;
                        ivDynamicDetailPicRound.setLayoutParams(layoutParams2);
                    }
                });
    }

    CommentDialogFragment commentDialogFragment;

    public void showCommentDialogFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (commentDialogFragment == null) {
            commentDialogFragment = new CommentDialogFragment();
            commentDialogFragment.show(fragmentManager, "comment");
        } else {
            commentDialogFragment.show(fragmentManager, "comment");
            llDynamicDetailComment.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtil.isEmpty(currentName)) {
                        commentDialogFragment.changeHint("回复" + currentName);
                    } else {
                        commentDialogFragment.changeHint("");
                    }
                }
            }, 500);

        }
        commentDialogFragment.setOnCommentSendListener(new CommentDialogFragment.OnCommentSendListener() {
            @Override
            public void onCommentAt() {
//                Intent intent = new Intent(NewDynamicDetailActivity.this, AtFansActivity.class);
                Intent intent = new Intent(NewDynamicDetailActivity.this, AtMemberActivity.class);
                intent.putExtra("type", "commentDetail");
                startActivityForResult(intent, 100);
            }

            @Override
            public void onCommentSend(String content) {
                sendComment();
            }
        });

    }


    void showMineMenuPop() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        if ("1".equals(dynamicBean.getStickstate())) {
            normalMenuItemList.add(new NormalMenuItem(1, "取消置顶"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(1, "置顶"));
        }
        normalMenuItemList.add(new NormalMenuItem(2, "编辑"));
        normalMenuItemList.add(new NormalMenuItem(0, "分享"));
        if ("1".equals(dynamicBean.getCollectstate())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消收藏"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "收藏"));
        }
        normalMenuItemList.add(new NormalMenuItem(0, "删除"));
        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(NewDynamicDetailActivity.this, normalMenuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                String data = normalMenuItemList.get(position).getContent();
                if (data.contains("置顶")) {
                    pushTopDynamic();
                }
//                else if (data.contains("话题置顶")) {
//                    pushTopTopic();
//                } else if (data.contains("置顶")) {
//                    dingDynamic();
//                }
                else if (data.contains("推荐")) {
                    recommendDynamic();
                } else if (data.contains("隐藏")) {
                    hideDynamic();
                } else if (data.contains("编辑")) {
                    editDynamic();
                } else if (data.contains("分享")) {
                    shareDynamic();
                } else if (data.contains("收藏")) {
                    collectDynamic();
                } else if (data.contains("举报")) {
                    reportDynamic();
                } else if (data.contains("删除")) {
                    deleteDynamic();
                }
                normalMenuPopup.dismiss();
            }
        });
    }

    void showUserMenuPop() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "分享"));
        if ("1".equals(dynamicBean.getCollectstate())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消收藏"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "收藏"));
        }
        normalMenuItemList.add(new NormalMenuItem(0, "举报"));
        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(NewDynamicDetailActivity.this, normalMenuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                String data = normalMenuItemList.get(position).getContent();
                if (data.contains("置顶")) {
                    pushTopDynamic();
                } else if (data.contains("话题置顶")) {
                    pushTopTopic();
                } else if (data.contains("推荐")) {
                    recommendDynamic();
                } else if (data.contains("隐藏")) {
                    hideDynamic();
                } else if (data.contains("编辑")) {
                    editDynamic();
                } else if (data.contains("分享")) {
                    shareDynamic();
                } else if (data.contains("收藏")) {
                    collectDynamic();
                } else if (data.contains("举报")) {
                    reportDynamic();
                } else if (data.contains("删除")) {
                    deleteDynamic();
                }
                normalMenuPopup.dismiss();
            }
        });
    }

    void showAdminMenuPop() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "标记"));
        if (!"0".equals(dynamicBean.getRecommendall())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消动态置顶"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "动态置顶"));
        }
        if ("2".equals(dynamicBean.getTopic_topping_status())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消话题置顶"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "话题置顶"));
        }
        if (!"0".equals(dynamicBean.getRecommend())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消推荐"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "推荐"));
        }
        if ("1".equals(dynamicBean.getIs_hidden())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消隐藏"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "隐藏"));
        }
        normalMenuItemList.add(new NormalMenuItem(2, "编辑"));
        normalMenuItemList.add(new NormalMenuItem(0, "分享"));
        if ("1".equals(dynamicBean.getCollectstate())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消收藏"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "收藏"));
        }
        normalMenuItemList.add(new NormalMenuItem(0, "删除"));
        final NormalMenuPopup normalMenuPopup = new NormalMenuPopup(NewDynamicDetailActivity.this, normalMenuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                String data = normalMenuItemList.get(position).getContent();
                if (data.contains("话题置顶")) {
                    pushTopTopic();
                } else if (data.contains("动态置顶")) {
                    //dingDynamic();
                    if ("0".equals(dynamicBean.getRecommendall())) {
                        showAdminRecommendPop();
                    } else {
                        dingDynamic(24);
                    }
                } else if (data.contains("推荐")) {
                    recommendDynamic();
                } else if (data.contains("隐藏")) {
                    hideDynamic();
                } else if (data.contains("编辑")) {
                    editDynamic();
                } else if (data.contains("分享")) {
                    shareDynamic();
                } else if (data.contains("收藏")) {
                    collectDynamic();
                } else if (data.contains("举报")) {
                    reportDynamic();
                } else if (data.contains("删除")) {
                    deleteDynamic();
                }
                normalMenuPopup.dismiss();
            }
        });

    }

    private void showSharePop() {
        NormalShareBean normalShareBean = new NormalShareBean(1, did,
                "来自圣魔的动态",
                dynamicBean.getContent(),
                dynamicBean.getNickname() + " 在圣魔发布了精彩的动态",
                HttpUrl.NetPic() + HttpUrl.ShareDynamicDetail + did,
                dynamicBean.getHead_pic());
        NormalSharePop normalSharePop = new NormalSharePop(NewDynamicDetailActivity.this, normalShareBean, true);
        normalSharePop.showPopupWindow();
    }

}


