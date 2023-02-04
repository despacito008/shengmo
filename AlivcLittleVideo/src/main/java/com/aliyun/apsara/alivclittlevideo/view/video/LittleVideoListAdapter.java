package com.aliyun.apsara.alivclittlevideo.view.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleLiveVideoInfo;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.BaseVideoListAdapter;

/**
 * 视频列表的adapter
 * @author xlx
 */
public class LittleVideoListAdapter extends BaseVideoListAdapter<LittleVideoListAdapter.MyHolder,BaseVideoSourceModel> {
    public static final String TAG = LittleVideoListAdapter.class.getSimpleName();
    private OnItemBtnClick mItemBtnClick;
    public LittleVideoListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public LittleVideoListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_pager, viewGroup, false);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
        super.onBindViewHolder(myHolder, position);
        myHolder.mIvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemBtnClick!=null){
                    mItemBtnClick.onDownloadClick(position);
                }
            }
        });
        myHolder.mVideoInfoView.setVideoInfo(list.get(position));
        BaseVideoSourceModel video = list.get(position);
        if (video instanceof LittleMineVideoInfo.VideoListBean){
            if ("success".equals(((LittleMineVideoInfo.VideoListBean)video).getNarrowTranscodeStatus())){
                myHolder.mIvNarrow.setVisibility(View.VISIBLE);
            }else {
                myHolder.mIvNarrow.setVisibility(View.GONE);
            }
            myHolder.mIvLive.setVisibility(View.GONE);
        }else if (video instanceof LittleLiveVideoInfo.LiveListBean){
            myHolder.mIvNarrow.setVisibility(View.GONE);
            myHolder.mIvDownload.setVisibility(View.GONE);
            myHolder.mIvLive.setVisibility(View.VISIBLE);
        }

    }

    public final class  MyHolder extends BaseVideoListAdapter.BaseHolder {
        private ImageView thumb;
        public FrameLayout playerView;
        private ImageView mIvDownload;
        private ViewGroup mRootView;
        private VideoInfoView mVideoInfoView;
        private ImageView mIvNarrow;
        private ImageView mIvLive;
        private ImageView mPlayIconImageView;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG,"new PlayerManager");
            thumb = itemView.findViewById(R.id.img_thumb);
            playerView = itemView.findViewById(R.id.player_view);
            mIvDownload = itemView.findViewById(R.id.iv_download);
            mRootView = itemView.findViewById(R.id.root_view);
            mVideoInfoView = itemView.findViewById(R.id.content_view);
            mIvNarrow = itemView.findViewById(R.id.iv_narrow);
            mIvLive = itemView.findViewById(R.id.iv_live);
            mPlayIconImageView = itemView.findViewById(R.id.iv_play_icon);
        }

        @Override
        public ImageView getCoverView() {
            return thumb;
        }

        @Override
        public ViewGroup getContainerView() {
            return mRootView;
        }

        @Override
        public ImageView getPlayIcon(){
            return mPlayIconImageView;
        }

    }
    public interface OnItemBtnClick{
        void onDownloadClick(int position);
    }

    public void setItemBtnClick(
        OnItemBtnClick mItemBtnClick) {
        this.mItemBtnClick = mItemBtnClick;
    }


}
