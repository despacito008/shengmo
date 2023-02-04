package com.aiwujie.shengmo.utils;

import android.view.MotionEvent;
import android.view.View;

import com.aiwujie.shengmo.eventbus.TopicViewIsVisibleEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 290243232 on 2017/6/17.
 */

public class ListviewOntouchListener implements View.OnTouchListener {
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private int type;

    /**
     *
     * @param type 0为动态的监听 1为话题的监听
     */
    public ListviewOntouchListener(int type) {
        this.type = type;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (y1 - y2 > 2) {
//                Log.i("listviewontouchlis", "onTouch: "+type);
                if(type==0) {
                    EventBus.getDefault().post(new ViewIsVisibleEvent(0));
                }else{
                    EventBus.getDefault().post(new TopicViewIsVisibleEvent(0));
                }
            } else if (y2 - y1 > 2) {
//                Log.i("listviewontouchlis", "onTouch: "+type);
                if(type==0) {
                    EventBus.getDefault().post(new ViewIsVisibleEvent(1));
                }else{
                    EventBus.getDefault().post(new TopicViewIsVisibleEvent(1));
                }
            }
//            else if(x1 - x2 > 50) {
//                Toast.makeText(this, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(this, "向右滑", Toast.LENGTH_SHORT).show();
//            }
        }
        return false;
    }
}
