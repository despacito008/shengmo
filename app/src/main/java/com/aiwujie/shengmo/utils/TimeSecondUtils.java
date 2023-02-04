package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by 290243232 on 2017/4/12.
 */

public class TimeSecondUtils {
    private PullToRefreshGridView pullToRefreshGridView;
    private PullToRefreshListView pullToRefreshListView;
    private Context context;
    private Runnable runnable;
    private int seconds = 0;
    private boolean isrun;

    public TimeSecondUtils(Context context,PullToRefreshGridView pullToRefreshGridView) {
        this.pullToRefreshGridView = pullToRefreshGridView;
        this.context=context;
        this.runnable = new MyThread();
        new Thread(runnable).start();
    }

    public TimeSecondUtils(Context context,PullToRefreshListView pullToRefreshListView) {
        this.pullToRefreshListView = pullToRefreshListView;
        this.context=context;
        this.runnable = new MyThread();
        new Thread(runnable).start();

    }

    Handler handler = new Handler() {          // handle
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    seconds++;
                    if (seconds == 8) {
                        if(pullToRefreshListView != null){
                            pullToRefreshListView.onRefreshComplete();
                        }else if(pullToRefreshGridView != null){
                            pullToRefreshGridView.onRefreshComplete();
                        }
                        if (context != null) {
                            ToastUtil.show(context.getApplicationContext(), "网络异常,请刷新重试...");
                        }
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public class MyThread implements Runnable {      // thread
        @Override
        public void run() {
            isrun=true;
            while (isrun) {
                try {
                    Thread.sleep(1000);     // sleep 1000ms
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void cancel(){
        isrun=false;
    }
}