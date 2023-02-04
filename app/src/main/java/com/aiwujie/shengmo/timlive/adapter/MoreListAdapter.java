package com.aiwujie.shengmo.timlive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import java.util.List;


/**
 * 用于展示房间列表的item
 */
public class MoreListAdapter extends RecyclerView.Adapter<MoreListAdapter.ViewHolder> {

    private Context mContext;
    private List<ScenesRoomInfoBean> mList;
    private OnItemClickListener onItemClickListener;

    public MoreListAdapter(Context context, List<ScenesRoomInfoBean> list,
                           OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_more_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mContext, mList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImagePic;
        private TextView mTextRoomName;
        private TextView mTextAnchorName;
        private TextView mTextMemberCount;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(@NonNull final View itemView) {
            mTextRoomName = itemView.findViewById(R.id.live_tv_live_room_name);
            mTextAnchorName = itemView.findViewById(R.id.live_tv_live_anchor_name);
            mTextMemberCount = itemView.findViewById(R.id.live_tv_live_member_count);
            mImagePic = itemView.findViewById(R.id.live_iv_live_room_pic);
        }

        public void bind(Context context, final ScenesRoomInfoBean roomInfo, final OnItemClickListener listener) {
            if (TextUtils.isEmpty(roomInfo.getLive_title())) {
                mTextRoomName.setVisibility(View.GONE);
            } else {
                mTextRoomName.setVisibility(View.VISIBLE);
                mTextRoomName.setText(roomInfo.getNickname());
            }
            if (TextUtils.isEmpty(roomInfo.getNickname())) {
                mTextAnchorName.setVisibility(View.GONE);
            } else {
                mTextAnchorName.setVisibility(View.VISIBLE);
                mTextAnchorName.setText(roomInfo.getNickname());
            }
            mTextMemberCount.setText(roomInfo.getWatchsum() + MyApp.instance().getString(R.string.online));
            if (!TextUtils.isEmpty(roomInfo.getHead_pic())) {
                GlideEngine.loadImage(mImagePic, roomInfo.getHead_pic(), 0, 10);
            } else {
                GlideEngine.loadImage(mImagePic, R.drawable.live_room_default_cover);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ClickUtils.isFastClick(v.getId())) {
                        listener.onItemClick(getLayoutPosition(), roomInfo);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ScenesRoomInfoBean roomInfo);
    }
}
