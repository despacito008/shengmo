package com.aiwujie.shengmo.timlive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.LiveLogBean;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.timlive.view.RoundAngleImageView;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;


/**
 * 用于展示房间列表的item
 */
public class LiveLogListAdapter extends RecyclerView.Adapter<LiveLogListAdapter.ViewHolder> {

    private Context mContext;
    private List<LiveLogBean.DataBean> mList;
    String type = "";

    public LiveLogListAdapter(Context context, List<LiveLogBean.DataBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_item_live_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
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

        public void bind(int index) {
            LiveLogBean.DataBean roomInfo = mList.get(index);
            mTextRoomName.setText(roomInfo.getNickname());
            switch (roomInfo.getSex()) {
                case "1":
                    mTextRoomName.setTextColor(ContextCompat.getColor(mContext,R.color.liveBoyColor));
                    break;
                case "2":
                    mTextRoomName.setTextColor(ContextCompat.getColor(mContext,R.color.liveGirlColor));
                    break;
                default:
                    mTextRoomName.setTextColor(ContextCompat.getColor(mContext,R.color.liveCdtColor));
                    break;
            }
            if (roomInfo.getNickname().equals(roomInfo.getLive_title())) {
                mTvTitle.setVisibility(View.GONE);
            } else {
                mTvTitle.setVisibility(View.VISIBLE);
                mTvTitle.setText(roomInfo.getLive_title());
            }
            mTextAnchorVLevel.setVisibility(View.GONE);

            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            //UserIdentityUtils.showIdentity(context, roomInfo.getHead_pic(), roomInfo.getUid(), roomInfo.getIs_admin(), roomInfo.getSvipannual(), roomInfo.getSvip(), roomInfo.getVipannual(), roomInfo.getVip(), mTextAnchorVLevel);

            /*if (!TextUtils.isEmpty(roomInfo.getLive_poster()) && !roomInfo.getLive_poster().contains("null")) { //直播封皮
                GlideEngine.loadImage(mImagePic, roomInfo.getLive_poster(), R.drawable.live_room_default_cover, 5);
            } else */
//            if(!TextUtils.isEmpty(roomInfo.getLive_poster()) && !roomInfo.getLive_poster().contains("null")){//用户头像
//                GlideEngine.loadImage(mImagePic, roomInfo.getLive_poster(), R.drawable.live_room_default_cover, 5);
//            } else{//默认bg图
//                GlideEngine.loadImage(mImagePic, R.drawable.live_room_default_cover);
//            }
            Glide.with(mContext).load(roomInfo.getLive_poster()).error(R.drawable.live_room_default_cover).into(mImagePic);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(index);
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

            if (type.equals("red")) {
                tvTicket.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7);
            } else {
                tvTicket.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            }

            if ("1".equals(roomInfo.getIs_free())) {
                groupTicket.setVisibility(View.VISIBLE);
                tvTicket.setText(roomInfo.getLive_beans());
            } else {
                groupTicket.setVisibility(View.GONE);
            }
            String watchSumStr = "浏览" + (TextUtil.isEmpty(roomInfo.getWatch_num())? "0": roomInfo.getWatch_num());
            String beansSumStr = "收入" + (TextUtil.isEmpty(roomInfo.getAll_live_beans())? "0": roomInfo.getAll_live_beans());
            mTextMemberCount.setText(watchSumStr + "/" +beansSumStr);
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
