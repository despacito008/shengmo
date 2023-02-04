package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class DynamicHotCardListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<DynamicListData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    private Handler handler = new Handler();
    Map<Integer,SinglePictureParameter> map=new HashMap<>();
    ViewGroup parent;
    public DynamicHotCardListviewAdapter(Context context, List<DynamicListData.DataBean> list, int retcode) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
        inflater = LayoutInflater.from(context);
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


    public void notify(List<DynamicListData.DataBean> list,int index){
        this.list = list;
        ViewHolder holder=(ViewHolder)(parent.getChildAt(index)).getTag();
        holder.itemDynamicListviewZan.setSelected(true);
        holder.itemDynamicListviewZan.setText(list.get(index).getLaudnum());
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        this.parent = parent;
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
        } else {
            holder.itemListviewDynamicAll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemDynamicListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemDynamicListviewIcon, 0);
        }
        if (retcode == 2000) {
            holder.itemDynamicListviewDistance.setVisibility(View.VISIBLE);
            holder.itemDynamicListviewDistance.setText(data.getDistance() + "km");
        } else {
            holder.itemDynamicListviewDistance.setVisibility(View.INVISIBLE);
        }
        holder.itemDynamicListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemDynamicListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemDynamicListviewOnline.setVisibility(View.VISIBLE);
        }

        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemDynamicListviewVip);
        // 判断用户的权限来看是否显示浏览数
//        if (data.getVip().equals("1") || data.getIs_admin().equals("1") || data.getRealname().equals("1")) {
//            holder.itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
//            if (!data.getReadtimes().equals("")) {
//                if (Integer.parseInt(data.getReadtimes()) > 99999) {
//                    holder.itemListviewDynamicReadcount.setText("浏览 " + data.getReadtimes());
//                } else {
//                    holder.itemListviewDynamicReadcount.setText("浏览 " + data.getReadtimes());
//                }
//            }
//        } else {
//            holder.itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
//        }

        if (!data.getRecommend().equals("0")) {
            //itemListviewDynamicAll.setBackgroundResource(R.drawable.jian);
            holder.ivRecommend.setVisibility(View.VISIBLE);
            holder.itemListviewDynamicReadcount.setVisibility(View.VISIBLE);
            holder.itemListviewDynamicReadcount.setText(data.getReadtimes());
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_browse);
            if(bmp != null) {
                Bitmap suitBmp = BitmapUtils.getNewBitmap(bmp, 100, 100);
                Drawable suitDrawable = new BitmapDrawable(suitBmp);
                holder.itemListviewDynamicReadcount.setCompoundDrawablesWithIntrinsicBounds(suitDrawable, null, null, null);
                holder.itemListviewDynamicReadcount.setCompoundDrawablePadding(15);
            }
        } else {
            holder.ivRecommend.setVisibility(View.INVISIBLE);
            //itemListviewDynamicAll.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.itemListviewDynamicReadcount.setVisibility(View.INVISIBLE);
        }

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
                holder.itemDynamicListviewIntro.setText(spanableInfo);
            } else {
                holder.itemDynamicListviewIntro.setText(data.getContent());
            }
        } else {
            holder.itemDynamicListviewIntro.setVisibility(View.GONE);
        }
        holder.itemDynamicListviewTime.setText(data.getAddtime() + "回复");
        holder.itemDynamicListviewZan.setText(data.getLaudnum());
        holder.itemDynamicListviewPinglun.setText(data.getComnum());
        holder.itemDynamicListviewDashang.setText(data.getRewardnum());

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
            holder.playBtn.setVisibility(View.VISIBLE);
            holder.videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("video_url", data.getPlayUrl());
                    intent.putExtra("cover_url",data.getCoverUrl());
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
                            holder.videoCoverImg.setLayoutParams(NineGridUtils.setPara(holder.videoCoverImg, width, height));
                            holder.videoCoverImg.setImageBitmap(bitmap);
//                            map.put(position,new SinglePictureParameter(height,width,bitmap));
                        }

                    });
//            Glide.with(context).load(data.getCoverUrl()).apply(requestOptions).into(holder.videoCoverImg);
        } else if (data.getPic().size() == 1) {
            final ArrayList<String> tempUrls = data.getPic();
            final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            holder.videoPlayerLayout.setVisibility(View.VISIBLE);
            holder.itemDynamicNineGrid.setVisibility(View.GONE);
            holder.playBtn.setVisibility(View.GONE);
            RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.rc_image_error);
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
                            int i = NineGridUtils.dp2px(300);
                            int j = NineGridUtils.dp2px(500);
                            float rate = (float) height / width;
                            if (width < i || width > j) {
                                height = (int) (i * rate);
                                width = i;
                            }
                            holder.videoCoverImg.setLayoutParams(NineGridUtils.setPara(holder.videoCoverImg, width, height));
                            holder.videoCoverImg.setImageBitmap(bitmap);
//                            map.put(position,new SinglePictureParameter(height,width,bitmap));
                        }

                    });
            holder.videoPlayerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            holder.videoPlayerLayout.setVisibility(View.GONE);
            holder.itemDynamicNineGrid.setVisibility(View.VISIBLE);
            ArrayList<String> urls = data.getPic();
            NineGridUtils.loadPic(context, map, position, data, urls, holder.itemDynamicNineGrid, holder.itemDynamicPic);
        } if (list.get(position).getLaudstate().equals("0")) {
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
       /* holder.itemDynamicListviewMore.setTag(position);
        holder.itemDynamicListviewMore.setOnClickListener(this);*/
        holder.itemDynamicListviewLl.setTag(position);
        holder.itemDynamicListviewLl.setOnClickListener(this);
        holder.itemDynamicListviewShiming.setTag(position);
        holder.itemDynamicListviewShiming.setOnClickListener(this);
        holder.itemDynamicNineGrid.setTag(position);
        holder.itemDynamicNineGrid.setOnClickListener(this);
        holder.itemDynamicListviewIntro.setTag(position);
        holder.itemDynamicListviewIntro.setOnClickListener(this);
        return convertView;
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
                if (list.get(pos).getDid() != null) {
                    showwhat = 1;
                    goToDynamicDetail(intent, showwhat, pos);
                } else {
                    ToastUtil.show(context.getApplicationContext(), "请刷新重试");
                }
                break;
            case R.id.item_dynamic_listview_dashang:
                String did = list.get(pos).getDid();
                /*//打赏
                if (list.get(pos).getUid().equals(MyApp.uid)) {
                    ToastUtil.show(context.getApplicationContext(), "您不能打赏自己");
                } else {
                    new DashangDialogNew(context, did, pos, list.get(pos).getRewardnum());
                }*/
                new DashangDialogNew(context, did, pos, list.get(pos).getRewardnum(),list.get(pos).getUid());
                break;
            case R.id.item_dynamic_listview_zan:
                v.setClickable(false);
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
     /*       case R.id.item_dynamic_listview_more:
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
//                                    ToastUtil.show(context.getApplicationContext(),obj.getString("msg"));
                                    list.get(pos).setLaudnum((Integer.parseInt(list.get(pos).getLaudnum()) + 1) + "");
                                    list.get(pos).setLaudstate("1");
                                    v.setText(list.get(pos).getLaudnum());
                                    v.setSelected(true);
//                                    notifyDataSetChanged();
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
                });

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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
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
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
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
      /*  @BindView(R.id.item_dynamic_listview_more)
        TextView itemDynamicListviewMore;*/
        @BindView(R.id.item_dynamic_listview_ll)
        PercentLinearLayout itemDynamicListviewLl;
        @BindView(R.id.video_player_layout)
        PercentRelativeLayout videoPlayerLayout;
        @BindView(R.id.video_cover_img)
        ImageView videoCoverImg;
        @BindView(R.id.play_btn)
        ImageView playBtn;
        @BindView(R.id.iv_recommend)
        ImageView ivRecommend;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


