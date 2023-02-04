package com.aiwujie.shengmo.timlive.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.timlive.adapter.MoreListAdapter;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.tencent.liteav.login.ProfileManager;

import java.util.ArrayList;
import java.util.List;
import razerdp.basepopup.BasePopupWindow;

import static com.aiwujie.shengmo.timlive.net.RoomManager.createRoom;
import static com.aiwujie.shengmo.timlive.net.RoomManager.enterRoom;
import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;
import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getString;

public class MoreLivePop extends BasePopupWindow implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = MoreLivePop.this.getClass().getSimpleName();
    private final Context context;
    private List<ScenesRoomInfoBean> mRoomInfoList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mMoreList;
    private MoreListAdapter mMoreListAdapter;

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.app_pop_more_live);
    }

    public MoreLivePop(Context context) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.RIGHT);
        //加入动画
        ObjectAnimator.ofFloat(getContentView(), "translationX", getWidth(), 0).setDuration(150).start();
        initialize();
        getScenesRoomInfos();
    }

    public void initialize(){
        mSwipeRefreshLayout = findViewById(R.id.live_more_swipe_refresh_layout_list);
        mMoreList = findViewById(R.id.live_room_more_list);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mMoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMoreListAdapter = new MoreListAdapter(getContext(), mRoomInfoList, new MoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ScenesRoomInfoBean roomInfo) {
                String selfUserId = ProfileManager.getInstance().getUserModel().userId;
                if (roomInfo.getUid().equals(selfUserId)) {
                    createRoom(context,selfUserId);
                } else {
                    enterRoom(context,mRoomInfoList,position,TAG);
                }
            }
        });
        mMoreList.setAdapter(mMoreListAdapter);
        mMoreList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int space = getResources().getDimensionPixelOffset(R.dimen.page_margin);//(pop=110dp rycleview-item=100dp margin=10dp)
                outRect.top = space/2; //7.5
                outRect.left = space/2; //7.5
                outRect.right = space/2; //7.5
            }
        });
        if(mMoreListAdapter != null){
            mMoreListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh() {
        getScenesRoomInfos();
    }
    private void getScenesRoomInfos() {
        if(getContext() == null) return;
        RoomManager.getInstance().getScenesRoomInfos(getContext(), RoomManager.TYPE_LIVE_ROOM, new RoomManager.GetScenesRoomInfosCallback() {

            public void onSuccess(List<ScenesRoomInfoBean> scenesRoomInfos) {
                mRoomInfoList.clear();
                mRoomInfoList.addAll(scenesRoomInfos);
                mSwipeRefreshLayout.setRefreshing(false);
                refreshView();
            }

            @Override
            public void onFailed(int code, String msg) {
                if(getContext() == null) return;
                Log.e(TAG, "onFailed: code -> " + code + ", msg -> " + msg);
                Toast.makeText(getContext(), getString(R.string.online_fail) + msg, Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                refreshView();
            }
        });
    }
    private void refreshView() {
        if(mMoreListAdapter != null){
            mMoreListAdapter.notifyDataSetChanged();
        }
    }
}
