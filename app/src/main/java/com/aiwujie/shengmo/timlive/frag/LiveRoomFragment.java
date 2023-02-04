package com.aiwujie.shengmo.timlive.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.kt.ui.activity.PlayBackVideoActivity;
import com.aiwujie.shengmo.qnlive.utils.QNRoomManager;
import com.aiwujie.shengmo.timlive.adapter.RoomListAdapter;
import com.aiwujie.shengmo.timlive.bean.ScenesRoomInfo;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




/**
 * 公共的容器类
 * @author derikli
 */
public class LiveRoomFragment extends Fragment{
    private static final String TAG = LiveRoomFragment.class.getSimpleName();
    @BindView(R.id.live_room_hot_room_list)
    RecyclerView mRecyclerView;
    private RoomListAdapter mRoomListAdapter;
    private String type;
    private List<ScenesRoomInfoBean> mRoomInfoList;
    private List<ScenesRoomInfoBean> mData = new ArrayList<>();

    public void getInstance(int span,List<ScenesRoomInfoBean> rRoomInfoList,String type) {
        this.type = type;
        mRoomInfoList = new ArrayList<>();
        initListener(span,rRoomInfoList);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_live_room,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    void initListener(int span, final List<ScenesRoomInfoBean> roomInfoList){
        mRoomInfoList.clear();
        mRoomInfoList.addAll(roomInfoList);
        mRoomListAdapter = new RoomListAdapter(getContext(), mRoomInfoList,onItemClickListener,type);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), span));
        mRecyclerView.setAdapter(mRoomListAdapter);
        mRoomListAdapter.notifyDataSetChanged();
        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setNestedScrollingEnabled(false);
    }

    public void notifyData(List<ScenesRoomInfoBean> tempData,int page) {
        if(page ==1){
            //mRoomListAdapter = new RoomListAdapter(getContext(),tempData,onItemClickListener,type);
            //mRecyclerView.setAdapter(mRoomListAdapter);
            mRoomInfoList.clear();
            mRoomInfoList.addAll(tempData);
            mRoomListAdapter.notifyDataSetChanged();
        }else{
            int temp = mRoomInfoList.size();
            mRoomInfoList.addAll(tempData);
            mRoomListAdapter.notifyItemRangeInserted(temp,tempData.size());
//            mData.addAll(tempData);
//            mRoomListAdapter.notifyItemRangeInserted(temp,mData.size());
        }
    }

    public void notifyData() {
        List<ScenesRoomInfoBean> noData = new ArrayList<>();
        mRoomListAdapter = new RoomListAdapter(getContext(),noData,onItemClickListener);
        mRecyclerView.setAdapter(mRoomListAdapter);
    }

    RoomListAdapter.OnItemClickListener onItemClickListener = new RoomListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, ScenesRoomInfoBean roomInfo) {
            String selfUserId = ProfileManager.getInstance().getUserModel().userId;
            if (!TextUtil.isEmpty(roomInfo.getLive_log_id()) && !"0".equals(roomInfo.getLive_log_id())) {
                PlayBackVideoActivity.Companion.start(getActivity(),roomInfo.getLive_log_id(),roomInfo.getUid(),"1");
            } else {
                if ("1".equals(roomInfo.getIs_live())) { //直播中
                    preEnterRoom2(position);
                } else { //已结束
                    UserInfoActivity.start(getActivity(),roomInfo.getUid());
                }
            }
        }
    };

    //只传入正在直播的主播
    void preEnterRoom(int index) {
        List<ScenesRoomInfoBean> tempRoomList = new ArrayList<>();
        for (int i = 0; i < mRoomInfoList.size(); i++) {
            if ("1".equals(mRoomInfoList.get(i).getIs_live())) {
                tempRoomList.add(mRoomInfoList.get(i));
            }
        }
        if (tempRoomList.size() > index) {
            RoomManager.enterRoom(getActivity(),tempRoomList,index,TAG);
        } else {
            Intent intent = new Intent(getActivity(),UserInfoActivity.class);
            intent.putExtra("uid",mRoomInfoList.get(index).getUid());
            startActivity(intent);
        }
    }

    //只传入正在直播的主播
    void preEnterRoom2(int index) {
        //List<ScenesRoomInfoBean> tempRoomList = new ArrayList<>();
        //tempRoomList.add(mRoomInfoList.get(index));
        //RoomManager.enterRoom(getActivity(),mRoomInfoList.get(index));
//        QNRoomManager.getInstance().gotoLiveRoom(getActivity(),
//                mRoomInfoList.get(index).getUid(),
//                mRoomInfoList.get(index).getRoom_id());

        RoomManager.enterLiveRoom(getActivity(),
                mRoomInfoList.get(index).getUid(),
                mRoomInfoList.get(index).getRoom_id());
    }
}
