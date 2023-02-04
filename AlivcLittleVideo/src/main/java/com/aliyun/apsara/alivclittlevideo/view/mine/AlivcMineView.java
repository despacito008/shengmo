package com.aliyun.apsara.alivclittlevideo.view.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.activity.AlivcLittlePlayerActivity;
import com.aliyun.apsara.alivclittlevideo.activity.AlivcLittleSettingActivity;
import com.aliyun.apsara.alivclittlevideo.activity.AlivcLittleUserSettingActivity;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleServerApiConstants;
import com.aliyun.apsara.alivclittlevideo.constants.IntentExtraKey;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.common.utils.image.ImageLoaderImpl;
import com.aliyun.svideo.common.utils.image.ImageLoaderOptions;
import com.aliyun.svideo.common.widget.AlivcCustomAlertDialog;
import com.aliyun.apsara.alivclittlevideo.view.video.BaseVideoSourceModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心
 */
public class AlivcMineView extends FrameLayout {
    private static final String TAG = "AlivcMineView";
    /**
     * 更新用户信息, 包括昵称, id, 头像等 request code
     */
    private static final int REQUEST_CHANGE_USER_CHANGE = 3001;
    /**
     * 修改昵称 result code
     */
    private static final int RESULT_CHANGE_NICK_NAME = 1002;
    /**
     * 更新用户信息, 包括昵称, id, 头像等 result code
     */
    private static final int RESULT_CHANGE_USER_INFO = 1003;
    /**
     * 进入播放列表，当有视频删除的时候需要
     */
    private static final int REQUEST_PLAY_VIDEO_LIST = 1004;

    /**
     * 审核状态 -- 审核中
     */
    private static final String CENSOR_STATUS_ONCENSOR = "onCensor";

    /**
     * 审核状态 -- 审核成功
     */
    private static final String CENSOR_STATUS_SUCCESS = "success";

    /**
     * 审核状态 -- 审核失败
     */
    private static final String CENSOR_STATUS_FAIL = "fail";
    /**
     * 审核状态 -- 待审核
     */
    private static final String CENSOR_STATUS_WAIT = "check";

    /**
     * 修改完昵称后,用于刷新用户信息
     */
    private static final String REFRESH_USER_INFO = "refresh";

    private TitleNestedScrollView titleNestedScroll;
    private RecyclerView recyclerView;
    private ImageView mUserAvatar;
    private TextView mUserId, mUserNickName, mVideoCount;
    private RelativeLayout rlUserNickNameClick;
    private LittleUserInfo mUserInfo;
    private MineVideoAdapter videoAdapter;
    private ArrayList<LittleMineVideoInfo.VideoListBean> videoListData = new ArrayList<>();
    private AlivcCustomAlertDialog unPassVideoDialog;
    private int mLastVideoId;
    private LittleMineVideoInfo mVideoInfo;
    private SwipeRefreshLayout swipeRefresh;
    private boolean isLoading;

    public AlivcMineView(@NonNull Context context) {
        this(context, null);
    }

    public AlivcMineView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setPadding(0, 50, 0, 0);
        LayoutInflater.from(context).inflate(R.layout.layout_mine_view, this, true);
        initView();
        initUserInfo();
        initRecycler();
        rootViewSetting();
        if (!isLoading) {
            loadMyVideoList(-1, false);
        }

        viewListener();
    }

    private void initView() {
        titleNestedScroll = findViewById(R.id.nsv_scroll);
        mUserAvatar = findViewById(R.id.iv_little_user_center_avatar);
        mUserId = findViewById(R.id.tv_little_mine_id);
        mUserNickName = findViewById(R.id.tv_little_nickname);
        mVideoCount = findViewById(R.id.tv_my_video_count);
        rlUserNickNameClick = findViewById(R.id.rl_little_userinfo);
        swipeRefresh = findViewById(R.id.alivc_swip_refresh);

        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
    }

    private void loadMyVideoList(int lastVideoId, final boolean isLoadMore) {
        isLoading = true;
        if (mUserInfo == null) {
            return;
        }

        LittleHttpManager.getInstance().requestMineVideoList(mUserInfo.getToken(), 1,
                AlivcLittleHttpConfig.DEFAULT_PAGE_SIZE, lastVideoId,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleMyVideoListResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleMyVideoListResponse data) {
                if (result) {
                    if (data != null) {
                        mVideoInfo = data.data;
                        List<LittleMineVideoInfo.VideoListBean> videoList = mVideoInfo.getVideoList();
                        if (videoList != null && videoList.size() > 0) {
                            mLastVideoId = videoList.get(videoList.size() - 1).getId();
                        }

                        if (mVideoInfo != null) {
                            notifyVideoList(mVideoInfo, isLoadMore);
                        }
                    }
                }
                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
                isLoading = false;
            }
        });
    }

    private void notifyVideoList(LittleMineVideoInfo videoInfo, boolean isLoadMore) {
        String videoCount = String.valueOf(videoInfo.getTotal());
        mVideoCount.setText(TextUtils.isEmpty(videoCount) ? "N/A" : videoCount);

        if (videoAdapter != null) {
            List<LittleMineVideoInfo.VideoListBean> videoList = videoInfo.getVideoList();

            if (videoList != null) {
                BaseVideoSourceModel.UserBean userBean = new BaseVideoSourceModel.UserBean(
                    mUserInfo.getUserId(), mUserInfo.getNickName(), mUserInfo.getAvatarUrl());
                int size = videoList.size();
                for (int i = 0; i < size; i++) {
                    videoList.get(i).setUser(userBean);
                }
            }
            if (!isLoadMore) {
                videoListData.clear();
                videoListData.addAll(videoList);
                videoAdapter.refreshData(videoList);
            } else {
                videoListData.addAll(videoList);
                videoAdapter.setVideoData(videoList);
            }
        }
    }

    public void initUserInfo() {
        mUserInfo = AlivcLittleUserManager.getInstance().getUserInfo(getContext());

        new ImageLoaderImpl().loadImage(getContext(), mUserInfo.getAvatarUrl(), new ImageLoaderOptions.Builder()
                                        .circle()//加载圆形
                                        .centerCrop()
                                        .build())
        .into(mUserAvatar);

        mUserId.setText(String.format("ID: %s", mUserInfo.getUserId()));
        mUserNickName.setText(mUserInfo.getNickName());
    }

    private void viewListener() {
        // 设置
        findViewById(R.id.fl_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSetting();
            }
        });

        // 用户头像
        mUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToUserSetting();
            }
        });

        // 视频列表item点击
        videoAdapter.setOnMineVideoItemClickListener(new MineVideoAdapter.OnMineVideoItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (videoListData != null) {
                    if (videoListData.size() > 0 && position >= 0 && position < videoListData.size()) {
                        LittleMineVideoInfo.VideoListBean videoListBean = videoListData.get(position);
                        if (videoListBean == null) {
                            return;
                        }
                        String censorStatus = videoListBean.getCensorStatus();
                        if (CENSOR_STATUS_SUCCESS.equals(censorStatus)) {
                            jumpToPlayVideo(videoListData, position);
                        } else if (CENSOR_STATUS_ONCENSOR.equals(censorStatus) || CENSOR_STATUS_WAIT.equals(censorStatus)) {
                            // 审核中不支持播放
                            showInPassVideoTip(videoListBean.getVideoId());
                        } else {
                            //审核不通过不支持播放
                            showUnPassVideoTip(videoListBean.getVideoId());
                        }
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    loadMyVideoList(mLastVideoId = -1, false);
                }

            }
        });
    }

    /**
     * 未通过审核的dialog
     *
     * @param videoId 视频id
     */
    private void showUnPassVideoTip(final String videoId) {
        if (unPassVideoDialog == null) {
            unPassVideoDialog = new AlivcCustomAlertDialog.Builder(getContext())
            .setMessage(getResources().getString(R.string.alivc_little_mine_dialog_audit_fail))
            .setDialogClickListener(getResources().getString(R.string.alivc_little_mine_dialog_confirm), getResources().getString(R.string.alivc_little_mine_dialog_cancel), new AlivcCustomAlertDialog.OnDialogClickListener() {
                @Override
                public void onConfirm() {
                    requestDeleteVideo(videoId);
                }

                @Override
                public void onCancel() {
                }
            })
            .create();
        }
        unPassVideoDialog.show();
    }

    /**
     * 审核中的dialog
     *
     * @param videoId 视频id
     */
    private void showInPassVideoTip(final String videoId) {
        new AlivcCustomAlertDialog.Builder(getContext())
        .setMessage(getResources().getString(R.string.alivc_little_mine_not_support_play))
        .setCustomDialogType(AlivcCustomAlertDialog.CustomDialogType.Confirm)
        .create()
        .show();
    }

    /**
     * request App Server delete this unPass video
     *
     * @param videoId videoId
     */
    private void requestDeleteVideo(final String videoId) {
        if (mUserInfo == null) {
            return;
        }
        LittleHttpManager.getInstance().requestDeleteVideo(mUserInfo.getToken(), mUserInfo.getUserId(), videoId,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleMyVideoListResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleMyVideoListResponse data) {
                if (result) {
                    videoAdapter.requestRemoveData(videoId);
                } else {
                    ToastUtils.show(getContext(), message);
                }
            }
        });
    }

    private void rootViewSetting() {
        final View rootView = ((Activity)getContext()).getWindow().findViewById(android.R.id.content);
        View headerTitle = findViewById(R.id.reycler_header_title);

        titleNestedScroll.setMyScrollHeight((int)headerTitle.getY());
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View headerTitle = findViewById(R.id.reycler_header_title);
                int statuBarHeight = getStatuBarHeight();
                statuBarHeight = statuBarHeight != 0 ? statuBarHeight : 50;
                titleNestedScroll.setMyScrollHeight((int)headerTitle.getY() - statuBarHeight);
                int rvNewHeight = rootView.getHeight() - headerTitle.getHeight() - statuBarHeight;

                recyclerView.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rvNewHeight));
            }
        });
    }

    private int getStatuBarHeight() {
        int height = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.id_nested_recycler);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(manager);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        videoAdapter = new MineVideoAdapter(getContext());
        recyclerView.setAdapter(videoAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollY = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                GridLayoutManager lm = (GridLayoutManager)recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (scrollY >= 0) {
                    // 上拉加载
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItemPosition > totalItemCount - 9
                            && visibleItemCount > 0) {
                        if (!isLoading) {
                            loadMyVideoList(mLastVideoId, true);
                        }

                    }
                } else {
                    // 下拉
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                this.scrollY = dy;
            }
        });
    }

    /**
     * 视频详情播放
     *
     * @param videoListBean VideoListBean
     */
    private void jumpToPlayVideo(ArrayList<LittleMineVideoInfo.VideoListBean> videoListBean, int position) {
        Intent intent = new Intent(getContext(), AlivcLittlePlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(IntentExtraKey.KEY_VIDEO_DETAIL_POSITION, position);
        bundle.putInt(IntentExtraKey.KEY_PAGE_INDEX, mLastVideoId);
        bundle.putParcelableArrayList(IntentExtraKey.KEY_VIDEO_DETAIL_DATA_LIST, videoListBean);
        intent.putExtra(IntentExtraKey.KEY_VIDEO_DETAIL_DATA, bundle);

        ((Activity)getContext()).startActivityForResult(intent, REQUEST_PLAY_VIDEO_LIST);
    }

    private void jumpToSetting() {
        Intent intent = new Intent(getContext(), AlivcLittleSettingActivity.class);
        getContext().startActivity(intent);
    }

    private void jumpToUserSetting() {
        Intent intent = new Intent(getContext(), AlivcLittleUserSettingActivity.class);
        ((Activity)getContext()).startActivityForResult(intent, REQUEST_CHANGE_USER_CHANGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CHANGE_USER_CHANGE) {
            if (resultCode == RESULT_CHANGE_USER_INFO) {
                initUserInfo();
                //更换用户后重新刷新数据
                refreshVideoList();
            } else if (resultCode == RESULT_CHANGE_NICK_NAME) {
                initUserInfo();
            }
        } else if (requestCode == REQUEST_PLAY_VIDEO_LIST) {
            if (resultCode == Activity.RESULT_OK) {
                //删除、刷新视频后刷新
                refreshVideoList();
            }
        }
    }

    /**
     * 重新刷新列表数据
     */
    public void refreshVideoList() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }
        if (!isLoading) {
            loadMyVideoList(mLastVideoId = -1, false);
        }
    }
    public void onPause() {
        if (unPassVideoDialog != null && unPassVideoDialog.isShowing()) {
            unPassVideoDialog.dismiss();
        }
    }

    public void onDestroy() {
        if (unPassVideoDialog != null) {
            unPassVideoDialog.dismiss();
            unPassVideoDialog = null;
        }

        LittleHttpManager instance = LittleHttpManager.getInstance();
        if (instance != null) {
            instance.cancelRequest(AlivcLittleServerApiConstants.URL_DELETE_VIDEO);
            instance.cancelRequest(AlivcLittleServerApiConstants.URL_GET_PERSIONAL_VIDEO_LIST);
        }
    }

    public void requestInsertVideo(String videoId, String imageUrl, String describe) {
        //改为直接请求网络数据，解决第一次添加视频的时候，会出现两个封面的问题
        if (!isLoading) {
            loadMyVideoList(mLastVideoId = -1, false);
        }
        //LittleMineVideoInfo.VideoListBean videoListBean = new LittleMineVideoInfo.VideoListBean();
        //videoListBean.setVideoId(videoId);
        //videoListBean.setCensorStatus(CENSOR_STATUS_ONCENSOR);
        //videoListBean.setCoverUrl(imageUrl);
        //videoListBean.setDescription(describe);
        //videoListData.add(0, videoListBean);
        //videoAdapter.requestInsertData(videoListBean);
        //if (mVideoInfo != null) {
        //    int total = mVideoInfo.getTotal();
        //    String s = String.valueOf(++total);
        //    mVideoInfo.setTotal(total);
        //    mVideoCount.setText(TextUtils.isEmpty(s) ? "N/A" : s);
        //}

    }
}
