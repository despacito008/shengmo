package com.aiwujie.shengmo.utils;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.aiwujie.shengmo.eventbus.NearTopEvent;
import com.aiwujie.shengmo.eventbus.RedWomenApplyEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 290243232 on 2017/3/20.
 */

public class AutoLoadListener implements AbsListView.OnScrollListener {
    private int page=0;
    private int mLastFirstPostion = 0;
    private int mLastFirstTop = 0;
    public interface AutoLoadCallBack {
        void execute(int page);
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private int getLastVisiblePosition = 0,lastVisiblePositionY=0;
    private AutoLoadCallBack  mCallback;
    public AutoLoadListener(AutoLoadCallBack callback)
    {
        this.mCallback = callback;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v=(View) view.getChildAt(view.getChildCount()-1);
                int[] location = new  int[2] ;
                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                int y=location [1];

                Log.e("x"+location[0],"y"+location[1]);
                if (view.getLastVisiblePosition()!=getLastVisiblePosition
                        && lastVisiblePositionY!=y)//第一次拖至底部
                {
//                    Toast.makeText(view.getContext(), "再次拖至底部，即可翻页",Toast.LENGTH_SHORT).show();
                    getLastVisiblePosition=view.getLastVisiblePosition();
                    lastVisiblePositionY=y;
                    page=page+1;
                    mCallback.execute(page);
                }
//                else if (view.getLastVisiblePosition()==getLastVisiblePosition
//                        && lastVisiblePositionY==y)//第二次拖至底部
//                {
//                    mCallback.execute(page+1);
//                }
            }
            //未滚动到底部，第二次拖至底部都初始化
            getLastVisiblePosition=0;
            lastVisiblePositionY=0;
        }
    }

    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int currentTop;
        View firstChildView = absListView.getChildAt(0);
        if (firstChildView != null) {
            currentTop = absListView.getChildAt(0).getTop();
        } else {
            //ListView初始化的时候会回调onScroll方法，此时getChildAt(0)仍是为空的
            return;
        }
        //判断上次可见的第一个位置和这次可见的第一个位置
        if (firstVisibleItem != mLastFirstPostion) {
            //不是同一个位置
            if (firstVisibleItem > mLastFirstPostion) {
//                Log.i("gridviewdicrection", "onScroll: "+"下");
                EventBus.getDefault().post(new RedWomenApplyEvent(0));
            } else {
                // 上
                EventBus.getDefault().post(new RedWomenApplyEvent(1));
//                Log.i("gridviewdicrection", "onScroll: "+"上");
            }
            mLastFirstTop = currentTop;
        }
        mLastFirstPostion = firstVisibleItem;

        if(firstVisibleItem==View.VISIBLE){
            EventBus.getDefault().post(new NearTopEvent(0));
        }else{
            EventBus.getDefault().post(new NearTopEvent(1));
        }
    }
}
