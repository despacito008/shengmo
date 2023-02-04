package com.aiwujie.shengmo.qnlive.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.qnlive.adapter.QNRemoteViewAdapter;
import com.aiwujie.shengmo.utils.LogUtil;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.QNTrackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QNLiveRtcLayout extends ConstraintLayout {
    Context mContext;
    ConstraintLayout mLayout;
    QNSurfaceView anchorSurfaceView,audienceSurfaceView;
    RecyclerView rvSurfaceView;
    public QNLiveRtcLayout(Context context) {
        super(context);
        initView(context);
    }

    public QNLiveRtcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public QNLiveRtcLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    QNRemoteViewAdapter remoteViewAdapter;
    List<List<QNTrackInfo>> remoteStreamList;
    Map<String,List<QNTrackInfo>> remoteStreamMap;
    void initView(Context context) {
        mContext = context;
        mLayout = (ConstraintLayout) inflate(mContext, R.layout.app_layout_live_rtc,this);
        anchorSurfaceView = mLayout.findViewById(R.id.surface_view_anchor);
        audienceSurfaceView = mLayout.findViewById(R.id.surface_view_audience);
        rvSurfaceView = mLayout.findViewById(R.id.rv_surface_view_audience);
        initRecyclerView();
    }

    void initRecyclerView() {
        remoteStreamList = new ArrayList<>();
        remoteStreamMap = new HashMap<>();
        remoteViewAdapter = new QNRemoteViewAdapter(mContext, remoteStreamList,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setReverseLayout(true);
        rvSurfaceView.setAdapter(remoteViewAdapter);
        rvSurfaceView.setLayoutManager(layoutManager);
        remoteViewAdapter.setOnRemoteViewListener(new QNRemoteViewAdapter.OnRemoteViewListener() {
            @Override
            public void doRemoteClose(int index) {

            }

            @Override
            public void doRemoteClick(int index) {
                if (onQNLiveRtcListener != null) {
                    onQNLiveRtcListener.showCard(remoteStreamList.get(index).get(0).getUserId());
                }
            }

            @Override
            public void doRemoteRender(QNSurfaceView surfaceView, QNTrackInfo qnTrackInfo) {
                if (onQNLiveRtcListener != null) {
                    onQNLiveRtcListener.doSurfaceRender(qnTrackInfo,surfaceView);
                }
            }
        });
    }


    public void initAnchorSurface(List<QNTrackInfo> qnTrackInfoList) {
       for (QNTrackInfo qnTrackInfo: qnTrackInfoList) {
           if (qnTrackInfo.isVideo()) {
               if (onQNLiveRtcListener != null) {
                   onQNLiveRtcListener.doSurfaceRender(qnTrackInfo,anchorSurfaceView);
               }
           }
       }
    }

    public void initUsSurface(List<QNTrackInfo> qnTrackInfoList) {
        Log.d("on us render","" + qnTrackInfoList.size());
        for (QNTrackInfo qnTrackInfo: qnTrackInfoList) {
            if (qnTrackInfo.isVideo()) {
                if (onQNLiveRtcListener != null) {
                    onQNLiveRtcListener.doSurfaceRender(qnTrackInfo,audienceSurfaceView);
                }
            }
        }
    }

    public void addOtherSurface(String userId,List<QNTrackInfo> qnTrackInfoList) {
        if (!remoteStreamMap.containsKey(userId)) {
            remoteStreamMap.put(userId,qnTrackInfoList);
            remoteStreamList.add(qnTrackInfoList);
            LogUtil.d("addSurface " + remoteStreamList.size());
            remoteViewAdapter.notifyDataSetChanged();
        }
    }

    public void removeOtherSurface(String userId,List<QNTrackInfo> qnTrackInfoList) {
        if (remoteStreamMap.containsKey(userId)) {
            remoteStreamList.remove(remoteStreamMap.get(userId));
            remoteStreamMap.remove(userId);
            LogUtil.d("removeSurface " + remoteStreamList.size());
            remoteViewAdapter.notifyDataSetChanged();
        }
    }

    public interface OnQNLiveRtcListener {
        void doSurfaceRender(QNTrackInfo qnTrackInfo, QNSurfaceView surfaceView);
        void showCard(String uid);
    }

    OnQNLiveRtcListener onQNLiveRtcListener;

    public void setOnQNLiveRtcListener(OnQNLiveRtcListener onQNLiveRtcListener) {
        this.onQNLiveRtcListener = onQNLiveRtcListener;
    }

    public void hideRtcView() {
        rvSurfaceView.setVisibility(GONE);
        anchorSurfaceView.setVisibility(GONE);
        audienceSurfaceView.setVisibility(GONE);
        mLayout.setVisibility(GONE);
        remoteStreamList.clear();
    }

    public void showRtcView() {
        rvSurfaceView.setVisibility(VISIBLE);
        anchorSurfaceView.setVisibility(VISIBLE);
        audienceSurfaceView.setVisibility(VISIBLE);
        mLayout.setVisibility(VISIBLE);
    }
}
