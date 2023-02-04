package com.tencent.qcloud.tim.tuikit.live.component.topbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.widget.ImageView;


import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopAudienceListAdapter extends RecyclerView.Adapter<TopAudienceListAdapter.ViewHolder> {
    private static final String TAG = "TopAudienceListAdapter";
    private Context mContext;

    private List<TRTCLiveRoomDef.TRTCLiveUserInfo>            mAudienceList;
    private OnItemClickListener                               mOnItemClickListener;
    private HashMap<String, TRTCLiveRoomDef.TRTCLiveUserInfo> mAudienceMap;
    private HashMap<String,Integer> mRoles;
    private static List<String> mAudienceTopList = new ArrayList<>();

    public TopAudienceListAdapter(Context mContext,List<TRTCLiveRoomDef.TRTCLiveUserInfo> audienceList, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        mAudienceList = audienceList;
        mOnItemClickListener = onItemClickListener;
        mAudienceMap = new HashMap<>();
        mRoles = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.live_item_top_audience_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.display(position);
        //TRTCLiveRoomDef.TRTCLiveUserInfo item = mAudienceList.get(position);
        //holder.bind(mContext,mRoles,item,position,mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mAudienceList.size();
    }

    public void addAudienceUser(final List<String> userIdList){
        V2TIMManager.getInstance().getUsersInfo(userIdList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                mAudienceList.clear();
                List<TRTCLiveRoomDef.TRTCLiveUserInfo> mData = new ArrayList<>();
                List<TRTCLiveRoomDef.TRTCLiveUserInfo> tempList = new ArrayList<>();
                Map<String,TRTCLiveRoomDef.TRTCLiveUserInfo> tempMap = new HashMap<>();
                for (V2TIMUserFullInfo userInfo : v2TIMUserFullInfos) {
                    TRTCLiveRoomDef.TRTCLiveUserInfo user = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    user.userId = userInfo.getUserID();
                    user.userName = userInfo.getNickName();
                    user.avatarUrl = userInfo.getFaceUrl();
                    user.role = userInfo.getRole();
                    tempMap.put(userInfo.getUserID(),user);
                }

                for (String id: mAudienceTopList) {
                    tempList.add(tempMap.get(id));
                }

                for (String id: userIdList) {
                    if (mAudienceTopList != null && mAudienceTopList.contains(id)) {
                        continue;
                    }
                    tempList.add(tempMap.get(id));
                }
                mAudienceList.addAll(tempList);
                notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String desc) {
                TUILiveLog.e(TAG, "getUserInfoBatch failed:" + code + ", desc:" + desc);
            }
        });
    }

    public void addAudienceUser(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (userInfo.userId == null) {
            return;
        }
        if (!mAudienceMap.containsKey(userInfo.userId)) {
            mAudienceList.add(userInfo);
            mAudienceMap.put(userInfo.userId, userInfo);
            notifyDataSetChanged();
        }
    }

    public void removeAudienceUser(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (userInfo.userId == null) {
            return;
        }
        TRTCLiveRoomDef.TRTCLiveUserInfo localUserInfo = mAudienceMap.get(userInfo.userId);
        if (localUserInfo != null) {
            mAudienceList.remove(localUserInfo);
            mAudienceMap.remove(userInfo.userId);
            notifyDataSetChanged();
        }
    }

    public void setAudienceTopListUser(List<String> list) {
        mAudienceTopList.clear();
        mAudienceTopList.addAll(list);
        //notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImageAudienceIcon;
        private ImageView ivHeadView;
        private ImageView userIdentityIcon;
        private View ivAudienceBg;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void bind(Context mContext,HashMap<String, Integer> mRoles,final TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo, int pos, final OnItemClickListener listener) {
            if (!TextUtils.isEmpty(audienceInfo.avatarUrl)) {
                GlideEngine.loadImage(mImageAudienceIcon, audienceInfo.avatarUrl);
            } else {
                GlideEngine.loadImage(mImageAudienceIcon, R.drawable.live_default_head_img);
            }

            if(mAudienceTopList != null && mAudienceTopList.size() > 0){
                //1.判断是否在打赏前三榜单里边
                if(mAudienceTopList.contains(audienceInfo.userId) ){
                    for (int i = 0; i < mAudienceTopList.size() ; i++) {
                        if(audienceInfo.userId.equals(mAudienceTopList.get(i))){
                            ivHeadView.setVisibility(View.VISIBLE);
                            ivAudienceBg.setVisibility(View.VISIBLE);
                            //2.判断在观众列席中的位置
                            switch (i){
                                case 0:
                                    ivHeadView.setBackgroundResource(R.drawable.live_audience_top_1);
                                    ivAudienceBg.setBackgroundResource(R.drawable.bg_round_audience_top_1);
                                    break;
                                case 1:
                                    ivHeadView.setBackgroundResource(R.drawable.live_audience_top_2);
                                    ivAudienceBg.setBackgroundResource(R.drawable.bg_round_audience_top_2);
                                    break;
                                case 2:
                                    ivHeadView.setBackgroundResource(R.drawable.live_audience_top_3);
                                    ivAudienceBg.setBackgroundResource(R.drawable.bg_round_audience_top_3);
                                    break;
                                default:
                                    ivHeadView.setVisibility(View.GONE);
                                    ivAudienceBg.setVisibility(View.GONE);
                                    break;
                            }
                        }

                    }
                }else{
                    ivHeadView.setVisibility(View.GONE);
                }
            }
            UserIdentityUtils.showUserIdentity(mContext,audienceInfo.role,userIdentityIcon);
//            //判断角色的定义(VIP、SVIP、年VIP)
//            if(mRoles != null && mRoles.size() > 0){
//                int role = mRoles.get(audienceInfo.userId);
//                if("0".equals(String.valueOf(role)) || "null".equals(String.valueOf(role)) || role <= 0){}else {
//                    UserIdentityUtils.getUserIdentity(mContext,role,userIdentityIcon);
//                }
//            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(audienceInfo);
                }
            });
            
        }

        private void initView(@NonNull final View itemView) {
            mImageAudienceIcon = itemView.findViewById(R.id.iv_audience_head);
            ivHeadView = itemView.findViewById(R.id.iv_head_view);
            ivAudienceBg = itemView.findViewById(R.id.view_audience_bg);
            userIdentityIcon = itemView.findViewById(R.id.item_identity_icon);
        }

        public void display(int index) {
            final TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo = mAudienceList.get(index);
            if (audienceInfo == null) {
                return;
            }
            if (!TextUtils.isEmpty(audienceInfo.avatarUrl)) {
                GlideEngine.loadImage(mImageAudienceIcon, audienceInfo.avatarUrl);
            } else {
                GlideEngine.loadImage(mImageAudienceIcon, R.drawable.live_default_head_img);
            }

            if(mAudienceTopList != null && mAudienceTopList.size() > 0){
                //1.判断是否在打赏前三榜单里边
                if(mAudienceTopList.contains(audienceInfo.userId) ){
                    ivHeadView.setVisibility(View.VISIBLE);
                    ivAudienceBg.setVisibility(View.VISIBLE);
                    //2.判断在观众列席中的位置
                    switch (index){
                        case 0:
                            ivHeadView.setBackgroundResource(R.drawable.live_audience_top_1);
                            ivAudienceBg.setBackgroundResource(R.drawable.bg_round_audience_top_1);
                            break;
                        case 1:
                            ivHeadView.setBackgroundResource(R.drawable.live_audience_top_2);
                            ivAudienceBg.setBackgroundResource(R.drawable.bg_round_audience_top_2);
                            break;
                        case 2:
                            ivHeadView.setBackgroundResource(R.drawable.live_audience_top_3);
                            ivAudienceBg.setBackgroundResource(R.drawable.bg_round_audience_top_3);
                            break;
                        default:
                            ivHeadView.setVisibility(View.GONE);
                            ivAudienceBg.setVisibility(View.GONE);
                            break;
                    }
                }else{
                    ivHeadView.setVisibility(View.GONE);
                }
            }
            UserIdentityUtils.showUserIdentity(mContext,audienceInfo.role,userIdentityIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(audienceInfo);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo);
    }
}