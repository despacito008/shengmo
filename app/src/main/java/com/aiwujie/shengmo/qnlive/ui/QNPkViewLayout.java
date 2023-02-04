package com.aiwujie.shengmo.qnlive.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.bumptech.glide.Glide;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.tencent.qcloud.tim.tuikit.live.bean.PkInfoBean;

import java.util.List;


public class QNPkViewLayout extends ConstraintLayout {
    private Context             mContext;
    private ConstraintLayout    mLayoutRoot;

    private ImageView           ivAudienceUsTop1;
    private ImageView           ivAudienceUsTop2;
    private ImageView           ivAudienceUsTop3;
    private ImageView           ivAudienceOtherTop1;
    private ImageView           ivAudienceOtherTop2;
    private ImageView           ivAudienceOtherTop3;
    private ImageView           ivPkResultUs;
    private ImageView           ivPkResultOther;
    private ImageView           ivPkStatus;
    private View                viewUsPower;
    private View                viewOtherPower;
    private TextView            tvPkTime;
    private TextView            tvUsPower;
    private TextView            tvOtherPower;
    private LinearLayout        llPkTop;
    private View                viewPkTopLeft;
    private View                viewPkTopRight;
    private ImageView           ivPkMuteOther;
    private QNSurfaceView       viewUsWindow;
    private QNSurfaceView       viewOtherWindow;

    boolean isMuteOther = false;

    boolean isAnchor = true;

    public QNPkViewLayout(Context context) {
        super(context);
        initView(context);
    }

    public QNPkViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public QNPkViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutRoot = (ConstraintLayout) inflate(context, R.layout.app_layout_qn_pk_view, this);
        initPkView();
        hidePkView();
    }

    void initPkView() {
        ivAudienceUsTop1 = mLayoutRoot.findViewById(R.id.iv_pk_view_audience_us_1);
        ivAudienceUsTop2 = mLayoutRoot.findViewById(R.id.iv_pk_view_audience_us_2);
        ivAudienceUsTop3 = mLayoutRoot.findViewById(R.id.iv_pk_view_audience_us_3);
        ivAudienceOtherTop1 = mLayoutRoot.findViewById(R.id.iv_pk_view_audience_other_1);
        ivAudienceOtherTop2 = mLayoutRoot.findViewById(R.id.iv_pk_view_audience_other_2);
        ivAudienceOtherTop3 = mLayoutRoot.findViewById(R.id.iv_pk_view_audience_other_3);
        ivPkResultUs = mLayoutRoot.findViewById(R.id.iv_pk_result_us);
        ivPkResultOther = mLayoutRoot.findViewById(R.id.iv_pk_result_other);
        ivPkStatus = mLayoutRoot.findViewById(R.id.iv_pk_view_status);
        viewUsPower = mLayoutRoot.findViewById(R.id.view_pk_user_power);
        viewOtherPower = mLayoutRoot.findViewById(R.id.view_pk_other_power);
        viewUsWindow = mLayoutRoot.findViewById(R.id.view_pk_us_window);
        viewOtherWindow = mLayoutRoot.findViewById(R.id.view_pk_other_window);
        tvUsPower = mLayoutRoot.findViewById(R.id.tv_pk_view_us_power);
        tvOtherPower = mLayoutRoot.findViewById(R.id.tv_pk_view_other_power);
        tvPkTime = mLayoutRoot.findViewById(R.id.tv_pk_view_time);
        viewPkTopLeft = mLayoutRoot.findViewById(R.id.view_pk_audience_left);
        viewPkTopRight = mLayoutRoot.findViewById(R.id.view_pk_audience_right);
        ivPkMuteOther = mLayoutRoot.findViewById(R.id.iv_pk_mute_other);

        ivPkResultUs.setVisibility(View.GONE);
        ivPkResultOther.setVisibility(View.GONE);


    }
    String uid = "";
    public void initPkData(final PkInfoBean pkInfoBean) {
        PkInfoBean.CurrentBean currentInfoBean = pkInfoBean.getCurrent();
        tvUsPower.setText(String.valueOf(currentInfoBean.getPk_score()));
        List<PkInfoBean.CurrentBean.PkTopBean> currentTopList = currentInfoBean.getPk_top();
        if (currentTopList.size() >= 1) {
            Glide.with(mContext).load(currentTopList.get(0).getHead_pic()).into(ivAudienceUsTop1);
        }
        if (currentTopList.size() >= 2) {
            Glide.with(mContext).load(currentTopList.get(1).getHead_pic()).into(ivAudienceUsTop2);
        }
        if (currentTopList.size() >= 3) {
            Glide.with(mContext).load(currentTopList.get(2).getHead_pic()).into(ivAudienceUsTop3);
        }


        PkInfoBean.OtherBean otherInfoBean = pkInfoBean.getOther();
        uid = pkInfoBean.getOther().getUid();
        tvOtherPower.setText(String.valueOf(otherInfoBean.getPk_score()));
        List<PkInfoBean.OtherBean.PkTopBeanX> otherTopList = otherInfoBean.getPk_top();
        if (otherTopList.size() >= 1) {
            Glide.with(mContext).load(otherTopList.get(0).getHead_pic()).into(ivAudienceOtherTop1);
        }
        if (otherTopList.size() >= 2) {
            Glide.with(mContext).load(otherTopList.get(1).getHead_pic()).into(ivAudienceOtherTop2);
        }
        if (otherTopList.size() >= 3) {
            Glide.with(mContext).load(otherTopList.get(2).getHead_pic()).into(ivAudienceOtherTop3);
        }

        LinearLayout.LayoutParams usLayoutParams = (LinearLayout.LayoutParams) viewUsPower.getLayoutParams();
        LinearLayout.LayoutParams otherLayoutParams = (LinearLayout.LayoutParams) viewOtherPower.getLayoutParams();
        int myScore = currentInfoBean.getPk_score();
        int otherScore = otherInfoBean.getPk_score();
        int myWeight,otherWeight;
        if (myScore == 0 && otherScore == 0) {
            myWeight = 1;
            otherWeight = 1;
        } else if (myScore > 9 * otherScore) {
            myWeight = 9;
            otherWeight = 1;
        } else if (otherScore > 9 * myScore) {
            myWeight = 1;
            otherWeight = 9;
        } else {
            myWeight = myScore;
            otherWeight = otherScore;
        }
        usLayoutParams.weight = myWeight;
        viewUsPower.setLayoutParams(usLayoutParams);
        otherLayoutParams.weight = otherWeight;
        viewOtherPower.setLayoutParams(otherLayoutParams);

        if (mHandler == null) {
            pkLength = pkInfoBean.getCurrent().getPk_end_pk_time_second();
            punishLength = pkInfoBean.getCurrent().getPk_end_punish_time_second();
            initPkViewTime(pkInfoBean.getPk_status() == 1,Long.parseLong(pkInfoBean.getCurrent().getPk_start_time()));
        }

        viewPkTopLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPkViewListener != null) {
                    onPkViewListener.doPkAudienceClick(pkInfoBean.getCurrent().getUid(),pkInfoBean.getOther().getUid(),true);
                }
            }
        });

        viewPkTopRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPkViewListener != null) {
                    onPkViewListener.doPkAudienceClick(pkInfoBean.getCurrent().getUid(),pkInfoBean.getOther().getUid(),false);
                }
            }
        });

        viewOtherWindow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPkViewListener != null) {
                    onPkViewListener.doPkOtherAnchorClick(pkInfoBean.getOther().getUid());
                }
            }
        });

        ivPkMuteOther.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPkViewListener != null) {
                    onPkViewListener.onMuteOtherVoice(pkInfoBean.getOther().getUid(),pkInfoBean.getOther().getNickname(),!isMuteOther);
                }
            }
        });

        if (pkInfoBean.getPk_status() == 2) {
            showResultView();
            Log.d("pkpkpk",pkInfoBean.getCurrent().getIs_win() + " ---- " + pkInfoBean.getOther().getIs_win());
            if (pkInfoBean.getCurrent().getIs_win() == pkInfoBean.getOther().getIs_win()) {
                ivPkResultUs.setImageResource(R.drawable.ic_live_pk_rect_level);
                ivPkResultOther.setImageResource(R.drawable.ic_live_pk_rect_level);
            } else if (pkInfoBean.getCurrent().getIs_win() > pkInfoBean.getOther().getIs_win()) {
                ivPkResultUs.setImageResource(R.drawable.ic_live_pk_rect_victor);
                ivPkResultOther.setImageResource(R.drawable.ic_live_pk_rect_fail);
            } else {
                ivPkResultUs.setImageResource(R.drawable.ic_live_pk_rect_fail);
                ivPkResultOther.setImageResource(R.drawable.ic_live_pk_rect_victor);
            }
        }

    }


    //显示pk结果
    void showResultView() {
        ivPkResultUs.setVisibility(View.VISIBLE);
        ivPkResultOther.setVisibility(View.VISIBLE);
    }

    void initPkViewTime(long time) {
        long startTime = time;
        long currentTime = System.currentTimeMillis() / 1000;
        int duration = (int) (currentTime - startTime);
        if (duration < 300) { //pk状态
            pkTime = 300 - duration;
            startPkTime();
            ivPkStatus.setImageResource(R.drawable.ic_pk_status_pk);
            tvPkTime.setTextColor(mContext.getResources().getColor(R.color.white));
        } else { //惩罚状态
            punishTime = 480 - duration;
            startPunishTime();
            ivPkStatus.setImageResource(R.drawable.ic_pk_status_punish);
            tvPkTime.setTextColor(mContext.getResources().getColor(R.color.colorPkPunish));
        }
    }

    long startTime;
    void initPkViewTime(boolean isPkIng,long time) {
        startTime = time;
        long currentTime = System.currentTimeMillis() / 1000;
        int duration = (int) (currentTime - startTime);
        if (isPkIng) { //pk状态
            pkTime = pkLength - duration;
            punishTotalTime = pkLength + punishLength;
            startPkTime();
            ivPkStatus.setImageResource(R.drawable.ic_pk_status_pk);
            tvPkTime.setTextColor(mContext.getResources().getColor(R.color.white));
        } else { //惩罚状态
            if (duration >= punishLength) {
                showLinkView();
            } else {
                punishTime = punishLength - duration;
                punishTotalTime = punishLength;
                startPunishTime();
                ivPkStatus.setImageResource(R.drawable.ic_pk_status_punish);
                tvPkTime.setTextColor(mContext.getResources().getColor(R.color.colorPkPunish));
            }
        }
    }

    Handler mHandler;
    Runnable mRunnable;
    int pkTime = 300;
    int punishTime = 120;
    int punishTotalTime = 420;

    int pkLength = 300; //pk时长
    int punishLength = 300; //惩罚时长
    void startPkTime() {
        mPkStatus = 1;
        ivPkStatus.setImageResource(R.drawable.ic_pk_status_pk);
        tvPkTime.setTextColor(mContext.getResources().getColor(R.color.white));
        mHandler = new Handler(Looper.myLooper());
        mHandler.removeCallbacks(mRunnable);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                pkTime = (int) ((300 - ((System.currentTimeMillis() / 1000) - startTime)));
                if (pkTime <= 0) {
                    mHandler.removeCallbacks(mRunnable);
                    startPunishTime();
                    if (onPkViewListener != null) {
                        onPkViewListener.onPkTimeOut(uid);
                    }
                    ivPkStatus.setImageResource(R.drawable.ic_pk_status_punish);
                    tvPkTime.setTextColor(mContext.getResources().getColor(R.color.colorPkPunish));
                } else {
                    showPkTime(pkTime);
                    mHandler.postDelayed(mRunnable,1000);
                }
            }
        };
        mHandler.postDelayed(mRunnable,1000);
    }

    int mPkStatus = 1;  //1-pk 2-惩罚 3-连线

    void startPunishTime() {
        mPkStatus = 2;
        mHandler = new Handler(Looper.myLooper());
        mHandler.removeCallbacks(mRunnable);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                punishTime = (int) ((punishTotalTime - ((System.currentTimeMillis() / 1000) - startTime)));
                if (punishTime <= 0) {
                    if (onPkViewListener != null) {
                        onPkViewListener.onPunishTimeOut(uid);
                    }
                } else {
                    showPkTime(punishTime);
                    mHandler.postDelayed(mRunnable,1000);
                }
            }
        };
        mHandler.postDelayed(mRunnable,1000);
    }

    void showPkTime(int pkTime) {
        if (pkTime < 60) {
            tvPkTime.setText("0:" + getSecondTime(pkTime));
        } else {
            tvPkTime.setText((pkTime/60) + ":" + getSecondTime(pkTime%60));
        }
    }

    String getSecondTime(int second) {
        return second >= 10 ? String.valueOf(second) : "0" + String.valueOf(second);
    }

    public boolean isShow() {
        return mLayoutRoot.getVisibility() == View.VISIBLE;
    }

    public void showPkView() {
        mLayoutRoot.setVisibility(View.VISIBLE);
    }


    public void hidePkView() {
        mLayoutRoot.setVisibility(View.GONE);
        clearPkStatus();
    }

    public void clearPkStatus() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
        pkTime = pkLength;
        punishTime = punishLength;
        ivAudienceUsTop1.setImageResource(R.color.transparent);
        ivAudienceUsTop2.setImageResource(R.color.transparent);
        ivAudienceUsTop3.setImageResource(R.color.transparent);
        ivAudienceOtherTop1.setImageResource(R.color.transparent);
        ivAudienceOtherTop2.setImageResource(R.color.transparent);
        ivAudienceOtherTop3.setImageResource(R.color.transparent);
        tvPkTime.setText("5:00");
        ivPkResultUs.setVisibility(View.GONE);
        ivPkResultOther.setVisibility(View.GONE);
        ivPkStatus.setVisibility(View.VISIBLE);
    }

    public void retryPk() {

    }

    public void showLinkView() {
        mPkStatus = 3;
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
        ivPkStatus.setVisibility(View.GONE);
        tvPkTime.setText("主播连线中");
        tvPkTime.setTextColor(mContext.getResources().getColor(R.color.colorPkLink));
    }

    public void refreshMuteStatus(boolean mute) {
        isMuteOther = mute;
        if (isMuteOther) {
            ivPkMuteOther.setImageResource(R.drawable.ic_pk_other_audio_mute);
        } else {
            ivPkMuteOther.setImageResource(R.drawable.ic_pk_other_audio_open);
        }
    }

    public interface OnPkViewListener {
        void doPkAudienceClick(String uid, String otherUid, boolean isUs);
        void doPkOtherAnchorClick(String uid);
        void onPkTimeOut(String uid);
        void onPunishTimeOut(String uid);
        void onMuteOtherVoice(String uid, String name, boolean isMute);
    }

    OnPkViewListener onPkViewListener;
    public void setOnPkViewListener(OnPkViewListener onPkViewListener) {
        this.onPkViewListener = onPkViewListener;
    }

    public void setIsAnchor(boolean isAnchor) {
        this.isAnchor = isAnchor;
        if (isAnchor) {
            ivPkMuteOther.setVisibility(View.VISIBLE);
        } else {
            ivPkMuteOther.setVisibility(View.GONE);
        }
    }

    public boolean isLinkStatus() {
        return mPkStatus == 3;
    }


}
