package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.eventbus.DynamicUpMediaEditDataEvent;
import com.aiwujie.shengmo.eventbus.RecorderEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recorder.AudioRecoderUtils;
import com.aiwujie.shengmo.recorder.PopupWindowFactory;
import com.aiwujie.shengmo.recorder.TimeUtils;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class RecorderActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, OnItemClickListener {
    @BindView(R.id.mRecorder_state)
    TextView mRecorderState;
    private AudioRecoderUtils mAudioRecoderUtils;
    @BindView(R.id.mRecorder_return)
    ImageView mRecorderReturn;
    @BindView(R.id.mRecorder_bofang)
    ImageView mRecorderBofang;
    @BindView(R.id.mRecorder_luyin)
    ImageView mRecorderLuyin;
    @BindView(R.id.mRecorder_shanchu)
    ImageView mRecorderShanchu;
    private ImageView mImageView;
    private TextView mTextView;
    private MediaPlayer player;
    private android.app.AlertDialog updialog;
    private String media;
    private PopupWindowFactory mPop;
    int time=10;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private String duration;
    private String isvip;
    private MyHandler handler = new MyHandler(this);


    static class MyHandler extends Handler {
        WeakReference<RecorderActivity> activityWeakReference;

        public MyHandler(RecorderActivity recorderActivity) {
            activityWeakReference = new WeakReference<>(recorderActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecorderActivity recorderActivity = activityWeakReference.get();
            switch (msg.what){
                case 0:
                    if (recorderActivity.time<1){
                        recorderActivity.mAudioRecoderUtils.stopRecord();        //????????????????????????????????????
                        if (recorderActivity.isvip.equals("1")){
                            ToastUtil.show(recorderActivity,"????????????30???...");
                        }else {
                            ToastUtil.show(recorderActivity,"????????????10???...");
                        }

//                        mAudioRecoderUtils.cancelRecord();    //???????????????????????????????????????
                        recorderActivity.mPop.dismiss();
                    }else{
                        recorderActivity.time--;
                        recorderActivity.handler.sendEmptyMessageDelayed(0,1000);
                    }
                    break;
                case 1:
                    recorderActivity.addMedia();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        isSVIP();
        getPermission();
    }
    private void setData() {
        //PopupWindow???????????????
        final PercentRelativeLayout rl = (PercentRelativeLayout) findViewById(R.id.rl);
        final View view = View.inflate(this, R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(this, view);
        //PopupWindow???????????????????????????
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

        mAudioRecoderUtils = new AudioRecoderUtils();
        //????????????
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //?????????....db??????????????????time???????????????
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //???????????????filePath???????????????
            @Override
            public void onStop(String filePath,long time) {
//                Toast.makeText(RecorderActivity.this, "??????????????????" + filePath, Toast.LENGTH_SHORT).show();
//                Toast.makeText(RecorderActivity.this, "????????????",0).show();
                int seconds = (int) (time /1000f);
                duration = String.valueOf(seconds);
                mTextView.setText(TimeUtils.long2String(0));
            }


        });
        //Button???touch??????
        mRecorderLuyin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mPop.showAtLocation(rl, Gravity.CENTER,0,0);
                        mRecorderState.setText("????????????"+time+"???");
                        mAudioRecoderUtils.startRecord();
                        handler.sendEmptyMessage(0);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("recorderActivity", "onTouch: "+time);
                        if (time==10){
                            if(time>=8){
                                mAudioRecoderUtils.cancelRecord();    //???????????????????????????????????????
                                ToastUtil.show(getApplicationContext(),"??????????????????,???????????????...");
                            }else {
                                mAudioRecoderUtils.stopRecord();        //????????????????????????????????????
                                ToastUtil.show(getApplicationContext(),"????????????");
                            }
                        }else {
                            if(time>=28){
                                mAudioRecoderUtils.cancelRecord();    //???????????????????????????????????????
                                ToastUtil.show(getApplicationContext(),"??????????????????,???????????????...");
                            }else {
                                mAudioRecoderUtils.stopRecord();        //????????????????????????????????????
                                ToastUtil.show(getApplicationContext(),"????????????");
                            }
                        }
                        if (isvip.equals("1")){
                            time=30;
                        }else {
                            time=10;
                        }
                        handler.removeMessages(0);
                        mPop.dismiss();
                        mRecorderState.setText("????????????");
                        break;
                }
                return true;
            }
        });
    }

        //??????url????????????????????????????????????
    public void getDurationLong(String url){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //?????????????????????
//            if(type == NETWORK){
//            retriever.setDataSource("http://hao.shengmo.org:888/"+url,new HashMap<String, String>());
            retriever.setDataSource(NetPic()+url,new HashMap<String, String>());
//            }else if(type == LOCAL){//?????????????????????
//                retriever.setDataSource(url);
//            }
            duration = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration = String.valueOf((int)Math.floor(Double.valueOf(duration)/1000));
        } catch (Exception ex) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {

            }
        }
        Log.i("getdurationlong", "getDurationLong: "+duration);
        handler.sendEmptyMessage(1);
    }

    void getDurationByUrl(String url) {

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int size = mp.getDuration();
                duration = size / 1000 + "";
            }
        });
        try {
            mediaPlayer.setDataSource(NetPic() + url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(1);
    }

    private void getPermission(){
        String[] perms = { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//???????????????????????????
            //????????????????????????????????????????????????????????????
            //???????????????????????????
            //????????????????????????????????????
            EasyPermissions.requestPermissions(this, "??????????????????????????????:\n\n1.????????????\n\n2.????????????????????????", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        }
    }
    @OnClick({R.id.mRecorder_return, R.id.mRecorder_bofang, R.id.mRecorder_shanchu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRecorder_return:
                dialog();
//                finish();
                break;
            case R.id.mRecorder_bofang:
                ToastUtil.showLong(getApplicationContext(),"????????????...");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if(player==null) {
                            try {
                                player = new MediaPlayer();
                                player.reset();
                                player.setDataSource(getRawFilePath());
                                player.prepare();
                                player.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                break;
            case R.id.mRecorder_shanchu:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("?????????????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isdelete=deleteFile(getRawFilePath());
                        if(isdelete){
                            mediaDel();
                        }else{
                            ToastUtil.show(getApplicationContext(),"??????????????????????????????????????????");
                        }

                    }
                }).create().show();
                break;
        }
    }

    private void mediaDel() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",MyApp.uid);
        IRequestManager manager=RequestFactory.getRequestManager();
        manager.post(HttpUrl.MediaDel, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("mediadelete", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    ToastUtil.show(getApplicationContext(),"????????????");
                                    EventBus.getDefault().post(new RecorderEvent(1));
                                    finish();
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void addMedia() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("media",media);
        map.put("mediaalong",duration);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.MediaEdit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("recordertime", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    updialog.dismiss();
                                    EventBus.getDefault().post(new DynamicUpMediaEditDataEvent(1));
                                    finish();
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void showdialog() {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        ProgressBar bar=new ProgressBar(this);
        builder.setTitle("???????????????,????????????,?????????...")
                .setCancelable(false)
                .setView(bar);
        updialog = builder.create();
        updialog.show();
    }


    private void uploadTextFile() {
        String filePath = getRawFilePath();
        File file = new File(filePath);
        if (file.exists()) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30,TimeUnit.SECONDS).build();
            //MediaType ???????????????
            final MediaType mediaType = MediaType.parse("application/octet-stream");
            //????????????????????????File??????RequestBody???
            RequestBody fileBody = RequestBody.create(mediaType, file);
            //???fileBody?????????MultipartBody
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", "xiaomo.wav", fileBody)
                    .build();
            //Request????????????
            Request request = new Request.Builder().post(requestBody).url(NetPic()+"Api/Api/MediaUpload").addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(),"url_token","")).build();//http://59.110.28.150:888/
            Call call = okHttpClient.newCall(request);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //????????????
                    ResponseBody responseBody = null;
                    try {
                        //?????????????????? ResponseBody
                        responseBody = response.body();
                        //???????????????
                        final String info = responseBody.string();
                            JSONObject obj=new JSONObject(info);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    media=obj.getString("data");
                                   // getDurationLong(media);
                                   // getDurationByUrl(media);
                                    handler.sendEmptyMessage(1);
                                    break;
                                case 4000:
                                    ToastUtil.show(getApplicationContext(),"???????????????????????????");
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        updialog.dismiss();
                    } catch (JSONException e){
                        e.printStackTrace();
                    } finally {//??????????????????
                        if (null != responseBody) {
                            responseBody.close();
                        }
                    }
                }
            });
        }
    }

    /**
     * ??????????????????
     * @param   filePath    ???????????????????????????
     * @return ????????????????????????true???????????????false
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
    public static String getRawFilePath(){
        try {
            File directory=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!directory.exists())
            {
                directory.mkdirs();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        Log.i("lvzhiweilog", "getRawFilePath: "+ Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmRecord/xiaomo.wav");
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmRecord/xiaomo.wav";
    }

    public void dialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("???????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(fileIsExists()) {
                    showdialog();
                    uploadTextFile();
                }else{
                    finish();
                }
            }
        }).create().show();
    }
    public boolean fileIsExists(){
        try{
            File f=new File(getRawFilePath());
            if(!f.exists()){
                return false;
            }

        }catch (Exception e) {
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        AlertView alertView=new AlertView("????????????", "?????????????????????????????????????????????????????????????????????????????????????????????...", null, new String[]{"??????"}, null, this, AlertView.Style.Alert, this);
        alertView.setCancelable(true);
        alertView.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog();
            return false;
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player!=null) {
            player.stop();
            player.release();
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onItemClick(Object o, int position,String data) {
        finish();
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            isvip = data.getData().getVip();
                          if (isvip.equals("1")){
                              time=30;
                          }

                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


}
