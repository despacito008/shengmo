package com.aiwujie.shengmo.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.Share_haoyou_Activity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.ShareCallBackEvent;
import com.aiwujie.shengmo.kt.ui.activity.statistical.ShareUserActivity;
import com.aiwujie.shengmo.utils.AppIsInstallUtils;
import com.aiwujie.shengmo.utils.QqShareManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.WechatShareManager;
import com.aiwujie.shengmo.utils.WeiboShareManager;
import com.tencent.liteav.custom.Constents;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 290243232 on 2017/4/18.
 */

public class SharedPop extends PopupWindow implements View.OnClickListener {
    ImageView itemSharedWeibo;
    ImageView itemSharedWeixin;
    ImageView itemSharedPengyouquan;
    ImageView itemSharedQq;
    ImageView itemSharedQqzone;
    private Context mContext;
    private View view;
    private String address;
    private String shareContents;
    private String headurl;
    private String title;
    private int shareCallback;
    private  TextView quxiao;
    private  ImageView item_shared_haoyou;
    private  LinearLayout ll_iv_1;
    private  LinearLayout ll_iv_2;
    private  LinearLayout ll_iv_3;
    private  LinearLayout ll_iv_4;
    private  LinearLayout ll_iv_5;
    private  LinearLayout ll_iv_6;
    private int leixing;
    private String id;
    private String content;
    private String pic;
    private String userId;

    public SharedPop(Context mContext,String address,String title,String shareContents,String headurl,int shareCallback,int leixing,String id,String content,String pic,String userId) {
        this.mContext=mContext;
        this.address=address;
        this.shareContents=shareContents;
        this.headurl=headurl;
        this.title=title;
        this.shareCallback=shareCallback;
        this.leixing=leixing;
        this.id=id;
        this.content=content;
        this.pic=pic;
        this.userId=userId;
        initView();
    }
    String anchorId;
    String anchorName;
    String anchorCover;
    public SharedPop(Context mContext,String anchorId,String anchorName,String anchorCover) {
        this.mContext = mContext;
        this.anchorId = anchorId;
        this.anchorName = anchorName;
        this.anchorCover = anchorCover;
        this.headurl = anchorCover;
        this.title = anchorName + " 正在直播中";
        this.shareContents = mContext.getResources().getString(R.string.share_content);
        this.address = "http://www.shengmo.cn";
        this.leixing = 4;
        this.shareCallback = 0;
        initView();
    }

    public SharedPop(Context mContext,String anchorId,String anchorName,String anchorCover,int leixing) {
        this.mContext = mContext;
        this.anchorId = anchorId;
        this.anchorName = anchorName;
        this.anchorCover = anchorCover;
        this.headurl = anchorCover;
        this.title = anchorName + " 正在直播中";
        this.shareContents = mContext.getResources().getString(R.string.share_content);
        this.address = "http://www.shengmo.cn";
        this.leixing = leixing;
        this.shareCallback = 0;
        initView();
    }

   public void initView() {
        if (Constents.isShowAnchorFloatWindow) {
            ToastUtil.show(mContext,"直播最小化窗口中,无法分享");
            dismiss();
        }
        this.view = LayoutInflater.from(mContext).inflate(R.layout.item_shared_pop, null);
        TextView tv_nei = view.findViewById(R.id.tv_nei);
        LinearLayout ll_nei = view.findViewById(R.id.ll_nei);
        View view_hui = view.findViewById(R.id.view_hui);

        itemSharedWeibo= (ImageView) view.findViewById(R.id.item_shared_weibo);
        itemSharedWeixin= (ImageView) view.findViewById(R.id.item_shared_weixin);
        itemSharedPengyouquan= (ImageView) view.findViewById(R.id.item_shared_pengyouquan);
        itemSharedQq= (ImageView) view.findViewById(R.id.item_shared_qq);
        itemSharedQqzone= (ImageView) view.findViewById(R.id.item_shared_qqzone);
        item_shared_haoyou = view.findViewById(R.id.item_shared_haoyou);
        ll_iv_1 = view.findViewById(R.id.ll_iv_1);
        ll_iv_2 = view.findViewById(R.id.ll_iv_2);
        ll_iv_3 = view.findViewById(R.id.ll_iv_3);
        ll_iv_4 = view.findViewById(R.id.ll_iv_4);
        ll_iv_5 = view.findViewById(R.id.ll_iv_5);
        ll_iv_6 = view.findViewById(R.id.ll_iv_6);

       if (leixing==3){
           tv_nei.setVisibility(View.GONE);
           ll_nei.setVisibility(View.GONE);
           view_hui.setVisibility(View.GONE);
       } else if (leixing == 5) {
           ll_iv_2.setVisibility(View.GONE);
           ll_iv_3.setVisibility(View.GONE);
       }
        item_shared_haoyou.setOnClickListener(this);
        quxiao = view.findViewById(R.id.quxiao);
        itemSharedWeibo.setOnClickListener(this);
        itemSharedWeixin.setOnClickListener(this);
        itemSharedPengyouquan.setOnClickListener(this);
        itemSharedQq.setOnClickListener(this);
        itemSharedQqzone.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.item_shared_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

        ObjectAnimator translationY = new  ObjectAnimator().ofFloat(ll_iv_1,"translationY",0,-20,0);
        ObjectAnimator translationY2 = new ObjectAnimator().ofFloat(ll_iv_2,"translationY",0,-20,0);
        ObjectAnimator translationY3 = new ObjectAnimator().ofFloat(ll_iv_3,"translationY",0,-20,0);
        ObjectAnimator translationY4 = new ObjectAnimator().ofFloat(ll_iv_4,"translationY",0,-20,0);
        ObjectAnimator translationY5 = new ObjectAnimator().ofFloat(ll_iv_5,"translationY",0,-20,0);
        ObjectAnimator translationY6 = new ObjectAnimator().ofFloat(ll_iv_6,"translationY",0,-20,0);
        translationY.setDuration(700);
        translationY.setStartDelay(10);
        translationY.start();
        translationY2.setDuration(700);
        translationY2.setStartDelay(20);
        translationY2.start();
        translationY3.setDuration(700);
        translationY3.setStartDelay(30);
        translationY3.start();
        translationY4.setDuration(700);
        translationY4.setStartDelay(40);
        translationY4.start();
        translationY5.setDuration(700);
        translationY5.setStartDelay(50);
        translationY5.start();
        translationY6.setDuration(700);
        translationY6.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_shared_weibo:
                if(AppIsInstallUtils.isWeiBoAvailable(mContext.getApplicationContext())) {
                    WeiboShareManager weiboShareManager = WeiboShareManager.getInstance(mContext);
                    weiboShareManager.WeiboShare(mContext,title+"\n"+shareContents,address,headurl);
                }else{
                    ToastUtil.show(mContext.getApplicationContext(),"您没有安装微博");
                }
                break;
            case R.id.item_shared_weixin:
                if(AppIsInstallUtils.isWeChatAvailable(mContext.getApplicationContext())) {
                    WechatShareManager mShareManager = WechatShareManager.getInstance(mContext);
                    WechatShareManager.ShareContent shareContent = mShareManager.getShareContentWebpag(title,shareContents, address, headurl);
                    mShareManager.shareByWebchat(shareContent, 0,headurl);
                }else{
                    ToastUtil.show(mContext.getApplicationContext(),"您没有安装微信");
                }
                break;
            case R.id.item_shared_pengyouquan:
                if(AppIsInstallUtils.isWeChatAvailable(mContext.getApplicationContext())) {
                    WechatShareManager mShareManager2 = WechatShareManager.getInstance(mContext);
                    WechatShareManager.ShareContent shareContent2 = mShareManager2.getShareContentWebpag(title,shareContents, address, headurl);
                    mShareManager2.shareByWebchat(shareContent2, 1,headurl);
                }else{
                    ToastUtil.show(mContext.getApplicationContext(),"您没有安装微信");
                }
                break;
            case R.id.item_shared_qq:
                if(AppIsInstallUtils.isQQClientAvailable(mContext.getApplicationContext())) {
                    if(onShareListener != null) {
                        onShareListener.qqShare();
                    } else {
                        if (shareCallback == 0) {
                            QqShareManager qqShareManager = QqShareManager.getInstance(mContext);
                            qqShareManager.shareToQQ(address, title, shareContents, headurl);
                        } else {
                            EventBus.getDefault().post(new ShareCallBackEvent(0));
                        }
                    }
                }else{
                    ToastUtil.show(mContext.getApplicationContext(),"您没有安装QQ");
                }
                break;
            case R.id.item_shared_qqzone:
                if(AppIsInstallUtils.isQQClientAvailable(mContext.getApplicationContext())) {
                    if(onShareListener != null) {
                        onShareListener.qqShare();
                    } else {
                        if(shareCallback==0) {
                            QqShareManager qqShareManager2 = QqShareManager.getInstance(mContext);
                            qqShareManager2.shareToQZone(address, title, shareContents, headurl);
                        }else{
                            EventBus.getDefault().post(new ShareCallBackEvent(1));
                        }
                    }
                }else{
                    ToastUtil.show(mContext.getApplicationContext(),"您没有安装QQ");
                }
                break;
            case R.id.quxiao:

                break;
            case R.id.item_shared_haoyou:
                if (leixing == 4) {
                    //Intent intent = new Intent(mContext, Share_haoyou_Activity.class);
                    Intent intent = new Intent(mContext, ShareUserActivity.class);
                    intent.putExtra("leixing",leixing);
                    intent.putExtra("id",id);
                    intent.putExtra("content",content);
                    intent.putExtra("pic",pic);
                    intent.putExtra("userId",userId);
                    intent.putExtra("anchorId",anchorId);
                    intent.putExtra("anchorName",anchorName);
                    intent.putExtra("anchorCover",anchorCover);
                    mContext.startActivity(intent);
                } else {
                   // Intent intent = new Intent(mContext, Share_haoyou_Activity.class);
                    Intent intent = new Intent(mContext, ShareUserActivity.class);
                    intent.putExtra("leixing",leixing);
                    intent.putExtra("id",id);
                    intent.putExtra("content",content);
                    intent.putExtra("pic",pic);
                    intent.putExtra("userId",userId);
                    mContext.startActivity(intent);
                }
                break;

        }
        dismiss();
    }

    public interface OnShareListener {
        void qqShare();
        void qqZoneShare();
    }
    OnShareListener onShareListener;

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }
}
