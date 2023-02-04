package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.aiwujie.shengmo.eventbus.MediaCompleteEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by 290243232 on 2017/6/16.
 */

public class MediaPlayerManager implements MediaPlayer.OnCompletionListener {
    private Context mContext;
    private MediaPlayer player;
    private static MediaPlayerManager mInstance;

    private MediaPlayerManager(Context context){
        this.mContext = context;
        //初始化数据
        //初始化MediaPlayer代码
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
    }

    private MediaPlayerManager(Context context,int resource){
        this.mContext = context;
        //初始化数据
        //初始化MediaPlayer代码
        player = MediaPlayer.create(context, resource);
    }

    //播放网络音频
    public static MediaPlayerManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new MediaPlayerManager(context);
        }
        return mInstance;
    }

    //播放本地音频
    public static MediaPlayerManager getInstance(Context context,int resource){
        if(mInstance == null){
            mInstance = new MediaPlayerManager(context,resource);
        }
        return mInstance;
    }

    public int start(String mediaUrl){
        int duration=0;
        try {
            player.reset();
            player.setDataSource(mediaUrl);
            player.prepare();
            player.start();
//            Log.i("mediaplayermanager", "start: "+duration);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        return duration;
    }

    public void start(){
        try {
            player.start();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        player.stop();
        player.release();
        mInstance=null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Log.i("MediaPlayer", "onCompletion: "+"播放结束");
        EventBus.getDefault().post(new MediaCompleteEvent());
    }
}
