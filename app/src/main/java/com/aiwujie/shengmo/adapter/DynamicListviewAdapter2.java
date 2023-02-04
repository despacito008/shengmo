package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.VideoPlayerActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.SinglePictureParameter;
import com.aiwujie.shengmo.bean.TopcardyesBean;
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
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.videoplay.VideoActivity;
import com.aiwujie.shengmo.videoplay.VideoPlayer;
import com.aiwujie.shengmo.view.PushTopCardPop;
import com.aiwujie.shengmo.view.TextureVideoViewOutlineProvider;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.aiwujie.shengmo.zdyview.TopcardDialog;
import com.aliyun.svideo.common.utils.image.GlideRoundedCornersTransform;
import com.aliyun.svideo.editor.effectmanager.AbstractActionBarActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dinuscxj.ellipsize.EllipsizeTextView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

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

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.utils.NineGridUtils.getStatusHeight;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class DynamicListviewAdapter2 extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<DynamicListData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    private VideoPlayer videoPlayer;

    Map<Integer, SinglePictureParameter> map = new HashMap<>();
    int toppos;
    String topdid;
    String myisadmin;
    ViewGroup parent;
    private String type;
    private String currentPlayUrl = "-1";

    public DynamicListviewAdapter2(Context context, List<DynamicListData.DataBean> list, int retcode, String myisadmin,String type) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
        this.type=type;
        videoPlayer = new VideoPlayer();
        this.myisadmin = myisadmin;
        inflater = LayoutInflater.from(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        this.parent = parent;
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_dynamic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DynamicListData.DataBean data = list.get(position);

        if (!data.getRecommend().equals("0")) {
            holder.itemListviewDynamicAll.setBackgroundResource(R.drawable.jian);
            holder.itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
            if (!data.getReadtimes().equals("")) {
                if (Integer.parseInt(data.getReadtimes()) > 99999) {
                    holder.itemListviewDynamicReadcount.setText("浏览 " + data.getReadtimes());
                } else {
                    holder.itemListviewDynamicReadcount.setText("浏览 " + data.getReadtimes());
                }
            }
        } else {
            holder.itemListviewDynamicAll.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
        }
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemDynamicListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemDynamicListviewIcon, 0);
        }
        if (retcode == 2000) {
            if (data.getLocation_switch().equals("1")) {
                holder.itemDynamicListviewDistance.setVisibility(View.GONE);
                holder.ivNoDistance.setVisibility(View.VISIBLE);
                holder.itemDynamicListviewDistance.setText("隐身");
            } else {
                holder.itemDynamicListviewDistance.setVisibility(View.VISIBLE);
                holder.ivNoDistance.setVisibility(View.GONE);
                holder.itemDynamicListviewDistance.setText(data.getDistance() + "km");
            }
        } else {
            holder.itemDynamicListviewDistance.setVisibility(View.GONE);
            holder.ivNoDistance.setVisibility(View.VISIBLE);
        }
        holder.itemDynamicListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemDynamicListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemDynamicListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.itemDynamicListviewVip);
        // 判断用户的权限来看是否显示浏览数
//        if(data.getVip().equals("1")||data.getIs_admin().equals("1")||data.getRealname().equals("1")){
//            holder.itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
//            if(!data.getReadtimes().equals("")) {
//                if (Integer.parseInt(data.getReadtimes()) > 99999) {
//                    holder.itemListviewDynamicReadcount.setText("浏览 100000+");
//                } else {
//                    holder.itemListviewDynamicReadcount.setText("浏览 " + data.getReadtimes());
//                }
//            }
//        }else{
//            holder.itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
//        }

        if (data.getIs_hand().equals("0")) {
            holder.itemDynamicListviewHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemDynamicListviewHongniang.setVisibility(View.VISIBLE);
        }
        if (data.getRealname().equals("0")) {
            holder.itemDynamicListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemDynamicListviewShiming.setVisibility(View.VISIBLE);
        }
        if (data.getSex().equals("1")) {
            holder.itemDynamicListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemDynamicListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemDynamicListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemDynamicListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemDynamicListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemDynamicListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemDynamicListviewRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemDynamicListviewRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemDynamicListviewRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemDynamicListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemDynamicListviewRole.setText("~");
        }
        holder.itemDynamicListviewName.setText(data.getNickname());
        if (!data.getContent().equals("")) {
            holder.itemDynamicListviewIntro.setVisibility(View.VISIBLE);
            if (!data.getTopictitle().equals("")) {
                holder.itemDynamicListviewIntro.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
                SpannableString spanableInfo = new SpannableString("#" + data.getTopictitle() + "#" + data.getContent());
                spanableInfo.setSpan(new Clickable(clickListener), 0, data.getTopictitle().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                holder.itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                holder.itemDynamicListviewIntro.setOnTouchListener(new LinkMovementMethodOverride());
                //holder.itemDynamicListviewIntro.setText(spanableInfo);
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
                holder.itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                holder.itemDynamicListviewIntro.setText(spanableInfo);
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
                holder.itemDynamicListviewIntro.setMovementMethod(LinkMovementMethod.getInstance());
                holder.itemDynamicListviewIntro.setText(spannableString);
                //holder.itemDynamicListviewIntro.setText(data.getContent());
            }
        } else {
            holder.itemDynamicListviewIntro.setVisibility(View.GONE);
        }

        if (list.get(position).getUsetopnums().equals(list.get(position).getAlltopnums())) {
            holder.mitem_dynamiclistviewtuiding.setText("" + list.get(position).getUsetopnums());
        } else {
            holder.mitem_dynamiclistviewtuiding.setText("" + list.get(position).getUsetopnums() + "/" + list.get(position).getAlltopnums());
        }

        if ("0".equals(list.get(position).getAlltopnums())) {
            holder.mitem_dynamiclistviewtuiding.setText("" + list.get(position).getAlltopnums());
        }

        holder.itemDynamicListviewTime.setText(data.getAddtime());
        holder.itemDynamicListviewZan.setText(data.getLaudnum());
        holder.itemDynamicListviewPinglun.setText(data.getComnum());

        if (Integer.valueOf(list.get(position).getAlltopnums()) >= 100) {
            holder.mitem_dynamiclistviewtuiding.setTextColor(0xffff9d00);
        } else {
            holder.mitem_dynamiclistviewtuiding.setTextColor(0x8a000000);
        }
        if (Integer.valueOf(data.getLaudnum()) >= 100) {
            holder.itemDynamicListviewZan.setTextColor(0xffff9d00);
        } else {
            holder.itemDynamicListviewZan.setTextColor(0x8a000000);
        }

        if (Integer.valueOf(data.getComnum()) >= 100) {
            holder.itemDynamicListviewPinglun.setTextColor(0xffff9d00);
        } else {
            holder.itemDynamicListviewPinglun.setTextColor(0x8a000000);
        }
        if (Integer.valueOf(data.getRewardnum()) >= 10000) {
            holder.itemDynamicListviewDashang.setTextColor(0xffff9d00);
        } else {
            holder.itemDynamicListviewDashang.setTextColor(0x8a000000);
        }
        if (!data.getRecommend().equals("0")) {
            if (Integer.parseInt(data.getReadtimes()) >= 10000) {
                SpannableString spanableInfo = new SpannableString(holder.itemListviewDynamicReadcount.getText().toString());
                spanableInfo.setSpan(new ForegroundColorSpan(Color.parseColor("#ff9d00")), 3, data.getReadtimes().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.itemListviewDynamicReadcount.setText(spanableInfo);
            } else {
                SpannableString spanableInfo = new SpannableString(holder.itemListviewDynamicReadcount.getText().toString());
                spanableInfo.setSpan(new ForegroundColorSpan(Color.parseColor("#8a000000")), 2, holder.itemListviewDynamicReadcount.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.itemListviewDynamicReadcount.setText(spanableInfo);
            }
        }


        if (data.getComArr().size() == 0) {
            holder.itemDynamicListviewLl.setVisibility(View.GONE);
        } else if (data.getComArr().size() == 1) {
            holder.itemDynamicListviewLl.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewLlcomentone.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewLlcomenttwo.setVisibility(View.GONE);
            holder.itemDynamicListviewLlcomentoneName.setText(data.getComArr().get(0).getNickname() + ":");
            holder.itemDynamicListviewLlcomentoneContent.setText(data.getComArr().get(0).getContent());
        } else if (data.getComArr().size() == 2) {
            holder.itemDynamicListviewLl.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewLlcomentone.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewLlcomenttwo.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewLlcomentoneName.setText(data.getComArr().get(0).getNickname() + ":");
            holder.itemDynamicListviewLlcomentoneContent.setText(data.getComArr().get(0).getContent());
            holder.itemDynamicListviewLlcomenttwoName.setText(data.getComArr().get(1).getNickname() + ":");
            holder.itemDynamicListviewLlcomenttwoContent.setText(data.getComArr().get(1).getContent());
        }


        if (data.getPlayUrl().length() > 0) {
            holder.videoPlayerLayout.setVisibility(View.VISIBLE);
            holder.itemDynamicNineGrid.setVisibility(View.GONE);
            //holder.videoCoverImg.setVisibility(View.GONE);
            holder.videoCoverTextTure.setVisibility(View.VISIBLE);
            holder.videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("type",type);
                    bundle.putSerializable("videoBean",data);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
            RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.rc_image_error);
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
//                            int i = dp2px(300);
//                            if (height > i) {
//                                height = i;
//                            }
                            holder.videoCoverImg.setLayoutParams(BitmapUtils.suitVideoPara(holder.videoCoverImg, width, height));
                            holder.videoCoverImg.setImageBitmap(bitmap);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                holder.videoCoverTextTure.setOutlineProvider(new TextureVideoViewOutlineProvider(10));
//                                holder.videoCoverTextTure.setClipToOutline(true);
//                            }
                            holder.videoCoverTextTure.setLayoutParams(BitmapUtils.suitVideoPara(holder.videoCoverTextTure, width, height));

//                            holder.videoCoverTextTure.setLayoutParams(setPara(holder.videoCoverTextTure, width, height));
//                            videoPlayer.setTextureView(holder.videoCoverTextTure);
                           // playVideo(position);
//                            map.put(position,new SinglePictureParameter(height,width,bitmap));
                        }

                    });
//            Glide.with(context).load(data.getCoverUrl()).apply(requestOptions).into(holder.videoCoverImg);
        } else if (data.getPic().size() == 1) {
            final ArrayList<String> tempUrls = data.getPic();
            final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            holder.videoPlayerLayout.setVisibility(View.VISIBLE);
            holder.itemDynamicNineGrid.setVisibility(View.GONE);
            holder.videoCoverImg.setVisibility(View.VISIBLE);
            holder.videoCoverTextTure.setVisibility(View.GONE);

            RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.rc_image_error);
            requestOptions.transform(new GlideRoundedCornersTransform(context,4, GlideRoundedCornersTransform.CornerType.ALL));
            requestOptions.error(R.drawable.rc_image_error);
            requestOptions.placeholder(R.drawable.rc_image_error);
            Glide.with(context)
                    .asBitmap()//制Glide返回一个Bitmap对象
                    .load(data.getPic().get(0))
                    .apply(requestOptions)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            int i = dp2px(300);
                            int j = dp2px(500);
                            float rate = (float) height / width;
                            if (width < i || width > j) {
                                height = (int) (i * rate);
                                width = i;
                            }
                            holder.videoCoverImg.setLayoutParams(BitmapUtils.suitImgPara(holder.videoCoverImg, width, height));
                            holder.videoCoverImg.setImageBitmap(bitmap);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                holder.videoCoverImg.setOutlineProvider(new TextureVideoViewOutlineProvider(10));
//                                holder.videoCoverImg.setClipToOutline(true);
//                            }
//                            bitmap = BitmapUtils.suitImgPara(holder.videoCoverImg, width, height,bitmap);
//                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(),bitmap);
//                            roundedBitmapDrawable.setCornerRadius(BitmapUtils.dip2pix(context,5));
//                            holder.videoCoverImg.setImageDrawable(roundedBitmapDrawable);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                holder.videoCoverImg.setOutlineProvider(new TextureVideoViewOutlineProvider(10));
//                                holder.videoCoverImg.setClipToOutline(true);
//                            }
//                            map.put(position,new SinglePictureParameter(height,width,bitmap));
                        }

                    });
            holder.videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
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
//                        info.imageViewWidth = holder.videoCoverImg.getWidth();
//                        info.imageViewHeight = holder.videoCoverImg.getHeight();
//                        int[] points = new int[2];
//                        holder.videoCoverImg.getLocationInWindow(points);
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
            holder.videoPlayerLayout.setVisibility(View.GONE);
            holder.itemDynamicNineGrid.setVisibility(View.VISIBLE);
            NineGridUtils.loadPic(context, map, position, data, data.getPic(), holder.itemDynamicNineGrid, holder.itemDynamicPic);
        }
        if (list.get(position).getLaudstate().equals("0")) {
            holder.itemDynamicListviewZan.setSelected(false);
        } else {
            holder.itemDynamicListviewZan.setSelected(true);
        }
        if (!data.getCharm_val().equals("0")) {
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewBeautyCount.setText(data.getCharm_val());
        } else {
            holder.itemDynamicListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!data.getWealth_val().equals("0")) {
            holder.itemDynamicListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewRichCount.setText(data.getWealth_val());
        } else {
            holder.itemDynamicListviewLlRichCount.setVisibility(View.GONE);
        }

        if ("1".equals(data.getAuditMark())) {
            if (myisadmin.equals("1")) {
                holder.ll_adminmarke.setBackgroundColor(0x33c450d6);
            } else {
                holder.ll_adminmarke.setBackgroundColor(0xfffffff);
            }
        } else {
            holder.ll_adminmarke.setBackgroundColor(0xfffffff);
        }


        holder.itemDynamicListviewIcon.setTag(position);
        holder.itemDynamicListviewIcon.setOnClickListener(this);
        holder.itemListviewDynamicAll.setTag(position);
        holder.itemListviewDynamicAll.setOnClickListener(this);
        holder.itemDynamicListviewDashang.setTag(position);
        holder.itemDynamicListviewDashang.setOnClickListener(this);
        holder.itemDynamicListviewZan.setTag(position);
        holder.itemDynamicListviewZan.setOnClickListener(this);
        holder.itemDynamicListviewPinglun.setTag(position);
        holder.itemDynamicListviewPinglun.setOnClickListener(this);
    /*    holder.itemDynamicListviewMore.setTag(position);
        holder.itemDynamicListviewMore.setOnClickListener(this);*/
        holder.itemDynamicListviewLl.setTag(position);
        holder.itemDynamicListviewLl.setOnClickListener(this);
        holder.itemDynamicListviewShiming.setTag(position);
        holder.itemDynamicListviewShiming.setOnClickListener(this);
        holder.itemDynamicNineGrid.setTag(position);
        holder.itemDynamicNineGrid.setOnClickListener(this);
        holder.itemDynamicListviewIntro.setTag(position);
        holder.itemDynamicListviewIntro.setOnClickListener(this);
        holder.itemDynamicLayouPic.setTag(position);
        holder.itemDynamicLayouPic.setOnClickListener(this);
        holder.mitem_dynamiclistviewtuiding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = list.get(position).getDid();
                toppos = position;
                topdid = did;
                //TopcardDialog.dialogShow(context, did, position);
                showTopCardPop(did,position);
            }
        });
        return convertView;
    }

    void showTopCardPop(String did,int pos) {
        PushTopCardPop pushTopCardPop = new PushTopCardPop(context,did,pos);
        pushTopCardPop.showPopupWindow();
    }

    public void notify(List<DynamicListData.DataBean> list,int index){
        this.list = list;
        ViewHolder holder=(ViewHolder)(parent.getChildAt(index)).getTag();
        holder.itemDynamicListviewZan.setSelected(true);
        holder.itemDynamicListviewZan.setText(list.get(index).getLaudnum());
        notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gettopcardyn(TopcardyesBean topcardyesBean) {
        if (topcardyesBean.getPos() == toppos && topcardyesBean.getDid().equals(topdid)) {
            int aa = Integer.valueOf(list.get(toppos).getTopnum()) + 1;
            list.get(toppos).setTopnum(aa + "");
            notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        int showwhat;
        int pos = (int) v.getTag();
        switch (v.getId()) {
            case R.id.item_dynamic_listview_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_dynamic_all:
            case R.id.item_dynamic_layou_pic:
                if (list.get(pos).getDid() != null) {
                    showwhat = 1;
                    goToDynamicDetail(intent, showwhat, pos);
                } else {
                    ToastUtil.show(context.getApplicationContext(), "请刷新重试");
                }
                break;
            case R.id.item_dynamic_listview_dashang:
                String did = list.get(pos).getDid();
//                打赏
              /*  if (list.get(pos).getUid().equals(MyApp.uid)) {
                    ToastUtil.show(context.getApplicationContext(), "您不能打赏自己");
                } else {*/
                new DashangDialogNew(context, did, pos, list.get(pos).getRewardnum(), list.get(pos).getUid());
                //}
//                ToastUtil.show(context.getApplicationContext(),"该功能暂未开放~");
                break;
            case R.id.item_dynamic_listview_zan:
                v.setClickable(false);
                v.setSelected(true);
                ((TextView) v).setText((Integer.valueOf(list.get(pos).getLaudnum())+1)+"");
                if (list.get(pos).getLaudstate().equals("0")) {
                    laudDynamic(pos, (TextView) v);
                }
//                else {
//                    cancelLaud(pos, (TextView) v);
//                }
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
                intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra("tid", list.get(position).getTid());
            intent.putExtra("topictitle", list.get(position).getTopictitle());
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
        }
    }

    private void goToDynamicDetail(Intent intent, int showwhat, int pos) {
        intent = new Intent(context, DynamicDetailActivity.class);
        intent.putExtra("uid", list.get(pos).getUid());
        intent.putExtra("did", list.get(pos).getDid());
        intent.putExtra("pos", pos);
        intent.putExtra("showwhat", showwhat);
        context.startActivity(intent);
    }

    private void laudDynamic(final int pos, final TextView v) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", list.get(pos).getDid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.LaudDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("laudDynamic", "onSuccess: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
//                                    ToastUtil.show(context.getApplicationContext(),obj.getString("msg"));
                                    list.get(pos).setLaudnum((Integer.parseInt(list.get(pos).getLaudnum()) + 1) + "");
                                    list.get(pos).setLaudstate("1");
                                    v.setText(list.get(pos).getLaudnum());
                                    v.setSelected(true);
                                    notifyDataSetChanged();
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

    private void cancelLaud(final int pos, final TextView v) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", list.get(pos).getDid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelLaud, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("cancelDynamic", "onSuccess: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
//                                    ToastUtil.show(context.getApplicationContext(),obj.getString("msg"));
                                    list.get(pos).setLaudnum((Integer.parseInt(list.get(pos).getLaudnum()) - 1) + "");
                                    list.get(pos).setLaudstate("0");
                                    v.setText(list.get(pos).getLaudnum());
                                    v.setSelected(false);
//                                    notifyDataSetChanged();
                                    break;
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

    public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置图片比例
     *
     * @param imagePic
     * @param width
     * @param height
     * @return
     */
    private static ViewGroup.LayoutParams setPara(View imagePic, int width, int height) {
        ViewGroup.LayoutParams para = imagePic.getLayoutParams();
        if (width > 1500 || height > 1500) {
            para.height = height * 2 / 5;
            para.width = width * 2 / 5;
        } else {
            if (width > 1000 || height > 1000) {
                para.height = height * 3 / 5;
                para.width = width * 3 / 5;
            } else {
                if (width > 500 || height > 500) {
                    para.height = height * 3 / 4;
                    para.width = width * 3 / 4;
                } else {
                    if (width < 200 || height < 200) {
                        para.height = height + height / 3;
                        para.width = width + width / 3;
                    } else {
                        para.height = height;
                        para.width = width;
                    }
                }
            }
        }
        return para;
    }


    public void tryToPlayVideo(AbsListView listView, int position, String url) {

        if(currentPlayUrl.equals(url)) {
            return;
        } else {
            currentPlayUrl = url;
        }
        ImageView videoCoverImg = listView.getChildAt(position).findViewById(R.id.video_cover_img);
        TextureView textureView = listView.getChildAt(position).findViewById(R.id.video_cover_texture);
        playVideo(position,url,textureView,videoCoverImg);
    }

    private void  playVideo(int position, String url,final TextureView textureView, final ImageView ivCover) {

        videoPlayer.reset();
        videoPlayer.setVolume(0,0);
        videoPlayer.setTextureView(textureView);
        videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
            @Override
            public void onReset() {

            }

            @Override
            public void onRenderingStart() {
                ivCover.setVisibility(View.GONE);
            }

            @Override
            public void onProgressUpdate(float per) {
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onStop() {
                ivCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete() {
                videoPlayer.start();
            }
        });
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                // Log.d("xmf","onSurfaceTextureAvailable");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Log.d("xmf","onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                //Log.d("xmf","onSurfaceTextureDestroyed");
                currentPlayUrl = "-1";
                textureView.setVisibility(View.GONE);
                ivCover.setVisibility(View.VISIBLE);
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

    static class ViewHolder {

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
        /* @BindView(R.id.item_dynamic_listview_more)
         TextView itemDynamicListviewMore;*/
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
        TextureView videoCoverTextTure;
        @BindView(R.id.iv_dynamic_listview_distance)
        ImageView ivNoDistance;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


