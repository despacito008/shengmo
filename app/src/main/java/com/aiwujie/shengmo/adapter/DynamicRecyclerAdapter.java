package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.SinglePictureParameter;
import com.aiwujie.shengmo.customview.NineGridViewWrapper;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop2;
import com.aiwujie.shengmo.kt.util.NormalUtilKt;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.BitmapUtils;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.NineGridUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.videoplay.VideoActivity;
import com.aiwujie.shengmo.videoplay.VideoPlayer;
import com.aiwujie.shengmo.view.PushTopCardPop;
import com.aiwujie.shengmo.view.roundview.RoundRelativeLayout;
import com.aiwujie.shengmo.zdyview.ATGroupSpan;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dinuscxj.ellipsize.EllipsizeTextView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class DynamicRecyclerAdapter extends RecyclerView.Adapter<DynamicRecyclerAdapter.DynamicHolder> implements View.OnClickListener {
    private Context context;
    private List<DynamicListData.DataBean> dynamicList;
    private int retcode;
    private String type;
    private String myisadmin = "0";

    private VideoPlayer videoPlayer;
    private String currentPlayUrl = "-1";

    Map<Integer, SinglePictureParameter> map = new HashMap<>();
    int toppos;
    String topdid;
    String topicId = "";
    Fragment fragment;

    public DynamicRecyclerAdapter(Context context, List<DynamicListData.DataBean> dynamicList, int retcode, String type) {
        this.context = context;
        this.dynamicList = dynamicList;
        this.retcode = retcode;
        this.type = type;
        videoPlayer = new VideoPlayer();
    }

    public DynamicRecyclerAdapter(Context context, Fragment fragment, List<DynamicListData.DataBean> dynamicList, int retcode, String type) {
        this.context = context;
        this.fragment = fragment;
        this.dynamicList = dynamicList;
        this.retcode = retcode;
        this.type = type;
        videoPlayer = new VideoPlayer();
    }

    public DynamicRecyclerAdapter(Context context, List<DynamicListData.DataBean> dynamicList, int retcode, String type, String topicId) {
        this.context = context;
        this.dynamicList = dynamicList;
        this.retcode = retcode;
        this.type = type;
        this.topicId = topicId;
        videoPlayer = new VideoPlayer();
    }

    @Override
    public DynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_dynamic, parent, false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return dynamicList.size();
    }

    class DynamicHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.item_dynamic_listview_intro)
        EllipsizeTextView itemDynamicListviewIntro;
        @BindView(R.id.item_dynamic_nineGrid)
        NineGridView itemDynamicNineGrid;
        @BindView(R.id.item_dynamic_pic)
        NineGridViewWrapper itemDynamicPic;
        @BindView(R.id.video_cover_img)
        ImageView videoCoverImg;
        @BindView(R.id.play_btn)
        ImageView playBtn;
        @BindView(R.id.video_cover_texture)
        TextureView videoCoverTexture;
        @BindView(R.id.iv_round_cover)
        ImageView ivRoundCover;
        @BindView(R.id.video_player_layout)
        RoundRelativeLayout videoPlayerLayout;
        @BindView(R.id.item_dynamic_layou_pic)
        FrameLayout itemDynamicLayouPic;
        @BindView(R.id.iv_dynamic_listview_distance)
        ImageView ivDynamicListviewDistance;
        @BindView(R.id.item_dynamic_listview_distance)
        TextView itemDynamicListviewDistance;
        @BindView(R.id.item_dynamic_listview_time)
        TextView itemDynamicListviewTime;
        @BindView(R.id.item_dynamic_listview_zan)
        TextView itemDynamicListviewZan;
        @BindView(R.id.item_dynamic_listview_pinglun)
        TextView itemDynamicListviewPinglun;
        @BindView(R.id.item_dynamic_listview_dashang)
        TextView itemDynamicListviewDashang;
        @BindView(R.id.item_dynamic_listview_tuiding)
        TextView itemDynamicListviewTuiding;
        @BindView(R.id.item_dynamic_listview_llcomentone_name)
        TextView itemDynamicListviewLlcomentoneName;
        @BindView(R.id.item_dynamic_listview_llcomentone_content)
        TextView itemDynamicListviewLlcomentoneContent;
        @BindView(R.id.item_dynamic_listview_llcomentone)
        LinearLayout itemDynamicListviewLlcomentone;
        @BindView(R.id.item_dynamic_listview_llcomenttwo_name)
        TextView itemDynamicListviewLlcomenttwoName;
        @BindView(R.id.item_dynamic_listview_llcomenttwo_content)
        TextView itemDynamicListviewLlcomenttwoContent;
        @BindView(R.id.item_dynamic_listview_llcomenttwo)
        LinearLayout itemDynamicListviewLlcomenttwo;
        @BindView(R.id.item_dynamic_listview_more)
        LinearLayout itemDynamicListviewMore;
        @BindView(R.id.item_dynamic_listview_ll)
        LinearLayout itemDynamicListviewLl;
        @BindView(R.id.ll_adminmarke)
        LinearLayout llAdminmarke;
        @BindView(R.id.tv_likeliar)
        TextView tvLikeLiar;

        public DynamicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            final DynamicListData.DataBean data = dynamicList.get(position);
            if (!data.getRecommend().equals("0")) {
                ivRecommend.setVisibility(View.VISIBLE);
                ivItemDynamicReadCount.setVisibility(View.VISIBLE);
                itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
                itemListviewDynamicReadcount.setText(data.getReadtimes());
                itemListviewDynamicReadcount.setTextColor(0x8a000000);
                if (Integer.parseInt(data.getReadtimes()) > 10000) {
                    itemListviewDynamicReadcount.setTextColor(0xffff7857);
                } else if (Integer.parseInt(data.getReadtimes()) > 99999) {
                    itemListviewDynamicReadcount.setText("100000+");
                }
            } else {
                ivRecommend.setVisibility(View.INVISIBLE);
                itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
                ivItemDynamicReadCount.setVisibility(View.INVISIBLE);
            }
            if (!"0".equals(data.getTopnum())) {
                ivTop.setVisibility(View.VISIBLE);
            } else {
                ivTop.setVisibility(View.GONE);
            }

            if ("3".equals(type)) {  //自己看
                if ("1".equals(data.getStickstate()) && data.getUid().equals(MyApp.uid)) {
                    ivDing.setVisibility(View.VISIBLE);
                } else {
                    ivDing.setVisibility(View.GONE);
                }
                if ("1".equals(data.getIs_hidden()) && ((data.getUid().equals(MyApp.uid) || "1".equals(MyApp.isAdmin)))) {
                    ivLock.setVisibility(View.VISIBLE);
                } else {
                    ivLock.setVisibility(View.GONE);
                }
            } else {
                if (!TextUtil.isEmpty(data.getRecommendall()) && !"0".equals(data.getRecommendall())) {
                    ivDing.setVisibility(View.VISIBLE);
                } else {
                    ivDing.setVisibility(View.GONE);
                }
            }

            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                ivUserAvatarIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                if (fragment == null) {
                    ImageLoader.loadCircleImage(context, data.getHead_pic(), ivUserAvatarIcon, R.mipmap.morentouxiang);
                } else {
                    ImageLoader.loadCircleImage(fragment, data.getHead_pic(), ivUserAvatarIcon, R.mipmap.morentouxiang);
                }
            }

            if (!TextUtil.isEmpty(data.getAnchor_room_id()) && !"0".equals(data.getAnchor_room_id())) {
                ivUserAvatarState.setVisibility(View.VISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
                if ("0".equals(data.getAnchor_is_live())) {
                    if (fragment == null) {
                        Glide.with(context).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
                    } else {
                        Glide.with(fragment).load(R.drawable.ic_user_liver).into(ivUserAvatarState);
                    }
                } else {
                    lottieUserAvatarState.setVisibility(View.VISIBLE);
                    lottieUserAvatarState.setImageAssetsFolder("images");
                    lottieUserAvatarState.setAnimation("user_living.json");
                    lottieUserAvatarState.loop(true);
                    lottieUserAvatarState.playAnimation();
                }
            } else {
                ivUserAvatarState.setVisibility(View.INVISIBLE);
                lottieUserAvatarState.setVisibility(View.INVISIBLE);
            }


            if (retcode == 2000) {
                if (data.getLocation_switch().equals("1")) {
                    itemDynamicListviewDistance.setVisibility(View.GONE);
                    ivDynamicListviewDistance.setVisibility(View.VISIBLE);
                } else {
                    ivDynamicListviewDistance.setVisibility(View.GONE);
                    itemDynamicListviewDistance.setVisibility(View.VISIBLE);
                    itemDynamicListviewDistance.setText(data.getDistance() + "km");
                }
            } else {
                itemDynamicListviewDistance.setVisibility(View.GONE);
                ivDynamicListviewDistance.setVisibility(View.VISIBLE);
            }
            tvLayoutUserNormalInfoAge.setText(data.getAge());
            if (data.getOnlinestate() == 0) {
                //itemDynamicListviewOnline.setVisibility(View.GONE);
                ivUserAvatarOnline.setVisibility(View.GONE);
            } else {
                //itemDynamicListviewOnline.setVisibility(View.VISIBLE);
                ivUserAvatarOnline.setVisibility(View.VISIBLE);
            }

            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            //UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), itemDynamicListviewVip);
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivUserAvatarLevel);
            // 判断用户的权限来看是否显示浏览数
//        if(data.getVip().equals("1")||data.getIs_admin().equals("1")||data.getRealname().equals("1")){
//            itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
//            if(!data.getReadtimes().equals("")) {
//                if (Integer.parseInt(data.getReadtimes()) > 99999) {
//                    itemListviewDynamicReadcount.setText("浏览 100000+");
//                } else {
//                    itemListviewDynamicReadcount.setText("浏览 " + data.getReadtimes());
//                }
//            }
//        }else{
//            itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
//        }


            if (TextUtil.isEmpty(data.getRealname()) || data.getRealname().equals("0")) {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.GONE);
            } else {
                ivLayoutUserNormalInfoAuthPic.setVisibility(View.VISIBLE);
            }

            if (TextUtil.isEmpty(data.getRealids()) || data.getRealids().equals("0")) {
                ivLayoutUserNormalInfoAuthCard.setVisibility(View.GONE);
            } else {
                ivLayoutUserNormalInfoAuthCard.setVisibility(View.VISIBLE);
            }

            if (data.getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
            } else if (data.getSex().equals("2")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
            } else if (data.getSex().equals("3")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
            }
            if (data.getRole().equals("S")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                tvLayoutUserNormalInfoRole.setText("双");
            } else if (data.getRole().equals("~")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
                tvLayoutUserNormalInfoRole.setText("~");
            } else {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
                tvLayoutUserNormalInfoRole.setText("-");
            }

            if (TextUtil.isEmpty(data.getUser_level()) || "0".equals(data.getUser_level())) {
                constraintLayoutAudienceLevel.setVisibility(View.GONE);
            } else {
                constraintLayoutAudienceLevel.setVisibility(View.VISIBLE);
                tvLiveAudienceLevel.setText(data.getUser_level());
            }
            if (TextUtil.isEmpty(data.getAnchor_level()) || "0".equals(data.getAnchor_level())) {
                constraintLayoutAnchorLevel.setVisibility(View.GONE);
            } else {
                constraintLayoutAnchorLevel.setVisibility(View.VISIBLE);
                tvLiveAnchorLevel.setText(data.getAnchor_level());
            }
            tvLayoutUserNormalInfoName.setText(data.getNickname());
            if (!TextUtil.isEmpty(data.getContent())) {
                itemDynamicListviewIntro.setVisibility(View.VISIBLE);
//                String atuid = data.getAtuid();
//                String atuname = data.getAtuname();
//                SpannableString spannableString = new SpannableString(data.getContent());
//                if (!"".equals(atuname) && null != atuname && !"".equals(atuid) && null != atuid) {
//                    String[] split = atuid.split(",");
//                    String[] split1 = atuname.split(",");
//                    for (int i = 0; i < split1.length; i++) {
//                        if (split1[i].trim().contains("*")) {
//                            split1[i] = split1[i].trim().replace("*", "\\*");
//                        }
//                        String patten = Pattern.quote(split1[i].trim());
//                        Pattern compile = Pattern.compile(patten);
//                        Matcher matcher = compile.matcher(data.getContent());
//                        while (matcher.find()) {
//                            int start = matcher.start();
//                            if (split1[i].trim().contains("*")) {
//                                split1[i] = split1[i].trim().replace("\\", "");
//                            }
////                                if (!TextUtil.isEmpty(split[i]) && start > 0) {
////                                    spannableString.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                                }
//
//                            if (!TextUtil.isEmpty(split[i]) && start >= 0) {
//                                if (split1[i].startsWith("@[群]")) {
//                                    spannableString.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                } else {
//                                    spannableString.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                }
//                            }
//                        }
//                    }
//                }
                itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                if (!TextUtil.isEmpty(data.getAtuname()) && !TextUtil.isEmpty(data.getAtuid())) {
                    SpannableString spannableString = NormalUtilKt.toDynamicSpannable(data.getContent(),
                            data.getAtuname(), data.getAtuid());
                    itemDynamicListviewIntro.setText(spannableString);
                } else {
                    itemDynamicListviewIntro.setText(data.getContent());
                }
                //itemDynamicListviewIntro.setText(data.getContent());
                //  }


            } else {
                itemDynamicListviewIntro.setVisibility(View.GONE);
            }

            if (!TextUtil.isEmpty(data.getTopictitle())) {
                llItemDynamicTopic.setVisibility(View.VISIBLE);
                tvItemDynamicTopic.setText(data.getTopictitle());
                llItemDynamicTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TopicDetailActivity.class);
                        intent.putExtra("tid", dynamicList.get(position).getTid());
                        intent.putExtra("topictitle", dynamicList.get(position).getTopictitle());
                        context.startActivity(intent);
                    }
                });
            } else {
                llItemDynamicTopic.setVisibility(View.GONE);
            }

            if (dynamicList.get(position).getUsetopnums().equals(dynamicList.get(position).getAlltopnums())) {
                itemDynamicListviewTuiding.setText("" + dynamicList.get(position).getUsetopnums());
            } else {
                itemDynamicListviewTuiding.setText("" + dynamicList.get(position).getUsetopnums() + "/" + dynamicList.get(position).getAlltopnums());
            }

            if ("0".equals(dynamicList.get(position).getAlltopnums())) {
                itemDynamicListviewTuiding.setText("" + dynamicList.get(position).getAlltopnums());
            }

            itemDynamicListviewTime.setText(data.getAddtime());
            itemDynamicListviewZan.setText(data.getLaudnum());
            itemDynamicListviewPinglun.setText(data.getComnum());
            itemDynamicListviewDashang.setText(data.getRewardnum());

            if (Integer.valueOf(dynamicList.get(position).getAlltopnums()) >= 100) {
                itemDynamicListviewTuiding.setTextColor(0xffff9d00);
            } else {
                itemDynamicListviewTuiding.setTextColor(0x8a000000);
            }
            if (Integer.valueOf(data.getLaudnum()) >= 100) {
                itemDynamicListviewZan.setTextColor(0xffff9d00);
            } else {
                itemDynamicListviewZan.setTextColor(0x8a000000);
            }

            if (Integer.valueOf(data.getComnum()) >= 100) {
                itemDynamicListviewPinglun.setTextColor(0xffff9d00);
            } else {
                itemDynamicListviewPinglun.setTextColor(0x8a000000);
            }
            if (Integer.valueOf(data.getRewardnum()) >= 10000) {
                itemDynamicListviewDashang.setTextColor(0xffff9d00);
            } else {
                itemDynamicListviewDashang.setTextColor(0x8a000000);
            }

            if (data.getComArr().size() == 0) {
                itemDynamicListviewLl.setVisibility(View.GONE);
            } else if (data.getComArr().size() == 1) {
                itemDynamicListviewLl.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomentone.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomenttwo.setVisibility(View.GONE);
                suitContent(data.getComArr().get(0).getNickname(), data.getComArr().get(0).getOthernickname(), data.getComArr().get(0).getContent(), itemDynamicListviewLlcomentoneName);
            } else if (data.getComArr().size() == 2) {
                itemDynamicListviewLl.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomentone.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomenttwo.setVisibility(View.VISIBLE);
                suitContent(data.getComArr().get(0).getNickname(), data.getComArr().get(0).getOthernickname(), data.getComArr().get(0).getContent(), itemDynamicListviewLlcomentoneName);
                suitContent(data.getComArr().get(1).getNickname(), data.getComArr().get(1).getOthernickname(), data.getComArr().get(1).getContent(), itemDynamicListviewLlcomenttwoName);
            }

            if (Integer.parseInt(data.getComnum()) > 2) {
                itemDynamicListviewMore.setVisibility(View.VISIBLE);
            } else {
                itemDynamicListviewMore.setVisibility(View.GONE);
            }

            if (data.getPlayUrl().length() > 0) {
                videoPlayerLayout.setVisibility(View.VISIBLE);
                itemDynamicNineGrid.setVisibility(View.GONE);
                videoCoverImg.setVisibility(View.VISIBLE);
                videoCoverTexture.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.VISIBLE);
                videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        Bundle bundle = new Bundle();
                        intent.putExtra("type", type);
                        intent.putExtra("tid", topicId);
                        bundle.putSerializable("videoBean", data);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.placeholder(R.drawable.rc_image_error);

                if (fragment == null) {
                    Glide.with(context)
                            .asBitmap()//制Glide返回一个Bitmap对象
                            .load(data.getCoverUrl())
                            .apply(requestOptions)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    suitView(videoCoverImg, width, height, 2);
                                    suitView(ivRoundCover, width, height, 2);
                                    suitView(videoCoverTexture, width, height, 2);
                                    videoCoverImg.setImageBitmap(bitmap);
                                }

                            });
                } else {
                    Glide.with(fragment)
                            .asBitmap()//制Glide返回一个Bitmap对象
                            .load(data.getCoverUrl())
                            .apply(requestOptions)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    suitView(videoCoverImg, width, height, 2);
                                    suitView(ivRoundCover, width, height, 2);
                                    suitView(videoCoverTexture, width, height, 2);
                                    videoCoverImg.setImageBitmap(bitmap);
                                }

                            });
                }

            } else if (data.getPic().size() == 1) {
                final ArrayList<String> tempUrls = data.getPic();
                final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                videoPlayerLayout.setVisibility(View.VISIBLE);
                itemDynamicNineGrid.setVisibility(View.GONE);
                videoCoverImg.setVisibility(View.VISIBLE);
                videoCoverTexture.setVisibility(View.GONE);
                playBtn.setVisibility(View.GONE);
                videoCoverImg.setImageResource(R.drawable.rc_image_error);
                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.placeholder(R.drawable.ic_normal_loading);
                requestOptions.centerCrop();
                ivRoundCover.setVisibility(View.INVISIBLE);

                if (fragment == null) {
                    Glide.with(context)
                            .asBitmap()//制Glide返回一个Bitmap对象
                            .load(data.getSypic().get(0))
                            .apply(requestOptions)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    int width = resource.getWidth();
                                    int height = resource.getHeight();
                                    suitView(videoCoverImg, width, height, 1);
                                    suitView(ivRoundCover, width, height, 1);
                                    videoCoverImg.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                } else {
                    Glide.with(fragment)
                            .asBitmap()//制Glide返回一个Bitmap对象
                            .load(data.getSypic().get(0))
                            .apply(requestOptions)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    int width = resource.getWidth();
                                    int height = resource.getHeight();
                                    suitView(videoCoverImg, width, height, 1);
                                    suitView(ivRoundCover, width, height, 1);
                                    videoCoverImg.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
                videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageInfo.clear();
                        for (int i = 0; i < tempUrls.size(); i++) {
                            ImageInfo info = new ImageInfo();
                            info.setThumbnailUrl(tempUrls.get(i));
                            if (data.getSypic().size() != 0) {
                                info.setBigImageUrl(data.getSypic().get(i));
                            } else {
                                info.setBigImageUrl(tempUrls.get(i));
                            }
                            imageInfo.add(info);
                        }

                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            } else {
                videoPlayerLayout.setVisibility(View.GONE);
                itemDynamicNineGrid.setVisibility(View.VISIBLE);
                NineGridUtils.loadPic(context, map, position, data, data.getPic(), itemDynamicNineGrid, itemDynamicPic);
            }
            if (dynamicList.get(position).getLaudstate().equals("0")) {
                itemDynamicListviewZan.setSelected(false);
            } else {
                itemDynamicListviewZan.setSelected(true);
            }

            if (!data.getCharm_val().equals("0")) {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText(data.getCharm_val());
            } else {
                llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            }

            if (!data.getWealth_val().equals("0")) {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText(data.getWealth_val());
            } else {
                llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
            }

            myisadmin = (String) SharedPreferencesUtils.getParam(context, "admin", "0");
            if ("1".equals(data.getAuditMark()) && "1".equals(myisadmin)) {
                llAdminmarke.setBackgroundColor(0x33c450d6);
            } else {
                llAdminmarke.setBackgroundColor(0xfffffff);
            }
            if ("1".equals(data.getIs_likeliar())){
                tvLikeLiar.setVisibility(View.VISIBLE);
            }else {
                tvLikeLiar.setVisibility(View.INVISIBLE);
            }




            ivUserAvatarIcon.setTag(position);
            ivUserAvatarIcon.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewLl.setTag(position);
            itemDynamicListviewLl.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewDashang.setTag(position);
            itemDynamicListviewDashang.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewZan.setTag(position);
            itemDynamicListviewZan.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewPinglun.setTag(position);
            itemDynamicListviewPinglun.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewLl.setTag(position);
            itemDynamicListviewLl.setOnClickListener(DynamicRecyclerAdapter.this);
            ivLayoutUserNormalInfoAuthPic.setTag(position);
            ivLayoutUserNormalInfoAuthPic.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicNineGrid.setTag(position);
            itemDynamicNineGrid.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewIntro.setTag(position);
            itemDynamicListviewIntro.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicLayouPic.setTag(position);
            itemDynamicLayouPic.setOnClickListener(DynamicRecyclerAdapter.this);
            itemDynamicListviewTuiding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String did = dynamicList.get(position).getDid();
                    toppos = position;
                    topdid = did;
                    showTopCardPop(did, position);
                }
            });

            ivUserAvatarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("uid", dynamicList.get(position).getUid());
                    context.startActivity(intent);
                }
            });
        }
    }


    void showTopCardPop(String did, final int pos) {
        PushTopCardPop pushTopCardPop = new PushTopCardPop(context, did, pos);
        pushTopCardPop.showPopupWindow();
        pushTopCardPop.setOnPushTopCardSuccessListener(new PushTopCardPop.OnPushTopCardSuccessListener() {
            @Override
            public void onPushTopCardSuc(int num, int allNum) {
                int topNum = Integer.parseInt(dynamicList.get(pos).getUsetopnums()) + num;
                int topAllNum = Integer.parseInt(dynamicList.get(pos).getAlltopnums()) + allNum;
                dynamicList.get(pos).setUsetopnums(String.valueOf(topNum));
                dynamicList.get(pos).setAlltopnums(String.valueOf(topAllNum));
                notifyItemChanged(pos);
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra("tid", dynamicList.get(position).getTid());
            intent.putExtra("topictitle", dynamicList.get(position).getTopictitle());
            context.startActivity(intent);
        }
    };

    private void goToDynamicDetail(int showwhat, int pos) {
        Intent intent = new Intent(context, DynamicDetailActivity.class);
        intent.putExtra("uid", dynamicList.get(pos).getUid());
        intent.putExtra("did", dynamicList.get(pos).getDid());
        intent.putExtra("pos", pos);
        intent.putExtra("showwhat", showwhat);
        context.startActivity(intent);
    }

    private void laudDynamic(final int index, final TextView textView) {
        HttpHelper.getInstance().thumbUpDynamic(dynamicList.get(index).getDid(), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                dynamicList.get(index).setLaudnum((Integer.parseInt(dynamicList.get(index).getLaudnum()) + 1) + "");
                dynamicList.get(index).setLaudstate("1");
                textView.setText(dynamicList.get(index).getLaudnum());
                textView.setSelected(true);
                notifyItemChanged(index);
            }

            @Override
            public void onFail(String msg) {
                textView.setSelected(false);
                ToastUtil.show(msg);
            }
        });
    }

    public void tryToPlayVideo(RecyclerView recyclerView, int position, String url) {
        if (currentPlayUrl.equals(url)) {
            return;
        } else {
            currentPlayUrl = url;
        }
        View childView = recyclerView.getLayoutManager().findViewByPosition(position);
        if (childView != null) {
            playVideo(position, url, childView);
        }
    }

    private void playVideo(final int position, String url, final View childView) {
        final ImageView videoCoverImg = childView.findViewById(R.id.video_cover_img);
        final ImageView playBtn = childView.findViewById(R.id.play_btn);
        final TextureView textureView = childView.findViewById(R.id.video_cover_texture);
        if (videoPlayer == null) {
            videoPlayer = new VideoPlayer();
        }
        videoPlayer.reset();
        videoPlayer.closeVolume();
        textureView.setAlpha(1);
        videoPlayer.setTextureView(textureView);
        videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
            @Override
            public void onReset() {

            }

            @Override
            public void onRenderingStart() {
//                videoCoverImg.setVisibility(View.GONE);
//                playBtn.setVisibility(View.GONE);
//                textureView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressUpdate(float per) {
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onStop() {
                videoCoverImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete() {
                videoPlayer.start();
            }
        });
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //Log.d("xmf","onSurfaceTextureAvailable" + width + "--" + height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                //Log.d("xmf","onSurfaceTextureSizeChanged" + width + "--" + height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                currentPlayUrl = "-1";
                textureView.setAlpha(0);
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                //  Log.d("xmf","onSurfaceTextureUpdated");
            }
        });
        videoPlayer.setDataSource(url);
        videoPlayer.prepare();
    }

    public void resetState(RecyclerView recyclerView, int position) {
        if (position == 0) {
            return;
        }
        View childView = recyclerView.getLayoutManager().findViewByPosition(position);
        if (childView != null) {
            ImageView videoCoverImg = childView.findViewById(R.id.video_cover_img);
            TextureView textureView = childView.findViewById(R.id.video_cover_texture);
            ImageView payBtn = childView.findViewById(R.id.play_btn);
            videoCoverImg.setVisibility(View.VISIBLE);
            if (textureView.getVisibility() == View.VISIBLE) {
                payBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int showwhat;
        final int pos = (int) v.getTag();
        switch (v.getId()) {
            case R.id.item_dynamic_listview_icon:
                //intent = new Intent(context, PesonInfoActivity.class);
                intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("uid", dynamicList.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_dynamic_all:
            case R.id.item_dynamic_layou_pic:
                if (dynamicList.get(pos).getDid() != null) {
                    showwhat = 1;
                    goToDynamicDetail(showwhat, pos);
                } else {
                    ToastUtil.show(context.getApplicationContext(), "请刷新重试");
                }
                break;
            case R.id.item_dynamic_listview_dashang:
                String did = dynamicList.get(pos).getDid();
                GiftPanelPop2 giftPanelPop = new GiftPanelPop2(context, 1, did, dynamicList.get(pos).getUid());
                giftPanelPop.showPopupWindow();
                giftPanelPop.setOnGiftSendSucListener(new GiftPanelPop2.OnGiftSendSucListener() {
                    @Override
                    public void onGiftSendSuc(@NotNull String orderId, @NotNull int value) {
                        //ToastUtil.show(context, "赠送成功");
                        dynamicList.get(pos).setRewardnum(String.valueOf(Integer.parseInt(dynamicList.get(pos).getRewardnum()) + value));
                        notifyItemChanged(pos);
                    }
                });
                break;
            case R.id.item_dynamic_listview_zan:
                v.setClickable(false);
                v.setSelected(true);
                if (dynamicList.get(pos).getLaudstate().equals("0")) {
                    laudDynamic(pos, (TextView) v);
                }
                break;
            case R.id.item_dynamic_listview_pinglun:
                showwhat = 2;
                goToDynamicDetail(showwhat, pos);
                break;
            case R.id.item_dynamic_listview_ll:
            case R.id.item_dynamic_nineGrid:
            case R.id.item_dynamic_listview_intro:
                showwhat = 1;
                goToDynamicDetail(showwhat, pos);
                break;
            case R.id.item_dynamic_listview_shiming:
//                intent = new Intent(context, PhotoRzActivity.class);
//                context.startActivity(intent);
                break;
        }
    }


    void suitView(View view, int width, int height, int type) {
        if (type == 1) {
            view.setLayoutParams(BitmapUtils.suitImgPara(view, width, height));
        } else {
            view.setLayoutParams(BitmapUtils.suitVideoPara(view, width, height));
        }
    }

    void suitContent(String nickName, String otherName, TextView textView) {
        textView.setTextColor(context.getResources().getColor(R.color.normalGray));
        SpannableStringBuilder builder = new SpannableStringBuilder(nickName + " 回复 " + otherName + ": ");
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
        ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#db57f3"));
        builder.setSpan(purSpan, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(purSpan2, nickName.length() + 4, nickName.length() + otherName.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    void suitContent(String nickName, String otherName, String content, TextView textView) {
        textView.setTextColor(context.getResources().getColor(R.color.normalGray));
        if (!TextUtil.isEmpty(otherName)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(nickName + " 回复 " + otherName + ": " + content);
            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
            ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#db57f3"));
            builder.setSpan(purSpan, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(purSpan2, nickName.length() + 4, nickName.length() + otherName.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        } else {
            SpannableStringBuilder builder = new SpannableStringBuilder(nickName + ": " + content);
            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
            builder.setSpan(purSpan, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(dynamicList.get(position).getDid());
    }

    class DynamicDiffCallBack extends DiffUtil.Callback {
        List<DynamicListData.DataBean> oldList;
        List<DynamicListData.DataBean> newList;

        public DynamicDiffCallBack(List<DynamicListData.DataBean> oldList, List<DynamicListData.DataBean> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getDid().equals(newList.get(newItemPosition).getDid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            DynamicListData.DataBean oldBean = oldList.get(oldItemPosition);
            DynamicListData.DataBean newBean = newList.get(newItemPosition);
            return oldBean.getLaudnum().equals(newBean.getLaudnum()) &&
                    oldBean.getContent().equals(newBean.getContent()) &&
                    oldBean.getRewardnum().equals(newBean.getRewardnum()) &&
                    oldBean.getTopnum().equals(newBean.getTopnum()) &&
                    oldBean.getAlltopnums().equals(newBean.getAlltopnums()) &&
                    oldBean.getAddtime().equals(newBean.getAddtime()) &&
                    oldBean.getComnum().equals(newBean.getComnum());
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

}
