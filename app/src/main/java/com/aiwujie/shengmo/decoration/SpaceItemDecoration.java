package com.aiwujie.shengmo.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aiwujie.shengmo.utils.DensityUtil;


/**
 * 作者：Created by zq on 2021/4/6 14:09
 * 邮箱：80776234@qq.com
 * 间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int topAndBBottom;

    private Context context;
    private  boolean setTopAndBottom=false;
    public SpaceItemDecoration(Context context,int topAndBBottom,boolean setTopAndBottom) {
        this.topAndBBottom = topAndBBottom;
        this.context=context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int totalCount = parent.getAdapter().getItemCount();
        if (position /2== 0) {//第一个
            outRect.top = 0;
            if (setTopAndBottom) {
                outRect.top = DensityUtil.dip2px(context,topAndBBottom)/2;
            }
            outRect.bottom = DensityUtil.dip2px(context,topAndBBottom)/2;
        } else if (position/2 == totalCount /2) {//最后一个
            outRect.top =  DensityUtil.dip2px(context,topAndBBottom)/2;
            outRect.bottom = 0;
            if (setTopAndBottom) {
                outRect.bottom =  DensityUtil.dip2px(context,topAndBBottom)/2;
            }
        } else {//中间其它的
            outRect.top =  DensityUtil.dip2px(context,topAndBBottom)/2;
            outRect.bottom =  DensityUtil.dip2px(context,topAndBBottom)/2 ;
        }


    }
}
