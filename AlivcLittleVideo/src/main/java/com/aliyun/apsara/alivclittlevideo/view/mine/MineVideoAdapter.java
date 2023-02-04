package com.aliyun.apsara.alivclittlevideo.view.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo;
import com.aliyun.svideo.common.utils.image.ImageLoaderImpl;
import com.aliyun.svideo.common.utils.image.ImageLoaderOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的 界面视频列表adapter
 *
 * @author xlx
 */
public class MineVideoAdapter extends RecyclerView.Adapter<MineVideoAdapter.MyVideoContentHolder> {
    private Context context;

    private List<LittleMineVideoInfo.VideoListBean> mDatas = new ArrayList<>();
    private OnMineVideoItemClickListener onMineVideoItemClickListener;
    private final ImageLoaderImpl imageLoader;
    private final ImageLoaderOptions imageLoaderOptions;

    public MineVideoAdapter(Context context) {
        this.context = context;
        imageLoader = new ImageLoaderImpl();
        imageLoaderOptions = new ImageLoaderOptions.Builder()
        .asBitmap()
        .placeholder(android.R.color.black)
        .centerCrop()
        .build();
    }

    @Override
    public MyVideoContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyVideoContentHolder(
                   LayoutInflater.from(parent.getContext()).inflate(R.layout.alivc_little_video_item_myvideo, parent,
                           false));
    }

    private static final String CENSOR_STATUS_ONCENSOR = "onCensor";
    private static final String CENSOR_STATUS_SUCCESS = "success";
    private static final String CENSOR_STATUS_FAIL = "fail";
    private static final String CENSOR_STATUS_WAIT = "check";

    @Override
    public void onBindViewHolder(final MyVideoContentHolder holder, int position) {
        LittleMineVideoInfo.VideoListBean videoListBean = mDatas.get(position);
        if (videoListBean != null) {
            String coverUrl = videoListBean.getCoverUrl();
            imageLoader.loadImage(context, coverUrl, imageLoaderOptions).into(holder.mVideoCover);

            String censorStatus = videoListBean.getCensorStatus();
            if (CENSOR_STATUS_ONCENSOR.equals(censorStatus) || CENSOR_STATUS_WAIT.equals(censorStatus)) {
                holder.mCensorLable.setText(context.getResources().getString(R.string.alivc_little_mine_reviewing));
                holder.mCensorLable.setBackgroundResource(R.drawable.little_mine_video_item_orange_shape);
            } else if (CENSOR_STATUS_SUCCESS.equals(censorStatus)) {
                holder.mCensorLable.setText(context.getResources().getString(R.string.alivc_little_mine_passed));
                holder.mCensorLable.setBackgroundResource(R.drawable.little_mine_video_item_black_shape);
            } else if (CENSOR_STATUS_FAIL.equals(censorStatus)) {
                holder.mCensorLable.setText(context.getResources().getString(R.string.alivc_little_mine_failed));
                holder.mCensorLable.setBackgroundResource(R.drawable.little_mine_video_item_red_shape);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void setVideoData(List<LittleMineVideoInfo.VideoListBean> videoList) {
        mDatas.addAll(videoList);
        notifyItemRangeInserted(mDatas.size() - videoList.size(), videoList.size());
    }

    /**
     * 刷新数据
     * @param list
     */
    public void refreshData(List<LittleMineVideoInfo.VideoListBean>  list) {
        this.mDatas.clear();
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void requestInsertData(LittleMineVideoInfo.VideoListBean videoListBean) {
        mDatas.add(0, videoListBean);
        notifyDataSetChanged();
    }
    public void requestRemoveData(String vid) {
        if (TextUtils.isEmpty(vid)) {
            return;
        }
        int position = -1;
        int dataSize = mDatas.size();
        for (int i = 0; i < dataSize; i++) {
            if (vid.equals(mDatas.get(i).getVideoId())) {
                position = i;
            }
        }
        if (position >= 0 && position < dataSize) {
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mDatas.size());
        }
    }
    public class MyVideoContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mVideoCover;
        private TextView mCensorLable;

        MyVideoContentHolder(View itemView) {
            super(itemView);
            mVideoCover = itemView.findViewById(R.id.iv_content);
            mCensorLable = itemView.findViewById(R.id.tv_censor_lable);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onMineVideoItemClickListener != null) {
                onMineVideoItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnMineVideoItemClickListener(OnMineVideoItemClickListener listener) {
        this.onMineVideoItemClickListener = listener;
    }

    public interface OnMineVideoItemClickListener {
        /**
         * 点击的item
         *
         * @param position 点击的索引
         */
        void onItemClick(int position);
    }

}
