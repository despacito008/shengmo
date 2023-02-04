package com.tencent.qcloud.tim.tuikit.live.component.manager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class LiveManagerLayout extends LinearLayout {
    RecyclerView mRvManager;
    LinearLayout mLayoutRoot;
    Context mContext;

    List<String> mUserIdList;
    List<V2TIMUserFullInfo> mUserFullInfoList;
    LiveManagerAdapter mManagerAdapter;

    public LiveManagerLayout(Context context) {
        super(context);
        initView(context);
    }

    public LiveManagerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LiveManagerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutRoot = (LinearLayout) inflate(context, R.layout.live_layout_manager_view, this);
        mRvManager = mLayoutRoot.findViewById(R.id.rv_live_manager);
        mUserIdList = new ArrayList<>();
        mUserFullInfoList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvManager.setLayoutManager(layoutManager);
        mManagerAdapter = new LiveManagerAdapter(mContext, mUserFullInfoList);
        mRvManager.setAdapter(mManagerAdapter);
        mManagerAdapter.setOnLiveManagerListener(new LiveManagerAdapter.OnLiveManagerListener() {
            @Override
            public void onLiveManagerClick(String uid) {
                if (onLiveManagerListener != null) {
                    onLiveManagerListener.doManagerClick(uid);
                }
            }
        });
    }

    public void addManager(String uid) {
        if (!mUserIdList.contains(uid)) {
            mUserIdList.add(uid);
            refreshData();
        }

    }

    public void removeManager(String uid) {
        if (mUserIdList.contains(uid)) {
            mUserIdList.remove(uid);
            refreshData();
        }
    }

    public void refreshManager(List<String> uidList) {
        mUserIdList.clear();
        mUserIdList.addAll(uidList);
        refreshData();
    }

    public void refreshData() {
        if (mUserIdList.size() == 0) {
            mUserFullInfoList.clear();
            mManagerAdapter.notifyDataSetChanged();
            return;
        }
//        final HashMap<String,V2TIMUserFullInfo> userFullInfoHashMap = new HashMap<>();
//        for (String uid: mUserIdList) {
//            userFullInfoHashMap.put(uid,null);
//        }
        V2TIMManager.getInstance().getUsersInfo(mUserIdList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> userFullInfoList) {
//                for (V2TIMUserFullInfo info: userFullInfoList) {
//                    userFullInfoHashMap.put(info.getUserID(),info);
//                }
//                List<V2TIMUserFullInfo> tempList = new ArrayList<>();
//                for (String uid: mUserIdList) {
//                    tempList.add(userFullInfoHashMap.get(uid));
//                }
//                mUserFullInfoList.clear();
//                mUserFullInfoList.addAll(tempList);
//                mManagerAdapter.notifyDataSetChanged();


                Collections.sort(userFullInfoList,new userSort());
                mUserFullInfoList.clear();
                mUserFullInfoList.addAll(userFullInfoList);
                mManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public interface OnLiveManagerListener {
        void doManagerClick(String uid);
    }

    OnLiveManagerListener onLiveManagerListener;

    public void setOnLiveManagerListener(OnLiveManagerListener onLiveManagerListener) {
        this.onLiveManagerListener = onLiveManagerListener;
    }

    class userSort implements Comparator<V2TIMUserFullInfo> {
        @Override
        public int compare(V2TIMUserFullInfo o1, V2TIMUserFullInfo o2) {
            return mUserIdList.indexOf(o1.getUserID()) - mUserIdList.indexOf(o2.getUserID());
        }
    }
}
