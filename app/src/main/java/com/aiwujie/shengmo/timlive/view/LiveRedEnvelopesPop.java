package com.aiwujie.shengmo.timlive.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.RedBaoshouActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.LiveRedEnvelopesReceiveBean;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.timlive.adapter.RedEnvelopesReceiveAdapter;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class LiveRedEnvelopesPop extends BasePopupWindow {
    Context mContext;
    LiveRedEnvelopesBean.DataBean mRedEnvelopesBean;
    public LiveRedEnvelopesPop(Context context,LiveRedEnvelopesBean.DataBean redEnvelopesBean) {
        super(context);
        mContext = context;
        mRedEnvelopesBean = redEnvelopesBean;
        initView();
    }
    boolean mIsReceive;
    public LiveRedEnvelopesPop(Context context,LiveRedEnvelopesBean.DataBean redEnvelopesBean,boolean isReceive) {
        super(context);
        mContext = context;
        mRedEnvelopesBean = redEnvelopesBean;
        mIsReceive = isReceive;
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.app_pop_live_red_envelopes);
    }
    ImageView ivRedIcon;
    ImageView ivRedGet;
    void initView() {
        TextView tvRedName = findViewById(R.id.tv_live_red_envelopes_name);
        ivRedIcon = findViewById(R.id.civ_live_red_envelopes_icon);
        ivRedGet = findViewById(R.id.iv_live_red_enveloped_get);
        llReceive = findViewById(R.id.ll_live_red_envelops_receive);
        smartRefreshLayout = findViewById(R.id.smart_refresh_live_red_envelopes);
        rvReceive = findViewById(R.id.rv_live_red_envelopes);
        tvReceive = findViewById(R.id.tv_receive_info);
        tvRedName.setText(mRedEnvelopesBean.getNickname());
        Glide.with(mContext).load(mRedEnvelopesBean.getHead_pic()).into(ivRedIcon);
        ivRedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveRedEnvelopes != null) {
                    onLiveRedEnvelopes.doLiveUserClick(mRedEnvelopesBean.getUid());
                }
             }
        });
        ivRedGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivRedGet.setEnabled(false);
                receiveLiveRedEnvelopes();
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                showReceiveUserView();
            }
        });
        if (mIsReceive) {
            showReceiveUserView();
        }
    }

    void receiveLiveRedEnvelopes() {
        ObjectAnimator ra = ObjectAnimator.ofFloat(ivRedGet, "rotationY", 0f, 720f);
        ra.setDuration(1000);
        ra.start();
        HttpHelper.getInstance().receiveRedBag(mRedEnvelopesBean.getOrderid(), new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data,String msg) {
                ToastUtil.show(mContext,msg);
                ivRedIcon.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showReceiveUserView();
                    }
                },500);

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(mContext,msg);
            }
        });
    }

    public interface OnLiveRedEnvelopesListener {
        void doLiveUserClick(String uid);
        void refreshRedEnvelopes();
    }

    OnLiveRedEnvelopesListener onLiveRedEnvelopes;

    public void setOnLiveRedEnvelopes(OnLiveRedEnvelopesListener onLiveRedEnvelopes) {
        this.onLiveRedEnvelopes = onLiveRedEnvelopes;
    }
    LinearLayout llReceive;
    SmartRefreshLayout smartRefreshLayout;
    RecyclerView rvReceive;
    TextView tvReceive;
    void showReceiveUserView() {
        llReceive.setVisibility(View.VISIBLE);
        HttpHelper.getInstance().getRedBagHistoryListNew(mRedEnvelopesBean.getOrderid(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                smartRefreshLayout.finishRefresh();
                LiveRedEnvelopesReceiveBean redEnvelopesBean = GsonUtil.GsonToBean(data, LiveRedEnvelopesReceiveBean.class);
                if (redEnvelopesBean != null && redEnvelopesBean.getData() != null) {
                    List<LiveRedEnvelopesReceiveBean.DataBean.ListBean> redEnvelopesList = redEnvelopesBean.getData().getList();
                    RedEnvelopesReceiveAdapter adapter = new RedEnvelopesReceiveAdapter(mContext,redEnvelopesList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                    tvReceive.setText(redEnvelopesBean.getData().getText());
                    rvReceive.setAdapter(adapter);
                    rvReceive.setLayoutManager(layoutManager);
                }
            }

            @Override
            public void onFail(int code, String msg) {
                smartRefreshLayout.finishRefresh();
            }
        });
        if (onLiveRedEnvelopes != null) {
            onLiveRedEnvelopes.refreshRedEnvelopes();
        }
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }
}
