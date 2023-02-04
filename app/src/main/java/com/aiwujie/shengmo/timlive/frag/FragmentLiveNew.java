package com.aiwujie.shengmo.timlive.frag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.bean.SearchUserData;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.qnlive.utils.QNRoomManager;
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.utils.SafeGridLayoutManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.liteav.login.ProfileManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * 最新
 * @author derikli
 */
public class FragmentLiveNew extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnRefreshLoadMoreListener {
    private static final String TAG = FragmentLiveRedHot.class.getSimpleName();
    private int page = 0; //分页加载的页码
    private boolean isLoading = false; //是否正在加载
    @BindView(R.id.live_new_swipe_refresh_layout_list)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.no_data)
    LinearLayout ivNoData;//没有数据
    @BindView(R.id.tv_anchor_no_data)
    TextView tvNoData;
    @BindView(R.id.rv_live_new)
    RecyclerView mRecycleView;
    private Handler handler = new Handler();
    List<ScenesRoomInfoBean> scenesRoomInfos = new ArrayList<>();
    List<ScenesRoomInfoBean> mData = new ArrayList<>();
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    private RoomListAdapter mRoomListAdapter;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {

    }

    private void onVisible() {
        if(isVisible){
            lazyLoad();
        }
    }
    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            isFirstLoad = false;
            smartRefreshLayout.setOnRefreshLoadMoreListener(this);
            //实现自动刷新
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getNewAnchorList();
                }
            }, 100);
        }
    }

    @Override
    public void onRefresh() {
        getNewAnchorList();
    }
    //获取最新列表
    private void getNewAnchorList() {
        if(isLoading) return;
        isLoading = true;
        LiveHttpHelper.getInstance().newAnchorList(page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                Log.d(TAG,data);
                isLoading = false;
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                SearchUserData scenesRoomInfo = new Gson().fromJson(data, SearchUserData.class);
                scenesRoomInfos.clear();
                scenesRoomInfos.addAll(scenesRoomInfo.getData());
                notifyData(scenesRoomInfos,page);
            }

            @Override
            public void onFail(String msg) {
                isLoading = false;
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                notifyData(scenesRoomInfos,page);
                ToastUtil.show(getActivity(),msg);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_live_new,container,false);
        ButterKnife.bind(this,view);
        isPrepared = true;
        initView();
        return view;
    }

    private void initView() {
        mRecycleView.setLayoutManager(new SafeGridLayoutManager(getContext(), 2));
        //mRecycleView.setHasFixedSize(true);
        //mRecycleView.setNestedScrollingEnabled(false);
        tvNoData.setText(getString(R.string.no_anchor_list));
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
        page ++;
        getNewAnchorList();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 0;
        getNewAnchorList();
    }

    /**
     * 更新数据
     * @param tempData
     * @param page
     */
    public void notifyData(List<ScenesRoomInfoBean> tempData,int page) {
        if(scenesRoomInfos != null && scenesRoomInfos.size() > 0){
            if (tempData.size() < 20) {
                smartRefreshLayout.finishLoadMore(true);
            } else {
                smartRefreshLayout.finishLoadMore(false);
            }
            if(page ==0){
                mRoomListAdapter = new RoomListAdapter(getContext(),tempData,onItemClickListener);
                mRecycleView.setAdapter(mRoomListAdapter);
                mRoomListAdapter.notifyDataSetChanged();
            }else{
                int temp = tempData.size();
                mData.addAll(tempData);
                mRoomListAdapter.notifyItemRangeInserted(temp,mData.size());
            }
            ivNoData.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        } else {
            if(page == 0){
                ivNoData.setVisibility(View.VISIBLE);
                mRecycleView.setVisibility(View.GONE);
            }
        }
    }

    RoomListAdapter.OnItemClickListener onItemClickListener = new RoomListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, ScenesRoomInfoBean roomInfo) {
            String selfUserId = ProfileManager.getInstance().getUserModel().userId;
            if (roomInfo.getUid().equals(selfUserId)) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("uid",roomInfo.getUid());
                startActivity(intent);
            } else {
               // enterRoom(getActivity(),scenesRoomInfos,position,TAG);
               // RoomManager.enterRoom(getActivity(),roomInfo.getUid(),roomInfo.getRoom_id());
               // QNRoomManager.getInstance().gotoLiveRoom(getActivity(),roomInfo.getUid(),roomInfo.getRoom_id());
                RoomManager.enterRoom(getActivity(),roomInfo.getUid(),roomInfo.getRoom_id());
            }
        }
    };
}
