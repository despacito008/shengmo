package com.aiwujie.shengmo.timlive.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.tencent.liteav.login.ProfileManager;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScenesLayout extends RelativeLayout implements SwipeRefreshLayout.OnRefreshListener {
    private static int spanCount = 2; //默认两列
    private List<ScenesRoomInfoBean> mRoomInfoList;
    @BindView(R.id.live_scenes_hot_room_swipe_refresh_layout_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.live_scenes_live_room_hot_room_list)
    RecyclerView mRecyclerView;
    private RoomListAdapter mRoomListAdapter;
    private static final String TAG = ScenesLayout.class.getSimpleName();
    private Context mContext;

    public void setRoomInfoList(List<ScenesRoomInfoBean> mRoomInfoList) {
        this.mRoomInfoList = mRoomInfoList;
    }

    public ScenesLayout(@NonNull Context context,List<ScenesRoomInfoBean> mRoomInfoList) {
        super(context);
        this.mRoomInfoList = mRoomInfoList;
        initialize(context);
    }

    public ScenesLayout(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ScenesLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(@NonNull Context context) {
        mContext = context;
        View view = inflate(mContext, R.layout.live_layout_scenes, this);
        ButterKnife.bind(this,view);
        initListener();
    }

    private void initListener() {
        if(getContext() == null) return;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mRoomListAdapter = new RoomListAdapter(getContext(), mRoomInfoList, new RoomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ScenesRoomInfoBean roomInfo) {
                String selfUserId = ProfileManager.getInstance().getUserModel().userId;
                if(roomInfo.getIs_live().equals("0")) { //是否直播 1是0不是
                    //ToastUtil.show(mContext,getString(R.string.current_anchor_is_not_live));
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("uid", roomInfo.getUid());
                    mContext.startActivity(intent);
                } else{
                    if (roomInfo.getUid().equals(selfUserId)) {
                        RoomManager.createRoom(mContext);
                    } else {
                        RoomManager.enterRoom(mContext,mRoomInfoList,position,TAG);
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mRoomListAdapter);
        mRoomListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mRoomListAdapter.notifyDataSetChanged();
    }
}
