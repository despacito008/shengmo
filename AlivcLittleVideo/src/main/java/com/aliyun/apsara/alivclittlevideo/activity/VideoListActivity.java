package com.aliyun.apsara.alivclittlevideo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.aliyun.apsara.alivclittlevideo.R;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.constants.LittleVideoParamConfig;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.NetWatchdog;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleImageUploadAuthInfo;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleMineVideoInfo;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleVideoUploadAuthInfo;
import com.aliyun.apsara.alivclittlevideo.sts.OnStsResultListener;
import com.aliyun.apsara.alivclittlevideo.sts.StsInfoManager;
import com.aliyun.apsara.alivclittlevideo.sts.StsTokenInfo;
import com.aliyun.apsara.alivclittlevideo.upgrade.AutoUpgradeClient;
import com.aliyun.apsara.alivclittlevideo.utils.Common;
import com.aliyun.apsara.alivclittlevideo.view.AlivcBottomView;
import com.aliyun.apsara.alivclittlevideo.view.MenuDialog;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcMineView;
import com.aliyun.apsara.alivclittlevideo.view.publish.AlivcVideoPublishView;
import com.aliyun.apsara.alivclittlevideo.view.publish.OnAuthInfoExpiredListener;
import com.aliyun.apsara.alivclittlevideo.view.video.AlivcVideoPlayView;
import com.aliyun.apsara.alivclittlevideo.view.video.OnStsInfoExpiredListener;
import com.aliyun.apsara.alivclittlevideo.view.video.OnUploadCompleteListener;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.AlivcVideoListView;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.common.utils.DateTimeUtils;
import com.aliyun.svideo.editor.EditorMediaActivity;
import com.aliyun.svideo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.recorder.bean.AlivcRecordInputParam;
import com.aliyun.svideo.base.AliyunSvideoActionConfig;
import com.aliyun.svideo.common.utils.DensityUtils;
import com.aliyun.svideo.editor.bean.AlivcEditInputParam;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.common.utils.ScreenUtils;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.common.widget.AlivcCustomAlertDialog;
import com.aliyun.svideo.recorder.bean.RenderingMode;
import com.aliyun.svideo.recorder.util.RecordCommon;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.aliyun.apsara.alivclittlevideo.activity.PublishActivity.KEY_PARAM_CONFIG;
import static com.aliyun.apsara.alivclittlevideo.activity.PublishActivity.KEY_PARAM_DESCRIBE;
import static com.aliyun.apsara.alivclittlevideo.activity.PublishActivity.KEY_PARAM_THUMBNAIL;

/**
 * 视频列表
 *
 * @author xlx
 */
public class VideoListActivity extends AppCompatActivity {

    private static final String TAG = "VideoListActivity";
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private AlivcVideoPlayView videoPlayView;
    private FrameLayout mContentView;
    private AlivcVideoPublishView mAlivcVideoPublishView;
    /**
     * 底部导航栏
     */
    private AlivcBottomView mBottomView;
    /**
     * 个人中心页面
     */
    private AlivcMineView mineView;
    /**
     * 跳转编辑、录制选择弹窗
     */
    private MenuDialog mMenuDialog;
    /**
     * 系统授权设置的弹框
     */
    AlertDialog openAppDetDialog;

    /**
     * 视频合成输出文件名称
     */
    private String mComposeFileName = "";
    /**
     * 视频合成输出文件路径
     */
    private String mComposeOutputPath = "";
    /**
     * 视频上传凭证信息
     */
    private LittleVideoUploadAuthInfo mVideoUploadAuthInfo;
    /**
     * 图片上传凭证信息
     */
    private LittleImageUploadAuthInfo mImageUploadAuthInfo;
    /**
     * 视频上传凭证信息获取成功
     */
    private boolean isVideoUploadAuthRequestSuccess = false;
    /**
     * 图片上传凭证信息获取成功
     */
    private boolean isImageUploadAuthRequestSuccess = false;
    /**
     * 权限列表
     */
    String[] permission = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * assets目录文件拷贝工具类
     */
    private Common commonUtils;
    /**
     * 当前请求的视频列表最后数据的主键id，用于查询下一页数据
     */
    private int mLastVideoId = -1;
    /**
     * 数据请求是否为加载更多数据
     */
    private boolean isLoadMoreData = false;
    /**
     * 编辑后的特效配置文件地址，用户合成视频
     */
    private String mConfigPath;
    /**
     * 封面地址
     */
    private String mThumbnailPath;
    /**
     * 视频描述
     */
    private String mVideoDesc;
    /**
     * 判断视频是否正在显示视频列表
     */
    private boolean isHome = true;
    /**
     * 网络状态监听器
     */
    private NetWatchdog netWatchdog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_video_list);
        checkPermission();
        //初始化路径
        initPath();
        // 复制加密证书到sdcard，在播放加密视频的时候需要
        copyAssets();
        //提前拷贝race相关文件，否则会出现美颜无效果的情况
        RecordCommon.copyRace(this);
        // 网络监听
        initNetWatchDog();
        //初始化页面
        initView();
        initLiveView();
        // 注册编辑完成之后跳转的页面
        AliyunSvideoActionConfig.getInstance().registerPublishActivity(PublishActivity.class.getName());
        // 版本升级
        AutoUpgradeClient.checkUpgrade(this, AutoUpgradeClient.LITTLEVIDEO_JSON);
        loadPlayList(mLastVideoId);
    }

    /**
     * 初始化文件路径
     */
    private void initPath(){
        mComposeFileName = System.currentTimeMillis() + "_output_compose_video.mp4";
        mComposeOutputPath = Constants.SDCardConstants.getDir(this) + LittleVideoParamConfig.DIR_COMPOSE + mComposeFileName;
    }

    /**
     * 复制文件
     */
    private void copyAssets() {
        commonUtils = Common.getInstance(getApplicationContext()).copyAssetsToSD("encrypt", "aliyun");
        commonUtils.setFileOperateCallback(

        new Common.FileOperateCallback() {
            @Override
            public void onSuccess() {
                //AliyunDownloadConfig config = new AliyunDownloadConfig();
                //config.setSecretImagePath(
                //    Environment.getExternalStorageDirectory().getAbsolutePath() + "/aliyun/encryptedApp.dat");
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save/");
                if (!file.exists()) {
                    file.mkdir();
                }
            }

            @Override
            public void onFailed(String error) {
                Log.e("Test", "unZip fail..");
            }
        });
    }
    /**
     * 网络监听
     */
    public void initNetWatchDog() {
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetConnectedListener(new MyNetConnectedListener(this));
    }
    /**
     * 初始化View
     */
    protected void initView() {
        mineView = findViewById(R.id.alivc_little_mine);
        mBottomView = findViewById(R.id.bottom_view);
        mBottomView.setOnBottomItemClickListener(new AlivcBottomView.OnBottomItemClickListener() {
            @Override
            public void onHomeItemClick() {
                // home item click
                videoPlayView.onResume();
                videoPlayView.setVisibility(VISIBLE);
                mineView.setVisibility(GONE);
                isHome = true;
                mBottomView.setBackgroundColor(Color.TRANSPARENT);
            }
            @Override
            public void onUserItemClick() {
                // user item click
                videoPlayView.onPause();
                isHome = false;
                videoPlayView.setVisibility(GONE);
                mineView.setVisibility(VISIBLE);
            }

            @Override
            public void onMoreItemClick() {
                // more item click
                if (mMenuDialog == null) {
                    initMenuDialog();
                }
                mMenuDialog.show();

            }
        });
        videoPlayView = findViewById(R.id.video_play);
        videoPlayView.setOnRefreshDataListener(new MyRefreshDataListener(this));
        videoPlayView.setOnStsInfoExpiredListener(new OnStsInfoExpiredListener() {
            @Override
            public void onTimeExpired() {
                //刷新获取STS
                StsInfoManager.getInstance().refreshStsToken(new MyStsResultListener(VideoListActivity.this));
            }

            @Override
            public StsTokenInfo refreshSts() {
                return StsInfoManager.getInstance().refreshStsToken();
            }
        });

        videoPlayView.setOnVideoDeleteListener(new AlivcVideoPlayView.OnVideoDeleteListener() {
            @Override
            public void onDeleteClick(LittleMineVideoInfo.VideoListBean video) {
                LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(VideoListActivity.this);
                if (!userInfo.getUserId().equals(video.getUser().getUserId())) {
//                    ToastUtils.show(VideoListActivity.this, getResources().getString(R.string.alivc_little_can_not_delete_video) );
                    return;
                }
                showDeleteConfirmDialog(video.getVideoId());
            }
        });
        float screenWidth = ScreenUtils.getWidth(this);
        float screenHeight = ScreenUtils.getRealHeight(this);
        if (screenHeight / screenWidth > 2) {
            int viewHeight = (int)(screenHeight - ScreenUtils.getNavigationHeight(this));
            ViewGroup.LayoutParams layoutParams = videoPlayView.getLayoutParams();
            layoutParams.height = viewHeight;
            videoPlayView.setLayoutParams(layoutParams);
        }
        mContentView = findViewById(R.id.fl_content);
    }
    private void initLiveView() {
        ImageView mImageView = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.width = DensityUtils.dip2px(this, 30);
        params.height = DensityUtils.dip2px(this, 30);
        int marginRight = DensityUtils.dip2px(this, 25);
        int marginTop = DensityUtils.dip2px(this, 40);
        params.setMargins(0, marginTop, marginRight, 0);
        params.gravity = Gravity.RIGHT;
        mImageView.setImageResource(R.mipmap.alivc_live_shift_icon);
        videoPlayView.addView(mImageView, params);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoListActivity.this, AlivcLittleLiveActivity.class);
                VideoListActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 显示删除视频确认提示窗
     */
    private void showDeleteConfirmDialog(final String videoId) {
        new AlivcCustomAlertDialog.Builder(this)
        .setNoIcon(true)
        .setMessage(getResources().getString(R.string.alivc_little_main_dialog_content_delete))
        .setDialogClickListener("", "", new AlivcCustomAlertDialog.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                requestDeleteVideo(videoId);
            }

            @Override
            public void onCancel() {

            }
        })
        .create()
        .show();
    }
    /**
     * 请求服务器删除指定视频
     *
     * @param videoId 视频id
     */
    private void requestDeleteVideo(String videoId) {
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(VideoListActivity.this);
        LittleHttpManager.getInstance().requestDeleteVideo(userInfo.getToken(), userInfo.getUserId(), videoId,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleMyVideoListResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleMyVideoListResponse data) {
                if (result) {
                    videoPlayView.removeCurrentPlayVideo();
                    //删除后刷新个人中心页面视频数据列表
                    mineView.refreshVideoList();
                } else {
                    ToastUtils.show(VideoListActivity.this, message );
                }
            }
        });
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        boolean checkResult = PermissionUtils.checkPermissionsGroup(this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPath();
        mThumbnailPath = intent.getStringExtra(KEY_PARAM_THUMBNAIL);
        mVideoDesc = intent.getStringExtra(KEY_PARAM_DESCRIBE);
        mConfigPath = intent.getStringExtra(KEY_PARAM_CONFIG);
        if (!TextUtils.isEmpty(mThumbnailPath)) {
            if (mAlivcVideoPublishView == null) {
                initUploadView();
            }
            AlivcLittleUserManager.getInstance().setAllowChangeUser(false);
        } else {
            return;
        }
        LittleHttpManager.getInstance().requestImageUploadAuth(
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleImageUploadAuthResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleImageUploadAuthResponse data) {
                if (result) {
                    isImageUploadAuthRequestSuccess = true;
                    mImageUploadAuthInfo = data.data;
                    if (isVideoUploadAuthRequestSuccess) {
                        mAlivcVideoPublishView.startUpload(
                            //编辑配置信息相关的json文件路径
                            mConfigPath,
                            //视频合成路径
                            mComposeOutputPath,
                            //视频id
                            mVideoUploadAuthInfo.getVideoId(),
                            //视频上传地址
                            mVideoUploadAuthInfo.getUploadAddress(),
                            //视频上传凭证
                            mVideoUploadAuthInfo.getUploadAuth(),
                            //视频描述
                            mVideoDesc,
                            //封面图片地址
                            mThumbnailPath,
                            //图片id
                            mImageUploadAuthInfo.getImageId(),
                            //图片访问地址
                            mImageUploadAuthInfo.getImageURL(),
                            //图片上传地址
                            mImageUploadAuthInfo.getUploadAddress(),
                            //图片上传凭证
                            mImageUploadAuthInfo.getUploadAuth());

                    }
                } else {
                    //  对失败处理
                    ToastUtil.showToast(VideoListActivity.this, message );
                    AlivcLittleUserManager.getInstance().setAllowChangeUser(true);
                }


            }
        });
        LittleHttpManager.getInstance().requestVideoUploadAuth("video", mComposeFileName,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleVideoUploadAuthResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleVideoUploadAuthResponse data) {
                if (result) {
                    isVideoUploadAuthRequestSuccess = true;
                    mVideoUploadAuthInfo = data.data;
                    if (isImageUploadAuthRequestSuccess) {
                        mAlivcVideoPublishView.startUpload(mConfigPath,
                                                           mComposeOutputPath,
                                                           mVideoUploadAuthInfo.getVideoId(),
                                                           mVideoUploadAuthInfo.getUploadAddress(),
                                                           mVideoUploadAuthInfo.getUploadAuth(),
                                                           mVideoDesc,
                                                           mThumbnailPath,
                                                           mImageUploadAuthInfo.getImageId(),
                                                           mImageUploadAuthInfo.getImageURL(),
                                                           mImageUploadAuthInfo.getUploadAddress(),
                                                           mImageUploadAuthInfo.getUploadAuth());
                    }
                } else {
                    //  对失败处理
                    ToastUtil.showToast(VideoListActivity.this, message );
                    AlivcLittleUserManager.getInstance().setAllowChangeUser(true);
                }

            }
        });

    }

    /**
     * 获取播放列表数据
     *
     * @param id 请求第pageNo页数据
     */
    private void loadPlayList(final int id) {
        Log.d(TAG, "pageNo:" + id);
        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "R.string.alivc_little_no_user:" + R.string.alivc_little_tip_no_user, Toast.LENGTH_SHORT)
            .show();
            return;
        }
        LittleHttpManager.getInstance().requestRecommonVideoList(userInfo.getToken(), 1, AlivcLittleHttpConfig.DEFAULT_PAGE_SIZE, id,
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleMyVideoListResponse>() {
            @Override
            public void onResponse(final boolean result, String message,
                                   final LittleHttpResponse.LittleMyVideoListResponse data) {

                if (result) {
                    List<LittleMineVideoInfo.VideoListBean> videoList = data.data.getVideoList();
                    if (videoList != null && videoList.size() > 0) {
                        int listSize = videoList.size();
                        for (int i = 0; i < listSize; i++) {
                            videoList.get(i).setCensorStatus(LittleMineVideoInfo
                                                             .VideoListBean.STATUS_CENSOR_SUCCESS);
                            if (i == listSize - 1) {
                                mLastVideoId = videoList.get(i).getId();
                            }
                        }
                    }
                    if (!isLoadMoreData) {
                        videoPlayView.refreshVideoList(data.data.getVideoList());
                    } else {
                        videoPlayView.addMoreData(data.data.getVideoList());
                    }
                } else {
                    if (videoPlayView != null) {
                        videoPlayView.loadFailure();
                    }
                    ToastUtils.show(VideoListActivity.this, message );
                }

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了
                //Toast.makeText(this, "get All Permisison", Toast.LENGTH_SHORT).show();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                showPermissionDialog();
            }
        }
    }
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(com.aliyun.apsara.alivclittlevideo.R.string.app_name)
                           + getResources().getString(R.string.alivc_little_dialog_permission_tips));
        builder.setPositiveButton(getResources().getString(R.string.alivc_little_main_dialog_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.alivc_little_main_dialog_not_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        if (null == openAppDetDialog) {
            openAppDetDialog = builder.create();
        }
        if (null != openAppDetDialog && !openAppDetDialog.isShowing()) {
            openAppDetDialog.show();
        }
    }

    private long onBackPressedTime;
    @Override
    public void onBackPressed() {

        long timeSpan = System.currentTimeMillis() - onBackPressedTime;
        onBackPressedTime = System.currentTimeMillis();
        if (timeSpan > 2000) {
            Toast.makeText(this, getResources().getString(R.string.alivc_little_main_tip_exit), Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mineView != null) {
            mineView.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (isHome) {
            videoPlayView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isHome) {
            videoPlayView.onPause();
        }

        if (mineView != null) {
            mineView.onPause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        netWatchdog.startWatch();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMenuDialog != null) {
            mMenuDialog.dismiss();
        }
        netWatchdog.stopWatch();

    }

    @Override
    protected void onDestroy() {
        if (commonUtils != null) {
            commonUtils.onDestroy();
            commonUtils = null;
        }

        if (videoPlayView != null) {
            videoPlayView.onDestroy();
        }

        if (mineView != null) {
            mineView.onDestroy();
        }

        super.onDestroy();
    }
    /**
     * 出事化上传view
     */
    private void initUploadView() {
        mAlivcVideoPublishView = new AlivcVideoPublishView(this);
        //设置上传凭证过期监听
        mAlivcVideoPublishView.setOnAuthInfoExpiredListener(new OnAuthInfoExpiredListener() {
            @Override
            public void onImageAuthInfoExpired() {
                LittleHttpManager.getInstance().requestImageUploadAuth(
                new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleImageUploadAuthResponse>() {
                    @Override
                    public void onResponse(boolean result, String message,
                                           LittleHttpResponse.LittleImageUploadAuthResponse data) {
                        if (result) {
                            if (mAlivcVideoPublishView != null) {
                                mAlivcVideoPublishView.refreshImageUploadAuthInfo(data.data.getImageId(),
                                        data.data.getImageURL(),
                                        data.data.getUploadAddress(),
                                        data.data.getUploadAuth());
                            }
                        }
                    }
                });
            }

            @Override
            public void onVideoAuthInfoExpired(String videoId) {

                LittleHttpManager.getInstance().refreshVideoUploadAuth(videoId,
                new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleVideoUploadAuthResponse>() {
                    @Override
                    public void onResponse(boolean result, String message,
                                           LittleHttpResponse.LittleVideoUploadAuthResponse data) {
                        if (result) {
                            if (mAlivcVideoPublishView != null) {
                                mAlivcVideoPublishView.refreshVideoAuth(data.data.getUploadAddress(),
                                                                        data.data.getUploadAuth());
                            }
                        }
                    }
                });
            }
        });
        //设置上传结果回调监听
        mAlivcVideoPublishView.setOnUploadCompleteListener(new MyUploadCompleteListener(this));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mContentView.addView(mAlivcVideoPublishView, params);
    }
    /**
     * 初始化进入录制、编辑的菜单弹窗
     */
    private void initMenuDialog() {
        mMenuDialog = new MenuDialog(VideoListActivity.this);
        mMenuDialog.setOnMenuItemClickListener(new MenuDialog.OnMenuItemClickListener() {
            @Override
            public void onEditroClick() {
                if (mAlivcVideoPublishView != null && mAlivcVideoPublishView.isPublishing()) {
                    ToastUtils.show(VideoListActivity.this, getResources().getString(R.string.alivc_little_publishing_not_edit));
                    return;
                }
                // 打开视频编辑 ,短视频sdk，暂时只支持api 18以上的版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    jumpToEditor();
                } else {
                    ToastUtils.show(VideoListActivity.this, getResources().getString(R.string.alivc_little_main_not_support_edit) );
                }
            }

            @Override
            public void onRecorderClick() {
                if (mAlivcVideoPublishView != null && mAlivcVideoPublishView.isPublishing()) {
                    ToastUtils.show(VideoListActivity.this, getResources().getString(R.string.alivc_little_publishing_can_not_recorder) );
                    return;
                }
                // 打开视频录制,短视频sdk，暂时只支持api 18以上的版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    jumpToRecorder();
                } else {
                    ToastUtils.show(VideoListActivity.this, getResources().getString(R.string.alivc_little_main_not_support_recorder));
                }

            }
        });
    }
    /**
     * 判断是编辑模块进入还是通过社区模块的编辑功能进入
     */
    private static final String INTENT_PARAM_KEY_ENTRANCE = "entrance";

    /**
     * 跳转到视频编辑 在{@link LittleVideoParamConfig.Editor} 中进行配置
     */
    private void jumpToEditor() {
        boolean checkResult = PermissionUtils.checkPermissionsGroup(this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
            return;
        }
        AlivcEditInputParam param = new AlivcEditInputParam.Builder()
        .setRatio(LittleVideoParamConfig.Editor.VIDEO_RATIO)
        .setScaleMode(LittleVideoParamConfig.Editor.VIDEO_SCALE)
        .setVideoQuality(LittleVideoParamConfig.Editor.VIDEO_QUALITY)
        .setFrameRate(LittleVideoParamConfig.Editor.VIDEO_FRAMERATE)
        .setGop(LittleVideoParamConfig.Editor.VIDEO_GOP)
        .build();
        EditorMediaActivity.startImport(this, param);
    }

    /**
     * 跳转到视频录制界面, 不经过视频参数设置, 参数取默认值
     * 在{@link LittleVideoParamConfig.Recorder}
     * 中进行配置
     */
    private void jumpToRecorder() {
        final AlivcRecordInputParam recordInputParam = new AlivcRecordInputParam.Builder()
        .setResolutionMode(LittleVideoParamConfig.Recorder.RESOLUTION_MODE)
        .setRatioMode(LittleVideoParamConfig.Recorder.RATIO_MODE)
        .setMaxDuration(LittleVideoParamConfig.Recorder.MAX_DURATION)
        .setMinDuration(LittleVideoParamConfig.Recorder.MIN_DURATION)
        .setVideoQuality(LittleVideoParamConfig.Recorder.VIDEO_QUALITY)
        .setGop(LittleVideoParamConfig.Recorder.GOP)
        .setVideoCodec(LittleVideoParamConfig.Recorder.VIDEO_CODEC)
        .setVideoRenderingMode(RenderingMode.Race)
        .build();
        AlivcSvideoRecordActivity.startRecord(this, recordInputParam);
    }
    /**
     * 视频播放列表刷新、加载更多事件监听
     */
    private static class MyRefreshDataListener implements AlivcVideoListView.OnRefreshDataListener {
        WeakReference<VideoListActivity> weakReference;

        MyRefreshDataListener(VideoListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onRefresh() {

            VideoListActivity activity = weakReference.get();
            if (activity != null) {
                activity.isLoadMoreData = false;
                activity.mLastVideoId = -1;
                activity.loadPlayList(activity.mLastVideoId);
            }
        }

        @Override
        public void onLoadMore() {
            VideoListActivity activity = weakReference.get();
            if (activity != null) {
                activity.isLoadMoreData = true;
                activity.loadPlayList(activity.mLastVideoId);
            }
        }
    }

    /**
     * sts刷新监听
     */
    private static class MyStsResultListener implements OnStsResultListener {
        WeakReference<VideoListActivity> weakReference;

        MyStsResultListener(VideoListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(StsTokenInfo tokenInfo) {

            VideoListActivity videoListActivity = weakReference.get();
            // videoListActivity.videoPlayView刷新sts信息
            videoListActivity.videoPlayView.refreshStsInfo(tokenInfo);

        }

        @Override
        public void onFail() {

        }
    }
    /**
     * 网络断开重连监听
     */
    private static class MyNetConnectedListener implements NetWatchdog.NetConnectedListener {
        private WeakReference<VideoListActivity> weakReference;

        MyNetConnectedListener(VideoListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onReNetConnected(boolean isReconnect) {
            if (isReconnect) {
                //刷新获取STS
                StsInfoManager.getInstance().refreshStsToken(new MyStsResultListener(weakReference.get()));
                //网络重连
                Log.e("Test", "onReNetConnected......");
            }
        }

        @Override
        public void onNetUnConnected() {
            VideoListActivity videoListActivity = weakReference.get();
            if(videoListActivity != null){
                ToastUtils.show(videoListActivity,videoListActivity.getString(R.string.alivc_editor_more_no_network));
            }
            //网络断开
            Log.e("Test", "onNetUnConnected......");
        }
    }



    /**
     * 上传结果回调
     */
    private static class MyUploadCompleteListener implements OnUploadCompleteListener {
        WeakReference<VideoListActivity> weakReference;

        MyUploadCompleteListener(VideoListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(final String videoId, final String imageUrl, final String describe) {
            if (weakReference == null) {
                return;
            }
            VideoListActivity activity = weakReference.get();
            LittleHttpManager.getInstance().videoPublish(
                AlivcLittleUserManager.getInstance().getUserInfo(activity).getToken(),
                videoId,
                imageUrl,
                "", describe, "", 0, 0, 0, "",
            new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittlePublishResponse>() {
                @Override
                public void onResponse(boolean result, String message,
                                       LittleHttpResponse.LittlePublishResponse data) {
                    if (weakReference == null) {
                        return;
                    }
                    VideoListActivity activity = weakReference.get();
                    //标识位，恢复初始状态
                    activity.isImageUploadAuthRequestSuccess = false;
                    activity.isVideoUploadAuthRequestSuccess = false;
                    AlivcLittleUserManager.getInstance().setAllowChangeUser(true);

                    if (result) {

                        if (activity == null) {
                            return;
                        }
                        if (activity.mineView != null) {
                            activity.mineView.requestInsertVideo(videoId, imageUrl, describe);
                        }
                        activity.mAlivcVideoPublishView.showSuccess();
                    } else {
                        activity.mAlivcVideoPublishView.showFailed(activity.getResources().getString(R.string.alivc_little_main_tip_publish_error) + message);
                    }
                }
            });
        }
        @Override
        public void onFailure(String code, String msg) {
            if (weakReference == null) {
                return;
            }
            VideoListActivity activity = weakReference.get();
            //标识位，恢复初始状态
            activity.isImageUploadAuthRequestSuccess = false;
            activity.isVideoUploadAuthRequestSuccess = false;
            AlivcLittleUserManager.getInstance().setAllowChangeUser(true);
            if (weakReference == null) {
                return;
            }
            activity.mAlivcVideoPublishView.showFailed(activity.getResources().getString(R.string.alivc_little_main_tip_publish_error) + msg);
        }
    }


}
