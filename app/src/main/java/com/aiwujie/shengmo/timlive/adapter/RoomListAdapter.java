package com.aiwujie.shengmo.timlive.adapter;

import android.content.Context;
import android.icu.util.UniversalTimeScale;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.kt.adapter.LiveRoomSignAdapter;
import com.aiwujie.shengmo.timlive.view.RoundAngleImageView;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.call.Position;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于展示房间列表的item
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private Context mContext;
    private List<ScenesRoomInfoBean> mList;
    private OnItemClickListener onItemClickListener;

    String type = "";

    public RoomListAdapter(Context context, List<ScenesRoomInfoBean> list,
                           OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.onItemClickListener = onItemClickListener;
    }

    public RoomListAdapter(Context context, List<ScenesRoomInfoBean> list,
                           OnItemClickListener onItemClickListener, String type) {
        this.mContext = context;
        this.mList = list;
        this.onItemClickListener = onItemClickListener;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mContext,position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundAngleImageView mImagePic;
        private TextView mTextRoomName;
        private ImageView mTextAnchorVLevel;
        private TextView mTextRoomType;
        //private RelativeLayout mLiveRoomItem;
        private TextView mTextMemberCount;
        private TextView mTvLiveEnd;
        private ImageView mIvInteract;
        private ImageView mIvAudio;
        private ImageView mIvContract;
        private View mViewMask;
        private TextView mTvTitle;
        private Group groupTicket;
        private TextView tvTicket;
        private View viewPkSign;
        private ImageView mIvPassword;
        private RecyclerView mRvSign;
        private TextView mTvPlayBack;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(@NonNull final View itemView) {
            mTextRoomName = itemView.findViewById(R.id.live_tv_live_room_name);
            mTextAnchorVLevel = itemView.findViewById(R.id.live_tv_live_anchor_v_level);
            mTextMemberCount = itemView.findViewById(R.id.live_tv_room_member_count);
            mTextRoomType = itemView.findViewById(R.id.live_tv_live_type);
            mImagePic = itemView.findViewById(R.id.live_iv_live_room_pic);
            mViewMask = itemView.findViewById(R.id.view_item_live_user_mask);
            mTvLiveEnd = itemView.findViewById(R.id.tv_item_live_user_end);
            mIvInteract = itemView.findViewById(R.id.iv_item_live_interact);
            mIvAudio = itemView.findViewById(R.id.iv_item_live_audio);
            mIvContract = itemView.findViewById(R.id.iv_item_live_contract);
            mTvTitle = itemView.findViewById(R.id.live_tv_live_room_live_title);
            groupTicket = itemView.findViewById(R.id.group_item_live_ticket);
            tvTicket = itemView.findViewById(R.id.tv_item_live_ticket_count);
            viewPkSign = itemView.findViewById(R.id.view_item_live_pk);
            mIvPassword = itemView.findViewById(R.id.iv_item_live_password);
            mRvSign = itemView.findViewById(R.id.rv_item_room_sign);
            mTvPlayBack = itemView.findViewById(R.id.tv_item_room_play_back);
        }

        public void bind(Context context, int position, final OnItemClickListener listener) {
            final ScenesRoomInfoBean roomInfo = mList.get(position);

            if (roomInfo == null) {
                return;
            }
            mTextRoomName.setText(roomInfo.getNickname());
            switch (roomInfo.getSex()) {
                case "1":
                    mTextRoomName.setTextColor(context.getResources().getColor(R.color.liveBoyColor));
                    break;
                case "2":
                    mTextRoomName.setTextColor(context.getResources().getColor(R.color.liveGirlColor));
                    break;
                default:
                    mTextRoomName.setTextColor(context.getResources().getColor(R.color.liveCdtColor));
                    break;
            }
            if (roomInfo.getNickname().equals(roomInfo.getLive_title())) {
                mTvTitle.setVisibility(View.GONE);
            } else {
                mTvTitle.setVisibility(View.VISIBLE);
                mTvTitle.setText(roomInfo.getLive_title());
            }

            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, roomInfo.getHead_pic(), roomInfo.getUid(), roomInfo.getIs_admin(), roomInfo.getSvipannual(), roomInfo.getSvip(), roomInfo.getVipannual(), roomInfo.getVip(), mTextAnchorVLevel);

            /*if (!TextUtils.isEmpty(roomInfo.getLive_poster()) && !roomInfo.getLive_poster().contains("null")) { //直播封皮
                GlideEngine.loadImage(mImagePic, roomInfo.getLive_poster(), R.drawable.live_room_default_cover, 5);
            } else */
//            if(!TextUtils.isEmpty(roomInfo.getLive_poster()) && !roomInfo.getLive_poster().contains("null")){//用户头像
//                GlideEngine.loadImage(mImagePic, roomInfo.getLive_poster(), R.drawable.live_room_default_cover, 5);
//            } else{//默认bg图
//                GlideEngine.loadImage(mImagePic, R.drawable.live_room_default_cover);
//            }
            Glide.with(context).load(roomInfo.getLive_poster()).error(R.drawable.live_room_default_cover).into(mImagePic);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ClickUtils.isFastClick(v.getId())) {
                        listener.onItemClick(getLayoutPosition(), roomInfo);
                    }
                }
            });

//            if ("0".equals(roomInfo.getIs_live())) {
//                mViewMask.setVisibility(View.VISIBLE);
//                mTvLiveEnd.setVisibility(View.VISIBLE);
//                mTvTitle.setVisibility(View.GONE);
//            } else {
//                mViewMask.setVisibility(View.GONE);
//                mTvLiveEnd.setVisibility(View.GONE);
//            }




//            if ("1".equals(roomInfo.getIs_interaction())) {
//                mIvInteract.setVisibility(View.VISIBLE);
//            } else {
//                mIvInteract.setVisibility(View.GONE);
//            }



            if (type.equals("red")) {
                tvTicket.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7);
            } else {
                tvTicket.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            }

            if ("1".equals(roomInfo.getIs_ticket())) {
                groupTicket.setVisibility(View.VISIBLE);
                tvTicket.setText(roomInfo.getTicket_beans());
            } else {
                groupTicket.setVisibility(View.GONE);
            }

            if ("1".equals(roomInfo.getIs_pk())) {
                viewPkSign.setVisibility(View.VISIBLE);
            } else {
                viewPkSign.setVisibility(View.GONE);
            }

//            if ("1".equals(roomInfo.getIs_pwd())) {
//                mIvPassword.setVisibility(View.VISIBLE);
//            } else {
//                mIvPassword.setVisibility(View.GONE);
//            }
//
//            if ("1".equals(roomInfo.getScreen_switch())) {
//                mIvInteract.setVisibility(View.VISIBLE);
//            } else {
//                mIvInteract.setVisibility(View.GONE);
//            }
//
//            if ("1".equals(roomInfo.getCamera_switch())) {
//                mIvAudio.setVisibility(View.VISIBLE);
//            } else {
//                mIvAudio.setVisibility(View.GONE);
//            }
//
//            if ("2".equals(roomInfo.getAnchor_status()) || "3".equals(roomInfo.getAnchor_status())) {
//                mIvContract.setVisibility(View.VISIBLE);
//            } else {
//                mIvContract.setVisibility(View.GONE);
//            }

//            List<String> signList = new ArrayList<>();
//            for (int i = 0; i <= position; i++) {
//                signList.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi-1.lanrentuku.com%2F2020%2F11%2F5%2Ffdcf7d25-b846-464c-9df1-86a9169d71e5.png%3FimageView2%2F2%2Fw%2F500&refer=http%3A%2F%2Fi-1.lanrentuku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1653643961&t=2541f63952ad8e8aa7fd7673e88f448b");
//            }

            if (TextUtils.isEmpty(roomInfo.getWatchsum())) {
                mTextMemberCount.setVisibility(View.GONE);
            } else {
                mTextMemberCount.setVisibility(View.VISIBLE);
                mTextMemberCount.setText(roomInfo.getWatchsum() + "");
            }

            //回放 还是 直播
            if (TextUtil.isEmpty(roomInfo.getLive_log_id()) || "0".equals(roomInfo.getLive_log_id())) {
                mTvPlayBack.setVisibility(View.GONE);
                mRvSign.setVisibility(View.VISIBLE);
                if (roomInfo.getIcon_list() != null && roomInfo.getIcon_list().size() > 0) {
                    mRvSign.setVisibility(View.VISIBLE);
                    LiveRoomSignAdapter signAdapter = new LiveRoomSignAdapter(context, roomInfo.getIcon_list());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRvSign.setLayoutManager(layoutManager);
                    mRvSign.setAdapter(signAdapter);
                    mRvSign.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                } else {
                    mRvSign.setVisibility(View.GONE);
                }
            } else {
                mTvPlayBack.setVisibility(View.VISIBLE);
                mRvSign.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ScenesRoomInfoBean roomInfo);
    }
}
