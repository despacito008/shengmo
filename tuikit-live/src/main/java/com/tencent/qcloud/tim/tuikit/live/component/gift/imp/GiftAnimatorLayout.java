package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieDrawable;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.XmLottieAnimationView;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;


public class GiftAnimatorLayout extends LinearLayout {

    private static final int MAX_SHOW_GIFT_BULLET_SIZE = 3; //礼物弹幕最多展示的个数
    private static final int MSG_PLAY_SCREEN_LOTTIE_ANIMATOR = 101;

    private Context mContext;
    private XmLottieAnimationView mLottieAnimationView;
    private LinearLayout mGiftBulletGroup;
    private LinkedList<String> mAnimationUrlList;
    private boolean mIsPlaying;
    private SVGAImageView mSVGAImage;

    public GiftAnimatorLayout(Context context) {
        super(context);
    }

    public GiftAnimatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAnimationUrlList = new LinkedList<>();
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.live_layout_lottie_animator, this, true);
        mLottieAnimationView = (XmLottieAnimationView) findViewById(R.id.lottie_view);
        mGiftBulletGroup = (LinearLayout) findViewById(R.id.gift_bullet_group);
        mSVGAImage = findViewById(R.id.svga_view);
    }

    public void show(GiftInfo info) {
        if (info == null) {
            return;
        }
        showGiftBullet(info);
        if (info.giftLottieStatus == GiftInfo.GIFT_TYPE_SHOW_ANIMATION_PLAY) {
            //showLottieAnimation(info.lottieUrl);
            showSVGAAnimation(info.svgaUrl);
        }

    }

    public void showAnmation(String svgaUrl) {
        if (svgaUrl == null) {
            return;
        }
        showSVGAAnimation(svgaUrl);


    }

    public void hide() {
        mLottieAnimationView.clearAnimation();
        mLottieAnimationView.setVisibility(GONE);
    }

    private void showLottieAnimation(String lottieSource) {
        if (!TextUtils.isEmpty(lottieSource)) {
            Message message = Message.obtain();
            message.obj = lottieSource;
            message.what = MSG_PLAY_SCREEN_LOTTIE_ANIMATOR;
            mHandler.sendMessage(message);
        }
    }

    private void showSVGAAnimation(String svgaSource) {
        if (!TextUtils.isEmpty(svgaSource)) {
            Message message = Message.obtain();
            message.obj = svgaSource;
            message.what = MSG_PLAY_SCREEN_LOTTIE_ANIMATOR;
            mHandler.sendMessage(message);
        }
    }

    private void playLottieAnimation() {
        final String lottieUrl = mAnimationUrlList.getFirst();
        if (!TextUtils.isEmpty(lottieUrl)) {
            Log.d("gift anim", "播放动画 ready");
            mAnimationUrlList.removeFirst();
            mLottieAnimationView.setVisibility(VISIBLE);
            mLottieAnimationView.setAnimationFromUrl(lottieUrl);
            mLottieAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // 判断动画加载结束
                    if (valueAnimator.getAnimatedFraction() == 1f) {
                        if (mAnimationUrlList.isEmpty()) {
                            mLottieAnimationView.clearAnimation();
                            mLottieAnimationView.setVisibility(GONE);
                            mIsPlaying = false;
                        } else {
                            Log.d("gift anim", "播放动画 " + lottieUrl);
                            playLottieAnimation();
                        }
                    }
                }
            });
            //LottieDrawable mLottieDrawable = new LottieDrawable();
            // mLottieDrawable.setComposition(LottieCompositionFactory.fromUrl(getContext(), "url"));
            mLottieAnimationView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLottieAnimationView.playAnimation();
                    mIsPlaying = true;
                }
            }, 100);

        }
    }

    private void showGiftBullet(GiftInfo info) {
        if (mGiftBulletGroup.getChildCount() >= MAX_SHOW_GIFT_BULLET_SIZE) {
            //如果礼物超过3个，就将第一个出现的礼物弹幕从界面上移除
            View firstShowBulletView = mGiftBulletGroup.getChildAt(0);
            if (firstShowBulletView != null) {
                GiftBulletFrameLayout bulletView = (GiftBulletFrameLayout) firstShowBulletView;
                bulletView.clearHandler();
                mGiftBulletGroup.removeView(bulletView);
            }
        }
        GiftBulletFrameLayout giftFrameLayout = new GiftBulletFrameLayout(mContext);
        mGiftBulletGroup.addView(giftFrameLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mGiftBulletGroup.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (giftFrameLayout.setGift(info)) {
            giftFrameLayout.startAnimation();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_PLAY_SCREEN_LOTTIE_ANIMATOR) {
                String animationUrl = (String) msg.obj;
                mAnimationUrlList.addLast(animationUrl);
                if (!mIsPlaying) {
                    //播放动效
                    //playLottieAnimation();
                    //loadAssets();
                    playSVGAAnimation();
                }
            }
        }
    };

    //低版本加载本地json动效
    private void loadAssets() {
        // assets文件夹
        mLottieAnimationView.setImageAssetsFolder("race_res/");
        mLottieAnimationView.setAnimation("data.json");
        mLottieAnimationView.setRepeatMode(LottieDrawable.REVERSE);//设置播放模式
        mLottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数
        mLottieAnimationView.playAnimation();
    }

    public void playSVGAAnim() {
        try {
            String url = "https://cdn.jsdelivr.net/gh/svga/SVGA-Samples@master/kingset.svga?raw=true";
            //url = "https://shengmo.oss-cn-shanghai.aliyuncs.com/Uploads/svga/Goddess.svga";
            url = "https://cdn.jsdelivr.net/gh/svga/SVGA-Samples@master/halloween.svga?raw=true";
            // url = "https://shengmo.oss-cn-shanghai.aliyuncs.com/Uploads/svga/posche.svga";
            //url = "https://shengmo.oss-cn-shanghai.aliyuncs.com/Uploads/svga/test.svga";
            new SVGAParser(mContext).decodeFromURL(new URL(url), new SVGAParser.ParseCompletion() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    if (mSVGAImage != null) {
                        Log.d("播放", "开始播放");
                        mSVGAImage.setVideoItem(videoItem);
                        mSVGAImage.setLoops(1);
                        mSVGAImage.startAnimation();
                        mSVGAImage.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {

                            }

                            @Override
                            public void onFinished() {
                                mSVGAImage.stopAnimation(true);
                            }

                            @Override
                            public void onRepeat() {

                            }

                            @Override
                            public void onStep(int i, double v) {

                            }
                        });
                    }
                }

                @Override
                public void onError() {
                    Log.d("播放", "播放异常");
                }
            }, null);
        } catch (MalformedURLException e) {
            Log.d("播放", "播放错误" + e.getMessage());
            e.printStackTrace();
        }
    }

    SVGAParser mSVAGParser;

    private void playSVGAAnimation() {
        final String svgaUrl = mAnimationUrlList.getFirst();
        if (!TextUtils.isEmpty(svgaUrl)) {
            Log.d("gift anim", "播放动画 ready");
            mAnimationUrlList.removeFirst();
            mSVGAImage.setVisibility(VISIBLE);
            if (mSVAGParser == null) {
                mSVAGParser = new SVGAParser(mContext);
            }
            try {
                SVGAParser.Companion.shareParser().decodeFromURL(new URL(svgaUrl), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                        if (mSVGAImage != null) {
                            mSVGAImage.setVideoItem(svgaVideoEntity);
                            mSVGAImage.setLoops(1);
                            mSVGAImage.startAnimation();
                            mSVGAImage.setCallback(new SVGACallback() {
                                @Override
                                public void onPause() {

                                }

                                @Override
                                public void onFinished() {
                                    if (mAnimationUrlList.isEmpty()) {
                                        mSVGAImage.stopAnimation(true);
                                        mSVGAImage.setVisibility(View.GONE);
                                        mIsPlaying = false;
                                    } else {
                                        playSVGAAnimation();
                                    }
                                }

                                @Override
                                public void onRepeat() {

                                }

                                @Override
                                public void onStep(int i, double v) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {
                        //播放错误 跳过当前 播放下一个
                        if (mAnimationUrlList.isEmpty()) {
                            mSVGAImage.stopAnimation(true);
                            mSVGAImage.setVisibility(View.GONE);
                            mIsPlaying = false;
                        } else {
                            playSVGAAnimation();
                        }
                    }
                }, null);
                mIsPlaying = true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }
}
