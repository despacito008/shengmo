package com.aiwujie.shengmo.timlive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
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
import com.aiwujie.shengmo.timlive.view.RoundAngleImageView;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * 用于展示房间列表的item
 */
public class RoomListAdapter2 extends RecyclerView.Adapter<RoomListAdapter2.ViewHolder> {

    private Context mContext;
    private List<ScenesRoomInfoBean> mList;
    private OnItemClickListener onItemClickListener;

    String type = "";

    public RoomListAdapter2(Context context, List<ScenesRoomInfoBean> list,
                            OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.onItemClickListener = onItemClickListener;
    }

    public RoomListAdapter2(Context context, List<ScenesRoomInfoBean> list,
                            OnItemClickListener onItemClickListener, String type) {
        this.mContext = context;
        this.mList = list;
        this.onItemClickListener = onItemClickListener;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_item_live_room_normal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mContext, mList.get(position), onItemClickListener);
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
        }

        public void bind(Context context, final ScenesRoomInfoBean roomInfo, final OnItemClickListener listener) {
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

            if (TextUtils.isEmpty(roomInfo.getWatchsum())) {
                mTextMemberCount.setVisibility(View.GONE);
            } else {
                mTextMemberCount.setVisibility(View.VISIBLE);
                mTextMemberCount.setText(roomInfo.getWatchsum() + "");
            }

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

            if ("0".equals(roomInfo.getIs_live())) {
                mViewMask.setVisibility(View.VISIBLE);
                mTvLiveEnd.setVisibility(View.VISIBLE);
                mTvTitle.setVisibility(View.GONE);
            } else {
                mViewMask.setVisibility(View.GONE);
                mTvLiveEnd.setVisibility(View.GONE);
            }

            if ("1".equals(roomInfo.getIs_interaction())) {
                mIvInteract.setVisibility(View.VISIBLE);
            } else {
                mIvInteract.setVisibility(View.GONE);
            }

            if ("1".equals(roomInfo.getCamera_switch())) {
                mIvAudio.setVisibility(View.VISIBLE);
            } else {
                mIvAudio.setVisibility(View.GONE);
            }

            if ("2".equals(roomInfo.getAnchor_status()) || "3".equals(roomInfo.getAnchor_status())) {
                mIvContract.setVisibility(View.VISIBLE);
            } else {
                mIvContract.setVisibility(View.GONE);
            }

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

            if ("1".equals(roomInfo.getIs_pwd())) {
                mIvPassword.setVisibility(View.VISIBLE);
            } else {
                mIvPassword.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ScenesRoomInfoBean roomInfo);
    }
}
