package com.aliyun.svideo.recorder.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.common.utils.UriUtils;
import com.aliyun.svideo.record.R;
import com.aliyun.svideo.recorder.bean.AlivcRecordInputParam;
import com.aliyun.svideo.recorder.bean.RenderingMode;
import com.aliyun.svideo.recorder.mixrecorder.AlivcRecorderFactory;
import com.aliyun.svideo.recorder.util.RecordCommon;
import com.aliyun.svideo.recorder.util.FixedToastUtils;
import com.aliyun.svideo.recorder.util.NotchScreenUtil;
import com.aliyun.svideo.recorder.util.voice.PhoneStateManger;
import com.aliyun.svideo.recorder.view.AliyunSVideoRecordView;
import com.aliyun.svideo.base.widget.ProgressDialog;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.media.MediaInfo;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 3.10.5 ??????????????????
 */

public class AlivcSvideoMixRecordActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int REQUEST_CODE_PLAY = 2002;
    public static final String KEY_PARAM_VIDEO = "VIDEO_PATH";

    /**
     * ??????????????????
     */
    private int mResolutionMode;
    private int mGop;
    private VideoQuality mVideoQuality = VideoQuality.HD;
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;
    private int mRatioMode = AliyunSnapVideoParam.RATIO_MODE_3_4;
    private RenderingMode renderingMode = RenderingMode.Race;
    private String mVideoPath;
    private AliyunVideoParam mVideoParam;
    private AliyunSVideoRecordView mVideoRecordView;
    private int mFrame;
    private boolean isSvideoRace = false;

    /**
     * ????????????????????????
     * true: ??????, ??????
     * false: ??????
     */
    private boolean isCalling = false;
    /**
     * ????????????????????????????????????
     */
    private boolean isUseMusic = false;
    /**
     * ?????????????????????, ???????????????, ??????????????????????????????
     */
    private static final String INTENT_PARAM_KEY_HAS_MUSIC = "hasRecordMusic";

    /**
     * ???????????????????????????????????????????????????????????????
     */
    private static final String INTENT_PARAM_KEY_IS_MIX = "isMixRecord";
    /**
     * ????????????
     */
    String[] permission = {
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * ????????????????????????????????????????????????????????????????????????
     */
    private static final String INTENT_PARAM_KEY_ENTRANCE = "entrance";
    /**
     * ????????????????????????????????????????????????????????????????????????
     * svideo: ?????????
     * community: ??????
     */
    private String mMixRecordEntrance;

    private Toast phoningToast;
    private PhoneStateManger phoneStateManger;
    private String[] mEffDirs;
    private AsyncTask<Void, Void, Void> initAssetPath;
    private AsyncTask<Void, Void, Void> copyAssetsTask;
    private int mMaxDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        // ??????????????????????????????, ????????????, ??????FullScreen
        if (!NotchScreenUtil.checkNotchScreen(this)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initAssetPath();

        setContentView(R.layout.alivc_recorder_activity_record);
        getData();

        boolean checkResult = PermissionUtils.checkPermissionsGroup(this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(mVideoPath);
            mMaxDuration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            mmr.release();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        mVideoRecordView = findViewById(R.id.alivc_recordView);
        mVideoRecordView.setActivity(this);
        mVideoRecordView.setGop(mGop);
        //???????????????????????????????????????????????????
        mVideoRecordView.setMaxRecordTime(mMaxDuration);
        mVideoRecordView.setMinRecordTime(mMaxDuration);
        mVideoRecordView.setRatioMode(mRatioMode);
        mVideoRecordView.setVideoQuality(mVideoQuality);
        mVideoRecordView.setResolutionMode(mResolutionMode);
        mVideoRecordView.setVideoCodec(mVideoCodec);
        mVideoRecordView.setVideoPath(mVideoPath);
        mVideoRecordView.setRenderingMode(renderingMode);
        mVideoRecordView.setSvideoRace(isSvideoRace);
        //????????????recorder
        mVideoRecordView.setRecorder(AlivcRecorderFactory.createAlivcRecorderFactory(AlivcRecorderFactory.RecorderType.MIX, this));
        if (PermissionUtils.checkPermissionsGroup(this, PermissionUtils.PERMISSION_STORAGE)) {
            //??????????????????????????????copy??????
            copyAssets();
        }
    }

    /**
     * ???????????????????????????
     */
    private void getData() {
        mResolutionMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, AliyunSnapVideoParam.RESOLUTION_540P);
        mRatioMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RATIO, AliyunSnapVideoParam.RATIO_MODE_3_4);
        mGop = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_GOP, 250);
        mVideoQuality = (VideoQuality) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_QUALITY);
        mVideoPath = getIntent().getStringExtra(KEY_PARAM_VIDEO);
        mMixRecordEntrance = getIntent().getStringExtra(INTENT_PARAM_KEY_ENTRANCE);
        isSvideoRace = getIntent().getBooleanExtra(AlivcRecordInputParam.INTENT_KEY_IS_SVIDEO_RACE, false);
        if (mVideoQuality == null) {
            mVideoQuality = VideoQuality.HD;
        }
        mVideoCodec = (VideoCodecs) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_CODEC);
        if (mVideoCodec == null) {
            mVideoCodec = VideoCodecs.H264_HARDWARE;
        }
        renderingMode = (RenderingMode) getIntent().getSerializableExtra(AlivcRecordInputParam.INTENT_KEY_VIDEO_RENDERING_MODE);
        if (renderingMode == null) {
            renderingMode = RenderingMode.FaceUnity;
        }
        /**
         * ??????????????????,??????30
         */
        mFrame = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE, 30);

        VideoDisplayMode cropMode = (VideoDisplayMode) getIntent().getSerializableExtra(AliyunSnapVideoParam.CROP_MODE);
        if (cropMode == null) {
            cropMode = VideoDisplayMode.SCALE;
        }
    }

    private void initAssetPath() {
        initAssetPath = new AssetPathInitTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static class AssetPathInitTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<AlivcSvideoMixRecordActivity> weakReference;

        AssetPathInitTask(AlivcSvideoMixRecordActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AlivcSvideoMixRecordActivity activity = weakReference.get();
            if (activity != null) {
                activity.setAssetPath();
            }
            return null;
        }
    }

    private void setAssetPath() {
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator + RecordCommon.QU_NAME
                      + File.separator;
        File filter = new File(new File(path), "filter");
        String[] list = filter.list();
        if (list == null || list.length == 0) {
            return;
        }
        mEffDirs = new String[list.length + 1];
        mEffDirs[0] = null;
        int length = list.length;
        for (int i = 0; i < length; i++) {
            mEffDirs[i + 1] = filter.getPath() + File.separator + list[i];
        }
    }

    private void copyAssets() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                copyAssetsTask = new CopyAssetsTask(AlivcSvideoMixRecordActivity.this).executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 700);

    }

    public static class CopyAssetsTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<AlivcSvideoMixRecordActivity> weakReference;
        ProgressDialog progressBar;

        CopyAssetsTask(AlivcSvideoMixRecordActivity activity) {

            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlivcSvideoMixRecordActivity activity = weakReference.get();
            if (activity != null && !activity.isFinishing()) {
                progressBar = new ProgressDialog(activity);
                progressBar.setMessage(activity.getString(R.string.alivc_progress_content_text));
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.setCancelable(false);
                progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
                progressBar.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AlivcSvideoMixRecordActivity activity = weakReference.get();
            if (activity != null) {
                RecordCommon.copyAll(activity);
                RecordCommon.copyRace(activity);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlivcSvideoMixRecordActivity activity = weakReference.get();
            if (activity != null && !activity.isFinishing()) {
                progressBar.dismiss();
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                activity.mVideoRecordView.setFaceTrackModePath();
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initPhoneStateManger();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoRecordView.onResume();
        mVideoRecordView.startPreview();
        mVideoRecordView.setBackClickListener(new AliyunSVideoRecordView.OnBackClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });


        mVideoRecordView.setCompleteListener(new AliyunSVideoRecordView.OnFinishListener() {

            @Override
            public void onComplete(final String path, int duration, int ratioMode) {
                // ?????????RACE??????????????????finish
                if (isSvideoRace) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        //??????android Q
                        ThreadUtils.runOnSubThread(new Runnable() {
                            @Override
                            public void run() {
                                UriUtils.saveVideoToMediaStore(AlivcSvideoMixRecordActivity.this, path);
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.show(AlivcSvideoMixRecordActivity.this, "??????????????????");
                                        AlivcSvideoMixRecordActivity.this.finish();
                                    }
                                });
                            }
                        });

                    } else {
                        MediaScannerConnection.scanFile(AlivcSvideoMixRecordActivity.this.getApplicationContext(),
                                new String[] {path},
                                new String[] {"video/mp4"}, null);
                        ToastUtils.show(AlivcSvideoMixRecordActivity.this, "??????????????????");
                        AlivcSvideoMixRecordActivity.this.finish();
                    }

                    return;
                }
                //????????????
                MediaInfo mediaInfo = new MediaInfo();
                mediaInfo.filePath = path;
                mediaInfo.startTime = 0;
                mediaInfo.mimeType = "video";
                mediaInfo.duration = duration;
                ArrayList<MediaInfo> infoList = new ArrayList<>();
                infoList.add(mediaInfo);

                Intent intent = new Intent();
                intent.setClassName(AlivcSvideoMixRecordActivity.this, "com.aliyun.svideo.editor.editor.EditorActivity");
                intent.putExtra("mFrame", mFrame);
                intent.putExtra("isMixRecord", true);
                intent.putExtra("mGop", mGop);
                intent.putExtra("mVideoQuality", mVideoQuality);
                intent.putExtra("mResolutionMode", mResolutionMode);
                intent.putExtra("mVideoCodec", mVideoCodec);
                intent.putExtra("canReplaceMusic", isUseMusic);
                intent.putExtra("hasWaterMark", true);
                intent.putParcelableArrayListExtra("mediaInfos", infoList);
                AlivcSvideoMixRecordActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        mVideoRecordView.onPause();
        mVideoRecordView.stopPreview();
        super.onPause();
        if (phoningToast != null) {
            phoningToast.cancel();
            phoningToast = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (phoneStateManger != null) {
            phoneStateManger.setOnPhoneStateChangeListener(null);
            phoneStateManger.unRegistPhoneStateListener();
            phoneStateManger = null;
        }

        if (mVideoRecordView != null) {
            mVideoRecordView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        mVideoRecordView.destroyRecorder();
        super.onDestroy();
        if (copyAssetsTask != null) {
            copyAssetsTask.cancel(true);
            copyAssetsTask = null;
        }

        if (initAssetPath != null) {
            initAssetPath.cancel(true);
            initAssetPath = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PLAY) {
            if (resultCode == Activity.RESULT_OK) {
                mVideoRecordView.deleteAllPart();
                finish();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isCalling) {
            phoningToast = FixedToastUtils.show(this, getResources().getString(R.string.alivc_recorder_record_tip_phone_state_calling));
        }
    }

    private void initPhoneStateManger() {
        if (phoneStateManger == null) {
            phoneStateManger = new PhoneStateManger(this);
            phoneStateManger.registPhoneStateListener();
            phoneStateManger.setOnPhoneStateChangeListener(new PhoneStateManger.OnPhoneStateChangeListener() {

                @Override
                public void stateIdel() {
                    // ??????
                    // mVideoRecordView.setRecordMute(false);
                    isCalling = false;
                }

                @Override
                public void stateOff() {
                    // ??????
                    //   mVideoRecordView.setRecordMute(true);
                    isCalling = true;
                }

                @Override
                public void stateRinging() {
                    // ??????
                    //  mVideoRecordView.setRecordMute(true);
                    isCalling = true;
                }
            });
        }

    }

    /**
     * ????????????
     */
    public static void startMixRecord(Context context, AliyunSnapVideoParam param, String videoPath, RenderingMode renderingMode, boolean isSvideoRace) {

        Intent intent = new Intent(context, AlivcSvideoMixRecordActivity.class);
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, param.getResolutionMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO, param.getRatioMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP, param.getGop());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_CODEC, param.getVideoCodec());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE, param.getFrameRate());
        intent.putExtra(AliyunSnapVideoParam.CROP_MODE, param.getScaleMode());
        intent.putExtra(AliyunSnapVideoParam.MAX_DURATION, param.getMaxDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_DURATION, param.getMinDuration());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY, param.getVideoQuality());
        intent.putExtra(KEY_PARAM_VIDEO, videoPath);
        intent.putExtra(AlivcRecordInputParam.INTENT_KEY_VIDEO_RENDERING_MODE, renderingMode);
        intent.putExtra(AlivcRecordInputParam.INTENT_KEY_IS_SVIDEO_RACE, isSvideoRace);
        context.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // ?????????????????????????????????????????????
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (!isAllGranted) {
                // ?????????????????????????????????
                showPermissionDialog();

            }
        }
    }

    //???????????????????????????
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.app_name) + getString(R.string.alivc_record_request_permission_content_text))
        .setPositiveButton(R.string.alivc_record_request_permission_positive_btn_text, new DialogInterface.OnClickListener() {
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
        })
        .setNegativeButton(R.string.alivc_record_request_permission_negative_btn_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        })
        .setCancelable(false)
        .create()
        .show();

    }

}
