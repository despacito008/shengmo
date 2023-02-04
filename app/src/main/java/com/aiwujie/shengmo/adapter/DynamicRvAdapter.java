package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.SinglePictureParameter;
import com.aiwujie.shengmo.customview.DashangDialogNew;
import com.aiwujie.shengmo.customview.NineGridViewWrapper;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.BitmapUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.utils.NineGridUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.videoplay.VideoActivity;
import com.aiwujie.shengmo.videoplay.VideoPlayer;
import com.aiwujie.shengmo.view.PushTopCardPop;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dinuscxj.ellipsize.EllipsizeTextView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
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

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class DynamicRvAdapter extends RecyclerView.Adapter<DynamicRvAdapter.DynamicHolder> implements View.OnClickListener {
    private Context context;
    private List<DynamicListData.DataBean> dynamicList;
    private int retcode;
    private String type;
    private String myisadmin = "0";

    private VideoPlayer videoPlayer;
    private TextureView textureView;
    private String currentPlayUrl = "-1";

    Map<Integer, SinglePictureParameter> map = new HashMap<>();
    int toppos;
    String topdid;

    public DynamicRvAdapter(Context context, List<DynamicListData.DataBean> dynamicList, int retcode, String type) {
        this.context = context;
        this.dynamicList = dynamicList;
        this.retcode = retcode;
        this.type = type;
        videoPlayer = new VideoPlayer();
        textureView = new TextureView(context);
    }

    @Override
    public DynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_dynamic, parent, false);
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
        @BindView(R.id.item_dynamic_listview_icon)
        ImageView itemDynamicListviewIcon;
        @BindView(R.id.item_dynamic_listview_vip)
        ImageView itemDynamicListviewVip;
        @BindView(R.id.item_dynamic_listview_hongniang)
        ImageView itemDynamicListviewHongniang;
        @BindView(R.id.item_dynamic_listview_name)
        TextView itemDynamicListviewName;
        @BindView(R.id.item_dynamic_listview_shiming)
        ImageView itemDynamicListviewShiming;
        @BindView(R.id.item_dynamic_listview_online)
        ImageView itemDynamicListviewOnline;
        @BindView(R.id.item_listview_dynamic_readcount)
        TextView itemListviewDynamicReadcount;
        @BindView(R.id.item_dynamic_listview_Sex)
        TextView itemDynamicListviewSex;
        @BindView(R.id.item_dynamic_listview_role)
        TextView itemDynamicListviewRole;
        @BindView(R.id.item_dynamic_listview_richCount)
        TextView itemDynamicListviewRichCount;
        @BindView(R.id.item_dynamic_listview_ll_richCount)
        PercentLinearLayout itemDynamicListviewLlRichCount;
        @BindView(R.id.item_dynamic_listview_beautyCount)
        TextView itemDynamicListviewBeautyCount;
        @BindView(R.id.item_dynamic_listview_ll_beautyCount)
        PercentLinearLayout itemDynamicListviewLlBeautyCount;
        @BindView(R.id.item_dynamic_listview_distance)
        TextView itemDynamicListviewDistance;
        @BindView(R.id.item_dynamic_listview_time)
        TextView itemDynamicListviewTime;
        @BindView(R.id.item_listview_dynamic_all)
        PercentLinearLayout itemListviewDynamicAll;
        @BindView(R.id.item_dynamic_listview_intro)
        EllipsizeTextView itemDynamicListviewIntro;
        @BindView(R.id.item_dynamic_nineGrid)
        NineGridView itemDynamicNineGrid;
        @BindView(R.id.item_dynamic_pic)
        NineGridViewWrapper itemDynamicPic;
        @BindView(R.id.item_dynamic_layou_pic)
        PercentFrameLayout itemDynamicLayouPic;
        @BindView(R.id.item_dynamic_listview_zan)
        TextView itemDynamicListviewZan;
        @BindView(R.id.item_dynamic_listview_pinglun)
        TextView itemDynamicListviewPinglun;
        @BindView(R.id.item_dynamic_listview_dashang)
        TextView itemDynamicListviewDashang;
        @BindView(R.id.item_dynamic_listview_llcomentone_name)
        TextView itemDynamicListviewLlcomentoneName;
        @BindView(R.id.item_dynamic_listview_llcomentone_content)
        TextView itemDynamicListviewLlcomentoneContent;
        @BindView(R.id.item_dynamic_listview_llcomentone)
        PercentLinearLayout itemDynamicListviewLlcomentone;
        @BindView(R.id.item_dynamic_listview_llcomenttwo_name)
        TextView itemDynamicListviewLlcomenttwoName;
        @BindView(R.id.item_dynamic_listview_llcomenttwo_content)
        TextView itemDynamicListviewLlcomenttwoContent;
        @BindView(R.id.item_dynamic_listview_llcomenttwo)
        PercentLinearLayout itemDynamicListviewLlcomenttwo;
        @BindView(R.id.item_dynamic_listview_ll)
        PercentLinearLayout itemDynamicListviewLl;
        @BindView(R.id.item_dynamic_listview_tuiding)
        TextView mitem_dynamiclistviewtuiding;
        @BindView(R.id.ll_adminmarke)
        PercentLinearLayout ll_adminmarke;
        @BindView(R.id.video_player_layout)
        PercentRelativeLayout videoPlayerLayout;
        @BindView(R.id.video_cover_img)
        ImageView videoCoverImg;
        @BindView(R.id.play_btn)
        ImageView playBtn;
        @BindView(R.id.video_cover_texture)
        FrameLayout textureView;
        @BindView(R.id.iv_round_cover)
        ImageView ivRoundCover;
        @BindView(R.id.iv_recommend)
        ImageView ivRecommend;
        @BindView(R.id.iv_top)
        ImageView ivTop;
        @BindView(R.id.iv_ding)
        ImageView ivDing;
        @BindView(R.id.iv_dynamic_listview_distance)
        ImageView ivNoDistance;
        @BindView(R.id.item_dynamic_listview_idcard)
        ImageView ivIdCard;
        @BindView(R.id.item_dynamic_listview_more)
        LinearLayout llMore;

        public DynamicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            final DynamicListData.DataBean data = dynamicList.get(position);
            if (!data.getRecommend().equals("0")) {
                ivRecommend.setVisibility(View.VISIBLE);
                itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
                itemListviewDynamicReadcount.setText(data.getReadtimes());
                itemListviewDynamicReadcount.setTextColor(0x8a000000);
                if (Integer.parseInt(data.getReadtimes()) > 10000) {
                    itemListviewDynamicReadcount.setTextColor(0xffff7857);
                } else if (Integer.parseInt(data.getReadtimes()) > 99999) {
                    itemListviewDynamicReadcount.setText("100000+");
                }
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_browse);
                if (bmp != null) {
                    Bitmap suitBmp = BitmapUtils.getNewBitmap(bmp, 100, 100);
                    Drawable suitDrawable = new BitmapDrawable(suitBmp);
                    itemListviewDynamicReadcount.setCompoundDrawablesWithIntrinsicBounds(suitDrawable, null, null, null);
                    itemListviewDynamicReadcount.setCompoundDrawablePadding(15);
                }
            } else {
                ivRecommend.setVisibility(View.INVISIBLE);
                itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
            }
            if (!"0".equals(data.getTopnum())) {
                ivTop.setVisibility(View.VISIBLE);
            } else {
                ivTop.setVisibility(View.GONE);
            }

            if("2".equals(type)) {
                if("1".equals(data.getStickstate())) {
                    ivDing.setVisibility(View.VISIBLE);
                } else {
                    ivDing.setVisibility(View.GONE);
                }
            } else {
                if (!TextUtil.isEmpty(data.getRecommendall()) && !"0".equals(data.getRecommendall())) {
                    ivDing.setVisibility(View.VISIBLE);
                } else {
                    ivDing.setVisibility(View.GONE);
                }
            }

            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                itemDynamicListviewIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemDynamicListviewIcon, 0);
            }
            if (retcode == 2000) {
                if (data.getLocation_switch().equals("1")) {
                    itemDynamicListviewDistance.setVisibility(View.GONE);
                    ivNoDistance.setVisibility(View.VISIBLE);
                } else {
                    ivNoDistance.setVisibility(View.GONE);
                    itemDynamicListviewDistance.setVisibility(View.VISIBLE);
                    itemDynamicListviewDistance.setText(data.getDistance() + "km");
                }
            } else {
                itemDynamicListviewDistance.setVisibility(View.GONE);
                ivNoDistance.setVisibility(View.VISIBLE);
            }
            itemDynamicListviewSex.setText(data.getAge());
            if (data.getOnlinestate() == 0) {
                itemDynamicListviewOnline.setVisibility(View.GONE);
            } else {
                itemDynamicListviewOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), itemDynamicListviewVip);
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

            if (data.getIs_hand().equals("0")) {
                itemDynamicListviewHongniang.setVisibility(View.INVISIBLE);
            } else {
                itemDynamicListviewHongniang.setVisibility(View.VISIBLE);
            }
            if (data.getRealname().equals("0")) {
                itemDynamicListviewShiming.setVisibility(View.GONE);
            } else {
                itemDynamicListviewShiming.setVisibility(View.VISIBLE);
            }


            if (data.getSex().equals("1")) {
                itemDynamicListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemDynamicListviewSex.setCompoundDrawables(drawable, null, null, null);
            } else if (data.getSex().equals("2")) {
                itemDynamicListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemDynamicListviewSex.setCompoundDrawables(drawable, null, null, null);
            } else if (data.getSex().equals("3")) {
                itemDynamicListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemDynamicListviewSex.setCompoundDrawables(drawable, null, null, null);
            }
            if (data.getRole().equals("S")) {
                itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                itemDynamicListviewRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                itemDynamicListviewRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                itemDynamicListviewRole.setText("双");
            } else if (data.getRole().equals("~")) {
                itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                itemDynamicListviewRole.setText("~");
            } else {
                itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                itemDynamicListviewRole.setText("-");
            }
            itemDynamicListviewName.setText(data.getNickname());
            if (!data.getContent().equals("")) {
                itemDynamicListviewIntro.setVisibility(View.VISIBLE);
                if (!data.getTopictitle().equals("")) {
                    itemDynamicListviewIntro.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
                    SpannableString spanableInfo = new SpannableString("#" + data.getTopictitle() + "# " + data.getContent());
                    spanableInfo.setSpan(new Clickable(clickListener), 0, data.getTopictitle().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                    itemDynamicListviewIntro.setOnTouchListener(new LinkMovementMethodOverride());
                    //itemDynamicListviewIntro.setText(spanableInfo);
                    String atuid = data.getAtuid();
                    String atuname = data.getAtuname();
                    if (!"".equals(atuname) && null != atuname && !"".equals(atuid) && null != atuid) {
                        String[] split = atuid.split(",");
                        String[] split1 = atuname.split(",");
                        for (int i = 0; i < split1.length; i++) {
                            if (split1[i].trim().contains("*")) {
                                split1[i] = split1[i].trim().replace("*", "\\*");
                            }
                            String patten = Pattern.quote(split1[i].trim());
                            Pattern compile = Pattern.compile(patten);
                            Matcher matcher = compile.matcher("#" + data.getTopictitle() + "#" + data.getContent());
                            while (matcher.find()) {
                                int start = matcher.start();
                                if (split1[i].trim().contains("*")) {
                                    split1[i] = split1[i].trim().replace("\\", "");
                                }
                                spanableInfo.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                    itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                    itemDynamicListviewIntro.setText(spanableInfo);
                } else {
                    String atuid = data.getAtuid();
                    String atuname = data.getAtuname();
                    SpannableString spannableString = new SpannableString(data.getContent());
                    if (!"".equals(atuname) && null != atuname && !"".equals(atuid) && null != atuid) {
                        String[] split = atuid.split(",");
                        String[] split1 = atuname.split(",");
                        for (int i = 0; i < split1.length; i++) {
                            if (split1[i].trim().contains("*")) {
                                split1[i] = split1[i].trim().replace("*", "\\*");
                            }
                            String patten = Pattern.quote(split1[i].trim());
                            Pattern compile = Pattern.compile(patten);
                            Matcher matcher = compile.matcher(data.getContent());
                            while (matcher.find()) {
                                int start = matcher.start();
                                if (split1[i].trim().contains("*")) {
                                    split1[i] = split1[i].trim().replace("\\", "");
                                }
                                spannableString.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                    itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                    itemDynamicListviewIntro.setText(spannableString);
                    //itemDynamicListviewIntro.setText(data.getContent());
                }
            } else {
                itemDynamicListviewIntro.setVisibility(View.GONE);
            }

            if (dynamicList.get(position).getUsetopnums().equals(dynamicList.get(position).getAlltopnums())) {
                mitem_dynamiclistviewtuiding.setText("" + dynamicList.get(position).getUsetopnums());
            } else {
                mitem_dynamiclistviewtuiding.setText("" + dynamicList.get(position).getUsetopnums() + "/" + dynamicList.get(position).getAlltopnums());
            }

            if ("0".equals(dynamicList.get(position).getAlltopnums())) {
                mitem_dynamiclistviewtuiding.setText("" + dynamicList.get(position).getAlltopnums());
            }

            itemDynamicListviewTime.setText(data.getAddtime());
            itemDynamicListviewZan.setText(data.getLaudnum());
            itemDynamicListviewPinglun.setText(data.getComnum());
            itemDynamicListviewDashang.setText(data.getRewardnum());

            if (Integer.valueOf(dynamicList.get(position).getAlltopnums()) >= 100) {
                mitem_dynamiclistviewtuiding.setTextColor(0xffff9d00);
            } else {
                mitem_dynamiclistviewtuiding.setTextColor(0x8a000000);
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
//            if (!data.getRecommend().equals("0")) {
//                if (Integer.parseInt(data.getReadtimes()) >= 10000) {
//                    SpannableString spanableInfo = new SpannableString(itemListviewDynamicReadcount.getText().toString());
//                    spanableInfo.setSpan(new ForegroundColorSpan(Color.parseColor("#ff9d00")), 3, data.getReadtimes().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    itemListviewDynamicReadcount.setText(spanableInfo);
//                } else {
//                    SpannableString spanableInfo = new SpannableString(itemListviewDynamicReadcount.getText().toString());
//                    spanableInfo.setSpan(new ForegroundColorSpan(Color.parseColor("#8a000000")), 2, itemListviewDynamicReadcount.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    itemListviewDynamicReadcount.setText(spanableInfo);
//                }
//            }


            if (data.getComArr().size() == 0) {
                itemDynamicListviewLl.setVisibility(View.GONE);
            } else if (data.getComArr().size() == 1) {
                itemDynamicListviewLl.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomentone.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomenttwo.setVisibility(View.GONE);
                suitContent(data.getComArr().get(0).getNickname(),data.getComArr().get(0).getOthernickname(),data.getComArr().get(0).getContent(),itemDynamicListviewLlcomentoneName);
            } else if (data.getComArr().size() == 2) {
                itemDynamicListviewLl.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomentone.setVisibility(View.VISIBLE);
                itemDynamicListviewLlcomenttwo.setVisibility(View.VISIBLE);
                suitContent(data.getComArr().get(0).getNickname(),data.getComArr().get(0).getOthernickname(),data.getComArr().get(0).getContent(),itemDynamicListviewLlcomentoneName);
                suitContent(data.getComArr().get(1).getNickname(),data.getComArr().get(1).getOthernickname(),data.getComArr().get(1).getContent(),itemDynamicListviewLlcomenttwoName);
            }

            if (Integer.parseInt(data.getComnum()) > 2) {
                llMore.setVisibility(View.VISIBLE);
            } else {
                llMore.setVisibility(View.GONE);
            }

            if (data.getPlayUrl().length() > 0) {
                videoPlayerLayout.setVisibility(View.VISIBLE);
                itemDynamicNineGrid.setVisibility(View.GONE);
                videoCoverImg.setVisibility(View.VISIBLE);
                textureView.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.VISIBLE);
                videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        Bundle bundle = new Bundle();
                        intent.putExtra("type", type);
                        bundle.putSerializable("videoBean", data);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.placeholder(R.drawable.rc_image_error);
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
                                suitView(textureView, width, height, 2);
                                videoCoverImg.setImageBitmap(bitmap);
                            }
                        });
            } else if (data.getPic().size() == 1) {
                final ArrayList<String> tempUrls = data.getPic();
                final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                videoPlayerLayout.setVisibility(View.VISIBLE);
                itemDynamicNineGrid.setVisibility(View.GONE);
                videoCoverImg.setVisibility(View.VISIBLE);
                textureView.setVisibility(View.GONE);
                playBtn.setVisibility(View.GONE);
                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.placeholder(R.drawable.rc_image_error);
                requestOptions.centerCrop();
                Glide.with(context)
                        .asBitmap()//制Glide返回一个Bitmap对象
                        .load(data.getSypic().get(0))
                        .apply(requestOptions)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                suitView(videoCoverImg, width, height, 1);
                                suitView(ivRoundCover, width, height, 1);
                                videoCoverImg.setImageBitmap(bitmap);
                            }
//
                        });
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
//                        info.imageViewWidth = videoCoverImg.getWidth();
//                        info.imageViewHeight = videoCoverImg.getHeight();
//                        int[] points = new int[2];
//                        videoCoverImg.getLocationInWindow(points);
//                        info.imageViewX = points[0];
//                        info.imageViewY = points[1] - getStatusHeight(context);
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
                itemDynamicListviewLlBeautyCount.setVisibility(View.VISIBLE);
                itemDynamicListviewBeautyCount.setText(data.getCharm_val());
            } else {
                itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
            }

            if (!data.getWealth_val().equals("0")) {
                itemDynamicListviewLlRichCount.setVisibility(View.VISIBLE);
                itemDynamicListviewRichCount.setText(data.getWealth_val());
            } else {
                itemDynamicListviewLlRichCount.setVisibility(View.GONE);
            }

            myisadmin = (String) SharedPreferencesUtils.getParam(context, "admin", "0");
            if ("1".equals(data.getAuditMark()) && "1".equals(myisadmin)) {
                ll_adminmarke.setBackgroundColor(0x33c450d6);
            } else {
                ll_adminmarke.setBackgroundColor(0xfffffff);
            }


            itemDynamicListviewIcon.setTag(position);
            itemDynamicListviewIcon.setOnClickListener(DynamicRvAdapter.this);
            itemListviewDynamicAll.setTag(position);
            itemListviewDynamicAll.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicListviewDashang.setTag(position);
            itemDynamicListviewDashang.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicListviewZan.setTag(position);
            itemDynamicListviewZan.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicListviewPinglun.setTag(position);
            itemDynamicListviewPinglun.setOnClickListener(DynamicRvAdapter.this);
    /*    itemDynamicListviewMore.setTag(position);
        itemDynamicListviewMore.setOnClickListener(this);*/
            itemDynamicListviewLl.setTag(position);
            itemDynamicListviewLl.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicListviewShiming.setTag(position);
            itemDynamicListviewShiming.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicNineGrid.setTag(position);
            itemDynamicNineGrid.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicListviewIntro.setTag(position);
            itemDynamicListviewIntro.setOnClickListener(DynamicRvAdapter.this);
            itemDynamicLayouPic.setTag(position);
            itemDynamicLayouPic.setOnClickListener(DynamicRvAdapter.this);
            mitem_dynamiclistviewtuiding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String did = dynamicList.get(position).getDid();
                    toppos = position;
                    topdid = did;
                    //TopcardDialog.dialogShow(context, did, position);
                    showTopCardPop(did, position);
                }
            });
        }
    }

    void showTopCardPop(String did, int pos) {
        PushTopCardPop pushTopCardPop = new PushTopCardPop(context, did, pos);
        pushTopCardPop.showPopupWindow();
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
            ds.setColor(context.getResources().getColor(R.color.purple_main));
            ds.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void goToDynamicDetail(Intent intent, int showwhat, int pos) {
        intent = new Intent(context, DynamicDetailActivity.class);
        intent.putExtra("uid", dynamicList.get(pos).getUid());
        intent.putExtra("did", dynamicList.get(pos).getDid());
        intent.putExtra("pos", pos);
        intent.putExtra("showwhat", showwhat);
        context.startActivity(intent);
    }

    private void laudDynamic(final int pos, final TextView v) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", dynamicList.get(pos).getDid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.LaudDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("laudDynamic", "onSuccess: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            dynamicList.get(pos).setLaudnum((Integer.parseInt(dynamicList.get(pos).getLaudnum()) + 1) + "");
                            dynamicList.get(pos).setLaudstate("1");
                            v.setText(dynamicList.get(pos).getLaudnum());
                            v.setSelected(true);
                            notifyItemChanged(pos);
                        case 4003:
                        case 4004:
                            ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                    }
                    v.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

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
        playVideo(position, url, childView);
    }

    private void playVideo(final int position, String url, final View childView) {
        final ImageView videoCoverImg = childView.findViewById(R.id.video_cover_img);
        final ImageView playBtn = childView.findViewById(R.id.play_btn);
        final FrameLayout frameLayout = childView.findViewById(R.id.video_cover_texture);
        if (textureView.getParent() != frameLayout) {
            if (textureView.getParent() != null) {
                ((FrameLayout) textureView.getParent()).removeView(textureView);
            }
            frameLayout.addView(textureView);
        }
        if (videoPlayer == null) {
            videoPlayer = new VideoPlayer();
        }

        videoPlayer.reset();
        videoPlayer.closeVolume();
        videoPlayer.setTextureView(textureView);

        videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
            @Override
            public void onReset() {
                videoCoverImg.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRenderingStart() {
                videoCoverImg.setVisibility(View.GONE);
                playBtn.setVisibility(View.GONE);
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
                playBtn.setVisibility(View.VISIBLE);
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
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        videoPlayer.setDataSource(url);
        videoPlayer.prepare();
    }

    public void resetState(RecyclerView recyclerView, int position) {
        View childView = recyclerView.getLayoutManager().findViewByPosition(position);
        if (childView != null) {
            ImageView videoCoverImg = childView.findViewById(R.id.video_cover_img);
            //TextureView textureView = childView.findViewById(R.id.video_cover_texture);
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
        int pos = (int) v.getTag();
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
                    goToDynamicDetail(intent, showwhat, pos);
                } else {
                    ToastUtil.show(context.getApplicationContext(), "请刷新重试");
                }
                break;
            case R.id.item_dynamic_listview_dashang:
                String did = dynamicList.get(pos).getDid();
//                打赏
              /*  if (list.get(pos).getUid().equals(MyApp.uid)) {
                    ToastUtil.show(context.getApplicationContext(), "您不能打赏自己");
                } else {*/
                new DashangDialogNew(context, did, pos, dynamicList.get(pos).getRewardnum(), dynamicList.get(pos).getUid());
                //}
//                ToastUtil.show(context.getApplicationContext(),"该功能暂未开放~");
                break;
            case R.id.item_dynamic_listview_zan:
                v.setClickable(false);
                v.setSelected(true);
                //((TextView) v).setText((Integer.valueOf(dynamicList.get(pos).getLaudnum())+1)+"");
                if (dynamicList.get(pos).getLaudstate().equals("0")) {
                    laudDynamic(pos, (TextView) v);
                }
                break;
            case R.id.item_dynamic_listview_pinglun:
                showwhat = 2;
                goToDynamicDetail(intent, showwhat, pos);
                break;
        /*    case R.id.item_dynamic_listview_more:
                showwhat = 1;
                goToDynamicDetail(intent, showwhat, pos);
                break;*/
            case R.id.item_dynamic_listview_ll:
                showwhat = 1;
                goToDynamicDetail(intent, showwhat, pos);
                break;
            case R.id.item_dynamic_listview_shiming:
//                intent = new Intent(context, PhotoRzActivity.class);
//                context.startActivity(intent);
                break;
            case R.id.item_dynamic_nineGrid:
                showwhat = 1;
                goToDynamicDetail(intent, showwhat, pos);
                break;
            case R.id.item_dynamic_listview_intro:
                showwhat = 1;
                goToDynamicDetail(intent, showwhat, pos);
//                break;
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

    void suitContent(String nickName,String otherName,String content,TextView textView) {
        textView.setTextColor(context.getResources().getColor(R.color.normalGray));
        if(!TextUtil.isEmpty(otherName)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(nickName + " 回复 " + otherName + ": " + content);
            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
            ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#db57f3"));
            builder.setSpan(purSpan, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(purSpan2, nickName.length() + 4, nickName.length() + otherName.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        } else {
            SpannableStringBuilder builder = new SpannableStringBuilder(nickName +  ": " + content);
            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
            builder.setSpan(purSpan, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
    }
}
