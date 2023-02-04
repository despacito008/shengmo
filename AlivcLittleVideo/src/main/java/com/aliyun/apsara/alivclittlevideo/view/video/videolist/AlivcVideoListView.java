package com.aliyun.apsara.alivclittlevideo.view.video.videolist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.constants.LittleVideoParamConfig;
import com.aliyun.player.AliListPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorCode;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.nativeclass.MediaInfo;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.nativeclass.TrackInfo;
import com.aliyun.player.source.StsInfo;
import com.aliyun.player.source.UrlSource;
import com.aliyun.player.source.VidSts;
import com.aliyun.svideo.common.utils.ToastUtils;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 视频列表view
 * @author xlx
 */
public class AlivcVideoListView extends FrameLayout {
    private static String TAG = AlivcVideoListView.class.getSimpleName();
    private Context mContext;
    private RecyclerViewEmptySupport recycler;
    private SwipeRefreshLayout refreshView;
    private BaseVideoListAdapter adapter;
    private PagerLayoutManager pagerLayoutManager;

    private View mPlayerViewContainer;
    /**
     * 数据是否到达最后一页
     */
    private boolean isEnd;
    /**
     * 播放器的封装，可以提前准备视频
     */
    private AliListPlayer mListPlayer;
    private TextureView mTextureView;
    private List<IVideoSourceModel> list;

    /**
     * 刷新数据listener
     */
    private OnRefreshDataListener onRefreshDataListener;
    /**
     * 判断是否处于加载数据的状态中
     */
    private boolean isLoadingData;
    /**
     * 预加载位置, 默认离底部还有5条数据时请求下一页视频列表
     */
    private static final int DEFAULT_PRELOAD_NUMBER = 5;
    /**
     * 是否点击暂停
     */
    private boolean isPause = false;
    /**
     * 当前页面是否处于可见状态
     */
    private boolean isOnBackground = true;
    /**
     * 当前选中位置
     */
    private int mCurrentPosition;
    /**
     * 正常滑动，上一个被暂停的位置
     */
    private int mLastStopPosition = -1;
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    private ImageView mPlayIcon;

    public AlivcVideoListView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        initPlayer();
        init();
    }
    private AlivcVideoListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        throw new IllegalArgumentException("this view isn't allow create by xml");
    }
    private GestureDetector gestureDetector;
    /**
     * 初始化播放器相关
     */
    private void initPlayer() {
        mPlayerViewContainer = View.inflate(getContext(), R.layout.layout_player_view, null );
        mTextureView = mPlayerViewContainer.findViewById(R.id.video_textureview);
        gestureDetector = new GestureDetector(mContext,
        new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //判断当前view是否可见，防止退后台、切换页面和单击同时操作导致，退后台时视频又重新开始播放
                if (AlivcVideoListView.this.isShown()) {
                    onPauseClick();
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
        mPlayerViewContainer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return  gestureDetector.onTouchEvent(event);
            }
        });
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Surface mSurface = new Surface(surface);
                if (mListPlayer != null) {
                    mListPlayer.setSurface(mSurface);
                    mListPlayer.redraw();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                if (mListPlayer != null) {
                    mListPlayer.redraw();
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if(mListPlayer != null){
                    mListPlayer.setSurface(null);
                }
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        mListPlayer = AliPlayerFactory.createAliListPlayer(mContext);
        //打开播放器
        mListPlayer.enableLog(LittleVideoParamConfig.Player.LOG_ENABLE);
        PlayerConfig config = mListPlayer.getConfig();
        config.mClearFrameWhenStop = true;
        mListPlayer.setConfig(config);
        mListPlayer.enableLog(true);
        mListPlayer.setLoop(true);
        mListPlayer.setAutoPlay(false);
        //mListPlayer.set
        //指定播放清晰度
        mListPlayer.setDefinition(VideoQuality.PLAY.getValue());
        mListPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                MediaInfo mediaInfo = mListPlayer.getMediaInfo();
                List<TrackInfo> trackInfos = mediaInfo.getTrackInfos();
                int size = trackInfos.size();
                for (TrackInfo trackInfo : trackInfos) {
                    if (trackInfo.getVodDefinition().equals(VideoQuality.PLAY.getValue())) {
                        float aspectRatio = (float)trackInfo.getVideoWidth() / trackInfo.getVideoHeight();
                        if (aspectRatio < 9f / 15f) {
                            mListPlayer.setScaleMode(IPlayer.ScaleMode.SCALE_ASPECT_FILL);
                        } else {
                            mListPlayer.setScaleMode(IPlayer.ScaleMode.SCALE_ASPECT_FIT);
                        }
                        break;
                    }
                }
                if (!isPause && !isOnBackground) {
                    mListPlayer.start();
                }
            }
        });
        mListPlayer.setOnRenderingStartListener(new IPlayer.OnRenderingStartListener() {
            @Override
            public void onRenderingStart() {
                //  2018/11/21 隐藏封面
                BaseVideoListAdapter.BaseHolder holder = (BaseVideoListAdapter.BaseHolder) recycler.findViewHolderForLayoutPosition(mCurrentPosition);
                if (holder != null) {
                    holder.getCoverView().setVisibility(GONE);
                }
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadingEnd();
                }
            }
        });

        mListPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadingBegin();
                }
            }

            @Override
            public void onLoadingEnd() {
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadingEnd();
                }
            }

            @Override
            public void onLoadingProgress(int percent,
                                          float netSpeed) {
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadingProgress(percent, netSpeed);
                }
            }
        });
        mListPlayer.setOnErrorListener(new IPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {
                if (errorInfo.getCode() == ErrorCode.ERROR_SERVER_POP_TOKEN_EXPIRED) {
                    //鉴权过期
                    if (mTimeExpiredErrorListener != null) {
                        mTimeExpiredErrorListener.onTimeExpiredError();
                        Log.i(TAG, "刷新鉴权");
                    }
                }
                ToastUtils.show(getContext(), errorInfo.getMsg() );
            }
        });

    }

    /**
     * 鉴权过期时发生
     */
    private OnTimeExpiredErrorListener mTimeExpiredErrorListener;

    public void setTimeExpiredErrorListener(
        OnTimeExpiredErrorListener mTimeExpiredErrorListener) {
        this.mTimeExpiredErrorListener = mTimeExpiredErrorListener;
    }

    private void init() {
        list = new ArrayList<>();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_list, this, true);
        recycler = view.findViewById(R.id.recycler);
        refreshView = view.findViewById(R.id.refresh_view);

        refreshView.setColorSchemeColors(Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshDataListener != null) {
                    isLoadingData = true;
                    onRefreshDataListener.onRefresh();
                }
            }
        });
        recycler.setHasFixedSize(true);
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
        mRecycledViewPool.setMaxRecycledViews(0,50);
        recycler.setRecycledViewPool(mRecycledViewPool);
        pagerLayoutManager = new PagerLayoutManager(mContext);
        pagerLayoutManager.setItemPrefetchEnabled(true);
        recycler.setLayoutManager(pagerLayoutManager);
        recycler.setEmptyView(view.findViewById(R.id.ll_empty_view));
        pagerLayoutManager.setOnViewPagerListener(new PagerLayoutManager.OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                Log.e(TAG, "onInitComplete mCurrentPosition= " + mCurrentPosition);
                int position = pagerLayoutManager.findFirstVisibleItemPosition();
                if (position != -1) {
                    mCurrentPosition = position;
                }
                int itemCount = adapter.getItemCount();
                if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !isLoadingData && !isEnd) {
                    // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
                    isLoadingData = true;
                    loadMore();
                }
                startPlay(mCurrentPosition);
                mLastStopPosition = -1;
                Log.e(TAG, "onInitComplete mCurrentPosition= " + mCurrentPosition);

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {

                if (mCurrentPosition == position) {
                    mLastStopPosition = position;
                    stopPlay();
                    BaseVideoListAdapter.BaseHolder holder = (BaseVideoListAdapter.BaseHolder) recycler.findViewHolderForLayoutPosition(position);
                    if (holder != null) {
                        holder.getCoverView().setVisibility(VISIBLE);
                        if(holder.getPlayIcon() != null){
                            holder.getPlayIcon().setVisibility(View.GONE);
                        }
                    }
                }

            }

            @Override
            public void onPageSelected(int position, boolean b) {
                //重新选中视频不播放，如果该位置被stop过则会重新播放视频
                if (mCurrentPosition == position && mLastStopPosition != position) {
                    return;
                }

                int itemCount = adapter.getItemCount();
                if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !isLoadingData && !isEnd) {
                    // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
                    isLoadingData = true;
                    loadMore();
                }
                if (itemCount == position + 1 && isEnd) {
                    Toast.makeText(getContext(), R.string.alivc_little_play_tip_last_video, Toast.LENGTH_SHORT).show();
                }
                //开始播放选中视频
                startPlay(position);
                mCurrentPosition = position;
            }
        });

    }
    /**
     * 停止视频播放
     */
    private void stopPlay() {
        ViewParent parent = mPlayerViewContainer.getParent();
        if (parent != null && parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(mPlayerViewContainer);
        }
        mListPlayer.stop();
        mListPlayer.setSurface(null);
    }
    /**
     * 开始播放
     * @param position 播放位置
     */
    private void startPlay(int position) {
        if (position < 0 || position > list.size()) {
            return;
        }
        IVideoSourceModel video = list.get(position);
        //恢复界面状态
        isPause = false;
        BaseVideoListAdapter.BaseHolder holder = (BaseVideoListAdapter.BaseHolder) recycler.findViewHolderForLayoutPosition(position);
        mPlayIcon = holder.getPlayIcon();
        mPlayIcon.setVisibility(View.GONE);
        ViewParent parent = mPlayerViewContainer.getParent();
        if (parent != null && parent instanceof FrameLayout) {
            ((ViewGroup) parent).removeView(mPlayerViewContainer);
        }

        if (holder != null) {
            holder.getContainerView().addView(mPlayerViewContainer, 0);
        }
        //防止退出后台之后，再次调用start方法，导致视频播放
        if (video.getSourceType() == VideoSourceType.TYPE_STS) {
            VidSts vidSts = video.getVidStsSource();
            StsInfo stsInfo = new StsInfo();
            stsInfo.setAccessKeyId(vidSts.getAccessKeyId());
            stsInfo.setAccessKeySecret(vidSts.getAccessKeySecret());
            stsInfo.setSecurityToken(vidSts.getSecurityToken());
            stsInfo.setRegion(vidSts.getRegion());
            if (position - mCurrentPosition == 1) {
                mListPlayer.moveToNext(stsInfo);
            } else if (position - mCurrentPosition == -1) {
                mListPlayer.moveToPrev(stsInfo);
            } else {
                mListPlayer.moveTo(video.getUUID(), stsInfo);
            }
        } else if (video.getSourceType() == VideoSourceType.TYPE_URL) {
            if (position - mCurrentPosition == 1) {
                mListPlayer.moveToNext();
            } else if (position - mCurrentPosition == -1) {
                mListPlayer.moveToPrev();
            } else {
                mListPlayer.moveTo(video.getUUID());
            }
        } else if (video.getSourceType() == VideoSourceType.TYPE_LIVE) {
            UrlSource urlSource = video.getUrlSource();
            mListPlayer.setDataSource(urlSource);
            mListPlayer.prepare();
        }
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        if (onRefreshDataListener != null) {
            onRefreshDataListener.onLoadMore();
        }
    }

    /**
     * 刷新数据
     * @param list 刷新数据
     */
    public void refreshData (List<IVideoSourceModel> list) {
        clearNotShowVideo(list);
        if (refreshView != null && refreshView.isRefreshing()) {
            refreshView.setRefreshing(false);
            // 加载完毕, 重置加载状态
        }
        if (mListPlayer != null) {
            mListPlayer.clear();
            for (IVideoSourceModel iVideoSourceModel : list) {
                if (iVideoSourceModel.getSourceType() == VideoSourceType.TYPE_STS) {
                    mListPlayer.addVid(iVideoSourceModel.getVidStsSource().getVid(), iVideoSourceModel.getUUID() );
                } else if (iVideoSourceModel.getSourceType() == VideoSourceType.TYPE_URL) {
                    String url = iVideoSourceModel.getUrlSource().getUri();
                    String uuid = iVideoSourceModel.getUUID();
                    mListPlayer.addUrl(url, uuid );
                }
            }
        }
        isEnd = false;
        isLoadingData = false;
        adapter.refreshData(list);
    }

    /**
     * 刷新数据，并播放指定位置的视频
     * @param list 视频列表数据
     * @param position 刷新完成之后播放位置
     */
    public void refreshData(List<IVideoSourceModel> list, int position) {
        int size = list.size();
        if (position < 0) {
            position = 0;
        }
        if (size <= position) {
            position = size - 1;
        }
        //获取不进行显示
        int notShowVideoCount = 0;
        for (int i = 0; i < size; i++) {
            if (i < position && list.get(i).getSourceType() == VideoSourceType.TYPE_ERROR_NOT_SHOW) {
                notShowVideoCount++;
            }
        }
        mCurrentPosition  = position - notShowVideoCount;
        refreshData(list);
        recycler.scrollToPosition(mCurrentPosition);

    }

    /**
     * 清除不允许显示的视频
     * @param list
     */
    private void clearNotShowVideo(List<IVideoSourceModel> list) {
        Iterator<IVideoSourceModel> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getSourceType() == VideoSourceType.TYPE_ERROR_NOT_SHOW) {
                it.remove();
            }
        }
    }
    /**
     * 加载更多数据
     * @param list
     */
    public void addMoreData(List<IVideoSourceModel> list) {
        if (list == null || list.size() < AlivcLittleHttpConfig.DEFAULT_PAGE_SIZE) {
            isEnd = true;
        } else {
            isEnd = false;
        }
        clearNotShowVideo(list);
        isLoadingData = false;

        if (adapter != null) {
            adapter.addMoreData(list);
        }
        if (mListPlayer != null) {
            for (IVideoSourceModel iVideoSourceModel : list) {
                if (iVideoSourceModel.getSourceType() == VideoSourceType.TYPE_STS) {
                    mListPlayer.addVid(iVideoSourceModel.getVidStsSource().getVid(), iVideoSourceModel.getUUID() );
                } else if (iVideoSourceModel.getSourceType() == VideoSourceType.TYPE_URL) {
                    mListPlayer.addUrl(iVideoSourceModel.getUrlSource().getUri(), iVideoSourceModel.getUUID());
                }
            }
        }
        if (refreshView.isRefreshing()) {
            refreshView.setRefreshing(false);
        }

    }

    private IPlayer.OnLoadingStatusListener mLoadingListener;

    public void setLoadingListener(IPlayer.OnLoadingStatusListener mLoadingListener) {
        this.mLoadingListener = mLoadingListener;
    }

    public void setOnRefreshDataListener(OnRefreshDataListener onRefreshDataListener) {
        this.onRefreshDataListener = onRefreshDataListener;
    }

    /**
     * 刷新数据
     */
    public interface OnRefreshDataListener {
        /**
         * 下拉刷新
         */
        void onRefresh();

        /**
         * 上拉加载
         */
        void onLoadMore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mListPlayer != null){
            mListPlayer.release();
        }
        if(mRecycledViewPool != null){
            mRecycledViewPool.clear();
            mRecycledViewPool = null;
        }
    }

    /**
     * 暂停播放
     */
    private void pausePlay() {
        isPause = true;
        if(mPlayIcon != null){
            mPlayIcon.setVisibility(View.VISIBLE);
        }
        mListPlayer.pause();

    }

    /**
     * 恢复播放
     */
    private void resumePlay() {
        isPause = false;
        if(mPlayIcon != null){
            mPlayIcon.setVisibility(View.GONE);
        }

        mListPlayer.start();
    }

    /**
     * activity不可见或者播放页面不可见时调用该方法
     */
    public void setOnBackground(boolean isOnBackground) {
        this.isOnBackground = isOnBackground;
        if (isOnBackground) {
            pausePlay();
        } else {
            resumePlay();
        }
    }
    /**
     * 视频暂停/恢复的时候使用，
     */
    public void onPauseClick() {
        if (isPause) {
            resumePlay();
        } else {
            pausePlay();
        }
    }
    /**
     * 设置播放器数量
     * @param playerCount
     */
    public void setPlayerCount(int playerCount) {

        mListPlayer.setPreloadCount(playerCount);
    }

    /**
     * 设置adapter
     * @param adapter
     */
    public void setAdapter(BaseVideoListAdapter adapter) {
        this.adapter = adapter;
        recycler.setAdapter(adapter);
        this.list = adapter.getDataList();
    }
    /**
     * 移除当前播放的视频
     */
    public void removeCurrentPlayVideo() {
        stopPlay();
        int tempCurrentPostion = mCurrentPosition;
        //当前视频如果为最后一个视频，则需要播放上一个视频
        if (tempCurrentPostion == list.size() - 1 && list.size() >= 2) {
            recycler.scrollToPosition(tempCurrentPostion - 1);
        }
        list.remove(tempCurrentPostion);
        adapter.notifyDataSetChanged();

    }
    /**
     * 视频列表获取失败
     */
    public void loadFailure() {
        if (refreshView.isRefreshing()) {
            refreshView.setRefreshing(false);
        }
    }

    /**
     * 获取当前播放的视频唯一标志
     */
    public String getCurrentUid(){
        String mCurrentUid = "";
        if(mListPlayer != null){
            mCurrentUid = mListPlayer.getCurrentUid();
        }
        return mCurrentUid;
    }

    /**
     * 移动到指定的视频
     */
    public void moveTo(String uid, StsInfo stsInfo){
        if(mListPlayer != null){
            mListPlayer.moveTo(uid,stsInfo);
        }
    }
}
