package com.aiwujie.shengmo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aiwujie.shengmo.R;

/**
 * Created by 290243232 on 2017/11/6.
 */

public class PlayAliveService extends Service {
    private final static String TAG = "PlayerAliveService";
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,TAG+"---->onCreate,启动服务");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silentmusic);
        mMediaPlayer.setLooping(true);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        return START_STICKY;
    }


    private void startPlayMusic(){
        if(mMediaPlayer != null){
            Log.d(TAG,"启动后台播放音乐");
            mMediaPlayer.start();
        }
    }


    private void stopPlayMusic(){
        if(mMediaPlayer != null){
            Log.d(TAG,"关闭后台播放音乐");
            mMediaPlayer.stop();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        Log.d(TAG,TAG+"---->onCreate,停止服务");
        // 重启
//        Intent intent = new Intent(getApplicationContext(), PlayAliveService.class);
//        startService(intent);
    }
}
